package org.jdom.contrib.xpath.a;

import java.util.StringTokenizer;
import org.jdom.contrib.xpath.XPathParseException;

/**
 * Proof-of-Concept XPath parser.
 *
 * <p>This parser is not fully optimized,
 * nor is it <b>yet</b> 100% correct.</p>
 *
 * @author Michael Hinchey
 * @version 1.0
 */
public class PoCParser implements XPathParser {
  private XPathHandler handler;
  private XPathPredicateParser predicateParser;

  /**
   *
   */
  public void setXPathHandler(XPathHandler handler) {
    this.handler = handler;
  }

  /**
   *
   */
  public XPathHandler getXPathHandler() {
    return handler;
  }

  /**
   * Parse the xpath, send events to the handler.
   */
  public void parse(String xpath) throws XPathParseException {
    if (handler == null) {
      throw new XPathParseException("Must set a XPathHandler first.  See XPathParser.setXPathHandler(XPathHandler).");
    }

    try {
      handler.startParsingXPath();
      assertValidCharacters(xpath);
      parseWhole(xpath);
    }
    finally {
      handler.endParsingXPath();
    }
  }

  /**
   *
   */
  public void setXPathPredicateParser(XPathPredicateParser parser) {
    predicateParser = parser;
  }

  /**
   *
   */
  public XPathPredicateParser getXPathPredicateParser() {
    return predicateParser;
  }

  /**
   * Check for invalid characters.
   */
  protected void assertValidCharacters(String xpath) throws XPathParseException {
    if (xpath == null) {
      throw new XPathParseException("xpath cannot be null");
    }
    xpath = xpath.trim();
    if ("".equals(xpath)) {
      throw new XPathParseException("xpath cannot be empty");
    }
  }

  /**
   * Parse and entire xpath, which can consist of multiple path
   * expressions OR'ed together.
   * <p>WARNING: This needs to be more intelligent because '|'
   * can be nested within predicates.</p>
   */
  private void parseWhole(final String xpath) throws XPathParseException {
    if (xpath.indexOf('|') == -1) {
      parseLocationPath(xpath);
    } else {
      for ( StringTokenizer st = new StringTokenizer(xpath, "|"); st.hasMoreTokens(); ) {
        parseLocationPath(st.nextToken());
      }
    }
  }

  /**
   * Parse a single xpath, with no OR's.
   * <p>WARNING: This needs to be more intelligent because '/'
   * can be nested within predicates.</p>
   */
  private void parseLocationPath(String xpath) throws XPathParseException {
    handler.startPath();
    xpath = xpath.trim();
    if (xpath.charAt(0) == '/') {
      handler.absolute();
      xpath = xpath.substring(1);
    }

    for (int i = xpath.indexOf('/'); i != -1; i = xpath.indexOf('/') ) {
      if (i == 0) {
        // double-slash means abbrevation for "/descendant-or-self::node()/"
        parseStep(null);
      } else {
        parseStep( xpath.substring(0, i));
      }
      xpath = xpath.substring(i + 1);
    }
    parseStep(xpath);

    handler.endPath();
  }

  /**
   *
   */
  private void parseStep(String xstep) throws XPathParseException {
    handler.startStep();
    if (xstep == null) {
      // double-slash means abbrevation for "/descendant-or-self::node()/"
      // This hides the abbreviation from the handler.
      // Should the parser hide abbreviations or just send an empty step?
      // Currently, no other abbreviations are hidden by the parser.
      //handler.axis("descendant-or-self");
      //handler.nodetype("node", null);
    } else {
      xstep = xstep.trim();
      int indexPredicate = xstep.indexOf('[');
      if (-1 == indexPredicate) {
        parseNodeTest(xstep);
      } else {
        parseNodeTest(xstep.substring(0, indexPredicate));
        parsePredicates(xstep.substring(indexPredicate));
      }
    }
    handler.endStep();
  }

  /**
   *
   */
  private void parseNodeTest(String nodetest) throws XPathParseException {
    int indexAxis = nodetest.indexOf("::");
    String axis = null;
    if (-1 != indexAxis) {
      // it has an unabbreviated axis ...
      axis = nodetest.substring(0, indexAxis);
      nodetest = nodetest.substring(indexAxis + 2, nodetest.length());
    }

    int indexOpenParen = nodetest.indexOf('(');
    if (indexOpenParen == -1) {
      int indexColon = nodetest.indexOf(':');
      if (indexColon == -1) {
        handler.nametest(axis, null, nodetest);
      } else {
        handler.nametest(axis, nodetest.substring(0, indexColon),
          nodetest.substring(indexColon + 1));
      }
    } else {
      int indexCloseParen = nodetest.indexOf(')');
      if (indexCloseParen == -1) {
        throw new XPathParseException("Closing parenthesis expected: " + nodetest);
      }
      if (indexCloseParen < indexOpenParen) {
        throw new XPathParseException("Closing parenthesis found before opening: " + nodetest);
      }
      handler.nodetype(axis, nodetest.substring(0, indexOpenParen),
        ParseUtils.extractLiteral(nodetest.substring(
          indexOpenParen + 1, indexCloseParen)
        )
      );
    }
  }

  /**
   * Parse a set of predicates - there can be multiple one after the other
   * or nested within each other.
   * This expects to have the brackets still in place.
   * <p>WARNING: This needs to be more intelligent because predicates
   * can be nested within predicates.</p>
   */
  private void parsePredicates(String string) throws XPathParseException {
    for (int i = string.indexOf('['); i != -1; i = string.indexOf('[') ) {
      int indexEnd = string.indexOf(']');
      parsePredicate( string.substring(i + 1, indexEnd) );
      string = string.substring(indexEnd + 1);
    }
  }

  /**
   * Parse a single predicate expression.
   * The outer brackets are expected to be removed.
   * May contain nested predicates.
   */
  private void parsePredicate(String predicate) throws XPathParseException {
    XPathPredicateHandler predicateHandler = handler.startXPathPredicate();
    if (predicateParser == null) {
      predicateParser = new PoCPredicateParser();
    }
    if (predicateParser.getXPathParser() == null) {
      predicateParser.setXPathParser(this);
    }
    predicateParser.setXPathPredicateHandler(predicateHandler);
    predicateParser.parsePredicate(predicate);
    handler.endXPathPredicate(predicateHandler);
  }

}


/**
 * Various parsing functions.
 *
 * @author Michael Hinchey
 * @version 1.0
 */
class ParseUtils {

  /**
   * Return the first String found within quotes.
   * <p>WARNING: Is it neccessary to check
   *   for &quot; and &apos;? That is not being done!</p>
   * @return The first quoted string (with quotes removed) or null.
   */
  public static String extractLiteral(String string) {
    char quote = '\''; // first, try with a single quote
    int index = string.indexOf(quote);    // index of first quote

    if (index == -1) {
      quote = '"'; // try with a double quote
      index = string.indexOf(quote);    // index of first quote
      if (index == -1) {
        return null;
      }
    }

    // remove everthing before the first quote
    string = string.substring(index + 1, string.length());

    // System.out.print("(" + string + ")");
    index = string.indexOf(quote);    // index of the next quote

    if (index == -1) {
      return null;
    }

    // remove everything following the next quote
    string = string.substring(0, index);

    return string;
  }

}

