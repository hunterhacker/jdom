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


import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import org.jdom2.*;
import org.jdom2.internal.SystemProperty;


/**
 * A utility class for performing XPath calls on JDOM nodes, with a factory
 * interface for obtaining a first XPath instance. Users operate against this
 * class while XPath vendors can plug-in implementations underneath.  Users
 * can choose an implementation using either {@link #setXPathClass} or
 * the system property "org.jdom2.xpath.class".
 *
 * @author  Laurent Bihanic
 * @deprecated Use XPathFactory/XPathExpression/XPathBuilder instead.
 */
@Deprecated
public abstract class XPath implements Serializable {

	/**
	 * Standard JDOM2 Serialization. Default mechanism.
	 */
	private static final long serialVersionUID = 200L;
	
	/**
	 * The name of the system property from which to retrieve the
	 * name of the implementation class to use.
	 * <p>
	 * The property name is:
	 * "<code>org.jdom2.xpath.class</code>".</p>
	 */
	private final static String  XPATH_CLASS_PROPERTY = "org.jdom2.xpath.class";

	/**
	 * The default implementation class to use if none was configured.
	 */
	private final static String  DEFAULT_XPATH_CLASS  =
			"org.jdom2.xpath.jaxen.JDOMXPath";

	/**
	 * The string passable to the JAXP 1.3 XPathFactory isObjectModelSupported()
	 * method to query an XPath engine regarding its support for JDOM.  Defined
	 * to be the well-known URI "http://jdom.org/jaxp/xpath/jdom".
	 */
	public final static String JDOM_OBJECT_MODEL_URI =
			"http://jdom.org/jaxp/xpath/jdom";

	/**
	 * The constructor to instantiate a new XPath concrete
	 * implementation.
	 *
	 * @see    #newInstance
	 */
	private static Constructor<? extends XPath> constructor = null;

	/**
	 * Creates a new XPath wrapper object, compiling the specified
	 * XPath expression.
	 *
	 * @param  path   the XPath expression to wrap.
	 * @return an XPath instance representing the input path
	 *
	 * @throws JDOMException   if the XPath expression is invalid.
	 */
	public static XPath newInstance(String path) throws JDOMException {
		try {
			if (constructor == null) {
				// First call => Determine implementation.
				String className;
				try {
					className = SystemProperty.get(XPATH_CLASS_PROPERTY,
							DEFAULT_XPATH_CLASS);
				}
				catch (SecurityException ex1) {
					// Access to system property denied. => Use default impl.
					className = DEFAULT_XPATH_CLASS;
				}
				@SuppressWarnings("unchecked")
				Class<? extends XPath> useclass = (Class<? extends XPath>) Class.forName(className);
				if (!XPath.class.isAssignableFrom(useclass)) {
					throw new JDOMException("Unable to create a JDOMXPath from class '" + className + "'.");
				}
				setXPathClass(useclass);
			}
			// Allocate and return new implementation instance.
			return constructor.newInstance(path);
		}
		catch (JDOMException ex1) {
			throw ex1;
		}
		catch (InvocationTargetException ex2) {
			// Constructor threw an error on invocation.
			Throwable t = ex2.getTargetException();

			throw (t instanceof JDOMException)? (JDOMException)t:
				new JDOMException(t.toString(), t);
		}
		catch (Exception ex3) {
			// Any reflection error (probably due to a configuration mistake).
			throw new JDOMException(ex3.toString(), ex3);
		}
	}

	/**
	 * Sets the concrete XPath subclass to use when allocating XPath
	 * instances.
	 *
	 * @param  aClass   the concrete subclass of XPath.
	 *
	 * @throws IllegalArgumentException   if <code>aClass</code> is
	 *                                    <code>null</code>.
	 * @throws JDOMException              if <code>aClass</code> is
	 *                                    not a concrete subclass
	 *                                    of XPath.
	 */
	public static void setXPathClass(Class<? extends XPath> aClass) throws JDOMException {
		if (aClass == null) {
			throw new IllegalArgumentException("aClass");
		}

		try {
			if ((XPath.class.isAssignableFrom(aClass)) &&
					(Modifier.isAbstract(aClass.getModifiers()) == false)) {
				// Concrete subclass of XPath => Get constructor
				constructor = aClass.getConstructor(String.class);
			}
			else {
				throw new JDOMException(aClass.getName() +
						" is not a concrete JDOM XPath implementation");
			}
		}
		catch (JDOMException ex1) {
			throw ex1;
		}
		catch (Exception ex2) {
			// Any reflection error (probably due to a configuration mistake).
			throw new JDOMException(ex2.toString(), ex2);
		}
	}

	/**
	 * Evaluates the wrapped XPath expression and returns the list
	 * of selected items.
	 *
	 * @param  context   the node to use as context for evaluating
	 *                   the XPath expression.
	 *
	 * @return the list of selected items, which may be of types: {@link Element},
	 *         {@link Attribute}, {@link Text}, {@link CDATA},
	 *         {@link Comment}, {@link ProcessingInstruction}, Boolean,
	 *         Double, or String.
	 *
	 * @throws JDOMException   if the evaluation of the XPath
	 *                         expression on the specified context
	 *                         failed.
	 */
	abstract public List<?> selectNodes(Object context) throws JDOMException;

	/**
	 * Evaluates the wrapped XPath expression and returns the first
	 * entry in the list of selected nodes (or atomics).
	 *
	 * @param  context   the node to use as context for evaluating
	 *                   the XPath expression.
	 *
	 * @return the first selected item, which may be of types: {@link Element},
	 *         {@link Attribute}, {@link Text}, {@link CDATA},
	 *         {@link Comment}, {@link ProcessingInstruction}, Boolean,
	 *         Double, String, or <code>null</code> if no item was selected.
	 *
	 * @throws JDOMException   if the evaluation of the XPath
	 *                         expression on the specified context
	 *                         failed.
	 */
	abstract public Object selectSingleNode(Object context) throws JDOMException;

	/**
	 * Returns the string value of the first node selected by applying
	 * the wrapped XPath expression to the given context.
	 *
	 * @param  context   the element to use as context for evaluating
	 *                   the XPath expression.
	 *
	 * @return the string value of the first node selected by applying
	 *         the wrapped XPath expression to the given context.
	 *
	 * @throws JDOMException   if the XPath expression is invalid or
	 *                         its evaluation on the specified context
	 *                         failed.
	 */
	abstract public String valueOf(Object context) throws JDOMException;

	/**
	 * Returns the number value of the first node selected by applying
	 * the wrapped XPath expression to the given context.
	 *
	 * @param  context   the element to use as context for evaluating
	 *                   the XPath expression.
	 *
	 * @return the number value of the first node selected by applying
	 *         the wrapped XPath expression to the given context,
	 *         <code>null</code> if no node was selected or the
	 *         special value {@link java.lang.Double#NaN}
	 *         (Not-a-Number) if the selected value can not be
	 *         converted into a number value.
	 *
	 * @throws JDOMException   if the XPath expression is invalid or
	 *                         its evaluation on the specified context
	 *                         failed.
	 */
	abstract public Number numberValueOf(Object context) throws JDOMException;

	/**
	 * Defines an XPath variable and sets its value.
	 *
	 * @param  name    the variable name.
	 * @param  value   the variable value.
	 *
	 * @throws IllegalArgumentException   if <code>name</code> is not
	 *                                    a valid XPath variable name
	 *                                    or if the value type is not
	 *                                    supported by the underlying
	 *                                    implementation
	 */
	abstract public void setVariable(String name, Object value);

	/**
	 * Adds a namespace definition to the list of namespaces known of
	 * this XPath expression.
	 * <p>
	 * <strong>Note</strong>: In XPath, there is no such thing as a
	 * 'default namespace'.  The empty prefix <b>always</b> resolves
	 * to the empty namespace URI.</p>
	 *
	 * @param  namespace   the namespace.
	 */
	abstract public void addNamespace(Namespace namespace);

	/**
	 * Adds a namespace definition (prefix and URI) to the list of
	 * namespaces known of this XPath expression.
	 * <p>
	 * <strong>Note</strong>: In XPath, there is no such thing as a
	 * 'default namespace'.  The empty prefix <b>always</b> resolves
	 * to the empty namespace URI.</p>
	 *
	 * @param  prefix   the namespace prefix.
	 * @param  uri      the namespace URI.
	 *
	 * @throws IllegalNameException   if the prefix or uri are null or
	 *                                empty strings or if they contain
	 *                                illegal characters.
	 */
	public void addNamespace(String prefix, String uri) {
		addNamespace(Namespace.getNamespace(prefix, uri));
	}

	/**
	 * Returns the wrapped XPath expression as a string.
	 *
	 * @return the wrapped XPath expression as a string.
	 */
	abstract public String getXPath();


	/**
	 * Evaluates an XPath expression and returns the list of selected
	 * items.
	 * <p>
	 * <strong>Note</strong>: This method should not be used when the
	 * same XPath expression needs to be applied several times (on the
	 * same or different contexts) as it requires the expression to be
	 * compiled before being evaluated.  In such cases,
	 * {@link #newInstance allocating} an XPath wrapper instance and
	 * {@link #selectNodes(java.lang.Object) evaluating} it several
	 * times is way more efficient.
	 * </p>
	 *
	 * @param  context   the node to use as context for evaluating
	 *                   the XPath expression.
	 * @param  path      the XPath expression to evaluate.
	 *
	 * @return the list of selected items, which may be of types: {@link Element},
	 *         {@link Attribute}, {@link Text}, {@link CDATA},
	 *         {@link Comment}, {@link ProcessingInstruction}, Boolean,
	 *         Double, or String.
	 *
	 * @throws JDOMException   if the XPath expression is invalid or
	 *                         its evaluation on the specified context
	 *                         failed.
	 */
	public static List<?> selectNodes(Object context, String path)
			throws JDOMException {
		return newInstance(path).selectNodes(context);
	}

	/**
	 * Evaluates the wrapped XPath expression and returns the first
	 * entry in the list of selected nodes (or atomics).
	 * <p>
	 * <strong>Note</strong>: This method should not be used when the
	 * same XPath expression needs to be applied several times (on the
	 * same or different contexts) as it requires the expression to be
	 * compiled before being evaluated.  In such cases,
	 * {@link #newInstance allocating} an XPath wrapper instance and
	 * {@link #selectSingleNode(java.lang.Object) evaluating} it
	 * several times is way more efficient.
	 * </p>
	 *
	 * @param  context   the element to use as context for evaluating
	 *                   the XPath expression.
	 * @param  path      the XPath expression to evaluate.
	 *
	 * @return the first selected item, which may be of types: {@link Element},
	 *         {@link Attribute}, {@link Text}, {@link CDATA},
	 *         {@link Comment}, {@link ProcessingInstruction}, Boolean,
	 *         Double, String, or <code>null</code> if no item was selected.
	 *
	 * @throws JDOMException   if the XPath expression is invalid or
	 *                         its evaluation on the specified context
	 *                         failed.
	 */
	public static Object selectSingleNode(Object context, String path)
			throws JDOMException {
		return newInstance(path).selectSingleNode(context);
	}


	//-------------------------------------------------------------------------
	// Serialization support
	//-------------------------------------------------------------------------

	/**
	 * <i>[Serialization support]</i> Returns the alternative object
	 * to write to the stream when serializing this object.  This
	 * method returns an instance of a dedicated nested class to
	 * serialize XPath expressions independently of the concrete
	 * implementation being used.
	 * <p>
	 * <strong>Note</strong>: Subclasses are not allowed to override
	 * this method to ensure valid serialization of all
	 * implementations.</p>
	 *
	 * @return an XPathString instance configured with the wrapped
	 *         XPath expression.
	 *
	 * @throws ObjectStreamException   never.
	 */
	protected final Object writeReplace() throws ObjectStreamException {
		return new XPathString(this.getXPath());
	}

	/**
	 * The XPathString is dedicated to serialize instances of
	 * XPath subclasses in a implementation-independent manner.
	 * <p>
	 * XPathString ensures that only string data are serialized.  Upon
	 * deserialization, XPathString relies on XPath factory method to
	 * to create instances of the concrete XPath wrapper currently
	 * configured.</p>
	 */
	private final static class XPathString implements Serializable {
		/**
		 * Standard JDOM2 Serialization. Default mechanism.
		 */
		private static final long serialVersionUID = 200L;
		
		/**
		 * The XPath expression as a string.
		 */
		private String xPath = null;

		/**
		 * Creates a new XPathString instance from the specified
		 * XPath expression.
		 *
		 * @param  xpath   the XPath expression.
		 */
		public XPathString(String xpath) {
			super();

			this.xPath = xpath;
		}

		/**
		 * <i>[Serialization support]</i> Resolves the read XPathString
		 * objects into XPath implementations.
		 *
		 * @return an instance of a concrete implementation of
		 *         XPath.
		 *
		 * @throws ObjectStreamException   if no XPath could be built
		 *                                 from the read object.
		 */
		private Object readResolve() throws ObjectStreamException {
			try {
				return XPath.newInstance(this.xPath);
			}
			catch (JDOMException ex1) {
				throw new InvalidObjectException(
						"Can't create XPath object for expression \"" +
								this.xPath + "\": " + ex1.toString());
			}
		}
	}
}

