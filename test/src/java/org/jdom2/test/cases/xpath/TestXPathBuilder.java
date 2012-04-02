package org.jdom2.test.cases.xpath;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Test;

import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.test.util.UnitTestUtil;
import org.jdom2.xpath.XPathBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

@SuppressWarnings("javadoc")
public class TestXPathBuilder {

	@Test
	public void testXPathBuilder() {
		try {
			new XPathBuilder<Object>(null, Filters.fpassthrough()).toString();
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
		try {
			new XPathBuilder<Object>("/", null).toString();
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
		assertNotNull(new XPathBuilder<Object>("/", Filters.fpassthrough()).toString());
	}

	@Test
	public void testGetSetVariable() {
		XPathBuilder<Object> xpb = 
				new XPathBuilder<Object>("/", Filters.fpassthrough());
		try {
			xpb.getVariable(null);
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
		try {
			xpb.setVariable(null, "");
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
		try {
			xpb.setVariable(null, "");
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
		assertNull(xpb.getVariable("hello"));
		assertTrue(xpb.setVariable("name", ""));
		assertEquals("", xpb.getVariable("name"));
		assertFalse(xpb.setVariable("name", "xxx"));
		assertEquals("xxx", xpb.getVariable("name"));
	}

	@Test
	public void testSetNamespaceStringString() {
		XPathBuilder<Object> xpb = 
				new XPathBuilder<Object>("/", Filters.fpassthrough());
		try {
			xpb.setNamespace("", "hello");
			UnitTestUtil.failNoException(IllegalArgumentException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(IllegalArgumentException.class, e);
		}
		try {
			xpb.setNamespace("", null);
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
		try {
			xpb.setNamespace(null, "");
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
		
		assertFalse(xpb.setNamespace("", ""));
		
		assertEquals(Namespace.NO_NAMESPACE, xpb.getNamespace(""));
		
		assertNull(xpb.getNamespace("hello"));
		Namespace hello = Namespace.getNamespace("h", "hello");
		assertTrue(xpb.setNamespace("h", "hello"));
		assertTrue(hello == xpb.getNamespace("h"));
		
		try {
			xpb.getNamespace(null);
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
		
	}

	@Test
	public void testSetNamespaceNamespace() {
		XPathBuilder<Object> xpb = 
				new XPathBuilder<Object>("/", Filters.fpassthrough());
		try {
			Namespace nsx = Namespace.getNamespace("", "hello");
			xpb.setNamespace(nsx);
			UnitTestUtil.failNoException(IllegalArgumentException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(IllegalArgumentException.class, e);
		}
		try {
			xpb.setNamespace(null);
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
		
		assertFalse(xpb.setNamespace(Namespace.NO_NAMESPACE));
		
		assertEquals(Namespace.NO_NAMESPACE, xpb.getNamespace(""));
		
		assertNull(xpb.getNamespace("hello"));
		Namespace hello = Namespace.getNamespace("h", "hello");
		assertTrue(xpb.setNamespace(hello));
		assertTrue(hello == xpb.getNamespace("h"));
	}

	@Test
	public void testSetNamespaces() {
		XPathBuilder<Object> xpb = 
				new XPathBuilder<Object>("/", Filters.fpassthrough());
		try {
			Namespace nsx = Namespace.getNamespace("", "hello");
			xpb.setNamespaces(Collections.singleton(nsx));
			UnitTestUtil.failNoException(IllegalArgumentException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(IllegalArgumentException.class, e);
		}
		try {
			xpb.setNamespaces(null);
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
		
		assertFalse(xpb.setNamespaces(Collections.singletonList(Namespace.NO_NAMESPACE)));
		
		assertEquals(Namespace.NO_NAMESPACE, xpb.getNamespace(""));
		
		assertNull(xpb.getNamespace("hello"));
		Namespace hello = Namespace.getNamespace("h", "hello");
		assertTrue(xpb.setNamespaces(Collections.singletonList(hello)));
		assertTrue(hello == xpb.getNamespace("h"));
		Namespace hellp = Namespace.getNamespace("h", "hellp");
		assertFalse(xpb.setNamespaces(Collections.singletonList(hellp)));
	}

	@Test
	public void testGetFilter() {
		XPathBuilder<Object> xpb = 
				new XPathBuilder<Object>("/", Filters.fpassthrough());
		assertTrue(xpb.getFilter() == Filters.fpassthrough());
	}

	@Test
	public void testGetExpression() {
		XPathBuilder<Object> xpb = 
				new XPathBuilder<Object>("/", Filters.fpassthrough());
		assertEquals("/", xpb.getExpression());
	}

	@Test
	public void testCompileWith() {
		XPathBuilder<Object> xpb = 
				new XPathBuilder<Object>("/", Filters.fpassthrough());
		xpb.setNamespace("p", "uri");
		xpb.setVariable("p:var", "value");
		XPathExpression<Object> xpe = xpb.compileWith(XPathFactory.instance());
		assertEquals("/", xpe.getExpression());
		assertEquals(Filters.fpassthrough(), xpe.getFilter());
		assertEquals("", xpe.getNamespace("").getURI());
		assertEquals("uri", xpe.getNamespace("p").getURI());
		assertEquals("value", xpe.getVariable("var", Namespace.getNamespace("uri")));
	}

}
