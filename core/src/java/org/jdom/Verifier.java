/*--

 Copyright 2000 Brett McLaughlin & Jason Hunter. All rights reserved.

 Redistribution and use in source and binary forms, with or without modifica-
 tion, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice,
    this list of conditions, and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions, the disclaimer that follows these conditions,
    and/or other materials provided with the distribution.

 3. The names "JDOM" and "Java Document Object Model" must not be used to
    endorse or promote products derived from this software without prior
    written permission. For written permission, please contact
    license@jdom.org.

 4. Products derived from this software may not be called "JDOM", nor may
    "JDOM" appear in their name, without prior written permission from the
    JDOM Project Management (pm@jdom.org).

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 JDOM PROJECT  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLUDING, BUT
 NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 This software  consists of voluntary contributions made  by many individuals
 on  behalf of the Java Document Object Model Project and was originally
 created by Brett McLaughlin <brett@jdom.org> and
 Jason Hunter <jhunter@jdom.org>. For more  information on the JDOM
 Project, please see <http://www.jdom.org/>.

 */
package org.jdom;

/**
 * <p>
 * <code>Verifier</code> handles XML checks on names, data, and other
 *   verification tasks for JDOM.
 * </p>
 *
 * @author Brett McLaughlin
 * @author Elliotte Rusty Harold
 * @version 1.0
 */
public final class Verifier {

    /**
     * <p>
     *  Ensure instantation cannot occur.
     * </p>
     */
    private Verifier() { }

    /**
     * <p>
     *  This will check the supplied name to see if it valid for use as
     *    a JDOM <code>{@link Element}</code> name.
     * </p>
     *
     * @param name <code>String</code> name to check.
     * @return <code>String</code> - reason name is invalid, or
     *         <code>null</code> if name is OK.
     */
    public static final String checkElementName(String name) {
        // Check basic XML name rules first
        String reason;
        if ((reason = checkXMLName(name)) != null) {
            return reason;
        }

        // No colons allowed, since elements handle this internally
        if (name.indexOf(":") != -1) {
            return "Element names cannot contain colons.";
        }

        // If we got here, everything is OK
        return null;
    }

    /**
     * <p>
     *  This will check the supplied name to see if it valid for use as
     *    a JDOM <code>{@link Attribute}</code> name.
     * </p>
     *
     * @param name <code>String</code> name to check.
     * @return <code>String</code> - reason name is invalid, or
     *         <code>null</code> if name is OK.
     */
    public static final String checkAttributeName(String name) {
        // Check basic XML name rules first
        String reason;
        if ((reason = checkXMLName(name)) != null) {
            return reason;
        }

        // Allow xml:space and xml:lang as special cases
        if (name.equals("xml:space") ||
            name.equals("xml:lang")) {
            return null;
        }

        // Otherwise, no colons are allowed,
        // since attributes handle this internally
        if (name.indexOf(":") != -1) {
            return "Attribute names cannot contain colons.";
        }

        // If we got here, everything is OK
        return null;
    }

    /**
     * <p>
     *  This will check the supplied name to see if it valid for use as
     *    a JDOM <code>{@link Namespace}</code> prefix.
     * </p>
     *
     * @param prefix <code>String</code> prefix to check.
     * @return <code>String</code> - reason name is invalid, or
     *         <code>null</code> if name is OK.
     */
    public static final String checkNamespacePrefix(String prefix) {
        // Manually do rules, since URIs can be null or empty
        if ((prefix == null) || (prefix.equals(""))) {
          return null;
        }

        // Cannot start with a number
        char first = prefix.charAt(0);
        if (Character.isDigit(first)) {
            return "Namespace prefixes cannot begin with a number.";
        }
        // Cannot start with a $
        if (first == '$') {
            return "Namespace prefixes cannot begin with a dollar sign ($).";
        }
        // Cannot start with a _
        if (first == '-') {
            return "Namespace prefixes cannot begin with a hyphen (-).";
        }

        // Ensure valid content
        for (int i=0, len = prefix.length(); i<len; i++) {
            char c = prefix.charAt(i);
            if ((!Character.isLetterOrDigit(c))
                && (c != '-')
                && (c != '$')
                && (c != '_')) {
                return c + " is not allowed in Namespace prefixes.";
            }
        }

        // No colons allowed
        if (prefix.indexOf(":") != -1) {
            return "Namespace prefixes cannot contain colons.";
        }

        // If we got here, everything is OK
        return null;
    }

    /**
     * <p>
     *  This will check the supplied name to see if it valid for use as
     *    a JDOM <code>{@link Namespace}</code> URI.
     * </p>
     *
     * @param uri <code>String</code> URI to check.
     * @return <code>String</code> - reason name is invalid, or
     *         <code>null</code> if name is OK.
     */
    public static final String checkNamespaceURI(String uri) {
        // Manually do rules, since URIs can be null or empty
        if ((uri == null) || (uri.equals(""))) {
            return null;
        }

        // Cannot start with a number
        char first = uri.charAt(0);
        if (Character.isDigit(first)) {
            return "Namespace URIs cannot begin with a number.";
        }
        // Cannot start with a $
        if (first == '$') {
            return "Namespace URIs cannot begin with a dollar sign ($).";
        }
        // Cannot start with a _
        if (first == '-') {
            return "Namespace URIs cannot begin with a hyphen (-).";
        }

        // If we got here, everything is OK
        return null;
    }

    /**
     * <p>
     *  This will check the supplied name to see if it valid for use as
     *    a JDOM processing instruction target.
     * </p>
     *
     * @param target <code>String</code> target to check.
     * @return <code>String</code> - reason target is invalid, or
     *         <code>null</code> if name is OK.
     */
    public static final String checkProcessingInstructionTarget(String target) {
        // Check basic XML name rules first
        String reason;
        if ((reason = checkXMLName(target)) != null) {
            return reason;
        }

        // Cannot begin with 'xml' in any case
        if (target.equalsIgnoreCase("xml")) {
            return "Processing Instructions cannot have a target of " +
                   "\"xml\" any combination of case.";
        }

        // If we got here, everything is OK
        return null;
    }

    /**
     * <p>
     *  This will ensure that the data for a <code>{@link Comment}</code>
     *    is appropriate.
     * </p>
     *
     * @param data <code>String</code> data to check.
     * @return <code>String</code> - reason data is invalid, or
     *         <code>null</code> is name is OK.
     */
    public static final String checkCommentData(String data) {
        if (data.indexOf("--") != -1) {
            return "Comments cannot contain double hyphens (--).";
        }

        // If we got here, everything is OK
        return null;
    }

    /**
     * <p>
     *  This is a utility function for sharing the base process of checking
     *    any XML name.
     * </p>
     *
     * @param name <code>String</code> to check for XML name compliance.
     * @return <code>String</code> - reason the name is invalid, or
     *         <code>null</code> if OK.
     */
    private static String checkXMLName(String name) {
        // Cannot be empty or null
        if ((name == null) || (name.length() == 0) || (name.trim().equals(""))) {
            return "XML names cannot be null or empty";
        }

        // Cannot start with a number
        char first = name.charAt(0);
        if (Character.isDigit(first)) {
            return "XML names cannot begin with a number.";
        }
        // Cannot start with a $
        if (first == '$') {
            return "XML names cannot begin with a dollar sign ($).";
        }
        // Cannot start with a _
        if (first == '-') {
            return "XML names cannot begin with a hyphen (-).";
        }

        // Ensure valid content
        for (int i=0, len = name.length(); i<len; i++) {
            char c = name.charAt(i);
            if ((!Character.isLetterOrDigit(c))
                && (c != '-')
                && (c != '$')
		&& (c != '.')
                && (c != '_')) {
                return c + " is not allowed in XML names.";
            }
        }

        // We got here, so everything is OK
        return null;
    }

}
