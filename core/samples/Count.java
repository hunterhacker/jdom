/*--

 $Id: Count.java,v 1.10 2004/02/06 09:39:10 jhunter Exp $

 Copyright (C) 2000-2004 Jason Hunter & Brett McLaughlin.
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

import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.input.DOMBuilder;
import org.jdom.output.*;
import java.io.*;
import java.util.*;


public class Count {

    public static void main(String[] args) {
  
        if (args.length == 0) {
            System.out.println("Usage: java Count URL1 URL2..."); 
        } 
        
        SAXBuilder saxBuilder = new SAXBuilder();
        DOMBuilder domBuilder = new DOMBuilder();
                
        System.out.println(
            "File\tElements\tAttributes\tComments\tProcessing Instructions\tCharacters");

        // start parsing... 
        for (int i = 0; i < args.length; i++) {
      
            // command line should offer URIs or file names
            try {
                System.out.print(args[i] + ":\t");
                Document jdomDocument = saxBuilder.build(args[i]);

                DOMOutputter domOutputter = new DOMOutputter();

                /*
                 * Test getting DOM Document from JDOM Document
                org.w3c.dom.Document domDocument = domOutputter.output(doc);
                 */

            } catch (JDOMException e) { // indicates a well-formedness or other error
                System.out.println(args[i] + " is not a well formed XML document.");
                System.out.println(e.getMessage());
            } catch (IOException e) { // indicates an IO problem
                System.out.println(args[i] + " could not be parsed");
                System.out.println(e.getMessage());
            }      
        }
    }  

    private static int numCharacters             = 0;
    private static int numComments               = 0;
    private static int numElements               = 0;
    private static int numAttributes             = 0;
    private static int numProcessingInstructions = 0;
      
    public static String count(Document doc) {

        numCharacters = 0;
        numComments = 0;
        numElements = 0;
        numAttributes = 0;
        numProcessingInstructions = 0;  

        List children = doc.getContent();
        Iterator iterator = children.iterator();
        while (iterator.hasNext()) {
            Object o = iterator.next();
            if (o instanceof Element) {
                numElements++;
                count((Element) o);
            }
            else if (o instanceof Comment) numComments++;
            else if (o instanceof ProcessingInstruction) numProcessingInstructions++;   
        }
    
        String result = numElements + "\t" + numAttributes + "\t" +
                        numComments + "\t" + numProcessingInstructions + "\t" + numCharacters;
        return result;       
    }     

    public static void count(Element element) {

        List attributes = element.getAttributes();
        numAttributes += attributes.size();
        List children = element.getContent();
        Iterator iterator = children.iterator();
        while (iterator.hasNext()) {
            Object o = iterator.next();
            if (o instanceof Element) {
                numElements++;
                count((Element) o);
            }
            else if (o instanceof Comment) numComments++;
            else if (o instanceof ProcessingInstruction) numProcessingInstructions++;   
            else if (o instanceof String) {
                String s = (String) o;
                numCharacters += s.length();
            }   
        }

        String result = numElements + "\t" + numAttributes + "\t" +
                        numComments + "\t" + numProcessingInstructions + "\t" + numCharacters;
        System.out.println(result);       
    }  
}
