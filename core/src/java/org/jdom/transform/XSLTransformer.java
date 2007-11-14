/*--

 $Id: XSLTransformer.java,v 1.5 2007/11/14 04:36:54 jhunter Exp $

 Copyright (C) 2001-2007 Jason Hunter & Brett McLaughlin.
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
import java.io.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamSource;
import org.jdom.*;
import org.xml.sax.EntityResolver;

/**
 * A convenience class to handle simple transformations. The JAXP TrAX classes
 * have more bells and whistles and can be used with {@link JDOMSource} and
 * {@link JDOMResult} for advanced uses. This class handles the common case and
 * presents a simple interface.  XSLTransformer is thread safe and may be
 * used from multiple threads.
 *
 * <pre><code>
 * XSLTransformer transformer = new XSLTransformer("file.xsl");
 *
 * Document x2 = transformer.transform(x);  // x is a Document
 * Document y2 = transformer.transform(y);  // y is a Document
 * </code></pre>
 *
 *  JDOM relies on TrAX to perform the transformation.
 *  The <code>javax.xml.transform.TransformerFactory</code> Java system property
 *  determines which XSLT engine TrAX uses. Its value should be
 *  the fully qualified name of the implementation of the abstract
 *  <code>javax.xml.transform.TransformerFactory</code> class.
 *  Values of this property for popular XSLT processors include:
 *  </p>
 *  <ul><li>Saxon 6.x: <code>com.icl.saxon.TransformerFactoryImpl</code></li>
 *  <li>Saxon 7.x: <code>net.sf.saxon.TransformerFactoryImpl</code></li>
 *  <li>Xalan: <code>org.apache.xalan.processor.TransformerFactoryImpl</code></li>
 *  <li>jd.xslt: <code>jd.xml.xslt.trax.TransformerFactoryImpl</code></li>
 *  <li>Oracle: <code>oracle.xml.jaxp.JXSAXTransformerFactory</code></li>
 *  </ul>
 *  <p>
 *   This property can be set in all the usual ways a Java system property
 *   can be set. TrAX picks from them in this order:</p>
 *   <ol>
 *   <li> Invoking <code>System.setProperty( "javax.xml.transform.TransformerFactory",
 *     "<i><code>classname</code></i>")</code></li>
 *   <li>The value specified at the command line using the
 *      <tt>-Djavax.xml.transform.TransformerFactory=<i><code>classname</code></i></tt>
 *      option to the <b>java</b> interpreter</li>
 *    <li>The class named in the  <code>lib/jaxp.properties</code> properties file
 *         in the JRE directory, in a line like this one:
 *      <pre>javax.xml.parsers.DocumentBuilderFactory=<i><code>classname</code></i></pre></li>
 *    <li>The class named in the
 *   <code>META-INF/services/javax.xml.transform.TransformerFactory</code> file
 *   in the JAR archives available to the runtime</li>
 *   <li>Finally, if all of the above options fail,
 *    a default implementation is chosen. In Sun's JDK 1.4, this is
 *       Xalan 2.2d10. </li>
 *    </ol>

 * @version $Revision: 1.5 $, $Date: 2007/11/14 04:36:54 $
 * @author  Jason Hunter
 * @author  Elliotte Rusty Harold
 */
public class XSLTransformer {

    private static final String CVS_ID =
            "@(#) $RCSfile: XSLTransformer.java,v $ $Revision: 1.5 $ $Date: 2007/11/14 04:36:54 $ $Name:  $";

    private Templates templates;

    /**
     * The custom JDOM factory to use when building the transformation
     * result or <code>null</code> to use the default JDOM classes.
     */
    private JDOMFactory factory = null;

    // Internal constructor to support the other constructors
    private XSLTransformer(Source stylesheet) throws XSLTransformException {
        try {
            templates = TransformerFactory.newInstance()
                    .newTemplates(stylesheet);
        }
        catch (TransformerException e) {
            throw new XSLTransformException("Could not construct XSLTransformer", e);
        }
    }

    /**
     * Creates a transformer for a given stylesheet system id.
     *
     * @param  stylesheetSystemId  source stylesheet as a Source object
     * @throws XSLTransformException       if there's a problem in the TrAX back-end
     */
    public XSLTransformer(String stylesheetSystemId) throws XSLTransformException {
        this(new StreamSource(stylesheetSystemId));
    }

    /**
     * <p>
     * This will create a new <code>XSLTransformer</code> by
     *  reading the stylesheet from the specified
     *   <code>InputStream</code>.
     * </p>
     *
     * @param stylesheet <code>InputStream</code> from which the stylesheet is read.
     * @throws XSLTransformException when an IOException, format error, or
     * something else prevents the stylesheet from being compiled
     */
    public XSLTransformer(InputStream stylesheet) throws XSLTransformException {
        this(new StreamSource(stylesheet));
    }

    /**
     * <p>
     * This will create a new <code>XSLTransformer</code> by
     *  reading the stylesheet from the specified
     *   <code>Reader</code>.
     * </p>
     *
     * @param stylesheet <code>Reader</code> from which the stylesheet is read.
     * @throws XSLTransformException when an IOException, format error, or
     * something else prevents the stylesheet from being compiled
     */
    public XSLTransformer(Reader stylesheet) throws XSLTransformException {
        this(new StreamSource(stylesheet));
    }

    /**
     * <p>
     * This will create a new <code>XSLTransformer</code> by
     *  reading the stylesheet from the specified
     *   <code>File</code>.
     * </p>
     *
     * @param stylesheet <code>File</code> from which the stylesheet is read.
     * @throws XSLTransformException when an IOException, format error, or
     * something else prevents the stylesheet from being compiled
     */
    public XSLTransformer(File stylesheet) throws XSLTransformException {
        this(new StreamSource(stylesheet));
    }

    /**
     * <p>
     * This will create a new <code>XSLTransformer</code> by
     *  reading the stylesheet from the specified
     *   <code>Document</code>.
     * </p>
     *
     * @param stylesheet <code>Document</code> containing the stylesheet.
     * @throws XSLTransformException when the supplied <code>Document</code>
     *  is not syntactically correct XSLT
     */
    public XSLTransformer(Document stylesheet) throws XSLTransformException {
        this(new JDOMSource(stylesheet));
    }

    /**
     * Transforms the given input nodes to a list of output nodes.
     *
     * @param  inputNodes          input nodes
     * @return                     transformed output nodes
     * @throws XSLTransformException       if there's a problem in the transformation
     */
    public List transform(List inputNodes) throws XSLTransformException {
        JDOMSource source = new JDOMSource(inputNodes);
        JDOMResult result = new JDOMResult();
        result.setFactory(factory);  // null ok
        try {
            templates.newTransformer().transform(source, result);
            return result.getResult();
        }
        catch (TransformerException e) {
            throw new XSLTransformException("Could not perform transformation", e);
        }
    }
    
    /**
     * Transforms the given document to an output document.
     *
     * @param  inputDoc            input document
     * @return                     transformed output document
     * @throws XSLTransformException       if there's a problem in the transformation
     */
    public Document transform(Document inputDoc) throws XSLTransformException {
    	return transform(inputDoc, null);
    }

    /**
     * Transforms the given document to an output document.
     *
     * @param  inputDoc            input document
     * @param  resolver			   entity resolver for the input document
     * @return                     transformed output document
     * @throws XSLTransformException       if there's a problem in the transformation
     */
    public Document transform(Document inputDoc, EntityResolver resolver) throws XSLTransformException {
        JDOMSource source = new JDOMSource(inputDoc, resolver);
        JDOMResult result = new JDOMResult();
        result.setFactory(factory);  // null ok
        try {
            templates.newTransformer().transform(source, result);
            return result.getDocument();
        }
        catch (TransformerException e) {
            throw new XSLTransformException("Could not perform transformation", e);
        }
    }

    /**
     * Sets a custom JDOMFactory to use when building the
     * transformation result. Use a custom factory to build the tree
     * with your own subclasses of the JDOM classes.
     *
     * @param  factory   the custom <code>JDOMFactory</code> to use or
     *                   <code>null</code> to use the default JDOM
     *                   classes.
     *
     * @see    #getFactory
     */
    public void setFactory(JDOMFactory factory) {
      this.factory = factory;
    }

    /**
     * Returns the custom JDOMFactory used to build the transformation
     * result.
     *
     * @return the custom <code>JDOMFactory</code> used to build the
     *         transformation result or <code>null</code> if the
     *         default JDOM classes are being used.
     *
     * @see    #setFactory
     */
    public JDOMFactory getFactory() {
      return this.factory;
    }
}
