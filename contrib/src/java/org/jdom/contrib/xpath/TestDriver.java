
package org.jdom.contrib.xpath;

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.Iterator;

import org.jdom.contrib.xpath.jdom.JDOMContext;
import org.jdom.contrib.xpath.jdom.JdomNodeSet;

import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

public class TestDriver
{

  public static void main(String[] args) throws Exception
  {

    System.out.println("XPath Test Driver");

    if ( args.length != 2 ) {
      System.err.println("Usage: java ...TestDriver <input file> <xpath_expr>");
      System.exit(1);
    }

    SAXBuilder builder = new SAXBuilder();

    File inFile = new File(args[0]);
    String xpathExpr = args[1];
    
    Document doc = builder.build(inFile);
    
    try {

      XMLOutputter outputter = new XMLOutputter("  ", true);
      outputter.output(doc, System.out);

    } catch (IOException ioe) {

      ioe.printStackTrace();

    }

    System.out.println();
    System.out.println();

    XPath xpath = new XPath(xpathExpr);

    NodeSet nodeset = new JdomNodeSet(doc.getRootElement());

    nodeset.apply(xpath);

    // xpath.apply(nodeset);

    Element resultRoot = new Element("xpath-results");

    Document resultDoc = new Document(resultRoot);

    for (Iterator iter = nodeset.iterator(); iter.hasNext(); ) {

      Object each = iter.next();

      if (each instanceof Element) {
        resultRoot.addContent((Element)((Element)each).clone());
        System.out.println(each + ": " + ((Element)each).getTextTrim());
      } else {
        System.out.println(each);
      }
      
    }
  }
}
