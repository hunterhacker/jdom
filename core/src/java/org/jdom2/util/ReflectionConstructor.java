package org.jdom2.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.jdom2.JDOMException;

/**
 * Utility class that handles constructing a class using reflection, and a
 * no-argument 'default' constructor.
 * 
 * @author Rolf Lear
 *
 */
public class ReflectionConstructor {
	
	/**
	 * Construct a new instance of the named class, and ensure it is cast
	 * to the type specified as the targetclass.
	 * @param <E> The generic type of the returned value.
	 * @param classname The class name of the instance to create.
	 * @param targetclass The return type of the created instance
	 * @return an instantiated class
	 * @throws JDOMException if there is a problem creating the class instance.
	 */
	public static final <E> E construct(String classname, Class<E> targetclass)
			throws JDOMException {
		try {
			Class<?> sclass = Class.forName(classname);
			if (!targetclass.isAssignableFrom(sclass)) {
				throw new JDOMException("Class '" + classname + "' is not assignable to '" + targetclass.getName() + "'.");
			}
			Constructor<?> constructor = sclass.getConstructor();
			Object o = constructor.newInstance();
			return targetclass.cast(o);
		} catch (ClassNotFoundException e) {
			throw new JDOMException("Unable to locate class '" + classname + "'.", e);
		} catch (NoSuchMethodException e) {
			throw new JDOMException("Unable to locate class no-arg constructor '" + classname + "'.", e);
		} catch (SecurityException e) {
			throw new JDOMException("Unable to access class constructor '" + classname + "'.", e);
		} catch (IllegalAccessException e) {
			throw new JDOMException("Unable to access class constructor '" + classname + "'.", e);
		} catch (InstantiationException e) {
			throw new JDOMException("Unable to instantiate class '" + classname + "'.", e);
		} catch (InvocationTargetException e) {
			throw new JDOMException("Unable to call class constructor '" + classname + "'.", e);
		}
	}
}
