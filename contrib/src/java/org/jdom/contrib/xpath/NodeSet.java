package org.jdom.contrib.xpath;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.jdom.contrib.xpath.impl.Step;
import org.jdom.contrib.xpath.impl.Predicate;
import org.jdom.contrib.xpath.impl.Axis;
import org.jdom.contrib.xpath.impl.Nodetype;

/**
 * An ordered collection of nodes.
 *
 * @author Michael Hinchey
 * @version 1.0
 */
public abstract class NodeSet {

  /**
   * Holds the nodes of the set.
   */
  private List list = new ArrayList();

  /**
   * Constructor.
   */
  public NodeSet() {
  }

  /**
   * For debugging.
   */
  public String toString() {
    return "[NodeSet: " + list + "]";
  }

  /**
   * Get an iterator of all nodes in this set.
   */
  public Iterator iterator() {
    return list.iterator();
  }

  /**
   * Get an unmodifiable list for all nodes in this set.
   */
  public List toList() {
    return Collections.unmodifiableList(list);
  }

  /**
   * Add to the set.
   */
  public void add(Object object) {
    list.add(object);
  }

  public void apply(XPath xpath) throws XPathParseException {
    xpath.applyTo(this);
  }

  public void apply(String xpath) throws XPathParseException {
    this.apply(new XPath(xpath));
  }

  /**
   * replace list with only the root element
   */
  public abstract void applyAbsolute();

  /**
   * Apply the step's nodetest to this nodeset.
   */
  public void applyNodetest(Step step) throws XPathParseException {

    System.err.println("Applying " + step );
    if (step.isAbsolute()) {
      applyAbsolute();
    }

    List newList = new ArrayList();
    for (Iterator iter = list.iterator(); iter.hasNext(); ) {
      Object each = iter.next();

      switch (step.getAxis().getCode()) {
      case Axis.CHILD: {
        nodetestChild(each, step, newList);
        break;
      }

      // All the other cases are still in xpath.a.JDOMLocator ...

      default: {
        throw new NotImplementedException("Support for " + step.getAxis() + " is not implemented. ");
      }
      } // end-switch
    }

    list = newList; // replace the old list
  }

  /**
   * Apply the step to this nodeset.
   */
  public void applyPredicate(Predicate predicate) throws XPathParseException {
    List newList = new ArrayList();
    list = newList; // replace the old list

    throw new NotImplementedException("Support for " + predicate + " is not implemented. ");
  }

  /* ----------------- nodetest ----------------- */

  /**
   * Give modifiable access of list to subclasses.
   */
  protected List list() {
    return list;
  }

  /**
   * Do nodetest for axis==child.
   */
  protected abstract void nodetestChild(final Object context, final Step step,
  List list) throws XPathParseException;

  /**
   * If both one and two are null return true,
   * otherwise, use {@link Object#equals(Object)}.
   * <p>The purpose of this function is to simplify IF statements
   * in case one or both objects can be null.</p>
   */
  protected static boolean equals(Object one, Object two) {
    if (one == null) {
      return two == null;
    } else {
      return one.equals(two);
    }
  }

  /**
   * Temporary class for use while package is in development.
   *
   * @author Michael Hinchey
   */
  public class NotImplementedException extends XPathParseException {
    public NotImplementedException(String message) {
      super(message);
    }
    public NotImplementedException(String message, Throwable rootCause) {
      super(message, rootCause);
    }
  }

}
