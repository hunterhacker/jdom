/*
 * See LICENSE.txt
 */

package org.jdom.contrib.xpath.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.jdom.contrib.xpath.XPathExpr;
import org.jdom.contrib.xpath.Context;
import org.jdom.contrib.xpath.NodeSet;
import org.jdom.contrib.xpath.XPathParseException;

/**
 * XPath location path contains a number of steps and if the path is absolute.
 *
 * @author Bob McWhirter
 * @author Michael Hinchey
 * @version 1.0
 */
public class LocationPath extends PathExpr {

  // ----------------------------------------
  // Private members

  private boolean _isAbsolute   = false;

  /**
   * Ordered list of {@link Step}s.
   */
  private ArrayList  _steps        = new ArrayList();


  // ----------------------------------------
  // Methods members

  public LocationPath() {
  }

  /**
   * For debugging.
   */
  public String toString() {
    return "[LocationPath: " + _steps + "]";
  }

  public void isAbsolute(boolean isAbsolute) {
    _isAbsolute = isAbsolute;
    if (_steps.size() == 1) {
      // set the first step to be absolute
      ((Step) _steps.get(0)).setAbsolute(isAbsolute);
    }
  }

  public boolean isAbsolute() {
    return _isAbsolute;
  }

  public void addStep(Step step) {
    _steps.add(step);
    if (_steps.size() == 1) {
      // set the first step to be absolute
      step.setAbsolute(_isAbsolute);
    }
  }

  /**
   * Apply self to given nodeset, to which modifications may apply.
   */
  public void apply(NodeSet nodeset) throws XPathParseException {
    for (Iterator iter = _steps.iterator(); iter.hasNext(); ) {
      Step step = (Step) iter.next();
      step.apply(nodeset);
    }
  }

}
