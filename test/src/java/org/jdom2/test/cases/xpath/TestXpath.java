package org.jdom2.test.cases.xpath;

import static org.junit.Assert.*;

import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.xpath.XPath;
import org.junit.Ignore;
import org.junit.Test;

public class TestXpath {
	/* 
	 * Test the static methods on the abstract class.
	 */

	@Test
	public void testNewInstance() {
		try {
			Document doc = new Document(new Element("main"));
			XPath xp = XPath.newInstance("/");
			assertTrue(doc == xp.selectSingleNode(doc));
		} catch (JDOMException e) {
			e.printStackTrace();
			fail("Could not process XPath.newInstance()");
		}
	}

	@Test
	@Ignore
	//TODO Cannot test this method because it could mess up the factory for other testing.
	public void testSetXPathClass() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectNodesObjectString() {
		try {
			Document doc = new Document(new Element("main"));
			List<?> lst = XPath.selectNodes(doc, "/");
			assertTrue(lst.size() == 1);
			assertTrue(doc == lst.get(0));
		} catch (JDOMException e) {
			e.printStackTrace();
			fail("Could not process XPath.selectNodes()");
		}
	}

	@Test
	public void testSelectSingleNodeObjectString() {
		try {
			Document doc = new Document(new Element("main"));
			Object ret = XPath.selectSingleNode(doc, "/");
			assertTrue(doc == ret);
		} catch (JDOMException e) {
			e.printStackTrace();
			fail("Could not process XPath.selectNodes()");
		}
	}

}
