package org.jdom.contrib.xpath.a;

import java.util.StringTokenizer;
import org.jdom.contrib.xpath.XPathParseException;

/**
 * Parser for a predicate expression.
 *
 * <p>Need to make this class smarter and extend the handler interface.</p>
 *
 * @author Michael Hinchey (michael.hinchey@inference.com)
 * @version 1.0
 */
public class PoCPredicateParser implements XPathPredicateParser {

  private static final String DELIM = " ,([/'\"*+=$";

  private XPathPredicateHandler handler;

  private XPathParser xpathParser;

  /**
   *
   */
  public void setXPathPredicateHandler(XPathPredicateHandler handler) {
    this.handler = handler;
  }

  /**
   *
   */
  public XPathPredicateHandler getXPathPredicateHandler() {
    return handler;
  }

  /**
   *
   */
  public void setXPathParser(XPathParser xpathParser) {
    this.xpathParser = xpathParser;
  }

  /**
   *
   */
  public XPathParser getXPathParser() {
    return xpathParser;
  }

  /**
   * Parse the xpath expression, send events to the handler.
   */
  public void parsePredicate(String predicate) throws XPathParseException {
    handler.startPredicate();
    //handler.predicate(predicate);
    parse(predicate);
    handler.endPredicate();
  }

  private void parse(String predicate) throws XPathParseException {
    for ( StringTokenizer st = new StringTokenizer(predicate, DELIM, true); st.hasMoreTokens(); ) {
      String token = st.nextToken();
      char delim = 0;
      if (st.hasMoreTokens()) {
        delim = st.nextToken().charAt(0);
        //System.out.print("<" + token + " " + delim + ">");
      } else {
        parseThing(token);
        //System.out.print("<" + token + ">");
        //handler.number(token);
        return;
      }
      switch (delim) {
      case ' ': {
        if ("or".equals(token)) {
          handler.opcode(XPathPredicateHandler.OR);
        } else if ("and".equals(token)) {
          handler.opcode(XPathPredicateHandler.AND);
        } else {
          parseThing(token);
          //System.out.print("{PocParse unknown: " + token + " " + delim + "}");
        }
        break;
      }
      case '(': {
        handler.function(token, new String[] {st.nextToken(")")} );
        break;
      }
      case '\'': {
        handler.literal( st.nextToken("'") );
        break;
      }
      case '"': {
        handler.literal( st.nextToken("\"") );
        break;
      }
      case '$': {
        handler.variable(st.nextToken());
        break;
      }
      case '=': {
        System.out.print("{PocParse unknown: " + token + " " + delim + "}");
        break;
      }
      case '+': {
        System.out.print("{PocParse unknown: " + token + " " + delim + "}");
        break;
      }
      case '*': {
        System.out.print("{PocParse unknown: " + token + " " + delim + "}");
        break;
      }
      case ',': {
        System.out.print("{PocParse unknown: " + token + " " + delim + "}");
        break;
      }
      case '[': {
        System.out.print("{PocParse unknown: " + token + " " + delim + "}");
        break;
      }
      case '/': {
        System.out.print("{PocParse unknown: " + token + " " + delim + "}");
        break;
      }
      default:
        throw new XPathParseException("Internal error: " + delim + " not expected parsing predicate.");
      }
    }
  }

  private void parseThing(String thing) throws XPathParseException {
    if ((thing.charAt(0) >= '0') && (thing.charAt(0) <= '9')) {
      handler.number(thing);
    } else {
      //System.out.print("{PocParse path: " + thing + "}");
    }
  }

}

