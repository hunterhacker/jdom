/*-- 

 $Id: SAXHandler.java,v 1.14 2001/06/01 23:15:46 jhunter Exp $

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

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;

import org.jdom.*;

import org.xml.sax.*;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * <p><code>SAXHandler</code> supports SAXBuilder</p>
 *
 * @author Brett McLaughlin
 * @author Jason Hunter
 */
public class SAXHandler extends DefaultHandler implements LexicalHandler,
                                                          DeclHandler {

    private static final String CVS_ID = 
      "@(#) $RCSfile: SAXHandler.java,v $ $Revision: 1.14 $ $Date: 2001/06/01 23:15:46 $ $Name:  $";

    /** <code>Document</code> object being built */
    private Document document;

    // Note: keeping a "current element" variable to avoid the constant
    // peek() calls to the top of the stack has shown to cause no noticeable 
    // performance improvement.

    /** Element stack */
    private Stack stack;

    /** Indicator of where in the document we are */
    private boolean atRoot;

    /** Indicator of whether we are in a DTD */
    private boolean inDTD = false;

    /** Indicator of whether we are in a CDATA */
    private boolean inCDATA = false;

    /** Indicator of whether we should expand entities */
    private boolean expand = true;

    /** Indicator of whether we are actively suppressing (non-expanding) a 
        current entity */
    private boolean suppress = false;

    /** How many nested entities we're currently within */
    private int entityDepth = 0;

    /** Temporary holder for namespaces that have been declared with
      * startPrefixMapping, but are not yet available on the element */
    private LinkedList declaredNamespaces;

    /** The namespaces in scope and actually attached to an element */
    private LinkedList availableNamespaces;

    private Map externalEntities;

    /** The JDOMFactory used for JDOM object creation */
    private JDOMFactory factory;

    /** Whether to ignore ignorable whitespace */
    private boolean ignoringWhite = false;

    /**
     * <p>
     * This will set the <code>Document</code> to use.
     * </p>
     *
     * @param document <code>Document</code> being parsed.
     * @throws IOException when errors occur.
     *
     * @deprecated Deprecated in beta7, use SAXHandler() instead and let
     * SAXHandler create the Document, then retrieve it with getDocument()
     */
    public SAXHandler(Document document) throws IOException {
        this(new DefaultJDOMFactory());
        this.document = document;
    }

    /**
     * <p>
     * This will create a new <code>SAXHandler</code> that listens to SAX
     * events and creates a JDOM Document.  The objects will be constructed
     * using the default factory.
     * </p>
     *
     * @throws IOException when errors occur.
     */
    public SAXHandler() throws IOException {
        this((JDOMFactory)null);
    }

    /**
     * <p>
     * This will create a new <code>SAXHandler</code> that listens to SAX
     * events and creates a JDOM Document.  The objects will be constructed
     * using the provided factory.
     * </p>
     *
     * @param factory <code>JDOMFactory</code> to be used for constructing
     * objects
     * @throws IOException when errors occur.
     */
    public SAXHandler(JDOMFactory factory) throws IOException {
        if (factory != null) {
            this.factory = factory;
        } else {
            this.factory = new DefaultJDOMFactory();
        }

        atRoot = true;
        stack = new Stack();
        declaredNamespaces = new LinkedList();
        availableNamespaces = new LinkedList();
        availableNamespaces.add(Namespace.XML_NAMESPACE);
        externalEntities = new HashMap();

        document = this.factory.document((Element)null);
    }

    /**
     * <p>
     * Returns the document.  Should be called after parsing is complete.
     * </p>
     *
     * @return <code>Document</code> - Document that was built
     */
    public Document getDocument() {
        return document;
    }

    /**
     * <p>
     * This sets whether or not to expand entities during the build.
     * A true means to expand entities as normal content.  A false means to
     * leave entities unexpanded as <code>EntityRef</code> objects.  The
     * default is true.
     * </p>
     *
     * @param expand <code>boolean</code> indicating whether entity expansion
     * should occur.
     */
    public void setExpandEntities(boolean expand) {
        this.expand = expand;
    }

    /**
     * <p>
     * Specifies whether or not the parser should elminate whitespace in
     * element content (sometimes known as "ignorable whitespace") when
     * building the document.  Only whitespace which is contained within
     * element content that has an element only content model will be
     * eliminated (see XML Rec 3.2.1).  For this setting to take effect
     * requires that validation be turned on.  The default value of this
     * setting is <code>false</code>.
     * </p>
     *
     * @param ignoringWhite Whether to ignore ignorable whitespace
     */
    public void setIgnoringElementContentWhitespace(boolean ignoringWhite) {
        this.ignoringWhite = ignoringWhite;
    }

    /**
     * This is called when the parser encounters an external entity 
     * declaration.
     * </p>
     *
     * @param name entity name
     * @param publicId public id
     * @param systemId system id
     * @throws SAXException when things go wrong
     */
    public void externalEntityDecl(String name, 
                                   String publicId, String systemId)
                                   throws SAXException {
        // Store the public and system ids for the name
        externalEntities.put(name, new String[]{publicId, systemId}); 
    }

    // These methods from the DeclHandler interface we can ignore right now
    public void attributeDecl(String eName, String aName, String type,
                              String valueDefault, String value) { }
    public void elementDecl(String name, String model) { }
    public void internalEntityDecl(String name, String value) { }

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

        if (suppress) return;

        if (atRoot) {
            document.addContent(factory.processingInstruction(target, data));
        } else {
            ((Element)stack.peek()).addContent(
                factory.processingInstruction(target, data));
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

        if (suppress) return;

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

        if (suppress) return;

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
        if (suppress) return;

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
            element = factory.element(localName, elementNamespace);

            // Remove this namespace from those in the temp declared list
            if (declaredNamespaces.size() > 0) {
                declaredNamespaces.remove(elementNamespace);
            }

            // It's now in available scope
            availableNamespaces.addFirst(elementNamespace);
        } else {
            element = factory.element(localName);
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
                attribute = factory.attribute(attLocalName, atts.getValue(i),
                                          getNamespace(attPrefix));
            } else {
                attribute = factory.attribute(attLocalName, atts.getValue(i));
            }

            element.setAttribute(attribute);
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

        if (suppress) return;

        String data = new String(ch, start, length);

/**
 * This is commented out because of some problems with
 * the inline DTDs that Xerces seems to have.
if (!inDTD) {
  if (inEntity) {
    ((Entity)stack.peek()).setContent(data);
  } else {
    Element e = (Element)stack.peek();
  e.addContent(data);
}
*/

        if (inCDATA) {
            ((Element)stack.peek()).addContent(factory.cdata(data));
        }
        else {
            Element e = (Element)stack.peek();
            e.addContent(data);
        }
    }

    /**
     * <p>
     * Capture ignorable whitespace as text.  If
     * setIgnoringElementContentWhitespace(true) has been called then this
     * method does nothing.
     * </p>
     *
     * @param ch <code>[]</code> - char array of ignorable whitespace
     * @param start <code>int</code> - starting position within array
     * @param length <code>int</code> - length of whitespace after start
     * @throws SAXException when things go wrong
     */
    public void ignorableWhitespace(char[] ch, int start, int length) 
                                                     throws SAXException {
        if (suppress) return;
        if (ignoringWhite) return;

        ((Element)stack.peek()).addContent(new String(ch, start, length));

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

        if (suppress) return;

        Element element = (Element)stack.pop();
        
        if (stack.empty()) {
            atRoot = true;
        }

        // Remove the namespaces that this element makes available
        List addl = element.getAdditionalNamespaces();
        if (addl.size() > 0) {
            availableNamespaces.removeAll(addl);
        }
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
            factory.docType(name, publicId, systemId));
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

        entityDepth++;

        if (expand || entityDepth > 1) {
            // Short cut out if we're expanding or if we're nested
            return;
        }

        // Ignore DTD references, and translate the standard 5
        if ((!inDTD) &&
            (!name.equals("amp")) &&
            (!name.equals("lt")) &&
            (!name.equals("gt")) &&
            (!name.equals("apos")) &&
            (!name.equals("quot"))) {

            if (!expand) {
                String pub = null;
                String sys = null;
                String[] ids = (String[]) externalEntities.get(name);
                if (ids != null) {
                  pub = ids[0];  // may be null, that's OK
                  sys = ids[1];  // may be null, that's OK
                }
                EntityRef entity = factory.entityRef(name, pub, sys);
                ((Element)stack.peek()).addContent(entity);
                suppress = true;
            }
        }
    }

    public void endEntity(String name) throws SAXException {
        entityDepth--;
        if (entityDepth == 0) {
            // No way are we suppressing if not in an entity,
            // regardless of the "expand" value
            suppress = false;
        }
    }

    /**
     * <p>
     * Report a CDATA section - ignored in SAXBuilder.
     * </p>
     */
    public void startCDATA() throws SAXException {
        if (suppress) return;

        inCDATA = true;
    }

    /**
     * <p>
     * Report a CDATA section - ignored in SAXBuilder.
     * </p>
     */
    public void endCDATA() throws SAXException {
        if (suppress) return;

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

        if (suppress) return;

        String commentText = new String(ch, start, end);
        if ((!inDTD) && (!commentText.equals(""))) {
            if (stack.empty()) {
                document.addContent(
                   factory.comment(commentText));
            } else {
                ((Element)stack.peek()).addContent(
                    factory.comment(commentText));
            }
        }
    }
}
