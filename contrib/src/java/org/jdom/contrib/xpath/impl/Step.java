
package org.jdom.contrib.xpath.impl;

import java.util.Vector;

public class Step
{
  // ----------------------------------------
  // Private members

  private String _axis         = null;
  private String _nodeTest     = null;
  private Vector _predicates   = new Vector();

  // ----------------------------------------
  // Methods

  public Step(String nodeTest)
  {
    _nodeTest = nodeTest;
  }

  public Step(String axis,
              String nodeTest)
  {
    _axis = axis;
    _nodeTest = nodeTest;
  }

  public void addPredicate(Predicate pred)
  {
    _predicates.add(pred);
  }

  public String getNodeTest()
  {
    return _nodeTest;
  }

  public String getAxis()
  {
    return _axis;
  }
}
