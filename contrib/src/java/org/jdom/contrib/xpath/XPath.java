
package org.jdom.contrib.xpath;

import java.io.StringReader;

import org.jdom.contrib.xpath.parser.XPathLexer;
import org.jdom.contrib.xpath.parser.XPathRecognizer;

import antlr.RecognitionException;
import antlr.TokenStreamException;

public class XPath
{
    private String _xpathSpec = null;
    
    public XPath(String xpathSpec)
    {
        _xpathSpec = xpathSpec;
    }

    public void compile()
    {
        XPathLexer lexer = new XPathLexer( new StringReader(_xpathSpec) );
        XPathRecognizer recog = new XPathRecognizer(lexer);

        try
        {
            recog.xpath();
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

}
