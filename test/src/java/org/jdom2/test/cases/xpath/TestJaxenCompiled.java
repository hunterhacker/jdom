package org.jdom2.test.cases.xpath;

import org.jdom2.xpath.XPathFactory;
import org.jdom2.xpath.jaxen.JaxenXPathFactory;

@SuppressWarnings({"javadoc"})
public class TestJaxenCompiled extends AbstractTestXPathCompiled {
	
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
	
}
