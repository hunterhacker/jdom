import java.io.*;
import java.util.*;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.contrib.xpath.XPathElement;
import org.jdom.contrib.xpath.XPathParseException;

/**
 * Super-simple demo for XPath package.
 *
 * @author Jason Hunter
 */
public class XPathDemo {

  public static void main(String[] args) {
    String doc = 
      "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
      "<web-app>" +
      "    <servlet>" +
      "        <servlet-name>" +
      "            snoop" +
      "        </servlet-name>" +
      "        <servlet-class>" +
      "            SnoopServlet" +
      "        </servlet-class>" +
      "    </servlet>" +
      "    <servlet>" +
      "        <servlet-name>" +
      "            file" +
      "        </servlet-name>" +
      "        <servlet-class>" +
      "            ViewFile" +
      "        </servlet-class>" +
      "        <init-param>" +
      "            <param-name>" +
      "                initial" +
      "            </param-name>" +
      "            <param-value>" +
      "                1000" +
      "            </param-value>" +
      "            <description>" +
      "                The initial value for the counter  <!-- optional -->" +
      "            </description>" +
      "        </init-param>" +
      "        <description>" +
      "            The default file serving servlet" +
      "        </description>" +
      "    </servlet>" +
      "</web-app>";

    try {
      Document d = new SAXBuilder().build(new StringReader(doc));
   
      XPathElement xpath = new XPathElement(d.getRootElement());
      List servletNames = xpath.getMatches("servlet/servlet-name/text()");
      System.out.println("Servlet names:");
      Iterator i = servletNames.iterator();
      while (i.hasNext()) {
        System.out.println(i.next());
      }

      // Need to change the XPath expression to get the description 
      // of the "file" servlet by name, not by index
      String fileDesc = xpath.getTextMatch("servlet[2]/description/text()");
      System.out.println("The \"file\" servlet desc: ");
      System.out.println(fileDesc.trim());
    }
    catch (XPathParseException e) {
      e.printStackTrace();
    }
    catch (JDOMException e) {
      e.printStackTrace();
    }

  }

}

