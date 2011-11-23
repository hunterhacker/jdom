package org.jdom2.input.sax;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.JDOMFactory;

/**
 * SAXEngine provides an interface to interact with either the SAXBuilder or
 * the SAXBuilderEngine. This makes it possible to do pooling of SAXEngines for
 * parsing using instances of either SAXBuilder or SAXBuilderEngine. 
 * 
 * @see org.jdom2.input.sax
 * 
 * @author Rolf Lear
 *
 */
public interface SAXEngine {

	/**
	 * Returns the current {@link org.jdom2.JDOMFactory} in use.
	 * @return the factory in use
	 */
	public abstract JDOMFactory getJDOMFactory();

	/**
	 * Returns whether validation is to be performed during the build.
	 *
	 * @return whether validation is to be performed during the build
	 */
	public abstract boolean isValidating();

	/**
	 * Returns the {@link ErrorHandler} assigned, or null if none.
	 * @return the ErrorHandler assigned, or null if none
	 */
	public abstract ErrorHandler getErrorHandler();

	/**
	 * Returns the {@link EntityResolver} assigned, or null if none.
	 *
	 * @return the EntityResolver assigned
	 */
	public abstract EntityResolver getEntityResolver();

	/**
	 * Returns the {@link DTDHandler} assigned, or null if none.
	 *
	 * @return the DTDHandler assigned
	 */
	public abstract DTDHandler getDTDHandler();

	/**
	 * Returns whether element content whitespace is to be ignored during the
	 * build.
	 *
	 * @return whether element content whitespace is to be ignored during the
	 * build
	 */
	public abstract boolean isIgnoringElementContentWhitespace();

	/**
	 * Returns whether or not the parser will elminate element content
	 * containing only whitespace.
	 *
	 * @return <code>boolean</code> - whether only whitespace content will
	 * be ignored during build.
	 *
	 */
	public abstract boolean isIgnoringBoundaryWhitespace();

	/**
	 * Returns whether or not entities are being expanded into normal text
	 * content.
	 *
	 * @return whether entities are being expanded
	 */
	public abstract boolean isExpandEntities();

	/**
	 * This builds a document from the supplied
	 * input source.
	 *
	 * @param in <code>InputSource</code> to read from
	 * @return <code>Document</code> resultant Document object
	 * @throws JDOMException when errors occur in parsing
	 * @throws IOException when an I/O error prevents a document
	 *         from being fully parsed
	 */
	public abstract Document build(InputSource in)
			throws JDOMException, IOException;

	/**
	 * <p>
	 * This builds a document from the supplied
	 *   input stream.
	 * </p>
	 *
	 * @param in <code>InputStream</code> to read from
	 * @return <code>Document</code> resultant Document object
	 * @throws JDOMException when errors occur in parsing
	 * @throws IOException when an I/O error prevents a document
	 *         from being fully parsed.
	 */
	public abstract Document build(InputStream in) throws JDOMException, IOException;

	/**
	 * <p>
	 * This builds a document from the supplied
	 *   filename.
	 * </p>
	 *
	 * @param file <code>File</code> to read from
	 * @return <code>Document</code> resultant Document object
	 * @throws JDOMException when errors occur in parsing
	 * @throws IOException when an I/O error prevents a document
	 *         from being fully parsed
	 */
	public abstract Document build(final File file) throws JDOMException, IOException;

	/**
	 * <p>
	 * This builds a document from the supplied
	 *   URL.
	 * </p>
	 *
	 * @param url <code>URL</code> to read from.
	 * @return <code>Document</code> - resultant Document object.
	 * @throws JDOMException when errors occur in parsing
	 * @throws IOException when an I/O error prevents a document
	 *         from being fully parsed.
	 */
	public abstract Document build(final URL url) throws JDOMException, IOException;

	/**
	 * <p>
	 * This builds a document from the supplied
	 *   input stream.
	 * </p>
	 *
	 * @param in <code>InputStream</code> to read from.
	 * @param systemId base for resolving relative URIs
	 * @return <code>Document</code> resultant Document object
	 * @throws JDOMException when errors occur in parsing
	 * @throws IOException when an I/O error prevents a document
	 *         from being fully parsed
	 */
	public abstract Document build(final InputStream in, final String systemId)
			throws JDOMException, IOException;

	/**
	 * <p>
	 * This builds a document from the supplied
	 *   Reader.  It's the programmer's responsibility to make sure
	 *   the reader matches the encoding of the file.  It's often easier
	 *   and safer to use an InputStream rather than a Reader, and to let the
	 *   parser auto-detect the encoding from the XML declaration.
	 * </p>
	 *
	 * @param characterStream <code>Reader</code> to read from
	 * @return <code>Document</code> resultant Document object
	 * @throws JDOMException when errors occur in parsing
	 * @throws IOException when an I/O error prevents a document
	 *         from being fully parsed
	 */
	public abstract Document build(final Reader characterStream)
			throws JDOMException, IOException;

	/**
	 * <p>
	 * This builds a document from the supplied
	 *   Reader.  It's the programmer's responsibility to make sure
	 *   the reader matches the encoding of the file.  It's often easier
	 *   and safer to use an InputStream rather than a Reader, and to let the
	 *   parser auto-detect the encoding from the XML declaration.
	 * </p>
	 *
	 * @param characterStream <code>Reader</code> to read from.
	 * @param systemId base for resolving relative URIs
	 * @return <code>Document</code> resultant Document object
	 * @throws JDOMException when errors occur in parsing
	 * @throws IOException when an I/O error prevents a document
	 *         from being fully parsed
	 */
	public abstract Document build(final Reader characterStream, final String systemId)
			throws JDOMException, IOException;

	/**
	 * <p>
	 * This builds a document from the supplied
	 *   URI.
	 * </p>
	 * @param systemId URI for the input
	 * @return <code>Document</code> resultant Document object
	 * @throws JDOMException when errors occur in parsing
	 * @throws IOException when an I/O error prevents a document
	 *         from being fully parsed
	 */
	public abstract Document build(final String systemId)
			throws JDOMException, IOException;

}