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

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;

import org.jdom2.JDOMConstants;
import org.jdom2.Namespace;

final class JNamespace extends JNamespaceAware implements Attr {

	final Namespace ns;
	public JNamespace(final JDocument topdoc, final JElement parent, final Namespace namespace,
			final Namespace[] nstack) {
		super(topdoc, parent, Node.ATTRIBUTE_NODE, nstack);
		this.ns = namespace;
	}

	@Override
	public final Object getWrapped() {
		return ns;
	}

	@Override
	public String getNodeName() {
		return getName();
	}
	@Override
	public String getNodeValue() throws DOMException {
		return getValue();
	}
	@Override
	public NodeList getChildNodes() {
		return EMPTYLIST;
	}
	@Override
	public Node getFirstChild() {
		return null;
	}
	@Override
	public Node getLastChild() {
		return null;
	}
	@Override
	public NamedNodeMap getAttributes() {
		return null;
	}
	@Override
	public boolean hasChildNodes() {
		return false;
	}
	@Override
	public String getNamespaceURI() {
		return "".equals(ns.getPrefix()) ? "" : JDOMConstants.NS_URI_XMLNS;
	}
	@Override
	public String getPrefix() {
		return "".equals(ns.getPrefix()) ? "" : JDOMConstants.NS_PREFIX_XMLNS;
	}
	@Override
	public String getLocalName() {
		return "".equals(ns.getPrefix()) ? JDOMConstants.NS_PREFIX_XMLNS : ns.getPrefix();
	}
	@Override
	public boolean hasAttributes() {
		return false;
	}
	@Override
	public String getBaseURI() {
		return parent.getBaseURI();
	}
	@Override
	public String getTextContent() throws DOMException {
		return "";
	}
	@Override
	public String getName() {
		return "".equals(ns.getPrefix()) ? ns.getPrefix() : (JDOMConstants.NS_PREFIX_XMLNS + ":" + ns.getPrefix());
	}
	@Override
	public boolean getSpecified() {
		return true;
	}
	@Override
	public String getValue() {
		return ns.getURI();
	}

	@Override
	public Element getOwnerElement() {
		return (Element)getParentNode();
	}

	@Override
	public TypeInfo getSchemaTypeInfo() {
		return TYPEINFO;
	}

	@Override
	public boolean isId() {
		return false;
	}

	@Override
	public void setValue(final String value) throws DOMException {
		throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
				"Cannot modify JDOM Wrapper DOM objects.");
	}


}