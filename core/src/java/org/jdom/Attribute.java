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
 DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
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
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

/**
 * <p><code>Attribute</code> defines behavior for an XML
 *   attribute, modeled in Java.  Methods allow the user
 *   to obtain the value of the attribute as well as
 *   namespace information.
 * </p>
 *
 * @author Brett McLaughlin
 * @author Jason Hunter
 * @author Elliotte Rusty Harold
 * @author Wesley Biggs
 * @version 1.0
 */
public class Attribute implements Serializable, Cloneable {

    /** The local name of the <code>Attribute</code> */
    protected String name;

    /** The <code>{@link Namespace}</code> of the <code>Attribute</code> */
    protected transient Namespace namespace;

    /** The value of the <code>Attribute</code> */
    protected String value;

    /**
     * <p>
     * Default, no-args constructor for implementations
     *   to use if needed.
     * </p>
     */
    protected Attribute() {}

    /**
     * <p>
     * This will create a new <code>Attribute</code> with the
     *   specified (local) name and value, and in the provided
     *   <code>{@link Namespace}</code>.
     * </p>
     *
     * @param name <code>String</code> name of <code>Attribute</code>.
     * @param value <code>String</code> value for new attribute.
     */
    public Attribute(String name, String value, Namespace namespace) {
        String reason;
        if ((reason = Verifier.checkAttributeName(name)) != null) {
            throw new IllegalNameException(name, "attribute", reason);
        }

        if (namespace == null) {
            namespace = Namespace.NO_NAMESPACE;
        }

        this.name = name;
        setValue(value);
        this.namespace = namespace;
    }

    /**
     * <p>
     * This will create a new <code>Attribute</code> with the
     *   specified (local) name and value, and place it in
     *   the specified namespace (with prefix).
     * </p>
     *
     * @param name <code>String</code> name of <code>Attribute</code>.
     * @param prefix <code>String</code> prefix for <code>Attribute</code>.
     * @param uri <code>String</code> URI for namespace this
     *        <code>Attribute</code> is in.
     * @param value <code>String</code> value for new attribute.
     */
    public Attribute(String name, String prefix, String uri, String value) {
        this(name, value, Namespace.getNamespace(prefix, uri));
    }

    /**
     * <p>
     * This will create a new <code>Attribute</code> with the
     *   specified (local) name and value, and does not place
     *   the attribute in a <code>{@link Namespace}</code>.
     * </p><p>
     *  <b>Note</b>: This actually explicitly puts the
     *    <code>Attribute</code> in the "empty" <code>Namespace</code>
     *    (<code>{@link Namespace#NO_NAMESPACE}</code>).
     * </p>
     *
     * @param name <code>String</code> name of <code>Attribute</code>.
     * @param value <code>String</code> value for new attribute.
     */
    public Attribute(String name, String value) {
        this(name, value, Namespace.NO_NAMESPACE);
    }

    /**
     * <p>
     * This will retrieve the local name of the
     *   <code>Attribute</code>. For any XML attribute
     *   which appears as
     *   <code>[namespacePrefix]:[attributeName]</code>,
     *   the local name of the attribute would be
     *   <code>[attributeName]</code>. When the attribute
     *   has no namespace , the local name is simply the attribute
     *   name.
     * </p><p>
     * To obtain the namespace prefix for this
     *   attribute, the
     *   <code>{@link #getNamespacePrefix()}</code>
     *   method should be used.
     * </p>
     *
     * @return <code>String</code> - name of this attribute,
     *                               without any namespace prefix.
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     * This will retrieve the qualified name of the <code>Attribute</code>.
     *   For any XML attribute whose name is
     *   <code>[namespacePrefix]:[elementName]</code>,
     *   the qualified name of the attribute would be
     *   everything (both namespace prefix and
     *   element name). When the attribute has no
     *   namespace, the qualified name is simply the attribute's
     *   local name.
     * </p><p>
     * To obtain the local name of the attribute, the
     *   <code>{@link #getName()}</code> method should be used.
     * </p><p>
     * To obtain the namespace prefix for this attribute,
     *   the <code>{@link #getNamespacePrefix()}</code>
     *   method should be used.
     * </p>
     *
     * @return <code>String</code> - full name for this element.
     */
    public String getQualifiedName() {
        StringBuffer qname = new StringBuffer();

        // Add prefix, if needed
        String prefix = namespace.getPrefix();
        if ((prefix != null) && (!prefix.equals(""))) {
            qname.append(prefix)
                 .append(":");
        }

        qname.append(name);

        return qname.toString();
    }

    /**
     * <p>
     * This will retrieve the namespace prefix of the
     *   <code>Attribute</code>. For any XML attribute
     *   which appears as
     *   <code>[namespacePrefix]:[attributeName]</code>,
     *   the namespace prefix of the attribute would be
     *   <code>[namespacePrefix]</code>. When the attribute
     *   has no namespace, an empty <code>String</code> is returned.
     * </p>
     *
     * @return <code>String</code> - namespace prefix of this
     *                               attribute.
     */
    public String getNamespacePrefix() {
        return namespace.getPrefix();
    }

    /**
     * <p>
     * This returns the URI mapped to this <code>Attribute</code>'s
     *   prefix. If no
     *   mapping is found, an empty <code>String</code> is returned.
     * </p>
     *
     * @return <code>String</code> - namespace URI for this <code>Attribute</code>.
     */
    public String getNamespaceURI() {
        return namespace.getURI();
    }

    /**
     * <p>
     *  This will return this <code>Attribute</code>'s
     *    <code>{@link Namespace}</code>.
     * </p>
     *
     * @return <code>Namespace</code> - Namespace object for this <code>Attribute</code>
     */
    public Namespace getNamespace() {
        return namespace;
    }

    /**
     * <p>
     * This will return the actual textual value of this
     *   <code>Attribute</code>.  This will include all text
     *   within the quotation marks.
     * </p>
     *
     * @return <code>String</code> - value for this attribute.
     */
    public String getValue() {
        return value;
    }

    /**
     * <p>
     * This will set the value of the <code>Attribute</code>.
     * </p>
     *
     * @param value <code>String</code> value for the attribute.
     * @return <code>Attribute</code> - this Attribute modified.
     */
    public Attribute setValue(String value) {
        this.value = value;
        return this;
    }

    /**
     * <p>
     *  This returns a <code>String</code> representation of the
     *    <code>Attribute</code>, suitable for debugging. If the XML
     *    representation of the <code>Attribute</code> is desired,
     *    <code>{@link #getSerializedForm}</code> should be used.
     * </p>
     *
     * @return <code>String</code> - information about the
     *         <code>Attribute</code>
     */
    public final String toString() {
        return new StringBuffer()
            .append("[Attribute: ")
            .append(getSerializedForm())
            .append("]")
            .toString();
    }

    /**
     * <p>
     *  This will return the <code>Attribute</code> in XML format,
     *    usable in an XML document.
     * </p>
     *
     * @return <code>String</code> - the serialized form of the
     *         <code>Attribute</code>.
     */
    public final String getSerializedForm() {
        return new StringBuffer()
            .append(getQualifiedName())
            .append("=\"")
            .append(value)
            .append("\"")
            .toString();
    }

    /**
     * <p>
     *  This tests for equality of this <code>Attribute</code> to the supplied
     *    <code>Object</code>.
     * </p>
     *
     * @param ob <code>Object</code> to compare to.
     * @return <code>boolean</code> - whether the <code>Attribute</code> is
     *         equal to the supplied <code>Object</code>.
     */
    public final boolean equals(Object ob) {
        return (ob == this);
    }

    /**
     * <p>
     *  This returns the hash code for this <code>Attribute</code>.
     * </p>
     *
     * @return <code>int</code> - hash code.
     */
    public final int hashCode() {
        return super.hashCode();
    }

    /**
     * <p>
     *  This will return a clone of this <code>Attribute</code>.
     * </p>
     *
     * @return <code>Object</code> - clone of this <code>Attribute</code>.
     */
    public final Object clone() {
        Attribute attribute = new Attribute(name, value, namespace);

        return attribute;
    }

    /////////////////////////////////////////////////////////////////
    // Convenience Methods below here
    /////////////////////////////////////////////////////////////////

    /**
     * <p>
     * This gets the value of the attribute, in
     *   <code>int</code> form, and if no conversion
     *   can occur, throws a
     *   <code>{@link DataConversionException}</code>
     * </p>
     *
     * @return <code>int</code> value of attribute.
     * @throws <code>DataConversionException</code> - when conversion fails.
     */
    public int getIntValue() throws DataConversionException {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new DataConversionException(name, "int");
        }
    }

    /**
     * <p>
     * This gets the value of the attribute, in
     *   <code>long</code> form, and if no conversion
     *   can occur, throws a
     *   <code>{@link DataConversionException}</code>
     * </p>
     *
     * @return <code>long</code> value of attribute.
     * @throws <code>DataConversionException</code> - when conversion fails.
     */
    public long getLongValue() throws DataConversionException {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new DataConversionException(name, "long");
        }
    }

    /**
     * <p>
     * This gets the value of the attribute, in
     *   <code>float</code> form, and if no conversion
     *   can occur, throws a
     *   <code>{@link DataConversionException}</code>
     * </p>
     *
     * @return <code>float</code> value of attribute.
     * @throws <code>DataConversionException</code> - when conversion fails.
     */
    public float getFloatValue() throws DataConversionException {
        try {
            // Avoid Float.parseFloat() to support JDK 1.1
            return Float.valueOf(value).floatValue();
        } catch (NumberFormatException e) {
            throw new DataConversionException(name, "float");
        }
    }

    /**
     * <p>
     * This gets the value of the attribute, in
     *   <code>double</code> form, and if no conversion
     *   can occur, throws a
     *   <code>{@link DataConversionException}</code>
     * </p>
     *
     * @return <code>double</code> value of attribute.
     * @throws <code>DataConversionException</code> - when conversion fails.
     */
    public double getDoubleValue() throws DataConversionException {
        try {
            // Avoid Double.parseDouble() to support JDK 1.1
            return Double.valueOf(value).doubleValue();
        } catch (NumberFormatException e) {
            throw new DataConversionException(name, "double");
        }
    }

    /**
     * <p>
     * This gets the value of the attribute, in
     *   <code>boolean</code> form, and if no conversion
     *   can occur, throws a
     *   <code>{@link DataConversionException}</code>
     * </p>
     *
     * @return <code>boolean</code> value of attribute.
     * @throws <code>DataConversionException</code> - when conversion fails.
     */
    public boolean getBooleanValue() throws DataConversionException {
        if ((value.equalsIgnoreCase("true")) ||
            (value.equalsIgnoreCase("on")) ||
            (value.equalsIgnoreCase("yes"))) {
            return true;
        } else if ((value.equalsIgnoreCase("false")) ||
                   (value.equalsIgnoreCase("off")) ||
                   (value.equalsIgnoreCase("no"))) {
            return false;
        } else {
            throw new DataConversionException(name, "boolean");
        }
    }

    // Support a custom Namespace serialization so no two namespace
    // object instances may exist for the same prefix/uri pair
    private void writeObject(ObjectOutputStream out) throws IOException {

        out.defaultWriteObject();

        // We use writeObject() and not writeUTF() to minimize space
        // This allows for writing pointers to already written strings
        out.writeObject(namespace.getPrefix());
        out.writeObject(namespace.getURI());
    }

    private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException {
        
        in.defaultReadObject();

        namespace = Namespace.getNamespace(
            (String)in.readObject(), (String)in.readObject());
    }
}
