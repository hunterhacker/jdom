/*--

 Copyright (C) 2011-2012 Jason Hunter & Brett McLaughlin.
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

import java.util.List;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.util.XMLEventConsumer;

import org.jdom2.Attribute;
import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.Content;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.EntityRef;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;
import org.jdom2.output.support.AbstractStAXEventProcessor;
import org.jdom2.output.support.StAXEventProcessor;
import org.jdom2.output.support.XMLOutputProcessor;

/**
 * Outputs a JDOM document as a StAX XMLEventConsumer of bytes.
 * <p>
 * The StAXStreamOutputter can manage many styles of document formatting, from
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
 * <b>All</b> of the <code>output*(...)</code> methods will flush the
 * destination XMLEventConsumer before returning, and <b>none</b> of them
 * will <code>close()</code> the destination.
 * <p>
 * To omit output of the declaration use
 * <code>{@link Format#setOmitDeclaration}</code>. To omit printing of the
 * encoding in the declaration use <code>{@link Format#setOmitEncoding}</code>.
 * <p>
 * If changing the {@link Format} settings are insufficient for your output
 * needs you can customise this StAXStreamOutputter further by setting a different
 * {@link StAXEventProcessor} with the
 * {@link #setStAXEventProcessor(StAXEventProcessor)} method or an appropriate
 * constructor. A fully-enabled Abstract class
 * {@link AbstractStAXEventProcessor} is available to be further extended to
 * your needs if all you want to do is tweak some details.
 * 
 * @since JDOM2
 * @author Rolf Lear
 */

public final class StAXEventOutputter implements Cloneable {

	/*
	 * =====================================================================
	 * Static content.
	 * =====================================================================
	 */

	/**
	 * Create a final and static instance of the AbstractStAXEventProcessor The
	 * final part is important because it improves performance.
	 * <p>
	 * The JDOM user can change the actual XMLOutputProcessor with the
	 * {@link StAXEventOutputter#setStAXEventProcessor(StAXEventProcessor)} method.
	 * 
	 * @author rolf
	 */
	private static final class DefaultStAXEventProcessor
			extends AbstractStAXEventProcessor {
		// nothing
	}

	/**
	 * This constant StAXEventProcessor is used for all non-customised
	 * StAXStreamOutputters
	 */
	private static final DefaultStAXEventProcessor DEFAULTPROCESSOR =
			new DefaultStAXEventProcessor();

	/**
	 * This constant StAXEventProcessor is used for all non-customised
	 * StAXStreamOutputters
	 */
	private static final XMLEventFactory DEFAULTEVENTFACTORY =
			XMLEventFactory.newInstance();
	
	/*
	 * =====================================================================
	 * Instance content.
	 * =====================================================================
	 */

	// For normal output
	private Format myFormat = null;

	// The actual StAXEventProcessor to delegate to.
	private StAXEventProcessor myProcessor = null;
	
	private XMLEventFactory myEventFactory = null;

	/*
	 * =====================================================================
	 * Constructors
	 * =====================================================================
	 */

	/**
	 * This will create an <code>StAXStreamOutputter</code> with the specified format
	 * characteristics.
	 * <p>
	 * <b>Note:</b> the format object is cloned internally before use. If you
	 * want to modify the Format after constructing the StAXStreamOutputter you can
	 * modify the Format instance {@link #getFormat()} returns.
	 * 
	 * @param format
	 *        The Format instance to use. This instance will be cloned() and as
	 *        a consequence, changes made to the specified format instance
	 *        <b>will not</b> be reflected in this StAXStreamOutputter. A null input
	 *        format indicates that StAXStreamOutputter should use the default
	 *        {@link Format#getRawFormat()}
	 * @param processor
	 *        The XMLOutputProcessor to delegate output to. If null the
	 *        StAXStreamOutputter will use the default XMLOutputProcessor.
	 * @param eventfactory 
	 * 		  The factory to use to create XMLEvent instances.
	 */
	public StAXEventOutputter(Format format, StAXEventProcessor processor, XMLEventFactory eventfactory) {
		myFormat = format == null ? Format.getRawFormat() : format.clone();
		myProcessor = processor == null ? DEFAULTPROCESSOR : processor;
		myEventFactory = eventfactory == null ? DEFAULTEVENTFACTORY : eventfactory;
	}

	/**
	 * This will create an <code>StAXStreamOutputter</code> with a default
	 * {@link Format} and {@link XMLOutputProcessor}.
	 */
	public StAXEventOutputter() {
		this(null, null, null);
	}

	/**
	 * This will create an <code>StAXStreamOutputter</code> with the specified format
	 * characteristics.
	 * <p>
	 * <b>Note:</b> the format object is cloned internally before use.
	 * 
	 * @param format
	 *        The Format instance to use. This instance will be cloned() and as
	 *        a consequence, changes made to the specified format instance
	 *        <b>will not</b> be reflected in this StAXStreamOutputter. A null input
	 *        format indicates that StAXStreamOutputter should use the default
	 *        {@link Format#getRawFormat()}
	 */
	public StAXEventOutputter(Format format) {
		this(format, null, null);
	}

	/**
	 * This will create an <code>StAXStreamOutputter</code> with the specified
	 * XMLOutputProcessor.
	 * 
	 * @param processor
	 *        The XMLOutputProcessor to delegate output to. If null the
	 *        StAXStreamOutputter will use the default XMLOutputProcessor.
	 */
	public StAXEventOutputter(StAXEventProcessor processor) {
		this(null, processor, null);
	}

	/**
	 * This will create an <code>StAXStreamOutputter</code> with the specified
	 * XMLOutputProcessor.
	 * 
	 * @param eventfactory
	 *        The XMLEventFactory to use to create XMLEvent instances.
	 */
	public StAXEventOutputter(XMLEventFactory eventfactory) {
		this(null, null, eventfactory);
	}

	/*
	 * =======================================================================
	 * API - Settings...
	 * =======================================================================
	 */

	/**
	 * Sets the new format logic for the StAXStreamOutputter. Note the Format object is
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
	 * Returns the current format in use by the StAXStreamOutputter. Note the Format
	 * object returned is <b>not</b> a clone of the one used internally, thus,
	 * an StAXStreamOutputter instance is able to have its Format changed by changing
	 * the settings on the Format instance returned by this method.
	 * 
	 * @return the current Format instance used by this StAXStreamOutputter.
	 */
	public Format getFormat() {
		return myFormat;
	}

	/**
	 * Returns the current XMLOutputProcessor instance in use by the
	 * StAXStreamOutputter.
	 * 
	 * @return the current XMLOutputProcessor instance.
	 */
	public StAXEventProcessor getStAXStream() {
		return myProcessor;
	}

	/**
	 * Sets a new XMLOutputProcessor instance for this StAXStreamOutputter. Note the
	 * processor object is expected to be thread-safe.
	 * 
	 * @param processor
	 *        the new XMLOutputProcesor to use for output
	 */
	public void setStAXEventProcessor(StAXEventProcessor processor) {
		this.myProcessor = processor;
	}

	
	
	/**
	 * @return the current XMLEventFactory used by this StAXEventOutputter
	 */
	public XMLEventFactory getEventFactory() {
		return myEventFactory;
	}

	/**
	 * @param myEventFactory the XMLEventFactory to use for subsequent output.
	 */
	public void setEventFactory(XMLEventFactory myEventFactory) {
		this.myEventFactory = myEventFactory;
	}

	/*
	 * ========================================================================
	 * API - Output to XMLEventConsumer Methods ... These are the core methods that the
	 * Stream and String output methods call. On the other hand, these methods
	 * defer to the protected/override methods. These methods flush the writer.
	 * ========================================================================
	 */

	/**
	 * This will print the <code>Document</code> to the given Writer.
	 * <p>
	 * Warning: using your own Writer may cause the outputter's preferred
	 * character encoding to be ignored. If you use encodings other than UTF-8,
	 * we recommend using the method that takes an OutputStream instead.
	 * </p>
	 * 
	 * @param doc
	 *        <code>Document</code> to format.
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @throws XMLStreamException
	 *         - if there's any problem writing.
	 * @throws NullPointerException
	 *         if the specified content is null.
	 */
	public final void output(Document doc, XMLEventConsumer out) throws XMLStreamException {
		myProcessor.process(out, myFormat, myEventFactory, doc);
		//out.flush();
	}

	/**
	 * Print out the <code>{@link DocType}</code>.
	 * 
	 * @param doctype
	 *        <code>DocType</code> to output.
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @throws XMLStreamException
	 *         - if there's any problem writing.
	 * @throws NullPointerException
	 *         if the specified content is null.
	 */
	public final void output(DocType doctype, XMLEventConsumer out) throws XMLStreamException {
		myProcessor.process(out, myFormat, myEventFactory, doctype);
		//out.flush();
	}

	/**
	 * Print out an <code>{@link Element}</code>, including its
	 * <code>{@link Attribute}</code>s, and all contained (child) elements, etc.
	 * 
	 * @param element
	 *        <code>Element</code> to output.
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @throws XMLStreamException
	 *         - if there's any problem writing.
	 * @throws NullPointerException
	 *         if the specified content is null.
	 */
	public final void output(Element element, XMLEventConsumer out) throws XMLStreamException {
		// If this is the root element we could pre-initialize the
		// namespace stack with the namespaces
		myProcessor.process(out, myFormat, myEventFactory, element);
		//out.flush();
	}

	/**
	 * This will handle printing out an <code>{@link
	 * Element}</code>'s content only, not including its tag, and attributes.
	 * This can be useful for printing the content of an element that contains
	 * HTML, like "&lt;description&gt;JDOM is
	 * &lt;b&gt;fun&gt;!&lt;/description&gt;".
	 * 
	 * @param element
	 *        <code>Element</code> to output.
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @throws XMLStreamException
	 *         - if there's any problem writing.
	 * @throws NullPointerException
	 *         if the specified content is null.
	 */
	public final void outputElementContent(Element element, XMLEventConsumer out)
			throws XMLStreamException {
		myProcessor.process(out, myFormat, myEventFactory, element.getContent());
		//out.flush();
	}

	/**
	 * This will handle printing out a list of nodes. This can be useful for
	 * printing the content of an element that contains HTML, like
	 * "&lt;description&gt;JDOM is &lt;b&gt;fun&gt;!&lt;/description&gt;".
	 * 
	 * @param list
	 *        <code>List</code> of nodes.
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @throws XMLStreamException
	 *         - if there's any problem writing.
	 * @throws NullPointerException
	 *         if the specified content is null.
	 */
	public final void output(List<? extends Content> list, XMLEventConsumer out)
			throws XMLStreamException {
		myProcessor.process(out, myFormat, myEventFactory, list);
		//out.flush();
	}

	/**
	 * Print out a <code>{@link CDATA}</code> node.
	 * 
	 * @param cdata
	 *        <code>CDATA</code> to output.
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @throws XMLStreamException
	 *         - if there's any problem writing.
	 * @throws NullPointerException
	 *         if the specified content is null.
	 */
	public final void output(CDATA cdata, XMLEventConsumer out) throws XMLStreamException {
		myProcessor.process(out, myFormat, myEventFactory, cdata);
		//out.flush();
	}

	/**
	 * Print out a <code>{@link Text}</code> node. Performs the necessary entity
	 * escaping and whitespace stripping.
	 * 
	 * @param text
	 *        <code>Text</code> to output.
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @throws XMLStreamException
	 *         - if there's any problem writing.
	 * @throws NullPointerException
	 *         if the specified content is null.
	 */
	public final void output(Text text, XMLEventConsumer out) throws XMLStreamException {
		myProcessor.process(out, myFormat, myEventFactory, text);
		//out.flush();
	}

	/**
	 * Print out a <code>{@link Comment}</code>.
	 * 
	 * @param comment
	 *        <code>Comment</code> to output.
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @throws XMLStreamException
	 *         - if there's any problem writing.
	 * @throws NullPointerException
	 *         if the specified content is null.
	 */
	public final void output(Comment comment, XMLEventConsumer out) throws XMLStreamException {
		myProcessor.process(out, myFormat, myEventFactory, comment);
		//out.flush();
	}

	/**
	 * Print out a <code>{@link ProcessingInstruction}</code>.
	 * 
	 * @param pi
	 *        <code>ProcessingInstruction</code> to output.
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @throws XMLStreamException
	 *         - if there's any problem writing.
	 * @throws NullPointerException
	 *         if the specified content is null.
	 */
	public final void output(ProcessingInstruction pi, XMLEventConsumer out)
			throws XMLStreamException {
		myProcessor.process(out, myFormat, myEventFactory, pi);
		//out.flush();
	}

	/**
	 * Print out an <code>{@link EntityRef}</code>.
	 * 
	 * @param entity
	 *        <code>EntityRef</code> to output.
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @throws XMLStreamException
	 *         - if there's any problem writing.
	 * @throws NullPointerException
	 *         if the specified content is null.
	 */
	public final void output(EntityRef entity, XMLEventConsumer out) throws XMLStreamException {
		myProcessor.process(out, myFormat, myEventFactory, entity);
		//out.flush();
	}

	/*
	 * ========================================================================
	 * Basic Support methods.
	 * ========================================================================
	 */

	/**
	 * Returns a cloned copy of this StAXStreamOutputter.
	 */
	@Override
	public StAXEventOutputter clone() {
		// Implementation notes: Since all state of an StAXStreamOutputter is
		// embodied in simple private instance variables, Object.clone
		// can be used. Note that since Object.clone is totally
		// broken, we must catch an exception that will never be
		// thrown.
		try {
			return (StAXEventOutputter) super.clone();
		} catch (java.lang.CloneNotSupportedException e) {
			// even though this should never ever happen, it's still
			// possible to fool Java into throwing a
			// CloneNotSupportedException. If that happens, we
			// shouldn't swallow it.
			throw new RuntimeException(e.toString());
		}
	}

	/**
	 * Return a string listing of the settings for this StAXStreamOutputter instance.
	 * 
	 * @return a string listing the settings for this StAXStreamOutputter instance
	 */
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("StAXStreamOutputter[omitDeclaration = ");
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
