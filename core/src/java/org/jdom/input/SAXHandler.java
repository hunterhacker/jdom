/*--

 $Id: SAXHandler.java,v 1.31 2002/01/27 11:15:59 jhunter Exp $

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
 * @author Philip Nelson
 * @author Bradley S. Huffman
 * @author phil@triloggroup.com
 * @version $Revision: 1.31 $, $Date: 2002/01/27 11:15:59 $
 */
public class SAXHandler extends DefaultHandler implements LexicalHandler,
                                                          DeclHandler,
                                                          DTDHandler {

    private static final String CVS_ID =
      "@(#) $RCSfile: SAXHandler.java,v $ $Revision: 1.31 $ $Date: 2002/01/27 11:15:59 $ $Name:  $";

    /** Hash table to map SAX attribute type names to JDOM attribute types. */
    private static final Map attrNameToTypeMap = new HashMap(13);

    /** <code>Document</code> object being built */
    private Document document;

    // Note: keeping a "current element" variable to avoid the constant
    // peek() calls to the top of the stack has shown to cause no noticeable
    // performance improvement.

    /** Element stack */
    protected Stack stack;

    /** Indicator of where in the document we are */
    protected boolean atRoot;

    /** Indicator of whether we are in the DocType */
    protected boolean inDTD = false;

    /** Indicator of whether we are in the internal subset */
    protected boolean inInternalSubset = false;

    /** Indicator of whether we previously where in a CDATA */
    protected boolean previousCDATA = false;

    /** Indicator of whether we are in a CDATA */
    protected boolean inCDATA = false;

    /** Indicator of whether we should expand entities */
    private boolean expand = true;

    /** Indicator of whether we are actively suppressing (non-expanding) a
        current entity */
    protected boolean suppress = false;

    /** How many nested entities we're currently within */
    private int entityDepth = 0;

    /** Temporary holder for namespaces that have been declared with
      * startPrefixMapping, but are not yet available on the element */
    protected LinkedList declaredNamespaces;

    /** The namespaces in scope and actually attached to an element */
    protected LinkedList availableNamespaces;

    /** Temporary holder for the internal subset */
    private StringBuffer buffer = new StringBuffer();

    /** Temporary holder for Text and CDATA */
    private StringBuffer textBuffer = new StringBuffer();

    /** The external entities defined in this document */
    private Map externalEntities;

    /** The JDOMFactory used for JDOM object creation */
    private JDOMFactory factory;

    /** Whether to ignore ignorable whitespace */
    private boolean ignoringWhite = false;

    /** The SAX Locator object provided by the parser */
    private Locator locator;

    /**
     * <p>
     * Class initializer: Populate a table to translate SAX attribute
     * type names into JDOM attribute type value (integer).
     * </p><p>
     * <b>Note that all the mappings defined below are compliant with
     * the SAX 2.0 specification exception for "ENUMERATION" with is
     * specific to Crinsom 1.1.X and Xerces 2.0.0-betaX which report
     * attributes of enumerated types with a type "ENUMERATION"
     * instead of the expected "NMTOKEN".
     * </p><p>
     * Note also that Xerces 1.4.X is not SAX 2.0 compliant either
     * but handling its case requires
     * {@link #getAttributeType specific code}.
     * </p>
     */
    static {
        attrNameToTypeMap.put("CDATA",
                              new Integer(Attribute.CDATA_ATTRIBUTE));
        attrNameToTypeMap.put("ID",
                              new Integer(Attribute.ID_ATTRIBUTE));
        attrNameToTypeMap.put("IDREF",
                              new Integer(Attribute.IDREF_ATTRIBUTE));
        attrNameToTypeMap.put("IDREFS",
                              new Integer(Attribute.IDREFS_ATTRIBUTE));
        attrNameToTypeMap.put("ENTITY",
                              new Integer(Attribute.ENTITY_ATTRIBUTE));
        attrNameToTypeMap.put("ENTITIES",
                              new Integer(Attribute.ENTITIES_ATTRIBUTE));
        attrNameToTypeMap.put("NMTOKEN",
                              new Integer(Attribute.NMTOKEN_ATTRIBUTE));
        attrNameToTypeMap.put("NMTOKENS",
                              new Integer(Attribute.NMTOKENS_ATTRIBUTE));
        attrNameToTypeMap.put("NOTATION",
                              new Integer(Attribute.NOTATION_ATTRIBUTE));
        attrNameToTypeMap.put("ENUMERATION",
                              new Integer(Attribute.ENUMERATED_ATTRIBUTE));
    }

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
     * Returns the factory used for constructing objects.
     * </p>
     *
     * @return <code>JDOMFactory</code> - the factory used for
     * constructing objects.
     *
     * @see #SAXHandler(org.jdom.input.JDOMFactory)
     */
    public JDOMFactory getFactory() {
        return factory;
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
     * Returns whether or not entities will be expanded during the
     * build.
     * </p>
     *
     * @return <code>boolean</code> - whether entity expansion
     * will occur during build.
     *
     * @see #setExpandEntities
     */
    public boolean getExpandEntities() {
        return expand;
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
     * <p>
     * Returns whether or not the parser will elminate whitespace in
     * element content (sometimes known as "ignorable whitespace") when
     * building the document.
     * </p>
     *
     * @return <code>boolean</code> - whether ignorable whitespace will
     * be ignored during build.
     *
     * @see #setIgnoringElementContentWhitespace
     */
    public boolean getIgnoringElementContentWhitespace() {
        return ignoringWhite;
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

        if (!inInternalSubset) return;

        buffer.append("  <!ENTITY ")
              .append(name);
        if (publicId != null) {
            buffer.append(" PUBLIC \"")
                  .append(publicId)
                  .append("\" ");
        }
        if (systemId != null) {
            buffer.append(" SYSTEM \"")
                  .append(systemId)
                  .append("\" ");
        }
        buffer.append(">\n");
    }
	
    /**
     * <P>
     *  This handles an attribute declaration in the internal subset
     * </P>
     *
     * @param eName <code>String</code> element name of attribute
     * @param aName <code>String</code> attribute name
     * @param type <code>String</code> attribute type
     * @param valueDefault <code>String</code> default value of attribute
     * @param value <code>String</code> value of attribute
     */
    public void attributeDecl(String eName, String aName, String type,
                              String valueDefault, String value)
        throws SAXException {

        if (!inInternalSubset) return;

        buffer.append("  <!ATTLIST ")
              .append(eName)
              .append(" ")
              .append(aName)
              .append(" ")
              .append(type)
              .append(" ");
        if (valueDefault != null) {
              buffer.append(valueDefault);
        } else {
            buffer.append("\"")
                  .append(value)
                  .append("\"");
        }
        if ((valueDefault != null) && (valueDefault.equals("#FIXED"))) {
            buffer.append(" \"")
                  .append(value)
                  .append("\"");
        }
        buffer.append(">\n");	
    }

    /**
     * <P>
     *  Handle an element declaration in a DTD
     * </P>
     *
     * @param name <code>String</code> name of element
     * @param model <code>String</code> model of the element in DTD syntax
     */
    public void elementDecl(String name, String model) throws SAXException {
        buffer.append("  <!ELEMENT ")
              .append(name)
              .append(" ")
              .append(model)
              .append(">\n");
    }

    /**
     * <P>
     *  Handle an internal entity declaration in a DTD.
     * </P>
     *
     * @param name <code>String</code> name of entity
     * @param value <code>String</code> value of the entity
     */
    public void internalEntityDecl(String name, String value)
        throws SAXException { 

        //skip entities that come from the dtd
        if (!inInternalSubset) return;

        buffer.append("  <!ENTITY ");
        if (name.startsWith("%")) {
           buffer.append("% ").append(name.substring(1));
        } else {
           buffer.append(name);
        }
        buffer.append(" \"")
              .append(value)
              .append("\">\n");
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

        if (suppress) return;

        flushCharacters();

        if (atRoot) {
            document.addContent(factory.processingInstruction(target, data));
        } else {
            getCurrentElement().addContent(
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
/**
 * I've commented out these lines to ensure that element's that have a namespace
 *   make those namespaces available to their attributes, which this seems to
 *   break. However, I'm not 100% sure that this doesn't cause some other
 *   problems. My gut feeling is "no", but I'm not sure, so I'm just commenting
 *   it out. We'll remove for good in the next drop I think.
 * - Brett, 07/30/2001
            if (declaredNamespaces.size() > 0) {
                declaredNamespaces.remove(elementNamespace);
            }
 */
        } else {
            element = factory.element(localName);
        }

        // Take leftover declared namespaces and add them to this element's
        // map of namespaces
        if (declaredNamespaces.size() > 0) {
            transferNamespaces(element);
        }

        // Handle attributes
        for (int i=0, len=atts.getLength(); i<len; i++) {
            Attribute attribute = null;

            String attLocalName = atts.getLocalName(i);
            String attQName = atts.getQName(i);
            int attType = getAttributeType(atts.getType(i));

            // Bypass any xmlns attributes which might appear, as we got
            // them already in startPrefixMapping().
            // This is sometimes necessary when SAXHandler is used with
            // another source than SAXBuilder, as with JDOMResult.
            if (attQName.startsWith("xmlns:") || attQName.equals("xmlns")) {
                continue;
            }

            // XXX This is probably an unsafe != unless we set up interning
            if (attLocalName != attQName) {
                String attPrefix = attQName.substring(0, attQName.indexOf(":"));
                attribute = factory.attribute(attLocalName, atts.getValue(i),
                                              attType, getNamespace(attPrefix));
            } else {
                attribute = factory.attribute(attLocalName, atts.getValue(i),
                                              attType);
            }
            element.setAttribute(attribute);
        }

        flushCharacters();

        if (atRoot) {
            document.setRootElement(element);
            stack.push(element);
            atRoot = false;
        } else {
            getCurrentElement().addContent(element);
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
            availableNamespaces.addFirst(ns);
            element.addNamespaceDeclaration(ns);
        }
        declaredNamespaces.clear();
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
     */
    public void characters(char[] ch, int start, int length)
                    throws SAXException {

        if (suppress || (length == 0))
            return;

        if (previousCDATA != inCDATA) {
            flushCharacters();
        }

        textBuffer.append( ch, start, length);
    }

    /**
     * <p>
     * This will flush any characters from SAX character calls we've
     * been buffering.
     * </p>
     *
     * @throws SAXException when things go wrong
     */
    protected void flushCharacters() throws SAXException {
        previousCDATA = inCDATA;

        if (textBuffer.length() == 0)
            return;

        String data = textBuffer.toString();
        textBuffer.setLength( 0);

/**
 * This is commented out because of some problems with
 * the inline DTDs that Xerces seems to have.
if (!inDTD) {
  if (inEntity) {
    getCurrentElement().setContent(factory.text(data));
  } else {
    getCurrentElement().addContent(factory.text(data));
}
*/

        if (inCDATA) {
            getCurrentElement().addContent(factory.cdata(data));
        }
        else {
            getCurrentElement().addContent(factory.text(data));
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
        if (length == 0) return;

        textBuffer.append( ch, start, length);
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

        flushCharacters();

        try {
            Element element = (Element)stack.pop();

            // Remove the namespaces that this element makes available
            List addl = element.getAdditionalNamespaces();
            if (addl.size() > 0) {
                availableNamespaces.removeAll(addl);
            }
        }
        catch (EmptyStackException ex1) {
            throw new SAXException(
                "Ill-formed XML document (missing opening tag for " +
                localName + ")");
        }
        if (stack.empty()) {
            atRoot = true;
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

        flushCharacters(); //XXX Is this needed here?

        document.setDocType(
            factory.docType(name, publicId, systemId));
        inDTD = true;
        inInternalSubset = true;
    }

    /**
     * <p>
     * This signifies that the reading of the DTD is complete.
     * </p>
     */
    public void endDTD() throws SAXException {

        document.getDocType().setInternalSubset(buffer.toString());
        inDTD = false;
        inInternalSubset = false;
    }

    public void startEntity(String name) throws SAXException {
        entityDepth++;

        if (expand || entityDepth > 1) {
            // Short cut out if we're expanding or if we're nested
            return;
        }

        if (inDTD) {
            inInternalSubset = false;
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
                /**
                 * if stack is empty, this entity belongs to an attribute
                 * in these cases, it is an error on the part of the parser
                 * to call startEntity but this will help in some cases.
                 * See org/xml/sax/ext/LexicalHandler.html#startEntity(java.lang.String)
                 * for more information
                 */
                if (!(atRoot || stack.isEmpty())) {
                    flushCharacters();
                    EntityRef entity = factory.entityRef(name, pub, sys);

                    // no way to tell if the entity was from an attribute or element so just assume element
                    getCurrentElement().addContent(entity);
                }
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
        if (inDTD)
            inInternalSubset = true;
    }

    /**
     * <p>
     * Report a CDATA section - ignored in SAXBuilder.
     * </p>
     */
    public void startCDATA() throws SAXException {
        if (suppress) return;

        previousCDATA = inCDATA;
        inCDATA = true;
    }

    /**
     * <p>
     * Report a CDATA section - ignored in SAXBuilder.
     * </p>
     */
    public void endCDATA() throws SAXException {
        if (suppress) return;

        previousCDATA = inCDATA;
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
     * @param length <code>int</code> length of data.
     */
    public void comment(char[] ch, int start, int length)
        throws SAXException {

        if (suppress) return;

        flushCharacters();

        String commentText = new String(ch, start, length);
        if (inDTD && inInternalSubset && (expand == false)) {
            buffer.append("  <!--")
                  .append(commentText)
                  .append("-->\n");
            return;
        }
        if ((!inDTD) && (!commentText.equals(""))) {
            if (stack.empty()) {
                document.addContent(
                   factory.comment(commentText));
            } else {
                getCurrentElement().addContent(
                    factory.comment(commentText));
            }
        }
    }

    /**
     * <p>
     *  Handle the declaration of a Notation in a DTD
     * </P>
     *
     * @param name name of the notation
     * @param publicID the public ID of the notation
     * @param systemID the system ID of the notation
     */
    public void notationDecl(String name, String publicID, String systemID)
        throws SAXException {

        if (!inInternalSubset) return;

        buffer.append("  <!NOTATION ")
              .append(name)
              .append(" \"")
              .append(systemID)
              .append("\">\n");	
    }

    /**
     * <P>
     *  Handler for unparsed entity declarations in the DTD
     * </P>
     *
     * @param name <code>String</code> of the unparsed entity decl
     * @param publicId <code>String</code> of the unparsed entity decl
     * @param systemId <code>String</code> of the unparsed entity decl
     * @param notationName <code>String</code> of the unparsed entity decl
     */
    public void unparsedEntityDecl(String name, String publicId,
                                   String systemId, String notationName)
        throws SAXException {

        if (!inInternalSubset) return;

        buffer.append("  <!ENTITY ")
              .append(name);
        if (publicId != null) {
            buffer.append(" PUBLIC \"")
                  .append(publicId)
                  .append("\" ");
        }
        if (systemId != null) {
            buffer.append(" SYSTEM \"")
                  .append(systemId)
                  .append("\" ");
        }

        buffer.append(" NDATA ")
              .append(notationName);
        buffer.append(">\n");
    }

    /**
     * <p>
     * Returns the being-parsed element.
     * </p>
     *
     * @return <code>Element</code> - element at the top of the stack.
     */
    protected Element getCurrentElement() throws SAXException {
        try {
            return (Element)(stack.peek());
        }
        catch (EmptyStackException ex1) {
            throw new SAXException(
                "Ill-formed XML document (multiple root elements detected)");
        }
    }

    /**
     * <p>
     * Returns the the JDOM Attribute type value from the SAX 2.0
     * attribute type string provided by the parser.
     * </p>
     *
     * @param typeName <code>String</code> the SAX 2.0 attribute
     * type string.
     *
     * @return <code>int</code> the JDOM attribute type.
     *
     * @see Attribute#setAttributeType
     * @see Attributes#getType
     */
    private int getAttributeType(String typeName) {
        Integer type = (Integer)(attrNameToTypeMap.get(typeName));
        if (type == null) {
            if (typeName.charAt(0) == '(') {
                // Xerces 1.4.X reports attributes of enumerated type with
                // a type string equals to the enumeration definition, i.e.
                // starting with an parenthesis.
                return Attribute.ENUMERATED_ATTRIBUTE;
            }
            else {
                return Attribute.UNDECLARED_ATTRIBUTE;
            }
        } else {
            return type.intValue();
        }
    }

    /**
     * <p>
     * Receives an object for locating the origin of SAX document
     * events.  This method is invoked by the SAX parser.
     * </p><p>
     * {@link JDOMFactory} implementations can use the
     * {@link #getDocumentLocator} method to get access to the
     * {@link Locator} during parse.
     * </p>
     *
     * @param locator <code>Locator</code> an object that can return
     * the location of any SAX document event.
     */
    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
    }

    /**
     * <p>
     * Provides access to the {@link Locator} object provided by the
     * SAX parser.
     * </p>
     *
     * @return <code>Locator</code> an object that can return
     * the location of any SAX document event.
     */
    public Locator getDocumentLocator() {
        return locator;
    }
}