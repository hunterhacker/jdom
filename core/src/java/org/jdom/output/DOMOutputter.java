/*-- 

 Copyright (C) 2000 Brett McLaughlin & Jason Hunter.
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
    written permission, please contact license@jdom.org.
 
 4. Products derived from this software may not be called "JDOM", nor
    may "JDOM" appear in their name, without prior written permission
    from the JDOM Project Management (pm@jdom.org).
 
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

import java.io.*;
import java.util.*;
import java.lang.reflect.*;

import org.jdom.*;
import org.jdom.adapters.*;


/**
 * Takes a JDOM tree and outputs to a DOM tree.
 *
 * @author Brett McLaughlin
 * @author Jason Hunter
 * @author Matthew Merlo
 * @author Dan Schaffer
 * @author Yusuf Goolamabbas
 * @version 1.0
 */
public class DOMOutputter {

    /** Default adapter class */
    private static final String DEFAULT_ADAPTER_CLASS =
        "org.jdom.adapters.XercesDOMAdapter";

    /** Adapter to use for interfacing with the DOM implementation */
    private String adapterClass;

    /**
     * <p>
     * This creates a new DOMOutputter which will attempt to first locate
     * a DOM implementation to use via JAXP, and if JAXP does not exist or
     * there's a problem, will fall back to the default parser.
     * </p>
     */
    public DOMOutputter() {
        // nothing
    }

    /**
     * <p>
     * This creates a new DOMOutputter using the specified DOMAdapter
     * implementation as a way to choose the underlying parser.
     * </p>
     *
     * @param adapterClass <code>String</code> name of class
     *                     to use for DOM output
     */
    public DOMOutputter(String adapterClass) {
        this.adapterClass = adapterClass;
    }


    /**
     * <p>
     * This converts the JDOM <code>Document</code> parameter to a 
     * DOM Document, returning the DOM version.  The DOM implementation
     * is the one chosen in the constructor.
     * </p>
     *
     * @param document <code>Document</code> to output.
     * @return an <code>org.w3c.dom.Document</code> version
     */
    public org.w3c.dom.Document output(Document document)
                                       throws JDOMException {
        NamespaceStack namespaces = new NamespaceStack();

        org.w3c.dom.Document domDoc = null;
        try {
            domDoc = createDOMDocument();

            // Assign DOCTYPE
            DocType dt = document.getDocType();
            if (dt != null) {
                // The DOM Level 2 doesn't support editing DocumentType nodes
                // Perhaps we could do an adapter.createDocument(DocType)?
            }

            // Add mixed content
            Iterator itr = document.getMixedContent().iterator();
            while (itr.hasNext()) {
                Object node = itr.next();

                if (node instanceof Element) {
                    Element element = (Element) node;
                    org.w3c.dom.Element domElement =
                        output(element, domDoc, namespaces);
                    domDoc.appendChild(domElement);
                }
                else if (node instanceof Comment) {
                    Comment comment = (Comment) node;
                    org.w3c.dom.Comment domComment =
                        domDoc.createComment(comment.getText());
                    domDoc.appendChild(domComment);
                }
                else if (node instanceof ProcessingInstruction) {
                    ProcessingInstruction pi = 
                        (ProcessingInstruction) node;
                    org.w3c.dom.ProcessingInstruction domPI =
                         domDoc.createProcessingInstruction(
                         pi.getTarget(), pi.getData());
                    domDoc.appendChild(domPI);
                }
                else {
                    throw new JDOMException(
                        "Document contained top-level content with type:" +
                        node.getClass().getName());
                }
            }
        }
        catch (Throwable e) {
            throw new JDOMException("Exception outputting Document", e);
        }

        return domDoc;
    }

    /** 
     * <p>
     * This converts the JDOM <code>Element</code> parameter to a 
     * DOM Element, returning the DOM version.
     * </p>
     *
     * @param element <code>Element</code> to output.
     * @return an <code>org.w3c.dom.Element</code> version
     */
    public org.w3c.dom.Element output(Element element) throws JDOMException {
        try {
            org.w3c.dom.Document domDoc = createDOMDocument();
            return output(element, domDoc, new NamespaceStack());
        }
        catch (Throwable e) {
            throw new JDOMException("Exception outputting Element " +
                                    element.getQualifiedName(), e);
        }
    }

    private org.w3c.dom.Document createDOMDocument() throws Throwable {
        if (adapterClass != null) {
            // The user knows that they want to use a particular impl
            try {
                DOMAdapter adapter =
                    (DOMAdapter)Class.forName(adapterClass).newInstance();
                // System.out.println("using specific " + adapterClass);
                return adapter.createDocument();
            }
            catch (ClassNotFoundException e) {
                // e.printStackTrace();
            }
        }
        else {
            // Try using JAXP...
            // Note we need DOM Level 2 and thus JAXP 1.1.
            // If JAXP 1.0 is all that's available then we skip
            // to the hard coded default parser
            try {
                // Verify this is JAXP 1.1 (or later)
                Class.forName("javax.xml.transform.Transformer");

                // Try JAXP 1.1 calls to build the document
                Class factoryClass =
                    Class.forName("javax.xml.parsers.DocumentBuilderFactory");

                // factory = DocumentBuilderFactory.newInstance();
                Method newParserInstance =
                    factoryClass.getMethod("newInstance", null);
                Object factory = newParserInstance.invoke(null, null);

                // jaxpParser = factory.newDocumentBuilder();
                Method newDocBuilder =
                    factoryClass.getMethod("newDocumentBuilder", null);
                Object jaxpParser  = newDocBuilder.invoke(factory, null);

                // domDoc = jaxpParser.newDocument();
                Class parserClass = jaxpParser.getClass();
                Method newDoc = parserClass.getMethod("newDocument", null);
                return (org.w3c.dom.Document) newDoc.invoke(jaxpParser, null);
                // System.out.println("Using jaxp " +
                //   domDoc.getClass().getName());
            } catch (ClassNotFoundException e) {
                // e.printStackTrace();
            } catch (NoSuchMethodException e) {
                // e.printStackTrace();
            } catch (InvocationTargetException ite) {
                throw ite.getTargetException(); // throw the root cause
            }
        }

        // If no DOM doc yet, try to use a hard coded default
        try {
            DOMAdapter adapter = (DOMAdapter)
                Class.forName(DEFAULT_ADAPTER_CLASS).newInstance();
            return adapter.createDocument();
            // System.out.println("Using default " +
            //   DEFAULT_ADAPTER_CLASS);
        }
        catch (ClassNotFoundException e) {
            // e.printStackTrace();
        }

        throw new Exception("No JAXP or default parser available");
    }

    protected org.w3c.dom.Element output(Element element,
                                         org.w3c.dom.Document domDoc,
                                         NamespaceStack namespaces)
                                         throws JDOMException {
        try {
            int previouslyDeclaredNamespaces = namespaces.size();

            org.w3c.dom.Element domElement = 
                domDoc.createElementNS(element.getNamespaceURI(),
                                       element.getQualifiedName());

            // Add namespace attributes, beginning with the element's own
            // Do this only if it's not the XML namespace and it's
            // not the NO_NAMESPACE with the prefix "" not yet mapped
            // (we do output xmlns="" if the "" prefix was already used 
            // and we need to reclaim it for the NO_NAMESPACE)
            Namespace ns = element.getNamespace();
            if (ns != Namespace.XML_NAMESPACE &&
                           !(ns == Namespace.NO_NAMESPACE && 
                             namespaces.getURI("") == null)) {
                String prefix = ns.getPrefix();
                String uri = namespaces.getURI(prefix);
                if (!ns.getURI().equals(uri)) { // output a new namespace decl
                    namespaces.push(ns);
                    String attrName = getXmlnsTagFor(ns);
                    domElement.setAttribute(attrName, ns.getURI());
                }
            }

            // Add additional namespaces also
            Iterator itr = element.getAdditionalNamespaces().iterator();
            while (itr.hasNext()) {
                Namespace additional = (Namespace)itr.next();
                String prefix = additional.getPrefix();
                String uri = namespaces.getURI(prefix);
                if (!additional.getURI().equals(uri)) {
                    String attrName = getXmlnsTagFor(additional);
                    domElement.setAttribute(attrName, additional.getURI());
                    namespaces.push(additional);
                }
            }

            // Add attributes to the DOM element
            itr = element.getAttributes().iterator();
            while (itr.hasNext()) {
                Attribute attribute = (Attribute) itr.next();
                domElement.setAttributeNode(output(attribute, domDoc));
                Namespace ns1 = attribute.getNamespace();
                if ((ns1 != Namespace.NO_NAMESPACE) && 
                    (ns1 != Namespace.XML_NAMESPACE)) {
                    String prefix = ns1.getPrefix();
                    String uri = namespaces.getURI(prefix);
                    if (!ns.getURI().equals(uri)) { // output a new decl
                        String attrName = getXmlnsTagFor(ns1);
                        domElement.setAttribute(attrName, ns1.getURI());
                        namespaces.push(ns);
                    }
                }
                // Crimson doesn't like setAttributeNS() for non-NS attribs
                if ("".equals(attribute.getNamespacePrefix())) {
                    // No namespace, use setAttribute
                    domElement.setAttribute(attribute.getQualifiedName(),
                                            attribute.getValue());
                }
                else {
                    domElement.setAttributeNS(attribute.getNamespaceURI(),
                                              attribute.getQualifiedName(),
                                              attribute.getValue());
                }
            }

            // Add mixed content to the DOM element
            itr = element.getMixedContent().iterator();
            while (itr.hasNext()) {
                Object node = itr.next();

                if (node instanceof Element) {
                    Element e = (Element) node;
                    org.w3c.dom.Element domElt = output(e, domDoc, namespaces);
                    domElement.appendChild(domElt);
                }
                else if (node instanceof String) {
                    String str = (String) node;
                    org.w3c.dom.Text domText = domDoc.createTextNode(str);
                    domElement.appendChild(domText);
                }
                else if (node instanceof CDATA) {
                    CDATA cdata = (CDATA) node;
                    org.w3c.dom.CDATASection domCdata =
                        domDoc.createCDATASection(cdata.getText());
                    domElement.appendChild(domCdata);
                }
                else if (node instanceof Comment) {
                    Comment comment = (Comment) node;
                    org.w3c.dom.Comment domComment =
                        domDoc.createComment(comment.getText());
                    domElement.appendChild(domComment);
                }
                else if (node instanceof ProcessingInstruction) {
                    ProcessingInstruction pi = 
                        (ProcessingInstruction) node;
                    org.w3c.dom.ProcessingInstruction domPI =
                         domDoc.createProcessingInstruction(
                         pi.getTarget(), pi.getData());
                    domElement.appendChild(domPI);
                }
                else if (node instanceof Entity) {
                    Entity entity = (Entity) node;
                    org.w3c.dom.EntityReference domEntity = 
                        domDoc.createEntityReference(entity.getName());
                    domElement.appendChild(domEntity);
                }
                else {
                    throw new JDOMException(
                        "Element contained content with type:" +
                        node.getClass().getName());
                }
            }
    
            // Remove declared namespaces from stack
            while (namespaces.size() > previouslyDeclaredNamespaces) {
                namespaces.pop();
            }

            return domElement;
        }
        catch (Exception e) {
            throw new JDOMException("Exception outputting Element " +
                                    element.getQualifiedName(), e);
        }
    }

    /**
     * <p>
     * This converts the JDOM <code>Attribute</code> parameter to a
     * DOM <code>Attr</code>, returning the DOM version.
     * </p>
     * @param attribute <code>Attribute</code> to output.
     * @return an <code>org.w3c.dom.Attr</code> version
     */
    public org.w3c.dom.Attr output(Attribute attribute) throws JDOMException {
        org.w3c.dom.Document domDoc = null;
        try {
            DOMAdapter adapter =
                (DOMAdapter)Class.forName(adapterClass).newInstance();
            domDoc = adapter.createDocument();
            return output(attribute, domDoc);
        }
        catch (Exception e) {
            throw new JDOMException("Exception outputting Attribute " +
                                    attribute.getQualifiedName(), e);
        }
    }

    protected org.w3c.dom.Attr output(Attribute attribute,
                                      org.w3c.dom.Document domDoc)
                                      throws JDOMException {
         org.w3c.dom.Attr domAttr = null;
         try {
             domAttr = domDoc.createAttributeNS(attribute.getNamespaceURI(),
                                                attribute.getQualifiedName());
             domAttr.setValue(attribute.getValue());
         } catch (Exception e) {
             throw new JDOMException("Exception outputting Attribute " +
                                     attribute.getQualifiedName(), e);
         }
         return domAttr;
    }

    /**
     * <p>
     *  This will handle adding any <code>{@link Namespace}</code>
     *    attributes to the DOM tree.
     * </p>
     *
     * @param ns <code>Namespace</code> to add definition of
     */
    private String getXmlnsTagFor(Namespace ns) {
        String attrName = "xmlns";
        if (!ns.getPrefix().equals("")) {
            attrName += ":";
            attrName += ns.getPrefix();
        }
        return attrName;
    }
}
