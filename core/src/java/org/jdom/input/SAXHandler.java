/*-- 

 $Id: SAXHandler.java,v 1.2 2001/03/28 18:35:23 bmclaugh Exp $

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
 created by Brett McLaughlin <brett@jdom.org> and 
 Jason Hunter <jhunter@jdom.org>.  For more information on the 
 JDOM Project, please see <http://www.jdom.org/>.
 
 */

package org.jdom.input;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Stack;

import org.jdom.Attribute;
import org.jdom.CDATA;
import org.jdom.Comment;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Entity;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.ProcessingInstruction;

import org.xml.sax.Attributes;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * <p><code>SAXHandler</code> supports SAXBuilder</p>
 *
 * @author Brett McLaughlin
 * @author Jason Hunter
 */
public class SAXHandler extends DefaultHandler implements LexicalHandler {

    /** <code>Document</code> object being built */
    private Document document;

    /** Element stack */
    private Stack stack;

    /** Indicator of where in the document we are */
    private boolean atRoot;

    /** Indicator of whether we are in a DTD */
    private boolean inDTD;

    /** Indicator of whether we are in a CDATA */
    private boolean inCDATA;

    /** Indicator of whether we are in an <code>Entity</code> */
    private boolean inEntity;

    /** Temporary holder for namespaces that have been declared with
      * startPrefixMapping, but are not yet available on the element */
    private LinkedList declaredNamespaces;

    /** The namespaces in scope and actually attached to an element */
    private LinkedList availableNamespaces;

    /**
     * <p>
     * This will set the <code>Document</code> to use.
     * </p>
     *
     * @param document <code>Document</code> being parsed.
     * @throws IOException when errors occur.
     */
    public SAXHandler(Document document) throws IOException {
        this.document = document;
        atRoot = true;
        stack = new Stack();
        declaredNamespaces = new LinkedList();
        availableNamespaces = new LinkedList();
        availableNamespaces.add(Namespace.XML_NAMESPACE);
        inEntity = false;
        inDTD = false;
        inCDATA = false;
    }

    /**
     * <p>
     * This will indicate that a processing instruction (other than
     *   the XML declaration) has been encountered.
     * </p>
     *
     * @param target <code>String</code> target of PI
     * @param data <code>String</code containing all data sent to the PI.
     *             This typically looks like one or more attribute value
     *             pairs.
     * @throws SAXException when things go wrong
     */
    public void processingInstruction(String target, String data)
        throws SAXException {

        if (atRoot) {
            document.addContent(new ProcessingInstruction(target, data));
        } else {
            ((Element)stack.peek()).addContent(
                new ProcessingInstruction(target, data));
        }
    }

    /**
     * <p>
     * This will add the prefix mapping to the JDOM
     *   <code>Document</code> object.
     * </p>
     *
     * @param prefix <code>String</code> namespace prefix.
     * @param uri <code>String</code> namespace URI.
     */
    public void startPrefixMapping(String prefix, String uri)
        throws SAXException {

        Namespace ns = Namespace.getNamespace(prefix, uri);
        declaredNamespaces.add(ns);
    }

    /**
     * <p>
     * This will add the prefix mapping to the JDOM
     *   <code>Document</code> object.
     * </p>
     *
     * @param prefix <code>String</code> namespace prefix.
     * @param uri <code>String</code> namespace URI.
     */
    public void endPrefixMapping(String prefix)
        throws SAXException {

        // Remove the namespace from the available list
        // (Should find the namespace fast because recent adds
        // are at the front of the list.  It may not be the head
        // tho because endPrefixMapping calls on the same element
        // can come in any order.)
        Iterator itr = availableNamespaces.iterator();
        while (itr.hasNext()) {
            Namespace ns = (Namespace) itr.next();
            if (prefix.equals(ns.getPrefix())) {
                itr.remove();
                return;
            }
        }
    }

    /**
     * <p>
     * This reports the occurrence of an actual element.  It will include
     *   the element's attributes, with the exception of XML vocabulary
     *   specific attributes, such as
     *   <code>xmlns:[namespace prefix]</code> and
     *   <code>xsi:schemaLocation</code>.
     * </p>
     *
     * @param namespaceURI <code>String</code> namespace URI this element
     *                     is associated with, or an empty
     *                     <code>String</code>
     * @param localName <code>String</code> name of element (with no
     *                  namespace prefix, if one is present)
     * @param qName <code>String</code> XML 1.0 version of element name:
     *                [namespace prefix]:[localName]
     * @param atts <code>Attributes</code> list for this element
     * @throws SAXException when things go wrong
     */
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts)
                             throws SAXException {
        Element element = null;

        if ((namespaceURI != null) && (!namespaceURI.equals(""))) {
            String prefix = "";

            // Determine any prefix on the Element
            if (localName != qName) {
                int split = qName.indexOf(":");
                prefix = qName.substring(0, split);
            }
            Namespace elementNamespace =
                Namespace.getNamespace(prefix, namespaceURI);
            element = new Element(localName, elementNamespace);

            // Remove this namespace from those in the temp declared list
            declaredNamespaces.remove(elementNamespace);

            // It's now in available scope
            availableNamespaces.addFirst(elementNamespace);
        } else {
            element = new Element(localName);
        }

        // Take leftover declared namespaces and add them to this element's
        // map of namespaces
        transferNamespaces(element);

        // Handle attributes
        for (int i=0, len=atts.getLength(); i<len; i++) {
            Attribute attribute = null;

            String attLocalName = atts.getLocalName(i);
            String attQName = atts.getQName(i);

            if (attLocalName != attQName) {
                String attPrefix = attQName.substring(0, attQName.indexOf(":"));
                attribute = new Attribute(attLocalName, atts.getValue(i),
                                          getNamespace(attPrefix));
            } else {
                attribute = new Attribute(attLocalName, atts.getValue(i));
            }

            element.addAttribute(attribute);
        }

        if (atRoot) {
            document.setRootElement(element);
            stack.push(element);
            atRoot = false;
        } else {
            ((Element)stack.peek()).addContent(element);
            stack.push(element);
        }
    }

    /**
     * <p>
     *  This will take the supplied <code>{@link Element}</code> and
     *    transfer its namespaces to the global namespace storage.
     * </p>
     *
     * @param element <code>Element</code> to read namespaces from.
     */
    private void transferNamespaces(Element element) {
        Iterator i = declaredNamespaces.iterator();
        while (i.hasNext()) {
            Namespace ns = (Namespace)i.next();
            i.remove();
            availableNamespaces.addFirst(ns);
            element.addNamespaceDeclaration(ns);
        }
    }

    /**
     * <p>
     *  For a given namespace prefix, this will return the
     *    <code>{@link Namespace}</code> object for that prefix,
     *    within the current scope.
     * </p>
     *
     * @param prefix namespace prefix.
     * @return <code>Namespace</code> - namespace for supplied prefix.
     */
    private Namespace getNamespace(String prefix) {
        Iterator i = availableNamespaces.iterator();
        while (i.hasNext()) {
            Namespace ns = (Namespace)i.next();
            if (prefix.equals(ns.getPrefix())) {
                return ns;
            }
        }
        return Namespace.NO_NAMESPACE;
    }

    /**
     * <p>
     * This will report character data (within an element).
     * </p>
     *
     * @param ch <code>char[]</code> character array with character data
     * @param start <code>int</code> index in array where data starts.
     * @param length <code>int</code> length of data.
     * @throws SAXException when things go wrong
     */
    public void characters(char[] ch, int start, int length)
        throws SAXException {

        String data = new String(ch, start, length);

        if (inCDATA) {
            ((Element)stack.peek()).addContent(new CDATA(data));
 
        /**
         * This is commented out because of some problems with
         *   the inline DTDs that Xerces seems to have.
        } else if (!inDTD) {
            if (inEntity) {
                ((Entity)stack.peek()).setContent(data);
            } else {
                Element e = (Element)stack.peek();
                e.addContent(data);
            }
         */
        } else if (inEntity) {
            ((Entity)stack.peek()).setContent(data);
        } else {
            Element e = (Element)stack.peek();
            e.addContent(data);
        }
    }

    /**
     * <p>
     * Indicates the end of an element
     *   (<code>&lt;/[element name]&gt;</code>) is reached.  Note that
     *   the parser does not distinguish between empty
     *   elements and non-empty elements, so this will occur uniformly.
     * </p>
     *
     * @param namespaceURI <code>String</code> URI of namespace this
     *                     element is associated with
     * @param localName <code>String</code> name of element without prefix
     * @param qName <code>String</code> name of element in XML 1.0 form
     * @throws SAXException when things go wrong
     */
    public void endElement(String namespaceURI, String localName,
                           String qName) throws SAXException {

        Element element = (Element)stack.pop();
        
        if (stack.empty()) {
            atRoot = true;
        }

        // Remove the namespaces that this element makes available
        availableNamespaces.remove(element.getAdditionalNamespaces());
    }

    /**
     * <p>
     * This will signify that a DTD is being parsed, and can be
     *   used to ensure that comments and other lexical structures
     *   in the DTD are not added to the JDOM <code>Document</code>
     *   object.
     * </p>
     *
     * @param name <code>String</code> name of element listed in DTD
     * @param publicId <code>String</code> public ID of DTD
     * @param systemId <code>String</code> syste ID of DTD
     */
    public void startDTD(String name, String publicId, String systemId)
        throws SAXException {

        document.setDocType(
            new DocType(name, publicId, systemId));
        inDTD = true;
    }

    /**
     * <p>
     * This signifies that the reading of the DTD is complete.
     * </p>
     */
    public void endDTD() throws SAXException {
        inDTD = false;
    }

    public void startEntity(String name)
        throws SAXException {

        // Ignore DTD references, and translate the standard 5
        if ((!inDTD) &&
            (!name.equals("amp")) &&
            (!name.equals("lt")) &&
            (!name.equals("gt")) &&
            (!name.equals("apos")) &&
            (!name.equals("quot"))) {

            Entity entity = new Entity(name);
            ((Element)stack.peek()).addContent(entity);
            stack.push(entity);
            inEntity = true;
        }
    }

    public void endEntity(String name) throws SAXException {
        if (inEntity) {
            stack.pop();
            inEntity = false;
        }
    }

    /**
     * <p>
     * Report a CDATA section - ignored in SAXBuilder.
     * </p>
     */
    public void startCDATA() throws SAXException {
        inCDATA = true;
    }

    /**
     * <p>
     * Report a CDATA section - ignored in SAXBuilder.
     * </p>
     */
    public void endCDATA() throws SAXException {
        inCDATA = false;
    }

    /**
     * <p>
     * This reports that a comments is parsed.  If not in the
     *   DTD, this comment is added to the current JDOM
     *   <code>Element</code>, or the <code>Document</code> itself
     *   if at that level.
     * </p>
     *
     * @param ch <code>ch[]</code> array of comment characters.
     * @param start <code>int</code> index to start reading from.
     * @param end <code>int</code> index to end reading at.
     */
    public void comment(char[] ch, int start, int end)
        throws SAXException {

        String commentText = new String(ch, start, end);
        if ((!inDTD) && (!commentText.equals(""))) {
            if (stack.empty()) {
                document.addContent(
                   new Comment(commentText));
            } else {
                ((Element)stack.peek()).addContent(
                    new Comment(commentText));
            }
        }
    }
}
