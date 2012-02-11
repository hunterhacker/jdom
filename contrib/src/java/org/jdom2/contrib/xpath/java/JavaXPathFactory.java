package org.jdom2.contrib.xpath.java;

import java.util.Map;

import javax.xml.xpath.XPathFactory;

import org.jdom2.Namespace;
import org.jdom2.filter.Filter;
import org.jdom2.xpath.XPathExpression;

/**
 * An XPathFactory using the underlying infrastructure in javax.xml.xpath.*
 * to process the XPath expressions against the JDOM content.
 * @author Rolf Lear
 *
 */
public class JavaXPathFactory extends org.jdom2.xpath.XPathFactory {
	
	ThreadLocal<XPathFactory> localfac = 
			new ThreadLocal<XPathFactory>();

	@Override
	public <T> XPathExpression<T> compile(String expression, Filter<T> filter,
			Map<String, Object> variables, Namespace... namespaces) {
		// Java XPath factories are not thread safe... use a thread-local.
		XPathFactory fac = localfac.get();
		if (fac == null) {
			fac = XPathFactory.newInstance();
			localfac.set(fac);
		}
		return new JavaXPathExpression<T>(
				expression, filter, variables, namespaces, fac);
	}

}
