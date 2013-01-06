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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.jdom2.DefaultJDOMFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMConstants;
import org.jdom2.JDOMFactory;
import org.jdom2.Namespace;
import org.jdom2.Parent;
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
	private static final Namespace[] EMPTYNSA = new Namespace[0];

	// The optional global namespace context for namespace bindings.
	private NamespaceContext globalcontext = null;

	// The active Element NamespaceStack of actual used Namespaces.
	private final NamespaceStack usednsstack = new NamespaceStack();
	// The stack of namespace bindings which may not necessarily be used in an element
	private final NamespaceStack boundstack = new NamespaceStack();
	
	// The namespaces pending in the current Element that need to be actively bound.
	private LinkedList<Namespace> pendingns = new LinkedList<Namespace>();

	// Are we throwing exceptions for namespace problems, or fixing them.
	private final boolean repairnamespace;

	private Document document = null;
	private Parent parent = null;
	private Element activeelement = null;
	private boolean isempty = false;
	private Text activetext = null;
	
	private int genprefix = 0;
	
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
		// create an empty level in the namespace stack.
		boundstack.push(new Namespace[0]);
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
		// ignore version.
		
		if (this.state != StreamWriterState.BEFORE_DOCUMENT_START) {
			throw new IllegalStateException(
					"Cannot write start document while in state " + this.state);
		}

		this.document = factory.document(null);
		if (encoding != null && !"".equals(encoding))
			this.document.setProperty("ENCODING", encoding);


		this.state = StreamWriterState.DOCUMENT_START;
		activeelement = null;
	}

	@Override
	public void setNamespaceContext(final NamespaceContext context)
			throws XMLStreamException {
		if (state != StreamWriterState.DOCUMENT_START) {
			throw new XMLStreamException("Can only set the NamespaceContext at the Document start");
		}
		globalcontext = context;
	}

	@Override
	public void writeDTD(final String dtd) throws XMLStreamException {
		// FIXME to do ... ?
		throw new UnsupportedOperationException("not supported yet");
	}

	@Override
	public String getPrefix(final String uri) throws XMLStreamException {
		for (Namespace n : boundstack) {
			if (n.getURI().equals(uri)) {
				return n.getPrefix();
			}
		}
		if (globalcontext != null) {
			return globalcontext.getPrefix(uri);
		}
		return null;
	}

	@Override
	public void setPrefix(final String prefix, final String uri)
			throws XMLStreamException {
		if (prefix == null) {
			throw new IllegalArgumentException("prefix may not be null");
		}
		if (state != StreamWriterState.IN_ELEMENT && state != StreamWriterState.DOCUMENT_START) {
			throw new IllegalStateException(
					"Attempt to set prefix at an illegal stream state.");
		}
		
		/*
		 * Setting a prefix (or a default namespace) is not the same as writing the namespace.
		 * It is only a 'binding' of the URI to a prefix. This binding is used for two purposes:
		 * - any prefixes bound in here are used if a prefix is not specified in the startelement
		 * - if repairnamespace is false, then all used namespaces must first be pre-bound
		 * This method is specified to allow a null URI. This makes no sense unless the prefix is ""
		 * and will cause a Namespace exception if the prefix is not "".
		 */

		final Namespace ns = Namespace.getNamespace(prefix, uri);
		if (!boundstack.isInScope(ns)) {
			ArrayList<Namespace> al = new ArrayList<Namespace>();
			for (Namespace n : boundstack) {
				if (n.getPrefix().equals(prefix)) {
					// do not rebind the same prefix with the old URI.
					continue;
				}
				al.add(n);
			}
			// bind the new URI to the prefix.
			al.add(ns);
			// kill the previous binding.
			boundstack.pop();
			// reset the binding.
			boundstack.push(al);
		}
	}

	@Override
	public void setDefaultNamespace(final String uri) throws XMLStreamException {
		setPrefix(JDOMConstants.NS_PREFIX_DEFAULT, uri);
	}

	/**
	 * Simple method that implements the start-element logic without the spec-required checks
	 * for null values.
	 * @param prefix The prefix for the namespace (may be null).
	 * @param localName The localName for the Element
	 * @param namespaceURI The namespaceURI (null implies "");
	 * @param withpfx true if the prefix is user-specified.
	 * @throws XMLStreamException if the stream is not in a good state for an Element.
	 */
	private void buildStartElement(final String prefix, final String localName,
			final String namespaceURI, boolean withpfx) throws XMLStreamException {
		
		flushActiveElement();
		flushActiveText();
		
		final Namespace ns = resolveElementNamespace(prefix, namespaceURI, withpfx);
		final Element e = factory.element(localName, ns);

		switch (state) {
			case DOCUMENT_START:
				factory.setRoot(document, e);
				parent = e;
				state = StreamWriterState.IN_ELEMENT;
				break;

			case IN_ELEMENT:
				factory.addContent(parent, e);
				parent = e;
				break;

			default:
				throw new XMLStreamException(
						"Cannot write new element when in state " + state);
		}
		activeelement = e;
		isempty = false;
	}

	private Namespace resolveElementNamespace(String prefix, String namespaceURI,
			boolean withpfx) throws XMLStreamException {
		final Namespace ns = Namespace.getNamespace(prefix, namespaceURI);
		if (withpfx) {
			final Namespace bnd = boundstack.getNamespaceForPrefix(prefix);
			if (bnd == null || bnd == ns) {
				// no existing binding for prefix, or same binding.
				return ns;
			}
			// prefix is bound to a different URI
			if (repairnamespace) {
				return Namespace.getNamespace(generatePrefix(), namespaceURI);
			}
			throw new XMLStreamException("Namespace prefix " + prefix + 
					" in this scope is bound to a different URI '" + bnd.getURI() + 
					"' (repairing not set for this XMLStreamWriter)."); 
		}
		// see if the default namespace is bound correctly...
		if (boundstack.getNamespaceForPrefix("") == ns) {
			return ns;
		}
		// nope, so see if there's any other bound namespace with the same URI.
		final Namespace bound = boundstack.getFirstNamespaceForURI(namespaceURI);
		if (bound != null) {
			return bound;
		}
		// there are no existing bindings with the specified URI.
		if (repairnamespace) {
			return Namespace.getNamespace(generatePrefix(), namespaceURI);
		}
		throw new XMLStreamException("Namespace URI " + namespaceURI + 
				" is not bound in this scope (repairing not set for this XMLStreamWriter)."); 
	}

	private Namespace resolveAttributeNamespace(String prefix, String namespaceURI,
			boolean withpfx) throws XMLStreamException {
		final Namespace ns = Namespace.getNamespace(prefix, namespaceURI);
		if (ns == Namespace.NO_NAMESPACE) {
			return ns;
		}
		if (withpfx && !"".equals(prefix)) {

			Namespace bnd = boundstack.getNamespaceForPrefix(prefix);
			
			if (bnd == null || bnd == ns) {
				// no existing binding for prefix, or same binding.
				return ns;
			}
			// prefix is bound to a different URI
			if (repairnamespace) {
				final Namespace gen = Namespace.getNamespace(generatePrefix(), namespaceURI);
				setPrefix(gen.getPrefix(), gen.getURI());
				return gen;
			}
			throw new XMLStreamException("Namespace prefix " + prefix + 
					" in this scope is bound to a different URI '" + bnd.getURI() + 
					"' (repairing not set for this XMLStreamWriter)."); 
		}
		
		// see if there's any other bound namespace with the same URI.
		final Namespace[] bound = boundstack.getAllNamespacesForURI(namespaceURI);
		for (Namespace b : bound) {
			if (!"".equals(b.getPrefix())) {
				// use an existing prefixed Namespace binding.
				// good for both repairing and non-repairing
				return b;
			}
		}
		// there are no existing prefixed bindings with the specified URI.
		if (repairnamespace || bound.length > 0) {
			return Namespace.getNamespace(generatePrefix(), namespaceURI);
		}
		throw new XMLStreamException("Namespace URI " + namespaceURI + 
				" is not bound in this attribute scope (repairing not set for this XMLStreamWriter)."); 
	}

	private String generatePrefix() {
		String pfx = String.format("ns%03d", ++genprefix);
		while (boundstack.getNamespaceForPrefix(pfx) != null) {
			pfx = String.format("ns%03d", ++genprefix);
		}
		return pfx;
	}

	@Override
	public void writeStartElement(final String localName) throws XMLStreamException {
		writeStartElement("", localName);
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
		this.buildStartElement("", localName, namespaceURI, false);
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
		buildStartElement(prefix, localName, namespaceURI, true);
	}

	/**
	 * Simple method that implements the empty-element logic without the spec-required
	 * null-check logic
	 * @param prefix The namespace prefix (may be null).
	 * @param localName The Element tag
	 * @param namespaceURI The namespace URI (may be null).
	 * @throws XMLStreamException If the stream is not in an appropriate state for a new Element.
	 */
	private final void buildEmptyElement(final String prefix, final String localName,
			final String namespaceURI, final boolean withpfx) throws XMLStreamException {
		
		flushActiveElement();
		flushActiveText();
		
		final Namespace ns = resolveElementNamespace(prefix, namespaceURI, withpfx);

		final Element e = factory.element(localName, ns);

		switch (state) {
			case DOCUMENT_START:
				// an empty root element
				factory.setRoot(document, e);
				state = StreamWriterState.OUT_ELEMENT;
				break;

			case IN_ELEMENT:
				factory.addContent(parent, e);
				// still in element
				break;

			default:
				throw new XMLStreamException(
						"Cannot write new element when in state " + state);
		}
		activeelement = e;
		isempty = true;
	}

	@Override
	public void writeEmptyElement(final String localName)
			throws XMLStreamException {
		if (localName == null) {
			throw new XMLStreamException("Cannot have a null localname");
		}
		this.buildEmptyElement("", localName, "", false);
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
		this.buildEmptyElement("", localName, namespaceURI, false);
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
		buildEmptyElement(prefix, localName, namespaceURI, true);
	}
	
	@Override
	public void writeDefaultNamespace(final String namespaceURI)
			throws XMLStreamException {
		this.writeNamespace("", namespaceURI);
	}

	@Override
	public void writeNamespace(final String prefix, final String namespaceURI)
			throws XMLStreamException {
		if (prefix == null || JDOMConstants.NS_PREFIX_XMLNS.equals(prefix)) {
			writeNamespace("", namespaceURI);
			return;
		}
		final Namespace ns = Namespace.getNamespace(prefix, namespaceURI);
		switch (state) {
			case IN_ELEMENT:
				if (activeelement == null) {
					throw new IllegalStateException("Not able to write a Namespace after adding content.");
				}
				pendingns.add(ns);
				break;

			default:
				throw new IllegalStateException(
						"Cannot write namespace when in state " + state);
		}
	}

	private final void buildAttribute(final String prefix,
			final String namespaceURI, final String localName,
			final String value, final boolean withpfx) throws XMLStreamException {
		if (state != StreamWriterState.IN_ELEMENT) {
			throw new IllegalStateException(
					"Cannot write attribute when in state " + state);
		}
		if (localName == null) {
			throw new XMLStreamException("localName is not allowed to be null");
		}
		if (value == null) {
			throw new XMLStreamException("value is not allowed to be null");
		}
		if (activeelement == null) { 
			throw new IllegalStateException("Cannot add Attributes to an Element after other content was added.");
		}
		
		Namespace ns = resolveAttributeNamespace(prefix, namespaceURI, withpfx);
		
		factory.setAttribute(activeelement, factory.attribute(localName, value, ns));
	}

	@Override
	public void writeAttribute(final String localName, final String value)
			throws XMLStreamException {
		buildAttribute("", "", localName, value, false);
	}

	@Override
	public void writeAttribute(final String namespaceURI,
			final String localName, final String value)
			throws XMLStreamException {
		buildAttribute("", namespaceURI == null ? "" : namespaceURI, localName, value, false);
	}
	
	@Override
	public void writeAttribute(final String prefix, final String namespaceURI,
			final String localName, final String value)
			throws XMLStreamException {
		buildAttribute(prefix == null ? "" : prefix, namespaceURI == null ? "" : namespaceURI, localName, value, true);
	}

	private final void flushActiveElement() {
		if (activeelement != null) {
			
			/*
			 * Add any written namespaces to the element as declared namespaces
			 * unless they are implied namespaces used by attributes, or something.
			 * If this Element is expecting content (it's not empty) then we also
			 * push the modified Element on to the Namespace stack.
			 */

			boolean mod = false;
			usednsstack.push(activeelement);
			for (Namespace ns : pendingns) {
				if (!usednsstack.isInScope(ns)) {
					activeelement.addNamespaceDeclaration(ns);
					mod = true;
				}
			}
			pendingns.clear();
			
			if (mod) {
				usednsstack.pop();
				if (isempty) {
					activeelement = null;
					return;
				}
				usednsstack.push(activeelement);
			}
			if (isempty) {
				activeelement = null;
				return;
			}
			boundstack.push(activeelement);
			boundstack.push(EMPTYNSA);
			
			activeelement = null;
		}
	}

	private final void flushActiveText() {
		activetext = null;
	}

	@Override
	public void writeComment(final String data) throws XMLStreamException {
		flushActiveElement();
		flushActiveText();
		factory.addContent(state == StreamWriterState.DOCUMENT_START ? document : parent, 
				factory.comment(data));
	}

	@Override
	public void writeProcessingInstruction(final String target)
			throws XMLStreamException {
		flushActiveElement();
		flushActiveText();
		factory.addContent(state == StreamWriterState.DOCUMENT_START ? document : parent, 
				factory.processingInstruction(target));
	}

	@Override
	public void writeProcessingInstruction(final String target,
			final String data) throws XMLStreamException {
		flushActiveElement();
		flushActiveText();
		factory.addContent(state == StreamWriterState.DOCUMENT_START ? document : parent, 
				factory.processingInstruction(target, data));
	}

	@Override
	public void writeCData(final String data) throws XMLStreamException {
		flushActiveElement();
		flushActiveText();
		factory.addContent(parent, factory.cdata(data));
	}

	@Override
	public void writeEntityRef(final String name) throws XMLStreamException {
		flushActiveElement();
		flushActiveText();
		factory.addContent(parent, factory.entityRef(name));
	}
	
	@Override
	public void writeCharacters(final String chars) throws XMLStreamException {
		flushActiveElement();
		if (chars == null) {
			return;
		}
		switch (state) {
			case IN_ELEMENT:
				if (activetext != null) {
					activetext.append(chars);
				} else {
					activetext = factory.text(chars);
					factory.addContent(parent, activetext);
				}
				return;
			case DOCUMENT_START:
			case OUT_ELEMENT:
				// JDOM Does not support activetext at document level (it can only be space anyway).
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
	public void writeEndElement() throws XMLStreamException {
		if (state != StreamWriterState.IN_ELEMENT) {
			throw new XMLStreamException(
					"Cannot write end element when in state " + state);
		}
		flushActiveElement();
		flushActiveText();
		usednsstack.pop();
		boundstack.pop();
		boundstack.pop();
		parent = parent.getParent();
		if (parent == document) {
			// back to root element
			state = StreamWriterState.OUT_ELEMENT;
		}
	}

	@Override
	public void writeEndDocument() throws XMLStreamException {
		if (state != StreamWriterState.OUT_ELEMENT) {
			throw new IllegalStateException(
					"Cannot write end document before writing end of root element");
		}
		state = StreamWriterState.DOCUMENT_ENDED;
	}

	@Override
	public void close() throws XMLStreamException {
		this.document = null;
		this.parent = null;
		this.state = StreamWriterState.CLOSED;
		activeelement = null;
		activetext = null;
	}

	@Override
	public void flush() throws XMLStreamException {
		// flush does nothing.
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
				return parent.getNamespacesInScope();

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
