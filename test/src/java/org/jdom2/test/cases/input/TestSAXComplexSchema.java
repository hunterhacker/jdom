/**
 * 
 */
package org.jdom2.test.cases.input;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.test.util.FidoFetch;


/**
 * @author Rolf Lear
 *
 */
@SuppressWarnings("javadoc")
public class TestSAXComplexSchema {

	
	/**
	 * Test method for {@link org.jdom.input.SAXBuilder#build(java.io.File)}.
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testBuildFileOldWay() throws IOException {
		SAXBuilder builder = new SAXBuilder(true);
		builder.setFeature("http://xml.org/sax/features/namespaces", true);
		builder.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
		builder.setFeature("http://apache.org/xml/features/validation/schema", true);
		
		URL rurl = FidoFetch.getFido().getURL("/xsdcomplex/input.xml");
		
		
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
			List<Element> kids = root.getChildren("data", defns);
			for (Iterator<Element> it = kids.iterator(); it.hasNext(); ) {
				Element data = it.next();
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

	/**
	 * Test method for {@link org.jdom.input.SAXBuilder#build(java.io.File)}.
	 */
	@Test
	public void testBuildFileNewSAX() throws IOException {
		SAXBuilder builder = new SAXBuilder(XMLReaders.XSDVALIDATING);
		
		URL rurl = FidoFetch.getFido().getURL("/xsdcomplex/input.xml");
		
		
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
			List<Element> kids = root.getChildren("data", defns);
			for (Iterator<Element> it = kids.iterator(); it.hasNext(); ) {
				Element data = it.next();
				assertTrue(defns.equals(data.getNamespace()));
				Attribute att = data.getAttribute("type", Namespace.NO_NAMESPACE);
				assertTrue("Could not find type attribute in default ns.", att != null);
				assertTrue(att.isSpecified());
				att = data.getAttribute("type", impns);
				assertTrue("Could not find type attribute in impns.", att != null);
				assertFalse(att.isSpecified());
			}
		} catch (JDOMException e) {
			e.printStackTrace();
			fail("Parsing failed. See stack trace.");
		}
		
	}

}
