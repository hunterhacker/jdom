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

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.jdom2.Attribute;
import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.Content;
import org.jdom2.JDOMConstants;
import org.jdom2.Content.CType;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.EntityRef;
import org.jdom2.Namespace;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;
import org.jdom2.Verifier;
import org.jdom2.output.Format;
import org.jdom2.output.Format.TextMode;
import org.jdom2.output.StAXStreamOutputter;
import org.jdom2.util.NamespaceStack;

/**
 * This class provides a concrete implementation of {@link StAXStreamProcessor}
 * for supporting the {@link StAXStreamOutputter}.
 * <p>
 * <h2>Overview</h2>
 * <p>
 * This class is marked abstract even though all methods are fully implemented.
 * The <code>process*(...)</code> methods are public because they match the
 * StAXStreamProcessor interface but the remaining methods are all protected.
 * <p>
 * People who want to create a custom StAXStreamProcessor for StAXStreamOutputter are
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
 * {@link #printElement(XMLStreamWriter, FormatStack, NamespaceStack, Element)} method.
 * The stacks are pushed and popped in that method only. They significantly
 * improve the performance and readability of the code.
 * <p>
 * The NamespaceStack is only sent through to the
 * {@link #printElement(XMLStreamWriter, FormatStack, NamespaceStack, Element)} and
 * {@link #printContent(XMLStreamWriter, FormatStack, NamespaceStack, Walker)} methods, but
 * the FormatStack is pushed through to all print* Methods.
 * <p>
 * An interesting read for people using this class:
 * <a href="http://ws.apache.org/axiom/devguide/ch05.html">Apache Axiom notes on setPrefix()</a>.
 *
 * @see StAXStreamOutputter
 * @see StAXStreamProcessor
 * @since JDOM2
 * @author Rolf Lear
 */
public abstract class AbstractStAXStreamProcessor
		extends AbstractOutputProcessor implements StAXStreamProcessor {


	/* *******************************************
	 * StAXStreamProcessor implementation.
	 * *******************************************
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.StAXStreamProcessor#process(java.io.XMLStreamWriter,
	 * org.jdom2.Document, org.jdom2.output.Format)
	 */
	@Override
	public void process(final XMLStreamWriter out, final Format format,
			final Document doc) throws XMLStreamException {
		printDocument(out, new FormatStack(format), new NamespaceStack(), doc);
		out.flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.StAXStreamProcessor#process(java.io.XMLStreamWriter,
	 * org.jdom2.DocType, org.jdom2.output.Format)
	 */
	@Override
	public void process(final XMLStreamWriter out, final Format format,
			final DocType doctype) throws XMLStreamException {
		printDocType(out, new FormatStack(format), doctype);
		out.flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.StAXStreamProcessor#process(java.io.XMLStreamWriter,
	 * org.jdom2.Element, org.jdom2.output.Format)
	 */
	@Override
	public void process(final XMLStreamWriter out, final Format format,
			final Element element) throws XMLStreamException {
		// If this is the root element we could pre-initialize the
		// namespace stack with the namespaces
		printElement(out, new FormatStack(format), new NamespaceStack(),
				element);
		out.flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.StAXStreamProcessor#process(java.io.XMLStreamWriter,
	 * java.util.List, org.jdom2.output.Format)
	 */
	@Override
	public void process(final XMLStreamWriter out, final Format format,
			final List<? extends Content> list)
			throws XMLStreamException {
		final FormatStack fstack = new FormatStack(format);
		final Walker walker = buildWalker(fstack, list, false);
		printContent(out, fstack, new NamespaceStack(), walker);
		out.flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.StAXStreamProcessor#process(java.io.XMLStreamWriter,
	 * org.jdom2.CDATA, org.jdom2.output.Format)
	 */
	@Override
	public void process(final XMLStreamWriter out, final Format format,
			final CDATA cdata) throws XMLStreamException {
		final List<CDATA> list = Collections.singletonList(cdata);
		final FormatStack fstack = new FormatStack(format);
		final Walker walker = buildWalker(fstack, list, false);
		if (walker.hasNext()) {
			final Content c = walker.next();
			if (c == null) {
				printCDATA(out, fstack, new CDATA(walker.text()));
			} else if (c.getCType() == CType.CDATA) {
				printCDATA(out, fstack, (CDATA)c);
			}
		}
		out.flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.StAXStreamProcessor#process(java.io.XMLStreamWriter,
	 * org.jdom2.Text, org.jdom2.output.Format)
	 */
	@Override
	public void process(final XMLStreamWriter out, final Format format,
			final Text text) throws XMLStreamException {
		final List<Text> list = Collections.singletonList(text);
		final FormatStack fstack = new FormatStack(format);
		final Walker walker = buildWalker(fstack, list, false);
		if (walker.hasNext()) {
			final Content c = walker.next();
			if (c == null) {
				printText(out, fstack, new Text(walker.text()));
			} else if (c.getCType() == CType.Text) {
				printText(out, fstack, (Text)c);
			}
		}
		out.flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.StAXStreamProcessor#process(java.io.XMLStreamWriter,
	 * org.jdom2.Comment, org.jdom2.output.Format)
	 */
	@Override
	public void process(final XMLStreamWriter out, final Format format,
			final Comment comment) throws XMLStreamException {
		printComment(out, new FormatStack(format), comment);
		out.flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.StAXStreamProcessor#process(java.io.XMLStreamWriter,
	 * org.jdom2.ProcessingInstruction, org.jdom2.output.Format)
	 */
	@Override
	public void process(final XMLStreamWriter out, final Format format,
			final ProcessingInstruction pi) throws XMLStreamException {
		FormatStack fstack = new FormatStack(format);
		// Output PI verbatim, disregarding TrAX escaping PIs.
		fstack.setIgnoreTrAXEscapingPIs(true);
		printProcessingInstruction(out, fstack, pi);
		out.flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.StAXStreamProcessor#process(java.io.XMLStreamWriter,
	 * org.jdom2.EntityRef, org.jdom2.output.Format)
	 */
	@Override
	public void process(final XMLStreamWriter out, final Format format,
			final EntityRef entity) throws XMLStreamException {
		printEntityRef(out, new FormatStack(format), entity);
		out.flush();
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
	 *        <code>XMLStreamWriter</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param nstack
	 *        the NamespaceStack
	 * @param doc
	 *        <code>Document</code> to write.
	 * @throws XMLStreamException
	 *         if the destination XMLStreamWriter fails
	 */
	protected void printDocument(final XMLStreamWriter out, final FormatStack fstack,
			final NamespaceStack nstack, final Document doc) throws XMLStreamException {

		if (fstack.isOmitDeclaration()) {
			// this actually writes the declaration as version 1, UTF-8
			out.writeStartDocument();
			if (fstack.getLineSeparator() != null) {
				out.writeCharacters(fstack.getLineSeparator());
			}
		} else if (fstack.isOmitEncoding()) {
			out.writeStartDocument("1.0");
			if (fstack.getLineSeparator() != null) {
				out.writeCharacters(fstack.getLineSeparator());
			}
		} else {
			out.writeStartDocument(fstack.getEncoding(), "1.0");
			if (fstack.getLineSeparator() != null) {
				out.writeCharacters(fstack.getLineSeparator());
			}
		}

		// we can output characters outside the Root element in StAX Event
		// code... so, take advantage.
		// If there is no root element then we cannot use the normal ways to
		// access the ContentList because Document throws an exception.
		// so we hack it and just access it by index.
		List<Content> list = doc.hasRootElement() ? doc.getContent() : 
			new ArrayList<Content>(doc.getContentSize());
		if (list.isEmpty()) {
			final int sz = doc.getContentSize();
			for (int i = 0; i < sz; i++) {
				list.add(doc.getContent(i));
			}
		}
		Walker walker = buildWalker(fstack, list, false);
		if (walker.hasNext()) {
			while (walker.hasNext()) {
				
				final Content c = walker.next();
				// we do not ignore Text-like things in the Document.
				// the walker creates the indenting for us.
				if (c == null) {
					// but, what we do is ensure it is all whitespace, and not CDATA
					final String padding = walker.text();
					if (padding != null && Verifier.isAllXMLWhitespace(padding) && 
							!walker.isCDATA()) {
						// we do not use the escaping or text* method because this
						// content is outside of the root element, and thus is not
						// strict text.
						out.writeCharacters(padding);
					}
				} else {
					switch (c.getCType()) {
						case Comment :
							printComment(out, fstack, (Comment)c);
							break;
						case DocType :
							printDocType(out, fstack, (DocType)c);
							break;
						case Element :
							printElement(out, fstack, nstack, (Element)c);
							break;
						case ProcessingInstruction :
							printProcessingInstruction(out, fstack, 
									(ProcessingInstruction)c);
							break;
						case Text :
							final String padding = ((Text)c).getText();
							if (padding != null && Verifier.isAllXMLWhitespace(padding)) {
								// we do not use the escaping or text* method because this
								// content is outside of the root element, and thus is not
								// strict text.
								out.writeCharacters(padding);
							}
						default :
							// do nothing.
					}
				}
				
			}
			
			if (fstack.getLineSeparator() != null) {
				out.writeCharacters(fstack.getLineSeparator());
			}
		}
		
		out.writeEndDocument();
		
	}

	/**
	 * This will handle printing of a {@link DocType}.
	 * 
	 * @param out
	 *        <code>XMLStreamWriter</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param docType
	 *        <code>DocType</code> to write.
	 * @throws XMLStreamException
	 *         if the destination XMLStreamWriter fails
	 */
	protected void printDocType(final XMLStreamWriter out, final FormatStack fstack,
			final DocType docType) throws XMLStreamException {

		final String publicID = docType.getPublicID();
		final String systemID = docType.getSystemID();
		final String internalSubset = docType.getInternalSubset();
		boolean hasPublic = false;

		// Declaration is never indented.
		// write(out, fstack.getLevelIndent());
		
		StringWriter sw = new StringWriter();

		sw.write("<!DOCTYPE ");
		sw.write(docType.getElementName());
		if (publicID != null) {
			sw.write(" PUBLIC \"");
			sw.write(publicID);
			sw.write("\"");
			hasPublic = true;
		}
		if (systemID != null) {
			if (!hasPublic) {
				sw.write(" SYSTEM");
			}
			sw.write(" \"");
			sw.write(systemID);
			sw.write("\"");
		}
		if ((internalSubset != null) && (!internalSubset.equals(""))) {
			sw.write(" [");
			sw.write(fstack.getLineSeparator());
			sw.write(docType.getInternalSubset());
			sw.write("]");
		}
		sw.write(">");
		
		// DocType does not write its own EOL
		// for compatibility reasons. Only
		// when output from inside a Content set.
		// write(out, fstack.getLineSeparator());
		out.writeDTD(sw.toString());
	}

	/**
	 * This will handle printing of a {@link ProcessingInstruction}.
	 * 
	 * @param out
	 *        <code>XMLStreamWriter</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param pi
	 *        <code>ProcessingInstruction</code> to write.
	 * @throws XMLStreamException
	 *         if the destination XMLStreamWriter fails
	 */
	protected void printProcessingInstruction(final XMLStreamWriter out,
			final FormatStack fstack, final ProcessingInstruction pi)
			throws XMLStreamException {
		String target = pi.getTarget();
		String rawData = pi.getData();
		if (rawData != null && rawData.trim().length() > 0) {
			out.writeProcessingInstruction(target, rawData);
		} else {
			out.writeProcessingInstruction(target);
		}
	}

	/**
	 * This will handle printing of a {@link Comment}.
	 * 
	 * @param out
	 *        <code>XMLStreamWriter</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param comment
	 *        <code>Comment</code> to write.
	 * @throws XMLStreamException
	 *         if the destination XMLStreamWriter fails
	 */
	protected void printComment(final XMLStreamWriter out, final FormatStack fstack,
			final Comment comment) throws XMLStreamException {
		out.writeComment(comment.getText());
	}

	/**
	 * This will handle printing of an {@link EntityRef}.
	 * 
	 * @param out
	 *        <code>XMLStreamWriter</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param entity
	 *        <code>EntotyRef</code> to write.
	 * @throws XMLStreamException
	 *         if the destination XMLStreamWriter fails
	 */
	protected void printEntityRef(final XMLStreamWriter out, final FormatStack fstack,
			final EntityRef entity) throws XMLStreamException {
		out.writeEntityRef(entity.getName());
	}

	/**
	 * This will handle printing of a {@link CDATA}.
	 * 
	 * @param out
	 *        <code>XMLStreamWriter</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param cdata
	 *        <code>CDATA</code> to write.
	 * @throws XMLStreamException
	 *         if the destination XMLStreamWriter fails
	 */
	protected void printCDATA(final XMLStreamWriter out, final FormatStack fstack,
			final CDATA cdata) throws XMLStreamException {
		// CDATAs are treated like text, not indented/newline content.
		out.writeCData(cdata.getText());
	}

	/**
	 * This will handle printing of a {@link Text}.
	 * 
	 * @param out
	 *        <code>XMLStreamWriter</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param text
	 *        <code>Text</code> to write.
	 * @throws XMLStreamException
	 *         if the destination XMLStreamWriter fails
	 */
	protected void printText(final XMLStreamWriter out, final FormatStack fstack,
			final Text text) throws XMLStreamException {
		out.writeCharacters(text.getText());
	}

	/**
	 * This will handle printing of an {@link Element}.
	 * 
	 * @param out
	 *        <code>XMLStreamWriter</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param nstack
	 *        the NamespaceStack
	 * @param element
	 *        <code>Element</code> to write.
	 * @throws XMLStreamException
	 *         if the destination XMLStreamWriter fails
	 */
	protected void printElement(final XMLStreamWriter out, final FormatStack fstack,
			final NamespaceStack nstack, final Element element) throws XMLStreamException {

		nstack.push(element);
		try {
			for (Namespace nsa : nstack.addedForward()) {
				if (JDOMConstants.NS_PREFIX_DEFAULT.equals(nsa.getPrefix())) {
					out.setDefaultNamespace(nsa.getURI());
				} else {
					out.setPrefix(nsa.getPrefix(), nsa.getURI());
				}
			}
			
			final List<Content> content = element.getContent();

			TextMode textmode = fstack.getTextMode();
			
			
			// OK, we play silly-buggers with the walker here in this class.
			// we need to know what sort of Element we have, expanded or not.
			// as a result, we need to know if we have content or not, and
			// the walker can be empty if it is all formatted out.
			// so we need to know the walker before we start the element.
			// if the walker resolves to nothing then we set it to null
			// as an indication that there is not sub-content.
			Walker walker = null;
			
			if (!content.isEmpty()) {
			
				// Check for xml:space and adjust format settings
				final String space = element.getAttributeValue("space",
						Namespace.XML_NAMESPACE);
	
				if ("default".equals(space)) {
					textmode = fstack.getDefaultMode();
				}
				else if ("preserve".equals(space)) {
					textmode = TextMode.PRESERVE;
				}
				
				fstack.push();
				try {
					fstack.setTextMode(textmode);
					walker = buildWalker(fstack, content, false);
					if (!walker.hasNext()) {
						// rip out the walker if there is no content.
						walker = null;
					}
				} finally {
					fstack.pop();
				}
			}

			// Three conditions that determine the required output.
			// do we have an expanded element( <emt></emt> or an single <emt />
			// if there is any printable content, or if expandempty is set
			// then we must expand.
			boolean expandit = walker != null || fstack.isExpandEmptyElements();
			
			final Namespace ns = element.getNamespace();
			if (expandit) {
				out.writeStartElement(ns.getPrefix(), element.getName(), ns.getURI());
				
				// Print the element's namespace, if appropriate
				for (final Namespace nsd : nstack.addedForward()) {
					printNamespace(out, fstack, nsd);
				}
	
				// Print out attributes
				if (element.hasAttributes()) {
					for (final Attribute attribute : element.getAttributes()) {
						printAttribute(out, fstack, attribute);
					}
				}
				
				// This neatens up the output stream for some reason - bug in standard StAX
				// implementation requires us to close off the Element start tag before we
				// start adding new Namespaces to child contexts...
				out.writeCharacters("");

				// OK, now we print out the meat of the Element
				if (walker != null) {
					// we need to re-create the walker/fstack.
					fstack.push();
					try {
						fstack.setTextMode(textmode);
						if (!walker.isAllText() && fstack.getPadBetween() != null) {
							// we need to newline/indent
							final String indent = fstack.getPadBetween();
							printText(out, fstack, new Text(indent));
						}
						
						printContent(out, fstack, nstack, walker);
						
						if (!walker.isAllText() && fstack.getPadLast() != null) {
							// we need to newline/indent
							final String indent = fstack.getPadLast(); 
							printText(out, fstack, new Text(indent));
						}
					} finally {
						fstack.pop();
					}
				}
			
				out.writeEndElement();
				
			} else {
				// implies:
				//      fstack.isExpandEmpty... is false
				// and       content.isEmpty()
				//       or      textonly == true
				//           and preserve == false
				//           and whiteonly == true
				
				out.writeEmptyElement(ns.getPrefix(), element.getName(), ns.getURI());
				
				// Print the element's namespace, if appropriate
				for (final Namespace nsd : nstack.addedForward()) {
					printNamespace(out, fstack, nsd);
				}
	
				// Print out attributes
				for (final Attribute attribute : element.getAttributes()) {
					printAttribute(out, fstack, attribute);
				}
				// This neatens up the output stream for some reason.
				out.writeCharacters("");
			}

		} finally {
			for (Namespace nsr : nstack.addedForward()) {
				Namespace nsa = nstack.getRebound(nsr.getPrefix());
				if (nsa != null) {
					if (JDOMConstants.NS_PREFIX_DEFAULT.equals(nsa.getPrefix())) {
						out.setDefaultNamespace(nsa.getURI());
					} else {
						out.setPrefix(nsa.getPrefix(), nsa.getURI());
					}
				}
			}
			nstack.pop();
		}
	}

	/**
	 * This will handle printing of a List of {@link Content}.
	 * <p>
	 * 
	 * @param out
	 *        <code>XMLStreamWriter</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param nstack
	 *        the NamespaceStack
	 * @param walker
	 *        <code>Walker</code> of <code>Content</code> to write.
	 * @throws XMLStreamException
	 *         if the destination XMLStreamWriter fails
	 */
	protected void printContent(final XMLStreamWriter out,
			final FormatStack fstack, final NamespaceStack nstack,
			final Walker walker) throws XMLStreamException {

		while (walker.hasNext()) {
			final Content content = walker.next();
			
			if (content == null) {
				if (walker.isCDATA()) {
					printCDATA(out, fstack, new CDATA(walker.text()));
				} else {
					printText(out, fstack, new Text(walker.text()));
				}
			} else {
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
						break;
					default:
						throw new IllegalStateException(
								"Unexpected Content " + content.getCType());
		
				}
			}
		}

	}


	/**
	 * This will handle printing of any needed <code>{@link Namespace}</code>
	 * declarations.
	 * 
	 * @param out
	 *        <code>XMLStreamWriter</code> to use.
	 * @param fstack
	 *        The current FormatStack
	 * @param ns
	 *        <code>Namespace</code> to print definition of
	 * @throws XMLStreamException
	 *         if the output fails
	 */
	protected void printNamespace(final XMLStreamWriter out, final FormatStack fstack, 
			final Namespace ns)  throws XMLStreamException {
		final String prefix = ns.getPrefix();
		final String uri = ns.getURI();
		
		out.writeNamespace(prefix, uri);
	}

	/**
	 * This will handle printing of an <code>{@link Attribute}</code>.
	 * 
	 * @param out
	 *        <code>XMLStreamWriter</code> to use.
	 * @param fstack
	 *        The current FormatStack
	 * @param attribute
	 *        <code>Attribute</code> to output
	 * @throws XMLStreamException
	 *         if the output fails
	 */
	protected void printAttribute(final XMLStreamWriter out, final FormatStack fstack,
			final Attribute attribute) throws XMLStreamException {

		if (!attribute.isSpecified() && fstack.isSpecifiedAttributesOnly()) {
			return;
		}
		
		final Namespace ns = attribute.getNamespace();
		if (ns == Namespace.NO_NAMESPACE) {
			out.writeAttribute(attribute.getName(), attribute.getValue());
		} else {
			out.writeAttribute(ns.getPrefix(), ns.getURI(), 
					attribute.getName(), attribute.getValue());
		}
	}
	
}
