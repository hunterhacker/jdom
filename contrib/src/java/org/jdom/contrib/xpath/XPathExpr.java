
package org.jdom.contrib.xpath;

public interface XPathExpr
{
  /** Apply this XPath expression to the context
      yielding some flavor of result.

      param context The context to walk>
  */
  Object apply(Context context);
}
