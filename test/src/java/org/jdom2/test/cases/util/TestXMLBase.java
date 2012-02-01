package org.jdom2.test.cases.util;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.test.util.UnitTestUtil;
import org.jdom2.util.XMLBase;

@SuppressWarnings("javadoc")
public class TestXMLBase {

	@Test
	public void testXmlBaseNone() throws URISyntaxException {
		Document doc = new Document();
		Element root = new Element("root");
		doc.setRootElement(root);
		assertTrue(null == XMLBase.xmlBase(root));
	}

	@Test
	public void testXmlBaseDocument() throws URISyntaxException {
		Document doc = new Document();
		doc.setBaseURI("http://jdom.org/");
		Element root = new Element("root");
		doc.setRootElement(root);
		URI uri = XMLBase.xmlBase(root);
		assertTrue(uri != null);
		assertEquals("http://jdom.org/", uri.toASCIIString());
	}

	@Test
	public void testXmlBaseRelative() throws URISyntaxException {
		Document doc = new Document();
		Element root = new Element("root");
		doc.setRootElement(root);
		root.setAttribute("base", "./sub/", Namespace.XML_NAMESPACE);
		URI uri = XMLBase.xmlBase(root);
		assertTrue(uri != null);
		assertEquals("./sub/", uri.toASCIIString());
	}

	@Test
	public void testXmlBaseRelativeToDoc() throws URISyntaxException {
		Document doc = new Document();
		doc.setBaseURI("http://jdom.org/");
		Element root = new Element("root");
		doc.setRootElement(root);
		root.setAttribute("base", "./sub/", Namespace.XML_NAMESPACE);
		URI uri = XMLBase.xmlBase(root);
		assertTrue(uri != null);
		assertEquals("http://jdom.org/sub/", uri.toASCIIString());
	}

	@Test
	public void testXmlBaseAbsolute() throws URISyntaxException {
		Document doc = new Document();
		doc.setBaseURI("http://jdom.org/some/path/to/low/level");
		Element root = new Element("root");
		doc.setRootElement(root);
		root.setAttribute("base", "/sub/", Namespace.XML_NAMESPACE);
		URI uri = XMLBase.xmlBase(root);
		assertTrue(uri != null);
		assertEquals("http://jdom.org/sub/", uri.toASCIIString());
	}

	@Test
	public void testXmlBaseSubAbsolute() throws URISyntaxException {
		Document doc = new Document();
		doc.setBaseURI("http://jdom.org/some/path/to/low/level");
		Element root = new Element("root");
		doc.setRootElement(root);
		root.setAttribute("base", "http://jdom2.org/sub/", Namespace.XML_NAMESPACE);
		URI uri = XMLBase.xmlBase(root);
		assertTrue(uri != null);
		assertEquals("http://jdom2.org/sub/", uri.toASCIIString());
	}

	@Test
	public void testXmlBaseBroken() {
		Document doc = new Document();
		doc.setBaseURI("http://jdom.org/");
		Element root = new Element("root");
		doc.setRootElement(root);
		root.setAttribute("base", "../  /sub/", Namespace.XML_NAMESPACE);
		try {
			XMLBase.xmlBase(root);
			UnitTestUtil.failNoException(URISyntaxException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(URISyntaxException.class, e);
		}
	}

}
