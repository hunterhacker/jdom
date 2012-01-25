/*--

 $Id: SAXOutputter.java,v 1.40 2007/11/10 05:29:01 jhunter Exp $

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

package org.jdom.output;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import org.jdom.*;
import org.xml.sax.*;
import org.xml.sax.ext.*;
import org.xml.sax.helpers.*;

/**
 * Outputs a JDOM document as a stream of SAX2 events.
 * <p>
 * Most ContentHandler callbacks are supported. Both
 * <code>ignorableWhitespace()</code> and <code>skippedEntity()</code> have not
 * been implemented. The <code>{@link JDOMLocator}</code> class returned by
 * <code>{@link #getLocator}</code> exposes the current node being operated
 * upon.
 * <p>
 * At this time, it is not possible to access notations and unparsed entity
 * references in a DTD from JDOM. Therefore, <code>DTDHandler</code> callbacks
 * have not been implemented yet.
 * <p>
 * The <code>ErrorHandler</code> callbacks have not been implemented, since
 * these are supposed to be invoked when the document is parsed and at this
 * point the document exists in memory and is known to have no errors. </p>
 *
 * @version $Revision: 1.40 $, $Date: 2007/11/10 05:29:01 $
 * @author  Brett McLaughlin
 * @author  Jason Hunter
 * @author  Fred Trimble
 * @author  Bradley S. Huffman
 */
public class SAXOutputter {

    private static final String CVS_ID =
      "@(#) $RCSfile: SAXOutputter.java,v $ $Revision: 1.40 $ $Date: 2007/11/10 05:29:01 $ $Name:  $";

    /** Shortcut for SAX namespaces core feature */
    private static final String NAMESPACES_SAX_FEATURE =
                        "http://xml.org/sax/features/namespaces";

    /** Shortcut for SAX namespace-prefixes core feature */
    private static final String NS_PREFIXES_SAX_FEATURE =
                        "http://xml.org/sax/features/namespace-prefixes";

    /** Shortcut for SAX validation core feature */
    private static final String VALIDATION_SAX_FEATURE =
                        "http://xml.org/sax/features/validation";

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
        "CDATA",        // Attribute.CDATA_TYPE
        "ID",           // Attribute.ID_TYPE
        "IDREF",        // Attribute.IDREF_TYPE
        "IDREFS",       // Attribute.IDREFS_TYPE
        "ENTITY",       // Attribute.ENTITY_TYPE
        "ENTITIES",     // Attribute.ENTITIES_TYPE
        "NMTOKEN",      // Attribute.NMTOKEN_TYPE
        "NMTOKENS",     // Attribute.NMTOKENS_TYPE
        "NOTATION",     // Attribute.NOTATION_TYPE
        "NMTOKEN",      // Attribute.ENUMERATED_TYPE, as per SAX 2.0 spec.
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
     * Whether to report DTD events to DeclHandlers and LexicalHandlers.
     * Defaults to <code>true</code>.
     */
    private boolean reportDtdEvents = true;

    /**
     * A SAX Locator that points at the JDOM node currently being
     * outputted.
     */
    private JDOMLocator locator = null;

    /**
     * This will create a <code>SAXOutputter</code> without any
     * registered handler.  The application is then responsible for
     * registering them using the <code>setXxxHandler()</code> methods.
     */
    public SAXOutputter() {
    }

    /**
     * This will create a <code>SAXOutputter</code> with the
     * specified <code>ContentHandler</code>.
     *
     * @param contentHandler contains <code>ContentHandler</code>
     * callback methods
     */
    public SAXOutputter(ContentHandler contentHandler) {
        this(contentHandler, null, null, null, null);
    }

    /**
     * This will create a <code>SAXOutputter</code> with the
     * specified SAX2 handlers. At this time, only <code>ContentHandler</code>
     * and <code>EntityResolver</code> are supported.
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
     * This will create a <code>SAXOutputter</code> with the
     * specified SAX2 handlers. At this time, only <code>ContentHandler</code>
     * and <code>EntityResolver</code> are supported.
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
     * This will set the <code>ContentHandler</code>.
     *
     * @param contentHandler contains <code>ContentHandler</code>
     * callback methods.
     */
    public void setContentHandler(ContentHandler contentHandler) {
        this.contentHandler = contentHandler;
    }

    /**
     * Returns the registered <code>ContentHandler</code>.
     *
     * @return the current <code>ContentHandler</code> or
     * <code>null</code> if none was registered.
     */
    public ContentHandler getContentHandler() {
        return this.contentHandler;
    }

    /**
     * This will set the <code>ErrorHandler</code>.
     *
     * @param errorHandler contains <code>ErrorHandler</code> callback methods.
     */
    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    /**
     * Return the registered <code>ErrorHandler</code>.
     *
     * @return the current <code>ErrorHandler</code> or
     * <code>null</code> if none was registered.
     */
    public ErrorHandler getErrorHandler() {
        return this.errorHandler;
    }

    /**
     * This will set the <code>DTDHandler</code>.
     *
     * @param dtdHandler contains <code>DTDHandler</code> callback methods.
     */
    public void setDTDHandler(DTDHandler dtdHandler) {
        this.dtdHandler = dtdHandler;
    }

    /**
     * Return the registered <code>DTDHandler</code>.
     *
     * @return the current <code>DTDHandler</code> or
     * <code>null</code> if none was registered.
     */
    public DTDHandler getDTDHandler() {
        return this.dtdHandler;
    }

    /**
     * This will set the <code>EntityResolver</code>.
     *
     * @param entityResolver contains EntityResolver callback methods.
     */
    public void setEntityResolver(EntityResolver entityResolver) {
        this.entityResolver = entityResolver;
    }

    /**
     * Return the registered <code>EntityResolver</code>.
     *
     * @return the current <code>EntityResolver</code> or
     * <code>null</code> if none was registered.
     */
    public EntityResolver getEntityResolver() {
        return this.entityResolver;
    }

    /**
     * This will set the <code>LexicalHandler</code>.
     *
     * @param lexicalHandler contains lexical callback methods.
     */
    public void setLexicalHandler(LexicalHandler lexicalHandler) {
        this.lexicalHandler = lexicalHandler;
    }

    /**
     * Return the registered <code>LexicalHandler</code>.
     *
     * @return the current <code>LexicalHandler</code> or
     * <code>null</code> if none was registered.
     */
    public LexicalHandler getLexicalHandler() {
        return this.lexicalHandler;
    }

    /**
     * This will set the <code>DeclHandler</code>.
     *
     * @param declHandler contains declaration callback methods.
     */
    public void setDeclHandler(DeclHandler declHandler) {
        this.declHandler = declHandler;
    }

    /**
     * Return the registered <code>DeclHandler</code>.
     *
     * @return the current <code>DeclHandler</code> or
     * <code>null</code> if none was registered.
     */
    public DeclHandler getDeclHandler() {
        return this.declHandler;
    }

    /**
     * Returns whether attribute namespace declarations shall be reported as
     * "xmlns" attributes.
     *
     * @return whether attribute namespace declarations shall be reported as
     * "xmlns" attributes.
     */
    public boolean getReportNamespaceDeclarations() {
        return declareNamespaces;
    }

    /**
     * This will define whether attribute namespace declarations shall be
     * reported as "xmlns" attributes.  This flag defaults to <code>false</code>
     * and behaves as the "namespace-prefixes" SAX core feature.
     *
     * @param declareNamespaces whether attribute namespace declarations
     * shall be reported as "xmlns" attributes.
     */
    public void setReportNamespaceDeclarations(boolean declareNamespaces) {
        this.declareNamespaces = declareNamespaces;
    }

    /**
     * Returns whether DTD events will be reported.
     *
     * @return whether DTD events will be reported
     */
    public boolean getReportDTDEvents() {
        return reportDtdEvents;
    }

    /**
     * This will define whether to report DTD events to SAX DeclHandlers
     * and LexicalHandlers if these handlers are registered and the
     * document to output includes a DocType declaration.
     *
     * @param reportDtdEvents whether to notify DTD events.
     */
    public void setReportDTDEvents(boolean reportDtdEvents) {
        this.reportDtdEvents = reportDtdEvents;
    }

    /**
     * This will set the state of a SAX feature.
     * <p>
     * All XMLReaders are required to support setting to true and to false.
     * </p>
     * <p>
     * SAXOutputter currently supports the following SAX core features:
     * <dl>
     *  <dt><code>http://xml.org/sax/features/namespaces</code></dt>
     *   <dd><strong>description:</strong> <code>true</code> indicates
     *       namespace URIs and unprefixed local names for element and
     *       attribute names will be available</dd>
     *   <dd><strong>access:</strong> read/write, but always
     *       <code>true</code>!</dd>
     *  <dt><code>http://xml.org/sax/features/namespace-prefixes</code></dt>
     *   <dd><strong>description:</strong> <code>true</code> indicates
     *       XML 1.0 names (with prefixes) and attributes (including xmlns*
     *       attributes) will be available</dd>
     *   <dd><strong>access:</strong> read/write</dd>
     *  <dt><code>http://xml.org/sax/features/validation</code></dt>
     *   <dd><strong>description:</strong> controls whether SAXOutputter
     *       is reporting DTD-related events; if <code>true</code>, the
     *       DocType internal subset will be parsed to fire DTD events</dd>
     *   <dd><strong>access:</strong> read/write, defaults to
     *       <code>true</code></dd>
     * </dl>
     * </p>
     *
     * @param name  <code>String</code> the feature name, which is a
     *              fully-qualified URI.
     * @param value <code>boolean</code> the requested state of the
     *              feature (true or false).
     *
     * @throws SAXNotRecognizedException when SAXOutputter does not
     *         recognize the feature name.
     * @throws SAXNotSupportedException  when SAXOutputter recognizes
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
                if (VALIDATION_SAX_FEATURE.equals(name)) {
                    // Report DTD events.
                    this.setReportDTDEvents(value);
                }
                else {
                    // Not a supported feature.
                    throw new SAXNotRecognizedException(name);
                }
            }
        }
    }

    /**
     * This will look up the value of a SAX feature.
     *
     * @param name <code>String</code> the feature name, which is a
     *             fully-qualified URI.
     * @return <code>boolean</code> the current state of the feature
     *         (true or false).
     *
     * @throws SAXNotRecognizedException when SAXOutputter does not
     *         recognize the feature name.
     * @throws SAXNotSupportedException  when SAXOutputter recognizes
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
                if (VALIDATION_SAX_FEATURE.equals(name)) {
                    // Report DTD events.
                    return (this.reportDtdEvents);
                }
                else {
                    // Not a supported feature.
                    throw new SAXNotRecognizedException(name);
                }
            }
        }
    }

    /**
     * This will set the value of a SAX property.
     * This method is also the standard mechanism for setting extended
     * handlers.
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
     * @throws SAXNotRecognizedException when SAXOutputter does not recognize
     *         the property name.
     * @throws SAXNotSupportedException  when SAXOutputter recognizes the
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
     * This will look up the value of a SAX property.
     *
     * @param name <code>String</code> the property name, which is a
     *             fully-qualified URI.
     * @return <code>Object</code> the current value of the property.
     *
     * @throws SAXNotRecognizedException when SAXOutputter does not recognize
     *         the property name.
     * @throws SAXNotSupportedException  when SAXOutputter recognizes the
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
     * This will output the <code>JDOM Document</code>, firing off the
     * SAX events that have been registered.
     *
     * @param document <code>JDOM Document</code> to output.
     *
     * @throws JDOMException if any error occurred.
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
        if (this.reportDtdEvents) {
           dtdEvents(document);
        }

        // Handle root element, as well as any root level
        // processing instructions and comments
        Iterator i = document.getContent().iterator();
        while (i.hasNext()) {
            Object obj = i.next();

            // update locator
            locator.setNode(obj);

            if (obj instanceof Element) {
                // process root element and its content
                element(document.getRootElement(), new NamespaceStack());
            }
            else if (obj instanceof ProcessingInstruction) {
                // contentHandler.processingInstruction()
                processingInstruction((ProcessingInstruction) obj);
            }
            else if (obj instanceof Comment) {
                // lexicalHandler.comment()
                comment(((Comment) obj).getText());
            }
        }

        // contentHandler.endDocument()
        endDocument();
    }

    /**
     * This will output a list of JDOM nodes as a document, firing
     * off the SAX events that have been registered.
     * <p>
     * <strong>Warning</strong>: This method may output ill-formed XML
     * documents if the list contains top-level objects that are not
     * legal at the document level (e.g. Text or CDATA nodes, multiple
     * Element nodes, etc.).  Thus, it should only be used to output
     * document portions towards ContentHandlers capable of accepting
     * such ill-formed documents (such as XSLT processors).</p>
     *
     * @param nodes <code>List</code> of JDOM nodes to output.
     *
     * @throws JDOMException if any error occurred.
     *
     * @see #output(org.jdom.Document)
     */
    public void output(List nodes) throws JDOMException {
        if ((nodes == null) || (nodes.size() == 0)) {
            return;
        }

        // contentHandler.setDocumentLocator()
        documentLocator(null);

        // contentHandler.startDocument()
        startDocument();

        // Process node list.
        elementContent(nodes, new NamespaceStack());

        // contentHandler.endDocument()
        endDocument();
    }

    /**
     * This will output a single JDOM element as a document, firing
     * off the SAX events that have been registered.
     *
     * @param node the <code>Element</code> node to output.
     *
     * @throws JDOMException if any error occurred.
     */
    public void output(Element node) throws JDOMException {
        if (node == null) {
            return;
        }

        // contentHandler.setDocumentLocator()
        documentLocator(null);

        // contentHandler.startDocument()
        startDocument();

        // Output node.
        elementContent(node, new NamespaceStack());

        // contentHandler.endDocument()
        endDocument();
    }

    /**
     * This will output a list of JDOM nodes as a fragment of an XML
     * document, firing off the SAX events that have been registered.
     * <p>
     * <strong>Warning</strong>: This method does not call the
     * {@link ContentHandler#setDocumentLocator},
     * {@link ContentHandler#startDocument} and
     * {@link ContentHandler#endDocument} callbacks on the
     * {@link #setContentHandler ContentHandler}.  The user shall
     * invoke these methods directly prior/after outputting the
     * document fragments.</p>
     *
     * @param nodes <code>List</code> of JDOM nodes to output.
     *
     * @throws JDOMException if any error occurred.
     *
     * @see #outputFragment(org.jdom.Content)
     */
    public void outputFragment(List nodes) throws JDOMException {
        if ((nodes == null) || (nodes.size() == 0)) {
            return;
        }

        // Output node list as a document fragment.
        elementContent(nodes, new NamespaceStack());
    }

    /**
     * This will output a single JDOM nodes as a fragment of an XML
     * document, firing off the SAX events that have been registered.
     * <p>
     * <strong>Warning</strong>: This method does not call the
     * {@link ContentHandler#setDocumentLocator},
     * {@link ContentHandler#startDocument} and
     * {@link ContentHandler#endDocument} callbacks on the
     * {@link #setContentHandler ContentHandler}.  The user shall
     * invoke these methods directly prior/after outputting the
     * document fragments.</p>
     *
     * @param node the <code>Content</code> node to output.
     *
     * @throws JDOMException if any error occurred.
     *
     * @see #outputFragment(java.util.List)
     */
    public void outputFragment(Content node) throws JDOMException {
        if (node == null) {
            return;
        }

        // Output single node as a document fragment.
        elementContent(node, new NamespaceStack());
    }

    /**
     * This parses a DTD declaration to fire the related events towards
     * the registered handlers.
     *
     * @param document <code>JDOM Document</code> the DocType is to
     *                 process.
     */
    private void dtdEvents(Document document) throws JDOMException {
        DocType docType = document.getDocType();

        // Fire DTD-related events only if handlers have been registered.
        if ((docType != null) &&
            ((dtdHandler != null) || (declHandler != null))) {

            // Build a dummy XML document that only references the DTD...
            String dtdDoc = new XMLOutputter().outputString(docType);

            try {
                // And parse it to fire DTD events.
                createDTDParser().parse(new InputSource(
                                                new StringReader(dtdDoc)));

                // We should never reach this point as the document is
                // ill-formed; it does not have any root element.
            }
            catch (SAXParseException e) {
                // Expected exception: There's no root element in document.
            }
            catch (SAXException e) {
                throw new JDOMException("DTD parsing error", e);
            }
            catch (IOException e) {
                throw new JDOMException("DTD parsing error", e);
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
        locator = new JDOMLocator();
        String publicID = null;
        String systemID = null;

        if (document != null) {
            DocType docType = document.getDocType();
            if (docType != null) {
                publicID = docType.getPublicID();
                systemID = docType.getSystemID();
            }
        }
        locator.setPublicId(publicID);
        locator.setSystemId(systemID);
        locator.setLineNumber(-1);
        locator.setColumnNumber(-1);

        contentHandler.setDocumentLocator(locator);
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

            // reset locator
            locator = null;
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
        elementContent(element.getContent(), namespaces);

        // update locator
        if (locator != null) {
          locator.setNode(element);
        }

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
        if (ns != Namespace.XML_NAMESPACE) {
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
        
        // Fire any namespace on Attributes that were not explicity added as additionals.
        List attributes = element.getAttributes();
        if (attributes != null) {
            Iterator itr = attributes.iterator();
            while (itr.hasNext()) {
                Attribute att = (Attribute)itr.next();
                ns = att.getNamespace();
                if (ns == Namespace.NO_NAMESPACE) {
                	// Issue #60
                	// no-prefix attributes are always in the NO_NAMESPACE
                	// namespace. This prefix mapping is implied for Attributes.
                	continue;
                }
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
     * @param nsAtts <code>List</code> of namespaces to declare with
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
     * @param content element content as a <code>List</code> of nodes.
     * @param namespaces <code>List</code> stack of Namespaces in scope.
     */
    private void elementContent(List content, NamespaceStack namespaces)
                      throws JDOMException {
        for (Iterator i=content.iterator(); i.hasNext(); ) {
            Object obj = i.next();

            if (obj instanceof Content) {
                this.elementContent((Content)obj, namespaces);
            }
            else {
                // Not a valid element child. This could happen with
                // application-provided lists which may contain non
                // JDOM objects.
                handleError(new JDOMException(
                                        "Invalid element content: " + obj));
            }
        }
    }

    /**
     * <p>
     * This will invoke the callbacks for the content of an element.
     * </p>
     *
     * @param node a <code>Content</code> node.
     * @param namespaces <code>List</code> stack of Namespaces in scope.
     */
    private void elementContent(Content node, NamespaceStack namespaces)
                      throws JDOMException {
        // update locator
        if (locator != null) {
          locator.setNode(node);
        }

        if (node instanceof Element) {
            element((Element) node, namespaces);
        }
        else if (node instanceof CDATA) {
            cdata(((CDATA) node).getText());
        }
        else if (node instanceof Text) {
            // contentHandler.characters()
            characters(((Text) node).getText());
        }
        else if (node instanceof ProcessingInstruction) {
            // contentHandler.processingInstruction()
            processingInstruction((ProcessingInstruction) node);
        }
        else if (node instanceof Comment) {
            // lexicalHandler.comment()
            comment(((Comment) node).getText());
        }
        else if (node instanceof EntityRef) {
            // contentHandler.skippedEntity()
            entityRef((EntityRef) node);
        }
        else {
            // Not a valid element child. This could happen with
            // application-provided lists which may contain non
            // JDOM objects.
            handleError(new JDOMException("Invalid element content: " + node));
        }
    }

    /**
     * <p>
     * This will be called for each chunk of CDATA section encountered.
     * </p>
     *
     * @param cdataText all text in the CDATA section, including whitespace.
     */
    private void cdata(String cdataText) throws JDOMException {
        try {
            if (lexicalHandler != null) {
                lexicalHandler.startCDATA();
                characters(cdataText);
                lexicalHandler.endCDATA();
            }
            else {
                characters(cdataText);
            }
        }
        catch (SAXException se) {
            throw new JDOMException("Exception in CDATA", se);
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
     * This will invoke the <code>ContentHandler.skippedEntity</code>
     * callback when an entity reference is encountered.
     * </p>
     *
     * @param entity <code>EntityRef</code>.
     */
    private void entityRef(EntityRef entity) throws JDOMException {
        if (entity != null) {
            try {
                // No need to worry about appending a '%' character as
                // we do not support parameter entities
                contentHandler.skippedEntity(entity.getName());
            }
            catch (SAXException se) {
                throw new JDOMException("Exception in entityRef", se);
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

            String prefix = ns.getPrefix();
            if (prefix.equals("")) {
                atts.addAttribute("",                        // namespace
                                  "",                        // local name
                                  "xmlns",                   // qualified name
                                  "CDATA",                   // type
                                  ns.getURI());              // value
            }
            else {
                atts.addAttribute("",                        // namespace
                                  "",                        // local name
                                  "xmlns:" + ns.getPrefix(), // qualified name
                                  "CDATA",                   // type
                                  ns.getURI());              // value
            }
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
    private static String getAttributeTypeName(int type) {
        if ((type < 0) || (type >= attrTypeToNameMap.length)) {
            type = Attribute.UNDECLARED_TYPE;
        }
        return attrTypeToNameMap[type];
    }

    /**
     * <p>
     * Notifies the registered {@link ErrorHandler SAX error handler}
     * (if any) of an input processing error. The error handler can
     * choose to absorb the error and let the processing continue.
     * </p>
     *
     * @param exception <code>JDOMException</code> containing the
     *                  error information; will be wrapped in a
     *                  {@link SAXParseException} when reported to
     *                  the SAX error handler.
     *
     * @throws JDOMException if no error handler has been registered
     *                       or if the error handler fired a
     *                       {@link SAXException}.
     */
    private void handleError(JDOMException exception) throws JDOMException {
        if (errorHandler != null) {
            try {
                errorHandler.error(new SAXParseException(
                                exception.getMessage(), null, exception));
            }
            catch (SAXException se) {
               if (se.getException() instanceof JDOMException) {
                   throw (JDOMException)(se.getException());
               }
               else {
                   throw new JDOMException(se.getMessage(), se);
               }
            }
        }
        else {
            throw exception;
        }
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

        // Absorb errors as much as possible, per Laurent
        parser.setErrorHandler(new DefaultHandler());

        return parser;
    }

    /**
     * Returns a JDOMLocator object referencing the node currently
     * being processed by this outputter.  The returned object is a
     * snapshot of the  location information and can thus safely be
     * memorized for later use.
     * <p>
     * This method allows direct access to the location information
     * maintained by SAXOutputter without requiring to implement
     * <code>XMLFilter</code>. (In SAX, locators are only available
     * though the <code>ContentHandler</code> interface).</p>
     * <p>
     * Note that location information is only available while
     * SAXOutputter is outputting nodes. Hence this method should
     * only be used by objects taking part in the output processing
     * such as <code>ErrorHandler</code>s.
     *
     * @return a JDOMLocator object referencing the node currently
     *         being processed or <code>null</code> if no output
     *         operation is being performed.
     */
    public JDOMLocator getLocator() {
        return (locator != null)? new JDOMLocator(locator): null;
    }
}
