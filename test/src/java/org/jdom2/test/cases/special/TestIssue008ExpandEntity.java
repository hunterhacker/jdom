package org.jdom2.test.cases.special;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;

public class TestIssue008ExpandEntity {
	
	private final void roundTrip(boolean expand, String encoding, String expect) {
		String docloc = this.getClass().getPackage().getName().replaceAll("\\.", "/") + "/TestIssue008.xml";
		URL docurl = ClassLoader.getSystemResource(docloc);

		SAXBuilder builder = new SAXBuilder();
		builder.setExpandEntities(expand);
		Document doc = null;
		try {
			doc = builder.build(docurl);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (doc == null) {
			fail("Unable to parse document, see output.");
		}
		
		Format fmt = Format.getCompactFormat();
		if (encoding != null) {
			fmt.setEncoding(encoding);
		}
		XMLOutputter xout = new XMLOutputter(fmt);
		
		String actual = xout.outputString(doc.getRootElement());
		assertEquals(expect, actual);
		
	}

	@Test
	public void testFalse() {
		roundTrip(false, null, "<doc>&minus;</doc>");
	}

	@Test
	public void testFalseUSASCII() {
		roundTrip(false, "US-ASCII", "<doc>&minus;</doc>");
	}

	@Test
	public void testFalseUTF8() {
		roundTrip(false, "UTF-8", "<doc>&minus;</doc>");
	}

	@Test
	public void testTrueUSASCII() {
		roundTrip(true, "US-ASCII", "<doc>&#x2212;</doc>");
	}

	@Test
	public void testTrueUTF8() {
		roundTrip(true, "UTF-8", "<doc>\u2212</doc>");
	}

	@Test
	public void testTrue() {
		roundTrip(true, null, "<doc>\u2212</doc>");
	}

}
