/*
* <to-be-provided />
*/

package org.jdom.contrib.transform;

import java.io.*;
import java.util.*;

import org.xml.sax.*;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.XMLFilterImpl;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;

import javax.xml.transform.sax.SAXResult;

/**
 * Acts as an holder for JDOM document sources.
 * <p>
 * This class shall be used to get the result of XSL Transformation
 * as a JDOM Document.</p>
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
 *
 * @see      org.jdom.output.JDOMSource
 *
 * @author   Laurent Bihanic
 * @author   Jason Hunter
 */
public class JDOMResult extends SAXResult {

  private DocumentBuilder documentBuilder = null;

  /**
   * Default empty contructor.
   */
  public JDOMResult() {
    documentBuilder = new DocumentBuilder();
    super.setHandler(this.documentBuilder);
    super.setLexicalHandler(this.documentBuilder);
  }

  /**
   * Returns the document produced as result of the XSL Transformation.
   *
   * @return the transformation result as a JDOM document.
   */
  public Document getDocument() {
    return documentBuilder.getDocument();
  }

  //-------------------------------------------------------------------------
  // SAXResult overwritten methods
  //-------------------------------------------------------------------------

  /**
   * Set the target to be a SAX2 ContentHandler.
   *
   * @param handler Must be a non-null ContentHandler reference.
   */
  public void setHandler(ContentHandler handler) { }

  /**
   * Set the SAX2 LexicalHandler for the output.
   * 
   * <p>This is needed to handle XML comments and the like.  If the 
   * lexical handler is not set, an attempt should be made by the 
   * transformer to cast the ContentHandler to a LexicalHandler.</p>
   *
   * @param handler A non-null LexicalHandler for 
   * handling lexical parse events.
   */
  public void setLexicalHandler(LexicalHandler handler) { }


  //=========================================================================
  // DocumentBuilder nested class 
  //=========================================================================

  private static class DocumentBuilder extends XMLFilterImpl
                                       implements LexicalHandler {
    /**
     * The JDOM document to populate as a result of the XSL
     * Transformation.
     */
    private Document resultDocument = null;

    private SAXHandler saxHandler = null;

    /**
     * Whether the transformation is complete.
     */
    private boolean documentReady = false;

    /**
     * Public default constructor.
     */
    DocumentBuilder() { }

    public Document getDocument() {
      return (this.documentReady == true) ? this.resultDocument : null;
    }

    //----------------------------------------------------------------------
    // XMLFilterImpl overwritten methods
    //----------------------------------------------------------------------

    public void startDocument() throws SAXException {
      try {
        this.documentReady  = false;
        this.resultDocument = new Document((Element)null);
        this.saxHandler     = new SAXHandler(this.resultDocument);
  
        super.setContentHandler(this.saxHandler);
        super.startDocument();
      }
      catch (IOException e) {
        throw new SAXException("SAXHandler allocation failure", e);
      }
    }
  
    public void endDocument() throws SAXException {
      this.documentReady = true;
      super.endDocument();
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

