
package org.jdom.contrib.xpath.jdom;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

import org.jdom.Element;

import org.jdom.contrib.xpath.Context;
import org.jdom.contrib.xpath.impl.Step;

public class JDOMContext implements Context
{

  private Element _element = null;

  public JDOMContext(Element element)
  {
    _element = element;
  }

  public List walkStep(Step step) {
    return wrapInContexts(_element.getChildren(step.getLocalName()));
  }

  private List wrapInContexts(List elements)
  {
    Iterator elemIter = elements.iterator();
    List wrapped = new ArrayList();

    while (elemIter.hasNext())
    {
      wrapped.add( new JDOMContext( (Element) elemIter.next() ) );
    }

    return wrapped;
  }

  public String toString()
  {
    if (_element != null) {
      return _element.toString() + " " + _element.getTextTrim();
    }

    return "";
  }

}
