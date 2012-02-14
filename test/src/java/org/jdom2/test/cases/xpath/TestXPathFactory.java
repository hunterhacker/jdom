package org.jdom2.test.cases.xpath;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.jdom2.xpath.jaxen.JaxenXPathFactory;

@SuppressWarnings("javadoc")
public class TestXPathFactory {

	@Test
	public void testNewInstance() {
		XPathFactory xpf = XPathFactory.instance();
		assertNotNull(xpf);
		assertTrue(xpf != XPathFactory.newInstance(xpf.getClass().getName()));
		XPathExpression<?> xp = xpf.compile(".");
		Element emt = new Element("root");
		assertTrue(emt == xp.evaluateFirst(emt));
	}

	@Test
	public void testNewInstanceString() {
		XPathFactory xpf = XPathFactory.newInstance(JaxenXPathFactory.class.getName());
		assertNotNull(xpf);
		assertTrue(xpf != XPathFactory.newInstance(JaxenXPathFactory.class.getName()));
		XPathExpression<?> xp = xpf.compile(".");
		Element emt = new Element("root");
		assertTrue(emt == xp.evaluateFirst(emt));
	}

	@Test
	public void testNewInstanceCompileNSList() {
		XPathFactory xpf = XPathFactory.newInstance(JaxenXPathFactory.class.getName());
		assertNotNull(xpf);
		assertTrue(xpf != XPathFactory.newInstance(JaxenXPathFactory.class.getName()));
		XPathExpression<?> xp = xpf.compile(".");
		Element emt = new Element("root");
		assertTrue(emt == xp.evaluateFirst(emt));
		xp = xpf.compile(".", Filters.element(), null, emt.getNamespacesInScope());
		assertTrue(emt == xp.evaluateFirst(emt));
	}

}
