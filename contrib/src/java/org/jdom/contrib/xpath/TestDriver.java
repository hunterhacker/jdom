
package org.jdom.contrib.xpath;

import org.jdom.contrib.xpath.jdom.JDOMContext;

import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.Iterator;

public class TestDriver
{

  public static void main(String[] args)
  {

    System.err.println("XPath Test Driver");

    if ( args.length != 1 ) {
      System.err.println("Usage: java ...TestDriver <input file>");
      System.exit(1);
    }

    SAXBuilder builder = new SAXBuilder();

    File inFile = new File(args[0]);

    try
    {
      Document doc = builder.build(inFile);

      try
      {
        XMLOutputter outputter = new XMLOutputter();
        outputter.output(doc, System.err);
      } 
      catch (IOException ioe)
      {
        ioe.printStackTrace();
      }

      XPath xpath = new XPath("/b/c");

      JDOMContext context = new JDOMContext(doc.getRootElement());

      List results = (List) xpath.applyTo(context);

      Iterator resultIter = results.iterator();

      System.err.println("\n\nResults");

      while (resultIter.hasNext())
      {
        System.err.println(resultIter.next());
      }
      

    }
    catch (JDOMException je) 
    {
      je.printStackTrace();
    }
  }
}
