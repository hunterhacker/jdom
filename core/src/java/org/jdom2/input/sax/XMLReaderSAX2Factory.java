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

import static org.jdom2.JDOMConstants.*;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import org.jdom2.JDOMException;

/**
 * Create XMLReaders directly from the SAX2.0 API using a SAX Driver class name
 * or the default SAX2.0 location process.
 * <p>
 * Unless you have good reason to use this mechanism you should rather use the
 * JAXP-based processes. Read the {@link org.jdom2.input.sax package
 * documentation} for other alternatives.
 * 
 * @see org.jdom2.input.sax
 * @author Rolf Lear
 */
public class XMLReaderSAX2Factory implements XMLReaderJDOMFactory {

	private final boolean validate;
	private final String saxdriver;

	/**
	 * The required details for SAX2.0-based XMLReader creation.
	 * 
	 * @param validate
	 *        whether to validate against the DocType
	 * @see XMLReaders#NONVALIDATING
	 * @see XMLReaders#DTDVALIDATING
	 * @see XMLReaders#XSDVALIDATING
	 */
	public XMLReaderSAX2Factory(boolean validate) {
		this(validate, null);
	}

	/**
	 * The required details for SAX2.0-based XMLReader creation.
	 * 
	 * @param validate
	 *        whether to validate against the DocType
	 * @param saxdriver
	 *        The SAX2.0 Driver classname (null to use the SAX2.0 default parser
	 *        searching algorithm - if you specify null you should probably be
	 *        using JAXP anyway).
	 * @see XMLReaders#NONVALIDATING
	 * @see XMLReaders#DTDVALIDATING
	 * @see XMLReaders#XSDVALIDATING
	 */
	public XMLReaderSAX2Factory(final boolean validate, final String saxdriver) {
		super();
		this.validate = validate;
		this.saxdriver = saxdriver;
	}

	@Override
	public XMLReader createXMLReader() throws JDOMException {
		try {
			XMLReader reader = saxdriver == null
					? XMLReaderFactory.createXMLReader()
					: XMLReaderFactory.createXMLReader(saxdriver);
			reader.setFeature(
					SAX_FEATURE_VALIDATION, validate);
			// Setup some namespace features.
			reader.setFeature(
					SAX_FEATURE_NAMESPACES, true);
			reader.setFeature(
					SAX_FEATURE_NAMESPACE_PREFIXES, true);

			return reader;
		} catch (SAXException e) {
			throw new JDOMException("Unable to create SAX2 XMLReader.", e);
		}

	}

	/**
	 * Get the SAX Driver class name used to bootstrap XMLReaders.
	 *
	 * @return The name of the SAX Driver class (null for SAX2 default class).
	 */
	public String getDriverClassName() {
		return saxdriver;
	}

	@Override
	public boolean isValidating() {
		return validate;
	}

}
