/*-- 

 Copyright (C) 2000-2007 Jason Hunter & Brett McLaughlin.
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

package org.jdom2.output;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.xml.transform.Result;

import org.jdom2.Attribute;
import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.Content;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.EntityRef;
import org.jdom2.IllegalDataException;
import org.jdom2.Namespace;
import org.jdom2.NamespaceStack;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;
import org.jdom2.Verifier;
import org.jdom2.output.Format.TextMode;

/**
 * This class provides a concrete implementation of {@link XMLOutputProcessor}
 * for supporting the {@link XMLOutputter}.
 * <p>
 * <h2>Overview</h2>
 * <p>
 * This class is marked abstract even though all methods are fully implemented.
 * The <code>process*(...)</code> methods are public because they match the
 * XMLOutputProcessor interface but the remaining methods are all protected.
 * <p>
 * People who want to create a custom XMLOutputProcessor for XMLOutputter are
 * able to extend this class and modify any functionality they want. Before
 * sub-classing this you should first check to see if the {@link Format} class
 * can get you the results you want.
 * <p>
 * <b><i>Subclasses of this should have reentrant methods.</i></b> This is
 * easiest to accomplish simply by not allowing any instance fields. If your
 * sub-class has an instance field/variable, then it's probably broken.
 * <p>
 * <h2>The Stacks</h2>
 * <p>
 * One significant feature of this implementation is that it creates and
 * maintains both a {@link NamespaceStack} and {@link FormatStack} that are
 * managed in the
 * {@link #printElement(Writer, FormatStack, NamespaceStack, Element)} method.
 * The stacks are pushed and popped in that method only. They significantly
 * improve the performance and readability of the code.
 * <p>
 * The NamespaceStack is only sent through to the
 * {@link #printElement(Writer, FormatStack, NamespaceStack, Element)} and
 * {@link #printContent(Writer, FormatStack, NamespaceStack, List)} methods, but
 * the FormatStack is pushed through to all print* Methods.
 * <p>
 * <h2>Text Processing</h2>
 * <p>
 * In XML the concept of 'Text' can be loosely defined as anything that can be
 * found between an Element's start and end tags, excluding Comments and
 * Processing Instructions. When considered from a JDOM perspective, this means
 * {@link Text}, {@link CDATA} and {@link EntityRef} content. This will be
 * referred to as 'text-type content'
 * <p>
 * In the context of XMLOutputter, {@link EntityRef} content is considered to be
 * text-type content. This happens because when XML is parsed the embedded
 * entity reference tokens are (normally) expanded to their replacement text. If
 * an EntityRef content has survived to this output stage it means that it
 * cannot (or should not) be expanded. As a result, it should simply be output
 * as it's escaped name in the form <code>&amp;refname;</code>.
 * <p>
 * Consecutive text-type JDOM content is an especially hard problem because the
 * complete set of consecutive values should be considered together. For
 * example, there is a significant difference between the 'trimmed' result of
 * two individual Text contents and the trimmed result of the same Text contents
 * which are combined before trimming: consider two consecutive Text contents
 * "&nbsp;X&nbsp;" and "&nbsp;Y&nbsp;" which (trimmed) should be output as:
 * "X&nbsp;&nbsp;Y" not "XY".
 * <p>
 * As a result, this class differentiates between stand-alone and consecutive
 * text-type JDOM Content. If the content is determined to be stand-alone, it is
 * fed to one of the {@link #printEntityRef(Writer, FormatStack, EntityRef)},
 * {@link #printCDATA(Writer, FormatStack, CDATA)}, or
 * {@link #printText(Writer, FormatStack, Text)} methods. If there is
 * consecutive text-type content it is all fed together to the
 * {@link #printTextConsecutive(Writer, FormatStack, List, int, int)} method.
 * <p>
 * The above 4 methods for printing text-type content will all use the various
 * <code>text*(...)</code> methods to produce output to the Writer.
 * <p>
 * <b>Note:</b> If the Format's TextMode is PRESERVE then all text-type content
 * is considered to be stand-alone. In other words, printTextConsecutive() will
 * never be called when the TextMode is PRESERVE.
 * <p>
 * <h2>Non-Text Content</h2>
 * <p>
 * Non-text content is processed via the respective print* methods. The usage
 * should be logical based on the method name.
 * <p>
 * The general observations are:
 * <ul>
 * <li>printElement - maintains the Stacks, prints the element open tags, with
 * attributes and namespaces. It checks to see whether the Element is text-only,
 * or has non-text content. If it is text-only there is no indent/newline
 * handling and it delegates to the correct text-type print method, otherwise it
 * delegates to printContent.
 * <li>printContent is called if there is a 'mixed' content list to output. It
 * will always wrap each respective content call (one of a text-type print, a
 * printTextConsecutive, or a non-text type print) with an indent and a newline.
 * </ul>
 * <p>
 * <h2>Final Notes</h2> No methods actually write to the destination Writer
 * except the <code>write(...)</code> methods. Thus, all other methods do their
 * respective processing and delegate the actual destination output to the
 * {@link #write(Writer, char)} or {@link #write(Writer, String)} methods.
 * <p>
 * Only the text-type print methods (printCDATA, printText, printEntityRef, and
 * printTextConsecutive) will call the text* methods.
 * <p>
 * There are two 'helper' methods which are simply there to reduce some code
 * redundancy in the class. They do nothing more than that. Have a look at the
 * source-code to see what they do. If you need them they may prove useful.
 * 
 * @see XMLOutputter
 * @see XMLOutputProcessor
 * @since JDOM2
 * @author Rolf Lear
 */
public abstract class AbstractXMLOutputProcessor extends AbstractOutputProcessor
	implements XMLOutputProcessor {

	/** Simple constant for an open-CDATA */
	protected static final String CDATAPRE = "<![CDATA[";
	/** Simple constant for a close-CDATA */
	protected static final String CDATAPOST = "]]>";

	

	/* *******************************************
	 * XMLOutputProcessor implementation.
	 * *******************************************
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.XMLOutputProcessor#process(java.io.Writer,
	 * org.jdom2.Document, org.jdom2.output.Format)
	 */
	@Override
	public void process(final Writer out, final Format format,
			final Document doc) throws IOException {
		printDocument(out, new FormatStack(format), new NamespaceStack(), doc);
		out.flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.XMLOutputProcessor#process(java.io.Writer,
	 * org.jdom2.DocType, org.jdom2.output.Format)
	 */
	@Override
	public void process(final Writer out, final Format format,
			final DocType doctype) throws IOException {
		printDocType(out, new FormatStack(format), doctype);
		out.flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.XMLOutputProcessor#process(java.io.Writer,
	 * org.jdom2.Element, org.jdom2.output.Format)
	 */
	@Override
	public void process(final Writer out, final Format format,
			final Element element) throws IOException {
		// If this is the root element we could pre-initialize the
		// namespace stack with the namespaces
		printElement(out, new FormatStack(format), new NamespaceStack(),
				element);
		out.flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.XMLOutputProcessor#process(java.io.Writer,
	 * java.util.List, org.jdom2.output.Format)
	 */
	@Override
	public void process(final Writer out, final Format format,
			final List<? extends Content> list)
			throws IOException {
		printContent(out, new FormatStack(format), new NamespaceStack(), list);
		out.flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.XMLOutputProcessor#process(java.io.Writer,
	 * org.jdom2.CDATA, org.jdom2.output.Format)
	 */
	@Override
	public void process(final Writer out, final Format format,
			final CDATA cdata) throws IOException {
		printCDATA(out, new FormatStack(format), cdata);
		out.flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.XMLOutputProcessor#process(java.io.Writer,
	 * org.jdom2.Text, org.jdom2.output.Format)
	 */
	@Override
	public void process(final Writer out, final Format format,
			final Text text) throws IOException {
		printText(out, new FormatStack(format), text);
		out.flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.XMLOutputProcessor#process(java.io.Writer,
	 * org.jdom2.Comment, org.jdom2.output.Format)
	 */
	@Override
	public void process(final Writer out, final Format format,
			final Comment comment) throws IOException {
		printComment(out, new FormatStack(format), comment);
		out.flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.XMLOutputProcessor#process(java.io.Writer,
	 * org.jdom2.ProcessingInstruction, org.jdom2.output.Format)
	 */
	@Override
	public void process(final Writer out, final Format format,
			final ProcessingInstruction pi) throws IOException {
		FormatStack fstack = new FormatStack(format);
		// Output PI verbatim, disregarding TrAX escaping PIs.
		fstack.setIgnoreTrAXEscapingPIs(true);
		printProcessingInstruction(out, fstack, pi);
		out.flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.XMLOutputProcessor#process(java.io.Writer,
	 * org.jdom2.EntityRef, org.jdom2.output.Format)
	 */
	@Override
	public void process(final Writer out, final Format format,
			final EntityRef entity) throws IOException {
		printEntityRef(out, new FormatStack(format), entity);
		out.flush();
	}

	/*
	 * ========================================================================
	 * Methods that actually write data to output. None of the other methods
	 * should directly write to the output unless they use these methods.
	 * ========================================================================
	 */

	/**
	 * Print some string value to the output. Null values are ignored. This
	 * ignore-null property is used for a few tricks.
	 * 
	 * @param out
	 *        The Writer to write to.
	 * @param str
	 *        The String to write (can be null).
	 * @throws IOException
	 *         if the out Writer fails.
	 */
	protected void write(final Writer out, final String str) throws IOException {
		if (str == null) {
			return;
		}
		out.write(str);
	}

	/**
	 * Write a single character to the output Writer.
	 * 
	 * @param out
	 *        The Writer to write to.
	 * @param c
	 *        The char to write.
	 * @throws IOException
	 *         if the Writer fails.
	 */
	protected void write(final Writer out, final char c) throws IOException {
		out.write(c);
	}

	/*
	 * ========================================================================
	 * Support methods for Text-content formatting. Should all be protected. The
	 * following are used when printing Text-based data. Because of complicated
	 * multi-sequential text sometimes the requirements are odd. All Text
	 * content will be output using these methods, which is why there is the Raw
	 * version.
	 * ========================================================================
	 */

	/**
	 * This will take the three pre-defined entities in XML 1.0 ('&lt;', '&gt;',
	 * and '&amp;' - used specifically in XML elements) and convert their
	 * character representation to the appropriate entity reference, suitable
	 * for XML element content. Further, some special characters (e.g.
	 * characters that are not valid in the current encoding) are converted to
	 * escaped representations.
	 * <p>
	 * <b>Note:</b> If {@link FormatStack#getEscapeOutput()} is false then no
	 * escaping will happen.
	 * 
	 * @param out
	 *        The destination Writer
	 * @param fstack
	 *        The {@link FormatStack}
	 * @param value
	 *        <code>String</code> Attribute value to escape.
	 * @throws IOException
	 *         if the destination Writer fails.
	 * @throws IllegalDataException
	 *         if an entity can not be escaped
	 */
	protected void attributeEscapedEntitiesFilter(final Writer out,
			final FormatStack fstack, final String value) throws IOException {
		
		if (!fstack.getEscapeOutput()) {
			// no escaping...
			write(out, value);
			return;
		}
		final EscapeStrategy strategy = fstack.getEscapeStrategy();

		char highsurrogate = 0;
		for (char ch : value.toCharArray()) {
			if (highsurrogate > 0) {
				if (!Verifier.isLowSurrogate(ch)) {
					throw new IllegalDataException(
							"Could not decode surrogate pair 0x" +
									Integer.toHexString(highsurrogate) + " / 0x"
									+ Integer.toHexString(ch));
				}
				int chp = Verifier.decodeSurrogatePair(highsurrogate, ch);
				write(out, "&#x");
				write(out, Integer.toHexString(chp));
				write(out, ';');
				highsurrogate = 0;
				continue;
			}
			switch (ch) {
				case '<':
					write(out, "&lt;");
					break;
				case '>':
					write(out, "&gt;");
					break;
				case '&':
					write(out, "&amp;");
					break;
				case '\r':
					write(out, "&#xD;");
					break;
				case '\"':
					write(out, "&quot;");
					break;
				case '\t':
					write(out, "&#x9;");
					break;
				case '\n':
					write(out, "&#xA;");
					break;
				default:

					if (strategy.shouldEscape(ch)) {
						// make sure what we are escaping is not the
						// beginning of a multi-byte character.
						if (Verifier.isHighSurrogate(ch)) {
							// this is a the high of a surrogate pair
							highsurrogate = ch;
						} else {
							write(out, "&#x");
							write(out, Integer.toHexString(ch));
							write(out, ';');
						}
					} else {
						write(out, ch);
					}
					break;
			}
		}
		if (highsurrogate > 0) {
			throw new IllegalDataException("Surrogate pair 0x" +
					Integer.toHexString(highsurrogate) + "truncated");
		}

	}

	/**
	 * Convenience method that simply passes the input str to
	 * {@link #write(Writer, String)}. This could be useful for subclasses to
	 * hook in to. All text-type output will come through this or the
	 * {@link #textRaw(Writer, char)} method.
	 * 
	 * @param out
	 *        the destination writer.
	 * @param str
	 *        the String to write.
	 * @throws IOException
	 *         if the Writer fails.
	 */
	protected void textRaw(final Writer out, final String str) throws IOException {
		write(out, str);
	}

	/**
	 * Convenience method that simply passes the input char to
	 * {@link #write(Writer, char)}. This could be useful for subclasses to hook
	 * in to. All text-type output will come through this or the
	 * {@link #textRaw(Writer, String)} method.
	 * 
	 * @param out
	 *        the destination Writer.
	 * @param ch
	 *        the char to write.
	 * @throws IOException
	 *         if the Writer fails.
	 */
	protected void textRaw(final Writer out, final char ch) throws IOException {
		write(out, ch);
	}

	/**
	 * Write an {@link EntityRef} to the destination.
	 * 
	 * @param out
	 *        the destination Writer.
	 * @param name
	 *        the EntityRef's name.
	 * @throws IOException
	 *         if the Writer fails.
	 */
	protected void textEntityRef(final Writer out, final String name) throws IOException {
		textRaw(out, '&');
		textRaw(out, name);
		textRaw(out, ';');
	}

	/**
	 * Write a CDATA's value to the destination. The value will not be formatted
	 * or modified in any way.
	 * <p>
	 * The input <code>str</code> will be wrapped in the
	 * <code>&lt;![CDATA[</code> and <code>]]&gt;</code> delimiters.
	 * 
	 * @param out
	 *        the destination Writer.
	 * @param str
	 *        the CDATA's value.
	 * @throws IOException
	 *         if the Writer fails.
	 */
	protected void textCDATARaw(final Writer out, final String str) throws IOException {
		textRaw(out, CDATAPRE);
		textRaw(out, str);
		textRaw(out, CDATAPOST);
	}

	/**
	 * Write a CDATA's value to the destination. The value will first be
	 * compacted (all leading and trailing whitespace will be removed and all
	 * internal whitespace will be be represented with a single ' ' character).
	 * If the CDATA contains only whitespace then nothing is output.
	 * <p>
	 * The results (if any) will be wrapped in the <code>&lt;![CDATA[</code> and
	 * <code>]]&gt;</code> delimiters.
	 * 
	 * @param out
	 *        the destination Writer.
	 * @param str
	 *        the CDATA's value.
	 * @throws IOException
	 *         if the Writer fails.
	 */
	protected void textCDATACompact(final Writer out, final String str) throws IOException {
		final char[] chars = str.toCharArray();
		int right = chars.length - 1;
		while (right >= 0 && Verifier.isXMLWhitespace(chars[right])) {
			right--;
		}
		if (right < 0) {
			return;
		}

		int left = 0;
		while (Verifier.isXMLWhitespace(chars[left])) {
			// we do not need to check left < right because
			// we know we will find some non-white char first.
			left++;
		}

		textRaw(out, CDATAPRE);

		boolean wspace = false;
		while (left <= right) {
			if (Verifier.isXMLWhitespace(chars[left])) {
				if (!wspace) {
					write(out, ' ');
				}
				wspace = true;
			} else {
				write(out, chars[left]);
				wspace = false;
			}
			left++;
		}

		textRaw(out, CDATAPOST);
	}

	/**
	 * Write a CDATA's value to the destination. The value will first be
	 * inspected. If the CDATA contains only whitespace then nothing is output,
	 * otherwise the entire input <code>str</code> will be wrapped in the
	 * <code>&lt;![CDATA[</code> and <code>]]&gt;</code> delimiters.
	 * 
	 * @param out
	 *        the destination Writer.
	 * @param str
	 *        the CDATA's value.
	 * @throws IOException
	 *         if the Writer fails.
	 */
	protected void textCDATATrimFullWhite(final Writer out, final String str) throws IOException {
		final char[] chars = str.toCharArray();
		int right = chars.length - 1;
		while (right >= 0 && Verifier.isXMLWhitespace(chars[right])) {
			right--;
		}
		if (right < 0) {
			return;
		}

		textRaw(out, CDATAPRE);
		write(out, str);
		textRaw(out, CDATAPOST);
	}

	/**
	 * Write a CDATA's value to the destination. The value will first be trimmed
	 * (all leading and trailing whitespace will be removed). If the CDATA
	 * contains only whitespace then nothing is output. The results (if any)
	 * will be wrapped in the <code>&lt;![CDATA[</code> and <code>]]&gt;</code>
	 * delimiters.
	 * 
	 * @param out
	 *        the destination Writer.
	 * @param str
	 *        the CDATA's value.
	 * @throws IOException
	 *         if the Writer fails.
	 */
	protected void textCDATATrimBoth(final Writer out, final String str) throws IOException {
		final char[] chars = str.toCharArray();
		int right = chars.length - 1;
		while (right >= 0 && Verifier.isXMLWhitespace(chars[right])) {
			right--;
		}
		if (right < 0) {
			return;
		}

		textRaw(out, CDATAPRE);
		int left = 0;
		while (Verifier.isXMLWhitespace(chars[left])) {
			// we do not need to check left < right because
			// we know we will find some non-white char first.
			left++;
		}
		while (left <= right) {
			write(out, chars[left++]);
		}
		textRaw(out, CDATAPOST);
	}

	/**
	 * Write a CDATA's value to the destination. The value will first be
	 * right-trimmed (all trailing whitespace will be removed). If the CDATA
	 * contains only whitespace then nothing is output. The results (if any)
	 * will be wrapped in the <code>&lt;![CDATA[</code> and <code>]]&gt;</code>
	 * delimiters.
	 * 
	 * @param out
	 *        the destination Writer.
	 * @param str
	 *        the CDATA's value.
	 * @throws IOException
	 *         if the Writer fails.
	 */
	protected void textCDATATrimRight(final Writer out, final String str) throws IOException {
		final char[] chars = str.toCharArray();
		int right = chars.length - 1;
		while (right >= 0 && Verifier.isXMLWhitespace(chars[right])) {
			right--;
		}
		if (right < 0) {
			return;
		}

		textRaw(out, CDATAPRE);
		int left = 0;
		while (left <= right) {
			write(out, chars[left++]);
		}
		textRaw(out, CDATAPOST);
	}

	/**
	 * Write a CDATA's value to the destination. The value will first be
	 * left-trimmed (all leading whitespace will be removed). If the CDATA
	 * contains only whitespace then nothing is output. The results (if any)
	 * will be wrapped in the <code>&lt;![CDATA[</code> and <code>]]&gt;</code>
	 * delimiters.
	 * 
	 * @param out
	 *        the destination Writer.
	 * @param str
	 *        the CDATA's value.
	 * @throws IOException
	 *         if the Writer fails.
	 */
	protected void textCDATATrimLeft(final Writer out, final String str) throws IOException {
		final char[] chars = str.toCharArray();
		int right = chars.length - 1;
		int left = 0;
		while (left < right && Verifier.isXMLWhitespace(chars[left])) {
			left++;
		}
		if (left >= right) {
			// all whitespace
			return;
		}
		textRaw(out, CDATAPRE);
		while (left <= right) {
			write(out, chars[left++]);
		}
		textRaw(out, CDATAPOST);
	}

	/**
	 * This will take the three pre-defined entities in XML 1.0 ('&lt;', '&gt;',
	 * and '&amp;' - used specifically in XML elements) and convert their
	 * character representation to the appropriate entity reference, suitable
	 * for XML element content. Further, some special characters (e.g.
	 * characters that are not valid in the current encoding) are converted to
	 * escaped representations.
	 * <p>
	 * <b>Note:</b> If {@link FormatStack#getEscapeOutput()} is false then no
	 * escaping will happen.
	 * 
	 * @param out
	 *        The destination Writer
	 * @param fstack
	 *        The {@link FormatStack}
	 * @param str
	 *        <code>char[]</code> input to escape.
	 * @param left
	 *        where in the input array (inclusive) to start escaping and
	 *        writing.
	 * @param right
	 *        where in the input array (exclusive) to end escaping and writing.
	 * @throws IOException
	 *         if the destination Writer fails.
	 * @throws IllegalDataException
	 *         if an entity can not be escaped
	 */
	protected void textEscapedEntitiesFilter(final Writer out,
			final FormatStack fstack, final char[] str, final int left,
			final int right) throws IOException {
		if (!fstack.getEscapeOutput()) {
			int idx = left;
			while (idx < right) {
				textRaw(out, str[idx++]);
			}
			return;
		}
		final EscapeStrategy strategy = fstack.getEscapeStrategy();

		char highsurrogate = 0;
		int idx = left;
		while (idx < right) {
			char ch = str[idx++];
			if (highsurrogate > 0) {
				if (!Verifier.isLowSurrogate(ch)) {
					throw new IllegalDataException(
							"Could not decode surrogate pair 0x" +
									Integer.toHexString(highsurrogate) + " / 0x"
									+ Integer.toHexString(ch));
				}
				int chp = Verifier.decodeSurrogatePair(highsurrogate, ch);
				textRaw(out, "&#x" + Integer.toHexString(chp) + ";");
				highsurrogate = 0;
				continue;
			}
			switch (ch) {
				case '<':
					textRaw(out, "&lt;");
					break;
				case '>':
					textRaw(out, "&gt;");
					break;
				case '&':
					textRaw(out, "&amp;");
					break;
				case '\r':
					textRaw(out, "&#xD;");
					break;
				case '\n':
					textRaw(out, fstack.getLineSeparator());
					break;
				default:

					if (strategy.shouldEscape(ch)) {
						// make sure what we are escaping is not the
						// beginning of a multi-byte character.
						if (Verifier.isHighSurrogate(ch)) {
							// this is a the high of a surrogate pair
							highsurrogate = ch;
						} else {
							textRaw(out, "&#x" + Integer.toHexString(ch) + ";");
						}
					} else {
						textRaw(out, ch);
					}
					break;
			}
		}
		if (highsurrogate > 0) {
			throw new IllegalDataException("Surrogate pair 0x" +
					Integer.toHexString(highsurrogate) + "truncated");
		}

	}

	/**
	 * Write an {@link Element}'s value to the destination. The value will not
	 * be formatted or modified in any way (except it will probably be
	 * XML-escaped).
	 * 
	 * @param out
	 *        the destination Writer.
	 * @param fstack
	 *        the {@link FormatStack}
	 * @param str
	 *        the Element's value.
	 * @throws IOException
	 *         if the Writer fails.
	 */
	protected void textEscapeRaw(final Writer out, FormatStack fstack, final String str) throws IOException {
		final char[] data = str.toCharArray();
		textEscapedEntitiesFilter(out, fstack, data, 0, data.length);
	}

	/**
	 * Write an {@link Element}'s value to the destination. The value will first
	 * be compacted (leading and trailing whitespace is removed, and internal
	 * whitespace is replaced with a single ' ' character). The value will also
	 * probably be XML-escaped.
	 * 
	 * @param out
	 *        the destination Writer.
	 * @param fstack
	 *        the {@link FormatStack}
	 * @param str
	 *        the Element's value.
	 * @throws IOException
	 *         if the Writer fails.
	 */
	protected void textEscapeCompact(final Writer out, FormatStack fstack, final String str) throws IOException {
		final char[] chars = str.toCharArray();
		int right = chars.length - 1;
		while (right >= 0 && Verifier.isXMLWhitespace(chars[right])) {
			right--;
		}
		if (right < 0) {
			// completely empty.
			return;
		}

		int left = 0;
		while (left < right && Verifier.isXMLWhitespace(chars[left])) {
			left++;
		}
		boolean wspace = false;

		final boolean escape = fstack.getEscapeOutput();

		int idx = left - 1;
		int firstnws = -1;
		// set to true if we just processed a higg-surrogate pair.
		// if true it means the next char cannot be a white-space.
		boolean highsp = false;
		while (++idx <= right) {
			char ch = chars[idx];
			if (!highsp && Verifier.isXMLWhitespace(ch)) {
				if (!wspace) {
					if (escape && firstnws >= 0) {
						// first whitespace character
						textEscapedEntitiesFilter(out, fstack, chars, firstnws, idx);
					}
					firstnws = -1;
					textRaw(out, ' ');
				}
				wspace = true;
				continue;
			}
			if (!highsp && Verifier.isHighSurrogate(ch)) {
				highsp = true;
			} else {
				highsp = false;
			}
			wspace = false;
			if (escape) {
				if (firstnws < 0) {
					firstnws = idx;
				}
			} else {
				textRaw(out, ch);
			}
		}
		if (escape && firstnws >= 0) {
			// first non-whitespace character
			textEscapedEntitiesFilter(out, fstack, chars, firstnws, idx);
		}

	}

	/**
	 * Write an {@link Element}'s value to the destination. The value will first
	 * be trimmed (leading and trailing whitespace is removed). The value will
	 * also probably be XML-escaped.
	 * 
	 * @param out
	 *        the destination Writer.
	 * @param fstack
	 *        the {@link FormatStack}
	 * @param str
	 *        the Element's value.
	 * @throws IOException
	 *         if the Writer fails.
	 */
	protected void textEscapeTrimBoth(final Writer out, FormatStack fstack, final String str) throws IOException {
		final char[] chars = str.toCharArray();
		int right = chars.length - 1;
		while (right >= 0 && Verifier.isXMLWhitespace(chars[right])) {
			right--;
		}
		if (right < 0) {
			return;
		}

		int left = 0;
		while (left < right && Verifier.isXMLWhitespace(chars[left])) {
			left++;
		}
		textEscapedEntitiesFilter(out, fstack, chars, left, right + 1);
	}

	/**
	 * Write an {@link Element}'s value to the destination. If the value
	 * consists of only Whitespace then nothing will be output, otherwise the
	 * entire input <code>str</code> value will be sent to the destination. The
	 * value (if any) will also probably be XML-escaped.
	 * 
	 * @param out
	 *        the destination Writer.
	 * @param fstack
	 *        the {@link FormatStack}
	 * @param str
	 *        the Element's value.
	 * @throws IOException
	 *         if the Writer fails.
	 */
	protected void textEscapeTrimFullWhite(final Writer out, FormatStack fstack, final String str) throws IOException {
		final char[] chars = str.toCharArray();
		int right = chars.length - 1;
		while (right >= 0 && Verifier.isXMLWhitespace(chars[right])) {
			right--;
		}
		if (right < 0) {
			return;
		}

		textEscapedEntitiesFilter(out, fstack, chars, 0, chars.length);
	}

	/**
	 * Write an {@link Element}'s value to the destination. The value will first
	 * be right-trimmed (trailing whitespace is removed). The value will also
	 * probably be XML-escaped.
	 * 
	 * @param out
	 *        the destination Writer.
	 * @param fstack
	 *        the {@link FormatStack}
	 * @param str
	 *        the Element's value.
	 * @throws IOException
	 *         if the Writer fails.
	 */
	protected void textEscapeTrimRight(final Writer out, FormatStack fstack, final String str) throws IOException {
		final char[] chars = str.toCharArray();
		int right = chars.length - 1;
		while (right >= 0 && Verifier.isXMLWhitespace(chars[right])) {
			right--;
		}
		if (right < 0) {
			return;
		}

		textEscapedEntitiesFilter(out, fstack, chars, 0, right + 1);
	}

	/**
	 * Write an {@link Element}'s value to the destination. The value will first
	 * be left-trimmed (leading whitespace is removed). The value will also
	 * probably be XML-escaped.
	 * 
	 * @param out
	 *        the destination Writer.
	 * @param fstack
	 *        the {@link FormatStack}
	 * @param str
	 *        the Element's value.
	 * @throws IOException
	 *         if the Writer fails.
	 */
	protected void textEscapeTrimLeft(final Writer out, FormatStack fstack, final String str) throws IOException {
		final char[] chars = str.toCharArray();
		int right = chars.length;
		int left = 0;
		while (left < right && Verifier.isXMLWhitespace(chars[left])) {
			left++;
		}
		textEscapedEntitiesFilter(out, fstack, chars, left, right);
	}

	/* *******************************************
	 * Support methods for output. Should all be protected. All content-type
	 * print methods have a FormatStack. Only printContent is responsible for
	 * outputting appropriate indenting and newlines, which are easily available
	 * using the FormatStack.getLevelIndent() and FormatStack.getLevelEOL().
	 * *******************************************
	 */

	/**
	 * This will handle printing of a {@link Document}.
	 * 
	 * @param out
	 *        <code>Writer</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param nstack
	 *        the NamespaceStack
	 * @param doc
	 *        <code>Document</code> to write.
	 * @throws IOException
	 *         if the destination Writer fails
	 */
	protected void printDocument(final Writer out, final FormatStack fstack,
			final NamespaceStack nstack, final Document doc) throws IOException {

		printDeclaration(out, fstack);

		if (doc.hasRootElement()) {
			printContent(out, fstack, nstack, doc.getContent());
		} else {
			final int sz = doc.getContentSize();
			for (int i = 0; i < sz; i++) {
				helperContentDispatcher(out, fstack, nstack, doc.getContent(i));
			}
		}

		// Output final line separator
		// We output this no matter what the newline flags say
		if (fstack.getIndent() == null) {
			write(out, fstack.getLineSeparator());
		}

	}

	/**
	 * This will handle printing of the XML declaration. Assumes XML version 1.0
	 * since we don't directly know.
	 * 
	 * @param out
	 *        <code>Writer</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @throws IOException
	 *         if the destination Writer fails
	 */
	protected void printDeclaration(final Writer out, final FormatStack fstack) throws IOException {

		// Only print the declaration if it's not being omitted
		if (fstack.isOmitDeclaration()) {
			return;
		}
		// Declaration is never indented.
		// write(out, fstack.getLevelIndent());

		// Assume 1.0 version
		if (fstack.isOmitEncoding()) {
			write(out, "<?xml version=\"1.0\"?>");
		} else {
			write(out, "<?xml version=\"1.0\"");
			write(out, " encoding=\"");
			write(out, fstack.getEncoding());
			write(out, "\"?>");
		}

		// Print new line after decl always, even if no other new lines
		// Helps the output look better and is semantically
		// inconsequential
		// newline(out, fstack);
		write(out, fstack.getLineSeparator());
	}

	/**
	 * This will handle printing of a {@link DocType}.
	 * 
	 * @param out
	 *        <code>Writer</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param docType
	 *        <code>DocType</code> to write.
	 * @throws IOException
	 *         if the destination Writer fails
	 */
	protected void printDocType(final Writer out, final FormatStack fstack,
			final DocType docType) throws IOException {

		final String publicID = docType.getPublicID();
		final String systemID = docType.getSystemID();
		final String internalSubset = docType.getInternalSubset();
		boolean hasPublic = false;

		// Declaration is never indented.
		// write(out, fstack.getLevelIndent());

		write(out, "<!DOCTYPE ");
		write(out, docType.getElementName());
		if (publicID != null) {
			write(out, " PUBLIC \"");
			write(out, publicID);
			write(out, "\"");
			hasPublic = true;
		}
		if (systemID != null) {
			if (!hasPublic) {
				write(out, " SYSTEM");
			}
			write(out, " \"");
			write(out, systemID);
			write(out, "\"");
		}
		if ((internalSubset != null) && (!internalSubset.equals(""))) {
			write(out, " [");
			write(out, fstack.getLineSeparator());
			write(out, docType.getInternalSubset());
			write(out, "]");
		}
		write(out, ">");

		// DocType does not write it's own EOL
		// for compatibility reasons. Only
		// when output from inside a Content set.
		// write(out, fstack.getLineSeparator());
	}

	/**
	 * This will handle printing of a {@link ProcessingInstruction}.
	 * 
	 * @param out
	 *        <code>Writer</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param pi
	 *        <code>ProcessingInstruction</code> to write.
	 * @throws IOException
	 *         if the destination Writer fails
	 */
	protected void printProcessingInstruction(final Writer out,
			final FormatStack fstack, final ProcessingInstruction pi)
			throws IOException {
		String target = pi.getTarget();
		boolean piProcessed = false;

		if (fstack.isIgnoreTrAXEscapingPIs() == false) {
			if (target.equals(Result.PI_DISABLE_OUTPUT_ESCAPING)) {
				// special case... change the FormatStack
				fstack.setEscapeOutput(false);
				piProcessed = true;
			}
			else if (target.equals(Result.PI_ENABLE_OUTPUT_ESCAPING)) {
				// special case... change the FormatStack
				fstack.setEscapeOutput(true);
				piProcessed = true;
			}
		}
		if (piProcessed == false) {
			String rawData = pi.getData();

			// Write <?target data?> or if no data then just <?target?>
			if (!"".equals(rawData)) {
				write(out, "<?");
				write(out, target);
				write(out, " ");
				write(out, rawData);
				write(out, "?>");
			}
			else {
				write(out, "<?");
				write(out, target);
				write(out, "?>");
			}
		}
	}

	/**
	 * This will handle printing of a {@link Comment}.
	 * 
	 * @param out
	 *        <code>Writer</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param comment
	 *        <code>Comment</code> to write.
	 * @throws IOException
	 *         if the destination Writer fails
	 */
	protected void printComment(final Writer out, final FormatStack fstack,
			final Comment comment) throws IOException {
		write(out, "<!--");
		write(out, comment.getText());
		write(out, "-->");
	}

	/**
	 * This will handle printing of an {@link EntityRef}.
	 * 
	 * @param out
	 *        <code>Writer</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param entity
	 *        <code>EntotyRef</code> to write.
	 * @throws IOException
	 *         if the destination Writer fails
	 */
	protected void printEntityRef(final Writer out, final FormatStack fstack,
			final EntityRef entity) throws IOException {
		// EntityRefs are treated like text, not indented/newline content.
		textEntityRef(out, entity.getName());
	}

	/**
	 * This will handle printing of a {@link CDATA}.
	 * 
	 * @param out
	 *        <code>Writer</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param cdata
	 *        <code>CDATA</code> to write.
	 * @throws IOException
	 *         if the destination Writer fails
	 */
	protected void printCDATA(final Writer out, final FormatStack fstack,
			final CDATA cdata) throws IOException {
		// CDATAs are treated like text, not indented/newline content.
		switch (fstack.getTextMode()) {
			case PRESERVE:
				textCDATARaw(out, cdata.getText());
				break;
			case NORMALIZE:
				textCDATACompact(out, cdata.getText());
				break;
			case TRIM:
				textCDATATrimBoth(out, cdata.getText());
				break;
			case TRIM_FULL_WHITE:
				textCDATATrimFullWhite(out, cdata.getText());
				break;
		}
	}

	/**
	 * This will handle printing of a {@link Text}.
	 * 
	 * @param out
	 *        <code>Writer</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param text
	 *        <code>Text</code> to write.
	 * @throws IOException
	 *         if the destination Writer fails
	 */
	protected void printText(final Writer out, final FormatStack fstack,
			final Text text) throws IOException {
		final String str = text.getText();
		switch (fstack.getTextMode()) {
			case PRESERVE:
				textEscapeRaw(out, fstack, str);
				break;
			case NORMALIZE:
				textEscapeCompact(out, fstack, str);
				break;
			case TRIM:
				textEscapeTrimBoth(out, fstack, str);
				break;
			case TRIM_FULL_WHITE:
				textEscapeTrimFullWhite(out, fstack, str);
				break;
		}
	}

	/**
	 * This will handle printing of an {@link Element}.
	 * <p>
	 * This method arranges for outputting the Element infrastructure including
	 * Namespace Declarations and Attributes.
	 * <p>
	 * There has to be a fair amount of inspection to determine whether there is
	 * any content and whether it is all text-type content, and based on the
	 * required Format output, how to deal with the content. Alternatives (in
	 * 'fall-through' order) are:
	 * <ol>
	 * <li>There is no content, then output just an empty element (depending on
	 * settings this may be either <code>&ltelement /&gt</code> or
	 * <code>&ltelement&gt&lt/element&gt</code>).
	 * <li>There is only text-type content and the current Format settings would
	 * condense the text-type content to nothing, then output just an empty
	 * element (depending on settings this may be either
	 * <code>&ltelement /&gt</code> or <code>&ltelement&gt&lt/element&gt</code>
	 * ).
	 * <li>There is just a single text-type content then delegate to that
	 * content's print method
	 * <li>There is only text-type content (more than one though) then delegate
	 * to {@link #printTextConsecutive(Writer, FormatStack, List, int, int)}
	 * <li>There is content other than just text-type content, then delegate to
	 * {@link #printContent(Writer, FormatStack, NamespaceStack, List)}
	 * </ol>
	 * <p>
	 * 
	 * @param out
	 *        <code>Writer</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param nstack
	 *        the NamespaceStack
	 * @param element
	 *        <code>Element</code> to write.
	 * @throws IOException
	 *         if the destination Writer fails
	 */
	protected void printElement(final Writer out, final FormatStack fstack,
			final NamespaceStack nstack, final Element element) throws IOException {

		nstack.push(element);
		try {
			final List<Content> content = element.getContent();

			// Print the beginning of the tag plus attributes and any
			// necessary namespace declarations
			write(out, "<");

			write(out, element.getQualifiedName());

			// Print the element's namespace, if appropriate
			for (final Namespace ns : nstack.addedForward()) {
				printNamespace(out, fstack, ns);
			}

			// Print out attributes
			for (final Attribute attribute : element.getAttributes()) {
				printAttribute(out, fstack, attribute);
			}

			if (content.isEmpty()) {
				// Case content is empty or all insignificant whitespace
				if (fstack.isExpandEmptyElements()) {
					write(out, "></");
					write(out, element.getQualifiedName());
					write(out, ">");
				}
				else {
					write(out, " />");
				}
				// nothing more to do.
				return;
			}
			boolean textonly = true;
			// OK, we have real content to push.
			fstack.push();
			try {

				// Check for xml:space and adjust format settings
				final String space = element.getAttributeValue("space",
						Namespace.XML_NAMESPACE);

				if ("default".equals(space)) {
					fstack.setTextMode(fstack.getDefaultMode());
				}
				else if ("preserve".equals(space)) {
					fstack.setTextMode(TextMode.PRESERVE);
				}

				if (TextMode.PRESERVE == fstack.getTextMode()) {
					write(out, ">");
					printContent(out, fstack, nstack, content);
				} else {
					// not PRESERVE mode:
					// two complicated logic steps happen in tandem now.
					// If we have no content and empty text, we don't print
					// anything.
					// If we have only text, we don't do newlines. textonly is
					// declared higher up....
					// we do quick scans of the data to get this sorted out.

					// is there some content to work on.
					boolean empty = true;

					// we can ignore TextMode preserve because that does not
					// change
					// white-space.
					empty = true;
					// do the quick checks first...
					emptyloop: for (final Content c : content) {
						switch (c.getCType()) {
							case Comment:
							case Element:
							case ProcessingInstruction:
								textonly = false;
							case EntityRef:
								// EntityRef counts as Text...
								empty = false;
								break emptyloop;
							default:
								break;
						}
					}
					if (empty) {
						// do the harder check for non-whitespace.
						textemptyloop: for (final Content c : content) {
							switch (c.getCType()) {
								case CDATA:
								case Text:
									// both return the text as getValue()
									for (char ch : c.getValue().toCharArray()) {
										if (!Verifier.isXMLWhitespace(ch)) {
											empty = false;
											break textemptyloop;
										}
									}
									break;
								default:
									break;
							}
						}
					}
					if (empty) {
						// Case content is empty or all insignificant whitespace
						if (fstack.isExpandEmptyElements()) {
							write(out, "></");
							write(out, element.getQualifiedName());
							write(out, ">");
						}
						else {
							write(out, " />");
						}
						// nothing more to do.
						return;
					}

					write(out, ">");

					if (textonly) {
						helperTextType(out, fstack, content, 0, content.size());
					} else {
						write(out, fstack.getLevelEOL());
						printContent(out, fstack, nstack, content);
					}

				}

			} finally {
				fstack.pop();
			}
			if (!textonly) {
				write(out, fstack.getLevelIndent());
			}
			write(out, "</");
			write(out, element.getQualifiedName());
			write(out, ">");
		} finally {
			nstack.pop();
		}

	}

	/**
	 * This will handle printing of a List of {@link Content}.
	 * <p>
	 * The list of Content is basically processed as one of three types of
	 * content
	 * <ol>
	 * <li>Consecutive text-type (Text, CDATA, and EntityRef) content
	 * <li>Stand-alone text-type content
	 * <li>Non-text-type content.
	 * </ol>
	 * Although the code looks complex, the theory is conceptually simple:
	 * <ol>
	 * <li>identify one of the three types (consecutive, stand-alone, non-text)
	 * <li>do indent if any is specified.
	 * <li>send the type to the respective print* handler (e.g.
	 * {@link #printTextConsecutive(Writer, FormatStack, List, int, int)},
	 * {@link #printCDATA(Writer, FormatStack, CDATA)}, or
	 * {@link #printComment(Writer, FormatStack, Comment)},
	 * <li>do a newline if one is specified.
	 * <li>loop back to 1. until there's no more content to process.
	 * </ol>
	 * 
	 * @param out
	 *        <code>Writer</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param nstack
	 *        the NamespaceStack
	 * @param content
	 *        <code>List</code> of <code>Content</code> to write.
	 * @throws IOException
	 *         if the destination Writer fails
	 */
	protected void printContent(final Writer out,
			final FormatStack fstack, final NamespaceStack nstack,
			final List<? extends Content> content)
			throws IOException {

		// Basic theory of operation is as follows:
		// 0. raw-print for PRESERVE.
		// 1. print any non-Text content
		// 2. Accumulate all sequential text content and print them together
		// Do not do any newlines before or after content.
		if (fstack.getTextMode() == TextMode.PRESERVE) {
			for (Content c : content) {
				helperContentDispatcher(out, fstack, nstack, c);
			}
			return;
		}

		int txti = -1;
		int index = 0;
		for (final Content c : content) {
			switch (c.getCType()) {
				case Text:
				case CDATA:
				case EntityRef:
					//
					// Handle consecutive CDATA, Text, and EntityRef nodes all
					// at once
					//
					if (txti < 0) {
						txti = index;
					}
					break;
				default:
					if (txti >= 0) {
						// we have text content we need to print.
						// we also know that we are 'mixed' content
						// so the text content should be indented.
						// but, since we have no PRESERVE content, only the
						// 'trimming' variants, if it is all whitespace we do
						// not want to even print the indent/newline.
						if (!isAllWhiteSpace(content, txti, index - txti)) {
							write(out, fstack.getLevelIndent());
							helperTextType(out, fstack, content, txti, index - txti);
							write(out, fstack.getLevelEOL());
						}
					}
					txti = -1;

					write(out, fstack.getLevelIndent());
					helperContentDispatcher(out, fstack, nstack, c);
					write(out, fstack.getLevelEOL());
			}
			index++;
		}
		if (txti >= 0) {
			// we have text content we need to print.
			// we also know that we are 'mixed' content
			// so the text content should be indented.
			// but, since we have no PRESERVE content, only the
			// 'trimming' variants, if it is all whitespace we do
			// not want to even print the indent/newline.
			if (!isAllWhiteSpace(content, txti, index - txti)) {
				write(out, fstack.getLevelIndent());
				helperTextType(out, fstack, content, txti, index - txti);
				write(out, fstack.getLevelEOL());
			}
		}

	}

	/**
	 * This will handle printing of a consecutive sequence of text-type
	 * {@link Content} (<code>{@link CDATA}</code> <code>{@link Text}</code>, or
	 * <code>{@link EntityRef}</code>) nodes. It is an error (unspecified
	 * Exception or unspecified behaviour) to pass this method any other type of
	 * node.
	 * <p>
	 * It is a requirement for this method that at least one of the specified
	 * text-type instances has non-whitespace, or, put the other way, if all
	 * specified text-type values are all white-space only, then you may
	 * get odd looking XML (empty lines). 
	 * 
	 * @param out
	 *        <code>Writer</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param content
	 *        <code>List</code> of <code>Content</code> to write.
	 * @param offset
	 *        index of first content node (inclusive).
	 * @param len
	 *        number of nodes that are text-type.
	 * @throws IOException
	 *         if the destination Writer fails
	 */
	protected void printTextConsecutive(final Writer out, FormatStack fstack,
			final List<? extends Content> content,
			final int offset, final int len) throws IOException {

		// we are guaranteed that len >= 2
		assert len >= 2 : "Need at least two text-types to be 'Consecutive'";

		// here we have a sequence of Text nodes to print.
		// we follow the TextMode rules for it.
		// also guaranteed is that at least one of the nodes has some real text.

		final TextMode mode = fstack.getTextMode();
		// For when there's just one thing to print, or in RAW mode we just
		// print the contents...
		// ... its the simplest mode.
		// so simple, in fact that it is dealt with differently as a special
		// case.
		assert TextMode.PRESERVE != mode : "PRESERVE text-types are not Consecutive";

		// Right, at this point it is complex.
		// we have multiple nodes to output, and it could be in one of three
		// modes, TRIM, TRIM_FULL_WHITE, or NORMALIZE
		// we aggregate the text together, and dump it

		// Let's get TRIM_FULL_WHITE out of the way.
		// in this mode, if there is any real text content, we print it all.
		// otherwise we print nothing
		// since it is a requirement for this method that there has to be some
		// non-whitespace text, we can just print everything.
		if (TextMode.TRIM_FULL_WHITE == mode) {
			// Right, if we find text, we print it all.
			// If we don't we print nothing.
			int cnt = len;
			for (ListIterator<? extends Content> li =
					content.listIterator(offset); cnt > 0; cnt--) {
				helperRawTextType(out, fstack, li.next());
			}
			return;
		}

		// OK, now we are left with just TRIM and NORMALIZE
		// Normalize is now actually easier.
		if (TextMode.NORMALIZE == mode) {
			boolean dospace = false;
			// we know there's at least 2 content nodes...
			// we process the 'gaps' to see if we need spaces, and print
			// all the content in NORMALIZED form.
			boolean first = true;
			int cnt = len;
			for (Iterator<? extends Content> li = content.listIterator(offset); cnt > 0; cnt--) {
				boolean endspace = false;
				final Content c = li.next();
				if (c instanceof Text) {
					final String val = ((Text) c).getValue();
					if (val.length() == 0) {
						// ignore empty text.
						continue;
					}
					if (isAllWhitespace(val)) {
						// we skip all whitespace.
						// but record the space
						if (!first) {
							dospace = true;
						}
						continue;
					}
					if (!first && !dospace &&
							Verifier.isXMLWhitespace(val.charAt(0))) {
						// we are between content, and need to put in a space.
						dospace = true;
					}
					endspace = Verifier.isXMLWhitespace(val.charAt(val.length() - 1));
				}
				first = false;
				if (dospace) {
					textRaw(out, ' ');
				}

				switch (c.getCType()) {
					case Text:
						textEscapeCompact(out, fstack, ((Text) c).getText());
						break;
					case CDATA:
						textCDATACompact(out, ((CDATA) c).getText());
						break;
					case EntityRef:
						textEntityRef(out, ((EntityRef) c).getName());
					default:
						// do nothing
				}
				dospace = endspace;
			}
			return;
		}

		// All that's left now is multi-content that needs to be trimmed.
		// For this, we do a cheat....
		// we trim the left and right content, and print any middle content
		// as raw.
		// we have to preserve the right-whitespace of the left content, and the
		// left whitespace of the right content.
		// no need to check the mode, it can only be TRIM.
		// if (TextMode.TRIM == fstack.getTextMode()) {

			int lcnt = 0;
			while (lcnt < len) {
				Content node = content.get(offset + lcnt);
				if (!(node instanceof Text) || !isAllWhitespace(node.getValue())) {
					break;
				}
				lcnt++;
			}
			final int left = offset + lcnt;
			int rcnt = len - 1;
			while (rcnt > lcnt) {
				Content node = content.get(offset + rcnt);
				if (!(node instanceof Text) || !isAllWhitespace(node.getValue())) {
					break;
				}
				rcnt--;
			}
			final int right = offset + rcnt;

			if (left == right) {
				// came down to just one value to output.
				final Content node = content.get(left);
				switch (node.getCType()) {
					case Text:
						textEscapeTrimBoth(out, fstack, ((Text) node).getText());
						break;
					case CDATA:
						textCDATATrimBoth(out, ((CDATA) node).getText());
						break;
					case EntityRef:
						textEntityRef(out, ((EntityRef) node).getName());
					default:
						// do nothing
				}
				return;
			}
			final Content leftc = content.get(left);
			switch (leftc.getCType()) {
				case Text:
					textEscapeTrimLeft(out, fstack, ((Text) leftc).getText());
					break;
				case CDATA:
					textCDATATrimLeft(out, ((CDATA) leftc).getText());
					break;
				case EntityRef:
					textEntityRef(out, ((EntityRef) leftc).getName());
				default:
					// do nothing
			}

			int cnt = right - left - 1;
			for (ListIterator<? extends Content> li =
					content.listIterator(left + 1); cnt > 0; cnt--) {
				helperRawTextType(out, fstack, li.next());
			}

			final Content rightc = content.get(right);
			switch (rightc.getCType()) {
				case Text:
					textEscapeTrimRight(out, fstack, ((Text) rightc).getText());
					break;
				case CDATA:
					textCDATATrimRight(out, ((CDATA) rightc).getText());
					break;
				case EntityRef:
					textEntityRef(out, ((EntityRef) rightc).getName());
				default:
					// do nothing
			}
		// }
	}

	/**
	 * This method contains code which is reused in a number of places. It
	 * simply determines what content is passed in, and dispatches it to the
	 * correct print* method.
	 * 
	 * @param out
	 *        The destination to write to.
	 * @param fstack
	 *        The current FormatStack
	 * @param nstack
	 *        the NamespaceStack
	 * @param content
	 *        The content to dispatch
	 * @throws IOException
	 *         if the output fails
	 */
	protected void helperContentDispatcher(final Writer out,
			final FormatStack fstack, final NamespaceStack nstack,
			final Content content) throws IOException {
		switch (content.getCType()) {
			case CDATA:
				printCDATA(out, fstack, (CDATA) content);
				break;
			case Comment:
				printComment(out, fstack, (Comment) content);
				break;
			case Element:
				printElement(out, fstack, nstack, (Element) content);
				break;
			case EntityRef:
				printEntityRef(out, fstack, (EntityRef) content);
				break;
			case ProcessingInstruction:
				printProcessingInstruction(out, fstack,
						(ProcessingInstruction) content);
				break;
			case Text:
				printText(out, fstack, (Text) content);
				break;
			case DocType:
				printDocType(out, fstack, (DocType) content);
				write(out, fstack.getLineSeparator());
				break;
			default:
				throw new IllegalStateException(
						"Unexpected Content " + content.getCType());
		}
	}

	/**
	 * This helper determines how much text-type content there is to print. If
	 * there is a single node only, then it is dispatched to the respective
	 * print* method, otherwise it is dispatched to the printTextConsecutive
	 * method.
	 * <p>
	 * It is a requirement for this method that at least one of the specified
	 * text-type instances has non-whitespace, or, put the other way, if all
	 * specified text-type values are all white-space only, then you may
	 * get odd looking XML (empty lines). 
	 * <p>
	 * Odd things can happen too if the len is <= 0;
	 * 
	 * @param out
	 *        where to write
	 * @param fstack
	 *        the current FormatStack
	 * @param content
	 *        The list of Content to process
	 * @param offset
	 *        The offset of the first text-type content in the sequence
	 * @param len
	 *        the number of text-type content in the sequence
	 * @throws IOException
	 *         if the output fails.
	 */
	protected final void helperTextType(final Writer out,
			final FormatStack fstack, final List<? extends Content> content,
			final int offset, final int len) throws IOException {

		assert len > 0 : "All calls to this should have *some* content.";

		if (len == 1) {
			final Content node = content.get(offset);
			// Print the node
			switch (node.getCType()) {
				case Text:
					printText(out, fstack, (Text) node);
					break;
				case CDATA:
					printCDATA(out, fstack, (CDATA) node);
					break;
				case EntityRef:
					printEntityRef(out, fstack, (EntityRef) node);
				default:
					// do nothing
			}
		} else {
			// we have text content we need to print.
			// we also know that we are 'mixed' content
			// so the text content should be indented.
			// Additionally
			printTextConsecutive(out, fstack, content, offset, len);
		}
	}

	/**
	 * This method contains code which is reused in a number of places. It
	 * simply determines which of the text-type content is passed in, and
	 * dispatches it to the correct text method (textEscapeRaw, textCDATARaw, or
	 * textEntityRef).
	 * 
	 * @param out
	 *        The destination to write to.
	 * @param fstack
	 *        The current FormatStack
	 * @param content
	 *        The content to dispatch
	 * @throws IOException
	 *         if the output fails
	 */
	protected final void helperRawTextType(final Writer out,
			final FormatStack fstack, final Content content) throws IOException {
		switch (content.getCType()) {
			case Text:
				textEscapeRaw(out, fstack, ((Text) content).getText());
				break;
			case CDATA:
				textCDATARaw(out, ((CDATA) content).getText());
				break;
			case EntityRef:
				textEntityRef(out, ((EntityRef) content).getName());
			default:
				// do nothing
		}
	}

	/**
	 * This will handle printing of any needed <code>{@link Namespace}</code>
	 * declarations.
	 * 
	 * @param out
	 *        <code>Writer</code> to use.
	 * @param fstack
	 *        The current FormatStack
	 * @param ns
	 *        <code>Namespace</code> to print definition of
	 * @throws IOException
	 *         if the output fails
	 */
	protected void printNamespace(final Writer out, final FormatStack fstack, 
			final Namespace ns)  throws IOException {
		final String prefix = ns.getPrefix();
		final String uri = ns.getURI();

		write(out, " xmlns");
		if (!prefix.equals("")) {
			write(out, ":");
			write(out, prefix);
		}
		write(out, "=\"");
		attributeEscapedEntitiesFilter(out, fstack, uri);
		write(out, "\"");
	}

	/**
	 * This will handle printing of an <code>{@link Attribute}</code>.
	 * 
	 * @param out
	 *        <code>Writer</code> to use.
	 * @param fstack
	 *        The current FormatStack
	 * @param attribute
	 *        <code>Attribute</code> to output
	 * @throws IOException
	 *         if the output fails
	 */
	protected void printAttribute(final Writer out, final FormatStack fstack,
			final Attribute attribute) throws IOException {

		write(out, " ");
		write(out, attribute.getQualifiedName());
		write(out, "=");

		write(out, "\"");
		attributeEscapedEntitiesFilter(out, fstack, attribute.getValue());
		write(out, "\"");
	}
	
}
