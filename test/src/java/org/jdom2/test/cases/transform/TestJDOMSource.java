package org.jdom2.test.cases.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.SAXOutputter;
import org.jdom2.output.XMLOutputter;
import org.jdom2.transform.JDOMSource;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.helpers.XMLFilterImpl;

@SuppressWarnings("javadoc")
public class TestJDOMSource {

	@Test
	public void testJDOMSourceDocument() {
		Document doc = new Document(new Element("root"));
		doc.setBaseURI("baseURI");
		JDOMSource source = new JDOMSource(doc);
		assertTrue(source.getDocument() == doc);
		// TODO Should the InputSource's SystemID be set?
		//assertEquals("baseURI", source.getInputSource().getSystemId());
	}

	@Test
	public void testJDOMSourceList() {
		List<Element> nodes = new ArrayList<Element>();
		nodes.add(new Element("nodea"));
		nodes.add(new Element("nodeb"));
		JDOMSource source = new JDOMSource(nodes);
		assertTrue(nodes == source.getNodes());
	}

	@Test
	public void testJDOMSourceElement() {
		Element emt = new Element("emt");
		JDOMSource source = new JDOMSource(emt);
		assertTrue(emt == source.getNodes().get(0));
	}

	@Test
	public void testJDOMSourceDocumentEntityResolver() {
		DefaultHandler2 handler = new DefaultHandler2();
		Document doc = new Document(new Element("root"));
		JDOMSource source = new JDOMSource(doc, handler);
		assertTrue(doc == source.getDocument());
		assertTrue(handler == source.getXMLReader().getEntityResolver());
	}

	@Test
	public void testGetSetDocument() {
		Element emt = new Element("emt");
		Document doc = new Document(new Element("root"));
		JDOMSource source = new JDOMSource(emt);
		assertTrue(null == source.getDocument());
		assertTrue(emt == source.getNodes().get(0));
		source.setDocument(doc);
		assertTrue(doc == source.getDocument());
		assertTrue(null == source.getNodes());
	}

	@Test
	public void testGetSetNodes() {
		Element emt = new Element("emt");
		Document doc = new Document(new Element("root"));
		JDOMSource source = new JDOMSource(doc);
		assertTrue(doc == source.getDocument());
		assertTrue(null == source.getNodes());
		source.setNodes(Collections.singletonList(emt));
		assertTrue(null == source.getDocument());
		assertTrue(emt == source.getNodes().get(0));
	}

	@Test
	public void testGetSetInputSourceInputSource() {
		Document doc = new Document(new Element("root"));
		JDOMSource source = new JDOMSource(doc);
		InputSource insrc = new InputSource("url");
		try {
			source.setInputSource(insrc);
			fail("Should not be able to change the InputSource");
		} catch (UnsupportedOperationException uoe) {
			// good
		} catch (Exception e) {
			fail("Expected exception UnsupportedOperationException, but got " + e.getClass());
		}
	}

	@Test
	public void testGetSetXMLReaderXMLReader() {
		Document doc = new Document(new Element("root"));
		JDOMSource source = new JDOMSource(doc);
		try {
			XMLReader f = (XMLReader)Proxy.newProxyInstance(null, 
					new Class<?>[]{XMLReader.class}, new InvocationHandler() {
				@Override
				public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
					if (String.class == method.getReturnType()) {
						return "XMLFilter Proxy";
					}
					return null;
				}
			});
			source.setXMLReader(f);
			fail("Should not be able to change the XMLReader");
		} catch (UnsupportedOperationException uoe) {
			// good
		} catch (Exception e) {
			e.printStackTrace();
			fail("Expected exception UnsupportedOperationException, but got " + e.getClass());
		}
		
		// but, you can add a Filter.
		XMLReader pnt = new XMLFilterImpl();
		XMLReader filter = new XMLFilterImpl(pnt);
		source.setXMLReader(filter);
		XMLReader r = source.getXMLReader();
		assertTrue(filter == r);
	}
	
	@Test
	public void testInputSourceSetters() {
		JDOMSource source = new JDOMSource(new Document(new Element("root")));
		InputSource isrc = source.getInputSource();
		try {
			isrc.setCharacterStream(new CharArrayReader(new char[0]));
			fail("should not be able to set the CharacterStream");
		} catch (UnsupportedOperationException uoe) {
			// good
		} catch (Exception e) {
			fail ("Expected Unsupported operation exception, not " + e.getClass());
		}
		try {
			isrc.setByteStream(new ByteArrayInputStream(new byte[0]));
			fail("should not be able to set the ByteStream");
		} catch (UnsupportedOperationException uoe) {
			// good
		} catch (Exception e) {
			fail ("Expected Unsupported operation exception, not " + e.getClass());
		}
	}

	@Test
	public void testInputSourceByteStream() {
		JDOMSource source = new JDOMSource(new Document(new Element("root")));
		assertNull(source.getInputSource().getByteStream());
	}

	@Test
	public void testInputSourceDocCharacterStream() throws IOException {
		JDOMSource source = new JDOMSource(new Document(new Element("root")));
		Document doc = source.getDocument();
		String expect = new XMLOutputter().outputString(doc);
		Reader r = source.getInputSource().getCharacterStream();
		StringWriter sw = new StringWriter();
		char[] buffer = new char[512];
		int len = 0;
		while ((len = r.read(buffer)) >= 0) {
			sw.write(buffer, 0, len);
		}
		r.close();
		String actual = sw.toString();
		assertEquals(expect, actual);
	}

	@Test
	public void testInputSourceListCharacterStream() throws IOException {
		JDOMSource source = new JDOMSource(new Element("root"));
		Element emt = (Element)source.getNodes().get(0);
		String expect = new XMLOutputter().outputString(emt);
		Reader r = source.getInputSource().getCharacterStream();
		StringWriter sw = new StringWriter();
		char[] buffer = new char[512];
		int len = 0;
		while ((len = r.read(buffer)) >= 0) {
			sw.write(buffer, 0, len);
		}
		r.close();
		String actual = sw.toString();
		assertEquals(expect, actual);
	}

	@Test
	public void testInputSourceNullCharacterStream() {
		JDOMSource source = new JDOMSource((Document)null);
		Reader r = source.getInputSource().getCharacterStream();
		assertNull(r);
	}

	@Test
	public void testXMLReaderParse() {
		JDOMSource source = new JDOMSource(new Document(new Element("root")));
		XMLReader reader = source.getXMLReader();
		try {
			reader.parse(new InputSource("someurl"));
			fail ("Should not be able to read a non-JDOM input source.");
		} catch (SAXNotSupportedException sne) {
			// good;
		} catch (Exception e) {
			e.printStackTrace();
			fail ("Expecting a SAXNotSupportedExcetpion, but got " + e.getClass());
		}
		
	}
	
	@Test
	public void testXMLReaderParseSystemID() {
		JDOMSource source = new JDOMSource(new Document(new Element("root")));
		XMLReader reader = source.getXMLReader();
		try {
			reader.parse("systemID");
			fail ("Should not be able to read a SystemID.");
		} catch (SAXNotSupportedException sne) {
			// good;
		} catch (Exception e) {
			e.printStackTrace();
			fail ("Expecting a SAXNotSupportedExcetpion, but got " + e.getClass());
		}
	}

	@Test
	public void testXMLReaderParseOtherInputSource() {
		JDOMSource source = new JDOMSource(new Document(new Element("root")));
		XMLReader reader = source.getXMLReader();
		try {
			reader.parse(new InputSource("someurl"));
			fail ("Should not be able to read a non-JDOM input source.");
		} catch (SAXNotSupportedException sne) {
			// good;
		} catch (Exception e) {
			e.printStackTrace();
			fail ("Expecting a SAXNotSupportedExcetpion, but got " + e.getClass());
		}		
	}

	@Test
	public void testXMLReaderParseJDOMDocumentInputSource() throws SAXException, IOException {
		JDOMSource sourcea = new JDOMSource(new Document(new Element("root")));
		JDOMSource sourceb = new JDOMSource(new Document(new Element("fubar")));
		XMLReader reader = sourcea.getXMLReader();
		assertTrue(reader instanceof SAXOutputter);
		SAXOutputter readeroutputter = (SAXOutputter)reader;
		final AtomicReference<String> emtname = new AtomicReference<String>(null);
		
		DefaultHandler2 handler = new DefaultHandler2() {
			@Override
			public void startElement(String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {
				emtname.set(arg1);
			}
		};
		
		readeroutputter.setContentHandler(handler);
		
		reader.parse(sourceb.getInputSource());
		
		assertEquals("fubar", emtname.get());

	}

	@Test
	public void testXMLReaderParseJDOMElementInputSource() throws SAXException, IOException {
		JDOMSource sourcea = new JDOMSource(new Element("root"));
		JDOMSource sourceb = new JDOMSource(new Element("fubar"));
		XMLReader reader = sourcea.getXMLReader();
		assertTrue(reader instanceof SAXOutputter);
		SAXOutputter readeroutputter = (SAXOutputter)reader;
		final AtomicReference<String> emtname = new AtomicReference<String>(null);
		
		DefaultHandler2 handler = new DefaultHandler2() {
			@Override
			public void startElement(String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {
				emtname.set(arg1);
			}
		};
		
		readeroutputter.setContentHandler(handler);
		
		reader.parse(sourceb.getInputSource());
		
		assertEquals("fubar", emtname.get());

	}

	@Test
	public void testXMLReaderParseGarbageInputSource() {
		
		JDOMSource sourcea = new JDOMSource(new Element("root"));
		List<Double> junk = Collections.singletonList(Double.valueOf(123.4));
		// Jump through hoops to defeat Generics.
		@SuppressWarnings("cast")
		List<?> tmpa = (List<?>)junk;
		@SuppressWarnings("unchecked")
		List<Content> tmpb = (List<Content>)tmpa;
		JDOMSource sourceb = new JDOMSource(tmpb);
		XMLReader reader = sourcea.getXMLReader();
		assertTrue(reader instanceof SAXOutputter);
		SAXOutputter readeroutputter = (SAXOutputter)reader;
		
		DefaultHandler2 handler = new DefaultHandler2();
		
		readeroutputter.setContentHandler(handler);
		
		try {
			reader.parse(sourceb.getInputSource());
			fail ("Should not be able to parse garbage data 123.4");
		} catch (ClassCastException se) {
			// good!
		} catch (Exception e) {
			e.printStackTrace();
			fail("Expecting SAXException, but got " + e.getClass());
		}
		
	}
}
