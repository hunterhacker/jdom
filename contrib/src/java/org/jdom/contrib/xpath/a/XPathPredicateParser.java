package org.jdom.contrib.xpath.a;

import org.jdom.contrib.xpath.XPathParseException;

/**
 * Parser for a predicate expression.
 *
 * @author Michael Hinchey (michael.hinchey@inference.com)
 * @version 1.0
 */
public interface XPathPredicateParser {

  /**
   *
   */
  void setXPathPredicateHandler(XPathPredicateHandler handler);

  /**
   *
   */
  XPathPredicateHandler getXPathPredicateHandler();

  /**
   * Parse the xpath expression, send events to the handler.
   */
  void parsePredicate(String predicate) throws XPathParseException;

  /**
   *
   */
  void setXPathParser(XPathParser xpathParser);

  /**
   *
   */
  XPathParser getXPathParser();

}

