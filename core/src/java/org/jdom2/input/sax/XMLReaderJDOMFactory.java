package org.jdom2.input.sax;

import org.xml.sax.XMLReader;

import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 * This interface can be used to supply custom XMLReaders to {@link SAXBuilder}.
 * <p>
 * See the {@link org.jdom2.input.sax package documentation} for details on what
 * XMLReaderJDOMFactory implementations are available and when they are
 * recommended.
 * 
 * @see org.jdom2.input.sax
 * 
 * @author Rolf Lear
 *
 */
public interface XMLReaderJDOMFactory {
	/**
	 * Return a new XMLReader according to the implementation of this
	 * JDOMXMLReaderFactory instance. The XMLReader is expected to be a new
	 * instance that is unrelated to any other XMLReaders, and can be reused at
	 * will by {@link SAXBuilder}.
	 * @return a new XMLReader
	 * @throws JDOMException if an XMLReader was not available.
	 */
	public XMLReader createXMLReader() throws JDOMException;
	
	/**
	 * Does an XMLReader from this factory do more than just well-formed checks.
	 * @return true if the XMLReader validates
	 */
	public boolean isValidating();
}
