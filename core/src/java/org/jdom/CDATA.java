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
 * <p>
 * <code>CDATA</code> defines behavior for an XML
 *   CDATA section, modeled in Java.  Methods
 *   allow the user to obtain the text of the CDATA.
 * </p>
 *
 * @author Dan Schaffer
 * @author Brett McLaughlin
 * @author Jason Hunter
 * @version $Revision: 1.8 $
 */
public class CDATA implements Serializable, Cloneable {

    /** Text of the <code>CDATA</code> */
    protected String text;

    /**
     * <p>
     * Default, no-args constructor for implementations
     *   to use if needed.
     * </p>
     */
    protected CDATA() {}

    /**
     * <p>
     * This creates the CDATA with the supplied
     *   text.
     * </p>
     *
     * @param text <code>String</code> content of CDATA.
     */
    public CDATA(String text) {
        String reason;
        if ((reason = Verifier.checkCDATASection(text)) != null) {
            throw new IllegalDataException(text, "CDATA section", reason);
        }

	this.text = text;
    }

    /**
     * <p>
     * This returns the textual data within the
     *   <code>CDATA</code>.
     * </p>
     *
     * @return <code>String</code> - text of CDATA.
     */
    public String getText() {
	return text;
    }


    /**
     * <p>
     * This will set the value of the <code>CDATA</code>.
     * </p>
     *
     * @param text <code>String</code> text for CDATA.
     * @return <code>CDATA</code> - this CDATA modified.
     * 
     * @deprecated Deprecated in beta6, because CDATA has no parentage it 
     * must be immutable
     */
    public void setText(String text) {
	this.text = text; // need above Verifier check!
    }

    /**
     * <p>
     *  This returns a <code>String</code> representation of the
     *    <code>CDATA</code>, suitable for debugging. If the XML
     *    representation of the <code>CDATA</code> is desired,
     *    <code>{@link #getSerializedForm}</code> should be used.
     * </p>
     *
     * @return <code>String</code> - information about the
     *         <code>Attribute</code>
     */
    public String toString() {
	return new StringBuffer()
	    .append("[CDATA: ")
	    .append(getSerializedForm())
	    .append("]")
	    .toString();
    }

    /**
     * <p>
     *  This will return the <code>CDATA</code> in XML format,
     *    usable in an XML document.
     * </p>
     *
     * @return <code>String</code> - the serialized form of the
     *         <code>CDATA</code>.
     */
    public final String getSerializedForm() {
	return new StringBuffer()
	    .append("<![CDATA[")
	    .append(text)
	    .append("]]>")
	    .toString();
    }

    /**
     * <p>
     *  This tests for equality of this <code>CDATA</code> to the supplied
     *    <code>Object</code>.
     * </p>
     *
     * @param ob <code>Object</code> to compare to.
     * @return <code>boolean</code> - whether the <code>Comment</code> is
     *         equal to the supplied <code>Object</code>.
     */
    public final boolean equals(Object ob) {
	return (ob == this);
    }

    /**
     * <p>
     *  This returns the hash code for this <code>CDATA</code>.
     * </p>
     *
     * @return <code>int</code> - hash code.
     */
    public final int hashCode() {
	return super.hashCode();
    }

    /**
     * <p>
     *  This will return a clone of this <code>CDATA</code>.
     * </p>
     *
     * @return <code>Object</code> - clone of this <code>CDATA</code>.
     */
    public Object clone() {
	CDATA clone = new CDATA(text);
	return clone;
    }

}
