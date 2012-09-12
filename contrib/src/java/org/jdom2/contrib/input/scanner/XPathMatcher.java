/*--

 Copyright (C) 2001-2004 Jason Hunter & Brett McLaughlin.
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


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.internal.SystemProperty;

import org.xml.sax.Attributes;


/**
 * The base class for all XPath matchers used by
 * {@link ElementScanner}.
 * <p>
 * This class also plays the role of factory for concrete
 * implementations: The system property
 * "<code>org.jdom2.XPathMatcher.class</code>" shall contain the
 * fully-qualified name of a concrete subclass of XPatchMatcher with
 * a public {@link #XPathMatcher two argument constructor}.  If this
 * property is not defined, the default concrete implementation
 * (supporting only node matching patterns) will be used.</p>
 * <p>
 * As the default implementation relies on Perl5-like regular
 * expression to match nodes, any regular expression can be used as
 * "<i>XPath expression</i>" with the restriction that any '*'
 * character be escaped (i&#46;e&#46; preceded with a '\' character).</p>
 *
 * @author Laurent Bihanic
 */
public abstract class XPathMatcher {

   /**
    * The name of the system property from which to retrieve the
    * name of the implementation class to use.
    * <p>
    * The property name is:
    * "<code>org.jdom2.XPathMatcher.class</code>".</p>
    */
   private final static String  IMPLEMENTATION_CLASS_PROPERTY   =
                                        "org.jdom2.XPathMatcher.class";

   /**
    * The default implementation class to use if none was configured.
    */
   private final static String  DEFAULT_IMPLEMENTATION_CLASS    =
                "org.jdom2.contrib.input.scanner.JakartaRegExpXPathMatcher";

   /**
    * The constructor to instanciate a new XPathMatcher concrete
    * implementation.
    *
    * @see    #newXPathMatcher
    */
   private static Constructor<?> constructor = null;

   /**
    * Whether debug traces are active.
    */
   private static boolean debug = false;

   /**
    * The XPath expression this matcher matches!
    */
   private final String expression;

   /**
    * The element listener.
    */
   private final        ElementListener listener;

   /**
    * Default constructor, protected on purpose.
    *
    * @param  expression   the XPath-like expression to match.
    * @param  listener     the element listener to notify when an
    *                      element matches the expression.
    *
    * @throws JDOMException  if one of the arguments is invalid.
    */
   protected XPathMatcher(String expression, ElementListener listener)
                                                        throws JDOMException {
      if ((expression == null) || (expression.length() == 0)) {
         throw (new JDOMException(
                        "Invalid XPath expression: \"" + expression + "\""));
      }
      if (listener == null) {
         throw (new JDOMException("Invalid ElementListener: <null>"));
      }
      this.expression = expression;
      this.listener   = listener;
   }

   /**
    * Returns the XPath expression this matcher matches.
    *
    * @return the XPath expression this matcher matches.
    */
   public String getExpression() {
      return (this.expression);
   }

   /**
    * Returns the element listener associated to this matcher object.
    *
    * @return the element listener.
    */
   public ElementListener getListener() {
      return (this.listener);
   }

   /**
    * Tries to match an element path and attributes with the XPath
    * expression this matcher matches.
    * <p>
    * This method is invoked when processing the
    * <code>startElement</code> SAX event.</p>
    * <p>
    * <strong>Note</strong>: The default implementation ignores the
    * attributes.</p>
    *
    * @param  path    the path to the element.
    * @param  attrs   the SAX attributes of the element.
    *
    * @return <code>true</code> is the element matches the XPath
    *         expression, <code>false</code> otherwise.
    */
   abstract public boolean match(String path, Attributes attrs);

   /**
    * Tries to match an element with the XPath expression this
    * matcher matches.
    * <p>
    * This method is invoked when processing the
    * <code>endElement</code> SAX event.  It allows matching on
    * data not part of the <code>startElement</code> event such
    * as the presence of child elements.</p>
    * <p>
    * <strong>Note</strong>: The default implementation always
    * returns <code>true</code> as it only supports filtering on
    * the node path.</p>
    *
    * @param  path   the path to the element.
    * @param  elt    the JDOM element.
    *
    * @return <code>true</code> is the element matches the XPath
    *         expression, <code>false</code> otherwise.
    */
   abstract public boolean match(String path, Element elt);

   /**
    * Extracts the node matching pattern part from an XPath
    * expression and converts it into a Perl5-like regular
    * expression.
    *
    * @param  expr   an XPath expression.
    *
    * @return the RE to match the element path.
    *
    * @throws JDOMException  if <code>expression</code> is invalid.
    */
   protected static String getPathPatternAsRE(String expr)
                                                        throws JDOMException {
      if ((expr == null) || (expr.length() == 0)) {
         expr = "/*";
      }

      // It the expression ends with a square backet, a test part is
      // present. => Strip it!
      // Note: Any other sub-expression between square backet is view as
      //       a RE alphabet definition and proccessed. OK, that's not
      //       XPath compliant but that's very convenient!
      String path = (expr.endsWith("]"))?
                     expr.substring(0, expr.lastIndexOf('[')): expr;

      int length = path.length();
      StringBuilder re = new StringBuilder(2 * length);

      char previous = (char)0;
      for (int i=0; i<length; i++) {
         char current = path.charAt(i);

         if (i == 0) {
            re.append((current == '/')? '^': '/');
         }
         if (current == '*') {
            if (previous == '\\') {
               // Escaped wildcard. => Remove previous '\' and insert '*'.
               re.setLength(re.length() - 1);
            }
            else {
               // Regular XPath wildcard.
               // => "*" -> ".[^/]*", i.e. all chars but the separator '/'
               re.append(".[^/]");
            }
            re.append('*');
         }
         else {
            if ((current == '/') && (previous == '/')) {
               // "//" -> "/.*/" or "/" !!!
               // => Remove previous '/'
               re.setLength(re.length() - 1);
 
               // And insert RE.
               re.append("(/.*/|/)");
            }
            else {
               re.append(current);
            }
         }
         previous = current;
      }
      re.append('$');

      return (re.toString());
   }

   /**
    * Extracts the test part from an XPath expression.  The test
    * part if the part of the XPath expression between square
    * backets.
    *
    * @param  expr   an XPath expression.
    *
    * @return the test part of the expression of <code>null</code>
    *         if no test was specified.
    *
    * @throws JDOMException  if <code>expression</code> is invalid.
    */
   protected static String getTestPattern(String expr) throws JDOMException {
      if (expr.endsWith("]")) {
         return (expr.substring(expr.lastIndexOf('[')));
      }
      return (null);
   }


   //-------------------------------------------------------------------------
   // XPathMatcher Factory methods
   //-------------------------------------------------------------------------

   /**
    * Activates or deactivates debug traces on the process standard
    * output.  Debug traces allow to trace the mapping between the
    * XPath-like expressions provided when registering a listener
    * and the regular expressions and/or XPath expressions actually
    * used to filter elements.
    *
    * @param  value   whether to activate debug traces.
    */
   public static void setDebug(boolean value) {
      debug = value;
   }

   /**
    * Returns whether debug traces are active.
    *
    * @return <code>true</code> if debug traces are active;
    *         <code>false</code> otherwise.
    */
   public static boolean isDebug() {
      return (debug);
   }

   /**
    * Sets the concrete XPathMatcher subclass to be used when
    * allocating XPathMatcher instances.
    *
    * @param  aClass   the concrete subclass of XPathMatcher.
    *
    * @throws IllegalArgumentException   if <code>aClass</code> is
    *                                    <code>null</code>.
    * @throws JDOMException              if <code>aClass</code> is
    *                                    not a concrete subclass
    *                                    of XPathMatcher.
    */
   public static void setXPathMatcherClass(Class<?> aClass)
                        throws  IllegalArgumentException, JDOMException {
      if (aClass != null) {
         try {
            if ((XPathMatcher.class.isAssignableFrom(aClass)) &&
                (Modifier.isAbstract(aClass.getModifiers()) == false)) {
               // Concrete subclass pf XPathMatcher.
               // => Get the constructor to use.
               constructor = aClass.getConstructor(String.class, ElementListener.class);
            }
            else {
               throw (new JDOMException(
                        aClass.getName() + " is not a concrete XPathMatcher"));
            }
         }
         catch (Exception ex1) {
            // Any reflection error (probably due to a configuration mistake).
            throw (new JDOMException(ex1.toString(), ex1));
         }
      }
      else {
         throw (new IllegalArgumentException("aClass"));
      }
   }

   /**
    * Creates a new XPath matcher matching the specified XPath
    * expression.
    *
    * @param  expression   the XPath-like expression to match.
    * @param  listener     the element listener to notify when an
    *                      element matches the expression.
    * @return a matcher.
    *
    * @throws JDOMException  if <code>expression</code> is invalid.
    */
   public static final XPathMatcher newXPathMatcher(
                                String expression, ElementListener listener)
                                                        throws JDOMException {
      try {
         if (constructor == null) {
            // First call.
            // => Load configuration to determine implementation.
            String className = SystemProperty.get(
                                        IMPLEMENTATION_CLASS_PROPERTY,
                                        DEFAULT_IMPLEMENTATION_CLASS);

            setXPathMatcherClass(Class.forName(className));
         }
         return (XPathMatcher)constructor.newInstance(expression, listener);
      }
      catch (InvocationTargetException ex1) {
         // Constructor threw an error on invocation.
         Throwable te = ex1.getTargetException();

         throw ((te instanceof JDOMException)? (JDOMException)te:
                                        new JDOMException(te.toString(), te));
      }
      catch (Exception ex3) {
         // Any reflection error (probably due to a configuration mistake).
         throw (new JDOMException(ex3.toString(), ex3));
      }
   }
}

