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

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * <p><code>TestChildren</code> tests the various
 *   means of accessing an <code>{@link Element}</code>'s
 *   children, ensuring that all output is as 
 *   dictated by the JDOM specification (for the complete
 *   specification, see 
 *   <a href="http://jdom.org/docs/index.html">jdom.org</a>).
 * </p>
 * 
 * @author Brett McLaughlin
 * @version 1.0
 */
public class TestChildren {

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
    public TestChildren(String saxDriverClass) {
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
     * @param out <code>OutputStream</code> to output to.
     */
    public void test(String filename, OutputStream out) 
        throws IOException, JDOMException {

        // Build the JDOM Document
        Document doc = builder.build(new File(filename));

        // XXX: Need to test all the various ways to getChildren
    }

    /**
     * <p>
     * This provides a static entry point for creating a JDOM
     *   <code>{@link Document}</code> object, and testing the
     *   reading of its <code>{@link Element}</code>'s children.
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
            System.out.println("Usage: java org.jdom.examples.testSuite.TestChildren " +
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
            TestChildren tester = new TestChildren(saxDriverClass);
            tester.test(filename, System.out);
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
