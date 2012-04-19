package org.jdom2.test.cases.input;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.jdom2.test.util.FidoFetch;

/**
 * This class encapsulates all the org.w3c.dom.DOM details, so that the actual
 * TestDOMBuilder class has a cleaner import * setup with just
 * JDOM imports.
 * @author rolf
 *
 */
@SuppressWarnings("javadoc")
public class HelpTestDOMBuilder {

	public static final Document getDocument(String resname, boolean xsdvalidate) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		dbf.setValidating(xsdvalidate);
		dbf.setExpandEntityReferences(false);
		
		if (xsdvalidate) {
			dbf.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
		}
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputSource is = new InputSource(FidoFetch.getFido().getURL(resname).toExternalForm());
		return db.parse(is);
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
