/*-- 

 Copyright (C) 2000 Brett McLaughlin & Jason Hunter.
 All rights reserved.
 
 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:
 
 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions, and the following disclaimer.
 
 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions, the disclaimer that follows these
    conditions, and/or other materials provided with the distribution.
 
 3. The name "JDOM" must not be used to endorse or promote products
    derived from this software without prior written permission.  For
    written permission, please contact license@jdom.org.
 
 4. Products derived from this software may not be called "JDOM", nor
    may "JDOM" appear in their name, without prior written permission
    from the JDOM Project Management (pm@jdom.org).
 
 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.

 This software consists of voluntary contributions made by many 
 individuals on behalf of the JDOM Project and was originally 
 created by Brett McLaughlin <brett@jdom.org> and 
 Jason Hunter <jhunter@jdom.org>.  For more information on the 
 JDOM Project, please see <http://www.jdom.org/>.
 
 */

package org.jdom.output;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import org.jdom.Attribute;
import org.jdom.Comment;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Entity;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.ProcessingInstruction;
import org.jdom.adapters.DOMAdapter;



/**
 * Takes a JDOM tree and outputs to a DOM tree.
 *
 * @author Brett McLaughlin
 * @author Jason Hunter
 * @author Matthew Merlo
 * @version 1.0
 */
public class DOMOutputter {

    /** Default adapter class */
    private static final String DEFAULT_ADAPTER_CLASS =
        "org.jdom.adapters.XercesDOMAdapter";

    /**
     * <p>
     * This creates a <code>DOMOutputter</code>.
     * </p>
     */
    public DOMOutputter() {
    }

    /**
     * <p>
     * This converts the JDOM <code>Document</code> parameter to a
     * DOM Document, returning the DOM version.  The default DOM adapter class
     * is used.
     * </p>
     *
     * @param document <code>Document</code> to output.
     * @return an <code>org.w3c.dom.Document</code> version
     */
    public org.w3c.dom.Document output(Document document) {
        return output(document, DEFAULT_ADAPTER_CLASS);
    }

    /**
     * <p>
     * This converts the JDOM <code>Document</code> parameter to a 
     * DOM Document, returning the DOM version.  The specified DOM adapter
     * class (see org.jdom.adapters.*) is used, as a way to choose the 
     * DOM implementation.
     * </p>
     *
     * @param document <code>Document</code> to output.
     * @param domAdapterClass DOM adapter class to use
     * @return an <code>org.w3c.dom.Document</code> version
     */
    public org.w3c.dom.Document output(Document document,
                                       String domAdapterClass) {
        LinkedList namespaces = new LinkedList();
        org.w3c.dom.Document domDoc = null;
        try {
            DOMAdapter adapter =
                (DOMAdapter)Class.forName(domAdapterClass).newInstance();

            domDoc = adapter.createDocument();

            // XXX We could reuse some of the "walking" code

            // XXX Should whitespace handling be configurable?

            List docContent = document.getMixedContent();
            for (int i = 0; i < docContent.size(); i++) {
                Object docObject = docContent.get(i);

                if (docObject instanceof Comment) {
                    org.w3c.dom.Comment domComment =
                        domDoc.createComment(((Comment)docObject).getText());
                    domDoc.appendChild(domComment);
                }

                else if (docObject instanceof ProcessingInstruction) {
                    ProcessingInstruction pi = 
                        (ProcessingInstruction)docObject;
                    org.w3c.dom.ProcessingInstruction domPI =
                         domDoc.createProcessingInstruction(
                         pi.getTarget(), pi.getData());
                    domDoc.appendChild(domPI);
                }

                else if (docObject instanceof Element) {
                    // Build the rest of the DOM tree from JDOM's root element
                    buildDOMTree(docObject, domDoc, null, true, namespaces);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return domDoc;
    }


    /**
     * <p>
     * This takes a JDOM <code>Object</code> and builds up
     *   a DOM tree, recursing until the JDOM tree is exhausted
     *   and the DOM tree results.
     * </p>
     *
     * @param content <code>Object</node> to examine.
     * @param doc DOM <code>Document</code> being built.
     * @param current <code>Element</code> that is current parent.
     * @param atRoot <code>boolean</code> indicating whether at root level.
     * @param namespaces <code>LinkedList</code> containing namespaces in scope
     */
    protected void buildDOMTree(Object content,
                                org.w3c.dom.Document doc,
                                org.w3c.dom.Element current,
                                boolean atRoot,
                                LinkedList namespaces) {
        boolean printedNS = false;
        if (content instanceof Element) {
            Element element = (Element)content;
            org.w3c.dom.Element domElement =
                doc.createElement(element.getQualifiedName());

            // Add namespace attributes
            Namespace ns = element.getNamespace();
            if ((ns != Namespace.NO_NAMESPACE) && 
                (ns != Namespace.XML_NAMESPACE) &&
                (!namespaces.contains(ns))) {
                String attrName = getXmlnsTagFor(ns);
                printedNS = true;
                namespaces.add(ns);
                domElement.setAttribute(attrName, ns.getURI());
            }

            // Add attributes to the DOM element
            List attributes = element.getAttributes();
            for (int i = 0, size = attributes.size(); i < size; i++) {
                Attribute attribute = (Attribute)attributes.get(i);
                Namespace ns1 = attribute.getNamespace();
                if ((ns1 != Namespace.NO_NAMESPACE) && 
                    (ns1 != Namespace.XML_NAMESPACE) &&
                                 (!namespaces.contains(ns1))) {
                    String attrName = getXmlnsTagFor(ns1);
                    printedNS = true;
                    namespaces.add(ns1);
                    domElement.setAttribute(attrName, ns1.getURI());
                }
                domElement.setAttribute(attribute.getQualifiedName(),
                                        attribute.getValue());
            }

            if (atRoot) {
                // If at root, set as document root
                doc.appendChild(domElement);
            } else {
                // Else add to parent element
                current.appendChild(domElement);
            }

            // Recurse on child nodes
            List children = ((Element)content).getMixedContent();
            for (int i = 0; i < children.size(); i++) {
                buildDOMTree(children.get(i), doc, 
                             domElement, false, namespaces);
            }
        } else if (content instanceof String) {
            // XXX: CDATA currently handled as text (Matt)
            org.w3c.dom.Text domText = doc.createTextNode((String)content);
            current.appendChild(domText);
        } else if (content instanceof ProcessingInstruction) {
            ProcessingInstruction pi = (ProcessingInstruction) content;
            org.w3c.dom.ProcessingInstruction domPI =
                doc.createProcessingInstruction(pi.getTarget(), pi.getData());
            current.appendChild(domPI);
        } else if (content instanceof Comment) {
            org.w3c.dom.Comment domComment = 
                doc.createComment(((Comment)content).getText());
            current.appendChild(domComment);
        } else if (content instanceof Entity) {
            org.w3c.dom.EntityReference domEntity = 
                doc.createEntityReference(((Entity)content).getName());
            doc.appendChild(domEntity);
        }
        /*
        *
        * XXX: DOM 1 does not allow us to set DocType (Matt)
        *
        } else if (content instanceof DocType) {
            org.w3c.dom.DocumentType domDocType = 
               doc.createEntityReference(((DocType)content).getName());
        } */

        // After recursion, remove the namespace defined on the element,
        // if any
        if (printedNS) {
            namespaces.removeLast();
        }
    }

    /**
     * <p>
     *  This will handle adding any <code>{@link Namespace}</code>
     *    attributes to the DOM tree.
     * </p>
     *
     * @param ns <code>Namespace</code> to add definition of
     */
    public String getXmlnsTagFor(Namespace ns) {
        String attrName = "xmlns";
        if (!ns.getPrefix().equals("")) {
            attrName += ":";
            attrName += ns.getPrefix();
        }
        return attrName;
    }
}
