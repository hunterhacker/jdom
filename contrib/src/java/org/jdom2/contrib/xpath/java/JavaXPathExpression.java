package org.jdom2.contrib.xpath.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathVariableResolver;

import org.w3c.dom.Attr;
import org.w3c.dom.NodeList;

import org.jdom2.Attribute;
import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.Content;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.EntityRef;
import org.jdom2.JDOMConstants;
import org.jdom2.Namespace;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;
import org.jdom2.contrib.dom.DOM;
import org.jdom2.contrib.dom.Wrapper;
import org.jdom2.filter.Filter;
import org.jdom2.internal.ArrayCopy;
import org.jdom2.xpath.util.AbstractXPathCompiled;

/**
 * An XPathExpression that uses the native Java5 javax.xml.xpath mechanisms
 * to implement XPath.
 * 
 * @author Rolf Lear
 *
 * @param <T> the type of the coerced results.
 */
class JavaXPathExpression<T> extends AbstractXPathCompiled <T> 
	implements  XPathVariableResolver, NamespaceContext {
	
	private static final Namespace[] EMPTYNS = new Namespace[0];
	
	final javax.xml.xpath.XPathExpression rawexpression;
	final Namespace[] nsraw;
	
	/**
	 * Construct the XPathExpression.
	 * @param query The XPath query to create.
	 * @param filter The coercion filter.
	 * @param variables The variable map
	 * @param namespaces The scope namespaces.
	 * @param fac The XMLFactory to use for construction.
	 */
	public JavaXPathExpression(final String query, final Filter<T> filter,
			final Map<String, Object> variables, final Namespace[] namespaces, 
			final XPathFactory fac) {
		super(query, filter, variables, namespaces);
		nsraw = namespaces == null ? EMPTYNS : 
			ArrayCopy.copyOf(namespaces, namespaces.length);
		final XPath xp = fac.newXPath();
		xp.setNamespaceContext(this);
		xp.setXPathVariableResolver(this);
		try {
			rawexpression = xp.compile(query);
		} catch (XPathExpressionException e) {
			throw new IllegalArgumentException(
					"Unable to compile expression: " + query, e);
		}
	}

	@Override
	public String getNamespaceURI(String prefix) {
		return getNamespace(prefix).getURI();
	}

	@Override
	public String getPrefix(String namespaceURI) {
		if (namespaceURI == null) {
			// the API doc for NamespaceContext says to throw IllegalArgument 
			// not NullPointer.
			throw new IllegalArgumentException("Null namespaceURI");
		}
		if (JDOMConstants.NS_URI_XML.equals(namespaceURI)) {
			return JDOMConstants.NS_PREFIX_XML;
		}
		if (JDOMConstants.NS_URI_XMLNS.equals(namespaceURI)) {
			return JDOMConstants.NS_PREFIX_XMLNS;
		}
		for (Namespace ns : nsraw) {
			if (namespaceURI.equals(ns.getURI())) {
				return ns.getPrefix();
			}
		}
		return null;
	}

	@Override
	public Iterator<String> getPrefixes(String namespaceURI) {
		if (namespaceURI == null) {
			// the API doc for NamespaceContext says to throw IllegalArgument 
			// not NullPointer.
			throw new IllegalArgumentException("Null namespaceURI");
		}
		if (JDOMConstants.NS_URI_XML.equals(namespaceURI)) {
			return Collections.singleton(JDOMConstants.NS_PREFIX_XML).iterator();
		}
		if (JDOMConstants.NS_URI_XMLNS.equals(namespaceURI)) {
			return Collections.singleton(JDOMConstants.NS_PREFIX_XMLNS).iterator();
		}
		ArrayList<String> pfx = new ArrayList<String>();
		for (Namespace ns : nsraw) {
			if (namespaceURI.equals(ns.getURI())) {
				pfx.add(ns.getPrefix());
			}
		}
		return Collections.unmodifiableList(pfx).iterator();
	}

	@Override
	public Object resolveVariable(QName variableName) {
		return getVariable(variableName.getLocalPart(),
				Namespace.getNamespace(variableName.getNamespaceURI()));
	}
	
	private Object wrapContext(Object context) {
		if (context instanceof Content) {
			switch (((Content)context).getCType()) {
				case CDATA :
					return DOM.wrap((CDATA)context);
				case Comment:
					return DOM.wrap((Comment)context);
				case DocType:
					return DOM.wrap((DocType)context);
				case Element:
					return DOM.wrap((Element)context);
				case EntityRef:
					return DOM.wrap((EntityRef)context);
				case ProcessingInstruction:
					return DOM.wrap((ProcessingInstruction)context);
				case Text:
					return DOM.wrap((Text)context);
			}
			throw new IllegalStateException("Should never break out of swich");
		} else if (context instanceof Attribute) {
			return DOM.wrap((Attribute)context);
		} else if (context instanceof Document) {
			return DOM.wrap((Document)context);
		} else {
			throw new IllegalArgumentException(
					"Unable to process context: " + context);
		}
	}
	
	private Object unWrap(final Object o) {
		if (o instanceof Wrapper) {
			return ((Wrapper)o).getWrapped();
		}
		if (o instanceof Attr) {
			// odd one....
			// DOM has no node for Namespaces, so the typical XPath engine on
			// DOM nodes returns an Attr representation for the Namespace.
			// easy to check....
			Attr a = (Attr)o;
			if (JDOMConstants.NS_URI_XMLNS.equals(a.getNamespaceURI())) {
				// it is an xml declaration.
				return Namespace.getNamespace(a.getLocalName(), a.getValue());
			}
			if (JDOMConstants.NS_PREFIX_DEFAULT.equals(a.getNamespaceURI()) &&
					JDOMConstants.NS_PREFIX_XMLNS.equals(a.getLocalName())) {
				return Namespace.getNamespace(a.getValue());
			}
		}
		return o;
	}

	@Override
	protected List<?> evaluateRawAll(Object context) {
		final Object ctx = wrapContext(context);
		try {
			final NodeList nl = (NodeList)rawexpression.evaluate(
					ctx, XPathConstants.NODESET);
			final int sz = nl.getLength();
			ArrayList<Object> ret = new ArrayList<Object>(sz);
			for (int i = 0; i < sz; i++) {
				ret.add(unWrap(nl.item(i)));
			}
			return ret;
		} catch (XPathExpressionException e) {
			throw new IllegalStateException(
					"Unable to evaluate expression: " + this.toString(), e);
		}
	}

	@Override
	protected Object evaluateRawFirst(Object context) {
		final Object ctx = wrapContext(context);
		try {
			final NodeList nl = (NodeList)rawexpression.evaluate(
					ctx, XPathConstants.NODESET);
			if (nl.getLength() == 0) {
				return null;
			}
			return unWrap(nl.item(0));
		} catch (XPathExpressionException e) {
			throw new IllegalStateException(
					"Unable to evaluate expression: " + this.toString(), e);
		}
	}
	
}
