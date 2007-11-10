/*--

 $Id: SAXBuilderDemo.java,v 1.20 2007/11/10 05:36:01 jhunter Exp $

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

import java.io.*;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;

/**
 * <p><code>SAXBuilderDemo</code> demonstrates how to
 *   build a JDOM <code>Document</code> using a SAX 2.0
 *   parser.
 * </p>
 * 
 * @author Brett McLaughlin
 * @author Jason Hunter
 * @version 1.0
 */
public class SAXBuilderDemo {

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
     *         <li>Second argument: optional boolean on whether to expand
     *         entities</li>
     *         <li>Third argument: optional String name of a SAX Driver class
     *         to use</li>
     *        </ul>
     */
    public static void main(String[] args) {
        if ((args.length < 1) || (args.length > 3)) {
            System.out.println(
              "Usage: java SAXBuilderDemo " +
              "[XML document filename] ([expandEntities] [SAX Driver Class])");
            return;
        }

        boolean expandEntities = true;

        // Load filename and SAX driver class
        String filename = args[0];
        String saxDriverClass = null;
        if (args.length > 1) {
            if (args[1].equalsIgnoreCase("false")) {
                expandEntities = false;
            }
            if (args.length > 2) {
                saxDriverClass = args[2];
            }
        }

        // Create an instance of the tester and test
        try {
            SAXBuilder builder = null;
            if (saxDriverClass == null) {
                builder = new SAXBuilder();
            } else {
                builder = new SAXBuilder(saxDriverClass);
            }
            builder.setExpandEntities(expandEntities);
            //builder.setIgnoringBoundaryWhitespace(true);

            Document doc = builder.build(filename);

            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
            //outputter.setExpandEmptyElements(true);
            outputter.output(doc, System.out);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }    
    }
}
