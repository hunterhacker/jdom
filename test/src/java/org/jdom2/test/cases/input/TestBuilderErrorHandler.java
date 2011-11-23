package org.jdom2.test.cases.input;

import static org.junit.Assert.*;

import org.jdom2.input.sax.BuilderErrorHandler;

import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

@SuppressWarnings("javadoc")
public class TestBuilderErrorHandler {

	@Test
	public void testWarning() {
		BuilderErrorHandler handler = new BuilderErrorHandler();
		try {
			handler.warning(new SAXParseException(null, null, null, 0, 0));
		} catch (Exception e) {
			fail("Warning should not throw an exception, but got " + e.getClass() + ": " + e.getMessage());
		}
	}

	@Test
	public void testError() {
		BuilderErrorHandler handler = new BuilderErrorHandler();
		try {
			handler.error(new SAXParseException(null, null, null, 0, 0));
			fail("Error should throw a SAXException, but did not");
		} catch (SAXException spe) {
			//good
		} catch (Exception e) {
			fail("Error should throw a SAXException, but got " + e.getClass() + ": " + e.getMessage());
		}
	}

	@Test
	public void testFatalError() {
		BuilderErrorHandler handler = new BuilderErrorHandler();
		try {
			handler.fatalError(new SAXParseException(null, null, null, 0, 0));
			fail("Error should throw a SAXException, but did not");
		} catch (SAXException spe) {
			//good
		} catch (Exception e) {
			fail("Error should throw a SAXException, but got " + e.getClass() + ": " + e.getMessage());
		}
	}

}
