/*-- 

 $Id: JDOMResult.java,v 1.10 2002/05/11 07:24:21 jhunter Exp $

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

import java.io.*;
import java.util.*;

import org.xml.sax.*;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.XMLFilterImpl;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;

import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.TransformerFactory; // workaround for @link bug

/**
 * Acts as a holder for a transformation result tree, in the form
 * of a JDOM Document.
 * <p>
 * This class shall be used to get the result of XSL Transformation
 * as a JDOM Document.
 * </p>
 * <p>
 * The following example shows how to apply an XSL Transformation
 * to a JDOM document and get the transformation result in the form
 * of another JDOM Document:</p>
 * <blockquote><pre>
 *   public static Document transform(Document in, String stylesheet)
 *                                      throws JDOMException {
 *     try {
 *       Transformer transformer = TransformerFactory.newInstance()
 *                             .newTransformer(new StreamSource(stylesheet));
 *       JDOMResult out = new JDOMResult();
 *       transformer.transform(new JDOMSource(in), out);
 *       return out.getDocument();
 *     }
 *     catch (TransformerException e) {
 *       throw new JDOMException("XSLT Trandformation failed", e);
 *     }
 *   }
 * </pre></blockquote>
 * </p>
 *
 * @see      org.jdom.transform.JDOMSource
 *
 * @author  Laurent Bihanic
 * @author  Jason Hunter
 * @version $Revision: 1.10 $, $Date: 2002/05/11 07:24:21 $
 */
public class JDOMResult extends SAXResult {

  /**
   * If {@link javax.xml.transform.TransformerFactory#getFeature}
   * returns <code>true</code> when passed this value as an
   * argument, the Transformer natively supports JDOM.
   * <p>
   * <strong>Note</strong>: This implementation does not override
   * the {@link SAXResult#FEATURE} value defined by its superclass
   * to be considered as a SAXResult by Transformer implementations
   * not natively supporting JDOM.
   * </p>
   */
  public final static String JDOM_FEATURE =
                      "http://org.jdom.transform.JDOMResult/feature";

  /**
   * The JDOM document result of a transformation, as set by
   * Transformer implementations that natively support JDOM.
   */
  private Document result = null;

  /**
   * The custom JDOM factory to use when building the result document
   * or <code>null</code> to use the default JDOM classes.
   */
  private JDOMFactory factory = null;

  /**
   * Default empty constructor.
   */
  public JDOMResult() {
    // Allocate custom builder object...
    DocumentBuilder builder = new DocumentBuilder();

    // And use it as ContentHandler and LexicalHandler.
    super.setHandler(builder);
    super.setLexicalHandler(builder);
  }

  /**
   * Sets the document produced as result of an XSL Transformation.
   * <p>
   * <strong>Note</strong>: This method shall be used by the
   * {@link javax.xml.transform.Transformer} implementations that
   * natively support JDOM to directly set the transformation
   * result rather than considering this object as a
   * {@link SAXResult}.  Applications should <i>not</i> use this
   * method.
   * </p>
   *
   * @param  document   the JDOM document result of a transformation.
   *
   * @see    #getDocument
   */
  public void setDocument(Document document) {
    this.result = document;
  }

  /**
   * Returns the document produced as result of an XSL Transformation.
   *
   * @return the transformation result as a JDOM document.
   */
  public Document getDocument() {
    return this.result;
  }

  /**
   * Sets a custom JDOMFactory to use when building the result
   * document. Use a custom factory to build the tree with your
   * own subclasses of the JDOM classes.
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
   * Returns the custom JDOMFactory used to build the result
   * document.
   *
   * @return the custom <code>JDOMFactory</code> used to build the
   *         result document or <code>null</code> if the default
   *         JDOM classes are being used.
   *
   * @see    #setFactory
   */
  public JDOMFactory getFactory() {
    return this.factory;
  }

  //-------------------------------------------------------------------------
  // SAXResult overwritten methods
  //-------------------------------------------------------------------------

  /**
   * Sets the target to be a SAX2 ContentHandler.
   *
   * @param handler Must be a non-null ContentHandler reference.
   */
  public void setHandler(ContentHandler handler) { }

  /**
   * Sets the SAX2 LexicalHandler for the output.
   * <p>
   * This is needed to handle XML comments and the like.  If the
   * lexical handler is not set, an attempt should be made by the
   * transformer to cast the ContentHandler to a LexicalHandler.
   * </p>
   *
   * @param handler A non-null LexicalHandler for
   *                handling lexical parse events.
   */
  public void setLexicalHandler(LexicalHandler handler) { }


  //=========================================================================
  // DocumentBuilder inner class
  //=========================================================================

  private class DocumentBuilder extends XMLFilterImpl
                                implements LexicalHandler {
    /**
     * The actual JDOM document builder.
     */
    private SAXHandler saxHandler = null;

    /**
     * Public default constructor.
     */
    DocumentBuilder() { }

    //----------------------------------------------------------------------
    // XMLFilterImpl overwritten methods
    //----------------------------------------------------------------------

    /**
     * Processes a start of document event.
     * <p>
     * This implementation creates a new JDOM document builder and
     * marks the current document as "under construction".
     * </p>
     *
     * @throws SAXException   if any error occurred while creating
     *                        the document builder.
     *
     * @see    SAXHandler
     */
    public void startDocument() throws SAXException {
        // Reset any previously set result document.
        setDocument(null);

        // Create the actual JDOM document builder and register it as
        // ContentHandler on the superclass (XMLFilterImpl): this
        // implementation will take care of propagating the LexicalHandler
        // events.
        this.saxHandler = new SAXHandler(getFactory());
        super.setContentHandler(this.saxHandler);

        // And propagate event.
        super.startDocument();
    }

    /**
     * Processes an end of document event.
     * <p>
     * This implementation makes the built document available.
     * </p>
     *
     * @throws SAXException   if thrown by the actual document
     *                        builder.
     */
    public void endDocument() throws SAXException {
      // Propagate event.
      super.endDocument();

      // And make the result document available.
      setDocument(this.saxHandler.getDocument());
    }

    //----------------------------------------------------------------------
    // LexicalHandler interface support
    //----------------------------------------------------------------------

    /**
     * Report the start of DTD declarations, if any.
     *
     * @param name The document type name.
     * @param publicId The declared public identifier for the
     *        external DTD subset, or null if none was declared.
     * @param systemId The declared system identifier for the
     *        external DTD subset, or null if none was declared.
     *
     * @exception SAXException The application may raise an
     *            exception.
     */
    public void startDTD(String name, String publicId, String systemId)
                                        throws SAXException {
      this.saxHandler.startDTD(name, publicId, systemId);
    }

    /**
     * Report the end of DTD declarations.
     *
     * @exception SAXException The application may raise an exception.
     */
    public void endDTD() throws SAXException {
      this.saxHandler.endDTD();
    }

    /**
     * Report the beginning of some internal and external XML entities.
     *
     * @param name The name of the entity.  If it is a parameter
     *        entity, the name will begin with '%', and if it is the
     *        external DTD subset, it will be "[dtd]".
     *
     * @exception SAXException The application may raise an exception.
     */
    public void startEntity(String name) throws SAXException {
      this.saxHandler.startEntity(name);
    }

    /**
     * Report the end of an entity.
     *
     * @param name The name of the entity that is ending.
     *
     * @exception SAXException The application may raise an exception.
     */
    public void endEntity(String name) throws SAXException {
      this.saxHandler.endEntity(name);
    }

    /**
     * Report the start of a CDATA section.
     *
     * @exception SAXException The application may raise an exception.
     */
    public void startCDATA() throws SAXException {
      this.saxHandler.startCDATA();
    }

    /**
     * Report the end of a CDATA section.
     *
     * @exception SAXException The application may raise an exception.
     */
    public void endCDATA() throws SAXException {
      this.saxHandler.endCDATA();
    }

    /**
     * Report an XML comment anywhere in the document.
     *
     * @param ch An array holding the characters in the comment.
     * @param start The starting position in the array.
     * @param length The number of characters to use from the array.
     *
     * @exception SAXException The application may raise an exception.
     */
    public void comment(char ch[], int start, int length)
                                  throws SAXException {
      this.saxHandler.comment(ch, start, length);
    }
  }
}

