/*-- 

 $Id: DOMAdapter.java,v 1.22 2007/11/10 05:28:59 jhunter Exp $

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

import org.jdom.*;
import org.w3c.dom.Document;

/**
 * Defines a standard set of adapter methods for interfacing with a DOM parser
 * and obtaining a DOM {@link org.w3c.dom.Document org.w3c.dom.Document} object.
 * Implementing classes map these calls to DOM parser-specific calls, allowing
 * any third-party parser to be used with JDOM.
 *
 * @version $Revision: 1.22 $, $Date: 2007/11/10 05:28:59 $
 * @author  Brett McLaughlin
 * @author  Jason Hunter
 */
public interface DOMAdapter {

    /**
     * This creates a new <code>Document</code> from a
     * given filename by letting a DOM parser handle parsing from the file.
     *
     * @param filename file to parse.
     * @param validate <code>boolean</code> to indicate if validation 
     * should occur.
     * @return <code>Document</code> - instance ready for use.
     * @throws IOException when I/O error occurs.
     * @throws JDOMException when errors occur in parsing.
     */
    public Document getDocument(File filename, boolean validate)
        throws IOException, JDOMException;

    /**
     * This creates a new <code>Document</code> from an
     * existing <code>InputStream</code> by letting a DOM
     * parser handle parsing using the supplied stream.
     *
     * @param in <code>InputStream</code> to parse.
     * @param validate <code>boolean</code> to indicate if validation 
     * should occur.
     * @return <code>Document</code> - instance ready for use.
     * @throws IOException when I/O error occurs.
     * @throws JDOMException when errors occur in parsing.
     */
    public Document getDocument(InputStream in, boolean validate)
        throws IOException, JDOMException;

    /**
     * This creates an empty <code>Document</code> object based
     * on a specific parser implementation.
     *
     * @return <code>Document</code> - created DOM Document.
     * @throws JDOMException when errors occur.
     */
    public Document createDocument() throws JDOMException;

    /**
     * This creates an empty <code>Document</code> object based
     * on a specific parser implementation with the given DOCTYPE.
     *
     * @param doctype Initial <code>DocType</code> of the document.
     * @return <code>Document</code> - created DOM Document.
     * @throws JDOMException when errors occur.
     */
    public Document createDocument(DocType doctype) throws JDOMException;
}
