package org.jdom2.test.cases.input;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * This class encapsulates all the org.w3c.dom.DOM details, so that the actual
 * TestDOMBuilder class has a cleaner import * setup with just
 * JDOM imports.
 * @author rolf
 *
 */
public class HelpTestDOMBuilder {

	public static final Document getDocument(String filename) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		dbf.setValidating(false);
		dbf.setExpandEntityReferences(false);
		DocumentBuilder db = dbf.newDocumentBuilder();
		return db.parse(filename);
	}
	
	public static final Element getRoot(Document doc) {
		Node n = doc.getFirstChild();
		while (n != null) {
			if (n instanceof Element) {
				return (Element)n;
			}
			n = n.getNextSibling();
		}
		return null;
	}
}
