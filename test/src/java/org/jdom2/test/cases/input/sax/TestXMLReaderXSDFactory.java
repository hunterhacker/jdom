package org.jdom2.test.cases.input.sax;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaderJDOMFactory;
import org.jdom2.input.sax.XMLReaderXSDFactory;
import org.jdom2.test.util.FidoFetch;
import org.jdom2.test.util.UnitTestUtil;

@SuppressWarnings("javadoc")
public class TestXMLReaderXSDFactory {
	//"./test/resources/xscomplex/multi_one.xsd",
	
	private final URL filemain() {
		return FidoFetch.getFido().getURL("/xsdcomplex/multi_main.xsd");
	}
	private final URL fileone() {
		return FidoFetch.getFido().getURL("/xsdcomplex/multi_one.xsd");
	}
	
	private final URL filetwo() {
		return FidoFetch.getFido().getURL("/xsdcomplex/multi_two.xsd");
	}
	private final URL source() {
		return FidoFetch.getFido().getURL("/xsdcomplex/multi.xml");
	}

	private void checkXML(XMLReaderJDOMFactory fac) {
		SAXBuilder builder = new SAXBuilder(fac);
		try {
			Namespace nsmain = Namespace.getNamespace("http://www.jdom.org/schema_main");
			Namespace nsone  = Namespace.getNamespace("http://www.jdom.org/schema_one");
			Namespace nstwo  = Namespace.getNamespace("http://www.jdom.org/schema_two");
			
			Document doc = builder.build(source());
			assertTrue(doc.hasRootElement());
			Element root = doc.getRootElement();
			assertTrue(nsmain == root.getNamespace());
			Element childone = root.getChild("child", nsone);
			Element childtwo = root.getChild("child", nstwo);
			assertTrue(childone != null);
			assertTrue(childtwo != null);
			
			assertEquals("valueone", childone.getAttributeValue("attribute"));
			assertEquals("valuetwo", childtwo.getAttributeValue("attribute"));
			assertEquals("schema_one", childone.getAttributeValue("source"));
			assertEquals("schema_two", childtwo.getAttributeValue("source"));
		} catch (Exception e) {
			UnitTestUtil.failException("Not expecting an exception", e);
		}
		
	}
	
	@Test
	public void testXMLReaderXSDFactoryStringArray() throws JDOMException {
		XMLReaderJDOMFactory fac = new XMLReaderXSDFactory(
				filemain().toExternalForm(),
				fileone().toExternalForm(),
				filetwo().toExternalForm());
		checkXML(fac);
	}

	@Test
	public void testXMLReaderXSDFactoryStringArrayJAXP() throws JDOMException {
		XMLReaderJDOMFactory fac = new XMLReaderXSDFactory(
				"org.apache.xerces.jaxp.SAXParserFactoryImpl", (ClassLoader)null,
				filemain().toExternalForm(),
				fileone().toExternalForm(),
				filetwo().toExternalForm());
		checkXML(fac);
	}

	@Test
	public void testXMLReaderXSDFactoryURLArray() throws JDOMException {
		XMLReaderJDOMFactory fac = new XMLReaderXSDFactory(
				filemain(),
				fileone(),
				filetwo());
		checkXML(fac);
	}

	@Test
	public void testXMLReaderXSDFactoryURLArrayJAXP() throws JDOMException {
		XMLReaderJDOMFactory fac = new XMLReaderXSDFactory(
				"org.apache.xerces.jaxp.SAXParserFactoryImpl", null,
				filemain(),
				fileone(),
				filetwo());
		checkXML(fac);
	}

	@Test
	public void testXMLReaderXSDFactoryFileArray() throws JDOMException {
		XMLReaderJDOMFactory fac = new XMLReaderXSDFactory(
				filemain(),
				fileone(),
				filetwo());
		checkXML(fac);
	}

	@Test
	public void testXMLReaderXSDFactoryFileArrayJAXP() throws JDOMException {
		XMLReaderJDOMFactory fac = new XMLReaderXSDFactory(
				"org.apache.xerces.jaxp.SAXParserFactoryImpl", null,
				filemain(),
				fileone(),
				filetwo());
		checkXML(fac);
	}

	@Test
	public void testXMLReaderXSDFactorySourceArray() throws JDOMException, IOException {
		XMLReaderJDOMFactory fac = new XMLReaderXSDFactory(
				new StreamSource(filemain().openStream()),
				new StreamSource(fileone().openStream()),
				new StreamSource(filetwo().openStream()));
		checkXML(fac);
	}

	@Test
	public void testXMLReaderXSDFactorySourceArrayJJAXP() throws JDOMException, IOException {
		XMLReaderJDOMFactory fac = new XMLReaderXSDFactory(
				"org.apache.xerces.jaxp.SAXParserFactoryImpl", null,
				new StreamSource(filemain().openStream()),
				new StreamSource(fileone().openStream()),
				new StreamSource(filetwo().openStream()));
		checkXML(fac);
	}

	/* Broken stuff */
	
	@Test
	public void testXMLReaderXSDFactoryStringNull() {
		try {
			String n = null;
			new XMLReaderXSDFactory(n);
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
	}

	@Test
	public void testXMLReaderXSDFactoryURLNull() {
		try {
			URL n = null;
			new XMLReaderXSDFactory(n);
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
	}

	@Test
	public void testXMLReaderXSDFactoryFileNull() {
		try {
			File n = null;
			new XMLReaderXSDFactory(n);
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
	}

	@Test
	public void testXMLReaderXSDFactorySourceNull() {
		try {
			Source n = null;
			new XMLReaderXSDFactory(n);
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
	}


	@Test
	public void testXMLReaderXSDFactoryStringEmpty() {
		try {
			new XMLReaderXSDFactory(new String[0]);
			UnitTestUtil.failNoException(IllegalArgumentException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(IllegalArgumentException.class, e);
		}
	}

	@Test
	public void testXMLReaderXSDFactoryURLEmpty() {
		try {
			new XMLReaderXSDFactory(new URL[0]);
			UnitTestUtil.failNoException(IllegalArgumentException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(IllegalArgumentException.class, e);
		}
	}

	@Test
	public void testXMLReaderXSDFactoryFileEmpty() {
		try {
			new XMLReaderXSDFactory(new File[0]);
			UnitTestUtil.failNoException(IllegalArgumentException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(IllegalArgumentException.class, e);
		}
	}

	@Test
	public void testXMLReaderXSDFactorySourceEmpty() {
		try {
			new XMLReaderXSDFactory(new Source[0]);
			UnitTestUtil.failNoException(IllegalArgumentException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(IllegalArgumentException.class, e);
		}
	}



	@Test
	public void testXMLReaderXSDFactoryStringNullArray() {
		try {
			new XMLReaderXSDFactory((String[])null);
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
	}

	@Test
	public void testXMLReaderXSDFactoryURLNullArray() {
		try {
			new XMLReaderXSDFactory((URL[])null);
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
	}

	@Test
	public void testXMLReaderXSDFactoryFileNullArray() {
		try {
			new XMLReaderXSDFactory((File[])null);
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
	}

	@Test
	public void testXMLReaderXSDFactorySourceNullArray() {
		try {
			new XMLReaderXSDFactory((Source[])null);
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
	}

}
