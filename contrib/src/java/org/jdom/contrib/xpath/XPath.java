
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

  public XPath(String xpath)
  {
    _xpath = xpath;
    parse();
  }

  private void parse()
  {
    StringReader reader = new StringReader(_xpath);
    InputBuffer buf = new CharBuffer(reader);

    XPathLexer lexer = new XPathLexer(buf);
    XPathRecognizer recog = new XPathRecognizer(lexer);

    try
    {
      XPathExpr expr = recog.xpath();
      _expr = expr;
    }
    catch (RecognitionException re)
    {
      re.printStackTrace();
    }
    catch (TokenStreamException tse)
    {
      tse.printStackTrace();
    }
  }

  public Object applyTo(Context context)
  {
    Object result = null;

    result = _expr.apply(context);
      
    return result;
  }
}
