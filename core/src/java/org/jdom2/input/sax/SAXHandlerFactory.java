package org.jdom2.input.sax;

import org.jdom2.JDOMFactory;

/**
 * Provides SAXBuilder with SAXHandler instances to support SAX parsing.
 * Users wanting to customise the way that JDOM processes the SAX events
 * should override the {@link SAXHandler} class, and then use an implementation
 * of this factory to feed instances of that new class to SAXBuilder.
 * 
 * @see SAXHandler
 * @see org.jdom2.input.sax
 * 
 * 
 * @author Rolf Lear
 *
 */
public interface SAXHandlerFactory {
	/**
	 * Create a new SAXHandler instance for SAXBuilder to use. 
	 * @param factory The {@link JDOMFactory} to use for creating JDOM content.
	 * @return a new instance of a SAXHandler (or subclass) class.
	 */
	public SAXHandler createSAXHandler(JDOMFactory factory);
}
