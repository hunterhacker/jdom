/*-- 

 $Id: DocType.java,v 1.16 2001/12/11 07:32:03 jhunter Exp $

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
 * <p><code>DocType</code> represents an XML
 *   DOCTYPE declaration.
 * </p>
 *
 * @author Brett McLaughlin
 * @author Jason Hunter
 * @version 1.0
 */
public class DocType implements Serializable, Cloneable {

    private static final String CVS_ID = 
      "@(#) $RCSfile: DocType.java,v $ $Revision: 1.16 $ $Date: 2001/12/11 07:32:03 $ $Name:  $";

    /** The element being constrained */
    protected String elementName;

    /** The public ID of the DOCTYPE */
    protected String publicID;

    /** The system ID of the DOCTYPE */
    protected String systemID;

    /** The document having this DOCTYPE */
    protected Document document;

    /** The internal subset of the DOCTYPE */
    protected String internalSubset;

    /**
     * <p>
     * Default, no-args constructor for implementations
     *   to use if needed.
     * </p>
     */
    protected DocType() {}

    /*
     * XXX:
     *   We need to take care of entities and notations here.
     */

    /**
     * <p>
     * This will create the <code>DocType</code> with
     *   the specified element name and a reference to an
     *   external DTD.
     * </p>
     *
     * @param elementName <code>String</code> name of
     *        element being constrained.
     * @param publicID <code>String</code> public ID of
     *        referenced DTD
     * @param systemID <code>String</code> system ID of
     *        referenced DTD
     */
    public DocType(String elementName, String publicID, String systemID) {
        this.elementName = elementName;
        this.publicID = publicID;
        this.systemID = systemID;
    }

    /**
     * <p>
     * This will create the <code>DocType</code> with
     *   the specified element name and reference to an
     *   external DTD.
     * </p>
     *
     * @param elementName <code>String</code> name of
     *        element being constrained.
     * @param systemID <code>String</code> system ID of
     *        referenced DTD
     */
    public DocType(String elementName, String systemID) {
        this(elementName, "", systemID);
    }

    /**
     * <p>
     * This will create the <code>DocType</code> with
     *   the specified element name
     * </p>
     *
     * @param elementName <code>String</code> name of
     *        element being constrained.
     */
    public DocType(String elementName) {
        this(elementName, "", "");
    }

    /**
     * <p>
     * This will retrieve the element name being
     *   constrained.
     * </p>
     *
     * @return <code>String</code> - element name for DOCTYPE
     */
    public String getElementName() {
        return elementName;
    }

    /**
     * <p>
     * This will retrieve the public ID of an externally
     *   referenced DTD, or an empty <code>String</code> if
     *   none is referenced.
     * </p>
     *
     * @return <code>String</code> - public ID of referenced DTD.
     */
    public String getPublicID() {
        return publicID;
    }

    /**
     * <p>
     * This will set the public ID of an externally
     *   referenced DTD.
     * </p>
     *
     * @return publicID <code>String</code> public ID of
     *                  referenced DTD.
     */
    public DocType setPublicID(String publicID) {
        this.publicID = publicID;

        return this;
    }

    /**
     * <p>
     * This will retrieve the system ID of an externally
     *   referenced DTD, or an empty <code>String</code> if
     *   none is referenced.
     * </p>
     *
     * @return <code>String</code> - system ID of referenced DTD.
     */
    public String getSystemID() {
        return systemID;
    }

    /**
     * <p>
     * This will set the system ID of an externally
     *   referenced DTD.
     * </p>
     *
     * @return systemID <code>String</code> system ID of
     *                  referenced DTD.
     */
    public DocType setSystemID(String systemID) {
        this.systemID = systemID;

        return this;
    }

    /**
     * <p>
     * This retrieves the owning <code>{@link Document}</code> for
     *   this DocType, or null if not a currently a member of a
     *   <code>{@link Document}</code>.
     * </p>
     *
     * @return <code>Document</code> owning this DocType, or null.
     */
    public Document getDocument() {
        return this.document;
    }

    /**
     * <p>
     * This sets the <code>{@link Document}</code> holding this doctype.
     * </p>
     *
     * @param document <code>Document</code> holding this doctype
     * @return <code>Document</code> this <code>DocType</code> modified
     */
    protected DocType setDocument(Document document) {
        this.document = document;
        return this;
    }

    /**
     * <p>This sets the data for the internal subset.</p>
     * 
     * @param newData data for the internal subset, as a 
     *        <code>String</code>.
     */
    public void setInternalSubset(String newData) {
        internalSubset = newData;
    }

    /**
     * <p>This returns the data for the internal subset</p>
     * 
     * @return <code>String</code> - the internal subset
     */
    public String getInternalSubset() {
        return internalSubset;
    }

    /**
     * <p>
     *  This returns a <code>String</code> representation of the
     *    <code>DocType</code>, suitable for debugging. 
     * </p>
     *
     * @return <code>String</code> - information about the
     *         <code>DocType</code>
     */
    public String toString() {
        return new StringBuffer()
            .append("[DocType: ")
            .append(new org.jdom.output.XMLOutputter().outputString(this))
            .append("]")
            .toString();
    }

    /**
     * <p>
     *  This tests for equality of this <code>DocType</code> to the supplied
     *    <code>Object</code>.
     * </p>
     *
     * @param ob <code>Object</code> to compare to.
     * @return <code>boolean</code> - whether the <code>DocType</code> is
     *         equal to the supplied <code>Object</code>.
     */
    public final boolean equals(Object ob) {
        if (ob instanceof DocType) {
            DocType dt = (DocType) ob;
            return (stringEquals(dt.elementName, elementName) &&
                    stringEquals(dt.publicID, publicID) &&
                    stringEquals(dt.systemID, systemID));
        }
        return false;
    }

    // Utility function to help with equals()
    private boolean stringEquals(String s1, String s2) {
        if (s1 == null && s2 == null) {
            return true;
        }
        else if (s1 == null && s2 != null) {
            return false;
        }
        else {
            // If we get here we know s1 can't be null 
            return s1.equals(s2);
        }
    }

    /**
     * <p>
     *  This returns the hash code for this <code>DocType</code>.
     * </p>
     *
     * @return <code>int</code> - hash code.
     */
    public final int hashCode() {
        return super.hashCode();
    }

    /**
     * <p>
     *  This will return a clone of this <code>DocType</code>.
     * </p>
     *
     * @return <code>Object</code> - clone of this <code>DocType</code>.
     */
    public Object clone() {
        DocType docType = null;

        try {
            docType = (DocType) super.clone();
        } catch (CloneNotSupportedException ce) {
            // Can't happen
        }

        docType.document = null;

        // elementName, publicID, and systemID are all immutable 
        // (Strings) and references are copied by Object.clone()
        return docType;
    }

}
