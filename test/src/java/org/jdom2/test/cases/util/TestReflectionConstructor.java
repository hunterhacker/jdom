package org.jdom2.test.cases.util;

import static org.junit.Assert.*;
import static org.jdom2.test.util.UnitTestUtil.*;

import java.util.List;

import org.junit.Test;

import org.jdom2.JDOMRuntimeException;
import org.jdom2.Namespace;
import org.jdom2.util.ReflectionConstructor;

@SuppressWarnings("javadoc")
public class TestReflectionConstructor {

	@Test
	public void testConstruct() {
		List<?> al = ReflectionConstructor.construct("java.util.ArrayList", List.class);
		assertTrue(al.isEmpty());
	}

	@Test
	public void testConstructNoSuchClass() {
		try {
			ReflectionConstructor.construct("java.util.Junk", List.class);
			failNoException(JDOMRuntimeException.class);
		} catch (Exception e) {
			checkException(JDOMRuntimeException.class, e);
		}
	}

	@Test
	public void testConstructDefaultConstructor() {
		try {
			ReflectionConstructor.construct("java.util.Integer", Integer.class);
			failNoException(JDOMRuntimeException.class);
		} catch (Exception e) {
			checkException(JDOMRuntimeException.class, e);
		}
	}

	@Test
	public void testConstructNoaccessConstructor() {
		try {
			ReflectionConstructor.construct("org.jdom2.Namespace", Namespace.class);
			failNoException(JDOMRuntimeException.class);
		} catch (Exception e) {
			checkException(JDOMRuntimeException.class, e);
		}
	}
}
