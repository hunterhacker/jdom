
/*-- 

 Copyright (C) @year@ Brett McLaughlin & Jason Hunter. All rights reserved.
 
 Redistribution and use in source and binary forms, with or without modifica-
 tion, are permitted provided that the following conditions are met:
 
 1. Redistributions of source code must retain the above copyright notice,
    this list of conditions, and the following disclaimer.
 
 2. Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions, the disclaimer that follows these conditions,
    and/or other materials provided with the distribution.
 
 3. The names "JDOM" and "Java Document Object Model" must not be used to
    endorse or promote products derived from this software without prior
    written permission. For written permission, please contact
    license@jdom.org.
 
 4. Products derived from this software may not be called "JDOM", nor may
    "JDOM" appear in their name, without prior written permission from the
    JDOM Project Management (pm@jdom.org).
 
 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 JDOM PROJECT  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT, INDIRECT, 
 INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLUDING, BUT 
 NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 
 This software  consists of voluntary contributions made  by many individuals
 on  behalf of the Java Document Object Model Project and was originally 
 created by Brett McLaughlin <brett@jdom.org> and 
 Jason Hunter <jhunter@jdom.org>. For more  information on the JDOM 
 Project, please see <http://www.jdom.org/>.
 
 */
package org.jdom.examples.testSuite;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * <p><code>TestNamespaces</code> tests the various
 *   means of accessing an <code>{@link Element}</code>'s
 *   and attribute's namespaces, ensuring that all output is as 
 *   dictated by the JDOM specification (for the complete
 *   specification, see 
 *   <a href="http://jdom.org/docs/index.html">jdom.org</a>).
 * </p>
 * 
 * @author Brett McLaughlin
 * @version 1.0
 */
public class TestNamespaces {

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
    public TestNamespaces(String saxDriverClass) {
        this.saxDriverClass = saxDriverClass;
        builder = new SAXBuilder(saxDriverClass);
    }

    /**
     * <p>
     * This will parse the specified filename and perform
     *   the tests.
     * </p>
     *
     * @param filename <code>String</code> name of file to parse.
     * @param out <code>PrintWriter</code> to output to.
     * @param stream <code>OutputStream</code> to output to.
     */
    public void test(String filename, PrintWriter out, OutputStream stream) 
        throws IOException, JDOMException {

        out.write("Testing on document created from scratch...");
        out.write("\n\n");

// This test needs to be updated for the new Namespace approach

/*

        Document doc = 
            new Document(new Element("JavaXML:Book", 
                                     "http://www.oreilly.com/catalog/javaxml")
                .addChild(new Element("JavaXML:Chapter")
                    .addChild(new Element("title")
                        .setContent("Introduction")))
                .addChild(new Element("JavaXML:SectionBreak"))
                .addChild(new Element("JavaXML:Chapter")
                    .addChild(new Element("title")
                        .setContent("JDOM")))
            );

        Element root = doc.getRootElement();

        out.write("Testing lookup directly on Element...");
        out.write("\n");

        out.write("Namespace URI for element ");
        out.write(root.getName());
        out.write(" is \"");
        out.write(root.getNamespaceURI());
        out.write("\"");
        out.write("\n");

        out.write("Testing lookup on nested (child) Element...");
        out.write("\n");

        Element child = (Element)root.getChildren().get(0);

        out.write("Namespace URI for element ");
        out.write(child.getName());
        out.write(" is \"");
        out.write(child.getNamespaceURI());
        out.write("\"");
        out.write("\n");

        out.write("Testing lookup on nested (child) Element with no mapping...");
        out.write("\n");

        Element title = root.getChild("JavaXML:Chapter").getChild("title");

        out.write("Namespace URI for element ");
        out.write(title.getName());
        out.write(" is \"");
        out.write(title.getNamespaceURI());
        out.write("\"");
        out.write("\n");

        out.write("Adding namespace mapping for default namespace to root element...");
        out.write("\n");

        root.addNamespaceMapping("", "http://xhtml.com");

        out.write("Namespace URI for element ");
        out.write(title.getName());
        out.write(" is \"");
        out.write(title.getNamespaceURI());
        out.write("\"");
        out.write("\n");

        out.write("\n");
        out.write("Testing namespace finds on children...");
        out.write("\n\n");

        List matching = 
            root.getChildren("Chapter", 
                             "http://www.oreilly.com/catalog/javaxml");
        for (Iterator i = matching.iterator(); i.hasNext(); ) {
            Element e = (Element)i.next();
            out.write("Match: ");
            out.write(e.getFullName());
            out.write(" with namespace URI \"");
            out.write(e.getNamespaceURI());
            out.write("\"");
            out.write("\n");
        }
        out.write("\n");

        out.write("\n\n");
        out.write("Testing with existing document...");
        out.write("\n\n");

        // Build the JDOM Document
        doc = builder.build(new File(filename));

        out.flush();
*/
    }

    /**
     * <p>
     * This provides a static entry point for creating a JDOM
     *   <code>{@link Document}</code> object, and testing the
     *   reading of its <code>{@link Element}</code>'s and
     *   <code>Attribute</code>'s namespaces.
     * </p>
     *
     * @param args <code>String[]</code>
     *        <ul>
     *         <li>First argument: filename of XML document to parse</li>
     *         <li>Second argument: optional name of SAX Driver class</li>
     *        </ul>
     */
    public static void main(String[] args) {
        if ((args.length != 1) && (args.length != 2)) {
            System.out.println("Usage: java org.jdom.examples.testSuite.TestNamespaces " +
                               "[XML document filename] ([SAX Driver Class])");
            return;
        }

        // Load filename and SAX driver class
        String filename = args[0];
        String saxDriverClass = TestConstants.DEFAULT_SAX_DRIVER_CLASS;
        if (args.length == 2) {
            saxDriverClass = args[1];
        }

        // Create an instance of the tester and test
        try {
            TestNamespaces tester = new TestNamespaces(saxDriverClass);
            tester.test(filename, new PrintWriter(System.out), System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }    
    }

}
