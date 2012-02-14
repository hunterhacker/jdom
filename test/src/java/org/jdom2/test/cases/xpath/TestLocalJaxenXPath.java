package org.jdom2.test.cases.xpath;

import org.jdom2.JDOMException;
import org.jdom2.xpath.XPath;
import org.jdom2.xpath.jaxen.JDOMXPath;

/**
 * 
 * @author Rolf Lear
 * @deprecated in lieu of TestJaxenCompiled
 */
@Deprecated
public class TestLocalJaxenXPath extends AbstractTestXPath {
	
	@Override
	@Deprecated
	XPath buildPath(String path) throws JDOMException {
		final XPath ret = new JDOMXPath(path);
		return ret;
	}
	
}
