package org.jdom2.test.cases.input.sax;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.xml.sax.SAXException;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaderSAX2Factory;
import org.jdom2.test.util.FidoFetch;
import org.jdom2.test.util.UnitTestUtil;

@SuppressWarnings("javadoc")
public class TestXMLReaderSAX2Factory {

	@Test
	public void testSAX2XMLReaderFactoryBoolean() throws JDOMException {
		XMLReaderSAX2Factory facval = new XMLReaderSAX2Factory(true);
		assertTrue(facval.isValidating());
		assertTrue(facval.createXMLReader() != null);
		XMLReaderSAX2Factory facnon = new XMLReaderSAX2Factory(false);
		assertFalse(facnon.isValidating());
		assertTrue(facnon.createXMLReader() != null);
	}

	@Test
	public void testSAX2XMLReaderFactoryBooleanString() throws JDOMException {
		XMLReaderSAX2Factory facval = new XMLReaderSAX2Factory(true, null);
		assertTrue(facval.isValidating());
		assertTrue(facval.createXMLReader() != null);

		facval = new XMLReaderSAX2Factory(true, 
				"com.sun.org.apache.xerces.internal.parsers.SAXParser");
		assertTrue(facval.isValidating());
		assertTrue(facval.createXMLReader() != null);
		
		XMLReaderSAX2Factory facnon = new XMLReaderSAX2Factory(false, null);
		assertFalse(facnon.isValidating());
		assertTrue(facnon.createXMLReader() != null);
		
		facnon = new XMLReaderSAX2Factory(false, 
				"com.sun.org.apache.xerces.internal.parsers.SAXParser");
		assertFalse(facnon.isValidating());
		assertTrue(facnon.createXMLReader() != null);
	}

	@Test
	public void testGetDriverClassName() {
		XMLReaderSAX2Factory facnon = new XMLReaderSAX2Factory(false, 
				"com.sun.org.apache.xerces.internal.parsers.SAXParser");
		assertFalse(facnon.isValidating());
		assertEquals("com.sun.org.apache.xerces.internal.parsers.SAXParser",
				facnon.getDriverClassName());
	}
	
	@Test
	public void testGetDummyDriver() {
		XMLReaderSAX2Factory facnon = new XMLReaderSAX2Factory(false, 
				"does.not.exist");
		assertFalse(facnon.isValidating());
		try {
			facnon.createXMLReader();
			UnitTestUtil.failNoException(JDOMException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(JDOMException.class, e);
			UnitTestUtil.checkException(SAXException.class, e.getCause());
		}
	}
	
	
	@Test
	public void testParseValidateWorks() throws JDOMException, IOException {
		XMLReaderSAX2Factory fac = new XMLReaderSAX2Factory(true);
		assertTrue(fac.isValidating());
		SAXBuilder builder = new SAXBuilder(fac);
		Document doc = builder.build(FidoFetch.getFido().getURL("/DOMBuilder/doctype.xml"));
		assertEquals("root", doc.getRootElement().getName());
	}
	
	@Test
	public void testParseValidateFails() {
		XMLReaderSAX2Factory fac = new XMLReaderSAX2Factory(true);
		assertTrue(fac.isValidating());
		SAXBuilder builder = new SAXBuilder(fac);
		try {
			builder.build(FidoFetch.getFido().getURL("/DOMBuilder/attributes.xml"));
			UnitTestUtil.failNoException(JDOMException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(JDOMException.class, e);
		}
	}
	
	@Test
	public void testParseNonValidateWorks() throws JDOMException, IOException {
		XMLReaderSAX2Factory fac = new XMLReaderSAX2Factory(false);
		assertFalse(fac.isValidating());
		SAXBuilder builder = new SAXBuilder(fac);
		Document doc = builder.build(FidoFetch.getFido().getURL("/DOMBuilder/attributes.xml"));
		assertEquals("root", doc.getRootElement().getName());
	}
	

}
