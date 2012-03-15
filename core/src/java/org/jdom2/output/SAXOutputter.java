/*--

 Copyright (C) 2000-2012 Jason Hunter & Brett McLaughlin.
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

package org.jdom2.output;

import static org.jdom2.JDOMConstants.*;

import java.util.List;

import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.Content;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.EntityRef;
import org.jdom2.JDOMException;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;
import org.jdom2.output.support.AbstractSAXOutputProcessor;
import org.jdom2.output.support.SAXOutputProcessor;
import org.jdom2.output.support.SAXTarget;

/**
 * Outputs a JDOM document as a stream of SAX2 events.
 * <p>
 * Most ContentHandler callbacks are supported. BOTH
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
 * point the document exists in memory and is known to have no errors.
 * </p>
 * 
 * @author Brett McLaughlin
 * @author Jason Hunter
 * @author Fred Trimble
 * @author Bradley S. Huffman
 */
public class SAXOutputter {

	private static final class DefaultSAXOutputProcessor extends
			AbstractSAXOutputProcessor {
		// nothing.
	}

	private static final SAXOutputProcessor DEFAULT_PROCESSOR = new DefaultSAXOutputProcessor();

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
	 * @see <a href="http://www.megginson.com/SAX/Java/namespaces.html"> SAX
	 *      namespace specifications</a>
	 */
	private boolean declareNamespaces = false;

	/**
	 * Whether to report DTD events to DeclHandlers and LexicalHandlers.
	 * Defaults to <code>true</code>.
	 */
	private boolean reportDtdEvents = true;

	/**
	 * A SAXOutputProcessor
	 */
	private SAXOutputProcessor processor = DEFAULT_PROCESSOR;

	/**
	 * The Format to use for output.
	 */
	private Format format = Format.getRawFormat();

	/**
	 * This will create a <code>SAXOutputter</code> without any registered
	 * handler. The application is then responsible for registering them using
	 * the <code>setXxxHandler()</code> methods.
	 */
	public SAXOutputter() {
	}

	/**
	 * This will create a <code>SAXOutputter</code> with the specified
	 * <code>ContentHandler</code>.
	 * 
	 * @param contentHandler
	 *        contains <code>ContentHandler</code> callback methods
	 */
	public SAXOutputter(ContentHandler contentHandler) {
		this(contentHandler, null, null, null, null);
	}

	/**
	 * This will create a <code>SAXOutputter</code> with the specified SAX2
	 * handlers. At this time, only <code>ContentHandler</code> and
	 * <code>EntityResolver</code> are supported.
	 * 
	 * @param contentHandler
	 *        contains <code>ContentHandler</code> callback methods
	 * @param errorHandler
	 *        contains <code>ErrorHandler</code> callback methods
	 * @param dtdHandler
	 *        contains <code>DTDHandler</code> callback methods
	 * @param entityResolver
	 *        contains <code>EntityResolver</code> callback methods
	 */
	public SAXOutputter(ContentHandler contentHandler,
			ErrorHandler errorHandler, DTDHandler dtdHandler,
			EntityResolver entityResolver) {
		this(contentHandler, errorHandler, dtdHandler, entityResolver, null);
	}

	/**
	 * This will create a <code>SAXOutputter</code> with the specified SAX2
	 * handlers. At this time, only <code>ContentHandler</code> and
	 * <code>EntityResolver</code> are supported.
	 * 
	 * @param contentHandler
	 *        contains <code>ContentHandler</code> callback methods
	 * @param errorHandler
	 *        contains <code>ErrorHandler</code> callback methods
	 * @param dtdHandler
	 *        contains <code>DTDHandler</code> callback methods
	 * @param entityResolver
	 *        contains <code>EntityResolver</code> callback methods
	 * @param lexicalHandler
	 *        contains <code>LexicalHandler</code> callbacks.
	 */
	public SAXOutputter(ContentHandler contentHandler,
			ErrorHandler errorHandler, DTDHandler dtdHandler,
			EntityResolver entityResolver, LexicalHandler lexicalHandler) {
		this.contentHandler = contentHandler;
		this.errorHandler = errorHandler;
		this.dtdHandler = dtdHandler;
		this.entityResolver = entityResolver;
		this.lexicalHandler = lexicalHandler;
	}

	/**
	 * This will create a <code>SAXOutputter</code> with the specified SAX2
	 * handlers. At this time, only <code>ContentHandler</code> and
	 * <code>EntityResolver</code> are supported.
	 * 
	 * @param processor
	 *        the {@link SAXOutputProcessor} to use for output.
	 * @param format
	 *        the {@link Format} to use for output.
	 * @param contentHandler
	 *        contains <code>ContentHandler</code> callback methods
	 * @param errorHandler
	 *        contains <code>ErrorHandler</code> callback methods
	 * @param dtdHandler
	 *        contains <code>DTDHandler</code> callback methods
	 * @param entityResolver
	 *        contains <code>EntityResolver</code> callback methods
	 * @param lexicalHandler
	 *        contains <code>LexicalHandler</code> callbacks.
	 */
	public SAXOutputter(SAXOutputProcessor processor, Format format,
			ContentHandler contentHandler, ErrorHandler errorHandler,
			DTDHandler dtdHandler, EntityResolver entityResolver,
			LexicalHandler lexicalHandler) {
		this.processor = processor == null ? DEFAULT_PROCESSOR : processor;
		this.format = format == null ? Format.getRawFormat() : format;
		this.contentHandler = contentHandler;
		this.errorHandler = errorHandler;
		this.dtdHandler = dtdHandler;
		this.entityResolver = entityResolver;
		this.lexicalHandler = lexicalHandler;
	}

	/**
	 * This will set the <code>ContentHandler</code>.
	 * 
	 * @param contentHandler
	 *        contains <code>ContentHandler</code> callback methods.
	 */
	public void setContentHandler(ContentHandler contentHandler) {
		this.contentHandler = contentHandler;
	}

	/**
	 * Returns the registered <code>ContentHandler</code>.
	 * 
	 * @return the current <code>ContentHandler</code> or <code>null</code> if
	 *         none was registered.
	 */
	public ContentHandler getContentHandler() {
		return this.contentHandler;
	}

	/**
	 * This will set the <code>ErrorHandler</code>.
	 * 
	 * @param errorHandler
	 *        contains <code>ErrorHandler</code> callback methods.
	 */
	public void setErrorHandler(ErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	/**
	 * Return the registered <code>ErrorHandler</code>.
	 * 
	 * @return the current <code>ErrorHandler</code> or <code>null</code> if
	 *         none was registered.
	 */
	public ErrorHandler getErrorHandler() {
		return this.errorHandler;
	}

	/**
	 * This will set the <code>DTDHandler</code>.
	 * 
	 * @param dtdHandler
	 *        contains <code>DTDHandler</code> callback methods.
	 */
	public void setDTDHandler(DTDHandler dtdHandler) {
		this.dtdHandler = dtdHandler;
	}

	/**
	 * Return the registered <code>DTDHandler</code>.
	 * 
	 * @return the current <code>DTDHandler</code> or <code>null</code> if none
	 *         was registered.
	 */
	public DTDHandler getDTDHandler() {
		return this.dtdHandler;
	}

	/**
	 * This will set the <code>EntityResolver</code>.
	 * 
	 * @param entityResolver
	 *        contains EntityResolver callback methods.
	 */
	public void setEntityResolver(EntityResolver entityResolver) {
		this.entityResolver = entityResolver;
	}

	/**
	 * Return the registered <code>EntityResolver</code>.
	 * 
	 * @return the current <code>EntityResolver</code> or <code>null</code> if
	 *         none was registered.
	 */
	public EntityResolver getEntityResolver() {
		return this.entityResolver;
	}

	/**
	 * This will set the <code>LexicalHandler</code>.
	 * 
	 * @param lexicalHandler
	 *        contains lexical callback methods.
	 */
	public void setLexicalHandler(LexicalHandler lexicalHandler) {
		this.lexicalHandler = lexicalHandler;
	}

	/**
	 * Return the registered <code>LexicalHandler</code>.
	 * 
	 * @return the current <code>LexicalHandler</code> or <code>null</code> if
	 *         none was registered.
	 */
	public LexicalHandler getLexicalHandler() {
		return this.lexicalHandler;
	}

	/**
	 * This will set the <code>DeclHandler</code>.
	 * 
	 * @param declHandler
	 *        contains declaration callback methods.
	 */
	public void setDeclHandler(DeclHandler declHandler) {
		this.declHandler = declHandler;
	}

	/**
	 * Return the registered <code>DeclHandler</code>.
	 * 
	 * @return the current <code>DeclHandler</code> or <code>null</code> if none
	 *         was registered.
	 */
	public DeclHandler getDeclHandler() {
		return this.declHandler;
	}

	/**
	 * Returns whether attribute namespace declarations shall be reported as
	 * "xmlns" attributes.
	 * 
	 * @return whether attribute namespace declarations shall be reported as
	 *         "xmlns" attributes.
	 */
	public boolean getReportNamespaceDeclarations() {
		return declareNamespaces;
	}

	/**
	 * This will define whether attribute namespace declarations shall be
	 * reported as "xmlns" attributes. This flag defaults to <code>false</code>
	 * and behaves as the "namespace-prefixes" SAX core feature.
	 * 
	 * @param declareNamespaces
	 *        whether attribute namespace declarations shall be reported as
	 *        "xmlns" attributes.
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
	 * This will define whether to report DTD events to SAX DeclHandlers and
	 * LexicalHandlers if these handlers are registered and the document to
	 * output includes a DocType declaration.
	 * 
	 * @param reportDtdEvents
	 *        whether to notify DTD events.
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
	 * <dt><code>http://xml.org/sax/features/namespaces</code></dt>
	 * <dd><strong>description:</strong> <code>true</code> indicates namespace
	 * URIs and unprefixed local names for element and attribute names will be
	 * available</dd>
	 * <dd><strong>access:</strong> read/write, but always <code>true</code>!</dd>
	 * <dt><code>http://xml.org/sax/features/namespace-prefixes</code></dt>
	 * <dd><strong>description:</strong> <code>true</code> indicates XML 1.0
	 * names (with prefixes) and attributes (including xmlns* attributes) will
	 * be available</dd>
	 * <dd><strong>access:</strong> read/write</dd>
	 * <dt><code>http://xml.org/sax/features/validation</code></dt>
	 * <dd><strong>description:</strong> controls whether SAXOutputter is
	 * reporting DTD-related events; if <code>true</code>, the DocType internal
	 * subset will be parsed to fire DTD events</dd>
	 * <dd><strong>access:</strong> read/write, defaults to <code>true</code></dd>
	 * </dl>
	 * </p>
	 * 
	 * @param name
	 *        <code>String</code> the feature name, which is a fully-qualified
	 *        URI.
	 * @param value
	 *        <code>boolean</code> the requested state of the feature (true or
	 *        false).
	 * @throws SAXNotRecognizedException
	 *         when SAXOutputter does not recognize the feature name.
	 * @throws SAXNotSupportedException
	 *         when SAXOutputter recognizes the feature name but cannot set the
	 *         requested value.
	 */
	public void setFeature(String name, boolean value)
			throws SAXNotRecognizedException, SAXNotSupportedException {
		if (SAX_FEATURE_NAMESPACE_PREFIXES.equals(name)) {
			// Namespace prefix declarations.
			this.setReportNamespaceDeclarations(value);
		} else {
			if (SAX_FEATURE_NAMESPACES.equals(name)) {
				if (value != true) {
					// Namespaces feature always supported by SAXOutputter.
					throw new SAXNotSupportedException(name);
				}
				// Else: true is OK!
			} else {
				if (SAX_FEATURE_VALIDATION.equals(name)) {
					// Report DTD events.
					this.setReportDTDEvents(value);
				} else {
					// Not a supported feature.
					throw new SAXNotRecognizedException(name);
				}
			}
		}
	}

	/**
	 * This will look up the value of a SAX feature.
	 * 
	 * @param name
	 *        <code>String</code> the feature name, which is a fully-qualified
	 *        URI.
	 * @return <code>boolean</code> the current state of the feature (true or
	 *         false).
	 * @throws SAXNotRecognizedException
	 *         when SAXOutputter does not recognize the feature name.
	 * @throws SAXNotSupportedException
	 *         when SAXOutputter recognizes the feature name but determine its
	 *         value at this time.
	 */
	public boolean getFeature(String name) throws SAXNotRecognizedException,
			SAXNotSupportedException {
		if (SAX_FEATURE_NAMESPACE_PREFIXES.equals(name)) {
			// Namespace prefix declarations.
			return (this.declareNamespaces);
		}
		if (SAX_FEATURE_NAMESPACES.equals(name)) {
			// Namespaces feature always supported by SAXOutputter.
			return (true);
		}
		if (SAX_FEATURE_VALIDATION.equals(name)) {
			// Report DTD events.
			return (this.reportDtdEvents);
		}
		// Not a supported feature.
		throw new SAXNotRecognizedException(name);
	}

	/**
	 * This will set the value of a SAX property. This method is also the
	 * standard mechanism for setting extended handlers.
	 * <p>
	 * SAXOutputter currently supports the following SAX properties:
	 * <dl>
	 * <dt><code>http://xml.org/sax/properties/lexical-handler</code></dt>
	 * <dd><strong>data type:</strong>
	 * <code>org.xml.sax.ext.LexicalHandler</code></dd>
	 * <dd><strong>description:</strong> An optional extension handler for
	 * lexical events like comments.</dd>
	 * <dd><strong>access:</strong> read/write</dd>
	 * <dt><code>http://xml.org/sax/properties/declaration-handler</code></dt>
	 * <dd><strong>data type:</strong> <code>org.xml.sax.ext.DeclHandler</code></dd>
	 * <dd><strong>description:</strong> An optional extension handler for
	 * DTD-related events other than notations and unparsed entities.</dd>
	 * <dd><strong>access:</strong> read/write</dd>
	 * </dl>
	 * </p>
	 * 
	 * @param name
	 *        <code>String</code> the property name, which is a fully-qualified
	 *        URI.
	 * @param value
	 *        <code>Object</code> the requested value for the property.
	 * @throws SAXNotRecognizedException
	 *         when SAXOutputter does not recognize the property name.
	 * @throws SAXNotSupportedException
	 *         when SAXOutputter recognizes the property name but cannot set the
	 *         requested value.
	 */
	public void setProperty(String name, Object value)
			throws SAXNotRecognizedException, SAXNotSupportedException {
		if ((SAX_PROPERTY_LEXICAL_HANDLER.equals(name))
				|| (SAX_PROPERTY_LEXICAL_HANDLER_ALT.equals(name))) {
			this.setLexicalHandler((LexicalHandler) value);
		} else {
			if ((SAX_PROPERTY_DECLARATION_HANDLER.equals(name))
					|| (SAX_PROPERTY_DECLARATION_HANDLER_ALT.equals(name))) {
				this.setDeclHandler((DeclHandler) value);
			} else {
				throw new SAXNotRecognizedException(name);
			}
		}
	}

	/**
	 * This will look up the value of a SAX property.
	 * 
	 * @param name
	 *        <code>String</code> the property name, which is a fully-qualified
	 *        URI.
	 * @return <code>Object</code> the current value of the property.
	 * @throws SAXNotRecognizedException
	 *         when SAXOutputter does not recognize the property name.
	 * @throws SAXNotSupportedException
	 *         when SAXOutputter recognizes the property name but cannot
	 *         determine its value at this time.
	 */
	public Object getProperty(String name) throws SAXNotRecognizedException,
			SAXNotSupportedException {
		if ((SAX_PROPERTY_LEXICAL_HANDLER.equals(name))
				|| (SAX_PROPERTY_LEXICAL_HANDLER_ALT.equals(name))) {
			return this.getLexicalHandler();
		}
		if ((SAX_PROPERTY_DECLARATION_HANDLER.equals(name))
				|| (SAX_PROPERTY_DECLARATION_HANDLER_ALT.equals(name))) {
			return this.getDeclHandler();
		}
		throw new SAXNotRecognizedException(name);
	}

	/**
	 * Get the current {@link SAXOutputProcessor} being used for output.
	 * 
	 * @return The current SAXOutputProcessor
	 */
	public SAXOutputProcessor getSAXOutputProcessor() {
		return processor;
	}

	/**
	 * Set the current {@link SAXOutputProcessor} to be used for output.
	 * 
	 * @param processor
	 *        the new SAXOutputProcessor
	 */
	public void setSAXOutputProcessor(SAXOutputProcessor processor) {
		this.processor = processor == null ? DEFAULT_PROCESSOR : processor;
	}

	/**
	 * Get the current {@link Format} being used for output
	 * 
	 * @return the current Format
	 */
	public Format getFormat() {
		return format;
	}

	/**
	 * Set the current {@link Format} to be used for output.
	 * 
	 * @param format
	 *        the new Format
	 */
	public void setFormat(Format format) {
		this.format = format == null ? Format.getRawFormat() : format;
	}

	private final SAXTarget buildTarget(Document doc) {
		String publicID = null;
		String systemID = null;
		if (doc != null) {
			DocType dt = doc.getDocType();
			if (dt != null) {
				publicID = dt.getPublicID();
				systemID = dt.getSystemID();
			}
		}
		return new SAXTarget(contentHandler, errorHandler, dtdHandler,
				entityResolver, lexicalHandler, declHandler, declareNamespaces,
				reportDtdEvents, publicID, systemID);
	}

	/**
	 * This will output the <code>JDOM Document</code>, firing off the SAX
	 * events that have been registered.
	 * 
	 * @param document
	 *        <code>JDOM Document</code> to output.
	 * @throws JDOMException
	 *         if any error occurred.
	 */
	public void output(Document document) throws JDOMException {
		processor.process(buildTarget(document), format, document);
	}

	/**
	 * This will output a list of JDOM nodes as a document, firing off the SAX
	 * events that have been registered.
	 * <p>
	 * <strong>Warning</strong>: This method may output ill-formed XML documents
	 * if the list contains top-level objects that are not legal at the document
	 * level (e.g. Text or CDATA nodes, multiple Element nodes, etc.). Thus, it
	 * should only be used to output document portions towards ContentHandlers
	 * capable of accepting such ill-formed documents (such as XSLT processors).
	 * </p>
	 * 
	 * @param nodes
	 *        <code>List</code> of JDOM nodes to output.
	 * @throws JDOMException
	 *         if any error occurred.
	 * @see #output(org.jdom2.Document)
	 */
	public void output(List<? extends Content> nodes) throws JDOMException {
		processor.processAsDocument(buildTarget(null), format, nodes);
	}

	/**
	 * This will output a single JDOM element as a document, firing off the SAX
	 * events that have been registered.
	 * 
	 * @param node
	 *        the <code>Element</code> node to output.
	 * @throws JDOMException
	 *         if any error occurred.
	 */
	public void output(Element node) throws JDOMException {
		processor.processAsDocument(buildTarget(null), format, node);
	}

	/**
	 * This will output a list of JDOM nodes as a fragment of an XML document,
	 * firing off the SAX events that have been registered.
	 * <p>
	 * <strong>Warning</strong>: This method does not call the
	 * {@link ContentHandler#setDocumentLocator},
	 * {@link ContentHandler#startDocument} and
	 * {@link ContentHandler#endDocument} callbacks on the
	 * {@link #setContentHandler ContentHandler}. The user shall invoke these
	 * methods directly prior/after outputting the document fragments.
	 * </p>
	 * 
	 * @param nodes
	 *        <code>List</code> of JDOM nodes to output.
	 * @throws JDOMException
	 *         if any error occurred.
	 * @see #outputFragment(org.jdom2.Content)
	 */
	public void outputFragment(List<? extends Content> nodes)
			throws JDOMException {
		if (nodes == null) {
			return;
		}
		processor.process(buildTarget(null), format, nodes);
	}

	/**
	 * This will output a single JDOM nodes as a fragment of an XML document,
	 * firing off the SAX events that have been registered.
	 * <p>
	 * <strong>Warning</strong>: This method does not call the
	 * {@link ContentHandler#setDocumentLocator},
	 * {@link ContentHandler#startDocument} and
	 * {@link ContentHandler#endDocument} callbacks on the
	 * {@link #setContentHandler ContentHandler}. The user shall invoke these
	 * methods directly prior/after outputting the document fragments.
	 * </p>
	 * 
	 * @param node
	 *        the <code>Content</code> node to output.
	 * @throws JDOMException
	 *         if any error occurred.
	 * @see #outputFragment(java.util.List)
	 */
	public void outputFragment(Content node) throws JDOMException {
		if (node == null) {
			return;
		}

		SAXTarget out = buildTarget(null);

		switch (node.getCType()) {
			case CDATA:
				processor.process(out, format, (CDATA) node);
				break;
			case Comment:
				processor.process(out, format, (Comment) node);
				break;
			case Element:
				processor.process(out, format, (Element) node);
				break;
			case EntityRef:
				processor.process(out, format, (EntityRef) node);
				break;
			case ProcessingInstruction:
				processor.process(out, format, (ProcessingInstruction) node);
				break;
			case Text:
				processor.process(out, format, (Text) node);
				break;
			default:
				handleError(new JDOMException("Invalid element content: " + node));
		}

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
				throw new JDOMException(se.getMessage(), se);
			}
		}
		else {
			throw exception;
		}
	}
	

	/**
	 * Returns null.
	 * 
	 * @return null
	 * @deprecated there is no way to get a meaningful document Locator outside
	 *             of an active output process, and the contents of the locator
	 *             are meaningless outside of an active output process anyway.
	 */
	@Deprecated
	public JDOMLocator getLocator() {
		return null;
	}
}
