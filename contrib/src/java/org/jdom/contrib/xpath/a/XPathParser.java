package org.jdom.contrib.xpath.a;

import org.jdom.contrib.xpath.XPathParseException;

/**
 * Interface for an XPath parser.
 *
 * @author Michael Hinchey
 * @version 1.0
 */
public interface XPathParser {

  /**
   *
   */
  void setXPathHandler(XPathHandler handler);

  /**
   *
   */
  XPathHandler getXPathHandler();

  /**
   *
   */
  void parse(String xpath) throws XPathParseException;

  /**
   *
   */
  void setXPathPredicateParser(XPathPredicateParser parser);

  /**
   *
   */
  XPathPredicateParser getXPathPredicateParser();

}

