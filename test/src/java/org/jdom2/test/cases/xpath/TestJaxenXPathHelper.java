package org.jdom2.test.cases.xpath;

import org.jdom2.xpath.XPathFactory;
import org.jdom2.xpath.jaxen.JaxenXPathFactory;

@SuppressWarnings("javadoc")
public class TestJaxenXPathHelper extends AbstractTestXPathHepler {
	@Override
	XPathFactory getFactory() {
		return XPathFactory.newInstance(JaxenXPathFactory.class.getName());
	}
}
