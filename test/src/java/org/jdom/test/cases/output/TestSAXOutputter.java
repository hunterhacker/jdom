package org.jdom.test.cases.output;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.output.SAXOutputter;

import junit.framework.TestCase;

/**
 * @author Rolf Lear
 *
 */
public class TestSAXOutputter extends TestCase {

	/**
	 * @throws JDOMException if there's a problem
	 */
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
