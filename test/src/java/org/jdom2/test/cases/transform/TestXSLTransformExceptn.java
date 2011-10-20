package org.jdom2.test.cases.transform;

import static org.junit.Assert.*;

import org.jdom2.transform.XSLTransformException;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class TestXSLTransformExceptn {

	@Test
	public void testXSLTransformException() {
		XSLTransformException e = new XSLTransformException();
		assertNull(e.getCause());
		assertEquals("Error occurred in JDOM application.", e.getMessage());
	}

	@Test
	public void testXSLTransformExceptionString() {
		XSLTransformException e = new XSLTransformException("msg");
		assertNull(e.getCause());
		assertEquals("msg", e.getMessage());
	}

	@Test
	public void testXSLTransformExceptionStringException() {
		RuntimeException re = new RuntimeException("abc");
		XSLTransformException e = new XSLTransformException("msg", re);
		assertTrue(re == e.getCause());
		assertEquals("msg", e.getMessage());
	}

}
