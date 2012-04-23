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

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import org.jdom2.JDOMException;

/**
 * This {@link AbstractReaderSchemaFactory} class returns XMLReaders configured to
 * validate against the supplied Schema instance. The Schema could be an XSD
 * schema or some other schema supported by SAX (e.g. RelaxNG). It takes a pre-declared
 * <p>
 * If you want to validate an XML document against the XSD references embedded
 * in the XML itself (xsdSchemaLocation) then you do not want to use this class
 * but rather use an alternate means like
 * {@link XMLReaders#XSDVALIDATING}.
 * <p>
 * See the {@link org.jdom2.input.sax package documentation} for the best
 * alternatives.
 * 
 * @see org.jdom2.input.sax
 * @author Rolf Lear
 */
public abstract class AbstractReaderSchemaFactory implements XMLReaderJDOMFactory {

	private final SAXParserFactory saxfac;

	/**
	 * XMLReader instances from this class will be configured to validate using
	 * the supplied Schema instance.
	 * 
	 * @param fac
	 *        The SAXParserFactory to use for creating XMLReader instances.
	 * @param schema
	 *        The Schema to use for validation.
	 */
	public AbstractReaderSchemaFactory(final SAXParserFactory fac, final Schema schema) {
		if (schema == null) {
			throw new NullPointerException("Cannot create a " +
					"SchemaXMLReaderFactory with a null schema");
		}
		saxfac = fac;
		if (saxfac != null) {
			saxfac.setNamespaceAware(true);
			saxfac.setValidating(false);
			saxfac.setSchema(schema);
		}
	}

	@Override
	public XMLReader createXMLReader() throws JDOMException {
		if (saxfac == null) {
			throw new JDOMException("It was not possible to configure a " +
					"suitable XMLReader to support " + this);
		}

		try {
			return saxfac.newSAXParser().getXMLReader();
		} catch (SAXException e) {
			throw new JDOMException(
					"Could not create a new Schema-Validating XMLReader.", e);
		} catch (ParserConfigurationException e) {
			throw new JDOMException(
					"Could not create a new Schema-Validating XMLReader.", e);
		}
	}

	@Override
	public boolean isValidating() {
		return true;
	}

}
