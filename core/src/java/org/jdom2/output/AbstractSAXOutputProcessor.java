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

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import org.jdom2.*;
import org.jdom2.output.Format.TextMode;

import org.xml.sax.*;
import org.xml.sax.ext.*;
import org.xml.sax.helpers.*;

/**
 * Outputs a JDOM document as a stream of SAX2 events.
 * <p>
 * Most ContentHandler callbacks are supported. Neither
 * <code>ignorableWhitespace()</code> nor <code>skippedEntity()</code> have been
 * implemented.
 * <p>
 * At this time, it is not possible to access notations and unparsed entity
 * references in a DTD from JDOM. Therefore, <code>DTDHandler</code> callbacks
 * have not been implemented yet.
 * <p>
 * The <code>ErrorHandler</code> callbacks have not been implemented, since
 * these are supposed to be invoked when the document is parsed and at this
 * point the document exists in memory and is known to have no errors.
 * </p>
 * 
 * @author Brett McLaughlin
 * @author Jason Hunter
 * @author Fred Trimble
 * @author Bradley S. Huffman
 * @author Rolf Lear
 */
public class AbstractSAXOutputProcessor extends AbstractOutputProcessor
		implements SAXOutputProcessor, JDOMConstants {

	private static final char[] singlespace = new char[] { ' ' };
	
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
			printContent(out, new FormatStack(format), new NamespaceStack(),
					list);
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
			printCDATA(out, new FormatStack(format), cdata);
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
			printText(out, new FormatStack(format), text);
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

			printContent(out, new FormatStack(format), new NamespaceStack(),
					nodes);

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
		for (Content obj : document.getContent()) {

			// update locator
			out.getLocator().setNode(obj);

			if (obj instanceof Element) {
				// process root element and its content
				printElement(out, fstack, nstack, document.getRootElement());
			} else if (obj instanceof ProcessingInstruction) {
				// contentHandler.processingInstruction()
				printProcessingInstruction(out, fstack,
						(ProcessingInstruction) obj);
			} else if (obj instanceof Comment) {
				// lexicalHandler.comment()
				printComment(out, fstack, (Comment) obj);
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
		String text = cdata.getText();
		switch (fstack.getTextMode()) {
			case PRESERVE:
				break;
			case NORMALIZE:
				text = textCompact(text);
				if (text.length() == 0) {
					text = null;
				}
				break;
			case TRIM:
				text = textTrimBoth(text);
				if (text.length() == 0) {
					text = null;
				}
				break;
			case TRIM_FULL_WHITE:
				text = textTrimFullWhite(text);
				if (text.length() == 0) {
					text = null;
				}
				break;
		}
		if (text != null) {
			final LexicalHandler lexicalHandler = out.getLexicalHandler();
			final char[] chars = text.toCharArray();
			if (lexicalHandler != null) {
				lexicalHandler.startCDATA();
				out.getContentHandler().characters(chars, 0, chars.length);
				lexicalHandler.endCDATA();
			} else {
				out.getContentHandler().characters(chars, 0, chars.length);
			}
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
		String str = text.getText();
		switch (fstack.getTextMode()) {
			case PRESERVE:
				break;
			case NORMALIZE:
				str = textCompact(str);
				break;
			case TRIM:
				str = textTrimBoth(str);
				break;
			case TRIM_FULL_WHITE:
				str = textTrimFullWhite(str);
				break;
		}
		if (str != null) {
			final char[] chars = str.toCharArray();
			out.getContentHandler().characters(chars, 0, chars.length);
		}
	}

	private void printIndent(final SAXTarget out, final FormatStack fstack)
			throws SAXException {
		if (fstack.getLevelIndent() != null
				&& fstack.getLineSeparator() != null) {
			final char[] chars = fstack.getLevelIndent().toCharArray();
			out.getContentHandler().ignorableWhitespace(chars, 0, chars.length);
		}
	}

	private void printEOL(final SAXTarget out, final FormatStack fstack)
			throws SAXException {
		if (fstack.getLevelIndent() != null
				&& fstack.getLineSeparator() != null) {
			final char[] chars = fstack.getLineSeparator().toCharArray();
			out.getContentHandler().ignorableWhitespace(chars, 0, chars.length);
		}
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

			TextMode textmode = fstack.getTextMode();

			// Check for xml:space and adjust format settings
			final String space = element.getAttributeValue("space",
					Namespace.XML_NAMESPACE);

			if ("default".equals(space)) {
				textmode = fstack.getDefaultMode();
			} else if ("preserve".equals(space)) {
				textmode = TextMode.PRESERVE;
			}

			final List<Content> content = element.getContent();

			// is there only text content
			// text content is Text/CDATA/EntityRef
			boolean textonly = true;
			// is the text content only whitespace.
			// whiteonly is obvious....
			boolean whiteonly = true;

			for (Content k : content) {
				if (k instanceof Text) {
					if (whiteonly) {
						if (textmode == TextMode.PRESERVE) {
							whiteonly = false;
						} else {
							// look for non-whitespace.
							String val = k.getValue();
							int cc = val.length();
							while (--cc >= 0) {
								if (!Verifier.isXMLWhitespace(val.charAt(cc))) {
									// found non-whitespace;
									whiteonly = false;
									break;
								}
							}
						}
					}
				} else {
					whiteonly = false;
					if (!(k instanceof EntityRef)) {
						textonly = false;
						// we know everything we need.
						break;
					}
				}
			}

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
			for (Attribute a : element.getAttributes()) {
				atts.addAttribute(a.getNamespaceURI(), a.getName(),
						a.getQualifiedName(),
						getAttributeTypeName(a.getAttributeType()),
						a.getValue());
			}

			// contentHandler.startElement()
			ch.startElement(element.getNamespaceURI(), element.getName(),
					element.getQualifiedName(), atts);

			// OK, now we print out the meat of the Element
			if (!content.isEmpty()) {
				if (textonly) {
					if (!whiteonly) {
						fstack.push();
						try {
							fstack.setTextMode(textmode);
							helperTextType(out, fstack, content, 0,
									content.size());
						} finally {
							fstack.pop();
						}
					}
				} else {
					fstack.push();
					try {
						fstack.setTextMode(textmode);
						printEOL(out, fstack);
						printContent(out, fstack, nstack, content);

					} finally {
						fstack.pop();
					}
					printIndent(out, fstack);
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
	 * {@link #printTextConsecutive(SAXTarget, FormatStack, List, int, int)},
	 * {@link #printCDATA(SAXTarget, FormatStack, CDATA)}, or
	 * {@link #printComment(SAXTarget, FormatStack, Comment)},
	 * <li>do a newline if one is specified.
	 * <li>loop back to 1. until there's no more content to process.
	 * </ol>
	 * 
	 * @param out
	 *        <code>SAXTarget</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param nstack
	 *        the NamespaceStack
	 * @param content
	 *        <code>List</code> of <code>Content</code> to write.
	 * @throws SAXException
	 *         if the destination SAXTarget fails
	 */
	protected void printContent(final SAXTarget out, final FormatStack fstack,
			final NamespaceStack nstack, final List<? extends Content> content)
			throws SAXException {

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
							printIndent(out, fstack);
							helperTextType(out, fstack, content, txti, index
									- txti);
							printEOL(out, fstack);
						}
					}
					txti = -1;

					printIndent(out, fstack);
					helperContentDispatcher(out, fstack, nstack, c);
					printEOL(out, fstack);
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
				printIndent(null, fstack);
				helperTextType(out, fstack, content, txti, index - txti);
				printEOL(out, fstack);
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
	 * specified text-type values are all white-space only, then you may get odd
	 * looking XML (empty lines).
	 * 
	 * @param out
	 *        <code>SAXTarget</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param content
	 *        <code>List</code> of <code>Content</code> to write.
	 * @param offset
	 *        index of first content node (inclusive).
	 * @param len
	 *        number of nodes that are text-type.
	 * @throws SAXException
	 *         if the destination SAXTarget fails
	 */
	protected void printTextConsecutive(final SAXTarget out,
			FormatStack fstack, final List<? extends Content> content,
			final int offset, final int len) throws SAXException {

		// we are guaranteed that len >= 2
		assert len >= 2 : "Need at least two text-types to be 'Consecutive'";

		final ContentHandler ch = out.getContentHandler();

		// here we have a sequence of Text nodes to print.
		// we follow the TextMode rules for it.
		// also guaranteed is that at least one of the nodes has some real text.

		final TextMode mode = fstack.getTextMode();

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
			for (ListIterator<? extends Content> li = content
					.listIterator(offset); cnt > 0; cnt--) {
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
					if (!first && !dospace
							&& Verifier.isXMLWhitespace(val.charAt(0))) {
						// we are between content, and need to put in a space.
						dospace = true;
					}
					endspace = Verifier
							.isXMLWhitespace(val.charAt(val.length() - 1));
				}
				first = false;
				if (dospace) {
					ch.characters(singlespace, 0, 1);
				}

				switch (c.getCType()) {
					case Text:
						characters(ch, textCompact(((Text) c).getText()));
						break;
					case CDATA:
						cdata(ch, out.getLexicalHandler(),
								textCompact(((Text) c).getText()));
						break;
					case EntityRef:
						printEntityRef(out, fstack, (EntityRef) c);
						break;
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
		if (TextMode.TRIM == fstack.getTextMode()) {

			int lcnt = 0;
			while (lcnt < len) {
				Content node = content.get(offset + lcnt);
				if (!(node instanceof Text)
						|| !isAllWhitespace(node.getValue())) {
					break;
				}
				lcnt++;
			}

			if (lcnt == len) {
				return;
			}

			final int left = offset + lcnt;
			int rcnt = len - 1;
			while (rcnt > lcnt) {
				Content node = content.get(offset + rcnt);
				if (!(node instanceof Text)
						|| !isAllWhitespace(node.getValue())) {
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
						characters(ch, textTrimBoth(((Text) node).getText()));
						break;
					case CDATA:
						cdata(ch, out.getLexicalHandler(),
								textTrimBoth(((CDATA) node).getText()));
						break;
					case EntityRef:
						printEntityRef(out, fstack, (EntityRef) node);
					default:
						// do nothing
				}
				return;
			}
			final Content leftc = content.get(left);
			switch (leftc.getCType()) {
				case Text:
					characters(ch, textTrimLeft(((Text) leftc).getText()));
					break;
				case CDATA:
					cdata(ch, out.getLexicalHandler(),
							textTrimLeft(((CDATA) leftc).getText()));
					break;
				case EntityRef:
					printEntityRef(out, fstack, (EntityRef) leftc);
				default:
					// do nothing
			}

			int cnt = right - left - 1;
			for (ListIterator<? extends Content> li = content
					.listIterator(left + 1); cnt > 0; cnt--) {
				helperRawTextType(out, fstack, li.next());
			}

			final Content rightc = content.get(right);
			switch (rightc.getCType()) {
				case Text:
					characters(ch, textTrimRight(((Text) rightc).getText()));
					break;
				case CDATA:
					cdata(ch, out.getLexicalHandler(),
							textTrimRight(((CDATA) rightc).getText()));
					break;
				case EntityRef:
					printEntityRef(out, fstack, (EntityRef) rightc);
				default:
					// do nothing
			}
			return;
		}
		// For when there's just one thing to print, or in RAW mode we just
		// print the contents...
		// ... its the simplest mode.
		// so simple, in fact that it is dealt with differently as a special
		// case.
		for (int i = 0; i < len; i++) {
			final Content rightc = content.get(offset + i);
			switch (rightc.getCType()) {
				case Text:
					characters(ch, ((Text) rightc).getText());
					break;
				case CDATA:
					cdata(ch, out.getLexicalHandler(),
							((CDATA) rightc).getText());
					break;
				case EntityRef:
					printEntityRef(out, fstack, (EntityRef) rightc);
				default:
					// do nothing
			}
		}
		// assert TextMode.PRESERVE != mode :
		// "PRESERVE text-types are not Consecutive";
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
	 * @throws SAXException
	 *         if the output fails
	 */
	protected void helperContentDispatcher(final SAXTarget out,
			final FormatStack fstack, final NamespaceStack nstack,
			final Content content) throws SAXException {
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
				if (fstack.getLineSeparator() != null) {
					final char[] chars = fstack.getLineSeparator()
							.toCharArray();
					out.getContentHandler().ignorableWhitespace(chars, 0,
							chars.length);
				}
				break;
			default:
				throw new IllegalStateException("Unexpected Content "
						+ content.getCType());
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
	 * specified text-type values are all white-space only, then you may get odd
	 * looking XML (empty lines).
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
	 * @throws SAXException
	 *         if the output fails.
	 */
	protected final void helperTextType(final SAXTarget out,
			final FormatStack fstack, final List<? extends Content> content,
			final int offset, final int len) throws SAXException {

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
	 * @throws SAXException
	 *         if the output fails
	 */
	protected final void helperRawTextType(final SAXTarget out,
			final FormatStack fstack, final Content content)
			throws SAXException {
		switch (content.getCType()) {
			case Text:
				characters(out.getContentHandler(), ((Text) content).getText());
				break;
			case CDATA:
				cdata(out.getContentHandler(), out.getLexicalHandler(),
						((CDATA) content).getText());
				break;
			case EntityRef:
				printEntityRef(out, fstack, (EntityRef) content);
			default:
				// do nothing
		}
	}

	/**
	 * <p>
	 * This will be called for each chunk of character data encountered.
	 * </p>
	 * 
	 * @param elementText
	 *        all text in an element, including whitespace.
	 */
	private void characters(final ContentHandler contentHandler,
			final String text) throws SAXException {
		if (text == null) {
			return;
		}
		char[] c = text.toCharArray();
		contentHandler.characters(c, 0, c.length);
	}

	/**
	 * <p>
	 * This will be called for each chunk of character data encountered.
	 * </p>
	 * 
	 * @param elementText
	 *        all text in an element, including whitespace.
	 */
	private void cdata(final ContentHandler contentHandler,
			final LexicalHandler lexicalHandler, final String text)
			throws SAXException {
		if (text == null) {
			return;
		}
		char[] c = text.toCharArray();
		lexicalHandler.startCDATA();
		contentHandler.characters(c, 0, c.length);
		lexicalHandler.endCDATA();
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

	// /**
	// * <p>
	// * Notifies the registered {@link ErrorHandler SAX error handler}
	// * (if any) of an input processing error. The error handler can
	// * choose to absorb the error and let the processing continue.
	// * </p>
	// *
	// * @param exception <code>JDOMException</code> containing the
	// * error information; will be wrapped in a
	// * {@link SAXParseException} when reported to
	// * the SAX error handler.
	// *
	// * @throws JDOMException if no error handler has been registered
	// * or if the error handler fired a
	// * {@link SAXException}.
	// */
	// private void handleError(JDOMException exception) throws JDOMException {
	// if (errorHandler != null) {
	// try {
	// errorHandler.error(new SAXParseException(
	// exception.getMessage(), null, exception));
	// }
	// catch (SAXException se) {
	// if (se.getException() instanceof JDOMException) {
	// throw (JDOMException)(se.getException());
	// }
	// throw new JDOMException(se.getMessage(), se);
	// }
	// }
	// else {
	// throw exception;
	// }
	// }

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
