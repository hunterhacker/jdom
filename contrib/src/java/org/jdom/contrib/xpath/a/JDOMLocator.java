package org.jdom.contrib.xpath.a;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import org.jdom.*;
import org.jdom.contrib.xpath.XPathElement;
import org.jdom.contrib.xpath.Keywords;
import org.jdom.contrib.xpath.XPathParseException;

/**
 * Recieves events as an XPathParser is parsing and
 * matches nodes in the JDOM tree.
 *
 * @author Michael Hinchey
 * @version 1.0
 */
public class JDOMLocator implements XPathHandler {

  public static boolean DEBUG = XPathElement.DEBUG;

  private List matchList;

  private List pathList;

  private List stepList;

  private Element originalContext;

  private XPathParser parser;

  private boolean absolute = false;

  /**
   * @see #elemext(Element)
   */
  private XPathElement reusableXPathElement;

  /**
   * Set the list to hold matched objects.
   */
  public void setMatchList(List matchList) {
    this.matchList = matchList;
  }

  /**
   * Set the context element against which to match the xpath.
   */
  public void setContextElement(Element context) {
    this.originalContext = context;
  }

  /**
   * Override the default parser to be used.
   */
  public void setParser(XPathParser parser) {
    this.parser = parser;
  }

  /**
   * Find all objects from the context element which
   * match the <code>xpath</code>.
   * @return Contains String, {@link Element}, {@link Attribute},
   * {@link Comment}, and {@link ProcessingInstruction} objects;
   * much like {@link Element#getMixedContent()}.
   */
  public List match(String xpath) throws XPathParseException {
    DEBUG = XPathElement.DEBUG;
    println();
    if (parser == null) {
      // if client did not provide one, use a new one
      parser = new PoCParser();
    }

    parser.setXPathHandler(this);
    parser.parse(xpath);

    return matchList;
  }

  /* ------------ XPathHandler methods ------------- */

  /**
   * Check the initial state of this object is okay to proceed
   * with parsing and matching.
   * @throws XPathParseException If context is null.
   * @see XPathHandler#startParsingXPath()
   */
  public void startParsingXPath() throws XPathParseException {
    if (originalContext == null) {
      throw new XPathParseException("JDOMLocator requires a context element.");
    }
    if (matchList == null) {
      // if client did not provide one, use a new one
      matchList = new ArrayList();
    }
    pathList = new ArrayList();
    stepList = new ArrayList();
  }

  /**
   * @see XPathHandler#endParsingXPath()
   */
  public void endParsingXPath() throws XPathParseException {
    println("{/parse}");
  }

  /**
   * @see XPathHandler#startPath()
   */
  public void startPath() throws XPathParseException {
    print("{path}");
    pathList.add(originalContext);
  }

  /**
   * @see XPathHandler#endPath()
   */
  public void endPath() throws XPathParseException {
    print("{/path}");
    matchList.addAll(pathList);
    pathList.clear();
  }

  /**
   * @see XPathHandler#absolute()
   */
  public void absolute() throws XPathParseException {
    absolute = true;
    print("{absolute}");
    pathList.clear(); // remove the originalContext added by startPath()
    pathList.add( elemext(originalContext).getRootElement() );
  }

  /**
   * @see XPathHandler#startStep()
   */
  public void startStep() throws XPathParseException {
    print("{step}");
  }

  /**
   * @see XPathHandler#nametest(String,String,String)
   */
  public void nametest(String axis, String prefix, String localName) throws XPathParseException {
    nodetest(axis, null, prefix, localName);
  }

  /**
   * @see XPathHandler#nodetype(String,String,String)
   */
  public void nodetype(String axis, String nodetype, String literal) throws XPathParseException {
    nodetest(axis, nodetype, null, literal);
  }

  /**
   * @see XPathHandler#getXPathPredicateHandler()
   */
  public XPathPredicateHandler startXPathPredicate() {
    print("{pred}");
    return new JDOMLocatorPredicateHandler();
  }

  /**
   * @see XPathHandler#endXPathPredicateHandler(XPathPredicateHandler)
   */
  public void endXPathPredicate(XPathPredicateHandler handler) {
    JDOMLocatorPredicateHandler predicate = (JDOMLocatorPredicateHandler) handler;
    if (predicate.getPosition() != null) {
      List list = new ArrayList();
      if (0 < predicate.getPosition().intValue()) {
        if (stepList.size() >= predicate.getPosition().intValue()) {
          list.add(stepList.get( predicate.getPosition().intValue() - 1 ));
        }
      }
      stepList = list;
    }
    print("{/pred}");
  }

  /**
   * @see XPathHandler#endStep()
   */
  public void endStep() throws XPathParseException {
    //println();
    //print("pathList: "); println(pathList);
    //print("stepList: "); println(stepList);
    pathList = stepList;
    stepList = new ArrayList();
    print("{/step}");
  }

  /* ------------ print methods ------------- */

  /**
   * Debug print.
   */
  protected static void print(Object message) {
    if (DEBUG) { System.out.print(message); }
  }

  /**
   * Debug print.
   */
  protected static void println(Object message) {
    if (DEBUG) { System.out.println(message); }
  }

  /**
   * Debug print.
   */
  protected static void println() {
    if (DEBUG) { System.out.println(); }
  }

  /* ----------------- nodetest ----------------- */

  /**
   * @param axis Null means child.
   * @param nodetype Null means node.
   * @param prefix Namespace prefix or null.
   * @param localName Null or '*' means ALL.
   */
  private void nodetest(String axis, String nodetype, String prefix, String localName) throws XPathParseException {
    print("{nodetest=" + axis + " " + nodetype + " "
      + (prefix == null ? "" : prefix) + " "
      + (localName == null ? "" : localName)
      + "}");

    // normalize synonyms
    final int axisCode = (axis == null) ? Keywords.CHILD : Keywords.axisCode(axis);
    final int nodetypeCode = Keywords.nodetypeCode(nodetype);
    if ("*".equals(localName)) {
      localName = null;
    }

    if (axisCode == Keywords.UNRECOGNIZED) {
      throw new XPathParseException("Unrecognized axis: " + axis);
    }

    if (nodetypeCode == Keywords.UNRECOGNIZED) {
      throw new XPathParseException("Unrecognized nodetype: " + nodetype);
    }

    // after normalizing the data, the method below does the real work
    nodetest(axisCode, nodetypeCode, prefix, localName);
  }

  /**
   * Handles a nodetest; the params are already normalized and converted
   * to Keywords codes, and may not be UNRECOGNIZED.
   * @param axisCode      Keywords code for axis.
   * @param nodetypeCode  Keywords code for nodetype.
   * @param prefix        Namespace prefix or null.
   * @param localName     null means ALL.
   *
   * @see Keywords
   */
  private void nodetest(int axisCode, int nodetypeCode, String prefix, String localName) throws XPathParseException {
    for (Iterator iter = pathList.iterator(); iter.hasNext(); ) {
      Object each = iter.next();

      switch (axisCode) {
      case Keywords.ANCESTOR: {
        nodetestAncestor(each, nodetypeCode, prefix, localName, false, true);
        break;
      }
      case Keywords.ANCESTOR_OR_SELF: {
        nodetestAncestor(each, nodetypeCode, prefix, localName, true, true);
        break;
      }
      case Keywords.ATTRIBUTE: {
        nodetestAttribute(each, nodetypeCode, prefix, localName);
        break;
      }
      case Keywords.CHILD: {
        nodetestChild(each, nodetypeCode, prefix, localName);
        break;
      }
      case Keywords.DESCENDANT: {
        nodetestDescendant(each, nodetypeCode, prefix, localName, false);
        break;
      }
      case Keywords.DESCENDANT_OR_SELF: {
        nodetestDescendant(each, nodetypeCode, prefix, localName, true);
        break;
      }
      case Keywords.FOLLOWING: {
        throw new NotImplementedException("Support for axis following is not implemented. ");
        //break;
      }
      case Keywords.FOLLOWING_SIBLING: {
        throw new NotImplementedException("Support for axis following-sibling is not implemented. ");
        //break;
      }
      case Keywords.NAMESPACE: {
        nodetestNamespace(each, nodetypeCode, prefix, localName);
        break;
      }
      case Keywords.PARENT: {
        nodetestAncestor(each, nodetypeCode, prefix, localName, true, false);
        break;
      }
      case Keywords.PRECEDING: {
        throw new NotImplementedException("Support for axis preceding is not implemented. ");
        //break;
      }
      case Keywords.PRECEDING_SIBLING: {
        throw new NotImplementedException("Support for axis preceding-sibling is not implemented. ");
        //break;
      }
      case Keywords.SELF: {
        nodetestSelf(each, nodetypeCode, prefix, localName);
        break;
      }
      } // end-switch
    }
  }

  /**
   * Do nodetest for axis==descendant.
   * After, stepList contains all descendant Elements and Strings of the nodes in pathList.
   * @param self If true, also add <code>element</code> if it matches.
   */
  private void nodetestDescendant(final Object context, final int nodetypeCode, final String prefix, final String localName, final boolean self) throws XPathParseException {
    switch (nodetypeCode) {
    case Keywords.NODE:
    case Keywords.ELEMENT: {
      if (context instanceof Element) {
        stepList.addAll(elemext((Element) context).getDescendants(prefix, localName, self));
      }
      break;
    }
    } // end-switch
  }

  /**
   * Do nodetest for axis==self.
   * After, stepList contains the same nodes as pathList.
   */
  private void nodetestSelf(final Object context, final int nodetypeCode, final String prefix, final String localName) throws XPathParseException {
    switch (nodetypeCode) {
    case Keywords.NODE: {
      stepList.add(context);
      break;
    }
    case Keywords.ELEMENT: {
      if (context instanceof Element) {
        stepList.add(context);
      }
      break;
    }
    case Keywords.COMMENT: {
      if (context instanceof Comment) {
        stepList.add(context);
      }
      break;
    }
    case Keywords.TEXT: {
      if (context instanceof String) {
        stepList.add(context);
      }
      break;
    }
    case Keywords.PROCESSING_INSTRUCTION: {
      if (context instanceof ProcessingInstruction) {
        if (localName == null) {
          stepList.add(context);
        } else {
          ProcessingInstruction pi = (ProcessingInstruction) context;
          if (localName.equals( pi.getTarget() )) {
            stepList.add(context);
          }
        }
      }
      break;
    }
    } // end-switch
  }

  /**
   * Do nodetest for axis==ancestor.
   * <p>Only makes sense for nodetype==NODE or nodetype==ELEMENT</p>
   * @param self If true, also add <code>element</code> if it matches.
   */
  private void nodetestAncestor(final Object context, final int nodetypeCode,
  final String prefix, final String localName, final boolean self,
  final boolean recursive) throws XPathParseException {
    if ((nodetypeCode == Keywords.ELEMENT) || (nodetypeCode == Keywords.NODE)) {
      if (context instanceof Element) {
        Element element = (Element) context;
        if (self == true) {
          stepList.add(element);
        }
        if (recursive) {
          stepList.addAll( elemext(element).getAncestors() );
        } else {
          stepList.add( element.getParent() );
        }
      }
    }
  }

  /**
   * Do nodetest for axis==child.
   */
  private void nodetestChild(final Object context, final int nodetypeCode, final String prefix, final String localName) throws XPathParseException {
    if ((context instanceof Element) == false) {
      return;
    }
    Element element = (Element) context;
    switch (nodetypeCode) {
    case Keywords.NODE: {
      if (localName == null) {
        stepList.addAll(element.getMixedContent());
      } else {
        throw new XPathParseException("LocalName for nodes is meaningless.  (localName=" + localName + ")");
      }
      break;
    }
    case Keywords.ELEMENT: {
      stepList.addAll(elemext(element).getChildren(prefix, localName) );
      break;
    }
    case Keywords.COMMENT: {
      if (localName == null) {
        stepList.addAll(elemext(element).getComments());
      } else {
        throw new XPathParseException("LocalName for comments is meaningless.  (localName=" + localName + ")");
      }
      break;
    }
    case Keywords.TEXT: {
      if (localName == null) {
        stepList.addAll(elemext(element).getTextChildren());
      } else {
        throw new XPathParseException("LocalName for text is meaningless.  (localName=" + localName + ")");
      }
      break;
    }
    case Keywords.PROCESSING_INSTRUCTION: {
      if (localName == null) {
        stepList.addAll(elemext(element).getProcessingInstructions());
      } else {
        stepList.addAll(elemext(element).getProcessingInstructions(localName));
      }
      break;
    }
    } // end-switch
  }

  /**
   * Do nodetest for axis==namespace.
   * <p>Only makes sense for nodetype==NODE or nodetype==ELEMENT</p>
   */
  private void nodetestNamespace(final Object context, final int nodetypeCode, final String prefix, final String localName) throws XPathParseException {
    if ((nodetypeCode == Keywords.ELEMENT) || (nodetypeCode == Keywords.NODE)) {
      if (context instanceof Element) {
        Element element = (Element) context;
        stepList.addAll( elemext(element).getNamespaces() );
      }
    }
  }

  /**
   * Do nodetest for axis==attribute.
   */
  private void nodetestAttribute(final Object context, final int nodetypeCode, final String prefix, final String localName) throws XPathParseException {
    if ((context instanceof Element) == false) {
      return;
    }
    Element element = (Element) context;
    switch (nodetypeCode) {
    case Keywords.ELEMENT: {
      if (localName == null) {
        print("*");
        stepList.addAll(element.getAttributes());
      } else {
        print("#");
        Object attr = elemext(element).getAttribute(prefix, localName);
        if (attr != null) {
          stepList.add(attr);
        }
      }
      break;
    }
    default:
      throw new XPathParseException("Cannot have axis=ATTRIBUTE for any other than nodetype=ELEMENT.");
    }
  }

  /**
   * Returns a reusable XPathElement object.
   */
  private XPathElement elemext(Element element) {
    if (reusableXPathElement == null) {
      reusableXPathElement = new XPathElement(element);
    } else {
      reusableXPathElement.extend(element);
    }
    return reusableXPathElement;
  }

  /**
   * Recieves events as an XPathParser is parsing a predicate expression.
   *
   * @author Michael Hinchey
   * @version 1.0
   */
  class JDOMLocatorPredicateHandler
   extends DefaultHandler
   implements XPathPredicateHandler {

    private Integer position = null;

    JDOMLocatorPredicateHandler() {
      super.DEBUG = XPathElement.DEBUG;
    }

    Integer getPosition() {
      return position;
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

    /**
     *
     */
    public void number(String number) throws XPathParseException {
      this.print("{number=" + number + "}");
      if (number.indexOf('.') == -1) {
        position = new Integer(number);
      } else {
        throw new NotImplementedException("Support for position numbers only are implemented. " + number);
      }
    }

  }

  /**
   * Temporary class for use while package is in development.
   *
   * @author Michael Hinchey
   */
  class NotImplementedException extends XPathParseException {
    public NotImplementedException(String message) {
      super(message);
    }
    public NotImplementedException(String message, Throwable rootCause) {
      super(message, rootCause);
    }
  }

}

