
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

    if ( args.length != 1 ) {
      System.err.println("Usage: java ...TestDriver <input file>");
      System.exit(1);
    }

    SAXBuilder builder = new SAXBuilder();

    File inFile = new File(args[0]);

    Document doc = builder.build(inFile);

    try {
      XMLOutputter outputter = new XMLOutputter();
      outputter.output(doc, System.out);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    System.out.println();
    System.out.println();

    XPath xpath = new XPath("/b/c");

    // use JDOMContext
    JDOMContext context = new JDOMContext(doc.getRootElement());

    List results = (List) xpath.applyTo(context);

    System.out.println("\n\nJDOMContext results for " + xpath + ":");
    for (Iterator iter = results.iterator(); iter.hasNext(); ) {
      Object each = iter.next();
      System.out.println(each);
    }

    // use NodeSet
    System.out.println("\n\nNodeSet results for " + xpath + ":");
    NodeSet nodeset = new JdomNodeSet(doc.getRootElement());
    xpath.apply(nodeset);
    for (Iterator iter = nodeset.iterator(); iter.hasNext(); ) {
      Object each = iter.next();
      if (each instanceof Element) {
        System.out.println(each + ": " + ((Element)each).getTextTrim());
      } else {
        System.out.println(each);
      }
    }
  }

}
