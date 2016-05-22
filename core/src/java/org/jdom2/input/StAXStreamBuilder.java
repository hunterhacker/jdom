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

package org.jdom2.input;

import static javax.xml.stream.XMLStreamConstants.*;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.jdom2.AttributeType;
import org.jdom2.Content;
import org.jdom2.DefaultJDOMFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.JDOMFactory;
import org.jdom2.Namespace;
import org.jdom2.Verifier;
import org.jdom2.input.stax.DTDParser;
import org.jdom2.input.stax.StAXFilter;

/**
 * Builds a JDOM Document from a StAX-based XMLStreamReader.
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
public class StAXStreamBuilder {

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

			int state = stream.getEventType();

			if (START_DOCUMENT != state) {
				throw new JDOMException("JDOM requires that XMLStreamReaders " +
						"are at their beginning when being processed.");
			}

			final Document document = factory.document(null);

			while (state != END_DOCUMENT) {
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
						document.setDocType(DTDParser.parse(
								stream.getText(), factory));
						break;

					case START_ELEMENT:
						document.setRootElement(processElementFragment(factory, stream));
						break;

					case END_ELEMENT:
						throw new JDOMException("Unexpected XMLStream event at Document level: END_ELEMENT");
					case ENTITY_REFERENCE:
						throw new JDOMException("Unexpected XMLStream event at Document level: ENTITY_REFERENCE");
					case CDATA:
						throw new JDOMException("Unexpected XMLStream event at Document level: CDATA");
					case SPACE:
						// Can happen when XMLInputFactory2.P_REPORT_PROLOG_WHITESPACE is set to true
						document.addContent(factory.text(stream.getText()));
						break;
					case CHARACTERS:
						final String badtxt = stream.getText();
						if (!Verifier.isAllXMLWhitespace(badtxt)) {
							throw new JDOMException("Unexpected XMLStream event at Document level: CHARACTERS (" + badtxt + ")");
						}
						// otherwise ignore the chars.
						break;

					case COMMENT:
						document.addContent(
								factory.comment(stream.getText()));
						break;

					case PROCESSING_INSTRUCTION:
						document.addContent(factory.processingInstruction(
								stream.getPITarget(), stream.getPIData()));
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

	private List<Content> processFragments(JDOMFactory factory, XMLStreamReader stream, StAXFilter filter) throws JDOMException {
		
		int state = stream.getEventType();

		if (START_DOCUMENT != state) {
			throw new JDOMException("JDOM requires that XMLStreamReaders " +
					"are at their beginning when being processed.");
		}
		List<Content> ret = new ArrayList<Content>();
		
		int depth = 0;
		String text = null;
		
		try {
			while (stream.hasNext() && (state = stream.next()) != END_DOCUMENT) {
				switch (state) {
					case START_DOCUMENT:
						throw new JDOMException("Illegal state for XMLStreamReader. Cannot get XML Fragment for state START_DOCUMENT" );
					case END_DOCUMENT:
						throw new JDOMException("Illegal state for XMLStreamReader. Cannot get XML Fragment for state END_DOCUMENT" );
					case END_ELEMENT:
						throw new JDOMException("Illegal state for XMLStreamReader. Cannot get XML Fragment for state END_ELEMENT" );

					case START_ELEMENT:
						final QName qn = stream.getName();
						if (filter.includeElement(depth, qn.getLocalPart(), 
								Namespace.getNamespace(qn.getPrefix(), qn.getNamespaceURI()))) {
							ret.add(processPrunableElement(factory, stream, depth, filter));
						} else {
							final int back = depth;
							depth++;
							
							while (depth > back && stream.hasNext()) {
								state = stream.next();
								if (state == START_ELEMENT) {
									depth++;
								} else if (state == END_ELEMENT) {
									depth--;
								}
							}
						}
						break;

					case DTD:
						if (filter.includeDocType()) {
							ret.add(DTDParser.parse(stream.getText(), factory));
						}
						break;

					case CDATA:
						if ((text = filter.includeCDATA(depth, stream.getText())) != null) {
							ret.add(factory.cdata(text));
						}
						break;

					case SPACE:
					case CHARACTERS:
						if ((text = filter.includeText(depth, stream.getText())) != null) {
							ret.add(factory.text(text));
						}
						break;

					case COMMENT:
						if ((text = filter.includeComment(depth, stream.getText())) != null) {
							ret.add(factory.comment(text));
						}
						break;

					case ENTITY_REFERENCE:
						if (filter.includeEntityRef(depth, stream.getLocalName())) {
							ret.add(factory.entityRef(stream.getLocalName()));
						}
						break;

					case PROCESSING_INSTRUCTION:
						if (filter.includeProcessingInstruction(depth, stream.getPITarget())) {
							ret.add(factory.processingInstruction(
								stream.getPITarget(), stream.getPIData()));
						}
						break;

					default:
						throw new JDOMException("Unexpected XMLStream event " + stream.getEventType());
				}
			}
		} catch (XMLStreamException e) {
			throw new JDOMException("Unable to process fragments from XMLStreamReader.", e);
		}
		
		return ret;
	}

	
	private static final Element processPrunableElement(final JDOMFactory factory, 
			final XMLStreamReader reader, final int topdepth, StAXFilter filter) 
					throws XMLStreamException, JDOMException {

		if (START_ELEMENT != reader.getEventType()) {
			throw new JDOMException("JDOM requires that the XMLStreamReader " +
					"is at the START_ELEMENT state when retrieving an " +
					"Element Fragment.");
		}
		
		final Element fragment = processElement(factory, reader);
		Element current = fragment;
		int depth = topdepth + 1;
		String text = null;
		while (depth > topdepth && reader.hasNext()) {
			switch(reader.next()) {
				case START_ELEMENT:
					QName qn = reader.getName();
					if (!filter.pruneElement(depth, qn.getLocalPart(), 
							Namespace.getNamespace(
									qn.getPrefix(), qn.getNamespaceURI()))) {
						Element tmp = processElement(factory, reader);
						current.addContent(tmp);
						current = tmp;
						depth++;
					} else {
						final int edepth = depth;
						depth++;
						int state = 0;
						while (depth > edepth && reader.hasNext() && 
								(state = reader.next()) != END_DOCUMENT) {
							if (state == START_ELEMENT) {
								depth++;
							} else if (state == END_ELEMENT) {
								depth--;
							}
						}
					}
					break;
				case END_ELEMENT:
					current = current.getParentElement();
					depth--;
					break;
				case CDATA:
					if ((text = filter.pruneCDATA(depth, reader.getText())) != null) {
						current.addContent(factory.cdata(text));
					}
					break;

				case SPACE:
				case CHARACTERS:
					if ((text = filter.pruneText(depth, reader.getText())) != null) {
						current.addContent(factory.text(text));
					}
					break;

				case COMMENT:
					if ((text = filter.pruneComment(depth, reader.getText())) != null) {
						current.addContent(factory.comment(text));
					}
					break;

				case ENTITY_REFERENCE:
					if (!filter.pruneEntityRef(depth, reader.getLocalName())) {
						current.addContent(factory.entityRef(reader.getLocalName()));
					}
					break;

				case PROCESSING_INSTRUCTION:
					if (!filter.pruneProcessingInstruction(depth, reader.getPITarget())) {
						current.addContent(factory.processingInstruction(
								reader.getPITarget(), reader.getPIData()));
					}
					break;

				default:
					throw new JDOMException("Unexpected XMLStream event " + reader.getEventType());
			}
			
		}
		
		return fragment;
	}

	/**
	 * Create a Content from an XMLStreamReader
	 * The stream is advanced to the event after the current event (or to the
	 * event after the matching END_ELEMENT for an Element fragment).
	 * @param factory The {@link JDOMFactory} to use
	 * @param stream The XMLStreamReader to read from
	 * @return the parsed Document
	 * @throws JDOMException if there is any issue
	 * 				(XMLStreamExceptions are wrapped).
	 */
	private static final Content processFragment(final JDOMFactory factory, 
			final XMLStreamReader stream) throws JDOMException {
		try {
			
			switch (stream.getEventType()) {

				case START_DOCUMENT:
					throw new JDOMException("Illegal state for XMLStreamReader. Cannot get XML Fragment for state START_DOCUMENT" );
				case END_DOCUMENT:
					throw new JDOMException("Illegal state for XMLStreamReader. Cannot get XML Fragment for state END_DOCUMENT" );
				case END_ELEMENT:
					throw new JDOMException("Illegal state for XMLStreamReader. Cannot get XML Fragment for state END_ELEMENT" );

				case START_ELEMENT:
					Element emt = processElementFragment(factory, stream);
					stream.next();
					return emt;

				case DTD:
					Content dt = DTDParser.parse(stream.getText(), factory);
					stream.next();
					return dt;

				case CDATA:
					Content cd = factory.cdata(stream.getText());
					stream.next();
					return cd;

				case SPACE:
				case CHARACTERS:
					Content txt = factory.text(stream.getText());
					stream.next();
					return txt;

				case COMMENT:
					Content comment = factory.comment(stream.getText());
					stream.next();
					return comment;

				case ENTITY_REFERENCE:
					Content er = factory.entityRef(stream.getLocalName());
					stream.next();
					return er;

				case PROCESSING_INSTRUCTION:
					Content pi = factory.processingInstruction(
							stream.getPITarget(), stream.getPIData());
					stream.next();
					return pi;

				default:
					throw new JDOMException("Unexpected XMLStream event " + stream.getEventType());

			}
		} catch (final XMLStreamException xse) {
			throw new JDOMException("Unable to process XMLStream. See Cause.", xse);
		}
	}

	private static final Element processElementFragment(final JDOMFactory factory, 
			final XMLStreamReader reader) throws XMLStreamException, JDOMException {

		if (START_ELEMENT != reader.getEventType()) {
			throw new JDOMException("JDOM requires that the XMLStreamReader " +
					"is at the START_ELEMENT state when retrieving an " +
					"Element Fragment.");
		}
		
		final Element fragment = processElement(factory, reader);
		Element current = fragment;
		int depth = 1;
		while (depth > 0 && reader.hasNext()) {
			switch(reader.next()) {
				case START_ELEMENT:
					Element tmp = processElement(factory, reader);
					current.addContent(tmp);
					current = tmp;
					depth++;
					break;
				case END_ELEMENT:
					current = current.getParentElement();
					depth--;
					break;
				case CDATA:
					current.addContent(factory.cdata(reader.getText()));
					break;

				case SPACE:
				case CHARACTERS:
					current.addContent(factory.text(reader.getText()));
					break;

				case COMMENT:
					current.addContent(factory.comment(reader.getText()));
					break;

				case ENTITY_REFERENCE:
					current.addContent(factory.entityRef(reader.getLocalName()));
					break;

				case PROCESSING_INSTRUCTION:
					current.addContent(factory.processingInstruction(
							reader.getPITarget(), reader.getPIData()));
					break;

				default:
					throw new JDOMException("Unexpected XMLStream event " + reader.getEventType());
			}
			
		}
		
		return fragment;
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


	/** The factory to use for parsing */
	private JDOMFactory builderfactory = new DefaultJDOMFactory();

	/**
	 * Returns the current {@link org.jdom2.JDOMFactory} in use.
	 * @return the factory in use
	 */
	public JDOMFactory getFactory() {
		return builderfactory;
	}

	/**
	 * This sets a custom JDOMFactory for the builder.  Use this to build
	 * the tree with your own subclasses of the JDOM classes.
	 *
	 * @param factory <code>JDOMFactory</code> to use
	 */
	public void setFactory(JDOMFactory factory) {
		this.builderfactory = factory;
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
		return process(builderfactory, reader);
	}
	
	/**
	 * Read the entire XMLStreamReader and from it build a list of Content that
	 * conforms to the rules in the supplied StAXFilter.
	 * @param reader The XMLStreamReader to parse
	 * @param filter The Filter to use for the Content
	 * @return a List of Content that were identified by the supplied filter
	 * @throws JDOMException if there was a parsing problem.
	 */
	public List<Content> buildFragments(XMLStreamReader reader, StAXFilter filter) throws JDOMException {
		return processFragments(builderfactory, reader, filter);
	}

	
	/**
	 * Read the current XML Fragment from the XMLStreamReader.
	 * The XMLStreamReader must be at some 'content' state, it cannot be
	 * at START_DOCUMENT, for example.
	 * @param reader The XMLStreamReader to read the next fragment from
	 * @return The JDOM fragment at the current position in the reader
	 * @throws JDOMException if there is an issue with the state of the
	 * XMLStreamReader or some other issue with the processing.
	 */
	public Content fragment(XMLStreamReader reader) throws JDOMException {
		return processFragment(builderfactory, reader);
	}

}
