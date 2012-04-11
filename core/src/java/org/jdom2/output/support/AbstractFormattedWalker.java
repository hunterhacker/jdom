/*-- 

 Copyright (C) 2012 Jason Hunter & Brett McLaughlin.
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:

 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions, and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions, and the disclaimer that follows 
    these conditions in the documentation and/or other materials 
    provided with the distribution.

 3. The name "JDOM" must not be used to endorse or promote products
    derived from this software without prior written permission.  For
    written permission, please contact <request_AT_jdom_DOT_org>.

 4. Products derived from this software may not be called "JDOM", nor
    may "JDOM" appear in their name, without prior written permission
    from the JDOM Project Management <request_AT_jdom_DOT_org>.

 In addition, we request (but do not require) that you include in the 
 end-user documentation provided with the redistribution and/or in the 
 software itself an acknowledgement equivalent to the following:
     "This product includes software developed by the
      JDOM Project (http://www.jdom.org/)."
 Alternatively, the acknowledgment may be graphical using the logos 
 available at http://www.jdom.org/images/logos.

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED.  IN NO EVENT SHALL THE JDOM AUTHORS OR THE PROJECT
 CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.

 This software consists of voluntary contributions made by many 
 individuals on behalf of the JDOM Project and was originally 
 created by Jason Hunter <jhunter_AT_jdom_DOT_org> and
 Brett McLaughlin <brett_AT_jdom_DOT_org>.  For more information
 on the JDOM Project, please see <http://www.jdom.org/>.

 */

package org.jdom2.output.support;

import java.util.List;
import java.util.NoSuchElementException;

import org.jdom2.CDATA;
import org.jdom2.Content;
import org.jdom2.Content.CType;
import org.jdom2.internal.ArrayCopy;
import org.jdom2.output.EscapeStrategy;
import org.jdom2.output.Format;

/**
 * This Walker implementation walks a list of Content in a Formatted form of
 * some sort.
 * <p>
 * The JDOM content can be loosely categorised in to 'Text-like' content
 * (consisting of Text, CDATA, and EntityRef), and everything else. This
 * distinction is significant for for this class and it's sub-classes.
 * <p>
 * There will be text manipulation, and some (but not necessarily 
 * all) Text-like content will be returned as text() instead of next().
 * <p>
 * The trick in this class is that it deals with the regular content, and
 * delegates the Text-like content to the sub-classes.
 * <p>
 * Subclasses are tasked with analysing chunks of Text-like content in the
 * {@link #analyzeMultiText(MultiText, int, int)}  method. The subclasses are
 * responsible for adding the relevant text content to the suppliedMultiText
 * instance in such a way as to result in the correct format.
 * <p>
 * The Subclass needs to concern itself with only the text portion because this
 * abstract class will ensure the Text-like content is appropriately indented.
 * 
 * @author Rolf Lear
 */
public abstract class AbstractFormattedWalker implements Walker {
	
	/*
	 * We use Text instances to return formatted text to the caller.
	 * We do not need to validate the Text content... it is 'safe' to
	 * not use the default Text class.
	 */
	private static final CDATA CDATATOKEN = new CDATA("");
	
	/**
	 * Indicate how text content should be added
	 * @author Rolf Lear
	 *
	 */
	protected enum Trim {
		/** Left Trim */
		LEFT,
		/** Right Trim */
		RIGHT,
		/** Both Trim */
		BOTH,
		/** Trim Both and replace all internal whitespace with a single space*/
		COMPACT,
		/** No Trimming at all */
		NONE
	}
	
	/**
	 * Collect together the items that constitute formatted Text-like content.
	 * 
	 * @author Rolf Lear
	 *
	 */
	protected final class MultiText {
		
		// what the walker's cursor will be when this text is complete
		private final int nextcursor;
		// if there should be indenting after this text.
		private final boolean postpad;
		// indicate whether there is something actually added.
		private boolean gottext = false;
		// the number of mixed content values.
		private int msize = 0;
		// the location of the processed content.
		private Content[] data;
		// whether the mixed content should be returned as raw JDOM objects
		private String[] ctext;
		// the current cursor in the mixed content.
		private int mpos = -1;
		// we cheat here by using Boolean as a three-state option...
		// we expect it to be null often.
		private final Boolean wasescape;
		
		/**
		 * This is private so only this abstract class can create instances.
		 * @param nextcursor The cursor of the next non-text-like content
		 * @param prepad True if this text sequence should be prepadded
		 * @param postpad True if this text sequence should be postpadded
		 * @param guess The approximate number of resulting text-like items
		 */
		private MultiText(final int nextcursor, final boolean prepad,
				final boolean postpad, final int guess, final Boolean wasescape) {
			this.nextcursor = nextcursor;
			this.postpad = postpad;
			this.wasescape = wasescape;
			buffer.setLength(0);
			if (prepad && newlineindent != null) {
				buffer.append(newlineindent);
			}
			data = new Content[guess];
			ctext = new String[guess];
		}
		
		/**
		 * Ensure we have space for at least one more text-like item.
		 */
		private void ensurespace() {
			if (msize >= data.length) {
				data = ArrayCopy.copyOf(data, msize + 4);
				ctext = ArrayCopy.copyOf(ctext, data.length);
			}
		}
		
		/**
		 * Handle the case where we have been accumulating true text content,
		 * and the next item is not more text.
		 * @param postspace true if the last char in the text should be a space
		 */
		private void closeText() {
			if (buffer.length() == 0) {
				// empty text does not need adding at all.
				return;
			}
			ensurespace();
			ctext[msize++] = buffer.toString();
			buffer.setLength(0);
		}
		
		/**
		 * Append some text to the text-like sequence that will be treated as
		 * plain XML text (PCDATA). If the last content added to this text-like
		 * sequence then this new text will be appended directly to the previous
		 * text.
		 * 
		 * @param trim How to prepare the Text content
		 * @param text The actual Text content.
		 */
		public void appendText(final Trim trim, final String text) {
			final int tlen = text.length();
			if (tlen == 0) {
				return;
			}
			String toadd = null;
			switch (trim) {
				case NONE:
					toadd = text;
					break;
				case BOTH:
					toadd = Format.trimBoth(text);
					break;
				case LEFT:
					toadd = Format.trimLeft(text);
					break;
				case RIGHT:
					toadd = Format.trimRight(text);
					break;
				case COMPACT:
					toadd = Format.compact(text);
					break;
			}
			if (toadd != null) {
				toadd = escapeText(toadd);
				buffer.append(toadd);
				gottext = true;
			}
		}
		
		private String escapeText(final String text) {
			if (escape == null || !fstack.getEscapeOutput()) {
				return text;
			}
			return Format.escapeText(escape, endofline, text);
		}

		private String escapeCDATA(final String text) {
			if (escape == null) {
				return text;
			}
			return text;
		}
		/**
		 * Append some text to the text-like sequence that will be treated as
		 * CDATA.
		 * @param trim How to prepare the CDATA content
		 * @param text The actual CDATA content.
		 */
		public void appendCDATA(final Trim trim, final String text) {
			// this resets the buffer too.
			closeText();
			String toadd = null;
			switch (trim) {
				case NONE:
					toadd = text;
					break;
				case BOTH:
					toadd = Format.trimBoth(text);
					break;
				case LEFT:
					toadd = Format.trimLeft(text);
					break;
				case RIGHT:
					toadd = Format.trimRight(text);
					break;
				case COMPACT:
					toadd = Format.compact(text);
					break;
			}
			
			toadd = escapeCDATA(toadd);
			ensurespace();
			// mark this as being CDATA text
			data[msize] = CDATATOKEN;
			ctext[msize++] = toadd;

			gottext = true;

		}
		
		/**
		 * Simple method that ensures the text is processed, regardless of
		 * content, and is never escaped.
		 * @param text
		 */
		private void forceAppend(final String text) {
			gottext = true;
			buffer.append(text);
		}
		
		/**
		 * Add some JDOM Content (typically an EntityRef) that will be treated
		 * as part of the Text-like sequence.
		 * @param c the content to add.
		 */
		public void appendRaw(final Content c) {
			closeText();
			ensurespace();
			data[msize++] = c;
			buffer.setLength(0);

		}
		
		/**
		 * Indicate that there is no further content to be added to the
		 * text-like sequence.
		 */
		public void done() {
			if (postpad && newlineindent != null) {
				// this will be ignored if there was not some content.
				buffer.append(newlineindent);
			}
			if (gottext) {
				closeText();
			}
			buffer.setLength(0);
		}
		
	}
	
	
	
	
	private final int size;
	private final List<? extends Content> content;
	private final boolean alltext;
	private final boolean allwhite;
	private final String newlineindent;
	private final String endofline;
	private final StringBuilder buffer = new StringBuilder();
	private final EscapeStrategy escape;
	private final FormatStack fstack;
	private boolean hasnext = true;

	private int cursor = 0;
	private MultiText multitext = null;
	private MultiText pendingmt = null;
	
	/**
	 * Create a Walker that preserves all content in its raw state.
	 * @param content the content to walk.
	 * @param fstack the current FormatStack
	 * @param doescape Whether Text values should be escaped.
	 */
	public AbstractFormattedWalker(final List<? extends Content> content,
			final FormatStack fstack, final boolean doescape) {
		super();
		this.fstack = fstack;
		this.content = content;
		this.escape = doescape ? fstack.getEscapeStrategy() : null;
		size = content.size();
		newlineindent = fstack.getPadBetween();
		endofline = fstack.getLevelEOL();
		if (size == 0) {
			alltext = true;
			allwhite = true;
		} else {
			boolean atext = false;
			boolean awhite = false;
			if (isTextLike(0)) {
				// the first item in the list is Text-like, and we pre-check
				// to see whether all content is text.... and whether it amounts
				// to something.
				pendingmt = buildMultiText();
				if (pendingmt.nextcursor >= size) {
					atext = true;
					awhite = pendingmt.msize == 0;
				}
				if (pendingmt.msize == 0) {
					// first content in list is ignorable.
					cursor = pendingmt.nextcursor;
					pendingmt = null;
				}
			}
			alltext = atext;
			allwhite = awhite;
		}
		hasnext = cursor < size;
	}

	@Override
	public final Content next() {
		
		if (!hasnext) {
			throw new NoSuchElementException("Cannot walk off end of Content");
		}
		
		if (multitext != null && multitext.mpos + 1 >= multitext.msize) {
			// finished this multitext. need to move on.
			cursor = multitext.nextcursor;
			multitext = null;
		}

		if (pendingmt != null) {
			// we have a multi-text pending from the last block
			// this will only be the case when the previous value was non-text.
			if (pendingmt.wasescape != null && 
					fstack.getEscapeOutput() != pendingmt.wasescape.booleanValue()) {
				// we calculated pending with one escape strategy, but it changed...
				// we need to recalculate it....
				pendingmt = buildMultiText();
				//analyzeMultiText(mtext, offset, len)
			}
			multitext = pendingmt;
			pendingmt = null;
		}
		
		if (multitext != null) {
			
			// OK, we have text-like content to push back.
			// and it still has values in it.
			// advance the cursor
			multitext.mpos++;
			
			final Content ret = multitext.ctext[multitext.mpos] == null
					? multitext.data[multitext.mpos] : null;

			
			// we can calculate the hasnext
			hasnext = multitext.mpos + 1 < multitext.msize || 
					multitext.nextcursor < size;
			
			// return null to indicate text content.
			return ret;
		}
		
		// non-text, increment and return content.
		final Content ret = content.get(cursor++);
		
		// OK, we are returning some content.
		// we need to determine the state of the next loop.
		// cursor at this point has been advanced!
		if (cursor >= size) {
			hasnext = false;
		} else {
			// there is some more content.
			// we need to inspect it to determine whether it is good
			if (isTextLike(cursor)) {
				// calculate what this next text-like content looks like.
				pendingmt = buildMultiText();
				if (pendingmt.msize > 0) {
					hasnext = true;
				} else {
					// all white text... perhaps we need indenting anyway.
					if (pendingmt.nextcursor < size && 
							newlineindent != null) {
						// yes, we need indenting.
						final int nc = pendingmt.nextcursor;
						// redefine the pending.
						pendingmt = new MultiText(nc, false, false, 1, null);
						pendingmt.forceAppend(newlineindent);
						pendingmt.done();
						hasnext = true;
					} else {
						cursor = pendingmt.nextcursor;
						pendingmt = null;
						hasnext = cursor < size;
					}
				}
			} else {
				// it is non-text content... we have more content.
				// but, we just returned non-text content. We may need to indent
				if (newlineindent != null) {
					pendingmt = new MultiText(cursor, false, false, 1, null);
					pendingmt.forceAppend(newlineindent);
					pendingmt.done();
				}
				hasnext = true;
			}
		}
		return ret;
	}

	/**
	 * Add the content at the specified indices to the provided MultiText.
	 * @param mtext the MultiText to append to.
	 * @param offset The first Text-like content to add to the MultiText
	 * @param len The number of Text-like content items to add. 
	 */
	protected abstract void analyzeMultiText(MultiText mtext, int offset, int len);

	/**
	 * Get the content at a position in the input content. Useful for subclasses
	 * in their {@link #analyzeMultiText(MultiText, int, int)} calls.
	 * @param index the index to get the content at.
	 * @return the content at the index.
	 */
	protected final Content get(final int index) {
		return content.get(index);
	}
	
	@Override
	public final boolean isAllText() {
		return alltext;
	}
	
	@Override
	public final boolean hasNext() {
		return hasnext;
	}

	private final MultiText buildMultiText() {
		// set up a sequence where the next bunch of stuff is text.
		int i = cursor - 1;
		wloop: while (++i < size) {
			final CType ctype = content.get(i).getCType();
			switch (ctype) {
				case Text :
				case CDATA :
				case EntityRef:
					break; //switch
				default :
					break wloop; //while loop
			}
		}
		final MultiText mt = new MultiText(i, cursor > 0, i < size, 
				(i - cursor) * 2 + 1, fstack.getEscapeOutput());
		analyzeMultiText(mt, cursor, i - cursor);
		mt.done();
		return mt;
	}

	@Override
	public final String text() {
		if (multitext == null || multitext.mpos >= multitext.msize) {
			return null;
		}
		return multitext.ctext[multitext.mpos];
	}

	@Override
	public final boolean isCDATA() {
		if (multitext == null || multitext.mpos >= multitext.msize) {
			return false;
		}
		if (multitext.ctext[multitext.mpos] == null) {
			return false;
		}
		
		return multitext.data[multitext.mpos] == CDATATOKEN;
	}

	@Override
	public final boolean isAllWhitespace() {
		return allwhite;
	}

	private final boolean isTextLike(final int pos) {
		switch (get(pos).getCType()) {
			case Text:
			case CDATA:
			case EntityRef:
				return true;
			default:
				// nothing.
		}
		return false;
	}

	
}
