/*-- 

 $Id: EntityRef.java,v 1.1 2001/05/09 05:52:20 jhunter Exp $

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
 * <p><code>EntityRef</code> Defines an XML entity in Java.</p>
 *
 * @author Brett McLaughlin
 * @author Jason Hunter
 * @author Philip Nelson
 * @version 1.0
 */
public class EntityRef implements Serializable, Cloneable {

    private static final String CVS_ID = 
      "@(#) $RCSfile: EntityRef.java,v $ $Revision: 1.1 $ $Date: 2001/05/09 05:52:20 $ $Name:  $";

    /** The name of the <code>EntityRef</code> */
    protected String name;

    /** The PublicID of the <code>EntityRef</code> */
    protected String publicID;

    /** The SystemID of the <code>EntityRef</code> */
    protected String systemID;

    /** Parent element, or null if none */
    protected Element parent;

    /** Containing document node, or null if none */
    protected Document document;

    /**
     * <p>
     * Default, no-args constructor for implementations
     *   to use if needed.
     * </p>
     */
    protected EntityRef() {}

    /**
     * <p>
     * This will create a new <code>EntityRef</code>
     *   with the supplied name.
     * </p>
     *
     * @param name <code>String</code> name of element.
     */
    public EntityRef(String name) {
        this.name = name;
        
    }

    /**
     * <p>
     *  This will return a clone of this <code>EntityRef</code>.
     * </p>
     *
     * @return <code>Object</code> - clone of this <code>EntityRef</code>.
     */
    public Object clone() {
        EntityRef entity = null;

        try {
            entity = (EntityRef) super.clone();
        } catch (CloneNotSupportedException ce) {
            // Can't happen
        }

        // name is a reference to an immutable (String) object
        // and is copied by Object.clone()

        // The parent and document references are copied by Object.clone(), so
        // must set to null
        entity.parent = null;
        entity.document = null;

        return entity;
    }

    /**
     * <p>
     * This detaches the <code>Entity</code> from its parent, or does nothing 
     * if the <code>Entity</code> has no parent.
     * </p>
     *
     * @return <code>Entity</code> - this <code>Entity</code> modified.
     */
    public EntityRef detach() {
        Element p = getParent();
        if (p != null) {
            p.removeContent(this);
        }
        return this;
    }

    /**
     * <p>
     *  This tests for equality of this <code>Entity</code> to the supplied
     *    <code>Object</code>.
     * </p>
     *
     * @param ob <code>Object</code> to compare to.
     * @return <code>boolean</code> - whether the <code>Entity</code> is
     *         equal to the supplied <code>Object</code>.
     */
    public final boolean equals(Object ob) {
        return (ob == this);
    }

    /**
     * <p>
     * This retrieves the owning <code>{@link Document}</code> for
     *   this Entity, or null if not a currently a member of a
     *   <code>{@link Document}</code>.
     * </p>
     *
     * @return <code>Document</code> owning this Entity, or null.
     */
    public Document getDocument() {
        if (document != null) {
            return document;
        }

        Element p = getParent();
        if (p != null) {
            return p.getDocument();
        }

        return null;
    }

    /**
     * <p>
     * This returns the name of the
     *   <code>EntityRef</code>.
     * </p>
     *
     * @return <code>String</code> - entity name.
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     * This will return the parent of this <code>EntityRef</code>.
     *   If there is no parent, then this returns <code>null</code>.
     * </p>
     *
     * @return parent of this <code>EntityRef</code>
     */
    public Element getParent() {
        return parent;
    }

    /**
     * <p>
     * This will return the publid ID of this <code>EntityRef</code>.
     *   If there is no public ID, then this returns <code>null</code>.
     * </p>
     *
     * @return public ID of this <code>EntityRef</code>
     */
    public java.lang.String getPublicID() {
        return publicID;
    }

    /**
     * <p>
     * This will return the system ID of this <code>EntityRef</code>.
     *   If there is no system ID, then this returns <code>null</code>.
     * </p>
     *
     * @return system ID of this <code>EntityRef</code>
     */
    public java.lang.String getSystemID() {
        return systemID;
    }

    /**
     * <p>
     *  This returns the hash code for this <code>Entity</code>.
     * </p>
     *
     * @return <code>int</code> - hash code.
     */
    public final int hashCode() {
        return super.hashCode();
    }

    /**
     * <p>
     * This will set the parent of this <code>Entity</code>.
     * </p>
     *
     * @param parent <code>Element</code> to be new parent.
     * @return this <code>Entity</code> modified.
     */
    protected EntityRef setParent(Element parent) {
        this.parent = parent;
        return this;
    }

    /**
     * <p>
     * This will set the name of this <code>EntityRef</code>.
     * </p>
     *
     * @param name new name of the entity
     * @return this <code>EntityRef</code> modified.
     */
    public EntityRef setName(String newPublicID) {
        this.name = name;
        return this;
    }

    /**
     * <p>
     * This will set the public ID of this <code>EntityRef</code>.
     * </p>
     *
     * @param newPublicID new public id
     * @return this <code>EntityRef</code> modified.
     */
    public EntityRef setPublicID(String newPublicID) {
        this.publicID = newPublicID;
        return this;
    }

    /**
     * <p>
     * This will set the system ID of this <code>EntityRef</code>.
     * </p>
     *
     * @param newSystemID new system id
     * @return this <code>EntityRef</code> modified.
     */
    public EntityRef setSystemID(String newSystemID) {
        this.systemID = newSystemID;
        return this;
    }

    /**
     * <p>
     *  This returns a <code>String</code> representation of the
     *    <code>EntityRef</code>, suitable for debugging.
     * </p>
     *
     * @return <code>String</code> - information about the
     *         <code>EntityRef</code>
     */
    public String toString() {
        return new StringBuffer()
            .append("[EntityRef: ")
            .append("&")
            .append(name)
            .append(";")
            .toString();
    }
}
