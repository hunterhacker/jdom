package org.jdom2.test.cases.output;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.test.util.FidoFetch;
import org.jdom2.test.util.UnitTestUtil;

@SuppressWarnings("javadoc")
public abstract class AbstractTestRoundTrip {

	abstract Document roundTrip(Document doc);
	
	abstract Document prepare(Document doc);
	
	private final void checkRoundTrip(final Document indoc) {
		final Document doc = prepare(indoc);
		final Document rtdoc = roundTrip(doc);
		assertTrue(rtdoc != null);
		try {
			XMLOutputter xout = new XMLOutputter();
			System.out.println("Compare from:\n");
			xout.output(doc, System.out);
			System.out.println("Compare to:\n");
			xout.output(rtdoc, System.out);
			System.out.println("\n\n");
		} catch (IOException ioe) {
			// swallow
		}
		UnitTestUtil.compare(doc,  rtdoc);
	}
	
	@Test
	public void testBasic() {
		Document doc = new Document(new Element("root"));
		checkRoundTrip(doc);
	}
	
	@Test
	public void testDefaultNamespace() {
		Element emt = new Element("root", "ns:1");
		emt.addContent(new Element("child")); // note, no namespace.
		Document doc = new Document(emt);
		checkRoundTrip(doc);
	}

	
	@Test
	public void testSimple() throws JDOMException, IOException {
		final SAXBuilder sb = new SAXBuilder();
		
		final Document doc = sb.build(FidoFetch.getFido().getURL("/DOMBuilder/simple.xml"));
		doc.setBaseURI(null);
		checkRoundTrip(doc);
	}

	@Test
	public void testNamespaces() throws JDOMException, IOException {
		final SAXBuilder sb = new SAXBuilder();
		
		final Document doc = sb.build(FidoFetch.getFido().getURL("/DOMBuilder/namespaces.xml"));
		doc.setBaseURI(null);
		checkRoundTrip(doc);
	}

	@Test
	public void testComplex() throws JDOMException, IOException {
		final SAXBuilder sb = new SAXBuilder();
		
		final Document doc = sb.build(FidoFetch.getFido().getURL("/DOMBuilder/complex.xml"));
		doc.setBaseURI(null);
		checkRoundTrip(doc);
	}

	
}
