/*--

 $Id: JDOMTransformer.java,v 1.3 2003/05/02 18:59:51 jhunter Exp $

 Copyright (C) 2001 Jason Hunter & Brett McLaughlin.
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

package org.jdom.transform;

import java.util.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamSource;
import org.jdom.*;

/**
 * A convenience class to handle simple transformations. The JAXP TrAX classes
 * have more bells and whistles and can be used with {@link JDOMSource} and
 * {@link JDOMResult} for advanced uses. This class handles the common case and
 * presents a simple interface.  JDOMTransformer is thread safe and may be
 * used from multiple threads.
 *
 * <pre><code>
 * JDOMTransformer transformer = new JDOMTransformer("file.xsl");
 *
 * Document x2 = transformer.transform(x);  // x is a Document
 * Document y2 = transformer.transform(y);  // y is a Document
 * </code></pre>
 *
 * @version $Revision: 1.3 $, $Date: 2003/05/02 18:59:51 $
 * @author  Jason Hunter
 */
public class JDOMTransformer {

    private static final String CVS_ID =
            "@(#) $RCSfile: JDOMTransformer.java,v $ $Revision: 1.3 $ $Date: 2003/05/02 18:59:51 $ $Name:  $";

    private Templates templates;

    /**
     * Creates a transformer for a given {@link javax.xml.transform.Source}
     * stylesheet.
     *
     * @param  stylesheet          source stylesheet as a Source object
     * @throws JDOMException       if there's a problem in the TrAX back-end
     */
    public JDOMTransformer(Source stylesheet) throws JDOMException {
        try {
            templates = TransformerFactory.newInstance()
                    .newTemplates(stylesheet);
        }
        catch (TransformerException e) {
            throw new JDOMException("Could not construct JDOMTransformer", e);
        }
    }

    /**
     * Creates a transformer for a given stylesheet system id.
     *
     * @param  stylesheetSystemId  source stylesheet as a Source object
     * @throws JDOMException       if there's a problem in the TrAX back-end
     */
    public JDOMTransformer(String stylesheetSystemId) throws JDOMException {
        try {
            templates = TransformerFactory.newInstance()
                .newTemplates(new StreamSource(stylesheetSystemId));
        }
        catch (TransformerException e) {
            throw new JDOMException("Could not construct JDOMTransformer", e);
        }
    }

    /**
     * Transforms the given input nodes to a list of output nodes.
     *
     * @param  inputNodes          input nodes
     * @return                     transformed output nodes
     * @throws JDOMException       if there's a problem in the transformation
     */
    public List transform(List inputNodes) throws JDOMException {
        JDOMSource source = new JDOMSource(inputNodes);
        JDOMResult result = new JDOMResult();
        try {
            templates.newTransformer().transform(source, result);
            return result.getResult();
        }
        catch (TransformerException e) {
            throw new JDOMException("Could not perform transformation", e);
        }
    }

    /**
     * Transforms the given document to an output document.
     *
     * @param  inputDoc            input document
     * @return                     transformed output document
     * @throws JDOMException       if there's a problem in the transformation
     */
    public Document transform(Document inputDoc) throws JDOMException {
        JDOMSource source = new JDOMSource(inputDoc);
        JDOMResult result = new JDOMResult();
        try {
            templates.newTransformer().transform(source, result);
            return result.getDocument();
        }
        catch (TransformerException e) {
            throw new JDOMException("Could not perform transformation", e);
        }
    }
}
