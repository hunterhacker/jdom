/*--

 $Id: TextHelper.java,v 1.2 2004/02/06 09:57:48 jhunter Exp $

 Copyright (C) 2000-2004 Jason Hunter & Brett McLaughlin.
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

package org.jdom.contrib.helpers;

import org.jdom.*;

/** <p>
 *    This class contains static helper methods. 
 *    </p>
 *    @author Alex Rosen
 */
public class TextHelper {
    /**
     * <p>
     * This returns the text with surrounding whitespace removed and 
     * internal whitespace normalized to a single space.  
     * </p>
     */
    public static String normalize(String text) {
        char[] chars = text.toCharArray();
        char[] newChars = new char[chars.length];
        boolean white = true;
        int pos = 0;
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == ' ' || c == '\r' || c == '\n' || c == '\t') {
                if (!white) {
                    newChars[pos++] = ' ';
                    white = true;
                }
            }
            else {
                newChars[pos++] = c;
                white = false;
            }
        }
        if (white && pos > 0) {
            pos--;
        }
        return new String(newChars, 0, pos);
    }

    /**
     * <p>
     *  This convenience method returns the textual content of the named
     *    child element, or returns an empty <code>String</code> ("")
     *    if the child has no textual content. However, if the child does
     *    not exist, <code>null</code> is returned.
     * </p>
     *
     * @param parent the parent element
     * @param name the name of the child
     * @return text content for the named child, or null if no
     * such child exists
     */
    public static String getChildText(Element parent, String name) {
        Element child = parent.getChild(name);
        if (child == null) {
            return null;
        }
        return child.getText();
    }

    /**
     * <p>
     *  This convenience method returns the textual content of the named
     *    child element, or returns an empty <code>String</code> ("")
     *    if the child has no textual content. However, if the child does
     *    not exist, <code>null</code> is returned.
     * </p>
     *
     * @param parent the parent element
     * @param name the name of the child
     * @param ns the namespace of the child
     * @return text content for the named child, or null if no
     * such child exists
     */
    public static String getChildText(Element parent, String name, Namespace ns) {
        Element child = parent.getChild(name, ns);
        if (child == null) {
            return null;
        }
        return child.getText();
    }

    /**
     * <p>
     *  This convenience method returns the trimmed textual content of the 
     *    named child element, or returns null if there's no such child.  
     *    See <code>String.trim()</code> for details of text trimming.
     * </p>
     *
     * @param parent the parent element
     * @param name the name of the child
     * @return trimmed text content for the named child, or null if no
     * such child exists
     */
    public static String getChildTextTrim(Element parent, String name) {
        Element child = parent.getChild(name);
        if (child == null) {
            return null;
        } else {
            return child.getText().trim();
        }
    }

    /**
     * <p>
     *  This convenience method returns the trimmed textual content of the 
     *    named child element, or returns null if there's no such child.  
     *    See <code>String.trim()</code> for
     *    details of text trimming.
     * </p>
     *
     * @param parent the parent element
     * @param name the name of the child
     * @param ns the namespace of the child
     * @return trimmed text content for the named child, or null if no
     * such child exists
     */
    public static String getChildTextTrim(Element parent, String name, Namespace ns) {
        Element child = parent.getChild(name, ns);
        if (child == null) {
            return null;
        } else {
            return child.getText().trim();
        }
    }

    /**
     * <p>
     *  This convenience method returns the normalized textual content of the 
     *    named child element, or returns null if there's no such child.  
     *    See <code>{@link #normalize}</code> for details of text normalization.
     * </p>
     *
     * @param parent the parent element
     * @param name the name of the child
     * @return normalized text content for the named child, or null if no
     * such child exists
     */
    public static String getChildTextNormalize(Element parent, String name) {
        Element child = parent.getChild(name);
        if (child == null) {
            return null;
        } else {
            return normalize(child.getText());
        }
    }

    /**
     * <p>
     *  This convenience method returns the normalized textual content of the 
     *    named child element, or returns null if there's no such child.  
     *    See <code>{@link #normalize}</code> for
     *    details of text normalization.
     * </p>
     *
     * @param parent the parent element
     * @param name the name of the child
     * @param ns the namespace of the child
     * @return normalized text content for the named child, or null if no
     * such child exists
     */
    public static String getChildTextNormalize(Element parent, String name, Namespace ns) {
        Element child = parent.getChild(name, ns);
        if (child == null) {
            return null;
        } else {
            return normalize(child.getText());
        }
    }
}

