package org.jdom.test.cases.output;

/* Please run replic.pl on me ! */
/**
 * Please put a description of your test here.
 * 
 * @author unascribed
 * @version 0.1
 */
import junit.framework.*;

import org.jdom.*;
import java.io.*;
import java.util.*;
import org.jdom.output.*;
import org.jdom.input.*;

public final class TestXMLOutputter
extends junit.framework.TestCase
{
	/**
	 * Resource Bundle for various testing resources
	 */
	private ResourceBundle rb = ResourceBundle.getBundle("org.jdom.test.Test");

	/**
	 * the directory where needed resource files will be kept
	 */
	private String resourceDir = "";

	/**
	 *  a directory for temporary storage of files
	 */
	private String scratchDir = "";
    /**
     *  Construct a new instance. 
     */
    public TestXMLOutputter(String name) {
        super(name);
    }
    /**
     * The main method runs all the tests in the text ui
     */
    public static void main (String args[]) 
     {
        junit.textui.TestRunner.run(suite());
    }
	/**
	 * This method is called before a test is executed.
	 */
	public void setUp() {
		resourceDir = rb.getString("test.resourceRoot");
		scratchDir = rb.getString("test.scratchDirectory");

	}
    /**
     * The suite method runs all the tests
     */
public static Test suite () {
        TestSuite suite = new TestSuite(TestXMLOutputter.class);
        return suite;
    }
    /**
     * This method is called after a test is executed.
     */
    public void tearDown() {
        // your code goes here.
    }


    public void test_HighSurrogatePair() throws JDOMException, IOException {
      SAXBuilder builder = new SAXBuilder();
      builder.setExpandEntities(true);
      Document doc = builder.build(new StringReader("<?xml version=\"1.0\"?><root>&#x10000; &#x10000;</root>"));
      Format format = Format.getCompactFormat().setEncoding("ISO-8859-1");
      XMLOutputter outputter = new XMLOutputter(format);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      outputter.output(doc, baos);
      String xml = baos.toString();
      assertEquals("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + format.getLineSeparator() +
                   "<root>&#x10000; &#x10000;</root>" + format.getLineSeparator(), xml);
    }

    public void test_HighSurrogatePairDecimal() throws JDOMException, IOException {
      SAXBuilder builder = new SAXBuilder();
      builder.setExpandEntities(true);
      Document doc = builder.build(new StringReader("<?xml version=\"1.0\"?><root>&#x10000; &#65536;</root>"));
      Format format = Format.getCompactFormat().setEncoding("ISO-8859-1");
      XMLOutputter outputter = new XMLOutputter(format);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      outputter.output(doc, baos);
      String xml = baos.toString();
      assertEquals("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + format.getLineSeparator() +
                   "<root>&#x10000; &#x10000;</root>" + format.getLineSeparator(), xml);
    }

    // Construct a raw surrogate pair character and confirm it outputs hex escaped
    public void test_RawSurrogatePair() throws JDOMException, IOException {
      SAXBuilder builder = new SAXBuilder();
      builder.setExpandEntities(true);
      Document doc = builder.build(new StringReader("<?xml version=\"1.0\"?><root>\uD800\uDC00</root>"));
      Format format = Format.getCompactFormat().setEncoding("ISO-8859-1");
      XMLOutputter outputter = new XMLOutputter(format);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      outputter.output(doc, baos);
      String xml = baos.toString();
      assertEquals("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + format.getLineSeparator() +
                   "<root>&#x10000;</root>" + format.getLineSeparator(), xml);
    }

    // Construct a raw surrogate pair character and confirm it outputs hex escaped, when UTF-8 too
    public void test_RawSurrogatePairUTF8() throws JDOMException, IOException {
      SAXBuilder builder = new SAXBuilder();
      builder.setExpandEntities(true);
      Document doc = builder.build(new StringReader("<?xml version=\"1.0\"?><root>\uD800\uDC00</root>"));
      Format format = Format.getCompactFormat().setEncoding("UTF-8");
      XMLOutputter outputter = new XMLOutputter(format);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      outputter.output(doc, baos);
      String xml = baos.toString();
      assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + format.getLineSeparator() +
                   "<root>&#x10000;</root>" + format.getLineSeparator(), xml);
    }

    // Construct illegal XML and check if the parser notices
    public void test_ErrorSurrogatePair() throws JDOMException, IOException {
      SAXBuilder builder = new SAXBuilder();
      builder.setExpandEntities(true);
      Document doc = builder.build(new StringReader("<?xml version=\"1.0\"?><root></root>"));
      try {
        doc.getRootElement().setText("\uD800\uDBFF");
        fail("Illegal surrogate pair should have thrown an exception");
      }
      catch (IllegalDataException e) {
      }
    }

    // Manually construct illegal XML and make sure the outputter notices
    public void test_ErrorSurrogatePairOutput() throws JDOMException, IOException {
      SAXBuilder builder = new SAXBuilder();
      builder.setExpandEntities(true);
      Document doc = builder.build(new StringReader("<?xml version=\"1.0\"?><root></root>"));
      Text t = new UncheckedJDOMFactory().text("\uD800\uDBFF");
      doc.getRootElement().setContent(t);
      Format format = Format.getCompactFormat().setEncoding("ISO-8859-1");
      XMLOutputter outputter = new XMLOutputter(format);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      try {
        outputter.output(doc, baos);
        fail("Illegal surrogate pair output should have thrown an exception");
      }
      catch (IllegalDataException e) {
      }
    }
}
