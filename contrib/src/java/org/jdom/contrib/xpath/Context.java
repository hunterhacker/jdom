
package org.jdom.contrib.xpath;

import java.util.List;

import org.jdom.contrib.xpath.impl.Step;

public interface Context
{
  // -- List of needed methods to carry out particular
  // -- parts of the XPath stuff.

  List walkStep(Step step);
}
