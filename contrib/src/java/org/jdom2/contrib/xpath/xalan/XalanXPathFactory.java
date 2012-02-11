package org.jdom2.contrib.xpath.xalan;

import java.util.Map;

import org.jdom2.Namespace;
import org.jdom2.filter.Filter;
import org.jdom2.xpath.XPathExpression;

/**
 * An XPathFactory that wraps the JDOM content in a thin DOM layer, and then
 * uses that to seed the Xalan API for XPath processing.
 * @author Rolf Lear
 *
 */
public class XalanXPathFactory extends org.jdom2.xpath.XPathFactory {
	
	@Override
	public <T> XPathExpression<T> compile(String expression, Filter<T> filter,
			Map<String, Object> variables, Namespace... namespaces) {
		// Java XPath factories are not thread safe... use a thread-local.
		return new XalanXPathExpression<T>(
				expression, filter, variables, namespaces);
	}

}
