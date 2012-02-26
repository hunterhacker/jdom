/*--

 Copyright (C) 2000-2004 Jason Hunter & Brett McLaughlin.
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

package org.jdom2.contrib.input.scanner;

import java.io.IOException;
import java.util.*;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.helpers.XMLFilterImpl;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;
import org.jdom2.input.sax.XMLReaderJDOMFactory;
import org.jdom2.input.sax.SAXHandler;
import org.jdom2.input.sax.SAXHandlerFactory;

/**
 * An XML filter that uses XPath-like expressions to select the
 * element nodes to build and notifies listeners when these
 * elements becomes available during the parse.
 * <p>
 * ElementScanner does not aim at providing a faster parsing of XML
 * documents.  Its primary focus is to allow the application to
 * control the parse and to consume the XML data while they are
 * being parsed.  ElementScanner can be viewed as a high-level SAX
 * parser that fires events conveying JDOM {@link Element elements}
 * rather that XML tags and character data.</p>
 * <p>
 * ElementScanner only notifies of the parsing of element nodes and
 * does not support reporting the parsing of DOCTYPE data, processing
 * instructions or comments except for those present within the
 * selected elements.  Application needing such data shall register
 * a specific {@link ContentHandler} of this filter to receive them
 * in the form of raw SAX events.</p>
 * <p>
 * To be notified of the parsing of JDOM Elements, an application
 * shall {@link #addElementListener register} objects implementing
 * the {@link ElementListener} interface.  For each registration,
 * an XPath-like expression defines the elements to be parsed and
 * reported.</p>
 * <p>
 * Opposite to XPath, there is no concept of <i>current context</i>
 * or <i>current node</i> in ElementScanner.  And thus, the syntax
 * of the "XPath-like expressions" is not as strict as in XPath and
 * closer to what one uses in XSLT stylesheets in the
 * <code>match</code> specification of the XSL templates:<br>
 * In ElementScanner, the expression "<code>x</code>" matches any
 * element named &quot;x&quot; at any level of the document and not
 * only the root element (as expected in strict XPath if the
 * document is considered the <i>current context</i>).  Thus, in
 * ElementScanner, "<code>x</code>" is equivalent to
 * "<code>//x</code>".</p>
 * <p>
 * Example:
 *  <blockquote><pre>
 *  ElementScanner f = new ElementScanner();
 *
 *  // All descendants of x named y
 *  f.addElementListener(new MyImpl(), "x//y");
 *  // All grandchilden of y named t
 *  f.addElementListener(new MyImpl(), "y/*&thinsp;/t");
 *
 *  ElementListener l2 = new MyImpl2();
 *  f.addElementListener(l2, "/*");     // Root element
 *  f.addElementListener(l2, "z");      // Any node named z
 *
 *  ElementListener l3 = new MyImpl3();
 *  // Any node having an attribute "name" whose value contains ".1"
 *  f.addElementListener(l3, "*[contains(@name,'.1')]");
 *  // Any node named y having at least one "y" descendant
 *  f.addElementListener(l3, "y[.//y]");
 *
 *  f.parse(new InputSource("test.xml"));
 *  </pre></blockquote>
 * </p>
 * <p>
 * The XPath interpreter can be changed (see {@link XPathMatcher}).
 * The default implementation is a mix of the
 * <a href="http://jakarta.apache.org/regexp/index.html">Jakarta
 * RegExp package</a> and the
 * <a href="http://www.jaxen.org">Jaxen XPath interpreter</a>.</p>
 * <p>
 * ElementScanner splits XPath expressions in 2 parts: a node
 * selection pattern and an optional test expression (the part of
 * the XPath between square backets that follow the node selection
 * pattern).</p>
 * <p>
 * Regular expressions are used to match nodes applying the node
 * selection pattern. This allows matching node without requiring to
 * build them (as Jaxen does).<br>
 * If a test expression appears in an XPath expression, Jaxen is used
 * to match the built elements against it and filter out those not
 * matching the test.</p>
 * <p>
 * As a consequence of using regular expressions, the  <i>or</i>"
 * operator ("<code>|</code>" in XPath) is not supported in node
 * selection patterns but can be achieved by registering the same
 * listener several times with different node patterns.</p>
 * <p>
 * <strong>Note</strong>: The methods marked with
 * "<i>[ContentHandler interface support]</i>" below shall not be
 * invoked by the application.  Their usage is reserved to
 * the XML parser.</p>
 *
 * @author Laurent Bihanic
 */
@SuppressWarnings("javadoc")
public class ElementScanner extends XMLFilterImpl {

	/**
	 * The registered element listeners, each wrapped in a
	 * XPathMatcher instance.
	 */
	private final Collection<XPathMatcher> listeners = new ArrayList<XPathMatcher>();

	/**
	 * The <i>SAXBuilder</i> instance to build the JDOM objects used
	 * for parsing the input XML documents.  We actually do not need
	 * SAXBuilder per se, we just want to reuse the tons of Java code
	 * this class implements!
	 */
	private SAXBuilder parserBuilder = new SAXBuilder();

	/**
	 * The <i>SAXHandler</i> instance to build the JDOM Elements.
	 */
	private SAXHandler saxHandler  = null;

	/**
	 * The path of the being parsed element.
	 */
	private              StringBuilder    currentPath     = new StringBuilder();

	/**
	 * The matching rules active for the current path.  It includes
	 * the matching rules active for all the ancestors of the
	 * current node.
	 */
	private Map<String,Collection<XPathMatcher>> activeRules = new HashMap<String, Collection<XPathMatcher>>();

	/**
	 * Construct an ElementScanner, with no parent.
	 * <p>
	 * If no parent has been assigned when {@link #parse} is invoked,
	 * ElementScanner will use JAXP to get an instance of the default
	 * SAX parser installed.</p>
	 */
	public ElementScanner() {
		super();
	}

	/**
	 * Constructs an ElementScanner with the specified parent.
	 */
	public ElementScanner(XMLReader parent) {
		super(parent);
	}

	//-------------------------------------------------------------------------
	// Specific implementation
	//-------------------------------------------------------------------------

	/**
	 * Adds a new element listener to the list of listeners
	 * maintained by this filter.
	 * <p>
	 * The same listener can be registered several times using
	 * different patterns and several listeners can be registered
	 * using the same pattern.</p>
	 *
	 * @param  listener   the element listener to add.
	 * @param  pattern    the XPath expression to select the elements
	 *                    the listener is interested in.
	 *
	 * @throws JDOMException   if <code>listener</code> is null or
	 *                         the expression is invalid.
	 */
	public void addElementListener(ElementListener listener, String pattern)
			throws JDOMException {
		if (listener != null) {
			this.listeners.add(XPathMatcher.newXPathMatcher(pattern, listener));
		}
		else {
			throw (new JDOMException("Invalid listener object: <null>"));
		}
	}

	/**
	 * Removes element listeners from the list of listeners maintained
	 * by this filter.
	 * <p>
	 * if <code>pattern</code> is <code>null</code>, this method
	 * removes all registrations of <code>listener</code>, regardless
	 * the pattern(s) used for creating the registrations.</p>
	 * <p>
	 * if <code>listener</code> is <code>null</code>, this method
	 * removes all listeners registered for <code>pattern</code>.</p>
	 * <p>
	 * if both <code>listener</code> and <code>pattern</code> are
	 * <code>null</code>, this method performs no action!</p>
	 *
	 * @param  listener   the element listener to remove.
	 */
	public void removeElementListener(ElementListener listener, String pattern) {
		if ((listener != null) || (pattern != null)) {
			for (Iterator<XPathMatcher> i=this.listeners.iterator(); i.hasNext(); ) {
				XPathMatcher m = i.next();

				if (((m.getListener().equals(listener))  || (listener == null)) &&
						((m.getExpression().equals(pattern)) || (pattern  == null))) {
					i.remove();
				}
			}
		}
		// Else: Both null => Just ignore that dummy call!
	}

	/**
	 * Returns the list of rules that match the element path and
	 * attributes.
	 *
	 * @param  path    the current element path.
	 * @param  attrs   the attributes of the element.
	 *
	 * @return the list of matching rules or <code>null</code> if
	 *         no match was found.
	 */
	private Collection<XPathMatcher> getMatchingRules(String path, Attributes attrs) {
		Collection<XPathMatcher> matchingRules = null;

		for (XPathMatcher rule : this.listeners) {
			if (rule.match(path, attrs)) {
				if (matchingRules == null) {
					matchingRules = new ArrayList<XPathMatcher>();
				}
				matchingRules.add(rule);
			}
		}
		return (matchingRules);
	}

	//-------------------------------------------------------------------------
	// SAXBuilder / SAXHandler configuration helper methods
	//-------------------------------------------------------------------------

	/**
	 * Sets a custom JDOMFactory for the builder.  Use this to build
	 * the tree with your own subclasses of the JDOM classes.
	 *
	 * @param  factory   <code>JDOMFactory</code> to use.
	 */
	public void setFactory(JDOMFactory factory) {
		this.parserBuilder.setJDOMFactory(factory);
	}

	/**
	 * Activates or deactivates validation for the builder.
	 *
	 * @param  validate   whether XML validation should occur.
	 */
	public void setValidation(boolean validate) {
		if (validate)
			this.parserBuilder.setXMLReaderFactory(XMLReaders.DTDVALIDATING);
		else
			this.parserBuilder.setXMLReaderFactory(XMLReaders.NONVALIDATING);
	}

	/**
	 * Specifies whether or not the parser should elminate whitespace
	 * in  element content (sometimes known as "ignorable whitespace")
	 * when building the document.  Only whitespace which is contained
	 * within element content that has an element only content model
	 * will be eliminated (see XML Rec 3.2.1).  For this setting to
	 * take effect requires that validation be turned on.
	 * <p>
	 * The default value is <code>false</code>.</p>
	 *
	 * @param  ignoringWhite   whether to ignore ignorable whitespace.
	 */
	public void setIgnoringElementContentWhitespace(boolean ignoringWhite) {
		this.parserBuilder.setIgnoringElementContentWhitespace(ignoringWhite);
	}

	/**
	 * Sets whether or not to expand entities for the builder.
	 * <p>
	 * A value <code>true</code> means to expand entities as normal
	 * content; <code>false</code> means to leave entities unexpanded
	 * as <code>EntityRef</code> objects.</p>
	 * <p>
	 * The default value is <code>true</code>.</p>
	 *
	 * @param  expand   whether entity expansion should occur.
	 */
	public void setExpandEntities(boolean expand) {
		this.parserBuilder.setExpandEntities(expand);
	}

	//-------------------------------------------------------------------------
	// XMLFilterImpl overwritten methods
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// XMLReader interface support
	//-------------------------------------------------------------------------

	/**
	 * Sets the state of a feature.
	 *
	 * @param  name     the feature name, which is a fully-qualified
	 *                  URI.
	 * @param  state    the requested state of the feature.
	 *
	 * @throws SAXNotRecognizedException   when the XMLReader does not
	 *         recognize the feature name.
	 * @throws SAXNotSupportedException    when the XMLReader
	 *         recognizes the feature name but cannot set the
	 *         requested value.
	 */
	@Override
	public void setFeature(String name, boolean state)
			throws SAXNotRecognizedException, SAXNotSupportedException {
		if (this.getParent() != null) {
			this.getParent().setFeature(name, state);
		}
		this.parserBuilder.setFeature(name, state);
	}

	/**
	 * Set the value of a property.
	 *
	 * @param  name   the property name, which is a fully-qualified
	 *                URI.
	 * @param value   the requested value for the property.
	 *
	 * @throws SAXNotRecognizedException   when the XMLReader does not
	 *         recognize the property name.
	 * @throws SAXNotSupportedException    when the XMLReader
	 *         recognizes the property name but cannot set the
	 *         requested value.
	 */
	@Override
	public void setProperty(String name, Object value)
			throws SAXNotRecognizedException, SAXNotSupportedException {
		if (this.getParent() != null) {
			this.getParent().setProperty(name, value);
		}
		this.parserBuilder.setProperty(name, value);
	}

	/**
	 * Parses an XML document.
	 * <p>
	 * The application can use this method to instruct ElementScanner
	 * to begin parsing an XML document from any valid input source
	 * (a character stream, a byte stream, or a URI).</p>
	 * <p>
	 * Applications may not invoke this method while a parse is in
	 * progress.  Once a parse is complete, an application may reuse
	 * the same ElementScanner object, possibly with a different input
	 * source.</p>
	 * <p>
	 * This method is synchronous: it will not return until parsing
	 * has ended.  If a client application wants to terminate parsing
	 * early, it should throw an exception.</p>
	 *
	 * @param  source   the input source for the XML document.
	 *
	 * @throws SAXException   any SAX exception, possibly wrapping
	 *                        another exception.
	 * @throws IOException    an IO exception from the parser,
	 *                        possibly from a byte stream or character
	 *                        stream supplied by the application.
	 */
	@Override
	public void parse(InputSource source)  throws IOException, SAXException {
		final SAXHandler shandler = new FragmentHandler(parserBuilder.getJDOMFactory());
		SAXHandlerFactory shfactory = new SAXHandlerFactory() {
			@Override
			public SAXHandler createSAXHandler(JDOMFactory fac) {
				// ignore the fac.
				return shandler;
			}
		};
		final XMLReaderJDOMFactory currentfac = parserBuilder.getXMLReaderFactory();
		final SAXHandlerFactory currentshfac = parserBuilder.getSAXHandlerFactory();
		try {
			final XMLReader xreader = getParent() != null 
					? getParent() 
					: parserBuilder.getXMLReaderFactory().createXMLReader();
			final boolean validating = currentfac.isValidating();
			parserBuilder.setSAXHandlerFactory(shfactory);
			parserBuilder.setXMLReaderFactory(new XMLReaderJDOMFactory() {
				@Override
				public boolean isValidating() {
					return validating;
				}
				
				@Override
				public XMLReader createXMLReader() throws JDOMException {
					return xreader;
				}
			});
			// configures the sax handler and parser to mate.
			parserBuilder.buildEngine();
			// Allocate the element builder (SAXHandler subclass).
			this.saxHandler = shandler;

			// Allocate (if not provided) and configure the parent parser.
			if (this.getParent() != null) {
				setParent(xreader);
			}

		} catch (JDOMException e) {
			throw new SAXException("Problem in JDOM.", e);
		} finally {
			parserBuilder.setXMLReaderFactory(currentfac);
			parserBuilder.setSAXHandlerFactory(currentshfac);
		}
		// And delegate to superclass now that everything has been set-up.
		// Note: super.parse() forces the registration of this filter as
		//       ContentHandler, ErrorHandler, DTDHandler and EntityResolver.
		super.parse(source);
	}

	//-------------------------------------------------------------------------
	// ContentHandler interface support
	//-------------------------------------------------------------------------

	/**
	 * <i>[ContentHandler interface support]</i> Receives notification
	 * of the beginning of a document.
	 *
	 * @throws SAXException   any SAX exception, possibly wrapping
	 *                        another exception.
	 */
	@Override
	public void startDocument()          throws SAXException {
		// Reset state.
		this.currentPath.setLength(0);
		this.activeRules.clear();

		// Propagate event.
		this.saxHandler.startDocument();
		super.startDocument();
	}

	/**
	 * <i>[ContentHandler interface support]</i> Receives notification
	 * of the end of a document.
	 *
	 * @throws SAXException   any SAX exception, possibly wrapping
	 *                        another exception.
	 */
	@Override
	public void endDocument()            throws SAXException {
		// Propagate event.
		this.saxHandler.endDocument();
		super.endDocument();
	}

	/**
	 * <i>[ContentHandler interface support]</i> Begins the scope of
	 * a prefix-URI Namespace mapping.
	 *
	 * @param  prefix   the Namespace prefix being declared.
	 * @param  uri      the Namespace URI the prefix is mapped to.
	 *
	 * @throws SAXException   any SAX exception, possibly wrapping
	 *                        another exception.
	 */
	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		// Propagate event.
		this.saxHandler.startPrefixMapping(prefix, uri);
		super.startPrefixMapping(prefix, uri);
	}

	/**
	 * <i>[ContentHandler interface support]</i> Ends the scope of a
	 * prefix-URI Namespace mapping.
	 *
	 * @param  prefix   the prefix that was being mapped.
	 *
	 * @throws SAXException   any SAX exception, possibly wrapping
	 *                        another exception.
	 */
	@Override
	public void endPrefixMapping(String prefix)          throws SAXException {
		// Propagate event.
		this.saxHandler.endPrefixMapping(prefix);
		super.endPrefixMapping(prefix);
	}

	/**
	 * <i>[ContentHandler interface support]</i> Receives notification
	 * of the beginning of an element.
	 *
	 * @param  nsUri       the Namespace URI, or the empty string if
	 *                     the element has no Namespace URI or if
	 *                     Namespace processing is not being performed.
	 * @param  localName   the local name (without prefix), or the
	 *                     empty string if Namespace processing is
	 *                     not being performed.
	 * @param  qName       the qualified name (with prefix), or the
	 *                     empty string if qualified names are not
	 *                     available.
	 * @param  attrs       the attributes attached to the element. If
	 *                     there are no attributes, it shall be an
	 *                     empty Attributes object.
	 *
	 * @throws SAXException   any SAX exception, possibly wrapping
	 *                        another exception.
	 */
	@Override
	public void startElement(String nsUri, String localName,
			String qName, Attributes attrs)
					throws SAXException {
		// Append new element to the current path.
		this.currentPath.append('/').append(localName);

		// Retrieve the matching rules for this element.
		String eltPath           = this.currentPath.substring(0);
		Collection<XPathMatcher> matchingRules = this.getMatchingRules(eltPath, attrs);
		if (matchingRules != null) {
			// Matching rules found.
			// => Make them active to trigger element building.
			this.activeRules.put(eltPath, matchingRules);
		}

		// Propagate event.
		if (this.activeRules.size() != 0) {
			this.saxHandler.startElement(nsUri, localName, qName, attrs);
		}
		super.startElement(nsUri, localName, qName, attrs);
	}

	/**
	 * <i>[ContentHandler interface support]</i> Receives notification
	 * of the end of an element.
	 *
	 * @param  nsUri       the Namespace URI, or the empty string if
	 *                     the element has no Namespace URI or if
	 *                     Namespace processing is not being performed.
	 * @param  localName   the local name (without prefix), or the
	 *                     empty string if Namespace processing is
	 *                     not being performed.
	 * @param  qName       the qualified name (with prefix), or the
	 *                     empty string if qualified names are not
	 *                     available.
	 *
	 * @throws SAXException   any SAX exception, possibly wrapping
	 *                        another exception.
	 */
	@Override
	public void endElement(String nsUri, String localName, String qName)
			throws SAXException {
		// Grab the being-built element.
		Element elt = this.saxHandler.getCurrentElement();

		// Complete element building before making use of it.
		// (This sets the current element to the parent of elt.)
		if (this.activeRules.size() != 0) {
			this.saxHandler.endElement(nsUri, localName, qName);
		}

		// Get the matching rules for this element (if any).
		String eltPath = this.currentPath.substring(0);
		Collection<XPathMatcher> matchingRules = this.activeRules.remove(eltPath);
		if (matchingRules != null) {
			// Matching rules found.
			// => Detach the current element if no rules remain active.
			if (this.activeRules.size() == 0) {
				elt.detach();
			}

			// And notify all matching listeners.
			try {
				for (XPathMatcher matcher : matchingRules) {
					if (matcher.match(eltPath, elt)) {
						matcher.getListener().elementMatched(eltPath, elt);
					}
				}
			}
			catch (JDOMException ex1) {
				// Oops! Listener-originated exception.
				// => Fire a SAXException to abort parsing.
				throw (new SAXException(ex1.getMessage(), ex1));
			}
		}
		// Remove notified element from the current path.
		this.currentPath.setLength(
				this.currentPath.length() - (localName.length() + 1));
		// Propagate event.
		super.endElement(nsUri, localName, qName);
	}

	/**
	 * <i>[ContentHandler interface support]</i> Receives notification
	 * of character data.
	 *
	 * @param  ch       the characters from the XML document.
	 * @param  start    the start position in the array.
	 * @param  length   the number of characters to read from the array.
	 *
	 * @throws SAXException   any SAX exception, possibly wrapping
	 *                        another exception.
	 */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// Propagate event.
		if (this.activeRules.size() != 0) {
			this.saxHandler.characters(ch, start, length);
		}
		super.characters(ch, start, length);
	}

	/**
	 * <i>[ContentHandler interface support]</i> Receives notification
	 * of ignorable whitespace in element content.
	 *
	 * @param  ch       the characters from the XML document.
	 * @param  start    the start position in the array.
	 * @param  length   the number of characters to read from the array.
	 *
	 * @throws SAXException   any SAX exception, possibly wrapping
	 *                        another exception.
	 */
	@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		// Propagate event.
		if (this.activeRules.size() != 0) {
			this.saxHandler.ignorableWhitespace(ch, start, length);
		}
		super.ignorableWhitespace(ch, start, length);
	}

	/**
	 * <i>[ContentHandler interface support]</i> Receives notification
	 * of processing instruction.
	 *
	 * @param  target   the processing instruction target.
	 * @param  data     the processing instruction data, or
	 *                  <code>null</code> if none was supplied.
	 *
	 * @throws SAXException   any SAX exception, possibly wrapping
	 *                        another exception.
	 */
	@Override
	public void processingInstruction(String target, String data)
			throws SAXException {
		// Propagate event.
		if (this.activeRules.size() != 0) {
			this.saxHandler.processingInstruction(target, data);
		}
		super.processingInstruction(target, data);
	}

	/**
	 * <i>[ContentHandler interface support]</i> Receives notification
	 * of a skipped entity.
	 *
	 * @param  name   the name of the skipped entity.
	 *
	 * @throws SAXException   any SAX exception, possibly wrapping
	 *                        another exception.
	 */
	@Override
	public void skippedEntity(String name)               throws SAXException {
		// Propagate event.
		if (this.activeRules.size() != 0) {
			this.saxHandler.skippedEntity(name);
		}
		super.skippedEntity(name);
	}


	//=========================================================================
	// Deviant implementations of JDOM builder objects
	//=========================================================================

	//-------------------------------------------------------------------------
	// ParserBuilder nested class
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// FragmentHandler nested class
	//-------------------------------------------------------------------------

	/**
	 * FragmentHandler extends SAXHandler to support matching nodes
	 * without a common ancestor. This class inserts a dummy root
	 * element in the being-built document. This prevents the document
	 * to have, from SAXHandler's point of view, multiple root
	 * elements (which would cause the parse to fail).
	 */
	private static class FragmentHandler extends SAXHandler {
		/**
		 * Public constructor.
		 */
		public FragmentHandler(JDOMFactory factory) {
			super(factory);

			// Add a dummy root element to the being-built document.
			this.pushElement(new Element("root", null, null));
		}
	}

}

