/*--

 $Id: JaxenXPath.java,v 1.9 2003/02/26 23:55:55 jhunter Exp $

 Copyright (C) 2000 Jason Hunter & Brett McLaughlin.
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

package org.jdom.xpath;


import java.util.List;
import java.util.Map;
import java.util.Hashtable;

import org.jdom.*;

import org.saxpath.SAXPathException;
import org.jaxen.jdom.JDOMXPath;
import org.jaxen.SimpleVariableContext;
import org.jaxen.SimpleNamespaceContext;
import org.jaxen.JaxenException;


/**
 * A JDOM-oriented wrapper around XPath engines.
 *
 * @author Laurent Bihanic
 * @version $Revision: 1.9 $, $Date: 2003/02/26 23:55:55 $
 */
class JaxenXPath extends    XPath {             // package protected

    private static final String CVS_ID =
    "@(#) $RCSfile: JaxenXPath.java,v $ $Revision: 1.9 $ $Date: 2003/02/26 23:55:55 $ $Name:  $";

   /**
    * The compiled XPath object to select nodes.  This attribute can
    * not be made final as it needs to be set upon object
    * deserialization.
    */
   private transient JDOMXPath xPath;

   /**
    * The current context for XPath expression evaluation.
    */
   private           Object    currentContext;

   /**
    * Creates a new XPath wrapper object, compiling the specified
    * XPath expression.
    *
    * @param  expr   the XPath expression to wrap.
    *
    * @throws JDOMException   if the XPath expression is invalid.
    */
   public JaxenXPath(String expr) throws JDOMException {
      super(expr);
      setXPath(expr);
   }

   /**
    * Evaluates the wrapped XPath expression and returns the list
    * of selected nodes.
    *
    * @param  context   the node to use as context for evaluating
    *                   the XPath expression.
    *
    * @return the list of selected nodes, which can be instances of
    *         the following JDOM classes: {@link Element},
    *         {@link Attribute}, {@link Text}, {@link CDATA},
    *         {@link Comment} or {@link ProcessingInstruction}.
    *
    * @throws JDOMException   if the evaluation of the XPath
    *                         expression on the specified context
    *                         failed.
    */
   public List selectNodes(Object context) throws JDOMException {
      try {
         currentContext = context;

         return xPath.selectNodes(context);
      }
      catch (JaxenException ex1) {
         throw new JDOMException("XPath error while evaluating \"" +
                        xPath.toString() + "\": " + ex1.getMessage(), ex1);
      }
      finally {
         currentContext = null;
      }
   }

   /**
    * Evaluates the wrapped XPath expression and returns the first
    * entry in the list of selected nodes.
    *
    * @param  context   the node to use as context for evaluating
    *                   the XPath expression.
    *
    * @return the first selected nodes, which is an instance of one
    *         of the following JDOM classes: {@link Element},
    *         {@link Attribute}, {@link Text}, {@link CDATA},
    *         {@link Comment} or {@link ProcessingInstruction} or
    *         <code>null</code> if no node was selected.
    *
    * @throws JDOMException   if the evaluation of the XPath
    *                         expression on the specified context
    *                         failed.
    */
   public Object selectSingleNode(Object context) throws JDOMException {
      try {
         currentContext = context;

         return xPath.selectSingleNode(context);
      }
      catch (JaxenException ex1) {
         throw new JDOMException("XPath error while evaluating \"" +
                        xPath.toString() + "\": " + ex1.getMessage(), ex1);
      }
      finally {
         currentContext = null;
      }
   }

   /**
    * Returns the string value of the first node selected by applying
    * the wrapped XPath expression to the given context.
    *
    * @param  context   the element to use as context for evaluating
    *                   the XPath expression.
    *
    * @return the string value of the first node selected by applying
    *         the wrapped XPath expression to the given context.
    *
    * @throws JDOMException   if the XPath expression is invalid or
    *                         its evaluation on the specified context
    *                         failed.
    */
   public String valueOf(Object context) throws JDOMException {
      try {
         currentContext = context;

         return xPath.valueOf(context);
      }
      catch (JaxenException ex1) {
         throw new JDOMException("XPath error while evaluating \"" +
                        xPath.toString() + "\": " + ex1.getMessage(), ex1);
      }
      finally {
         currentContext = null;
      }
   }

   /**
    * Returns the number value of the first node selected by applying
    * the wrapped XPath expression to the given context.
    *
    * @param  context   the element to use as context for evaluating
    *                   the XPath expression.
    *
    * @return the number value of the first node selected by applying
    *         the wrapped XPath expression to the given context,
    *         <code>null</code> if no node was selected or the
    *         special value {@link java.lang.Double#NaN}
    *         (Not-a-Number) if the selected value can not be
    *         converted into a number value.
    *
    * @throws JDOMException   if the XPath expression is invalid or
    *                         its evaluation on the specified context
    *                         failed.
    */
   public Number numberValueOf(Object context) throws JDOMException {
      try {
         currentContext = context;

         return xPath.numberValueOf(context);
      }
      catch (JaxenException ex1) {
         throw new JDOMException("XPath error while evaluating \"" +
                        xPath.toString() + "\": " + ex1.getMessage(), ex1);
      }
      finally {
         currentContext = null;
      }
   }

   /**
    * Defines an XPath variable and sets its value.
    *
    * @param  name    the variable name.
    * @param  value   the variable value.
    *
    * @throws IllegalArgumentException   if <code>name</code> is not
    *                                    a valid XPath variable name
    *                                    or if the value type is not
    *                                    supported by the underlying
    *                                    implementation
    */
   public void setVariable(String name, Object value)
                                        throws IllegalArgumentException {
      Object o = xPath.getVariableContext();
      if (o instanceof SimpleVariableContext) {
           ((SimpleVariableContext)o).setVariableValue(null, name, value);
      }
   }

   /**
    * Adds a namespace definition to the list of namespaces known of
    * this XPath expression.
    * <p>
    * <strong>Note</strong>: In XPath, there is no such thing as a
    * 'default namespace'.  The empty prefix <b>always</b> resolves
    * to the empty namespace URI.</p>
    *
    * @param  namespace   the namespace.
    */
   public void addNamespace(Namespace namespace) {
      try {
         xPath.addNamespace(namespace.getPrefix(), namespace.getURI());
      }
      catch (JaxenException ex1) { /* Can't happen here. */ }
   }

   /**
    * Returns the wrapped XPath expression as a string.
    *
    * @return the wrapped XPath expression as a string.
    */
   public String getXPath() {
      return (xPath.toString());
   }

   /**
    * Compiles and sets the XPath expression wrapped by this object.
    *
    * @param  expr   the XPath expression to wrap.
    *
    * @throws JDOMException   if the XPath expression is invalid.
    */
   private void setXPath(String expr) throws JDOMException {
      try {
         xPath = new JDOMXPath(expr);
         xPath.setNamespaceContext(new NSContext());
      }
      catch (SAXPathException ex1) {
         throw new JDOMException(
                        "Invalid XPath expression: \"" + expr + "\"", ex1);
      }
   }

   public String toString() {
      return (xPath.toString());
   }

   public boolean equals(Object o) {
      if (o instanceof JaxenXPath) {
         JaxenXPath x = (JaxenXPath)o;

         return (super.equals(o) &&
                 xPath.toString().equals(x.xPath.toString()));
      }
      return false;
   }

   public int hashCode() {
      return xPath.hashCode();
   }

   private class NSContext extends SimpleNamespaceContext {
      public NSContext() {
         super();
      }

      /**
       * <i>[Jaxen NamespaceContext interface support]</i> Translates
       * the provided namespace prefix into the matching bound
       * namespace URI.
       *
       * @param  prefix   the namespace prefix to resolve.
       *
       * @return the namespace URI matching the prefix.
       */
      public String translateNamespacePrefixToUri(String prefix) {
         if ((prefix == null) || (prefix.length() == 0)) {
            return null;
         }

         String uri = super.translateNamespacePrefixToUri(prefix);
         if (uri == null) {
            Object ctx = currentContext;
            if (ctx != null) {
               Element elt = null;

               // Get closer element node
               if (ctx instanceof Element) {
                  elt = (Element)ctx;
               } else if (ctx instanceof Attribute) {
                  elt = ((Attribute)ctx).getParent();
               } else if (ctx instanceof Text) {
                  elt = ((Text)ctx).getParent();
               } else if (ctx instanceof ProcessingInstruction) {
                  elt = ((ProcessingInstruction)ctx).getParent();
               } else if (ctx instanceof Comment) {
                  elt = ((Comment)ctx).getParent();
               } else if (ctx instanceof EntityRef) {
                  elt = ((EntityRef)ctx).getParent();
               } else if (ctx instanceof Document) {
                  elt = ((Document)ctx).getRootElement();
               }

               if (elt != null) {
                  Namespace ns = elt.getNamespace(prefix);
                  if (ns != null) {
                     uri = ns.getURI();
                  }
               }
            }
         }
         return (uri);
      }
   }
}

