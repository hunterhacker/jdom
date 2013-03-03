package org.jdom2.test.cases.jaxb;

import static org.jdom2.test.util.UnitTestUtil.checkException;
import static org.jdom2.test.util.UnitTestUtil.failNoException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Iterator;

import org.junit.Test;

import org.jdom2.JDOMConstants;
import org.jdom2.Namespace;
import org.jdom2.util.JDOMNamespaceContext;

@SuppressWarnings("javadoc")
public class TestJDOMNamespaceContext {
	private final Namespace[] nsok = {Namespace.getNamespace("", "defns"), Namespace.XML_NAMESPACE,
			Namespace.getNamespace("a", "urla"), Namespace.getNamespace("b", "urlb"),
			Namespace.getNamespace("a2", "urla")}; 

	private final Namespace[] nsmay = {Namespace.getNamespace("a", "urla"), Namespace.getNamespace("b", "urlb"),
			Namespace.getNamespace("a2", "urla")};
	
	private final Namespace[] nsduppfx = {Namespace.getNamespace("a", "urla"), Namespace.getNamespace("b", "urlb"),
			Namespace.getNamespace("a", "urlx")};
	
	private final Namespace[] nsnullmember = {Namespace.getNamespace("a", "urla"), Namespace.getNamespace("b", "urlb"),
			null, Namespace.getNamespace("c", "urlc")};
	
	
	@Test
	public void testJDOMNamespaceContext() {
		final JDOMNamespaceContext nsc = new JDOMNamespaceContext(nsok);
		assertEquals("urla", nsc.getNamespaceURI("a"));
		try {
			new JDOMNamespaceContext(null);
			failNoException(IllegalArgumentException.class);
		} catch (Exception e) {
			checkException(IllegalArgumentException.class, e);
		}
		try {
			new JDOMNamespaceContext(nsduppfx);
			failNoException(IllegalArgumentException.class);
		} catch (Exception e) {
			checkException(IllegalArgumentException.class, e);
		}
		try {
			new JDOMNamespaceContext(nsnullmember);
			failNoException(IllegalArgumentException.class);
		} catch (Exception e) {
			checkException(IllegalArgumentException.class, e);
		}
	}

	@Test
	public void testGetNamespaceURIWithDef() {
		final JDOMNamespaceContext nsc = new JDOMNamespaceContext(nsok);
		assertEquals("defns", nsc.getNamespaceURI(""));
		assertEquals(Namespace.XML_NAMESPACE.getURI(), nsc.getNamespaceURI(Namespace.XML_NAMESPACE.getPrefix()));
		assertEquals(JDOMConstants.NS_URI_XMLNS, nsc.getNamespaceURI(JDOMConstants.NS_PREFIX_XMLNS));
		assertEquals("urla", nsc.getNamespaceURI("a"));
		assertEquals("urla", nsc.getNamespaceURI("a2"));
		assertEquals("urlb", nsc.getNamespaceURI("b"));
		assertEquals("", nsc.getNamespaceURI("dummy"));
		
		try {
			nsc.getNamespaceURI(null);
			failNoException(IllegalArgumentException.class);
		} catch (Exception e) {
			checkException(IllegalArgumentException.class, e);
		}
	}

	@Test
	public void testGetNamespaceURIWithoutDef() {
		final JDOMNamespaceContext nsc = new JDOMNamespaceContext(nsmay);
		assertEquals("", nsc.getNamespaceURI(""));
		assertEquals(Namespace.XML_NAMESPACE.getURI(), nsc.getNamespaceURI(Namespace.XML_NAMESPACE.getPrefix()));
		assertEquals(JDOMConstants.NS_URI_XMLNS, nsc.getNamespaceURI(JDOMConstants.NS_PREFIX_XMLNS));
		assertEquals("urla", nsc.getNamespaceURI("a"));
		assertEquals("urla", nsc.getNamespaceURI("a2"));
		assertEquals("urlb", nsc.getNamespaceURI("b"));
		assertEquals("", nsc.getNamespaceURI("dummy"));
		
		try {
			nsc.getNamespaceURI(null);
			failNoException(IllegalArgumentException.class);
		} catch (Exception e) {
			checkException(IllegalArgumentException.class, e);
		}
	}

	@Test
	public void testGetPrefixWithDef() {
		final JDOMNamespaceContext nsc = new JDOMNamespaceContext(nsok);
		assertEquals("", nsc.getPrefix("defns"));
		assertEquals(null, nsc.getPrefix(Namespace.NO_NAMESPACE.getURI()));
		assertEquals(Namespace.XML_NAMESPACE.getPrefix(), nsc.getPrefix(Namespace.XML_NAMESPACE.getURI()));
		assertEquals(JDOMConstants.NS_PREFIX_XMLNS, nsc.getPrefix(JDOMConstants.NS_URI_XMLNS));
		assertEquals("a", nsc.getPrefix("urla"));
		assertEquals("b", nsc.getPrefix("urlb"));
		try {
			nsc.getPrefix(null);
			failNoException(IllegalArgumentException.class);
		} catch (Exception e) {
			checkException(IllegalArgumentException.class, e);
		}
	}

	@Test
	public void testGetPrefixWithoutDef() {
		final JDOMNamespaceContext nsc = new JDOMNamespaceContext(nsmay);
		assertEquals(null, nsc.getPrefix("defns"));
		assertEquals(null, nsc.getPrefix(Namespace.NO_NAMESPACE.getURI()));
		assertEquals(Namespace.XML_NAMESPACE.getPrefix(), nsc.getPrefix(Namespace.XML_NAMESPACE.getURI()));
		assertEquals(JDOMConstants.NS_PREFIX_XMLNS, nsc.getPrefix(JDOMConstants.NS_URI_XMLNS));
		assertEquals("a", nsc.getPrefix("urla"));
		assertEquals("b", nsc.getPrefix("urlb"));
	}

	@Test
	public void testGetPrefixesWithDef() {
		final JDOMNamespaceContext nsc = new JDOMNamespaceContext(nsok);
		checkIteratorEquals(nsc.getPrefixes("defns"), "");
		checkIteratorEquals(nsc.getPrefixes(Namespace.NO_NAMESPACE.getURI()));
		checkIteratorEquals(nsc.getPrefixes(Namespace.XML_NAMESPACE.getURI()), Namespace.XML_NAMESPACE.getPrefix());
		checkIteratorEquals(nsc.getPrefixes(JDOMConstants.NS_URI_XMLNS), JDOMConstants.NS_PREFIX_XMLNS);
		checkIteratorEquals(nsc.getPrefixes("urla"), "a", "a2");
		checkIteratorEquals(nsc.getPrefixes("urlb"), "b");
		try {
			nsc.getPrefixes(null);
			failNoException(IllegalArgumentException.class);
		} catch (Exception e) {
			checkException(IllegalArgumentException.class, e);
		}
	}

	@Test
	public void testGetPrefixesWithoutDef() {
		final JDOMNamespaceContext nsc = new JDOMNamespaceContext(nsmay);
		checkIteratorEquals(nsc.getPrefixes("defns"));
		checkIteratorEquals(nsc.getPrefixes(Namespace.NO_NAMESPACE.getURI()));
		checkIteratorEquals(nsc.getPrefixes(Namespace.XML_NAMESPACE.getURI()), Namespace.XML_NAMESPACE.getPrefix());
		checkIteratorEquals(nsc.getPrefixes(JDOMConstants.NS_URI_XMLNS), JDOMConstants.NS_PREFIX_XMLNS);
		checkIteratorEquals(nsc.getPrefixes("urla"), "a", "a2");
		checkIteratorEquals(nsc.getPrefixes("urlb"), "b");
	}

	private void checkIteratorEquals(Iterator<?> prefixes, String...vals) {
		final int sz = vals.length;
		int c = 0;
		while (c < sz && prefixes.hasNext()) {
			final String v = (String)prefixes.next();
			try {
				prefixes.remove();
				failNoException(UnsupportedOperationException.class);
			} catch (Exception e) {
				checkException(UnsupportedOperationException.class, e);
			}
			assertEquals("Expect '" + vals[c] + "' at position " + c + " but got '" + v + "'", vals[c], v);
			c++;
		}
		if (prefixes.hasNext()) {
			fail ("Expected there to be no more prefixes, but there are more than " + sz + " with the next one being '" + prefixes.next() + "'.");
		}
		if (c < sz) {
			fail ("Expected there to be more prefixes (" + sz + ") , but there were only " + c + ".");
		}
		
	}

}
