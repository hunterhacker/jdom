
package org.jdom.contrib.xpath;

public class NoOpXPathHandler implements XPathHandler
{
    public void startParsingXPath()
    {
        // intentially left blank
    }
    public void endParsingXPath()
    {
        // intentially left blank
    }

    public void startPath()
    {
        // intentially left blank
    }
    public void endPath()
    {
        // intentially left blank
    }

    public void absolute()
    {
        // intentially left blank
    }

    public void startStep()
    {
        // intentially left blank
    }
    public void endStep()
    {
        // intentially left blank
    }

    public XPathPredicateHandler startPredicate()
    {
        return new NoOpXPathPredicateHandler();
    }
    public void endPredicate()
    {
        // intentially left blank
    }

    public void nameTest(String axis,
                         String prefix,
                         String localName)
    {
        // intentially left blank
    }
                  
}
