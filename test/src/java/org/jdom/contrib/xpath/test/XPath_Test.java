package org.jdom.contrib.xpath.test;

import java.util.List;
import junit.textui.TestRunner;

import org.jdom.*;
import org.jdom.contrib.xpath.XPathElement;
import org.jdom.contrib.xpath.XPathParseException;

/**
 * Test for XPath package.
 *
 * @author Michael Hinchey
 * @version 1.0
 */
public class XPath_Test extends XPathTestCase {

  /**
   * Run test.
   */
  public static void main(String[] args) {
    TestRunner.run(XPath_Test.class);
  }

  /**
   * Constructor required by JUnit of a TestCase.
   */
  public XPath_Test(String name) {
    super(name);
  }

  /* ************ TESTS *********************************** */

  public void test_negative() throws Exception {
    //DEBUG = true;
    try {
      matchContent(relem(XML1), null, new String[] {});
      this.fail("null not allowed for xpath");
    } catch (XPathParseException e) {
      // success
    }

    try {
      matchContent(relem(XML1), "", new String[] {});
      this.fail("empty not allowed for xpath");
    } catch (XPathParseException e) {
      // success
    }

    try {
      matchContent(relem(XML1), " ", new String[] {});
      this.fail("empty whitespace not allowed for xpath");
    } catch (XPathParseException e) {
      // success
    }
  }

  public void test_ancestor() throws Exception {
    //DEBUG = true;

    matchContent(relem(XML1), "ancestor-or-self::node()", new String[] {"root"});
    matchContent(relem(XML1), "c[1]/ancestor-or-self::node()", new String[] {"c1", "root"});
    //matchContent(element, "c/ancestor-or-self::node()", new String[] {"c1", "c2", "root"});
    Element element = relem(XML1).getChild("c");
    matchContent(element, "ancestor::node()", "root");
    matchContent(element, "ancestor-or-self::node()", new String[] {"c1", "root"});
  }

  public void test_self() throws Exception {
    //DEBUG = true;
    matchContent(relem(XML1), "self::node()", "root");

    {
      Element c = relem(XML1).getChild("c");
      matchContent(c, "self::node()", "c1");
    }
    /*{ // maybe test this later differently, starting with other than an element doesn't make sense
      Comment comment = JDOMUtil.getComment(element);
      List matches = match(comment, "self::comment()");
      this.assertEquals("match one", 1, matches.size());
      this.assertEquals("match the same object", comment, matches.get(0));
    }*/
  }

  public void test_child() throws Exception {
    //DEBUG = true;
    Element childF = relem(XML_F).getChild("f");
    matchContent(childF, "child::*", new String[] {"fa", "fb"});
    matchContent(childF, "child::fa", new String[] {"fa"});
    matchContent(childF, "child::text()", new String[] {"f"});
    matchContent(childF, "child::node()", new String[] {"f", "fa", "fb"});
  }

  public void test_child_comment() throws Exception {
    //DEBUG = true;
    Element childG = relem(XML1).getChild("g");
    Comment comment = new XPathElement(childG).getComment();
    List matches = match(childG, "child::comment()");
    assertEquals("size", 1, matches.size());
    assertEquals("match the same object", comment, matches.get(0));
  }

  public void test_child_pi() throws Exception {
    //DEBUG = true;
    Element childG = relem(XML1).getChild("g");
    List piList = new XPathElement(childG).getProcessingInstructions();
    List matches = match(childG, "child::processing-instruction()");
    assertEquals("size", 2, matches.size());
    assertEquals("match the same object", piList.get(0), matches.get(0));
    assertEquals("match the same object", piList.get(1), matches.get(1));
  }

  public void test_child_pi_named() throws Exception {
    //DEBUG = true;
    Element childG = relem(XML1).getChild("g");
    List piList = new XPathElement(childG).getProcessingInstructions("gpi1");
    List matches = match(childG, "child::processing-instruction('gpi1')");
    assertEquals("size", 1, matches.size());
    assertEquals("match the same object", piList.get(0), matches.get(0));
  }

  public void test_multi_child_steps_element1() throws Exception {
    //DEBUG = true;
    matchContent(relem(XML_F), "child::f/child::*", new String[] {"fa", "fb"});
    matchContent(relem(XML_F), "child::f/child::fa", new String[] {"fa"});
    matchContent(relem(XML_F), "child::f/child::*", new String[] {"fa", "fb"});
    matchContent(relem(XML_F), "child::f/child::fa", new String[] {"fa"});
  }

  public void test_multi_child_steps_element2() throws Exception {
    //DEBUG = true;
    matchContent(relem(XML_F), "child::f/child::fa/child::*", new String[] {"fgc-a", "fgc2-a"});
    matchContent(relem(XML_F), "child::f/child::fa/child::fgc", new String[] {"fgc-a"});
    matchContent(relem(XML_F), "child::f/child::fa/child::*", new String[] {"fgc-a", "fgc2-a"});
    matchContent(relem(XML_F), "child::f/child::fa/child::fgc", new String[] {"fgc-a"});
  }

  public void test_multi_child_steps_element3() throws Exception {
    //DEBUG = true;
    matchContent(relem(XML_F), "child::f/child::*/child::*", new String[] {"fgc-a", "fgc2-a", "fgc-b", "fgc2-b"});
    matchContent(relem(XML_F), "f/*/*", new String[] {"fgc-a", "fgc2-a", "fgc-b", "fgc2-b"});
    matchContent(relem(XML_F), "child::f/child::*/child::fgc", new String[] {"fgc-a", "fgc-b"});
  }

  /**
   * Is this valid XPath? (hinchey)
   */
  public void test_multiple_paths() throws Exception {
    //DEBUG = true;
    matchContent(relem(XML1), "c/cc|r", new String[] {"cc1", "cc2", "r2"});
  }

  public void test_axis_attribute() throws Exception {
    //DEBUG = true;
    matchContent(relem(XML1), "a/attribute::attr", "a-attr");
    matchContent(relem(XML_B), "b/attribute::ba", new String[] {"b1ba", "b2ba"});
    //matchContent(element, "b/@ba", new String[] {"b1ba", "b2ba"});
    matchContent(relem(XML_B), "b/attribute::*", new String[] {"b1ba", "b2ba"});
    matchContent(relem(XML1), "c/cc/attribute::*", new String[] {"cc1a", "cc2a"});
  }

  public void test_descendant() throws Exception {
    //DEBUG = true;
    matchContent(relem(XML_F), "f/fa/descendant::*", new String[] {"fgc-a", "fgc2-a"});
    matchContent(relem(XML_F), "f/fa/descendant-or-self::*", new String[] {"fa", "fgc-a", "fgc2-a"});
    matchContent(relem(XML_F), "f/descendant::fa", new String[] {"fa"});
  }

  public void test_namespace() throws Exception {
    //DEBUG = true;
    matchContent(relem(XML_NAMESPACE1), "namespaces/nsone:a", new String[] {"nsone-a"});
    matchContent(relem(XML_NAMESPACE1), "namespaces/nsone:a/namespace::*", new String[] {"", "nsone-uri"});
  }

  public void test_getXPathText() throws Exception {
    //XPathElement.DEBUG = true;
    assertEquals("a", new XPathElement(relem(XML1)).getTextMatch("a/child::text()"));
    assertEquals("ecc", new XPathElement(relem(XML1)).getTextMatch("e[1]/cc[1]/child::text()"));
    assertEquals("ecc", new XPathElement(relem(XML1)).getTextMatch("e/cc/text()"));
  }

  /**
   * This finds a bug in the June 9 source: SAXBuilder.java/SAXHandler#endElement().
   * Fixed in CVS code by 7/9/2000.
   */
  public void test_text_mix() throws Exception {
    matchContent(relem(XML_MIX), "mix/text()", new String[] {"m1", "m2"});
  }

  /**
   * These fail due to a bug with absolute paths. (hinchey)
   */
  public void _test_root2() throws Exception {
    //DEBUG = true;
    matchContent(relem(XML1), "/child::r/child::f/child::*", new String[] {"fa", "fb"});
    matchContent(relem(XML1), "/child::r/child::f/child::fa", new String[] {"fa"});

    matchContent(relem(XML1), "/child::r/child::f/child::fa/child::*", new String[] {"fgc-a", "fgc2-a"});
    matchContent(relem(XML1), "/child::r/child::f/child::fa/child::fgc", new String[] {"fgc-a"});

    matchContent(relem(XML1), "/child::r/child::f/child::*/child::*", new String[] {"fgc-a", "fgc2-a", "fgc-b", "fgc2-b"});
    matchContent(relem(XML_F), "/f/*/*", new String[] {"fgc-a", "fgc2-a", "fgc-b", "fgc2-b"});
    matchContent(relem(XML1), "/child::r/child::f/child::*/child::fgc", new String[] {"fgc-a", "fgc-b"});
  }

}

