package org.jdom2.test.cases.xpath;

import org.jdom2.JDOMException;
import org.jdom2.xpath.XPath;
import org.jdom2.xpath.jaxen.JDOMXPath;

@SuppressWarnings("javadoc")
public class TestLocalJaxenXPath extends AbstractTestXPath {
	
	@Override
	XPath buildPath(String path) throws JDOMException {
		return new JDOMXPath(path);
	}
	
}
