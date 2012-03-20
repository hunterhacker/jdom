package org.jdom2.test.cases.output;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jdom2.output.EscapeStrategy;
import org.jdom2.output.Format;
import org.jdom2.output.Format.TextMode;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class TestFormat {

	public static final void checkEquals(Format a, Format b) {
		assertEquals("Expect formatters to have the same Encoding", 
				a.getEncoding(), b.getEncoding());
		//assertEquals("Expect formatters to have the same EscapeStrategy", 
		//		a.getEscapeStrategy(), b.getEscapeStrategy());
		assertEquals("Expect formatters to have the same ExpandEmptyElements", 
				a.getExpandEmptyElements(), b.getExpandEmptyElements());
		assertEquals("Expect formatters to have the same TrAXEscapingPIs", 
				a.getIgnoreTrAXEscapingPIs(), b.getIgnoreTrAXEscapingPIs());
		assertEquals("Expect formatters to have the same Indent", 
				a.getIndent(), b.getIndent());
		assertEquals("Expect formatters to have the same LineSeparator", 
				a.getLineSeparator(), b.getLineSeparator());
		assertEquals("Expect formatters to have the same OmitDeclaration", 
				a.getOmitDeclaration(), b.getOmitDeclaration());
		assertEquals("Expect formatters to have the same OmitEncoding", 
				a.getOmitEncoding(), b.getOmitEncoding());
		assertEquals("Expect formatters to have the same TextMode", 
				a.getTextMode(), b.getTextMode());
		
	}



	@Test
	public void testGetRawFormat() {
		Format mine = Format.getRawFormat();
		assertTrue(mine.getTextMode() == TextMode.PRESERVE);
		assertEquals(mine.getEncoding(), "UTF-8");		
	}

	@Test
	public void testGetPrettyFormat() {
		Format mine = Format.getPrettyFormat();
		assertTrue(mine.getTextMode() == TextMode.TRIM);
		assertEquals(mine.getEncoding(), "UTF-8");
	}

	@Test
	public void testGetCompactFormat() {
		Format mine = Format.getCompactFormat();
		assertTrue(mine.getTextMode() == TextMode.NORMALIZE);
		assertEquals(mine.getEncoding(), "UTF-8");		
	}

	@Test
	public void testEscapeStrategy() {
		EscapeStrategy es = new EscapeStrategy() {
			@Override
			public boolean shouldEscape(char ch) {
				return false;
			}
		};
		Format mine = Format.getRawFormat();
		assertTrue(mine.getEscapeStrategy() != es);
		mine.setEscapeStrategy(es);
		assertTrue(mine.getEscapeStrategy() == es);
	}

	@Test
	public void testLineSeparator() {
		assertEquals("\r\n", Format.getPrettyFormat().getLineSeparator());
		assertEquals("\r\n", Format.getCompactFormat().getLineSeparator());
		
		Format mine = Format.getRawFormat();
		assertEquals("\r\n", mine.getLineSeparator());
		mine.setLineSeparator("  \n  ");
		assertEquals("  \n  ", mine.getLineSeparator());
	}

	@Test
	public void testOmitEncoding() {
		assertFalse(Format.getPrettyFormat().getOmitEncoding());
		assertFalse(Format.getCompactFormat().getOmitEncoding());
		Format mine = Format.getRawFormat();
		assertFalse(mine.getOmitEncoding());
		mine.setOmitEncoding(true);
		assertTrue (mine.getOmitEncoding());
	}

	@Test
	public void testOmitDeclaration() {
		assertFalse(Format.getPrettyFormat().getOmitDeclaration());
		assertFalse(Format.getCompactFormat().getOmitDeclaration());
		Format mine = Format.getRawFormat();
		assertFalse(mine.getOmitDeclaration());
		mine.setOmitDeclaration(true);
		assertTrue (mine.getOmitDeclaration());
	}

	@Test
	public void testSpecifiedAttributesOnly() {
		assertFalse(Format.getPrettyFormat().isSpecifiedAttributesOnly());
		assertFalse(Format.getCompactFormat().isSpecifiedAttributesOnly());
		Format mine = Format.getRawFormat();
		assertFalse(mine.isSpecifiedAttributesOnly());
		mine.setSpecifiedAttributesOnly(true);
		assertTrue (mine.isSpecifiedAttributesOnly());
	}

	@Test
	public void testExpandEmptyElements() {
		assertFalse(Format.getPrettyFormat().getExpandEmptyElements());
		assertFalse(Format.getCompactFormat().getExpandEmptyElements());
		Format mine = Format.getRawFormat();
		assertFalse(mine.getExpandEmptyElements());
		mine.setExpandEmptyElements(true);
		assertTrue (mine.getExpandEmptyElements());
	}

	@Test
	public void testIgnoreTrAXEscapingPIs() {
		assertFalse(Format.getPrettyFormat().getIgnoreTrAXEscapingPIs());
		assertFalse(Format.getCompactFormat().getIgnoreTrAXEscapingPIs());
		Format mine = Format.getRawFormat();
		assertFalse(mine.getIgnoreTrAXEscapingPIs());
		mine.setIgnoreTrAXEscapingPIs(true);
		assertTrue (mine.getIgnoreTrAXEscapingPIs());
	}

	@Test
	public void testTextMode() {
		assertEquals(TextMode.TRIM, Format.getPrettyFormat().getTextMode());
		assertEquals(TextMode.NORMALIZE, Format.getCompactFormat().getTextMode());
		
		Format mine = Format.getRawFormat();
		assertEquals(TextMode.PRESERVE, mine.getTextMode());
		mine.setTextMode(TextMode.TRIM_FULL_WHITE);
		assertEquals(TextMode.TRIM_FULL_WHITE, mine.getTextMode());
		
		assertEquals("TRIM", TextMode.TRIM.toString());
	}

	@Test
	public void testIndent() {
		assertEquals("  ", Format.getPrettyFormat().getIndent());
		assertEquals(null, Format.getCompactFormat().getIndent());
		
		Format mine = Format.getRawFormat();
		assertEquals(null, mine.getIndent());
		mine.setIndent("    ");
		assertEquals("    ", mine.getIndent());
	}

	@Test
	public void testEncoding() {
		assertEquals("UTF-8", Format.getPrettyFormat().getEncoding());
		assertEquals("UTF-8", Format.getCompactFormat().getEncoding());
		
		Format mine = Format.getRawFormat();
		assertEquals("UTF-8", mine.getEncoding());
		mine.setEncoding("US-ASCII");
		assertEquals("US-ASCII", mine.getEncoding());
	}

	@Test
	public void testClone() {
		Format mine = Format.getRawFormat();
		Format clone = mine.clone();
		assertFalse(mine == clone);
		checkEquals(mine, clone);
	}
	
	@Test
	public void test7BitEscapes() {
		checkBitEscape("US-ASCII", 
				new char[] {'a', 'b', '\n', '!'},
				new char[] {(char)128, (char)255, (char)1234});
	}
	
	@Test
	public void testASCIIBitEscapes() {
		// ISO646-US is another name for US-ASCII
		// JDOM does not know that, but Java does.
		checkBitEscape("ISO646-US", 
				new char[] {'a', 'b', '\n', '!'},
				new char[] {(char)128, (char)255, (char)1234});
	}
	
	@Test
	public void test8BitEscapes() {
		checkBitEscape("Latin1", 
				new char[] {'a', 'b', '\n', '!', (char)128, (char)255},
				new char[] {(char)1234});
	}
	
	@Test
	public void test16BitEscapes() {
		checkBitEscape("UTF-16", 
				new char[] {'a', 'b', '\n', '!', (char)128, (char)255, (char)1234},
				new char[] {(char)0xD800});
	}
	
	@Test
	public void testCharsetEncodingEscapes() {
		checkBitEscape("windows-1252",
				new char[] {'a', 'b', '\n', '!', (char)127, (char)255},
				new char[] {(char)0xD800, (char)1234});
	}
	
	@Test
	public void testIllegalEncodingEscapes() {
		checkBitEscape("junk",
				new char[] {'a', 'b', '\n', '!', (char)128, (char)255, (char)1234},
				new char[] {(char)0xD800});
	}
	
	private void checkBitEscape(String encoding, 
			char[] keep, char[] escape) {
		Format form = Format.getPrettyFormat();
		form.setEncoding(encoding);
		EscapeStrategy es = form.getEscapeStrategy();
		for (char ch : keep) {
			assertFalse("Should Not Escape " + ch, es.shouldEscape(ch));
		}
		for (char ch : escape) {
			assertTrue("Should Escape " + ch, es.shouldEscape(ch));
		}
	}
	
	private void checkTrim(String base, String both, String left, String right, String compact) {
		assertEquals(both,    Format.trimBoth(base));
		assertEquals(left,    Format.trimLeft(base));
		assertEquals(right,   Format.trimRight(base));
		assertEquals(compact, Format.compact(base));
	}
	
	@Test
	public void testTrimming() {
		checkTrim("", "", "", "", "");
		checkTrim(" ", "", "", "", "");
		checkTrim("x", "x", "x", "x", "x");
		checkTrim("foo", "foo", "foo", "foo", "foo");
		checkTrim(" \r\n  ", "", "", "", "");
		checkTrim(" \rx\n  ", "x", "x\n  ", " \rx", "x");
		checkTrim(" \rx \t y\n  ", "x \t y", "x \t y\n  ", " \rx \t y", "x y");
	}
	
	private void checkEscapes(String eol, String base, String txt, String att) {
		EscapeStrategy strategy = Format.getPrettyFormat().getEscapeStrategy();
		assertEquals(txt, Format.escapeText(strategy, eol, base));
		assertEquals(att, Format.escapeAttribute(strategy, base));
	}
	
	@Test
	public void testEscapeText() {
		checkEscapes(null, "", "", "");
		checkEscapes(null, " \n ", " \n ", " &#xA; ");
		checkEscapes("\r\n", " \n ", " \r\n ", " &#xA; ");
		checkEscapes(null, " \" \n ", " \" \n ", " &quot; &#xA; ");
		checkEscapes("\r\n", " \" \n ", " \" \r\n ", " &quot; &#xA; ");
	}

}
