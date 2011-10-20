package org.jdom2.test.cases;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.PrintStream;
import java.io.PrintWriter;

import org.jdom2.JDOMException;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class TestJDOMExceptn {

	@Test
	public void testJDOMException() {
		JDOMException e = new JDOMException();
		assertEquals(e.getMessage(), "Error occurred in JDOM application.");
		assertEquals(e.getCause(), null);
	}

	@Test
	public void testJDOMExceptionString() {
		JDOMException e = new JDOMException("foo");
		assertEquals(e.getMessage(), "foo");
		assertEquals(e.getCause(), null);
	}

	@Test
	public void testJDOMExceptionStringThrowable() {
		Exception c = new RuntimeException("cause");
		JDOMException e = new JDOMException("foo", c);
		// assertEquals(e.getMessage(), "foo");
		assertEquals(e.getCause(), c);
	}

	@Test
	public void testInitCauseThrowable() {
		Exception c = new RuntimeException("cause");
		JDOMException e = new JDOMException("foo");
		assertEquals(e.getMessage(), "foo");
		assertEquals(e.getCause(), null);
		e.initCause(c);
		assertEquals(e.getCause(), c);
	}

	@Test
	public void testPrintStackTracePrintStream() {
		Exception c = new RuntimeException("cause");
		JDOMException e = new JDOMException("foo", c);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		e.printStackTrace(new PrintStream(baos));
		String msg = baos.toString();
		assertTrue(msg != null);
		assertTrue(msg.indexOf(JDOMException.class.getName()) == 0);
		assertTrue(msg.indexOf("Caused by") > 0);
	}

	@Test
	public void testPrintStackTracePrintWriter() {
		Exception c = new RuntimeException("cause");
		JDOMException e = new JDOMException("foo", c);
		CharArrayWriter caw = new CharArrayWriter();
		e.printStackTrace(new PrintWriter(caw));
		String msg = caw.toString();
		assertTrue(msg != null);
		assertTrue(msg.indexOf(JDOMException.class.getName()) == 0);
		assertTrue(msg.indexOf("Caused by") > 0);
	}


}
