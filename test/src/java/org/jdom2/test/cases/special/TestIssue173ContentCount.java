package org.jdom2.test.cases.special;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.test.util.FidoFetch;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class TestIssue173ContentCount {

	@Test
	public void testContentSize() throws Exception {
		// In Version 1.1 this was new SAXBuilder(false)
		String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><![CDATA[TEST]]></root>";
		Document doc = new SAXBuilder(XMLReaders.NONVALIDATING).build(new StringReader(input));
		Element rootElement = doc.getRootElement();
		System.out.println("Content Size is " + rootElement.getContentSize());
		assertEquals(1, rootElement.getContentSize());
	}

}
