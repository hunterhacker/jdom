/*--

 Copyright (C) 2000-2011 Jason Hunter & Brett McLaughlin.
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

package org.jdom2.input;

import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.jdom2.AttributeType;
import org.jdom2.Comment;
import org.jdom2.DefaultJDOMFactory;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.JDOMFactory;
import org.jdom2.Namespace;
import org.jdom2.ProcessingInstruction;

/**
 * Builds a JDOM document from a StAX-based XMLStremReader.
 * <p>
 * XMLStreamReaders are pre-configured and as a result JDOM is not able to
 * alter whether the input is validated, or whether the Stream has escaped
 * entities or not. These (and other) characteristics are configurable by
 * setting the correct features and properties on the XMLInputFactory when it
 * is used to create the XMLStreamReader.
 * <p>
 * Useful configuration to set, or know about is:
 * <ul>
 * <li>StAX streams seldom differentiate between Text and CDATA content. You
 * will likely want to configure your StAX factory (XMLInputFactory) with
 * <code>http://java.sun.com/xml/stream/properties/report-cdata-event</code>
 * for the default Java StAX implementation, or the equivalent property for your
 * StAX engine.
 * <li>The remaining XMLInputFactory settings are likely to work fine at their
 * default values.
 * <li>StAX is not likely to be your best option if you want a validating
 * parser, at least not with the default (built-in Java implementation in Java6 
 * which does not support it). Consider a SAX parser.
 * </ul>
 * <p>
 * From a JDOM perspective XMLStreamReaders are more efficient than 
 * XMLEventReaders. Where possible use an XMLStreamReader.
 * <p>
 * If you happen to be looking at the source code, pay careful attention to the
 * imports so you know what type of instance is being processed, whether it is
 * a StAX class, or a JDOM class, because there are name conflicts.
 * 
 * @author Rolf Lear
 *
 */
public class StAXBuilder implements XMLStreamConstants {
	
	/**
	 * Create a Document from an XMLStreamReader
	 * @param factory The {@link JDOMFactory} to use
	 * @param stream The XMLStreamReader to read from
	 * @return the parsed Document
	 * @throws JDOMException if there is any issue
	 * 				(XMLStreamExceptions are wrapped).
	 */
	private static final Document process(final JDOMFactory factory, 
			final XMLStreamReader stream) throws JDOMException {
		try {

			final Document document = factory.document(null);

			Element current = null;

			int state = stream.getEventType();

			if (XMLStreamConstants.START_DOCUMENT != state) {
				throw new JDOMException("JDOM requires that XMLStreamReaders " +
						"are at their beginning when being processed.");
			}


			while (state != XMLStreamConstants.END_DOCUMENT) {
				switch (state) {

					case START_DOCUMENT:
						// for the <?xml version="..." standalone=".."?>
						document.setBaseURI(stream.getLocation().getSystemId());
						document.setProperty("ENCODING_SCHEME",
								stream.getCharacterEncodingScheme());
						document.setProperty("STANDALONE",
								String.valueOf(stream.isStandalone()));
						document.setProperty("ENCODING",
								stream.getEncoding());
						break;
						
					case DTD:
						final DocType dtype = DTDParser.parse(
								stream.getText(), factory);
						document.setDocType(dtype);
						break;

					case START_ELEMENT:
						final Element emt = processElement(factory, stream);
						if (current == null) {
							document.setRootElement(emt);
							final DocType dt = document.getDocType();
							if (dt != null) {
								dt.setElementName(emt.getName());
							}
						} else {
							current.addContent(emt);
						}
						current = emt;
						break;

					case END_ELEMENT:
						current = current.getParentElement();
						break;

					case CDATA:
						if (current != null) {
							current.addContent(factory.cdata(stream.getText()));
						}
						break;

					case SPACE:
					case CHARACTERS:
						if (current != null) {
							current.addContent(factory.text(stream.getText()));
						}
						break;

					case COMMENT:
						if (current == null) {
							document.addContent(
									factory.comment(stream.getText()));
						} else {
							current.addContent(
									factory.comment(stream.getText()));
						}
						break;

					case ENTITY_REFERENCE:
						if (current != null) {
							current.addContent(
									factory.entityRef(stream.getLocalName()));
						}
						break;

					case PROCESSING_INSTRUCTION:
						if (current == null) {
							document.addContent(factory.processingInstruction(
									stream.getPITarget(), stream.getPIData()));
						} else {
							current.addContent(factory.processingInstruction(
									stream.getPITarget(), stream.getPIData()));
						}
						break;

					default:
						throw new JDOMException("Unexpected XMLStream event " + state);

				}
				if (stream.hasNext()) {
					state = stream.next();
				} else {
					throw new JDOMException("Unexpected end-of-XMLStreamReader");
				}
			}
			return document;
		} catch (final XMLStreamException xse) {
			throw new JDOMException("Unable to process XMLStream. See Cause.", xse);
		}
	}

	private static final Element processElement(final JDOMFactory factory, 
			final XMLStreamReader reader) {

		final Element element = factory.element(reader.getLocalName(),
				Namespace.getNamespace(reader.getPrefix(), 
						reader.getNamespaceURI()));

		// Handle attributes
		for (int i=0, len=reader.getAttributeCount(); i<len; i++) {
			factory.setAttribute(element, factory.attribute(
					reader.getAttributeLocalName(i),
					reader.getAttributeValue(i), 
					AttributeType.getAttributeType(reader.getAttributeType(i)),
					Namespace.getNamespace(reader.getAttributePrefix(i),
										   reader.getAttributeNamespace(i))));
		}

		// Handle Namespaces
		for (int i = 0, len = reader.getNamespaceCount(); i < len; i++) {
			element.addNamespaceDeclaration(Namespace.getNamespace(
					reader.getNamespacePrefix(i), reader.getNamespaceURI(i)));
		}

		return element;
	}


	/**
	 * Create a Document from an XMLEventReader
	 * @param factory the {@link JDOMFactory} to use
	 * @param stream the XMLEventReader to read from
	 * @return the parsed Document
	 * @throws JDOMException if there is any issue
	 * 				(XMLStreamExceptions are wrapped).
	 */
	private static final Document process(final JDOMFactory factory, 
			final XMLEventReader events) throws JDOMException {
		try {

			final Document document = factory.document(null);
			Element current = null;

			XMLEvent event = events.peek();

			if (XMLStreamConstants.START_DOCUMENT != event.getEventType()) {
				throw new JDOMException("JDOM requires that XMLStreamReaders " +
						"are at their beginning when being processed.");
			}



			while (event.getEventType() != XMLStreamConstants.END_DOCUMENT) {
				if (event.isStartDocument()) {
					document.setBaseURI(event.getLocation().getSystemId());
					document.setProperty("ENCODING_SCHEME",
							((javax.xml.stream.events.StartDocument)event).getCharacterEncodingScheme());
					document.setProperty("STANDALONE", String.valueOf(
							((javax.xml.stream.events.StartDocument)event).isStandalone()));
					//						document.setProperty("ENCODING",
					//								((StartDocument)event).getEncoding());
				} else if (event instanceof javax.xml.stream.events.DTD) {
					//List<?> list = (List<?>)reader.getProperty("javax.xml.stream.entities");
					//System.out.println(list);
					final DocType dtype = DTDParser.parse(((javax.xml.stream.events.DTD)event).getDocumentTypeDeclaration(), factory);
					document.setDocType(dtype);
				} else if (event.isStartElement()) {
					final Element emt = processElement(factory, event.asStartElement());
					if (current == null) {
						document.setRootElement(emt);
						final DocType dt = document.getDocType();
						if (dt != null) {
							dt.setElementName(emt.getName());
						}
					} else {
						current.addContent(emt);
					}
					current = emt;
				} else if (event.isCharacters()) {
					final Characters chars = event.asCharacters();
					if (chars.isCData()) {
						current.addContent(factory.cdata(
								((Characters)event).getData()));
					} else {
						current.addContent(factory.text(
								((Characters)event).getData()));
					}
				} else if (event instanceof javax.xml.stream.events.Comment) {
					final Comment comment = factory.comment(
							((javax.xml.stream.events.Comment)event).getText());
					if (current == null) {
						document.addContent(comment);
					} else {
						current.addContent(comment);
					}
				} else if (event.isEntityReference()) {
					current.addContent(factory.entityRef(
							((javax.xml.stream.events.EntityReference)event).getName()));
				} else if (event.isProcessingInstruction()) {
					final ProcessingInstruction pi = factory.processingInstruction(
							((javax.xml.stream.events.ProcessingInstruction)event).getTarget(),
							((javax.xml.stream.events.ProcessingInstruction)event).getData());
					if (current == null) {
						document.addContent(pi);
					} else {
						current.addContent(pi);
					}
				} else if (event.isEndElement()) {
					current = current.getParentElement();
				}
				if (events.hasNext()) {
					event = events.nextEvent();
				} else {
					break;
				}
			}
			return document;
		} catch (final XMLStreamException xse) {
			throw new JDOMException("Unable to process XMLStream. See Cause.", xse);
		}
	}

	private static final Element processElement(final JDOMFactory factory,
			final StartElement event) {
		final QName qname = event.getName();

		final Element element = factory.element(qname.getLocalPart(),
				Namespace.getNamespace(qname.getPrefix(), qname.getNamespaceURI()));

		// Handle attributes
		for (final Iterator<?> it = event.getAttributes();
				it.hasNext(); ) {

			final javax.xml.stream.events.Attribute att =
					(javax.xml.stream.events.Attribute)it.next();

			final QName aqname = att.getName();

			final Namespace attNs = Namespace.getNamespace(aqname.getPrefix(),
					aqname.getNamespaceURI());

			factory.setAttribute(element, factory.attribute(
					aqname.getLocalPart(), att.getValue(),
					AttributeType.getAttributeType(att.getDTDType()), attNs));
		}

		for (final Iterator<?> it = event.getNamespaces(); it.hasNext();) {
			final javax.xml.stream.events.Namespace ns =
					(javax.xml.stream.events.Namespace)it.next();

			element.addNamespaceDeclaration(Namespace.getNamespace(
					ns.getPrefix(), ns.getNamespaceURI()));
		}

		return element;
	}

	
	
	/** The factory to use for parsing */
	private JDOMFactory factory = new DefaultJDOMFactory();
	
	/**
	 * Returns the current {@link org.jdom2.JDOMFactory} in use.
	 * @return the factory in use
	 */
	public JDOMFactory getFactory() {
		return factory;
	}

	/**
	 * This sets a custom JDOMFactory for the builder.  Use this to build
	 * the tree with your own subclasses of the JDOM classes.
	 *
	 * @param factory <code>JDOMFactory</code> to use
	 */
	public void setFactory(JDOMFactory factory) {
		this.factory = factory;
	}
	
	/**
	 * This builds a document from the supplied
	 * XMLStreamReader.
	 * <p>
	 * The JDOMContent will be built by the current JDOMFactory.
	 *
	 * @param reader <code>XMLStreamReader</code> to read from
	 * @return <code>Document</code> resultant Document object
	 * @throws JDOMException when errors occur in parsing
	 */
	public Document build(XMLStreamReader reader) throws JDOMException {
		return process(factory, reader);
	}
	
	/**
	 * This builds a document from the supplied
	 * XMLEventReader.
	 * <p>
	 * The JDOMContent will be built by the current JDOMFactory.
	 *
	 * @param events <code>XMLEventReader</code> to read from
	 * @return <code>Document</code> resultant Document object
	 * @throws JDOMException when errors occur in parsing
	 */
	public Document build(XMLEventReader events) throws JDOMException {
		return process(factory, events);
	}
	
}
