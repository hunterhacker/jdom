/*--

 $Id: JaxenXPath.java,v 1.4 2002/04/29 02:30:48 jhunter Exp $

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

package org.jdom.xpath;


import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.List;

import org.jdom.JDOMException;

import org.saxpath.SAXPathException;
// import org.jaxen.jdom.XPath;
import org.jaxen.SimpleVariableContext;
import org.jaxen.JaxenException;


/**
 * A JDOM-oriented wrapper around XPath engines.
 *
 * @author Laurent Bihanic
 */
class JaxenXPath extends XPath {  // package protected

   /**
    * The compiled XPath object to select nodes.  This attribute can
    * not be made final as it needs to be set upon object
    * deserialization.
    */
   private transient org.jaxen.jdom.XPath xPath;

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
         return xPath.selectNodes(context);
      }
      catch (JaxenException ex1) {
         throw new JDOMException("XPath error while evaluating \"" +
                        xPath.toString() + "\": " + ex1.getMessage(), ex1);
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
         return xPath.selectSingleNode(context);
      }
      catch (JaxenException ex1) {
         throw new JDOMException("XPath error while evaluating \"" +
                        xPath.toString() + "\": " + ex1.getMessage(), ex1);
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
         return xPath.valueOf(context);
      }
      catch (JaxenException ex1) {
         throw new JDOMException("XPath error while evaluating \"" +
                        xPath.toString() + "\": " + ex1.getMessage(), ex1);
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
         return xPath.numberValueOf(context);
      }
      catch (JaxenException ex1) {
         throw new JDOMException("XPath error while evaluating \"" +
                        xPath.toString() + "\": " + ex1.getMessage(), ex1);
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
         xPath = new org.jaxen.jdom.XPath(expr);
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


   /**
    * Serializes this object on the specified object stream.
    *
    * @param  out   the object stream to write the serialized
    *               object to.
    *
    * @throws IOException   if thrown by the output stream.
    */
   private void writeObject(ObjectOutputStream out) throws IOException {
      out.writeUTF(getXPath());
   }

   /**
    * Deserializes this object from the specified object stream.
    *
    * @param  in   the object stream to read the serialized
    *              object from.
    *
    * @throws IOException   if thrown by the input stream.
    */
   private void readObject(ObjectInputStream in) throws IOException {
      try {
         this.setXPath(in.readUTF());
      }
      catch (JDOMException ex1) {
         throw new IOException(ex1.toString());
      }
   }
}

