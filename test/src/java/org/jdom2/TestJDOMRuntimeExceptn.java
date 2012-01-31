package org.jdom2;

import static org.junit.Assert.*;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class TestJDOMRuntimeExceptn {

	@Test
	public void testJDOMRuntimeException() {
		assertTrue (null != new JDOMRuntimeException().getMessage());
		assertEquals (null, new JDOMRuntimeException().getCause());

	}

	@Test
	public void testJDOMRuntimeExceptionString() {
		assertEquals ("reason", new JDOMRuntimeException("reason").getMessage());

	}

	@Test
	public void testJDOMRuntimeExceptionStringExn() {
		Throwable t = new Throwable();
		Exception e = new JDOMRuntimeException("reason", t);
		assertTrue (t == e.getCause());
		assertEquals("reason", e.getMessage());
	}

}
