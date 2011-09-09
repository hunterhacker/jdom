package org.jdom2.test.cases.input;

import static org.junit.Assert.*;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

import org.jdom2.Attribute;
import org.jdom2.DefaultJDOMFactory;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.DOMBuilder;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;

public class TestDOMBuilder {

	@Test
	public void testDOMBuilder() {
		DOMBuilder db = new DOMBuilder();
		assertNotNull(db);
	}

	@Test
	public void testDOMBuilderString() {
		@SuppressWarnings("deprecation")
		DOMBuilder db = new DOMBuilder("doesnothing");
		assertNotNull(db);
	}

	@Test
	public void testFactory() {
		DOMBuilder db = new DOMBuilder();
		assertTrue(db.getFactory() instanceof DefaultJDOMFactory);
		DefaultJDOMFactory fac = new DefaultJDOMFactory();
		assertFalse(db.getFactory() == fac);
		db.setFactory(fac);
		assertTrue(db.getFactory() == fac);
	}
	
	@Test
	public void testSimpleDocument() {
		checkDOM("test/resources/DOMBuilder/simple.xml", false);
	}
	
	@Test
	public void testAttributesDocument() {
		checkDOM("test/resources/DOMBuilder/attributes.xml", false);
	}
	
	@Test
	public void testNamespaceDocument() {
		checkDOM("test/resources/DOMBuilder/namespaces.xml", false);
	}
	
	@Test
	public void testDocTypeDocument() {
		checkDOM("test/resources/DOMBuilder/doctype.xml", false);
	}
	
	@Test
	public void testComplexDocument() {
		checkDOM("test/resources/DOMBuilder/complex.xml", false);
	}
	
	@Test
	public void testXSDDocument() {
		checkDOM("test/resources/xsdcomplex/input.xml", true);
	}
	
	private void checkDOM(String filename, boolean xsdvalidate) {
		try {
			org.w3c.dom.Document domdoc = HelpTestDOMBuilder.getDocument(filename, xsdvalidate);
			DOMBuilder db = new DOMBuilder();
			Document dombuild = db.build(domdoc);
			Element domroot = db.build(HelpTestDOMBuilder.getRoot(domdoc));
			
			SAXBuilder sb = new SAXBuilder(false);
			sb.setExpandEntities(false);
			sb.setFeature("http://xml.org/sax/features/namespaces", true);
			sb.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
			sb.setFeature("http://apache.org/xml/features/validation/schema", xsdvalidate);
			
			Document saxbuild = sb.build(filename);
			Element saxroot = saxbuild.hasRootElement() ? saxbuild.getRootElement() : null;
			
			assertEquals(toString(saxbuild), toString(dombuild));
			assertEquals(toString(saxroot), toString(domroot));
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Could not parse file '" + filename + "': " + e.getMessage());
		}
	}
	
	private void normalizeAttributes(Element emt) {
		TreeMap<String,Attribute> sorted = new TreeMap<String, Attribute>();
		List<?> atts = emt.getAttributes();
		for (Object o : atts.toArray()) {
			Attribute a = (Attribute)o;
			sorted.put(a.getQualifiedName(), a);
			a.detach();
		}
		for (Attribute a : sorted.values()) {
			emt.setAttribute(a);
		}
		for (Object o : emt.getChildren()) {
			normalizeAttributes((Element)o);
		}
	}
	
	private void normalizeDTD(DocType dt) {
		if (dt == null) {
			return;
		}
		// do some tricks so that we can compare the results.
		// these may well break the actual syntax of DTD's but for testing
		// purposes it is OK.
		String internalss = dt.getInternalSubset().trim() ;
		// the spaceing in and around the internal subset is different between
		// our SAX parse, and the DOM parse.
		// make all whitespace a single space.
		internalss = internalss.replaceAll("\\s+", " ");
		// It seems the DOM parser internally quotes entities with single quote
		// but our sax parser uses double-quote.
		// simply replace all " with ' and be done with it.
		internalss = internalss.replaceAll("\"", "'");
		dt.setInternalSubset("\n" + internalss + "\n");
	}
	
	private String toString(Document doc) {
		normalizeAttributes(doc.getRootElement());
		normalizeDTD(doc.getDocType());
		XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
		CharArrayWriter caw = new CharArrayWriter();
		try {
			out.output(doc, caw);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return caw.toString();
	}

	private String toString(Element emt) {
		normalizeAttributes(emt);
		XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
		CharArrayWriter caw = new CharArrayWriter();
		try {
			out.output(emt, caw);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return caw.toString();
	}

}
