
package org.jdom.contrib.xpath;

public class NoOpXPathPredicateHandler implements XPathPredicateHandler
{
    public void startPredicate()
    {
        // intentially left blank
    }
    public void endPredicate()
    {
        // intentially left blank
    }

    public XPathHandler startXPathParsing()
    {
        return new NoOpXPathHandler();
    }
    public void endXPathParsing()
    {
        // intentially left blank
    }

    public void or()
    {
        // intentially left blank
    }
    public void and()
    {
        // intentially left blank
    }
    public void greaterThan()
    {
        // intentially left blank
    }
    public void greaterThanEquals()
    {
        // intentially left blank
    }
    public void lessThan()
    {
        // intentially left blank
    }
    public void lessThanEquals()
    {
        // intentially left blank
    }
    public void equals()
    {
        // intentially left blank
    }
    public void notEquals()
    {
        // intentially left blank
    }

    public void plus()
    {
        // intentially left blank
    }
    public void minus()
    {
        // intentially left blank
    }

    public void multiply()
    {
        // intentially left blank
    }
    public void divide()
    {
        // intentially left blank
    }
    public void modulo()
    {
        // intentially left blank
    }

    public void negative()
    {
        // intentially left blank
    }

}
