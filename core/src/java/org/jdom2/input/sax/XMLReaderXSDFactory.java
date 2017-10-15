/*--

 Copyright (C) 2011 Jason Hunter & Brett McLaughlin.
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

package org.jdom2.input.sax;

import java.io.File;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.validation.SchemaFactory;

import org.jdom2.JDOMException;

/**
 * This XMLReaderJDOMFactory class returns XMLReaders configured to validate
 * against the supplied XML Schema (XSD) instance. The SAX Parser is obtained through
 * the JAXP process.
 * 
 * <p>
 * This class has var-arg constructors, accepting potentially many XSD sources.
 * It is just as simple though to have a single source:
 * 
 * <pre>
 * File xsdfile = new File(&quot;schema.xsd&quot;);
 * XMLReaderJDOMFactory schemafac = new XMLReaderXSDFactory(xsdfile);
 * SAXBuilder builder = new SAXBuilder(schemafac);
 * File xmlfile = new File(&quot;data.xml&quot;);
 * Document validdoc = builder.build(xmlfile);
 * </pre>
 * 
 * @see org.jdom2.input.sax
 * @author Rolf Lear
 */
public class XMLReaderXSDFactory extends AbstractReaderXSDFactory {
	
	private static final SchemaFactoryProvider xsdschemas = new SchemaFactoryProvider() {
	    /**
	     * Use a Thread-Local system to manage SchemaFactory. SchemaFactory is not
	     * thread-safe, so we need some mechanism to isolate it, and thread-local is
	     * a logical way because it only creates an instance when needed in each
	     * thread, and they die when the thread dies. Does not need any
	     * synchronisation either.
	     */
	    private final ThreadLocal<SchemaFactory> schemafactl = new ThreadLocal<SchemaFactory>();
	    
		@Override
		public SchemaFactory getSchemaFactory() {
            SchemaFactory sfac = schemafactl.get();
            if (sfac == null) {
                sfac = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                schemafactl.set(sfac);
            }
			return sfac;
		}
	};


	/**
	 * Create an XML Schema validating XMLReader factory using one or more XSD
	 * sources from SystemID references.
	 * 
	 * @param systemid
	 *        The var-arg array of at least one SystemID reference (URL) to
	 *        locate the XSD's used to validate
	 * @throws JDOMException
	 *         If the Schemas could not be loaded from the SystemIDs This will
	 *         wrap a SAXException that contains the actual fault.
	 */
	public XMLReaderXSDFactory(String... systemid)
			throws JDOMException {
		super(SAXParserFactory.newInstance(), xsdschemas, systemid);
	}

	/**
	 * Create an XML Schema validating XMLReader factory using one or more XSD
	 * sources from SystemID references, and use the specified JAXP SAXParserFactory.
	 * 
	 * @param factoryClassName The name of the SAXParserFactory class to use
	 * @param classloader The classLoader to use for loading the SAXParserFactory. 
	 * @param systemid
	 *        The var-arg array of at least one SystemID reference (URL) to
	 *        locate the XSD's used to validate
	 * @throws JDOMException
	 *         If the Schemas could not be loaded from the SystemIDs This will
	 *         wrap a SAXException that contains the actual fault.
	 * @since 2.0.3
	 */
	public XMLReaderXSDFactory(final String factoryClassName, final ClassLoader classloader,
			final String... systemid) throws JDOMException {
		super(SAXParserFactory.newInstance(factoryClassName, classloader), xsdschemas, systemid);
	}

	/**
	 * Create an XML Schema validating XMLReader factory using one or more XSD
	 * sources from URL references.
	 * 
	 * @param systemid
	 *        The var-arg array of at least one SystemID reference (URL) to
	 *        locate the XSD's used to validate
	 * @throws JDOMException
	 *         If the Schemas could not be loaded from the SystemIDs This will
	 *         wrap a SAXException that contains the actual fault.
	 */
	public XMLReaderXSDFactory(URL... systemid) throws JDOMException {
		super(SAXParserFactory.newInstance(), xsdschemas, systemid);
	}

	/**
	 * Create an XML Schema validating XMLReader factory using one or more XSD
	 * sources from URL references, and use the specified JAXP SAXParserFactory.
	 * 
	 * @param factoryClassName The name of the SAXParserFactory class to use
	 * @param classloader The classLoader to use for loading the SAXParserFactory. 
	 * @param systemid
	 *        The var-arg array of at least one SystemID reference (URL) to
	 *        locate the XSD's used to validate
	 * @throws JDOMException
	 *         If the Schemas could not be loaded from the SystemIDs This will
	 *         wrap a SAXException that contains the actual fault.
	 * @since 2.0.3
	 */
	public XMLReaderXSDFactory(final String factoryClassName, final ClassLoader classloader,
			URL... systemid) throws JDOMException {
		super(SAXParserFactory.newInstance(factoryClassName, classloader), xsdschemas, systemid);
	}

	/**
	 * Create an XML Schema validating XMLReader factory using one or more XSD
	 * sources from File references.
	 * 
	 * @param systemid
	 *        The var-arg array of at least one SystemID reference (File) to
	 *        locate the XSD's used to validate
	 * @throws JDOMException
	 *         If the Schemas could not be loaded from the SystemIDs This will
	 *         wrap a SAXException that contains the actual fault.
	 */
	public XMLReaderXSDFactory(File... systemid) throws JDOMException {
		super(SAXParserFactory.newInstance(), xsdschemas, systemid);
	}

	/**
	 * Create an XML Schema validating XMLReader factory using one or more XSD
	 * sources from File references, and use the specified JAXP SAXParserFactory.
	 * 
	 * @param factoryClassName The name of the SAXParserFactory class to use
	 * @param classloader The classLoader to use for loading the SAXParserFactory. 
	 * @param systemid
	 *        The var-arg array of at least one SystemID reference (File) to
	 *        locate the XSD's used to validate
	 * @throws JDOMException
	 *         If the Schemas could not be loaded from the SystemIDs This will
	 *         wrap a SAXException that contains the actual fault.
	 * @since 2.0.3
	 */
	public XMLReaderXSDFactory(final String factoryClassName, final ClassLoader classloader,
			File... systemid) throws JDOMException {
		super(SAXParserFactory.newInstance(factoryClassName, classloader), xsdschemas, systemid);
	}

	/**
	 * Create an XML Schema validating XMLReader factory using one or more XSD
	 * sources from Transform Source references.
	 * 
	 * @param sources
	 *        The var-arg array of at least one transform Source reference to
	 *        locate the XSD's used to validate
	 * @throws JDOMException
	 *         If the Schemas could not be loaded from the Sources This will
	 *         wrap a SAXException that contains the actual fault.
	 */
	public XMLReaderXSDFactory(Source... sources) throws JDOMException {
		super(SAXParserFactory.newInstance(), xsdschemas, sources);
	}

	/**
	 * Create an XML Schema validating XMLReader factory using one or more XSD
	 * sources from Transform Source references, and use the specified JAXP SAXParserFactory.
	 * 
	 * @param factoryClassName The name of the SAXParserFactory class to use
	 * @param classloader The classLoader to use for loading the SAXParserFactory. 
	 * @param sources
	 *        The var-arg array of at least one transform Source reference to
	 *        locate the XSD's used to validate
	 * @throws JDOMException
	 *         If the Schemas could not be loaded from the Sources This will
	 *         wrap a SAXException that contains the actual fault.
	 * @since 2.0.3
	 */
	public XMLReaderXSDFactory(final String factoryClassName, final ClassLoader classloader,
			Source... sources) throws JDOMException {
		super(SAXParserFactory.newInstance(factoryClassName, classloader), xsdschemas, sources);
	}

}
