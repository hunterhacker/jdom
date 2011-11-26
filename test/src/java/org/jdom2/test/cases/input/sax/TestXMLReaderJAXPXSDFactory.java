package org.jdom2.test.cases.input.sax;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaderJAXPXSDFactory;
import org.jdom2.input.sax.XMLReaderJDOMFactory;
import org.jdom2.test.util.UnitTestUtil;

@SuppressWarnings("javadoc")
public class TestXMLReaderJAXPXSDFactory {
	//"./test/resources/xscomplex/multi_one.xsd",
	
	File filemain = new File("./test/resources/xsdcomplex/multi_main.xsd");
	File fileone  = new File("./test/resources/xsdcomplex/multi_one.xsd");
	File filetwo  = new File("./test/resources/xsdcomplex/multi_two.xsd");
	File source   = new File("./test/resources/xsdcomplex/multi.xml");

	private void checkXML(XMLReaderJDOMFactory fac) {
		SAXBuilder builder = new SAXBuilder(fac);
		try {
			Namespace nsmain = Namespace.getNamespace("http://www.jdom.org/schema_main");
			Namespace nsone  = Namespace.getNamespace("http://www.jdom.org/schema_one");
			Namespace nstwo  = Namespace.getNamespace("http://www.jdom.org/schema_two");
			
			Document doc = builder.build(source);
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
	public void testXMLReaderJAXPXSDFactoryStringArray() throws MalformedURLException, JDOMException {
		XMLReaderJDOMFactory fac = new XMLReaderJAXPXSDFactory(
				filemain.toURI().toURL().toExternalForm(),
				fileone.toURI().toURL().toExternalForm(),
				filetwo.toURI().toURL().toExternalForm());
		checkXML(fac);
	}

	@Test
	public void testXMLReaderJAXPXSDFactoryURLArray() throws JDOMException, MalformedURLException {
		XMLReaderJDOMFactory fac = new XMLReaderJAXPXSDFactory(
				filemain.toURI().toURL(),
				fileone.toURI().toURL(),
				filetwo.toURI().toURL());
		checkXML(fac);
	}

	@Test
	public void testXMLReaderJAXPXSDFactoryFileArray() throws JDOMException {
		XMLReaderJDOMFactory fac = new XMLReaderJAXPXSDFactory(
				filemain,
				fileone,
				filetwo);
		checkXML(fac);
	}

	@Test
	public void testXMLReaderJAXPXSDFactorySourceArray() throws JDOMException {
		XMLReaderJDOMFactory fac = new XMLReaderJAXPXSDFactory(
				new StreamSource(filemain),
				new StreamSource(fileone),
				new StreamSource(filetwo));
		checkXML(fac);
	}

	/* Broken stuff */
	
	@Test
	public void testXMLReaderJAXPXSDFactoryStringNull() {
		try {
			String n = null;
			new XMLReaderJAXPXSDFactory(n);
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
	}

	@Test
	public void testXMLReaderJAXPXSDFactoryURLNull() {
		try {
			URL n = null;
			new XMLReaderJAXPXSDFactory(n);
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
	}

	@Test
	public void testXMLReaderJAXPXSDFactoryFileNull() {
		try {
			File n = null;
			new XMLReaderJAXPXSDFactory(n);
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
	}

	@Test
	public void testXMLReaderJAXPXSDFactorySourceNull() {
		try {
			Source n = null;
			new XMLReaderJAXPXSDFactory(n);
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
	}


	@Test
	public void testXMLReaderJAXPXSDFactoryStringEmpty() {
		try {
			new XMLReaderJAXPXSDFactory(new String[0]);
			UnitTestUtil.failNoException(IllegalArgumentException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(IllegalArgumentException.class, e);
		}
	}

	@Test
	public void testXMLReaderJAXPXSDFactoryURLEmpty() {
		try {
			new XMLReaderJAXPXSDFactory(new URL[0]);
			UnitTestUtil.failNoException(IllegalArgumentException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(IllegalArgumentException.class, e);
		}
	}

	@Test
	public void testXMLReaderJAXPXSDFactoryFileEmpty() {
		try {
			new XMLReaderJAXPXSDFactory(new File[0]);
			UnitTestUtil.failNoException(IllegalArgumentException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(IllegalArgumentException.class, e);
		}
	}

	@Test
	public void testXMLReaderJAXPXSDFactorySourceEmpty() {
		try {
			new XMLReaderJAXPXSDFactory(new Source[0]);
			UnitTestUtil.failNoException(IllegalArgumentException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(IllegalArgumentException.class, e);
		}
	}



	@Test
	public void testXMLReaderJAXPXSDFactoryStringNullArray() {
		try {
			new XMLReaderJAXPXSDFactory((String[])null);
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
	}

	@Test
	public void testXMLReaderJAXPXSDFactoryURLNullArray() {
		try {
			new XMLReaderJAXPXSDFactory((URL[])null);
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
	}

	@Test
	public void testXMLReaderJAXPXSDFactoryFileNullArray() {
		try {
			new XMLReaderJAXPXSDFactory((File[])null);
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
	}

	@Test
	public void testXMLReaderJAXPXSDFactorySourceNullArray() {
		try {
			new XMLReaderJAXPXSDFactory((Source[])null);
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
	}

}
