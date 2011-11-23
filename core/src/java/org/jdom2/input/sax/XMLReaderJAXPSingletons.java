package org.jdom2.input.sax;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import org.jdom2.JDOMException;

/**
 * An enumeration of {@link XMLReaderJDOMFactory} that allows for a single central
 * location to create XMLReaders. The Singletons (members) of this enumeration
 * can produce the most common XMLReaders: non-validating, XSD validating, and DocType
 * validating.
 * <p>
 * See the {@link org.jdom2.input.sax package documentation} for details of how
 * to create the SAXParser you desire.
 * 
 * @see org.jdom2.input.sax
 * 
 * @author Rolf Lear
 *
 */
public enum XMLReaderJAXPSingletons implements XMLReaderJDOMFactory {

	/**
	 * The non-validating singleton
	 */
	NONVALIDATING(0),
	
	/**
	 * The DTD-validating Singleton
	 */
	DTDVALIDATING(1),
	
	/**
	 * The XSD-validating Singleton
	 */
	XSDVALIDATING(2);
	
	/** 
	 * the actual SAXParserFactory in the respective singletons. 
	 */
	private final SAXParserFactory jaxpfactory;
	private final boolean validates;


	private XMLReaderJAXPSingletons(int validate) {
		SAXParserFactory fac = SAXParserFactory.newInstance();
		boolean val = false;
		// All JDOM parsers are namespace aware.
		fac.setNamespaceAware(true);
		switch (validate) {
			case 0 :
				fac.setValidating(false);
				break;
			case 1 :
				fac.setValidating(true);
				val = true;
				break;
			case 2 :
				fac.setValidating(false);
				try {
					SchemaFactory sfac = SchemaFactory.
							newInstance("http://www.w3.org/2001/XMLSchema");
					Schema schema = sfac.newSchema();
					fac.setSchema(schema);
					val = true;
				} catch (SAXException se) {
					// we could not get a validating system, set the fac to null
					fac = null;
				}
				break;
		}
		jaxpfactory = fac;
		validates = val;
	}

	/**
	 * Get a new XMLReader from this JAXP-based {@link XMLReaderJDOMFactory}.
	 * <p>
	 * 
	 * @return a new XMLReader instance.
	 * @throws JDOMException if there is a problem creating the XMLReader
	 */
	@Override
	public XMLReader createXMLReader() throws JDOMException {
		if (jaxpfactory == null) {
			throw new JDOMException("It was not possible to configure a " +
					"suitable XMLReader to support " + this);
		}
		try {
			return jaxpfactory.newSAXParser().getXMLReader();
		} catch (SAXException e) {
			throw new JDOMException(
					"Unable to create a new XMLReader instance", e);
		} catch (ParserConfigurationException e) {
			throw new JDOMException(
					"Unable to create a new XMLReader instance", e);
		}
	}
	
	@Override
	public boolean isValidating() {
		return validates;
	}
	
}
