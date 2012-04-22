package org.jdom2.test.cases.special;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.test.util.FidoFetch;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class TestIssue008ExpandEntity {
	
	private final void roundTrip(boolean expand, boolean validating, String encoding, String expect) {
		String docloc = "/" + this.getClass().getPackage().getName().replaceAll("\\.", "/") + "/TestIssue008.xml";
		URL docurl = FidoFetch.getFido().getURL(docloc);
		
		if (docurl == null) {
			throw new IllegalStateException("Unable to get resource " + docloc);
		}

		@SuppressWarnings("deprecation")
		SAXBuilder builder = new SAXBuilder(validating);
		//builder.setValidation(validating);
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
		roundTrip(false, false, null, "<doc>&minus;</doc>");
	}

	@Test
	public void testFalseUSASCII() {
		roundTrip(false, false, "US-ASCII", "<doc>&minus;</doc>");
	}

	@Test
	public void testFalseUTF8() {
		roundTrip(false, false, "UTF-8", "<doc>&minus;</doc>");
	}

	@Test
	public void testTrueUSASCII() {
		roundTrip(true, false, "US-ASCII", "<doc>&#x2212;</doc>");
	}

	@Test
	public void testTrueUTF8() {
		roundTrip(true, false, "UTF-8", "<doc>\u2212</doc>");
	}

	@Test
	public void testTrue() {
		roundTrip(true, false, null, "<doc>\u2212</doc>");
	}



	@Test
	public void testValidFalse() {
		roundTrip(false, true, null, "<doc>&minus;</doc>");
	}

	@Test
	public void testValidFalseUSASCII() {
		roundTrip(false, true, "US-ASCII", "<doc>&minus;</doc>");
	}

	@Test
	public void testValidFalseUTF8() {
		roundTrip(false, true, "UTF-8", "<doc>&minus;</doc>");
	}

	@Test
	public void testValidTrueUSASCII() {
		roundTrip(true, true, "US-ASCII", "<doc>&#x2212;</doc>");
	}

	@Test
	public void testValidTrueUTF8() {
		roundTrip(true, true, "UTF-8", "<doc>\u2212</doc>");
	}

	@Test
	public void testValidTrue() {
		roundTrip(true, true, null, "<doc>\u2212</doc>");
	}


}
