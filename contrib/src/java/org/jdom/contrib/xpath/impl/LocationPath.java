
package org.jdom.contrib.xpath.impl;

import org.jdom.contrib.xpath.XPathExpr;
import org.jdom.contrib.xpath.Context;

import java.util.List;
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
    Iterator resultIter = null;

    Context current = null;
    Step step = null;

    List results = new Vector();
    List tmpResults = null;
    List nextResults = null;

    results.add(context);

    while (stepIter.hasNext())
    {
      step = (Step) stepIter.next();

      resultIter = results.iterator();
      tmpResults = new Vector();

      while (resultIter.hasNext())
      {
        current = (Context) resultIter.next();
        
        tmpResults.addAll(current.walkStep(step.getAxis(),
                                           step.getNodeTest()));
      }

      results = tmpResults;
    }

    return results;
  }

}
