
package org.jdom.contrib.xpath.impl;

import org.jdom.contrib.xpath.XPathExpr;
import org.jdom.contrib.xpath.Context;
import org.jdom.contrib.xpath.NodeSet;
import org.jdom.contrib.xpath.XPathParseException;

public class XPathExprImpl implements XPathExpr
{

  public XPathExprImpl()
  {

  }

  public Object apply(Context context)
  {
    Object result = null;

    return result;
  }

  /**
   * Default implementation: no-op.
   */
  public void apply(NodeSet nodeset) throws XPathParseException {
  }

}
