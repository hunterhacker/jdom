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
package examples;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.adapters.DOMAdapter;
import org.jdom.input.DOMBuilder;
import org.jdom.output.XMLOutputter;

/**
 * <p><code>DOMBuilderDemo</code> demonstrates how to
 *   build a JDOM <code>Document</code> using a DOM
 *   tree.
 * </p>
 *
 * @author Brett McLaughlin
 * @author Philip Nelson
 * @version 1.0
 */
public class DOMBuilderDemo {

    /** Default DOM Adapter class to use */
    private static final String DEFAULT_DOM_ADAPTER_CLASS =
        "org.jdom.adapters.XercesDOMAdapter";

    /** DOM Adapter Class to use */
    private String domAdapterClass;

    /** <code>{@link DOMBuilder}</code> instance to use */
    private DOMBuilder builder;

    /**
     * <p>
     * This will create an instance of <code>{@link DOMBuilder}</code>
     *   for use in the rest of this program.
     * </p>
     *
     * @param domAdapterClass <code>String</code> name of adapter class to use.
     */
    public DOMBuilderDemo(String domAdapterClass) {
        this.domAdapterClass = domAdapterClass;
        builder = new DOMBuilder(domAdapterClass);
    }

    /**
     * <p>
     * This will parse the specified filename using DOM and the
     *   DOM adapter class specified in the constructor.
     * </p>
     *
     * @param filename <code>String</code> name of file to parse.
     * @param out <code>OutputStream</code> to output to.
     */
    public void testBuilder(String filename, OutputStream out)
        throws IOException, JDOMException {

		//build a DOM tree with the specified or default parser
		DOMAdapter domAdapter;
        FileInputStream in = new FileInputStream(filename);
		try {
	        Class parserClass = Class.forName(domAdapterClass);
			domAdapter = (DOMAdapter)parserClass.newInstance();
        } catch (ClassNotFoundException e) {
				throw new JDOMException("Parser class " + domAdapterClass +
                                        " not found");
		} catch (Exception e) {
				throw new JDOMException("Parser class " + domAdapterClass +
                                        " instantiation error");
		}


        // Build the JDOM Document - validating is off by default for this example
		// because of the document root issue
        org.w3c.dom.Document w3cdoc =
            domAdapter.getDocument((InputStream)in, false);
        Document doc = builder.build(w3cdoc);

        // Create an outputter with default formatting
        XMLOutputter outputter = new XMLOutputter();
        outputter.output(doc, out);
    }

    /**
     * <p>
     * This provides a static entry point for creating a JDOM
     *   <code>{@link Document}</code> object using a DOM
     *   tree.
     * </p>
     *
     * @param args <code>String[]</code>
     *        <ul>
     *         <li>First argument: filename of XML document to parse</li>
     *         <li>Second argument: optional name of DOM Adapter class</li>
     *        </ul>
     */
    public static void main(String[] args) {
        if ((args.length < 1) || (args.length > 2)) {
            System.out.println("Usage: java org.jdom.examples.io.DOMBuilderTest " +
                               "[XML document filename] ([DOM Adapter Class])");
            System.exit(-1);
        }

        // Load filename and SAX driver class
        String filename = args[0];
        String domAdapterClass = DEFAULT_DOM_ADAPTER_CLASS;
        if (args.length == 2) {
            domAdapterClass = args[1];
        }

        // Create an instance of the tester and test
        try {
            DOMBuilderDemo demo = new DOMBuilderDemo(domAdapterClass);
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
