package org.jdom.contrib.xpath.a;

import org.jdom.contrib.xpath.XPathParseException;

/**
 * Provides empty implementations for all handler methods so
 * they can be easily extened.
 *
 * @author Michael Hinchey (michael.hinchey@inference.com)
 * @version 1.0
 */
public class DefaultHandler extends PrintHandler
implements XPathHandler, XPathPredicateHandler {

  /* ------------ XPathHandler methods ------------- */

  /**
   * @see XPathHandler#startParsingXPath()
   */
  public void startParsingXPath() throws XPathParseException {
  }

  /**
   * @see XPathHandler#endParsingXPath()
   */
  public void endParsingXPath() throws XPathParseException {
  }

  /**
   * @see XPathHandler#startPath()
   */
  public void startPath() throws XPathParseException {
  }

  /**
   * @see XPathHandler#endPath()
   */
  public void endPath() throws XPathParseException {
  }

  /**
   * @see XPathHandler#absolute()
   */
  public void absolute() throws XPathParseException {
  }

  /**
   * @see XPathHandler#startStep()
   */
  public void startStep() throws XPathParseException {
  }

  /**
   * @see XPathHandler#nametest(String,String)
   */
  public void nametest(String axis, String prefix, String localName) throws XPathParseException {
  }

  /**
   * @see XPathHandler#startParsing()
   */
  public void nodetype(String axis, String nodetype, String literal) throws XPathParseException {
  }

  /**
   * Returns null.
   * @return null
   * @see XPathHandler#getXPathPredicateHandler()
   */
  public XPathPredicateHandler getXPathPredicateHandler() {
    return null;
  }

  /**
   * @see XPathHandler#endXPathPredicateHandler(XPathPredicateHandler)
   */
  public void endXPathPredicateHandler(XPathPredicateHandler handler) {
  }

  /**
   * @see XPathHandler#endStep()
   */
  public void endStep() throws XPathParseException {
  }

  /* ------------ XPathPredicateHandler methods ------------- */

  /**
   * @see XPathPredicateHandler#startPredicate()
   */
  public void startPredicate() throws XPathParseException {
  }

  /**
   * @see XPathPredicateHandler#endPredicate()
   */
  public void endPredicate() throws XPathParseException {
  }

  public void predicate(String predicate) throws XPathParseException {
  }

  public void opcode(int opcode) throws XPathParseException {
  }
  public void variable(String variable) throws XPathParseException {
  }
  public void literal(String variable) throws XPathParseException {
  }
  public void number(String variable) throws XPathParseException {
  }
  public void function(String functionName, String[] args) throws XPathParseException {
  }

}

