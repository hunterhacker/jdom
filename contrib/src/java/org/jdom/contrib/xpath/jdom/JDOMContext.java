
package org.jdom.contrib.xpath.jdom;

import org.jdom.contrib.xpath.Context;

import org.jdom.Element;

import java.util.List;
import java.util.Vector;
import java.util.Iterator;
import java.util.Collections;

public class JDOMContext implements Context
{

  private Element _element = null;

  public JDOMContext(Element element)
  {
    _element = element;
  }

  public List walkStep(String axis,
                         String nodeTest)
  {
    return wrapInContexts(_element.getChildren(nodeTest));
  }

  private List wrapInContexts(List elements)
  {
    Iterator elemIter = elements.iterator();
    Vector wrapped = new Vector();

    while (elemIter.hasNext())
    {
      wrapped.add( new JDOMContext( (Element) elemIter.next() ) );
    }
    
    return wrapped;
  }

  public String toString()
  {
    if (_element != null) {
      return _element.toString();
    }

    return "";
  }

}
