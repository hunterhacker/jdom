package org.jdom2.xpath;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.jdom2.JDOMConstants;
import org.jdom2.JDOMException;
import org.jdom2.xpath.jaxen.JaxenXPathFactory;

/**
 * XPathFactory allows JDOM users to configure which XPath implementation to
 * use when evaluating XPath expressions.
 * <p>
 * JDOM does not extend the core Java XPath API (javax.xml.xpath.XPath). Instead
 * it creates a new API that is more JDOM and Java friendly leading to neater
 * and more understandable code (in a JDOM context).
 * <p>
 * A JDOM XPathFactory instance is able to create JDOM XPath instances that can
 * be used to evaluate XPath expressions against JDOM Content.
 * <p>
 * The XPathFactory allows either default or custom XPathFactory instances to be
 * created. If you use the {@link #newInstance(String)} method
 * then an XPathFactory of that specific type will be created. If you use the
 * {@link #newInstance()} method then a default XPathFactory instance will be
 * created.
 * <p>
 * XPathFactory instances are not specified to be thread-safe. You must ensure
 * that each XPathFactory instances are appropriately protected from concurrent
 * access. If you are using the default mechanisms for accessing XPath instances
 * then you can use JDOM 1.x compatible {@link XPath#newInstance(String)} method
 * to create XPath instances as that method is able to operate concurrently and
 * efficiently. 
 * 
 * @author Rolf Lear
 *
 */
public abstract class XPathFactory {
	
	private static final String DEFAULTFACTORY =
			System.getProperty(JDOMConstants.JDOM2_PROPERTY_XPATH_FACTORY, null);
	
	/**
	 * Create an instance of an XPathFactory using the default mechanisms
	 * to determine what XPathFactory implementation to use.
	 * <p>
	 * The default mechanism will inspect the system property
	 * org.jdom2.xpath.XPathFactory to determine what class should be used for
	 * the XPathFactory. If that property is not set then JDOM will use the
	 * {@link JaxenXPathFactory}.
	 * @return a new instance of the default XPathFactory 
	 * @throws JDOMException if there is a problem creating a new instance.
	 */
	public static final XPathFactory newInstance() throws JDOMException {
		return DEFAULTFACTORY == null
				? new JaxenXPathFactory()
				: newInstance(DEFAULTFACTORY);
	}
	
	/**
	 * Create a specific instance of an XPathFactory.
	 * @param factoryclass The name of the XPathFactory class to create.
	 * @return An XPathFactory of the specifiec class.
	 * @throws JDOMException if there was a problem loading that XPath factory.
	 */
	// , ClassLoader loader
	// * @param loader The classloader to use to load the Factory class.
	public static final XPathFactory newInstance(String factoryclass) throws JDOMException {
		try {
			Class<?> fclass = Class.forName(factoryclass);
			if (!XPathFactory.class.isAssignableFrom(fclass)) {
				throw new JDOMException("Unable to cast an XPathFactory from " + factoryclass);
			}
			Constructor<?> constructor = fclass.getConstructor();
			Object o = constructor.newInstance();
			return XPathFactory.class.cast(o);
		} catch (ClassNotFoundException e) {
			throw new JDOMException("Unable to locate XPathFactory " + factoryclass, e);
		} catch (SecurityException e) {
			throw new JDOMException("Unable to access the XPathFactory " + factoryclass, e);
		} catch (NoSuchMethodException e) {
			throw new JDOMException("Unable to locate the Default constructor on XPathFactory " + factoryclass, e);
		} catch (InstantiationException e) {
			throw new JDOMException("Unable to instantiate XPathFactory " + factoryclass, e);
		} catch (IllegalAccessException e) {
			throw new JDOMException("Unable to access the constructor for XPathFactory " + factoryclass, e);
		} catch (InvocationTargetException e) {
			throw new JDOMException("Unable to construct the XPathFactory " + factoryclass, e);
		}
	}
	
	/**
	 * Create a concrete XPath instance from this factory.
	 * You can set Namespace and Variable lookups on the returned instance. 
	 * @param xpath The expression to use for the XPath.
	 * @return an XPath instance.
	 * @throws JDOMException if there is a problem compiling the expression
	 */
	public abstract XPath compile(String xpath) throws JDOMException;
	
}
