package org.jdom2.xpath.jaxen;

import org.jdom2.JDOMException;
import org.jdom2.xpath.XPath;
import org.jdom2.xpath.XPathFactory;

/**
 * This simple Factory creates XPath instances tailored to the Jaxen library.
 * 
 * @author Rolf Lear
 *
 */
public class JaxenXPathFactory extends XPathFactory {
	
	/**
	 * The public default constructor used by the XPathFactory.
	 */
	public JaxenXPathFactory() {
		// do nothing.
	}

	@Override
	public XPath compile(String xpath) throws JDOMException {
		return new JDOMXPath(xpath);
	}

}
