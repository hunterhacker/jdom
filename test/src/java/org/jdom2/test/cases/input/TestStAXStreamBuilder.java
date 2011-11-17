package org.jdom2.test.cases.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.junit.Ignore;
import org.junit.Test;

import org.jdom2.Content;
import org.jdom2.DefaultJDOMFactory;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.DefaultStAXFilter;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.StAXStreamBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.test.util.UnitTestUtil;

@SuppressWarnings("javadoc")
public class TestStAXStreamBuilder {

	@Test
	public void testStAXBuilder() {
		StAXStreamBuilder db = new StAXStreamBuilder();
		assertNotNull(db);
	}

	@Test
	public void testFactory() {
		StAXStreamBuilder db = new StAXStreamBuilder();
		assertTrue(db.getFactory() instanceof DefaultJDOMFactory);
		DefaultJDOMFactory fac = new DefaultJDOMFactory();
		assertFalse(db.getFactory() == fac);
		db.setFactory(fac);
		assertTrue(db.getFactory() == fac);
	}
	
	@Test
	public void testSimpleDocumentExpand() {
		checkStAX("test/resources/DOMBuilder/simple.xml", true);
	}
	
	@Test
	public void testAttributesDocumentExpand() {
		checkStAX("test/resources/DOMBuilder/attributes.xml", true);
	}
	
	@Test
	public void testNamespaceDocumentExpand() {
		checkStAX("test/resources/DOMBuilder/namespaces.xml", true);
	}
	
	@Test
	@Ignore
	public void testDocTypeDocumentExpand() {
		checkStAX("test/resources/DOMBuilder/doctype.xml", true);
	}
	
	@Test
	@Ignore
	public void testDocTypeDocumentSimpleExpand() {
		checkStAX("test/resources/DOMBuilder/doctypesimple.xml", true);
	}
	
	@Test
	public void testComplexDocumentExpand() {
		checkStAX("test/resources/DOMBuilder/complex.xml", true);
	}
	
	@Test
	public void testXSDDocumentExpand() {
		checkStAX("test/resources/xsdcomplex/input.xml", true);
	}
	
	@Test
	public void testSimpleDocument() {
		checkStAX("test/resources/DOMBuilder/simple.xml", false);
	}
	
	@Test
	public void testAttributesDocument() {
		checkStAX("test/resources/DOMBuilder/attributes.xml", false);
	}
	
	@Test
	public void testNamespaceDocument() {
		checkStAX("test/resources/DOMBuilder/namespaces.xml", false);
	}
	
	@Test
	public void testDocTypeDocument() {
		checkStAX("test/resources/DOMBuilder/doctype.xml", false);
	}
	
	@Test
	public void testDocTypeSimpleDocument() {
		checkStAX("test/resources/DOMBuilder/doctypesimple.xml", false);
	}
	
	@Test
	public void testComplexDocument() {
		checkStAX("test/resources/DOMBuilder/complex.xml", false);
	}
	
	@Test
	public void testXSDDocument() {
		checkStAX("test/resources/xsdcomplex/input.xml", false);
	}
	
	private void checkStAX(String filename, boolean expand) {
		try {
			StAXStreamBuilder stxb = new StAXStreamBuilder();
			FileReader source = new FileReader(new File(filename));
			XMLInputFactory inputfac = XMLInputFactory.newInstance();
			inputfac.setProperty(
					"javax.xml.stream.isReplacingEntityReferences", Boolean.valueOf(expand));
			inputfac.setProperty("http://java.sun.com/xml/stream/properties/report-cdata-event", Boolean.TRUE);
			XMLStreamReader reader = inputfac.createXMLStreamReader(source);
			Document staxbuild = stxb.build(reader);
			Element staxroot = staxbuild.hasRootElement() ? staxbuild.getRootElement() : null;
			
			source = new FileReader(new File(filename));
			XMLStreamReader fragreader = inputfac.createXMLStreamReader(source);
			List<Content> contentlist = stxb.buildFragments(fragreader, new DefaultStAXFilter());
			Document fragbuild = new Document();
			fragbuild.addContent(contentlist);
			Element fragroot = fragbuild.getRootElement();

			SAXBuilder sb = new SAXBuilder(false);
			sb.setExpandEntities(expand);
			sb.setFeature("http://xml.org/sax/features/namespaces", true);
			sb.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
			
			Document saxbuild = sb.build(filename);
			Element saxroot = saxbuild.hasRootElement() ? saxbuild.getRootElement() : null;
			
			assertEquals("DOC SAX to StAXReader", toString(saxbuild), toString(staxbuild));
			assertEquals("ROOT SAX to StAXReader", toString(saxroot), toString(staxroot));
			assertEquals("DOC SAX to StAXReader FragmentList", toString(saxbuild), toString(fragbuild));
			assertEquals("ROOT SAX to StAXReader FragmentList", toString(saxroot), toString(fragroot));
			
		} catch (Exception e) {
			UnitTestUtil.failException("Could not parse file '" + filename + "': " + e.getMessage(), e);
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
		UnitTestUtil.normalizeAttributes(doc.getRootElement());
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
		UnitTestUtil.normalizeAttributes(emt);
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
