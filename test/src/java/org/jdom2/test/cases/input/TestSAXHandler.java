package org.jdom2.test.cases.input;

import static org.junit.Assert.*;

import org.jdom2.Comment;
import org.jdom2.DefaultJDOMFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMFactory;
import org.jdom2.input.SAXHandler;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.Attributes2Impl;
import org.xml.sax.helpers.AttributesImpl;

public class TestSAXHandler {
	
	private static final Attributes EmptyAttributes = new Attributes2Impl() {
		
	};

	@Test
	public void testDocument() {
		try {
			SAXHandler handler = new SAXHandler();
			handler.startDocument();
			handler.endDocument();
			Document doc = handler.getDocument();
			assertTrue(doc.getDocType() == null);
			assertFalse(doc.hasRootElement());
		} catch (SAXException se) {
			se.printStackTrace();
			fail("Failed testDocument() with SAXException: " + se.getMessage());
		}
		
		try {
			JDOMFactory fac = new DefaultJDOMFactory();
			SAXHandler handler = new SAXHandler(fac);
			handler.startDocument();
			handler.endDocument();
			Document doc = handler.getDocument();
			assertTrue(doc.getDocType() == null);
			assertFalse(doc.hasRootElement());
			assertTrue(fac == handler.getFactory());
		} catch (SAXException se) {
			se.printStackTrace();
			fail("Failed testDocument() with SAXException: " + se.getMessage());
		}
		
		try {
			SAXHandler handler = new SAXHandler();
			handler.startDocument();
			handler.startDTD("dtdname", "publicID", "systemID");
			handler.endDTD();
			handler.endDocument();
			Document doc = handler.getDocument();
			assertFalse(doc.hasRootElement());
			assertTrue(doc.getDocType() != null);
			assertEquals("dtdname", doc.getDocType().getElementName());
		} catch (SAXException se) {
			se.printStackTrace();
			fail("Failed testDocument() with SAXException: " + se.getMessage());
		}

		try {
			SAXHandler handler = new SAXHandler();
			handler.startDocument();
			handler.startDTD("dtdname", "publicID", "systemID");
			handler.endDTD();
			handler.comment("comment".toCharArray(), 2, 2);
			handler.endDocument();
			Document doc = handler.getDocument();
			assertFalse(doc.hasRootElement());
			assertTrue(doc.getDocType() != null);
			assertEquals("dtdname", doc.getDocType().getElementName());
			assertTrue(doc.getContent(1) instanceof Comment);
			assertEquals("mm", ((Comment)doc.getContent(1)).getText());
		} catch (SAXException se) {
			se.printStackTrace();
			fail("Failed testDocument() with SAXException: " + se.getMessage());
		}

		try {
			SAXHandler handler = new SAXHandler();
			handler.startDocument();
			handler.startDTD("dtdname", "publicID", "systemID");
			handler.endDTD();
			handler.comment("comment".toCharArray(), 2, 2);
			handler.startElement("", "root", "", new AttributesImpl());
			handler.endDocument();
			Document doc = handler.getDocument();
			assertTrue(doc.hasRootElement());
			assertTrue(doc.getDocType() != null);
			assertEquals("dtdname", doc.getDocType().getElementName());
			assertTrue(doc.getContent(1) instanceof Comment);
			assertEquals("mm", ((Comment)doc.getContent(1)).getText());
			assertTrue(doc.getContent(2) instanceof Element);
			assertEquals("root", ((Element)doc.getContent(2)).getName());
		} catch (SAXException se) {
			se.printStackTrace();
			fail("Failed testDocument() with SAXException: " + se.getMessage());
		}
	}

	@Test
	public void testSetExpandEntities() {
		SAXHandler handler = new SAXHandler();
		assertTrue(handler.getExpandEntities());
		handler.setExpandEntities(true);
		assertTrue(handler.getExpandEntities());
		handler.setExpandEntities(false);
		assertFalse(handler.getExpandEntities());
		handler.setExpandEntities(true);
		assertTrue(handler.getExpandEntities());
	}

	@Test
	public void testSetIgnoringElementContentWhitespace() {
		SAXHandler handler = new SAXHandler();
		assertFalse(handler.getIgnoringElementContentWhitespace());
		handler.setIgnoringElementContentWhitespace(true);
		assertTrue(handler.getIgnoringElementContentWhitespace());
		handler.setIgnoringElementContentWhitespace(false);
		assertFalse(handler.getIgnoringElementContentWhitespace());
		handler.setIgnoringElementContentWhitespace(true);
		assertTrue(handler.getIgnoringElementContentWhitespace());
	}

	@Test
	public void testSetIgnoringBoundaryWhitespace() {
		SAXHandler handler = new SAXHandler();
		assertFalse(handler.getIgnoringBoundaryWhitespace());
		handler.setIgnoringBoundaryWhitespace(true);
		assertTrue(handler.getIgnoringBoundaryWhitespace());
		handler.setIgnoringBoundaryWhitespace(false);
		assertFalse(handler.getIgnoringBoundaryWhitespace());
		handler.setIgnoringBoundaryWhitespace(true);
		assertTrue(handler.getIgnoringBoundaryWhitespace());
	}

	/*
	@Test
	public void testCharacters() {
		fail("Not yet implemented");
	}

	@Test
	public void testIgnorableWhitespace() {
		fail("Not yet implemented");
	}

	@Test
	public void testPushElement() {
		fail("Not yet implemented");
	}

	@Test
	public void testExternalEntityDecl() {
		fail("Not yet implemented");
	}

	@Test
	public void testAttributeDecl() {
		fail("Not yet implemented");
	}

	@Test
	public void testElementDecl() {
		fail("Not yet implemented");
	}

	@Test
	public void testInternalEntityDecl() {
		fail("Not yet implemented");
	}

	@Test
	public void testProcessingInstructionStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testSkippedEntityString() {
		fail("Not yet implemented");
	}

	@Test
	public void testStartPrefixMappingStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testStartElementStringStringStringAttributes() {
		fail("Not yet implemented");
	}

	@Test
	public void testFlushCharacters() {
		fail("Not yet implemented");
	}

	@Test
	public void testFlushCharactersString() {
		fail("Not yet implemented");
	}

	@Test
	public void testEndElementStringStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testStartDTD() {
		fail("Not yet implemented");
	}

	@Test
	public void testEndDTD() {
		fail("Not yet implemented");
	}

	@Test
	public void testStartEntity() {
		fail("Not yet implemented");
	}

	@Test
	public void testEndEntity() {
		fail("Not yet implemented");
	}

	@Test
	public void testStartCDATA() {
		fail("Not yet implemented");
	}

	@Test
	public void testEndCDATA() {
		fail("Not yet implemented");
	}

	@Test
	public void testComment() {
		fail("Not yet implemented");
	}

	@Test
	public void testNotationDeclStringStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testUnparsedEntityDeclStringStringStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCurrentElement() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetDocumentLocatorLocator() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDocumentLocator() {
		fail("Not yet implemented");
	}
*/
}
