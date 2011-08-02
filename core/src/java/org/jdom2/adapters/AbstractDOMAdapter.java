/*-- 

 $Id: AbstractDOMAdapter.java,v 1.21 2007/11/10 05:28:59 jhunter Exp $

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
import org.w3c.dom.*;
import org.w3c.dom.Document;

/**
 * A DOMAdapter utility abstract base class.
 * 
 * @version $Revision: 1.21 $, $Date: 2007/11/10 05:28:59 $
 * @author  Brett McLaughlin
 * @author  Jason Hunter
 */
public abstract class AbstractDOMAdapter implements DOMAdapter {

    private static final String CVS_ID = 
      "@(#) $RCSfile: AbstractDOMAdapter.java,v $ $Revision: 1.21 $ $Date: 2007/11/10 05:28:59 $ $Name:  $";

    /**
     * This creates a new <code>{@link Document}</code> from an
     * existing <code>InputStream</code> by letting a DOM
     * parser handle parsing using the supplied stream.
     *
     * @param filename file to parse.
     * @param validate <code>boolean</code> to indicate if validation should occur.
     * @return <code>Document</code> - instance ready for use.
     * @throws IOException when I/O error occurs.
     * @throws JDOMException when errors occur in parsing.
     */
    public Document getDocument(File filename, boolean validate)
        throws IOException, JDOMException {

        return getDocument(new FileInputStream(filename), validate);
    }

    /**
     * This creates a new <code>{@link Document}</code> from an
     * existing <code>InputStream</code> by letting a DOM
     * parser handle parsing using the supplied stream.
     *
     * @param in <code>InputStream</code> to parse.
     * @param validate <code>boolean</code> to indicate if validation should occur.
     * @return <code>Document</code> - instance ready for use.
     * @throws IOException when I/O error occurs.
     * @throws JDOMException when errors occur in parsing.
     */
    public abstract Document getDocument(InputStream in, boolean validate)
        throws IOException, JDOMException;

    /**
     * This creates an empty <code>Document</code> object based
     * on a specific parser implementation.
     *
     * @return <code>Document</code> - created DOM Document.
     * @throws JDOMException when errors occur.
     */
    public abstract Document createDocument() throws JDOMException;

    /**
     * This creates an empty <code>Document</code> object based
     * on a specific parser implementation with the given DOCTYPE.
     * If the doctype parameter is null, the behavior is the same as
     * calling <code>createDocument()</code>.
     *
     * @param doctype Initial <code>DocType</code> of the document.
     * @return <code>Document</code> - created DOM Document.
     * @throws JDOMException when errors occur.
     */
    public Document createDocument(DocType doctype) throws JDOMException {
        if (doctype == null) {
            return createDocument();
        }
  
        DOMImplementation domImpl = createDocument().getImplementation();
        DocumentType domDocType = domImpl.createDocumentType(
                                      doctype.getElementName(),
                                      doctype.getPublicID(),
                                      doctype.getSystemID());

        // Set the internal subset if possible
        setInternalSubset(domDocType, doctype.getInternalSubset());

        return domImpl.createDocument("http://temporary",
                                      doctype.getElementName(),
                                      domDocType);
    }

    /**
     * This attempts to change the DocumentType to have the given internal DTD 
     * subset value.  This is not a standard ability in DOM, so it's only
     * available with some parsers.  Subclasses can alter the mechanism by
     * which the attempt is made to set the value.
     *
     * @param dt DocumentType to be altered
     * @param s String to use as the internal DTD subset
     */
    protected void setInternalSubset(DocumentType dt, String s) {
        if (dt == null || s == null) return;

        // Default behavior is to attempt a setInternalSubset() call using
        // reflection.  This method is not part of the DOM spec, but it's
        // available on Xerces 1.4.4+.  It's not currently in Crimson.
        try {
            Class dtclass = dt.getClass();
            Method setInternalSubset = dtclass.getMethod(
                "setInternalSubset", new Class[] {java.lang.String.class});
            setInternalSubset.invoke(dt, new Object[] {s});
        }
        catch (Exception e) {
            // ignore
        }
    }
}
