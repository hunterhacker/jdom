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

import java.io.Serializable;

/**
 * <p><code><b>Text</b></code> represents character-based content within an
 *   XML document represented by JDOM. It is intended to provide a modular,
 *   perantable method of representing that text. Additionally,
 *   <code>Text</code> makes no guarantees about the underlying textual
 *   representation of character data, but does expose that data as a Java
 *   <code>String</code>.</p>
 *
 * @author Brett McLaughlin
 * @version 1.0
 */
public class Text implements Serializable, Cloneable {

    /** The actual character content */
    protected StringBuffer value;

    /** This <code>Text</code> node's parent. */
    protected Element parent;

    /** 
     * <p>This is the protected, no-args constructor standard in all JDOM
     *  classes. It allows subclassers to get a raw instance with no
     *  initialization.</p>
     */
    protected Text() { }

    /**
     * <p>This constructor creates a new <code>Text</code> node, with the
     *   supplied value as it's character content.</p>
     *
     * @param stringValue the node's character content.
     */
    public Text(String stringValue) {
        value = new StringBuffer(stringValue);
    }

    /**
     * <p>This returns the value of this <code>Text</code> node as a Java
     *   <code>String</code>.</p>
     *
     * @return <code>String</code> - character content of this node.
     */
    public String getValue() {
        return value.toString();
    }

    /**
     * <p>This will set the value of this <code>Text</code> node.
     *
     * @param stringValue value for node's content.
     */
    public void setValue(String stringValue) {
        value.setLength(0);
        if (stringValue == null) {
            value.append("");
        } else {
            value.append(stringValue);
        }
    }

    /**
     * <p>This will append character content to whatever content already
     *   exists within this <code>Text</code> node.</p>
     *
     * @param stringValue character content to append.
     */
    public void append(String stringValue) {
        if (stringValue == null) {
            return;
        }
        value.append(stringValue);
    }

    /**
     * <p>This will return the parent of this <code>Text</code> node, which
     *   is always a JDOM <code>{@link Element}</code>.</p>
     *
     * @return <code>Element</code> - this node's parent.
     */
    public Element getParent() {
        return parent;
    }

    /**
     * <p>This will set the parent of the <code>Text</code> node to the supplied
     *   <code>{@link Element}</code>. This method is intentionally left as
     *   <code>protected</code> so that only JDOM internals use it.</p>
     * <p>If you need an instance of this <code>Text</code> node with a new
     *   parent, you should get a copy of this node with 
     *   <code>{@link #clone}</code> and set it on the desired (new) parent
     *   <code>Element</code>.</p>
     *
     * @param parent parent for this node.
    protected void setParent(Element parent) {
        this.parent = parent;
    }

    /**
     * <p>
     * Detaches the text from its parent, or does nothing if the
     * text has no parent.
     * </p>
     *
     * @return <code>Text</code> - this <code>Text</code> modified.
     */
/* Commented out til the parent knows about text, won't compile as is.
    public Text detach() {
        if (parent != null) {
            parent.removeContent(this);
        }
        parent = null;
        return this;
    }
*/

    /**
     * <p>This returns a <code>String</code> representation of the 
     *   <code>Text</code> node, suitable for debugging. If the XML
     *   representation of the <code>Text</code> node is desired, 
     *   either <code>{@link #getValue}</code> or
     *   {@link org.jdom.output.XMLOutputter#outputString(Text)}</code>
     *   should be used.</p>
     *
     * @return <code>String</code> - information about this node.
     */
    public String toString() {
        return new StringBuffer(64)
            .append("Text: ")
            .append(getValue())
            .toString();
    }

    /**
     * <p>This will generate a hash code for this node.</p>
     *
     * @return <code>int</code> - hash code for this node.
     */
    public int hashCode() {
        return value.toString().hashCode();
    }

    /**
     * <p>This will return a clone of this <code>Text</code> node, with the
     *   same character content, but no parent.</p>
     *
     * @return <code>Text</code> - cloned node.
     */
    public Object clone() {
        Text text = null;

        try {
            text = (Text)super.clone();
        } catch (CloneNotSupportedException ce) {
            // Can't happen
        } 

        text.parent = null;
        text.value = new StringBuffer(value.toString());

        return text;
    }

    /**
     * <p>
     *  This tests for equality of this <code>Text</code> to the supplied
     *    <code>Object</code>, explicitly using the == operator.
     * </p>
     *
     * @param ob <code>Object</code> to compare to
     * @return whether the <code>Text</code> nodes are equal
     */
    public final boolean equals(Object ob) {
        return (this == ob);
    }
}
