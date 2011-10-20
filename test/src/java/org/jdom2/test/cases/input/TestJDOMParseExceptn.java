package org.jdom2.test.cases.input;

import static org.junit.Assert.*;

import org.jdom2.Document;
import org.jdom2.input.JDOMParseException;
import org.junit.Test;
import org.xml.sax.SAXParseException;

// Do not use name ending in Exception.
@SuppressWarnings("javadoc")
public class TestJDOMParseExceptn {
	
	private final SAXParseException spe = new SAXParseException("message", "publicID", "systemID", 5, 10);

	@Test
	public void testJDOMParseExceptionStringThrowable() {
		JDOMParseException e = new JDOMParseException("test", spe);
		assertTrue(e.getPartialDocument() == null);
	}

	@Test
	public void testJDOMParseExceptionStringThrowableDocument() {
		Document doc = new Document();
		JDOMParseException e = new JDOMParseException("test", spe, doc);
		assertTrue(e.getPartialDocument() == doc);
	}

	@Test
	public void testGetPartialDocument() {
		Document doc = new Document();
		JDOMParseException e = new JDOMParseException("test", spe, doc);
		assertTrue(e.getPartialDocument() == doc);
	}

	@Test
	public void testGetPublicId() {
		assertEquals("publicID", new JDOMParseException("test", spe).getPublicId());
		assertTrue(null== new JDOMParseException("test", new Exception()).getPublicId());
	}

	@Test
	public void testGetSystemId() {
		assertEquals("systemID", new JDOMParseException("test", spe).getSystemId());
		assertTrue(null== new JDOMParseException("test", new Exception()).getSystemId());
	}

	@Test
	public void testGetLineNumber() {
		assertEquals(5, new JDOMParseException("test", spe).getLineNumber());
		assertTrue(-1 == new JDOMParseException("test", new Exception()).getLineNumber());
	}

	@Test
	public void testGetColumnNumber() {
		assertEquals(10, new JDOMParseException("test", spe).getColumnNumber());
		assertTrue(-1 == new JDOMParseException("test", new Exception()).getColumnNumber());
	}

}
