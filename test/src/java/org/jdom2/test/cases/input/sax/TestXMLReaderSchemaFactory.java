package org.jdom2.test.cases.input.sax;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.junit.Test;
import org.xml.sax.SAXException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaderJAXPSchemaFactory;

@SuppressWarnings("javadoc")
public class TestXMLReaderSchemaFactory {

	@Test
	public void testSchemaXMLReaderFactory() throws SAXException, JDOMException {
		SchemaFactory schemafac = 
				SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schemafac.newSchema(new File("test/resources/xsdcomplex/SAXTestComplexMain.xsd"));
		XMLReaderJAXPSchemaFactory readerfac = new XMLReaderJAXPSchemaFactory(schema);
		assertTrue(readerfac.isValidating());
		assertNotNull(readerfac.createXMLReader());
	}

	@Test
	public void testParseValidateWorks() throws JDOMException, IOException, SAXException {
		SchemaFactory schemafac = 
				SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schemafac.newSchema(new File("test/resources/xsdcomplex/SAXTestComplexMain.xsd"));
		XMLReaderJAXPSchemaFactory readerfac = new XMLReaderJAXPSchemaFactory(schema);
		assertTrue(readerfac.isValidating());
		SAXBuilder builder = new SAXBuilder(readerfac);
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
	
	
}
