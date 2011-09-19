package org.jdom2.test.cases.xpath;

import org.jdom2.JDOMException;
import org.jdom2.xpath.JaxenXPath;
import org.jdom2.xpath.XPath;

public class TestJaxenXPath extends AbstractTestXPath {

	
	@Override
	XPath buildPath(String path) throws JDOMException {
		return new JaxenXPath(path);
	}

}
