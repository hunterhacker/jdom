/*-- 

 $Id: Verifier.java,v 1.57 2009/07/23 05:54:23 jhunter Exp $

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
 * A utility class to handle well-formedness checks on names, data, and other
 * verification tasks for JDOM. The class is final and may not be subclassed.
 *
 * @version $Revision: 1.57 $, $Date: 2009/07/23 05:54:23 $
 * @author  Brett McLaughlin
 * @author  Elliotte Rusty Harold
 * @author  Jason Hunter
 * @author  Bradley S. Huffman
 */
final public class Verifier {

    private static final String CVS_ID = 
      "@(#) $RCSfile: Verifier.java,v $ $Revision: 1.57 $ $Date: 2009/07/23 05:54:23 $ $Name:  $";

    /**
     * Ensure instantation cannot occur.
     */
    private Verifier() { }

    /**
     * This will check the supplied name to see if it is legal for use as
     * a JDOM <code>{@link Element}</code> name.
     *
     * @param name <code>String</code> name to check.
     * @return <code>String</code> reason name is illegal, or
     *         <code>null</code> if name is OK.
     */
    public static String checkElementName(String name) {
        // Check basic XML name rules first
        String reason;
        if ((reason = checkXMLName(name)) != null) {
            return reason;
        }

        // No colons allowed, since elements handle this internally
        if (name.indexOf(":") != -1) {
            return "Element names cannot contain colons";
        }

        // If we got here, everything is OK
        return null;
    }

    /**
     * This will check the supplied name to see if it is legal for use as
     * a JDOM <code>{@link Attribute}</code> name.
     *
     * @param name <code>String</code> name to check.
     * @return <code>String</code> reason name is illegal, or
     *         <code>null</code> if name is OK.
     */
    public static String checkAttributeName(String name) {
        // Check basic XML name rules first
        String reason;
        if ((reason = checkXMLName(name)) != null) {
            return reason;
        }

        // No colons are allowed, since attributes handle this internally
        if (name.indexOf(":") != -1) {
            return "Attribute names cannot contain colons";
        }

        // Attribute names may not be xmlns since we do this internally too
        if (name.equals("xmlns")) {
            return "An Attribute name may not be \"xmlns\"; " +
                   "use the Namespace class to manage namespaces";
        }

        // If we got here, everything is OK
        return null;
    }
    
    /**
     * This will check the supplied string to see if it only contains
     * characters allowed by the XML 1.0 specification. The C0 controls
     * (e.g. null, vertical tab, formfeed, etc.) are specifically excluded
     * except for carriage return, linefeed, and the horizontal tab.
     * Surrogates are also excluded. 
     * <p>
     * This method is useful for checking element content and attribute
     * values. Note that characters 
     * like " and &lt; are allowed in attribute values and element content. 
     * They will simply be escaped as &quot; or &lt; 
     * when the value is serialized. 
     * </p>
     *
     * @param text <code>String</code> value to check.
     * @return <code>String</code> reason name is illegal, or
     *         <code>null</code> if name is OK.
     */
    public static String checkCharacterData(String text) {
        if (text == null) {
            return "A null is not a legal XML value";
        }

        // Do check
        for (int i = 0, len = text.length(); i<len; i++) {

            int ch = text.charAt(i);
            
            // Check if high part of a surrogate pair
            if (isHighSurrogate((char) ch)) {
                // Check if next char is the low-surrogate
                i++;
                if (i < len) {
                    char low = text.charAt(i);
                    if (!isLowSurrogate(low)) {
                        return "Illegal Surrogate Pair";
                    }
                    // It's a good pair, calculate the true value of
                    // the character to then fall thru to isXMLCharacter
                    ch = decodeSurrogatePair((char) ch, low);
                }
                else {
                    return "Surrogate Pair Truncated";
                }
            }

            if (!isXMLCharacter(ch)) {
                // Likely this character can't be easily displayed
                // because it's a control so we use it'd hexadecimal 
                // representation in the reason.
                return ("0x" + Integer.toHexString(ch) +
                        " is not a legal XML character");
            }
        }

        // If we got here, everything is OK
        return null;
    }

    /**
     * This will check the supplied data to see if it is legal for use as
     * JDOM <code>{@link CDATA}</code>.
     *
     * @param data <code>String</code> data to check.
     * @return <code>String</code> reason data is illegal, or
     *         <code>null</code> is name is OK.
     */
    public static String checkCDATASection(String data) {
        String reason = null;
        if ((reason = checkCharacterData(data)) != null) {
            return reason;
        }

        if (data.indexOf("]]>") != -1) {
            return "CDATA cannot internally contain a CDATA ending " +
                   "delimiter (]]>)";
        }

        // If we got here, everything is OK
        return null;
    }

    /**
     * This will check the supplied name to see if it is legal for use as
     * a JDOM <code>{@link Namespace}</code> prefix.
     *
     * @param prefix <code>String</code> prefix to check.
     * @return <code>String</code> reason name is illegal, or
     *         <code>null</code> if name is OK.
     */
    public static String checkNamespacePrefix(String prefix) {
        // Manually do rules, since URIs can be null or empty
        if ((prefix == null) || (prefix.equals(""))) {
            return null;
        }

        // Cannot start with a number
        char first = prefix.charAt(0);
        if (isXMLDigit(first)) {
            return "Namespace prefixes cannot begin with a number";
        }
        // Cannot start with a $
        if (first == '$') {
            return "Namespace prefixes cannot begin with a dollar sign ($)";
        }
        // Cannot start with a -
        if (first == '-') {
            return "Namespace prefixes cannot begin with a hyphen (-)";
        }
        // Cannot start with a .
        if (first == '.') {
            return "Namespace prefixes cannot begin with a period (.)";
        }
        // Cannot start with "xml" in any character case
        if (prefix.toLowerCase().startsWith("xml")) {
            return "Namespace prefixes cannot begin with " +
                   "\"xml\" in any combination of case";
        }

        // Ensure legal content
        for (int i=0, len = prefix.length(); i<len; i++) {
            char c = prefix.charAt(i);
            if (!isXMLNameCharacter(c)) {
                return "Namespace prefixes cannot contain the character \"" +
                        c + "\"";
            }
        }

        // No colons allowed
        if (prefix.indexOf(":") != -1) {
            return "Namespace prefixes cannot contain colons";
        }

        // If we got here, everything is OK
        return null;
    }

    /**
     * This will check the supplied name to see if it is legal for use as
     * a JDOM <code>{@link Namespace}</code> URI.
     *
     * @param uri <code>String</code> URI to check.
     * @return <code>String</code> reason name is illegal, or
     *         <code>null</code> if name is OK.
     */
    public static String checkNamespaceURI(String uri) {
        // Manually do rules, since URIs can be null or empty
        if ((uri == null) || (uri.equals(""))) {
            return null;
        }

        // Cannot start with a number
        char first = uri.charAt(0);
        if (Character.isDigit(first)) {
            return "Namespace URIs cannot begin with a number";
        }
        // Cannot start with a $
        if (first == '$') {
            return "Namespace URIs cannot begin with a dollar sign ($)";
        }
        // Cannot start with a -
        if (first == '-') {
            return "Namespace URIs cannot begin with a hyphen (-)";
        }

        // If we got here, everything is OK
        return null;
    }

    /**
     * Check if two namespaces collide.
     *
     * @param namespace <code>Namespace</code> to check.
     * @param other <code>Namespace</code> to check against.
     * @return <code>String</code> reason for collision, or
     *         <code>null</code> if no collision.
     */
    public static String checkNamespaceCollision(Namespace namespace,
                                                 Namespace other) {
        String p1,p2,u1,u2,reason;

        reason = null;
        p1 = namespace.getPrefix();
        u1 = namespace.getURI();
        p2 = other.getPrefix();
        u2 = other.getURI();
        if (p1.equals(p2) && !u1.equals(u2)) {
            reason = "The namespace prefix \"" + p1 + "\" collides";
        }
        return reason;
    }

    /**
     * Check if <code>{@link Attribute}</code>'s namespace collides with a 
     * <code>{@link Element}</code>'s namespace.
     *
     * @param attribute <code>Attribute</code> to check.
     * @param element <code>Element</code> to check against.
     * @return <code>String</code> reason for collision, or
     *         <code>null</code> if no collision.
     */
    public static String checkNamespaceCollision(Attribute attribute,
                                                 Element element) {
        Namespace namespace = attribute.getNamespace();
        String prefix = namespace.getPrefix();
        if ("".equals(prefix)) {
            return null;
        }

        return checkNamespaceCollision(namespace, element);
    }

    /**
     * Check if a <code>{@link Namespace}</code> collides with a
     * <code>{@link Element}</code>'s namespace.
     *
     * @param namespace <code>Namespace</code> to check.
     * @param element <code>Element</code> to check against.
     * @return <code>String</code> reason for collision, or
     *         <code>null</code> if no collision.
     */
    public static String checkNamespaceCollision(Namespace namespace,
                                                 Element element) {
        String reason = checkNamespaceCollision(namespace,
                                                element.getNamespace());
        if (reason != null) {
            return reason + " with the element namespace prefix";
        }

        reason = checkNamespaceCollision(namespace,
                                         element.getAdditionalNamespaces());
        if (reason != null) {
            return reason;
        }

        reason = checkNamespaceCollision(namespace, element.getAttributes());
        if (reason != null) {
            return reason;
        }

        return null;
    }

    /**
     * Check if a <code>{@link Namespace}</code> collides with a
     * <code>{@link Attribute}</code>'s namespace.
     *
     * @param namespace <code>Namespace</code> to check.
     * @param attribute <code>Attribute</code> to check against.
     * @return <code>String</code> reason for collision, or
     *         <code>null</code> if no collision.
     */
    public static String checkNamespaceCollision(Namespace namespace,
                                                 Attribute attribute) {
        String reason = null;
        if (!attribute.getNamespace().equals(Namespace.NO_NAMESPACE)) {
            reason = checkNamespaceCollision(namespace,
                                                attribute.getNamespace());
            if (reason != null) {
                reason += " with an attribute namespace prefix on the element";
            }
        }
        return reason;
    }

    /**
     * Check if a <code>{@link Namespace}</code> collides with any namespace
     * from a list of objects.
     *
     * @param namespace <code>Namespace</code> to check.
     * @param list <code>List</code> to check against.
     * @return <code>String</code> reason for collision, or
     *         <code>null</code> if no collision.
     */
    public static String checkNamespaceCollision(Namespace namespace,
                                                 List list) {
        if (list == null) {
            return null;
        }

        String reason = null;
        Iterator i = list.iterator();
        while ((reason == null) && i.hasNext()) {
            Object obj = i.next();
            if (obj instanceof Attribute) {
                reason = checkNamespaceCollision(namespace, (Attribute) obj);
            }
            else if (obj instanceof Element) {
                reason = checkNamespaceCollision(namespace, (Element) obj);
            }
            else if (obj instanceof Namespace) {
                reason = checkNamespaceCollision(namespace, (Namespace) obj);
                if (reason != null) {
                    reason += " with an additional namespace declared" +
                              " by the element";
                }
            }
        }
        return reason;
    }

    /**
     * This will check the supplied data to see if it is legal for use as
     * a JDOM <code>{@link ProcessingInstruction}</code> target.
     *
     * @param target <code>String</code> target to check.
     * @return <code>String</code> reason target is illegal, or
     *         <code>null</code> if target is OK.
     */
    public static String checkProcessingInstructionTarget(String target) {
        // Check basic XML name rules first
        String reason;
        if ((reason = checkXMLName(target)) != null) {
            return reason;
        }

        // No colons allowed, per Namespace Specification Section 6
        if (target.indexOf(":") != -1) {
            return "Processing instruction targets cannot contain colons";
        }

        // Cannot begin with 'xml' in any case
        if (target.equalsIgnoreCase("xml")) {
            return "Processing instructions cannot have a target of " +
                   "\"xml\" in any combination of case. (Note that the " +
                   "\"<?xml ... ?>\" declaration at the beginning of a " +
                   "document is not a processing instruction and should not " + 
                   "be added as one; it is written automatically during " +
                   "output, e.g. by XMLOutputter.)";
        }

        // If we got here, everything is OK
        return null;
    }

   /**
     * This will check the supplied data to see if it is legal for use as
     * <code>{@link ProcessingInstruction}</code> data. Besides checking that
     * all the characters are allowed in XML, this also checks
     * that the data does not contain the PI end-string "?&gt;".
     *
     * @param data <code>String</code> data to check.
     * @return <code>String</code> reason data is illegal, or
     *         <code>null</code> if data is OK.
     */
    public static String checkProcessingInstructionData(String data) {
        // Check basic XML name rules first
        String reason = checkCharacterData(data);

        if (reason == null) {
            if (data.indexOf("?>") >= 0) {
                return "Processing instructions cannot contain " +
                       "the string \"?>\"";
            }
        }

        return reason;
    }

    /**
     * This will check the supplied data to see if it is legal for use as
     * JDOM <code>{@link Comment}</code> data.
     *
     * @param data <code>String</code> data to check.
     * @return <code>String</code> reason data is illegal, or
     *         <code>null</code> if data is OK.
     */
    public static String checkCommentData(String data) {
        String reason = null;
        if ((reason = checkCharacterData(data)) != null) {
            return reason;
        }

        if (data.indexOf("--") != -1) {
            return "Comments cannot contain double hyphens (--)";
        }
        if (data.endsWith("-")) {
            return "Comment data cannot end with a hyphen.";
        }

        // If we got here, everything is OK
        return null;
    }
    /**
     * This is a utility function to decode a non-BMP 
     * UTF-16 surrogate pair.
     * @param high high 16 bits
     * @param low low 16 bits
     * @return decoded character
     */
    public static int decodeSurrogatePair(char high, char low) {
    	return 0x10000 + (high - 0xD800) * 0x400 + (low - 0xDC00);
    }

    // [13] PubidChar ::= #x20 | #xD | #xA | [a-zA-Z0-9] |
    // [-'()+,./:=?;*#@$_%]
    public static boolean isXMLPublicIDCharacter(char c) {

        if (c >= 'a' && c <= 'z') return true;
        if (c >= '?' && c <= 'Z') return true;
        if (c >= '\'' && c <= ';') return true;

        if (c == ' ') return true;
        if (c == '!') return true;
        if (c == '=') return true;
        if (c == '#') return true;
        if (c == '$') return true;
        if (c == '_') return true;
        if (c == '%') return true;
        if (c == '\n') return true;
        if (c == '\r') return true;
        if (c == '\t') return true;

        return false;
    }

    /**
     * This will ensure that the data for a public identifier
     * is legal.
     *
     * @param publicID <code>String</code> public ID to check.
     * @return <code>String</code> reason public ID is illegal, or
     *         <code>null</code> if public ID is OK.
     */
    public static String checkPublicID(String publicID) {
        String reason = null;

        if (publicID == null) return null;
        // This indicates there is no public ID

        for (int i = 0; i < publicID.length(); i++) {
          char c = publicID.charAt(i);
          if (!isXMLPublicIDCharacter(c)) {
            reason = c + " is not a legal character in public IDs";
            break;
          }
        }

        return reason;
    }


    /**
     * This will ensure that the data for a system literal
     * is legal.
     *
     * @param systemLiteral <code>String</code> system literal to check.
     * @return <code>String</code> reason system literal is illegal, or
     *         <code>null</code> if system literal is OK.
     */
    public static String checkSystemLiteral(String systemLiteral) {
        String reason = null;

        if (systemLiteral == null) return null;
        // This indicates there is no system ID

        if (systemLiteral.indexOf('\'') != -1
          && systemLiteral.indexOf('"') != -1) {
            reason =
             "System literals cannot simultaneously contain both single and double quotes.";
        }
        else {
          reason = checkCharacterData(systemLiteral);
        }

        return reason;
    }

    /**
     * This is a utility function for sharing the base process of checking
     * any XML name.
     *
     * @param name <code>String</code> to check for XML name compliance.
     * @return <code>String</code> reason the name is illegal, or
     *         <code>null</code> if OK.
     */
    public static String checkXMLName(String name) {
        // Cannot be empty or null
        if ((name == null) || (name.length() == 0) 
                           || (name.trim().equals(""))) {
            return "XML names cannot be null or empty";
        }

      
        // Cannot start with a number
        char first = name.charAt(0);
        if (!isXMLNameStartCharacter(first)) {
            return "XML names cannot begin with the character \"" + 
                   first + "\"";
        }
        // Ensure legal content for non-first chars
        for (int i=1, len = name.length(); i<len; i++) {
            char c = name.charAt(i);
            if (!isXMLNameCharacter(c)) {
                return "XML names cannot contain the character \"" + c + "\"";
            }
        }

        // We got here, so everything is OK
        return null;
    }

   /**
     * <p>
     * Checks a string to see if it is a legal RFC 2396 URI.
     * Both absolute and relative URIs are supported.
     * </p>
     *
     * @param uri <code>String</code> to check.
     * @return <code>String</code> reason the URI is illegal, or
     *         <code>null</code> if OK.
     */
    public static String checkURI(String uri) {
        // URIs can be null or empty
        if ((uri == null) || (uri.equals(""))) {
            return null;
        }

        for (int i = 0; i < uri.length(); i++) {
            char test = uri.charAt(i);
            if (!isURICharacter(test)) {
                String msgNumber = "0x" + Integer.toHexString(test);
                if (test <= 0x09) msgNumber = "0x0" + Integer.toHexString(test);
                return "URIs cannot contain " + msgNumber;
            } // end if
            if (test == '%') { // must be followed by two hexadecimal digits
                   try {
                       char firstDigit = uri.charAt(i+1);
                       char secondDigit = uri.charAt(i+2);
                       if (!isHexDigit(firstDigit) ||
                           !isHexDigit(secondDigit)) {
                           return "Percent signs in URIs must be followed by "
                            + "exactly two hexadecimal digits.";
                       }

                   }
                   catch (StringIndexOutOfBoundsException e) {
                       return "Percent signs in URIs must be followed by "
                        + "exactly two hexadecimal digits.";
                   }
            }
        } // end for

        // If we got here, everything is OK
        return null;
    }

   /**
     * <p>
     * This is a utility function for determining whether a specified
     * Unicode character is a hexadecimal digit as defined in RFC 2396;
     * that is, one of the ASCII characters 0-9, a-f, or A-F.
     * </p>
     *
     * @param c  to check for hex digit.
     * @return true if it's allowed, false otherwise.
     */
    public static boolean isHexDigit(char c) {

    // I suspect most characters passed to this method will be
    // correct hexadecimal digits, so I test for the true cases
    // first. If this proves to be a performance bottleneck
    // a switch statement or lookup table
    // might optimize this.
        if (c >= '0' && c <= '9') return true;
        if (c >= 'A' && c <= 'F') return true;
        if (c >= 'a' && c <= 'f') return true;

        return false;
    }
    
    /**
     * This is a function for determining whether the
     * specified character is the high 16 bits in a 
     * UTF-16 surrogate pair.
     * @param ch character to check
     * @return true if the character is a high surrogate, false otherwise
     */
    public static boolean isHighSurrogate(char ch) {
    	return (ch >= 0xD800 && ch <= 0xDBFF);
    }
    
    /**
     * This is a function for determining whether the 
     * specified character is the low 16 bits in a 
     * UTF-16 surrogate pair.
     * @param ch character to check
     * @return true if the character is a low surrogate, false otherwise.
     */
    public static boolean isLowSurrogate(char ch) {
    	return (ch >= 0xDC00 && ch <= 0xDFFF);
    }

    /**
     * <p>
     * This is a utility function for determining whether
     * a specified Unicode character is legal in URI references
     * as determined by RFC 2396.
     * </p>
     *
     * @param c <code>char</code> to check for URI reference compliance.
     * @return true if it's allowed, false otherwise.
     */
    public static boolean isURICharacter(char c) {
        if (c >= 'a' && c <= 'z') return true;
        if (c >= 'A' && c <= 'Z') return true;
        if (c >= '0' && c <= '9') return true;
        if (c == '/') return true;
        if (c == '-') return true;
        if (c == '.') return true;
        if (c == '?') return true;
        if (c == ':') return true;
        if (c == '@') return true;
        if (c == '&') return true;
        if (c == '=') return true;
        if (c == '+') return true;
        if (c == '$') return true;
        if (c == ',') return true;
        if (c == '%') return true;

        if (c == '_') return true;
        if (c == '!') return true;
        if (c == '~') return true;
        if (c == '*') return true;
        if (c == '\'') return true;
        if (c == '(') return true;
        if (c == ')') return true;
        return false;
    }
    
    /**
     * This is a utility function for determining whether a specified 
     * character is a character according to production 2 of the 
     * XML 1.0 specification.
     *
     * @param c <code>char</code> to check for XML compliance
     * @return <code>boolean</code> true if it's a character, 
     *                                false otherwise
     */
    public static boolean isXMLCharacter(int c) {
    
        if (c == '\n') return true;
        if (c == '\r') return true;
        if (c == '\t') return true;
        
        if (c < 0x20) return false;  if (c <= 0xD7FF) return true;
        if (c < 0xE000) return false;  if (c <= 0xFFFD) return true;
        if (c < 0x10000) return false;  if (c <= 0x10FFFF) return true;
        
        return false;
    }


    /**
     * This is a utility function for determining whether a specified 
     * character is a name character according to production 4 of the 
     * XML 1.0 specification.
     *
     * @param c <code>char</code> to check for XML name compliance.
     * @return <code>boolean</code> true if it's a name character, 
     *                                false otherwise.
     */
    public static boolean isXMLNameCharacter(char c) {
    
      return (isXMLLetter(c) || isXMLDigit(c) || c == '.' || c == '-' 
                             || c == '_' || c == ':' || isXMLCombiningChar(c) 
                             || isXMLExtender(c));
    }

    /**
     * This is a utility function for determining whether a specified 
     * character is a legal name start character according to production 5
     * of the XML 1.0 specification. This production does allow names
     * to begin with colons which the Namespaces in XML Recommendation
     * disallows. 
     *
     * @param c <code>char</code> to check for XML name start compliance.
     * @return <code>boolean</code> true if it's a name start character, 
     *                                false otherwise.
     */
    public static boolean isXMLNameStartCharacter(char c) {
    
      return (isXMLLetter(c) || c == '_' || c ==':');
    
    }

    /**
     * This is a utility function for determining whether a specified 
     * character is a letter or digit according to productions 84 and 88
     * of the XML 1.0 specification.
     *
     * @param c <code>char</code> to check.
     * @return <code>boolean</code> true if it's letter or digit, 
     *                                false otherwise.
     */
    public static boolean isXMLLetterOrDigit(char c) {
    
      return (isXMLLetter(c) || isXMLDigit(c));
    
    }

    /**
     * This is a utility function for determining whether a specified character
     * is a letter according to production 84 of the XML 1.0 specification.
     *
     * @param c <code>char</code> to check for XML name compliance.
     * @return <code>String</code> true if it's a letter, false otherwise.
     */
    public static boolean isXMLLetter(char c) {
        // Note that order is very important here.  The search proceeds 
        // from lowest to highest values, so that no searching occurs 
        // above the character's value.  BTW, the first line is equivalent to:
        // if (c >= 0x0041 && c <= 0x005A) return true;

        if (c < 0x0041) return false;  if (c <= 0x005a) return true;
        if (c < 0x0061) return false;  if (c <= 0x007A) return true;
        if (c < 0x00C0) return false;  if (c <= 0x00D6) return true;
        if (c < 0x00D8) return false;  if (c <= 0x00F6) return true;
        if (c < 0x00F8) return false;  if (c <= 0x00FF) return true;
        if (c < 0x0100) return false;  if (c <= 0x0131) return true;
        if (c < 0x0134) return false;  if (c <= 0x013E) return true;
        if (c < 0x0141) return false;  if (c <= 0x0148) return true;
        if (c < 0x014A) return false;  if (c <= 0x017E) return true;
        if (c < 0x0180) return false;  if (c <= 0x01C3) return true;
        if (c < 0x01CD) return false;  if (c <= 0x01F0) return true;
        if (c < 0x01F4) return false;  if (c <= 0x01F5) return true;
        if (c < 0x01FA) return false;  if (c <= 0x0217) return true;
        if (c < 0x0250) return false;  if (c <= 0x02A8) return true;
        if (c < 0x02BB) return false;  if (c <= 0x02C1) return true;
        if (c == 0x0386) return true;
        if (c < 0x0388) return false;  if (c <= 0x038A) return true;
        if (c == 0x038C) return true;
        if (c < 0x038E) return false;  if (c <= 0x03A1) return true;
        if (c < 0x03A3) return false;  if (c <= 0x03CE) return true;
        if (c < 0x03D0) return false;  if (c <= 0x03D6) return true;
        if (c == 0x03DA) return true;
        if (c == 0x03DC) return true;
        if (c == 0x03DE) return true;
        if (c == 0x03E0) return true;
        if (c < 0x03E2) return false;  if (c <= 0x03F3) return true;
        if (c < 0x0401) return false;  if (c <= 0x040C) return true;
        if (c < 0x040E) return false;  if (c <= 0x044F) return true;
        if (c < 0x0451) return false;  if (c <= 0x045C) return true;
        if (c < 0x045E) return false;  if (c <= 0x0481) return true;
        if (c < 0x0490) return false;  if (c <= 0x04C4) return true;
        if (c < 0x04C7) return false;  if (c <= 0x04C8) return true;
        if (c < 0x04CB) return false;  if (c <= 0x04CC) return true;
        if (c < 0x04D0) return false;  if (c <= 0x04EB) return true;
        if (c < 0x04EE) return false;  if (c <= 0x04F5) return true;
        if (c < 0x04F8) return false;  if (c <= 0x04F9) return true;
        if (c < 0x0531) return false;  if (c <= 0x0556) return true;
        if (c == 0x0559) return true;
        if (c < 0x0561) return false;  if (c <= 0x0586) return true;
        if (c < 0x05D0) return false;  if (c <= 0x05EA) return true;
        if (c < 0x05F0) return false;  if (c <= 0x05F2) return true;
        if (c < 0x0621) return false;  if (c <= 0x063A) return true;
        if (c < 0x0641) return false;  if (c <= 0x064A) return true;
        if (c < 0x0671) return false;  if (c <= 0x06B7) return true;
        if (c < 0x06BA) return false;  if (c <= 0x06BE) return true;
        if (c < 0x06C0) return false;  if (c <= 0x06CE) return true;
        if (c < 0x06D0) return false;  if (c <= 0x06D3) return true;
        if (c == 0x06D5) return true;
        if (c < 0x06E5) return false;  if (c <= 0x06E6) return true;
        if (c < 0x0905) return false;  if (c <= 0x0939) return true;
        if (c == 0x093D) return true;
        if (c < 0x0958) return false;  if (c <= 0x0961) return true;
        if (c < 0x0985) return false;  if (c <= 0x098C) return true;
        if (c < 0x098F) return false;  if (c <= 0x0990) return true;
        if (c < 0x0993) return false;  if (c <= 0x09A8) return true;
        if (c < 0x09AA) return false;  if (c <= 0x09B0) return true;
        if (c == 0x09B2) return true;
        if (c < 0x09B6) return false;  if (c <= 0x09B9) return true;
        if (c < 0x09DC) return false;  if (c <= 0x09DD) return true;
        if (c < 0x09DF) return false;  if (c <= 0x09E1) return true;
        if (c < 0x09F0) return false;  if (c <= 0x09F1) return true;
        if (c < 0x0A05) return false;  if (c <= 0x0A0A) return true;
        if (c < 0x0A0F) return false;  if (c <= 0x0A10) return true;
        if (c < 0x0A13) return false;  if (c <= 0x0A28) return true;
        if (c < 0x0A2A) return false;  if (c <= 0x0A30) return true;
        if (c < 0x0A32) return false;  if (c <= 0x0A33) return true;
        if (c < 0x0A35) return false;  if (c <= 0x0A36) return true;
        if (c < 0x0A38) return false;  if (c <= 0x0A39) return true;
        if (c < 0x0A59) return false;  if (c <= 0x0A5C) return true;
        if (c == 0x0A5E) return true;
        if (c < 0x0A72) return false;  if (c <= 0x0A74) return true;
        if (c < 0x0A85) return false;  if (c <= 0x0A8B) return true;
        if (c == 0x0A8D) return true;
        if (c < 0x0A8F) return false;  if (c <= 0x0A91) return true;
        if (c < 0x0A93) return false;  if (c <= 0x0AA8) return true;
        if (c < 0x0AAA) return false;  if (c <= 0x0AB0) return true;
        if (c < 0x0AB2) return false;  if (c <= 0x0AB3) return true;
        if (c < 0x0AB5) return false;  if (c <= 0x0AB9) return true;
        if (c == 0x0ABD) return true;
        if (c == 0x0AE0) return true;
        if (c < 0x0B05) return false;  if (c <= 0x0B0C) return true;
        if (c < 0x0B0F) return false;  if (c <= 0x0B10) return true;
        if (c < 0x0B13) return false;  if (c <= 0x0B28) return true;
        if (c < 0x0B2A) return false;  if (c <= 0x0B30) return true;
        if (c < 0x0B32) return false;  if (c <= 0x0B33) return true;
        if (c < 0x0B36) return false;  if (c <= 0x0B39) return true;
        if (c == 0x0B3D) return true;
        if (c < 0x0B5C) return false;  if (c <= 0x0B5D) return true;
        if (c < 0x0B5F) return false;  if (c <= 0x0B61) return true;
        if (c < 0x0B85) return false;  if (c <= 0x0B8A) return true;
        if (c < 0x0B8E) return false;  if (c <= 0x0B90) return true;
        if (c < 0x0B92) return false;  if (c <= 0x0B95) return true;
        if (c < 0x0B99) return false;  if (c <= 0x0B9A) return true;
        if (c == 0x0B9C) return true;
        if (c < 0x0B9E) return false;  if (c <= 0x0B9F) return true;
        if (c < 0x0BA3) return false;  if (c <= 0x0BA4) return true;
        if (c < 0x0BA8) return false;  if (c <= 0x0BAA) return true;
        if (c < 0x0BAE) return false;  if (c <= 0x0BB5) return true;
        if (c < 0x0BB7) return false;  if (c <= 0x0BB9) return true;
        if (c < 0x0C05) return false;  if (c <= 0x0C0C) return true;
        if (c < 0x0C0E) return false;  if (c <= 0x0C10) return true;
        if (c < 0x0C12) return false;  if (c <= 0x0C28) return true;
        if (c < 0x0C2A) return false;  if (c <= 0x0C33) return true;
        if (c < 0x0C35) return false;  if (c <= 0x0C39) return true;
        if (c < 0x0C60) return false;  if (c <= 0x0C61) return true;
        if (c < 0x0C85) return false;  if (c <= 0x0C8C) return true;
        if (c < 0x0C8E) return false;  if (c <= 0x0C90) return true;
        if (c < 0x0C92) return false;  if (c <= 0x0CA8) return true;
        if (c < 0x0CAA) return false;  if (c <= 0x0CB3) return true;
        if (c < 0x0CB5) return false;  if (c <= 0x0CB9) return true;
        if (c == 0x0CDE) return true;
        if (c < 0x0CE0) return false;  if (c <= 0x0CE1) return true;
        if (c < 0x0D05) return false;  if (c <= 0x0D0C) return true;
        if (c < 0x0D0E) return false;  if (c <= 0x0D10) return true;
        if (c < 0x0D12) return false;  if (c <= 0x0D28) return true;
        if (c < 0x0D2A) return false;  if (c <= 0x0D39) return true;
        if (c < 0x0D60) return false;  if (c <= 0x0D61) return true;
        if (c < 0x0E01) return false;  if (c <= 0x0E2E) return true;
        if (c == 0x0E30) return true;
        if (c < 0x0E32) return false;  if (c <= 0x0E33) return true;
        if (c < 0x0E40) return false;  if (c <= 0x0E45) return true;
        if (c < 0x0E81) return false;  if (c <= 0x0E82) return true;
        if (c == 0x0E84) return true;
        if (c < 0x0E87) return false;  if (c <= 0x0E88) return true;
        if (c == 0x0E8A) return true;
        if (c == 0x0E8D) return true;
        if (c < 0x0E94) return false;  if (c <= 0x0E97) return true;
        if (c < 0x0E99) return false;  if (c <= 0x0E9F) return true;
        if (c < 0x0EA1) return false;  if (c <= 0x0EA3) return true;
        if (c == 0x0EA5) return true;
        if (c == 0x0EA7) return true;
        if (c < 0x0EAA) return false;  if (c <= 0x0EAB) return true;
        if (c < 0x0EAD) return false;  if (c <= 0x0EAE) return true;
        if (c == 0x0EB0) return true;
        if (c < 0x0EB2) return false;  if (c <= 0x0EB3) return true;
        if (c == 0x0EBD) return true;
        if (c < 0x0EC0) return false;  if (c <= 0x0EC4) return true;
        if (c < 0x0F40) return false;  if (c <= 0x0F47) return true;
        if (c < 0x0F49) return false;  if (c <= 0x0F69) return true;
        if (c < 0x10A0) return false;  if (c <= 0x10C5) return true;
        if (c < 0x10D0) return false;  if (c <= 0x10F6) return true;
        if (c == 0x1100) return true;
        if (c < 0x1102) return false;  if (c <= 0x1103) return true;
        if (c < 0x1105) return false;  if (c <= 0x1107) return true;
        if (c == 0x1109) return true;
        if (c < 0x110B) return false;  if (c <= 0x110C) return true;
        if (c < 0x110E) return false;  if (c <= 0x1112) return true;
        if (c == 0x113C) return true;
        if (c == 0x113E) return true;
        if (c == 0x1140) return true;
        if (c == 0x114C) return true;
        if (c == 0x114E) return true;
        if (c == 0x1150) return true;
        if (c < 0x1154) return false;  if (c <= 0x1155) return true;
        if (c == 0x1159) return true;
        if (c < 0x115F) return false;  if (c <= 0x1161) return true;
        if (c == 0x1163) return true;
        if (c == 0x1165) return true;
        if (c == 0x1167) return true;
        if (c == 0x1169) return true;
        if (c < 0x116D) return false;  if (c <= 0x116E) return true;
        if (c < 0x1172) return false;  if (c <= 0x1173) return true;
        if (c == 0x1175) return true;
        if (c == 0x119E) return true;
        if (c == 0x11A8) return true;
        if (c == 0x11AB) return true;
        if (c < 0x11AE) return false;  if (c <= 0x11AF) return true;
        if (c < 0x11B7) return false;  if (c <= 0x11B8) return true;
        if (c == 0x11BA) return true;
        if (c < 0x11BC) return false;  if (c <= 0x11C2) return true;
        if (c == 0x11EB) return true;
        if (c == 0x11F0) return true;
        if (c == 0x11F9) return true;
        if (c < 0x1E00) return false;  if (c <= 0x1E9B) return true;
        if (c < 0x1EA0) return false;  if (c <= 0x1EF9) return true;
        if (c < 0x1F00) return false;  if (c <= 0x1F15) return true;
        if (c < 0x1F18) return false;  if (c <= 0x1F1D) return true;
        if (c < 0x1F20) return false;  if (c <= 0x1F45) return true;
        if (c < 0x1F48) return false;  if (c <= 0x1F4D) return true;
        if (c < 0x1F50) return false;  if (c <= 0x1F57) return true;
        if (c == 0x1F59) return true;
        if (c == 0x1F5B) return true;
        if (c == 0x1F5D) return true;
        if (c < 0x1F5F) return false;  if (c <= 0x1F7D) return true;
        if (c < 0x1F80) return false;  if (c <= 0x1FB4) return true;
        if (c < 0x1FB6) return false;  if (c <= 0x1FBC) return true;
        if (c == 0x1FBE) return true;
        if (c < 0x1FC2) return false;  if (c <= 0x1FC4) return true;
        if (c < 0x1FC6) return false;  if (c <= 0x1FCC) return true;
        if (c < 0x1FD0) return false;  if (c <= 0x1FD3) return true;
        if (c < 0x1FD6) return false;  if (c <= 0x1FDB) return true;
        if (c < 0x1FE0) return false;  if (c <= 0x1FEC) return true;
        if (c < 0x1FF2) return false;  if (c <= 0x1FF4) return true;
        if (c < 0x1FF6) return false;  if (c <= 0x1FFC) return true;
        if (c == 0x2126) return true;
        if (c < 0x212A) return false;  if (c <= 0x212B) return true;
        if (c == 0x212E) return true;
        if (c < 0x2180) return false;  if (c <= 0x2182) return true;
        if (c == 0x3007) return true;                          // ideographic
        if (c < 0x3021) return false;  if (c <= 0x3029) return true;  // ideo
        if (c < 0x3041) return false;  if (c <= 0x3094) return true;
        if (c < 0x30A1) return false;  if (c <= 0x30FA) return true;
        if (c < 0x3105) return false;  if (c <= 0x312C) return true;
        if (c < 0x4E00) return false;  if (c <= 0x9FA5) return true;  // ideo
        if (c < 0xAC00) return false;  if (c <= 0xD7A3) return true;
      
        return false;
        
    }

    /**
     * This is a utility function for determining whether a specified character
     * is a combining character according to production 87
     * of the XML 1.0 specification.
     *
     * @param c <code>char</code> to check.
     * @return <code>boolean</code> true if it's a combining character,
     *         false otherwise.
     */
    public static boolean isXMLCombiningChar(char c) {
        // CombiningChar
        if (c < 0x0300) return false;  if (c <= 0x0345) return true;
        if (c < 0x0360) return false;  if (c <= 0x0361) return true;
        if (c < 0x0483) return false;  if (c <= 0x0486) return true;
        if (c < 0x0591) return false;  if (c <= 0x05A1) return true;
                                       
        if (c < 0x05A3) return false;  if (c <= 0x05B9) return true;
        if (c < 0x05BB) return false;  if (c <= 0x05BD) return true;
        if (c == 0x05BF) return true;
        if (c < 0x05C1) return false;  if (c <= 0x05C2) return true;
                                       
        if (c == 0x05C4) return true;
        if (c < 0x064B) return false;  if (c <= 0x0652) return true;
        if (c == 0x0670) return true;
        if (c < 0x06D6) return false;  if (c <= 0x06DC) return true;
                                       
        if (c < 0x06DD) return false;  if (c <= 0x06DF) return true;
        if (c < 0x06E0) return false;  if (c <= 0x06E4) return true;
        if (c < 0x06E7) return false;  if (c <= 0x06E8) return true;
                                       
        if (c < 0x06EA) return false;  if (c <= 0x06ED) return true;
        if (c < 0x0901) return false;  if (c <= 0x0903) return true;
        if (c == 0x093C) return true;
        if (c < 0x093E) return false;  if (c <= 0x094C) return true;
                                       
        if (c == 0x094D) return true;
        if (c < 0x0951) return false;  if (c <= 0x0954) return true;
        if (c < 0x0962) return false;  if (c <= 0x0963) return true;
        if (c < 0x0981) return false;  if (c <= 0x0983) return true;
                                       
        if (c == 0x09BC) return true;
        if (c == 0x09BE) return true;
        if (c == 0x09BF) return true;
        if (c < 0x09C0) return false;  if (c <= 0x09C4) return true;
        if (c < 0x09C7) return false;  if (c <= 0x09C8) return true;
                                       
        if (c < 0x09CB) return false;  if (c <= 0x09CD) return true;
        if (c == 0x09D7) return true;
        if (c < 0x09E2) return false;  if (c <= 0x09E3) return true;
        if (c == 0x0A02) return true;
        if (c == 0x0A3C) return true;
                                       
        if (c == 0x0A3E) return true;
        if (c == 0x0A3F) return true;
        if (c < 0x0A40) return false;  if (c <= 0x0A42) return true;
        if (c < 0x0A47) return false;  if (c <= 0x0A48) return true;
                                       
        if (c < 0x0A4B) return false;  if (c <= 0x0A4D) return true;
        if (c < 0x0A70) return false;  if (c <= 0x0A71) return true;
        if (c < 0x0A81) return false;  if (c <= 0x0A83) return true;
        if (c == 0x0ABC) return true;
                                       
        if (c < 0x0ABE) return false;  if (c <= 0x0AC5) return true;
        if (c < 0x0AC7) return false;  if (c <= 0x0AC9) return true;
        if (c < 0x0ACB) return false;  if (c <= 0x0ACD) return true;
                                       
        if (c < 0x0B01) return false;  if (c <= 0x0B03) return true;
        if (c == 0x0B3C) return true;
        if (c < 0x0B3E) return false;  if (c <= 0x0B43) return true;
        if (c < 0x0B47) return false;  if (c <= 0x0B48) return true;
                                       
        if (c < 0x0B4B) return false;  if (c <= 0x0B4D) return true;
        if (c < 0x0B56) return false;  if (c <= 0x0B57) return true;
        if (c < 0x0B82) return false;  if (c <= 0x0B83) return true;
                                       
        if (c < 0x0BBE) return false;  if (c <= 0x0BC2) return true;
        if (c < 0x0BC6) return false;  if (c <= 0x0BC8) return true;
        if (c < 0x0BCA) return false;  if (c <= 0x0BCD) return true;
        if (c == 0x0BD7) return true;
                                       
        if (c < 0x0C01) return false;  if (c <= 0x0C03) return true;
        if (c < 0x0C3E) return false;  if (c <= 0x0C44) return true;
        if (c < 0x0C46) return false;  if (c <= 0x0C48) return true;
                                       
        if (c < 0x0C4A) return false;  if (c <= 0x0C4D) return true;
        if (c < 0x0C55) return false;  if (c <= 0x0C56) return true;
        if (c < 0x0C82) return false;  if (c <= 0x0C83) return true;
                                       
        if (c < 0x0CBE) return false;  if (c <= 0x0CC4) return true;
        if (c < 0x0CC6) return false;  if (c <= 0x0CC8) return true;
        if (c < 0x0CCA) return false;  if (c <= 0x0CCD) return true;
                                       
        if (c < 0x0CD5) return false;  if (c <= 0x0CD6) return true;
        if (c < 0x0D02) return false;  if (c <= 0x0D03) return true;
        if (c < 0x0D3E) return false;  if (c <= 0x0D43) return true;
                                       
        if (c < 0x0D46) return false;  if (c <= 0x0D48) return true;
        if (c < 0x0D4A) return false;  if (c <= 0x0D4D) return true;
        if (c == 0x0D57) return true;
        if (c == 0x0E31) return true;
                                       
        if (c < 0x0E34) return false;  if (c <= 0x0E3A) return true;
        if (c < 0x0E47) return false;  if (c <= 0x0E4E) return true;
        if (c == 0x0EB1) return true;
        if (c < 0x0EB4) return false;  if (c <= 0x0EB9) return true;
                                       
        if (c < 0x0EBB) return false;  if (c <= 0x0EBC) return true;
        if (c < 0x0EC8) return false;  if (c <= 0x0ECD) return true;
        if (c < 0x0F18) return false;  if (c <= 0x0F19) return true;
        if (c == 0x0F35) return true;
                                       
        if (c == 0x0F37) return true;
        if (c == 0x0F39) return true;
        if (c == 0x0F3E) return true;
        if (c == 0x0F3F) return true;
        if (c < 0x0F71) return false;  if (c <= 0x0F84) return true;
                                       
        if (c < 0x0F86) return false;  if (c <= 0x0F8B) return true;
        if (c < 0x0F90) return false;  if (c <= 0x0F95) return true;
        if (c == 0x0F97) return true;
        if (c < 0x0F99) return false;  if (c <= 0x0FAD) return true;
                                       
        if (c < 0x0FB1) return false;  if (c <= 0x0FB7) return true;
        if (c == 0x0FB9) return true;
        if (c < 0x20D0) return false;  if (c <= 0x20DC) return true;
        if (c == 0x20E1) return true;
                                       
        if (c < 0x302A) return false;  if (c <= 0x302F) return true;
        if (c == 0x3099) return true;
        if (c == 0x309A) return true; 
        
        return false;
        
    }
    
    /**
     * This is a utility function for determining whether a specified 
     * character is an extender according to production 88 of the XML 1.0
     * specification.
     *
     * @param c <code>char</code> to check.
     * @return <code>String</code> true if it's an extender, false otherwise.
     */
    public static boolean isXMLExtender(char c) {

        if (c < 0x00B6) return false;  // quick short circuit

        // Extenders                               
        if (c == 0x00B7) return true;
        if (c == 0x02D0) return true;
        if (c == 0x02D1) return true;
        if (c == 0x0387) return true;
        if (c == 0x0640) return true;
        if (c == 0x0E46) return true;
        if (c == 0x0EC6) return true;
        if (c == 0x3005) return true;
                                       
        if (c < 0x3031) return false;  if (c <= 0x3035) return true;
        if (c < 0x309D) return false;  if (c <= 0x309E) return true;
        if (c < 0x30FC) return false;  if (c <= 0x30FE) return true;
        
        return false;
        
    }
      
    /**
     * This is a utility function for determining whether a specified 
     * Unicode character
     * is a digit according to production 88 of the XML 1.0 specification.
     *
     * @param c <code>char</code> to check for XML digit compliance
     * @return <code>boolean</code> true if it's a digit, false otherwise
     */
    public static boolean isXMLDigit(char c) {
      
        if (c < 0x0030) return false;  if (c <= 0x0039) return true;
        if (c < 0x0660) return false;  if (c <= 0x0669) return true;
        if (c < 0x06F0) return false;  if (c <= 0x06F9) return true;
        if (c < 0x0966) return false;  if (c <= 0x096F) return true;
                                       
        if (c < 0x09E6) return false;  if (c <= 0x09EF) return true;
        if (c < 0x0A66) return false;  if (c <= 0x0A6F) return true;
        if (c < 0x0AE6) return false;  if (c <= 0x0AEF) return true;
                                       
        if (c < 0x0B66) return false;  if (c <= 0x0B6F) return true;
        if (c < 0x0BE7) return false;  if (c <= 0x0BEF) return true;
        if (c < 0x0C66) return false;  if (c <= 0x0C6F) return true;
                                       
        if (c < 0x0CE6) return false;  if (c <= 0x0CEF) return true;
        if (c < 0x0D66) return false;  if (c <= 0x0D6F) return true;
        if (c < 0x0E50) return false;  if (c <= 0x0E59) return true;
                                       
        if (c < 0x0ED0) return false;  if (c <= 0x0ED9) return true;
        if (c < 0x0F20) return false;  if (c <= 0x0F29) return true; 
      
        return false;
    }  
    
    /**
     * This is a utility function for determining whether a specified 
     * Unicode character is a whitespace character according to production 3
     * of the XML 1.0 specification.
     *
     * @param c <code>char</code> to check for XML whitespace compliance
     * @return <code>boolean</code> true if it's a whitespace, false otherwise
     */
    public static boolean isXMLWhitespace(char c) {
        if (c==' ' || c=='\n' || c=='\t' || c=='\r' ){
            return true;
        }
        return false;
    }
}
