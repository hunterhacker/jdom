
package org.jdom.contrib.xpath;

import java.util.List;

public interface Context
{
  // -- List of needed methods to carry out particular
  // -- parts of the XPath stuff.

  List walkStep(String axis,
                String nodeTest);
}
