/*-- 

 $Id: SAXOutputter.java,v 1.20 2002/04/28 08:44:29 jhunter Exp $

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
    written permission, please contact <pm AT jdom DOT org>.
 
 4. Products derived from this software may not be called "JDOM", nor
    may "JDOM" appear in their name, without prior written permission
    from the JDOM Project Management <pm AT jdom DOT org>.
 
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
 created by Jason Hunter <jhunter AT jdom DOT org> and
 Brett McLaughlin <brett AT jdom DOT org>.  For more information
 on the JDOM Project, please see <http://www.jdom.org/>.
 
 */

package org.jdom.output;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import org.xml.sax.*;
import org.xml.sax.Locator;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.LocatorImpl;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.XMLReaderFactory;

import org.jdom.*;

/**
 * <p>
 * <code>SAXOutputter</code> takes a JDOM tree and fires SAX2 events.
 * </p>
 *
 * Most <code>ContentHandler</code> callbacks are supported. Both
 * <code>ignorableWhitespace</code> and <code>skippedEntity</code> have
 * not been implemented. The <code>setDocumentLocator</code> callback has
 * been implemented, but the locator object always returns -1 for
 * <code>getColumnNumber</code> and <code>getLineNumber</code>.
 * </p>
 *
 * The <code>EntityResolver</code> callback <code>resolveEntity</code> has
 * been implemented for DTDs.
 * </p>
 *
 * At this time, it is not possible to access notations and unparsed entity
 * references in a DTD from a JDOM tree. Therefore, <code>DTDHandler</code>
 * callbacks have not been implemented yet.
 * </p>
 *
 * The <code>ErrorHandler</code> callbacks have not been implemented, since
 * these are supposed to be invoked when the document is parsed. However, the
 * document has already been parsed in order to create the JDOM tree.
 * </p>
 *
 * @author Brett McLaughlin
 * @author Jason Hunter
 * @author Fred Trimble
 * @author Bradley S. Huffman
 * @version $Revision: 1.20 $, $Date: 2002/04/28 08:44:29 $
 */
public class SAXOutputter {
   
    private static final String CVS_ID = 
      "@(#) $RCSfile: SAXOutputter.java,v $ $Revision: 1.20 $ $Date: 2002/04/28 08:44:29 $ $Name:  $";

    /** Shortcut for SAX namespaces core feature */
    private static final String NAMESPACES_SAX_FEATURE =
                        "http://xml.org/sax/features/namespaces";

    /** Shortcut for SAX namespace-prefixes core feature */
    private static final String NS_PREFIXES_SAX_FEATURE =
                        "http://xml.org/sax/features/namespace-prefixes";

    /** Shortcut for SAX-ext. lexical handler property */
    private static final String LEXICAL_HANDLER_SAX_PROPERTY =
                        "http://xml.org/sax/properties/lexical-handler";

    /** Shortcut for SAX-ext. declaration handler property */
    private static final String DECL_HANDLER_SAX_PROPERTY =
                        "http://xml.org/sax/properties/declaration-handler";

    /**
     * Shortcut for SAX-ext. lexical handler alternate property.
     * Although this property URI is not the one defined by the SAX
     * "standard", some parsers use it instead of the official one.
     */
    private static final String LEXICAL_HANDLER_ALT_PROPERTY =
                        "http://xml.org/sax/handlers/LexicalHandler";

    /** Shortcut for SAX-ext. declaration handler alternate property */
    private static final String DECL_HANDLER_ALT_PROPERTY =
                        "http://xml.org/sax/handlers/DeclHandler";

    /**
     * Array to map JDOM attribute type (as entry index) to SAX
     * attribute type names.
     */
    private static final String[] attrTypeToNameMap = new String[] {
        "CDATA",        // Attribute.UNDEFINED_ATTRIBUTE, as per SAX 2.0 spec.
        "CDATA",        // Attribute.CDATA_ATTRIBUTE
        "ID",           // Attribute.ID_ATTRIBUTE
        "IDREF",        // Attribute.IDREF_ATTRIBUTE
        "IDREFS",       // Attribute.IDREFS_ATTRIBUTE
        "ENTITY",       // Attribute.ENTITY_ATTRIBUTE
        "ENTITIES",     // Attribute.ENTITIES_ATTRIBUTE
        "NMTOKEN",      // Attribute.NMTOKEN_ATTRIBUTE
        "NMTOKENS",     // Attribute.NMTOKENS_ATTRIBUTE
        "NOTATION",     // Attribute.NOTATION_ATTRIBUTE
        "NMTOKEN",      // Attribute.ENUMERATED_ATTRIBUTE, as per SAX 2.0 spec.
    };

    /** registered <code>ContentHandler</code> */
    private ContentHandler contentHandler;
   
    /** registered <code>ErrorHandler</code> */
    private ErrorHandler errorHandler;
   
    /** registered <code>DTDHandler</code> */
    private DTDHandler dtdHandler;
   
    /** registered <code>EntityResolver</code> */
    private EntityResolver entityResolver;

    /** registered <code>LexicalHandler</code> */
    private LexicalHandler lexicalHandler;

    /** registered <code>DeclHandler</code> */
    private DeclHandler declHandler;
   
    /**
     * Whether to report attribute namespace declarations as xmlns attributes.
     * Defaults to <code>false</code> as per SAX specifications.
     *
     * @see <a href="http://www.megginson.com/SAX/Java/namespaces.html">
     *  SAX namespace specifications</a>
     */
    private boolean declareNamespaces = false;

    /**
     * <p>
     * This will create a <code>SAXOutputter</code> without any
     * registered handler.  The application is then responsible for
     * registering them using the <code>setXxxHandler()</code> methods.
     * </p>
     */
    public SAXOutputter() {
    }

    /**
     * <p>
     * This will create a <code>SAXOutputter</code> with the
     * specified <code>ContentHandler</code>.
     * </p>
     *
     * @param contentHandler contains <code>ContentHandler</code> 
     * callback methods
     */
    public SAXOutputter(ContentHandler contentHandler) {
        this(contentHandler, null, null, null, null);
    }

    /**
     * <p>
     * This will create a <code>SAXOutputter</code> with the
     * specified SAX2 handlers. At this time, only <code>ContentHandler</code>
     * and <code>EntityResolver</code> are supported.
     * </p>
     *
     * @param contentHandler contains <code>ContentHandler</code> 
     * callback methods
     * @param errorHandler contains <code>ErrorHandler</code> callback methods
     * @param dtdHandler contains <code>DTDHandler</code> callback methods
     * @param entityResolver contains <code>EntityResolver</code> 
     * callback methods
     */
    public SAXOutputter(ContentHandler contentHandler,
                        ErrorHandler   errorHandler,
                        DTDHandler     dtdHandler,
                        EntityResolver entityResolver) {
        this(contentHandler, errorHandler, dtdHandler, entityResolver, null);
    }
   
    /**
     * <p>
     * This will create a <code>SAXOutputter</code> with the
     * specified SAX2 handlers. At this time, only <code>ContentHandler</code>
     * and <code>EntityResolver</code> are supported.
     * </p>
     *
     * @param contentHandler contains <code>ContentHandler</code> 
     * callback methods
     * @param errorHandler contains <code>ErrorHandler</code> callback methods
     * @param dtdHandler contains <code>DTDHandler</code> callback methods
     * @param entityResolver contains <code>EntityResolver</code> 
     * callback methods
     * @param lexicalHandler contains <code>LexicalHandler</code> callbacks.
     */
    public SAXOutputter(ContentHandler contentHandler,
                        ErrorHandler   errorHandler,
                        DTDHandler     dtdHandler,
                        EntityResolver entityResolver,
                        LexicalHandler lexicalHandler) {
        this.contentHandler = contentHandler;
        this.errorHandler = errorHandler;
        this.dtdHandler = dtdHandler;
        this.entityResolver = entityResolver;
        this.lexicalHandler = lexicalHandler;
    }
   
    /**
     * <p>
     * This will set the <code>ContentHandler</code>.
     * </p>
     *
     * @param contentHandler contains <code>ContentHandler</code> 
     * callback methods.
     */
    public void setContentHandler(ContentHandler contentHandler) {
        this.contentHandler = contentHandler;
    }
   
    /**
     * <p>
     * Returns the registered <code>ContentHandler</code>.
     * </p>
     *
     * @return the current <code>ContentHandler</code> or
     * <code>null</code> if none was registered.
     */
    public ContentHandler getContentHandler() {
        return this.contentHandler;
    }

    /**
     * <p>
     * This will set the <code>ErrorHandler</code>.
     * </p>
     *
     * @param errorHandler contains <code>ErrorHandler</code> callback methods.
     */
    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    /**
     * <p>
     * Return the registered <code>ErrorHandler</code>.
     * </p>
     *
     * @return the current <code>ErrorHandler</code> or
     * <code>null</code> if none was registered.
     */
    public ErrorHandler getErrorHandler() {
        return this.errorHandler;
    }

    /**
     * <p>
     * This will set the <code>DTDHandler</code>.
     * </p>
     *
     * @param dtdHandler contains <code>DTDHandler</code> callback methods.
     */
    public void setDTDHandler(DTDHandler dtdHandler) {
        this.dtdHandler = dtdHandler;
    }

    /**
     * <p>
     * Return the registered <code>DTDHandler</code>.
     * </p>
     *
     * @return the current <code>DTDHandler</code> or
     * <code>null</code> if none was registered.
     */
    public DTDHandler getDTDHandler() {
        return this.dtdHandler;
    }

    /**
     * <p>
     * This will set the <code>EntityResolver</code>.
     * </p>
     *
     * @param entityResolver contains EntityResolver callback methods.
     */
    public void setEntityResolver(EntityResolver entityResolver) {
        this.entityResolver = entityResolver;
    }

    /**
     * <p>
     * Return the registered <code>EntityResolver</code>.
     * </p>
     *
     * @return the current <code>EntityResolver</code> or
     * <code>null</code> if none was registered.
     */
    public EntityResolver getEntityResolver() {
        return this.entityResolver;
    }

    /**
     * <p>
     *  This will set the <code>LexicalHandler</code>.
     * </p>
     *
     * @param lexicalHandler contains lexical callback methods.
     */
    public void setLexicalHandler(LexicalHandler lexicalHandler) {
        this.lexicalHandler = lexicalHandler;
    }

    /**
     * <p>
     * Return the registered <code>LexicalHandler</code>.
     * </p>
     *
     * @return the current <code>LexicalHandler</code> or
     * <code>null</code> if none was registered.
     */
    public LexicalHandler getLexicalHandler() {
        return this.lexicalHandler;
    }

    /**
     * <p>
     *  This will set the <code>DeclHandler</code>.
     * </p>
     *
     * @param declHandler contains declaration callback methods.
     */
    public void setDeclHandler(DeclHandler declHandler) {
        this.declHandler = declHandler;
    }

    /**
     * <p>
     * Return the registered <code>DeclHandler</code>.
     * </p>
     *
     * @return the current <code>DeclHandler</code> or
     * <code>null</code> if none was registered.
     */
    public DeclHandler getDeclHandler() {
        return this.declHandler;
    }

    /**
     * <p>
     * This will define whether attribute namespace declarations shall be
     * reported as "xmlns" attributes.  This flag defaults to <code>false</code>
     * and behaves as the "namespace-prefixes" SAX core feature.
     * </p>
     *
     * @param reportDecl whether attribute namespace declarations shall be
     * reported as "xmlns" attributes.
     */
    public void setReportNamespaceDeclarations(boolean declareNamespaces) {
        this.declareNamespaces = declareNamespaces;
    }

    /**
     * <p>
     * This will set the state of a SAX feature.
     * </p>
     * <p>
     * All XMLReaders are required to support setting  to true and  to false.
     * </p>
     * <p>
     * SAXOutputter currently supports the following SAX core features:
     * <dl>
     *  <dt><code>http://xml.org/sax/features/namespaces</code></dt>
     *   <dd><strong>description:</strong> An optional extension handler for
     *       lexical events like comments.</dd>
     *   <dd><strong>access:</strong> read/write, but always
     *       <code>true</code>!</dd>
     *  <dt><code>http://xml.org/sax/features/namespace-prefixes</code></dt>
     *   <dd><strong>description:</strong> An optional extension handler
     *       for DTD-related events other than notations and unparsed
     *       entities.</dd>
     *   <dd><strong>access:</strong> read/write</dd>
     * </dl>
     * </p>
     *
     * @param name  <code>String</code> the feature name, which is a
     *              fully-qualified URI.
     * @param value <code>boolean</code> the requested state of the
     *              feature (true or false).
     *
     * @throws SAXNotRecognizedException When SAXOutputter does not
     *         recognize the feature name.
     * @throws SAXNotSupportedException  When SAXOutputter recognizes
     *         the feature name but cannot set the requested value.
     */
    public void setFeature(String name, boolean value)
                throws SAXNotRecognizedException, SAXNotSupportedException {
        if (NS_PREFIXES_SAX_FEATURE.equals(name)) {
            // Namespace prefix declarations.
            this.setReportNamespaceDeclarations(value);
        }
        else {
            if (NAMESPACES_SAX_FEATURE.equals(name)) {
                if (value != true) {
                    // Namespaces feature always supported by SAXOutputter.
                    throw new SAXNotSupportedException(name);
                }
                // Else: true is OK!
            }
            else {
                // Not a supported feature.
                throw new SAXNotRecognizedException(name);
            }
        }
    }

    /**
     * <p>
     * This will look up the value of a SAX feature.
     * </p>
     *
     * @param name <code>String</code> the feature name, which is a
     *             fully-qualified URI.
     * @return <code>boolean</code> the current state of the feature
     *         (true or false).
     *
     * @throws SAXNotRecognizedException When SAXOutputter does not
     *         recognize the feature name.
     * @throws SAXNotSupportedException  When SAXOutputter recognizes
     *         the feature name but determine its value at this time.
     */
    public boolean getFeature(String name)
                throws SAXNotRecognizedException, SAXNotSupportedException {
        if (NS_PREFIXES_SAX_FEATURE.equals(name)) {
            // Namespace prefix declarations.
            return (this.declareNamespaces);
        }
        else {
            if (NAMESPACES_SAX_FEATURE.equals(name)) {
                // Namespaces feature always supported by SAXOutputter.
                return (true);
            }
            else {
                // Not a supported feature.
                throw new SAXNotRecognizedException(name);
            }
        }
    }

    /**
     * <p>
     * This will set the value of a SAX property.
     * This method is also the standard mechanism for setting extended
     * handlers.
     * </p>
     * <p>
     * SAXOutputter currently supports the following SAX properties:
     * <dl>
     *  <dt><code>http://xml.org/sax/properties/lexical-handler</code></dt>
     *   <dd><strong>data type:</strong>
     *       <code>org.xml.sax.ext.LexicalHandler</code></dd>
     *   <dd><strong>description:</strong> An optional extension handler for
     *       lexical events like comments.</dd>
     *   <dd><strong>access:</strong> read/write</dd>
     *  <dt><code>http://xml.org/sax/properties/declaration-handler</code></dt>
     *   <dd><strong>data type:</strong>
     *       <code>org.xml.sax.ext.DeclHandler</code></dd>
     *   <dd><strong>description:</strong> An optional extension handler for
     *       DTD-related events other than notations and unparsed entities.</dd>
     *   <dd><strong>access:</strong> read/write</dd>
     * </dl>
     * </p>
     *
     * @param name  <code>String</code> the property name, which is a
     *              fully-qualified URI.
     * @param value <code>Object</code> the requested value for the property.
     *
     * @throws SAXNotRecognizedException When SAXOutputter does not recognize
     *         the property name.
     * @throws SAXNotSupportedException  When SAXOutputter recognizes the
     *         property name but cannot set the requested value.
     */
    public void setProperty(String name, Object value)
                throws SAXNotRecognizedException, SAXNotSupportedException {
        if ((LEXICAL_HANDLER_SAX_PROPERTY.equals(name)) ||
            (LEXICAL_HANDLER_ALT_PROPERTY.equals(name))) {
            this.setLexicalHandler((LexicalHandler)value);
        }
        else {
            if ((DECL_HANDLER_SAX_PROPERTY.equals(name)) ||
                (DECL_HANDLER_ALT_PROPERTY.equals(name))) {
                this.setDeclHandler((DeclHandler)value);
            }
            else {
                throw new SAXNotRecognizedException(name);
            }
        }
    }

    /**
     * <p>
     * This will look up the value of a SAX property.
     * </p>
     *
     * @param name <code>String</code> the property name, which is a
     *             fully-qualified URI.
     * @return <code>Object</code> the current value of the property.
     *
     * @throws SAXNotRecognizedException When SAXOutputter does not recognize
     *         the property name.
     * @throws SAXNotSupportedException  When SAXOutputter recognizes the
     *         property name but cannot determine its value at this time.
     */
    public Object getProperty(String name)
                throws SAXNotRecognizedException, SAXNotSupportedException {
        if ((LEXICAL_HANDLER_SAX_PROPERTY.equals(name)) ||
            (LEXICAL_HANDLER_ALT_PROPERTY.equals(name))) {
            return this.getLexicalHandler();
        }
        else {
            if ((DECL_HANDLER_SAX_PROPERTY.equals(name)) ||
                (DECL_HANDLER_ALT_PROPERTY.equals(name))) {
                return this.getDeclHandler();
            }
            else {
                throw new SAXNotRecognizedException(name);
            }
        }
    }


    /**
     * <p>
     * This will output the <code>JDOM Document</code>, firing off the
     * SAX events that have been registered.
     * </p>
     *
     * @param document <code>JDOM Document</code> to output.
     */
    public void output(Document document) throws JDOMException {
        if (document == null) {
            return;
        }

        // contentHandler.setDocumentLocator()
        documentLocator(document);

        // contentHandler.startDocument()
        startDocument();

        // Fire DTD events
        dtdEvents(document);

        // Handle root element, as well as any root level
        // processing instructions and CDATA sections
        Iterator i = document.getContent().iterator();
        while (i.hasNext()) {
            Object obj = i.next();
            if (obj instanceof Element) {
                // process root element and its content
                element(document.getRootElement(), new NamespaceStack());
            }
            else if (obj instanceof ProcessingInstruction) {
                // contentHandler.processingInstruction()
                processingInstruction((ProcessingInstruction) obj);
            }
            else if (obj instanceof CDATA) {
                // contentHandler.characters()
                characters(((CDATA) obj).getText());
            }
            else if (obj instanceof Comment) {
                // lexicalHandler.comment()
                comment(((Comment)obj).getText()); 
            }
        }
       
        // contentHandler.endDocument()
        endDocument();
    }
   
    /**
     * <p>
     * This parses a DTD declaration to fire the related events towards
     * the registered handlers.
     * </p>
     *
     * @param document <code>JDOM Document</code> the DocType is to
     *                 process.
     */
    private void dtdEvents(Document document) throws JDOMException {
        DocType docType = document.getDocType();

        if ((docType != null) &&
            ((dtdHandler != null) || (declHandler != null))) {
            // Fire DTD-related events only if handlers have been registered
            String publicID  = docType.getPublicID();
            String systemID  = docType.getSystemID();
            String intSubset = docType.getInternalSubset();

            if (intSubset != null) {
                intSubset = intSubset.trim();
            }

            // Build dummy XML document to reference DTD.
            StringBuffer buf = new StringBuffer(64);
            buf.append("<!DOCTYPE ").append(docType.getElementName());

            if ((intSubset != null) && (intSubset.length() !=0)) {
               // Internal subset present => Parse it
               buf.append(" [\n").append(intSubset).append(']');
            }
            else {
               // No internal subset defined => Try to parse original DTD
               if ((publicID != null) || (systemID != null)) {
                    if (publicID != null) {
                        buf.append(" PUBLIC ");
                        buf.append('\"').append(publicID).append('\"');
                    }
                    else {
                        buf.append(" SYSTEM ");
                    }
                    buf.append('\"').append(systemID).append('\"');
                }
                else {
                    // Doctype is totally empty! => Skip parsing
                    buf.setLength(0);
                }
            }

            if (buf.length() != 0) {
                try {
                    String dtdDoc = buf.append('>').toString();

                    // Parse dummy XML document to fire DTD events.
                    createDTDParser().parse(new InputSource(
                                                new StringReader(dtdDoc)));
                }
                catch (SAXParseException ex1) {
                    // Expected exception: There's no root element in DTD
                    // so the parser complains!
                    // ex1.printStackTrace();
                }
                catch (SAXException ex2) {
                    throw new JDOMException("DTD parsing error", ex2);
                }
                catch (IOException ex3) {
                    throw new JDOMException("DTD parsing error", ex3);
                }
            }
        }
    }

    /**
     * <p>
     * This method tells you the line of the XML file being parsed.
     * For an in-memory document, it's meaningless. The location
     * is only valid for the current parsing lifecycle, but
     * the document has already been parsed. Therefore, it returns
     * -1 for both line and column numbers.
     * </p>
     *
     * @param document JDOM <code>Document</code>.
     */
    private void documentLocator(Document document) {
        LocatorImpl locator = new LocatorImpl();
        String publicID = null;
        String systemID = null;
        DocType docType = document.getDocType();
        if (docType != null) {
            publicID = docType.getPublicID();
            systemID = docType.getSystemID();
        }
      
        locator.setPublicId(publicID);
        locator.setSystemId(systemID);
        locator.setLineNumber(-1);
        locator.setColumnNumber(-1);
      
        contentHandler.setDocumentLocator((Locator) locator);
    }
   
    /**
     * <p>
     * This method is always the second method of all callbacks in
     * all handlers to be invoked (setDocumentLocator is always first).
     * </p>
     */
    private void startDocument() throws JDOMException {
        try {
            contentHandler.startDocument();
        }
        catch (SAXException se) {
            throw new JDOMException("Exception in startDocument", se);
        }
    }
   
    /**
     * <p>
     * Always the last method of all callbacks in all handlers
     * to be invoked.
     * </p>
     */
    private void endDocument() throws JDOMException {
        try {
            contentHandler.endDocument();
        }
        catch (SAXException se) {
            throw new JDOMException("Exception in endDocument", se);
        }
    }
   
    /**
     * <p>
     * This will invoke the <code>ContentHandler.processingInstruction</code>
     * callback when a processing instruction is encountered.
     * </p>
     *
     * @param pi <code>ProcessingInstruction</code> containing target and data.
     */
    private void processingInstruction(ProcessingInstruction pi) 
                           throws JDOMException {
        if (pi != null) {
            String target = pi.getTarget();
            String data = pi.getData();
            try {
                contentHandler.processingInstruction(target, data);
            }
            catch (SAXException se) {
                throw new JDOMException(
                    "Exception in processingInstruction", se);
            }
        }
    }
   
    /**
     * <p>
     * This will recursively invoke all of the callbacks for a particular 
     * element.
     * </p>
     *
     * @param element <code>Element</code> used in callbacks.
     * @param namespaces <code>List</code> stack of Namespaces in scope.
     */
    private void element(Element element, NamespaceStack namespaces) 
                           throws JDOMException {
        // used to check endPrefixMapping
        int previouslyDeclaredNamespaces = namespaces.size();

        // contentHandler.startPrefixMapping()
        Attributes nsAtts = startPrefixMapping(element, namespaces);

        // contentHandler.startElement()
        startElement(element, nsAtts);

        // handle content in the element
        elementContent(element, namespaces);

        // contentHandler.endElement()
        endElement(element);

        // contentHandler.endPrefixMapping()
        endPrefixMapping(namespaces, previouslyDeclaredNamespaces);
    }
   
    /**
     * <p>
     * This will invoke the <code>ContentHandler.startPrefixMapping</code> 
     * callback
     * when a new namespace is encountered in the <code>Document</code>.
     * </p>
     *
     * @param element <code>Element</code> used in callbacks.
     * @param namespaces <code>List</code> stack of Namespaces in scope.
     *
     * @return <code>Attributes</code> declaring the namespaces local to
     * <code>element</code> or <code>null</code>.
     */
    private Attributes startPrefixMapping(Element element, 
                                          NamespaceStack namespaces) 
                                                   throws JDOMException {
        AttributesImpl nsAtts = null;   // The namespaces as xmlns attributes

        Namespace ns = element.getNamespace();
        if (ns != Namespace.NO_NAMESPACE && ns != Namespace.XML_NAMESPACE) {
            String prefix = ns.getPrefix();
            String uri = namespaces.getURI(prefix);
            if (!ns.getURI().equals(uri)) {
                namespaces.push(ns);
                nsAtts = this.addNsAttribute(nsAtts, ns);
                try {
                    contentHandler.startPrefixMapping(prefix, ns.getURI());
                }
                catch (SAXException se) {
                   throw new JDOMException(
                       "Exception in startPrefixMapping", se);
                }
            }
        }

        // Fire additional namespace declarations
        List additionalNamespaces = element.getAdditionalNamespaces();
        if (additionalNamespaces != null) {
            Iterator itr = additionalNamespaces.iterator();
            while (itr.hasNext()) {
                ns = (Namespace)itr.next();
                String prefix = ns.getPrefix();
                String uri = namespaces.getURI(prefix);
                if (!ns.getURI().equals(uri)) {
                    namespaces.push(ns);
                    nsAtts = this.addNsAttribute(nsAtts, ns);
                    try {
                        contentHandler.startPrefixMapping(prefix, ns.getURI());
                    }
                    catch (SAXException se) {
                        throw new JDOMException(
                            "Exception in startPrefixMapping", se);
                    }
                }
            }
        }
        return nsAtts;
    }

    /**
     * <p>
     * This will invoke the <code>endPrefixMapping</code> callback in the
     * <code>ContentHandler</code> when a namespace is goes out of scope
     * in the <code>Document</code>.
     * </p>
     *
     * @param namespaces <code>List</code> stack of Namespaces in scope.
     * @param previouslyDeclaredNamespaces number of previously declared 
     * namespaces
     */
    private void endPrefixMapping(NamespaceStack namespaces, 
                                  int previouslyDeclaredNamespaces) 
                                                throws JDOMException {
        while (namespaces.size() > previouslyDeclaredNamespaces) {
            String prefix = namespaces.pop();
            try {
                contentHandler.endPrefixMapping(prefix);
            }
            catch (SAXException se) {
                throw new JDOMException("Exception in endPrefixMapping", se);
            }
        }
    }
   
    /**
     * <p>
     * This will invoke the <code>startElement</code> callback
     * in the <code>ContentHandler</code>.
     * </p>
     *
     * @param element <code>Element</code> used in callbacks.
     * @param eltNamespaces <code>List</code> of namespaces to declare with
     * the element or <code>null</code>.
     */
    private void startElement(Element element, Attributes nsAtts) 
                      throws JDOMException {
        String namespaceURI = element.getNamespaceURI();
        String localName = element.getName();
        String rawName = element.getQualifiedName();

        // Allocate attribute list.
        AttributesImpl atts = (nsAtts != null)?
                              new AttributesImpl(nsAtts): new AttributesImpl();

        List attributes = element.getAttributes();
        Iterator i = attributes.iterator();
        while (i.hasNext()) {
            Attribute a = (Attribute) i.next();
            atts.addAttribute(a.getNamespaceURI(),
                              a.getName(),
                              a.getQualifiedName(),
                              getAttributeTypeName(a.getAttributeType()),
                              a.getValue());
        }
         
        try {
            contentHandler.startElement(namespaceURI, localName, rawName, atts);
        }
        catch (SAXException se) {
            throw new JDOMException("Exception in startElement", se);
        }
    }
   
    /**
     * <p>
     * This will invoke the <code>endElement</code> callback
     * in the <code>ContentHandler</code>.
     * </p>
     *
     * @param element <code>Element</code> used in callbacks.
     */
    private void endElement(Element element) throws JDOMException {
        String namespaceURI = element.getNamespaceURI();
        String localName = element.getName();
        String rawName = element.getQualifiedName();
        
        try {
            contentHandler.endElement(namespaceURI, localName, rawName);
        }
        catch (SAXException se) {
            throw new JDOMException("Exception in endElement", se);
        }
    }
   
    /**
     * <p>
     * This will invoke the callbacks for the content of an element.
     * </p>
     *
     * @param element <code>Element</code> used in callbacks.
     * @param namespaces <code>List</code> stack of Namespaces in scope.
     */
    private void elementContent(Element element, NamespaceStack namespaces) 
                      throws JDOMException {
        List eltContent = element.getContent();
      
        boolean empty = eltContent.size() == 0;
        boolean stringOnly =
            !empty &&
            eltContent.size() == 1 &&
            eltContent.get(0) instanceof Text;
          
        if (stringOnly) {
            // contentHandler.characters()
            characters(element.getText());
        }
        else {
            Object content = null;
            for (int i = 0, size = eltContent.size(); i < size; i++) {
                content = eltContent.get(i);
                if (content instanceof Element) {
                    element((Element) content, namespaces);
                }
                else if (content instanceof Text) {  // includes CDATA
                    // contentHandler.characters()
                    characters(((Text) content).getText());
                }
                else if (content instanceof ProcessingInstruction) {
                    // contentHandler.processingInstruction()
                    processingInstruction((ProcessingInstruction) content);
                }
            }
        }
    }
    
    /**
     * <p>
     * This will be called for each chunk of character data encountered.
     * </p>
     *
     * @param elementText all text in an element, including whitespace.
     */
    private void characters(String elementText) throws JDOMException {
        char[] c = elementText.toCharArray();
        try {
            contentHandler.characters(c, 0, c.length);
        }
        catch (SAXException se) {
            throw new JDOMException("Exception in characters", se);
        }
    }

    /**
     * <p>
     *  This will be called for each chunk of comment data encontered.
     * </p>
     *
     * @param commentText all text in a comment, including whitespace.
     */
    private void comment(String commentText) throws JDOMException {
        if (lexicalHandler != null) {
            char[] c = commentText.toCharArray();
            try {
                lexicalHandler.comment(c, 0, c.length);
            } catch (SAXException se) {
                throw new JDOMException("Exception in comment", se);
            }
        }
    }

    /**
     * <p>
     * Appends a namespace declaration in the form of a xmlns attribute to
     * an attribute list, crerating this latter if needed.
     * </p>
     *
     * @param atts <code>AttributeImpl</code> where to add the attribute.
     * @param ns <code>Namespace</code> the namespace to declare.
     *
     * @return <code>AttributeImpl</code> the updated attribute list.
     */
    private AttributesImpl addNsAttribute(AttributesImpl atts, Namespace ns) {
        if (this.declareNamespaces) {
            if (atts == null) {
                atts = new AttributesImpl();
            }
            atts.addAttribute("",                          // namespace
                              "",                          // local name
                              "xmlns:" + ns.getPrefix(),   // qualified name
                              "CDATA",                     // type
                              ns.getURI());                // value
        }
        return atts;
    }

    /**
     * <p>
     * Returns the SAX 2.0 attribute type string from the type of
     * a JDOM Attribute.
     * </p>
     *
     * @param type <code>int</code> the type of the JDOM attribute.
     *
     * @return <code>String</code> the SAX 2.0 attribute type string.
     *
     * @see org.jdom.Attribute#getAttributeType
     * @see org.xml.sax.Attributes#getType
     */
    private String getAttributeTypeName(int type) {
        if ((type < 0) || (type >= attrTypeToNameMap.length)) {
            type = Attribute.UNDECLARED_ATTRIBUTE;
        }
        return attrTypeToNameMap[type];
    }

    /**
     * <p>
     * Creates a SAX XMLReader.
     * </p>
     *
     * @return <code>XMLReader</code> a SAX2 parser.
     *
     * @throws Exception if no parser can be created.
     */
    protected XMLReader createParser() throws Exception {
        XMLReader parser = null;

        // Try using JAXP...
        // Note we need JAXP 1.1, and if JAXP 1.0 is all that's
        // available then the getXMLReader call fails and we skip
        // to the hard coded default parser
        try {
            Class factoryClass = 
                    Class.forName("javax.xml.parsers.SAXParserFactory");

            // factory = SAXParserFactory.newInstance();
            Method newParserInstance = 
                    factoryClass.getMethod("newInstance", null);
            Object factory = newParserInstance.invoke(null, null);

            // jaxpParser = factory.newSAXParser();
            Method newSAXParser = factoryClass.getMethod("newSAXParser", null);
            Object jaxpParser   = newSAXParser.invoke(factory, null);

            // parser = jaxpParser.getXMLReader();
            Class parserClass = jaxpParser.getClass();
            Method getXMLReader = 
                    parserClass.getMethod("getXMLReader", null);
            parser = (XMLReader)getXMLReader.invoke(jaxpParser, null);
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
        } catch (InvocationTargetException e) {
            //e.printStackTrace();
        } catch (NoSuchMethodException e) {
            //e.printStackTrace();
        } catch (IllegalAccessException e) {
            //e.printStackTrace();
        }

        // Check to see if we got a parser yet, if not, try to use a
        // hard coded default
        if (parser == null) {
            parser = XMLReaderFactory.createXMLReader(
                                "org.apache.xerces.parsers.SAXParser");
        }
        return parser;
    }

    /**
     * <p>
     * This will create a SAX XMLReader capable of parsing a DTD and
     * configure it so that the DTD parsing events are routed to the
     * handlers registered onto this SAXOutputter.
     * </p>
     *
     * @return <code>XMLReader</code> a SAX2 parser.
     *
     * @throws JDOMException if no parser can be created.
     */
    private XMLReader createDTDParser() throws JDOMException {
        XMLReader parser = null;

        // Get a parser instance
        try
        {
            parser = createParser();
        }
        catch (Exception ex1) {
           throw new JDOMException("Error in SAX parser allocation", ex1);
        }

        // Register handlers
        if (this.getDTDHandler() != null) {
            parser.setDTDHandler(this.getDTDHandler());
        }
        if (this.getEntityResolver() != null) {
            parser.setEntityResolver(this.getEntityResolver());
        }
        if (this.getLexicalHandler() != null) {
            try {
                parser.setProperty(LEXICAL_HANDLER_SAX_PROPERTY,
                                   this.getLexicalHandler());
            }
            catch (SAXException ex1) {
                try {
                    parser.setProperty(LEXICAL_HANDLER_ALT_PROPERTY,
                                       this.getLexicalHandler());
                } catch (SAXException ex2) {
                    // Forget it!
                }
            }
        }
        if (this.getDeclHandler() != null) {
            try {
                parser.setProperty(DECL_HANDLER_SAX_PROPERTY,
                                   this.getDeclHandler());
            }
            catch (SAXException ex1) {
                try {
                    parser.setProperty(DECL_HANDLER_ALT_PROPERTY,
                                       this.getDeclHandler());
                } catch (SAXException ex2) {
                    // Forget it!
                }
            }
        }

        return (parser);
    }
}
