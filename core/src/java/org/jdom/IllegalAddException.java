/*--

 Copyright 2000 Brett McLaughlin & Jason Hunter. All rights reserved.

 Redistribution and use in source and binary forms, with or without modifica-
 tion, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice,
    this list of conditions, and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions, the disclaimer that follows these conditions,
    and/or other materials provided with the distribution.

 3. The names "JDOM" and "Java Document Object Model" must not be used to
    endorse or promote products derived from this software without prior
    written permission. For written permission, please contact
    license@jdom.org.

 4. Products derived from this software may not be called "JDOM", nor may
    "JDOM" appear in their name, without prior written permission from the
    JDOM Project Management (pm@jdom.org).

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 JDOM PROJECT  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLUDING, BUT
 NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 This software  consists of voluntary contributions made  by many individuals
 on  behalf of the Java Document Object Model Project and was originally
 created by Brett McLaughlin <brett@jdom.org> and
 Jason Hunter <jhunter@jdom.org>. For more  information on the JDOM
 Project, please see <http://www.jdom.org/>.

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
     * @param attribute <code>Attribute</code> that could not be added
     * @param element <code>Element</code> that <code>Attribute</code>
     *        couldn't be added to.
     */
    public IllegalAddException(Attribute attribute, Element element, String reason) {
        super(new StringBuffer()
              .append("The attribute \"")
              .append(attribute.getQualifiedName())
              .append("\" could not be added to the element \"")
              .append(element.getQualifiedName())
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
     * @param element <code>Element</code> that could not be added
     * @param parent <code>Element</code> that the child
     *        couldn't be added to.
     */
    public IllegalAddException(Element element, Element parent) {
        super(new StringBuffer()
              .append("The element ")
              .append(element.getQualifiedName())
              .append(" could not be added as a child of ")
              .append(parent.getQualifiedName())
              .append(" because it already has an existing parent (")
              .append((element.isRootElement() ?
                  "document root" :
                  element.getParent().getQualifiedName()))
              .append(").")
              .toString());
    }

    /**
     * <p>
     * This will create an <code>Exception</code> with the specified
     *   error message.
     * </p>
     *
     */
    public IllegalAddException(String msg) {
        super(msg);
    }
}
