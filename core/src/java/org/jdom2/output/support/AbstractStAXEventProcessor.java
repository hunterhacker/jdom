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
import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.util.XMLEventConsumer;

import org.jdom2.Attribute;
import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.Content;
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
import org.jdom2.output.StAXEventOutputter;
import org.jdom2.util.NamespaceStack;

/**
 * This class provides a concrete implementation of {@link StAXEventProcessor}
 * for supporting the {@link StAXEventOutputter}.
 * <p>
 * <h2>Overview</h2>
 * <p>
 * This class is marked abstract even though all methods are fully implemented.
 * The <code>process*(...)</code> methods are public because they match the
 * StAXEventProcessor interface but the remaining methods are all protected.
 * <p>
 * People who want to create a custom StAXEventProcessor for StAXEventOutputter are
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
 * {@link #printElement(XMLEventConsumer, FormatStack, NamespaceStack, XMLEventFactory, Element)} method.
 * The stacks are pushed and popped in that method only. They significantly
 * improve the performance and readability of the code.
 * <p>
 * The NamespaceStack is only sent through to the
 * {@link #printElement(XMLEventConsumer, FormatStack, NamespaceStack, XMLEventFactory, Element)} and
 * {@link #printContent(XMLEventConsumer, FormatStack, NamespaceStack, XMLEventFactory, Walker)} methods, but
 * the FormatStack is pushed through to all print* Methods.
 * 
 * @see StAXEventOutputter
 * @see StAXEventProcessor
 * @since JDOM2
 * @author Rolf Lear
 */
public abstract class AbstractStAXEventProcessor extends AbstractOutputProcessor 
		implements StAXEventProcessor {
	
	private static final class NSIterator implements Iterator<javax.xml.stream.events.Namespace> {
		private final Iterator<Namespace> source;
		private final XMLEventFactory fac;
		
		public NSIterator(Iterator<Namespace> source, XMLEventFactory fac) {
			super();
			this.source = source;
			this.fac = fac;
		}

		@Override
		public boolean hasNext() {
			return source.hasNext();
		}

		@Override
		public javax.xml.stream.events.Namespace next() {
			Namespace ns = source.next();
			return fac.createNamespace(ns.getPrefix(), ns.getURI());
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Cannot remove Namespaces");
			
		}
		
	}
	private static final class AttIterator implements Iterator<javax.xml.stream.events.Attribute> {
		private final Iterator<Attribute> source;
		private final XMLEventFactory fac;
		
		public AttIterator(final Iterator<Attribute> source, final XMLEventFactory fac, 
				final boolean specifiedAttributesOnly) {
			super();
			// remove not-specified Attributes if needed....
			this.source = specifiedAttributesOnly ? specified(source) : source;
			this.fac = fac;
		}

		private Iterator<Attribute> specified(Iterator<Attribute> src) {
			if (src == null) {
				return null;
			}
			final ArrayList<Attribute> al = new ArrayList<Attribute>();
			while (src.hasNext()) {
				Attribute att = src.next();
				if (att.isSpecified()) {
					al.add(att);
				}
			}
			return al.isEmpty() ? null : al.iterator();
		}

		@Override
		public boolean hasNext() {
			return source != null && source.hasNext();
		}

		@Override
		public javax.xml.stream.events.Attribute next() {
			final Attribute att = source.next();
			final Namespace ns = att.getNamespace();
			if (ns == Namespace.NO_NAMESPACE) {
				return fac.createAttribute(att.getName(), att.getValue());
			}
			return fac.createAttribute(ns.getPrefix(), ns.getURI(), 
					att.getName(), att.getValue());
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Cannot remove attributes");
			
		}
		
	}

	

	/* *******************************************
	 * StAXEventProcessor implementation.
	 * *******************************************
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.StAXEventProcessor#process(java.io.XMLEventConsumer,
	 * org.jdom2.Document, org.jdom2.output.Format)
	 */
	@Override
	public void process(final XMLEventConsumer out, final Format format,
			final XMLEventFactory eventfactory, final Document doc) throws XMLStreamException {
		printDocument(out, new FormatStack(format), new NamespaceStack(), eventfactory, doc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.StAXEventProcessor#process(java.io.XMLEventConsumer,
	 * org.jdom2.DocType, org.jdom2.output.Format)
	 */
	@Override
	public void process(final XMLEventConsumer out, final Format format,
			final XMLEventFactory eventfactory, final DocType doctype) throws XMLStreamException {
		printDocType(out, new FormatStack(format), eventfactory, doctype);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.StAXEventProcessor#process(java.io.XMLEventConsumer,
	 * org.jdom2.Element, org.jdom2.output.Format)
	 */
	@Override
	public void process(final XMLEventConsumer out, final Format format,
			final XMLEventFactory eventfactory, final Element element) throws XMLStreamException {
		// If this is the root element we could pre-initialize the
		// namespace stack with the namespaces
		printElement(out, new FormatStack(format), new NamespaceStack(),
				eventfactory, element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.StAXEventProcessor#process(java.io.XMLEventConsumer,
	 * java.util.List, org.jdom2.output.Format)
	 */
	@Override
	public void process(final XMLEventConsumer out, final Format format,
			final XMLEventFactory eventfactory, final List<? extends Content> list)
			throws XMLStreamException {
		final FormatStack fstack = new FormatStack(format);
		final Walker walker = buildWalker(fstack, list, false);
		printContent(out, new FormatStack(format), new NamespaceStack(), eventfactory, walker);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.StAXEventProcessor#process(java.io.XMLEventConsumer,
	 * org.jdom2.CDATA, org.jdom2.output.Format)
	 */
	@Override
	public void process(final XMLEventConsumer out, final Format format,
			final XMLEventFactory eventfactory, final CDATA cdata) throws XMLStreamException {
		final List<CDATA> list = Collections.singletonList(cdata);
		final FormatStack fstack = new FormatStack(format);
		final Walker walker = buildWalker(fstack, list, false);
		if (walker.hasNext()) {
			final Content c = walker.next();
			if (c == null) {
				printCDATA(out, fstack, eventfactory, new CDATA(walker.text()));
			} else if (c.getCType() == CType.CDATA) {
				printCDATA(out, fstack, eventfactory, (CDATA)c);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.StAXEventProcessor#process(java.io.XMLEventConsumer,
	 * org.jdom2.Text, org.jdom2.output.Format)
	 */
	@Override
	public void process(final XMLEventConsumer out, final Format format,
			final XMLEventFactory eventfactory, final Text text) throws XMLStreamException {
		final List<Text> list = Collections.singletonList(text);
		final FormatStack fstack = new FormatStack(format);
		final Walker walker = buildWalker(fstack, list, false);
		if (walker.hasNext()) {
			final Content c = walker.next();
			if (c == null) {
				printText(out, fstack, eventfactory, new Text(walker.text()));
			} else if (c.getCType() == CType.Text) {
				printText(out, fstack, eventfactory, (Text)c);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.StAXEventProcessor#process(java.io.XMLEventConsumer,
	 * org.jdom2.Comment, org.jdom2.output.Format)
	 */
	@Override
	public void process(final XMLEventConsumer out, final Format format,
			final XMLEventFactory eventfactory, final Comment comment) throws XMLStreamException {
		printComment(out, new FormatStack(format), eventfactory, comment);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.StAXEventProcessor#process(java.io.XMLEventConsumer,
	 * org.jdom2.ProcessingInstruction, org.jdom2.output.Format)
	 */
	@Override
	public void process(final XMLEventConsumer out, final Format format,
			final XMLEventFactory eventfactory, final ProcessingInstruction pi) throws XMLStreamException {
		FormatStack fstack = new FormatStack(format);
		// Output PI verbatim, disregarding TrAX escaping PIs.
		fstack.setIgnoreTrAXEscapingPIs(true);
		printProcessingInstruction(out, fstack, eventfactory, pi);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.StAXEventProcessor#process(java.io.XMLEventConsumer,
	 * org.jdom2.EntityRef, org.jdom2.output.Format)
	 */
	@Override
	public void process(final XMLEventConsumer out, final Format format,
			final XMLEventFactory eventfactory, final EntityRef entity) throws XMLStreamException {
		printEntityRef(out, new FormatStack(format), eventfactory, entity);
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
	 *        <code>XMLEventConsumer</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param nstack
	 *        the NamespaceStack
	 * @param eventfactory
	 * 		  The XMLEventFactory for creating XMLEvents 
	 * @param doc
	 *        <code>Document</code> to write.
	 * @throws XMLStreamException
	 *         if the destination XMLEventConsumer fails
	 */
	protected void printDocument(final XMLEventConsumer out, final FormatStack fstack,
			final NamespaceStack nstack, final XMLEventFactory eventfactory, final Document doc) throws XMLStreamException {

		if (fstack.isOmitDeclaration()) {
			// this actually writes the declaration as version 1, UTF-8
			out.add(eventfactory.createStartDocument(null, null));
		} else if (fstack.isOmitEncoding()) {
			out.add(eventfactory.createStartDocument(null, "1.0"));
			if (fstack.getLineSeparator() != null) {
				out.add(eventfactory.createCharacters(fstack.getLineSeparator()));
			}
		} else {
			out.add(eventfactory.createStartDocument(fstack.getEncoding(), "1.0"));
			if (fstack.getLineSeparator() != null) {
				out.add(eventfactory.createCharacters(fstack.getLineSeparator()));
			}
		}

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
						out.add(eventfactory.createCharacters(padding));
					}
				} else {
					switch (c.getCType()) {
						case Comment :
							printComment(out, fstack, eventfactory, (Comment) c);
							break;
						case DocType :
							printDocType(out, fstack, eventfactory, (DocType)c);
							break;
						case Element :
							printElement(out, fstack, nstack, eventfactory,
									(Element)c);
							break;
						case ProcessingInstruction :
							printProcessingInstruction(out, fstack, eventfactory, 
									(ProcessingInstruction)c);
							break;
						default :
							// do nothing.
					}
				}
				
			}
			
			if (fstack.getLineSeparator() != null) {
				out.add(eventfactory.createCharacters(fstack.getLineSeparator()));
			}
		}

		out.add(eventfactory.createEndDocument());
		
	}

	/**
	 * This will handle printing of a {@link DocType}.
	 * 
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param eventfactory
	 * 		  The XMLEventFactory for creating XMLEvents 
	 * @param docType
	 *        <code>DocType</code> to write.
	 * @throws XMLStreamException
	 *         if the destination XMLEventConsumer fails
	 */
	protected void printDocType(final XMLEventConsumer out, final FormatStack fstack,
			final XMLEventFactory eventfactory, final DocType docType) throws XMLStreamException {

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
		out.add(eventfactory.createDTD(sw.toString()));
	}

	/**
	 * This will handle printing of a {@link ProcessingInstruction}.
	 * 
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param eventfactory
	 * 		  The XMLEventFactory for creating XMLEvents 
	 * @param pi
	 *        <code>ProcessingInstruction</code> to write.
	 * @throws XMLStreamException
	 *         if the destination XMLEventConsumer fails
	 */
	protected void printProcessingInstruction(final XMLEventConsumer out,
			final FormatStack fstack, final XMLEventFactory eventfactory, final ProcessingInstruction pi)
			throws XMLStreamException {
		String target = pi.getTarget();
		String rawData = pi.getData();
		if (rawData != null && rawData.trim().length() > 0) {
			out.add(eventfactory.createProcessingInstruction(target, rawData));
		} else {
			out.add(eventfactory.createProcessingInstruction(target, ""));
		}
	}

	/**
	 * This will handle printing of a {@link Comment}.
	 * 
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param eventfactory
	 * 		  The XMLEventFactory for creating XMLEvents 
	 * @param comment
	 *        <code>Comment</code> to write.
	 * @throws XMLStreamException
	 *         if the destination XMLEventConsumer fails
	 */
	protected void printComment(final XMLEventConsumer out, final FormatStack fstack,
			final XMLEventFactory eventfactory, final Comment comment) throws XMLStreamException {
		out.add(eventfactory.createComment(comment.getText()));
	}

	/**
	 * This will handle printing of an {@link EntityRef}.
	 * 
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param eventfactory
	 * 		  The XMLEventFactory for creating XMLEvents 
	 * @param entity
	 *        <code>EntotyRef</code> to write.
	 * @throws XMLStreamException
	 *         if the destination XMLEventConsumer fails
	 */
	protected void printEntityRef(final XMLEventConsumer out, final FormatStack fstack,
			final XMLEventFactory eventfactory, final EntityRef entity) throws XMLStreamException {
		out.add(eventfactory.createEntityReference(entity.getName(), null));
	}

	/**
	 * This will handle printing of a {@link CDATA}.
	 * 
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param eventfactory
	 * 		  The XMLEventFactory for creating XMLEvents 
	 * @param cdata
	 *        <code>CDATA</code> to write.
	 * @throws XMLStreamException
	 *         if the destination XMLEventConsumer fails
	 */
	protected void printCDATA(final XMLEventConsumer out, final FormatStack fstack,
			final XMLEventFactory eventfactory, final CDATA cdata) throws XMLStreamException {
		// CDATAs are treated like text, not indented/newline content.
		out.add(eventfactory.createCData(cdata.getText()));
	}

	/**
	 * This will handle printing of a {@link Text}.
	 * 
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param eventfactory
	 * 		  The XMLEventFactory for creating XMLEvents 
	 * @param text
	 *        <code>Text</code> to write.
	 * @throws XMLStreamException
	 *         if the destination XMLEventConsumer fails
	 */
	protected void printText(final XMLEventConsumer out, final FormatStack fstack,
			final XMLEventFactory eventfactory, final Text text) throws XMLStreamException {
		out.add(eventfactory.createCharacters(text.getText()));
	}

	/**
	 * This will handle printing of an {@link Element}.
	 * <p>
	 * This method arranges for outputting the Element infrastructure including
	 * Namespace Declarations and Attributes.
	 * <p>
	 * 
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param nstack
	 *        the NamespaceStack
	 * @param eventfactory
	 * 		  The XMLEventFactory for creating XMLEvents 
	 * @param element
	 *        <code>Element</code> to write.
	 * @throws XMLStreamException
	 *         if the destination XMLEventConsumer fails
	 */
	protected void printElement(final XMLEventConsumer out, final FormatStack fstack,
			final NamespaceStack nstack, final XMLEventFactory eventfactory, 
			final Element element) throws XMLStreamException {

		nstack.push(element);
		try {
			
			Namespace ns = element.getNamespace();
			Iterator<Attribute> ait = element.hasAttributes() ?
					element.getAttributes().iterator() :
						null;
			if (ns == Namespace.NO_NAMESPACE) {
				out.add(eventfactory.createStartElement("", "", element.getName(), 
						new AttIterator(ait, eventfactory, fstack.isSpecifiedAttributesOnly()), 
						new NSIterator(nstack.addedForward().iterator(), eventfactory)));
			} else if ("".equals(ns.getPrefix())) {
				out.add(eventfactory.createStartElement("", ns.getURI(), element.getName(), 
						new AttIterator(ait, eventfactory, fstack.isSpecifiedAttributesOnly()), 
						new NSIterator(nstack.addedForward().iterator(), eventfactory)));
			} else {
				out.add(eventfactory.createStartElement(ns.getPrefix(), ns.getURI(), element.getName(), 
						new AttIterator(ait, eventfactory, fstack.isSpecifiedAttributesOnly()), 
						new NSIterator(nstack.addedForward().iterator(), eventfactory)));
			}
			ait = null;
			
			final List<Content> content = element.getContent();
			
			if (!content.isEmpty()) {
				TextMode textmode = fstack.getTextMode();
				
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
					
					final Walker walker = buildWalker(fstack, content, false);
					if (walker.hasNext()) {
						if (!walker.isAllText() && fstack.getPadBetween() != null) {
							// we need to newline/indent
							final String indent = fstack.getPadBetween();
							printText(out, fstack, eventfactory, new Text(indent));
						}
						
						printContent(out, fstack, nstack, eventfactory, walker);
						
						if (!walker.isAllText() && fstack.getPadLast() != null) {
							// we need to newline/indent
							final String indent = fstack.getPadLast(); 
							printText(out, fstack, eventfactory, new Text(indent));
						}
					}
				} finally {
					fstack.pop();
				}
				
			}
			
			out.add(eventfactory.createEndElement(element.getNamespacePrefix(), 
					element.getNamespaceURI(), element.getName(), 
					new NSIterator(nstack.addedReverse().iterator(), eventfactory)));
		

		} finally {
			nstack.pop();
		}
	}

	/**
	 * This will handle printing of a List of {@link Content}.
	 * <p>
	 * 
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param nstack
	 *        the NamespaceStack
	 * @param eventfactory
	 * 		  The XMLEventFactory for creating XMLEvents 
	 * @param walker
	 *        <code>Walker</code> of <code>Content</code> to write.
	 * @throws XMLStreamException
	 *         if the destination XMLEventConsumer fails
	 */
	protected void printContent(final XMLEventConsumer out,
			final FormatStack fstack, final NamespaceStack nstack,
			final XMLEventFactory eventfactory, final Walker walker)
			throws XMLStreamException {

		while (walker.hasNext()) {
			
			final Content content = walker.next();
			
			if (content == null) {
				if (walker.isCDATA()) {
					printCDATA(out, fstack, eventfactory, new CDATA(walker.text()));
				} else {
					printText(out, fstack, eventfactory, new Text(walker.text()));
				}
			} else {
				switch (content.getCType()) {
					case CDATA:
						printCDATA(out, fstack, eventfactory, (CDATA) content);
						break;
					case Comment:
						printComment(out, fstack, eventfactory, (Comment) content);
						break;
					case Element:
						printElement(out, fstack, nstack, eventfactory, (Element) content);
						break;
					case EntityRef:
						printEntityRef(out, fstack, eventfactory, (EntityRef) content);
						break;
					case ProcessingInstruction:
						printProcessingInstruction(out, fstack, eventfactory,
								(ProcessingInstruction) content);
						break;
					case Text:
						printText(out, fstack, eventfactory, (Text) content);
						break;
					case DocType:
						printDocType(out, fstack, eventfactory, (DocType) content);
						break;
					default:
						throw new IllegalStateException(
								"Unexpected Content " + content.getCType());
		
				}
			}
		}
	}
}
