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


import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 * <p><code>TestXMLOutputter</code> demonstrates how to use the {@link
 *   org.jdom.output.XMLOutputter}.  It reads an XML file and then
 *   outputs it according to the command-line parameters.  Use it to
 *   figure out what combination of options gives your desired output.
 * </p>
 * 
 * @author Alex Chaffee (alex@jguru.com)
 * @version 1.0
 **/
public class TestXMLOutputter {

    /** Default SAX Driver class to use */
    private static final String DEFAULT_SAX_DRIVER_CLASS =
        "org.apache.xerces.parsers.SAXParser";

    /** SAX Driver Class to use */
    private String saxDriverClass;

    /** <code>{@link SAXBuilder}</code> instance to use */
    private SAXBuilder builder;

    XMLOutputter outputter;
    boolean testElements;
    
    /**
     * <p>
     * This will create an instance of <code>{@link SAXBuilder}</code>
     *   for use in the rest of this program.
     * </p>
     *
     * @param saxDriverClass <code>String</code> name of driver class to use.
     */
    public TestXMLOutputter(String saxDriverClass) {
        this.saxDriverClass = saxDriverClass;
        builder = new SAXBuilder(saxDriverClass);
        
        // Create an outputter with default formatting
        outputter = new XMLOutputter();
    }

    /**
     * <p>
     * This will parse the specified filename using SAX and the
     *   SAX driver class specified in the constructor.
     * </p>
     *
     * @param filename <code>String</code> name of file to parse.
     * @param out <code>OutputStream</code> to output to.
     */
    public void testBuilder(String filename, OutputStream out) 
        throws IOException, JDOMException {

        // Build the JDOM Document
        Document doc = builder.build(new File(filename));

        // Output the document
        if (!testElements) {
            outputter.output(doc, out);
        }
        else {
            printElements(doc.getRootElement(), out);
        }
    }

    protected void printElements(Element e, OutputStream out) throws IOException, JDOMException {
        out.write(("\n===== " + e.getName() + ": \n").getBytes());
        out.flush();
        outputter.output(e, out);
        out.flush();
        for (Iterator i=e.getChildren().iterator(); i.hasNext(); ) {
            Element child = (Element)i.next();
            printElements(child, out);
        }
        out.flush();
    }

    /**
     * <p>
     * This provides a static entry point for creating a JDOM
     *   <code>{@link Document}</code> object using a SAX 2.0
     *   parser (an <code>XMLReader</code> implementation).
     * </p>
     *
     * @param args <code>String[]</code>
     *        <ul>
     *         <li>First argument: filename of XML document to parse</li>
     *         <li>Second argument: optional name of SAX Driver class</li>
     *        </ul>
     */
    public static void main(String[] args) {

        String saxDriverClass = DEFAULT_SAX_DRIVER_CLASS;
        boolean testElements = false;
        XMLOutputter outputter = new XMLOutputter();
        int i = 0;
        do {
            i = outputter.parseArgs(args, i);       
            if (i==args.length) break;
            if (args[i].equals("-driver")) {
                saxDriverClass = args[++i];
            }
            if (args[i].equals("-testElements")) {
                testElements = true;
            }
            else if (args[i].equals("-h")) {
                i=args.length;
                break;
            }
            else if (!args[i].startsWith("-"))  // we're at the file list
                break;
            ++i;
        } while (i<args.length);
        
        if (i>=args.length) {
            System.out.println
                ("Usage: java TestXMLOutputter " +
                 "[-driver SAX Driver Class] [-testElements]\n" +
                 "\t[-suppressDeclaration] [-omitEncoding]\n" +
                 "\t[-indent x] [-indentSize x] [-indentLevel x]\n" +
                 "\t[-expandEmpty] [-encoding x] [-newlines] [-lineSeparator x]\n" +
                 "\t[-trimText]\n" +
                 "\t(XML document filename)...\n" 
                 );
            return;
        }

        // Create an instance of the tester and test
        TestXMLOutputter demo = new TestXMLOutputter(saxDriverClass);
        demo.outputter = outputter;
        demo.testElements = testElements;
        
        for (; i<args.length; ++i) {
            // Load filename
            String filename = args[i];

            try {
                demo.testBuilder(filename, System.out);
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
}