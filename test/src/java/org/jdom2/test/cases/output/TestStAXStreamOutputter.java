package org.jdom2.test.cases.output;

import static org.jdom2.test.util.UnitTestUtil.failException;
import static org.jdom2.test.util.UnitTestUtil.normalizeAttributes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;
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
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;
import org.jdom2.UncheckedJDOMFactory;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.StAXStreamBuilder;
import org.jdom2.input.stax.DefaultStAXFilter;
import org.jdom2.output.Format;
import org.jdom2.output.Format.TextMode;
import org.jdom2.output.SAXOutputter;
import org.jdom2.output.StAXStreamOutputter;
import org.jdom2.output.support.AbstractStAXStreamProcessor;
import org.jdom2.output.support.StAXStreamProcessor;
import org.jdom2.test.util.UnitTestUtil;

@SuppressWarnings("javadoc")
public final class TestStAXStreamOutputter extends AbstractTestOutputter {


	private static final XMLOutputFactory soutfactory = XMLOutputFactory.newInstance();
	private static final XMLInputFactory sinfactory = XMLInputFactory.newInstance();

	private static final class OutWrapper {
		private final StringWriter swriter = new StringWriter();
		private final StAXStreamOutputter stax;
		private final XMLStreamWriter xwriter;
		private int from = 0, to = -1;
		
		public OutWrapper(Format format) {
			try {
				xwriter = soutfactory.createXMLStreamWriter(swriter);
				stax = new StAXStreamOutputter(format);
			} catch (Exception xse) {
				throw new IllegalStateException("Cannot construct: See Cause", xse);
			}
		}
		
		public void setMarkFrom() {
			try {
				xwriter.flush();
			} catch (XMLStreamException e) {
				throw new IllegalStateException("Cannot flush(): See Cause", e);
			}
			from = swriter.getBuffer().length();
		}
		
		public void setMarkTo() {
			try {
				xwriter.flush();
			} catch (XMLStreamException e) {
				throw new IllegalStateException("Cannot flush(): See Cause", e);
			}
			to = swriter.getBuffer().length();
		}
		
		@Override
		public String toString() {
			return to >= 0 ? swriter.getBuffer().substring(from, to) :
				swriter.getBuffer().substring(from);
		}
		
		
		
		public StAXStreamOutputter getStax() {
			return stax;
		}

		public void close() {
			try {
				xwriter.close();
			} catch (XMLStreamException e) {
				throw new IllegalStateException("Cannot flush(): See Cause", e);
			}
		}

		public XMLStreamWriter getStream() {
			return xwriter;
		}
		
	}
	
    public TestStAXStreamOutputter() {
		super(false, false, false, false, false);
	}
    
	@Override
	public String outputString(Format format, Document doc) {
		OutWrapper ow = new OutWrapper(format);
		try {
			ow.getStax().output(doc, ow.getStream());
		} catch (XMLStreamException e) {
			throw new IllegalStateException(e);
		}
		ow.close();
		return ow.toString();
	}

	@Override
	public String outputString(Format format, DocType doctype) {
		OutWrapper ow = new OutWrapper(format);
		try {
			ow.getStream().writeStartDocument();
			ow.setMarkFrom();
			ow.getStax().output(doctype, ow.getStream());
			ow.setMarkTo();
			ow.getStream().writeEndDocument();
		} catch (XMLStreamException e) {
			throw new IllegalStateException(e);
		}
		ow.close();
		return ow.toString();
	}

	@Override
	public String outputString(Format format, Element element) {
		OutWrapper ow = new OutWrapper(format);
		try {
			ow.getStream().writeStartDocument();
			ow.setMarkFrom();
			ow.getStax().output(element, ow.getStream());
			ow.setMarkTo();
			ow.getStream().writeEndDocument();
		} catch (XMLStreamException e) {
			throw new IllegalStateException(e);
		}
		ow.close();
		return ow.toString();
	}

	@Override
	public String outputString(Format format, List<? extends Content> list) {
		OutWrapper ow = new OutWrapper(format);
		try {
			ow.getStream().writeStartDocument();
			ow.getStream().writeStartElement("root");
			ow.getStream().writeCharacters("");
			ow.setMarkFrom();
			ow.getStax().output(list, ow.getStream());
			ow.setMarkTo();
			ow.getStream().writeEndElement();
			ow.getStream().writeEndDocument();
		} catch (XMLStreamException e) {
			throw new IllegalStateException(e);
		}
		ow.close();
		return ow.toString();
	}

	@Override
	public String outputString(Format format, CDATA cdata) {
		OutWrapper ow = new OutWrapper(format);
		try {
			ow.getStream().writeStartDocument();
			ow.getStream().writeStartElement("root");
			ow.getStream().writeCharacters("");
			ow.setMarkFrom();
			ow.getStax().output(cdata, ow.getStream());
			ow.setMarkTo();
			ow.getStream().writeEndElement();
			ow.getStream().writeEndDocument();
		} catch (XMLStreamException e) {
			throw new IllegalStateException(e);
		}
		ow.close();
		return ow.toString();
	}

	@Override
	public String outputString(Format format, Text text) {
		OutWrapper ow = new OutWrapper(format);
		try {
			ow.getStream().writeStartDocument();
			ow.getStream().writeStartElement("root");
			ow.getStream().writeCharacters("");
			ow.setMarkFrom();
			ow.getStax().output(text, ow.getStream());
			ow.setMarkTo();
			ow.getStream().writeEndElement();
			ow.getStream().writeEndDocument();
		} catch (XMLStreamException e) {
			throw new IllegalStateException(e);
		}
		ow.close();
		return ow.toString();
	}

	@Override
	public String outputString(Format format, Comment comment) {
		OutWrapper ow = new OutWrapper(format);
		try {
			ow.getStream().writeStartDocument();
			ow.setMarkFrom();
			ow.getStax().output(comment, ow.getStream());
			ow.setMarkTo();
			ow.getStream().writeEndDocument();
		} catch (XMLStreamException e) {
			throw new IllegalStateException(e);
		}
		ow.close();
		return ow.toString();
	}

	@Override
	public String outputString(Format format, ProcessingInstruction pi) {
		OutWrapper ow = new OutWrapper(format);
		try {
			ow.getStream().writeStartDocument();
			ow.setMarkFrom();
			ow.getStax().output(pi, ow.getStream());
			ow.setMarkTo();
			ow.getStream().writeEndDocument();
		} catch (XMLStreamException e) {
			throw new IllegalStateException(e);
		}
		ow.close();
		return ow.toString();
	}

	@Override
	public String outputString(Format format, EntityRef entity) {
		OutWrapper ow = new OutWrapper(format);
		try {
			ow.getStream().writeStartDocument();
			ow.getStream().writeStartElement("root");
			ow.getStream().writeCharacters("");
			ow.setMarkFrom();
			ow.getStax().output(entity, ow.getStream());
			ow.setMarkTo();
			ow.getStream().writeEndElement();
			ow.getStream().writeEndDocument();
		} catch (XMLStreamException e) {
			throw new IllegalStateException(e);
		}
		ow.close();
		return ow.toString();
	}

	@Override
	public String outputElementContentString(Format format, Element element) {
		OutWrapper ow = new OutWrapper(format);
		try {
			ow.getStream().writeStartDocument();
			ow.getStream().writeStartElement("root");
			ow.getStream().writeCharacters("");
			ow.setMarkFrom();
			ow.getStax().outputElementContent(element, ow.getStream());
			ow.setMarkTo();
			ow.getStream().writeEndElement();
			ow.getStream().writeEndDocument();
		} catch (XMLStreamException e) {
			throw new IllegalStateException(e);
		}
		ow.close();
		return ow.toString();
	}

	@Test
	@Override // because omit still adds declaration....
	public void testOutputDocumentOmitDeclaration() {
		Document doc = new Document();
		doc.addContent(new Element("root"));
		FormatSetup setup = new FormatSetup() {
			@Override
			public void setup(Format fmt) {
				fmt.setOmitDeclaration(true);
			}
		};
		String rtdec = "<?xml version=\"1.0\" ?>\n<root />\n";
		checkOutput(doc, setup,
				rtdec, 
				rtdec,
				rtdec,
				rtdec,
				rtdec);
	}

	@Test
    public void test_HighSurrogatePair() throws XMLStreamException, IOException, JDOMException {
      SAXBuilder builder = new SAXBuilder();
      builder.setExpandEntities(true);
      Document doc = builder.build(new StringReader("<?xml version=\"1.0\"?><root>&#x10000; &#x10000;</root>"));
      
      Format format = Format.getCompactFormat().setEncoding("ISO-8859-1");
      StAXStreamOutputter outputter = new StAXStreamOutputter(format);
      ByteArrayOutputStream sw = new ByteArrayOutputStream();
      XMLStreamWriter xsw = soutfactory.createXMLStreamWriter(sw, "ISO-8859-1");
      outputter.output(doc, xsw);
      String xml = sw.toString();
      assertEquals("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + format.getLineSeparator() +
                   "<root>&#xd800;&#xdc00; &#xd800;&#xdc00;</root>" + format.getLineSeparator(), xml);
    }

    @Test
    public void test_HighSurrogatePairDecimal() throws JDOMException, IOException, XMLStreamException {
      SAXBuilder builder = new SAXBuilder();
      builder.setExpandEntities(true);
      Document doc = builder.build(new StringReader("<?xml version=\"1.0\"?><root>&#x10000; &#65536;</root>"));
      Format format = Format.getCompactFormat().setEncoding("ISO-8859-1");
      StAXStreamOutputter outputter = new StAXStreamOutputter(format);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      XMLStreamWriter xsw = soutfactory.createXMLStreamWriter(baos, "ISO-8859-1");
      outputter.output(doc, xsw);
      String xml = baos.toString();
      assertEquals("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + format.getLineSeparator() +
                   "<root>&#xd800;&#xdc00; &#xd800;&#xdc00;</root>" + format.getLineSeparator(), xml);
    }

    @Test
    public void test_HighSurrogateAttPair() throws JDOMException, IOException, XMLStreamException {
      SAXBuilder builder = new SAXBuilder();
      builder.setExpandEntities(true);
      Document doc = builder.build(new StringReader("<?xml version=\"1.0\"?><root att=\"&#x10000; &#x10000;\" />"));
      Format format = Format.getCompactFormat().setEncoding("ISO-8859-1");
      StAXStreamOutputter outputter = new StAXStreamOutputter(format);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      XMLStreamWriter xsw = soutfactory.createXMLStreamWriter(baos, "ISO-8859-1");
      outputter.output(doc, xsw);
      String xml = baos.toString();
      assertEquals("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + format.getLineSeparator() +
                   "<root att=\"&#xd800;&#xdc00; &#xd800;&#xdc00;\"/>" + format.getLineSeparator(), xml);
    }

    @Test
    public void test_HighSurrogateAttPairDecimal() throws JDOMException, IOException, XMLStreamException {
      SAXBuilder builder = new SAXBuilder();
      builder.setExpandEntities(true);
      Document doc = builder.build(new StringReader("<?xml version=\"1.0\"?><root att=\"&#x10000; &#65536;\" />"));
      Format format = Format.getCompactFormat().setEncoding("ISO-8859-1");
      StAXStreamOutputter outputter = new StAXStreamOutputter(format);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      XMLStreamWriter xsw = soutfactory.createXMLStreamWriter(baos, "ISO-8859-1");
      outputter.output(doc, xsw);
      String xml = baos.toString();
      assertEquals("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + format.getLineSeparator() +
                   "<root att=\"&#xd800;&#xdc00; &#xd800;&#xdc00;\"/>" + format.getLineSeparator(), xml);
    }

    // Construct a raw surrogate pair character and confirm it outputs hex escaped
    @Test
    public void test_RawSurrogatePair() throws JDOMException, IOException, XMLStreamException {
      SAXBuilder builder = new SAXBuilder();
      builder.setExpandEntities(true);
      Document doc = builder.build(new StringReader("<?xml version=\"1.0\"?><root>\uD800\uDC00</root>"));
      Format format = Format.getCompactFormat().setEncoding("ISO-8859-1");
      StAXStreamOutputter outputter = new StAXStreamOutputter(format);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      XMLStreamWriter xsw = soutfactory.createXMLStreamWriter(baos, "ISO-8859-1");
      outputter.output(doc, xsw);
      String xml = baos.toString();
      assertEquals("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + format.getLineSeparator() +
                   "<root>&#xd800;&#xdc00;</root>" + format.getLineSeparator(), xml);
    }

    // Construct a raw surrogate pair character and confirm it outputs hex escaped, when UTF-8 too
    @Test
    public void test_RawSurrogatePairUTF8() throws JDOMException, IOException, XMLStreamException {
      SAXBuilder builder = new SAXBuilder();
      builder.setExpandEntities(true);
      Document doc = builder.build(new StringReader("<?xml version=\"1.0\"?><root>\uD800\uDC00</root>"));
      Format format = Format.getCompactFormat().setEncoding("UTF-8");
      StAXStreamOutputter outputter = new StAXStreamOutputter(format);
      StringWriter baos = new StringWriter();
      XMLStreamWriter xsw = soutfactory.createXMLStreamWriter(baos);
      outputter.output(doc, xsw);
      String xml = baos.toString();
      assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + format.getLineSeparator() +
                   "<root>\uD800\uDC00</root>" + format.getLineSeparator(), xml);
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
    public void test_ErrorSurrogatePairOutput() throws JDOMException, IOException, XMLStreamException {
      SAXBuilder builder = new SAXBuilder();
      builder.setExpandEntities(true);
      Document doc = builder.build(new StringReader("<?xml version=\"1.0\"?><root></root>"));
      Text t = new UncheckedJDOMFactory().text("\uD800\uDBFF");
      doc.getRootElement().setContent(t);
      Format format = Format.getCompactFormat().setEncoding("ISO-8859-1");
      StAXStreamOutputter outputter = new StAXStreamOutputter(format);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      XMLStreamWriter xsw = soutfactory.createXMLStreamWriter(baos);
      try {
        outputter.output(doc, xsw);
        fail("Illegal surrogate pair output should have thrown an exception");
      }
      catch (XMLStreamException e) {
    	  // do nothing
      } catch (Exception e) {
    	  fail ("Unexpected exception " + e.getClass());
      }
    }
    
    
	@Test
	public void testStAXStreamOutputter() {
		StAXStreamOutputter out = new StAXStreamOutputter();
		TestFormat.checkEquals(out.getFormat(), Format.getRawFormat());
	}


	@Test
	public void testStAXStreamOutputterFormat() {
		Format mine = Format.getCompactFormat();
		mine.setEncoding("US-ASCII");
		StAXStreamOutputter out = new StAXStreamOutputter(mine);
		TestFormat.checkEquals(mine, out.getFormat());
	}

//	@Test
//	public void testXMLOutputterXMLOutputter() {
//		Format mine = Format.getCompactFormat();
//		StAXStreamProcessor xoutp = new StAXStreamOutputter().getStAXStream();
//		mine.setEncoding("US-ASCII");
//		// double-construct it.
//		StAXStreamOutputter out = new StAXStreamOutputter();
//		TestFormat.checkEquals(mine, out.getFormat());
//		assertTrue(xoutp == out.getXMLOutputProcessor());
//	}

	@Test
	public void testStAXStreamOutputterXMLOutputProcessor() {
		StAXStreamProcessor xoutp = new AbstractStAXStreamProcessor() {
			// nothing;
		};
		// double-constrcut it.
		StAXStreamOutputter out = new StAXStreamOutputter(xoutp);
		TestFormat.checkEquals(Format.getRawFormat(), out.getFormat());
		assertTrue(xoutp == out.getStAXStream());
	}

	@Test
	public void testFormat() {
		Format mine = Format.getCompactFormat();
		mine.setEncoding("US-ASCII");
		// double-constcut it.
		StAXStreamOutputter out = new StAXStreamOutputter();
		TestFormat.checkEquals(Format.getRawFormat(), out.getFormat());
		out.setFormat(mine);
		TestFormat.checkEquals(mine, out.getFormat());
	}

	@Test
	public void testStAXStreamOutputProcessor() {
		StAXStreamProcessor xoutp = new AbstractStAXStreamProcessor() {
			// nothing;
		};
		// double-constcut it.
		StAXStreamOutputter out = new StAXStreamOutputter();
		StAXStreamProcessor xop = out.getStAXStream();
		out.setStAXStreamProcessor(xoutp);
		assertTrue(xoutp != xop);
		assertTrue(xoutp == out.getStAXStream());
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
		StAXStreamOutputter xout = new StAXStreamOutputter(mf);
		StringWriter sw = new StringWriter();
		XMLStreamWriter xsw = soutfactory.createXMLStreamWriter(sw);
		xout.output(root, xsw);
		assertEquals("<root> x </root>", sw.toString());
	}

	@Test
	public void testClone() {
		StAXStreamOutputter xo = new StAXStreamOutputter();
		assertTrue(xo != xo.clone());
	}

	@Test
	public void testToString() {
		Format fmt = Format.getCompactFormat();
		fmt.setLineSeparator("\n\t ");
		StAXStreamOutputter out = new StAXStreamOutputter(fmt);
		assertNotNull(out.toString());
	}
	
	/*
	 * The following are borrowed from the TestSAXOutputter
	 * The effect is that we compare the StAX string output with the re-parsed
	 * value of the input.
	 */
	
    private void roundTripDocument(Document doc) {
    	roundTripDocument(doc, true);
    	roundTripDocument(doc, false);
    }

    private void roundTripDocument(Document doc, boolean repairing) {
    	StAXStreamOutputter xout = new StAXStreamOutputter(Format.getRawFormat());
    	// create a String representation of the input.
    	if (doc.hasRootElement()) {
    		normalizeAttributes(doc.getRootElement());
    	}
    	
    	try {
    		StringWriter sw = new StringWriter();
    		soutfactory.setProperty("javax.xml.stream.isRepairingNamespaces", repairing);
    		XMLStreamWriter xsw = soutfactory.createXMLStreamWriter(sw);
        	xout.output(doc, xsw);
        	xsw.close();
        	Document expect = new SAXBuilder().build(new StringReader(sw.toString()));
    		// convert the input to a SAX Stream
	    	UnitTestUtil.compare(doc, expect);

        	StAXStreamBuilder sbuilder = new StAXStreamBuilder();
        	
        	StringReader car = new StringReader(sw.toString());
        	XMLStreamReader xsr = sinfactory.createXMLStreamReader(car);
        	
			Document backagain = sbuilder.build(xsr);
			xsr.close();
			
	    	UnitTestUtil.compare(expect, backagain);
			
		} catch (Exception e) {
			failException("Failed to round-trip the document with exception: " 
					+ e.getMessage(), e);
		}
    }
    
    private void roundTripElement(Element emt) {
    	
    	try {
        	StAXStreamOutputter xout = new StAXStreamOutputter(Format.getRawFormat());

        	StringWriter sw = new StringWriter();
        	XMLStreamWriter xsw = soutfactory.createXMLStreamWriter(sw);
        	xout.output(emt, xsw);
        	xsw.close();
        	String expect = sw.toString();
        	
        	StAXStreamBuilder sbuilder = new StAXStreamBuilder();
        	
        	XMLStreamReader xsr = sinfactory.createXMLStreamReader(new StringReader(expect));
        	assertTrue(xsr.getEventType() == XMLStreamConstants.START_DOCUMENT);
        	assertTrue(xsr.hasNext());
        	xsr.next();
        	
			Element backagain = (Element)sbuilder.fragment(xsr);

			// convert the input to a SAX Stream

			sw.getBuffer().setLength(0);
        	xsw = soutfactory.createXMLStreamWriter(sw);
        	xout.output(backagain, xsw);
        	xsw.close();
        	
        	String actual = sw.toString();
        	assertEquals(expect, actual);
		} catch (Exception e) {
			failException("Failed to round-trip the document with exception: " 
					+ e.getMessage(), e);
		}
    }
    
    private void roundTripFragment(List<Content> content) {
    	try {
        	StAXStreamOutputter xout = new StAXStreamOutputter(Format.getRawFormat());

        	StringWriter sw = new StringWriter();
        	XMLStreamWriter xsw = soutfactory.createXMLStreamWriter(sw);
        	xout.output(content, xsw);
        	xsw.close();
        	String expect = sw.toString();
        	
        	StAXStreamBuilder sbuilder = new StAXStreamBuilder();
        	
        	XMLStreamReader xsr = sinfactory.createXMLStreamReader(new StringReader(expect));
//        	assertTrue(xsr.getEventType() == XMLStreamConstants.START_DOCUMENT);
//        	assertTrue(xsr.hasNext());
//        	xsr.next();
        	
			List<Content> backagain = sbuilder.buildFragments(xsr, new DefaultStAXFilter());

			// convert the input to a SAX Stream

			sw.getBuffer().setLength(0);
        	xsw = soutfactory.createXMLStreamWriter(sw);
        	xout.output(backagain, xsw);
        	xsw.close();
        	
        	String actual = sw.toString();
        	assertEquals(expect, actual);
		} catch (Exception e) {
			failException("Failed to round-trip the document with exception: " 
					+ e.getMessage(), e);
		}
    	
    }
    
    private void roundTripFragment(Content content) {
    	try {
        	StAXStreamOutputter xout = new StAXStreamOutputter(Format.getRawFormat());

        	StringWriter sw = new StringWriter();
        	XMLStreamWriter xsw = soutfactory.createXMLStreamWriter(sw);
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
        	xsw.close();
        	String expect = sw.toString();
        	
        	StAXStreamBuilder sbuilder = new StAXStreamBuilder();
        	
			Content backagain = sbuilder.fragment(
					sinfactory.createXMLStreamReader(new StringReader(expect)));

			// convert the input to a SAX Stream

			sw.getBuffer().setLength(0);
        	xsw = soutfactory.createXMLStreamWriter(sw);
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
        	xsw.close();
        	
        	String actual = sw.toString();
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
	public void testRTOutputDocumentDefaultNS() {
		Element root = new Element("root", "http://jdom/noprefix");
		Document doc = new Document(root);
		roundTripDocument(doc);
	}

    @Test
	@Ignore
	// TODO
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
	// TODO
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
	// TODO
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
