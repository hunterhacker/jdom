/*--

 $Id: CDATA.java,v 1.16 2001/12/11 07:32:03 jhunter Exp $

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
 * <p><code><b>CDATA</b></code> represents character-based content within an
 *   XML document represented by JDOM. It is intended to provide a modular,
 *   perantable method of representing CDATA. Additionally,
 *   <code>CDATA</code> makes no guarantees about the underlying textual
 *   representation of character data, but does expose that data as a Java
 *   <code>String</code>.</p>
 *
 * @author Dan Schaffer
 * @author Brett McLaughlin
 * @author Jason Hunter
 * @author Bradley S. Huffman
 * @version 1.0
 */
public class CDATA implements Serializable, Cloneable {

    private static final String CVS_ID = 
      "@(#) $RCSfile: CDATA.java,v $ $Revision: 1.16 $ $Date: 2001/12/11 07:32:03 $ $Name:  $";

    /** The actual character content */
    // XXX See http://www.servlets.com/archive/servlet/ReadMsg?msgId=8776
    // from Alex Rosen for a reason why String might be better
    // XXX See http://www.servlets.com/archive/servlet/ReadMsg?msgId=8612
    // from elharo for a description of why Java characters may not suffice
    // long term
    protected StringBuffer value;

    /** This <code>CDATA</code> node's parent. */
    protected Element parent;

    /**
     * <p>This is the protected, no-args constructor standard in all JDOM
     *  classes. It allows subclassers to get a raw instance with no
     *  initialization.</p>
     */
    protected CDATA() { }

    /**
     * <p>This constructor creates a new <code>CDATA</code> node, with the
     *   supplied string value as it's character content.</p>
     *
     * @param str the node's character content.
     */
    public CDATA(String str) {
        setText(str);
    }

    /**
     * <p>This returns the value of this <code>CDATA</code> node as a Java
     *   <code>String</code>.</p>
     *
     * @return <code>String</code> - character content of this node.
     */
    public String getText() {
        return value.toString();
    }

    /**
     * <p>
     * This returns the textual content with all surrounding whitespace
     * removed.  If only whitespace exists, the empty string is returned.
     * </p>
     *
     * @return trimmed cdata content or empty string
     */
    public String getTextTrim() {
        return getText().trim();
    }

    /**
     * <p>
     * This returns the textual content with all surrounding whitespace
     * removed and internal whitespace normalized to a single space.  If
     * only whitespace exists, the empty string is returned.
     * </p>
     *
     * @return normalized cdata content or empty string
     */
    public String getTextNormalize() {
        return Text.normalizeString(getText());
    }

    /**
     * <p>This will set the value of this <code>CDATA</code> node.</p>
     *
     * @param str value for node's content.
     */
    public CDATA setText(String str) {
        value = new StringBuffer();          /* Clear old cdata */
        if (str == null)
            return this;
        append(str);        /* so we get the Verifier check */
        return this;
    }

    /**
     * <p>This will append character content to whatever content already
     *   exists within this <code>CDATA</code> node.</p>
     *
     * @param str character content to append.
     */
    public void append(String str) {
        String reason;

        if (str == null) {
            return;
        }
        if ((reason = Verifier.checkCDATASection(str)) != null) {
            throw new IllegalDataException(str, "CDATA section", reason);
        }

        value.append(str);
    }

    /**
     * <p>This will append the content of another <code>CDATA</code> node
     *   to this node.</p>
     *
     * @param cdata CDATA node to append.
     */
    public void append(CDATA cdata) {
        if (cdata == null) {
            return;
        }
        value.append(cdata.getText());
    }

    /**
     * <p>This will return the parent of this <code>CDATA</code> node, which
     *   is always a JDOM <code>{@link Element}</code>.</p>
     *
     * @return <code>Element</code> - this node's parent.
     */
    public Element getParent() {
        return parent;
    }

    /**
     * <p>
     *   This retrieves the owning <code>{@link Document}</code> for
     *   this <code>CDATA</code>, or null if not a currently a member
     *   of a <code>{@link Document}</code>.
     * </p>
     *
     * @return <code>Document</code> owning this <code>CDATA</code>, or null.
     */
    public Document getDocument() {
        if (parent != null) {
            return parent.getDocument();
        }
        return null;
    }

    /**
     * <p>This will set the parent of the <code>CDATA</code> node to the 
     *   supplied <code>{@link Element}</code>. This method is intentionally 
     *   left as <code>protected</code> so that only JDOM internals use it.</p>
     * <p>If you need an instance of this <code>CDATA</code> node with a new
     *   parent, you should get a copy of this node with
     *   <code>{@link #clone}</code> and set it on the desired (new) parent
     *   <code>Element</code>.</p>
     *
     * @param parent parent for this node.
     */
    protected CDATA setParent(Element parent) {
        this.parent = parent;
        return this;
    }

    /**
     * <p>
     * Detaches the <code>CDATA</code> from its parent, or does nothing
     *  if the <code>CDATA</code> has no parent.
     * </p>
     *
     * @return <code>CDATA</code> - this <code>CDATA</code> modified.
     */
    public CDATA detach() {
        if (parent != null) {
            parent.removeContent(this);
        }
        parent = null;
        return this;
    }

    /**
     * <p>This returns a <code>String</code> representation of the
     *   <code>CDATA</code> node, suitable for debugging. If the XML
     *   representation of the <code>CDATA</code> node is desired,
     *   either <code>{@link #getText}</code> or
     *   {@link org.jdom.output.XMLOutputter#output(CDATA, Writer)}</code>
     *   should be used.</p>
     *
     * @return <code>String</code> - information about this node.
     */
    public String toString() {
        return new StringBuffer(64)
            .append("[CDATA: ")
            .append(getText())
            .append("]")
            .toString();
    }

    /**
     * <p>This will generate a hash code for this node.</p>
     *
     * @return <code>int</code> - hash code for this node.
     */
    public final int hashCode() {
        return super.hashCode();
    }

    /**
     * <p>This will return a clone of this <code>CDATA</code> node, with the
     *   same character content, but no parent.</p>
     *
     * @return <code>CDATA</code> - cloned node.
     */
    public Object clone() {
        CDATA cdata = null;

        try {
            cdata = (CDATA)super.clone();
        } catch (CloneNotSupportedException ce) {
            // Can't happen
        }

        cdata.parent = null;
        cdata.value = new StringBuffer(value.toString());

        return cdata;
    }

    /**
     * <p>
     *  This tests for equality of this <code>CDATA</code> to the supplied
     *    <code>Object</code>, explicitly using the == operator.
     * </p>
     *
     * @param ob <code>Object</code> to compare to
     * @return whether the <code>CDATA</code> nodes are equal
     */
    public final boolean equals(Object ob) {
        return (this == ob);
    }
}
