/*--

 $Id: Child.java,v 1.4 2004/02/06 03:39:02 jhunter Exp $

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

import java.io.*;

/**
 * Superclass for JDOM objects which can be legal child content
 * of {@link org.jdom.Parent} nodes.
 *
 * @see org.jdom.Comment
 * @see org.jdom.DocType
 * @see org.jdom.Element
 * @see org.jdom.EntityRef
 * @see org.jdom.Parent
 * @see org.jdom.ProcessingInstruction
 * @see org.jdom.Text
 *
 * @author Bradley S. Huffman
 * @author Jason Hunter
 * @version $Revision: 1.4 $, $Date: 2004/02/06 03:39:02 $
 */
public abstract class Child implements Cloneable, Serializable {
    
    protected Parent parent = null;
    
    protected Child() {}
    
    /**
     * Detaches this child from its parent or does nothing if the child
     * has no parent.
     *
     * @return this child detached
     */
    public Child detach() {
        if (parent != null) {
            parent.removeContent(this);
        }
        return this;
    }
    
    /**
     * Return this child's parent, or null if this child is currently
     * not attached. The parent can be either an {@link Element}
     * or a {@link Document}.
     *
     * @return this child's parent or null if none
     */
    public Parent getParent() {
        return parent;
    }

    /**
     * Sets the parent of this Child. The caller is responsible for removing
     * any pre-existing parentage.
     *
     * @param  parent              new parent element
     * @return                     the target element
     */
    protected Child setParent(Parent parent) {
        this.parent = parent;
        return this;
    }

    /**
     * Return this child's owning document or null if the branch containing
     * this child is currently not attached to a document.
     *
     * @return this child's owning document or null if none
     */
    public Document getDocument() {
        if (parent == null) return null;
        return parent.getDocument(); 
    }

    
    /**
     * Returns the XPath 1.0 string value of this child.
     *
     * @return xpath string value of this child.
     */
    public abstract String getValue();

    /**
     * Returns a deep, unattached copy of this child and its descendants
     * detached from any parent or document.
     *
     * @return a detached deep copy of this child and descendants
     */
    public Object clone() {
        try {
            Child c = (Child)super.clone();
            c.parent = null;
            return c;
        } catch (CloneNotSupportedException e) {
            //Can not happen ....
            //e.printStackTrace();
            return null;
        }
    }
}
