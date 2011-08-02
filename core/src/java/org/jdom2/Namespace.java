/*-- 

 $Id: Namespace.java,v 1.44 2008/12/17 23:22:48 jhunter Exp $

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

import java.util.*;

/**
 * An XML namespace representation, as well as a factory for creating XML
 * namespace objects. Namespaces are not Serializable, however objects that use
 * namespaces have special logic to handle serialization manually. These classes
 * call the getNamespace() method on deserialization to ensure there is one
 * unique Namespace object for any unique prefix/uri pair.
 *
 * @version $Revision: 1.44 $, $Date: 2008/12/17 23:22:48 $
 * @author  Brett McLaughlin
 * @author  Elliotte Rusty Harold
 * @author  Jason Hunter
 * @author  Wesley Biggs
 */
public final class Namespace {

    // XXX May want to use weak references to keep the maps from growing 
    // large with extended use

    private static final String CVS_ID =
      "@(#) $RCSfile: Namespace.java,v $ $Revision: 1.44 $ $Date: 2008/12/17 23:22:48 $ $Name:  $";

    /** 
     * Factory list of namespaces. 
     * Keys are <i>prefix</i>&amp;<i>URI</i>. 
     * Values are Namespace objects 
     */
    private static HashMap namespaces;

    /** Define a <code>Namespace</code> for when <i>not</i> in a namespace */
    public static final Namespace NO_NAMESPACE = new Namespace("", "");

    /** Define a <code>Namespace</code> for the standard xml prefix. */
    public static final Namespace XML_NAMESPACE =
        new Namespace("xml", "http://www.w3.org/XML/1998/namespace");

    /** The prefix mapped to this namespace */
    private String prefix;

    /** The URI for this namespace */
    private String uri;

    /**
     * This static initializer acts as a factory contructor.
     * It sets up storage and required initial values.
     */
    static {
        namespaces = new HashMap(16);

        // Add the "empty" namespace
        namespaces.put(new NamespaceKey(NO_NAMESPACE), NO_NAMESPACE);
        namespaces.put(new NamespaceKey(XML_NAMESPACE), XML_NAMESPACE);
    }

    /**
     * This will retrieve (if in existence) or create (if not) a 
     * <code>Namespace</code> for the supplied prefix and URI.
     *
     * @param prefix <code>String</code> prefix to map to 
     *               <code>Namespace</code>.
     * @param uri <code>String</code> URI of new <code>Namespace</code>.
     * @return <code>Namespace</code> - ready to use namespace.
     * @throws IllegalNameException if the given prefix and uri make up
     *         an illegal namespace name.
     */
    public static Namespace getNamespace(String prefix, String uri) {
        // Sanity checking
        if ((prefix == null) || (prefix.trim().equals(""))) {
            // Short-cut out for common case of no namespace
            if ((uri == null) || (uri.trim().equals(""))) {
                return NO_NAMESPACE;
            }
            prefix = "";
        }
        else if ((uri == null) || (uri.trim().equals(""))) {
            uri = "";
        }

        // Return existing namespace if found. The preexisting namespaces
        // should all be legal. In other words, an illegal namespace won't
        // have been placed in this.  Thus we can do this test before
        // verifying the URI and prefix.
        NamespaceKey lookup = new NamespaceKey(prefix, uri);
        Namespace preexisting;
        synchronized (namespaces) {
            preexisting = (Namespace) namespaces.get(lookup);
        }
        if (preexisting != null) {
            return preexisting;
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
                "Namespace URIs must be non-null and non-empty Strings");
        }

        // Handle XML namespace mislabels. If the user requested the correct
        // namespace and prefix -- xml, http://www.w3.org/XML/1998/namespace
        // -- then it was already returned from the preexisting namespaces.
        // Thus any use of the xml prefix or the
        // http://www.w3.org/XML/1998/namespace URI at this point must be
        // incorrect. 
        if (prefix.equals("xml")) {
            throw new IllegalNameException(prefix, "Namespace prefix",
             "The xml prefix can only be bound to " +
             "http://www.w3.org/XML/1998/namespace");        
        }

        // The erratum to Namespaces in XML 1.0 that suggests this 
        // next check is controversial. Not everyone accepts it. 
        if (uri.equals("http://www.w3.org/XML/1998/namespace")) {
            throw new IllegalNameException(uri, "Namespace URI",
             "The http://www.w3.org/XML/1998/namespace must be bound to " +
             "the xml prefix.");        
        }

        // Finally, store and return
        Namespace ns = new Namespace(prefix, uri);
        synchronized (namespaces) {
            namespaces.put(lookup, ns);
        }
        return ns;
    }

    /**
     * This will retrieve (if in existence) or create (if not) a 
     * <code>Namespace</code> for the supplied URI, and make it usable 
     * as a default namespace, as no prefix is supplied.
     *
     * @param uri <code>String</code> URI of new <code>Namespace</code>.
     * @return <code>Namespace</code> - ready to use namespace.
     */
    public static Namespace getNamespace(String uri) {
        return getNamespace("", uri);
    }

    /**
     * This constructor handles creation of a <code>Namespace</code> object
     * with a prefix and URI; it is intentionally left <code>private</code>
     * so that it cannot be invoked by external programs/code.
     *
     * @param prefix <code>String</code> prefix to map to this namespace.
     * @param uri <code>String</code> URI for namespace.
     */
    private Namespace(String prefix, String uri) {
        this.prefix = prefix;
        this.uri = uri;
    }

    /**
     * This returns the prefix mapped to this <code>Namespace</code>.
     *
     * @return <code>String</code> - prefix for this <code>Namespace</code>.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * This returns the namespace URI for this <code>Namespace</code>.
     *
     * @return <code>String</code> - URI for this <code>Namespace</code>.
     */
    public String getURI() {
        return uri;
    }

    /**
     * This tests for equality - Two <code>Namespaces</code>
     * are equal if and only if their URIs are byte-for-byte equals.
     *
     * @param ob <code>Object</code> to compare to this <code>Namespace</code>.
     * @return <code>boolean</code> - whether the supplied object is equal to
     *         this <code>Namespace</code>.
     */
    public boolean equals(Object ob) {
        if (this == ob) {
            return true;
        }
        if (ob instanceof Namespace) {  // instanceof returns false if null
            return uri.equals(((Namespace)ob).uri);
        }
        return false;
    }

    /**
     * This returns a <code>String</code> representation of this 
     * <code>Namespace</code>, suitable for use in debugging.
     *
     * @return <code>String</code> - information about this instance.
     */
    public String toString() {
        return "[Namespace: prefix \"" + prefix + "\" is mapped to URI \"" + 
               uri + "\"]";
    }

    /**
     * This returns a probably unique hash code for the <code>Namespace</code>.
     * If two namespaces have the same URI, they are equal and have the same
     * hash code, even if they have different prefixes.
     *
     * @return <code>int</code> - hash code for this <code>Namespace</code>.
     */
    public int hashCode() {
        return uri.hashCode();
    }
}
