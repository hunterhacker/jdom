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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * <p><code>Namespace</code> defines both a factory for
 *   creating XML namespaces, and a namespace itself. This class
 *   represents an XML namespace in Java.  
 * </p>
 * <p>
 *   Elements and Attributes containing Namespaces <b>can</b> be serialized; 
 *   however the Namespace class itself does not implement 
 *   <code>java.io.Serializable</code>.  This works because the Element and
 *   Attribute classes handle serialization of their Namespaces manually.
 *   The classes use the getNamespace() method on deserialization to ensure 
 *   there may be only one unique Namespace object for any unique 
 *   prefix/uri pair, something needed for efficiency reasons.
 * </p>
 *
 * @author Brett McLaughlin
 * @author Elliotte Rusty Harold
 * @author Wesley Biggs
 * @version 1.0
 */
public final class Namespace {

    // XXX May want to use weak references to keep the maps from growing 
    // large with extended use

    /** 
     * Factory list of namespaces. 
     *  Keys are <i>prefix</i>&amp;<i>URI</i>. 
     *  Values are Namespace objects 
     */
    private static HashMap namespaces;

    /** Factory list of mappings */
    private static HashMap mappings;

    /** Define a <code>Namespace</code> for when <i>not</i> in a namespace */
    public static final Namespace NO_NAMESPACE = new Namespace("", "");

    public static final Namespace XML_NAMESPACE = 
        new Namespace("xml", "http://www.w3.org/XML/1998/namespace");

    /** The prefix mapped to this namespace */
    private String prefix;

    /** The URI for this namespace */
    private String uri;

    /**
     * <p>
     *  This static initializer acts as a factory contructor.
     *  It sets up storage and required initial values.
     * </p>
     *
     * XXX: Maybe this should be a singleton? The code would be cleaner (brett)
     */
    static {
        namespaces = new HashMap();
        mappings = new HashMap();

        // Add the "empty" namespace
        namespaces.put("", NO_NAMESPACE);
        mappings.put("", "");
        mappings.put("xml", "http://www.w3.org/XML/1998/namespace");
        namespaces.put("xml&http://www.w3.org/XML/1998/namespace", XML_NAMESPACE);
    }

    /**
     * <p>
     *  This will retrieve (if in existence) or create (if not) a 
     *  <code>Namespace</code> for the supplied prefix and URI.
     * </p><p>
     *  <b>Note</b>: Because the prefix of an XML namespace is both 
     *    non-normative and not an intrinsic part of the 
     *    <code>Namespace</code>, it is possible that the
     *    supplied <code>uri</code> is already attached to a 
     *    <code>Namespace</code>, and a different prefix is used by it. 
     *    In this case, the existing <code>Namespace</code>
     *    is returned, <i>with the different prefix</i>, and the supplied 
     *    <code>prefix</code>
     *    is ignored. This is perfectly legal XML namespace behavior.
     * </p>
     *
     * @param prefix <code>String</code> prefix to map to 
     *               <code>Namespace</code>.
     * @param uri <code>String</code> URI of new <code>Namespace</code>.
     * @return <code>Namespace</code> - ready to use namespace.
     */
    public static Namespace getNamespace(String prefix, String uri) {
        // Sanity checking
        if ((prefix == null) || (prefix.trim().equals(""))) {
            prefix = "";
        }
        if ((uri == null) || (uri.trim().equals(""))) {
            uri = "";
        }

        // Handle XML namespace
        if (prefix.equals("xml")) {
            return XML_NAMESPACE;
        }

        // Return existing namespace if found
        if (namespaces.containsKey(uri)) {
            return (Namespace)namespaces.get(uri);
        }

        // Ensure proper naming
        String reason;
        if ((reason = Verifier.checkNamespacePrefix(prefix)) != null) {
            throw new IllegalNameException(prefix, "Namespace prefix", reason);
        }
        if ((reason = Verifier.checkNamespaceURI(uri)) != null) {
            throw new IllegalNameException(uri, "Namespace URI", reason);
        }

        // Unless the "empty" Namespace (no prefix and no URI), require a URI
        if ((!prefix.equals("")) && (uri.equals(""))) {
            throw new IllegalNameException("", "namespace",
                "Namespace URIs must be non-null and non-empty Strings.");
        }

        // Return existing namespace if found
        if (namespaces.containsKey(uri)) {
            return (Namespace)namespaces.get(prefix + "&" + uri);
        }

        // Finally, store and return
        mappings.put(prefix, uri);
        Namespace ns = new Namespace(prefix, uri);
        namespaces.put(prefix + "&" + uri, ns);
        return ns;
    }

    /**
     * <p>
     *  This will retrieve (if in existence) or create (if not) a 
     *  <code>Namespace</code> for the supplied URI, and make it usable 
     *  as a default namespace, as no prefix is supplied.
     * </p>
     *
     * @param uri <code>String</code> URI of new <code>Namespace</code>.
     * @return <code>Namespace</code> - ready to use namespace.
     */
    public static Namespace getNamespace(String uri) {
        return getNamespace("", uri);
    }

    /**
     * <p>
     *  This will retrieve the  
     *  <code>Namespace</code> for the supplied prefix
     *  in the specified context. It returns null if the prefix
     *  is not mapped within that element.   
     * </p>
     *
     * @param prefix <code>String</code> prefix of the existing <code>Namespace</code>.
     * @param context <code>Element</code> against which this prefix is resolved.
     * @return <code>Namespace</code> - ready to use namespace.
     */
    public static Namespace getNamespace(String prefix, Element context) {
       
       if (context == null) {
           return null;
       }
       
       Namespace ns = context.getNamespace();
       if (ns.getPrefix().equals(prefix)) {
           return ns;
       }
       // check the Attributes for the requested prefix
       List attributes = context.getAttributes();
       Iterator iterator = attributes.iterator();
       while (iterator.hasNext()) {
           Attribute a = (Attribute) iterator.next();
           ns = a.getNamespace();
           if (ns.getPrefix().equals(prefix)) {
               return ns;
           }
       }
       
       // recurse through the ancestors
       return getNamespace(prefix, context.getParent()); 
        
    }

    /**
     * <p>
     *  This constructor handles creation of a <code>Namespace</code> object
     *    with a prefix and URI; it is intentionally left <code>private</code>
     *    so that it cannot be invoked by external programs/code.
     * </p>
     *
     * @param prefix <code>String</code> prefix to map to this namespace.
     * @param uri <code>String</code> URI for namespace.
     */
    private Namespace(String prefix, String uri) {
        this.prefix = prefix;
        this.uri = uri;
    }

    /**
     * <p>
     *  This returns the prefix mapped to this <code>Namespace</code>.
     * </p>
     *
     * @return <code>String</code> - prefix for this <code>Namespace</code>.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * <p>
     *  This returns the namespace URI for this <code>Namespace</code>.
     * </p>
     *
     * @return <code>String</code> - URI for this <code>Namespace</code>.
     */
    public String getURI() {
        return uri;
    }

    /**
     * <p>
     *  This tests for equality - Two <code>Namespaces</code>
     *  are equal if and only if their URIs are byte-for-byte equals
     *  and their prefixes are equal.
     * </p>
     *
     * @param ob <code>Object</code> to compare to this <code>Namespace</code>.
     * @return <code>boolean</code> - whether the supplied object is equal to
     *         this <code>Namespace</code>.
     */
    public boolean equals(Object ob) {
        if (ob == null) {
            return false;
        }

        if (ob instanceof Namespace) {
            Namespace ns = (Namespace)ob;
            // Compare URIs
            if (ns.getURI().equals(uri) && ns.getPrefix().equals(prefix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>
     *  This returns a <code>String</code> representation of this 
     *  <code>Namespace</code>, suitable for use in debugging.
     * </p>
     *
     * @return <code>String</code> - information about this instance.
     */
    public String toString() {
        return "[Namespace: prefix \"" + prefix + "\" is mapped to URI \"" + 
               uri + "\"]";
    }

    /**
     * <p>
     *  This returns a probably unique hash code for the <code>Namespace</code>.
     *  If two namespaces have the same URI, they are equal and have the same
     *  hash code, even if they have different prefixes.
     * </p>
     *
     * @return <code>int</code> - hash code for this <code>Namespace</code>.
     */
    public int hashCode() {
        // Since neither URI nor prefix are guaranteed to be unique, 
        // use the combination
        return (prefix+uri).hashCode();
    }

}
