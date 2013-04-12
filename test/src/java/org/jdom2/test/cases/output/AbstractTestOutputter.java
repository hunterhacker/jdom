package org.jdom2.test.cases.output;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import org.jdom2.Attribute;
import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.Content;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.EntityRef;
import org.jdom2.Namespace;
import org.jdom2.Parent;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;
import org.jdom2.output.Format;
import org.jdom2.output.Format.TextMode;

@SuppressWarnings("javadoc")
public abstract class AbstractTestOutputter {
	
	protected static interface FormatSetup {
		public void setup(Format fmt);
	}
	
	
	private final boolean cr2xD;
	private final boolean pademptyelement;
	private final boolean forceexpand;
	private final boolean padpi;
	private final boolean usesrawxmlout;

	public AbstractTestOutputter(boolean cr2xD, boolean padpreempty, boolean padpi,
			boolean forceexpand, boolean usesrawxmlout) {
		this.cr2xD = cr2xD;
		this.pademptyelement = padpreempty;
		this.forceexpand = forceexpand;
		this.padpi = padpi;
		this.usesrawxmlout = usesrawxmlout;
	}
	
	protected final String expect(String expect) {
		if (cr2xD) {
			expect = expect.replaceAll("\r", "&#xD;");
		}
		if (forceexpand) {
			expect = expect.replaceAll("<(\\w+(:\\w+)?)\\s*/>", "<$1></$1>");
			expect = expect.replaceAll("<(\\w+(:\\w+)?)\\s+(.+?)\"\\s*/>", "<$1 $3\"></$1>");
		}
		if (padpi) {
			expect = expect.replaceAll("(<\\?\\w+)(\\?>)", "$1 $2");
		}
		if (pademptyelement) {
			//expect = expect.replaceAll("\">", "\" >");
			//expect = expect.replaceAll("<(\\w+)>", "<$1 >");
			expect = expect.replaceAll("<(\\w+(:\\w+)?)/>", "<$1 />");
			expect = expect.replaceAll("<(\\w+(:\\w+)?\\s.+\")/>", "<$1 />");
		} else {
			//expect = expect.replaceAll("<(\\w+)\\s+>", "<$1>");
			expect = expect.replaceAll("<(\\w+)(\\s+.+?)?\\s+/>", "<$1$2/>");
			expect = expect.replaceAll("<(\\w+:\\w+)(\\s+.+?)?\\s+/>", "<$1$2/>");
		}
//		if (rawoutsideroot) {
//			// outside the root element will be raw-formatted.
//			StringBuilder sb = new StringBuilder(expect.length());
//			int gotstuff = 0;
//			boolean indoctype = false;
//			boolean gotroot = false;
//			int depth = 0;
//			char[] chars = expect.toCharArray();
//			int i = 0;
//			while (i < chars.length && Verifier.isXMLWhitespace(chars[i])) {
//				// skip initial whitespace.
//				i++;
//			}
//			for (; i < chars.length; i++) {
//				char c = chars[i];
//				sb.append(c);
//				if (!gotroot) {
//					if (c == '<') {
//						if (depth == 0) {
//							if (i < chars.length - 2) {
//								if (chars[i + 1] == '?') {
//									// PI or XML Declaration
//									gotstuff++;
//								} else if (chars[i + 1] == '!') {
//									// Comment of DOCTYPE
//									gotstuff++;
//									if (chars[i + 2] == 'D') {
//										// DOCTYPE
//										indoctype = true;
//									}
//								} else {
//									// root element
//									gotroot = true;
//								}
//							} else {
//								gotroot = true;
//							}
//						}
//						depth++;
//					} else if (c == '>') {
//						depth--;
//						if (depth == 0) {
//							if (indoctype) {
//								sb.append('\n');
//								indoctype = false;
//							}
//							while (i+1 < chars.length && Verifier.isXMLWhitespace(chars[i + 1])) {
//								// skip whitespace after top-level content.
//								i++;
//							}
//						}
//					}
//				}
//			}
//			while (Verifier.isXMLWhitespace(sb.charAt(sb.length() - 1))) {
//				// eliminate trailing whitespace.
//				sb.setLength(sb.length() - 1);
//			}
//			if (gotstuff > 1 || (gotroot && gotstuff > 0)) {
//				// there is multiple content stuff, need to trim the whitespace....
//				expect = sb.toString();
//			}
//		}
		return expect;
	}

	/**
	 * Return a string representing a {@link Document}. Uses an internal
	 * StringWriter.
	 * <p>
	 * <b>Warning</b>: a String is Unicode, which may not match the outputter's
	 * specified encoding.
	 * 
	 * @param doc
	 *        <code>Document</code> to format.
	 * @return the input content formatted as an XML String.
	 * @throws NullPointerException
	 *         if the specified content is null.
	 */
	public abstract String outputString(Format format, Document doc);

	/**
	 * Return a string representing a {@link DocType}.
	 * <p>
	 * <b>Warning</b>: a String is Unicode, which may not match the outputter's
	 * specified encoding.
	 * 
	 * @param doctype
	 *        <code>DocType</code> to format.
	 * @return the input content formatted as an XML String.
	 * @throws NullPointerException
	 *         if the specified content is null.
	 */
	public abstract String outputString(Format format, DocType doctype);

	/**
	 * Return a string representing an {@link Element}.
	 * <p>
	 * <b>Warning</b>: a String is Unicode, which may not match the outputter's
	 * specified encoding.
	 * 
	 * @param element
	 *        <code>Element</code> to format.
	 * @return the input content formatted as an XML String.
	 * @throws NullPointerException
	 *         if the specified content is null.
	 */
	public abstract String outputString(Format format, Element element);

	/**
	 * Return a string representing a List of {@link Content} nodes. <br>
	 * The list is assumed to contain legal JDOM nodes. If other content is
	 * coerced on to the list it will cause ClassCastExceptions, and null List
	 * members will cause NullPointerException.
	 * <p>
	 * <b>Warning</b>: a String is Unicode, which may not match the outputter's
	 * specified encoding.
	 * 
	 * @param list
	 *        <code>List</code> to format.
	 * @return the input content formatted as an XML String.
	 * @throws ClassCastException
	 *         if non-{@link Content} is forced in to the list
	 * @throws NullPointerException
	 *         if the List is null or contains null members.
	 */
	public abstract String outputString(Format format, List<? extends Content> list);
	
	/**
	 * Return a string representing a {@link CDATA} node.
	 * <p>
	 * <b>Warning</b>: a String is Unicode, which may not match the outputter's
	 * specified encoding.
	 * 
	 * @param cdata
	 *        <code>CDATA</code> to format.
	 * @return the input content formatted as an XML String.
	 * @throws NullPointerException
	 *         if the specified content is null.
	 */
	public abstract String outputString(Format format, CDATA cdata);

	/**
	 * Return a string representing a {@link Text} node.
	 * <p>
	 * <b>Warning</b>: a String is Unicode, which may not match the outputter's
	 * specified encoding.
	 * 
	 * @param text
	 *        <code>Text</code> to format.
	 * @return the input content formatted as an XML String.
	 * @throws NullPointerException
	 *         if the specified content is null.
	 */
	public abstract String outputString(Format format, Text text);
	
	/**
	 * Return a string representing a {@link Comment}.
	 * <p>
	 * <b>Warning</b>: a String is Unicode, which may not match the outputter's
	 * specified encoding.
	 * 
	 * @param comment
	 *        <code>Comment</code> to format.
	 * @return the input content formatted as an XML String.
	 * @throws NullPointerException
	 *         if the specified content is null.
	 */
	public abstract String outputString(Format format, Comment comment);

	/**
	 * Return a string representing a {@link ProcessingInstruction}.
	 * <p>
	 * <b>Warning</b>: a String is Unicode, which may not match the outputter's
	 * specified encoding.
	 * 
	 * @param pi
	 *        <code>ProcessingInstruction</code> to format.
	 * @return the input content formatted as an XML String.
	 * @throws NullPointerException
	 *         if the specified content is null.
	 */
	public abstract String outputString(Format format, ProcessingInstruction pi);

	/**
	 * Return a string representing an {@link EntityRef}.
	 * <p>
	 * <b>Warning</b>: a String is Unicode, which may not match the outputter's
	 * specified encoding.
	 * 
	 * @param entity
	 *        <code>EntityRef</code> to format.
	 * @return the input content formatted as an XML String.
	 * @throws NullPointerException
	 *         if the specified content is null.
	 */
	public abstract String outputString(Format format, EntityRef entity);

	/**
	 * This will handle printing out an <code>{@link
	 * Element}</code>'s content only, not including its tag, and attributes.
	 * This can be useful for printing the content of an element that contains
	 * HTML, like "&lt;description&gt;JDOM is
	 * &lt;b&gt;fun&gt;!&lt;/description&gt;".
	 * <p>
	 * <b>Warning</b>: a String is Unicode, which may not match the outputter's
	 * specified encoding.
	 * 
	 * @param element
	 *        <code>Element</code> to output.
	 * @return the input content formatted as an XML String.
	 * @throws NullPointerException
	 *         if the specified content is null.
	 */
	public abstract String outputElementContentString(Format format, Element element);

	protected static final Format fraw = Format.getRawFormat();
	protected static final Format fcompact = Format.getCompactFormat();
	protected static final Format fpretty = Format.getPrettyFormat();
	protected static final Format ftso = Format.getPrettyFormat();
	protected static final Format ftfw = Format.getPrettyFormat();
	
	static {
		fraw.setLineSeparator("\n");
		fcompact.setLineSeparator("\n");
		fpretty.setLineSeparator("\n");
		ftso.setLineSeparator("\n");
		ftso.setSpecifiedAttributesOnly(true);
		ftfw.setLineSeparator("\n");
		ftfw.setTextMode(TextMode.TRIM_FULL_WHITE);
	}
	

	@Test
	public void testTextEmpty() {
		Text content = new Text("");
		assertEquals("", outputString(fraw,     content));
		assertEquals("", outputString(fcompact, content));
		assertEquals("", outputString(fpretty,  content));
		assertEquals("", outputString(ftso,     content));
		assertEquals("", outputString(ftfw,     content));
	}
	
	@Test
	public void testTextWhitespace() {
		Text content = new Text(" \r \n \t ");
		assertEquals(expect(" \r \n \t "), 
				outputString(fraw,     content));
		assertEquals("", 
				outputString(fcompact, content));
		assertEquals("", 
				outputString(fpretty,  content));
		assertEquals("", 
				outputString(ftso,     content));
		assertEquals("", 
				outputString(ftfw,     content));
	}

	@Test
	public void testTextWithText() {
		Text content = new Text(" \r & \n \t ");
		assertEquals(expect(" \r &amp; \n \t "), 
				outputString(fraw,     content));
		assertEquals(expect("&amp;"), 
				outputString(fcompact, content));
		assertEquals(expect("&amp;"), 
				outputString(fpretty,  content));
		assertEquals(expect("&amp;"), 
				outputString(ftso,     content));
		assertEquals(expect(" \r &amp; \n \t "), 
				outputString(ftfw,     content));
	}

	@Test
	public void testCDATAEmpty() {
		CDATA content = new CDATA("");
		assertEquals("<![CDATA[]]>", 
				outputString(fraw,     content));
		assertEquals("",
				outputString(fcompact, content));
		assertEquals("",
				outputString(fpretty,  content));
		assertEquals("",
				outputString(ftso,     content));
		assertEquals("",
				outputString(ftfw,     content));
	}
	
	@Test
	public void testCDATAWhitespace() {
		CDATA content = new CDATA(" \r \n \t ");
		assertEquals("<![CDATA[ \r \n \t ]]>", 
				outputString(fraw,     content));
		assertEquals("", 
				outputString(fcompact, content));
		assertEquals("", 
				outputString(fpretty,  content));
		assertEquals("", 
				outputString(ftso,      content));
		assertEquals("", 
				outputString(ftfw,     content));
	}

	@Test
	public void testCDATAWithText() {
		CDATA content = new CDATA(" \r & \n \t ");
		assertEquals("<![CDATA[ \r & \n \t ]]>", 
				outputString(fraw,     content));
		assertEquals("<![CDATA[&]]>", 
				outputString(fcompact, content));
		assertEquals("<![CDATA[&]]>", 
				outputString(fpretty,  content));
		assertEquals("<![CDATA[&]]>", 
				outputString(ftso,  content));
		assertEquals("<![CDATA[ \r & \n \t ]]>", 
				outputString(ftfw,     content));
	}

	@Test
	public void testEntityRef() {
		EntityRef content = new EntityRef("ref");
		assertEquals("&ref;", 
				outputString(fraw,     content));
		assertEquals("&ref;", 
				outputString(fcompact, content));
		assertEquals("&ref;", 
				outputString(fpretty,  content));
		assertEquals("&ref;", 
				outputString(ftso,  content));
		assertEquals("&ref;", 
				outputString(ftfw,     content));
	}
	
	@Test
	public void testProcessingInstructionTargetOnly() {
		ProcessingInstruction content = new ProcessingInstruction("target");
		assertEquals(expect("<?target?>"), 
				outputString(fraw,     content));
		assertEquals(expect("<?target?>"), 
				outputString(fcompact, content));
		assertEquals(expect("<?target?>"), 
				outputString(fpretty,  content));
		assertEquals(expect("<?target?>"), 
				outputString(ftfw,     content));
	}
	
	@Test
	public void testProcessingInstructionTargetWithData() {
		ProcessingInstruction content = 
				new ProcessingInstruction("target", "data");
		assertEquals("<?target data?>", 
				outputString(fraw,     content));
		assertEquals("<?target data?>", 
				outputString(fcompact, content));
		assertEquals("<?target data?>", 
				outputString(fpretty,  content));
		assertEquals("<?target data?>", 
				outputString(ftso,  content));
		assertEquals("<?target data?>", 
				outputString(ftfw,     content));
	}
	
	@Test
	public void testComment() {
		Comment content = new Comment("comment");
		assertEquals("<!--comment-->", 
				outputString(fraw,     content));
		assertEquals("<!--comment-->", 
				outputString(fcompact, content));
		assertEquals("<!--comment-->", 
				outputString(fpretty,  content));
		assertEquals("<!--comment-->", 
				outputString(ftso,  content));
		assertEquals("<!--comment-->", 
				outputString(ftfw,     content));
	}
	

	@Test
	public void testDocTypeSimple() {
		DocType content = new DocType("root");
		assertEquals("<!DOCTYPE root>", 
				outputString(fraw,     content));
		assertEquals("<!DOCTYPE root>", 
				outputString(fcompact, content));
		assertEquals("<!DOCTYPE root>", 
				outputString(fpretty,  content));
		assertEquals("<!DOCTYPE root>", 
				outputString(ftso,  content));
		assertEquals("<!DOCTYPE root>", 
				outputString(ftfw,     content));
	}
	
	@Test
	public void testDocTypeSimpleISS() {
		DocType content = new DocType("root");
		content.setInternalSubset("<!ENTITY name \"value\">");
		assertEquals("<!DOCTYPE root [\n<!ENTITY name \"value\">]>", 
				outputString(fraw,     content));
		assertEquals("<!DOCTYPE root [\n<!ENTITY name \"value\">]>", 
				outputString(fcompact, content));
		assertEquals("<!DOCTYPE root [\n<!ENTITY name \"value\">]>", 
				outputString(fpretty,  content));
		assertEquals("<!DOCTYPE root [\n<!ENTITY name \"value\">]>", 
				outputString(ftso,  content));
		assertEquals("<!DOCTYPE root [\n<!ENTITY name \"value\">]>", 
				outputString(ftfw,     content));
	}
	
	@Test
	public void testDocTypeSystemID() {
		DocType content = new DocType("root", "sysid");
		assertEquals("<!DOCTYPE root SYSTEM \"sysid\">", 
				outputString(fraw,     content));
		assertEquals("<!DOCTYPE root SYSTEM \"sysid\">", 
				outputString(fcompact, content));
		assertEquals("<!DOCTYPE root SYSTEM \"sysid\">", 
				outputString(fpretty,  content));
		assertEquals("<!DOCTYPE root SYSTEM \"sysid\">", 
				outputString(ftso,  content));
		assertEquals("<!DOCTYPE root SYSTEM \"sysid\">", 
				outputString(ftfw,     content));
	}
	
	@Test
	public void testDocTypeSystemIDISS() {
		DocType content = new DocType("root", "sysid");
		content.setInternalSubset("internal");
		assertEquals("<!DOCTYPE root SYSTEM \"sysid\" [\ninternal]>", 
				outputString(fraw,     content));
		assertEquals("<!DOCTYPE root SYSTEM \"sysid\" [\ninternal]>", 
				outputString(fcompact, content));
		assertEquals("<!DOCTYPE root SYSTEM \"sysid\" [\ninternal]>", 
				outputString(fpretty,  content));
		assertEquals("<!DOCTYPE root SYSTEM \"sysid\" [\ninternal]>", 
				outputString(ftso,  content));
		assertEquals("<!DOCTYPE root SYSTEM \"sysid\" [\ninternal]>", 
				outputString(ftfw,     content));
	}
	
	@Test
	public void testDocTypePublicSystemID() {
		DocType content = new DocType("root", "pubid", "sysid");
		assertEquals("<!DOCTYPE root PUBLIC \"pubid\" \"sysid\">", 
				outputString(fraw,     content));
		assertEquals("<!DOCTYPE root PUBLIC \"pubid\" \"sysid\">", 
				outputString(fcompact, content));
		assertEquals("<!DOCTYPE root PUBLIC \"pubid\" \"sysid\">", 
				outputString(fpretty,  content));
		assertEquals("<!DOCTYPE root PUBLIC \"pubid\" \"sysid\">", 
				outputString(ftso,  content));
		assertEquals("<!DOCTYPE root PUBLIC \"pubid\" \"sysid\">", 
				outputString(ftfw,     content));
	}
	
	@Test
	public void testDocTypePublicSystemIDISS() {
		DocType content = new DocType("root", "pubid", "sysid");
		content.setInternalSubset("internal");
		assertEquals("<!DOCTYPE root PUBLIC \"pubid\" \"sysid\" [\ninternal]>", 
				outputString(fraw,     content));
		assertEquals("<!DOCTYPE root PUBLIC \"pubid\" \"sysid\" [\ninternal]>", 
				outputString(fcompact, content));
		assertEquals("<!DOCTYPE root PUBLIC \"pubid\" \"sysid\" [\ninternal]>", 
				outputString(fpretty,  content));
		assertEquals("<!DOCTYPE root PUBLIC \"pubid\" \"sysid\" [\ninternal]>", 
				outputString(ftso,  content));
		assertEquals("<!DOCTYPE root PUBLIC \"pubid\" \"sysid\" [\ninternal]>", 
				outputString(ftfw,     content));
	}
	
	@Test
	public void testMultiWhiteText() {
		Element root = new Element("root");
		root.addContent(new CDATA(" "));
		root.addContent(new Text(" "));
		root.addContent(new Text("    "));
		root.addContent(new Text(""));
		root.addContent(new Text(" "));
		root.addContent(new Text("  \n \n "));
		root.addContent(new Text("  \t "));
		root.addContent(new Text("  "));
		assertEquals(expect("<root><![CDATA[ ]]>        \n \n   \t   </root>"), 
				outputString(fraw,     root));
		assertEquals(expect("<root/>"), 
				outputString(fcompact, root));
		assertEquals(expect("<root/>"), 
				outputString(fpretty,  root));
		assertEquals(expect("<root/>"), 
				outputString(ftso,  root));
		assertEquals(expect("<root/>"), 
				outputString(ftfw,     root));
	}
	
	@Test
	public void testMultiText() {
		Element root = new Element("root");
		root.addContent(new CDATA(" "));
		root.addContent(new Text(" "));
		root.addContent(new Text("    "));
		root.addContent(new Text(""));
		root.addContent(new Text("X"));
		root.addContent(new Text("  \n \n "));
		root.addContent(new Text("  \t "));
		root.addContent(new Text("  "));
		assertEquals(expect("<root><![CDATA[ ]]>     X  \n \n   \t   </root>"), 
				outputString(fraw,     root));
		assertEquals(expect("<root>X</root>"), 
				outputString(fcompact, root));
		assertEquals(expect("<root>X</root>"), 
				outputString(fpretty,  root));
		assertEquals(expect("<root>X</root>"), 
				outputString(ftso,  root));
		assertEquals(expect("<root><![CDATA[ ]]>     X  \n \n   \t   </root>"), 
				outputString(ftfw,     root));
	}
	
	@Test
	public void testDocumentSimple() {
		Document content = new Document();
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n", 
				outputString(fraw,     content));
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n", 
				outputString(fcompact, content));
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n", 
				outputString(fpretty,  content));
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n", 
				outputString(ftso,  content));
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n",
				outputString(ftfw,     content));
	}
	
	@Test
	public void testDocumentDocType() {
		Document content = new Document();
		content.setDocType(new DocType("root"));
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE root>\n", 
				outputString(fraw,     content));
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE root>\n", 
				outputString(fcompact, content));
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE root>\n", 
				outputString(fpretty,  content));
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE root>\n", 
				outputString(ftso,  content));
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE root>\n",
				outputString(ftfw,     content));
	}
	
	@Test
	public void testDocumentComment() {
		Document content = new Document();
		content.addContent(new Comment("comment"));
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!--comment-->\n", 
				outputString(fraw,     content));
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!--comment-->\n", 
				outputString(fcompact, content));
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!--comment-->\n", 
				outputString(fpretty,  content));
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!--comment-->\n", 
				outputString(ftso,  content));
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!--comment-->\n",
				outputString(ftfw,     content));
	}

	
	
	
	
	
	
	
	
	
	@Test
	public void testXXX() {
		Text content = new Text("");
		assertEquals("", 
				outputString(fraw,     content));
		assertEquals("", 
				outputString(fcompact, content));
		assertEquals("", 
				outputString(fpretty,  content));
		assertEquals("", 
				outputString(ftfw,     content));
	}
	
	
	
	
	
	@Test
	public void testOutputText() {
		checkOutput(new Text(" hello  there  "), " hello  there  ", "hello there", "hello  there", "hello  there", " hello  there  ");
	}

	@Test
	public void testOutputCDATA() {
		String indata = "   hello   there  bozo !   ";
		String rawcdata   = "<![CDATA[   hello   there  bozo !   ]]>";
		String compdata   = "<![CDATA[hello there bozo !]]>";
		String prettydata = "<![CDATA[hello   there  bozo !]]>";
		String trimdata   = "<![CDATA[   hello   there  bozo !   ]]>";
		
		checkOutput(new CDATA(indata), rawcdata, compdata, prettydata, prettydata, trimdata);
	}

	@Test
	public void testOutputComment() {
		String incomment = "   hello   there  bozo !   ";
		String outcomment = "<!--" + incomment + "-->";
		checkOutput(new Comment(incomment), outcomment, outcomment, outcomment, outcomment, outcomment);
	}

	@Test
	public void testOutputProcessingInstructionSimple() {
		ProcessingInstruction inpi = new ProcessingInstruction("jdomtest", "");
		String outpi = "<?jdomtest?>";
		checkOutput(inpi, outpi, outpi, outpi, outpi, outpi);
	}

	@Test
	public void testOutputProcessingInstructionData() {
		String pi = "  hello   there  ";
		ProcessingInstruction inpi = new ProcessingInstruction("jdomtest", pi);
		String outpi = "<?jdomtest " + pi + "?>";
		checkOutput(inpi, outpi, outpi, outpi, outpi, outpi);
	}

	@Test
	public void testOutputEntityRef() {
		checkOutput(new EntityRef("name", "publicID", "systemID"),
				"&name;", "&name;", "&name;", "&name;", "&name;");
	}

	@Test
	public void testOutputElementSimple() {
		String txt = "<root/>";
		checkOutput(new Element("root"), txt, txt, txt, txt, txt);
	}

	@Test
	public void testOutputElementAttribute() {
		String txt = "<root att=\"val\" />";
		checkOutput(new Element("root").setAttribute("att", "val"), txt, txt, txt, txt, txt);
	}

	@Test
	public void testOutputElementAttributeNotSpecifiedA() {
		String txt = "<root att=\"val\" />";
		final Element root = new Element("root");
		final Attribute att = new Attribute("att", "val");
		root.setAttribute(att);
		att.setSpecified(false);
		checkOutput(root, txt, txt, txt, "<root />", txt);
	}

	@Test
	public void testOutputElementAttributeNotSpecifiedB() {
		String txt = "<root atta=\"val\" attb=\"attb\" />";
		final Element root = new Element("root");
		final Attribute atta = new Attribute("atta", "val");
		final Attribute attb = new Attribute("attb", "attb");
		root.setAttribute(atta);
		root.setAttribute(attb);
		atta.setSpecified(false);
		checkOutput(root, txt, txt, txt, "<root attb=\"attb\" />", txt);
	}

	@Test
	public void testOutputElementCDATA() {
		String txt = "<root><![CDATA[xx]]></root>";
		Element root = new Element("root");
		root.addContent(new CDATA("xx"));
		checkOutput(root, txt, txt, txt, txt, txt);
	}

	@Test
	public void testOutputElementExpandEmpty() {
		String txt = "<root></root>";
		FormatSetup setup = new FormatSetup() {
			@Override
			public void setup(Format fmt) {
				fmt.setExpandEmptyElements(true);
			}
		};
		checkOutput(new Element("root"), setup, txt, txt, txt, txt, txt);
	}

	@Test
	public void testOutputElementPreserveSpace() {
		String txt = "<root xml:space=\"preserve\">    <child xml:space=\"default\">abc</child> </root>";
		Element root = new Element("root");
		root.setAttribute("space", "preserve", Namespace.XML_NAMESPACE);
		root.addContent("    ");
		Element child = new Element("child");
		child.setAttribute("space", "default", Namespace.XML_NAMESPACE);
		child.addContent("abc");
		root.addContent(child);
		root.addContent(" ");
		checkOutput(root, txt, txt, txt, txt, txt);
	}
	
	@Test
	public void testOutputElementPreserveSpaceComplex() {
		// the purpose of this test is to ensure that the different
		// formatting values are used when going down one level,
		// back up, then in to 'preserve', back up, and then again
		// down in to normal (not preserve).
		
		// this is essentially a test of the FormatStack code....

		Element tst = new Element("child");
		Comment cmt = new Comment("comment");
		tst.addContent(cmt);
		String spaced = "  <child>\n    <!--comment-->\n  </child>\n";
		String compact = "<child><!--comment--></child>";
		String preserved = "<child xml:space=\"preserve\"><!--comment--></child>";
		Element root = new Element("root");
		root.addContent(tst.clone());
		root.addContent(tst.clone().setAttribute("space", "preserve", Namespace.XML_NAMESPACE));
		root.addContent(tst.clone());
		String rawcompact = "<root>" + compact + preserved + compact + "</root>";
		String pretty = "<root>\n" + spaced + "  " + preserved + "\n" + spaced + "</root>";
		checkOutput(root, rawcompact, rawcompact, pretty, pretty, pretty);
	}
	

	@Test
	public void testOutputElementMultiText() {
		Element root = new Element("root");
		root.addContent(new CDATA(" "));
		root.addContent(new Text(" xx "));
		root.addContent(new Text("yy"));
		root.addContent(new Text("    "));
		root.addContent(new Text("zz"));
		root.addContent(new Text("  ww"));
		root.addContent(new EntityRef("amp"));
		root.addContent(new Text("vv"));
		root.addContent(new Text("  "));
		checkOutput(root,
				"<root><![CDATA[ ]]> xx yy    zz  ww&amp;vv  </root>", 
				"<root>xx yy zz ww&amp;vv</root>",
				"<root>xx yy    zz  ww&amp;vv</root>",
				"<root>xx yy    zz  ww&amp;vv</root>",
				// This should be changed with issue #31.
				// The real value should have one additional
				// space at the beginning and two at the end
				// for now we leave the broken test here because it
				// helps with the coverage reports.
				// the next test is added to be a failing test.
				"<root><![CDATA[ ]]> xx yy    zz  ww&amp;vv  </root>");
	}

	@Test
	public void testOutputElementMultiAllWhite() {
		Element root = new Element("root");
		root.addContent(new CDATA(" "));
		root.addContent(new Text(" "));
		root.addContent(new Text("    "));
		root.addContent(new Text(""));
		root.addContent(new Text(" "));
		root.addContent(new Text("  \n \n "));
		root.addContent(new Text("  \t "));
		root.addContent(new Text("  "));
		checkOutput(root,
				"<root><![CDATA[ ]]>        \n \n   \t   </root>", 
				"<root />",
				"<root />",
				"<root />",
				"<root />");
	}

	@Test
	public void testOutputElementMultiAllWhiteExpandEmpty() {
		Element root = new Element("root");
		root.addContent(new CDATA(" "));
		root.addContent(new Text(" "));
		root.addContent(new Text("    "));
		root.addContent(new Text(""));
		root.addContent(new Text(" "));
		root.addContent(new Text("  \n \n "));
		root.addContent(new Text("  \t "));
		root.addContent(new Text("  "));
		FormatSetup fs = new FormatSetup() {
			@Override
			public void setup(Format fmt) {
				fmt.setExpandEmptyElements(true);
			}
		};
		checkOutput(root, fs,  
				"<root><![CDATA[ ]]>        \n \n   \t   </root>", 
				"<root></root>",
				"<root></root>",
				"<root></root>",
				"<root></root>");
	}

	@Test
	public void testOutputElementMultiMostWhiteExpandEmpty() {
		// this test has mixed content (text-type and not text type).
		// and, it has a multi-text-type at the end.
		Element root = new Element("root");
		root.addContent(new CDATA(" "));
		root.addContent(new Text(" "));
		root.addContent(new Text("    "));
		root.addContent(new Text(""));
		root.addContent(new Text(" "));
		root.addContent(new Text("  \n \n "));
		root.addContent(new Comment("Boo"));
		root.addContent(new Text("  \t "));
		root.addContent(new Text("  "));
		FormatSetup fs = new FormatSetup() {
			@Override
			public void setup(Format fmt) {
				fmt.setExpandEmptyElements(true);
			}
		};
		checkOutput(root, fs,  
				"<root><![CDATA[ ]]>        \n \n <!--Boo-->  \t   </root>", 
				"<root><!--Boo--></root>",
				"<root>\n  <!--Boo-->\n</root>",
				"<root>\n  <!--Boo-->\n</root>",
				"<root>\n  <!--Boo-->\n</root>");
	}

	@Test
	public void testOutputElementMixedMultiCDATA() {
		// this test has mixed content (text-type and not text type).
		// and, it has a multi-text-type at the end.
		Element root = new Element("root");
		root.addContent(new Comment("Boo"));
		root.addContent(new Text(" "));
		root.addContent(new CDATA("A"));
		FormatSetup fs = new FormatSetup() {
			@Override
			public void setup(Format fmt) {
				fmt.setExpandEmptyElements(true);
			}
		};
		checkOutput(root, fs,  
				"<root><!--Boo--> <![CDATA[A]]></root>", 
				"<root><!--Boo--><![CDATA[A]]></root>",
				"<root>\n  <!--Boo-->\n  <![CDATA[A]]>\n</root>",
				"<root>\n  <!--Boo-->\n  <![CDATA[A]]>\n</root>",
				"<root>\n  <!--Boo-->\n   <![CDATA[A]]>\n</root>");
	}

	@Test
	public void testOutputElementMixedMultiEntityRef() {
		// this test has mixed content (text-type and not text type).
		// and, it has a multi-text-type at the end.
		Element root = new Element("root");
		root.addContent(new Comment("Boo"));
		root.addContent(new Text(" "));
		root.addContent(new EntityRef("aer"));
		FormatSetup fs = new FormatSetup() {
			@Override
			public void setup(Format fmt) {
				fmt.setExpandEmptyElements(true);
			}
		};
		checkOutput(root, fs,  
				"<root><!--Boo--> &aer;</root>", 
				"<root><!--Boo-->&aer;</root>",
				"<root>\n  <!--Boo-->\n  &aer;\n</root>",
				"<root>\n  <!--Boo-->\n  &aer;\n</root>",
				"<root>\n  <!--Boo-->\n   &aer;\n</root>");
	}

	@Test
	public void testOutputElementMixedMultiText() {
		// this test has mixed content (text-type and not text type).
		// and, it has a multi-text-type at the end.
		Element root = new Element("root");
		root.addContent(new Comment("Boo"));
		root.addContent(new Text(" "));
		root.addContent(new Text("txt"));
		FormatSetup fs = new FormatSetup() {
			@Override
			public void setup(Format fmt) {
				fmt.setExpandEmptyElements(true);
			}
		};
		checkOutput(root, fs,  
				"<root><!--Boo--> txt</root>", 
				"<root><!--Boo-->txt</root>",
				"<root>\n  <!--Boo-->\n  txt\n</root>",
				"<root>\n  <!--Boo-->\n  txt\n</root>",
				"<root>\n  <!--Boo-->\n   txt\n</root>");
	}

	@Test
	public void testOutputElementMixedMultiZeroText() {
		// this test has mixed content (text-type and not text type).
		// and, it has a multi-text-type at the end.
		Element root = new Element("root");
		root.addContent(new Comment("Boo"));
		root.addContent(new Text(""));
		root.addContent(new Text(" "));
		root.addContent(new Text(""));
		root.addContent(new Text("txt"));
		root.addContent(new Text(""));
		FormatSetup fs = new FormatSetup() {
			@Override
			public void setup(Format fmt) {
				fmt.setExpandEmptyElements(true);
			}
		};
		checkOutput(root, fs,  
				"<root><!--Boo--> txt</root>", 
				"<root><!--Boo-->txt</root>",
				"<root>\n  <!--Boo-->\n  txt\n</root>",
				"<root>\n  <!--Boo-->\n  txt\n</root>",
				"<root>\n  <!--Boo-->\n   txt\n</root>");
	}

	@Test
	public void testOutputElementInterleavedEmptyText() {
		// this is to test issue #72
		// Compact format only prints first child.
		// and, it has a multi-text-type at the end.
		Element root = new Element("root");
		root.addContent(new Text(" "));
		root.addContent(new Comment("Boo"));
		root.addContent(new Text(" "));
		root.addContent(new Element("child"));
		root.addContent(new Text(" "));
		root.addContent(new ProcessingInstruction("pitarget"));
		root.addContent(new Text(" "));
		checkOutput(root,  
				"<root> <!--Boo--> <child /> <?pitarget?> </root>", 
				"<root><!--Boo--><child /><?pitarget?></root>", 
				"<root>\n  <!--Boo-->\n  <child />\n  <?pitarget?>\n</root>",
				"<root>\n  <!--Boo-->\n  <child />\n  <?pitarget?>\n</root>",
				"<root>\n  <!--Boo-->\n  <child />\n  <?pitarget?>\n</root>");
	}

	@Test
	public void testOutputElementMultiEntityLeftRight() {
		Element root = new Element("root");
		root.addContent(new EntityRef("erl"));
		root.addContent(new Text(" "));
		root.addContent(new Text("    "));
		root.addContent(new EntityRef("err"));
		checkOutput(root,
				"<root>&erl;     &err;</root>", 
				"<root>&erl; &err;</root>",
				"<root>&erl;     &err;</root>",
				"<root>&erl;     &err;</root>",
				"<root>&erl;     &err;</root>");
	}

	@Test
	public void testOutputElementMultiTrimLeftRight() {
		Element root = new Element("root");
		root.addContent(new Text(" tl "));
		root.addContent(new Text(" mid "));
		root.addContent(new Text(" tr "));
		checkOutput(root,
				"<root> tl  mid  tr </root>", 
				"<root>tl mid tr</root>",
				"<root>tl  mid  tr</root>",
				"<root>tl  mid  tr</root>",
				"<root> tl  mid  tr </root>");
	}

	@Test
	public void testOutputElementMultiCDATALeftRight() {
		Element root = new Element("root");
		root.addContent(new CDATA(" tl "));
		root.addContent(new Text(" mid "));
		root.addContent(new CDATA(" tr "));
		checkOutput(root,
				"<root><![CDATA[ tl ]]> mid <![CDATA[ tr ]]></root>", 
				"<root><![CDATA[tl]]> mid <![CDATA[tr]]></root>",
				"<root><![CDATA[tl ]]> mid <![CDATA[ tr]]></root>",
				"<root><![CDATA[tl ]]> mid <![CDATA[ tr]]></root>",
				"<root><![CDATA[ tl ]]> mid <![CDATA[ tr ]]></root>");
	}
	
	
	
	@Test
	public void testOutputElementNamespaces() {
		String txt = "<ns:root xmlns:ns=\"myns\" xmlns:ans=\"attributens\" xmlns:two=\"two\" ans:att=\"val\"/>";
		Element emt = new Element("root", Namespace.getNamespace("ns", "myns"));
		Namespace ans = Namespace.getNamespace("ans", "attributens");
		emt.setAttribute(new Attribute("att", "val", ans));
		emt.addNamespaceDeclaration(Namespace.getNamespace("two", "two"));
		checkOutput(emt,
				txt, txt,txt, txt, txt);
	}

	@Test
	public void testOutputDocTypeSimple() {
		checkOutput(new DocType("root"), "<!DOCTYPE root>", "<!DOCTYPE root>", "<!DOCTYPE root>", 
				"<!DOCTYPE root>", "<!DOCTYPE root>");
	}

	@Test
	public void testOutputDocTypeInternalSubset() {
		String dec = "<!DOCTYPE root [\ninternal]>";
		DocType dt = new DocType("root");
		dt.setInternalSubset("internal");
		checkOutput(dt, dec, dec, dec, dec, dec);
	}

	@Test
	public void testOutputDocTypeSystem() {
		String dec = "<!DOCTYPE root SYSTEM \"systemID\">";
		checkOutput(new DocType("root", "systemID"), dec, dec, dec, dec, dec);
	}

	@Test
	public void testOutputDocTypePublic() {
		String dec = "<!DOCTYPE root PUBLIC \"publicID\">";
		checkOutput(new DocType("root", "publicID", null), dec, dec, dec, dec, dec);
	}

	@Test
	public void testOutputDocTypePublicSystem() {
		String dec = "<!DOCTYPE root PUBLIC \"publicID\" \"systemID\">";
		checkOutput(new DocType("root", "publicID", "systemID"), dec, dec, dec, dec, dec);
	}

	@Test
	public void testOutputDocumentSimple() {
		Document doc = new Document();
		doc.addContent(new Element("root"));
		String xmldec = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		String rtdec = "<root />";
		checkOutput(doc, 
				xmldec + "\n" + rtdec + "\n", 
				xmldec + "\n" + rtdec + "\n",
				xmldec + "\n" + rtdec + "\n",
				xmldec + "\n" + rtdec + "\n",
				xmldec + "\n" + rtdec + "\n");
	}

	@Test
	public void testOutputDocumentOmitEncoding() {
		Document doc = new Document();
		doc.addContent(new Element("root"));
		String xmldec = "<?xml version=\"1.0\"?>";
		FormatSetup setup = new FormatSetup() {
			@Override
			public void setup(Format fmt) {
				fmt.setOmitEncoding(true);
			}
		};
		String rtdec = "<root />";
		checkOutput(doc, setup,
				xmldec + "\n" + rtdec + "\n", 
				xmldec + "\n" + rtdec + "\n",
				xmldec + "\n" + rtdec + "\n",
				xmldec + "\n" + rtdec + "\n",
				xmldec + "\n" + rtdec + "\n");
	}

	@Test
	public void testOutputDocumentOmitDeclaration() {
		Document doc = new Document();
		doc.addContent(new Element("root"));
		FormatSetup setup = new FormatSetup() {
			@Override
			public void setup(Format fmt) {
				fmt.setOmitDeclaration(true);
			}
		};
		String rtdec = "<root />";
		checkOutput(doc, setup,
				rtdec + "\n", 
				rtdec + "\n",
				rtdec + "\n",
				rtdec + "\n",
				rtdec + "\n");
	}

	@Test
	public void testOutputDocumentFull() {
		DocType dt = new DocType("root");
		Comment comment = new Comment("comment");
		ProcessingInstruction pi = new ProcessingInstruction("jdomtest", "");
		Element root = new Element("root");
		Document doc = new Document();
		doc.addContent(dt);
		doc.addContent(comment);
		doc.addContent(pi);
		doc.addContent(root);
		String xmldec = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		String dtdec = "<!DOCTYPE root>";
		String commentdec = "<!--comment-->";
		String pidec = "<?jdomtest?>";
		String rtdec = "<root />";
		String lf = "\n";
		String dlf = usesrawxmlout ? "" : lf;
		checkOutput(doc, 
				xmldec + lf + dtdec + commentdec + pidec + rtdec + lf, 
				xmldec + lf + dtdec + commentdec + pidec + rtdec + lf,
				xmldec + lf + dtdec + dlf + commentdec + dlf + pidec  + dlf + rtdec + lf,
				xmldec + lf + dtdec + dlf + commentdec + dlf + pidec  + dlf + rtdec + lf,
				xmldec + lf + dtdec + dlf + commentdec + dlf + pidec  + dlf + rtdec + lf);
	}
	
	@Test
	public void testDeepNesting() {
		// need to get beyond 16 levels of XML.
		DocType dt = new DocType("root");
		Element root = new Element("root");
		Document doc = new Document();
		doc.addContent(dt);
		doc.addContent(root);
		String xmldec = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		String dtdec = "<!DOCTYPE root>";
		String lf = "\n";
		
		StringBuilder raw = new StringBuilder();
		raw.append(xmldec).append(lf).append(dtdec);
		StringBuilder pretty = new StringBuilder();
		pretty.append(xmldec).append(lf).append(dtdec);
		if (!usesrawxmlout) {
			// most test systems use the XMLOutputter in raw mode to output
			// the results of the conversion. In Raw mode the XMLOutputter will
			// not make pretty content outside of the root element (but it will
			// put the XMLDeclaration on it's own line).
			// so, in the cases where the actual pretty format is used, we add
			// this newline after the DocType...
			pretty.append(lf);
		}
		raw.append("<root>");
		pretty.append("<root>");
		pretty.append(lf);
		final int depth = 40;
		int cnt = depth;
		Parent parent = root;
		StringBuilder indent = new StringBuilder();
		while (--cnt > 0) {
			Element emt = new Element("emt");
			parent.getContent().add(emt);
			parent = emt;
			raw.append("<emt>");
			indent.append("  ");
			pretty.append(indent.toString());
			pretty.append("<emt>");
			pretty.append(lf);
		}
		
		parent.getContent().add(new Element("bottom"));
		raw.append("<bottom />");
		pretty.append(indent.toString());
		pretty.append("  <bottom />");
		pretty.append(lf);
		
		cnt = depth;
		while (--cnt > 0) {
			raw.append("</emt>");
			pretty.append(indent.toString());
			pretty.append("</emt>");
			indent.setLength(indent.length() - 2);
			pretty.append(lf);
		}
		raw.append("</root>");
		raw.append(lf);
		pretty.append("</root>");
		pretty.append(lf);
		
		checkOutput(doc, raw.toString(), raw.toString(), pretty.toString(), pretty.toString(), pretty.toString()); 
	}
	
	@Test
	public void testOutputElementContent() {
		Element root = new Element("root");
		root.addContent(new Element("child"));
		checkOutput(root, "outputElementContent", Element.class, null, "<child />", "<child />", "<child />", "<child />", "<child />");
	}

	@Test
	public void testOutputList() {
		List<Object> c = new ArrayList<Object>();
		c.add(new Element("root"));
		checkOutput(c, "output", List.class, null, "<root />", "<root />", "<root />", "<root />", "<root />");
	}

	
	@Test
	public void testOutputEscapedMixedMultiText() {
		// this test has mixed content (text-type and not text type).
		// and, it has a multi-text-type at the end.
		Element root = new Element("root");
		root.addContent(new Comment("Boo"));
		root.addContent(new Text(" xx "));
		root.addContent(new Text("<emb>"));
		root.addContent(new Text(" xx "));
		FormatSetup fs = new FormatSetup() {
			@Override
			public void setup(Format fmt) {
				fmt.setExpandEmptyElements(true);
			}
		};
		checkOutput(root, fs,  
				"<root><!--Boo--> xx &lt;emb&gt; xx </root>", 
				"<root><!--Boo-->xx &lt;emb&gt; xx</root>",
				"<root>\n  <!--Boo-->\n  xx &lt;emb&gt; xx\n</root>",
				"<root>\n  <!--Boo-->\n  xx &lt;emb&gt; xx\n</root>",
				"<root>\n  <!--Boo-->\n   xx &lt;emb&gt; xx \n</root>");
	}

	@Test
	public void testOutputLotsOfMixedMultiText() {
		// this test has many text content members.
		Element root = new Element("root");
		StringBuilder sb = new StringBuilder();
		sb.append("<root>i");
		root.addContent("i");
		for (int i = 0; i < 100; i++) {
			sb.append("&ent;");
			sb.append(i);
			root.addContent(new EntityRef("ent"));
			root.addContent("" + i);
		}
		sb.append("</root>");
		FormatSetup fs = new FormatSetup() {
			@Override
			public void setup(Format fmt) {
				fmt.setExpandEmptyElements(true);
			}
		};
		final String expect = sb.toString();
		checkOutput(root, fs, expect, expect, expect, expect, expect); 
	}

	
	
	protected void checkOutput(Object content, String raw, String compact, String pretty, String tso, String trimfw) {
		Class<?> clazz = content.getClass();
		checkOutput(content, "output", clazz, null, raw, compact, pretty, tso, trimfw);
	}
	
	protected void checkOutput(Object content, FormatSetup setup, String raw, String compact, String pretty, String tso, String trimfw) {
		Class<?> clazz = content.getClass();
		checkOutput(content, "output", clazz, setup, raw, compact, pretty, tso, trimfw);
	}
	
	/**
	 * The following method will run the output data through each of the three base
	 * formatters, raw, compact, and pretty. It will also run each of those
	 * formatters as the outputString(content), output(content, OutputStream)
	 * and output(content, Writer).
	 * 
	 * The expectation is that the results of the three output forms (String,
	 * OutputStream, and Writer) will be identical, and that it will match
	 * the expected value for the appropriate formatter.
	 * 
	 * @param content The content to output
	 * @param methodprefix What the methods are called
	 * @param clazz The class used as the parameter for the methods.
	 * @param setup A callback mechanism to modify the formatters
	 * @param raw  What we expect the content to look like with the RAW format
	 * @param compact What we expect the content to look like with the COMPACT format
	 * @param pretty What we expect the content to look like with the PRETTY format
	 * @param trimfw What we expect the content to look like with the TRIM_FULL_WHITE format
	 */
	protected void checkOutput(Object content, String methodprefix, Class<?> clazz, 
			FormatSetup setup, String raw, String compact, String pretty, String tso, String trimfw) {
		Method meth = getMyMethod(methodprefix + "String", Format.class, clazz);
		
		if (meth == null) {
			return;
		}
		
		String[] descn   = new String[] {"Raw", "Compact", "Pretty", "PrettySpecifiedOnly", "TrimFullWhite"};
		Format ftrimfw = Format.getPrettyFormat();
		ftrimfw.setTextMode(TextMode.TRIM_FULL_WHITE);
		Format fattspec = Format.getPrettyFormat();
		fattspec.setSpecifiedAttributesOnly(true);
		Format[] formats = new Format[] {
				getFormat(setup, Format.getRawFormat()), 
				getFormat(setup, Format.getCompactFormat()),
				getFormat(setup, Format.getPrettyFormat()),
				getFormat(setup, fattspec),
				getFormat(setup, ftrimfw)};
		String[] result  = new String[] {raw, compact, pretty, tso, trimfw};
		
		for (int i = 0; i < 5; i++) {
			
			String mstring;
			try {
				mstring = (String) meth.invoke(this, formats[i], content);
			} catch (Exception e) {
				e.printStackTrace();
				throw new IllegalStateException(e);
			}
			String msg = "outputString Format " + descn[i];
			assertEquals(msg, expect(result[i]), mstring);
		}
	}
	
	protected Format getFormat(FormatSetup setup, Format input) {
		if (setup == null) {
			input.setLineSeparator("\n");
			return input;
		}
		input.setLineSeparator("\n");
		setup.setup(input);
		return input;
	}
	
	private Method getMyMethod(String name, Class<?>...classes) {
		try {
			return this.getClass().getMethod(name, classes);
		} catch (Exception e) {
			// ignore.
			System.out.println("Can't find " + name + " on " + this.getClass().getName() + ": " + e.getMessage());
		}
		return null;
	}

}
