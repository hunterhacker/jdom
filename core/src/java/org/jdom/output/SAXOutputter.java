/*-- 

 $Id: SAXOutputter.java,v 1.8 2001/06/10 22:01:04 jhunter Exp $

 Copyright (C) 2000 Brett McLaughlin & Jason Hunter.
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

package org.jdom.output;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;
import org.xml.sax.Locator;
import org.xml.sax.helpers.LocatorImpl;
import org.xml.sax.helpers.AttributesImpl;

import org.jdom.Document;
import org.jdom.DocType;
import org.jdom.JDOMException;
import org.jdom.ProcessingInstruction;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.Attribute;
import org.jdom.CDATA;

/**
 * <p>
 * <code>SAXOutputter</code> takes a JDOM tree and fires SAX2 events.
 * </p>
 *
 * Most <code>ContentHandler</code> callbacks are supported. Both
 * <code>ignorableWhitespace</code> and <code>skippedEntity</code> have
 * not been implemented. The <code>setDocumentLocator</code> callback has
 * been implemented, but the locator object always returns -1 for
 * <code>getColumnNumber</code> and <code>getLineNumber</code>.
 * </p>
 *
 * The <code>EntityResolver</code> callback <code>resolveEntity</code> has
 * been implemented for DTDs.
 * </p>
 *
 * At this time, it is not possible to access notations and unparsed entity
 * references in a DTD from a JDOM tree. Therefore, <code>DTDHandler</code>
 * callbacks have not been implemented yet.
 * </p>
 *
 * The <code>ErrorHandler</code> callbacks have not been implemented, since
 * these are supposed to be invoked when the document is parsed. However, the
 * document has already been parsed in order to create the JDOM tree.
 * </p>
 *
 * @author Brett McLaughlin
 * @author Jason Hunter
 * @author Fred Trimble
 * @version 1.0
 */
public class SAXOutputter {
   
    private static final String CVS_ID = 
      "@(#) $RCSfile: SAXOutputter.java,v $ $Revision: 1.8 $ $Date: 2001/06/10 22:01:04 $ $Name:  $";

   /** registered <code>ContentHandler</code> */
   private ContentHandler contentHandler;
   
   /** registered <code>ErrorHandler</code> */
   private ErrorHandler errorHandler;
   
   /** registered <code>DTDHandler</code> */
   private DTDHandler dtdHandler;
   
   /** registered <code>EntityResolver</code> */
   private EntityResolver entityResolver;
   
   /**
    * Whether to report attribute namespace declarations as xmlns attributes.
    * Defaults to <code>false</code> as per SAX specifications.
    *
    * @see <a href="http://www.megginson.com/SAX/Java/namespaces.html">SAX namespace specifications</a>
    */
   private boolean declareNamespaces = false;
   
   /**
    * <p>
    * This will create a <code>SAXOutputter</code> with the
    * specified <code>ContentHandler</code>.
    * </p>
    *
    * @param contentHandler contains <code>ContentHandler</code> callback methods
    */
   public SAXOutputter(ContentHandler contentHandler) {
      this.contentHandler = contentHandler;
   }
   
   /**
    * <p>
    * This will create a <code>SAXOutputter</code> with the
    * specified SAX2 handlers. At this time, only <code>ContentHandler</code>
    * and <code>EntityResolver</code> are supported.
    * </p>
    *
    * @param contentHandler contains <code>ContentHandler</code> callback methods
    * @param errorHandler contains <code>ErrorHandler</code> callback methods
    * @param dtdHandler contains <code>DTDHandler</code> callback methods
    * @param entityResolver contains <code>EntityResolver</code> callback methods
    */
   public SAXOutputter(ContentHandler contentHandler,
                       ErrorHandler   errorHandler,
                       DTDHandler     dtdHandler,
                       EntityResolver entityResolver) {
      this.contentHandler = contentHandler;
      this.errorHandler = errorHandler;
      this.dtdHandler = dtdHandler;
      this.entityResolver = entityResolver;
   }
   
   /**
    * <p>
    * This will set the <code>ContentHandler</code>.
    * </p>
    *
    * @param contentHandler contains <code>ContentHandler</code> callback methods.
    */
   public void setContentHandler(ContentHandler contentHandler) {
      this.contentHandler = contentHandler;
   }
   
   /**
    * <p>
    * This will set the <code>ErrorHandler</code>.
    * </p>
    *
    * @param errorHandler contains <code>ErrorHandler</code> callback methods.
    */
   public void setErrorHandler(ErrorHandler errorHandler) {
      this.errorHandler = errorHandler;
   }
   
   /**
    * <p>
    * This will set the <code>DTDHandler</code>.
    * </p>
    *
    * @param dtdHandler contains <code>DTDHandler</code> callback methods.
    */
   public void setDTDHandler(DTDHandler dtdHandler) {
      this.dtdHandler = dtdHandler;
   }
   
   /**
    * <p>
    * This will set the <code>EntityResolver</code>.
    * </p>
    *
    * @param entityResolver contains EntityResolver callback methods.
    */
   public void setEntityResolver(EntityResolver entityResolver) {
      this.entityResolver = entityResolver;
   }
   
   /**
    * <p>
    * This will define whether attribute namespace declarations shall be
    * reported as "xmlns" attributes.  This flag defaults to <code>false</code>
    * and behaves as the "namespace-prefixes" SAX core feature.
    * </p>
    *
    * @param reportDecl whether attribute namespace declarations shall be
    * reported as "xmlns" attributes.
    */
   public void setReportNamespaceDeclarations(boolean declareNamespaces) {
      this.declareNamespaces = declareNamespaces;
   }

   /**
    * <p>
    * This will output the <code>JDOM Document</code>, firing off the
    * SAX events that have been registered.
    * </p>
    *
    * @param document <code>JDOM Document</code> to output.
    */
   public void output(Document document) throws JDOMException {
      if (document == null) {
         return;
      }
       
      // contentHandler.setDocumentLocator()
      documentLocator(document);
       
      // contentHandler.startDocument()
      startDocument();
       
      // entityResolver.resolveEntity()
      entityResolver(document);
       
      // JDOM cannot read unparsed entities and notations in DTD (yet)
      // dtdHandler(document);
       
      // Handle root element, as well as any root level
      // processing instructions and CDATA sections
      Iterator i = document.getMixedContent().iterator();
      while (i.hasNext()) {
         Object obj = i.next();
         if (obj instanceof Element) {
            // process root element and its mixed content
            element(document.getRootElement(), new NamespaceStack());
         }
         else if (obj instanceof ProcessingInstruction) {
            // contentHandler.processingInstruction()
            processingInstruction((ProcessingInstruction) obj);
         }
         else if (obj instanceof CDATA) {
            // contentHandler.characters()
            characters(((CDATA) obj).getText());
         }
      }
       
       // contentHandler.endDocument()
       endDocument();
    }
   
   /**
    * <p>
    * This enables an application to resolve external entities.
    * </p>
    *
    * @param document JDOM <code>Document</code>.
    */
   private void entityResolver(Document document) throws JDOMException {
      if (entityResolver != null) {
         DocType docType = document.getDocType();
         String publicID = null;
         String systemID = null;
         if (docType != null) {
            publicID = docType.getPublicID();
            systemID = docType.getSystemID();
         }
         
         if ((publicID != null) || (systemID != null)) {
            try {
               entityResolver.resolveEntity(publicID, systemID);
            }
            catch (SAXException se) {
               throw new JDOMException("SAXException", se);
            }
            catch (IOException ioe) {
               throw new JDOMException("IOException", ioe);
            }
         }
      }
   }
   
   // Not implemented yet, since a JDOM Document does not
   // have access to the unparsed entities and notations
   // defined in a DTD (yet)
   // private void dtdHandler(Document document) {
   // }
   
   /**
    * <p>
    * This method tells you the line of the XML file being parsed.
    * For an in-memory document, it's meaningless. The location
    * is only valid for the current parsing lifecycle, but
    * the document has already been parsed. Therefore, it returns
    * -1 for both line and column numbers.
    * </p>
    *
    * @param document JDOM <code>Document</code>.
    */
   private void documentLocator(Document document) {
      LocatorImpl locator = new LocatorImpl();
      String publicID = null;
      String systemID = null;
      DocType docType = document.getDocType();
      if (docType != null) {
         publicID = docType.getPublicID();
         systemID = docType.getSystemID();
      }
      
      locator.setPublicId(publicID);
      locator.setSystemId(systemID);
      locator.setLineNumber(-1);
      locator.setColumnNumber(-1);
      
      contentHandler.setDocumentLocator((Locator) locator);
   }
   
   /**
    * <p>
    * This method is always the second method of all callbacks in
    * all handlers to be invoked (setDocumentLocator is always first).
    * </p>
    */
   private void startDocument() throws JDOMException {
      try {
         contentHandler.startDocument();
      }
      catch (SAXException se) {
         throw new JDOMException("SAXException", se);
      }
   }
   
   /**
    * <p>
    * Always the last method of all callbacks in all handlers
    * to be invoked.
    * </p>
    */
   private void endDocument() throws JDOMException {
      try {
         contentHandler.endDocument();
      }
      catch (SAXException se) {
         throw new JDOMException("SAXException", se);
      }
   }
   
   /**
    * <p>
    * This will invoke the <code>ContentHandler.processingInstruction</code>
    * callback when a processing instruction is encountered.
    * </p>
    *
    * @param pi <code>ProcessingInstruction</code> containing target and data.
    */
   private void processingInstruction(ProcessingInstruction pi) throws JDOMException {
      if (pi != null) {
         String target = pi.getTarget();
         String data = pi.getData();
         try {
            contentHandler.processingInstruction(target, data);
         }
         catch (SAXException se) {
            throw new JDOMException("SAXException", se);
         }
      }
   }
   
   /**
    * <p>
    * This will recursively invoke all of the callbacks for a particular element.
    * </p>
    *
    * @param element <code>Element</code> used in callbacks.
    * @param namespaces <code>List</code> stack of Namespaces in scope.
    */
   private void element(Element element, NamespaceStack namespaces) throws JDOMException {
      // used to check endPrefixMapping
      int previouslyDeclaredNamespaces = namespaces.size();
      
      // contentHandler.startPrefixMapping()
      startPrefixMapping(element, namespaces);
      
      // contentHandler.startElement()
      startElement(element);
      
      // handle mixed content in the element
      elementContent(element, namespaces);
      
      // contentHandler.endElement()
      endElement(element);
      
      // contentHandler.endPrefixMapping()
      endPrefixMapping(namespaces, previouslyDeclaredNamespaces);
   }
   
   /**
    * <p>
    * This will invoke the <code>ContentHandler.startPrefixMapping</code> callback
    * when a new namespace is encountered in the <code>Document</code>.
    * </p>
    *
    * @param element <code>Element</code> used in callbacks.
    * @param namespaces <code>List</code> stack of Namespaces in scope.
    */
   private void startPrefixMapping(Element element, NamespaceStack namespaces) throws JDOMException {
      Namespace ns = element.getNamespace();
      if (ns != Namespace.NO_NAMESPACE && ns != Namespace.XML_NAMESPACE) {
         String prefix = ns.getPrefix();
         String uri = namespaces.getURI(prefix);
         if (!ns.getURI().equals(uri)) {
            namespaces.push(ns);
            try {
               contentHandler.startPrefixMapping(prefix, ns.getURI());
            }
            catch (SAXException se) {
               throw new JDOMException("SAXException", se);
            }
         }
      }

      // Fire additional namespace declarations
      List additionalNamespaces = element.getAdditionalNamespaces();
      if (additionalNamespaces != null) {
         Iterator itr = additionalNamespaces.iterator();
         while (itr.hasNext()) {
            ns = (Namespace)itr.next();
            String prefix = ns.getPrefix();
            String uri = namespaces.getURI(prefix);
            if (!ns.getURI().equals(uri)) {
               namespaces.push(ns);
               try {
                  contentHandler.startPrefixMapping(prefix, ns.getURI());
               }
               catch (SAXException se) {
                  throw new JDOMException("SAXException", se);
               }
            }
         }
      }
   }

   /**
    * <p>
    * This will invoke the <code>endPrefixMapping</code> callback in the
    * <code>ContentHandler</code> when a namespace is goes out of scope
    * in the <code>Document</code>.
    * </p>
    *
    * @param namespaces <code>List</code> stack of Namespaces in scope.
    * @param previouslyDeclaredNamespaces number of previously declared namespaces
    */
   private void endPrefixMapping(NamespaceStack namespaces, int previouslyDeclaredNamespaces) throws JDOMException {
      while (namespaces.size() > previouslyDeclaredNamespaces) {
         String prefix = namespaces.pop();
         try {
            contentHandler.endPrefixMapping(prefix);
         }
         catch (SAXException se) {
            throw new JDOMException("SAXException", se);
         }
      }
   }
   
   /**
    * <p>
    * This will invoke the <code>startElement</code> callback
    * in the <code>ContentHandler</code>.
    * </p>
    *
    * @param element <code>Element</code> used in callbacks.
    */
   private void startElement(Element element) throws JDOMException {
      String namespaceURI = element.getNamespaceURI();
      String localName = element.getName();
      String rawName = element.getQualifiedName();
      AttributesImpl atts = new AttributesImpl();

      if (this.declareNamespaces) {
         // Report attribute namespaces as xmlns attributes
         List namespaces = element.getAdditionalNamespaces();
         Iterator j = namespaces.iterator();
         while (j.hasNext()) {
            Namespace ns = (Namespace) j.next();
            atts.addAttribute("",                          // namespace
                              "",                          // local name
                              "xmlns:" + ns.getPrefix(),   // qualified name
                              "CDATA",                     // type
                              ns.getURI());                // value
         }
      }

      List attributes = element.getAttributes();
      Iterator i = attributes.iterator();
      while (i.hasNext()) {
         Attribute a = (Attribute) i.next();
         atts.addAttribute(a.getNamespaceURI(),
                           a.getName(),
                           a.getQualifiedName(),
                           "CDATA",
                           a.getValue());
      }
         
      try {
         contentHandler.startElement(namespaceURI, localName, rawName, atts);
      }
      catch (SAXException se) {
         throw new JDOMException("SAXException", se);
      }
   }
   
   /**
    * <p>
    * This will invoke the <code>endElement</code> callback
    * in the <code>ContentHandler</code>.
    * </p>
    *
    * @param element <code>Element</code> used in callbacks.
    */
   private void endElement(Element element) throws JDOMException {
      String namespaceURI = element.getNamespaceURI();
      String localName = element.getName();
      String rawName = element.getQualifiedName();
      
      try {
         contentHandler.endElement(namespaceURI, localName, rawName);
      }
      catch (SAXException se) {
         throw new JDOMException("SAXException", se);
      }
   }
   
   /**
    * <p>
    * This will invoke the callbacks for the content of an element.
    * </p>
    *
    * @param element <code>Element</code> used in callbacks.
    * @param namespaces <code>List</code> stack of Namespaces in scope.
    */
   private void elementContent(Element element, NamespaceStack namespaces) throws JDOMException {
      List mixedContent = element.getMixedContent();
      
      boolean empty = mixedContent.size() == 0;
      boolean stringOnly =
         !empty &&
         mixedContent.size() == 1 &&
         mixedContent.get(0) instanceof String;
         
      if (stringOnly) {
         // contentHandler.characters()
         characters(element.getText());
      }
      else {
         Object content = null;
         for (int i = 0, size = mixedContent.size(); i < size; i++) {
            content = mixedContent.get(i);
            if (content instanceof Element) {
               element((Element) content, namespaces);
            }
            else if (content instanceof String) {
               // contentHandler.characters()
               characters((String) content);
            }
            else if (content instanceof CDATA) {
               // contentHandler.characters()
               characters(((CDATA) content).getText());
            }
            else if (content instanceof ProcessingInstruction) {
               // contentHandler.processingInstruction()
               processingInstruction((ProcessingInstruction) content);
            }
         }
      }
   }
   
   /**
    * <p>
    * This will be called for each chunk of character data encountered.
    * </p>
    *
    * @param elementText all text in an element, including whitespace.
    */
   private void characters(String elementText) throws JDOMException {
      char[] c = elementText.toCharArray();
      try {
         contentHandler.characters(c, 0, c.length);
      }
      catch (SAXException se) {
         throw new JDOMException("SAXException", se);
      }
   }
}
