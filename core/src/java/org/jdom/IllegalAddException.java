/*-- 

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

package org.jdom;

/**
 * <p><code>IllegalAddException</code>
 *   is thrown when an <code>{@link Element}</code> or
 *   <code>{@link Attribute}</code> is added to a JDOM
 *   construct illegally..
 * </p>
 *
 * @author Brett McLaughlin
 * @version 1.0
 */
public class IllegalAddException extends IllegalArgumentException {

    /**
     * <p>
     * This will create an <code>Exception</code> indicating
     *   that the addition of the <code>{@link Attribute}</code>
     *   supplied to the <code>{@link Element}</code> supplied
     *   is illegal.
     * </p>
     *
     * @param base <code>Element</code> that <code>Attribute</code>
     *        couldn't be added to
     * @param added <code>Attribute</code> that could not be added
     * @param reason cause for the problem
     */
    public IllegalAddException(Element base, Attribute added, String reason) {
        super(new StringBuffer()
              .append("The attribute \"")
              .append(added.getQualifiedName())
              .append("\" could not be added to the element \"")
              .append(base.getQualifiedName())
              .append("\": ")
              .append(reason)
              .toString());
    }

    /**
     * <p>
     * This will create an <code>Exception</code> indicating
     *   that the addition of the <code>{@link Element}</code>
     *   supplied as a child of the supplied parent is not allowed.
     * </p>
     *
     * @param base <code>Element</code> that the child
     *        couldn't be added to
     * @param added <code>Element</code> that could not be added
     * @param reason cause for the problem
     */
    public IllegalAddException(Element base, Element added, String reason) {
        super(new StringBuffer()
              .append("The element \"")
              .append(added.getQualifiedName())
              .append("\" could not be added as a child of \"")
              .append(base.getQualifiedName())
              .append("\": ")
              .append(reason)
              .toString());
    }

    /**
     * <p>
     * This will create an <code>Exception</code> indicating
     *   that the addition of the <code>{@link Element}</code>
     *   supplied as a child of the document is not allowed.
     * </p>
     *
     */
    public IllegalAddException(Document base, Element added, String reason) {
        super(new StringBuffer()
              .append("The element \"")
              .append(added.getQualifiedName())
              .append("\" could not be added as the root of the document: ")
              .append(reason)
              .toString());
    }

    /**
     * <p>
     * This will create an <code>Exception</code> indicating
     *   that the addition of the <code>{@link ProcessingInstruction}</code>
     *   supplied as content to the supplied element is not allowed.
     * </p>
     *
     * @param base element that the PI couldn't be added to
     * @param added PI that could not be added
     * @param reason cause for the problem
     */
    public IllegalAddException(Element base, ProcessingInstruction added,
                               String reason) {
        super(new StringBuffer()
              .append("The PI \"")
              .append(added.getTarget())
              .append("\" could not be added as content to \"")
              .append(base.getQualifiedName())
              .append("\": ")
              .append(reason)
              .toString());
    }

    /**
     * <p>
     * This will create an <code>Exception</code> indicating
     *   that the addition of the <code>{@link ProcessingInstruction}</code>
     *   supplied as content to the supplied document is not allowed.
     * </p>
     *
     * @param base document that the PI couldn't be added to
     * @param added PI that could not be added
     * @param reason cause for the problem
     */
    public IllegalAddException(Document base, ProcessingInstruction added,
                               String reason) {
        super(new StringBuffer()
              .append("The PI \"")
              .append(added.getTarget())
              .append("\" could not be added to the top level of the document: ")
              .append(reason)
              .toString());
    }

    /**
     * <p>
     * This will create an <code>Exception</code> indicating
     *   that the addition of the <code>{@link Comment}</code>
     *   supplied as content to the supplied element is not allowed.
     * </p>
     *
     * @param base element that the comment couldn't be added to
     * @param added comment that could not be added
     * @param reason cause for the problem
     */
    public IllegalAddException(Element base, Comment added, String reason) {
        super(new StringBuffer()
              .append("The comment \"")
              .append(added.getText())
              .append("\" could not be added as content to \"")
              .append(base.getQualifiedName())
              .append("\": ")
              .append(reason)
              .toString());
    }

    /**
     * <p>
     * This will create an <code>Exception</code> indicating
     *   that the addition of the <code>{@link Comment}</code>
     *   supplied as content to the supplied document is not allowed.
     * </p>
     *
     * @param base document that the PI couldn't be added to
     * @param added PI that could not be added
     * @param reason cause for the problem
     */
    public IllegalAddException(Document base, Comment added, String reason) {
        super(new StringBuffer()
              .append("The comment \"")
              .append(added.getText())
              .append("\" could not be added to the top level of the document: ")
              .append(reason)
              .toString());
    }

    /**
     * <p>
     * This will create an <code>Exception</code> indicating
     *   that the addition of the <code>{@link Entity}</code>
     *   supplied as content to the supplied element is not allowed.
     * </p>
     *
     * @param base element that the entity couldn't be added to
     * @param added entity that could not be added
     * @param reason cause for the problem
     */
    public IllegalAddException(Element base, Entity added, String reason) {
        super(new StringBuffer()
              .append("The entity \"")
              .append(added.getName())
              .append("\" could not be added as content to \"")
              .append(base.getQualifiedName())
              .append("\": ")
              .append(reason)
              .toString());
    }

    /**
     * <p>
     * This will create an <code>Exception</code> with the specified
     *   error message.
     * </p>
     *
     */
    public IllegalAddException(String reason) {
        super(reason);
    }
}
