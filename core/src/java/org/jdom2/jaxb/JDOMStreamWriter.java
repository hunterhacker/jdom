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
package org.jdom2.jaxb;

import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.jdom2.DefaultJDOMFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMFactory;
import org.jdom2.Namespace;
import org.jdom2.Text;
import org.jdom2.util.NamespaceStack;

/**
 * An {@link XMLStreamWriter} implementation that writes XML data to a new JDOM
 * {@link Document}. The document can be retrieved using {@link #getDocument() }.
 * 
 * @author gordon burgett https://github.com/gburgett
 */
public class JDOMStreamWriter implements XMLStreamWriter {

	private static final DefaultJDOMFactory DEFFAC = new DefaultJDOMFactory();

	private final NamespaceStack nstack = new NamespaceStack();
	private final boolean repairnamespace;

	private Document document = null;
	private Element current = null;
	private Element attelement = null;
	private Text text = null;
	
	private final JDOMFactory factory;

	private StreamWriterState state = StreamWriterState.BEFORE_DOCUMENT_START;

	/**
	 * Create a JDOMStreamWriter with the default JDOMFactory for creating JDOM
	 * Content.
	 */
	public JDOMStreamWriter() {
		this(DEFFAC, true);
	}

	/**
	 * Create a JDOMStreamWriter with the specified JDOMFactory for creating
	 * JDOM Content.
	 * 
	 * @param fac
	 *        The JDOMFactory to use.
	 * @param repairnamespace
	 *        If true, then repair namespace errors.
	 */
	public JDOMStreamWriter(final JDOMFactory fac, final boolean repairnamespace) {
		this.factory = fac;
		this.repairnamespace = repairnamespace;
	}

	/**
	 * Gets the {@link Document} that was created by this writer. Only available
	 * after {@link #writeEndDocument() } has been called, and before the Writer
	 * is closed.
	 * 
	 * @return The created {@link Document}
	 */
	public Document getDocument() {
		if (state == StreamWriterState.DOCUMENT_ENDED) {
			return document;
		}
		
		if (state == StreamWriterState.CLOSED) {
			throw new IllegalStateException("Writer is closed");
		}

		throw new IllegalStateException(
				"Cannot get Document until writer has ended the document");

	}

	/**
	 * Simple method that implements the start-element logic without the spec-required checks
	 * for null values.
	 * @param prefix The prefix for the namespace (may be null).
	 * @param localName The localName for the Element
	 * @param namespaceURI The namespaceURI (null implies "");
	 * @throws XMLStreamException if the stream is not in a good state for an Element.
	 */
	private void buildStartElement(final String prefix, final String localName,
			final String namespaceURI) throws XMLStreamException {
		
		final Namespace ns = Namespace.getNamespace(prefix, namespaceURI);
		final Element e = factory.element(localName, ns);

		switch (state) {
			case DOCUMENT_START:
				factory.setRoot(document, e);
				current = e;
				state = StreamWriterState.IN_ELEMENT;
				break;

			case IN_ELEMENT:
				factory.addContent(current, e);
				current = e;
				break;

			default:
				throw new XMLStreamException(
						"Cannot write new element when in state " + state);
		}
		attelement = current;
		text = null;
	}

	@Override
	public void writeStartElement(final String localName) throws XMLStreamException {
		if (localName == null) {
			throw new XMLStreamException("Cannot have a null localname");
		}
		this.buildStartElement(null, localName, null);
	}

	@Override
	public void writeStartElement(final String namespaceURI, final String localName) 
			throws XMLStreamException {
		if (namespaceURI == null) {
			throw new XMLStreamException("Cannot have a null namespaceURI");
		}
		if (localName == null) {
			throw new XMLStreamException("Cannot have a null localname");
		}
		this.buildStartElement(null, localName, namespaceURI);
	}

	@Override
	public void writeStartElement(final String prefix, final String localName,
			final String namespaceURI) throws XMLStreamException {
		if (prefix == null) {
			throw new XMLStreamException("Cannot have a null prefix");
		}
		if (localName == null) {
			throw new XMLStreamException("Cannot have a null localName");
		}
		if (namespaceURI == null) {
			throw new XMLStreamException("Cannot have a null namespaceURI");
		}
		buildStartElement(prefix, localName, namespaceURI);
	}

	/**
	 * Simple method that implements the empty-element logic without the spec-required
	 * null-check logic
	 * @param prefix The namespace prefix (may be null).
	 * @param localName The Element tag
	 * @param namespaceURI The namespace URI (may be null).
	 * @throws XMLStreamException If the stream is not in an appropriate state for a new Element.
	 */
	private final void buildEmptyElement(final String prefix,
			final String localName, final String namespaceURI) throws XMLStreamException {
		final Namespace ns = Namespace.getNamespace(prefix, namespaceURI);

		final Element e = factory.element(localName, ns);

		switch (state) {
			case DOCUMENT_START:
				// an empty root element
				factory.setRoot(document, e);
				state = StreamWriterState.OUT_ELEMENT;
				break;

			case IN_ELEMENT:
				factory.addContent(current, e);
				// still in element
				break;

			default:
				throw new XMLStreamException(
						"Cannot write new element when in state " + state);
		}
		attelement = e;
		text = null;
	}

	@Override
	public void writeEmptyElement(final String localName)
			throws XMLStreamException {
		if (localName == null) {
			throw new XMLStreamException("Cannot have a null localname");
		}
		this.buildEmptyElement(null, localName, null);
	}

	@Override
	public void writeEmptyElement(final String namespaceURI,
			final String localName) throws XMLStreamException {
		if (namespaceURI == null) {
			throw new XMLStreamException("Cannot have a null namespaceURI");
		}
		if (localName == null) {
			throw new XMLStreamException("Cannot have a null localname");
		}
		this.buildEmptyElement(null, localName, null);
	}

	@Override
	public void writeEmptyElement(final String prefix, final String localName,
			final String namespaceURI) throws XMLStreamException {
		if (prefix == null) {
			throw new XMLStreamException("Cannot have a null prefix");
		}
		if (localName == null) {
			throw new XMLStreamException("Cannot have a null localname");
		}
		if (namespaceURI == null) {
			throw new XMLStreamException("Cannot have a null namespaceURI");
		}
		buildEmptyElement(prefix, localName, namespaceURI);
	}
	
	@Override
	public void writeEndElement() throws XMLStreamException {
		if (state != StreamWriterState.IN_ELEMENT) {
			throw new XMLStreamException(
					"Cannot write end element when in state " + state);
		}
		current = current.getParentElement();
		if (current == null) {
			// back to root element
			state = StreamWriterState.OUT_ELEMENT;
		}
		attelement = null;
		text = null;
	}

	@Override
	public void writeEndDocument() throws XMLStreamException {
		if (state != StreamWriterState.OUT_ELEMENT) {
			throw new IllegalStateException(
					"Cannot write end document before writing end of root element");
		}

		attelement = null;
		text = null;
		state = StreamWriterState.DOCUMENT_ENDED;
	}

	@Override
	public void close() throws XMLStreamException {
		this.document = null;
		this.current = null;
		this.state = StreamWriterState.CLOSED;
		attelement = null;
		text = null;
	}

	@Override
	public void flush() throws XMLStreamException {
		// flush does nothing.
	}

	private final void buildAttribute(final String prefix,
			final String namespaceURI, final String localName,
			final String value) throws XMLStreamException {
		if (state != StreamWriterState.IN_ELEMENT) {
			throw new IllegalStateException(
					"Cannot write attribute when in state " + state);
		}
		if (attelement == null) { 
			throw new IllegalStateException("Cannot add Attributes to an Element after other content was added.");
		}
		if (localName == null) {
			throw new XMLStreamException("localName is not allowed to be null");
		}
		if (value == null) {
			throw new XMLStreamException("localName is not allowed to be null");
		}
		factory.setAttribute(
				attelement,
				factory.attribute(localName, value,
						Namespace.getNamespace(prefix, namespaceURI)));
	}

	@Override
	public void writeAttribute(final String localName, final String value)
			throws XMLStreamException {
		buildAttribute(null, null, localName, value);
	}

	@Override
	public void writeAttribute(final String prefix, final String namespaceURI,
			final String localName, final String value)
			throws XMLStreamException {
		buildAttribute(prefix, namespaceURI, localName, value);
	}

	@Override
	public void writeAttribute(final String namespaceURI,
			final String localName, final String value)
			throws XMLStreamException {
		buildAttribute(null, namespaceURI, localName, value);
	}

	@Override
	public void writeNamespace(final String prefix, final String namespaceURI)
			throws XMLStreamException {
		final Namespace ns = Namespace.getNamespace(prefix, namespaceURI);
		switch (state) {
			case IN_ELEMENT:
				current.addNamespaceDeclaration(ns);
				break;

			default:
				throw new IllegalStateException(
						"Cannot write namespace when in state " + state);
		}
	}

	@Override
	public void writeDefaultNamespace(final String namespaceURI)
			throws XMLStreamException {
		this.writeNamespace(null, namespaceURI);
	}

	@Override
	public void writeComment(final String data) throws XMLStreamException {
		factory.addContent(state == StreamWriterState.DOCUMENT_START ? document : current, 
				factory.comment(data));
		attelement = null;
		text = null;
	}

	@Override
	public void writeProcessingInstruction(final String target)
			throws XMLStreamException {
		factory.addContent(state == StreamWriterState.DOCUMENT_START ? document : current, 
				factory.processingInstruction(target));
		attelement = null;
		text = null;
	}

	@Override
	public void writeProcessingInstruction(final String target,
			final String data) throws XMLStreamException {
		factory.addContent(state == StreamWriterState.DOCUMENT_START ? document : current, 
				factory.processingInstruction(target, data));
		attelement = null;
		text = null;
	}

	@Override
	public void writeCData(final String data) throws XMLStreamException {
		factory.addContent(current, factory.cdata(data));
		attelement = null;
		text = null;
	}

	@Override
	public void writeDTD(final String dtd) throws XMLStreamException {
		// FIXME to do ... ?
		throw new UnsupportedOperationException("not supported yet");
	}

	@Override
	public void writeEntityRef(final String name) throws XMLStreamException {
		factory.addContent(current, factory.entityRef(name));
		attelement = null;
		text = null;
	}

	@Override
	public void writeStartDocument() throws XMLStreamException {
		this.writeStartDocument(null, null);
	}

	@Override
	public void writeStartDocument(final String version)
			throws XMLStreamException {
		this.writeStartDocument(null, version);
	}

	@Override
	public void writeStartDocument(final String encoding, final String version)
			throws XMLStreamException {
		
		// JDOM has no support for XML Version specification/handling
		
		if (this.state != StreamWriterState.BEFORE_DOCUMENT_START) {
			throw new IllegalStateException(
					"Cannot write start document while in state " + this.state);
		}

		this.document = factory.document(null);
		if (encoding != null && !"".equals(encoding))
			this.document.setProperty("ENCODING", encoding);

		// TODO: how to set version?

		this.state = StreamWriterState.DOCUMENT_START;
		attelement = null;
	}

	@Override
	public void writeCharacters(final String chars) throws XMLStreamException {
		attelement = null;
		if (chars == null) {
			return;
		}
		switch (state) {
			case IN_ELEMENT:
				if (text != null) {
					text.append(chars);
				} else {
					text = factory.text(chars);
					factory.addContent(current, text);
				}
				return;
			case DOCUMENT_START:
			case OUT_ELEMENT:
				// JDOM Does not support text at document level (it can only be space anyway).
				return;
			default:
				throw new XMLStreamException("Unable to add Characters at this point in the stream.");
		}
	}

	@Override
	public void writeCharacters(final char[] chars, final int start,
			final int len) throws XMLStreamException {
		writeCharacters(new String(chars, start, len));
	}

	@Override
	public String getPrefix(final String uri) throws XMLStreamException {
		for (Namespace n : getCurrentNamespaces()) {
			if (uri == null) {
				if (n.getURI() == null) {
					return n.getPrefix();
				}
			} else {
				if (uri.equals(n.getURI())) {
					return n.getPrefix();
				}
			}
		}

		return null;
	}

	@Override
	public void setPrefix(final String prefix, final String uri)
			throws XMLStreamException {
		if (state != StreamWriterState.IN_ELEMENT) {
			throw new IllegalStateException(
					"Attempt to set prefix outside the context of an element");
		}
		current.addNamespaceDeclaration(Namespace.getNamespace(prefix, uri));
	}

	@Override
	public void setDefaultNamespace(final String uri) throws XMLStreamException {
		if (state != StreamWriterState.IN_ELEMENT) {
			throw new IllegalStateException(
					"Attempt to set prefix outside the context of an element");
		}
		current.addNamespaceDeclaration(Namespace.getNamespace(uri));
	}

	@Override
	public void setNamespaceContext(final NamespaceContext context)
			throws XMLStreamException {
		// FIXME Todo....
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public NamespaceContext getNamespaceContext() {
		final List<Namespace> namespaces = getCurrentNamespaces();

		return new org.jdom2.jaxb.JDOMNamespaceContext(namespaces);
	}

	@Override
	public Object getProperty(String name) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	// *****************************
	// Private methods.
	// *****************************

	private List<Namespace> getCurrentNamespaces() {
		switch (state) {
			case IN_ELEMENT:
				return current.getNamespacesInScope();

			case DOCUMENT_START:
			case OUT_ELEMENT:
				return document.getNamespacesInScope();

			default:
				throw new IllegalStateException(
						"Attempt to get namespaces in unsupported state "
								+ this.state);
		}
	}

	private enum StreamWriterState {
		BEFORE_DOCUMENT_START, DOCUMENT_START, IN_ELEMENT, OUT_ELEMENT, DOCUMENT_ENDED, CLOSED
	}
}
