/*-- 

 $Id: JDOMSource.java,v 1.8 2002/04/29 02:30:47 jhunter Exp $

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
    written permission, please contact <pm_AT_jdom_DOT_org>.
 
 4. Products derived from this software may not be called "JDOM", nor
    may "JDOM" appear in their name, without prior written permission
    from the JDOM Project Management <pm_AT_jdom_DOT_org>.
 
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
import org.xml.sax.helpers.XMLFilterImpl;

import org.jdom.*;
import org.jdom.output.*;

import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.TransformerFactory; // workaround for @link bug

/**
 * Acts as an holder for JDOM document sources.
 * <p>
 * This class shall be used to wrap a JDOM Document to provide it
 * as input to a JAXP Transformer</p>
 * <p>
 * The following example shows how to apply an XSL Transformation
 * to a JDOM document and get the transformation result in the form
 * of another JDOM Document:</p>
 * <blockquote><pre>
 *   public static Document transform(Document in, String stylesheet)
 *                                        throws JDOMException {
 *     try {
 *       Transformer transformer = TransformerFactory.newInstance()
 *          .newTransformer(new StreamSource(stylesheet));
 *
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
 * @see org.jdom.transform.JDOMResult
 *
 * @author Laurent Bihanic
 * @author Jason Hunter
 * @version $Revision: 1.8 $, $Date: 2002/04/29 02:30:47 $
 */
public class JDOMSource extends SAXSource {

  /**
   * If {@link javax.xml.transform.TransformerFactory#getFeature}
   * returns <code>true</code> when passed this value as an
   * argument, the Transformer natively supports JDOM.
   * <p>
   * <strong>Note</strong>: This implementation does not override
   * the {@link SAXSource#FEATURE} value defined by its superclass
   * to be considered as a SAXSource by Transformer implementations
   * not natively supporting JDOM.</p>
   */
  public final static String JDOM_FEATURE =
                      "http://org.jdom.transform.JDOMSource/feature";

  /**
   * An optional (chain of) SAX XMLFilter to apply to the SAX events
   * describing the JDOM document.
   *
   * @see    #getXMLReader
   */
  private XMLFilter xmlFilter = null;

  /**
   * Creates a JDOM TRaX source wrapping a JDOM document.
   *
   * @param  source   the JDOM document to use as source for the
   *                  transformations
   *
   * @throws NullPointerException   if <code>source</code> is
   *                                <code>null</code>.
   *
   * @see    #setDocument
   */
  public JDOMSource(Document source) {
    setDocument(source);
  }

  /**
   * Sets the source document used by this TRaX source.
   *
   * @param  source   the JDOM document to use as source for the
   *                  transformations
   *
   * @throws NullPointerException   if <code>source</code> is
   *                                <code>null</code>.
   *
   * @see    #getDocument
   */
  public void setDocument(Document source) {
    super.setInputSource(new JDOMInputSource(source));
  }

  /**
   * Returns the source document used by this TRaX source.
   *
   * @return the source document used by this TRaX source or
   *         <code>null</code> if none has been set.
   *
   * @see    #setDocument
   */
  public Document getDocument() {
    return ((JDOMInputSource) getInputSource()).getDocument();
  }


  //-------------------------------------------------------------------------
  // SAXSource overwritten methods
  //-------------------------------------------------------------------------

  /**
   * Sets the SAX InputSource to be used for the Source.
   * <p>
   * As this implementation only supports JDOM document as data
   * source, this method always throws an
   * {@link UnsupportedOperationException}.
   *
   * @param  inputSource   a valid InputSource reference.
   *
   * @throws UnsupportedOperationException   always!
   */
  public void setInputSource(InputSource inputSource)
                                  throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * Set the XMLReader to be used for the Source.
   * <p>
   * As this implementation only supports JDOM document as data
   * source, this method throws an
   * {@link UnsupportedOperationException} if the provided reader
   * object does not implement the SAX {@link XMLFilter}
   * interface.  Otherwise, the JDOM document reader will be
   * attached as parent of the filter chain.</p>
   *
   * @param  reader   a valid XMLReader or XMLFilter reference.
   *
   * @throws UnsupportedOperationException   always!
   *
   * @see    #getXMLReader
   */
  public void setXMLReader(XMLReader reader)
                              throws UnsupportedOperationException {
    if (reader instanceof XMLFilter) {
      this.xmlFilter = (XMLFilter)reader;
    }
    else {
      throw new UnsupportedOperationException();
    }
  }

  /**
   * Returns the XMLReader to be used for the Source.
   * <p>
   * This implementation returns a specific XMLReader reading
   * the XML data from the source JDOM document.</p>
   *
   * @return an XMLReader reading the XML data from the source
   *         JDOM document.
   */
  public XMLReader getXMLReader() {
    XMLReader documentReader = new DocumentReader();

    // Install filter (if any)
    if (this.xmlFilter != null) {
       // Connect the filter chain to the document reader.
       XMLFilter root = this.xmlFilter;
       while (root.getParent() instanceof XMLFilter) {
         root = (XMLFilter) root.getParent();
       }
       root.setParent(documentReader);

       // Read from filter
       documentReader = this.xmlFilter;
    }
    return documentReader;
  }

  //=========================================================================
  // JDOMInputSource nested class
  //=========================================================================

  /**
   * A subclass of the SAX InputSource interface that wraps a JDOM
   * Document.
   * <p>
   * This class is nested in JDOMSource as it is not intented to
   * be used independently of its friend: DocumentReader.</p>
   *
   * @see    org.jdom.Document
   */
  private static class JDOMInputSource extends InputSource {
    /**
     * The source document.
     */
    private Document document = null;

    /**
     * Builds a Input Source wrapping the specified JDOM Document.
     *
     * @param  source   the source document.
     *
     * @see    #setDocument
     */
    public JDOMInputSource(Document source) {
      setDocument(source);
    }

    /**
     * Sets the source document.
     *
     * @param  source   the JDOM document to use as source.
     *
     * @throws NullPointerException   if <code>source</code> is
     *                                <code>null</code>.
     *
     * @see    #getDocument
     */
    public void setDocument(Document source) {
      if (source == null) {
        throw new NullPointerException("source");
      }
      document = source;
    }

    /**
     * Returns the source document.
     *
     * @return the source document or <code>null</code> if none
     *         has been set.
     *
     * @see    #setDocument
     */
    public Document getDocument() {
      return document;
    }

    //-------------------------------------------------------------------------
    // InputSource overwritten methods
    //-------------------------------------------------------------------------

    /**
     * Sets the character stream for this input source.
     * <p>
     * This implementation always throws an
     * {@link UnsupportedOperationException} as the only source
     * stream supported is the source JDOM document.</p>
     *
     * @param  characterStream   a character stream containing
     *                           an XML document.
     *
     * @throws UnsupportedOperationException  always!
     */
    public void setCharacterStream(Reader characterStream)
                                      throws UnsupportedOperationException {
      throw new UnsupportedOperationException();
    }

    /**
     * Gets the character stream for this input source.
     * <p>
     * Note that this method is only provided to make this
     * InputSource implementation acceptable by any XML
     * parser.  As it generates an in-memory string representation
     * of the JDOM document, it is quite inefficient from both
     * speed and memory consumption points of view.</p>
     *
     * @return a Reader to a string representation of the
     *         source JDOM document.
     */
    public Reader getCharacterStream() {
      Document doc = this.getDocument();
      Reader reader = null;

      if (doc != null) {
        //try {
          // Get an in-memory string representation of the document
          // and return a reader on it.
          reader = new StringReader(new XMLOutputter().outputString(doc));
        //}
        //catch (final IOException outputterError) {
        //  // Oops! Can't stringify document.
        //  // => Return a dummy reader implementation that will
        //  //    notify of the error on every call.
        //  reader = new Reader() {
        //    public int read(char cbuf[], int off, int len)
        //                        throws IOException {
        //      throw outputterError;
        //    }
        //    public void close() throws IOException {
        //      return;
        //    }
        //  };
       // }
      }
      // Else: No document, no reader!

      return reader;
    }
  }

  //=========================================================================
  // DocumentReader nested class
  //=========================================================================

  /**
   * An implementation of the SAX2 XMLReader interface that presents
   * a SAX view of a JDOM Document.  The actual generation of the
   * SAX events is delegated to JDOM's SAXOutputter.
   *
   * @see    org.jdom.Document
   * @see    org.jdom.output.SAXOutputter
   */
  private static class DocumentReader   extends    SAXOutputter
                                        implements XMLReader    {
    /**
     * Public default constructor.
     */
    public DocumentReader() {
      super();
    }

    //----------------------------------------------------------------------
    // SAX XMLReader interface support
    //----------------------------------------------------------------------

    /**
     * Parses an XML document from a system identifier (URI).
     * <p>
     * This implementation does not support reading XML data from
     * system identifiers, only from JDOM documents.  Hence,
     * this method always throws a {@SAXNotSupportedException}.</p>
     *
     * @param  systemId   the system identifier (URI).
     *
     * @throws SAXNotSupportedException   always!
     */
    public void parse(String systemId) throws SAXNotSupportedException {
      throw new SAXNotSupportedException(
                       "Only JDOM Documents are supported as input");
    }

    /**
     * Parses an XML document.
     * <p>
     * The methods accepts only <code>JDOMInputSource</code>s
     * instances as input sources.</p>
     *
     * @param  source   the input source for the top-level of the
     *                  XML document.
     *
     * @throws SAXException               any SAX exception,
     *                                    possibly wrapping
     *                                    another exception.
     * @throws SAXNotSupportedException   if the input source does
     *                                    not wrap a JDOM document.
     */
    public void parse(InputSource input) throws SAXException {
      if (input instanceof JDOMInputSource) {
        try {
          this.output(((JDOMInputSource)input).getDocument());
        }
        catch (JDOMException e) {
          throw new SAXException(e.getMessage(), e);
        }
      }
      else {
        throw new SAXNotSupportedException(
                         "Only JDOM Documents are supported as input");
      }
    }
  }
}

