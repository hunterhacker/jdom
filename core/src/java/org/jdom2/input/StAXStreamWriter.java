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
package org.jdom2.input;

import java.util.ArrayList;
import java.util.LinkedList;

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
import org.jdom2.util.JDOMNamespaceContext;
import org.jdom2.util.NamespaceStack;

/**
 * An {@link XMLStreamWriter} implementation that writes XML data to a new JDOM
 * {@link Document}. The document can be retrieved using {@link #getDocument() }.
 * <p>
 * This class is the inverse of the class {@link StAXStreamBuilder} in the sense that
 * this class is written to (it's an XMLStreamWriter implementation) to create a JDOM
 * Document whereas the StAXStreamBuilder <strong>reads from</strong> a user-supplied
 * XMLStreamReader. It is the difference between a 'push' concept and a 'pull' concept.
 * <p>
 * An interesting read for people using this class:
 * <a href="http://ws.apache.org/axiom/devguide/ch05.html">Apache Axiom notes on setPrefix()</a>.
 * 
 * @author gordon burgett https://github.com/gburgett
 * @author Rolf Lear
 * @since JDOM 2.1
 */
public class StAXStreamWriter implements XMLStreamWriter {

	private static final DefaultJDOMFactory DEFFAC = new DefaultJDOMFactory();

	// The optional global namespace context for namespace bindings.
	private NamespaceContext globalcontext = null;

	// The active Element NamespaceStack of actual used Namespaces.
	private NamespaceStack usednsstack = new NamespaceStack();
	// The stack of namespace bindings which may not necessarily be used in an element
	private NamespaceStack boundstack = new NamespaceStack();
	
	// The namespaces pending in the current Element that need to be actively bound.
	private LinkedList<Namespace> pendingns = new LinkedList<Namespace>();

	// Are we throwing exceptions for namespace problems, or fixing them.
	private final boolean repairnamespace;

	private Document document = null;
	private boolean done = false;
	private Parent parent = null;
	private Element activeelement = null;
	private boolean isempty = false;
	private Text activetext = null;
	
	private int genprefix = 0;
	
	private final JDOMFactory factory;

	/**
	 * Create a StAXStreamWriter with the default JDOMFactory for creating JDOM
	 * Content.
	 */
	public StAXStreamWriter() {
		this(DEFFAC, true);
	}

	/**
	 * Create a StAXStreamWriter with the specified JDOMFactory for creating
	 * JDOM Content.
	 * 
	 * @param fac
	 *        The JDOMFactory to use.
	 * @param repairnamespace
	 *        If true, then repair namespace errors.
	 */
	public StAXStreamWriter(final JDOMFactory fac, final boolean repairnamespace) {
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
		if (done && document != null) {
			return document;
		}
		
		if (done) {
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
		
		if (done || document != null) {
			throw new IllegalStateException(
					"Cannot write start document twice.");
		}

		document = factory.document(null);
		parent = document;
		if (encoding != null && !"".equals(encoding))
			this.document.setProperty("ENCODING", encoding);

		activeelement = null;
	}

	@Override
	public void setNamespaceContext(final NamespaceContext context)
			throws XMLStreamException {
		if (document == null || document.hasRootElement()) {
			throw new XMLStreamException("Can only set the NamespaceContext at the Document start");
		}
		globalcontext = context;
	}

	@Override
	public String getPrefix(final String uri) throws XMLStreamException {
		if (document == null) {
			return null;
		}
		final Namespace n = boundstack.getFirstNamespaceForURI(uri);
		if (n != null) {
			return n.getPrefix();
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
		if (prefix.equals(JDOMConstants.NS_PREFIX_XMLNS)) {
			return;
		}
		if (document == null || done) {
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
			final ArrayList<Namespace> al = new ArrayList<Namespace>();
			for (Namespace n : boundstack) {
				if (n.getPrefix().equals(prefix)) {
					// do not rebind the same prefix with the old URI.
					continue;
				}
				al.add(n);
			}
			// bind the new URI to the prefix.
			al.add(ns);
			// kill the previous bindings.
			boundstack.pop();
			// reset the binding.
			boundstack.push(al);
		}
	}

	@Override
	public void setDefaultNamespace(final String uri) throws XMLStreamException {
		setPrefix(JDOMConstants.NS_PREFIX_DEFAULT, uri);
	}

	@Override
	public void writeDTD(final String dtd) throws XMLStreamException {
		// FIXME to do ... ?
		throw new UnsupportedOperationException("not supported yet");
	}

	@Override
	public void writeStartElement(final String localName) throws XMLStreamException {
		final int pos = localName.indexOf(':');
		if (pos >= 0) {
			writeStartElement(localName.substring(0, pos), localName.substring(pos + 1));
		} else {
			writeStartElement("", localName);
		}
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
		final int pos = localName.indexOf(':');
		if (pos >= 0) {
			this.buildElement(localName.substring(0, pos), localName.substring(pos + 1), namespaceURI, false, false);
		} else {
			this.buildElement("", localName, namespaceURI, false, false);
		}
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
		buildElement(prefix, localName, namespaceURI, true, false);
	}

	@Override
	public void writeEmptyElement(final String localName)
			throws XMLStreamException {
		if (localName == null) {
			throw new XMLStreamException("Cannot have a null localname");
		}
		this.buildElement("", localName, "", false, true);
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
		this.buildElement("", localName, namespaceURI, false, true);
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
		buildElement(prefix, localName, namespaceURI, true, true);
	}
	
	@Override
	public void writeDefaultNamespace(final String namespaceURI)
			throws XMLStreamException {
		this.writeNamespace("", namespaceURI);
	}

	@Override
	public void writeNamespace(final String prefix, final String namespaceURI)
			throws XMLStreamException {
		if (activeelement == null) {
			throw new IllegalStateException("Can only write a Namespace after starting an Element" +
					" and before adding content to that Element.");
		}
		if (prefix == null || JDOMConstants.NS_PREFIX_XMLNS.equals(prefix)) {
			// recurse with the "" prefix.
			// yet another special case....
			// if the element itself was written out without a prefix, and without a namespace
			// then we update the element to have the same namespace as we have here.
			// this is required to support some native Java Transform engines that do not
			// supply content in the right order.
			if ("".equals(activeelement.getNamespacePrefix())) {
				activeelement.setNamespace(Namespace.getNamespace("", namespaceURI));
			}
			writeNamespace("", namespaceURI);
		} else {
			pendingns.add(Namespace.getNamespace(prefix, namespaceURI));
		}
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

	@Override
	public void writeComment(final String data) throws XMLStreamException {
		if (document == null || done) {
			throw new XMLStreamException("Can only add a Comment to the Document or an Element.");
		}
		flushActiveElement();
		flushActiveText();
		factory.addContent(parent, factory.comment(data));
	}

	@Override
	public void writeProcessingInstruction(final String target)
			throws XMLStreamException {
		if (document == null || done) {
			throw new XMLStreamException("Can only add a ProcessingInstruction to the Document or an Element.");
		}
		flushActiveElement();
		flushActiveText();
		factory.addContent(parent, factory.processingInstruction(target));
	}

	@Override
	public void writeProcessingInstruction(final String target,
			final String data) throws XMLStreamException {
		if (document == null || done) {
			throw new XMLStreamException("Can only add a ProcessingInstruction to the Document or an Element.");
		}
		flushActiveElement();
		flushActiveText();
		factory.addContent(parent, factory.processingInstruction(target, data));
	}

	@Override
	public void writeCData(final String data) throws XMLStreamException {
		if (!(parent instanceof Element)) {
			throw new XMLStreamException("Can only writeCDATA() inside an Element.");
		}
		flushActiveElement();
		flushActiveText();
		factory.addContent(parent, factory.cdata(data));
	}

	@Override
	public void writeEntityRef(final String name) throws XMLStreamException {
		if (!(parent instanceof Element)) {
			throw new XMLStreamException("Can only writeEntityRef() inside an Element.");
		}
		flushActiveElement();
		flushActiveText();
		factory.addContent(parent, factory.entityRef(name));
	}
	
	@Override
	public void writeCharacters(final String chars) throws XMLStreamException {
		if (document == null || done) {
			throw new XMLStreamException("Unable to add Characters at this point in the stream.");
		}
		flushActiveElement();
		if (chars == null) {
			return;
		}
		if (parent instanceof Element) {
			if (activetext != null) {
				activetext.append(chars);
			} else if (chars.length() > 0) {
				activetext = factory.text(chars);
				factory.addContent(parent, activetext);
			}
		}
		// ignore case where parent is Document.
	}

	@Override
	public void writeCharacters(final char[] chars, final int start,
			final int len) throws XMLStreamException {
		writeCharacters(new String(chars, start, len));
	}

	@Override
	public void writeEndElement() throws XMLStreamException {
		if (!(parent instanceof Element)) {
			throw new XMLStreamException("Cannot end an Element unless you are in an Element.");
		}
		flushActiveElement();
		flushActiveText();
		usednsstack.pop();
		boundstack.pop();
		boundstack.pop();
		boundstack.pop();
		parent = parent.getParent();
	}

	@Override
	public void writeEndDocument() throws XMLStreamException {
		// flush may change state if isempty root element
		if (document == null || done || parent instanceof Element) {
			throw new IllegalStateException(
					"Cannot write end document before writing the end of root element");
		}
		flushActiveElement();
		done = true;
	}

	@Override
	public void close() throws XMLStreamException {
		this.document = null;
		this.parent = null;
		activeelement = null;
		activetext = null;
		boundstack = null;
		usednsstack = null;
		done = true;
	}

	@Override
	public void flush() throws XMLStreamException {
		// flush does nothing.
	}

	@Override
	public NamespaceContext getNamespaceContext() {
		if (document == null) {
			return new JDOMNamespaceContext(new Namespace[0]);
		}
		return new JDOMNamespaceContext(boundstack.getScope());
	}

	@Override
	public Object getProperty(String name) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	// *****************************
	// Private methods.
	// *****************************

	/**
	 * Simple method that implements the empty-element logic without the spec-required
	 * null-check logic
	 * @param prefix The namespace prefix (may be null).
	 * @param localName The Element tag
	 * @param namespaceURI The namespace URI (may be null).
	 * @param withpfx whether the prefix is user-specified
	 * @param empty Is this an Empty element (expecting children?)
	 * @throws XMLStreamException If the stream is not in an appropriate state for a new Element.
	 */
	private final void buildElement(final String prefix, final String localName,
			final String namespaceURI, final boolean withpfx, boolean empty) throws XMLStreamException {
		
		if (document == null || done) {
			throw new XMLStreamException(
					"Cannot write new element when in current state.");
		}
		
		if (parent == document && document.hasRootElement()) {
			throw new XMLStreamException(
					"Document can have only one root Element.");
		}
			
		
		flushActiveElement();
		flushActiveText();
		
		// create an element-specific namespace binding layer.
		boundstack.push();
		
		final Namespace ns = resolveElementNamespace(prefix, namespaceURI, withpfx);

		final Element e = factory.element(localName, ns);

		factory.addContent(parent, e);
		activeelement = e;
		if (empty) {
			isempty = true;
		} else {
			isempty = false;
			parent = e;
		}
	}

	private Namespace resolveElementNamespace(String prefix, String namespaceURI,
			boolean withpfx) throws XMLStreamException {
		
		if ("".equals(namespaceURI)) {
			final Namespace defns = boundstack.getNamespaceForPrefix("");
			if(Namespace.NO_NAMESPACE != defns) {
				// inconsistency in XMLStreamWriter specification....
				// In theory the repairing code should create a generated prefix for unbound
				// namespace URI, but you can't create a prefixed ""-URI namespace.
				//
				// It next makes sense to throw an exception for this, and insist that the
				// "" URI must be explicitly bound using a prior setPrefix("","") call,
				// but, broken though it is, the better option is to replicate the undocumented
				// special-case handling that the BEA Reference implementation does ....
				// if you have the prefix (in this case "") bound already, and now you are trying
				// to bind the "" URI against the "" prefix, we let you do that as a special case
				// but be warned that "" is no longer bound to the previous URI in this Element's
				// scope.
				// http://svn.stax.codehaus.org/browse/stax/trunk/dev/src/com/bea/xml/stream/XMLWriterBase.java?r=4&r=4&r=124#to278
				if (repairnamespace) {
					return Namespace.NO_NAMESPACE;
				}
				throw new XMLStreamException("This attempt to use the empty URI \"\" as an " +
						"Element Namespace is illegal because the default Namespace is already " +
						"bound to the URI '" + defns.getURI() + "'. You must call " +
						"setPrefix(\"\", \"\") prior to this call.");
			}
		}
		
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

	private final void buildAttribute(final String prefix,
			final String namespaceURI, final String localName,
			final String value, final boolean withpfx) throws XMLStreamException {
		if (!(parent instanceof Element)) {
			throw new IllegalStateException(
					"Cannot write attribute unless inside an Element.");
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
					boundstack.pop();
					activeelement = null;
					return;
				}
				// reload the stack with the additional namespaces.
				usednsstack.push(activeelement);
			}
			if (isempty) {
				boundstack.pop();
				activeelement = null;
				return;
			}
			boundstack.push(activeelement);
			boundstack.push();
			
			activeelement = null;
		}
	}

	private final void flushActiveText() {
		activetext = null;
	}

}
