/*--

 $Id: SAXHandler.java,v 1.73 2007/11/10 05:29:00 jhunter Exp $

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Attribute;
import org.jdom.DefaultJDOMFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.EntityRef;
import org.jdom.JDOMFactory;
import org.jdom.Namespace;
import org.jdom.Parent;
import org.xml.sax.Attributes;
import org.xml.sax.DTDHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A support class for {@link SAXBuilder}.
 *
 * @version $Revision: 1.73 $, $Date: 2007/11/10 05:29:00 $
 * @author  Brett McLaughlin
 * @author  Jason Hunter
 * @author  Philip Nelson
 * @author  Bradley S. Huffman
 * @author  phil@triloggroup.com
 */
public class SAXHandler extends DefaultHandler implements LexicalHandler,
                                                          DeclHandler,
                                                          DTDHandler {

    private static final String CVS_ID =
      "@(#) $RCSfile: SAXHandler.java,v $ $Revision: 1.73 $ $Date: 2007/11/10 05:29:00 $ $Name:  $";

    /** Hash table to map SAX attribute type names to JDOM attribute types. */
    private static final Map attrNameToTypeMap = new HashMap(13);

    /** <code>Document</code> object being built */
    private Document document;

    /** <code>Element</code> object being built */
    private Element currentElement;

    /** Indicator of where in the document we are */
    private boolean atRoot;

    /** Indicator of whether we are in the DocType. Note that the DTD consists
     * of both the internal subset (inside the <!DOCTYPE> tag) and the
      * external subset (in a separate .dtd file). */
    private boolean inDTD = false;

    /** Indicator of whether we are in the internal subset */
    private boolean inInternalSubset = false;

    /** Indicator of whether we previously were in a CDATA */
    private boolean previousCDATA = false;

    /** Indicator of whether we are in a CDATA */
    private boolean inCDATA = false;

    /** Indicator of whether we should expand entities */
    private boolean expand = true;

    /** Indicator of whether we are actively suppressing (non-expanding) a
        current entity */
    private boolean suppress = false;

    /** How many nested entities we're currently within */
    private int entityDepth = 0;  // XXX may not be necessary anymore?

    /** Temporary holder for namespaces that have been declared with
      * startPrefixMapping, but are not yet available on the element */
    private List declaredNamespaces;

    /** Temporary holder for the internal subset */
    private StringBuffer internalSubset = new StringBuffer();

    /** Temporary holder for Text and CDATA */
    private TextBuffer textBuffer = new TextBuffer();

    /** The external entities defined in this document */
    private Map externalEntities;

    /** The JDOMFactory used for JDOM object creation */
    private JDOMFactory factory;

    /** Whether to ignore ignorable whitespace */
    private boolean ignoringWhite = false;

    /** Whether to ignore text containing all whitespace */
    private boolean ignoringBoundaryWhite = false;

    /** The SAX Locator object provided by the parser */
    private Locator locator;

    /**
     * Class initializer: Populate a table to translate SAX attribute
     * type names into JDOM attribute type value (integer).
     * <p>
     * <b>Note that all the mappings defined below are compliant with
     * the SAX 2.0 specification exception for "ENUMERATION" with is
     * specific to Crimson 1.1.X and Xerces 2.0.0-betaX which report
     * attributes of enumerated types with a type "ENUMERATION"
     * instead of the expected "NMTOKEN".
     * </p>
     * <p>
     * Note also that Xerces 1.4.X is not SAX 2.0 compliant either
     * but handling its case requires
     * {@link #getAttributeType specific code}.
     * </p>
     */
    static {
        attrNameToTypeMap.put("CDATA",
                              new Integer(Attribute.CDATA_TYPE));
        attrNameToTypeMap.put("ID",
                              new Integer(Attribute.ID_TYPE));
        attrNameToTypeMap.put("IDREF",
                              new Integer(Attribute.IDREF_TYPE));
        attrNameToTypeMap.put("IDREFS",
                              new Integer(Attribute.IDREFS_TYPE));
        attrNameToTypeMap.put("ENTITY",
                              new Integer(Attribute.ENTITY_TYPE));
        attrNameToTypeMap.put("ENTITIES",
                              new Integer(Attribute.ENTITIES_TYPE));
        attrNameToTypeMap.put("NMTOKEN",
                              new Integer(Attribute.NMTOKEN_TYPE));
        attrNameToTypeMap.put("NMTOKENS",
                              new Integer(Attribute.NMTOKENS_TYPE));
        attrNameToTypeMap.put("NOTATION",
                              new Integer(Attribute.NOTATION_TYPE));
        attrNameToTypeMap.put("ENUMERATION",
                              new Integer(Attribute.ENUMERATED_TYPE));
    }

    /**
     * This will create a new <code>SAXHandler</code> that listens to SAX
     * events and creates a JDOM Document.  The objects will be constructed
     * using the default factory.
     */
    public SAXHandler() {
        this(null);
    }

    /**
     * This will create a new <code>SAXHandler</code> that listens to SAX
     * events and creates a JDOM Document.  The objects will be constructed
     * using the provided factory.
     *
     * @param factory <code>JDOMFactory</code> to be used for constructing
     * objects
     */
    public SAXHandler(JDOMFactory factory) {
        if (factory != null) {
            this.factory = factory;
        } else {
            this.factory = new DefaultJDOMFactory();
        }

        atRoot = true;
        declaredNamespaces = new ArrayList();
        externalEntities = new HashMap();

        document = this.factory.document(null);
    }

    /**
     * Pushes an element onto the tree under construction.  Allows subclasses
     * to put content under a dummy root element which is useful for building
     * content that would otherwise be a non-well formed document.
     *
     * @param element root element under which content will be built
     */
    protected void pushElement(Element element) {
        if (atRoot) {
            document.setRootElement(element);  // XXX should we use a factory call?
            atRoot = false;
        }
        else {
            factory.addContent(currentElement, element);
        }
        currentElement = element;
    }

    /**
     * Returns the document.  Should be called after parsing is complete.
     *
     * @return <code>Document</code> - Document that was built
     */
    public Document getDocument() {
        return document;
    }

    /**
     * Returns the factory used for constructing objects.
     *
     * @return <code>JDOMFactory</code> - the factory used for
     * constructing objects.
     *
     * @see #SAXHandler(org.jdom.JDOMFactory)
     */
    public JDOMFactory getFactory() {
        return factory;
    }

    /**
     * This sets whether or not to expand entities during the build.
     * A true means to expand entities as normal content.  A false means to
     * leave entities unexpanded as <code>EntityRef</code> objects.  The
     * default is true.
     *
     * @param expand <code>boolean</code> indicating whether entity expansion
     * should occur.
     */
    public void setExpandEntities(boolean expand) {
        this.expand = expand;
    }

    /**
     * Returns whether or not entities will be expanded during the
     * build.
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
     * Specifies whether or not the parser should elminate whitespace in
     * element content (sometimes known as "ignorable whitespace") when
     * building the document.  Only whitespace which is contained within
     * element content that has an element only content model will be
     * eliminated (see XML Rec 3.2.1).  For this setting to take effect
     * requires that validation be turned on.  The default value of this
     * setting is <code>false</code>.
     *
     * @param ignoringWhite Whether to ignore ignorable whitespace
     */
    public void setIgnoringElementContentWhitespace(boolean ignoringWhite) {
        this.ignoringWhite = ignoringWhite;
    }

    /**
     * Specifies whether or not the parser should elminate text() nodes
     * containing only whitespace when building the document.  See
     * {@link SAXBuilder#setIgnoringBoundaryWhitespace(boolean)}.
     *
     * @param ignoringBoundaryWhite Whether to ignore only whitespace content
     */
    public void setIgnoringBoundaryWhitespace(boolean ignoringBoundaryWhite) {
        this.ignoringBoundaryWhite = ignoringBoundaryWhite;
    }

    /**
     * Returns whether or not the parser will elminate element content
     * containing only whitespace.
     *
     * @return <code>boolean</code> - whether only whitespace content will
     * be ignored during build.
     *
     * @see #setIgnoringBoundaryWhitespace
     */
    public boolean getIgnoringBoundaryWhitespace() {
        return ignoringBoundaryWhite;
    }

    /**
     * Returns whether or not the parser will elminate whitespace in
     * element content (sometimes known as "ignorable whitespace") when
     * building the document.
     *
     * @return <code>boolean</code> - whether ignorable whitespace will
     * be ignored during build.
     *
     * @see #setIgnoringElementContentWhitespace
     */
    public boolean getIgnoringElementContentWhitespace() {
        return ignoringWhite;
    }

    public void startDocument() {
        if (locator != null) {
            document.setBaseURI(locator.getSystemId());
        }
    }

    /**
     * This is called when the parser encounters an external entity
     * declaration.
     *
     * @param name entity name
     * @param publicID public id
     * @param systemID system id
     * @throws SAXException when things go wrong
     */
    public void externalEntityDecl(String name,
                                   String publicID, String systemID)
                                   throws SAXException {
        // Store the public and system ids for the name
        externalEntities.put(name, new String[]{publicID, systemID});

        if (!inInternalSubset) return;

        internalSubset.append("  <!ENTITY ")
              .append(name);
        appendExternalId(publicID, systemID);
        internalSubset.append(">\n");
    }

    /**
     * This handles an attribute declaration in the internal subset.
     *
     * @param eName <code>String</code> element name of attribute
     * @param aName <code>String</code> attribute name
     * @param type <code>String</code> attribute type
     * @param valueDefault <code>String</code> default value of attribute
     * @param value <code>String</code> value of attribute
     * @throws SAXException
     */
    public void attributeDecl(String eName, String aName, String type,
                              String valueDefault, String value)
        throws SAXException {

        if (!inInternalSubset) return;

        internalSubset.append("  <!ATTLIST ")
              .append(eName)
              .append(' ')
              .append(aName)
              .append(' ')
              .append(type)
              .append(' ');
        if (valueDefault != null) {
              internalSubset.append(valueDefault);
        } else {
            internalSubset.append('\"')
                  .append(value)
                  .append('\"');
        }
        if ((valueDefault != null) && (valueDefault.equals("#FIXED"))) {
            internalSubset.append(" \"")
                  .append(value)
                  .append('\"');
        }
        internalSubset.append(">\n");
    }

    /**
     * Handle an element declaration in a DTD.
     *
     * @param name <code>String</code> name of element
     * @param model <code>String</code> model of the element in DTD syntax
     * @throws SAXException
     */
    public void elementDecl(String name, String model) throws SAXException {
        // Skip elements that come from the external subset
        if (!inInternalSubset) return;

        internalSubset.append("  <!ELEMENT ")
              .append(name)
              .append(' ')
              .append(model)
              .append(">\n");
    }

    /**
     * Handle an internal entity declaration in a DTD.
     *
     * @param name <code>String</code> name of entity
     * @param value <code>String</code> value of the entity
     * @throws SAXException
     */
    public void internalEntityDecl(String name, String value)
        throws SAXException {

        // Skip entities that come from the external subset
        if (!inInternalSubset) return;

        internalSubset.append("  <!ENTITY ");
        if (name.startsWith("%")) {
           internalSubset.append("% ").append(name.substring(1));
        } else {
           internalSubset.append(name);
        }
        internalSubset.append(" \"")
              .append(value)
              .append("\">\n");
    }

    /**
     * This will indicate that a processing instruction has been encountered.
     * (The XML declaration is not a processing instruction and will not
     * be reported.)
     *
     * @param target <code>String</code> target of PI
     * @param data <code>String</code> containing all data sent to the PI.
     *             This typically looks like one or more attribute value
     *             pairs.
     * @throws SAXException when things go wrong
     */
    public void processingInstruction(String target, String data)
        throws SAXException {

        if (suppress) return;

        flushCharacters();

        if (atRoot) {
            factory.addContent(document, factory.processingInstruction(target, data));
        } else {
            factory.addContent(getCurrentElement(),
                factory.processingInstruction(target, data));
        }
    }

    /**
     * This indicates that an unresolvable entity reference has been
     * encountered, normally because the external DTD subset has not been
     * read.
     *
     * @param name <code>String</code> name of entity
     * @throws SAXException when things go wrong
     */
    public void skippedEntity(String name)
        throws SAXException {

        // We don't handle parameter entity references.
        if (name.startsWith("%")) return;

        flushCharacters();

        factory.addContent(getCurrentElement(), factory.entityRef(name));
    }

    /**
     * This will add the prefix mapping to the JDOM
     * <code>Document</code> object.
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
     * This reports the occurrence of an actual element.  It will include
     * the element's attributes, with the exception of XML vocabulary
     * specific attributes, such as
     * <code>xmlns:[namespace prefix]</code> and
     * <code>xsi:schemaLocation</code>.
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
        String prefix = "";
        
        // If QName is set, then set prefix and local name as necessary
        if (!"".equals(qName)) {
        	int colon = qName.indexOf(':');

        	if (colon > 0) {
        		prefix = qName.substring(0, colon);
        	}

        	// If local name is not set, try to get it from the QName
        	if ((localName == null) || (localName.equals(""))) {
        		localName = qName.substring(colon + 1);
        	}
        }
        // At this point either prefix and localName are set correctly or
        // there is an error in the parser.

        Namespace namespace = Namespace.getNamespace(prefix, namespaceURI);
        Element element = factory.element(localName, namespace);

		// Take leftover declared namespaces and add them to this element's
		// map of namespaces
		if (declaredNamespaces.size() > 0) {
			transferNamespaces(element);
		}

		flushCharacters();

		if (atRoot) {
			document.setRootElement(element);  // XXX should we use a factory call?
			atRoot = false;
		} else {
			factory.addContent(getCurrentElement(), element);
		}
		currentElement = element;

		// Handle attributes
		for (int i=0, len=atts.getLength(); i<len; i++) {

			String attPrefix = "";
			String attLocalName = atts.getLocalName(i);
			String attQName = atts.getQName(i);

			// If attribute QName is set, then set attribute prefix and
			// attribute local name as necessary
			if (!attQName.equals("")) {
				// Bypass any xmlns attributes which might appear, as we got
				// them already in startPrefixMapping(). This is sometimes
				// necessary when SAXHandler is used with another source than
				// SAXBuilder, as with JDOMResult.
				if (attQName.startsWith("xmlns:") || attQName.equals("xmlns")) {
					continue;
				}

				int attColon = attQName.indexOf(':');

				if (attColon > 0) {
					attPrefix = attQName.substring(0, attColon);
				}

				// If localName is not set, try to get it from the QName
				if ("".equals(attLocalName)) {
					attLocalName = attQName.substring(attColon + 1);
				}
			}
			// At this point either attPrefix and attLocalName are set
			// correctly or there is an error in the parser.

			int attType = getAttributeType(atts.getType(i));
			String attValue = atts.getValue(i);
			String attURI = atts.getURI(i);
			
			if ("xmlns".equals(attLocalName) || 
					"xmlns".equals(attPrefix) || 
					"http://www.w3.org/2000/xmlns/".equals(attURI)) {
				// use the actual Namespace to check too, because, in theory, a
				// namespace-aware parser does not need to set the qName unless
				// the namespace-prefixes feature is set as well.
				continue;
			}

			// just one thing to sort out....
			// the prefix for the namespace.
			if (!"".equals(attURI) && "".equals(attPrefix)) {
                // the localname and qName are the same, but there is a
                // Namspace URI. We need to figure out the namespace prefix.
                // this is an unusual condition. Currently the only known trigger
                // is when there is a fixed/defaulted attribute from a validating
                // XMLSchema, and the attribute is in a different namespace
                // than the rest of the document, this happens whenever there
                // is an attribute definition that has form="qualified".
                //  <xs:attribute name="attname" form="qualified" ... />
                // or the schema sets attributeFormDefault="qualified"
                Element p = element;
                // We need to ensure that a particular prefix has not been
                // overridden at a lower level than what we are expecting.
                // track all prefixes to ensure they are not changed lower
                // down.
                HashSet overrides = new HashSet();
                uploop: do {
                    // Search up the Element tree looking for a prefixed namespace
                    // matching our attURI
                    if (p.getNamespace().getURI().equals(attURI)
                            && !overrides.contains(p.getNamespacePrefix())
                            && !"".equals(element.getNamespace().getPrefix())) {
                        // we need a prefix. It's impossible to have a namespaced
                        // attribute if there is no prefix for that attribute.
                        attPrefix = p.getNamespacePrefix();
                        break uploop;
                    }
                    overrides.add(p.getNamespacePrefix());
                    for (Iterator it = p.getAdditionalNamespaces().iterator();
                            it.hasNext(); ) {
                        Namespace ns = (Namespace)it.next();
                        if (!overrides.contains(ns.getPrefix())
                                 && attURI.equals(ns.getURI())) {
                            attPrefix = ns.getPrefix();
                            break uploop;
                        }
                        overrides.add(ns.getPrefix());
                    }
                    for (Iterator it = p.getAttributes().iterator();
                            it.hasNext(); ) {
                        Namespace ns = ((Attribute)it.next()).getNamespace();
                        if (!overrides.contains(ns.getPrefix())
                                 && attURI.equals(ns.getURI())) {
                            attPrefix = ns.getPrefix();
                            break uploop;
                        }
                        overrides.add(ns.getPrefix());
                    }
                    p = p.getParentElement();
                } while (p != null);
                if ("".equals(attPrefix)) {
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
                    attPrefix = pfx;
                }
			}
			Namespace attNs = Namespace.getNamespace(attPrefix, attURI);
				
            Attribute attribute = factory.attribute(attLocalName, attValue,
				                                                    attType, attNs);
			factory.setAttribute(element, attribute);
		}
    }

    /**
     * This will take the supplied <code>{@link Element}</code> and
     * transfer its namespaces to the global namespace storage.
     *
     * @param element <code>Element</code> to read namespaces from.
     */
    private void transferNamespaces(Element element) {
        Iterator i = declaredNamespaces.iterator();
        while (i.hasNext()) {
            Namespace ns = (Namespace)i.next();
            if (ns != element.getNamespace()) {
                element.addNamespaceDeclaration(ns);
            }
        }
        declaredNamespaces.clear();
    }

    /**
     * This will report character data (within an element).
     *
     * @param ch <code>char[]</code> character array with character data
     * @param start <code>int</code> index in array where data starts.
     * @param length <code>int</code> length of data.
     * @throws SAXException
     */
    public void characters(char[] ch, int start, int length)
                    throws SAXException {

        if (suppress || (length == 0))
            return;

        if (previousCDATA != inCDATA) {
            flushCharacters();
        }

        textBuffer.append(ch, start, length);
    }

    /**
     * Capture ignorable whitespace as text.  If
     * setIgnoringElementContentWhitespace(true) has been called then this
     * method does nothing.
     *
     * @param ch <code>[]</code> - char array of ignorable whitespace
     * @param start <code>int</code> - starting position within array
     * @param length <code>int</code> - length of whitespace after start
     * @throws SAXException when things go wrong
     */
    public void ignorableWhitespace(char[] ch, int start, int length)
                                                     throws SAXException {
        if (!ignoringWhite) {
            characters(ch, start, length);
        }
    }

    /**
     * This will flush any characters from SAX character calls we've
     * been buffering.
     *
     * @throws SAXException when things go wrong
     */
    protected void flushCharacters() throws SAXException {
        if (ignoringBoundaryWhite) {
            if (!textBuffer.isAllWhitespace()) {
                flushCharacters(textBuffer.toString());
            }
        }
        else {
            flushCharacters(textBuffer.toString());
        }
        textBuffer.clear();
    }

    /**
     * Flush the given string into the document.  This is a protected method
     * so subclassers can control text handling without knowledge of the
     * internals of this class.
     *
     * @param data string to flush
     */
    protected void flushCharacters(String data) throws SAXException {
        if (data.length() == 0) {
            previousCDATA = inCDATA;
            return;
        }

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

        if (previousCDATA) {
            factory.addContent(getCurrentElement(), factory.cdata(data));
        }
        else {
            factory.addContent(getCurrentElement(), factory.text(data));
        }

        previousCDATA = inCDATA;
    }

    /**
     * Indicates the end of an element
     * (<code>&lt;/[element name]&gt;</code>) is reached.  Note that
     * the parser does not distinguish between empty
     * elements and non-empty elements, so this will occur uniformly.
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

        if (!atRoot) {
            Parent p = currentElement.getParent();
            if (p instanceof Document) {
               atRoot = true;
            }
            else {
                currentElement = (Element) p;
            }
        }
        else {
            throw new SAXException(
                "Ill-formed XML document (missing opening tag for " +
                localName + ")");
        }
    }

    /**
     * This will signify that a DTD is being parsed, and can be
     * used to ensure that comments and other lexical structures
     * in the DTD are not added to the JDOM <code>Document</code>
     * object.
     *
     * @param name <code>String</code> name of element listed in DTD
     * @param publicID <code>String</code> public ID of DTD
     * @param systemID <code>String</code> system ID of DTD
     */
    public void startDTD(String name, String publicID, String systemID)
        throws SAXException {

        flushCharacters(); // Is this needed here?

        factory.addContent(document, factory.docType(name, publicID, systemID));
        inDTD = true;
        inInternalSubset = true;
    }

    /**
     * This signifies that the reading of the DTD is complete.
     *
     * @throws SAXException
     */
    public void endDTD() throws SAXException {

        document.getDocType().setInternalSubset(internalSubset.toString());
        inDTD = false;
        inInternalSubset = false;
    }

    public void startEntity(String name) throws SAXException {
        entityDepth++;

        if (expand || entityDepth > 1) {
            // Short cut out if we're expanding or if we're nested
            return;
        }

        // A "[dtd]" entity indicates the beginning of the external subset
        if (name.equals("[dtd]")) {
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
                 * if no current element, this entity belongs to an attribute
                 * in these cases, it is an error on the part of the parser
                 * to call startEntity but this will help in some cases.
                 * See org/xml/sax/ext/LexicalHandler.html#startEntity(java.lang.String)
                 * for more information
                 */
                if (!atRoot) {
                    flushCharacters();
                    EntityRef entity = factory.entityRef(name, pub, sys);

                    // no way to tell if the entity was from an attribute or element so just assume element
                    factory.addContent(getCurrentElement(), entity);
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
        if (name.equals("[dtd]")) {
            inInternalSubset = true;
        }
    }

    /**
     * Report a CDATA section
     *
     * @throws SAXException
     */
    public void startCDATA() throws SAXException {
        if (suppress) return;

        inCDATA = true;
    }

    /**
     * Report a CDATA section
     */
    public void endCDATA() throws SAXException {
        if (suppress) return;

        previousCDATA = true;
        inCDATA = false;
    }

    /**
     * This reports that a comments is parsed.  If not in the
     * DTD, this comment is added to the current JDOM
     * <code>Element</code>, or the <code>Document</code> itself
     * if at that level.
     *
     * @param ch <code>ch[]</code> array of comment characters.
     * @param start <code>int</code> index to start reading from.
     * @param length <code>int</code> length of data.
     * @throws SAXException
     */
    public void comment(char[] ch, int start, int length)
        throws SAXException {

        if (suppress) return;

        flushCharacters();

        String commentText = new String(ch, start, length);
        if (inDTD && inInternalSubset && (expand == false)) {
            internalSubset.append("  <!--")
                  .append(commentText)
                  .append("-->\n");
            return;
        }
        if ((!inDTD) && (!commentText.equals(""))) {
            if (atRoot) {
                factory.addContent(document, factory.comment(commentText));
            } else {
                factory.addContent(getCurrentElement(), factory.comment(commentText));
            }
        }
    }

    /**
     * Handle the declaration of a Notation in a DTD
     *
     * @param name name of the notation
     * @param publicID the public ID of the notation
     * @param systemID the system ID of the notation
     */
    public void notationDecl(String name, String publicID, String systemID)
        throws SAXException {

        if (!inInternalSubset) return;

        internalSubset.append("  <!NOTATION ")
              .append(name);
        appendExternalId(publicID, systemID);
        internalSubset.append(">\n");
    }

    /**
     * Handler for unparsed entity declarations in the DTD
     *
     * @param name <code>String</code> of the unparsed entity decl
     * @param publicID <code>String</code> of the unparsed entity decl
     * @param systemID <code>String</code> of the unparsed entity decl
     * @param notationName <code>String</code> of the unparsed entity decl
     */
    public void unparsedEntityDecl(String name, String publicID,
                                   String systemID, String notationName)
        throws SAXException {

        if (!inInternalSubset) return;

        internalSubset.append("  <!ENTITY ")
              .append(name);
        appendExternalId(publicID, systemID);
        internalSubset.append(" NDATA ")
              .append(notationName);
        internalSubset.append(">\n");
    }

    /**
     * Appends an external ID to the internal subset buffer. Either publicID
     * or systemID may be null, but not both.
     *
     * @param publicID the public ID
     * @param systemID the system ID
     */
    private void appendExternalId(String publicID, String systemID) {
        if (publicID != null) {
            internalSubset.append(" PUBLIC \"")
                  .append(publicID)
                  .append('\"');
        }
        if (systemID != null) {
            if (publicID == null) {
                internalSubset.append(" SYSTEM ");
            }
            else {
                internalSubset.append(' ');
            }
            internalSubset.append('\"')
                  .append(systemID)
                  .append('\"');
        }
    }

    /**
     * Returns the being-parsed element.
     *
     * @return <code>Element</code> - element being built.
     * @throws SAXException
     */
    public Element getCurrentElement() throws SAXException {
        if (currentElement == null) {
            throw new SAXException(
                "Ill-formed XML document (multiple root elements detected)");
        }
        return currentElement;
    }

    /**
     * Returns the the JDOM Attribute type value from the SAX 2.0
     * attribute type string provided by the parser.
     *
     * @param typeName <code>String</code> the SAX 2.0 attribute
     * type string.
     *
     * @return <code>int</code> the JDOM attribute type.
     *
     * @see Attribute#setAttributeType
     * @see Attributes#getType
     */
    private static int getAttributeType(String typeName) {
        Integer type = (Integer)(attrNameToTypeMap.get(typeName));
        if (type == null) {
            if (typeName != null && typeName.length() > 0 &&
                typeName.charAt(0) == '(') {
                // Xerces 1.4.X reports attributes of enumerated type with
                // a type string equals to the enumeration definition, i.e.
                // starting with a parenthesis.
                return Attribute.ENUMERATED_TYPE;
            }
            else {
                return Attribute.UNDECLARED_TYPE;
            }
        } else {
            return type.intValue();
        }
    }

    /**
     * Receives an object for locating the origin of SAX document
     * events.  This method is invoked by the SAX parser.
     * <p>
     * {@link org.jdom.JDOMFactory} implementations can use the
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
     * Provides access to the {@link Locator} object provided by the
     * SAX parser.
     *
     * @return <code>Locator</code> an object that can return
     * the location of any SAX document event.
     */
    public Locator getDocumentLocator() {
        return locator;
    }
}
