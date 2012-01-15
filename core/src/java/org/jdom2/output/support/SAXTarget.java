/*-- 

 Copyright (C) 2011 - 2012 Jason Hunter & Brett McLaughlin.
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

package org.jdom2.output.support;

import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

import org.jdom2.output.JDOMLocator;

/**
 * The target for all SAX notifications in this OuputProcessor
 * 
 * @author Rolf Lear
 */
public final class SAXTarget {
	
	/**
	 * A locator specific to the SAXOutputter process.
	 * @author Rolf Lear
	 *
	 */
	public static final class SAXLocator implements JDOMLocator {
		
		private final String publicid, systemid;
		private Object node = null ;
		
		/**
		 * Creates a SAXLocator which implements JDOMLocator
		 * @param publicid This Locator's SystemID
		 * @param systemid This Locator's PublicID
		 */
		public SAXLocator(String publicid, String systemid) {
			super();
			this.publicid = publicid;
			this.systemid = systemid;
		}

		@Override
		public int getColumnNumber() {
			return -1;
		}

		@Override
		public int getLineNumber() {
			return -1;
		}

		@Override
		public String getPublicId() {
			return publicid;
		}

		@Override
		public String getSystemId() {
			return systemid;
		}

		@Override
		public Object getNode() {
			return node;
		}

		/**
		 * Set the location on this SAXLocator
		 * @param node The location to set.
		 */
		public void setNode(Object node) {
			this.node = node;
		}
		
	}
	
	/** registered <code>ContentHandler</code> */
	private final ContentHandler contentHandler;

	/** registered <code>ErrorHandler</code> */
	private final ErrorHandler errorHandler;

	/** registered <code>DTDHandler</code> */
	private final DTDHandler dtdHandler;

	/** registered <code>EntityResolver</code> */
	private final EntityResolver entityResolver;

	/** registered <code>LexicalHandler</code> */
	private final LexicalHandler lexicalHandler;

	/** registered <code>DeclHandler</code> */
	private final DeclHandler declHandler;

	private final SAXLocator locator;

	/**
	 * Whether to report attribute namespace declarations as xmlns
	 * attributes. Defaults to <code>false</code> as per SAX specifications.
	 * 
	 * @see <a href="http://www.megginson.com/SAX/Java/namespaces.html"> SAX
	 *      namespace specifications</a>
	 */
	private final boolean declareNamespaces;

	/**
	 * Whether to report DTD events to DeclHandlers and LexicalHandlers.
	 * Defaults to <code>true</code>.
	 */
	private final boolean reportDtdEvents;

	/**
	 * Create the collection of handlers for a SAXOutputProcessor
	 * 
	 * @param contentHandler
	 *        The ContentHandler
	 * @param errorHandler
	 *        The ErrorHandler
	 * @param dtdHandler
	 *        The DTDHandler
	 * @param entityResolver
	 *        The EntityResolver
	 * @param lexicalHandler
	 *        The LexicalHandler
	 * @param declHandler
	 *        The DeclHandler
	 * @param declareNamespaces
	 *        Whether to declare Namespaces
	 * @param reportDtdEvents
	 *        Whether to report DTD Events
	 * @param publicID
	 *        The public ID (null if none)
	 * @param systemID
	 *        The System ID (null if none)
	 */
	public SAXTarget(ContentHandler contentHandler,
			ErrorHandler errorHandler, DTDHandler dtdHandler,
			EntityResolver entityResolver, LexicalHandler lexicalHandler,
			DeclHandler declHandler, boolean declareNamespaces,
			boolean reportDtdEvents, String publicID, String systemID) {
		super();
		this.contentHandler = contentHandler;
		this.errorHandler = errorHandler;
		this.dtdHandler = dtdHandler;
		this.entityResolver = entityResolver;
		this.lexicalHandler = lexicalHandler;
		this.declHandler = declHandler;
		this.declareNamespaces = declareNamespaces;
		this.reportDtdEvents = reportDtdEvents;
		this.locator = new SAXLocator(publicID, systemID);

	}

	/**
	 * @return The target ContentHandler
	 */
	public ContentHandler getContentHandler() {
		return contentHandler;
	}

	/**
	 * @return The target ErrorHandler
	 */
	public ErrorHandler getErrorHandler() {
		return errorHandler;
	}

	/**
	 * @return The target DTDHandler
	 */
	public DTDHandler getDTDHandler() {
		return dtdHandler;
	}

	/**
	 * @return The target EntityResolver
	 */
	public EntityResolver getEntityResolver() {
		return entityResolver;
	}

	/**
	 * @return The target LexicalHandler
	 */
	public LexicalHandler getLexicalHandler() {
		return lexicalHandler;
	}

	/**
	 * @return The target DeclHandler
	 */
	public DeclHandler getDeclHandler() {
		return declHandler;
	}

	/**
	 * @return Whether to declare Namespaces
	 */
	public boolean isDeclareNamespaces() {
		return declareNamespaces;
	}

	/**
	 * @return Whether to report DTD Events
	 */
	public boolean isReportDTDEvents() {
		return reportDtdEvents;
	}

	/**
	 * @return the Locator used for this Output
	 */
	public SAXLocator getLocator() {
		return locator;
	}

}