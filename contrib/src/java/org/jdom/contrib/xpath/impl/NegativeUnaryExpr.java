
package org.jdom.contrib.xpath.impl;

import org.jdom.contrib.xpath.XPathExpr;
import org.jdom.contrib.xpath.Context;

public class NegativeUnaryExpr extends XPathExprImpl
{
  // ----------------------------------------
  // Private members
  
  private XPathExpr _expr = null;

  // ----------------------------------------
  // Methods

  public NegativeUnaryExpr(XPathExpr expr)
  {
    _expr = expr;
  }

  public Object apply(Context context)
  {
    Object result = _expr.apply(context);

    // make it negative, safely.

    return result;
  }

}
