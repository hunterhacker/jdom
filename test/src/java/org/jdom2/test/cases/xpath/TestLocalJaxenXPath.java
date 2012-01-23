package org.jdom2.test.cases.xpath;

import org.jdom2.JDOMException;
import org.jdom2.xpath.XPath;
import org.jdom2.xpath.jaxen.JDOMXPath;

@SuppressWarnings({"javadoc", "deprecation"})
public class TestLocalJaxenXPath extends AbstractTestXPath {
	
	@Override
	XPath buildPath(String path) throws JDOMException {
		final XPath ret = new JDOMXPath(path);
		return ret;
	}
	
}
