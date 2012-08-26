package org.jdom2.test.cases.input.sax;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.xml.parsers.FactoryConfigurationError;

import org.junit.Test;

import org.jdom2.JDOMException;
import org.jdom2.input.sax.XMLReaderJAXPFactory;
import org.jdom2.test.util.UnitTestUtil;

@SuppressWarnings("javadoc")
public class TestXMLReaderJAXPFactory {

	//org.apache.xerces.jaxp.SAXParserFactoryImpl
	
	@Test
	public void testJAXPXMLReaderFactoryDTDVal() throws JDOMException {
		XMLReaderJAXPFactory readerfac = new XMLReaderJAXPFactory(
				"org.apache.xerces.jaxp.SAXParserFactoryImpl", null, true);
		assertTrue(readerfac.isValidating());
		assertNotNull(readerfac.createXMLReader());
	}

	@Test
	public void testJAXPXMLReaderFactory() throws JDOMException {
		XMLReaderJAXPFactory readerfac = new XMLReaderJAXPFactory(
				"org.apache.xerces.jaxp.SAXParserFactoryImpl", null, false);
		assertFalse(readerfac.isValidating());
		assertNotNull(readerfac.createXMLReader());
	}

	@Test
	public void testSchemaXMLReaderFactoryNull() {
		try {
			assertTrue(null != new XMLReaderJAXPFactory(
					null, null, false));
			UnitTestUtil.failNoException(FactoryConfigurationError.class);
		} catch (Throwable e) {
			UnitTestUtil.checkException(FactoryConfigurationError.class, e);
		}
	}

}
