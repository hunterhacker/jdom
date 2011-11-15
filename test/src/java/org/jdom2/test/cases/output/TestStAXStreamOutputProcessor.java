package org.jdom2.test.cases.output;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import org.jdom2.output.AbstractStAXStreamProcessor;

@SuppressWarnings("javadoc")
public class TestStAXStreamOutputProcessor extends AbstractStAXStreamProcessor {

	@Test
	public void testTextCompact() {
		// this is a trick one because the \t \r \n are whitespace and get
		// compacted out.
		assertEquals("\" ' & < > \uD800\uDC00",
				textCompact(" \" ' & < > \r \n \t \uD800\uDC00 "));
	}

	@Test
	public void testTextCompactEmpty() {
		// this is a trick one because the \t \r \n are whitespace and get
		// compacted out.
		assertEquals("",
				textCompact("  \r \n \t  "));
	}

	@Test
	public void testTextTrimFullWhite() {
		assertEquals(" \" ' & < > \r \n \t \uD800\uDC00 ",
				textTrimFullWhite(" \" ' & < > \r \n \t \uD800\uDC00 "));
	}

	@Test
	public void testTextTrimFullWhiteEmpty() {
		assertEquals("",
				textTrimFullWhite("  \r \n \t  "));
	}

	@Test
	public void testTextTrimBoth() {
		assertEquals("\" ' & < > \r \n \t \uD800\uDC00",
				textTrimBoth(" \" ' & < > \r \n \t \uD800\uDC00 "));
	}

	@Test
	public void testTextTrimBothEmpty() {
		assertEquals("",
				textTrimBoth("  \r \n \t  "));
	}

	@Test
	public void testTextTrimRight() {
		assertEquals(" \" ' & < > \r \n \t \uD800\uDC00",
				textTrimRight(" \" ' & < > \r \n \t \uD800\uDC00 "));
	}

	@Test
	public void testTextTrimRightEmpty() {
		assertEquals("",
				textTrimRight("  \r \n \t "));
	}

	@Test
	public void testTextTrimLeft() {
		assertEquals("\" ' & < > \r \n \t \uD800\uDC00 ",
				textTrimLeft(" \" ' & < > \r \n \t \uD800\uDC00 "));
	}

	@Test
	public void testTextTrimLeftEmpty() {
		assertEquals("", textTrimLeft("  \r \n \t  "));
	}

}
