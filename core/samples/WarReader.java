/*-- 

 Copyright (C) 2000 Brett McLaughlin & Jason Hunter.
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
    written permission, please contact license@jdom.org.
 
 4. Products derived from this software may not be called "JDOM", nor
    may "JDOM" appear in their name, without prior written permission
    from the JDOM Project Management (pm@jdom.org).
 
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
 DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.

 This software consists of voluntary contributions made by many 
 individuals on behalf of the JDOM Project and was originally 
 created by Brett McLaughlin <brett@jdom.org> and 
 Jason Hunter <jhunter@jdom.org>.  For more information on the 
 JDOM Project, please see <http://www.jdom.org/>.
 
 */
package samples;

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
    
    /** Default SAX Driver class to use */
    private static final String DEFAULT_SAX_DRIVER_CLASS =
        "org.apache.xerces.parsers.SAXParser";

    /** SAX Driver Class to use */
    private String saxDriverClass;

    /** <code>{@link SAXBuilder}</code> instance to use */
    private SAXBuilder builder;
    
    /**
     * <p>
     * This will create an instance of <code>{@link SAXBuilder}</code>
     *   for use in the rest of this program.
     * </p>
     *
     * @param saxDriverClass <code>String</code> name of driver class to use.
     */
    public WarReader(String saxDriverClass) {
        this.saxDriverClass = saxDriverClass;
        builder = new SAXBuilder(saxDriverClass);
    }
    
    /**
     * <p>
     * This will parse the specified WAR using SAX and the
     *   SAX driver class specified in the constructor.
     * </p>
     *
     * @param filename <code>String</code> name of file to parse.
     * @param out <code>OutputStream</code> to output to.
     */
    public void read(String filename, PrintStream out) 
        throws IOException, JDOMException {

        // Build the JDOM Document
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
        } else {
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

    /**
     * <p>
     * This provides a static entry point for reading a servlet
     *   WAR file using JDOM.
     * </p>
     *
     * @param args <code>String[]</code>
     *        <ul>
     *         <li>First argument: filename of XML document to parse</li>
     *         <li>Second argument: optional name of SAX Driver class</li>
     *        </ul>
     */    
    public static void main(String[] args) {

        PrintStream out = System.out;

        if (args.length != 1 && args.length != 2) {
            out.println("Usage: org.jdom.examples.servlet.WarReader [web.xml]");
            return;
        }
    
        // Load filename and SAX driver class
        String filename = args[0];
        String saxDriverClass = DEFAULT_SAX_DRIVER_CLASS;
        if (args.length == 2) {
            saxDriverClass = args[1];
        }    
        
        // Create an instance of the tester and test
        try {
            WarReader reader = new WarReader(saxDriverClass);
            reader.read(filename, System.out);
        } catch (JDOMException e) {
            if (e.getRootCause() != null) {
                e.getRootCause().printStackTrace();
            } else {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }
}
