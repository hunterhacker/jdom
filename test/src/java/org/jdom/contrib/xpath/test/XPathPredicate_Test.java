package org.jdom.contrib.xpath.test;

import junit.framework.TestCase;
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
public class XPathPredicate_Test extends XPathTestCase {

  /**
   * For printing output.
   */
  public boolean DEBUG = false;

  /**
   * Run test.
   */
  public static void main(String[] args) {
    TestRunner.run(XPathPredicate_Test.class);
  }

  /**
   * Constructor required by JUnit of a TestCase.
   */
  public XPathPredicate_Test(String name) {
    super(name);
  }

  public void test_predicate_position_only() throws Exception {
    //DEBUG = true;
    matchContent(relem(XML_B), "b[1]/attribute::ba", new String[] {"b1ba"});
    matchContent(relem(XML_B), "b[2]/attribute::ba", new String[] {"b2ba"});
    matchContent(relem(XML_B), "b[0]/attribute::ba", new String[] {});
  }

}

