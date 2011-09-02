/*--

 $Id: Text.java,v 1.25 2007/11/10 05:28:59 jhunter Exp $

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
 * Character-based XML content. Provides a modular, parentable method of
 * representing text. Text makes no guarantees about the underlying textual
 * representation of character data, but does expose that data as a Java String.
 *
 * @version $Revision: 1.25 $, $Date: 2007/11/10 05:28:59 $
 * @author  Brett McLaughlin
 * @author  Jason Hunter
 * @author  Bradley S. Huffman
 */
public class Text extends Content {

    private static final String CVS_ID =
      "@(#) $RCSfile: Text.java,v $ $Revision: 1.25 $ $Date: 2007/11/10 05:28:59 $ $Name:  $";

    static final String EMPTY_STRING = "";

    /** The actual character content */
    // XXX See http://www.servlets.com/archive/servlet/ReadMsg?msgId=8612
    // from elharo for a description of why Java characters may not suffice
    // long term
    protected String value;

    /**
     * This is the protected, no-args constructor standard in all JDOM
     * classes. It allows subclassers to get a raw instance with no
     * initialization.
     */
    protected Text() { }

    /**
     * This constructor creates a new <code>Text</code> node, with the
     * supplied string value as it's character content.
     *
     * @param str the node's character content.
     * @throws IllegalDataException if <code>str</code> contains an
     *         illegal character such as a vertical tab (as determined
     *         by {@link org.jdom.Verifier#checkCharacterData})
     */
    public Text(String str) {
        setText(str);
    }

    /**
     * This returns the value of this <code>Text</code> node as a Java
     * <code>String</code>.
     *
     * @return <code>String</code> - character content of this node.
     */
    public String getText() {
        return value;
    }

    /**
     * This returns the textual content with all surrounding whitespace
     * removed.  If only whitespace exists, the empty string is returned.
     *
     * @return trimmed text content or empty string
     */
    public String getTextTrim() {
        return getText().trim();
    }

    /**
     * This returns the textual content with all surrounding whitespace
     * removed and internal whitespace normalized to a single space.  If
     * only whitespace exists, the empty string is returned.
     *
     * @return normalized text content or empty string
     */
    public String getTextNormalize() {
        return normalizeString(getText());
    }

    /**
     * This returns a new string with all surrounding whitespace
     * removed and internal whitespace normalized to a single space.  If
     * only whitespace exists, the empty string is returned.
     * <p>
     * Per XML 1.0 Production 3 whitespace includes: #x20, #x9, #xD, #xA
     * </p>
     *
     * @param str string to be normalized.
     * @return normalized string or empty string
     */
    public static String normalizeString(String str) {
        if (str == null)
            return EMPTY_STRING;

        char[] c = str.toCharArray();
        char[] n = new char[c.length];
        boolean white = true;
        int pos = 0;
        for (int i = 0; i < c.length; i++) {
            if (" \t\n\r".indexOf(c[i]) != -1) {
                if (!white) {
                    n[pos++] = ' ';
                    white = true;
                }
            }
            else {
                n[pos++] = c[i];
                white = false;
            }
        }
        if (white && pos > 0) {
            pos--;
        }
        return new String(n, 0, pos);
    }

    /**
     * This will set the value of this <code>Text</code> node.
     *
     * @param str value for node's content.
     * @return the object on which the method was invoked
     * @throws IllegalDataException if <code>str</code> contains an
     *         illegal character such as a vertical tab (as determined
     *         by {@link org.jdom.Verifier#checkCharacterData})
     */
    public Text setText(String str) {
        String reason;

        if (str == null) {
            value = EMPTY_STRING;
            return this;
        }

        if ((reason = Verifier.checkCharacterData(str)) != null) {
            throw new IllegalDataException(str, "character content", reason);
        }
        value = str;
        return this;
    }

    /**
     * This will append character content to whatever content already
     * exists within this <code>Text</code> node.
     *
     * @param str character content to append.
     * @throws IllegalDataException if <code>str</code> contains an
     *         illegal character such as a vertical tab (as determined
     *         by {@link org.jdom.Verifier#checkCharacterData})
     */
    public void append(String str) {
        String reason;

        if (str == null) {
            return;
        }
        if ((reason = Verifier.checkCharacterData(str)) != null) {
            throw new IllegalDataException(str, "character content", reason);
        }

        if (str.length() > 0) {
             value += str;
        }
    }

    /**
     * This will append the content of another <code>Text</code> node
     * to this node.
     *
     * @param text Text node to append.
     */
    public void append(Text text) {
        if (text == null) {
            return;
        }
        value += text.getText();
    }

    /**
     * Returns the XPath 1.0 string value of this element, which is the
     * text itself.
     *
     * @return the text
     */
    public String getValue() {
        return value;
    }

    /**
     * This returns a <code>String</code> representation of the
     * <code>Text</code> node, suitable for debugging. If the XML
     * representation of the <code>Text</code> node is desired,
     * either <code>{@link #getText}</code> or
     * {@link org.jdom.output.XMLOutputter#outputString(Text)}</code>
     * should be used.
     *
     * @return <code>String</code> - information about this node.
     */
    public String toString() {
        return new StringBuffer(64)
            .append("[Text: ")
            .append(getText())
            .append("]")
            .toString();
    }

    /**
     * This will return a clone of this <code>Text</code> node, with the
     * same character content, but no parent.
     *
     * @return <code>Text</code> - cloned node.
     */
    public Object clone() {
        Text text = (Text)super.clone();
        text.value = value;
        return text;
    }

}
