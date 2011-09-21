/*--

 $Id: DOMBuilder.java,v 1.60 2007/11/10 05:29:00 jhunter Exp $

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

package org.jdom.input;

import java.util.HashSet;
import java.util.Iterator;

import org.jdom.*;
import org.jdom.Document;
import org.jdom.Element;
import org.w3c.dom.*;

/**
 * Builds a JDOM {@link org.jdom.Document org.jdom.Document} from a pre-existing
 * DOM {@link org.w3c.dom.Document org.w3c.dom.Document}. Also handy for testing
 * builds from files to sanity check {@link SAXBuilder}.
 *
 * @version $Revision: 1.60 $, $Date: 2007/11/10 05:29:00 $
 * @author  Brett McLaughlin
 * @author  Jason Hunter
 * @author  Philip Nelson
 * @author  Kevin Regan
 * @author  Yusuf Goolamabbas
 * @author  Dan Schaffer
 * @author  Bradley S. Huffman
 */
public class DOMBuilder {

    private static final String CVS_ID =
      "@(#) $RCSfile: DOMBuilder.java,v $ $Revision: 1.60 $ $Date: 2007/11/10 05:29:00 $ $Name:  $";

    /** Adapter class to use */
    private String adapterClass;

    /** The factory for creating new JDOM objects */
    private JDOMFactory factory = new DefaultJDOMFactory();

    /**
     * This creates a new DOMBuilder which will attempt to first locate
     * a parser via JAXP, then will try to use a set of default parsers.
     * The underlying parser will not validate.
     */
    public DOMBuilder() {
    }

    /**
     * This creates a new DOMBuilder using the specified DOMAdapter
     * implementation as a way to choose the underlying parser.
     * The underlying parser will not validate.
     *
     * @param adapterClass <code>String</code> name of class
     *                     to use for DOM building.
     */
    public DOMBuilder(String adapterClass) {
        this.adapterClass = adapterClass;
    }

    /*
     * This sets a custom JDOMFactory for the builder.  Use this to build
     * the tree with your own subclasses of the JDOM classes.
     *
     * @param factory <code>JDOMFactory</code> to use
     */
    public void setFactory(JDOMFactory factory) {
        this.factory = factory;
    }

    /**
     * Returns the current {@link org.jdom.JDOMFactory} in use.
     * @return the factory in use
     */
    public JDOMFactory getFactory() {
        return factory;
    }

    /**
     * This will build a JDOM tree from an existing DOM tree.
     *
     * @param domDocument <code>org.w3c.dom.Document</code> object
     * @return <code>Document</code> - JDOM document object.
     */
    public Document build(org.w3c.dom.Document domDocument) {
        Document doc = factory.document(null);
        buildTree(domDocument, doc, null, true);
        return doc;
    }

    /**
     * This will build a JDOM Element from an existing DOM Element
     *
     * @param domElement <code> org.w3c.dom.Element</code> object
     * @return <code>Element</code> - JDOM Element object
     */
    public org.jdom.Element build(org.w3c.dom.Element domElement) {
        Document doc = factory.document(null);
        buildTree(domElement, doc, null, true);
        return doc.getRootElement();
    }

    /**
     * This takes a DOM <code>Node</code> and builds up
     * a JDOM tree, recursing until the DOM tree is exhausted
     * and the JDOM tree results.
     *
     * @param node <code>Code</node> to examine.
     * @param doc JDOM <code>Document</code> being built.
     * @param current <code>Element</code> that is current parent.
     * @param atRoot <code>boolean</code> indicating whether at root level.
     */
    private void buildTree(Node node,
                           Document doc,
                           Element current,
                           boolean atRoot) {
        // Recurse through the tree
        switch (node.getNodeType()) {
            case Node.DOCUMENT_NODE:
                NodeList nodes = node.getChildNodes();
                for (int i=0, size=nodes.getLength(); i<size; i++) {
                    buildTree(nodes.item(i), doc, current, true);
                }
                break;

            case Node.ELEMENT_NODE:
                String nodeName = node.getNodeName();
                String prefix = "";
                String localName = nodeName;
                int colon = nodeName.indexOf(':');
                if (colon >= 0) {
                    prefix = nodeName.substring(0, colon);
                    localName = nodeName.substring(colon + 1);
                }

                // Get element's namespace
                Namespace ns = null;
                String uri = node.getNamespaceURI();
                if (uri == null) {
                    ns = (current == null) ? Namespace.NO_NAMESPACE
                                           : current.getNamespace(prefix);
                }
                else {
                    ns = Namespace.getNamespace(prefix, uri);
                }

                Element element = factory.element(localName, ns);

                if (atRoot) {
                    // If at root, set as document root
                    doc.setRootElement(element);  // XXX should we use a factory call?
                } else {
                    // else add to parent element
                    factory.addContent(current, element);
                }

                // Add namespaces
                NamedNodeMap attributeList = node.getAttributes();
                int attsize = attributeList.getLength();

                for (int i = 0; i < attsize; i++) {
                    Attr att = (Attr) attributeList.item(i);

                    String attname = att.getName();
                    if (attname.startsWith("xmlns")) {
                        String attPrefix = "";
                        colon = attname.indexOf(':');
                        if (colon >= 0) {
                            attPrefix = attname.substring(colon + 1);
                        }

                        String attvalue = att.getValue();

                        Namespace declaredNS =
                            Namespace.getNamespace(attPrefix, attvalue);

                        // Add as additional namespaces if it's different
                        // to this element's namespace (perhaps we should
                        // also have logic not to mark them as additional if
                        // it's been done already, but it probably doesn't
                        // matter)
                        if (prefix.equals(attPrefix)) {
                        	// RL: note, it should also be true that uri.equals(attvalue)
                        	// if not, then the parser is boken.
                        	// further, declaredNS should be exactly the same as ns
                        	// so the following should in fact do nothing.
                            element.setNamespace(declaredNS);
                        }
                        else {
                            factory.addNamespaceDeclaration(element, declaredNS);
                        }
                    }
                }

                // Add attributes
                for (int i = 0; i < attsize; i++) {
                    Attr att = (Attr) attributeList.item(i);

                    String attname = att.getName();

                    if ( !attname.startsWith("xmlns")) {
                        String attPrefix = "";
                        String attLocalName = attname;
                        colon = attname.indexOf(':');
                        if (colon >= 0) {
                            attPrefix = attname.substring(0, colon);
                            attLocalName = attname.substring(colon + 1);
                        }

                        String attvalue = att.getValue();

                        // Get attribute's namespace
                        Namespace attNS = null;
                        String attURI = att.getNamespaceURI(); 
                        if (attURI == null || "".equals(attURI)) {
                        	attNS = Namespace.NO_NAMESPACE;
                        } else {
                        	// various conditions can lead here.
                        	// the logical one is that we have a prefix for the
                        	// attribute, and also a namespace URI.
                        	// The alternative to that is in some conditions,
                        	// the parser could have a 'default' or 'fixed'
                        	// attribute that comes from an XSD used for
                        	// validation. In that case there may not be a prefix
                        	// There's also the possibility the DOM contains
                        	// garbage.
                        	if (attPrefix.length() > 0) {
	                        	// If the att has a prefix, we can assume that
	                        	// the DOM is valid, and we can just use the prefix.
                        		// if this prefix conflicts with some other namespace
                        		// then we re-declare it. If redeclaring it screws up
                        		// other attributes in this Element, then the DOM
                        		// was broken to start with.
                        		attNS = Namespace.getNamespace(attPrefix, attURI);
                        	} else {
                        		// OK, no prefix.
                        		// must be a defaulted value from an XSD.
                        		// perhaps we can find the namespace in our
                        		// element's ancestry, and use the prefix from that.
                                // We need to ensure that a particular prefix has not been
                                // overridden at a lower level than what we are expecting.
                                // track all prefixes to ensure they are not changed lower
                                // down.
                                HashSet overrides = new HashSet();
                                Element p = element;
                                uploop: do {
                                    // Search up the Element tree looking for a prefixed namespace
                                    // matching our attURI
                                    if (p.getNamespace().getURI().equals(attURI)
                                            && !overrides.contains(p.getNamespacePrefix())
                                            && !"".equals(element.getNamespace().getPrefix())) {
                                        // we need a prefix. It's impossible to have a namespaced
                                        // attribute if there is no prefix for that attribute.
                                        attNS = p.getNamespace();
                                        break uploop;
                                    }
                                    overrides.add(p.getNamespacePrefix());
                                    for (Iterator it = p.getAdditionalNamespaces().iterator();
                                            it.hasNext(); ) {
                                        Namespace tns = (Namespace)it.next();
                                        if (!overrides.contains(tns.getPrefix())
                                                 && attURI.equals(tns.getURI())) {
                                            attNS = tns;
                                            break uploop;
                                        }
                                        overrides.add(tns.getPrefix());
                                    }
                                    p = p.getParentElement();
                                } while (p != null);
                                if (attNS == null) {
                                    // we cannot find a 'prevailing' namespace that has a prefix
                                    // that is for this namespace.
                                    // This basically means that there's an XMLSchema, for the
                                    // DEFAULT namespace, and there's a defaulted/fixed
                                    // attribute definition in the XMLSchema that's targeted
                                    // for this namespace,... but, the user has either not
                                    // declared a prefixed version of the namespace, or has
                                    // re-declared the same prefix at a lower level with a
                                    // different namespace.
                                    // All of these things are possible.
                                    // Create some sort of default prefix.
                                    int cnt = 0;
                                    String base = "attns";
                                    String pfx = base + cnt;
                                    while (overrides.contains(pfx)) {
                                        cnt++;
                                        pfx = base + cnt;
                                    }
                                    attNS = Namespace.getNamespace(pfx, attURI);
                                }
                        	}
                        }

                        Attribute attribute =
                            factory.attribute(attLocalName, attvalue, attNS);
                        factory.setAttribute(element, attribute);
                    }
                }

                // Recurse on child nodes
                // The list should never be null nor should it ever contain
                // null nodes, but some DOM impls are broken
                NodeList children = node.getChildNodes();
                if (children != null) {
                    int size = children.getLength();
                    for (int i = 0; i < size; i++) {
                        Node item = children.item(i);
                        if (item != null) {
                            buildTree(item, doc, element, false);
                        }
                    }
                }
                break;

            case Node.TEXT_NODE:
                String data = node.getNodeValue();
                factory.addContent(current, factory.text(data));
                break;

            case Node.CDATA_SECTION_NODE:
                String cdata = node.getNodeValue();
                factory.addContent(current, factory.cdata(cdata));
                break;


            case Node.PROCESSING_INSTRUCTION_NODE:
                if (atRoot) {
                    factory.addContent(doc,
                        factory.processingInstruction(node.getNodeName(),
                                                      node.getNodeValue()));
                } else {
                    factory.addContent(current,
                        factory.processingInstruction(node.getNodeName(),
                                                      node.getNodeValue()));
                }
                break;

            case Node.COMMENT_NODE:
                if (atRoot) {
                    factory.addContent(doc, factory.comment(node.getNodeValue()));
                } else {
                    factory.addContent(current, factory.comment(node.getNodeValue()));
                }
                break;

            case Node.ENTITY_REFERENCE_NODE:
                EntityRef entity = factory.entityRef(node.getNodeName());
                factory.addContent(current, entity);
                break;

            case Node.ENTITY_NODE:
                // ??
                break;

            case Node.DOCUMENT_TYPE_NODE:
                DocumentType domDocType = (DocumentType)node;
                String publicID = domDocType.getPublicId();
                String systemID = domDocType.getSystemId();
                String internalDTD = domDocType.getInternalSubset();

                DocType docType = factory.docType(domDocType.getName());
                docType.setPublicID(publicID);
                docType.setSystemID(systemID);
                docType.setInternalSubset(internalDTD);

                factory.addContent(doc, docType);
                break;
        }
    }
}
