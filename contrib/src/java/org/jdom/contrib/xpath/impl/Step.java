/*
  See LICENSE.txt
*/

package org.jdom.contrib.xpath.impl;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import org.jdom.contrib.xpath.NodeSet;
import org.jdom.contrib.xpath.XPathParseException;

/**
 * A single step in an xpath.
 *
 * @author Bob McWhirter
 * @author Michael Hinchey
 * @version 1.0
 */
public class Step extends XPathExprImpl {

  private boolean         isAbsolute = false;
  private Axis            axis       = null;
  private Nodetype        nodetype   = null;
  private String          prefix     = null;
  private String          localName  = null;
  private List            predicates = new ArrayList();

  /**
   * Constructor
   * @param axis Null means child.
   * @param nodetype Null means node.
   * @param prefix Namespace prefix or null.
   * @param localName Null or '*' means ALL.
   */
  public Step(String axis,
              String nodetype,
              String prefix,
              String localName) {

    this.axis = new Axis(axis);
    this.nodetype = new Nodetype(nodetype);
    this.prefix = prefix;
    this.localName = localName;
  }

  /**
   * For debugging.
   */
  public String toString() {
    return "[Step: "
      + (isAbsolute ? "/" : "")
      + axis
      + nodetype
      + prefix + ":" + localName
      + "]";
  }

  public void setAbsolute(boolean isAbsolute) {
    this.isAbsolute = isAbsolute;
  }

  public boolean isAbsolute() {
    return isAbsolute;
  }

  public void addPredicate(Predicate pred) {
    predicates.add(pred);
  }

  public Axis getAxis() {
    return axis;
  }

  public Nodetype getNodetype() {
    return nodetype;
  }

  public String getPrefix() {
    return prefix;
  }

  public String getLocalName() {
    return localName;
  }

  public List getPredicates() {
    return predicates;
  }

  /**
   * Apply self to given nodeset, to which modifications may apply.
   */
  public void apply(NodeSet nodeset) throws XPathParseException {

    System.out.println("before nodetest: " + nodeset);
    nodeset.applyNodetest(this);
    System.out.println("after nodetest: " + nodeset);

    // apply predicates
    for (Iterator iter = predicates.iterator(); iter.hasNext(); ) {
      Predicate each = (Predicate) iter.next();
      nodeset.applyPredicate(each);
      System.out.println("after " + each + ": " + nodeset);
    }
  }

}
