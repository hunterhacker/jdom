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

import static org.jdom2.JDOMConstants.*;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import org.jdom2.Attribute;
import org.jdom2.AttributeType;
import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.Content;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.EntityRef;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;
import org.jdom2.output.Format;
import org.jdom2.output.Format.TextMode;
import org.jdom2.output.XMLOutputter;
import org.jdom2.util.NamespaceStack;

/**
 * Outputs a JDOM document as a stream of SAX2 events.
 * <p>
 * Most ContentHandler callbacks are supported. Neither
 * <code>ignorableWhitespace()</code> nor <code>skippedEntity()</code> have been
 * implemented.
 * <p>
 * At this time, it is not possible to access notations and unparsed entity
 * references in a DTD from JDOM. Therefore, full <code>DTDHandler</code>
 * call-backs have not been implemented yet.
 * <p>
 * The <code>ErrorHandler</code> call-backs have not been implemented, since
 * these are supposed to be invoked when the document is parsed and at this
 * point the document exists in memory and is known to have no errors.
 * </p>
 * The SAX2 API does not support whitespace formatting outside the root element.
 * As a consequence any Formatting options that would normally affect the
 * structures outside the root element will be ignored.
 * 
 * @author Brett McLaughlin
 * @author Jason Hunter
 * @author Fred Trimble
 * @author Bradley S. Huffman
 * @author Rolf Lear
 */
public class AbstractSAXOutputProcessor extends AbstractOutputProcessor
		implements SAXOutputProcessor {

	private static void locate(SAXTarget out) {
		out.getContentHandler().setDocumentLocator(out.getLocator());
	}

	@Override
	public void process(SAXTarget out, Format format, Document doc)
			throws JDOMException {
		try {
			locate(out);
			printDocument(out, new FormatStack(format), new NamespaceStack(),
					doc);
		} catch (SAXException se) {
			throw new JDOMException(
					"Encountered a SAX exception processing the Document: ", se);
		}
	}

	@Override
	public void process(SAXTarget out, Format format, DocType doctype)
			throws JDOMException {
		try {
			locate(out);
			printDocType(out, new FormatStack(format), doctype);
		} catch (SAXException se) {
			throw new JDOMException(
					"Encountered a SAX exception processing the DocType: ", se);
		}
	}

	@Override
	public void process(SAXTarget out, Format format, Element element)
			throws JDOMException {
		try {
			locate(out);
			printElement(out, new FormatStack(format), new NamespaceStack(),
					element);
		} catch (SAXException se) {
			throw new JDOMException(
					"Encountered a SAX exception processing the Element: ", se);
		}
	}

	@Override
	public void process(SAXTarget out, Format format,
			List<? extends Content> list) throws JDOMException {
		try {
			locate(out);
			final FormatStack fstack = new FormatStack(format);
			final Walker walker = buildWalker(fstack, list, false);
			printContent(out, fstack, new NamespaceStack(), walker);
		} catch (SAXException se) {
			throw new JDOMException(
					"Encountered a SAX exception processing the List: ", se);
		}
	}

	@Override
	public void process(SAXTarget out, Format format, CDATA cdata)
			throws JDOMException {
		try {
			locate(out);
			final List<CDATA> list = Collections.singletonList(cdata);
			final FormatStack fstack = new FormatStack(format);
			final Walker walker = buildWalker(fstack, list, false);
			printContent(out, fstack, new NamespaceStack(), walker);
		} catch (SAXException se) {
			throw new JDOMException(
					"Encountered a SAX exception processing the CDATA: ", se);
		}
	}

	@Override
	public void process(SAXTarget out, Format format, Text text)
			throws JDOMException {
		try {
			locate(out);
			final List<Text> list = Collections.singletonList(text);
			final FormatStack fstack = new FormatStack(format);
			final Walker walker = buildWalker(fstack, list, false);
			printContent(out, fstack, new NamespaceStack(), walker);
		} catch (SAXException se) {
			throw new JDOMException(
					"Encountered a SAX exception processing the Text: ", se);
		}
	}

	@Override
	public void process(SAXTarget out, Format format, Comment comment)
			throws JDOMException {
		try {
			locate(out);
			printComment(out, new FormatStack(format), comment);
		} catch (SAXException se) {
			throw new JDOMException(
					"Encountered a SAX exception processing the Comment: ", se);
		}
	}

	@Override
	public void process(SAXTarget out, Format format, ProcessingInstruction pi)
			throws JDOMException {
		try {
			locate(out);
			printProcessingInstruction(out, new FormatStack(format), pi);
		} catch (SAXException se) {
			throw new JDOMException(
					"Encountered a SAX exception processing the ProcessingInstruction: ",
					se);
		}
	}

	@Override
	public void process(SAXTarget out, Format format, EntityRef entity)
			throws JDOMException {
		try {
			locate(out);
			printEntityRef(out, new FormatStack(format), entity);
		} catch (SAXException se) {
			throw new JDOMException(
					"Encountered a SAX exception processing the EntityRef: ",
					se);
		}
	}

	@Override
	public void processAsDocument(SAXTarget out, Format format,
			List<? extends Content> nodes) throws JDOMException {
		try {
			if ((nodes == null) || (nodes.size() == 0)) {
				return;
			}

			locate(out);
			// contentHandler.setDocumentLocator()
			out.getContentHandler().startDocument();
			
			FormatStack fstack = new FormatStack(format);
			
			// Fire DTD events .. if there is a DocType node
			if (out.isReportDTDEvents()) {
				for (Content c : nodes) {
					if (c instanceof DocType) {
						printDocType(out, fstack, (DocType)c);
						// fire only the first DocType's events
						// subsequent ones are ignored.
						break;
					}
				}
			}

			Walker walker = buildWalker(fstack, nodes, false);

			printContent(out, fstack, new NamespaceStack(), walker);

			// contentHandler.endDocument()
			out.getContentHandler().endDocument();
		} catch (SAXException se) {
			throw new JDOMException(
					"Encountered a SAX exception processing the List: ", se);
		}
	}

	@Override
	public void processAsDocument(SAXTarget out, Format format, Element node)
			throws JDOMException {
		try {
			if (node == null) {
				return;
			}

			locate(out);
			// contentHandler.setDocumentLocator()
			out.getContentHandler().startDocument();

			printElement(out, new FormatStack(format), new NamespaceStack(),
					node);

			// contentHandler.endDocument()
			out.getContentHandler().endDocument();
		} catch (SAXException se) {
			throw new JDOMException(
					"Encountered a SAX exception processing the Element: ", se);
		}
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
	 *        <code>SAXTarget</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param nstack
	 *        the NamespaceStack
	 * @param document
	 *        <code>Document</code> to write.
	 * @throws SAXException
	 *         if the destination SAXTarget fails
	 */
	protected void printDocument(final SAXTarget out, final FormatStack fstack,
			final NamespaceStack nstack, final Document document)
			throws SAXException {
		if (document == null) {
			return;
		}

		// contentHandler.startDocument()
		out.getContentHandler().startDocument();

		// Fire DTD events
		if (out.isReportDTDEvents()) {
			printDocType(out, fstack, document.getDocType());
		}

		// Handle root element, as well as any root level
		// processing instructions and comments
		// ignore DocType, if any.
		final int sz = document.getContentSize();
		
		if (sz > 0) {
			for (int i = 0; i < sz; i++) {
				final Content c = document.getContent(i);
				out.getLocator().setNode(c);
				switch (c.getCType()) {
					case Comment :
						printComment(out, fstack, (Comment) c);
						break;
					case DocType :
						// cannot simply add a DocType to a SAX stream
						// it is added when the Stream is created.
						break;
					case Element :
						printElement(out, fstack, nstack, (Element)c);
						break;
					case ProcessingInstruction :
						printProcessingInstruction(out, fstack,  
								(ProcessingInstruction)c);
						break;
					default :
						// do nothing.
				}
			}
		}
		
		// contentHandler.endDocument()
		out.getContentHandler().endDocument();

	}

	/**
	 * This will handle printing of a {@link DocType}.
	 * 
	 * @param out
	 *        <code>SAXTarget</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param docType
	 *        <code>DocType</code> to write.
	 * @throws SAXException
	 *         if the destination SAXTarget fails
	 */
	protected void printDocType(final SAXTarget out, final FormatStack fstack,
			final DocType docType) throws SAXException {

		// Fire DTD-related events only if handlers have been registered.
		final DTDHandler dtdHandler = out.getDTDHandler();
		final DeclHandler declHandler = out.getDeclHandler();
		if ((docType != null)
				&& ((dtdHandler != null) || (declHandler != null))) {

			// Build a dummy XML document that only references the DTD...
			String dtdDoc = new XMLOutputter().outputString(docType);

			try {
				// And parse it to fire DTD events.
				createDTDParser(out).parse(
						new InputSource(new StringReader(dtdDoc)));

				// We should never reach this point as the document is
				// ill-formed; it does not have any root element.
			} catch (SAXParseException e) {
				// Expected exception: There's no root element in document.
			} catch (IOException e) {
				throw new SAXException("DTD parsing error", e);
			}
		}

	}

	/**
	 * This will handle printing of a {@link ProcessingInstruction}.
	 * 
	 * @param out
	 *        <code>SAXTarget</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param pi
	 *        <code>ProcessingInstruction</code> to write.
	 * @throws SAXException
	 *         if the destination SAXTarget fails
	 */
	protected void printProcessingInstruction(final SAXTarget out,
			final FormatStack fstack, final ProcessingInstruction pi)
			throws SAXException {
		out.getContentHandler().processingInstruction(pi.getTarget(),
				pi.getData());
	}

	/**
	 * This will handle printing of a {@link Comment}.
	 * 
	 * @param out
	 *        <code>SAXTarget</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param comment
	 *        <code>Comment</code> to write.
	 * @throws SAXException
	 *         if the destination SAXTarget fails
	 */
	protected void printComment(final SAXTarget out, final FormatStack fstack,
			final Comment comment) throws SAXException {
		if (out.getLexicalHandler() != null) {
			char[] c = comment.getText().toCharArray();
			out.getLexicalHandler().comment(c, 0, c.length);
		}
	}

	/**
	 * This will handle printing of an {@link EntityRef}.
	 * 
	 * @param out
	 *        <code>SAXTarget</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param entity
	 *        <code>EntotyRef</code> to write.
	 * @throws SAXException
	 *         if the destination SAXTarget fails
	 */
	protected void printEntityRef(final SAXTarget out,
			final FormatStack fstack, final EntityRef entity)
			throws SAXException {
		out.getContentHandler().skippedEntity(entity.getName());
	}

	/**
	 * This will handle printing of a {@link CDATA}.
	 * 
	 * @param out
	 *        <code>SAXTarget</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param cdata
	 *        <code>CDATA</code> to write.
	 * @throws SAXException
	 *         if the destination SAXTarget fails
	 */
	protected void printCDATA(final SAXTarget out, final FormatStack fstack,
			final CDATA cdata) throws SAXException {
		// CDATAs are treated like text, not indented/newline content.
		final LexicalHandler lexicalHandler = out.getLexicalHandler();
		final char[] chars = cdata.getText().toCharArray();
		if (lexicalHandler != null) {
			lexicalHandler.startCDATA();
			out.getContentHandler().characters(chars, 0, chars.length);
			lexicalHandler.endCDATA();
		} else {
			out.getContentHandler().characters(chars, 0, chars.length);
		}
	}

	/**
	 * This will handle printing of a {@link Text}.
	 * 
	 * @param out
	 *        <code>SAXTarget</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param text
	 *        <code>Text</code> to write.
	 * @throws SAXException
	 *         if the destination SAXTarget fails
	 */
	protected void printText(final SAXTarget out, final FormatStack fstack,
			final Text text) throws SAXException {
		final char[] chars = text.getText().toCharArray();
		out.getContentHandler().characters(chars, 0, chars.length);
	}

	/**
	 * This will handle printing of an {@link Element}.
	 * <p>
	 * This method arranges for outputting the Element infrastructure including
	 * Namespace Declarations and Attributes.
	 * 
	 * @param out
	 *        <code>SAXTarget</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param nstack
	 *        the NamespaceStack
	 * @param element
	 *        <code>Element</code> to write.
	 * @throws SAXException
	 *         if the destination SAXTarget fails
	 */
	protected void printElement(final SAXTarget out, final FormatStack fstack,
			final NamespaceStack nstack, final Element element)
			throws SAXException {

		final ContentHandler ch = out.getContentHandler();
		final Object origloc = out.getLocator().getNode();
		nstack.push(element);
		try {

			// update locator
			out.getLocator().setNode(element);

			AttributesImpl atts = new AttributesImpl();

			// contentHandler.startPrefixMapping()
			for (Namespace ns : nstack.addedForward()) {
				ch.startPrefixMapping(ns.getPrefix(), ns.getURI());
				if (out.isDeclareNamespaces()) {
					// add a physical attribute if requested.
					String prefix = ns.getPrefix();
					if (prefix.equals("")) {
						atts.addAttribute("", "", "xmlns", "CDATA", ns.getURI());
					} else {
						atts.addAttribute("", "", "xmlns:" + ns.getPrefix(),
								"CDATA", ns.getURI());
					}
				}
			}

			// Allocate attribute list.
			if (element.hasAttributes()) {
				for (Attribute a : element.getAttributes()) {
					if (!a.isSpecified() && fstack.isSpecifiedAttributesOnly()) {
						continue;
					}
					atts.addAttribute(a.getNamespaceURI(), a.getName(),
							a.getQualifiedName(),
							getAttributeTypeName(a.getAttributeType()),
							a.getValue());
				}
			}

			// contentHandler.startElement()
			ch.startElement(element.getNamespaceURI(), element.getName(),
					element.getQualifiedName(), atts);

			final List<Content> content = element.getContent();

			// OK, now we print out the meat of the Element
			if (!content.isEmpty()) {
				TextMode textmode = fstack.getTextMode();

				// Check for xml:space and adjust format settings
				final String space = element.getAttributeValue("space",
						Namespace.XML_NAMESPACE);

				if ("default".equals(space)) {
					textmode = fstack.getDefaultMode();
				} else if ("preserve".equals(space)) {
					textmode = TextMode.PRESERVE;
				}

				fstack.push();
				try {
					fstack.setTextMode(textmode);
					Walker walker = buildWalker(fstack, content, false);
					if (walker.hasNext()) {
						
						if (!walker.isAllText() 
								&& fstack.getPadBetween() != null) {
							// we need to newline/indent
							final String indent = fstack.getPadBetween();
							printText(out, fstack, new Text(indent));
						}
						
						printContent(out, fstack, nstack, walker);
						
						if (!walker.isAllText() && 
								fstack.getPadLast() != null) {
							// we need to newline/indent
							final String indent = 
									fstack.getPadLast();
							printText(out, fstack, new Text(indent));
						}
						
					}

				} finally {
					fstack.pop();
				}
			}

			// contentHandler.endElement()
			out.getContentHandler().endElement(element.getNamespaceURI(),
					element.getName(), element.getQualifiedName());

			// contentHandler.endPrefixMapping()
			// de-map in reverse order to the mapping.
			for (Namespace ns : nstack.addedReverse()) {
				ch.endPrefixMapping(ns.getPrefix());
			}

		} finally {
			nstack.pop();
			out.getLocator().setNode(origloc);
		}
	}

	/**
	 * This will handle printing of a List of {@link Content}.
	 * <p>
	 * It relies on the appropriate Walker to get the formatting right.
	 * 
	 * @param out
	 *        <code>SAXTarget</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param nstack
	 *        the NamespaceStack
	 * @param walker
	 *        <code>Waker</code> of <code>Content</code> to write.
	 * @throws SAXException
	 *         if the destination SAXTarget fails
	 */
	protected void printContent(final SAXTarget out, final FormatStack fstack,
			final NamespaceStack nstack, final Walker walker)
			throws SAXException {

		while (walker.hasNext()) {
			final Content c = walker.next();
			if (c == null) {
				// Formatted Text or CDATA
				final String text = walker.text();
				if (walker.isCDATA()) {
					printCDATA(out, fstack, new CDATA(text));
				} else {
					printText(out, fstack, new Text(text));
				}
			} else {
				switch (c.getCType()) {
					case CDATA:
						printCDATA(out, fstack, (CDATA)c);
						break;
					case Comment:
						printComment(out, fstack, (Comment)c);
						break;
					case DocType:
						// do nothing.
						break;
					case Element :
						printElement(out, fstack, nstack, (Element)c);
						break;
					case EntityRef:
						printEntityRef(out, fstack, (EntityRef)c);
						break;
					case ProcessingInstruction:
						printProcessingInstruction(out, fstack, 
								(ProcessingInstruction)c);
						break;
					case Text:
						printText(out, fstack, (Text)c);
						break;
				}
			}
		}
	}

	/**
	 * <p>
	 * Returns the SAX 2.0 attribute type string from the type of a JDOM
	 * Attribute.
	 * </p>
	 * 
	 * @param type
	 *        <code>int</code> the type of the JDOM attribute.
	 * @return <code>String</code> the SAX 2.0 attribute type string.
	 * @see org.jdom2.Attribute#getAttributeType
	 * @see org.xml.sax.Attributes#getType
	 */
	private static String getAttributeTypeName(AttributeType type) {
		switch (type) {
			case UNDECLARED:
				return "CDATA";
			default:
				return type.name();
		}
	}

	/**
	 * <p>
	 * Creates a SAX XMLReader.
	 * </p>
	 * 
	 * @return <code>XMLReader</code> a SAX2 parser.
	 * @throws Exception
	 *         if no parser can be created.
	 */
	protected XMLReader createParser() throws Exception {
		XMLReader parser = null;

		// Try using JAXP...
		// Note we need JAXP 1.1, and if JAXP 1.0 is all that's
		// available then the getXMLReader call fails and we skip
		// to the hard coded default parser
		try {
			Class<?> factoryClass = Class
					.forName("javax.xml.parsers.SAXParserFactory");

			// factory = SAXParserFactory.newInstance();
			Method newParserInstance = factoryClass.getMethod("newInstance");
			Object factory = newParserInstance.invoke(null);

			// jaxpParser = factory.newSAXParser();
			Method newSAXParser = factoryClass.getMethod("newSAXParser");
			Object jaxpParser = newSAXParser.invoke(factory);

			// parser = jaxpParser.getXMLReader();
			Class<? extends Object> parserClass = jaxpParser.getClass();
			Method getXMLReader = parserClass.getMethod("getXMLReader");
			parser = (XMLReader) getXMLReader.invoke(jaxpParser);
		} catch (ClassNotFoundException e) {
			// e.printStackTrace();
		} catch (InvocationTargetException e) {
			// e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// e.printStackTrace();
		} catch (IllegalAccessException e) {
			// e.printStackTrace();
		}

		// Check to see if we got a parser yet, if not, try to use a
		// hard coded default
		if (parser == null) {
			parser = XMLReaderFactory
					.createXMLReader("org.apache.xerces.parsers.SAXParser");
		}
		return parser;
	}

	/**
	 * <p>
	 * This will create a SAX XMLReader capable of parsing a DTD and configure
	 * it so that the DTD parsing events are routed to the handlers registered
	 * onto this SAXOutputter.
	 * </p>
	 * 
	 * @return <code>XMLReader</code> a SAX2 parser.
	 * @throws JDOMException
	 *         if no parser can be created.
	 */
	private XMLReader createDTDParser(SAXTarget out) throws SAXException {
		XMLReader parser = null;

		// Get a parser instance
		try {
			parser = createParser();
		} catch (Exception ex1) {
			throw new SAXException("Error in SAX parser allocation", ex1);
		}

		// Register handlers
		if (out.getDTDHandler() != null) {
			parser.setDTDHandler(out.getDTDHandler());
		}
		if (out.getEntityResolver() != null) {
			parser.setEntityResolver(out.getEntityResolver());
		}
		if (out.getLexicalHandler() != null) {
			try {
				parser.setProperty(SAX_PROPERTY_LEXICAL_HANDLER,
						out.getLexicalHandler());
			} catch (SAXException ex1) {
				try {
					parser.setProperty(SAX_PROPERTY_LEXICAL_HANDLER_ALT,
							out.getLexicalHandler());
				} catch (SAXException ex2) {
					// Forget it!
				}
			}
		}
		if (out.getDeclHandler() != null) {
			try {
				parser.setProperty(SAX_PROPERTY_DECLARATION_HANDLER,
						out.getDeclHandler());
			} catch (SAXException ex1) {
				try {
					parser.setProperty(SAX_PROPERTY_DECLARATION_HANDLER_ALT,
							out.getDeclHandler());
				} catch (SAXException ex2) {
					// Forget it!
				}
			}
		}

		// Absorb errors as much as possible, per Laurent
		parser.setErrorHandler(new DefaultHandler());

		return parser;
	}

}
