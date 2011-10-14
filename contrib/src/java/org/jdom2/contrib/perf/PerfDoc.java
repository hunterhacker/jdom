package org.jdom2.contrib.perf;

import java.io.CharArrayReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jdom2.*;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.SAXHandler;
import org.jdom2.output.Format;
import org.jdom2.output.SAXOutputter;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPath;

public class PerfDoc {
	
	private class LoadRunnable implements TimeRunnable {
		private final char[] data;
		
		LoadRunnable(char[] indata) {
			this.data = indata;
		}
		
		@Override
		public void run() throws Exception {
			subload(data);
		}
	}
	
	private static final Writer devnull = new Writer() {
		
		@Override
		public void write(char[] cbuf, int off, int len) throws IOException {
			// do nothing
		}
		
		@Override
		public void flush() throws IOException {
			// do nothing
		}
		
		@Override
		public void close() throws IOException {
			// do nothing
		}

		@Override
		public void write(int c) throws IOException {
			// do nothing
		}

		@Override
		public void write(char[] cbuf) throws IOException {
			// do nothing
		}

		@Override
		public void write(String str) throws IOException {
			// do nothing
		}

		@Override
		public void write(String str, int off, int len) throws IOException {
			// do nothing
		}

		@Override
		public Writer append(CharSequence csq) throws IOException {
			return this;
		}

		@Override
		public Writer append(CharSequence csq, int start, int end) throws IOException {
			return this;
		}

		@Override
		public Writer append(char c) throws IOException {
			return this;
		}
		
	};
	
	private final File infile;
	
	private long loadTime = -1L;
	private long loadMem = -1L;
	
	private long dumpTime = -1L;
	private long xpathTime = -1L;
	private long dupeTime = -1L;
	private long scanTime = -1L;
	private long checkedTime = -1L;
	private long uncheckedTime = -1L;
	
	
	private Document document = null;

	public PerfDoc(File file) {
		infile = file;
	}

	public File getFile() {
		return infile;
	}

	public long load() throws Exception {
		char[] chars = new char[(int)infile.length()];
		int len = 0;
		int cnt = 0;
		FileReader fr = null;
		try {
			fr = new FileReader(infile);
			while ((cnt = fr.read(chars, len, chars.length - len)) >= 0) {
				if (cnt == 0 && len == chars.length) {
					chars = Arrays.copyOf(chars, chars.length + 10240);
				}
				len += cnt;
			}
			chars = Arrays.copyOf(chars, len);
		} finally {
			if (fr != null) {
				fr.close();
			}
			fr = null;
		}
		final long startmem = PerfTest.usedMem();
		loadTime = PerfTest.timeRun( new LoadRunnable(chars) );
		loadMem = PerfTest.usedMem() - startmem;
		for (char c : chars) {
			// need to keep chars in memory until after the usedMem test.
			if (c == (char)0) {
				throw new IllegalStateException();
			}
		}
		return loadTime;
	}
	
	public long subload(char[] chars) throws IOException, JDOMException {

		CharArrayReader car = new CharArrayReader(chars);
		SAXBuilder sax = new SAXBuilder();
		sax.setValidation(false);
		PerfTest.usedMem();
		long start = System.nanoTime();
		document = sax.build(car);
		return System.nanoTime() - start;
	}
	
	public int recurse(Element emt) {
		int cnt = 1;
		for (Element kid : emt.getChildren()) {
			cnt += recurse(kid);
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
				Iterator<Element> et = document.getDescendants(Filters.element());
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
				dump(Format.getCompactFormat());
				dump(Format.getPrettyFormat());
				dump(Format.getRawFormat());
			}
		});
		return dumpTime;
	}
	
	private long dump(Format format) throws IOException {
		XMLOutputter xout = new XMLOutputter(format);
		long start = System.nanoTime();
		xout.output(document, devnull);
		return System.nanoTime() - start;
	}
	
	public long xpath() throws Exception {

		xpathTime = PerfTest.timeRun(new TimeRunnable() {
			@Override
			public void run() throws Exception {
				XPath patha = XPath.newInstance("//@null");
				patha.selectNodes(document);
				// select everything
				XPath pathb = XPath.newInstance("//node()");
				pathb.selectNodes(document);
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
				for (Attribute att : emt.getAttributes()) {
					Attribute a = new Attribute(att.getName(), att.getValue(), 
							att.getAttributeType(), 
							Namespace.getNamespace(att.getNamespacePrefix(), att.getNamespaceURI()));
					emt.setAttribute(a);
				}
				for (Namespace ns : emt.getAdditionalNamespaces()) {
					ne.addNamespaceDeclaration(ns);
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
	
	public long getLoadTime() {
		return loadTime;
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


	public Document getDocument() {
		return document;
	}

	@Override
	public String toString() {
		return String.format("PerfDoc %s mem=%d load=%d scan=%d xpath=%d dump=%d", 
				infile.getPath(), loadMem, loadTime, scanTime, xpathTime, dumpTime);
	}
	
}
