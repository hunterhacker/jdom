
package org.jdom.contrib.xpath.impl;

import org.jdom.contrib.xpath.XPathExpr;
import org.jdom.contrib.xpath.Context;

public class BinaryExpr extends XPathExprImpl
{

  // ----------------------------------------
  // Private Member Data

  private Op          _op     = null;
  private XPathExpr   _lhs    = null;
  private XPathExpr   _rhs    = null;


  // ----------------------------------------
  // Typesafe enumeration of operators 

  public static class Op {
    private Op () { }

    public final static Op UNION     = new Op();
    
    public final static Op OR        = new Op();
    public final static Op AND       = new Op();
    public final static Op EQUAL     = new Op();
    public final static Op NOT_EQUAL = new Op();
    public final static Op LT        = new Op();
    public final static Op GT        = new Op();
    public final static Op LT_EQUAL  = new Op();
    public final static Op GT_EQUAL  = new Op();
    public final static Op MOD       = new Op();
    public final static Op DIV       = new Op();
    public final static Op PLUS      = new Op();
    public final static Op MINUS     = new Op();
    public final static Op MULTIPLY  = new Op();
  }

  // ----------------------------------------
  // Methods

  public BinaryExpr(Op op,
                    XPathExpr lhs,
                    XPathExpr rhs)
  {
    _op = op;
    _lhs = lhs;
    _rhs = rhs;
  }

  public Object apply(Context context)
  {
    Object result = null;

    return result;
  }

}
