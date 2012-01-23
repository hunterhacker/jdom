package org.jdom2.xpath.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Namespace;
import org.jdom2.Verifier;
import org.jdom2.filter.Filter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathDiagnostic;

/**
 * A mostly-implemented XPathExpression that only needs two methods to be
 * implemented in order to satisfy the complete API. Subclasses of this
 * <strong>MUST</strong> correctly override the clone() method which in turn
 * should call <code>super.clone();</code>
 * 
 * @param <T>
 *        The generic type of the returned values.
 * @author Rolf Lear
 */
public abstract class AbstractXPathCompiled<T> implements XPathExpression<T> {

	private final Map<String, Namespace> xnamespaces = new HashMap<String, Namespace>();
	// Not final to support cloning.
	private Map<String, Map<String, Object>> xvariables = new HashMap<String, Map<String, Object>>();
	private final String xquery;
	private final Filter<T> xfilter;

	/**
	 * Construct an XPathExpression.
	 * 
	 * @see XPathExpression for conditions which throw
	 *      {@link NullPointerException} or {@link IllegalArgumentException}.
	 * @param query
	 *        The XPath query
	 * @param filter
	 *        The coercion filter.
	 * @param variables
	 *        A map of variables.
	 * @param namespaces
	 *        The namespaces referenced from the query.
	 */
	public AbstractXPathCompiled(final String query, final Filter<T> filter,
			final Map<String, Object> variables, final Namespace[] namespaces) {
		if (query == null) {
			throw new NullPointerException("Null query");
		}
		if (filter == null) {
			throw new NullPointerException("Null filter");
		}
		xnamespaces.put(Namespace.NO_NAMESPACE.getPrefix(),
				Namespace.NO_NAMESPACE);
		if (namespaces != null) {
			for (Namespace ns : namespaces) {
				if (ns == null) {
					throw new NullPointerException("Null namespace");
				}
				final Namespace oldns = xnamespaces.put(ns.getPrefix(), ns);
				if (oldns != null && oldns != ns) {
					throw new IllegalArgumentException(
							"A Namespace with the prefix '" + ns.getPrefix()
									+ "' has already been declared.");
				}
			}
		}

		if (variables != null) {
			for (Map.Entry<String, Object> me : variables.entrySet()) {
				final String qname = me.getKey();
				if (qname == null) {
					throw new NullPointerException("Variable with a null name");
				}
				final int p = qname.indexOf(':');
				final String pfx = p < 0 ? "" : qname.substring(0, p);
				final String lname = p < 0 ? qname : qname.substring(p + 1);

				final String vpfxmsg = Verifier.checkNamespacePrefix(pfx);
				if (vpfxmsg != null) {
					throw new IllegalArgumentException("Prefix '" + pfx
							+ "' for variable " + qname + " is illegal: "
							+ vpfxmsg);
				}
				final String vnamemsg = Verifier.checkXMLName(lname);
				if (vnamemsg != null) {
					throw new IllegalArgumentException("Variable name '"
							+ lname + "' for variable " + qname
							+ " is illegal: " + vnamemsg);
				}

				final Namespace ns = xnamespaces.get(pfx);
				if (ns == null) {
					throw new IllegalArgumentException("Prefix '" + pfx
							+ "' for variable " + qname
							+ " has not been assigned a Namespace.");
				}

				Map<String, Object> vmap = xvariables.get(ns.getURI());
				if (vmap == null) {
					vmap = new HashMap<String, Object>();
					xvariables.put(ns.getURI(), vmap);
				}

				if (vmap.put(lname, me.getValue()) != null) {
					throw new IllegalArgumentException("Variable with name "
							+ me.getKey() + "' has already been defined.");
				}
			}
		}
		xquery = query;
		xfilter = filter;
	}

	/**
	 * Subclasses of this AbstractXPathCompile class must call super.clone() in
	 * their clone methods!
	 * <p>
	 * This would be a sample clone method from a subclass:
	 * 
	 * 
	 * <code><pre>
	 * 		public XPathExpression&lt;T&gt; clone() {
	 * 			{@literal @}SuppressWarnings("unchecked")
	 * 			final MyXPathCompiled&lt;T&gt; ret = (MyXPathCompiled&lt;T&gt;)super.clone();
	 * 			// change any fields that need to be cloned.
	 * 			....
	 * 			return ret;
	 * 		}
	 * </pre></code>
	 * 
	 * Here's the documentation from {@link XPathExpression#clone()}
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public XPathExpression<T> clone() {
		AbstractXPathCompiled<T> ret = null;
		try {
			@SuppressWarnings("unchecked")
			final AbstractXPathCompiled<T> c = (AbstractXPathCompiled<T>) super
					.clone();
			ret = c;
		} catch (CloneNotSupportedException cnse) {
			throw new IllegalStateException(
					"Should never be getting a CloneNotSupportedException!",
					cnse);
		}
		Map<String, Map<String, Object>> vmt = new HashMap<String, Map<String, Object>>();
		for (Map.Entry<String, Map<String, Object>> me : xvariables.entrySet()) {
			final Map<String, Object> cmap = new HashMap<String, Object>();
			for (Map.Entry<String, Object> ne : me.getValue().entrySet()) {
				cmap.put(ne.getKey(), ne.getValue());
			}
			vmt.put(me.getKey(), cmap);
		}
		ret.xvariables = vmt;
		return ret;
	}

	@Override
	public final String getExpression() {
		return xquery;
	}

	@Override
	public final String getNamespace(final String prefix) {
		final Namespace ns = xnamespaces.get(prefix);
		if (ns == null) {
			throw new IllegalArgumentException("Namespace with prefix '"
					+ prefix + "' has not been declared.");
		}
		return ns.getURI();
	}

	@Override
	public final Object getVariable(final String uri, final String name) {
		final Map<String, Object> vmap = xvariables.get(uri);
		if (vmap == null) {
			throw new IllegalArgumentException("Variable with name '" + name
					+ "' in namespace '" + uri + "' has not been declared.");
		}
		final Object ret = vmap.get(name);
		if (ret == null) {
			if (!vmap.containsKey(name)) {
				throw new IllegalArgumentException("Variable with name '"
						+ name + "' in namespace '" + uri
						+ "' has not been declared.");
			}
			// leave translating null variable values to the implementation.
			return null;
		}
		return ret;
	}

	@Override
	public Object setVariable(String uri, String name, Object value) {
		final Object ret = getVariable(uri, name);
		// if that succeeded then we have it easy....
		xvariables.get(uri).put(name, value);
		return ret;
	}

	@Override
	public final Filter<T> getFilter() {
		return xfilter;
	}

	@Override
	public List<T> evaluate(Object context) {
		return xfilter.filter(evaluateRawAll(context));
	}

	/**
	 * 
	 */
	@Override
	public T evaluateFirst(Object context) {
		Object raw = evaluateRawFirst(context);
		if (raw == null) {
			return null;
		}
		return xfilter.filter(raw);
	}

	@Override
	public XPathDiagnostic<T> diagnose(Object context, boolean firstonly) {
		final List<?> result = firstonly ? Collections
				.singletonList(evaluateRawFirst(context))
				: evaluateRawAll(context);
		return new XPathDiagnosticImpl<T>(context, this, result, firstonly);
	}

	@Override
	public String toString() {
		int nscnt = xnamespaces.size();
		int vcnt = 0;
		for (Map<String, Object> cmap : xvariables.values()) {
			vcnt += cmap.size();
		}
		return String.format(
				"[XPathExpression: %d namespaces and %d variables for query %s]",
				nscnt, vcnt, getExpression());
	}

	/**
	 * This is the raw expression evaluator to be implemented by the back-end
	 * XPath library.
	 * 
	 * @param context
	 *        The context against which to evaluate the query
	 * @return A list of XPath results.
	 */
	protected abstract List<?> evaluateRawAll(Object context);

	/**
	 * This is the raw expression evaluator to be implemented by the back-end
	 * XPath library. When this method is processed the implementing library is
	 * free to stop processing when the result that would be the first result is
	 * retrieved.
	 * <p>
	 * Only the first value in the result will be processed (if any).
	 * 
	 * @param context
	 *        The context against which to evaluate the query
	 * @return The first item in the XPath results, or null if there are no
	 *         results.
	 */
	protected abstract Object evaluateRawFirst(Object context);

}
