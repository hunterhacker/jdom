package org.jdom2.test.cases.output;

import static org.jdom2.test.util.UnitTestUtil.failException;
import static org.jdom2.test.util.UnitTestUtil.normalizeAttributes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.util.XMLEventConsumer;
import javax.xml.transform.Result;

import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.DefaultHandler2;

import org.jdom2.Attribute;
import org.jdom2.AttributeType;
import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.Content;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.EntityRef;
import org.jdom2.IllegalDataException;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.Parent;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;
import org.jdom2.UncheckedJDOMFactory;
import org.jdom2.input.DefaultStAXFilter;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.SAXHandler;
import org.jdom2.input.StAXStreamBuilder;
import org.jdom2.output.AbstractStAXEventProcessor;
import org.jdom2.output.Format;
import org.jdom2.output.Format.TextMode;
import org.jdom2.output.SAXOutputter;
import org.jdom2.output.StAXEventOutputter;
import org.jdom2.output.StAXEventProcessor;
import org.jdom2.output.XMLOutputter;

@SuppressWarnings("javadoc")
public final class TestStAXEventOutputter {

	private final static XMLOutputFactory soutfactory = XMLOutputFactory.newInstance();
	private final static XMLInputFactory sinfactory = XMLInputFactory.newInstance();
	
	private static final class EventStore implements XMLEventConsumer {
		private final ArrayList<XMLEvent> store = new ArrayList<XMLEvent>();
		private final String encoding;
		
		EventStore(String enc) {
			encoding = enc;
		}
		
		@Override
		public void add(XMLEvent event) throws XMLStreamException {
			store.add(event);
		}
		
		@Override
		public String toString() {
			ByteArrayOutputStream sw = new ByteArrayOutputStream();
			try {
				XMLEventWriter xew = soutfactory.createXMLEventWriter(sw, encoding);
				for (XMLEvent x : store) {
					xew.add(x);
				}
				xew.flush();
				xew.close();
				return new String(sw.toByteArray());
			} catch (XMLStreamException e) {
				throw new IllegalStateException("Can't get toString...", e);
			}
			
		}
	}

    @Test
    public void test_HighSurrogatePair() throws XMLStreamException, IOException, JDOMException {
      SAXBuilder builder = new SAXBuilder();
      builder.setExpandEntities(true);
      Document doc = builder.build(new StringReader("<?xml version=\"1.0\"?><root>&#x10000; &#x10000;</root>"));
      
      Format format = Format.getCompactFormat().setEncoding("ISO-8859-1");
      StAXEventOutputter outputter = new StAXEventOutputter(format);
      EventStore es = new EventStore("ISO-8859-1");
      outputter.output(doc, es);
      String xml = es.toString();
      assertEquals("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + 
                   "<root>&#xd800;&#xdc00; &#xd800;&#xdc00;</root>", xml);
    }

    @Test
    public void test_HighSurrogatePairDecimal() throws JDOMException, IOException, XMLStreamException {
      SAXBuilder builder = new SAXBuilder();
      builder.setExpandEntities(true);
      Document doc = builder.build(new StringReader("<?xml version=\"1.0\"?><root>&#x10000; &#65536;</root>"));
      Format format = Format.getCompactFormat().setEncoding("ISO-8859-1");
      StAXEventOutputter outputter = new StAXEventOutputter(format);
      EventStore es = new EventStore("ISO-8859-1");
      outputter.output(doc, es);
      String xml = es.toString();
      assertEquals("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + 
                   "<root>&#xd800;&#xdc00; &#xd800;&#xdc00;</root>", xml);
    }

    @Test
    public void test_HighSurrogateAttPair() throws JDOMException, IOException, XMLStreamException {
      SAXBuilder builder = new SAXBuilder();
      builder.setExpandEntities(true);
      Document doc = builder.build(new StringReader("<?xml version=\"1.0\"?><root att=\"&#x10000; &#x10000;\" />"));
      Format format = Format.getCompactFormat().setEncoding("ISO-8859-1");
      StAXEventOutputter outputter = new StAXEventOutputter(format);
      EventStore es = new EventStore("ISO-8859-1");
      outputter.output(doc, es);
      String xml = es.toString();
      assertEquals("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
                   "<root att=\"&#xd800;&#xdc00; &#xd800;&#xdc00;\"></root>", xml);
    }

    @Test
    public void test_HighSurrogateAttPairDecimal() throws JDOMException, IOException, XMLStreamException {
      SAXBuilder builder = new SAXBuilder();
      builder.setExpandEntities(true);
      Document doc = builder.build(new StringReader("<?xml version=\"1.0\"?><root att=\"&#x10000; &#65536;\" />"));
      Format format = Format.getCompactFormat().setEncoding("ISO-8859-1");
      StAXEventOutputter outputter = new StAXEventOutputter(format);
      EventStore es = new EventStore("ISO-8859-1");
      outputter.output(doc, es);
      String xml = es.toString();
      assertEquals("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + 
                   "<root att=\"&#xd800;&#xdc00; &#xd800;&#xdc00;\"></root>", xml);
    }

    // Construct a raw surrogate pair character and confirm it outputs hex escaped
    @Test
    public void test_RawSurrogatePair() throws JDOMException, IOException, XMLStreamException {
      SAXBuilder builder = new SAXBuilder();
      builder.setExpandEntities(true);
      Document doc = builder.build(new StringReader("<?xml version=\"1.0\"?><root>\uD800\uDC00</root>"));
      Format format = Format.getCompactFormat().setEncoding("ISO-8859-1");
      StAXEventOutputter outputter = new StAXEventOutputter(format);
      EventStore es = new EventStore("ISO-8859-1");
      outputter.output(doc, es);
      String xml = es.toString();
      assertEquals("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
                   "<root>&#xd800;&#xdc00;</root>", xml);
    }

    // Construct a raw surrogate pair character and confirm it outputs hex escaped, when UTF-8 too
    @Test
	@Ignore
	// TODO
    public void test_RawSurrogatePairUTF8() throws JDOMException, IOException, XMLStreamException {
      SAXBuilder builder = new SAXBuilder();
      builder.setExpandEntities(true);
      Document doc = builder.build(new StringReader("<?xml version=\"1.0\"?><root>\uD800\uDC00</root>"));
      Format format = Format.getCompactFormat().setEncoding("UTF-8");
      StAXEventOutputter outputter = new StAXEventOutputter(format);
      EventStore es = new EventStore("UTF-8");
      outputter.output(doc, es);
      String xml = es.toString();
      assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                   "<root>\uD800\uDC00</root>", xml);
    }

    // Construct illegal XML and check if the parser notices
    @Test
    public void test_ErrorSurrogatePair() throws JDOMException, IOException {
      SAXBuilder builder = new SAXBuilder();
      builder.setExpandEntities(true);
      Document doc = builder.build(new StringReader("<?xml version=\"1.0\"?><root></root>"));
      try {
        doc.getRootElement().setText("\uD800\uDBFF");
        fail("Illegal surrogate pair should have thrown an exception");
      }
      catch (IllegalDataException e) {
    	  // do nothing
      } catch (Exception e) {
    	  fail ("Unexpected exception " + e.getClass());
      }
    }

    // Manually construct illegal XML and make sure the outputter notices
    @Test
	@Ignore
	// TODO
    public void test_ErrorSurrogatePairOutput() throws JDOMException, IOException {
      SAXBuilder builder = new SAXBuilder();
      builder.setExpandEntities(true);
      Document doc = builder.build(new StringReader("<?xml version=\"1.0\"?><root></root>"));
      Text t = new UncheckedJDOMFactory().text("\uD800\uDBFF");
      doc.getRootElement().setContent(t);
      Format format = Format.getCompactFormat().setEncoding("ISO-8859-1");
      StAXEventOutputter outputter = new StAXEventOutputter(format);
      try {
          EventStore es = new EventStore("ISO-8859-1");
        outputter.output(doc, es);
        fail("Illegal surrogate pair output should have thrown an exception");
      }
      catch (XMLStreamException e) {
    	  // do nothing
      } catch (Exception e) {
    	  fail ("Unexpected exception " + e.getClass());
      }
    }
    
    
	@Test
	public void testXMLOutputter() {
		StAXEventOutputter out = new StAXEventOutputter();
		TestFormat.checkEquals(out.getFormat(), Format.getRawFormat());
	}


	@Test
	public void testXMLOutputterFormat() {
		Format mine = Format.getCompactFormat();
		mine.setEncoding("US-ASCII");
		StAXEventOutputter out = new StAXEventOutputter(mine);
		TestFormat.checkEquals(mine, out.getFormat());
	}

//	@Test
//	public void testXMLOutputterXMLOutputter() {
//		Format mine = Format.getCompactFormat();
//		StAXEventProcessor xoutp = new StAXEventOutputter().getStAXStream();
//		mine.setEncoding("US-ASCII");
//		// double-construct it.
//		StAXEventOutputter out = new StAXEventOutputter();
//		TestFormat.checkEquals(mine, out.getFormat());
//		assertTrue(xoutp == out.getXMLOutputProcessor());
//	}

	@Test
	public void testXMLOutputterXMLOutputProcessor() {
		StAXEventProcessor xoutp = new AbstractStAXEventProcessor() {
			// nothing;
		};
		// double-constrcut it.
		StAXEventOutputter out = new StAXEventOutputter(xoutp);
		TestFormat.checkEquals(Format.getRawFormat(), out.getFormat());
		assertTrue(xoutp == out.getStAXStream());
	}

	@Test
	public void testFormat() {
		Format mine = Format.getCompactFormat();
		mine.setEncoding("US-ASCII");
		// double-constcut it.
		StAXEventOutputter out = new StAXEventOutputter();
		TestFormat.checkEquals(Format.getRawFormat(), out.getFormat());
		out.setFormat(mine);
		TestFormat.checkEquals(mine, out.getFormat());
	}

	@Test
	public void testXMLOutputProcessor() {
		StAXEventProcessor xoutp = new AbstractStAXEventProcessor() {
			// nothing;
		};
		// double-constcut it.
		StAXEventOutputter out = new StAXEventOutputter();
		StAXEventProcessor xop = out.getStAXStream();
		out.setStAXEventProcessor(xoutp);
		assertTrue(xoutp != xop);
		assertTrue(xoutp == out.getStAXStream());
	}

	@Test
	public void testOutputText() {
		checkOutput(new Text(" hello  there  "), " hello  there  ", "hello there", "hello  there", " hello  there  ");
	}

	@Test
	public void testOutputCDATA() {
		String indata = "   hello   there  bozo !   ";
		String rawcdata   = "<![CDATA[   hello   there  bozo !   ]]>";
		String compdata   = "<![CDATA[hello there bozo !]]>";
		String prettydata = "<![CDATA[hello   there  bozo !]]>";
		String trimdata   = "<![CDATA[   hello   there  bozo !   ]]>";
		
		checkOutput(new CDATA(indata), rawcdata, compdata, prettydata, trimdata);
	}

	@Test
	public void testOutputComment() {
		String incomment = "   hello   there  bozo !   ";
		String outcomment = "<!--" + incomment + "-->";
		checkOutput(new Comment(incomment), outcomment, outcomment, outcomment, outcomment);
	}

	@Test
	public void testOutputProcessingInstructionSimple() {
		ProcessingInstruction inpi = new ProcessingInstruction("jdomtest", "");
		String outpi = "<?jdomtest?>";
		checkOutput(inpi, outpi, outpi, outpi, outpi);
	}

	@Test
	public void testOutputProcessingInstructionData() {
		String pi = "  hello   there  ";
		ProcessingInstruction inpi = new ProcessingInstruction("jdomtest", pi);
		String outpi = "<?jdomtest " + pi + "?>";
		checkOutput(inpi, outpi, outpi, outpi, outpi);
	}

	@Test
	public void testOutputEntityRef() {
		checkOutput(new EntityRef("name", "publicID", "systemID"),
				"&name;", "&name;", "&name;", "&name;");
	}

	@Test
	public void testOutputElementSimple() {
		String txt = "<root/>";
		checkOutput(new Element("root"), txt, txt, txt, txt);
	}

	@Test
	public void testOutputElementAttribute() {
		String txt = "<root att=\"val\"/>";
		checkOutput(new Element("root").setAttribute("att", "val"), txt, txt, txt, txt);
	}

	@Test
	public void testOutputElementCDATA() {
		String txt = "<root><![CDATA[xx]]></root>";
		Element root = new Element("root");
		root.addContent(new CDATA("xx"));
		checkOutput(root, txt, txt, txt, txt);
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
		checkOutput(new Element("root"), setup, txt, txt, txt, txt);
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
		checkOutput(root, txt, txt, txt, txt);
	}
	
	@Test
	@Ignore
	//TODO
	public void testOutputElementIgnoreTrAXEscapingPIs() {
		Element root = new Element("root");
		root.addContent(new Text("&"));
		root.addContent(new ProcessingInstruction(Result.PI_DISABLE_OUTPUT_ESCAPING, ""));
		root.addContent(new Text(" && "));
		root.addContent(new ProcessingInstruction(Result.PI_ENABLE_OUTPUT_ESCAPING, ""));
		root.addContent(new Text("&"));
		String expect = "<root>&amp; && &amp;</root>";
		String excompact = "<root>&amp;&&&amp;</root>";
		String expretty = "<root>\n  &amp;\n  \n  &&\n  \n  &amp;\n</root>";
		String extfw = "<root>\n  &amp;\n  \n   && \n  \n  &amp;\n</root>";
		checkOutput(root,
				expect, 
				excompact, 
				expretty, 
				extfw);
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
				"<root/>",
				"<root/>",
				"<root/>");
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
				fmt.setLineSeparator("\n");
			}
		};
		checkOutput(root, fs,  
				"<root><!--Boo--> txt</root>", 
				"<root><!--Boo-->txt</root>",
				"<root>\n  <!--Boo-->\n  txt\n</root>",
				"<root>\n  <!--Boo-->\n   txt\n</root>");
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
				"<root><![CDATA[ tl ]]> mid <![CDATA[ tr ]]></root>");
	}

	@Test
	public void testTrimFullWhite() throws XMLStreamException {
		// See issue #31.
		// https://github.com/hunterhacker/jdom/issues/31
		// This tests should pass when issue 31 is resolved.
		Element root = new Element("root");
		root.addContent(new Text(" "));
		root.addContent(new Text("x"));
		root.addContent(new Text(" "));
		Format mf = Format.getRawFormat();
		mf.setTextMode(TextMode.TRIM_FULL_WHITE);
		StAXEventOutputter xout = new StAXEventOutputter(mf);
		EventStore es = new EventStore("UTF-8");
		xout.output(root, es);
		assertEquals("<root> x </root>", es.toString());
	}

	@Test
	public void testOutputElementNamespaces() {
		String txt = "<ns:root xmlns:ns=\"myns\" xmlns:ans=\"attributens\" xmlns:two=\"two\" ans:att=\"val\"/>";
		Element emt = new Element("root", Namespace.getNamespace("ns", "myns"));
		Namespace ans = Namespace.getNamespace("ans", "attributens");
		emt.setAttribute(new Attribute("att", "val", ans));
		emt.addNamespaceDeclaration(Namespace.getNamespace("two", "two"));
		checkOutput(emt,
				txt, txt, txt, txt);
	}

	@Test
	public void testOutputDocTypeSimple() {
		checkOutput(new DocType("root"), "<!DOCTYPE root>", "<!DOCTYPE root>", "<!DOCTYPE root>", "<!DOCTYPE root>");
	}

	@Test
	public void testOutputDocTypeInternalSubset() {
		String dec = "<!DOCTYPE root [\ninternal]>";
		DocType dt = new DocType("root");
		dt.setInternalSubset("internal");
		checkOutput(dt, dec, dec, dec, dec);
	}

	@Test
	public void testOutputDocTypeSystem() {
		String dec = "<!DOCTYPE root SYSTEM \"systemID\">";
		checkOutput(new DocType("root", "systemID"), dec, dec, dec, dec);
	}

	@Test
	public void testOutputDocTypePublic() {
		String dec = "<!DOCTYPE root PUBLIC \"publicID\">";
		checkOutput(new DocType("root", "publicID", null), dec, dec, dec, dec);
	}

	@Test
	public void testOutputDocTypePublicSystem() {
		String dec = "<!DOCTYPE root PUBLIC \"publicID\" \"systemID\">";
		checkOutput(new DocType("root", "publicID", "systemID"), dec, dec, dec, dec);
	}

	@Test
	public void testOutputDocumentSimple() {
		Document doc = new Document();
		doc.addContent(new Element("root"));
		String xmldec = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		String rtdec = "<root/>";
		checkOutput(doc, 
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
		String rtdec = "<root/>";
		checkOutput(doc, setup,
				xmldec + "\n" + rtdec + "\n", 
				xmldec + "\n" + rtdec + "\n",
				xmldec + "\n" + rtdec + "\n",
				xmldec + "\n" + rtdec + "\n");
	}

	@Test
	@Ignore
	public void testOutputDocumentOmitDeclaration() {
		Document doc = new Document();
		doc.addContent(new Element("root"));
		FormatSetup setup = new FormatSetup() {
			@Override
			public void setup(Format fmt) {
				fmt.setOmitDeclaration(true);
			}
		};
		String rtdec = "<root/>";
		checkOutput(doc, setup,
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
		String rtdec = "<root/>";
		String lf = "\n";
		checkOutput(doc, 
				xmldec + lf + dtdec + lf + commentdec + pidec + rtdec + lf, 
				xmldec + lf + dtdec + lf + commentdec + pidec + rtdec + lf,
				xmldec + lf + dtdec + lf + lf + commentdec + lf + pidec  + lf + rtdec + lf,
				xmldec + lf + dtdec + lf + lf + commentdec + lf + pidec  + lf + rtdec + lf);
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
		
		String base = xmldec + lf + dtdec + lf;
		StringBuilder raw = new StringBuilder(base);
		StringBuilder pretty = new StringBuilder(base);
		raw.append("<root>");
		pretty.append(lf);
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
		raw.append("<bottom/>");
		pretty.append(indent.toString());
		pretty.append("  <bottom/>");
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
		
		checkOutput(doc, raw.toString(), raw.toString(), pretty.toString(), pretty.toString()); 
	}
	
	@Test
	public void testOutputElementContent() {
		Element root = new Element("root");
		root.addContent(new Element("child"));
		checkOutput(root, "outputElementContent", Element.class, null, "<child/>", "<child/>", "<child/>\n", "<child/>\n");
	}

	@Test
	public void testOutputList() {
		List<Object> c = new ArrayList<Object>();
		c.add(new Element("root"));
		checkOutput(c, "output", List.class, null, "<root/>", "<root/>", "<root/>\n", "<root/>\n");
	}

	@Test
	public void testClone() {
		StAXEventOutputter xo = new StAXEventOutputter();
		assertTrue(xo != xo.clone());
	}

	@Test
	public void testToString() {
		Format fmt = Format.getCompactFormat();
		fmt.setLineSeparator("\n\t ");
		StAXEventOutputter out = new StAXEventOutputter(fmt);
		assertNotNull(out.toString());
	}
	
	private interface FormatSetup {
		public void setup(Format fmt);
	}
	
	private void checkOutput(Object content, String raw, String compact, String pretty, String trimfw) {
		Class<?> clazz = content.getClass();
		checkOutput(content, "output", clazz, null, raw, compact, pretty, trimfw);
	}
	private void checkOutput(Object content, FormatSetup setup, String raw, String compact, String pretty, String trimfw) {
		Class<?> clazz = content.getClass();
		checkOutput(content, "output", clazz, setup, raw, compact, pretty, trimfw);
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
	private void checkOutput(Object content, String methodprefix, Class<?> clazz, 
			FormatSetup setup, String raw, String compact, String pretty, String trimfw) {
		Method mstream = getMethod(methodprefix, clazz, XMLStreamWriter.class);
		
		String[] descn   = new String[] {"Raw", "Compact", "Pretty", "TrimFullWhite"};
		Format ftrimfw = Format.getPrettyFormat();
		ftrimfw.setTextMode(TextMode.TRIM_FULL_WHITE);
		Format[] formats = new Format[] {
				getFormat(setup, Format.getRawFormat()), 
				getFormat(setup, Format.getCompactFormat()),
				getFormat(setup, Format.getPrettyFormat()),
				getFormat(setup, ftrimfw)};
		String[] result  = new String[] {raw, compact, pretty, trimfw};
		
		for (int i = 0; i < 4; i++) {
			StAXEventOutputter out = new StAXEventOutputter(formats[i]);
			CharArrayWriter caw = new CharArrayWriter(result[i].length() + 2);
			try {
				if (mstream != null) {
					XMLStreamWriter xsw = soutfactory.createXMLStreamWriter(caw);
					mstream.invoke(out, content, xsw);
					xsw.close();
					String rstream = caw.toString();
					assertEquals("output OutputStream Format " + descn[i], result[i], rstream);
				}
				
			} catch (Exception e) {
				//e.printStackTrace();
				AssertionError ae = new AssertionError("Failed to process " + descn[i] + " on content " + clazz + ": " + e.getMessage());
				ae.initCause(e);
				throw ae;
			}
		}
	}
	
	private Format getFormat(FormatSetup setup, Format input) {
		input.setLineSeparator("\n");
		if (setup == null) {
			return input;
		}
		setup.setup(input);
		return input;
	}
	
	private Method getMethod(String name, Class<?>...classes) {
		try {
			return StAXEventOutputter.class.getMethod(name, classes);
		} catch (Exception e) {
			// ignore.
			System.out.println("Can't find " + name + ": " + e.getMessage());
		}
		return null;
	}
	

	
	
	
	
	/*
	 * The following are borrowed from the TestSAXOutputter
	 * The effect is that we compare the StAX string output with the re-parsed
	 * value of the input.
	 */
	
    private void roundTripDocument(Document doc) {
    	StAXEventOutputter xout = new StAXEventOutputter(Format.getRawFormat());
    	// create a String representation of the input.
    	if (doc.hasRootElement()) {
    		normalizeAttributes(doc.getRootElement());
    	}
    	
    	try {
    		EventStore xsw = new EventStore("UTF-8");
        	xout.output(doc, xsw);
        	String expect = xsw.toString();
        	
    		// convert the input to a SAX Stream

        	StAXStreamBuilder sbuilder = new StAXStreamBuilder();
        	
        	char[] chars = expect.toCharArray();
        	CharArrayReader car = new CharArrayReader(chars);
        	XMLStreamReader xsr = sinfactory.createXMLStreamReader(car);
        	
			Document backagain = sbuilder.build(xsr);
			xsr.close();
			
			// get a String representation of the round-trip.
	    	if (backagain.hasRootElement()) {
	    		normalizeAttributes(backagain.getRootElement());
	    	}
    		xsw = new EventStore("UTF-8");
			xout.output(backagain, xsw);
			String actual = xsw.toString();
			
	    	assertEquals(expect, actual);
		} catch (Exception e) {
			failException("Failed to round-trip the document with exception: " 
					+ e.getMessage(), e);
		}
    }
    
    private void roundTripElement(Element emt) {
    	
    	try {
        	StAXEventOutputter xout = new StAXEventOutputter(Format.getRawFormat());

    		EventStore xsw = new EventStore("UTF-8");
        	xout.output(emt, xsw);
        	String expect = xsw.toString();
        	
        	StAXStreamBuilder sbuilder = new StAXStreamBuilder();
        	
        	XMLStreamReader xsr = sinfactory.createXMLStreamReader(new StringReader(expect));
        	assertTrue(xsr.getEventType() == XMLStreamConstants.START_DOCUMENT);
        	assertTrue(xsr.hasNext());
        	xsr.next();
        	
			Element backagain = (Element)sbuilder.fragment(xsr);

			// convert the input to a SAX Stream

    		xsw = new EventStore("UTF-8");
        	xout.output(backagain, xsw);
        	
        	String actual = xsw.toString();
        	assertEquals(expect, actual);
		} catch (Exception e) {
			failException("Failed to round-trip the document with exception: " 
					+ e.getMessage(), e);
		}
    }
    
    private void roundTripFragment(List<Content> content) {
    	try {
        	StAXEventOutputter xout = new StAXEventOutputter(Format.getRawFormat());

    		EventStore xsw = new EventStore("UTF-8");
        	xout.output(content, xsw);
        	String expect = xsw.toString();
        	
        	StAXStreamBuilder sbuilder = new StAXStreamBuilder();
        	
        	XMLStreamReader xsr = sinfactory.createXMLStreamReader(new StringReader(expect));
//        	assertTrue(xsr.getEventType() == XMLStreamConstants.START_DOCUMENT);
//        	assertTrue(xsr.hasNext());
//        	xsr.next();
        	
			List<Content> backagain = sbuilder.buildFragments(xsr, new DefaultStAXFilter());

			// convert the input to a SAX Stream

    		xsw = new EventStore("UTF-8");
        	xout.output(backagain, xsw);
        	
        	String actual = xsw.toString();
        	assertEquals(expect, actual);
		} catch (Exception e) {
			failException("Failed to round-trip the document with exception: " 
					+ e.getMessage(), e);
		}
    	
    }
    
    private void roundTripFragment(Content content) {
    	try {
        	StAXEventOutputter xout = new StAXEventOutputter(Format.getRawFormat());

    		EventStore xsw = new EventStore("UTF-8");
        	switch(content.getCType()) {
        		case CDATA :
                	xout.output((CDATA)content, xsw);
                	break;
        		case Text:
                	xout.output((Text)content, xsw);
                	break;
        		case Comment:
                	xout.output((Comment)content, xsw);
                	break;
        		case DocType:
                	xout.output((DocType)content, xsw);
                	break;
        		case Element:
                	xout.output((Element)content, xsw);
                	break;
        		case EntityRef:
                	xout.output((EntityRef)content, xsw);
                	break;
        		case ProcessingInstruction:
                	xout.output((ProcessingInstruction)content, xsw);
                	break;
        		default:
        			throw new IllegalStateException(content.getCType().toString());
        	}
        	String expect = xsw.toString();
        	
        	StAXStreamBuilder sbuilder = new StAXStreamBuilder();
        	
			Content backagain = sbuilder.fragment(
					sinfactory.createXMLStreamReader(new StringReader(expect)));

			// convert the input to a SAX Stream

    		xsw = new EventStore("UTF-8");
        	switch(content.getCType()) {
        		case CDATA :
                	xout.output((CDATA)backagain, xsw);
                	break;
        		case Text:
                	xout.output((Text)backagain, xsw);
                	break;
        		case Comment:
                	xout.output((Comment)backagain, xsw);
                	break;
        		case DocType:
                	xout.output((DocType)backagain, xsw);
                	break;
        		case Element:
                	xout.output((Element)backagain, xsw);
                	break;
        		case EntityRef:
                	xout.output((EntityRef)backagain, xsw);
                	break;
        		case ProcessingInstruction:
                	xout.output((ProcessingInstruction)backagain, xsw);
                	break;
        		default:
        			throw new IllegalStateException(backagain.getCType().toString());
        	}
        	
        	String actual = xsw.toString();
        	assertEquals(expect, actual);
		} catch (Exception e) {
			failException("Failed to round-trip the document with exception: " 
					+ e.getMessage(), e);
		}
    }
    
	@Test
	public void testRTOutputDocumentSimple() {
		Document doc = new Document(new Element("root"));
		roundTripDocument(doc);
	}

    @Test
    // TODO
	@Ignore
    public void testRTOutputDocumentFull() {
    	Document doc = new Document();
    	DocType dt = new DocType("root");
    	dt.setInternalSubset(" ");
    	doc.addContent(dt);
    	doc.addContent(new Comment("This is a document"));
    	doc.addContent(new ProcessingInstruction("jdomtest", ""));
    	Element e = new Element("root");
    	e.addContent(new EntityRef("ref"));
    	doc.addContent(e);
    	roundTripDocument(doc);
    }
    
    @Test
    public void testOutputDocumentRootAttNS() {
    	Document doc = new Document();
    	Element e = new Element("root");
    	e.setAttribute(new Attribute("att", "val", Namespace.getNamespace("ans", "mynamespace")));
    	doc.addContent(e);
    	roundTripDocument(doc);
    }
    
    @Test
    public void testOutputDocumentFullNoLexical() throws JDOMException {
    	Document doc = new Document();
    	doc.addContent(new ProcessingInstruction("jdomtest", ""));
    	Element e = new Element("root");
    	doc.addContent(e);
    	e.addContent(new Text("text"));
    	e.addContent(new EntityRef("ref"));
    	
    	// the Lexical handler is what helps track comments,
    	// and identifies CDATA sections.
    	// as a result the output of CDATA structures appears as plain text.
    	// and comments are dropped....
    	e.addContent(new Text("cdata"));
    	String expect = new XMLOutputter().outputString(doc);
    	e.removeContent(2);
    	e.addContent(new CDATA("cdata"));
    	e.addContent(new Comment("This is a document"));
    	
    	SAXHandler handler = new SAXHandler();
    	SAXOutputter saxout = new SAXOutputter(handler, handler, handler, handler);
    	saxout.output(doc);
    	
    	Document act = handler.getDocument();
    	String actual = new XMLOutputter().outputString(act);
    	
    	assertEquals(expect, actual);
    	
    }
    
    @Test
    public void testOutputDocumentNoErrorHandler() {
    	DefaultHandler2 handler = new DefaultHandler2() {
    		@Override
    		public void error(SAXParseException arg0) throws SAXException {
    			throw new SAXException ("foo!");
    		}
    	};
    	
    	SAXOutputter saxout = new SAXOutputter(handler);
    	
    	try {
			saxout.outputFragment(new DocType("rootemt"));
			fail("SHould have exception!");
		} catch (JDOMException e) {
			assertTrue(e.getMessage().indexOf("rootemt") >= 0);
		}
    	
    	saxout.setErrorHandler(handler);
    	
    	try {
			saxout.outputFragment(new DocType("rootemt"));
			fail("SHould have exception!");
		} catch (JDOMException e) {
			assertTrue(e.getMessage().endsWith("foo!"));
		}
    	
    }
    
    @Test
    public void testOutputDocumentAttributes() {
    	Element emt = new Element("root");
    	emt.setAttribute("att", "val");
		Document doc = new Document(emt);
		roundTripDocument(doc);
    }
    
    @Test
    public void testOutputDocumentNamespaces() {
		Element emt = new Element("root", Namespace.getNamespace("ns", "myns"));
		Namespace ans = Namespace.getNamespace("ans", "attributens");
		emt.addNamespaceDeclaration(ans);
		emt.addNamespaceDeclaration(Namespace.getNamespace("two", "two"));
		emt.setAttribute(new Attribute("att", "val", ans));
		emt.addContent(new Element("child", Namespace.getNamespace("", "childuri")));
		Document doc = new Document(emt);
		roundTripDocument(doc);
    }
    
	@Test
	public void testRTOutputList() {
		List<Content> list = new ArrayList<Content>();
		list.add(new ProcessingInstruction("jdomtest", ""));
		list.add(new Comment("comment"));
		list.add(new Element("root"));
		roundTripFragment(list);
	}

    @Test
    public void testOutputElementAttributes() {
    	Element emt = new Element("root");
    	emt.setAttribute("att", "val");
    	emt.setAttribute(new Attribute("attx", "valx", AttributeType.UNDECLARED));
		roundTripElement(emt);
    }
    
    @Test
    public void testRTOutputElementNamespaces() {
		Element emt = new Element("root", Namespace.getNamespace("ns", "myns"));
		Namespace ans = Namespace.getNamespace("ans", "attributens");
		emt.addNamespaceDeclaration(ans);
		emt.addNamespaceDeclaration(Namespace.getNamespace("two", "two"));
		emt.setAttribute(new Attribute("att", "val", ans));
		emt.addContent(new Element("child", Namespace.getNamespace("", "childns")));
		roundTripElement(emt);
    }
    
	@Test
	@Ignore
	public void testOutputFragmentList() {
		List<Content> list = new ArrayList<Content>();
		list.add(new ProcessingInstruction("jdomtest", ""));
		list.add(new Comment("comment"));
		list.add(new CDATA("foo"));
		list.add(new Element("root"));
		list.add(new Text("bar"));
		roundTripFragment(list);
	}

	@Test
	@Ignore
	public void testOutputFragmentContent() {
		roundTripFragment(new ProcessingInstruction("jdomtest", ""));
		roundTripFragment(new Comment("comment"));
		roundTripFragment(new CDATA("foo"));
		roundTripFragment(new Element("root"));
		roundTripFragment(new Text("bar"));
	}

	@Test
	public void testOutputNullContent() throws JDOMException {
		DefaultHandler2 handler = new DefaultHandler2() {
			@Override
			public void startDocument() throws SAXException {
				throw new SAXException("SHould not be reaching this, ever");
			}
		};
		SAXOutputter saxout = new SAXOutputter(handler, handler, handler, handler, handler);
		
		Document doc = null;
		Element emt = null;
		List<Content> list = null;
		List<Content> empty = new ArrayList<Content>();
		saxout.output(doc);
		saxout.output(emt);
		saxout.output(list);
		saxout.output(empty);
		saxout.outputFragment(emt);
		saxout.outputFragment(list);
		saxout.outputFragment(empty);
	}

    
}
