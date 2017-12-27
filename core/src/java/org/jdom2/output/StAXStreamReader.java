/*--

 Copyright (C) 2000-2012 Jason Hunter & Brett McLaughlin.
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

import javax.xml.stream.XMLStreamReader;

import org.jdom2.Document;
import org.jdom2.output.support.AbstractStAXStreamReaderProcessor;
import org.jdom2.output.support.StAXStreamReaderProcessor;

/**
 * Represents a JDOM document as a StAX XMLStreamReader that can be read from.
 * <p>
 * The StAXStreamReader can manage many styles of document formatting, from
 * untouched to 'pretty' printed. The default is to output the document content
 * exactly as created, but this can be changed by setting a new Format object:
 * <ul>
 * <li>For pretty-print output, use
 * <code>{@link Format#getPrettyFormat()}</code>.
 * <li>For whitespace-normalised output, use
 * <code>{@link Format#getCompactFormat()}</code>.
 * <li>For unmodified-format output, use
 * <code>{@link Format#getRawFormat()}</code>.
 * </ul>
 * <p>
 * There is only one <code>{@link #output output(Document)}</code> method that exposes
 * a JDOM Document as a StAX Stream.
 * <p>
 * If changing the {@link Format} settings are insufficient for your output
 * needs you can customise this StAXStreamReader further by setting a different
 * {@link StAXStreamReaderProcessor} with the
 * {@link #setStAXAsStreamProcessor(StAXStreamReaderProcessor)} method or an appropriate
 * constructor. A fully-enabled Abstract class
 * {@link AbstractStAXStreamReaderProcessor} is available to be further extended to
 * your needs if all you want to do is tweak some details.
 * 
 * @author Rolf Lear
 * @since JDOM 2.1.0
 */

public final class StAXStreamReader implements Cloneable {

	/*
	 * =====================================================================
	 * Static content.
	 * =====================================================================
	 */

	/**
	 * Create a final and static instance of the StAXStreamReaderProcessor The
	 * final part is important because it improves performance.
	 * <p>
	 * The JDOM user can change the actual XMLOutputProcessor with the
	 * {@link StAXStreamReader#setStAXAsStreamProcessor(StAXStreamReaderProcessor)} method.
	 * 
	 * @author rolf
	 */
	private static final class DefaultStAXAsStreamProcessor extends AbstractStAXStreamReaderProcessor {

		// do nothing different

	}

	/**
	 * This constant XMLOutputProcessor is used for all non-customised
	 * XMLOutputters
	 */
	private static final DefaultStAXAsStreamProcessor DEFAULTPROCESSOR =
			new DefaultStAXAsStreamProcessor();

	/*
	 * =====================================================================
	 * Instance content.
	 * =====================================================================
	 */

	// For normal output
	private Format myFormat = null;

	// The actual XMLOutputProcessor to delegate to.
	private StAXStreamReaderProcessor myProcessor = null;

	/*
	 * =====================================================================
	 * Constructors
	 * =====================================================================
	 */

	/**
	 * This will create an <code>XMLOutputter</code> with the specified format
	 * characteristics.
	 * <p>
	 * <b>Note:</b> the format object is cloned internally before use. If you
	 * want to modify the Format after constructing the XMLOutputter you can
	 * modify the Format instance {@link #getFormat()} returns.
	 * 
	 * @param format
	 *        The Format instance to use. This instance will be cloned() and as
	 *        a consequence, changes made to the specified format instance
	 *        <b>will not</b> be reflected in this XMLOutputter. A null input
	 *        format indicates that XMLOutputter should use the default
	 *        {@link Format#getRawFormat()}
	 * @param processor
	 *        The XMLOutputProcessor to delegate output to. If null the
	 *        XMLOutputter will use the default XMLOutputProcessor.
	 */
	public StAXStreamReader(Format format, StAXStreamReaderProcessor processor) {
		myFormat = format == null ? Format.getRawFormat() : format.clone();
		myProcessor = processor == null ? DEFAULTPROCESSOR : processor;
	}

	/**
	 * This will create an <code>XMLOutputter</code> with a default
	 * {@link Format} and {@link StAXStreamReaderProcessor}.
	 */
	public StAXStreamReader() {
		this(null, null);
	}

	/**
	 * This will create an <code>XMLOutputter</code> with the same
	 * customisations set in the given <code>XMLOutputter</code> instance. Note
	 * that <code>XMLOutputter two = one.clone();</code> would work equally
	 * well.
	 * 
	 * @param that
	 *        the XMLOutputter to clone
	 */
	public StAXStreamReader(StAXStreamReader that) {
		this(that.myFormat, null);
	}

	/**
	 * This will create an <code>XMLOutputter</code> with the specified format
	 * characteristics.
	 * <p>
	 * <b>Note:</b> the format object is cloned internally before use.
	 * 
	 * @param format
	 *        The Format instance to use. This instance will be cloned() and as
	 *        a consequence, changes made to the specified format instance
	 *        <b>will not</b> be reflected in this XMLOutputter. A null input
	 *        format indicates that XMLOutputter should use the default
	 *        {@link Format#getRawFormat()}
	 */
	public StAXStreamReader(Format format) {
		this(format, null);
	}

	/**
	 * This will create an <code>XMLOutputter</code> with the specified
	 * XMLOutputProcessor.
	 * 
	 * @param processor
	 *        The XMLOutputProcessor to delegate output to. If null the
	 *        XMLOutputter will use the default XMLOutputProcessor.
	 */
	public StAXStreamReader(StAXStreamReaderProcessor processor) {
		this(null, processor);
	}

	/*
	 * =======================================================================
	 * API - Settings...
	 * =======================================================================
	 */

	/**
	 * Sets the new format logic for the XMLOutputter. Note the Format object is
	 * cloned internally before use.
	 * 
	 * @see #getFormat()
	 * @param newFormat
	 *        the format to use for subsequent output
	 */
	public void setFormat(Format newFormat) {
		this.myFormat = newFormat.clone();
	}

	/**
	 * Returns the current format in use by the XMLOutputter. Note the Format
	 * object returned is <b>not</b> a clone of the one used internally, thus,
	 * an XMLOutputter instance is able to have its Format changed by changing
	 * the settings on the Format instance returned by this method.
	 * 
	 * @return the current Format instance used by this XMLOutputter.
	 */
	public Format getFormat() {
		return myFormat;
	}

	/**
	 * Returns the current XMLOutputProcessor instance in use by the
	 * StAXStreamReader.
	 * 
	 * @return the current XMLOutputProcessor instance.
	 */
	public StAXStreamReaderProcessor getStAXAsStreamProcessor() {
		return myProcessor;
	}

	/**
	 * Sets a new StAXStreamReaderProcessor instance for this StAXStreamReader. Note the
	 * processor object is expected to be thread-safe.
	 * 
	 * @param processor
	 *        the new XMLOutputProcesor to use for output
	 */
	public void setStAXAsStreamProcessor(StAXStreamReaderProcessor processor) {
		this.myProcessor = processor;
	}

	/*
	 * =======================================================================
	 * API - Output to STREAM Methods ... All methods defer to the WRITER
	 * equivalents
	 * =======================================================================
	 */

	/**
	 * This will expose the <code>{@link Document}</code> as a StAX XMLStreamReader.
	 * 
	 * @param doc
	 *        <code>Document</code> to format.
	 * @return The XMLStreamReader representing the input Document.
	 * @throws NullPointerException
	 *         if the specified content is null.
	 */
	public final XMLStreamReader output(Document doc) {
		return myProcessor.buildReader(doc, myFormat.clone());
	}

	/*
	 * ========================================================================
	 * Basic Support methods.
	 * ========================================================================
	 */

	/**
	 * Returns a cloned copy of this XMLOutputter.
	 */
	@Override
	public StAXStreamReader clone() {
		// Implementation notes: Since all state of an XMLOutputter is
		// embodied in simple private instance variables, Object.clone
		// can be used. Note that since Object.clone is totally
		// broken, we must catch an exception that will never be
		// thrown.
		try {
			return (StAXStreamReader) super.clone();
		} catch (java.lang.CloneNotSupportedException e) {
			// even though this should never ever happen, it's still
			// possible to fool Java into throwing a
			// CloneNotSupportedException. If that happens, we
			// shouldn't swallow it.
			throw new RuntimeException("Unexpected CloneNotSupportedException", e);
		}
	}

	/**
	 * Return a string listing of the settings for this XMLOutputter instance.
	 * 
	 * @return a string listing the settings for this XMLOutputter instance
	 */
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("StAXStreamReader[omitDeclaration = ");
		buffer.append(myFormat.omitDeclaration);
		buffer.append(", ");
		buffer.append("encoding = ");
		buffer.append(myFormat.encoding);
		buffer.append(", ");
		buffer.append("omitEncoding = ");
		buffer.append(myFormat.omitEncoding);
		buffer.append(", ");
		buffer.append("indent = '");
		buffer.append(myFormat.indent);
		buffer.append("'");
		buffer.append(", ");
		buffer.append("expandEmptyElements = ");
		buffer.append(myFormat.expandEmptyElements);
		buffer.append(", ");
		buffer.append("lineSeparator = '");
		for (char ch : myFormat.lineSeparator.toCharArray()) {
			switch (ch) {
				case '\r':
					buffer.append("\\r");
					break;
				case '\n':
					buffer.append("\\n");
					break;
				case '\t':
					buffer.append("\\t");
					break;
				default:
					buffer.append("[" + ((int) ch) + "]");
					break;
			}
		}
		buffer.append("', ");
		buffer.append("textMode = ");
		buffer.append(myFormat.mode + "]");
		return buffer.toString();
	}

}
