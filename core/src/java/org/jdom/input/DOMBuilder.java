/*--

 Copyright 2000 Brett McLaughlin & Jason Hunter. All rights reserved.

 Redistribution and use in source and binary forms, with or without modifica-
 tion, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice,
    this list of conditions, and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions, the disclaimer that follows these conditions,
    and/or other materials provided with the distribution.

 3. The names "JDOM" and "Java Document Object Model" must not be used to
    endorse or promote products derived from this software without prior
    written permission. For written permission, please contact
    license@jdom.org.

 4. Products derived from this software may not be called "JDOM", nor may
    "JDOM" appear in their name, without prior written permission from the
    JDOM Project Management (pm@jdom.org).

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 JDOM PROJECT  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLUDING, BUT
 NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 This software  consists of voluntary contributions made  by many individuals
 on  behalf of the Java Document Object Model Project and was originally
 created by Brett McLaughlin <brett@jdom.org> and
 Jason Hunter <jhunter@jdom.org>. For more  information on the JDOM
 Project, please see <http://www.jdom.org/>.

 */
package org.jdom.input;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jdom.Comment;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Entity;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.ProcessingInstruction;
import org.jdom.adapters.DOMAdapter;

import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * <p><code>DOMBuilder</code> builds a JDOM tree using DOM.</p>
 *
 * @author Brett McLaughlin
 * @author Jason Hunter
 * @author Philip Nelson
 * @version 1.0
 */
public class DOMBuilder implements Builder {

    /** Default adapter class */
    private static final String DEFAULT_ADAPTER_CLASS =
        "org.jdom.adapters.XercesDOMAdapter";

    /** Whether validation should occur */
    private boolean validate;

    /** Adapter class to use */
    private String adapterClass;

    /** Prefixed Namespaces for this Document */
    private Map prefixedNamespaces;

    /**
     * <p>
     * This allows the validation features to be turned on/off
     *   in the builder at creation, as well as set the
     *   DOM Adapter class to use.
     * </p>
     *
     * @param adapterClass <code>String</code> name of class
     *                     to use for DOM building.
     * @param validate <code>boolean</code> indicating if
     *                 validation should occur.
     */
    public DOMBuilder(String adapterClass, boolean validate) {
        this.adapterClass = adapterClass;
        this.validate = validate;
        prefixedNamespaces = new HashMap();
    }

    /**
     * <p>
     * This sets the DOM Adapter class to use, and leaves
     *   validation off.
     * </p>
     *
     * @param validate <code>boolean</code> indicating if
     *                 validation should occur.
     */
    public DOMBuilder(String adapterClass) {
        this(adapterClass, false);
    }

    /**
     * <p>
     * This sets validation for the <code>Builder</code>.
     * </p>
     *
     * @param validate <code>boolean</code> indicating if
     *                 validation should occur.
     */
    public DOMBuilder(boolean validate) {
        this(DEFAULT_ADAPTER_CLASS, validate);
    }

    /**
     * <p>
     * This creates a <code>DOMBuilder</code> with
     *   the default parser and no validation.
     * </p>
     */
    public DOMBuilder() {
        this(DEFAULT_ADAPTER_CLASS, false);
    }

    /**
     * <p>
     * This builds a document from the supplied
     *   input stream.
     * </p>
     *
     * @param in <code>InputStream</code> to read from.
     * @return <code>Document</code> - resultant Document object.
     * @throws <code>JDOMException</code> when errors occur in
     *                                    parsing.
     */
    public Document build(InputStream in) throws JDOMException {

        Document doc = new Document(null);

        try {
            DOMAdapter adapter =
                (DOMAdapter)Class.forName(adapterClass)
                                 .newInstance();

            org.w3c.dom.Document domDoc =
                adapter.getDocument(in, validate);

            // Start out at root level
            buildTree(domDoc, doc, null, true);

        } catch (Exception e) {
            throw new JDOMException(e.getMessage(), e);
        }

        return doc;
    }

    /**
     * <p>
     * This builds a document from the supplied
     *   filename.
     * </p>
     *
     * @param file <code>File</code> to read from.
     * @return <code>Document</code> - resultant Document object.
     * @throws <code>JDOMException</code> when errors occur in
     *                                    parsing.
     */
    public Document build(File file) throws JDOMException {
        try {
            FileInputStream in = new FileInputStream(file);
            return build(in);
        } catch (FileNotFoundException e) {
            throw new JDOMException(e.getMessage(), e);
        }
    }

    /**
     * <p>
     * This builds a document from the supplied
     *   URL.
     * </p>
     *
     * @param url <code>URL</code> to read from.
     * @return <code>Document</code> - resultant Document object.
     * @throws <code>JDOMException</code> when errors occur in
     *                                    parsing.
     */
    public Document build(URL url) throws JDOMException {
        try {
            return build(url.openStream());
        } catch (IOException e) {
            throw new JDOMException(e.getMessage(), e);
        }
    }

    /**
     * <p>
     * This will build a JDOM tree from an existing DOM tree.
     * </p>
     *
     * @param domDocument <code>org.w3c.dom.Document</code> object
     * @return <code>Document</code> - JDOM document object.
     */
    public Document build(org.w3c.dom.Document domDocument) {
        Document doc = new Document(null);

        buildTree(domDocument, doc, null, true);

        return doc;
    }

    /**
     * <p>
     * This takes a DOM <code>Node</code> and builds up
     *   a JDOM tree, recursing until the DOM tree is exhausted
     *   and the JDOM tree results.
     * </p>
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
                // Get attributes first, as they may contain namespace declarations
                NamedNodeMap attributeList = node.getAttributes();
                List elementAttributes = new LinkedList();
                for (int i=0, size=attributeList.getLength(); i<size; i++) {
                    Node att = attributeList.item(i);

                    // Distinguish between namespace and attribute
                    if (node.getNodeName().startsWith("xmlns")) {
                        String prefix;
                        String uri = node.getNodeValue();
                        int colon;
                        if ((colon = node.getNodeName().indexOf(":")) != -1) {
                            prefix = node.getNodeName().substring(colon + 1);
                        } else {
                            prefix = "";
                        }

                        // Get the namespace, essentially
                        Namespace ns = Namespace.getNamespace(prefix, uri);
                        // Only store if a prefix
                        if (!prefix.equals("")) {
                            prefixedNamespaces.put(prefix, ns);
                        }
                    } else {
                        // We have to save these for later, and handle
                        //   namespaces first.
                        elementAttributes.add(att);
                    }
                }

                // Get name and split on colon
                Element element = null;
                String qualifiedName = node.getNodeName();
                int split;
                if ((split = qualifiedName.indexOf(":")) != -1) {
                    String prefix = qualifiedName.substring(0, split);
                    String localName = qualifiedName.substring(split + 1);
                    String uri = "foo";
                    element = new Element(localName, prefix, uri);
                } else {
                    element = new Element(qualifiedName);
                }

                if (atRoot) {
                    // If at root, set as document root
                    doc.setRootElement(element);
                } else {
                    // else add to parent element
                    current.addChild(element);
                }

                // Handle attributes
                for (int i=0, size=elementAttributes.size(); i<size; i++) {
                    Node att = (Node)elementAttributes.get(i);

                    element.addAttribute(att.getNodeName(),
                                         att.getNodeValue());
                }

                // Recurse on child nodes
                NodeList children = node.getChildNodes();
                for (int i=0, size=children.getLength(); i<size; i++) {
                    buildTree(children.item(i), doc, element, false);
                }
                break;

            case Node.TEXT_NODE:
                String text = node.getNodeValue();
                if (!text.trim().equals("")) {
                    if (node.getPreviousSibling() == null) {
                        text = ltrim(text);
                    }
                    if (node.getNextSibling() == null) {
                        text = rtrim(text);
                    }
                    current.addChild(text);
                }
                break;

            case Node.CDATA_SECTION_NODE:
                String cdata = node.getNodeValue();
                if (!cdata.equals("")) {
                    current.addChild(cdata);
                }
                break;


            case Node.PROCESSING_INSTRUCTION_NODE:
                if (atRoot) {
                    doc.addProcessingInstruction(
                        node.getNodeName(),
                        node.getNodeValue());
                } else {
                    current.addChild(
                        new ProcessingInstruction(node.getNodeName(),
                                                  node.getNodeValue()));
                }
                break;

            case Node.COMMENT_NODE:
                if (atRoot) {
                    doc.addComment(new Comment(node.getNodeValue()));
                } else {
                    current.addChild(new Comment(node.getNodeValue()));
                }
                break;

            case Node.ENTITY_REFERENCE_NODE:
                Entity entity = new Entity(node.getNodeName());

                // XXX: Temp Hack (brett)
                entity.setContent(node.getFirstChild().getNodeValue());

                current.addChild(entity);
                break;

            case Node.ENTITY_NODE:
                // ??
                break;

            case Node.DOCUMENT_TYPE_NODE:
                DocumentType domDocType = (DocumentType)node;
                String publicID = domDocType.getPublicId();
                String systemID = domDocType.getSystemId();

                DocType docType = new DocType(domDocType.getName());
                if ((publicID != null) && (!publicID.equals(""))) {
                    docType.setPublicID(publicID);
                }
                if ((systemID != null) && (!systemID.equals(""))) {
                    docType.setSystemID(systemID);
                }

                doc.setDocType(docType);
                break;
        }
    }

    /**
     * <p>
     *  Trim the right spacing off of a <code>String</code>.
     * </p>
     *
     * @param orig <code>String</code> to rtrim.
     * @return <code>String</code> - orig with no right spaces
     */
    private String rtrim(String orig) {
        int len = orig.length();
        int st = 0;
        int off = 0;
        char[] val = orig.toCharArray();

        while ((st < len) && (val[off + len - 1] <= ' ')) {
            len--;
        }
        return ((st > 0) || (len < orig.length())) ? orig.substring(st, len) : orig;
    }

    /**
     * <p>
     *  Trim the left spacing off of a <code>String</code>.
     * </p>
     *
     * @param orig <code>String</code> to rtrim.
     * @return <code>String</code> - orig with no left spaces
     */
    private String ltrim(String orig) {
        int len = orig.length();
        int st = 0;
        int off = 0;
        char[] val = orig.toCharArray();

        while ((st < len) && (val[off + st] <= ' ')) {
            st++;
        }
        return ((st > 0) || (len < orig.length())) ? orig.substring(st, len) : orig;
    }

}
