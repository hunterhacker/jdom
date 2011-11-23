package org.jdom2.test.cases.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.NoSuchElementException;

import javax.xml.XMLConstants;

import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.ext.Attributes2;
import org.xml.sax.helpers.LocatorImpl;

import org.jdom2.Attribute;
import org.jdom2.AttributeType;
import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.DefaultJDOMFactory;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.EntityRef;
import org.jdom2.JDOMFactory;
import org.jdom2.Namespace;
import org.jdom2.ProcessingInstruction;
import org.jdom2.filter.ContentFilter;
import org.jdom2.input.sax.SAXHandler;

@SuppressWarnings("javadoc")
public class TestSAXHandler {
	
	private static final class AttributesSingleOnly implements Attributes2 {

		private final String uri, localName, qName, type, value;
		
		public AttributesSingleOnly(String uri, String localName, String qName, String type, String value)  {
			this.uri = uri;
			this.localName = localName;
			this.qName = qName;
			this.type = type;
			this.value = value;
		}
		
		private final boolean areEquals(Object a, Object b) {
			if (a == null && b == null) {
				return true;
			}
			if (a != null) {
				return a.equals(b);
			}
			return false;
		}
		
		@Override
		public int getIndex(String puri, String plocalName) {
			return areEquals(uri, puri) && areEquals(localName, plocalName) ?
					0 : -1;
		}

		@Override
		public int getIndex(String pqName) {
			return areEquals(qName, pqName) ? 0 : -1;
		}

		@Override
		public int getLength() {
			return 1;
		}

		@Override
		public String getLocalName(int index) {
			if (index == 0) {
				return localName;
			}
			throw new NoSuchElementException();
		}

		@Override
		public String getQName(int index) {
			if (index == 0) {
				return qName;
			}
			throw new NoSuchElementException();
		}

		@Override
		public String getType(int index) {
			if (index == 0) {
				return type;
			}
			throw new NoSuchElementException();
		}

		@Override
		public String getType(String puri, String plocalName) {
			return getType(getIndex(puri, plocalName));
		}

		@Override
		public String getType(String pqName) {
			return getType(getIndex(pqName));
		}

		@Override
		public String getURI(int index) {
			if (index == 0) {
				return uri;
			}
			throw new NoSuchElementException();
		}

		@Override
		public String getValue(int index) {
			if (index == 0) {
				return value;
			}
			throw new NoSuchElementException();
		}

		@Override
		public String getValue(String puri, String plocalName) {
			return getValue(getIndex(puri, plocalName));
		}

		@Override
		public String getValue(String pqName) {
			return getType(getIndex(pqName));
		}

		@Override
		public boolean isDeclared(int index) {
			if (index == 0) {
				return true;
			}
			throw new NoSuchElementException();
		}

		@Override
		public boolean isDeclared(String puri, String plocalName) {
			return isDeclared(getIndex(puri, plocalName));
		}

		@Override
		public boolean isDeclared(String pqName) {
			return isDeclared(getIndex(pqName));
		}

		@Override
		public boolean isSpecified(int index) {
			if (index == 0) {
				return true;
			}
			throw new NoSuchElementException();
		}

		@Override
		public boolean isSpecified(String puri, String plocalName) {
			return isSpecified(getIndex(puri, plocalName));
		}

		@Override
		public boolean isSpecified(String pqName) {
			return isSpecified(getIndex(pqName));
		}

	}
	
	private class MyHandler extends SAXHandler {
		private MyHandler () {
			super();
		}
		@Override
		public void pushElement(Element element) {
			super.pushElement(element);
		}
	}

	private static final Attributes2 EMPTYATTRIBUTES = new org.xml.sax.ext.Attributes2Impl();

	private static final void assertMatches(String pattern, String value) {
		assertTrue("Pattern for assertMatches is null", pattern != null);
		assertTrue("Value for assertMatches is null", value != null);
		if (!value.matches(pattern)) {
			fail("Value '" + value + "' does not match pattern '" + pattern +".");
		}
	}
	
	private abstract class Builder {
		public SAXHandler createHandler() {
			return new SAXHandler();
		}
		
		public abstract void build(SAXHandler handler) throws SAXException;
	}

	private static final Document checkHandlerDocument(Builder cd) {
		try {
			SAXHandler handler = cd.createHandler();
			handler.startDocument();
			cd.build(handler);
			handler.endDocument();
			return handler.getDocument();
		} catch (SAXException se) {
			se.printStackTrace();
			fail("Failed TestSAXHandler with SAXException: " + se.getMessage());
		}
		return null;

	}

	private static final Element checkHandlerElement(Builder cd) {
		try {
			SAXHandler handler = cd.createHandler();
			handler.startDocument();
			handler.startElement("", "root", "root", EMPTYATTRIBUTES);
			cd.build(handler);
			handler.endElement("", "root", "root");
			handler.endDocument();
			return handler.getDocument().getRootElement();
		} catch (SAXException se) {
			se.printStackTrace();
			fail("Failed TestSAXHandler with SAXException: " + se.getMessage());
		}
		return null;

	}

	private static final String checkHandlerDTDInternalSubset(Builder cd) {
		try {
			SAXHandler handler = cd.createHandler();
			handler.startDocument();
			handler.startDTD("root", "publicID", "systemID");
			cd.build(handler);
			handler.endDTD();
			handler.endDocument();
			return handler.getDocument().getDocType().getInternalSubset().trim(); //.replaceAll("(^\\s*<\\s*)|(\\s*>\\s*$)", "");
		} catch (SAXException se) {
			se.printStackTrace();
			fail("Failed TestSAXHandler with SAXException: " + se.getMessage());
		}
		return null;

	}

	@Test
	public void testDocument() {
		Document doc = null;
		
		doc = checkHandlerDocument(new Builder() {
			@Override
			public void build(SAXHandler handler) throws SAXException {
				// do nothing.
			}
		});
		assertTrue(doc.getDocType() == null);
		assertFalse(doc.hasRootElement());

		final JDOMFactory deffac = new DefaultJDOMFactory();
		doc = checkHandlerDocument(new Builder() {
			@Override
			public SAXHandler createHandler() {
				return new SAXHandler(deffac);
			}
			@Override
			public void build(SAXHandler handler) throws SAXException {
				assertTrue(deffac == handler.getFactory());
			}
		});
		assertTrue(doc.getDocType() == null);
		assertFalse(doc.hasRootElement());

		doc = checkHandlerDocument(new Builder() {
			@Override
			public void build(SAXHandler handler) throws SAXException {
				handler.startDTD("dtdname", "publicID", "systemID");
				handler.endDTD();
			}
		});
		assertFalse(doc.hasRootElement());
		assertTrue(doc.getDocType() != null);
		assertEquals("dtdname", doc.getDocType().getElementName());
		
		doc = checkHandlerDocument(new Builder() {
			@Override
			public void build(SAXHandler handler) throws SAXException {
				handler.startDTD("dtdname", "publicID", "systemID");
				handler.endDTD();
				handler.comment("comment".toCharArray(), 2, 2);
			}
		});
		assertFalse(doc.hasRootElement());
		assertTrue(doc.getDocType() != null);
		assertEquals("dtdname", doc.getDocType().getElementName());
		assertTrue(doc.getContent(1) instanceof Comment);
		assertEquals("mm", ((Comment)doc.getContent(1)).getText());

		doc = checkHandlerDocument(new Builder() {
			@Override
			public void build(SAXHandler handler) throws SAXException {
				handler.startDTD("dtdname", "publicID", "systemID");
				handler.endDTD();
				handler.comment("comment".toCharArray(), 2, 2);
				handler.startElement("", "root", "", EMPTYATTRIBUTES);
			}
		});
		assertTrue(doc.hasRootElement());
		assertTrue(doc.getDocType() != null);
		assertEquals("dtdname", doc.getDocType().getElementName());
		assertTrue(doc.getContent(1) instanceof Comment);
		assertEquals("mm", ((Comment)doc.getContent(1)).getText());
		assertTrue(doc.getContent(2) instanceof Element);
		assertEquals("root", ((Element)doc.getContent(2)).getName());

		final LocatorImpl loc = new LocatorImpl();
		loc.setSystemId("baseURL");
		final SAXHandler handler = new SAXHandler();
		handler.setDocumentLocator(loc);
		doc = checkHandlerDocument(new Builder() {
			@Override
			public SAXHandler createHandler() {
				return handler;
			}
			@Override
			public void build(SAXHandler phandler) throws SAXException {
				phandler.startDTD("dtdname", "publicID", "systemID");
				phandler.endDTD();
				phandler.comment("comment".toCharArray(), 2, 2);
				phandler.startElement("", "root", "", EMPTYATTRIBUTES);
			}
		});
		assertTrue(doc.hasRootElement());
		assertTrue(doc.getDocType() != null);
		assertEquals("dtdname", doc.getDocType().getElementName());
		assertTrue(doc.getContent(1) instanceof Comment);
		assertEquals("mm", ((Comment)doc.getContent(1)).getText());
		assertTrue(doc.getContent(2) instanceof Element);
		assertEquals("root", ((Element)doc.getContent(2)).getName());
		assertEquals("baseURL", doc.getBaseURI());
		assertTrue(loc == handler.getDocumentLocator());
		
	}

	@Test
	public void testExpandEntities() {
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
	public void testIgnoringElementContentWhitespace() {
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
	public void testIgnoringBoundaryWhitespace() {
		SAXHandler handler = new SAXHandler();
		assertFalse(handler.getIgnoringBoundaryWhitespace());
		handler.setIgnoringBoundaryWhitespace(true);
		assertTrue(handler.getIgnoringBoundaryWhitespace());
		handler.setIgnoringBoundaryWhitespace(false);
		assertFalse(handler.getIgnoringBoundaryWhitespace());
		handler.setIgnoringBoundaryWhitespace(true);
		assertTrue(handler.getIgnoringBoundaryWhitespace());
	}


	/* **********************************
	 * LexicalHandler method tests.
	 * **********************************/
	@Test
	public void testDTD() {
		Document doc = checkHandlerDocument(new Builder() {
			@Override
			public void build(SAXHandler handler) throws SAXException {
				handler.startDTD("root", "publicID", "systemID");
				// bunch of things for use during a DTD.
				handler.elementDecl("root", "model");
				handler.attributeDecl("root", "att", "UNKNOWN", "", "");
				handler.externalEntityDecl("extent", "publicID", "systemID");
				handler.comment("foo".toCharArray(), 0, 3);
				handler.internalEntityDecl("intent", "value");
				handler.endDTD();
			}
		});

		assertEquals("root", doc.getDocType().getElementName());
		assertEquals("publicID", doc.getDocType().getPublicID());
		assertEquals("systemID", doc.getDocType().getSystemID());
	}

	@Test
	public void testEntity() {
		// with expandEntities set to true, we lose the entity during parsing
		assertTrue(
				checkHandlerElement(new Builder() {
					@Override
					public SAXHandler createHandler() {
						SAXHandler handler = new SAXHandler();
						handler.setExpandEntities(true);
						return handler;
					}
					@Override
					public void build(SAXHandler handler) throws SAXException {
						handler.startEntity("entity");
						handler.endEntity("entity");
					}
				}).getContentSize() == 0);
		
		// with expandEntities set to false, we expect an entity
		assertMatches(".*&entity;.*",
				checkHandlerElement(new Builder() {
					@Override
					public SAXHandler createHandler() {
						SAXHandler handler = new SAXHandler();
						handler.setExpandEntities(false);
						return handler;
					}
					@Override
					public void build(SAXHandler handler) throws SAXException {
						handler.startEntity("entity");
						handler.endEntity("entity");
					}
				}).getContent(0).toString());
		
		// with [dtd] we expect nothing
		assertTrue(
				checkHandlerElement(new Builder() {
					@Override
					public SAXHandler createHandler() {
						SAXHandler handler = new SAXHandler();
						handler.setExpandEntities(false);
						return handler;
					}
					@Override
					public void build(SAXHandler handler) throws SAXException {
						handler.startEntity("[dtd]");
						handler.endEntity("[dtd]");
					}
				}).getContentSize() == 0);

		// 5 standard entities should be ignored.
		assertTrue(
				checkHandlerElement(new Builder() {
					@Override
					public SAXHandler createHandler() {
						SAXHandler handler = new SAXHandler();
						handler.setExpandEntities(false);
						return handler;
					}
					@Override
					public void build(SAXHandler handler) throws SAXException {
						handler.startEntity("amp");
						handler.endEntity("amp");

						handler.startEntity("apos");
						handler.endEntity("apos");

						handler.startEntity("quot");
						handler.endEntity("quot");

						handler.startEntity("gt");
						handler.endEntity("gt");

						handler.startEntity("lt");
						handler.endEntity("lt");
					}
				}).getContentSize() == 0);
		
		// with expandEntities set to false, we expect an entity
		EntityRef ent = (EntityRef)checkHandlerElement(new Builder() {
			@Override
			public SAXHandler createHandler() {
				SAXHandler handler = new SAXHandler();
				handler.setExpandEntities(false);
				return handler;
			}
			@Override
			public void build(SAXHandler handler) throws SAXException {
				handler.externalEntityDecl("entity", "publicID", "systemID");
				handler.startEntity("entity");
				handler.endEntity("entity");
			}
		}).getContent(0);
		assertEquals("entity", ent.getName());
		assertEquals("publicID", ent.getPublicID());
		assertEquals("systemID", ent.getSystemID());
		
		// when processing an entity, we should ignore all other event types.
		Element emt = checkHandlerElement(new Builder() {
			@Override
			public SAXHandler createHandler() {
				SAXHandler handler = new SAXHandler();
				handler.setExpandEntities(false);
				return handler;
			}
			@Override
			public void build(SAXHandler handler) throws SAXException {
				handler.startEntity("entity");
				handler.startPrefixMapping("prefix", "uri");
				handler.startElement("", "ignore", "", EMPTYATTRIBUTES);
				handler.endElement("", "ignore", "");
				handler.endPrefixMapping("prefix");
				handler.processingInstruction("target", "data");
				handler.comment("ignore".toCharArray(), 0, 6);
				handler.characters("ignore".toCharArray(), 0, 6);
				handler.characters("ignore".toCharArray(), 0, 0);
				handler.ignorableWhitespace("  ".toCharArray(), 0, 2);
				handler.startCDATA();
				handler.characters("ignore".toCharArray(), 0, 6);
				handler.endCDATA();
				handler.endEntity("entity");
			}
		});
		// everything should have been ignored because of the startEntity()
		// which just leaves the actual entity itself.
		assertTrue(emt.getContentSize() == 1);
		assertTrue(emt.getContent(0) instanceof EntityRef);
		EntityRef ent2 = (EntityRef)emt.getContent(0);
		assertEquals("entity", ent2.getName());

		// when processing an entity, we should ignore all other event types.
		Element emt2 = checkHandlerElement(new Builder() {
			@Override
			public SAXHandler createHandler() {
				SAXHandler handler = new SAXHandler();
				handler.setExpandEntities(false);
				return handler;
			}
			@Override
			public void build(SAXHandler handler) throws SAXException {
				handler.startEntity("entity");
				// nested should be ignored.
				handler.startEntity("nested");
				handler.endEntity("nested");
				handler.endEntity("entity");
			}
		});
		// everything should have been ignored because of the startEntity()
		// which just leaves the actual entity itself.
		assertTrue(emt2.getContentSize() == 1);
		assertTrue(emt2.getContent(0) instanceof EntityRef);
		EntityRef ent3 = (EntityRef)emt2.getContent(0);
		assertEquals("entity", ent3.getName());

		// when outside the roo element should be ignored...
		Document doc = checkHandlerDocument(new Builder() {
			@Override
			public SAXHandler createHandler() {
				SAXHandler handler = new SAXHandler();
				handler.setExpandEntities(false);
				return handler;
			}
			@Override
			public void build(SAXHandler handler) throws SAXException {
				handler.startEntity("entity");
				handler.endEntity("entity");
			}
		});
		// everything should have been ignored because of the startEntity()
		// which just leaves the actual entity itself.
		assertTrue(doc.getContentSize() == 0);

	}

	@Test
	public void testCDATA() {
		Element emt = checkHandlerElement(new Builder() {
			@Override
			public void build(SAXHandler handler) throws SAXException {
				handler.comment("foo".toCharArray(), 0, 3);
				handler.characters("   ".toCharArray(), 0, 2);
				handler.startCDATA();
				handler.characters("  foobar  ".toCharArray(), 0, 10);
				handler.endCDATA();
				handler.characters("  ".toCharArray(), 0, 2);
			}
		});

		assertEquals("foobar", emt.getTextTrim());
		assertEquals("  foobar  ", ((CDATA)emt.getContent(new ContentFilter(ContentFilter.CDATA)).get(0)).getText());

	}

	@Test
	public void testComment() {
		Element emt = checkHandlerElement(new Builder() {
			@Override
			public void build(SAXHandler handler) throws SAXException {
				handler.comment("foo".toCharArray(), 0, 3);
				handler.characters("   ".toCharArray(), 0, 2);
				handler.startCDATA();
				handler.characters("  foobar  ".toCharArray(), 0, 10);
				handler.endCDATA();
				handler.characters("  ".toCharArray(), 0, 2);
				handler.comment("bar".toCharArray(), 0, 3);
			}
		});

		int cnt = 0;
		for (Object o : emt.getContent(new ContentFilter(ContentFilter.COMMENT))) {
			assertTrue(o instanceof Comment);
			switch (cnt++) {
			case 0 :
				assertEquals("foo", ((Comment)o).getText());
				break;
			case 1 :
				assertEquals("bar", ((Comment)o).getText());
				break;
			default :
				fail("Expecting only two comments");
			
			}
		}
		
		assertMatches("\\s*<!--\\s*comment\\s*-->\\s*", 
				checkHandlerDTDInternalSubset(new Builder() {
					@Override
					public SAXHandler createHandler() {
						SAXHandler handler = new SAXHandler();
						handler.setExpandEntities(false);
						return handler;
					}
					@Override
					public void build(SAXHandler handler) throws SAXException {
						handler.startEntity("[dtd]");
						handler.unparsedEntityDecl("name", null, "systemID", "notationName");
						handler.endEntity("[dtd]");
						handler.comment("comment".toCharArray(), 0, 7);
					}
				}));

	}



	/* **********************************
	 * DeclHandler method tests These should
	 * all be run between LexicalHandler's
	 * startDTD and endDTD events.
	 * **********************************/
	@Test
	public void testAttributeDecl() {
		assertMatches("<!ATTLIST\\s+root\\s+att\\s*>", 
				checkHandlerDTDInternalSubset(new Builder() {
					@Override
					public void build(SAXHandler handler) throws SAXException {
						handler.attributeDecl("root", "att", "", "", "");
					}
				}));

		assertMatches("<!ATTLIST\\s+root\\s+att\\s+default\\s*>", 
				checkHandlerDTDInternalSubset(new Builder() {
					@Override
					public void build(SAXHandler handler) throws SAXException {
						handler.attributeDecl("root", "att", "", "default", "value");
					}
				}));

		assertMatches("<!ATTLIST\\s+root\\s+att\\s+\"value\"\\s*>", 
				checkHandlerDTDInternalSubset(new Builder() {
					@Override
					public void build(SAXHandler handler) throws SAXException {
						handler.attributeDecl("root", "att", "", null, "value");
					}
				}));

		assertMatches("<!ATTLIST\\s+root\\s+att\\s+type\\s+default\\s*>", 
				checkHandlerDTDInternalSubset(new Builder() {
					@Override
					public void build(SAXHandler handler) throws SAXException {
						handler.attributeDecl("root", "att", "type", "default", "value");
					}
				}));

		assertMatches("<!ATTLIST\\s+root\\s+att\\s+type\\s+\"value\"\\s*>", 
				checkHandlerDTDInternalSubset(new Builder() {
					@Override
					public void build(SAXHandler handler) throws SAXException {
						handler.attributeDecl("root", "att", "type", null, "value");
					}
				}));

		assertMatches("<!ATTLIST\\s+root\\s+att\\s+type\\s+#FIXED\\s+\"value\"\\s*>", 
				checkHandlerDTDInternalSubset(new Builder() {
					@Override
					public void build(SAXHandler handler) throws SAXException {
						handler.attributeDecl("root", "att", "type", "#FIXED", "value");
					}
				}));

	}

	@Test
	public void testElementDecl() {
		assertMatches("<!ELEMENT\\s+root\\s+model\\s*>", 
				checkHandlerDTDInternalSubset(new Builder() {
					@Override
					public void build(SAXHandler handler) throws SAXException {
						handler.elementDecl("root", "model");
					}
				}));
	}

	@Test
	public void testInternalEntityDecl() {
		assertMatches("<!ENTITY\\s+name\\s+\"value\"\\s*>", 
				checkHandlerDTDInternalSubset(new Builder() {
					@Override
					public void build(SAXHandler handler) throws SAXException {
						handler.internalEntityDecl("name", "value");
					}
				}));
		
		//Parameter Entity Declaration
		assertMatches("<!ENTITY\\s+%\\s+name\\s+\"value\"\\s*>", 
				checkHandlerDTDInternalSubset(new Builder() {
					@Override
					public void build(SAXHandler handler) throws SAXException {
						handler.internalEntityDecl("%name", "value");
					}
				}));
	}

	@Test
	public void testExternalEntityDecl() {
		assertMatches("<!ENTITY\\s+name\\s+PUBLIC\\s+\"publicID\"\\s+\"systemID\"\\s*>", 
				checkHandlerDTDInternalSubset(new Builder() {
					@Override
					public void build(SAXHandler handler) throws SAXException {
						handler.externalEntityDecl("name", "publicID", "systemID");
					}
				}));

		assertMatches("<!ENTITY\\s+name\\s+SYSTEM\\s+\"systemID\"\\s*>", 
				checkHandlerDTDInternalSubset(new Builder() {
					@Override
					public void build(SAXHandler handler) throws SAXException {
						handler.externalEntityDecl("name", null, "systemID");
					}
				}));

	}

	@Test
	public void testUnparsedEntityDecl() {
		assertMatches("<!ENTITY\\s+name\\s+PUBLIC\\s+\"publicID\"\\s+\"systemID\"\\s+NDATA\\s+notationName\\s*>", 
				checkHandlerDTDInternalSubset(new Builder() {
					@Override
					public void build(SAXHandler handler) throws SAXException {
						handler.unparsedEntityDecl("name", "publicID", "systemID", "notationName");
					}
				}));
		
		assertMatches("<!ENTITY\\s+name\\s+SYSTEM\\s+\"systemID\"\\s+NDATA\\s+notationName\\s*>", 
				checkHandlerDTDInternalSubset(new Builder() {
					@Override
					public void build(SAXHandler handler) throws SAXException {
						handler.unparsedEntityDecl("name", null, "systemID", "notationName");
					}
				}));
		assertMatches("\\s*", 
				checkHandlerDTDInternalSubset(new Builder() {
					@Override
					public SAXHandler createHandler() {
						SAXHandler handler = new SAXHandler();
						handler.setExpandEntities(false);
						return handler;
					}
					@Override
					public void build(SAXHandler handler) throws SAXException {
						handler.startEntity("[dtd]");
						handler.unparsedEntityDecl("name", null, "systemID", "notationName");
						handler.endEntity("[dtd]");
					}
				}));
	}
	
	@Test
	public void testNotationDecl() {
		assertMatches("<!NOTATION\\s+name\\s+PUBLIC\\s+\"publicID\"\\s+\"systemID\"\\s*>", 
				checkHandlerDTDInternalSubset(new Builder() {
					@Override
					public void build(SAXHandler handler) throws SAXException {
						handler.notationDecl("name", "publicID", "systemID");
					}
				}));

		assertMatches("<!NOTATION\\s+name\\s+SYSTEM\\s+\"systemID\"\\s*>", 
				checkHandlerDTDInternalSubset(new Builder() {
					@Override
					public void build(SAXHandler handler) throws SAXException {
						handler.notationDecl("name", null, "systemID");
					}
				}));
		
		assertMatches("<!NOTATION\\s+name\\s+PUBLIC\\s+\"publicID\"\\s*>", 
				checkHandlerDTDInternalSubset(new Builder() {
					@Override
					public void build(SAXHandler handler) throws SAXException {
						handler.notationDecl("name", "publicID", null);
					}
				}));
		
		Document doc = checkHandlerDocument(new Builder() {
			@Override
			public SAXHandler createHandler() {
				SAXHandler handler = new SAXHandler();
				handler.setExpandEntities(false);
				return handler;
			}
			@Override
			public void build(SAXHandler handler) throws SAXException {
				handler.startDTD("name", "publicID", "systemID");
				handler.startEntity("[dtd]");
				handler.notationDecl("exdtd", "publicIDA", "systemIDA");
				handler.endEntity("[dtd]");
				handler.notationDecl("indtd", "publicIDB", "systemIDB");
				handler.endDTD();
			}
		});
		
		DocType dt = doc.getDocType();
		assertTrue(dt != null);
		assertMatches("<!NOTATION\\s+indtd\\s+PUBLIC\\s+\"publicIDB\"\\s+\"systemIDB\"\\s*>", 
				dt.getInternalSubset().trim());
	}

	/* **********************************
	 * ContentHandler method tests.
	 * **********************************/
	@Test
	public void testProcessingInstruction() {
		Element emt = checkHandlerElement(new Builder() {
					@Override
					public void build(SAXHandler handler) throws SAXException {
						handler.processingInstruction("target", "data");
					}
				});
		assertTrue(emt.getContentSize() == 1);
		assertTrue(emt.getContent(0) instanceof ProcessingInstruction);
		ProcessingInstruction pi = (ProcessingInstruction)emt.getContent(0);
		assertEquals("target", pi.getTarget());
		assertEquals("data", pi.getData());
		
		Document doc = checkHandlerDocument(new Builder() {
			@Override
			public void build(SAXHandler handler) throws SAXException {
				handler.processingInstruction("target", "data");
			}
		});
		assertTrue(doc.getContentSize() == 1);
		assertTrue(doc.getContent(0) instanceof ProcessingInstruction);
		pi = (ProcessingInstruction)emt.getContent(0);
		assertEquals("target", pi.getTarget());
		assertEquals("data", pi.getData());
		
	}

	@Test
	public void testSkippedEntityString() {
		Element emt = checkHandlerElement(new Builder() {
			@Override
			public void build(SAXHandler handler) throws SAXException {
				// this one should be ignored.
				handler.skippedEntity("%ignore");
				// this one should be added.
				handler.skippedEntity("entity");
			}
		});
		assertTrue(emt.getContentSize() == 1);
		assertTrue(emt.getContent(0) instanceof EntityRef);
		EntityRef er = (EntityRef)emt.getContent(0);
		assertEquals("entity", er.getName());
	}

	@Test
	public void testElementSimple() {
		Element emt = checkHandlerElement(new Builder() {
			@Override
			public void build(SAXHandler handler) throws SAXException {
				handler.startElement("", "child", "", EMPTYATTRIBUTES);
				handler.endElement("", "child", "");
			}
		});
		assertTrue(emt.getContentSize() == 1);
		assertTrue(emt.getContent(0) instanceof Element);
		Element child = (Element)emt.getContent(0);
		assertEquals("child", child.getName());
		assertEquals("", child.getNamespacePrefix());
		assertEquals("", child.getNamespaceURI());
	}

	@Test
	public void testElementNullURI() {
		Element emt = checkHandlerElement(new Builder() {
			@Override
			public void build(SAXHandler handler) throws SAXException {
				handler.startElement(null, "child", "", EMPTYATTRIBUTES);
				handler.endElement(null, "child", "");
			}
		});
		assertTrue(emt.getContentSize() == 1);
		assertTrue(emt.getContent(0) instanceof Element);
		Element child = (Element)emt.getContent(0);
		assertEquals("child", child.getName());
		assertEquals("", child.getNamespacePrefix());
		assertEquals("", child.getNamespaceURI());
	}

	@Test
	public void testElementBadEndElement() {
		try {
			SAXHandler handler = new SAXHandler();
			handler.startDocument();
			handler.startElement("", "root", "root", EMPTYATTRIBUTES);
			handler.endElement("", "root", "root");
			handler.endElement("", "bad", "bad");
			handler.endDocument();
			fail("Should not have been able to create Element ");
		} catch (SAXException se) {
			// good
		} catch (Exception e) {
			fail("Expecting SAXEsception, but got " + e.getClass().getName());
		}
	}

	@Test
	public void testElementAttributesSimple() {
		// simple attribute.
		final AttributesSingleOnly atts = new AttributesSingleOnly("", "att", "att", "CDATA", "val");
		Element emt = checkHandlerElement(new Builder() {
			@Override
			public void build(SAXHandler handler) throws SAXException {
				handler.startElement("", "child", "", atts);
				handler.endElement("", "child", "");
			}
		});
		
		assertTrue(emt.getContentSize() == 1);
		assertTrue(emt.getContent(0) instanceof Element);
		Element child = (Element)emt.getContent(0);
		assertEquals("child", child.getName());
		assertEquals("", child.getNamespacePrefix());
		assertEquals("", child.getNamespaceURI());
		assertTrue(child.getAttributes().size() == 1);
		assertEquals("val", child.getAttributeValue("att"));
		assertEquals(AttributeType.CDATA, child.getAttribute("att").getAttributeType());
	}

	@Test
	public void testElementAttributesNameXMLNS() {
		// invalid xmlns attibute.
		final AttributesSingleOnly atts = new AttributesSingleOnly("", "xmlns", "xmlns", "CDATA", "val");
		Element emt = checkHandlerElement(new Builder() {
			@Override
			public void build(SAXHandler handler) throws SAXException {
				handler.startElement("", "child", "", atts);
				handler.endElement("", "child", "");
			}
		});
		
		assertTrue(emt.getContentSize() == 1);
		assertTrue(emt.getContent(0) instanceof Element);
		Element child = (Element)emt.getContent(0);
		assertEquals("child", child.getName());
		assertEquals("", child.getNamespacePrefix());
		assertEquals("", child.getNamespaceURI());
		assertTrue(child.getAttributes().isEmpty());
	}

	@Test
	public void testElementAttributesPrefixXMLNS() {
		// invalid xmlns attibute.
		final AttributesSingleOnly atts = new AttributesSingleOnly("", "ns", "xmlns:ns", "CDATA", "uri");
		Element emt = checkHandlerElement(new Builder() {
			@Override
			public void build(SAXHandler handler) throws SAXException {
				handler.startElement("", "child", "", atts);
				handler.endElement("", "child", "");
			}
		});
		
		assertTrue(emt.getContentSize() == 1);
		assertTrue(emt.getContent(0) instanceof Element);
		Element child = (Element)emt.getContent(0);
		assertEquals("child", child.getName());
		assertEquals("", child.getNamespacePrefix());
		assertEquals("", child.getNamespaceURI());
		assertTrue(child.getAttributes().size() == 0);
	}

	@Test
	public void testElementAttributesNoLocalName() {
		// no-localname, but has qname.
		final AttributesSingleOnly atts = new AttributesSingleOnly("", "", "att", "CDATA", "val");
		Element emt = checkHandlerElement(new Builder() {
			@Override
			public void build(SAXHandler handler) throws SAXException {
				handler.startElement("", "child", "", atts);
				handler.endElement("", "child", "");
			}
		});
		
		assertTrue(emt.getContentSize() == 1);
		assertTrue(emt.getContent(0) instanceof Element);
		Element child = (Element)emt.getContent(0);
		assertEquals("child", child.getName());
		assertEquals("", child.getNamespacePrefix());
		assertEquals("", child.getNamespaceURI());
		assertTrue(child.getAttributes().size() == 1);
		assertEquals("val", child.getAttributeValue("att"));
	}


	@Test
	public void testElementAttributesSimpleInNamespace() {
		// normal att-in-namespace.
		final AttributesSingleOnly atts = new AttributesSingleOnly("nsuri", "att", "pfx:att", "CDATA", "val");
		Element emt = checkHandlerElement(new Builder() {
			@Override
			public void build(SAXHandler handler) throws SAXException {
				handler.startPrefixMapping("pfx", "nsuri");
				handler.startElement("", "child", "", atts);
				handler.endElement("", "child", "");
				handler.endPrefixMapping("pfx");
			}
		});
		
		assertTrue(emt.getContentSize() == 1);
		assertTrue(emt.getContent(0) instanceof Element);
		Element child = (Element)emt.getContent(0);
		assertEquals("child", child.getName());
		assertEquals("", child.getNamespacePrefix());
		assertEquals("", child.getNamespaceURI());
		assertTrue(child.getAttributes().size() == 1);
		assertTrue(child.getAttribute("att") == null);
		Namespace ns = Namespace.getNamespace("pfx", "nsuri");
		assertTrue(child.getAttribute("att", ns) != null);
		assertEquals("val", child.getAttributeValue("att", ns));
	}

	
	@Test
	public void testElementAttributesNoPrefixNamespaceMustGeneratePrefix() {
		// weird att-in-namespace - no prefix.
		// namespace of parent element matches, but no prefix.
		// should invent a prefix (attns0)
		final AttributesSingleOnly atts = new AttributesSingleOnly("nsuri", "att", "att", "CDATA", "val");
		Element emt = checkHandlerElement(new Builder() {
			@Override
			public void build(SAXHandler handler) throws SAXException {
				handler.startElement("nsuri", "child", "child", atts);
				handler.endElement("nsuri", "child", "child");
			}
		});
		
		assertTrue(emt.getContentSize() == 1);
		assertTrue(emt.getContent(0) instanceof Element);
		Element child = (Element)emt.getContent(0);
		assertEquals("child", child.getName());
		assertEquals("", child.getNamespacePrefix());
		assertEquals("nsuri", child.getNamespaceURI());
		assertTrue(child.getAttributes().size() == 1);
		assertTrue(child.getAttribute("att") == null);
		Attribute a = child.getAttribute("att", child.getNamespace());
		assertEquals("attns0", a.getNamespacePrefix());
		assertEquals("nsuri", a.getNamespaceURI());
		assertEquals("val", a.getValue());
	}

	@Test
	public void testElementAttributesNoPrefixNamespaceMustGenerateAlternatePrefix() {
		// weird att-in-namespace - no prefix.
		// namespace of parent element matches, but no prefix.
		// also, attns0 is used by some other namespace.
		// should invent a prefix (attns1)
		final AttributesSingleOnly atts = new AttributesSingleOnly("nsuri", "att", "att", "CDATA", "val");
		Element emt = checkHandlerElement(new Builder() {
			@Override
			public void build(SAXHandler handler) throws SAXException {
				handler.startPrefixMapping("attns0", "otheruri");
				handler.startElement("nsuri", "child", "child", atts);
				handler.endElement("nsuri", "child", "child");
				handler.endPrefixMapping("atns0");
			}
		});
		
		assertTrue(emt.getContentSize() == 1);
		assertTrue(emt.getContent(0) instanceof Element);
		Element child = (Element)emt.getContent(0);
		assertEquals("child", child.getName());
		assertEquals("", child.getNamespacePrefix());
		assertEquals("nsuri", child.getNamespaceURI());
		assertTrue(child.getAttributes().size() == 1);
		assertTrue(child.getAttribute("att") == null);
		Attribute a = child.getAttribute("att", child.getNamespace());
		assertEquals("attns1", a.getNamespacePrefix());
		assertEquals("nsuri", a.getNamespaceURI());
		assertEquals("val", a.getValue());
	}

	@Test
	public void testElementAttributesNoPrefixNamespaceMustUseParentLevelPrefix() {
		// weird att-in-namespace - no prefix.
		// but there is a prefix declared for it at the parent level.
		final AttributesSingleOnly atts = new AttributesSingleOnly("nsuri", "att", "att", "CDATA", "val");
		Element emt = checkHandlerElement(new Builder() {
			@Override
			public void build(SAXHandler handler) throws SAXException {
				handler.startPrefixMapping("pfx", "nsuri");
				handler.startElement("", "child", "", atts);
				handler.endElement("", "child", "");
				handler.endPrefixMapping("pfx");
			}
		});
		
		assertTrue(emt.getContentSize() == 1);
		assertTrue(emt.getContent(0) instanceof Element);
		Element child = (Element)emt.getContent(0);
		assertEquals("child", child.getName());
		assertEquals("", child.getNamespacePrefix());
		assertEquals("", child.getNamespaceURI());
		assertTrue(child.getAttributes().size() == 1);
		assertTrue(child.getAttribute("att") == null);
		Namespace nsd = Namespace.getNamespace("differentprefix", "nsuri");
		Attribute a = child.getAttribute("att", nsd);
		assertEquals("pfx", a.getNamespacePrefix());
		assertEquals("nsuri", a.getNamespaceURI());
		assertEquals("val", a.getValue());
	}

	@Test
	public void testElementAttributesNoPrefixNamespaceMustUseActualParentPrefix() {
		// weird att-in-namespace - no prefix.
		// namespace of parent element matches, it has prefix.
		// should use parent prefix (pfx)
		final AttributesSingleOnly atts = new AttributesSingleOnly("nsuri", "att", "att", "CDATA", "val");
		Element emt = checkHandlerElement(new Builder() {
			@Override
			public void build(SAXHandler handler) throws SAXException {
				handler.startPrefixMapping("attns0", "otheruri");
				handler.startElement("nsuri", "child", "pfx:child", atts);
				handler.endElement("nsuri", "child", "pfx:child");
				handler.endPrefixMapping("atns0");
			}
		});
		
		assertTrue(emt.getContentSize() == 1);
		assertTrue(emt.getContent(0) instanceof Element);
		Element child = (Element)emt.getContent(0);
		assertEquals("child", child.getName());
		assertEquals("pfx", child.getNamespacePrefix());
		assertEquals("nsuri", child.getNamespaceURI());
		assertTrue(child.getAttributes().size() == 1);
		assertTrue(child.getAttribute("att") == null);
		Attribute a = child.getAttribute("att", child.getNamespace());
		assertEquals("pfx", a.getNamespacePrefix());
		assertEquals("nsuri", a.getNamespaceURI());
		assertEquals("val", a.getValue());
	}


	@Test
	public void testElementAttributesNoPrefixNamespaceParentPrefixOverrides() {
		// weird att-in-namespace - no prefix.
		// also, a matching prefix was overridden.
		// should generate one (attns0).
		final AttributesSingleOnly atts = new AttributesSingleOnly("nsuri", "att", "att", "CDATA", "val");
		Element emt = checkHandlerElement(new Builder() {
			@Override
			public void build(SAXHandler handler) throws SAXException {
				handler.startPrefixMapping("pfx", "nsuri");
				handler.startElement("nsuri", "middle", "pfx:middle", EMPTYATTRIBUTES);
				// re-define the namespace prefix with a different uri
				handler.startElement("childuri", "child", "pfx:child", atts);
				handler.endElement("childuri", "child", "pfx:child");
				handler.endElement("", "middle", "pfx:middle");
				handler.endPrefixMapping("pfx");
			}
		});
		
		assertTrue(emt.getContentSize() == 1);
		assertTrue(emt.getContent(0) instanceof Element);
		Element middle = (Element)emt.getContent(0);
		assertEquals("middle", middle.getName());
		assertEquals("pfx", middle.getNamespacePrefix());
		assertEquals("nsuri", middle.getNamespaceURI());
		
		Element child = middle.getChild("child", Namespace.getNamespace("childuri"));
		assertEquals("child", child.getName());
		assertEquals("pfx", child.getNamespacePrefix());
		assertEquals("childuri", child.getNamespaceURI());
		assertTrue(child.getAttributes().size() == 1);
		assertTrue(child.getAttribute("att") == null);
		Attribute a = child.getAttribute("att", middle.getNamespace());
		assertEquals("attns0", a.getNamespacePrefix());
		assertEquals("nsuri", a.getNamespaceURI());
		assertEquals("val", a.getValue());
	}

	@Test
	public void testElementAttributesNoPrefixNamespaceParentPrefixLevelOverrides() {
		// weird att-in-namespace - no prefix.
		// also, a matching prefix at the parent level was overridden.
		// should generate one (attns0).
		final AttributesSingleOnly atts = new AttributesSingleOnly("nsuri", "att", "att", "CDATA", "val");
		Element emt = checkHandlerElement(new Builder() {
			@Override
			public void build(SAXHandler handler) throws SAXException {
				handler.startPrefixMapping("pfx", "nsuri");
				handler.startElement("nsuri", "middle", "pfx:middle", EMPTYATTRIBUTES);
				// re-define the namespace prefix with a different uri
				handler.startPrefixMapping("pfx", "ignoreuri");
				handler.startElement("childuri", "child", "kid:child", atts);
				handler.endElement("childuri", "child", "kid:child");
				handler.endPrefixMapping("pfx");
				handler.endElement("", "middle", "pfx:middle");
				handler.endPrefixMapping("pfx");
			}
		});
		
		assertTrue(emt.getContentSize() == 1);
		assertTrue(emt.getContent(0) instanceof Element);
		Element middle = (Element)emt.getContent(0);
		assertEquals("middle", middle.getName());
		assertEquals("pfx", middle.getNamespacePrefix());
		assertEquals("nsuri", middle.getNamespaceURI());
		
		Element child = middle.getChild("child", Namespace.getNamespace("childuri"));
		assertEquals("child", child.getName());
		assertEquals("kid", child.getNamespacePrefix());
		assertEquals("childuri", child.getNamespaceURI());
		assertTrue(child.getAttributes().size() == 1);
		assertTrue(child.getAttribute("att") == null);
		Attribute a = child.getAttribute("att", middle.getNamespace());
		assertEquals("attns0", a.getNamespacePrefix());
		assertEquals("nsuri", a.getNamespaceURI());
		assertEquals("val", a.getValue());
	}

	
	@Test
	public void testElementAttributesTypeIsEnumeration() {
		// simple attribute.
		final AttributesSingleOnly atts = new AttributesSingleOnly("", "att", "att", "(val1,val2)", "val");
		Element emt = checkHandlerElement(new Builder() {
			@Override
			public void build(SAXHandler handler) throws SAXException {
				handler.startElement("", "child", "", atts);
				handler.endElement("", "child", "");
			}
		});
		
		assertTrue(emt.getContentSize() == 1);
		assertTrue(emt.getContent(0) instanceof Element);
		Element child = (Element)emt.getContent(0);
		assertEquals("child", child.getName());
		assertEquals("", child.getNamespacePrefix());
		assertEquals("", child.getNamespaceURI());
		assertTrue(child.getAttributes().size() == 1);
		assertEquals("val", child.getAttributeValue("att"));
	}

	@Test
	public void testElementAttributesTypeIsUnknown() {
		// simple attribute.
		final AttributesSingleOnly atts = new AttributesSingleOnly("", "att", "att", "poppygook", "val");
		Element emt = checkHandlerElement(new Builder() {
			@Override
			public void build(SAXHandler handler) throws SAXException {
				handler.startElement("", "child", "", atts);
				handler.endElement("", "child", "");
			}
		});
		
		assertTrue(emt.getContentSize() == 1);
		assertTrue(emt.getContent(0) instanceof Element);
		Element child = (Element)emt.getContent(0);
		assertEquals("child", child.getName());
		assertEquals("", child.getNamespacePrefix());
		assertEquals("", child.getNamespaceURI());
		assertTrue(child.getAttributes().size() == 1);
		Attribute att = child.getAttribute("att");
		assertEquals("val", att.getValue());
		assertEquals(AttributeType.UNDECLARED, att.getAttributeType());
	}

	@Test
	public void testElementAttributesTypeIsNull() {
		// null attribute type.
		final AttributesSingleOnly atts = new AttributesSingleOnly("", "att", "att", null, "val");
		Element emt = checkHandlerElement(new Builder() {
			@Override
			public void build(SAXHandler handler) throws SAXException {
				handler.startElement("", "child", "", atts);
				handler.endElement("", "child", "");
			}
		});
		
		assertTrue(emt.getContentSize() == 1);
		assertTrue(emt.getContent(0) instanceof Element);
		Element child = (Element)emt.getContent(0);
		assertEquals("child", child.getName());
		assertEquals("", child.getNamespacePrefix());
		assertEquals("", child.getNamespaceURI());
		assertTrue(child.getAttributes().size() == 1);
		Attribute att = child.getAttribute("att");
		assertEquals("val", att.getValue());
		assertEquals(AttributeType.UNDECLARED, att.getAttributeType());
	}

	
	@Test
	public void testElementAttributesTypeIsEmptyString() {
		// "" attribute type.
		final AttributesSingleOnly atts = new AttributesSingleOnly("", "att", "att", "", "val");
		Element emt = checkHandlerElement(new Builder() {
			@Override
			public void build(SAXHandler handler) throws SAXException {
				handler.startElement("", "child", "", atts);
				handler.endElement("", "child", "");
			}
		});
		
		assertTrue(emt.getContentSize() == 1);
		assertTrue(emt.getContent(0) instanceof Element);
		Element child = (Element)emt.getContent(0);
		assertEquals("child", child.getName());
		assertEquals("", child.getNamespacePrefix());
		assertEquals("", child.getNamespaceURI());
		assertTrue(child.getAttributes().size() == 1);
		Attribute att = child.getAttribute("att");
		assertEquals("val", att.getValue());
		assertEquals(AttributeType.UNDECLARED, att.getAttributeType());
	}

	
	@Test
	public void testPrefixMapping() {
		Element emt = checkHandlerElement(new Builder() {
			@Override
			public void build(SAXHandler handler) throws SAXException {
				handler.startPrefixMapping("prefix", "uri");
				handler.startElement("uri", "child", "prefix:uri", EMPTYATTRIBUTES);
				handler.endElement("uri", "child", "prefix:uri");
				handler.endPrefixMapping("prefix");
			}
		});
		assertTrue(emt.getContentSize() == 1);
		assertTrue(emt.getContent(0) instanceof Element);
		Element child = (Element)emt.getContent(0);
		assertEquals("child", child.getName());
		assertEquals("prefix", child.getNamespacePrefix());
		assertEquals("uri", child.getNamespaceURI());
	}

	@Test
	public void testCharacters() {
		Element emt = checkHandlerElement(new Builder() {
			@Override
			public void build(SAXHandler handler) throws SAXException {
				handler.comment("foo".toCharArray(), 0, 3);
				handler.characters("   ".toCharArray(), 0, 2);
				// 0-length should be ignored.
				handler.characters("xxxx".toCharArray(), 0, 0);
				handler.characters("  foobar  ".toCharArray(), 0, 10);
				handler.characters("  ".toCharArray(), 0, 2);
			}
		});

		assertEquals("    foobar    ", emt.getText());
		assertEquals("foobar", emt.getTextTrim());
	}

	@Test
	public void testIgnorableWhitespaceTrue() {
		Element emt = checkHandlerElement(new Builder() {
			@Override
			public SAXHandler createHandler() {
				SAXHandler ret = new SAXHandler();
				ret.setIgnoringElementContentWhitespace(true);
				return ret;
			}
			@Override
			public void build(SAXHandler handler) throws SAXException {
				handler.comment("foo".toCharArray(), 0, 3);
				// 3 chars, length 2 though.
				handler.ignorableWhitespace("   ".toCharArray(), 0, 2);
				handler.characters("  foobar  ".toCharArray(), 0, 10);
				// 0 length
				handler.ignorableWhitespace("  ".toCharArray(), 0, 0);
				handler.ignorableWhitespace("  ".toCharArray(), 0, 2);
			}
		});

		assertEquals("  foobar  ", emt.getText());
		assertEquals("foobar", emt.getTextTrim());
	}

	@Test
	public void testIgnorableBoundaryWhitespaceTrue() {
		assertEquals("", checkHandlerElement(new Builder() {
			@Override
			public SAXHandler createHandler() {
				SAXHandler ret = new SAXHandler();
				ret.setIgnoringBoundaryWhitespace(true);
				return ret;
			}
			@Override
			public void build(SAXHandler handler) throws SAXException {
				handler.characters("  \n\n  ".toCharArray(), 0, 6);
			}
		}).getText());

		assertEquals(" \nx\n ", checkHandlerElement(new Builder() {
			@Override
			public SAXHandler createHandler() {
				SAXHandler ret = new SAXHandler();
				ret.setIgnoringBoundaryWhitespace(true);
				return ret;
			}
			@Override
			public void build(SAXHandler handler) throws SAXException {
				handler.characters(" \nx\n ".toCharArray(), 0, 5);
			}
		}).getText());

	}

	@Test
	public void testIgnorableWhitespaceFalse() {
		Element emt = checkHandlerElement(new Builder() {
			@Override
			public SAXHandler createHandler() {
				SAXHandler ret = new SAXHandler();
				ret.setIgnoringElementContentWhitespace(false);
				return ret;
			}
			@Override
			public void build(SAXHandler handler) throws SAXException {
				handler.comment("foo".toCharArray(), 0, 3);
				handler.ignorableWhitespace("   ".toCharArray(), 0, 2);
				handler.characters("  foobar  ".toCharArray(), 0, 10);
				handler.ignorableWhitespace("  ".toCharArray(), 0, 2);
			}
		});

		assertEquals("    foobar    ", emt.getText());
		assertEquals("foobar", emt.getTextTrim());
	}
	
	@Test
	public void testPushElement() {
		try {
			MyHandler handler = new MyHandler();
			handler.startDocument();
			handler.pushElement(new Element("root"));
			handler.endDocument();
			Document doc = handler.getDocument();
			assertTrue(doc.hasRootElement());
			assertEquals("root", doc.getRootElement().getName());
		} catch (SAXException se) {
			se.printStackTrace();
			fail ("Failed to load MyHandler: " + se.getMessage());
		}
		try {
			MyHandler handler = new MyHandler();
			handler.startDocument();
			handler.startElement("", "root", "root", EMPTYATTRIBUTES);
			handler.pushElement(new Element("child"));
			handler.endElement("", "root", "root");
			handler.endDocument();
			Document doc = handler.getDocument();
			assertTrue(doc.hasRootElement());
			assertEquals("root", doc.getRootElement().getName());
			assertTrue(doc.getRootElement().getChild("child") != null);
		} catch (SAXException se) {
			se.printStackTrace();
			fail ("Failed to load MyHandler: " + se.getMessage());
		}
	}
	
	@Test
	public void testIllegalGetCurrentElement() {
		SAXHandler handler = new SAXHandler();
		handler.startDocument();
		try {
			handler.getCurrentElement();
			fail ("Should not be able to append bad element strcuture.");
		} catch (SAXException se) {
			// good/.
		} catch (Exception e) {
			fail("Expected to get SAXException but instead got " + e.getClass().getName() + "'.");
		}
		
	}
	
	@Test
	public void testAndroidParserIssue2LocalOnly() throws SAXException {
		SAXHandler handler = new SAXHandler();
		handler.startDocument();
		AttributesSingleOnly attrs = new AttributesSingleOnly("", "attname", "", "CDATA", "val");
		handler.startElement("", "root", "", attrs);
		handler.endElement("", "root", "");
		handler.endDocument();
		Document doc = handler.getDocument();
		Element root = doc.getRootElement();
		assertEquals("root", root.getName());
		Attribute att = root.getAttribute("attname");
		assertNotNull(att);
		assertEquals("val", att.getValue());
	}

	@Test
	public void testAndroidParserIssue2QNameOnly() throws SAXException {
		SAXHandler handler = new SAXHandler();
		handler.startDocument();
		AttributesSingleOnly attrs = new AttributesSingleOnly("", "", "attname", "CDATA", "val");
		handler.startElement("", "", "root", attrs);
		handler.endElement("", "root", "");
		handler.endDocument();
		Document doc = handler.getDocument();
		Element root = doc.getRootElement();
		assertEquals("root", root.getName());
		Attribute att = root.getAttribute("attname");
		assertNotNull(att);
		assertEquals("val", att.getValue());
	}

	@Test
	public void testAndroidParserIssue2AttXMLNS() throws SAXException {
		// test a namespace-aware parser that is not configured namespace-prefixes
		// in theory, it should leave the qName empty, even for an XMLNS declaration
		SAXHandler handler = new SAXHandler();
		handler.startDocument();
		AttributesSingleOnly attrs = new AttributesSingleOnly(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "pfx", "", "CDATA", "nsuri");
		handler.startElement("", "", "root", attrs);
		handler.endElement("", "root", "");
		handler.endDocument();
		Document doc = handler.getDocument();
		Element root = doc.getRootElement();
		assertEquals("root", root.getName());
		Attribute att = root.getAttribute("pfx");
		assertNull(att);
		assertTrue(root.getAttributes().isEmpty());
	}

}
