package org.jdom2.test.cases.output;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.Writer;

import org.jdom2.IllegalDataException;
import org.jdom2.output.AbstractXMLOutputProcessor;
import org.jdom2.output.Format;
import org.junit.Test;

/**
 * 
 * @author Rolf Lear
 *
 */
@SuppressWarnings("javadoc")
public class TestXMLOutputProcessor extends AbstractXMLOutputProcessor {
	
	private static final String formatChar(char ch) {
		return String.format(" char '%s' (0x%04x)", "" + ch, (int)ch);
	}

	private static final class CheckWriter extends Writer {
		private final char[] expect;
		private int cursor = 0;
		private CheckWriter(String expect) {
			this.expect = expect.toCharArray();
		}
		
		private final String formatLast() {
			if (cursor < 10) {
				return "\"" + new String(expect, 0, cursor) + "\"";
			}
			return "\"...{x" + (cursor - 10) + "}" + new String(expect, cursor - 10, 10) + "\"";
		}

		private void checkChar(char ch) {
			if (cursor >= expect.length) {
				fail("We have additional characters. Not expecting " + 
						formatChar(ch) + " after " + formatLast());
			}
			if (ch != expect[cursor]) {
				fail("Expecting " + formatChar(expect[cursor]) + " not " + 
						formatChar(ch) + " after " + formatLast());
			}
			cursor++;
		}
		
		@Override
		public void write(char[] cbuf, int off, int len) {
			for (int i = 0; i < len; i++) {
				checkChar(cbuf[off + i]);
			}
			
		}
		@Override
		public void write(int c) {
			checkChar((char)c);
		}
		
		@Override
		public void close() {
			flush();
		}
		
		@Override
		public void flush() {
			// this is called after a complete output.
			if (cursor < expect.length) {
				fail ("Expected additional characters after " + formatLast());
			}
		}
		
	}
	
	private final Format RAW;
	private final FormatStack fsraw;
	
	public TestXMLOutputProcessor() {
		RAW = Format.getRawFormat();
		RAW.setEncoding("US-ASCII");
		fsraw = new FormatStack(RAW);
	}
	
	
	@Test
	public void testAttributeEscapedEntitiesFilter() throws IOException {
		CheckWriter cw = new CheckWriter(" &quot; &#x153; ' &amp; &lt; &gt; &#xD; &#xA; &#x9; &#x10000; ");
		attributeEscapedEntitiesFilter(cw, fsraw, " \" \u0153 ' & < > \r \n \t \uD800\uDC00 ");
		cw.close();
	}

	@Test
	public void testAttributeEscapedEntitiesFilterASCII() throws IOException {
		CheckWriter cw = new CheckWriter(" &quot; ' &amp; &lt; &gt; &#xD; &#xA; &#x9; &#x10000; ");
		attributeEscapedEntitiesFilter(cw, fsraw, " \" ' & < > \r \n \t \uD800\uDC00 ");
		cw.close();
	}

	@Test
	public void testAttributeEscapedEntitiesFilterErrorMid() {
		CheckWriter cw = new CheckWriter(" &quot; ' &amp; &lt; &gt; &#xD; &#xA; &#x9; &#x10000; ");
		try {
			attributeEscapedEntitiesFilter(cw, fsraw, " \" ' & < > \r \n \t \uD800 \uDC00 ");
			fail("Should have missed the low surrogate...");
		} catch (IllegalDataException ide) {
			//good
		} catch (Exception e) {
			e.printStackTrace();
			fail("Expected IllegalDataException but got " + e.getClass());
		}
	}

	@Test
	public void testAttributeEscapedEntitiesFilterErrorEnd() {
		CheckWriter cw = new CheckWriter(" &quot; ' &amp; &lt; &gt; &#xD; &#xA; &#x9; &#x10000; ");
		try {
			attributeEscapedEntitiesFilter(cw, fsraw, " \" ' & < > \r \n \t \uD800");
			fail("Should have missed the low surrogate...");
		} catch (IllegalDataException ide) {
			//good
		} catch (Exception e) {
			e.printStackTrace();
			fail("Expected IllegalDataException but got " + e.getClass());
		}
	}

	@Test
	public void testAttributeEscapedEntitiesFilterNoEscape() throws IOException {
		CheckWriter cw = new CheckWriter(" \" ' & < > \r \n \t \uD800\uDC00 ");
		FormatStack tmps = new FormatStack(RAW);
		tmps.setEscapeOutput(false);
		attributeEscapedEntitiesFilter(cw, tmps, " \" ' & < > \r \n \t \uD800\uDC00 ");
		cw.close();
	}

	@Test
	public void testTextRawWriterString() throws IOException {
		CheckWriter cw = new CheckWriter(" \" ' & < > \r \n \t \uD800\uDC00 ");
		textRaw(cw, " \" ' & < > \r \n \t \uD800\uDC00 ");
		cw.close();
	}

	@Test
	public void testTextRawWriterChar() throws IOException {
		CheckWriter cw = new CheckWriter(" ");
		textRaw(cw, ' ');
		cw.close();
	}

	@Test
	public void testTextEntityRef() throws IOException {
		CheckWriter cw = new CheckWriter("&er;");
		textEntityRef(cw, "er");
		cw.close();
	}

	@Test
	public void testTextCDATARaw() throws IOException {
		CheckWriter cw = new CheckWriter("<![CDATA[ \" ' & < > \r \n \t \uD800\uDC00 ]]>");
		textCDATARaw(cw, " \" ' & < > \r \n \t \uD800\uDC00 ");
		cw.close();
	}

	@Test
	public void testTextCDATARawEmpty() throws IOException {
		CheckWriter cw = new CheckWriter("<![CDATA[]]>");
		textCDATARaw(cw, "");
		cw.close();
	}

	@Test
	public void testTextCDATACompact() throws IOException {
		// this is a trick one because the \t \r \n are whitespace and get
		// compacted out.
		CheckWriter cw = new CheckWriter("<![CDATA[\" ' & < > \uD800\uDC00]]>");
		textCDATACompact(cw, " \" ' & < > \r \n \t \uD800\uDC00 ");
		cw.close();
	}

	@Test
	public void testTextCDATACompactEmpty() throws IOException {
		// this is a trick one because the \t \r \n are whitespace and get
		// compacted out.
		CheckWriter cw = new CheckWriter("");
		textCDATACompact(cw, "  \r \n \t  ");
		cw.close();
	}

	@Test
	public void testTextCDATATrimFullWhite() throws IOException {
		CheckWriter cw = new CheckWriter("<![CDATA[ \" ' & < > \r \n \t \uD800\uDC00 ]]>");
		textCDATATrimFullWhite(cw, " \" ' & < > \r \n \t \uD800\uDC00 ");
		cw.close();
	}

	@Test
	public void testTextCDATATrimFullWhiteEmpty() throws IOException {
		CheckWriter cw = new CheckWriter("");
		textCDATATrimFullWhite(cw, "  \r \n \t  ");
		cw.close();
	}

	@Test
	public void testTextCDATATrimBoth() throws IOException {
		CheckWriter cw = new CheckWriter("<![CDATA[\" ' & < > \r \n \t \uD800\uDC00]]>");
		textCDATATrimBoth(cw, " \" ' & < > \r \n \t \uD800\uDC00 ");
		cw.close();
	}

	@Test
	public void testTextCDATATrimBothEmpty() throws IOException {
		CheckWriter cw = new CheckWriter("");
		textCDATATrimBoth(cw, "  \r \n \t  ");
		cw.close();
	}

	@Test
	public void testTextCDATATrimRight() throws IOException {
		CheckWriter cw = new CheckWriter("<![CDATA[ \" ' & < > \r \n \t \uD800\uDC00]]>");
		textCDATATrimRight(cw, " \" ' & < > \r \n \t \uD800\uDC00 ");
		cw.close();
	}

	@Test
	public void testTextCDATATrimRightEmpty() throws IOException {
		CheckWriter cw = new CheckWriter("");
		textCDATATrimRight(cw, "  \r \n \t ");
		cw.close();
	}

	@Test
	public void testTextCDATATrimLeft() throws IOException {
		CheckWriter cw = new CheckWriter("<![CDATA[\" ' & < > \r \n \t \uD800\uDC00 ]]>");
		textCDATATrimLeft(cw, " \" ' & < > \r \n \t \uD800\uDC00 ");
		cw.close();
	}

	@Test
	public void testTextCDATATrimLeftEmpty() throws IOException {
		CheckWriter cw = new CheckWriter("");
		textCDATATrimLeft(cw, "  \r \n \t  ");
		cw.close();
	}

	@Test
	public void testTextEscapedEntitiesFilter() throws IOException {
		CheckWriter cw = new CheckWriter(" \" &#x153; ' &amp; &lt; &gt; &#xD; \r\n \t &#x10000; ");
		char[] data = " \" \u0153 ' & < > \r \n \t \uD800\uDC00 ".toCharArray();
		textEscapedEntitiesFilter(cw, fsraw, data, 0, data.length);
		cw.close();
	}

	@Test
	public void testTextEscapedEntitiesFilterErrorMid() {
		CheckWriter cw = new CheckWriter(" \" ' &amp; &lt; &gt; &#xD; \r\n \t &#x10000; ");
		// the HighSurrogate is broken here....
		try {
			char[] data = " \" ' & < > \r \n \t \uD800 \uDC00 ".toCharArray();
			textEscapedEntitiesFilter(cw, fsraw, data, 0, data.length);
			fail("Should have missed the low surrogate...");
		} catch (IllegalDataException ide) {
			//good
		} catch (Exception e) {
			e.printStackTrace();
			fail("Expected IllegalDataException but got " + e.getClass());
		}
	}

	@Test
	public void testTextEscapedEntitiesFilterErrorEnd() {
		CheckWriter cw = new CheckWriter(" \" ' &amp; &lt; &gt; &#xD; \r\n \t &#x10000; ");
		// the HighSurrogate is broken here....
		try {
			char[] data = " \" ' & < > \r \n \t \uD800".toCharArray();
			textEscapedEntitiesFilter(cw, fsraw, data, 0, data.length);
			fail("Should have missed the low surrogate...");
		} catch (IllegalDataException ide) {
			//good
		} catch (Exception e) {
			e.printStackTrace();
			fail("Expected IllegalDataException but got " + e.getClass());
		}
	}

	@Test
	public void testTextEscapedEntitiesFilterNoEscape() throws IOException {
		CheckWriter cw = new CheckWriter(" \" ' & < > \r \n \t \uD800\uDC00 ");
		char[] data = " \" ' & < > \r \n \t \uD800\uDC00 ".toCharArray();
		FormatStack tmps = new FormatStack(RAW);
		tmps.setEscapeOutput(false);
		textEscapedEntitiesFilter(cw, tmps, data, 0, data.length);
		cw.close();
	}

	@Test
	public void testTextEscapeRaw() throws IOException {
		CheckWriter cw = new CheckWriter(" \" ' &amp; &lt; &gt; &#xD; \r\n \t &#x10000; ");
		textEscapeRaw(cw, fsraw, " \" ' & < > \r \n \t \uD800\uDC00 ");
		cw.close();
	}

	@Test
	public void testTextEscapeRawEmpty() throws IOException {
		CheckWriter cw = new CheckWriter("");
		textEscapeRaw(cw, fsraw, "");
		cw.close();
	}

	@Test
	public void testTextEscapeCompact() throws IOException {
		CheckWriter cw = new CheckWriter("\" ' &amp; &lt; &gt; &#x10000;");
		textEscapeCompact(cw, fsraw, " \" ' & < > \r \n \t \uD800\uDC00 ");
		cw.close();
	}

	@Test
	public void testTextEscapeCompactEmpty() throws IOException {
		CheckWriter cw = new CheckWriter("");
		textEscapeCompact(cw, fsraw, "  \r \n   \t  ");
		cw.close();
	}

	@Test
	public void testTextEscapeCompactSurrogateErrorMid() {
		CheckWriter cw = new CheckWriter("\" ' &amp; &lt; &gt; &#x10000;");
		// the HighSurrogate is broken here....
		try {
			textEscapeCompact(cw, fsraw, " \" ' & < > \r \n \t \uD800 \uDC00 ");
			fail("Should have missed the low surrogate...");
		} catch (IllegalDataException ide) {
			//good
		} catch (Exception e) {
			e.printStackTrace();
			fail("Expected IllegalDataException but got " + e.getClass());
		}
	}

	@Test
	public void testTextEscapeCompactSurrogateErrorEnd() {
		CheckWriter cw = new CheckWriter("\" ' &amp; &lt; &gt; &#x10000;");
		// the HighSurrogate is broken here....
		try {
			textEscapeCompact(cw, fsraw, " \" ' & < > \r \n \t \uD800");
			fail("Should have missed the low surrogate...");
		} catch (IllegalDataException ide) {
			//good
		} catch (Exception e) {
			e.printStackTrace();
			fail("Expected IllegalDataException but got " + e.getClass());
		}
	}

	@Test
	public void testTextEscapeCompactNoEscape() throws IOException {
		CheckWriter cw = new CheckWriter("\" ' & < > \uD800\uDC00");
		FormatStack tmps = new FormatStack(RAW);
		tmps.setEscapeOutput(false);
		textEscapeCompact(cw, tmps, " \" ' & < > \r \n \t \uD800\uDC00 ");
		cw.close();
	}

	@Test
	public void testTextEscapeTrimBoth() throws IOException {
		CheckWriter cw = new CheckWriter("\" ' &amp; &lt; &gt; &#xD; \r\n \t &#x10000;");
		textEscapeTrimBoth(cw, fsraw, " \" ' & < > \r \n \t \uD800\uDC00 ");
		cw.close();
	}

	@Test
	public void testTextEscapeTrimBothEmpty() throws IOException {
		CheckWriter cw = new CheckWriter("");
		textEscapeTrimBoth(cw, fsraw, "  \r \n \t  ");
		cw.close();
	}

	@Test
	public void testTextEscapeTrimFullWhite() throws IOException {
		CheckWriter cw = new CheckWriter(" \" ' &amp; &lt; &gt; &#xD; \r\n \t &#x10000; ");
		textEscapeTrimFullWhite(cw, fsraw, " \" ' & < > \r \n \t \uD800\uDC00 ");
		cw.close();
	}

	@Test
	public void testTextEscapeTrimFullWhiteEmpty() throws IOException {
		CheckWriter cw = new CheckWriter("");
		textEscapeTrimFullWhite(cw, fsraw, "  \r \n \t  ");
		cw.close();
	}

	@Test
	public void testTextEscapeTrimRight() throws IOException {
		CheckWriter cw = new CheckWriter(" \" ' &amp; &lt; &gt; &#xD; \r\n \t &#x10000;");
		textEscapeTrimRight(cw, fsraw, " \" ' & < > \r \n \t \uD800\uDC00 ");
		cw.close();
	}

	@Test
	public void testTextEscapeTrimRightEmpty() throws IOException {
		CheckWriter cw = new CheckWriter("");
		textEscapeTrimRight(cw, fsraw, "  \r \n \t    ");
		cw.close();
	}

	@Test
	public void testTextEscapeTrimLeft() throws IOException {
		CheckWriter cw = new CheckWriter("\" ' &amp; &lt; &gt; &#xD; \r\n \t &#x10000; ");
		textEscapeTrimLeft(cw, fsraw, " \" ' & < > \r \n \t \uD800\uDC00 ");
		cw.close();
	}

	@Test
	public void testTextEscapeTrimLeftEmpty() throws IOException {
		CheckWriter cw = new CheckWriter("");
		textEscapeTrimLeft(cw, fsraw, "  \r \n \t  ");
		cw.close();
	}

//	@Test
//	public void testProcessWriterFormatDocument() {
//		
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testProcessWriterFormatDocType() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testProcessWriterFormatElement() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testProcessWriterFormatListOfQextendsContent() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testProcessWriterFormatCDATA() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testProcessWriterFormatText() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testProcessWriterFormatComment() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testProcessWriterFormatProcessingInstruction() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testProcessWriterFormatEntityRef() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testWriteWriterString() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testWriteWriterChar() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testPrintDocument() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testPrintDeclaration() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testPrintDocType() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testPrintProcessingInstruction() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testPrintComment() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testPrintEntityRef() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testPrintCDATA() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testPrintText() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testPrintElement() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testPrintContent() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testPrintTextConsecutive() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testHelperContentDispatcher() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testHelperTextType() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testHelperRawTextType() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testPrintNamespace() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testPrintAttribute() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testIsAllWhitespace() {
//		fail("Not yet implemented");
//	}
//
//
//

}
