
package org.jdom.contrib.xpath.impl;

import org.jdom.contrib.xpath.XPathExpr;

public class Predicate
{

  // ----------------------------------------
  // Private members

  private XPathExpr _expr   = null;

  // ----------------------------------------
  // Methods

  public Predicate(XPathExpr expr)
  {
    _expr = expr;
  }

}
