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

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;

abstract class JNode implements Node, Wrapper {

	protected static final TypeInfo TYPEINFO = new TypeInfo() {
		@Override
		public boolean isDerivedFrom(final String typeNamespaceArg, final String typeNameArg,
				final int derivationMethod) {
			return false;
		}

		@Override
		public String getTypeNamespace() {
			return "http://www.w3.org/TR/REC-xml";
		}

		@Override
		public String getTypeName() {
			return null;
		}
	};



	protected static final NodeList EMPTYLIST = new NodeList() {
		@Override
		public Node item(final int index) {
			return null;
		}
		@Override
		public int getLength() {
			return 0;
		}
	};

	protected static final NamedNodeMap EMPTYMAP = new NamedNodeMap() {

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

		@Override
		public Node item(final int index) {
			return null;
		}

		@Override
		public Node getNamedItemNS(final String namespaceURI, final String localName)
				throws DOMException {
			return null;
		}

		@Override
		public Node getNamedItem(final String name) {
			return null;
		}

		@Override
		public int getLength() {
			return 0;
		}
	};

	private final short nodetype;
	protected final JDocument topdoc;
	protected final JParent parent;

	private HashMap<String, Object> userdata;

	JNode(final JDocument topdoc, final JParent parent, final short nodetype) {
		// the rule is that only JDocument constructor can pass a null topdoc.
		this.topdoc = topdoc == null ? (JDocument)this : topdoc;
		this.nodetype = nodetype;
		this.parent = parent;
	}

	/**
	 * Attribute, Document, and Element need to override this.
	 * @param other will be another node of the same type that needs to be
	 * compared.
	 * @return true if the rules match the Node.isEqualsNode() method.
	 */
	protected boolean detailEquals(final JNode other) {
		return true;
	}

	/* ****************************************************
	 * Below this all methods are final...
	 * **************************************************** */

	@Override
	public final Node getPreviousSibling() {
		if (parent == null) {
			return null;
		}
		return parent.getPreviousSibling(this);
	}

	@Override
	public final Node getNextSibling() {
		if (parent == null) {
			return null;
		}
		return parent.getNextSibling(this);
	}




	@Override
	public final Object setUserData(final String key, final Object data, final UserDataHandler handler) {
		// since the node can never be adopted or cloned, the handler
		// events can never be called, thus we do not need to track the handler.
		if (userdata == null) {
			userdata = new HashMap<String, Object>();
		}
		return userdata.put(key, data);
	}

	@Override
	public final Object getUserData(final String key) {
		if (userdata == null) {
			return null;
		}
		return userdata.get(key);
	}

	@Override
	public final Object getFeature(final String feature, final String version) {
		return null;
	}

	@Override
	public final Node getParentNode() {
		return parent;
	}

	@Override
	public final short getNodeType() {
		return nodetype;
	}

	@Override
	public final Document getOwnerDocument() {
		return topdoc;
	}

	@Override
	public final short compareDocumentPosition(final Node other) throws DOMException {
		if ((other instanceof JNode)) {
			JNode their = (JNode)other;
			if (their == this) {
				return 0;
			}
			if (their.topdoc == topdoc) {
				// same document....
				final ArrayList<JNode> ancestry = new ArrayList<JNode>();
				ancestry.add(their);
				JNode p = their.parent;
				while (p != null) {
					if (p == this) {
						// we contain the other node.
						return DOCUMENT_POSITION_CONTAINED_BY | DOCUMENT_POSITION_FOLLOWING;
					}
					ancestry.add(p);
					p = p.parent;
				}
				// OK, we have the ancestry.... but, we are not on the direct ancestry of that node.
				// let's find an intersection of our ancestry...
				p = this.parent;
				JNode k = this;
				final int alen = ancestry.size();
				while (p != null) {
					for (int apos = 0; apos < alen; apos++) {
						if (p == ancestry.get(apos)) {
							// we have intersected...
							final int sibpos = apos - 1;
							if (sibpos < 0) {
								return DOCUMENT_POSITION_CONTAINS | DOCUMENT_POSITION_PRECEDING;
							}
							final JNode sibling = ancestry.get(sibpos);
							// need to get relative order of 'k' and sibling within 'p'.
							final JParent prnt = (JParent)p;
							if (sibling instanceof JAttribute) {
								final NamedNodeMap nnm = prnt.getAttributes();
								for (int i = nnm.getLength() - 1; i >= 0; i--) {
									final Node n = nnm.item(i);
									if (n == sibling) {
										return DOCUMENT_POSITION_FOLLOWING;
									} else if (n == k) {
										return DOCUMENT_POSITION_PRECEDING;
									}
								}
							} else {
								for (int i = prnt.getLength() - 1; i >= 0; i--) {
									final Node n = prnt.item(i);
									if (n == sibling) {
										return DOCUMENT_POSITION_FOLLOWING;
									} else if (n == k) {
										return DOCUMENT_POSITION_PRECEDING;
									}
								}
							}
							throw new IllegalStateException("Sibling nodes appear not to be siblings?");
						}
					}
					k = p;
					p = p.parent;
				}
			}
			return Node.DOCUMENT_POSITION_DISCONNECTED;
		}
		throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Not same Document Model");
	}

	@Override
	public final boolean isEqualNode(final Node other) {
		// definition of DOM isEquals:
		//		Two nodes are equal if and only if the following conditions are satisfied:
		//
		//		    The two nodes are of the same type.
		//		    The following string attributes are equal: nodeName, localName, namespaceURI, prefix, nodeValue . This is: they are both null, or they have the same length and are character for character identical.
		//		    The attributes NamedNodeMaps are equal. This is: they are both null, or they have the same length and for each node that exists in one map there is a node that exists in the other map and is equal, although not necessarily at the same index.
		//		    The childNodes NodeLists are equal. This is: they are both null, or they have the same length and contain equal nodes at the same index. Note that normalization can affect equality; to avoid this, nodes should be normalized before being compared.
		//
		if (other == null) {
			return false;
		}
		if (!getClass().isInstance(other)) {
			return false;
		}
		if (isSameNode(other)) {
			return true;
		}
		if (areSame(getNodeName(), other.getNodeName()) &&
				areSame(getLocalName(), other.getLocalName()) &&
				areSame(getNamespaceURI(), other.getNamespaceURI()) &&
				areSame(getPrefix(), other.getPrefix()) &&
				areSame(getNodeValue(), other.getNodeValue())) {
			return detailEquals((JNode)other);
		}

		return false;
	}

	@Override
	public final boolean isSameNode(final Node other) {
		return this == other;
	}

	@Override
	public final Node cloneNode(final boolean deep) {
		// this would modify things.... we skip it.
		// as a convenience we just return ourselves ... readonly.
		// this breaks the DOM specification, but there is so much
		// that is broken by this thin wrapper.....
		return this;
	}

	@Override
	public final void normalize() {
		// this would modify things.... we skip it.
	}

	@Override
	public final boolean isSupported(final String feature, final String version) {
		// anything that requires feature support is not supported... ;-)
		return false;
	}

	@Override
	public final void setPrefix(final String prefix) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
				"Cannot modify JDOM Wrapper DOM objects.");
	}

	@Override
	public final Node insertBefore(final Node newChild, final Node refChild) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
				"Cannot modify JDOM Wrapper DOM objects.");
	}

	@Override
	public final Node replaceChild(final Node newChild, final Node oldChild) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
				"Cannot modify JDOM Wrapper DOM objects.");
	}

	@Override
	public final Node removeChild(final Node oldChild) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
				"Cannot modify JDOM Wrapper DOM objects.");
	}

	@Override
	public final Node appendChild(final Node newChild) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
				"Cannot modify JDOM Wrapper DOM objects.");
	}

	@Override
	public final void setTextContent(final String textContent) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
				"Cannot modify JDOM Wrapper DOM objects.");
	}

	@Override
	public final void setNodeValue(final String nodeValue) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
				"Cannot modify JDOM Wrapper DOM objects.");
	}

	protected static final boolean areSame(final String a, final String b) {
		if (b == null) {
			return a == null;
		}
		if (a == null) {
			return false;
		}
		return a.equals(b);
	}

}
