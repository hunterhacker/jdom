
package org.jdom.contrib.xpath;

public interface XPathHandler
{
    void startParsingXPath();
    void endParsingXPath();

    void startPath();
    void endPath();

    void absolute();

    void startStep();
    void endStep();

    XPathPredicateHandler startPredicate();
    void endPredicate();

    void nameTest(String axis,
                  String prefix,
                  String localName);
                  
}
