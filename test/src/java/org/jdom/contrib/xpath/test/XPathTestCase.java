package org.jdom.contrib.xpath.test;

import java.io.*;
import java.util.*;

import junit.framework.TestCase;
import junit.framework.Assert;
import junit.textui.TestRunner;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.contrib.xpath.XPathElement;
import org.jdom.contrib.xpath.XPathParseException;

/**
 * Test for XPath package.
 *
 * @author Michael Hinchey
 * @version 1.0
 */
public class XPathTestCase extends TestCase {

  public static final String XML1 = "<?xml version=\"1.0\"?>\n"
+"<r attr1='attr1' attr2='attr2' >root"
  +"<a attr='a-attr'>a</a>"
  +"<c>c1<cc cca='cc1a'>cc1</cc></c>"
  +"<c>c2<cc cca='cc2a'>cc2</cc></c>"
  +"<d>d<c>dc<cc>dcc</cc></c></d>"
  +"<e>e<cc>ecc</cc></e>"
  +"<r>r2</r>"
  +"<g>g<ga>ga</ga><gb>gb</gb><!-- g-commment --><?gpi1 gpi1val ?><?gpi2 gpi2val ?></g>"
  +"<!-- one-commment -->"
+"</r>"
+"";

  public static final String XML_B = "<?xml version=\"1.0\"?>\n"
+"<r>root"
  +"<b ba='b1ba'>b1<bb>bb1</bb></b>"
  +"<b ba='b2ba'>b2<bb>bb2</bb></b>"
+"</r>"
+"";

  public static final String XML_F = "<?xml version=\"1.0\"?>\n"
+"<r>root"
  +"<notf><fa><fb /></fa></notf>"
  +"<f>f<fa>fa<fgc>fgc-a</fgc><fgc2>fgc2-a</fgc2>"
    +"</fa>"
    +"<fb>fb<fgc>fgc-b</fgc><fgc2>fgc2-b</fgc2>"
    +"</fb>"
  +"</f>"
  +"<notf><fa><fb /></fa></notf>"
+"</r>"
+"";

  public static final String XML_MIX = "<?xml version=\"1.0\"?>\n"
+"<r>root"
  +"<mix>m1<mc />m2</mix>"
+"</r>"
+"";

  public static final String XML_NAMESPACE1 = "<?xml version=\"1.0\"?>\n"
+"<r>root"
  +"<namespaces>namespaces"
    +"<nsone:a xmlns:nsone='nsone-uri'>nsone-a"
    +"</nsone:a>"
  +"</namespaces>"
+"</r>"
+"";


  /**
   * For printing output.
   */
  public boolean DEBUG = false;

  /**
   * Count number of time assert is called.
   */
  public static int assertCount = 0;

  /**
   * Maps XML strings to their root elements.
   * Map of {@link String} to {@link Element}.
   */
  protected static Map rootElementMap = new HashMap();

  /**
   * Constructor required by JUnit of a TestCase.
   */
  public XPathTestCase(String name) {
    super(name);
  }

  public static void printAssertCount() {
    System.out.println("Number of asserts: " + assertCount);
  }


  /**
   * Return the root element for the given XML.
   */
  protected Element relem(final String xml) throws JDOMException {
    Element result = (Element) rootElementMap.get(xml);
    if (result == null) {
      SAXBuilder builder = new SAXBuilder();
      Document document = builder.build( new java.io.StringReader(xml) );
      result = document.getRootElement();
      rootElementMap.put(xml, result);
    }
    return result;
  }

  /************* HELPER METHODS ************************************/

  protected void matchContent(Element element, String xpath) throws Exception {
    matchContent(element, xpath, (String[])null);
  }

  protected void matchContent(Element element, String xpath, String expected) throws Exception {
    matchContent(element, xpath, new String[] {expected});
  }

  /**
   * Assert the content of the matches from xpath are equal to the
   * expected values.
   */
  protected void matchContent(Element element, String xpath, String[] expected) throws Exception {
    List matched = match(element, xpath);
    String[] actual = getContents(matched);
    print("--> [");
    print(actual, ", ");
    println("]");

    assertArrayEquals(xpath, expected, actual);
  }

  /**
   * Get the matches for the xpath.
   */
  protected List match(Element element, String xpath) throws XPathParseException {
    println("XPATH{" + xpath + "}");
    XPathElement.DEBUG = DEBUG;
    return new XPathElement(element).getMatches(xpath);
  }

  /************* HELPER METHODS ************************************/

  /**
   * deep compare on array
   */
  public void assertArrayEquals(String message, Object[] expected, Object[] actual) {
    assertEquals("{" + message + "} length", expected.length, actual.length);
    System.out.print("$");
    for (int i=0; i < actual.length; i++) {
      assertEquals("{" + message + "} i=" + i, expected[i], actual[i]);
      System.out.print("$");
    }
    println();
  }

  /**
   * Overrides Assert.
   */
  public static void assertEquals(String message, Object expected, Object actual) {
    Assert.assertEquals(message, expected, actual);
    assertCount++;
  }

  /**
   * Overrides Assert.
   */
  public static void assertEquals(String message, long expected, long actual) {
    Assert.assertEquals(message, expected, actual);
    assertCount++;
  }

  public static String[] getContents(List list) {
    String[] result = new String[ list.size() ];
    for (int i=0; i < result.length; i++ ) {
      Object each = list.get(i);
      if (each == null) {
        result[i] = "null";
      } else if (each instanceof Element) {
        Element elem = (Element) each;
        result[i] = elem.getTextTrim();
      } else if (each instanceof Attribute) {
        Attribute attr = (Attribute) each;
        result[i] = attr.getValue().trim();
      } else if (each instanceof Namespace) {
        Namespace ns = (Namespace) each;
        result[i] = ns.getURI();
      } else {
        result[i] = each.toString();
      }
    }
    return result;
  }

  public static String[] getNames(List list) {
    String[] result = new String[ list.size() ];
    for (int i=0; i < result.length; i++ ) {
      Object each = list.get(i);
      if (each == null) {
        result[i] = "null";
      } else if (each instanceof Element) {
        Element elem = (Element) each;
        result[i] = elem.getName();
      } else if (each instanceof Attribute) {
        Attribute attr = (Attribute) each;
        result[i] = attr.getName();
      } else {
        result[i] = each.toString();
      }
    }
    return result;
  }

  /**
   * Print the array.
   */
  public void print(Object[] array, String separator) {
    if (array == null) {
      print("null");
      return;
    }
    if (array.length == 0) {
      print("empty");
    }
    boolean notFirst = false;
    for ( int i=0; i < array.length; i++ ) {
      if (notFirst) {
        print(separator);
      } else if (separator != null) {
        notFirst = true;
      }
      print(array[i]);
    }
  }

  public void print(Object object) {
    if (DEBUG) {
      System.out.print(object);
    }
  }

  public void println(Object object) {
    if (DEBUG) {
      System.out.println(object);
    }
  }

  public void println() {
    if (DEBUG) {
      System.out.println();
    }
  }

}

