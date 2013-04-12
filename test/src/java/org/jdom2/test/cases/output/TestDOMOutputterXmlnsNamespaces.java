package org.jdom2.test.cases.output;

/* Please run replic.pl on me ! */
/**
 * Tests that the namespace of xmlns attributes from DOMOutputter are correct
 * 
 * @author unascribed
 * @version 0.1
 */

import org.jdom2.Element;
import org.jdom2.JDOMConstants;
import org.jdom2.input.DOMBuilder;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.DOMOutputter;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.runner.JUnitCore;

@SuppressWarnings("javadoc")
public class TestDOMOutputterXmlnsNamespaces
{
    /**
     * The XML document to test - this document
     */
    private static final String XML = "<el xmlns:test=\"urn:test\" />";

    /**
     * The main method runs all the tests in the text ui
     */
    public static void main (String args[]) 
    {
        JUnitCore.runClasses(TestDOMOutputterXmlnsNamespaces.class);
    }


    /**
     * Parse the XML using DOM
     *
     * @throws Exception
     */
    @Test
    public void testDocumentBuilderSource() throws Exception
    {
        org.w3c.dom.Element element = parseDOM(XML);

        final String namespace = element.getAttributeNode("xmlns:test").getNamespaceURI();

        assertEquals("DocumentBuilder output", JDOMConstants.NS_URI_XMLNS, namespace);
    }


    /**
     * Parse the XML using JDOM, convert to DOM
     *
     * @throws Exception
     */
    @Test
    public void testJdomToDom() throws Exception
    {
        org.w3c.dom.Element element = jdomToDom(parseJDOM(XML)); // load JDOM and convert to DOM

        final String namespace = element.getAttributeNode("xmlns:test").getNamespaceURI();

        assertEquals("SAXBuilder->DOMOutputter output", JDOMConstants.NS_URI_XMLNS, namespace);
    }


    /**
     * Parse the XML using DOM, then convert to JDOM and then finally back to DOM
     *
     * @throws Exception
     */

    @Test
    public void testDomToJdomToDom() throws Exception
    {
        org.w3c.dom.Element element = jdomToDom(domToJdom(parseDOM(XML))); // load DOM, convert to JDOM and back to DOM

        final String namespace = element.getAttributeNode("xmlns:test").getNamespaceURI();

        assertEquals("DocumentBuilder->DOMBuilder->DOMOutputter output", JDOMConstants.NS_URI_XMLNS, namespace);
    }


    private Element parseJDOM(String xml) throws Exception
    {
        final StringReader src = new StringReader(xml);

        return new SAXBuilder().build(src).getRootElement();
    }


    private org.w3c.dom.Element parseDOM(String xml) throws Exception
    {
        InputSource src = new InputSource(new StringReader(xml));

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        DocumentBuilder documentBuilder = factory.newDocumentBuilder();

        Document result = documentBuilder.parse(src);

        return result.getDocumentElement();
    }


    /**
     * Converts a JDOM Element to a DOM Element
     *
     * @param element
     *
     * @return
     *
     * @throws Exception
     */
    private org.w3c.dom.Element jdomToDom(org.jdom2.Element element) throws Exception
    {
        return new DOMOutputter().output(element);
    }


    /**
     * Converts a DOM Element to a JDOM Element
     *
     * @param element
     *
     * @return
     *
     * @throws Exception
     */
    private org.jdom2.Element domToJdom(org.w3c.dom.Element element) throws Exception
    {
        final DOMBuilder builder = new DOMBuilder();

        return builder.build(element);
    }
}