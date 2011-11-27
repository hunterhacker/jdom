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

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import org.jdom2.JDOMException;

/**
 * This XMLReaderJDOMFactory class returns XMLReaders configured to validate
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
public class XMLReaderXSDFactory extends XMLReaderSchemaFactory {

	/**
	 * Use a Thread-Local system to manage SchemaFactory. SchemaFactory is not
	 * thread-safe, so we need some mechanism to isolate it, and thread-local is
	 * a logical way because it only creates an instance when needed in each
	 * thread, and they die when the thread dies. Does not need any
	 * synchronisation either.
	 */
	private static final ThreadLocal<SchemaFactory> schemafactl =
			new ThreadLocal<SchemaFactory>() {
				@Override
				protected SchemaFactory initialValue() {
					return SchemaFactory.newInstance(
							XMLConstants.W3C_XML_SCHEMA_NS_URI);
				}
			};

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
	private static final Schema getSchemaFromString(String... systemID)
			throws JDOMException {
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
		return getSchemaFromSource(urls);
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
	private static final Schema getSchemaFromFile(File... systemID)
			throws JDOMException {
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
		return getSchemaFromSource(sources);
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
	private static final Schema getSchemaFromURL(URL... systemID)
			throws JDOMException {
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
			return getSchemaFromSource(sources);
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
	private static final Schema getSchemaFromSource(Source... sources)
			throws JDOMException {
		if (sources == null) {
			throw new NullPointerException("Cannot specify a null input array");
		}
		if (sources.length == 0) {
			throw new IllegalArgumentException("You need at least one " +
					"XSD Source for an XML Schema validator");
		}
		try {
			return schemafactl.get().newSchema(sources);
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
	 * @param systemid
	 *        The var-arg array of at least one SystemID reference (URL) to
	 *        locate the XSD's used to validate
	 * @throws JDOMException
	 *         If the Schemas could not be loaded from the SystemIDs This will
	 *         wrap a SAXException that contains the actual fault.
	 */
	public XMLReaderXSDFactory(String... systemid)
			throws JDOMException {
		super(getSchemaFromString(systemid));
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
		super(getSchemaFromURL(systemid));
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
		super(getSchemaFromFile(systemid));
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
		super(getSchemaFromSource(sources));
	}

}
