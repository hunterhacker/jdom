package org.jdom2.contrib.perf;

import java.io.CharArrayReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DefaultHandler2;

import org.jdom2.Attribute;
import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.Content;
import org.jdom2.DefaultJDOMFactory;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.EntityRef;
import org.jdom2.Namespace;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;
import org.jdom2.UncheckedJDOMFactory;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.DOMBuilder;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.StAXEventBuilder;
import org.jdom2.input.StAXStreamBuilder;
import org.jdom2.input.sax.SAXHandler;
import org.jdom2.internal.ArrayCopy;
import org.jdom2.output.Format;
import org.jdom2.output.SAXOutputter;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

@SuppressWarnings("javadoc")
public class PerfDoc {
	
	private class SAXLoadRunnable implements TimeRunnable {
		private final int type;
		
		SAXLoadRunnable(int type) {
			this.type = type;
		}
		
		@Override
		public void run() throws Exception {
			Document doc = subload(type);
			if (document == null) {
				document = doc;
			}
		}
	}
	
	private static final DevNull devnull = new DevNull();
	
	private final File infile;
	
	private long saxTime = -1L;
	private long domTime = -1L;
	private long staxTime = -1L;
	private long staxETime = -1L;
	private long saxDTime = -1L;
	private long domDTime = -1L;
	private long staxDTime = -1L;
	private long staxDETime = -1L;
	private long loadMem = -1L;
	
	private long dumpTime = -1L;
	private long xpathTime = -1L;
	private long dupeTime = -1L;
	private long scanTime = -1L;
	private long checkedTime = -1L;
	private long uncheckedTime = -1L;
	
	
	private Document document = null;
	private final char[] chars;

	public PerfDoc(File file) throws IOException {
		infile = file;
		char[] fchars = new char[(int)infile.length()];
		int len = 0;
		int cnt = 0;
		FileReader fr = null;
		try {
			fr = new FileReader(infile);
			while ((cnt = fr.read(fchars, len, fchars.length - len)) >= 0) {
				if (cnt == 0 && len == fchars.length) {
					fchars = ArrayCopy.copyOf(fchars, fchars.length + 10240);
				}
				len += cnt;
			}
			fchars = ArrayCopy.copyOf(fchars, len);
		} finally {
			if (fr != null) {
				fr.close();
			}
			fr = null;
		}
		this.chars = fchars;
	}

	public File getFile() {
		return infile;
	}

	public long saxLoad() throws Exception {
		final long startmem = PerfTest.usedMem();
		saxTime = PerfTest.timeRun(new SAXLoadRunnable(0) );
		loadMem = PerfTest.usedMem() - startmem;
		saxDTime = PerfTest.timeRun(new SAXLoadRunnable(8) );
		return saxTime;
	}
	
	public long domLoad() throws Exception {
		domTime = PerfTest.timeRun( new SAXLoadRunnable(1) );
		domDTime = PerfTest.timeRun( new SAXLoadRunnable(9) );
		return domTime;
	}
	
	public long staxLoad() throws Exception {
		staxTime = PerfTest.timeRun( new SAXLoadRunnable(2) );
		staxDTime = PerfTest.timeRun( new SAXLoadRunnable(10) );
		return staxTime;
	}
	
	public long staxELoad() throws Exception {
		staxETime = PerfTest.timeRun( new SAXLoadRunnable(3) );
		staxDETime = PerfTest.timeRun( new SAXLoadRunnable(11) );
		return staxETime;
	}
	
	public Document subload(int type) throws Exception {

		CharArrayReader car = new CharArrayReader(chars);
		switch (type) {
			case 0:
				SAXBuilder sax = new SAXBuilder();
				sax.setJDOMFactory(new UncheckedJDOMFactory());
				return sax.build(car);
			case 1:
				DOMBuilder dom = new DOMBuilder();
				dom.setFactory(new UncheckedJDOMFactory());
				InputSource source = new InputSource(car);
				return dom.build(getDocument(source, false));
			case 2:
				StAXStreamBuilder stax = new StAXStreamBuilder();
				stax.setFactory(new UncheckedJDOMFactory());
				XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(car);
				return stax.build(reader);
			case 3:
				StAXEventBuilder staxe = new StAXEventBuilder();
				staxe.setFactory(new UncheckedJDOMFactory());
				XMLEventReader events = XMLInputFactory.newInstance().createXMLEventReader(car);
				return staxe.build(events);
			case 8:
				SAXBuilder dsax = new SAXBuilder();
				DefaultHandler2 def = new DefaultHandler2();
				XMLReader sread = dsax.getXMLReaderFactory().createXMLReader();
				sread.setContentHandler(def);
				sread.setDTDHandler(def);
				sread.setEntityResolver(def);
				sread.setErrorHandler(def);
				sread.setProperty("http://xml.org/sax/properties/lexical-handler",
						def);
				sread.parse(new InputSource(car));
				return null;
			case 9:
				InputSource dsource = new InputSource(car);
				getDocument(dsource, false);
				return null;
			case 10:
				XMLStreamReader dreader = XMLInputFactory.newInstance().createXMLStreamReader(car);
				int sum = 0;
				while (dreader.hasNext()) {
					sum += dreader.next();
				}
				System.out.println("Sum " + sum);
				return null;
			case 11:
				XMLEventReader dereader = XMLInputFactory.newInstance().createXMLEventReader(car);
				int esum = 0;
				while (dereader.hasNext()) {
					esum += dereader.nextEvent().getEventType();
				}
				System.out.println("Sum " + esum);
				return null;
			case 12:
				SAXBuilder slimsax = new SAXBuilder();
				//slimsax.setJDOMFactory(new SlimJDOMFactory());
				//slimsax.setFeature("http://xml.org/sax/features/string-interning", true);
				return slimsax.build(car);
		}
		return null;
	}
	
	public int recurse(Element emt) {
		int cnt = 1;
		for (Object kid : emt.getChildren()) {
			cnt += recurse((Element)kid);
		}
		return cnt;
	}
	
	public long scan() throws Exception {
		scanTime = PerfTest.timeRun(new TimeRunnable() {
			@Override
			public void run() {
				int elements = 0;
				Iterator<Content> it = document.getDescendants();
				while (it.hasNext()) {
					if (it.next() instanceof Element) {
						elements++;
					}
				}
				
				int felements = 0;
				Iterator<?> et = document.getDescendants(new ElementFilter());
				while (et.hasNext()) {
					et.next();
					felements++;
				}
				if (felements != elements) {
					System.out.printf("Different counts Descendants=%d Elements=%d\n", elements, felements);
				}
				
				int rcnt = recurse(document.getRootElement());
				if (rcnt != elements) {
					System.out.printf("Different counts Descendants=%d Recurse=%d\n", elements, rcnt);
				}
			}
		});
		return scanTime;
	}
	
	public long dump () throws Exception {
		dumpTime = PerfTest.timeRun(new TimeRunnable() {
			
			@Override
			public void run() throws Exception {
				long start = System.nanoTime();
				dump(Format.getCompactFormat());
				long comp = System.nanoTime();
				dump(Format.getPrettyFormat());
				long pretty = System.nanoTime();
				dump(Format.getRawFormat());
				long raw = System.nanoTime();
				raw -= pretty;
				pretty -= comp;
				comp -= start;
				System.out.printf("Raw=%d Pretty=%8d Compact=%8d\n", raw, pretty, comp);
			}
		});
		return dumpTime;
	}
	
	private void dump(Format format) throws IOException {
		XMLOutputter xout = new XMLOutputter(format);
		devnull.reset();
		System.setProperty("NamespaceStack", "");
		xout.output(document, devnull);
		System.clearProperty("NamespaceStack");
		if (devnull.getCounter() <= 0) {
			throw new IllegalStateException("Needed a counter");
		}
	}
	
	public long xpath() throws Exception {
		
		final XPathFactory fac = XPathFactory.instance();

		xpathTime = PerfTest.timeRun(new TimeRunnable() {
			@Override
			public void run() throws Exception {
				XPathExpression<Object> patha = fac.compile("//@null");
				patha.evaluate(document);
				// select everything
				XPathExpression<?> pathb = fac.compile("//node()");
				pathb.evaluate(document);
			}
		});
		return xpathTime;
	}
	
	private Collection<Content>duplicateContent(List<? extends Content> content) {
		ArrayList<Content>ret = new ArrayList<Content>(content.size());
		for (Content c : content) {
			if (c instanceof Element) {
				Element emt = (Element)c;
				Element ne = new Element(emt.getName(), emt.getNamespacePrefix(), emt.getNamespaceURI());
				if (emt.hasAttributes()) {
					for (Object oatt : emt.getAttributes()) {
						Attribute att = (Attribute)oatt;
						Attribute a = new Attribute(att.getName(), att.getValue(), 
								att.getAttributeType(), 
								Namespace.getNamespace(att.getNamespacePrefix(), att.getNamespaceURI()));
						emt.setAttribute(a);
					}
				}
				if (emt.hasAdditionalNamespaces()) {
					for (Object ns : emt.getAdditionalNamespaces()) {
						ne.addNamespaceDeclaration((Namespace)ns);
					}
				}
				ne.addContent(duplicateContent(emt.getContent()));
				ret.add(ne);
			} else if (c instanceof CDATA) {
				ret.add(new CDATA(((CDATA)c).getText()));
			} else if (c instanceof Comment) {
				ret.add(new Comment(((Comment)c).getText()));
			} else if (c instanceof EntityRef) {
				EntityRef er = (EntityRef)c;
				ret.add(new EntityRef(er.getName(), er.getPublicID(), er.getSystemID()));
			} else if (c instanceof Text) {
				ret.add(new Text(((Text)c).getText()));
			} else if (c instanceof ProcessingInstruction) {
				ProcessingInstruction pi = (ProcessingInstruction)c;
				ret.add(new ProcessingInstruction(pi.getTarget(), pi.getData()));
			} else if (c instanceof DocType) {
				DocType dt = (DocType)c;
				DocType ndt = new DocType(dt.getElementName(), dt.getPublicID(), dt.getSystemID());
				if (dt.getInternalSubset() != null) {
					ndt.setInternalSubset(dt.getInternalSubset());
				}
				ret.add(ndt);
			} else {
				throw new IllegalStateException("Unknown content " + c);
			}
		}
		return ret;
	}
	
	public long duplicate() throws Exception {
		final XMLOutputter xout = new XMLOutputter(Format.getRawFormat());
		final String orig = xout.outputString(document);
		dupeTime = PerfTest.timeRun(new TimeRunnable() {
			@Override
			public void run() throws Exception {
				// select nothing.
				Document doc = new Document();
				doc.addContent(duplicateContent(document.getContent()));
				String dupe = xout.outputString(doc);
				if (!orig.equals(dupe)) {
					throw new IllegalStateException("Bad clone!");
				}
			}
		});
		return dupeTime;
	}
	
	public long checkedParse() throws Exception {
		final XMLOutputter xout = new XMLOutputter(Format.getRawFormat());
		final String orig = xout.outputString(document);
		checkedTime = PerfTest.timeRun(new TimeRunnable() {
			@Override
			public void run() throws Exception {
				// select nothing.
				SAXHandler handler = new SAXHandler(new DefaultJDOMFactory());
				SAXOutputter saxout = new SAXOutputter(handler, handler, handler, handler, handler);
				saxout.output(document);
				Document doc = handler.getDocument();
				String dupe = xout.outputString(doc);
				if (!orig.equals(dupe)) {
					throw new IllegalStateException("Bad clone!");
				}
			}
		});
		return checkedTime;
	}
	
	public long unCheckedParse() throws Exception {
		final XMLOutputter xout = new XMLOutputter(Format.getRawFormat());
		final String orig = xout.outputString(document);
		uncheckedTime = PerfTest.timeRun(new TimeRunnable() {
			@Override
			public void run() throws Exception {
				// select nothing.
				SAXHandler handler = new SAXHandler(new UncheckedJDOMFactory());
				SAXOutputter saxout = new SAXOutputter(handler, handler, handler, handler, handler);
				saxout.output(document);
				Document doc = handler.getDocument();
				String dupe = xout.outputString(doc);
				if (!orig.equals(dupe)) {
					throw new IllegalStateException("Bad clone!");
				}
			}
		});
		return uncheckedTime;
	}
	
	public long getSAXLoadTime() {
		return saxTime;
	}


	public long getLoadMem() {
		return loadMem;
	}


	public long getDumpTime() {
		return dumpTime;
	}

	public long getDupeTime() {
		return dupeTime;
	}

	public long getXpathTime() {
		return xpathTime;
	}

	public long getCheckedTime() {
		return checkedTime;
	}

	public long getUncheckedTime() {
		return uncheckedTime;
	}


	public long getScanTime() {
		return scanTime;
	}

	

	public long getSaxDTime() {
		return saxDTime;
	}

	public long getDomDTime() {
		return domDTime;
	}

	public long getStaxDTime() {
		return staxDTime;
	}

	
	public long getStaxETime() {
		return staxETime;
	}

	public long getStaxDETime() {
		return staxDETime;
	}

	public Document getDocument() {
		return document;
	}

	@Override
	public String toString() {
		return String.format("PerfDoc %s mem=%d sax=%d dom=%d stax=%d scan=%d xpath=%d dump=%d", 
				infile.getPath(), loadMem, saxTime, domTime, staxTime, scanTime, xpathTime, dumpTime);
	}
	
	private static final org.w3c.dom.Document getDocument(InputSource data, boolean xsdvalidate) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		dbf.setValidating(xsdvalidate);
		dbf.setExpandEntityReferences(false);
		
		if (xsdvalidate) {
			dbf.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
		}
		DocumentBuilder db = dbf.newDocumentBuilder();
		return db.parse(data);
	}
}
