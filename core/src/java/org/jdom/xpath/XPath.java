/*--

 $Id: XPath.java,v 1.6 2002/04/29 13:38:16 jhunter Exp $

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


import java.io.Serializable;
import java.util.List;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.jdom.JDOMException;


/**
 * A JDOM-oriented wrapper around XPath engines.
 * 
 * @author Laurent Bihanic
 */
public abstract class XPath implements Serializable {

    private static final String CVS_ID =
    "@(#) $RCSfile: XPath.java,v $ $Revision: 1.6 $ $Date: 2002/04/29 13:38:16 $ $Name:  $";

   private static Constructor jaxen = null;

   /**
    * Creates a new XPath wrapper object, compiling the specified
    * XPath expression.
    *
    * @param  expr   the XPath expression to wrap.
    *
    * @throws JDOMException   if the XPath expression is invalid.
    */
   protected XPath(String expr) throws JDOMException {
   }

   /**
    * Creates a new XPath wrapper object, compiling the specified
    * XPath expression.
    *
    * @param  path   the XPath expression to wrap.
    *
    * @throws JDOMException   if the XPath expression is invalid.
    */
   public static XPath newInstance(String path) throws JDOMException {
      if (jaxen == null) {
          try {
              Class clz = Class.forName("org.jdom.xpath.JaxenXPath");
              jaxen = clz.getConstructor(new Class[] {String.class});
          } catch (ClassNotFoundException cnfe) {
              throw new JDOMException(cnfe.toString());
          } catch (NoSuchMethodException nsme) {
              throw new JDOMException(nsme.toString());
          }
      }

      try {
          return (XPath) jaxen.newInstance(new Object[] {path});
      } catch (IllegalAccessException iae) {
          throw new JDOMException(iae.toString());
      } catch (InstantiationException ie) {
          throw new JDOMException(ie.toString());
      } catch (InvocationTargetException ite) {
          throw new JDOMException(ite.toString());
      }
   }

   /**
    * Evaluates the wrapped XPath expression and returns the list
    * of selected nodes.
    *
    * @param  context   the node to use as context for evaluating
    *                   the XPath expression.
    *
    * @return the list of selected nodes, which can be instances of
    *         the following JDOM classes: {@link org.jdom# Element},
    *         {@link org.jdom# Attribute}, {@link org.jdom# Text}, {@link org.jdom# CDATA},
    *         {@link org.jdom# Comment} or {@link org.jdom# ProcessingInstruction}.
    *
    * @throws JDOMException   if the evaluation of the XPath
    *                         expression on the specified context
    *                         failed.
    */
   abstract public List selectNodes(Object context) throws JDOMException;

   /**
    * Evaluates the wrapped XPath expression and returns the first
    * entry in the list of selected nodes.
    *
    * @param  context   the node to use as context for evaluating
    *                   the XPath expression.
    *
    * @return the first selected nodes, which is an instance of one
    *         of the following JDOM classes: {@link org.jdom# Element},
    *         {@link org.jdom# Attribute}, {@link org.jdom# Text}, {@link org.jdom# CDATA},
    *         {@link org.jdom# Comment} or {@link org.jdom# ProcessingInstruction} or
    *         <code>null</code> if no node was selected.
    *
    * @throws JDOMException   if the evaluation of the XPath
    *                         expression on the specified context
    *                         failed.
    */
   abstract public Object selectSingleNode(Object context) throws JDOMException;

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
   abstract public String valueOf(Object context) throws JDOMException;

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
    *         special value {@link org.jdom# java.lang.Double#NaN}
    *         (Not-a-Number) if the selected value can not be
    *         converted into a number value.
    *
    * @throws JDOMException   if the XPath expression is invalid or
    *                         its evaluation on the specified context
    *                         failed.
    */
   abstract public Number numberValueOf(Object context) throws JDOMException;

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
   abstract public void setVariable(String name, Object value)
                                        throws IllegalArgumentException;


   /**
    * Returns the wrapped XPath expression as a string.
    *
    * @return the wrapped XPath expression as a string.
    */
   abstract public String getXPath();


   /**
    * Evaluates an XPath expression and returns the list of selected
    * nodes.
    * <p>
    * <strong>Note</strong>: This method should not be used when the
    * same XPath expression needs to be applied several times (on the
    * same or different contexts) as it requires the expression to be
    * compiled before being evaluated.  In such cases,
    * {@link org.jdom# #newInstance allocating} an XPath wrapper instance and
    * {@link org.jdom# #selectNodes(java.lang.Object) evaluating} it several
    * times is way more efficient.
    * </p>
    *
    * @param  context   the node to use as context for evaluating
    *                   the XPath expression.
    * @param  path      the XPath expression to evaluate.
    *
    * @return the list of selected nodes, which can be instances of
    *         the following JDOM classes: {@link org.jdom# Element},
    *         {@link org.jdom# Attribute}, {@link org.jdom# Text}, {@link org.jdom# CDATA},
    *         {@link org.jdom# Comment} or {@link org.jdom# ProcessingInstruction}.
    *
    * @throws JDOMException   if the XPath expression is invalid or
    *                         its evaluation on the specified context
    *                         failed.
    */
   public static List selectNodes(Object context, String path)
                                                        throws JDOMException {
      return newInstance(path).selectNodes(context);
   }

   /**
    * Evaluates the wrapped XPath expression and returns the first
    * entry in the list of selected nodes.
    * <p>
    * <strong>Note</strong>: This method should not be used when the
    * same XPath expression needs to be applied several times (on the
    * same or different contexts) as it requires the expression to be
    * compiled before being evaluated.  In such cases,
    * {@link org.jdom# #newInstance allocating} an XPath wrapper instance and
    * {@link org.jdom# #selectSingleNode(java.lang.Object) evaluating} it
    * several times is way more efficient.
    * </p>
    *
    * @param  context   the element to use as context for evaluating
    *                   the XPath expression.
    * @param  path      the XPath expression to evaluate.
    *
    * @return the first selected nodes, which is an instance of one
    *         of the following JDOM classes: {@link org.jdom# Element},
    *         {@link org.jdom# Attribute}, {@link org.jdom# Text}, {@link org.jdom# CDATA},
    *         {@link org.jdom# Comment} or {@link org.jdom# ProcessingInstruction} or
    *         <code>null</code> if no node was selected.
    *
    * @throws JDOMException   if the XPath expression is invalid or
    *                         its evaluation on the specified context
    *                         failed.
    */
   public static Object selectSingleNode(Object context, String path)
                                                        throws JDOMException {
      return newInstance(path).selectSingleNode(context);
   }
}
