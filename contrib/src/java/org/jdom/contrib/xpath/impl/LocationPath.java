
package org.jdom.contrib.xpath.impl;

import org.jdom.contrib.xpath.XPathExpr;
import org.jdom.contrib.xpath.Context;

import java.util.Vector;
import java.util.Iterator;

public class LocationPath extends PathExpr
{

  // ----------------------------------------
  // Private members
  
  private boolean _isAbsolute   = false;
  private Vector  _steps        = new Vector();


  // ----------------------------------------
  // Methods members

  public LocationPath()
  {
  }

  public void isAbsolute(boolean isAbsolute)
  {
    _isAbsolute = isAbsolute;
  }

  public boolean isAbsolute()
  {
    return _isAbsolute;
  }

  public void addStep(Step step)
  {
    _steps.add(step);
  }

  public Object apply(Context context)
  {
    Object result = null;

    Iterator stepIter = _steps.iterator();

    Context current = context;

    while (stepIter.hasNext())
    {
      // walk'em?
      // context.walkStep( (Step) stepIter.next() )
    }

    return result;
  }

}
