package org.jdom.contrib.xpath.a;

import org.jdom.contrib.xpath.XPathParseException;

/**
 * Receives events as an XPathParser is parsing, when each token
 * starts and ends and a few methods describing the steps.
 *
 * @author Michael Hinchey
 * @version 1.0
 */
public interface XPathHandler {

  /**
   * Called before any parsing begins.
   * @see #endParsing()
   */
  void startParsingXPath() throws XPathParseException;

  /**
   * Called after any parsing is finished.
   * <p>Should this be called after an error? (Hinchey)</p>
   * @see #startParsing()
   */
  void endParsingXPath() throws XPathParseException;

  /**
   * Since an xpath can be composed of multiple paths separated
   * by '|' characters, this is called before each one.
   * @see #endPath()
   */
  void startPath() throws XPathParseException;

  /**
   * Called after parsing each path among one or more
   * @see #startPath()
   */
  void endPath() throws XPathParseException;

  /**
   * The path which just started to be parsed is absolute.
   * <p>This means the context node should be changed to the root node.</p>
   * <p>Called after startPath() and before any startStep().</p>
   */
  void absolute() throws XPathParseException;

  /**
   * Each path may have one or more steps.
   * <p>Special note: Since <b><code>//</code></b> is the abbreviated
   * form of <b><code>/descendant-or-self::node()/</code></b>,
   * the event sequence will be startStep followed by endStep
   * with no axis, nodetest or predicates.</p>
   * @see #endStep()
   */
  void startStep() throws XPathParseException;

  /**
   * The nametest for a step.
   * <p>Called after startStep() and before endStep().
   * A step may only have one nametest or a nodetype.</p>
   * @param axis The axis for a step (double colons not included).
   * @param prefix The namespace prefix if any, or null.
   * @param localName The nodetest without a namespace prefix.
   */
  void nametest(String axis, String prefix, String localName) throws XPathParseException;

  /**
   * A nodetype for a step.
   * <p>Called after startStep() and before endStep().
   * A step may only have one nametest or a nodetype.</p>
   * @param nodetype Type of node.
   * @param literal A literal given in parenthesis for the nodetype, or null.
   *   <a href="http://www.w3.org/TR/xpath#NT-Literal">Literal</a>
   */
  void nodetype(String axis, String nodetype, String literal) throws XPathParseException;

  /**
   * Is called at the beginning of every predicate expression.
   * <p>Called after startStep(), and after axis() and
   * after nodetest() if they are called at all, and before endStep().
   * A step may zero or more predicates.</p>
   * <p>Whether this is a new handler, a reused one, or the
   * object itself is up to the implementation, of course.</p>
   */
  XPathPredicateHandler startXPathPredicate();

  /**
   * Is called at the end of every predicate expression.
   */
  void endXPathPredicate(XPathPredicateHandler handler);

  /**
   * The end of a step, before the next startStep() if any.
   * @see #startStep()
   */
  void endStep() throws XPathParseException;

}

