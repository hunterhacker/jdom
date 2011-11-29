/*--

 Copyright (C) 2000-2007 Jason Hunter & Brett McLaughlin.
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

import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.Element;
import org.jdom2.IllegalNameException;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;

/**
 * A utility class for performing XPath calls on JDOM nodes, with a factory
 * interface for obtaining a first XPath instance. Users operate against this
 * class while XPath vendors can plug-in implementations underneath. Users can
 * choose an implementation using either {@link #setXPathClass} or the system
 * property "org.jdom2.xpath.class".
 * 
 * <strong>Note about Serialisation:</strong> JDOM2 has removed Serialisation
 * support from XPath. JDOM 1.x support relied on serialising just the String
 * XPath value, and ignored any Namespace and Variable settings. Serializing
 * the Variables is problematic because the variable value is not necessarily
 * serialisable, making it too complicated to attempt to serialise the entire
 * XPath.
 * 
 * @author Laurent Bihanic
 * @author Rolf Lear
 */
public abstract class XPath {

	/** A thread-local storing an instance of the default XPathFactory. */
	private static final ThreadLocal<XPathFactory> localfactory =
			new ThreadLocal<XPathFactory>();

	/**
	 * Unused field - JDOM XPath implementation has never been based on the core
	 * Java XPath system.
	 * <p>
	 * <b>Old Documentation</b>: The string passable to the JAXP 1.3
	 * XPathFactory isObjectModelSupported() method to query an XPath engine
	 * regarding its support for JDOM. Defined to be the well-known URI
	 * "http://jdom.org/jaxp/xpath/jdom".
	 * 
	 * @deprecated in lieu of the JDOM {@link XPathFactory}
	 */
	@Deprecated
	public final static String JDOM_OBJECT_MODEL_URI =
	"http://jdom.org/jaxp/xpath/jdom";

	/**
	 * Creates a new XPath wrapper object, compiling the specified XPath
	 * expression.
	 * <p>
	 * This method is a backward-compatible JDOM 1.x mechanism. It is the
	 * conceptual equivalent of (but it is optimised not to need to create a new
	 * factory each time):
	 * 
	 * <pre>
	 * return XPathFactory.newInstance().compile(path);
	 * </pre>
	 * 
	 * The impact of this is that XPath.newInstance() will use the default
	 * XPathFactory for creating XPath instances. See XPathFactory for how to
	 * set and override the default XPathFactory.
	 * 
	 * @see XPathFactory
	 * @param path
	 *        the XPath expression to wrap.
	 * @return an XPath instance representing the input path
	 * @throws JDOMException
	 *         if the XPath expression is invalid.
	 */
	public static final XPath newInstance(final String path) throws JDOMException {
		XPathFactory fac = localfactory.get();
		if (fac == null) {
			fac = XPathFactory.newInstance();
			localfactory.set(fac);
		}
		return fac.compile(path);
	}

	/**
	 * Sets the concrete XPath subclass to use when allocating XPath instances.
	 * 
	 * @param aClass
	 *        the concrete subclass of XPath.
	 * @throws IllegalArgumentException
	 *         if <code>aClass</code> is <code>null</code>.
	 * @throws JDOMException
	 *         if <code>aClass</code> is not a concrete subclass of XPath.
	 * @deprecated in lieu of {@link XPathFactory}. This does nothing now.
	 */
	@Deprecated
	public static void setXPathClass(final Class<? extends XPath> aClass) throws JDOMException {
		// do nothing.
	}

	/**
	 * Evaluates an XPath expression and returns the list of selected items.
	 * <p>
	 * <strong>Note</strong>: This method should not be used when the same XPath
	 * expression needs to be applied several times (on the same or different
	 * contexts) as it requires the expression to be compiled before being
	 * evaluated. In such cases, {@link XPathFactory#compile(String) compiling}
	 * an XPath instance and {@link #selectNodes(java.lang.Object) evaluating}
	 * it several times is far more efficient. Additionally, this method does
	 * not allow the XPath to evaluate expressions with custom Namespaces or
	 * Variables.
	 * </p>
	 * 
	 * @param context
	 *        the node to use as context for evaluating the XPath expression.
	 * @param path
	 *        the XPath expression to evaluate.
	 * @return the list of selected items, which may be of types: {@link Element}
	 *         , {@link Attribute}, {@link Text}, {@link CDATA}, {@link Comment}
	 *         , {@link ProcessingInstruction}, Boolean, Double, or String.
	 * @throws JDOMException
	 *         if the XPath expression is invalid or its evaluation on the
	 *         specified context failed.
	 */
	public static List<?> selectNodes(final Object context, final String path)
			throws JDOMException {
		return newInstance(path).selectNodes(context);
	}

	/**
	 * Evaluates the wrapped XPath expression and returns the first entry in the
	 * list of selected nodes (or atomics).
	 * <p>
	 * <strong>Note</strong>: This method should not be used when the same XPath
	 * expression needs to be applied several times (on the same or different
	 * contexts) as it requires the expression to be compiled before being
	 * evaluated. In such cases, {@link XPathFactory#compile(String) compiling}
	 * an XPath wrapper instance and {@link #selectSingleNode(java.lang.Object)
	 * evaluating} it several times is way more efficient. Additionally, this
	 * method does not allow the XPath to evaluate expressions with custom
	 * Namespaces or Variables.
	 * </p>
	 * 
	 * @param context
	 *        the element to use as context for evaluating the XPath expression.
	 * @param path
	 *        the XPath expression to evaluate.
	 * @return the first selected item, which may be of types: {@link Element},
	 *         {@link Attribute}, {@link Text}, {@link CDATA}, {@link Comment},
	 *         {@link ProcessingInstruction}, Boolean, Double, String, or
	 *         <code>null</code> if no item was selected.
	 * @throws JDOMException
	 *         if the XPath expression is invalid or its evaluation on the
	 *         specified context failed.
	 */
	public static Object selectSingleNode(final Object context, final String path)
			throws JDOMException {
		return newInstance(path).selectSingleNode(context);
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
	abstract public List<?> selectNodes(Object context) throws JDOMException;

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
	abstract public Object selectSingleNode(Object context) throws JDOMException;

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
	abstract public String valueOf(Object context) throws JDOMException;

	/**
	 * Returns the number value of the first node selected by applying the
	 * wrapped XPath expression to the given context.
	 * 
	 * @param context
	 *        the element to use as context for evaluating the XPath expression.
	 * @return the number value of the first node selected by applying the
	 *         wrapped XPath expression to the given context, <code>null</code>
	 *         if no node was selected or the special value
	 *         {@link java.lang.Double#NaN} (Not-a-Number) if the selected value
	 *         can not be converted into a number value.
	 * @throws JDOMException
	 *         if the XPath expression is invalid or its evaluation on the
	 *         specified context failed.
	 */
	abstract public Number numberValueOf(Object context) throws JDOMException;

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
	abstract public void setVariable(String name, Object value);

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
	abstract public void addNamespace(Namespace namespace);

	/**
	 * Adds a namespace definition (prefix and URI) to the list of namespaces
	 * known of this XPath expression.
	 * <p>
	 * <strong>Note</strong>: In XPath, there is no such thing as a 'default
	 * namespace'. The empty prefix <b>always</b> resolves to the empty
	 * namespace URI.
	 * </p>
	 * 
	 * @param prefix
	 *        the namespace prefix.
	 * @param uri
	 *        the namespace URI.
	 * @throws IllegalNameException
	 *         if the prefix or uri are null or empty strings or if they contain
	 *         illegal characters.
	 */
	public void addNamespace(final String prefix, final String uri) {
		addNamespace(Namespace.getNamespace(prefix, uri));
	}

	/**
	 * Returns the wrapped XPath expression as a string.
	 * 
	 * @return the wrapped XPath expression as a string.
	 */
	abstract public String getXPath();

}
