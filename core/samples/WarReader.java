/*--

 $Id: WarReader.java,v 1.13 2007/11/10 05:36:01 jhunter Exp $

 Copyright (C) 2000-2007 Jason Hunter & Brett McLaughlin.
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:

 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions, and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions, and the disclaimer that follows
    these conditions in the documentation and/or other materials
    provided with the distribution.

 3. The name "JDOM" must not be used to endorse or promote products
    derived from this software without prior written permission.  For
    written permission, please contact <request_AT_jdom_DOT_org>.

 4. Products derived from this software may not be called "JDOM", nor
    may "JDOM" appear in their name, without prior written permission
    from the JDOM Project Management <request_AT_jdom_DOT_org>.

 In addition, we request (but do not require) that you include in the
 end-user documentation provided with the redistribution and/or in the
 software itself an acknowledgement equivalent to the following:
     "This product includes software developed by the
      JDOM Project (http://www.jdom.org/)."
 Alternatively, the acknowledgment may be graphical using the logos
 available at http://www.jdom.org/images/logos.

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED.  IN NO EVENT SHALL THE JDOM AUTHORS OR THE PROJECT
 CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.

 This software consists of voluntary contributions made by many
 individuals on behalf of the JDOM Project and was originally
 created by Jason Hunter <jhunter_AT_jdom_DOT_org> and
 Brett McLaughlin <brett_AT_jdom_DOT_org>.  For more information
 on the JDOM Project, please see <http://www.jdom.org/>.

 */

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 * <p><code>WarReader</code> demonstrates how to
 *   read a Servlet 2.2 Web Archive file with JDOM.
 * </p>
 * 
 * @author Brett McLaughlin, Jason Hunter
 * @version 1.0
 */
public class WarReader {
    
  public static void main(String[] args) throws IOException, JDOMException {
    if (args.length != 1) {
      System.err.println("Usage: java WarReader [web.xml]");
      return;
    }
    String filename = args[0];
    PrintStream out = System.out;

    SAXBuilder builder = new SAXBuilder();
    Document doc = builder.build(new File(filename));

    // Get the root element
    Element root = doc.getRootElement();

    // Print servlet information
    List servlets = root.getChildren("servlet");
    out.println("This WAR has "+ servlets.size() +" registered servlets:");
    Iterator i = servlets.iterator();
    while (i.hasNext()) {
      Element servlet = (Element) i.next();
      out.print("\t" + servlet.getChild("servlet-name")
                              .getTextTrim() +
                " for " + servlet.getChild("servlet-class")
                                 .getTextTrim());
      List initParams = servlet.getChildren("init-param");
      out.println(" (it has " + initParams.size() + " init params)");
    }

    // Print security role information
    List securityRoles = root.getChildren("security-role");
    if (securityRoles.size() == 0) {
      out.println("This WAR contains no roles");
    }
    else {
      Element securityRole = (Element) securityRoles.get(0);
      List roleNames = securityRole.getChildren("role-name");
      out.println("This WAR contains " + roleNames.size() + " roles:");
      i = roleNames.iterator();
      while (i.hasNext()) {
        Element e = (Element) i.next();
        out.println("\t" + e.getTextTrim());
      }
    }
        
    // Print distributed information (notice this is out of order)
    List distrib = root.getChildren("distributed");
    if (distrib.size() == 0) {
      out.println("This WAR is not distributed");
    } else {
      out.println("This WAR is distributed");
    }        
  }
}
