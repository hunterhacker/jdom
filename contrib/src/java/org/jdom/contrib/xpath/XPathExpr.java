
package org.jdom.contrib.xpath;

public interface XPathExpr
{
  /** Apply this XPath expression to the context
      yielding some flavor of result.

      param context The context to walk>
  */
  Object apply(Context context);

  /**
   * Apply self to given nodeset, to which modifications may apply.
   */
  void apply(NodeSet nodeset) throws XPathParseException;

}
