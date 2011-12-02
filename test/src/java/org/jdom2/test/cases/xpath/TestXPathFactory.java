package org.jdom2.test.cases.xpath;

import static org.junit.Assert.*;

import org.junit.Test;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.xpath.XPath;
import org.jdom2.xpath.XPathFactory;
import org.jdom2.xpath.jaxen.JaxenXPathFactory;

@SuppressWarnings("javadoc")
public class TestXPathFactory {

	@Test
	public void testNewInstance() throws JDOMException {
		XPathFactory xpf = XPathFactory.newInstance();
		assertNotNull(xpf);
		assertTrue(xpf != XPathFactory.newInstance());
		XPath xp = xpf.compile(".");
		Element emt = new Element("root");
		assertTrue(emt == xp.selectSingleNode(emt));
	}

	@Test
	public void testNewInstanceString() throws JDOMException {
		XPathFactory xpf = XPathFactory.newInstance(JaxenXPathFactory.class.getName());
		assertNotNull(xpf);
		assertTrue(xpf != XPathFactory.newInstance(JaxenXPathFactory.class.getName()));
		XPath xp = xpf.compile(".");
		Element emt = new Element("root");
		assertTrue(emt == xp.selectSingleNode(emt));
	}

}
