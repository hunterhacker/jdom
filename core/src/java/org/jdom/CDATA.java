/*--

 $Id: CDATA.java,v 1.22 2002/04/28 08:44:28 jhunter Exp $

 Copyright (C) 2000 Jason Hunter & Brett McLaughlin.
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
    written permission, please contact <pm AT jdom DOT org>.

 4. Products derived from this software may not be called "JDOM", nor
    may "JDOM" appear in their name, without prior written permission
    from the JDOM Project Management <pm AT jdom DOT org>.

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
 created by Jason Hunter <jhunter AT jdom DOT org> and
 Brett McLaughlin <brett AT jdom DOT org>.  For more information
 on the JDOM Project, please see <http://www.jdom.org/>.

 */

package org.jdom;

/**
 * <p><code><b>CDATA</b></code> represents character-based content within an
 *   XML document represented by JDOM. It is intended to provide a modular,
 *   printable method of representing CDATA. Additionally,
 *   <code>CDATA</code> makes no guarantees about the underlying textual
 *   representation of character data, but does expose that data as a Java
 *   <code>String</code>.</p>
 *
 * @author Dan Schaffer
 * @author Brett McLaughlin
 * @author Jason Hunter
 * @author Bradley S. Huffman
 * @version $Revision: 1.22 $, $Date: 2002/04/28 08:44:28 $
 */
public class CDATA extends Text {

    private static final String CVS_ID = 
      "@(#) $RCSfile: CDATA.java,v $ $Revision: 1.22 $ $Date: 2002/04/28 08:44:28 $ $Name:  $";

    private static final String EMPTY_STRING = "";

    /**
     * <p>This is the protected, no-args constructor standard in all JDOM
     *  classes. It allows subclassers to get a raw instance with no
     *  initialization.</p>
     */
    protected CDATA() { }

    /**
     * <p>This constructor creates a new <code>CDATA</code> node, with the
     *   supplied string value as it's character content.</p>
     *
     * @param str the node's character content.
     */
    public CDATA(String str) {
        super(str);
    }

    /**
     * <p>This will set the value of this <code>CDATA</code> node.</p>
     *
     * @param str value for node's content.
     */
    public Text setText(String str) {
        // Overrides Text.setText() because this needs to check CDATA
        // rules are enforced.  We could have a separate Verifier check
        // for CDATA beyond Text and call that alone before super.setText().

        String reason;

        if (str == null) {
            value = EMPTY_STRING;
            return this;
        }

        if ((reason = Verifier.checkCDATASection(str)) != null) {
            throw new IllegalDataException(str, "CDATA section", reason);
        }
        value = str;
        return this;
    }

    /**
     * <p>This will append character content to whatever content already
     *   exists within this <code>CDATA</code> node.</p>
     *
     * @param str character content to append.
     */
    public void append(String str) {
        // Overrides Text.setText() because this needs to check CDATA
        // rules are enforced.  We could have a separate Verifier check
        // for CDATA beyond Text and call that alone before super.setText().

        String reason;

        if (str == null) {
            return;
        }
        if ((reason = Verifier.checkCDATASection(str)) != null) {
            throw new IllegalDataException(str, "CDATA section", reason);
        }

        if (value == EMPTY_STRING)
             value = str;
        else value += str;
    }

    /**
     * <p>This returns a <code>String</code> representation of the
     *   <code>CDATA</code> node, suitable for debugging. If the XML
     *   representation of the <code>CDATA</code> node is desired,
     *   either <code>{@link #getText}</code> or
     *   {@link org.jdom.output.XMLOutputter#output(CDATA, Writer)}</code>
     *   should be used.</p>
     *
     * @return <code>String</code> - information about this node.
     */
    public String toString() {
        return new StringBuffer(64)
            .append("[CDATA: ")
            .append(getText())
            .append("]")
            .toString();
    }
}
