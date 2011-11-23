package org.jdom2.input.sax;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import org.jdom2.JDOMException;

/**
 * Create XMLReaders directly from the SAX2.0 API using a SAX Driver class name
 * or the default SAX2.0 location process.
 * <p>
 * Unless you have good reason to use this mechanism you should rather use the
 * JAXP-based processes. Read the {@link org.jdom2.input.sax package documentation}
 * for other alternatives.
 * 
 * @see org.jdom2.input.sax
 * 
 * @author Rolf Lear
 *
 */
public class XMLReaderSAX2Factory implements XMLReaderJDOMFactory {

	private final boolean validate;
	private final String saxdriver;
	
	/**
	 * The required details for SAX2.0-based XMLReader creation.
	 * @param validate whether to validate against the DocType
	 */
	public XMLReaderSAX2Factory(boolean validate) {
		this(validate, null);
	}

	/**
	 * The required details for SAX2.0-based XMLReader creation.
	 * @param validate whether to validate against the DocType
	 * @param saxdriver The SAX2.0 Driver classname
	 */
	public XMLReaderSAX2Factory(boolean validate, String saxdriver) {
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
					"http://xml.org/sax/features/validation", validate);
			// Setup some namespace features.
			reader.setFeature(
					"http://xml.org/sax/features/namespaces", true);
			reader.setFeature(
					"http://xml.org/sax/features/namespace-prefixes", true);
	
			return reader;
		} catch (SAXException e) {
			throw new JDOMException("Unable to create SAX2 XMLReader.", e);
		}
		
	}

	/**
	 * Get the SAX Driver class name used to boostrap XMLReaders.
	 * @return The name of the SAX Driver class (null for SAX2 default class).
	 */
	public String getDriverClassName() {
		return saxdriver;
	}
	
	/**
	 * Are XMLReaders from this factory validating XMLReaders?
	 * @return true if the XMLReaders will (DTD) Validate.
	 */
	@Override
	public boolean isValidating() {
		return validate;
	}

}
