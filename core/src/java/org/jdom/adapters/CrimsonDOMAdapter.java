/*-- 

 $Id: CrimsonDOMAdapter.java,v 1.5 2001/03/15 06:07:18 jhunter Exp $

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
 created by Brett McLaughlin <brett@jdom.org> and 
 Jason Hunter <jhunter@jdom.org>.  For more information on the 
 JDOM Project, please see <http://www.jdom.org/>.
 
 */

package org.jdom.adapters;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

/**
 * <b><code>CrimsonDOMAdapater</code></b>
 * <p>
 * This class defines wrapper behavior for obtaining a DOM
 *   <code>Document</code> object from the Apache Crimson DOM parser.
 * </p>
 *
 * @author Jason Hunter
 * @version 1.0
 */
public class CrimsonDOMAdapter extends AbstractDOMAdapter {

    /**
     * <p>
     * This creates a new <code>{@link Document}</code> from an
     *   existing <code>InputStream</code> by letting a DOM
     *   parser handle parsing using the supplied stream.
     * </p>
     *
     * @param in <code>InputStream</code> to parse.
     * @param validate <code>boolean</code> to indicate if validation should occur.
     * @return <code>Document</code> - instance ready for use.
     * @throws IOException when errors occur in parsing.
     */
    public Document getDocument(InputStream in, boolean validate)
        throws IOException  {

        try {
            Class[] parameterTypes = new Class[2];
            parameterTypes[0] = Class.forName("java.io.InputStream");
            parameterTypes[1] = boolean.class;

            Object[] args = new Object[2];
            args[0] = in;
            args[1] = new Boolean(false);

            /*
             * XXX: Add in validation support for this parser. (brett)
             */

            // Load the parser class and invoke the parse method
            Class parserClass = Class.forName("org.apache.crimson.tree.XmlDocument");
            Method createXmlDocument =
                parserClass.getMethod("createXmlDocument", parameterTypes);
            Document doc =
                (Document)createXmlDocument.invoke(null, args);

            return doc;

        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            if (targetException instanceof org.xml.sax.SAXParseException) {
                SAXParseException parseException = (SAXParseException)targetException;
                throw new IOException("Error on line " + parseException.getLineNumber() +
                                      " of XML document: " + parseException.getMessage());
            } else {
                throw new IOException(targetException.getMessage());
            }
        } catch (Exception e) {
            throw new IOException(e.getClass().getName() + ": " +
                                  e.getMessage());
        }
    }

    /**
     * <p>
     * This creates an empty <code>Document</code> object based
     *   on a specific parser implementation.
     * </p>
     *
     * @return <code>Document</code> - created DOM Document.
     * @throws IOException when errors occur.
     */
    public Document createDocument() throws IOException {
        try {
            return
                (Document)Class.forName(
                    "org.apache.crimson.tree.XmlDocument")
                    .newInstance();

        } catch (Exception e) {
            throw new IOException(e.getClass().getName() + ": " +
                                  e.getMessage());
        }
    }
}
