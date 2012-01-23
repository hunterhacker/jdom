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


import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import org.xml.sax.Attributes;


/* package */ class JakartaRegExpXPathMatcher extends XPathMatcher {

   /**
    * The compiled regular expression this matcher matches.
    */
   private final Pattern re;

   private final XPathExpression<Object> test;

   /**
    * Creates a new XPath matcher using Jakarta RegExp regular
    * expression matcher implementation.
    *
    * @param  expression   the XPath-like expression to match.
    * @param  listener     the element listener to notify when an
    *                      element matches the expression.
    *
    * @throws JDOMException  if one of the arguments is invalid.
    */
   public JakartaRegExpXPathMatcher(
                                String expression, ElementListener listener)
                                                        throws JDOMException {
      super(expression, listener);

      try {
         String pathPattern = getPathPatternAsRE(expression);

         this.re = Pattern.compile(pathPattern);

         String testPattern = getTestPattern(expression);
         if (testPattern != null) {
            testPattern = "." + testPattern;

            this.test = XPathFactory.instance().compile(testPattern);
         }
         else {
            this.test = null;
         }

         if (isDebug()) {
            System.out.println("Listener " + listener + ":");
            System.out.println("   " + expression +
                                        " -> RE    = " + pathPattern);
            System.out.println("   " + expression +
                                        " -> XPath = " + testPattern);
         }
      }
      catch (PatternSyntaxException ex1) {
         throw (new JDOMException(
                        "Illegal XPath expression: " + expression, ex1));
      }
   }

   /**
    * Tries to match an element path and attributes with the XPath
    * expression this matcher matches.
    * <p>
    * This method is invoked when processing the
    * <code>startElement</code> SAX event.</p>
    * <p>
    * This implementation only matches the element path, ignoring
    * the attributes.</p>
    *
    * @param  path    the path to the element.
    * @param  attrs   the SAX attributes of the element.
    *
    * @return <code>true</code> is the element matches the XPath
    *         expression, <code>false</code> otherwise.
    */
   @Override
public boolean match(String path, Attributes attrs) {
      return (this.re.matcher(path).matches());
   }

   /**
    * Tries to match an element with the XPath expression this
    * matcher matches.
    * <p>
    * This method is invoked when processing the
    * <code>endElement</code> SAX event.  It allows matching on
    * data not part of the <code>startElement</code> event such
    * as the presence of child elements.</p>
    * <p>
    * This implementation always return <code>true</code> as it
    * does not support matching on anything but the path.</p>
    *
    * @param  path   the path to the element.
    * @param  elt    the JDOM element.
    *
    * @return <code>true</code> is the element matches the XPath
    *         expression, <code>false</code> otherwise.
    */
   @Override
public boolean match(String path, Element elt) {
      if (this.test != null) {
         return !this.test.evaluate(elt).isEmpty();
      }
      return (true);
   }
}

