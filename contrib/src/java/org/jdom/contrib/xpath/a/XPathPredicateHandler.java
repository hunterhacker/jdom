package org.jdom.contrib.xpath.a;

import org.jdom.contrib.xpath.XPathParseException;

/**
 * Recieves events as an XPathParser is parsing a predicate expression.
 * <p>This was put in a separate interface from XPathHandler
 * because these expressions can be very complex.</p>
 *
 * @author Michael Hinchey
 * @version 1.0
 */
public interface XPathPredicateHandler {

  /**
   * Called before any parsing begins.
   * <p>This can happen recursively.</p>
   * @see #endExpression()
   */
  void startPredicate() throws XPathParseException;

  /**
   * Called after any parsing is finished.
   * <p>Should this be called after an error condition? (Hinchey)</p>
   * @see #startExpression()
   */
  void endPredicate() throws XPathParseException;

  /*
   * This method obviously needs to be broken up into more -
   * the expression is not parsed at all.
   * What should the other methods be?  (Hinchey)
  void predicate(String predicate) throws XPathParseException;
   */

  /*
   * Only partial work is done deciding how to break up the predicate.
   *
   */
  int OR = 1;
  int AND = 2;
  int PLUS = 3;
  int MINUS = 4;
  int EQUAL = 5;
  int NOTEQUAL = 6;
  int LT = 7;
  int LE = 8;
  int GT = 9;
  int GE = 10;
  int MULTIPLY = 11;
  int DIV = 12;
  int MOD = 13;
  int UNION = 14;

  void opcode(int opcode) throws XPathParseException;

  void variable(String variable) throws XPathParseException;
  void literal(String variable) throws XPathParseException;
  void number(String variable) throws XPathParseException;

  /**
   * Is clearly wrong as the args could be other expressions.
   */
  void function(String functionName, String[] args) throws XPathParseException;

}

