package org.jdom.test.cases.output;

/* Please run replic.pl on me ! */
/**
 * Please put a description of your test here.
 * 
 * @author unascribed
 * @version 0.1
 */
import junit.framework.*;

import org.jdom.*;
import java.io.*;
import java.util.*;
import org.jdom.output.*;
import org.jdom.input.*;

public final class TestDOMOutputter
extends junit.framework.TestCase
{
	/**
	 * Resource Bundle for various testing resources
	 */
	private ResourceBundle rb = ResourceBundle.getBundle("org.jdom.test.Test");

	/**
	 * the directory where needed resource files will be kept
	 */
	private String resourceDir = "";

	/**
	 *  a directory for temporary storage of files
	 */
	private String scratchDir = "";
    /**
     *  Construct a new instance. 
     */
    public TestDOMOutputter(String name) {
        super(name);
    }
    /**
     * The main method runs all the tests in the text ui
     */
    public static void main (String args[]) 
     {
        junit.textui.TestRunner.run(suite());
    }
	/**
	 * This method is called before a test is executed.
	 */
	public void setUp() {
		resourceDir = rb.getString("test.resourceRoot");
		scratchDir = rb.getString("test.scratchDirectory");

	}
    /**
     * The suite method runs all the tests
     */
public static Test suite () {
        TestSuite suite = new TestSuite(TestDOMOutputter.class);
        return suite;
    }
    /**
     * This method is called after a test is executed.
     */
    public void tearDown() {
        // your code goes here.
    }


    public void test_ForceNamespaces() throws JDOMException {
         Document doc = new Document();
         Element root = new Element("root");
         Element el = new Element("a");
         el.setText("abc");
         root.addContent(el);
         doc.setRootElement(root);

         DOMOutputter out = new DOMOutputter();
         out.setForceNamespaceAware(true);
         org.w3c.dom.Document dom = out.output(doc);
         org.w3c.dom.Element domel = dom.getDocumentElement();
         //System.out.println("Dom impl: "+ domel.getClass().getName());
         assertNotNull(domel.getLocalName()); 
    }
}
