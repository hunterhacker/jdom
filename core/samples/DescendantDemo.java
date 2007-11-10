/*--

 $Id: DescendantDemo.java,v 1.5 2007/11/10 05:36:01 jhunter Exp $

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
import java.util.*;

import org.jdom.*;
import org.jdom.filter.ElementFilter;
import org.jdom.input.*;
import org.jdom.output.*;

/**
 * Demonstrates the use of {@link Parent#getDescendants}.
 */
public class DescendantDemo {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: java DescendantDemo [web.xml]");
            return;
        }

        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(args[0]);

        System.out.println("All content:");
        Iterator itr = doc.getDescendants();
        while (itr.hasNext()) {
            Content c = (Content) itr.next();
            System.out.println(c);
        }

        System.out.println();
        System.out.println("Only elements:");
        itr = doc.getDescendants(new ElementFilter());
        while (itr.hasNext()) {
            Content c = (Content) itr.next();
            System.out.println(c);
        }

        System.out.println();
        System.out.println("Everything that's not an element:");
        itr = doc.getDescendants(new ElementFilter().negate());
        while (itr.hasNext()) {
            Content c = (Content) itr.next();
            System.out.println(c);
        }

        System.out.println();
        System.out.println("Only elements with localname of servlet:");
        itr = doc.getDescendants(new ElementFilter("servlet"));
        while (itr.hasNext()) {
            Content c = (Content) itr.next();
            System.out.println(c);
        }

        System.out.println();
        System.out.println(
             "Only elements with localname of servlet-name or servlet-class:");
        itr = doc.getDescendants(new ElementFilter("servlet-name")
                                 .or(new ElementFilter("servlet-class")));
        while (itr.hasNext()) {
            Content c = (Content) itr.next();
            System.out.println(c);
        }

        System.out.println();
        System.out.println("Remove elements with localname of servlet:");
        itr = doc.getDescendants(new ElementFilter("servlet"));
        while (itr.hasNext()) {
            itr.next();
            itr.remove();
        }

        XMLOutputter outp = new XMLOutputter();
        outp.output(doc, System.out);
    }
}
