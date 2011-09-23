package org.jdom2.test.cases.output;

/* Please run replic.pl on me ! */
/**
 * Please put a description of your test here.
 * 
 * @author unascribed
 * @version 0.1
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jdom2.Attribute;
import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.EntityRef;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;
import org.jdom2.input.DOMBuilder;
import org.jdom2.output.DOMOutputter;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.test.util.UnitTestUtil;
import org.junit.Test;
import org.junit.runner.JUnitCore;

public final class TestDOMOutputter {

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

    @Test
    public void test_ForceNamespaces() throws JDOMException {
         Document doc = new Document();
         Element root = new Element("root");
         Element el = new Element("a");
         el.setText("abc");
         root.addContent(el);
         doc.setRootElement(root);

         DOMOutputter out = new DOMOutputter();
         assertFalse(out.getForceNamespaceAware());
         out.setForceNamespaceAware(true);
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
    public void testOutputDocumentSimple() {
    	Document doc = new Document();
    	doc.addContent(new Element("root"));
    	roundTrip(doc);
    }
    
    @Test
    public void testOutputDocumentFull() {
    	Document doc = new Document();
    	doc.addContent(new DocType("root"));
    	doc.addContent(new Comment("This is a document"));
    	doc.addContent(new ProcessingInstruction("jdomtest", ""));
    	doc.addContent(new Element("root"));
    	roundTrip(doc);
    }
    
    @Test
    public void testOutputElementAttributes() {
    	Element emt = new Element("root");
    	emt.setAttribute("att", "val");
		Document doc = new Document(emt);
		roundTrip(doc);
    }
    
    @Test
    public void testOutputElementNamespaces() {
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
			@Override
			public DOMOutputter buildOutputter() {
				DOMOutputter dom = new DOMOutputter();
				dom.setForceNamespaceAware(true);
				return dom;
			}
		};
		roundTrip(setup, doc);
    }
    
}
