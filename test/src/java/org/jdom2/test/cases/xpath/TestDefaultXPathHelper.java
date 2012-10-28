package org.jdom2.test.cases.xpath;

import org.jdom2.xpath.XPathFactory;

@SuppressWarnings("javadoc")
public class TestDefaultXPathHelper extends AbstractTestXPathHepler {
	@Override
	XPathFactory getFactory() {
		return XPathFactory.instance();
	}
}
