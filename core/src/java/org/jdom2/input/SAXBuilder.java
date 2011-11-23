/*--

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

package org.jdom2.input;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;

import org.jdom2.DefaultJDOMFactory;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.EntityRef;
import org.jdom2.JDOMException;
import org.jdom2.JDOMFactory;
import org.jdom2.input.sax.BuilderErrorHandler;
import org.jdom2.input.sax.XMLReaderJAXPSingletons;
import org.jdom2.input.sax.XMLReaderJDOMFactory;
import org.jdom2.input.sax.XMLReaderSAX2Factory;
import org.jdom2.input.sax.SAXBuilderEngine;
import org.jdom2.input.sax.SAXEngine;
import org.jdom2.input.sax.SAXHandler;
import org.jdom2.input.sax.SAXHandlerFactory;

/**
 * Builds a JDOM document from files, streams, readers, URLs, or a SAX {@link
 * org.xml.sax.InputSource} instance using a SAX parser. The builder uses a
 * third-party SAX parser (chosen by JAXP by default, or you can choose
 * manually) to handle the parsing duties and simply listens to the SAX events
 * to construct a document. Details which SAX does not provide, such as
 * whitespace outside the root element, are not represented in the JDOM
 * document. Information about SAX can be found at <a
 * href="http://www.saxproject.org">http://www.saxproject.org</a>.
 * <p>
 * For a description of how SAXBuilder is used, and how to customise the process
 * you should look at the {@link org.jdom2.input.sax} package documentation.
 * 
 * Known issues: Relative paths for a {@link DocType} or {@link EntityRef} may
 * be converted by the SAX parser into absolute paths.
 *
 * @see org.jdom2.input.sax
 * 
 * @author  Jason Hunter
 * @author  Brett McLaughlin
 * @author  Dan Schaffer
 * @author  Philip Nelson
 * @author  Alex Rosen
 * @author  Rolf Lear
 */
public class SAXBuilder implements SAXEngine {
	
	/**
	 * For performance reasons it helps to use 'final' instances of classes.
	 * This makes the SAXHandler class a 'final' class for all normal
	 * SAXBuilders. It adds no other functionality.
	 * 
	 * @author Rolf Lear
	 *
	 */
	private static final class DefaultSAXHandler extends SAXHandler {
		public DefaultSAXHandler(JDOMFactory factory) {
			super(factory);
		}
	}
	
	/**
	 * This SAXHandlerFactory instance provides default-configured SAXHandler
	 * instances for all non-custom situations.
	 * 
	 * @author Rolf Lear
	 *
	 */
	private static final class DefaultSAXHandlerFactory 
		implements SAXHandlerFactory {
		@Override
		public SAXHandler createSAXHandler(JDOMFactory factory) {
			return new DefaultSAXHandler(factory);
		}
	}
	
	private static final SAXHandlerFactory DEFAULTSAXHANDLERFAC =
			new DefaultSAXHandlerFactory();
	
	private static final JDOMFactory DEFAULTJDOMFAC = new DefaultJDOMFactory();

	/**
	 * The XMLReader pillar of SAXBuilder
	 */
	private XMLReaderJDOMFactory readerfac = null;
	
	/**
	 * The SAXHandler pillar of SAXBuilder
	 */
	private SAXHandlerFactory handlerfac = null;
	
	/** 
	 * The JDOMFactory pillar for creating new JDOM objects
	 */
	private JDOMFactory jdomfac = null;

	
	/** Whether expansion of entities should occur */
	private boolean expand = true;

	/** ErrorHandler class to use */
	private ErrorHandler saxErrorHandler = null;

	/** EntityResolver class to use */
	private EntityResolver saxEntityResolver = null;

	/** DTDHandler class to use */
	private DTDHandler saxDTDHandler = null;

	/** XMLFilter instance to use */
	private XMLFilter saxXMLFilter = null;

	/** Whether to ignore ignorable whitespace */
	private boolean ignoringWhite = false;

	/** Whether to ignore all whitespace content */
	private boolean ignoringBoundaryWhite = false;

	/** User-specified features to be set on the SAX parser */
	private HashMap<String,Boolean> features = new HashMap<String, Boolean>(5);

	/** User-specified properties to be set on the SAX parser */
	private HashMap<String,Object> properties = new HashMap<String, Object>(5);

	/**
	 * Whether parser reuse is allowed.
	 * <p>Default: <code>true</code></p>
	 */
	private boolean reuseParser = true;
	
	/** The current SAX parser, if parser reuse has been activated. */
	private SAXEngine engine = null;

	/**
	 * Creates a new JAXP-based SAXBuilder. The underlying parser will not
	 * validate.
	 */
	public SAXBuilder() {
		this(XMLReaderJAXPSingletons.NONVALIDATING);
	}

	/**
	 * Creates a new JAXP-based SAXBuilder. The underlying parser will validate
	 * (using DTD) or not according to the given parameter. If you want Schema
	 * validation then use SAXBuilder(JAXPXMLReaderSingleton.XSDVALIDATOR)
	 *
	 * @param validate <code>boolean</code> indicating if DTD
	 *                 validation should occur.
	 */
	public SAXBuilder(boolean validate) {
		this(validate 
				? XMLReaderJAXPSingletons.DTDVALIDATING 
						: XMLReaderJAXPSingletons.NONVALIDATING);
	}

	/**
	 * Creates a new SAXBuilder using the specified SAX parser.
	 * The underlying parser will not validate.
	 *
	 * @param saxDriverClass <code>String</code> name of SAX Driver
	 *                       to use for parsing.
	 */
	public SAXBuilder(String saxDriverClass) {
		this(saxDriverClass, false);
	}

	/**
	 * Creates a new SAXBuilder using the specified SAX parser.
	 * The underlying parser will validate or not
	 * according to the given parameter.
	 *
	 * @param saxDriverClass <code>String</code> name of SAX Driver
	 *                       to use for parsing.
	 * @param validate <code>boolean</code> indicating if
	 *                 validation should occur.
	 */
	public SAXBuilder(String saxDriverClass, boolean validate) {
		this(new XMLReaderSAX2Factory(validate, saxDriverClass));
	}

	/**
	 * Creates a new SAXBuilder. This is the base constructor for all other
	 * SAXBuilder constructors: they all find a way to create a
	 * JDOMXMLReaderFactory and then call this constructor with that factory.
	 * <p>
	 * @see XMLReaderJDOMFactory and its package documentation for details on
	 * creating and using JDOMXMLReaderFactories.
	 * @param readersouce the {@link XMLReaderJDOMFactory} that supplies
	 * 		XMLReaders. If the value is null then a Non-Validating JAXP-based
	 * 		SAX2.0 parser will be used.
	 */
	public SAXBuilder(XMLReaderJDOMFactory readersouce) {
		this(readersouce, null, null);
	}
	
	/**
	 * Creates a new SAXBuilder. This is the base constructor for all other
	 * SAXBuilder constructors: they all find a way to create a
	 * JDOMXMLReaderFactory and then call this constructor with that factory.
	 * <p>
	 * @see XMLReaderJDOMFactory and its package documentation for details on
	 * creating and using JDOMXMLReaderFactories.
	 *
	 * @param xmlreaderfactory a {@link XMLReaderJDOMFactory} that creates XMLReaders
	 * @param handlerfactory a {@link SAXHandlerFactory} that creates SAXHandlers
	 * @param jdomfactory a {@link JDOMFactory} that creates JDOM Content.
	 */
	public SAXBuilder(XMLReaderJDOMFactory xmlreaderfactory, SAXHandlerFactory handlerfactory, JDOMFactory jdomfactory) {
		this.readerfac = xmlreaderfactory == null
				? XMLReaderJAXPSingletons.NONVALIDATING
				: xmlreaderfactory;
		this.handlerfac = handlerfactory == null
				? DEFAULTSAXHANDLERFAC
				: handlerfactory;
		this.jdomfac = jdomfactory == null
				? DEFAULTJDOMFAC
				: jdomfactory;
	}

	/**
	 * Returns the driver class assigned in the constructor, or null if none.
	 *
	 * @return the driver class assigned in the constructor
	 */
	public String getDriverClass() {
		if (readerfac instanceof XMLReaderSAX2Factory) {
			return ((XMLReaderSAX2Factory)readerfac).getDriverClassName();
		}
		return null;
	}

	/**
	 * Returns the current {@link org.jdom2.JDOMFactory} in use.
	 * @return the factory in use
	 * @deprecated is replaced by {@link #getJDOMFactory()}
	 */
	@Deprecated
	public JDOMFactory getFactory() {
		return getJDOMFactory();
	}

	/**
	 * Returns the current {@link org.jdom2.JDOMFactory} in use.
	 * @return the factory in use
	 */
	@Override
	public JDOMFactory getJDOMFactory() {
		return jdomfac;
	}

	/**
	 * This sets a custom JDOMFactory for the builder.  Use this to build
	 * the tree with your own subclasses of the JDOM classes.
	 *
	 * @param factory <code>JDOMFactory</code> to use
	 * @deprecated use {@link #setJDOMFactory(JDOMFactory)}
	 */
	@Deprecated
	public void setFactory(JDOMFactory factory) {
		setJDOMFactory(factory);
	}
	
	/**
	 * This sets a custom JDOMFactory for the builder.  Use this to build
	 * the tree with your own subclasses of the JDOM classes.
	 *
	 * @param factory <code>JDOMFactory</code> to use
	 */
	public void setJDOMFactory(JDOMFactory factory) {
		this.jdomfac = factory;
		engine = null;
	}
	
	/**
	 * Get the current XMLReader factory.
	 * @return the current JDOMXMLReaderFactory
	 */
	public XMLReaderJDOMFactory getXMLReaderFactory() {
		return readerfac;
	}

	/**
	 * Set the current XMLReader factory.
	 * @param rfac the JDOMXMLReaderFactory to set.
	 */
	public void setXMLReaderFactory(XMLReaderJDOMFactory rfac) {
		readerfac = rfac == null
				? XMLReaderJAXPSingletons.NONVALIDATING
				: rfac;
		engine = null;
	}
	
	
	/**
	 * Get the SAXHandlerFactory used to supply SAXHandlers to this SAXBuilder.
	 * @return the current SAXHandlerFactory (never null).
	 */
	public SAXHandlerFactory getSAXHandlerFactory() {
		return handlerfac;
	}

	/**
	 * Set the SAXHandlerFactory to be used by this SAXBuilder.
	 * @param factory the required SAXHandlerFactory. Null input will request
	 * 		the default SAXHandlerFactory.
	 */
	public void setSAXHandlerFactory(SAXHandlerFactory factory) {
		this.handlerfac = factory == null ? DEFAULTSAXHANDLERFAC : factory;
		engine = null;
	}

	/**
	 * Returns whether validation is to be performed during the build.
	 *
	 * @return whether validation is to be performed during the build
	 * @deprecated in lieu of {@link #isValidating()}
	 */
	@Deprecated
	public boolean getValidation() {
		return isValidating();
	}

	/**
	 * Returns whether validation is to be performed during the build.
	 *
	 * @return whether validation is to be performed during the build
	 */
	@Override
	public boolean isValidating() {
		return readerfac.isValidating();
	}

	/**
	 * This sets validation for the builder.
	 * <p>
	 * <b>Do Not Use</b>
	 * <p>
	 * JDOM2 introduces the concept of XMLReader factories. The XMLReader is
	 * what determines the type of validation. A simple boolean is not enough
	 * to indicate what sort of validation is required. The
	 * {@link #setXMLReaderFactory(XMLReaderJDOMFactory)} method provides a
	 * means to me more specific about validation.
	 * <p>
	 * For backward compatibility this method has been retained, but its use is
	 * discouraged. It does make some logical choices though. The code is
	 * equivalent to:
	 * <p>
	 * <pre>setXMLReaderFactory(JAXPXMLReaderSingleton.DTDVALIDATING)</pre> for
	 * true, and
	 * <pre>setXMLReaderFactory(JAXPXMLReaderSingleton.NONVALIDATING)</pre> for
	 * false.
	 *
	 * @param validate <code>boolean</code> indicating whether validation
	 * should occur.
	 * @deprecated use {@link #setXMLReaderFactory(XMLReaderJDOMFactory)} 
	 */
	@Deprecated
	public void setValidation(boolean validate) {
		setXMLReaderFactory(validate 
				? XMLReaderJAXPSingletons.DTDVALIDATING 
				: XMLReaderJAXPSingletons.NONVALIDATING);
	}

	/**
	 * Returns the {@link ErrorHandler} assigned, or null if none.
	 * @return the ErrorHandler assigned, or null if none
	 */
	@Override
	public ErrorHandler getErrorHandler() {
		return saxErrorHandler;
	}

	/**
	 * This sets custom ErrorHandler for the <code>Builder</code>.
	 *
	 * @param errorHandler <code>ErrorHandler</code>
	 */
	public void setErrorHandler(ErrorHandler errorHandler) {
		saxErrorHandler = errorHandler;
		engine = null;
	}

	/**
	 * Returns the {@link EntityResolver} assigned, or null if none.
	 *
	 * @return the EntityResolver assigned
	 */
	@Override
	public EntityResolver getEntityResolver() {
		return saxEntityResolver;
	}

	/**
	 * This sets custom EntityResolver for the <code>Builder</code>.
	 *
	 * @param entityResolver <code>EntityResolver</code>
	 */
	public void setEntityResolver(EntityResolver entityResolver) {
		saxEntityResolver = entityResolver;
		engine = null;
	}

	/**
	 * Returns the {@link DTDHandler} assigned, or null if none.
	 *
	 * @return the DTDHandler assigned
	 */
	@Override
	public DTDHandler getDTDHandler() {
		return saxDTDHandler;
	}

	/**
	 * This sets custom DTDHandler for the <code>Builder</code>.
	 *
	 * @param dtdHandler <code>DTDHandler</code>
	 */
	public void setDTDHandler(DTDHandler dtdHandler) {
		saxDTDHandler = dtdHandler;
		engine = null;
	}

	/**
	 * Returns the {@link XMLFilter} used during parsing, or null if none.
	 *
	 * @return the XMLFilter used during parsing
	 */
	public XMLFilter getXMLFilter() {
		return saxXMLFilter;
	}

	/**
	 * This sets a custom {@link org.xml.sax.XMLFilter} for the builder.
	 *
	 * @param xmlFilter the filter to use
	 */
	public void setXMLFilter(XMLFilter xmlFilter) {
		saxXMLFilter = xmlFilter;
		engine = null;
	}

	/**
	 * Returns whether element content whitespace is to be ignored during the
	 * build.
	 *
	 * @return whether element content whitespace is to be ignored during the
	 * build
	 * @deprecated in lieu of {@link #isIgnoringElementContentWhitespace()}
	 */
	@Deprecated
	public boolean getIgnoringElementContentWhitespace() {
		return isIgnoringElementContentWhitespace();
	}

	/**
	 * Returns whether element content whitespace is to be ignored during the
	 * build.
	 *
	 * @return whether element content whitespace is to be ignored during the
	 * build
	 */
	@Override
	public boolean isIgnoringElementContentWhitespace() {
		return ignoringWhite;
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
		engine = null;
	}

	/**
	 * Returns whether or not the parser will elminate element content
	 * containing only whitespace.
	 *
	 * @return <code>boolean</code> - whether only whitespace content will
	 * be ignored during build.
	 *
	 * @see #setIgnoringBoundaryWhitespace
	 * @deprecated in lieu of {@link #isIgnoringBoundaryWhitespace()}
	 */
	@Deprecated
	public boolean getIgnoringBoundaryWhitespace() {
		return isIgnoringBoundaryWhitespace();
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
	@Override
	public boolean isIgnoringBoundaryWhitespace() {
		return ignoringBoundaryWhite;
	}
	

	/**
	 * Specifies whether or not the parser should elminate boundary whitespace,
	 * a term that indicates whitespace-only text between element tags.  This
	 * feature is a lot like {@link #setIgnoringElementContentWhitespace(boolean)}
	 * but this feature is more aggressive and doesn't require validation be
	 * turned on.  The {@link #setIgnoringElementContentWhitespace(boolean)}
	 * call impacts the SAX parse process while this method impacts the JDOM
	 * build process, so it can be beneficial to turn both on for efficiency.
	 * For implementation efficiency, this method actually removes all
	 * whitespace-only text() nodes.  That can, in some cases (like beteween an
	 * element tag and a comment), include whitespace that isn't just boundary
	 * whitespace.  The default is <code>false</code>.
	 *
	 * @param ignoringBoundaryWhite Whether to ignore whitespace-only text
	 *  nodes
	 */
	public void setIgnoringBoundaryWhitespace(boolean ignoringBoundaryWhite) {
		this.ignoringBoundaryWhite = ignoringBoundaryWhite;
		engine = null;
	}

	/**
	 * Returns whether or not entities are being expanded into normal text
	 * content.
	 *
	 * @return whether entities are being expanded
	 * @deprecated in lieu of {@link #isExpandEntities()}
	 */
	@Deprecated
	public boolean getExpandEntities() {
		return expand;
	}

	/**
	 * Returns whether or not entities are being expanded into normal text
	 * content.
	 *
	 * @return whether entities are being expanded
	 */
	@Override
	public boolean isExpandEntities() {
		return expand;
	}
	
	/**
	 * <p>
	 * This sets whether or not to expand entities for the builder.
	 * A true means to expand entities as normal content.  A false means to
	 * leave entities unexpanded as <code>EntityRef</code> objects.  The
	 * default is true.
	 * </p>
	 * <p>
	 * When this setting is false, the internal DTD subset is retained; when
	 * this setting is true, the internal DTD subset is not retained.
	 * </p>
	 * <p>
	 * Note that Xerces (at least up to 1.4.4) has a bug where entities
	 * in attribute values will be misreported if this flag is turned off,
	 * resulting in entities to appear within element content.  When turning
	 * entity expansion off either avoid entities in attribute values, or
	 * use another parser like Crimson.
	 * http://nagoya.apache.org/bugzilla/show_bug.cgi?id=6111
	 * </p>
	 *
	 * @param expand <code>boolean</code> indicating whether entity expansion
	 * should occur.
	 */
	public void setExpandEntities(boolean expand) {
		this.expand = expand;
		engine = null;
	}

	/**
	 * Returns whether the contained SAX parser instance is reused across
	 * multiple parses.  The default is true.
	 *
	 * @return whether the contained SAX parser instance is reused across
	 * multiple parses
	 */
	public boolean getReuseParser() {
		return reuseParser;
	}

	/**
	 * Specifies whether this builder shall reuse the same SAX parser
	 * when performing subsequent parses or allocate a new parser for
	 * each parse.  The default value of this setting is
	 * <code>true</code> (parser reuse).
	 * <p>
	 * <strong>Note</strong>: As SAX parser instances are not thread safe,
	 * the parser reuse feature should not be used with SAXBuilder instances
	 * shared among threads.</p>
	 *
	 * @param reuseParser Whether to reuse the SAX parser.
	 */
	public void setReuseParser(boolean reuseParser) {
		this.reuseParser = reuseParser;
		if (!reuseParser) {
			engine = null;
		}
	}

	/**
	 * Specifies whether this builder will do fast reconfiguration of the
	 * underlying SAX parser when reuseParser is true. This improves
	 * performance in cases where SAXBuilders are reused and lots of small
	 * documents are frequently parsed. This avoids attempting to set features
	 * on the SAX parser each time build() is called which result in
	 * SaxNotRecognizedExceptions. This should ONLY be set for builders where
	 * this specific case is an issue. The default value of this setting is
	 * <code>false</code> (no fast reconfiguration). If reuseParser is false,
	 * calling this has no effect.
	 *
	 * @param fastReconfigure Whether to do a fast reconfiguration of the parser
	 * @deprecated All reused Parsers are now fast-reconfigured. No need to set it.
	 */
	@Deprecated
	public void setFastReconfigure(boolean fastReconfigure) {
		// do nothing
	}

	/**
	 * This sets a feature on the SAX parser. See the SAX documentation for .
	 * more information.
	 * </p>
	 * <p>
	 * NOTE: SAXBuilder requires that some particular features of the SAX parser be
	 * set up in certain ways for it to work properly. The list of such features
	 * may change in the future. Therefore, the use of this method may cause
	 * parsing to break, and even if it doesn't break anything today it might
	 * break parsing in a future JDOM version, because what JDOM parsers require
	 * may change over time. Use with caution.
	 * </p>
	 *
	 * @param name The feature name, which is a fully-qualified URI.
	 * @param value The requested state of the feature (true or false).
	 */
	public void setFeature(String name, boolean value) {
		// Save the specified feature for later.
		features.put(name, value ? Boolean.TRUE : Boolean.FALSE);
		engine = null;
	}

	/**
	 * This sets a property on the SAX parser. See the SAX documentation for
	 * more information.
	 * <p>
	 * NOTE: SAXBuilder requires that some particular properties of the SAX parser be
	 * set up in certain ways for it to work properly. The list of such properties
	 * may change in the future. Therefore, the use of this method may cause
	 * parsing to break, and even if it doesn't break anything today it might
	 * break parsing in a future JDOM version, because what JDOM parsers require
	 * may change over time. Use with caution.
	 * </p>
	 *
	 * @param name The property name, which is a fully-qualified URI.
	 * @param value The requested value for the property.
	 */
	public void setProperty(String name, Object value) {
		// Save the specified property for later.
		properties.put(name, value);
		engine = null;
	}

	/**
	 * This method builds a SAXBuilderEngine that can be reused many times, but
	 * not concurrently.
	 * @return a {@link SAXBuilderEngine} representing the current state of the
	 *  	current SAXBuilder settings.
	 * @throws JDOMException if there is any problem initialising the engine.
	 */
	public SAXEngine buildEngine() throws JDOMException {

		// Create and configure the content handler.
		SAXHandler contentHandler = handlerfac.createSAXHandler(jdomfac);
		
		contentHandler.setExpandEntities(expand);
		contentHandler.setIgnoringElementContentWhitespace(ignoringWhite);
		contentHandler.setIgnoringBoundaryWhitespace(ignoringBoundaryWhite);

		XMLReader parser = createParser(contentHandler);
		boolean valid = readerfac.isValidating();

		return new SAXBuilderEngine(parser, contentHandler, valid);
	}
	
	/**
	 * Allow overriding classes access to the Parser before it is used in a
	 * SAXBuilderEngine.
	 * @param contentHandler The SAXHandler to set for the parser.
	 * @return THe configured parser.
	 * @throws JDOMException if there is a problem
	 */
	protected XMLReader createParser(SAXHandler contentHandler) throws JDOMException {
		XMLReader parser = readerfac.createXMLReader();

		// Install optional filter
		if (saxXMLFilter != null) {
			// Connect filter chain to parser
			XMLFilter root = saxXMLFilter;
			while (root.getParent() instanceof XMLFilter) {
				root = (XMLFilter)root.getParent();
			}
			root.setParent(parser);

			// Read from filter
			parser = saxXMLFilter;
		}

		// Configure parser
		configureParser(parser, contentHandler);
		return parser;
	}

	
	/**
	 * This method retrieves (or builds) a SAXBuilderEngine that represents the
	 * current SAXBuilder state.
	 * @return a {@link SAXBuilderEngine} representing the current state of the
	 *  	current SAXBuilder settings.
	 * @throws JDOMException if there is any problem initializing the engine.
	 */
	private SAXEngine getEngine() throws JDOMException {

		if (engine != null) {
			return engine;
		}

		engine =  buildEngine();
		return engine;
	}


	/**
	 * This configures the XMLReader to be used for reading the XML document.
	 * <p>
	 * The default implementation sets various options on the given XMLReader,
	 *  such as validation, DTD resolution, entity handlers, etc., according
	 *  to the options that were set (e.g. via <code>setEntityResolver</code>)
	 *  and set various SAX properties and features that are required for JDOM
	 *  internals. These features may change in future releases, so change this
	 *  behavior at your own risk.
	 * </p>
	 * @param parser the XMLReader to configure.
	 * @param contentHandler The SAXHandler to use for the XMLReader
	 * @throws JDOMException if configuration fails.
	 */
	protected void configureParser(XMLReader parser, SAXHandler contentHandler)
			throws JDOMException {

		// Setup SAX handlers.

		parser.setContentHandler(contentHandler);

		if (saxEntityResolver != null) {
			parser.setEntityResolver(saxEntityResolver);
		}

		if (saxDTDHandler != null) {
			parser.setDTDHandler(saxDTDHandler);
		} else {
			parser.setDTDHandler(contentHandler);
		}

		if (saxErrorHandler != null) {
			parser.setErrorHandler(saxErrorHandler);
		} else {
			parser.setErrorHandler(new BuilderErrorHandler());
		}

		boolean success = false;

		try {
			parser.setProperty("http://xml.org/sax/handlers/LexicalHandler",
					contentHandler);
			success = true;
		} catch (SAXNotSupportedException e) {
			// No lexical reporting available
		} catch (SAXNotRecognizedException e) {
			// No lexical reporting available
		}

		// Some parsers use alternate property for lexical handling (grr...)
		if (!success) {
			try {
				parser.setProperty("http://xml.org/sax/properties/lexical-handler",
						contentHandler);
				success = true;
			} catch (SAXNotSupportedException e) {
				// No lexical reporting available
			} catch (SAXNotRecognizedException e) {
				// No lexical reporting available
			}
		}

		// Set any user-specified features on the parser.
		for (Map.Entry<String,Boolean> me : features.entrySet()) {
			internalSetFeature(parser, me.getKey(), me.getValue().booleanValue(), me.getKey());
		}

		// Set any user-specified properties on the parser.
		for (Map.Entry<String,Object> me : properties.entrySet()) {
			internalSetProperty(parser, me.getKey(), me.getValue(), me.getKey());
		}

		// Set entity expansion
		// Note SAXHandler can work regardless of how this is set, but when
		// entity expansion it's worth it to try to tell the parser not to
		// even bother with external general entities.
		// Apparently no parsers yet support this feature.
		// XXX It might make sense to setEntityResolver() with a resolver
		// that simply ignores external general entities
		try {
			if (parser.getFeature("http://xml.org/sax/features/external-general-entities") != expand) {
				parser.setFeature("http://xml.org/sax/features/external-general-entities", expand);
			}
		}
		catch (SAXException e) { /* Ignore... */ }

		// Try setting the DeclHandler if entity expansion is off
		if (!expand) {
			try {
				parser.setProperty("http://xml.org/sax/properties/declaration-handler",
						contentHandler);
				success = true;
			} catch (SAXNotSupportedException e) {
				// No lexical reporting available
			} catch (SAXNotRecognizedException e) {
				// No lexical reporting available
			}
		}

	}


	/**
	 * Tries to set a feature on the parser. If the feature cannot be set,
	 * throws a JDOMException describing the problem.
	 */
	private void internalSetFeature(XMLReader parser, String feature,
			boolean value, String displayName) throws JDOMException {
		try {
			parser.setFeature(feature, value);
		} catch (SAXNotSupportedException e) {
			throw new JDOMException(
					displayName + " feature not supported for SAX driver " + parser.getClass().getName());
		} catch (SAXNotRecognizedException e) {
			throw new JDOMException(
					displayName + " feature not recognized for SAX driver " + parser.getClass().getName());
		}
	}

	/**
	 * <p>
	 * Tries to set a property on the parser. If the property cannot be set,
	 * throws a JDOMException describing the problem.
	 * </p>
	 */
	private void internalSetProperty(XMLReader parser, String property,
			Object value, String displayName) throws JDOMException {
		try {
			parser.setProperty(property, value);
		} catch (SAXNotSupportedException e) {
			throw new JDOMException(
					displayName + " property not supported for SAX driver " + parser.getClass().getName());
		} catch (SAXNotRecognizedException e) {
			throw new JDOMException(
					displayName + " property not recognized for SAX driver " + parser.getClass().getName());
		}
	}

	/**
	 * This builds a document from the supplied
	 * input source.
	 *
	 * @param in <code>InputSource</code> to read from
	 * @return <code>Document</code> resultant Document object
	 * @throws JDOMException when errors occur in parsing
	 * @throws IOException when an I/O error prevents a document
	 *         from being fully parsed
	 */
	@Override
	public Document build(InputSource in)
			throws JDOMException, IOException {

		try {
			return getEngine().build(in);
		} finally {
			if (!reuseParser) {
				engine = null;
			}
		}

	}

	/**
	 * <p>
	 * This builds a document from the supplied
	 *   input stream.
	 * </p>
	 *
	 * @param in <code>InputStream</code> to read from
	 * @return <code>Document</code> resultant Document object
	 * @throws JDOMException when errors occur in parsing
	 * @throws IOException when an I/O error prevents a document
	 *         from being fully parsed.
	 */
	@Override
	public Document build(InputStream in)
			throws JDOMException, IOException {
		try {
			return getEngine().build(in);
		} finally {
			if (!reuseParser) {
				engine = null;
			}
		}
	}

	/**
	 * <p>
	 * This builds a document from the supplied
	 *   filename.
	 * </p>
	 *
	 * @param file <code>File</code> to read from
	 * @return <code>Document</code> resultant Document object
	 * @throws JDOMException when errors occur in parsing
	 * @throws IOException when an I/O error prevents a document
	 *         from being fully parsed
	 */
	@Override
	public Document build(File file)
			throws JDOMException, IOException {
		try {
			return getEngine().build(file);
		} finally {
			if (!reuseParser) {
				engine = null;
			}
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
	 * @throws JDOMException when errors occur in parsing
	 * @throws IOException when an I/O error prevents a document
	 *         from being fully parsed.
	 */
	@Override
	public Document build(URL url)
			throws JDOMException, IOException {
		try {
			return getEngine().build(url);
		} finally {
			if (!reuseParser) {
				engine = null;
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
	 * @param systemId base for resolving relative URIs
	 * @return <code>Document</code> resultant Document object
	 * @throws JDOMException when errors occur in parsing
	 * @throws IOException when an I/O error prevents a document
	 *         from being fully parsed
	 */
	@Override
	public Document build(InputStream in, String systemId)
			throws JDOMException, IOException {
		try {
			return getEngine().build(in, systemId);
		} finally {
			if (!reuseParser) {
				engine = null;
			}
		}
	}

	/**
	 * <p>
	 * This builds a document from the supplied
	 *   Reader.  It's the programmer's responsibility to make sure
	 *   the reader matches the encoding of the file.  It's often easier
	 *   and safer to use an InputStream rather than a Reader, and to let the
	 *   parser auto-detect the encoding from the XML declaration.
	 * </p>
	 *
	 * @param characterStream <code>Reader</code> to read from
	 * @return <code>Document</code> resultant Document object
	 * @throws JDOMException when errors occur in parsing
	 * @throws IOException when an I/O error prevents a document
	 *         from being fully parsed
	 */
	@Override
	public Document build(Reader characterStream)
			throws JDOMException, IOException {
		try {
			return getEngine().build(characterStream);
		} finally {
			if (!reuseParser) {
				engine = null;
			}
		}
	}

	/**
	 * <p>
	 * This builds a document from the supplied
	 *   Reader.  It's the programmer's responsibility to make sure
	 *   the reader matches the encoding of the file.  It's often easier
	 *   and safer to use an InputStream rather than a Reader, and to let the
	 *   parser auto-detect the encoding from the XML declaration.
	 * </p>
	 *
	 * @param characterStream <code>Reader</code> to read from.
	 * @param systemId base for resolving relative URIs
	 * @return <code>Document</code> resultant Document object
	 * @throws JDOMException when errors occur in parsing
	 * @throws IOException when an I/O error prevents a document
	 *         from being fully parsed
	 */
	@Override
	public Document build(Reader characterStream, String systemId)
			throws JDOMException, IOException {

		try {
			return getEngine().build(characterStream, systemId);
		} finally {
			if (!reuseParser) {
				engine = null;
			}
		}
	}

	/**
	 * <p>
	 * This builds a document from the supplied
	 *   URI.
	 * </p>
	 * @param systemId URI for the input
	 * @return <code>Document</code> resultant Document object
	 * @throws JDOMException when errors occur in parsing
	 * @throws IOException when an I/O error prevents a document
	 *         from being fully parsed
	 */
	@Override
	public Document build(String systemId)
			throws JDOMException, IOException {
		try {
			return getEngine().build(systemId);
		} finally {
			if (!reuseParser) {
				engine = null;
			}
		}
	}

}
