package org.jdom2.input.sax;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import org.jdom2.JDOMException;

/**
 * This JDOMXMLReaderFactory class returns XMLReaders configured to validate
 * against the supplied Schema instance. The Schema could be an XSD schema or
 * some other schema supported by SAX (e.g. RelaxNG).
 * <p>
 * If you want to validate an XML document against the XSD references embedded
 * in the XML itself (xsdSchemaLocation) then you do not want to use this class
 * but rather use an alternate means.
 * <p>
 * See the {@link org.jdom2.input.sax package documentation} for the best
 * alternatives.
 *  
 * @see org.jdom2.input.sax
 * 
 * @author Rolf Lear
 *
 */
public class XMLReaderJAXPSchemaFactory implements XMLReaderJDOMFactory {
	
	private final SAXParserFactory saxfac;
	
	/**
	 * XMLReader instances from this class will be configured to validate using
	 * the supplied Schema instance.
	 * 
	 * @param schema The Schema to use for validation.
	 */
	public XMLReaderJAXPSchemaFactory(Schema schema) {
		if (schema == null) {
			throw new NullPointerException("Cannot create a " +
					"SchemaXMLReaderFactory with a null schema");
		}
		saxfac = SAXParserFactory.newInstance();
		saxfac.setNamespaceAware(true);
		saxfac.setValidating(false);
		saxfac.setSchema(schema);
	}

	@Override
	public XMLReader createXMLReader() throws JDOMException {
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
