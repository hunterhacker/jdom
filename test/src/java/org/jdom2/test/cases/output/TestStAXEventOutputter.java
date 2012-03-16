package org.jdom2.test.cases.output;

import static org.jdom2.test.util.UnitTestUtil.failException;
import static org.jdom2.test.util.UnitTestUtil.normalizeAttributes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.util.XMLEventConsumer;

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
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;
import org.jdom2.UncheckedJDOMFactory;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.StAXStreamBuilder;
import org.jdom2.input.sax.SAXHandler;
import org.jdom2.input.stax.DefaultStAXFilter;
import org.jdom2.output.Format;
import org.jdom2.output.Format.TextMode;
import org.jdom2.output.support.AbstractStAXEventProcessor;
import org.jdom2.output.support.StAXEventProcessor;
import org.jdom2.output.SAXOutputter;
import org.jdom2.output.StAXEventOutputter;
import org.jdom2.output.XMLOutputter;

@SuppressWarnings("javadoc")
public final class TestStAXEventOutputter extends AbstractTestOutputter {

	private final static XMLOutputFactory soutfactory = XMLOutputFactory.newInstance();
	private final static XMLInputFactory sinfactory = XMLInputFactory.newInstance();
	private final static XMLEventFactory seventfactory = XMLEventFactory.newInstance();
	
	private static final class OutWrapper {
		private final StringWriter swriter = new StringWriter();
		
		private final StAXEventOutputter stax;
		private final XMLEventWriter xwriter;
		private int from = 0, to = -1;
		
		public OutWrapper(Format format) {
			try {
				xwriter = soutfactory.createXMLEventWriter(swriter);
				stax = new StAXEventOutputter(format);
			} catch (Exception xse) {
				throw new IllegalStateException("Cannot construct: See Cause", xse);
			}
		}
		
		@Override
		public String toString() {
			return to >= 0 ? swriter.getBuffer().substring(from, to) :
				swriter.getBuffer().substring(from);
		}
		
		
		
		public StAXEventOutputter getStax() {
			return stax;
		}

		public void close() {
			try {
				xwriter.close();
			} catch (XMLStreamException e) {
				throw new IllegalStateException("Cannot flush(): See Cause", e);
			}
		}

		public XMLEventWriter getStream() {
			return xwriter;
		}

		public void setDocumentMarkFrom() {
			try {
				xwriter.add(seventfactory.createStartDocument());
				xwriter.add(seventfactory.createCharacters(""));
				xwriter.flush();
			} catch (XMLStreamException e) {
				throw new IllegalStateException("Cannot flush(): See Cause", e);
			}
			from = swriter.getBuffer().length();
		}

		public void setDocumentMarkTo() {
			try {
				xwriter.add(seventfactory.createCharacters(""));
				xwriter.flush();
				to = swriter.getBuffer().length();
				xwriter.add(seventfactory.createEndDocument());
			} catch (XMLStreamException e) {
				throw new IllegalStateException("Cannot flush(): See Cause", e);
			}
		}
		
		public void setElementMarkFrom() {
			try {
				xwriter.add(seventfactory.createStartDocument());
				xwriter.add(seventfactory.createStartElement("", "", "root"));
				xwriter.add(seventfactory.createCharacters(""));
				xwriter.flush();
				from = swriter.getBuffer().length();
			} catch (XMLStreamException e) {
				throw new IllegalStateException("Cannot flush(): See Cause", e);
			}
		}

		public void setElementMarkTo() {
			try {
				xwriter.add(seventfactory.createCharacters(""));
				xwriter.flush();
				to = swriter.getBuffer().length();
				xwriter.add(seventfactory.createEndElement("", "", "root"));
				xwriter.add(seventfactory.createEndDocument());
				xwriter.flush();
				xwriter.close();
			} catch (XMLStreamException e) {
				throw new IllegalStateException("Cannot flush(): See Cause", e);
			}
		}
		
	}
	
	
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
	
	public TestStAXEventOutputter() {
		super(false, false, true, true, false);
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
			ow.setDocumentMarkFrom();
			ow.getStax().output(doctype, ow.getStream());
			ow.setDocumentMarkTo();
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
			ow.setDocumentMarkFrom();
			ow.getStax().output(element, ow.getStream());
			ow.setDocumentMarkTo();
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
			ow.setElementMarkFrom();
			ow.getStax().output(list, ow.getStream());
			ow.setElementMarkTo();
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
			ow.setElementMarkFrom();
			ow.getStax().output(cdata, ow.getStream());
			ow.setElementMarkTo();
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
			ow.setElementMarkFrom();
			ow.getStax().output(text, ow.getStream());
			ow.setElementMarkTo();
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
			ow.setDocumentMarkFrom();
			ow.getStax().output(comment, ow.getStream());
			ow.setDocumentMarkTo();
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
			ow.setDocumentMarkFrom();
			ow.getStax().output(pi, ow.getStream());
			ow.setDocumentMarkTo();
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
			ow.setElementMarkFrom();
			ow.getStax().output(entity, ow.getStream());
			ow.setElementMarkTo();
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
			ow.setElementMarkFrom();
			ow.getStax().outputElementContent(element, ow.getStream());
			ow.setElementMarkTo();
		} catch (XMLStreamException e) {
			throw new IllegalStateException(e);
		}
		ow.close();
		return ow.toString();
	}

	@Override
	public void testOutputDocumentOmitDeclaration() {
		// skip this test.
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
      assertEquals("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" + 
                   "<root>&#xd800;&#xdc00; &#xd800;&#xdc00;</root>\r\n", xml);
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
      assertEquals("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" + 
                   "<root>&#xd800;&#xdc00; &#xd800;&#xdc00;</root>\r\n", xml);
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
      assertEquals("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" +
                   "<root att=\"&#xd800;&#xdc00; &#xd800;&#xdc00;\"></root>\r\n", xml);
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
      assertEquals("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" + 
                   "<root att=\"&#xd800;&#xdc00; &#xd800;&#xdc00;\"></root>\r\n", xml);
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
      assertEquals("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" +
                   "<root>&#xd800;&#xdc00;</root>\r\n", xml);
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
