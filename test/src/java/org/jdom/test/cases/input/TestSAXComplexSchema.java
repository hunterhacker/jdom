/**
 * 
 */
package org.jdom.test.cases.input;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author rlear
 *
 */
public class TestSAXComplexSchema extends TestCase {

	
    /**
     * The main method runs all the tests in the text ui
     */
    public static void main (String args[]) 
     {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * The suite method runs all the tests
     */
    public static Test suite () {
        return new TestSuite(TestSAXComplexSchema.class);
    }
	
	/**
	 * Test method for {@link org.jdom.input.SAXBuilder#build(java.io.File)}.
	 */
	public void testBuildFile() throws IOException {
		SAXBuilder builder = new SAXBuilder(true);
		builder.setFeature("http://xml.org/sax/features/namespaces", true);
		builder.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
		builder.setFeature("http://apache.org/xml/features/validation/schema", true);
		
		File inputdir = new File(".");
		String relpath = "resources/xsdcomplex/input.xml";
		File inpf = new File(inputdir, relpath);
		if (!inpf.exists()) {
			relpath = "test/" + relpath;
		}
		URL furl = inputdir.toURI().toURL();
		URL rurl = new URL(furl, relpath);
		
		
		try {
			Document doc = builder.build(rurl);
			XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
			StringWriter sw = new StringWriter();
			out.output(doc, sw);
			assertTrue(sw.toString().length() > 0);
			//System.out.println("Document parsed. Content:\n" + xml + "\n");
			
			Namespace defns = Namespace.getNamespace("http://www.jdom.org/tests/default");
			Namespace impns = Namespace.getNamespace("http://www.jdom.org/tests/imp");
			
			Element root = doc.getRootElement();
			assertTrue(root != null);
			assertTrue("test".equals(root.getName()));
			List kids = root.getChildren("data", defns);
			for (Iterator it = kids.iterator(); it.hasNext(); ) {
				Element data = (Element)it.next();
				assertTrue(defns.equals(data.getNamespace()));
				Attribute att = data.getAttribute("type", Namespace.NO_NAMESPACE);
				assertTrue("Could not find type attribute in default ns.", att != null);
				att = data.getAttribute("type", impns);
				assertTrue("Could not find type attribute in impns.", att != null);
			}
		} catch (JDOMException e) {
			e.printStackTrace();
			fail("Parsing failed. See stack trace.");
		}
		
	}

}
