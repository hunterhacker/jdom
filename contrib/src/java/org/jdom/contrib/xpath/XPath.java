
package org.jdom.contrib.xpath;

import org.jdom.contrib.xpath.parser.XPathLexer;
import org.jdom.contrib.xpath.parser.XPathRecognizer;

import antlr.CharBuffer;
import antlr.*;

import java.io.StringReader;

public class XPath
{
  private String _xpath = "";
  private XPathExpr _expr = null;

  public XPath(String xpath) {
    _xpath = xpath;
    parse();
  }

  /**
   * For debugging.
   */
  public String toString() {
    return "[XPath: " + _xpath + " " + _expr + "]";
  }


  private void parse() {
    StringReader reader = new StringReader(_xpath);
    InputBuffer buf = new CharBuffer(reader);

    XPathLexer lexer = new XPathLexer(buf);
    XPathRecognizer recog = new XPathRecognizer(lexer);

    try {
      _expr = recog.xpath();
    } catch (RecognitionException e) {
      e.printStackTrace();
    } catch (TokenStreamException e) {
      e.printStackTrace();
    }
  }

  public Object applyTo(Context context) {
    Object result = _expr.apply(context);

    return result;
  }

  /**
   * Apply self to given nodeset, to which modifications may apply.
   */
  public void apply(NodeSet nodeset) throws XPathParseException {
    _expr.apply(nodeset);
  }

}
