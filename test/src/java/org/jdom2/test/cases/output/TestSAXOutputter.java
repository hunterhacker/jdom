package org.jdom2.test.cases.output;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.LocatorImpl;

import org.jdom2.Attribute;
import org.jdom2.AttributeType;
import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.Content;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.EntityRef;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;
import org.jdom2.input.sax.SAXHandler;
import org.jdom2.output.Format;
import org.jdom2.output.JDOMLocator;
import org.jdom2.output.LineSeparator;
import org.jdom2.output.SAXOutputter;
import org.jdom2.output.XMLOutputter;
import org.jdom2.output.support.AbstractSAXOutputProcessor;
import org.jdom2.output.support.SAXOutputProcessor;
import org.jdom2.test.util.UnitTestUtil;


@SuppressWarnings("javadoc")
public class TestSAXOutputter extends AbstractTestOutputter {
	
    private interface SAXSetup {
    	public SAXOutputter buildOutputter(SAXHandler handler);
    }
    
    

    /**
	 * @param cr2xD
	 * @param padpreempty
	 * @param padpi
	 * @param forceexpand
	 * @param forceplatformeol
	 */
	public TestSAXOutputter() {
		super(true, true, false, false, true);
	}

	private void roundTrip(Document doc) {
    	roundTrip(null, doc);
    }
    
    private void roundTrip(SAXSetup setup, Document doc) {
    	XMLOutputter xout = new XMLOutputter(Format.getRawFormat());
    	// create a String representation of the input.
    	if (doc.hasRootElement()) {
    		UnitTestUtil.normalizeAttributes(doc.getRootElement());
    	}
    	String expect = xout.outputString(doc);
    	
    	String actual = null;
    	try {
    		// convert the input to a SAX Stream
    		SAXHandler handler = new SAXHandler();
        	SAXOutputter saxout = setup == null 
        			? new SAXOutputter(handler, handler, handler, handler, handler) 
        			: setup.buildOutputter(handler);

        	saxout.output(doc);
        	
			Document backagain = handler.getDocument();
			
			// get a String representation of the round-trip.
	    	if (backagain.hasRootElement()) {
	    		UnitTestUtil.normalizeAttributes(backagain.getRootElement());
	    	}
			actual = xout.outputString(backagain);
		} catch (JDOMException e) {
			e.printStackTrace();
			fail("Failed to round-trip the document with exception: " 
					+ e.getMessage() + "\n" + expect);
		}
    	assertEquals(expect, actual);
    }
    
    private void roundTrip(SAXSetup setup, Element emt) {
    	XMLOutputter xout = new XMLOutputter(Format.getRawFormat());
    	Document tstdoc = new Document();
    	tstdoc.addContent(emt.clone());
    	String expect = xout.outputString(tstdoc);
    	
    	String actual = null;
    	try {
    		// convert the input to a SAX Stream
    		SAXHandler handler = new SAXHandler();
        	SAXOutputter saxout = setup == null 
        			? new SAXOutputter(handler, handler, handler, handler, handler) 
        			: setup.buildOutputter(handler);

        	saxout.output(emt);
        	
			Document backagain = handler.getDocument();
			
			// get a String representation of the round-trip.
	    	if (backagain.hasRootElement()) {
	    		UnitTestUtil.normalizeAttributes(backagain.getRootElement());
	    	}
			actual = xout.outputString(backagain);
		} catch (JDOMException e) {
			e.printStackTrace();
			fail("Failed to round-trip the document with exception: " 
					+ e.getMessage() + "\n" + expect);
		}
    	assertEquals(expect, actual);
    }
    
    private void roundTripFragment(SAXSetup setup, List<Content> content) {
    	XMLOutputter xout = new XMLOutputter(Format.getRawFormat());
    	Element root = new Element("root");
    	Document tstdoc = new Document(root);
    	
    	for (Content c : content) {
    		root.addContent(c);
    	}
    	
    	String expect = xout.outputString(tstdoc);
    	
    	String actual = null;
    	try {
    		// convert the input to a SAX Stream
    		SAXHandler handler = new SAXHandler();
        	SAXOutputter saxout = setup == null 
        			? new SAXOutputter(handler, handler, handler, handler, handler) 
        			: setup.buildOutputter(handler);

        	Locator loc = new LocatorImpl();
        	handler.setDocumentLocator(loc);
        	handler.startDocument();
        	handler.startElement("", "root", "root", new AttributesImpl());
        	saxout.outputFragment(content);
        	handler.endElement("", "root", "root");
        	handler.endDocument();
        	
			Document backagain = handler.getDocument();
			
			// get a String representation of the round-trip.
	    	if (backagain.hasRootElement()) {
	    		UnitTestUtil.normalizeAttributes(backagain.getRootElement());
	    	}
			actual = xout.outputString(backagain);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed to round-trip the document with exception: " 
					+ e.getMessage() + "\n" + expect);
		}
    	assertEquals(expect, actual);
    }
    
    private void roundTripFragment(SAXSetup setup, Content content) {
    	XMLOutputter xout = new XMLOutputter(Format.getRawFormat());
    	Element root = new Element("root");
    	Document tstdoc = new Document(root);
    	
   		root.addContent(content);
    	
    	String expect = xout.outputString(tstdoc);
    	
    	String actual = null;
    	try {
    		// convert the input to a SAX Stream
    		SAXHandler handler = new SAXHandler();
        	SAXOutputter saxout = setup == null 
        			? new SAXOutputter(handler, handler, handler, handler, handler) 
        			: setup.buildOutputter(handler);

        	Locator loc = new LocatorImpl();
        	handler.setDocumentLocator(loc);
        	handler.startDocument();
        	handler.startElement("", "root", "root", new AttributesImpl());
        	saxout.outputFragment(content);
        	handler.endElement("", "root", "root");
        	handler.endDocument();
        	
			Document backagain = handler.getDocument();
			
			// get a String representation of the round-trip.
	    	if (backagain.hasRootElement()) {
	    		UnitTestUtil.normalizeAttributes(backagain.getRootElement());
	    	}
			actual = xout.outputString(backagain);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed to round-trip the document with exception: " 
					+ e.getMessage() + "\n" + expect);
		}
    	assertEquals(expect, actual);
    }
    
    private void roundTrip(SAXSetup setup, List<? extends Content> content) {
    	XMLOutputter xout = new XMLOutputter(Format.getRawFormat());
    	Document tstdoc = new Document();
    	for (Object o : content) {
    		tstdoc.addContent((Content)o);
    	}
    	String expect = xout.outputString(tstdoc);
    	
    	String actual = null;
    	try {
    		// convert the input to a SAX Stream
    		SAXHandler handler = new SAXHandler();
        	SAXOutputter saxout = setup == null 
        			? new SAXOutputter(handler, handler, handler, handler, handler) 
        			: setup.buildOutputter(handler);

        	saxout.output(content);
        	
			Document backagain = handler.getDocument();
			
			// get a String representation of the round-trip.
	    	if (backagain.hasRootElement()) {
	    		UnitTestUtil.normalizeAttributes(backagain.getRootElement());
	    	}
			actual = xout.outputString(backagain);
		} catch (JDOMException e) {
			e.printStackTrace();
			fail("Failed to round-trip the document with exception: " 
					+ e.getMessage() + "\n" + expect);
		}
    	assertEquals(expect, actual);
    }
    

	@Test
	public void testSAXOutputter() {
		SAXOutputter saxout = new SAXOutputter(null, null, null, null);
		assertTrue(null == saxout.getContentHandler());
		assertTrue(null == saxout.getDTDHandler());
		assertTrue(null == saxout.getLexicalHandler());
		assertTrue(null == saxout.getErrorHandler());
		assertTrue(null == saxout.getDeclHandler());
		assertTrue(null == saxout.getEntityResolver());
	}

	@Test
	public void testSAXOutputterContentHandler() {
		SAXOutputter saxout = new SAXOutputter(null);
		assertTrue(null == saxout.getContentHandler());
		assertTrue(null == saxout.getDTDHandler());
		assertTrue(null == saxout.getLexicalHandler());
		assertTrue(null == saxout.getErrorHandler());
		assertTrue(null == saxout.getDeclHandler());
		assertTrue(null == saxout.getEntityResolver());

		DefaultHandler2 handler = new DefaultHandler2();
		saxout = new SAXOutputter(handler);
		assertTrue(handler == saxout.getContentHandler());
		assertTrue(null == saxout.getDTDHandler());
		assertTrue(null == saxout.getLexicalHandler());
		assertTrue(null == saxout.getErrorHandler());
		assertTrue(null == saxout.getDeclHandler());
		assertTrue(null == saxout.getEntityResolver());
	}

	@Test
	public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerEntityResolver() {
		SAXOutputter saxout = new SAXOutputter(null, null, null, null);
		assertTrue(null == saxout.getContentHandler());
		assertTrue(null == saxout.getDTDHandler());
		assertTrue(null == saxout.getLexicalHandler());
		assertTrue(null == saxout.getErrorHandler());
		assertTrue(null == saxout.getDeclHandler());
		assertTrue(null == saxout.getEntityResolver());

		DefaultHandler2 handler = new DefaultHandler2();
		saxout = new SAXOutputter(handler, handler, handler, handler);
		assertTrue(handler == saxout.getContentHandler());
		assertTrue(handler == saxout.getDTDHandler());
		assertTrue(null == saxout.getLexicalHandler());
		assertTrue(handler == saxout.getErrorHandler());
		assertTrue(null == saxout.getDeclHandler());
		assertTrue(handler == saxout.getEntityResolver());
	}

	@Test
	public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerEntityResolverLexicalHandler() {
		SAXOutputter saxout = new SAXOutputter(null, null, null, null, null);
		assertTrue(null == saxout.getContentHandler());
		assertTrue(null == saxout.getDTDHandler());
		assertTrue(null == saxout.getLexicalHandler());
		assertTrue(null == saxout.getErrorHandler());
		assertTrue(null == saxout.getDeclHandler());
		assertTrue(null == saxout.getEntityResolver());

		DefaultHandler2 handler = new DefaultHandler2();
		saxout = new SAXOutputter(handler, handler, handler, handler, handler);
		assertTrue(handler == saxout.getContentHandler());
		assertTrue(handler == saxout.getDTDHandler());
		assertTrue(handler == saxout.getLexicalHandler());
		assertTrue(handler == saxout.getErrorHandler());
		assertTrue(null == saxout.getDeclHandler());
		assertTrue(handler == saxout.getEntityResolver());
	}

	@Test
	public void testContentHandler() {
		ContentHandler handler = new DefaultHandler2();
		SAXOutputter saxout = new SAXOutputter();
		assertNull(saxout.getContentHandler());
		saxout.setContentHandler(handler);
		assertTrue(saxout.getContentHandler() == handler);
	}

	@Test
	public void testErrorHandler() {
		ErrorHandler handler = new DefaultHandler2();
		SAXOutputter saxout = new SAXOutputter();
		assertNull(saxout.getErrorHandler());
		saxout.setErrorHandler(handler);
		assertTrue(saxout.getErrorHandler() == handler);
	}

	@Test
	public void testDTDHandler() {
		DTDHandler handler = new DefaultHandler2();
		SAXOutputter saxout = new SAXOutputter();
		assertNull(saxout.getDTDHandler());
		saxout.setDTDHandler(handler);
		assertTrue(saxout.getDTDHandler() == handler);
	}

	@Test
	public void testEntityResolver() {
		EntityResolver handler = new DefaultHandler2();
		SAXOutputter saxout = new SAXOutputter();
		assertNull(saxout.getEntityResolver());
		saxout.setEntityResolver(handler);
		assertTrue(saxout.getEntityResolver() == handler);
	}

	@Test
	public void testLexicalHandler() {
		LexicalHandler handler = new DefaultHandler2();
		SAXOutputter saxout = new SAXOutputter();
		assertNull(saxout.getLexicalHandler());
		saxout.setLexicalHandler(handler);
		assertTrue(saxout.getLexicalHandler() == handler);
	}

	@Test
	public void testDeclHandler() {
		DeclHandler handler = new DefaultHandler2();
		SAXOutputter saxout = new SAXOutputter();
		assertNull(saxout.getDeclHandler());
		saxout.setDeclHandler(handler);
		assertTrue(saxout.getDeclHandler() == handler);
	}

	@Test
	public void testReportNamespaceDeclarations() throws JDOMException {
		final AtomicInteger includesxmlns = new AtomicInteger(0);
		DefaultHandler2 handler = new DefaultHandler2() {
			@Override
			public void startElement(String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {
				super.startElement(arg0, arg1, arg2, arg3);
				for (int i = arg3.getLength() - 1; i >= 0; i--) {
					if (arg3.getQName(i).startsWith("xmlns")) {
						includesxmlns.incrementAndGet();
					}
				}
			}
		};
		Element root = new Element("root", Namespace.getNamespace("", "rooturi"));
		root.addContent(new Element("child", Namespace.getNamespace("pfx", "childuri")));
		Document doc = new Document(root);
		SAXOutputter saxout = new SAXOutputter(handler, handler, handler, handler, handler);
		assertFalse(saxout.getReportNamespaceDeclarations());
		saxout.output(doc);
		// we should not have reported a separate namespace xmlns:pfx="uri"
		// thus, includesxmlns == 0
		assertTrue(includesxmlns.get() == 0);
		saxout.setReportNamespaceDeclarations(true);
		assertTrue(saxout.getReportNamespaceDeclarations());
		saxout.output(doc);
		// we should have reported a separate namespace xmlns:pfx="childuri"
		// and also xmlns="rooturi"
		// thus, includesxmlns == 2
		assertTrue(includesxmlns.get() == 2);
	}

	@Test
	public void testReportDTDEvents() throws JDOMException {
		final AtomicInteger includesdtd = new AtomicInteger(0);
		DefaultHandler2 handler = new DefaultHandler2() {
			@Override
			public void notationDecl(String arg0, String arg1, String arg2)
					throws SAXException {
				super.notationDecl(arg0, arg1, arg2);
				includesdtd.incrementAndGet();
			}
			@Override
			public void unparsedEntityDecl(String arg0, String arg1,
					String arg2, String arg3) throws SAXException {
				super.unparsedEntityDecl(arg0, arg1, arg2, arg3);
				includesdtd.incrementAndGet();
			}
		};
		DocType dt = new DocType("root");
		dt.setInternalSubset("<!ELEMENT root EMPTY>" +
				"<!ENTITY junk SYSTEM \"junk\" NDATA junk>" +
				"<!NOTATION junk PUBLIC \"junk\">");
		Document doc = new Document(new Element("root"), dt);
		SAXOutputter saxout = new SAXOutputter(handler, handler, handler, handler, handler);
		assertTrue(saxout.getReportDTDEvents());
		saxout.setReportDTDEvents(false);
		assertFalse(saxout.getReportDTDEvents());
		saxout.output(doc);
		// we should not have reported start/end DTD
		// thus, includesdtd == 0
		assertTrue(includesdtd.get() == 0);
		saxout.setReportDTDEvents(true);
		assertTrue(saxout.getReportDTDEvents());
		saxout.output(doc);
		// we should not have reported start/end DTD
		// thus, includesdtd == 2
		assertTrue(includesdtd.get() == 2);
	}

	@Test
	public void testFeature() throws SAXNotRecognizedException, SAXNotSupportedException {
	    String NAMESPACES_SAX_FEATURE = "http://xml.org/sax/features/namespaces";
	    String NS_PREFIXES_SAX_FEATURE = "http://xml.org/sax/features/namespace-prefixes";
	    String VALIDATION_SAX_FEATURE = "http://xml.org/sax/features/validation";
	    String JUNK_FEATURE = "http://xml.org/sax/features/junk";


		SAXOutputter saxout = new SAXOutputter();
		
		assertFalse(saxout.getFeature(NS_PREFIXES_SAX_FEATURE));
		assertFalse(saxout.getReportNamespaceDeclarations());
		
		assertTrue(saxout.getFeature(VALIDATION_SAX_FEATURE));
		assertTrue(saxout.getReportDTDEvents());
		
		assertTrue(saxout.getFeature(NAMESPACES_SAX_FEATURE));
		
		saxout.setReportDTDEvents(false);
		saxout.setReportNamespaceDeclarations(true);
		
		assertTrue(saxout.getFeature(NS_PREFIXES_SAX_FEATURE));
		assertTrue(saxout.getReportNamespaceDeclarations());
		
		assertFalse(saxout.getFeature(VALIDATION_SAX_FEATURE));
		assertFalse(saxout.getReportDTDEvents());
		
		assertTrue(saxout.getFeature(NAMESPACES_SAX_FEATURE));
		
		saxout.setFeature(NS_PREFIXES_SAX_FEATURE, false);
		saxout.setFeature(VALIDATION_SAX_FEATURE, true);
		
		assertFalse(saxout.getFeature(NS_PREFIXES_SAX_FEATURE));
		assertFalse(saxout.getReportNamespaceDeclarations());
		
		assertTrue(saxout.getFeature(VALIDATION_SAX_FEATURE));
		assertTrue(saxout.getReportDTDEvents());
		
		assertTrue(saxout.getFeature(NAMESPACES_SAX_FEATURE));
		
		// can re-set the feature to true.
		saxout.setFeature(NAMESPACES_SAX_FEATURE, true);
		
		try {
			saxout.setFeature(NAMESPACES_SAX_FEATURE, false);
			fail("Should not be able to set Feature" + NAMESPACES_SAX_FEATURE + " to false");
		} catch (SAXNotSupportedException e) {
			// good
		} catch (Exception e) {
			fail ("Expecting SAXNotSupportedException, not " + e.getClass().getName());
		}
		
		try {
			saxout.getFeature(JUNK_FEATURE);
			fail("Should not be able to get Feature" + JUNK_FEATURE);
		} catch (SAXNotRecognizedException e) {
			// good
		} catch (Exception e) {
			fail ("Expecting SAXNotRecognizedException, not " + e.getClass().getName());
		}
		
		try {
			saxout.setFeature(JUNK_FEATURE, true);
			fail("Should not be able to set Feature" + JUNK_FEATURE);
		} catch (SAXNotRecognizedException e) {
			// good
		} catch (Exception e) {
			fail ("Expecting SAXNotRecognizedException, not " + e.getClass().getName());
		}
		
	}

	@Test
	public void testProperty() throws SAXNotRecognizedException, SAXNotSupportedException {
		String lhs = "http://xml.org/sax/properties/lexical-handler";
		String dhs = "http://xml.org/sax/properties/declaration-handler";
		String jhs = "http://xml.org/sax/properties/junk-handler";
		SAXOutputter saxout = new SAXOutputter();
		DefaultHandler2 handler = new DefaultHandler2();
		assertNull(saxout.getProperty(lhs));
		assertNull(saxout.getProperty(dhs));
		assertNull(saxout.getLexicalHandler());
		assertNull(saxout.getDeclHandler());
		
		saxout.setDeclHandler(handler);
		saxout.setLexicalHandler(handler);
		assertTrue(handler == saxout.getProperty(lhs));
		assertTrue(handler == saxout.getProperty(dhs));
		assertTrue(handler == saxout.getLexicalHandler());
		assertTrue(handler == saxout.getDeclHandler());
		
		saxout.setDeclHandler(null);
		saxout.setLexicalHandler(null);
		assertNull(saxout.getProperty(lhs));
		assertNull(saxout.getProperty(dhs));

		saxout.setProperty(lhs, handler);
		saxout.setProperty(dhs, handler);
		assertTrue(handler == saxout.getProperty(lhs));
		assertTrue(handler == saxout.getProperty(dhs));
		assertTrue(handler == saxout.getLexicalHandler());
		assertTrue(handler == saxout.getDeclHandler());

		try {
			saxout.getProperty(jhs);
			fail("Should not be able to get " + jhs);
		} catch (SAXNotRecognizedException e) {
			// good
		} catch (Exception e) {
			fail ("Expecting SAXNotRecognizedException, not " + e.getClass().getName());
		}
		try {
			saxout.setProperty(jhs, new Object());
			fail("Should not be able to set " + jhs);
		} catch (SAXNotRecognizedException e) {
			// good
		} catch (Exception e) {
			fail ("Expecting SAXNotRecognizedException, not " + e.getClass().getName());
		}
	}

	@Test
	public void testSAXOutputDocumentSimple() {
		Document doc = new Document(new Element("root"));
		roundTrip(doc);
	}

    @Test
    public void testSAXOutputDocumentFull() {
    	Document doc = new Document();
    	doc.addContent(new DocType("root"));
    	doc.addContent(new Comment("This is a document"));
    	doc.addContent(new ProcessingInstruction("jdomtest", ""));
    	Element e = new Element("root");
    	e.addContent(new EntityRef("ref"));
    	doc.addContent(e);
    	roundTrip(doc);
    }
    
    @Test
    public void testOutputDocumentRootAttNS() {
    	Document doc = new Document();
    	Element e = new Element("root");
    	e.setAttribute(new Attribute("att", "val", Namespace.getNamespace("ans", "mynamespace")));
    	doc.addContent(e);
    	roundTrip(doc);
    }
    
    @Test
    public void testOutputDocumentFullNoLexical() throws JDOMException {
    	Document doc = new Document();
    	doc.addContent(new ProcessingInstruction("jdomtest", ""));
    	Element e = new Element("root");
    	doc.addContent(e);
    	e.addContent(new Text("text"));
    	e.addContent(new EntityRef("ref"));
    	
    	// the Lexical handler is what helps track comments,
    	// and identifies CDATA sections.
    	// as a result the output of CDATA structures appears as plain text.
    	// and comments are dropped....
    	e.addContent(new Text("cdata"));
    	String expect = new XMLOutputter().outputString(doc);
    	e.removeContent(2);
    	e.addContent(new CDATA("cdata"));
    	e.addContent(new Comment("This is a document"));
    	
    	SAXHandler handler = new SAXHandler();
    	SAXOutputter saxout = new SAXOutputter(handler, handler, handler, handler);
    	saxout.output(doc);
    	
    	Document act = handler.getDocument();
    	String actual = new XMLOutputter().outputString(act);
    	
    	assertEquals(expect, actual);
    	
    }
    
    @Test
    public void testOutputDocumentNoErrorHandler() {
    	DefaultHandler2 handler = new DefaultHandler2() {
    		@Override
    		public void error(SAXParseException arg0) throws SAXException {
    			throw new SAXException ("foo!");
    		}
    	};
    	
    	SAXOutputter saxout = new SAXOutputter(handler);
    	
    	try {
			saxout.outputFragment(new DocType("rootemt"));
			fail("SHould have exception!");
		} catch (JDOMException e) {
			assertTrue(e.getMessage().indexOf("rootemt") >= 0);
		}
    	
    	saxout.setErrorHandler(handler);
    	
    	try {
			saxout.outputFragment(new DocType("rootemt"));
			fail("SHould have exception!");
		} catch (JDOMException e) {
			assertTrue(e.getMessage().endsWith("foo!"));
		}
    	
    }
    
    @Test
    public void testOutputDocumentAttributes() {
    	Element emt = new Element("root");
    	emt.setAttribute("att", "val");
		Document doc = new Document(emt);
		roundTrip(doc);
    }
    
    @Test
    public void testOutputDocumentNamespaces() {
		Element emt = new Element("root", Namespace.getNamespace("ns", "myns"));
		Namespace ans = Namespace.getNamespace("ans", "attributens");
		emt.addNamespaceDeclaration(ans);
		emt.addNamespaceDeclaration(Namespace.getNamespace("two", "two"));
		emt.setAttribute(new Attribute("att", "val", ans));
		emt.addContent(new Element("child", Namespace.getNamespace("", "childuri")));
		Document doc = new Document(emt);
		roundTrip(doc);
    }
    
	@Test
	public void testSAXOutputList() {
		List<Content> list = new ArrayList<Content>();
		list.add(new ProcessingInstruction("jdomtest", ""));
		list.add(new Comment("comment"));
		list.add(new Element("root"));
		roundTrip(null, list);
	}

    @Test
    public void testOutputElementAttributes() {
    	Element emt = new Element("root");
    	emt.setAttribute("att", "val");
    	emt.setAttribute(new Attribute("attx", "valx", AttributeType.UNDECLARED));
		roundTrip(null, emt);
    }
    
    @Test
    public void testSAXOutputElementNamespaces() {
		Element emt = new Element("root", Namespace.getNamespace("ns", "myns"));
		Namespace ans = Namespace.getNamespace("ans", "attributens");
		emt.addNamespaceDeclaration(ans);
		emt.addNamespaceDeclaration(Namespace.getNamespace("two", "two"));
		emt.setAttribute(new Attribute("att", "val", ans));
		emt.addContent(new Element("child", Namespace.getNamespace("", "childns")));
		roundTrip(null, emt);
    }
    
	@Test
	public void testOutputFragmentList() {
		List<Content> list = new ArrayList<Content>();
		list.add(new ProcessingInstruction("jdomtest", ""));
		list.add(new Comment("comment"));
		list.add(new CDATA("foo"));
		list.add(new Element("root"));
		list.add(new Text("bar"));
		roundTripFragment(null, list);
	}

	@Test
	public void testOutputFragmentContent() {
		roundTripFragment(null, new ProcessingInstruction("jdomtest", ""));
		roundTripFragment(null, new Comment("comment"));
		roundTripFragment(null, new CDATA("foo"));
		roundTripFragment(null, new Element("root"));
		roundTripFragment(null, new Text("bar"));
	}

	@Test
	public void testOutputNullContent() throws JDOMException {
		DefaultHandler2 handler = new DefaultHandler2() {
			@Override
			public void startDocument() throws SAXException {
				throw new SAXException("SHould not be reaching this, ever");
			}
		};
		SAXOutputter saxout = new SAXOutputter(handler, handler, handler, handler, handler);
		
		Document doc = null;
		Element emt = null;
		List<Content> list = null;
		List<Content> empty = new ArrayList<Content>();
		saxout.output(doc);
		saxout.output(emt);
		saxout.output(list);
		saxout.output(empty);
		saxout.outputFragment(emt);
		saxout.outputFragment(list);
		saxout.outputFragment(empty);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetLocator() throws JDOMException {
		SAXOutputter saxout = new SAXOutputter();
		final AtomicBoolean coderan = new AtomicBoolean(false);
		assertTrue(saxout.getLocator() == null);
		DefaultHandler2 handler = new DefaultHandler2() {
			JDOMLocator locator = null;
			@Override
			public void setDocumentLocator(Locator loc) {
				if (loc instanceof JDOMLocator) {
					locator = (JDOMLocator)loc;
				} else {
					fail ("We excpected the locator to be a JDOMLocator, not " + loc);
				}
			}
			@Override
			public void endElement(String uri, String localname, String qname) {
				assertNotNull(locator);
				assertTrue(locator.getNode() != null);
				assertTrue(locator.getNode() instanceof Element);
				Element emt = (Element)locator.getNode();
				assertEquals(emt.getName(), localname);
				coderan.set(true);
			}
		};
		saxout.setContentHandler(handler);
		saxout.setDTDHandler(handler);
		saxout.setEntityResolver(handler);
		saxout.setLexicalHandler(handler);
		saxout.setDeclHandler(handler);
		saxout.setErrorHandler(handler);
		Document doc = new Document(new Element("root"));
		saxout.output(doc);
		assertTrue(coderan.get());
	}
	
	
	
	
	@Test
	public void testGetSetFormat() {
    	SAXOutputter sout = new SAXOutputter();
    	Format def = sout.getFormat();
    	assertTrue(def != null);
    	Format f = Format.getPrettyFormat();
    	sout.setFormat(f);
    	assertTrue(f == sout.getFormat());
    	sout.setFormat(null);
    	TestFormat.checkEquals(def, sout.getFormat());
	}

    @Test
    public void testGetSetDOMOutputProcessor() {
    	SAXOutputProcessor dop = new AbstractSAXOutputProcessor() {
    		// nothing.
		};
		
    	SAXOutputter dout = new SAXOutputter();
    	SAXOutputProcessor def = dout.getSAXOutputProcessor();
    	assertTrue(def != null);
    	dout.setSAXOutputProcessor(dop);
    	assertTrue(dop == dout.getSAXOutputProcessor());
    	dout.setSAXOutputProcessor(null);
    	assertEquals(def, dout.getSAXOutputProcessor());
    }
    
	@Override
	public String outputString(Format format, Document doc) {
		SAXHandler handler = new SAXHandler();
		SAXOutputter saxout = new SAXOutputter(null, format, handler, handler, 
				handler, handler, handler);
		try {
			saxout.output(doc);
		} catch (Exception e) {
			throw new IllegalStateException("Could not output", e);
		}
		XMLOutputter xout = new XMLOutputter();
		xout.getFormat().setLineSeparator(LineSeparator.NL);
		return xout.outputString(handler.getDocument());
	}

	@Override
	public String outputString(Format format, DocType doctype) {
		SAXHandler handler = new SAXHandler();
		SAXOutputter saxout = new SAXOutputter(null, format, handler, handler, 
				handler, handler, handler);
		List<Content> list = new ArrayList<Content>(1);
		list.add(doctype);
		//list.add(new Element("root"));
		
		try {
			saxout.output(list);
		} catch (Exception e) {
			throw new IllegalStateException("Could not output", e);
		}
		XMLOutputter xout = new XMLOutputter();
		xout.getFormat().setLineSeparator(LineSeparator.NL);
		return xout.outputString(handler.getDocument().getDocType());
	}

	@Override
	public String outputString(Format format, Element element) {
		SAXHandler handler = new SAXHandler();
		SAXOutputter saxout = new SAXOutputter(null, format, handler, handler, 
				handler, handler, handler);
		
		try {
			saxout.output(element);
		} catch (Exception e) {
			throw new IllegalStateException("Could not output", e);
		}
		XMLOutputter xout = new XMLOutputter();
		xout.getFormat().setLineSeparator(LineSeparator.NL);
		return xout.outputString(handler.getDocument().getRootElement());
	}

	@Override
	public String outputString(Format format, List<? extends Content> list) {
		SAXHandler handler = new SAXHandler();
		SAXOutputter saxout = new SAXOutputter(null, format, handler, handler, 
				handler, handler, handler);
		
		try {
			handler.startDocument();
			handler.startElement("", "root", "root", new AttributesImpl());
			saxout.outputFragment(list);
			handler.endElement("", "root", "root");
			handler.endDocument();
		} catch (Exception e) {
			throw new IllegalStateException("Could not output", e);
		}
		XMLOutputter xout = new XMLOutputter();
		xout.getFormat().setLineSeparator(LineSeparator.NL);
		return xout.outputString(handler.getDocument().getRootElement().getContent());
	}

	@Override
	public String outputString(Format format, CDATA cdata) {
		SAXHandler handler = new SAXHandler();
		SAXOutputter saxout = new SAXOutputter(null, format, handler, handler, 
				handler, handler, handler);
		
		try {
			handler.startDocument();
			handler.startElement("", "root", "root", new AttributesImpl());
			saxout.outputFragment(cdata);
			handler.endElement("", "root", "root");
			handler.endDocument();
		} catch (Exception e) {
			throw new IllegalStateException("Could not output", e);
		}
		XMLOutputter xout = new XMLOutputter();
		xout.getFormat().setLineSeparator(LineSeparator.NL);
		return xout.outputString(handler.getDocument().getRootElement().getContent());
	}

	@Override
	public String outputString(Format format, Text text) {
		SAXHandler handler = new SAXHandler();
		SAXOutputter saxout = new SAXOutputter(null, format, handler, handler, 
				handler, handler, handler);
		
		try {
			handler.startDocument();
			handler.startElement("", "root", "root", new AttributesImpl());
			saxout.outputFragment(text);
			handler.endElement("", "root", "root");
			handler.endDocument();
		} catch (Exception e) {
			throw new IllegalStateException("Could not output", e);
		}
		XMLOutputter xout = new XMLOutputter();
		xout.getFormat().setLineSeparator(LineSeparator.NL);
		return xout.outputString(handler.getDocument().getRootElement().getContent());
	}

	@Override
	public String outputString(Format format, Comment comment) {
		SAXHandler handler = new SAXHandler();
		SAXOutputter saxout = new SAXOutputter(null, format, handler, handler, 
				handler, handler, handler);
		
		try {
			handler.startDocument();
			handler.startElement("", "root", "root", new AttributesImpl());
			saxout.outputFragment(comment);
			handler.endElement("", "root", "root");
			handler.endDocument();
		} catch (Exception e) {
			throw new IllegalStateException("Could not output", e);
		}
		XMLOutputter xout = new XMLOutputter();
		xout.getFormat().setLineSeparator(LineSeparator.NL);
		return xout.outputString(handler.getDocument().getRootElement().getContent());
	}

	@Override
	public String outputString(Format format, ProcessingInstruction pi) {
		SAXHandler handler = new SAXHandler();
		SAXOutputter saxout = new SAXOutputter(null, format, handler, handler, 
				handler, handler, handler);
		
		try {
			handler.startDocument();
			handler.startElement("", "root", "root", new AttributesImpl());
			saxout.outputFragment(pi);
			handler.endElement("", "root", "root");
			handler.endDocument();
		} catch (Exception e) {
			throw new IllegalStateException("Could not output", e);
		}
		XMLOutputter xout = new XMLOutputter();
		xout.getFormat().setLineSeparator(LineSeparator.NL);
		return xout.outputString(handler.getDocument().getRootElement().getContent());
	}

	@Override
	public String outputString(Format format, EntityRef entity) {
		SAXHandler handler = new SAXHandler();
		SAXOutputter saxout = new SAXOutputter(null, format, handler, handler, 
				handler, handler, handler);
		
		try {
			handler.startDocument();
			handler.startElement("", "root", "root", new AttributesImpl());
			saxout.outputFragment(entity);
			handler.endElement("", "root", "root");
			handler.endDocument();
		} catch (Exception e) {
			throw new IllegalStateException("Could not output", e);
		}
		XMLOutputter xout = new XMLOutputter();
		xout.getFormat().setLineSeparator(LineSeparator.NL);
		return xout.outputString(handler.getDocument().getRootElement().getContent());
	}

	@Override
	public String outputElementContentString(Format format, Element element) {
		SAXHandler handler = new SAXHandler();
		SAXOutputter saxout = new SAXOutputter(null, format, handler, handler, 
				handler, handler, handler);
		
		try {
			saxout.outputFragment(element.getContent());
		} catch (Exception e) {
			throw new IllegalStateException("Could not output", e);
		}
		XMLOutputter xout = new XMLOutputter();
		xout.getFormat().setLineSeparator(LineSeparator.NL);
		return xout.outputString(handler.getDocument().getRootElement());
	}

	
	
	
	@Test
	@Override
	@Ignore
	public void testOutputElementExpandEmpty() {
		// Can't control expand in SAXOutputter.
	}
	
	@Test
	@Override
	@Ignore
	public void testOutputElementMultiAllWhiteExpandEmpty() {
		// Can't control expand in SAXOutputter.
	}
	
	@Test
	@Ignore
	@Override
	public void testDocTypeSimpleISS() {
		//Cannot preserve internal subset in DOCTYPE through the round-trip test.
	}
	
	@Test
	@Ignore
	@Override
	public void testDocTypePublicSystemID() {
		//Cannot test with a SystemID because it needs to be resolved/referenced
	}
	
	@Test
	@Ignore
	@Override
	public void testDocTypePublicSystemIDISS() {
		//Cannot test with a SystemID because it needs to be resolved/referenced
		//Cannot preserve internal subset in DOCTYPE through the round-trip test.
	}
	
	@Test
	@Ignore
	@Override
	public void testDocTypeSystemID() {
		//Cannot test with a SystemID because it needs to be resolved/referenced
	}
	
	@Test
	@Ignore
	@Override
	public void testDocTypeSystemIDISS() {
		//Cannot preserve internal subset in DOCTYPE through the round-trip test.
	}
	
	@Test
	@Override
	@Ignore
	public void testDocumentDocType() {
		// override because we can't control whitespace outside of the root element
	}

	
	@Test
	@Override
	@Ignore
	public void testOutputDocTypeInternalSubset() {
		//Cannot preserve internal subset in DOCTYPE through the round-trip test.
	}
	
	
	@Test
	@Override
	@Ignore
	public void testOutputDocTypeSystem() {
		//Cannot test with a SystemID because it needs to be resolved/referenced
	}
	
	@Test
	@Override
	@Ignore
	public void testOutputDocTypePublic() {
		// cannot test with a non-resolved publicID
	}
	
	@Test
	@Override
	@Ignore
	public void testOutputDocTypePublicSystem() {
		//Cannot test with a SystemID because it needs to be resolved/referenced
	}


	@Test
	@Override
	@Ignore
	public void testOutputDocumentOmitEncoding() {
		// Cannot test for formatting outside of root element
	}

	@Test
	@Override
	@Ignore
	public void testOutputDocumentOmitDeclaration() {
		// Cannot test for formatting outside of root element
	}

	@Test
	public void testNoNamespaceIssue60 () throws JDOMException {
		Document doc = new Document();
		Namespace ns = Namespace.getNamespace("myurl");
		Element root = new Element("root", ns);
		Element child = new Element("child", ns);
		root.addContent(child);
		doc.setRootElement(root);
		final String[] count = new String[1];
		
		child.setAttribute("att", "val");
		
		ContentHandler ch = new DefaultHandler2() {
			@Override
			public void startPrefixMapping(String pfx, String uri)
					throws SAXException {
				if ("".equals(pfx) && "".equals(uri)) {
					fail("Should not be firing xmlns=\"\"");
				}
				if (!"".equals(pfx)) {
					fail("we should not have prefix " + pfx);
				}
				if (count[0] != null) {
					fail("we should not have multiple mappings " + pfx + " -> " + uri);
				}
				count[0] = uri;
			}
		};
		SAXOutputter saxout = new SAXOutputter(ch);
		saxout.output(doc);
		assertTrue("myurl".equals(count[0]));
	}
	
}
