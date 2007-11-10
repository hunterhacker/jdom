/*-- 

 $Id: JAXPDOMAdapter.java,v 1.13 2007/11/10 05:28:59 jhunter Exp $

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

package org.jdom.adapters;

import java.io.*;
import java.lang.reflect.*;

import org.jdom.*;
import org.jdom.input.*;
import org.w3c.dom.Document;

/**
 * An adapter for any parser supporting the Sun JAXP APIs.
 * 
 * @version $Revision: 1.13 $, $Date: 2007/11/10 05:28:59 $
 * @author  Jason Hunter
 */
public class JAXPDOMAdapter extends AbstractDOMAdapter {

    private static final String CVS_ID = 
      "@(#) $RCSfile: JAXPDOMAdapter.java,v $ $Revision: 1.13 $ $Date: 2007/11/10 05:28:59 $ $Name:  $";

    /**
     * This creates a new <code>{@link Document}</code> from an
     * existing <code>InputStream</code> by letting a JAXP
     * parser handle parsing using the supplied stream.
     *
     * @param in <code>InputStream</code> to parse.
     * @param validate <code>boolean</code> to indicate if validation 
     *        should occur.
     * @return <code>Document</code> - instance ready for use.
     * @throws IOException when I/O error occurs.
     * @throws JDOMException when errors occur in parsing.
      */
    public Document getDocument(InputStream in, boolean validate)
        throws IOException, JDOMException {

        try {
            // Try using JAXP...
            // Note we need DOM Level 2 and thus JAXP 1.1.
            Class.forName("javax.xml.transform.Transformer");

            // Try JAXP 1.1 calls to build the document
            Class factoryClass =
                Class.forName("javax.xml.parsers.DocumentBuilderFactory");

            // factory = DocumentBuilderFactory.newInstance();
            Method newParserInstance =
                factoryClass.getMethod("newInstance", null);
            Object factory = newParserInstance.invoke(null, null);

            // factory.setValidating(validate);
            Method setValidating =
                factoryClass.getMethod("setValidating",
                                   new Class[]{boolean.class});
            setValidating.invoke(factory,
                                 new Object[]{new Boolean(validate)});

            // factory.setNamespaceAware(true);
            Method setNamespaceAware =
                factoryClass.getMethod("setNamespaceAware",
                                       new Class[]{boolean.class});
            setNamespaceAware.invoke(factory,
                                 new Object[]{Boolean.TRUE});
    
            // jaxpParser = factory.newDocumentBuilder();
            Method newDocBuilder =
                factoryClass.getMethod("newDocumentBuilder", null);
            Object jaxpParser  = newDocBuilder.invoke(factory, null);

            // jaxpParser.setErrorHandler(null);
            Class parserClass = jaxpParser.getClass();
            Method setErrorHandler =
                parserClass.getMethod("setErrorHandler",
                                 new Class[]{org.xml.sax.ErrorHandler.class});
            setErrorHandler.invoke(jaxpParser,
                                 new Object[]{new BuilderErrorHandler()});

            // domDoc = jaxpParser.parse(in);
            Method parse = parserClass.getMethod(
                "parse", new Class[]{InputStream.class});
            org.w3c.dom.Document domDoc = (org.w3c.dom.Document)
                parse.invoke(jaxpParser, new Object[]{in});

            return domDoc;
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            if (targetException instanceof IOException) {
                throw (IOException) targetException;
            } else {
                throw new JDOMException(targetException.getMessage(), targetException);
            }
        } catch (Exception e) {
            throw new JDOMException("Reflection failed while parsing a document with JAXP", e); 
        }

        // Allow all exceptions to pass through
    }

    /**
     * This creates an empty <code>Document</code> object based
     * on a specific parser implementation.
     *
     * @return <code>Document</code> - created DOM Document.
     * @throws JDOMException when errors occur in parsing.
      */
    public Document createDocument() 
        throws JDOMException {

        try {
            // We need DOM Level 2 and thus JAXP 1.1.
            // If JAXP 1.0 is all that's available then we error out.
            Class.forName("javax.xml.transform.Transformer");

            // Try JAXP 1.1 calls to build the document
            Class factoryClass =
                Class.forName("javax.xml.parsers.DocumentBuilderFactory");

            // factory = DocumentBuilderFactory.newInstance();
            Method newParserInstance =
                factoryClass.getMethod("newInstance", null);
            Object factory = newParserInstance.invoke(null, null);

            // jaxpParser = factory.newDocumentBuilder();
            Method newDocBuilder =
                factoryClass.getMethod("newDocumentBuilder", null);
            Object jaxpParser  = newDocBuilder.invoke(factory, null);

            // domDoc = jaxpParser.newDocument();
            Class parserClass = jaxpParser.getClass();
            Method newDoc = parserClass.getMethod("newDocument", null);
            org.w3c.dom.Document domDoc =
                (org.w3c.dom.Document) newDoc.invoke(jaxpParser, null);

            return domDoc;
        } catch (Exception e) {
            throw new JDOMException("Reflection failed while creating new JAXP document", e); 
        }

    }
}
