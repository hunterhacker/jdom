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

package org.jdom2.output.support;

import java.util.List;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.util.XMLEventConsumer;
import javax.xml.stream.XMLStreamException;

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
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * This interface provides a base support for the {@link XMLOutputter}.
 * <p>
 * People who want to create a custom XMLOutputProcessor for XMLOutputter are
 * able to implement this interface with the following notes and restrictions:
 * <ol>
 * <li>The XMLOutputter will call one, and only one of the <code>process(XMLEventConsumer,Format,*)</code> methods each
 * time the XMLOutputter is requested to output some JDOM content. It is thus
 * safe to assume that a <code>process(XMLEventConsumer,Format,*)</code> method can set up any
 * infrastructure needed to process the content, and that the XMLOutputter will
 * not re-call that method, or some other <code>process(XMLEventConsumer,Format,*)</code> method for the same output
 * sequence.
 * <li>The process methods should be thread-safe and reentrant: The same
 * <code>process(XMLEventConsumer,Format,*)</code> method may (will) be called concurrently from different threads.
 * </ol>
 * <p>
 * The {@link AbstractXMLOutputProcessor} class is a full implementation of this
 * interface and is fully customisable. People who want a custom XMLOutputter
 * are encouraged to extend the AbstractXMLOutputProcessor rather than do a full
 * re-implementation of this interface.
 * 
 * @see XMLOutputter
 * @see AbstractXMLOutputProcessor
 * @since JDOM2
 * @author Rolf Lear
 */
public interface StAXEventProcessor {

	/**
	 * This will print the <code>{@link Document}</code> to the given XMLEventConsumer.
	 * <p>
	 * Warning: using your own XMLEventConsumer may cause the outputter's preferred
	 * character encoding to be ignored. If you use encodings other than UTF-8,
	 * we recommend using the method that takes an OutputStream instead.
	 * </p>
	 * 
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @param format
	 *        <code>Format</code> instance specifying output style
	 * @param eventfactory
	 * 		  <code>XMLEventFactory</code> for creating XMLEvent instances.
	 * @param doc
	 *        <code>Document</code> to format.
	 * @throws XMLStreamException
	 *         if there's any problem writing.
	 * @throws NullPointerException
	 *         if the input content is null
	 */
	public abstract void process(XMLEventConsumer out, Format format, XMLEventFactory eventfactory, Document doc) throws XMLStreamException;

	/**
	 * Print out the <code>{@link DocType}</code>.
	 * 
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @param format
	 *        <code>Format</code> instance specifying output style
	 * @param eventfactory
	 * 		  <code>XMLEventFactory</code> for creating XMLEvent instances.
	 * @param doctype
	 *        <code>DocType</code> to output.
	 * @throws XMLStreamException
	 *         if there's any problem writing.
	 * @throws NullPointerException
	 *         if the input content is null
	 */
	public abstract void process(XMLEventConsumer out, Format format, XMLEventFactory eventfactory, DocType doctype) throws XMLStreamException;

	/**
	 * Print out an <code>{@link Element}</code>, including its
	 * <code>{@link Attribute}</code>s, and all contained (child) elements, etc.
	 * 
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @param format
	 *        <code>Format</code> instance specifying output style
	 * @param eventfactory
	 * 		  <code>XMLEventFactory</code> for creating XMLEvent instances.
	 * @param element
	 *        <code>Element</code> to output.
	 * @throws XMLStreamException
	 *         if there's any problem writing.
	 * @throws NullPointerException
	 *         if the input content is null
	 */
	public abstract void process(XMLEventConsumer out, Format format, XMLEventFactory eventfactory, Element element) throws XMLStreamException;

	/**
	 * This will handle printing out a list of nodes. This can be useful for
	 * printing the content of an element that contains HTML, like
	 * "&lt;description&gt;JDOM is &lt;b&gt;fun&gt;!&lt;/description&gt;".
	 * 
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @param format
	 *        <code>Format</code> instance specifying output style
	 * @param eventfactory
	 * 		  <code>XMLEventFactory</code> for creating XMLEvent instances.
	 * @param list
	 *        <code>List</code> of nodes.
	 * @throws XMLStreamException
	 *         if there's any problem writing.
	 * @throws NullPointerException
	 *         if the input list is null or contains null members
	 * @throws ClassCastException
	 *         if any of the list members are not {@link Content}
	 */
	public abstract void process(XMLEventConsumer out, Format format, XMLEventFactory eventfactory, List<? extends Content> list)
			throws XMLStreamException;

	/**
	 * Print out a <code>{@link CDATA}</code> node.
	 * 
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @param format
	 *        <code>Format</code> instance specifying output style
	 * @param eventfactpry
	 * 		  <code>XMLEventFactory</code> for creating XMLEvent instances.
	 * @param cdata
	 *        <code>CDATA</code> to output.
	 * @throws XMLStreamException
	 *         if there's any problem writing.
	 * @throws NullPointerException
	 *         if the input content is null
	 */
	public abstract void process(XMLEventConsumer out, Format format, XMLEventFactory eventfactpry, CDATA cdata) throws XMLStreamException;

	/**
	 * Print out a <code>{@link Text}</code> node. Performs the necessary entity
	 * escaping and whitespace stripping.
	 * 
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @param format
	 *        <code>Format</code> instance specifying output style
	 * @param eventfactory 
	 * 		  <code>XMLEventFactory</code> for creating XMLEvent instances.
	 * @param text
	 *        <code>Text</code> to output.
	 * @throws XMLStreamException
	 *         if there's any problem writing.
	 * @throws NullPointerException
	 *         if the input content is null
	 */
	public abstract void process(XMLEventConsumer out, Format format, XMLEventFactory eventfactory, Text text) throws XMLStreamException;

	/**
	 * Print out a <code>{@link Comment}</code>.
	 * 
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @param format
	 *        <code>Format</code> instance specifying output style
	 * @param eventfactory 
	 * 		  <code>XMLEventFactory</code> for creating XMLEvent instances.
	 * @param comment
	 *        <code>Comment</code> to output.
	 * @throws XMLStreamException
	 *         if there's any problem writing.
	 * @throws NullPointerException
	 *         if the input content is null
	 */
	public abstract void process(XMLEventConsumer out, Format format, XMLEventFactory eventfactory, Comment comment) throws XMLStreamException;

	/**
	 * Print out a <code>{@link ProcessingInstruction}</code>.
	 * 
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @param format
	 *        <code>Format</code> instance specifying output style
	 * @param eventfactory 
	 * 		  <code>XMLEventFactory</code> for creating XMLEvent instances.
	 * @param pi
	 *        <code>ProcessingInstruction</code> to output.
	 * @throws XMLStreamException
	 *         if there's any problem writing.
	 * @throws NullPointerException
	 *         if the input content is null
	 */
	public abstract void process(XMLEventConsumer out, Format format, XMLEventFactory eventfactory, ProcessingInstruction pi)
			throws XMLStreamException;

	/**
	 * Print out a <code>{@link EntityRef}</code>.
	 * 
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @param format
	 *        <code>Format</code> instance specifying output style
	 * @param eventfactory
	 * 		  <code>XMLEventFactory</code> for creating XMLEvent instances.
	 * @param entity
	 *        <code>EntityRef</code> to output.
	 * @throws XMLStreamException
	 *         if there's any problem writing.
	 * @throws NullPointerException
	 *         if the input content is null
	 */
	public abstract void process(XMLEventConsumer out, Format format, XMLEventFactory eventfactory, EntityRef entity) throws XMLStreamException;

}