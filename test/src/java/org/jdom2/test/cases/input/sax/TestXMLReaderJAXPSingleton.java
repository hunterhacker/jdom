package org.jdom2.test.cases.input.sax;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaderJAXPSingletons;
import org.jdom2.test.util.UnitTestUtil;

@SuppressWarnings("javadoc")
public class TestXMLReaderJAXPSingleton {

	@Test
	public void testNonValidatingReader() throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder(XMLReaderJAXPSingletons.NONVALIDATING);
		assertFalse(builder.isValidating());
		Document doc = builder.build("test/resources/DOMBuilder/attributes.xml");
		assertEquals("root", doc.getRootElement().getName());
	}

	@Test
	public void testDTDValidatingReader() throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder(XMLReaderJAXPSingletons.DTDVALIDATING);
		assertTrue(builder.isValidating());
		Document doc = builder.build("test/resources/DOMBuilder/doctype.xml");
		assertEquals("root", doc.getRootElement().getName());
	}

	@Test
	public void testDTDValidatingReaderFails() {
		SAXBuilder builder = new SAXBuilder(XMLReaderJAXPSingletons.DTDVALIDATING);
		assertTrue(builder.isValidating());
		try {
			builder.build("test/resources/DOMBuilder/attributes.xml");
			UnitTestUtil.failNoException(JDOMException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(JDOMException.class, e);
		}
	}

	@Test
	public void testXSDValidatingReader() throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder(XMLReaderJAXPSingletons.XSDVALIDATING);
		assertTrue(builder.isValidating());
		Document doc = builder.build(new File("test/resources/xsdcomplex/input.xml"));
		assertEquals("test", doc.getRootElement().getName());
		// the whole point of this particular XML input is that it should apply
		// default attribute values.... lets make sure they make it.
		int count = 4;
		for (Element data : doc.getRootElement().getChildren("data", Namespace.getNamespace("http://www.jdom.org/tests/default"))) {
			count--;
			assertEquals("simple", data.getAttributeValue("type", Namespace.getNamespace("http://www.jdom.org/tests/imp")));
		}
		assertTrue("" + count + " left", count == 0);
	}

	@Test
	public void testXSDValidatingReaderFails() {
		SAXBuilder builder = new SAXBuilder(XMLReaderJAXPSingletons.XSDVALIDATING);
		assertTrue(builder.isValidating());
		try {
			builder.build("test/resources/DOMBuilder/attributes.xml");
			UnitTestUtil.failNoException(JDOMException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(JDOMException.class, e);
		}
	}

}
