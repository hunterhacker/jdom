package org.jdom2.input.sax;

import static org.junit.Assert.*;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class TestTextBuffer {

	@Test
	public void testIsAllWhitespace() {
		TextBuffer tb = new TextBuffer();
		tb.append("    ".toCharArray(), 0, 3);
		assertTrue(tb.isAllWhitespace());
		tb.append("frodo".toCharArray(), 0, 4);
		assertFalse(tb.isAllWhitespace());
	}

	@Test
	public void testToString() {
		// this tests the expansion of the backing array.
		final StringBuilder sb = new StringBuilder();
		final TextBuffer tb = new TextBuffer();
		final char[] data = "frodo".toCharArray();
		for (int i = 1000; i >= 0; i--) {
			sb.append(data);
			tb.append(data, 0, data.length);
			assertEquals(sb.toString(), tb.toString());
		}
	}

}
