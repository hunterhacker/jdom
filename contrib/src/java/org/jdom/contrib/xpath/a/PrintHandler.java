package org.jdom.contrib.xpath.a;

import org.jdom.contrib.xpath.XPathParseException;

/**
 * For all events from an XPathParser, print them out in
 * a nice format to help debug a parser.
 *
 * @author Michael Hinchey
 * @version 1.0
 */
public class PrintHandler implements XPathHandler, XPathPredicateHandler {

  public static boolean DEBUG = true;

  /* ------------ XPathHandler methods ------------- */

  /**
   * @see XPathHandler#startParsingXPath()
   */
  public void startParsingXPath() throws XPathParseException {
    print("{parse}");
  }

  /**
   * @see XPathHandler#endParsingXPath()
   */
  public void endParsingXPath() throws XPathParseException {
    println("{/parse}");
  }

  /**
   * Paths may be nested within predicates.
   * @see XPathHandler#startPath()
   */
  public void startPath() throws XPathParseException {
    print("{path}");
  }

  /**
   * @see XPathHandler#endPath()
   */
  public void endPath() throws XPathParseException {
    print("{/path}");
  }

  /**
   * @see XPathHandler#absolute()
   */
  public void absolute() throws XPathParseException {
    print("{absolute}");
  }

  /**
   * @see XPathHandler#startStep()
   */
  public void startStep() throws XPathParseException {
    print("{step}");
  }

  /**
   * @see XPathHandler#nametest(String,String)
   */
  public void nametest(String axis, String prefix, String localName) throws XPathParseException {
    print("{nametest=" + axis + " " + prefix + " " + localName + "}");
  }

  /**
   * @see XPathHandler#startParsing()
   */
  public void nodetype(String axis, String nodetype, String literal) throws XPathParseException {
    print("{nodetype=" + axis + " " + nodetype + " " + (literal == null ? "" : literal) + "}");
  }

  /**
   * @see XPathHandler#getXPathPredicateHandler()
   */
  public XPathPredicateHandler startXPathPredicate() {
    return this;
  }

  /**
   * @see XPathHandler#endXPathPredicateHandler(XPathPredicateHandler)
   */
  public void endXPathPredicate(XPathPredicateHandler handler) {
  }

  /**
   * @see XPathHandler#endStep()
   */
  public void endStep() throws XPathParseException {
    print("{/step}");
  }

  /* ------------ XPathPredicateHandler methods ------------- */

  /**
   * @see XPathPredicateHandler#startPredicate()
   */
  public void startPredicate() throws XPathParseException {
    print("{predicate= ");
  }

  /**
   * @see XPathPredicateHandler#endPredicate()
   */
  public void endPredicate() throws XPathParseException {
    print("}");
  }

  public void predicate(String predicate) throws XPathParseException {
    print(predicate);
  }

  public void opcode(int opcode) throws XPathParseException {
    print("{opcode=" + opcode + "}");
  }
  public void variable(String variable) throws XPathParseException {
    print("{variable=" + variable + "}");
  }
  public void literal(String literal) throws XPathParseException {
    print("{literal=" + literal + "}");
  }
  public void number(String number) throws XPathParseException {
    print("{number=" + number + "}");
  }
  public void function(String functionName, String[] args) throws XPathParseException {
    print("{function=" + functionName);
    print("(");
    if (args != null) {
      for (int i=0; i < args.length; i++) {
        print(args[i]);
      }
    }
    print(") }");
  }

  /* ------------ print methods ------------- */

  /**
   * Debug print.
   */
  public static void print(Object message) {
    if (DEBUG) { System.out.print(message); }
  }

  /**
   * Debug print.
   */
  public static void println(Object message) {
    if (DEBUG) { System.out.println(message); }
  }

  /**
   * Debug print.
   */
  public static void println() {
    if (DEBUG) { System.out.println(); }
  }

}

