/*--

 Copyright (C) 2000-2012 Jason Hunter & Brett McLaughlin.
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

package org.jdom2.xpath.jaxen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jaxen.BaseXPath;
import org.jaxen.JaxenException;
import org.jaxen.SimpleVariableContext;
import org.jaxen.XPath;

import org.jdom2.Attribute;
import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;

/**
 * A concrete XPath implementation for Jaxen. This class must be public because
 * the main JDOM XPath class needs to access the class, and the constructor.
 * 
 *        The generic type of the returned values from this XPath instance.
 * @author Laurent Bihanic
 * @deprecated replaced by compiled version.
 */
@Deprecated
public class JDOMXPath extends org.jdom2.xpath.XPath {

	/**
	 * Default mechanism.
	 * The serialization for this class is broken. It is only included here for
	 * compatibility with JDOM 1.x
	 */
	private static final long serialVersionUID = 200L;
	
	
	/**
	 * The compiled XPath object to select nodes. This attribute cannot be made
	 * final as it needs to be set upon object deserialization.
	 */
	private transient XPath xPath;

	/**
	 * The current context for XPath expression evaluation. The navigator is
	 * responsible for exposing JDOM content to Jaxen, including the wrapping of
	 * Namespace instances in NamespaceContainer
	 * <p>
	 * Because of the need to wrap Namespace, we also need to unwrap namespace.
	 * Further, we can't re-use the details from one 'selectNodes' to another
	 * because the Document tree may have been modified between, and also, we do
	 * not want to be holding on to memory.
	 * <p>
	 * Finally, we want to pre-load the NamespaceContext with the namespaces
	 * that are in scope for the contextNode being searched.
	 * <p>
	 * So, we need to reset the Navigator before and after each use. try{}
	 * finally {} to the rescue.
	 */
	private final JDOMNavigator navigator = new JDOMNavigator();

	/**
	 * Same story, need to be able to strip NamespaceContainer instances from
	 * Namespace content.
	 * 
	 * @param o
	 *        A result object which could potentially be a NamespaceContainer
	 * @return The input parameter unless it is a NamespaceContainer in which
	 *         case return the wrapped Namespace
	 */
	private static final Object unWrapNS(Object o) {
		if (o instanceof NamespaceContainer) {
			return ((NamespaceContainer) o).getNamespace();
		}
		return o;
	}

	/**
	 * Same story, need to be able to replace NamespaceContainer instances with
	 * Namespace content.
	 * 
	 * @param results
	 *        A list potentially containing NamespaceContainer instances
	 * @return The parameter list with NamespaceContainer instances replaced by
	 *         the wrapped Namespace instances.
	 */
	private static final List<Object> unWrap(List<?> results) {
		final ArrayList<Object> ret = new ArrayList<Object>(results.size());
		for (Iterator<?> it = results.iterator(); it.hasNext();) {
			ret.add(unWrapNS(it.next()));
		}
		return ret;
	}

	/**
	 * Creates a new XPath wrapper object, compiling the specified XPath
	 * expression.
	 * 
	 * @param expr
	 *        the XPath expression to wrap.
	 * @throws JDOMException
	 *         if the XPath expression is invalid.
	 */
	public JDOMXPath(String expr)
			throws JDOMException {
		setXPath(expr);
	}

	/**
	 * Evaluates the wrapped XPath expression and returns the list of selected
	 * items.
	 * 
	 * @param context
	 *        the node to use as context for evaluating the XPath expression.
	 * @return the list of selected items, which may be of types: {@link Element}
	 *         , {@link Attribute}, {@link Text}, {@link CDATA}, {@link Comment}
	 *         , {@link ProcessingInstruction}, Boolean, Double, or String.
	 * @throws JDOMException
	 *         if the evaluation of the XPath expression on the specified
	 *         context failed.
	 */
	@Override
	public List<?> selectNodes(Object context)
			throws JDOMException {
		try {
			navigator.setContext(context);

			return unWrap(xPath.selectNodes(context));
		} catch (JaxenException ex1) {
			throw new JDOMException(
					"XPath error while evaluating \"" + xPath.toString()
							+ "\": " + ex1.getMessage(), ex1);
		} finally {
			navigator.reset();
		}
	}

	/**
	 * Evaluates the wrapped XPath expression and returns the first entry in the
	 * list of selected nodes (or atomics).
	 * 
	 * @param context
	 *        the node to use as context for evaluating the XPath expression.
	 * @return the first selected item, which may be of types: {@link Element},
	 *         {@link Attribute}, {@link Text}, {@link CDATA}, {@link Comment},
	 *         {@link ProcessingInstruction}, Boolean, Double, String, or
	 *         <code>null</code> if no item was selected.
	 * @throws JDOMException
	 *         if the evaluation of the XPath expression on the specified
	 *         context failed.
	 */
	@Override
	public Object selectSingleNode(Object context)
			throws JDOMException {
		try {
			navigator.setContext(context);

			return unWrapNS(xPath.selectSingleNode(context));
		} catch (JaxenException ex1) {
			throw new JDOMException(
					"XPath error while evaluating \"" + xPath.toString()
							+ "\": " + ex1.getMessage(), ex1);
		} finally {
			navigator.reset();
		}
	}

	/**
	 * Returns the string value of the first node selected by applying the
	 * wrapped XPath expression to the given context.
	 * 
	 * @param context
	 *        the element to use as context for evaluating the XPath expression.
	 * @return the string value of the first node selected by applying the
	 *         wrapped XPath expression to the given context.
	 * @throws JDOMException
	 *         if the XPath expression is invalid or its evaluation on the
	 *         specified context failed.
	 */
	@Override
	public String valueOf(Object context) throws JDOMException {
		try {
			navigator.setContext(context);

			return xPath.stringValueOf(context);
		} catch (JaxenException ex1) {
			throw new JDOMException(
					"XPath error while evaluating \"" + xPath.toString()
							+ "\": " + ex1.getMessage(), ex1);
		} finally {
			navigator.reset();
		}
	}

	/**
	 * Returns the number value of the first item selected by applying the
	 * wrapped XPath expression to the given context.
	 * 
	 * @param context
	 *        the element to use as context for evaluating the XPath expression.
	 * @return the number value of the first item selected by applying the
	 *         wrapped XPath expression to the given context, <code>null</code>
	 *         if no node was selected or the special value
	 *         {@link java.lang.Double#NaN} (Not-a-Number) if the selected value
	 *         can not be converted into a number value.
	 * @throws JDOMException
	 *         if the XPath expression is invalid or its evaluation on the
	 *         specified context failed.
	 */
	@Override
	public Number numberValueOf(Object context) throws JDOMException {
		try {
			navigator.setContext(context);

			return xPath.numberValueOf(context);
		} catch (JaxenException ex1) {
			throw new JDOMException(
					"XPath error while evaluating \"" + xPath.toString()
							+ "\": " + ex1.getMessage(), ex1);
		} finally {
			navigator.reset();
		}
	}

	/**
	 * Defines an XPath variable and sets its value.
	 * 
	 * @param name
	 *        the variable name.
	 * @param value
	 *        the variable value.
	 * @throws IllegalArgumentException
	 *         if <code>name</code> is not a valid XPath variable name or if the
	 *         value type is not supported by the underlying implementation
	 */
	@Override
	public void setVariable(String name, Object value)
			throws IllegalArgumentException {
		Object o = xPath.getVariableContext();
		if (o instanceof SimpleVariableContext) {
			((SimpleVariableContext) o).setVariableValue(null, name, value);
		}
	}

	/**
	 * Adds a namespace definition to the list of namespaces known of this XPath
	 * expression.
	 * <p>
	 * <strong>Note</strong>: In XPath, there is no such thing as a 'default
	 * namespace'. The empty prefix <b>always</b> resolves to the empty
	 * namespace URI.
	 * </p>
	 * 
	 * @param namespace
	 *        the namespace.
	 */
	@Override
	public void addNamespace(Namespace namespace) {
		navigator.includeNamespace(namespace);
	}

	/**
	 * Returns the wrapped XPath expression as a string.
	 * 
	 * @return the wrapped XPath expression as a string.
	 */
	@Override
	public String getXPath() {
		return (xPath.toString());
	}

	/**
	 * Compiles and sets the XPath expression wrapped by this object.
	 * 
	 * @param expr
	 *        the XPath expression to wrap.
	 * @throws JDOMException
	 *         if the XPath expression is invalid.
	 */
	private void setXPath(String expr) throws JDOMException {
		try {
			xPath = new BaseXPath(expr, navigator);
			xPath.setNamespaceContext(navigator);
		} catch (Exception ex1) {
			throw new JDOMException("Invalid XPath expression: \""
					+ expr + "\"", ex1);
		}
	}

	@Override
	public String toString() {
		return (String.format("[XPath: %s]", xPath.toString()));
	}

}
