package org.jdom2.test.cases.xpath;

import org.junit.Ignore;
import org.junit.Test;

import org.jdom2.Comment;
import org.jdom2.Element;
import org.jdom2.xpath.XPathFactory;
import org.jdom2.xpath.jaxen.JaxenXPathFactory;

@SuppressWarnings({"javadoc"})
public class TestJaxenCompiled extends AbstractTestXPathCompiled {
	
	public TestJaxenCompiled() {
		super(true);
	}

	private static final XPathFactory myfac = new JaxenXPathFactory();
	
	@Override
	XPathFactory getFactory() {
		return myfac;
	}

	public static void main(String[] args) {
		System.setProperty("jaxp.debug", "true");
		javax.xml.xpath.XPathFactory myfacx = javax.xml.xpath.XPathFactory.newInstance();
		System.out.println(myfacx);
	}
	
	@Override
	@Test
	@Ignore
	public void testXPathOR() {
		// JAXEN Does not support document order for unions....
		super.testXPathOR();
	}
	
	@Test
	@Ignore
	public void testSpecialOR() {
		Element m = new Element("main");
		m.setAttribute("att", "value");
		m.addContent(new Comment("comment"));
		checkXPath("/main/node()[1] | /main/@*", main, null, m.getAttribute("att"), m.getContent(0));
	}
	

}
