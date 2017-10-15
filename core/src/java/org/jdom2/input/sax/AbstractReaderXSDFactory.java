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
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import org.jdom2.JDOMException;

/**
 * This AbstractReaderJDOMFactory class returns XMLReaders configured to validate
 * against the supplied XML Schema (XSD) instance.
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
public class AbstractReaderXSDFactory extends AbstractReaderSchemaFactory {
	
	/**
	 * Simple interface makes it easier to pass logic around in static methods.
	 * @author Rolf Lear
	 *
	 */
	protected interface SchemaFactoryProvider {
		/**
		 * Return a SchemaFactory
		 * @return a SchemaFactory
		 */
		SchemaFactory getSchemaFactory();
	}

	/**
	 * Compile an array of String URLs in to Sources which are then compiled in
	 * to a single Schema
	 * 
	 * @param systemID
	 *        The source URLs to compile
	 * @return the resulting Schema
	 * @throws JDOMException
	 *         if there is a problem with the Sources
	 */
	private static final Schema getSchemaFromString(final SchemaFactoryProvider sfp,
			String... systemID) throws JDOMException {
		if (systemID == null) {
			throw new NullPointerException("Cannot specify a null input array");
		}
		if (systemID.length == 0) {
			throw new IllegalArgumentException("You need at least one " +
					"XSD source for an XML Schema validator");
		}
		Source[] urls = new Source[systemID.length];
		for (int i = 0; i < systemID.length; i++) {
			if (systemID[i] == null) {
				throw new NullPointerException("Cannot specify a null SystemID");
			}
			urls[i] = new StreamSource(systemID[i]);
		}
		return getSchemaFromSource(sfp, urls);
	}

	/**
	 * Compile an array of Files in to URLs which are then compiled in to a
	 * single Schema
	 * 
	 * @param systemID
	 *        The source Files to compile
	 * @return the resulting Schema
	 * @throws JDOMException
	 *         if there is a problem with the Sources
	 */
	private static final Schema getSchemaFromFile(final SchemaFactoryProvider sfp,
			File... systemID) throws JDOMException {
		if (systemID == null) {
			throw new NullPointerException("Cannot specify a null input array");
		}
		if (systemID.length == 0) {
			throw new IllegalArgumentException("You need at least one " +
					"XSD source for an XML Schema validator");
		}
		Source[] sources = new Source[systemID.length];
		for (int i = 0; i < systemID.length; i++) {
			if (systemID[i] == null) {
				throw new NullPointerException("Cannot specify a null SystemID");
			}
			sources[i] = new StreamSource(systemID[i]);
		}
		return getSchemaFromSource(sfp, sources);
	}

	/**
	 * Compile an array of URLs in to Sources which are then compiled in to a
	 * single Schema
	 * 
	 * @param systemID
	 *        The source URLs to compile
	 * @return the resulting Schema
	 * @throws JDOMException
	 *         if there is a problem with the Sources
	 */
	private static final Schema getSchemaFromURL(final SchemaFactoryProvider sfp,
			URL... systemID) throws JDOMException {
		if (systemID == null) {
			throw new NullPointerException("Cannot specify a null input array");
		}
		if (systemID.length == 0) {
			throw new IllegalArgumentException("You need at least one " +
					"XSD source for an XML Schema validator");
		}
		InputStream[] streams = new InputStream[systemID.length];
		try {
			Source[] sources = new Source[systemID.length];
			for (int i = 0; i < systemID.length; i++) {
				if (systemID[i] == null) {
					throw new NullPointerException("Cannot specify a null SystemID");
				}
				InputStream is = null;
				try {
					is = systemID[i].openStream();
				} catch (IOException e) {
					throw new JDOMException("Unable to read Schema URL " +
							systemID[i].toString(), e);
				}
				streams[i] = is;
				sources[i] = new StreamSource(is, systemID[i].toString());
			}
			return getSchemaFromSource(sfp, sources);
		} finally {
			for (InputStream is : streams) {
				if (is != null) {
					try {
						is.close();
					} catch (IOException ioe) {
						// swallow it.
					}
				}
			}
		}
	}

	/**
	 * Compile an array of Sources in to a single Schema
	 * 
	 * @param sources
	 *        The sources to compile
	 * @return the resulting Schema
	 * @throws JDOMException
	 *         if there is a problem with the Sources
	 */
	private static final Schema getSchemaFromSource(final SchemaFactoryProvider sfp, 
			Source... sources) throws JDOMException {
		if (sources == null) {
			throw new NullPointerException("Cannot specify a null input array");
		}
		if (sources.length == 0) {
			throw new IllegalArgumentException("You need at least one " +
					"XSD Source for an XML Schema validator");
		}
		try {
			SchemaFactory sfac = sfp.getSchemaFactory();
			if (sfac == null) {
				throw new JDOMException("Unable to create XSDSchema validator.");
			}
			return sfac.newSchema(sources);
		} catch (SAXException e) {
			String msg = Arrays.toString(sources);
			throw new JDOMException("Unable to create a Schema for Sources " +
					msg, e);
		}
	}

	/**
	 * Create an XML Schema validating XMLReader factory using one or more XSD
	 * sources from SystemID references.
	 * 
	 * @param fac
	 *        The SAXParserFactory used to create the XMLReader instances.
	 * @param sfp
	 *        The SchemaFactoryProvider instance that gives us Schema Factories 
	 * @param systemid
	 *        The var-arg array of at least one SystemID reference (URL) to
	 *        locate the XSD's used to validate
	 * @throws JDOMException
	 *         If the Schemas could not be loaded from the SystemIDs This will
	 *         wrap a SAXException that contains the actual fault.
	 */
	public AbstractReaderXSDFactory(final SAXParserFactory fac,
			final SchemaFactoryProvider sfp, String... systemid) throws JDOMException {
		super(fac, getSchemaFromString(sfp, systemid));
	}

	/**
	 * Create an XML Schema validating XMLReader factory using one or more XSD
	 * sources from URL references.
	 * 
	 * @param fac
	 *        The SAXParserFactory used to create the XMLReader instances.
	 * @param sfp
	 *        The SchemaFactoryProvider instance that gives us Schema Factories 
	 * @param systemid
	 *        The var-arg array of at least one SystemID reference (URL) to
	 *        locate the XSD's used to validate
	 * @throws JDOMException
	 *         If the Schemas could not be loaded from the SystemIDs This will
	 *         wrap a SAXException that contains the actual fault.
	 */
	public AbstractReaderXSDFactory(final SAXParserFactory fac,
			final SchemaFactoryProvider sfp, URL... systemid) throws JDOMException {
		super(fac, getSchemaFromURL(sfp, systemid));
	}

	/**
	 * Create an XML Schema validating XMLReader factory using one or more XSD
	 * sources from File references.
	 * 
	 * @param fac
	 *        The SAXParserFactory used to create the XMLReader instances.
	 * @param sfp
	 *        The SchemaFactoryProvider instance that gives us Schema Factories 
	 * @param systemid
	 *        The var-arg array of at least one SystemID reference (File) to
	 *        locate the XSD's used to validate
	 * @throws JDOMException
	 *         If the Schemas could not be loaded from the SystemIDs This will
	 *         wrap a SAXException that contains the actual fault.
	 */
	public AbstractReaderXSDFactory(final SAXParserFactory fac,
			final SchemaFactoryProvider sfp, File... systemid) throws JDOMException {
		super(fac, getSchemaFromFile(sfp, systemid));
	}

	/**
	 * Create an XML Schema validating XMLReader factory using one or more XSD
	 * sources from Transform Source references.
	 * 
	 * @param fac
	 *        The SAXParserFactory used to create the XMLReader instances.
	 * @param sfp
	 *        The SchemaFactoryProvider instance that gives us Schema Factories 
	 * @param sources
	 *        The var-arg array of at least one transform Source reference to
	 *        locate the XSD's used to validate
	 * @throws JDOMException
	 *         If the Schemas could not be loaded from the Sources This will
	 *         wrap a SAXException that contains the actual fault.
	 */
	public AbstractReaderXSDFactory(final SAXParserFactory fac,
			final SchemaFactoryProvider sfp, Source... sources) throws JDOMException {
		super(fac, getSchemaFromSource(sfp, sources));
	}

}
