/*-- 

 Copyright (C) 2000 Brett McLaughlin & Jason Hunter. All rights reserved.
 
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

package org.jdom.contrib.input;

import java.io.*;
import java.net.*;
import org.jdom.*;
import com.XML_Technologies.Spitfire.Handlers.JDOM1.*;

/**
 * <p><code>SpitfireBuilder</code> builds a JDOM <code>Document</code> 
 *   using the Spitfire beta1 parser from XML-Technologies (not available 
 *   with this distribution).
 * </p>
 * 
 * <p>Eventually this class should have as many build methods as SAXBuilder,
 *    but right now it's just a straight pass through to JDOM1Driver.</p>
 *
 * <p>THIS EXAMPLE WILL NOT WORK UNLESS YOU DOWNLOAD THE SPITFIRE PARSER
 *    BETA1 VERSION AND PUT IT IN YOUR CLASSPATH</p>
 *
 * @author Jason Hunter
 * @version 1.0
 */
public class SpitfireBuilder {

    /** <code>{@link JDOM1Driver}</code> instance to use */
    private JDOM1Driver builder;

    /**
     * <p>
     * This creates a document builder, without validation.
     * </p>
     */
    public SpitfireBuilder() {
        builder = new JDOM1Driver();
    }

    /**
     * <p>
     * This creates a document builder, with optional validation.
     * </p>
     *
     * @param validate whether to validate the document
     */
    public SpitfireBuilder(boolean validate) {
        builder = new JDOM1Driver(validate);
    }

    /**
     * <p>
     * This builds a document from the supplied
     *   input stream.
     * </p>
     *
     * @param in <code>InputStream</code> to read from.
     * @return <code>Document</code> - resultant Document object.
     * @throws <code>JDOMException</code> when errors occur in
     *                                    parsing.
     */
    public Document build(InputStream in) throws JDOMException {
        return builder.build(in);
    }

    /**
     * <p>
     * This builds a document from the supplied
     *   filename.
     * </p>
     *
     * @param file <code>File</code> to read from.
     * @return <code>Document</code> - resultant Document object.
     * @throws <code>JDOMException</code> when errors occur in
     *                                    parsing.
     */
    public Document build(File file) throws JDOMException {
        return builder.build(file);
    }

    /**
     * <p>
     * This builds a document from the supplied
     *   URL.
     * </p>
     *
     * @param url <code>URL</code> to read from.
     * @return <code>Document</code> - resultant Document object.
     * @throws <code>JDOMException</code> when errors occur in
     *                                    parsing.
     */
    public Document build(URL url) throws JDOMException {
        return builder.build(url);
    }

    /**
     * <p>
     * This builds a document from the supplied
     *   URI.
     * </p>
     * @param systemId URI for the input
     * @return <code>Document</code> - resultant Document object.
     * @throws <code>JDOMException</code> when errors occur in
     *                                    parsing.
     */
    public Document build(String systemId) throws JDOMException {
        return builder.build(systemId);
    }
}
