
package org.jdom.contrib.xpath;

public interface XPathPredicateHandler
{
    void startPredicate();
    void endPredicate();

    XPathHandler startXPathParsing();
    void endXPathParsing();

    void or();
    void and();
    void greaterThan();
    void greaterThanEquals();
    void lessThan();
    void lessThanEquals();
    void equals();
    void notEquals();

    void plus();
    void minus();

    void multiply();
    void divide();
    void modulo();

    void negative();

}
