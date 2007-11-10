/*--

 $Id: EntityRef.java,v 1.22 2007/11/10 05:28:59 jhunter Exp $

 Copyright (C) 2000-2007 Jason Hunter & Brett McLaughlin.
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

package org.jdom;

/**
 * An XML entity reference. Methods allow the user to manage its name, public
 * id, and system id.
 *
 * @version $Revision: 1.22 $, $Date: 2007/11/10 05:28:59 $
 * @author  Brett McLaughlin
 * @author  Jason Hunter
 * @author  Philip Nelson
 */
public class EntityRef extends Content {

    private static final String CVS_ID =
      "@(#) $RCSfile: EntityRef.java,v $ $Revision: 1.22 $ $Date: 2007/11/10 05:28:59 $ $Name:  $";

    /** The name of the <code>EntityRef</code> */
    protected String name;

    /** The PublicID of the <code>EntityRef</code> */
    protected String publicID;

    /** The SystemID of the <code>EntityRef</code> */
    protected String systemID;

    /**
     * Default, no-args constructor for implementations to use if needed.
     */
    protected EntityRef() {}

    /**
     * This will create a new <code>EntityRef</code> with the supplied name.
     *
     * @param name <code>String</code> name of element.
     * @throws IllegalNameException if the given name is not a legal
     *         XML name.
     */
    public EntityRef(String name) {
        this(name, null, null);
    }

    /**
     * This will create a new <code>EntityRef</code>
     * with the supplied name and system id.
     *
     * @param name <code>String</code> name of element.
     * @param systemID system id of the entity reference being constructed
     * @throws IllegalNameException if the given name is not a legal
     *         XML name.
     * @throws IllegalDataException if the given system ID is not a legal
     *         system literal.
     */
    public EntityRef(String name, String systemID) {
        this(name, null, systemID);
    }

    /**
     * This will create a new <code>EntityRef</code>
     * with the supplied name, public id, and system id.
     *
     * @param name <code>String</code> name of element.
     * @param publicID public id of the entity reference being constructed
     * @param systemID system id of the entity reference being constructed
     * @throws IllegalDataException if the given system ID is not a legal
     *         system literal or the the given public ID is not a
     *         legal public ID
     * @throws IllegalNameException if the given name is not a legal
     *         XML name.
     */
    public EntityRef(String name, String publicID, String systemID) {
        setName(name);
        setPublicID(publicID);
        setSystemID(systemID);
    }

    /**
     * This returns the name of the <code>EntityRef</code>.
     *
     * @return <code>String</code> - entity name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the empty string since entity references don't have an XPath
     * 1.0 string value.
     * @return the empty string
     */
    public String getValue() {
        return "";  // entity references don't have XPath string values
    }

    /**
     * This will return the publid ID of this <code>EntityRef</code>.
     * If there is no public ID, then this returns <code>null</code>.
     *
     * @return public ID of this <code>EntityRef</code>
     */
    public String getPublicID() {
        return publicID;
    }

    /**
     * This will return the system ID of this <code>EntityRef</code>.
     * If there is no system ID, then this returns <code>null</code>.
     *
     * @return system ID of this <code>EntityRef</code>
     */
    public String getSystemID() {
        return systemID;
    }

    /**
     * This will set the name of this <code>EntityRef</code>.
     *
     * @param name new name of the entity
     * @return this <code>EntityRef</code> modified.
     * @throws IllegalNameException if the given name is not a legal
     *         XML name.
     */
    public EntityRef setName(String name) {
        // This can contain a colon so we use checkXMLName()
        // instead of checkElementName()
        String reason = Verifier.checkXMLName(name);
        if (reason != null) {
            throw new IllegalNameException(name, "EntityRef", reason);
        }
        this.name = name;
        return this;
    }

    /**
     * This will set the public ID of this <code>EntityRef</code>.
     *
     * @param publicID new public id
     * @return this <code>EntityRef</code> modified.
     * @throws IllegalDataException if the given public ID is not a legal
     *         public ID.
     */
    public EntityRef setPublicID(String publicID) {
        String reason = Verifier.checkPublicID(publicID);
        if (reason != null) {
            throw new IllegalDataException(publicID, "EntityRef", reason);
        }
        this.publicID = publicID;
        return this;
    }

    /**
     * This will set the system ID of this <code>EntityRef</code>.
     *
     * @param systemID new system id
     * @throws IllegalDataException if the given system ID is not a legal
     *         system literal.
     * @return this <code>EntityRef</code> modified.
     */
    public EntityRef setSystemID(String systemID) {
        String reason = Verifier.checkSystemLiteral(systemID);
        if (reason != null) {
            throw new IllegalDataException(systemID, "EntityRef", reason);
        }
        this.systemID = systemID;
        return this;
    }

    /**
     * This returns a <code>String</code> representation of the
     * <code>EntityRef</code>, suitable for debugging.
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
            .append("]")
            .toString();
    }
}
