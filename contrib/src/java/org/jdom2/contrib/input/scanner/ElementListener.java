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

import org.jdom2.Element;
import org.jdom2.JDOMException;

/**
 * The interface objects listening for element creation notification
 * fired by {@link ElementScanner} shall implement.
 *
 * @author Laurent Bihanic
 */
public interface ElementListener {

   /**
    * Notifies of the parsing of an Element.
    * <p>
    * <code>ElementScanner</code> invokes this method when
    * encountering the closing tag of the element definition.  Thus,
    * element <code>e</code> and all its child nodes are fully built
    * but the parent element is not.</p>
    * <p>
    * Note that the element is not attached to any
    * {@link Element#getDocument document} and that the
    * {@link Element#getParent() parent element} is <code>null</code>
    * unless another listener is listening on one of the element
    * ancestors.</p>
    * <p>
    * As no copy of the notified elements is performed, all changes
    * made on element <code>e</code> will be visible of the
    * not-yet-notified listeners listening on this same element and
    * of the listeners listening on one of the element ancestors.</p>
    *
    * @param  path   the path to the parsed element.
    * @param  e      the parsed <code>Element</code>.
    *
    * @throws JDOMException   if the listener wishes to abort the
    *                         parsing of the input XML document.
    */
   abstract public void elementMatched(String path, Element e)
                                                        throws JDOMException;
}

