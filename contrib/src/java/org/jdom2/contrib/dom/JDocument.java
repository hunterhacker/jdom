/*--

 Copyright (C) 2012 Jason Hunter & Brett McLaughlin.
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

package org.jdom2.contrib.dom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import org.jdom2.Attribute;
import org.jdom2.AttributeType;
import org.jdom2.CDATA;
import org.jdom2.DocType;
import org.jdom2.EntityRef;
import org.jdom2.Namespace;
import org.jdom2.Parent;
import org.jdom2.filter.Filters;
import org.jdom2.util.NamespaceStack;

class JDocument extends JParent implements Document {

	private static final JDOMImplementation implementation =
			new JDOMImplementation();

	private static final JDOMConfiguration configuration =
			new JDOMConfiguration();

	private boolean allscanned = false;
	private final JElement root;
	private final JDocType doctype;
	private final IdentityHashMap<Object, JNamespaceAware> mapped =
			new IdentityHashMap<Object, JNamespaceAware>();
	private final HashMap<String, JElement> idmap = new HashMap<String, JElement>();

	public JDocument(final org.jdom2.Document shadow) {
		super(null, null, shadow, Node.DOCUMENT_NODE, new Namespace[]{
				Namespace.NO_NAMESPACE,
				Namespace.XML_NAMESPACE
		});
		if (shadow == null) {
			root = null;
			doctype = null;
		} else {
			final JNamespaceAware[] kids = checkKids();
			JDocType dt = null;
			JElement je = null;
			for (final JNamespaceAware n : kids) {
				if (n.getNodeType() == Node.DOCUMENT_TYPE_NODE) {
					dt = (JDocType)n;
				}
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					je = (JElement)n;
				}
			}
			root = je;
			doctype = dt;
		}
	}

	public void scanAll() {
		if (allscanned) {
			return;
		}
		if (shadow == null) {
			allscanned = true;
			return;
		}
		allscanned = true;
		final Iterator<org.jdom2.Element> it = shadow.getDescendants(Filters.element());
		while (it.hasNext()) {
			find(it.next());
		}
	}

	public JElement find(final org.jdom2.Element emt) {
		final JNamespaceAware me = mapped.get(emt);
		if (me != null) {
			return (JElement)me;
		}
		final org.jdom2.Element jp = emt.getParentElement();
		if (jp == null) {
			// root level element (or detached).
			final org.jdom2.Document jd = emt.getDocument();
			// both may be null....
			if (jd != shadow) {
				// we are from different documents...
				throw new DOMException(DOMException.WRONG_DOCUMENT_ERR,
						"Element is not part of our document");
			}
			// OK, we are a root level Element... let's map ourselves.
			final NamespaceStack ns = new NamespaceStack(scope);
			ns.push(emt);
			final ArrayList<Namespace> added = new ArrayList<Namespace>();
			for (final Namespace ans : ns.addedForward()) {
				added.add(ans);
			}
			final JElement je = new JElement(this, this, emt, ns.getScope(),
					added.toArray(new Namespace[added.size()]));
			mapped.put(emt, je);
			checkID(je);
			return je;
		}
		final JElement pnt = find(jp);
		final NamespaceStack ns = new NamespaceStack(pnt.scope);
		ns.push(emt);
		final ArrayList<Namespace> added = new ArrayList<Namespace>();
		for (final Namespace ans : ns.addedForward()) {
			added.add(ans);
		}
		final JElement ret = new JElement(this, pnt, emt, ns.getScope(),
				added.toArray(new Namespace[added.size()]));
		mapped.put(emt, ret);
		checkID(ret);
		return ret;
	}

	public JAttribute find(final org.jdom2.Attribute att) {
		final JNamespaceAware me = mapped.get(att);
		if (me != null) {
			return (JAttribute)me;
		}
		final org.jdom2.Element jp = att.getParent();
		final JParent pnt = jp == null ? this : find(jp);
		final NamespaceStack ns = jp == null ?
				new NamespaceStack() : new NamespaceStack(find(jp).scope);
				ns.push(att);
				final JAttribute ret = new JAttribute(this, pnt, att, ns.getScope());
				mapped.put(att, ret);
				return ret;
	}

	private JContent findContent(final org.jdom2.Content content) {
		final JNamespaceAware me = mapped.get(content);
		if (me != null) {
			return (JContent)me;
		}
		final org.jdom2.Element jp = content.getParentElement();
		final JParent pnt = jp == null ? this : find(jp);
		JContent ret = null;
		switch (content.getCType()) {
			case CDATA:
				ret = new JCDATA(this, pnt, content, pnt.scope);
				break;
			case Comment:
				ret = new JComment(this, pnt, content, pnt.scope);
				break;
			case DocType:
				ret = new JDocType(this, pnt, content, pnt.scope);
				break;
			case EntityRef:
				ret = new JEntityRef(this, pnt, content, pnt.scope);
				break;
			case ProcessingInstruction:
				ret = new JProcessingInstruction(this, pnt, content, pnt.scope);
				break;
			case Text:
				ret = new JText(this, pnt, content, pnt.scope);
				break;
			default:
				throw new IllegalStateException(
						"Other types should have their own methods.");
		}
		mapped.put(content, ret);
		return ret;
	}

	public JCDATA find(final CDATA content) {
		return (JCDATA)findContent(content);
	}

	public JDocType find(final DocType content) {
		return (JDocType)findContent(content);
	}

	public JProcessingInstruction find(
			final org.jdom2.ProcessingInstruction content) {
		return (JProcessingInstruction)findContent(content);
	}

	public JEntityRef find(final EntityRef content) {
		return (JEntityRef)findContent(content);
	}

	public JComment find(final org.jdom2.Comment content) {
		return (JComment)findContent(content);
	}

	public JText find(final org.jdom2.Text content) {
		return (JText)findContent(content);
	}

	private void checkID(final JElement je) {
		final org.jdom2.Element emt = (org.jdom2.Element)(je.shadow);
		if (emt.hasAttributes()) {
			for (final Attribute a : emt.getAttributes()) {
				if (a.getAttributeType() == AttributeType.ID) {
					if (idmap.put(a.getValue(), je) != null) {
						throw new DOMException(DOMException.INVALID_STATE_ERR,
								"Multiple elements with id " + a.getValue());
					}
				}
			}
		}
	}

	@Override
	public DocumentType getDoctype() {
		return doctype;
	}

	@Override
	public Element getDocumentElement() {
		return root;
	}



	@Override
	public NodeList getElementsByTagName(final String tagname) {
		return getElementsByTagName(shadow, tagname);
	}

	@Override
	public NodeList getElementsByTagNameNS(final String namespaceURI, final String localName) {
		return getElementsByTagNameNS(shadow, namespaceURI, localName);
	}

	NodeList getElementsByTagName(final Parent xshadow, final String tagname) {
		if (tagname == null) {
			return EMPTYLIST;
		}
		final ArrayList<JElement> enodes = new ArrayList<JElement>();
		final boolean alltags = "*".equals(tagname);
		
		final Iterator<org.jdom2.Element> it =
				xshadow.getDescendants(Filters.element());

		while (it.hasNext()) {
			final org.jdom2.Element e = it.next();
			if (alltags || tagname.equals(e.getQualifiedName())) {
				enodes.add(find(e));
			}
		}
		
		return new JNodeList(enodes);
	}

	NodeList getElementsByTagNameNS(final Parent xshadow, final String namespaceURI, final String localName) {
		if (localName == null) {
			return EMPTYLIST;
		}
		if (namespaceURI == null) {
			return EMPTYLIST;
		}

		final boolean alluri = "*".equals(namespaceURI);
		final boolean allname = "*".equals(localName);

		final ArrayList<JElement> enodes = new ArrayList<JElement>();

		final Iterator<org.jdom2.Element> it =
				xshadow.getDescendants(Filters.element());

		while (it.hasNext()) {
			final org.jdom2.Element e = it.next();
			if ((allname || localName.equals(e.getName())) &&
					(alluri || namespaceURI.equals(e.getNamespaceURI()))) {
				enodes.add(find(e));
			}
		}

		return new JNodeList(enodes);
	}

	@Override
	public Element getElementById(final String elementId) {
		scanAll();
		return idmap.get(elementId);
	}

	@Override
	public String getDocumentURI() {
		return shadow == null ? null : ((org.jdom2.Document)shadow).getBaseURI();
	}

	@Override
	public String getNodeName() {
		return "#document";
	}

	@Override
	public String getBaseURI() {
		return shadow == null ? null : ((org.jdom2.Document)shadow).getBaseURI();
	}

	/* *********************************************************
	 * Everything after this point is 'dumb' code... (or error code)
	 * ********************************************************* */

	@Override
	public String getNodeValue() throws DOMException {
		return null;
	}

	@Override
	public NamedNodeMap getAttributes() {
		return null;
	}

	@Override
	public String getNamespaceURI() {
		return null;
	}

	@Override
	public String getPrefix() {
		return null;
	}

	@Override
	public String getLocalName() {
		return null;
	}

	@Override
	public boolean hasAttributes() {
		return false;
	}

	@Override
	public String getTextContent() throws DOMException {
		return null;
	}

	@Override
	public String getXmlVersion() {
		return "1.0";
	}

	@Override
	public String getInputEncoding() {
		return null;
	}

	@Override
	public String getXmlEncoding() {
		return null;
	}

	@Override
	public boolean getXmlStandalone() {
		return false;
	}

	@Override
	public final DOMImplementation getImplementation() {
		return implementation;
	}

	@Override
	public final DOMConfiguration getDomConfig() {
		return configuration;
	}

	@Override
	public final boolean getStrictErrorChecking() {
		return false;
	}

	@Override
	public final void setStrictErrorChecking(final boolean strictErrorChecking) {
		// nothing
	}

	@Override
	public final void setDocumentURI(final String documentURI) {
		// Do nothing
	}

	@Override
	public final void normalizeDocument() {
		// do nothing
	}

	@Override
	public final Node renameNode(final Node n, final String namespaceURI, final String qualifiedName)
			throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
				"Cannot modify JDOM Wrapper DOM objects.");
	}

	@Override
	public final void setXmlStandalone(final boolean xmlStandalone) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
				"Cannot modify JDOM Wrapper DOM objects.");
	}

	@Override
	public final void setXmlVersion(final String xmlVersion) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
				"Cannot modify JDOM Wrapper DOM objects.");
	}

	@Override
	public final Node adoptNode(final Node source) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
				"Cannot modify JDOM Wrapper DOM objects.");
	}

	@Override
	public final Element createElement(final String tagName) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
				"Cannot modify JDOM Wrapper DOM objects.");
	}

	@Override
	public final DocumentFragment createDocumentFragment() {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
				"Cannot modify JDOM Wrapper DOM objects.");
	}

	@Override
	public final Text createTextNode(final String data) {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
				"Cannot modify JDOM Wrapper DOM objects.");
	}

	@Override
	public final Comment createComment(final String data) {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
				"Cannot modify JDOM Wrapper DOM objects.");
	}

	@Override
	public final CDATASection createCDATASection(final String data) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
				"Cannot modify JDOM Wrapper DOM objects.");
	}

	@Override
	public final ProcessingInstruction createProcessingInstruction(final String target,
			final String data) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
				"Cannot modify JDOM Wrapper DOM objects.");
	}

	@Override
	public final Attr createAttribute(final String name) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
				"Cannot modify JDOM Wrapper DOM objects.");
	}

	@Override
	public final EntityReference createEntityReference(final String name)
			throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
				"Cannot modify JDOM Wrapper DOM objects.");
	}

	@Override
	public final Node importNode(final Node importedNode, final boolean deep) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
				"Cannot modify JDOM Wrapper DOM objects.");
	}

	@Override
	public final Element createElementNS(final String namespaceURI, final String qualifiedName)
			throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
				"Cannot modify JDOM Wrapper DOM objects.");
	}

	@Override
	public final Attr createAttributeNS(final String namespaceURI, final String qualifiedName)
			throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
				"Cannot modify JDOM Wrapper DOM objects.");
	}

}
