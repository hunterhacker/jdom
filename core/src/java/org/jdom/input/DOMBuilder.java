/*-- 

 $Id: DOMBuilder.java,v 1.48 2002/04/29 13:38:16 jhunter Exp $

 Copyright (C) 2000 Jason Hunter & Brett McLaughlin.
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

import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.reflect.*;

import org.jdom.*;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.EntityRef;
import org.jdom.ProcessingInstruction;
import org.jdom.adapters.*;

import org.w3c.dom.*;
import org.xml.sax.*;

/**
 * <code>DOMBuilder</code> builds a JDOM tree using DOM.
 * Note that this class should only be used for building from a pre-existing
 * DOM tree.  The class can be used to build from files, streams, etc but
 * other builders like SAXBuilder can perform the task faster because
 * they don't create a DOM tree first.
 *
 * @author Brett McLaughlin
 * @author Jason Hunter
 * @author Philip Nelson
 * @author Kevin Regan
 * @author Yusuf Goolamabbas
 * @author Dan Schaffer
 * @author Bradley S. Huffman
 * @version $Revision: 1.48 $, $Date: 2002/04/29 13:38:16 $
 */
public class DOMBuilder {

    private static final String CVS_ID = 
      "@(#) $RCSfile: DOMBuilder.java,v $ $Revision: 1.48 $ $Date: 2002/04/29 13:38:16 $ $Name:  $";

    /** Default adapter class to use. This is used when no other parser
      * is given and JAXP isn't available. 
      */
    private static final String DEFAULT_ADAPTER_CLASS =
        "org.jdom.adapters.XercesDOMAdapter";

    /** Whether validation should occur */
    private boolean validate;

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
        this(false);
    }

    /**
     * This creates a new DOMBuilder which will attempt to first locate
     * a parser via JAXP, then will try to use a set of default parsers.
     * The underlying parser will validate or not according to the given
     * parameter.
     *
     * @param validate <code>boolean</code> indicating if
     *                 validation should occur.
     * @deprecated Deprecated in Beta 9, DOMBuilder shouldn't be used for
     *             building from files and that's the only time validation
     *             matters
     */
    public DOMBuilder(boolean validate) {
        setValidation(validate);
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
        this(adapterClass, false);
    }

    /**
     * This creates a new DOMBuilder using the specified DOMAdapter
     * implementation as a way to choose the underlying parser.
     * The underlying parser will validate or not according to the given
     * parameter.
     *
     * @param adapterClass <code>String</code> name of class
     *                     to use for DOM building.
     * @param validate <code>boolean</code> indicating if
     *                 validation should occur.
     * @deprecated Deprecated in Beta 9, DOMBuilder shouldn't be used for
     *             building from files and that's the only time validation
     *             matters
     */
    public DOMBuilder(String adapterClass, boolean validate) {
        this.adapterClass = adapterClass;
        setValidation(validate);
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
     * This sets validation for the builder.
     *
     * @param validate <code>boolean</code> indicating whether validation
     * should occur.
     */
    public void setValidation(boolean validate) {
        this.validate = validate;
    }

    /**
     * This builds a document from the supplied
     * input stream by constructing a DOM tree and reading information from
     * the DOM to create a JDOM document, a slower approach than SAXBuilder 
     * but useful for debugging.
     *
     * @param in <code>InputStream</code> to read from.
     * @return <code>Document</code> - resultant Document object.
     * @throws JDOMException when errors occur in parsing.
     * @deprecated Deprecated in Beta 7, <code>{@link SAXBuilder}</code>
     *             should be used for building from any input other than a
     *             DOM tree
     */
    public Document build(InputStream in) throws JDOMException {
        Document doc = factory.document((Element)null);
        org.w3c.dom.Document domDoc = null;

        try {
            if (adapterClass != null) {
                // The user knows that they want to use a particular impl
                try {
                    DOMAdapter adapter =
                        (DOMAdapter)Class.forName(adapterClass).newInstance();
                    domDoc = adapter.getDocument(in, validate);
                    // System.out.println("using specific " + adapterClass);
                }
                catch (ClassNotFoundException e) {
                    // e.printStackTrace();
                }
            }
            else {
                // Try using JAXP...
                // Ignore any exceptions that result from JAXP not existing
                try {
                    DOMAdapter adapter =
                        (DOMAdapter)Class.forName(
                        "org.jdom.adapters.JAXPDOMAdapter").newInstance();
                    // System.out.println("using JAXP");
                    domDoc = adapter.getDocument(in, validate);
                }
                catch (ClassNotFoundException e) {
                    // e.printStackTrace();
                }
                catch (IllegalAccessException e) {
                    // e.printStackTrace();
                }
            }

            // If we don't have a domDoc yet and there's no adapter, try
            // the hard coded default parser
            if (domDoc == null && adapterClass == null) {
                try {
                    DOMAdapter adapter = (DOMAdapter)
                        Class.forName(DEFAULT_ADAPTER_CLASS).newInstance();
                    domDoc = adapter.getDocument(in, validate);
                    // System.out.println("Using default " +
                    //   DEFAULT_ADAPTER_CLASS);
                }
                catch (ClassNotFoundException e) {
                    // e.printStackTrace();
                }
            }

            // Start out at root level
            buildTree(domDoc, doc, null, true);
        }
        catch (Throwable e) {
           if (e instanceof SAXParseException) {
                SAXParseException p = (SAXParseException)e;
                String systemId = p.getSystemId();
                if (systemId != null) {
                    throw new JDOMException("Error on line " +
                              p.getLineNumber() + " of document "
                              + systemId, e);
                }
                else {
                    throw new JDOMException("Error on line " +
                              p.getLineNumber(), e);
                }
            }
            else {
                throw new JDOMException("Error in building from stream", e);
            }
        }
        return doc;
    }

    /**
     * This builds a document from the supplied
     * filename by constructing a DOM tree and reading information from the
     * DOM to create a JDOM document, a slower approach than SAXBuilder but 
     * useful for debugging.
     *
     * @param file <code>File</code> to read from.
     * @return <code>Document</code> - resultant Document object.
     * @throws JDOMException when errors occur in parsing.
     * @deprecated Deprecated in Beta 7, <code>{@link SAXBuilder}</code>
     *             should be used for building from any input other than a
     *             DOM tree
     */
    public Document build(File file) throws JDOMException {
        try {
            FileInputStream in = new FileInputStream(file);
            return build(in);
        }
        catch (FileNotFoundException e) {
            throw new JDOMException("Error in building from " + file, e);
        }
    }

    /**
     * This builds a document from the supplied
     * URL by constructing a DOM tree and reading information from the
     * DOM to create a JDOM document, a slower approach than SAXBuilder but 
     * useful for debugging.
     *
     * @param url <code>URL</code> to read from.
     * @return <code>Document</code> - resultant Document object.
     * @throws JDOMException when errors occur in parsing.
     * @deprecated Deprecated in Beta 7, <code>{@link SAXBuilder}</code>
     *             should be used for building from any input other than a
     *             DOM tree
     */
    public Document build(URL url) throws JDOMException {
        try {
            return build(url.openStream());
        }
        catch (IOException e) {
            throw new JDOMException("Error in building from " + url, e);
        }
    }

    /**
     * This will build a JDOM tree from an existing DOM tree.
     *
     * @param domDocument <code>org.w3c.dom.Document</code> object
     * @return <code>Document</code> - JDOM document object.
     */
    public Document build(org.w3c.dom.Document domDocument) {
        Document doc = factory.document((Element)null);
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
        Document doc = factory.document((Element)null);
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
                    doc.setRootElement(element);
                } else {
                    // else add to parent element
                    current.addContent(element);
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
                        // than this element's namespace (perhaps we should
                        // also have logic not to mark them as additional if
                        // it's been done already, but it probably doesn't
                        // matter)
                        if (prefix.equals(attPrefix)) {
                            element.setNamespace(declaredNS);
                        }
                        else {
                            element.addNamespaceDeclaration(declaredNS);
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
                        Namespace attns = null;
                        if ("".equals(attPrefix)) {
                            attns = Namespace.NO_NAMESPACE;
                        } 
                        else {
                            attns = element.getNamespace(attPrefix);
                        } 

                        Attribute attribute =
                            factory.attribute(attLocalName, attvalue, attns);
                        element.setAttribute(attribute);
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
                current.addContent(factory.text(data));
                break;

            case Node.CDATA_SECTION_NODE:
                String cdata = node.getNodeValue();
                current.addContent(factory.cdata(cdata));
                break;


            case Node.PROCESSING_INSTRUCTION_NODE:
                if (atRoot) {
                    doc.addContent(
                        factory.processingInstruction(node.getNodeName(),
                                                      node.getNodeValue()));
                } else {
                    current.addContent(
                        factory.processingInstruction(node.getNodeName(),
                                                      node.getNodeValue()));
                }
                break;

            case Node.COMMENT_NODE:
                if (atRoot) {
                    doc.addContent(factory.comment(node.getNodeValue()));
                } else {
                    current.addContent(factory.comment(node.getNodeValue()));
                }
                break;

            case Node.ENTITY_REFERENCE_NODE:
                EntityRef entity = factory.entityRef(node.getNodeName());
                current.addContent(entity);
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

                doc.setDocType(docType);
                break;
        }
    }
}
