package org.jdom2.test.cases.output;

/* Please run replic.pl on me ! */
/**
 * Please put a description of your test here.
 * 
 * @author unascribed
 * @version 0.1
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.w3c.dom.Attr;

import org.jdom2.Attribute;
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
import org.jdom2.adapters.DOMAdapter;
import org.jdom2.adapters.JAXPDOMAdapter;
import org.jdom2.input.DOMBuilder;
import org.jdom2.output.DOMOutputter;
import org.jdom2.output.Format;
import org.jdom2.output.LineSeparator;
import org.jdom2.output.XMLOutputter;
import org.jdom2.output.support.AbstractDOMOutputProcessor;
import org.jdom2.output.support.DOMOutputProcessor;
import org.jdom2.test.util.UnitTestUtil;

@SuppressWarnings("javadoc")
public final class TestDOMOutputter extends AbstractTestOutputter {

    /**
     * The main method runs all the tests in the text ui
     */
    public static void main (String args[]) 
     {
        JUnitCore.runClasses(TestDOMOutputter.class);
    }
    
    private interface DOMSetup {
    	public DOMOutputter buildOutputter();
    }
    
    public TestDOMOutputter() {
		super(true, true, false, false, true);
	}
    
    @SuppressWarnings("deprecation")
	@Test
    public void test_ForceNamespaces() throws JDOMException {
         Document doc = new Document();
         Element root = new Element("root");
         Element el = new Element("a");
         el.setText("abc");
         root.addContent(el);
         doc.setRootElement(root);

         DOMOutputter out = new DOMOutputter();
         // deprecated stuff..
         assertTrue(out.getForceNamespaceAware());
         out.setForceNamespaceAware(false);
         assertTrue(out.getForceNamespaceAware());
         org.w3c.dom.Document dom = out.output(doc);
         org.w3c.dom.Element domel = dom.getDocumentElement();
         //System.out.println("Dom impl: "+ domel.getClass().getName());
         assertNotNull(domel.getLocalName()); 
    }
    
    private void roundTrip(Document doc) {
    	roundTrip(null, doc);
    }
    
    private void roundTrip(DOMSetup setup, Document doc) {
    	XMLOutputter xout = new XMLOutputter(Format.getRawFormat());
    	// create a String representation of the input.
    	if (doc.hasRootElement()) {
    		UnitTestUtil.normalizeAttributes(doc.getRootElement());
    	}
    	String expect = xout.outputString(doc);
    	
    	String actual = null;
    	try {
    		// convert the input to a DOM document
        	DOMOutputter domout = setup == null ? new DOMOutputter() : setup.buildOutputter();
			org.w3c.dom.Document outonce = domout.output(doc);
			
			// convert the DOM document back again.
			DOMBuilder builder = new DOMBuilder();
			Document backagain = builder.build(outonce);
			
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
    public void testDOMAdapter() {
    	Document doc = new Document(new Element("root"));
    	DOMSetup setup = new DOMSetup() {
			@Override
			public DOMOutputter buildOutputter() {
				return new DOMOutputter();
			}
		};
		roundTrip(setup, doc);
    }
    
    @Test
    public void testDOMOutputDocumentSimple() {
    	Document doc = new Document();
    	doc.addContent(new Element("root"));
    	roundTrip(doc);
    }
    
    @Test
    public void testDOMOutputDocumentFull() {
    	Document doc = new Document();
    	doc.addContent(new DocType("root"));
    	doc.addContent(new Comment("This is a document"));
    	doc.addContent(new ProcessingInstruction("jdomtest", ""));
    	doc.addContent(new Element("root"));
    	roundTrip(doc);
    }
    
    @Test
    public void testDOMOutputElementAttributes() {
    	Element emt = new Element("root");
    	emt.setAttribute("att", "val");
		Document doc = new Document(emt);
		roundTrip(doc);
    }
    
    @Test
    public void testDOMOutputElementNamespaces() {
		Element emt = new Element("root", Namespace.getNamespace("ns", "myns"));
		Namespace ans = Namespace.getNamespace("ans", "attributens");
		emt.addNamespaceDeclaration(ans);
		emt.addNamespaceDeclaration(Namespace.getNamespace("two", "two"));
		emt.setAttribute(new Attribute("att", "val", ans));
		Document doc = new Document(emt);
		roundTrip(doc);
    }
    
    @Test
    public void testOutputElementNamespaceNoPrefix() {
		Element emt = new Element("root", Namespace.getNamespace("", "myns"));
		Namespace ans = Namespace.getNamespace("ans", "attributens");
		emt.addNamespaceDeclaration(ans);
		emt.addNamespaceDeclaration(Namespace.getNamespace("two", "two"));
		emt.setAttribute(new Attribute("att", "val", ans));
		Document doc = new Document(emt);
		roundTrip(doc);
    }
    
    @Test
    public void testOutputElementNamespacesForceNS() {
		Element emt = new Element("root", Namespace.getNamespace("ns", "myns"));
		Namespace ans = Namespace.getNamespace("ans", "attributens");
		emt.addNamespaceDeclaration(ans);
		emt.addNamespaceDeclaration(Namespace.getNamespace("two", "two"));
		emt.setAttribute(new Attribute("att", "val", ans));
		Document doc = new Document(emt);
		DOMSetup setup = new DOMSetup() {
			@SuppressWarnings("deprecation")
			@Override
			public DOMOutputter buildOutputter() {
				DOMOutputter dom = new DOMOutputter();
				dom.setForceNamespaceAware(true);
				return dom;
			}
		};
		roundTrip(setup, doc);
    }
    
    @Test
    public void testOutputElementFull() {
		Element emt = new Element("root");
		emt.setAttribute("att1", "val1");
		emt.setAttribute("att2", "val2");
		emt.addContent(new Comment("comment"));
		emt.addContent(new Text("txt"));
		emt.addContent(new ProcessingInstruction("jdomtest", ""));
		emt.addContent(new Element("child"));
		emt.addContent(new EntityRef("ref"));
		emt.addContent(new CDATA("cdata"));
		Document doc = new Document(emt);
		roundTrip(doc);
    }
    
    @Test
    public void testOutputElementFullForceNS() {
		Element emt = new Element("root");
		emt.setAttribute("att1", "val1");
		emt.setAttribute("att2", "val2");
		emt.addContent(new Comment("comment"));
		emt.addContent(new Text("txt"));
		emt.addContent(new ProcessingInstruction("jdomtest", ""));
		emt.addContent(new Element("child"));
		emt.addContent(new EntityRef("ref"));
		emt.addContent(new CDATA("cdata"));
		Document doc = new Document(emt);
		DOMSetup setup = new DOMSetup() {
			@SuppressWarnings("deprecation")
			@Override
			public DOMOutputter buildOutputter() {
				DOMOutputter dom = new DOMOutputter();
				dom.setForceNamespaceAware(true);
				return dom;
			}
		};
		roundTrip(setup, doc);
    }
    
    @Test
    public void testWithDocType() {
    	DocType dt = new DocType("root");
    	dt.setInternalSubset("<!ELEMENT root (#PCDATA)>");
    	Element root = new Element("root");
    	Document doc = new Document(root, dt);

    	roundTrip(doc);
    }
    
    
    @Test
    public void testGetSetFormat() {
    	DOMOutputter dout = new DOMOutputter();
    	Format def = dout.getFormat();
    	assertTrue(def != null);
    	Format f = Format.getPrettyFormat();
    	dout.setFormat(f);
    	assertTrue(f == dout.getFormat());
    	dout.setFormat(null);
    	TestFormat.checkEquals(def, dout.getFormat());
    }
    
    @Test
    public void testGetSetDOMOutputProcessor() {
    	DOMOutputProcessor dop = new AbstractDOMOutputProcessor() {
    		// nothing.
		};
		
    	DOMOutputter dout = new DOMOutputter();
    	DOMOutputProcessor def = dout.getDOMOutputProcessor();
    	assertTrue(def != null);
    	dout.setDOMOutputProcessor(dop);
    	assertTrue(dop == dout.getDOMOutputProcessor());
    	dout.setDOMOutputProcessor(null);
    	assertEquals(def, dout.getDOMOutputProcessor());
    }
    
    @Test
    public void testGetSetDOMAdapter() {
    	DOMAdapter dop = new JAXPDOMAdapter();
		
    	DOMOutputter dout = new DOMOutputter();
    	DOMAdapter def = dout.getDOMAdapter();
    	assertTrue(def != null);
    	dout.setDOMAdapter(dop);
    	assertTrue(dop == dout.getDOMAdapter());
    	dout.setDOMAdapter(null);
    	assertEquals(def, dout.getDOMAdapter());
    }
    
    @Test
    public void testFullConstructor() {
    	DOMOutputProcessor dop = new AbstractDOMOutputProcessor() {
    		// nothing.
		};
		
    	DOMAdapter dap = new JAXPDOMAdapter();
    	
    	Format fmt = Format.getPrettyFormat();
    	
    	DOMOutputter out = new DOMOutputter(null, null, null);
    	
    	DOMOutputProcessor defop = out.getDOMOutputProcessor();
    	DOMAdapter defap = out.getDOMAdapter();
    	Format defmt = out.getFormat();
    	
    	out = new DOMOutputter(dap, fmt, dop);
    	
    	assertTrue(dap == out.getDOMAdapter());
    	assertTrue(dop == out.getDOMOutputProcessor());
    	assertTrue(fmt == out.getFormat());

    	out = new DOMOutputter(null, null, null);
    	assertTrue(defap == out.getDOMAdapter());
    	assertTrue(defop == out.getDOMOutputProcessor());
    	TestFormat.checkEquals(defmt , out.getFormat());
    }
    
    @Test
    public void testDOMOutputProcessorConstructor() {
    	DOMOutputProcessor dop = new AbstractDOMOutputProcessor() {
    		// nothing to add
		};
    	
    	DOMOutputter out = new DOMOutputter((DOMOutputProcessor)null);
    	
    	DOMOutputProcessor defap = out.getDOMOutputProcessor();
    	
    	out = new DOMOutputter(dop);
    	
    	assertTrue(dop == out.getDOMOutputProcessor());

    	out = new DOMOutputter((DOMOutputProcessor)null);
    	assertTrue(defap == out.getDOMOutputProcessor());
    }
    
    @Test
    public void testDOMAdapterConstructor() {
    	DOMAdapter dap = new JAXPDOMAdapter();
    	
    	DOMOutputter out = new DOMOutputter((DOMAdapter)null);
    	
    	DOMAdapter defap = out.getDOMAdapter();
    	
    	out = new DOMOutputter(dap);
    	
    	assertTrue(dap == out.getDOMAdapter());

    	out = new DOMOutputter((DOMAdapter)null);
    	assertTrue(defap == out.getDOMAdapter());
    }
    
    @Test
	@SuppressWarnings("deprecation")
    public void testDOMAdapterNameConstructor() {
		DOMOutputter out = new DOMOutputter((String)null);
    	assertTrue(out.getDOMAdapter() instanceof JAXPDOMAdapter);
    	out = new DOMOutputter(JAXPDOMAdapter.class.getName());
    	assertTrue(out.getDOMAdapter() instanceof JAXPDOMAdapter);
    }
    
    @Test
    public void testOutputAttribute() throws JDOMException {
    	DOMOutputter out = getOutputter(Format.getRawFormat());
    	org.w3c.dom.Document domdoc = out.getDOMAdapter().createDocument();
    	Attribute att = new Attribute("name", "val");
    	
    	Attr doma = out.output(att);
    	assertEquals("name", doma.getNodeName());
    	assertEquals("name", doma.getLocalName());
    	assertEquals("val", doma.getNodeValue());
    	// Android can have "" values, xerces has null. Technically it is implementation dependant
    	assertTrue(null == doma.getPrefix() || "".equals(doma.getPrefix()));
    	// Android can have "" values, xerces has null. Technically it is implementation dependant
    	assertTrue(null == doma.getNamespaceURI() || "".equals(doma.getNamespaceURI()));
    	assertTrue(domdoc != doma.getOwnerDocument());
    	
    	doma = out.output(domdoc, att);
    	assertEquals("name", doma.getNodeName());
    	assertEquals("name", doma.getLocalName());
    	assertEquals("val", doma.getNodeValue());
    	assertTrue(null == doma.getPrefix() || "".equals(doma.getPrefix()));
    	assertTrue(null == doma.getNamespaceURI() || "".equals(doma.getNamespaceURI()));
    	assertTrue(domdoc == doma.getOwnerDocument());
    	
    	att = new Attribute("name", "val", Namespace.getNamespace("ns", "http://jdom.org/junit/ns"));
    	
    	doma = out.output(att);
    	assertEquals("ns:name", doma.getNodeName());
    	assertEquals("name", doma.getLocalName());
    	assertEquals("val", doma.getNodeValue());
    	assertEquals("ns", doma.getPrefix());
    	assertEquals("http://jdom.org/junit/ns", doma.getNamespaceURI());
    	assertTrue(domdoc != doma.getOwnerDocument());
    	
    	doma = out.output(domdoc, att);
    	assertEquals("ns:name", doma.getNodeName());
    	assertEquals("name", doma.getLocalName());
    	assertEquals("val", doma.getNodeValue());
    	assertEquals("ns", doma.getPrefix());
    	assertEquals("http://jdom.org/junit/ns", doma.getNamespaceURI());
    	assertTrue(domdoc == doma.getOwnerDocument());
    	
    }
    
    private final DOMOutputter getOutputter(Format format) {
    	DOMOutputter outputter = new DOMOutputter();
    	outputter.setFormat(format);
    	return outputter;
    }
    
	private final String nodeToString (Object input) {
    	try {
    		// Do not use Transforms, messes up end-of-lines, etc.
    		// instead, reuse the DOMBuilder and convert back to string using
    		// XMLOutputter.
    		if (input == null) {
    			return "";
    		}
    		XMLOutputter xout = new XMLOutputter();
    		xout.getFormat().setLineSeparator(LineSeparator.NL);
    		DOMBuilder builder = new DOMBuilder();
    		
    		if (input instanceof List) {
    			StringBuilder sb = new StringBuilder();
    			for (Object o : (List<?>)input) {
    				sb.append(nodeToString(o));
    			}
    			return sb.toString();
    		}
    		if (input instanceof org.w3c.dom.Document) {
    			return xout.outputString(builder.build((org.w3c.dom.Document)input));
    		}
    		if (input instanceof org.w3c.dom.CDATASection) {
				return xout.outputString(builder.build((org.w3c.dom.CDATASection)input));
    		}
    		if (input instanceof org.w3c.dom.Comment) {
				return xout.outputString(builder.build((org.w3c.dom.Comment)input));
    		}
    		if (input instanceof org.w3c.dom.DocumentType) {
				return xout.outputString(builder.build((org.w3c.dom.DocumentType)input));
    		}
    		if (input instanceof org.w3c.dom.Element) {
				return xout.outputString(builder.build((org.w3c.dom.Element)input));
    		}
    		if (input instanceof org.w3c.dom.EntityReference) {
				return xout.outputString(builder.build((org.w3c.dom.EntityReference)input));
    		}
    		if (input instanceof org.w3c.dom.ProcessingInstruction) {
				return xout.outputString(builder.build((org.w3c.dom.ProcessingInstruction)input));
    		}
    		if (input instanceof org.w3c.dom.Text) {
				return xout.outputString(builder.build((org.w3c.dom.Text)input));
    		}

    		return null;
//	    	TransformerFactory factory = TransformerFactory.newInstance();
//	    	Transformer transformer = factory.newTransformer();
//	    	transformer.setOutputProperty("{http://xml.apache.org/xalan}line-separator","\n");	    	
//	    	StringWriter writer = new StringWriter();
//	    	for (Node node : nodes) {
//	    		if (node instanceof org.w3c.dom.Document) {
//	    			((org.w3c.dom.Document)node).setXmlStandalone(true);
//	    			transformer.setOutputProperty("omit-xml-declaration", "no");
//	    		} else {
//	    			transformer.setOutputProperty("omit-xml-declaration", "yes");
//	    		}
//		    	Result result = new StreamResult(writer);
//		    	Source source = new DOMSource(node);
//		    	transformer.transform(source, result);
//	    	}
//	    	writer.close();
//	    	String xml = writer.toString();
//	    	return xml;
    	} catch (Exception e) {
    		throw new IllegalStateException(e);
    	}
    }
    
	@Override
	public String outputString(Format format, Document doc) {
		try {
			DOMOutputter out = getOutputter(format);
			String opta = nodeToString(out.output(doc));
			return opta;
		} catch (JDOMException e) {
			UnitTestUtil.failException("Unexpected JDOMException", e);
			return null;
		}
	}

	@Override
	public String outputString(Format format, DocType doctype) {
		try {
			DOMOutputter out = getOutputter(format);
//			org.w3c.dom.Document doc = out.getDOMAdapter().createDocument();
			String opta = nodeToString(out.output(doctype));
//			String optb = nodeToString(out.output(doc, doctype));
//			assertEquals(opta, optb);
			return opta;
		} catch (JDOMException e) {
			UnitTestUtil.failException("Unexpected JDOMException", e);
			return null;
		}
	}

	@Override
	public String outputString(Format format, Element element) {
		try {
			DOMOutputter out = getOutputter(format);
			org.w3c.dom.Document doc = out.getDOMAdapter().createDocument();
			String opta = nodeToString(out.output(element));
			String optb = nodeToString(out.output(doc, element));
			assertEquals(opta, optb);
			return opta;
		} catch (JDOMException e) {
			UnitTestUtil.failException("Unexpected JDOMException", e);
			return null;
		}
	}

	@Override
	public String outputString(Format format, List<? extends Content> list) {
		try {
			DOMOutputter out = getOutputter(format);
			org.w3c.dom.Document doc = out.getDOMAdapter().createDocument();
			String opta = nodeToString(out.output(list));
			String optb = nodeToString(out.output(doc, list));
			assertEquals(opta, optb);
			return opta;
		} catch (JDOMException e) {
			UnitTestUtil.failException("Unexpected JDOMException", e);
			return null;
		}
	}

	@Override
	public String outputString(Format format, CDATA cdata) {
		try {
			DOMOutputter out = getOutputter(format);
			org.w3c.dom.Document doc = out.getDOMAdapter().createDocument();
			String opta = nodeToString(out.output(cdata));
			String optb = nodeToString(out.output(doc, cdata));
			assertEquals(opta, optb);
			return opta;
		} catch (JDOMException e) {
			UnitTestUtil.failException("Unexpected JDOMException", e);
			return null;
		}
	}

	@Override
	public String outputString(Format format, Text text) {
		try {
			DOMOutputter out = getOutputter(format);
			org.w3c.dom.Document doc = out.getDOMAdapter().createDocument();
			String opta = nodeToString(out.output(text));
			String optb = nodeToString(out.output(doc, text));
			assertEquals(opta, optb);
			return opta;
		} catch (JDOMException e) {
			UnitTestUtil.failException("Unexpected JDOMException", e);
			return null;
		}
	}

	@Override
	public String outputString(Format format, Comment comment) {
		try {
			DOMOutputter out = getOutputter(format);
			org.w3c.dom.Document doc = out.getDOMAdapter().createDocument();
			String opta = nodeToString(out.output(comment));
			String optb = nodeToString(out.output(doc, comment));
			assertEquals(opta, optb);
			return opta;
		} catch (JDOMException e) {
			UnitTestUtil.failException("Unexpected JDOMException", e);
			return null;
		}
	}

	@Override
	public String outputString(Format format, ProcessingInstruction pi) {
		try {
			DOMOutputter out = getOutputter(format);
			org.w3c.dom.Document doc = out.getDOMAdapter().createDocument();
			String opta = nodeToString(out.output(pi));
			String optb = nodeToString(out.output(doc, pi));
			assertEquals(opta, optb);
			return opta;
		} catch (JDOMException e) {
			UnitTestUtil.failException("Unexpected JDOMException", e);
			return null;
		}
	}

	@Override
	public String outputString(Format format, EntityRef entity) {
		try {
			DOMOutputter out = getOutputter(format);
			org.w3c.dom.Document doc = out.getDOMAdapter().createDocument();
			String opta = nodeToString(out.output(entity));
			String optb = nodeToString(out.output(doc, entity));
			assertEquals(opta, optb);
			return opta;
		} catch (JDOMException e) {
			UnitTestUtil.failException("Unexpected JDOMException", e);
			return null;
		}
	}

	@Override
	public String outputElementContentString(Format format, Element element) {
		try {
			DOMOutputter out = getOutputter(format);
			org.w3c.dom.Document doc = out.getDOMAdapter().createDocument();
			String opta = nodeToString(out.output(element.getContent()));
			String optb = nodeToString(out.output(doc, element.getContent()));
			assertEquals(opta, optb);
			return opta;
		} catch (JDOMException e) {
			UnitTestUtil.failException("Unexpected JDOMException", e);
			return null;
		}
	}
    
    @Override
    @Ignore // we can't control expand in DOM.
    public void testOutputElementExpandEmpty() {
    	// pass this test always.
    }

    @Override
    @Ignore // we can't control expand in DOM.
	public void testOutputElementMultiAllWhiteExpandEmpty() {
		// nothing.
	}
    
    @Override
    @Ignore // we can't control expand in DOM.
    public void testOutputDocumentOmitEncoding() {
    	// nothing.
    }
    
    @Override
    @Ignore // we can't control expand in DOM.
    public void testOutputDocumentOmitDeclaration() {
    	// nothing.
    }
    
}
