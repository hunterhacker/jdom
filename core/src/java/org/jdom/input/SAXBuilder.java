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
import java.io.InputStream;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Stack;

import org.jdom.Attribute;
import org.jdom.Comment;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Entity;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.ProcessingInstruction;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * <p><code>SAXBuilder</code> builds a JDOM tree using SAX.</p>
 *
 * @author Brett McLaughlin
 * @author Jason Hunter
 * @version 1.0
 */
public class SAXBuilder {

    /** Default parser class to use */
    private static final String DEFAULT_SAX_DRIVER =
        "org.apache.xerces.parsers.SAXParser";

    /** Whether validation should occur */
    private boolean validate;

    /** Adapter class to use */
    private String saxDriverClass;

    /**
     * <p>
     * This allows the validation features to be turned on/off
     *   in the builder at creation, as well as set the
     *   DOM Adapter class to use.
     * </p>
     *
     * @param saxDriverClass <code>String</code> name of SAX Driver
     *                       to use for parsing.
     * @param validate <code>boolean</code> indicating if
     *                 validation should occur.
     */
    public SAXBuilder(String saxDriverClass, boolean validate) {
        this.saxDriverClass = saxDriverClass;
        this.validate = validate;
    }

    /**
     * <p>
     * This sets the SAX Driver class to use, and leaves
     *   validation off.
     * </p>
     *
     * @param saxDriverClass <code>String</code> name of SAX Driver
     *                       to use for parsing.
     */
    public SAXBuilder(String saxDriverClass) {
        this(saxDriverClass, false);
    }

    /**
     * <p>
     * This sets validation for the <code>Builder</code>.
     * </p>
     *
     * @param validate <code>boolean</code> indicating if
     *                 validation should occur.
     */
    public SAXBuilder(boolean validate) {
        this(DEFAULT_SAX_DRIVER, validate);
    }

    /**
     * <p>
     * This creates a <code>SAXBuilder</code> with
     *   the default SAX driver and no validation.
     * </p>
     */
    public SAXBuilder() {
        this(DEFAULT_SAX_DRIVER, false);
    }

    /**
     * <p>
     * This builds a document from the supplied
     *   input stream.
     * </p>
     *
     * @param in <code>InputSource</code> to read from.
     * @return <code>Document</code> - resultant Document object.
     * @throws <code>JDOMException</code> when errors occur in
     *                                    parsing.
     */
    protected Document build(InputSource in)
        throws JDOMException {

        Document doc = new Document(null);

        try {
            XMLReader parser =
                XMLReaderFactory.createXMLReader(saxDriverClass);

            DefaultHandler contentHandler =
                new SAXHandler(doc);

            parser.setContentHandler(contentHandler);

            boolean lexicalReporting = false;
            try {
                parser.setProperty("http://xml.org/sax/handlers/LexicalHandler",
                                   contentHandler);
                lexicalReporting = true;
            } catch (SAXNotSupportedException e) {
                // No lexical reporting available
            } catch (SAXNotRecognizedException e) {
                // No lexical reporting available
            }

            // Some parsers use alternate propety for lexical handling (grr...)
            if (!lexicalReporting) {
                try {
                    parser.setProperty("http://xml.org/sax/properties/lexical-handler",
                                       contentHandler);
                    lexicalReporting = true;
                } catch (SAXNotSupportedException e) {
                    // No lexical reporting available
                } catch (SAXNotRecognizedException e) {
                    // No lexical reporting available
                }
            }

            // Set validation
            try {
                parser.setFeature("http://xml.org/sax/features/validation", validate);
                parser.setFeature("http://xml.org/sax/features/namespaces", false);
                parser.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
                parser.setErrorHandler(contentHandler);
            } catch (SAXNotSupportedException e) {
                // No validation available
                if (validate) {
                    throw new JDOMException(
                        "Validation not supported for " + saxDriverClass +
                        " SAX Driver");
                }
            } catch (SAXNotRecognizedException e) {
                // No validation available
                if (validate) {
                    throw new JDOMException(
                        "Validation feature not recognized for " + saxDriverClass +
                        " SAX Driver");
                }
            }

            parser.parse(in);

            return doc;
        } catch (Exception e) {
            if (e instanceof SAXParseException) {
                SAXParseException p =
                    (SAXParseException)e;
                throw new JDOMException(e.getMessage(),
                    new JDOMException("Error on line " + p.getLineNumber() +
                                      " of XML document: " + p.getMessage()));
            } else {
                throw new JDOMException(e.getMessage(), e);
            }
        }
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
    public Document build(InputStream in)
        throws JDOMException {

        return build(new InputSource(in));
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
            URL url = file.toURL();
            return build(url);
        } catch (MalformedURLException e) {
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
        String systemID = url.toExternalForm();
        return build(new InputSource(systemID));
    }

    /**
     * <p>
     * This builds a document from the supplied
     *   input stream.
     * </p>
     *
     * @param in <code>InputStream</code> to read from.
     * @param systemId base for resolving relative URIs
     * @return <code>Document</code> - resultant Document object.
     * @throws <code>JDOMException</code> when errors occur in
     *                                    parsing.
     */
    public Document build(InputStream in, String systemId)
        throws JDOMException {

        InputSource src = new InputSource(in);
        src.setSystemId(systemId);
        return build(src);
    }

    /**
     * <p>
     * This builds a document from the supplied
     *   Reader.
     * </p>
     *
     * @param in <code>Reader</code> to read from.
     * @return <code>Document</code> - resultant Document object.
     * @throws <code>JDOMException</code> when errors occur in
     *                                    parsing.
     */
    public Document build(Reader characterStream)
        throws JDOMException {

        return build(new InputSource(characterStream));
    }

    /**
     * <p>
     * This builds a document from the supplied
     *   Reader.
     * </p>
     *
     * @param in <code>Reader</code> to read from.
     * @param systemId base for resolving relative URIs
     * @return <code>Document</code> - resultant Document object.
     * @throws <code>JDOMException</code> when errors occur in
     *                                    parsing.
     */
    public Document build(Reader characterStream, String SystemId)
        throws JDOMException {

        InputSource src = new InputSource(characterStream);
        src.setSystemId(SystemId);
        return build(src);
    }

    /**
     * <p>
     * This builds a document from the supplied
     *   URI.
     * </p>
     * @param systemId URI for the input
     * @return <code>Document</code> - resultant Document object.
     * @throws <code>JDOMException</code> when errors occur in
     *                                    parsing.
     */
    public Document build(String systemId)
        throws JDOMException {

        return build(new InputSource(systemId));
    }
}

class SAXHandler extends DefaultHandler implements LexicalHandler {

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

    /** Namespaces for this Document */
    private LinkedList namespaces;

    /** Mappings on elements for this Document */
    private HashMap namespaceDefinitions;

    /** Method to get an Attribute's full name */
    private Method getQName;

    /**
     * <p>
     * This will set the <code>Document</code> to use.
     * </p>
     *
     * @param document <code>Document</code> being parsed.
     * @throws <code>IOException</code> when errors occur.
     */
    public SAXHandler(Document document) throws IOException {
        this.document = document;
        atRoot = true;
        stack = new Stack();
        namespaces = new LinkedList();
        namespaceDefinitions = new HashMap();
        inEntity = false;
        inDTD = false;
        inCDATA = false;

        // Figure out which SAX is being used, so we use the correct method
        //   on Attributes
        try {
            getQName = Attributes.class.getMethod("getQName", new Class[] {int.class});
        } catch (NoSuchMethodException e) {
            try {
                getQName = Attributes.class.getMethod("getRawName", new Class[] {int.class});
            } catch (NoSuchMethodException f) {
                throw new IOException("Cannot locate SAX 2.0 (pre-release or final) classes.");
            }
        }
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
     * @throws <code>SAXException</code> when things go wrong
     */
    public void processingInstruction(String target, String data)
        throws SAXException {

        if (atRoot) {
            document.addProcessingInstruction(target, data);
        } else {
            ((Element)stack.peek()).addChild(new ProcessingInstruction(target, data));
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

       // Never gets called
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
     * @param rawName <code>String</code> XML 1.0 version of element name:
     *                [namespace prefix]:[localName]
     * @param atts <code>Attributes</code> list for this element
     * @throws <code>SAXException</code> when things go wrong
     */
    public void startElement(String namespaceURI, String localName,
                             String rawName, Attributes atts) throws SAXException {

        // Handle attributes for namespaces first
        List elementAttributes = new LinkedList();
        List elementDefinitions = new LinkedList();

        for (int i=0, size=atts.getLength(); i<size; i++) {
            String attName;
            try {
                attName = (String)getQName.invoke(atts, new Object[] {new Integer(i)});
            } catch (InvocationTargetException e) {
                throw new SAXException("Cannot determine Attribute's full name");
            } catch (IllegalAccessException e) {
                throw new SAXException("Cannot access Attribute's full name");
            }

            if (attName.startsWith("xmlns")) {
                int attributeSplit;
                String prefix = "";
                String uri = atts.getValue(i);
                Namespace ns = null;
                if ((attributeSplit = attName.indexOf(":")) != -1) {
                    prefix = attName.substring(attributeSplit + 1);
                }

                // Add namespace to list
                ns = Namespace.getNamespace(prefix, uri);
                elementDefinitions.add(ns);
                namespaces.add(ns);
            }
        }

        // Add this element and its definitions to the global mapping map
        namespaceDefinitions.put(rawName, elementDefinitions);

        int elementSplit;
        String prefix = "";
        String name = rawName;
        if ((elementSplit = rawName.indexOf(":")) != -1) {
            prefix = rawName.substring(0, elementSplit);
            name = rawName.substring(elementSplit + 1);
        }
        Element element =
            new Element(name, prefix, getNamespaceURI(prefix));

        // With namespaces set up, we can do attributes now
        for (int i=0, size=atts.getLength(); i<size; i++) {
            String attName;
            try {
                attName = (String)getQName.invoke(atts, new Object[] {new Integer(i)});
            } catch (InvocationTargetException e) {
                throw new SAXException("Cannot determine Attribute's full name");
            } catch (IllegalAccessException e) {
                throw new SAXException("Cannot access Attribute's full name");
            }

            if (!attName.startsWith("xmlns")) {
                name = attName;
                int attSplit;
                prefix = "";
                if ((attSplit = name.indexOf(":")) != -1) {
                    prefix = name.substring(0, attSplit);
                    name = name.substring(attSplit + 1);
                }
                element.addAttribute(
                    new Attribute(name,
                                  prefix,
                                  getNamespaceURI(prefix),
                                  atts.getValue(i)));
            }
        }

        if (atRoot) {
            document.setRootElement(element);
            stack.push(element);
            atRoot = false;
        } else {
            ((Element)stack.peek()).addChild(element);
            stack.push(element);
        }
    }

    /**
     * <p>
     *  This "walks" up the namespaces, and returns the first one found
     *    (which means it is the last one added) with the prefix
     *    supplied. For the default namespace, the supplied prefix
     *    should be "".
     * </p>
     *
     * @param prefix <code>String</code> prefix to look up.
     * @return <code>String</code> - URI mapped to that prefix.
     */
    private String getNamespaceURI(String prefix) {
        // Cycle backwards and find URI
        for (int i=namespaces.size() - 1; i >= 0; i--) {
            Namespace ns = (Namespace)namespaces.get(i);
            if (ns.getPrefix().equals(prefix)) {
                return ns.getURI();
            }
        }

        // If we got here, no URI
        return "";
    }

    /**
     * <p>
     * This will report character data (within an element).
     * </p>
     *
     * @param ch <code>char[]</code> character array with character data
     * @param start <code>int</code> index in array where data starts.
     * @param end <code>int</code> index in array where data ends.
     * @throws <code>SAXException</code> when things go wrong
     */
    public void characters(char[] ch, int start, int end)
        throws SAXException {

        String data = new String(ch, start, end);

        if (inCDATA) {
            ((Element)stack.peek()).addChild(data);
        } else if ((!inDTD) && (!data.trim().equals(""))) {
            if (inEntity) {
                ((Entity)stack.peek()).setContent(data);
            } else {
                Element e = (Element)stack.peek();
                e.addChild(data);
            }
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
     * @param rawName <code>String</code> name of element in XML 1.0 form
     * @throws <code>SAXException</code> when things go wrong
     */
    public void endElement(String namespaceURI, String localName,
                           String rawName) {

        stack.pop();

        // Remove the namespaces this Element makes available
        LinkedList elementDefinitions =
            (LinkedList)namespaceDefinitions.get(rawName);
        if (elementDefinitions != null) {
            elementDefinitions.remove(rawName);
            namespaces.removeAll(elementDefinitions);
        }
    }

    /**
     * <p>
     * This will report an error that has occurred; this indicates
     *   that a rule was broken, typically in validation, but that
     *   parsing can reasonably continue.
     * </p>
     *
     * @param exception <code>SAXParseException</code> that occurred.
     * @throws <code>SAXException</code> when things go wrong
     */
    public void error(SAXParseException exception)
        throws SAXException {

        throw exception;
    }

    /**
     * <p>
     * This will report an error that has occurred; this indicates
     *   that a rule was broken, typically in validation, but that
     *   parsing can reasonably continue.
     * </p>
     *
     * @param exception <code>SAXParseException</code> that occurred.
     * @throws <code>SAXException</code> when things go wrong
     */
    public void warning(SAXParseException exception)
        throws SAXException {

        throw exception;
    }

    /**
     * <p>
     * This will report an error that has occurred; this indicates
     *   that a rule was broken, typically in validation, but that
     *   parsing can reasonably continue.
     * </p>
     *
     * @param exception <code>SAXParseException</code> that occurred.
     * @throws <code>SAXException</code> when things go wrong
     */
    public void fatalError(SAXParseException exception)
        throws SAXException {

        throw exception;
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
            ((Element)stack.peek()).addChild(entity);
            stack.push(entity);
            inEntity = true;
        }
    }

    public void endEntity(String name)
        throws SAXException {

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
                document.addComment(
                   new Comment(commentText));
            } else {
                ((Element)stack.peek()).addChild(
                    new Comment(commentText));
            }
        }
    }

}
