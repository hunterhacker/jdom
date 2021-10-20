/*--

 Copyright (C) 2012 Jason Hunter & Brett McLaughlin.
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:

 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions, and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions, and the disclaimer that follows
    these conditions in the documentation and/or other materials
    provided with the distribution.

 3. The name "JDOM" must not be used to endorse or promote products
    derived from this software without prior written permission.  For
    written permission, please contact <request_AT_jdom_DOT_org>.

 4. Products derived from this software may not be called "JDOM", nor
    may "JDOM" appear in their name, without prior written permission
    from the JDOM Project Management <request_AT_jdom_DOT_org>.

 In addition, we request (but do not require) that you include in the
 end-user documentation provided with the redistribution and/or in the
 software itself an acknowledgement equivalent to the following:
     "This product includes software developed by the
      JDOM Project (http://www.jdom.org/)."
 Alternatively, the acknowledgment may be graphical using the logos
 available at http://www.jdom.org/images/logos.

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED.  IN NO EVENT SHALL THE JDOM AUTHORS OR THE PROJECT
 CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.

 This software consists of voluntary contributions made by many
 individuals on behalf of the JDOM Project and was originally
 created by Jason Hunter <jhunter_AT_jdom_DOT_org> and
 Brett McLaughlin <brett_AT_jdom_DOT_org>.  For more information
 on the JDOM Project, please see <http://www.jdom.org/>.

 */

package org.jdom2.xpath;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jdom2.Namespace;
import org.jdom2.filter.Filter;

/**
 * A helper class for creating {@link XPathExpression} instances without having
 * to manage your own Namespace and Variable contexts.
 * 
 * @author Rolf Lear
 * @param <T>
 *        The generic type of the returned results.
 */
public class XPathBuilder<T> {
	private final Filter<T> filter;
	private final String expression;
	private Map<String, Object> variables;
	private Map<String, Namespace> namespaces;

	/**
	 * Create a skeleton XPathBuilder with the given expression and result
	 * filter.
	 * 
	 * @param expression
	 *        The XPath expression.
	 * @param filter
	 *        The Filter to perform the coercion with.
	 */
	public XPathBuilder(String expression, Filter<T> filter) {
		if (expression == null) {
			throw new NullPointerException("Null expression");
		}
		if (filter == null) {
			throw new NullPointerException("Null filter");
		}
		this.filter = filter;
		this.expression = expression;
	}

	/**
	 * Define or redefine an XPath expression variable value. In XPath, variable
	 * names can be in a namespace, and thus the variable name is in a QName
	 * form: prefix:name
	 * <p>
	 * Variables without a prefix are in the "" Namespace.
	 * <p>
	 * Variables can have a null value. The {@link XPathExpression} can change
	 * the variable value before the expression is evaluated, and, some XPath
	 * libraries support a null variable value. See
	 * {@link XPathExpression#setVariable(String, Namespace, Object)}.
	 * <p>
	 * In order to validate that a Variable is unique you have to know the
	 * namespace associated with the prefix. This class is designed to make it
	 * possible to make the namespace associations after the variables have been
	 * added. As a result it is not possible to validate the uniqueness of a
	 * variable name until the {@link #compileWith(XPathFactory)} method is
	 * called.
	 * <p>
	 * As a consequence of the above, this class assumes that each unique prefix
	 * is for a unique Namespace URI (thus calling this method with different
	 * QNames is assumed to be setting different variables). This may lead to an
	 * IllegalArgumentException when the {@link #compileWith(XPathFactory)}
	 * method is called.
	 * <p>
	 * This method does not validate the format of the variable name either, it
	 * instead postpones the validation until the expression is compiled. As a
	 * result you may encounter IllegalArgumentExceptions at compile time if the
	 * variable names are not valid XPath QNames ("name" or "prefix:name").
	 * 
	 * @param qname
	 *        The variable name to define.
	 * @param value
	 *        The variable value to set.
	 * @return true if this variable was defined, false if it was previously
	 *         defined and has now been redefined.
	 * @throws NullPointerException
	 *         if the name is null.
	 */
	public boolean setVariable(String qname, Object value) {
		if (qname == null) {
			throw new NullPointerException("Null variable name");
		}
		if (variables == null) {
			variables = new HashMap<String, Object>();
		}
		return variables.put(qname, value) == null;
	}

	/**
	 * Define a Namespace to be available for the XPath expression. If a
	 * Namespace with the same prefix was previously defined then the prefix
	 * will be re-defined.
	 * 
	 * @param prefix
	 *        The namespace prefix to define.
	 * @param uri
	 *        The namespace URI to define.
	 * @return true if the Namespace prefix was newly defined, false if the
	 *         prefix was previously defined and has now been redefined.
	 */
	public boolean setNamespace(String prefix, String uri) {
		if (prefix == null) {
			throw new NullPointerException("Null prefix");
		}
		if (uri == null) {
			throw new NullPointerException("Null URI");
		}
		return setNamespace(Namespace.getNamespace(prefix, uri));
	}

	/**
	 * Define a Namespace to be available for the XPath expression.
	 * 
	 * @param namespace
	 *        The namespace to define.
	 * @return true if this Namespace prefix was newly defined, false if the
	 *         prefix was previously defined and has now been redefined.
	 * @throws NullPointerException
	 *         if the namespace is null.
	 */
	public boolean setNamespace(Namespace namespace) {
		if (namespace == null) {
			throw new NullPointerException("Null Namespace");
		}
		if ("".equals(namespace.getPrefix())) {
			if (Namespace.NO_NAMESPACE != namespace) {
				throw new IllegalArgumentException(
						"Cannot set a Namespace URI in XPath for the \"\" prefix.");
			}
			// NO_NAMESPACE is always defined...
			return false;
		}

		if (namespaces == null) {
			namespaces = new HashMap<String, Namespace>();
		}
		return namespaces.put(namespace.getPrefix(), namespace) == null;
	}

	/**
	 * Add a number of namespaces to this XPathBuilder
	 * 
	 * @param namespaces
	 *        The namespaces to set.
	 * @return true if any of the Namespace prefixes are new to the XPathBuilder
	 * @throws NullPointerException
	 *         if the namespace collection, or any of its members are null.
	 */
	public boolean setNamespaces(Collection<Namespace> namespaces) {
		if (namespaces == null) {
			throw new NullPointerException("Null namespaces Collection");
		}
		boolean ret = false;
		for (Namespace ns : namespaces) {
			if (setNamespace(ns)) {
				ret = true;
			}
		}
		return ret;
	}

	/**
	 * Get the variable value associated with the given name. See
	 * {@link #setVariable(String, Object)} for notes on how the Namespaces need
	 * to be established before an authoritative reference to a variable can be
	 * made. As a result, this method uses the simple variable QName name to
	 * reference the variable.
	 * 
	 * @param qname
	 *        The variable name to get the variable value for.
	 * @return the variable value, or null if the variable was not defined.
	 * @throws NullPointerException
	 *         if the qname is null.
	 */
	public Object getVariable(String qname) {
		if (qname == null) {
			throw new NullPointerException("Null qname");
		}
		if (variables == null) {
			return null;
		}
		return variables.get(qname);
	}

	/**
	 * Get the Namespace associated with the given prefix.
	 * 
	 * @param prefix
	 *        The Namespace prefix to get the Namespace for.
	 * @return the Namespace with that prefix, or null if that prefix was never
	 *         defined.
	 */
	public Namespace getNamespace(String prefix) {
		if (prefix == null) {
			throw new NullPointerException("Null prefix");
		}
		if ("".equals(prefix)) {
			return Namespace.NO_NAMESPACE;
		}
		if (namespaces == null) {
			return null;
		}
		return namespaces.get(prefix);
	}

	/**
	 * Get the Filter instance used for coercion.
	 * 
	 * @return the coercion Filter.
	 */
	public Filter<T> getFilter() {
		return filter;
	}

	/**
	 * Get the XPath expression.
	 * 
	 * @return the XPath expression.
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * Compile an XPathExpression using the details currently stored in the
	 * XPathBuilder.
	 * 
	 * @param factory
	 *        The XPath factory to use for compiling.
	 * @return The compiled XPath expression
	 * @throws IllegalArgumentException
	 *         if the expression cannot be compiled.
	 */
	public XPathExpression<T> compileWith(XPathFactory factory) {
		if (namespaces == null) {
			return factory.compile(expression, filter, variables);
		}
		return factory.compile(expression, filter, variables, namespaces
				.values().toArray(new Namespace[namespaces.size()]));
	}

}
