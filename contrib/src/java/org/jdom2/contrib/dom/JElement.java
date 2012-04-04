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

import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;

import org.jdom2.Attribute;
import org.jdom2.Namespace;
import org.jdom2.Parent;
import org.jdom2.filter.Filters;

class JElement extends JParent implements Element {

	private final class AttMap implements NamedNodeMap {

		private final Attr[] atts;

		public AttMap(final Attr[] atts) {
			super();
			this.atts = atts;
		}

		@Override
		public Node item(final int index) {
			if (index < 0 || index >= atts.length) {
				return null;
			}
			return atts[index];
		}

		@Override
		public Node getNamedItemNS(final String namespaceURI, final String localName)
				throws DOMException {
			if (namespaceURI == null || localName == null) {
				return null;
			}
			for (int i = 0; i < atts.length; i++) {
				if (namespaceURI.equals(atts[i].getNamespaceURI()) &&
						localName.equals(atts[i].getLocalName())) {
					return atts[i];
				}
			}
			return null;
		}

		@Override
		public Node getNamedItem(final String name) {
			if (name == null) {
				return null;
			}
			for (int i = 0; i < atts.length; i++) {
				if (name.equals(atts[i].getName())) {
					return atts[i];
				}
			}
			return null;
		}

		@Override
		public int getLength() {
			return atts.length;
		}

		@Override
		public Node setNamedItemNS(final Node arg) throws DOMException {
			throw new DOMException(
					DOMException.NO_MODIFICATION_ALLOWED_ERR, "JDOM Wrapper");
		}

		@Override
		public Node setNamedItem(final Node arg) throws DOMException {
			throw new DOMException(
					DOMException.NO_MODIFICATION_ALLOWED_ERR, "JDOM Wrapper");
		}

		@Override
		public Node removeNamedItemNS(final String namespaceURI, final String localName)
				throws DOMException {
			throw new DOMException(
					DOMException.NO_MODIFICATION_ALLOWED_ERR, "JDOM Wrapper");
		}

		@Override
		public Node removeNamedItem(final String name) throws DOMException {
			throw new DOMException(
					DOMException.NO_MODIFICATION_ALLOWED_ERR, "JDOM Wrapper");
		}

	}

	private NamedNodeMap attmap = null;
	private final Namespace[] nsdec;

	public JElement(final JDocument topdoc, final JParent parent,
			final Parent shadow, final Namespace[] nstack,
			final Namespace[] ndec) {
		super(topdoc, parent, shadow, Node.ELEMENT_NODE, nstack);
		this. nsdec = ndec;
	}

	@Override
	public final String getNodeName() {
		return getTagName();
	}

	@Override
	public final String getNodeValue() throws DOMException {
		return null;
	}

	@Override
	public final NamedNodeMap getAttributes() {
		if (attmap == null) {
			final org.jdom2.Element emt = (org.jdom2.Element)shadow;
			if (emt.hasAttributes() || nsdec.length > 0) {
				final List<Attribute> list = emt.getAttributes();
				final int sz = list.size();
				final Attr[] ja = new Attr[sz + nsdec.length];
				for (int i = 0; i < nsdec.length; i++) {
					ja[i] = new JNamespace(topdoc, this, nsdec[i], scope);
				}
				for (int i = 0; i < sz; i++) {
					ja[nsdec.length + i] = topdoc.find(list.get(i));
				}
				attmap = new AttMap(ja);
			} else {
				attmap = EMPTYMAP;
			}
		}
		return attmap;
	}

	@Override
	public final String getNamespaceURI() {
		return ((org.jdom2.Element)shadow).getNamespaceURI();
	}

	@Override
	public final String getPrefix() {
		return ((org.jdom2.Element)shadow).getNamespacePrefix();
	}

	@Override
	public final String getLocalName() {
		return ((org.jdom2.Element)shadow).getName();
	}

	@Override
	public final String getBaseURI() {
		try {
			return ((org.jdom2.Element)shadow).getXMLBaseURI().toASCIIString();
		} catch (final URISyntaxException e) {
			throw new IllegalStateException("Broken base URI references.", e);
		}
	}

	@Override
	public String getTextContent() throws DOMException {
		final Iterator<org.jdom2.Text> it = ((org.jdom2.Element)shadow).
				getDescendants(Filters.fclass(org.jdom2.Text.class));
		final StringBuilder sb = new StringBuilder();
		while (it.hasNext()) {
			sb.append(it.next().getText());
		}
		return sb.toString();
	}

	@Override
	public String getTagName() {
		return ((org.jdom2.Element)shadow).getQualifiedName();
	}

	@Override
	public TypeInfo getSchemaTypeInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeList getElementsByTagName(final String tagname) {
		return topdoc.getElementsByTagName(shadow, tagname);
	}

	@Override
	public NodeList getElementsByTagNameNS(final String namespaceURI, final String localName) {
		return topdoc.getElementsByTagNameNS(shadow, namespaceURI, localName);
	}


	/* ********************************************************
	 * Attribute Access methods below
	 * ******************************************************** */

	@Override
	public final boolean hasAttributes() {
		return ((org.jdom2.Element)shadow).hasAttributes();
	}

	@Override
	public boolean hasAttribute(final String name) {
		final Attribute att = ((org.jdom2.Element)shadow).getAttribute(name);
		return att != null;
	}

	@Override
	public boolean hasAttributeNS(final String namespaceURI, final String localName)
			throws DOMException {
		final Attribute att =
				((org.jdom2.Element)shadow).getAttribute(
						localName, Namespace.getNamespace(namespaceURI));
		return att != null;
	}

	@Override
	public String getAttribute(final String name) {
		final Attribute att = ((org.jdom2.Element)shadow).getAttribute(name);
		return att == null ? "" : att.getValue();
	}

	@Override
	public String getAttributeNS(final String namespaceURI, final String localName)
			throws DOMException {
		final Attribute att =
				((org.jdom2.Element)shadow).getAttribute(
						localName, Namespace.getNamespace(namespaceURI));
		return att == null ? "" : att.getValue();
	}

	@Override
	public Attr getAttributeNode(final String name) {
		return (Attr)getAttributes().getNamedItem(name);
	}

	@Override
	public Attr getAttributeNodeNS(final String namespaceURI, final String localName)
			throws DOMException {
		return (Attr)getAttributes().getNamedItemNS(namespaceURI, localName);
	}

	/* ********************************************************
	 * Illegal modification methods below.
	 * ******************************************************** */

	@Override
	public Attr setAttributeNodeNS(final Attr newAttr) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
				"Cannot modify JDOM Wrapper DOM objects.");
	}

	@Override
	public void setIdAttribute(final String name, final boolean isId) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
				"Cannot modify JDOM Wrapper DOM objects.");
	}

	@Override
	public void setIdAttributeNS(final String namespaceURI, final String localName,
			final boolean isId) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
				"Cannot modify JDOM Wrapper DOM objects.");
	}

	@Override
	public void setIdAttributeNode(final Attr idAttr, final boolean isId)
			throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
				"Cannot modify JDOM Wrapper DOM objects.");
	}

	@Override
	public void setAttributeNS(final String namespaceURI, final String qualifiedName,
			final String value) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
				"Cannot modify JDOM Wrapper DOM objects.");
	}

	@Override
	public void removeAttributeNS(final String namespaceURI, final String localName)
			throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
				"Cannot modify JDOM Wrapper DOM objects.");
	}

	@Override
	public Attr setAttributeNode(final Attr newAttr) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
				"Cannot modify JDOM Wrapper DOM objects.");
	}

	@Override
	public void setAttribute(final String name, final String value) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
				"Cannot modify JDOM Wrapper DOM objects.");
	}

	@Override
	public void removeAttribute(final String name) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
				"Cannot modify JDOM Wrapper DOM objects.");
	}

	@Override
	public Attr removeAttributeNode(final Attr oldAttr) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
				"Cannot modify JDOM Wrapper DOM objects.");
	}

}
