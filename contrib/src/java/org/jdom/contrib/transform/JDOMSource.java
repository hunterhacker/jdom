/*-- 

 $Id: JDOMSource.java,v 1.2 2001/03/22 09:00:49 jhunter Exp $

 Copyright (C) 2001 Brett McLaughlin & Jason Hunter.
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

package org.jdom.contrib.transform;

import java.io.*;
import java.util.*;

import org.xml.sax.*;
import org.xml.sax.helpers.XMLFilterImpl;

import org.jdom.*;
import org.jdom.output.*;

import javax.xml.transform.sax.SAXSource;

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
 * @see org.jdom.input.JDOMResult
 *
 * @author Laurent Bihanic
 * @author Jason Hunter
 */
public class JDOMSource extends SAXSource {

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
   * @see     getXMLReader
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
        try {
          // Get an in-memory string representation of the document
          // and return a reader on it.
          reader = new StringReader(new XMLOutputter().outputString(doc));
        }
        catch (final IOException outputterError) {
          // Oops! Can't stringify document.
          // => Return a dummy reader implementation that will
          //    notify of the error on every call.
          reader = new Reader() {
            public int read(char cbuf[], int off, int len)
                                throws IOException {
              throw outputterError;
            }
            public void close() throws IOException {
              return;
            }
          };
        }
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
  private static class DocumentReader implements XMLReader {
    /**
     * The JDOM SAXOutputter used to get a SAX2 view of the source
     * JDOM document.
     * <p>
     * As SAXOutputter requires at least a ContentHandler as
     * constructor argument, a dummy
     * {@link XMLFilterImpl empty XMLFilter} is used.</p>
     */
    private SAXOutputter saxOutputter = new SAXOutputter(new XMLFilterImpl());

    /**
     * The SAX ContentHandler associated to this XMLReader.
     * <p>
     * We have to maintain a reference on the ContentHandler as
     * JDOM's SAXOutputter does not provide any getter method.</p>
     */
    private ContentHandler contentHandler = null;

    /**
     * The SAX DTDHandler associated to this XMLReader.
     * <p>
     * We have to maintain a reference on the DTDHandler as
     * JDOM's SAXOutputter does not provide any getter method.</p>
     */
    private DTDHandler dtdHandler = null;

    /**
     * The SAX ErrorHandler associated to this XMLReader.
     * <p>
     * We have to maintain a reference on the ErrorHandler as
     * JDOM's SAXOutputter does not provide any getter method.</p>
     */
    private ErrorHandler errorHandler = null;

    /**
     * The SAX EntityResolver associated to this XMLReader.
     * <p>
     * We have to maintain a reference on the EntityResolver as
     * JDOM's SAXOutputter does not provide any getter method.</p>
     */
    private EntityResolver entityResolver = null;

    /**
     * The SAX features defined for this XMLReader.
     * <p>
     * This class does not define any feature (yet) and ignores
     * the SAX mandatory feature.  Thus, this member is present
     * only to support the mandatory feature setting and retrieval
     * logic defined by SAX.</p>
     */
    private Map features = new HashMap();

    /**
     * Public default constructor.
     */
    public DocumentReader() { }

    //----------------------------------------------------------------------
    // SAX XMLReader interface support
    //----------------------------------------------------------------------

    //----------------------------------------------------------------------
    // Configuration
    //----------------------------------------------------------------------

    /**
     * Sets the state of a feature.
     * <p>
     * The feature name is any fully-qualified URI.</p>
     * <p>
     * All XMLReaders are required to support setting
     * <code>http://xml.org/sax/features/namespaces</code> to
     * <code>true</code> and
     * <code>http://xml.org/sax/features/namespace-prefixes</code> to
     * <code>false</code>.</p>
     * <p>
     * This implementation only recognizes the above listed
     * mandatory features but ignores them.</p>
     *
     * @param  name    the feature name, which is a fully-qualified URI.
     * @param  state   the requested state of the feature (true or false).
     *
     * @throws SAXNotRecognizedException   when the XMLReader does not
     *                                     recognize the feature name.
     * @throws SAXNotSupportedException    when the XMLReader recognizes
     *                                     the feature name but cannot set
     *                                     the requested value.
     *
     * @see    #getFeature
     */
    public void setFeature(String name, boolean value)
                              throws SAXNotRecognizedException,
                                     SAXNotSupportedException {
      if ((name.equals("http://xml.org/sax/features/namespaces")) ||
         (name.equals("http://xml.org/sax/features/namespace-prefixes"))) {
        this.features.put(name, new Boolean(value));
      }
      else {
        throw new SAXNotRecognizedException(name);
      }
    }

    /**
     * Looks up the value of a feature.
     * <p>
     * All XMLReaders are required to recognize the
     * <code>http://xml.org/sax/features/namespaces</code> and the
     * <code>http://xml.org/sax/features/namespace-prefixes</code> feature
     * names.</p>
     * <p>
     * This implementation only recognizes the above listed
     * mandatory features.</p>
     *
     * @param  name   the feature name, which is a fully-qualified URI.
     *
     * @return the current state of the feature (true or false).
     *
     * @throws SAXNotRecognizedException   when the XMLReader does not
     *                                     recognize the feature name.
     * @throws SAXNotSupportedException    when the XMLReader recognizes
     *                                     the feature name but cannot
     *                                     determine its value at this time.
     *
     * @see    #setFeature
     */
    public boolean getFeature(String name) throws SAXNotRecognizedException,
                                                  SAXNotSupportedException {
      if ((name.equals("http://xml.org/sax/features/namespaces")) ||
          (name.equals("http://xml.org/sax/features/namespace-prefixes"))) {
        Boolean value = (Boolean) this.features.get(name);

        if (value == null) {
          value = Boolean.FALSE;
        }
        return value.booleanValue();
      }
      else {
        throw new SAXNotRecognizedException(name);
      }
    }

    /**
     * Sets the value of a property.
     * <p>
     * The property name is any fully-qualified URI.  It is
     * possible for an XMLReader to recognize a property name but
     * to be unable to set its value.</p>
     * <p>
     * This implementation does not recognized nor support any
     * property and thus always throws a
     * {@link SAXNotRecognizedException}.</p>
     *
     * @param name    the property name, which is a fully-qualified URI.
     * @param state   the requested value for the property.
     *
     * @throws SAXNotRecognizedException   always!
     *
     * @see    #getProperty
     */
    public void setProperty(String name, Object value)
                                throws SAXNotRecognizedException {
      throw new SAXNotRecognizedException(name);
    }

    /**
     * Looks up the value of a property.
     * <p>
     * The property name is any fully-qualified URI.  It is
     * possible for an XMLReader to recognize a property name but
     * to be unable to return its state.</p>
     * <p>
     * This implementation does not recognized nor support any
     * property and thus always throws a
     * {@link SAXNotRecognizedException}.</p>
     *
     * @param  name   the property name, which is a fully-qualified URI.
     *
     * @return the current value of the property.
     *
     * @throws SAXNotRecognizedException   always!
     *
     * @see    #getProperty
     */
    public Object getProperty(String name) throws SAXNotRecognizedException {
      throw new SAXNotRecognizedException(name);
    }

    //----------------------------------------------------------------------
    // Event handlers
    //----------------------------------------------------------------------

    /**
     * Allows an application to register a content event handler.
     *
     * @param  handler   the content handler.
     *
     * @throws NullPointerException   if the handler argument
     *                                is <code>null</code>.
     *
     * @see    #getContentHandler
     */
    public void setContentHandler(ContentHandler handler) {
      this.saxOutputter.setContentHandler(handler);
      this.contentHandler = handler;
    }

    /**
     * Returns the current content handler.
     *
     * @return the current content handler, or
     *         <code>null</code> if none has been registered.
     *
     * @see    #setContentHandler
     */
    public ContentHandler getContentHandler() {
      return this.contentHandler;
    }

    /**
     * Allows an application to register a DTD event handler.
     *
     * @param  handler   the DTD handler.
     *
     * @throws NullPointerException   if the handler argument
     *                                is <code>null</code>.
     *
     * @see    #getDTDHandler
     */
    public void setDTDHandler(DTDHandler handler) {
      if (handler == null) {
        throw new NullPointerException("handler");
      }
      this.saxOutputter.setDTDHandler(handler);
      this.dtdHandler = handler;
    }

    /**
     * Returns the current DTD handler.
     *
     * @return the current DTD handler, or
     *         <code>null</code> if none has been registered.
     *
     * @see    #setDTDHandler
     */
    public DTDHandler getDTDHandler() {
      return this.dtdHandler;
    }

    /**
     * Allows an application to register an error event handler.
     *
     * @param  handler   the error handler.
     *
     * @throws NullPointerException   if the handler argument
     *                                is <code>null</code>.
     *
     * @see    #getErrorHandler
     */
    public void setErrorHandler(ErrorHandler handler) {
      this.saxOutputter.setErrorHandler(handler);
      this.errorHandler = handler;
    }

    /**
     * Returns the current error handler.
     *
     * @return the current error handler, or
     *         <code>null</code> if none has been registered.
     *
     * @see    #setErrorHandler
     */
    public ErrorHandler getErrorHandler() {
      return errorHandler;
    }

    /**
     * Allows an application to register an entity resolver.
     *
     * @param  resolver   the entity resolver.
     *
     * @throws NullPointerException   if the resolver argument
     *                                is <code>null</code>.
     *
     * @see    #getEntityResolver
     */
    public void setEntityResolver(EntityResolver resolver) {
      if (resolver == null) {
        throw new NullPointerException("resolver");
      }
      this.saxOutputter.setEntityResolver(resolver);
      this.entityResolver = resolver;
    }

    /**
     * Returns the current entity resolver.
     *
     * @return the current entity resolver, or
     *         <code>null</code> if none has been registered.
     *
     * @see    #setEntityResolver
     */
    public EntityResolver getEntityResolver() {
      return entityResolver;
    }

    //----------------------------------------------------------------------
    // Parsing
    //----------------------------------------------------------------------

    /**
     * Parses an XML document from a system identifier (URI).
     * <p>
     * This implementation does not support reading XML data from
     * system identifiers, only from JDOM docuements.  Hence,
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
          this.saxOutputter.output(((JDOMInputSource)input).getDocument());
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

