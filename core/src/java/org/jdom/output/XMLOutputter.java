/*--

 $Id: XMLOutputter.java,v 1.84 2002/06/18 12:11:20 jhunter Exp $

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

package org.jdom.output;

import java.io.*;
import java.util.*;

import org.jdom.*;
import org.jdom.output.*;

/**
 * <code>XMLOutputter</code> takes a JDOM tree and formats it to a
 * stream as XML.  The outputter can manage many styles of document
 * formatting, from untouched to pretty printed.  The default is to
 * output the document content exactly as created, but this can be
 * changed with the various <code>set*()</code> methods.
 *
 * <p>
 * There are <code>output(&#133;)<code> methods to print any of the
 * standard JDOM classes, including <code>Document</code> and
 * <code>Element</code>, to either a <code>Writer</code> or an
 * <code>OutputStream</code>. <b>Warning</b>: When outputting to a
 * Writer, make sure the writer's encoding matches the encoding setting
 * in the XMLOutputter.  This ensures the encoding in which the content
 * is written (controlled by the Writer configuration) matches the
 * encoding placed in the document's XML declaration (controlled by the
 * XMLOutputter).  Because a Writer cannot be queried for its encoding,
 * the information must be passed to the XMLOutputter manually in its
 * constructor or via the setEncoding() method.  The default XMLOutputter
 * encoding is UTF-8.
 * </p>
 *
 * <p>
 * The methods <code>outputString(&#133;)</code> are for convenience
 * only; for top performance you should call one of the
 * <code>output(&#133;)</code> and pass in your own <code>Writer</code> or
 * <code>OutputStream</code> if possible.
 * </p>
 *
 * <p>
 * XML declarations are always printed on their own line followed by a
 * line seperator (this doesn't change the semantics of the document). To
 * omit printing of the declaration use <code>setOmitDeclaration</code>.
 * To omit printing of the encoding in the declaration use
 * <code>setOmitEncoding</code>. Unfortunatly there is currently no way
 * to know the original encoding of the document.
 * </p>
 *
 * <p>
 * Empty elements are by default printed as &lt;empty/&gt, but this can
 * be configured with <code>setExpandEmptyElements</code> to cause them to
 * be expanded to &lt;empty&gt&lt;/empty&gt;.
 * </p>
 *
 * <p>
 * Several modes are available to effect the way textual content is printed.
 * All modes are configurable through corresponding set*() methods. 
 * Below is a table which explains the modes and the effect on the
 * resulting output.
 * </p>
 *
 * <table>
 *   <tr>
 *     <th align="left">
 *       Text Mode
 *     </th>
 *     <th>
 *       Resulting behavior.
 *     </th>
 *   </tr>
 *
 *   <tr valign="top">
 *     <td>
 *       <i>Default</i>
 *     </td>
 *     <td>
 *       All content is printed in the format it was created, no whitespace
 *       or line separators are are added or removed.
 *     </td>
 *   </tr>
 *
 *   <tr valign="top">
 *     <td>
 *       TrimAllWhite
 *     </td>
 *     <td>
 *       Content between tags consisting of all whitespace is not printed.
 *       If the content contains even one non-whitespace character, it is
 *       printed verbatim, whitespace and all.
 *     </td>
 *   </tr>
 *
 *   <tr valign="top">
 *     <td>
 *       TextTrim
 *     </td>
 *     <td>
 *       Same as TrimAllWhite, plus leading/trailing whitespace are
 *       trimmed.
 *     </td>
 *   </tr>
 *
 *   <tr valign="top">
 *     <td>
 *       TextNormalize
 *     </td>
 *     <td>
 *       Same as TextTrim, in addition interior whitespace is compressed to
 *       a single space.
 *     </td>
 *   </tr>
 * </table>
 *
 * <p>
 * For pretty-print output, use <code>setNewlines</code> in conjunction
 * with <code>setIndent</code>. Setting newlines to <code>true</code> causes
 * tags to be aligned and possibly indented. With newlines <code>true</code>,
 * whitespace might be added back to fit alignment needs. In most cases
 * texual content is aligned with the surrounding tags (after the
 * appropriate text mode is applied).  In the case where the only content
 * between the start and end tags is textual, the start tag, text, and end
 * tag are all printed on the same line.
 *
 * <p>
 * When a element has a xml:space attribute with the value of "preserve",
 * all formating is turned off and reverts back to the default until the
 * element and its contents have been printed.  If a nested element
 * contains another xml:space with the value "default" formatting is turned
 * back on for the child element and then off for the remainder of the
 * parent element.
 * </p>
 *
 * @author Brett McLaughlin
 * @author Jason Hunter
 * @author Jason Reid
 * @author Wolfgang Werner
 * @author Elliotte Rusty Harold
 * @author David &amp; Will (from Post Tool Design)
 * @author Dan Schaffer
 * @author Alex Chaffee (alex@jguru.com)
 * @author Bradley S. Huffman
 * @version $Revision: 1.84 $, $Date: 2002/06/18 12:11:20 $
 */

public class XMLOutputter implements Cloneable {

    private static final String CVS_ID =
      "@(#) $RCSfile: XMLOutputter.java,v $ $Revision: 1.84 $ $Date: 2002/06/18 12:11:20 $ $Name:  $";

    /** Whether or not to output the XML declaration
      * - default is <code>false</code> */
    private boolean omitDeclaration = false;

    /** The encoding format */
    private String encoding = "UTF-8";

    /** Whether or not to output the encoding in the XML declaration
      * - default is <code>false</code> */
    private boolean omitEncoding = false;

    /** standard value to indent by, if we are indenting */
    private static final String STANDARD_INDENT = "  ";

    /** standard string with which to end a line */
    private static final String STANDARD_LINE_SEPARATOR = "\r\n";

    class Format implements Cloneable {

        /** The default indent is no spaces (as original document) */
        String indent = null;

        /** Whether or not to expand empty elements to
          * &lt;tagName&gt;&lt;/tagName&gt; - default is <code>false</code> */
        boolean expandEmptyElements = false;

        /** New line separator */
        String lineSeparator = STANDARD_LINE_SEPARATOR;

        /** Should we trim whitespace only content or not */
        boolean trimAllWhite = false;

        /** Should we trim leading/trailing whitespace or not in text nodes */
        boolean textTrim = false;

        /** Should we preserve whitespace or not in text nodes */
        boolean textNormalize = false;

        /** The default new line flag, set to do new lines only as in
          * original document */
        boolean newlines = false;

        Format() {}

        protected Object clone() {
            Format format = null;

            try {
                format = (Format) super.clone();
            } catch (CloneNotSupportedException ce) { }

            return format;
        }
    }

    Format noFormatting = new Format();
    Format defaultFormat = new Format();
    Format currentFormat = defaultFormat;

    // * * * * * * * * * * Constructors * * * * * * * * * *
    // * * * * * * * * * * Constructors * * * * * * * * * *

    /**
     * This will create an <code>XMLOutputter</code> with no additional
     * whitespace (indent or newlines) added; the whitespace from the
     * element text content is fully preserved.
     */
    public XMLOutputter() {
    }

    /**
     * This will create an <code>XMLOutputter</code> with the given indent
     * added but no new lines added; all whitespace from the element text
     * content is included as well.
     *
     * @param indent  the indent string, usually some number of spaces
     */
    public XMLOutputter(String indent) {
       setIndent( indent);
    }

    /**
     * This will create an <code>XMLOutputter</code> with the given indent
     * that prints newlines only if <code>newlines</code> is
     * <code>true</code>; all whitespace from the element text content is
     * included as well.
     *
     * @param indent the indent <code>String</code>, usually some number
     *        of spaces
     * @param newlines <code>true</code> indicates new lines should be
     *                 printed, else new lines are ignored (compacted).
     */
    public XMLOutputter(String indent, boolean newlines) {
       setIndent( indent);
       setNewlines( newlines);
    }

    /**
     * This will create an <code>XMLOutputter</code> with
     * the given indent and new lines printing only if
     * <code>newlines</code> is <code>true</code>, and encoding format
     * <code>encoding</code>.
     *
     * @param indent the indent <code>String</code>, usually some number
     *        of spaces
     * @param newlines <code>true</code> indicates new lines should be
     *                 printed, else new lines are ignored (compacted).
     * @param encoding set encoding format.  Use XML-style names like
     *                 "UTF-8" or "ISO-8859-1" or "US-ASCII"
     */
    public XMLOutputter(String indent, boolean newlines, String encoding) {
       setEncoding( encoding);
       setIndent( indent);
       setNewlines( newlines);
    }

    /**
     * This will create an <code>XMLOutputter</code> with all the
     * options as set in the given <code>XMLOutputter</code>.  Note
     * that <code>XMLOutputter two = (XMLOutputter)one.clone();</code>
     * would work equally well.
     *
     * @param that the XMLOutputter to clone
     */
    public XMLOutputter(XMLOutputter that) {
        this.encoding = that.encoding;
        this.omitDeclaration = that.omitDeclaration;
        this.omitEncoding = that.omitEncoding;
        this.defaultFormat = (Format) that.defaultFormat.clone();
    }

    // * * * * * * * * * * Set parameters methods * * * * * * * * * *
    // * * * * * * * * * * Set parameters methods * * * * * * * * * *

    /**
     * This will set the newline separator (<code>lineSeparator</code>).
     * The default is <code>\r\n</code>. Note that if the "newlines"
     * property is false, this value is irrelevant.  To make it output
     * the system default line ending string, call
     * <code>setLineSeparator(System.getProperty("line.separator"))</code>
     *
     * <p>
     * To output "UNIX-style" documents, call
     * <code>setLineSeparator("\n")</code>.  To output "Mac-style"
     * documents, call <code>setLineSeparator("\r")</code>.  DOS-style
     * documents use CR-LF ("\r\n"), which is the default.
     * </p>
     *
     * <p>
     * Note that this only applies to newlines generated by the
     * outputter.  If you parse an XML document that contains newlines
     * embedded inside a text node, and you do not call
     * <code>setTextNormalize</code>, then the newlines will be output
     * verbatim, as "\n" which is how parsers normalize them.
     * </p>
     *
     * @see #setNewlines(boolean)
     * @see #setTextNormalize(boolean)
     *
     * @param separator <code>String</code> line separator to use.
     */
    public void setLineSeparator(String separator) {
        defaultFormat.lineSeparator = separator;
    }

    /**
     * Sets whether newlines (<code>lineSeparator</code>) should
     * be printed.
     *
     * @see #setLineSeparator(String)
     * @param newlines <code>true</code> indicates new lines should be
     *                 printed, else new lines are ignored (compacted).
     */
    public void setNewlines(boolean newlines) {
        defaultFormat.newlines = newlines;
    }

    /**
     * Sets the output encoding.  The name should be an accepted XML
     * encoding.
     *
     * @param encoding the encoding format.  Use XML-style names like
     *                 "UTF-8" or "ISO-8859-1" or "US-ASCII"
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * This will set whether the XML declaration
     * (<code>&lt;&#063;xml version="1&#046;0"
     * encoding="UTF-8"&#063;&gt;</code>)
     * includes the encoding of the document. It is common to omit
     * this in uses such as WML and other wireless device protocols.
     *
     * @param omitEncoding <code>boolean</code> indicating whether or not
     *        the XML declaration should indicate the document encoding.
     */
    public void setOmitEncoding(boolean omitEncoding) {
        this.omitEncoding = omitEncoding;
    }

    /**
     * This will set whether the XML declaration
     * (<code>&lt;&#063;xml version="1&#046;0"&#063;gt;</code>)
     * will be omitted or not. It is common to omit this in uses such
     * as SOAP and XML-RPC calls.
     *
     * @param omitDeclaration <code>boolean</code> indicating whether or not
     *        the XML declaration should be omitted.
     */
    public void setOmitDeclaration(boolean omitDeclaration) {
        this.omitDeclaration = omitDeclaration;
    }

    /**
     * This will set whether empty elements are expanded from
     * <code>&lt;tagName/&gt;</code> to
     * <code>&lt;tagName&gt;&lt;/tagName&gt;</code>.
     *
     * @param expandEmptyElements <code>boolean</code> indicating whether or not
     *        empty elements should be expanded.
     */
    public void setExpandEmptyElements(boolean expandEmptyElements) {
        defaultFormat.expandEmptyElements = expandEmptyElements;
    }

    /**
     * This will set whether content between tags consisting of all
     * whitespace is printed or trimmed.
     *
     * <p>Default: false </p>
     *
     * @param trimAllWhite <code>boolean</code> true=>content consisting of
     *                 only whitespace is not print, false=>use text verbatim
     */
    public void setTrimAllWhite(boolean trimAllWhite) {
        defaultFormat.trimAllWhite = trimAllWhite;
    }

    /**
     * This will set whether the text has leading/trailing whitespace
     * trimmed.
     *
     * <p>Default: false </p>
     *
     * @param textTrim <code>boolean</code> true=>trim the leading/trailing
     *                 whitespace, false=>use text verbatim
     */
    public void setTextTrim(boolean textTrim) {
        defaultFormat.textTrim = textTrim;
    }

    /**
     * This will set whether the text is output verbatim (false)
     * or with whitespace normalized as per <code>{@link
     * org.jdom.Element#getTextNormalize()}</code>.
     *
     * <p>Default: false </p>
     *
     * @param textNormalize <code>boolean</code> true=>normalize the
     *        whitespace, false=>use text verbatim
     */
    public void setTextNormalize(boolean textNormalize) {
        defaultFormat.textNormalize = textNormalize;
    }

    /**
     * This will set the indent <code>String</code> to use; this
     * is usually a <code>String</code> of empty spaces. If you pass
     * null, or the empty string (""), then no indentation will
     * happen.  Default: none (null)
     *
     * @param indent <code>String</code> to use for indentation.
     */
    public void setIndent(String indent) {
        // if passed the empty string, change it to null, for marginal
        // performance gains later (can compare to null first instead
        // of calling equals())
        if ("".equals(indent)) {
            indent = null;
        }
        defaultFormat.indent = indent;
    }

    // * * * * * * * * * * Output to a OutputStream * * * * * * * * * *
    // * * * * * * * * * * Output to a OutputStream * * * * * * * * * *

    /**
     * This will print the <code>Document</code> to the given output stream.
     * The characters are printed using the encoding specified in the
     * constructor, or a default of UTF-8.
     *
     * @param doc <code>Document</code> to format.
     * @param out <code>OutputStream</code> to use.
     * @throws IOException - if there's any problem writing.
     */
    public void output(Document doc, OutputStream out)
                    throws IOException {
        Writer writer = makeWriter(out);
        output(doc, writer);  // output() flushes
    }

    /**
     * Print out the <code>{@link DocType}</code>.
     *
     * @param doctype <code>DocType</code> to output.
     * @param out <code>OutputStream</code> to use.
     */
    public void output(DocType doctype, OutputStream out) throws IOException {
        Writer writer = makeWriter(out);
        output(doctype, writer);  // output() flushes
    }

    /**
     * Print out an <code>{@link Element}</code>, including
     * its <code>{@link Attribute}</code>s, and all
     * contained (child) elements, etc.
     *
     * @param element <code>Element</code> to output.
     * @param out <code>Writer</code> to use.
     */
    public void output(Element element, OutputStream out) throws IOException {
        Writer writer = makeWriter(out);
        output(element, writer);  // output() flushes
    }

    /**
     * This will handle printing out an <code>{@link
     * Element}</code>'s content only, not including its tag, and
     * attributes.  This can be useful for printing the content of an
     * element that contains HTML, like "&lt;description&gt;JDOM is
     * &lt;b&gt;fun&gt;!&lt;/description&gt;".
     *
     * @param element <code>Element</code> to output.
     * @param out <code>OutputStream</code> to use.
     */
    public void outputElementContent(Element element, OutputStream out)
                    throws IOException {
        Writer writer = makeWriter(out);
        outputElementContent(element, writer);  // output() flushes
    }

    /**
     * This will handle printing out a list of nodes.
     * This can be useful for printing the content of an element that
     * contains HTML, like "&lt;description&gt;JDOM is
     * &lt;b&gt;fun&gt;!&lt;/description&gt;".
     *
     * @param list <code>List</code> of nodes.
     * @param out <code>OutputStream</code> to use.
     */
    public void output(List list, OutputStream out)
                    throws IOException {
        Writer writer = makeWriter(out);
        output(list, writer);  // output() flushes
    }

    /**
     * Print out a <code>{@link CDATA}</code> node.
     *
     * @param cdata <code>CDATA</code> to output.
     * @param out <code>OutputStream</code> to use.
     */
    public void output(CDATA cdata, OutputStream out) throws IOException {
        Writer writer = makeWriter(out);
        output(cdata, writer);  // output() flushes
    }

    /**
     * Print out a <code>{@link Text}</code> node.  Perfoms
     * the necessary entity escaping and whitespace stripping.
     *
     * @param text <code>Text</code> to output.
     * @param out <code>OutputStream</code> to use.
     */
    public void output(Text text, OutputStream out) throws IOException {
        Writer writer = makeWriter(out);
        output(text, writer);  // output() flushes
    }

    /**
     * Print out a <code>{@link Comment}</code>.
     *
     * @param comment <code>Comment</code> to output.
     * @param out <code>OutputStream</code> to use.
     */
    public void output(Comment comment, OutputStream out) throws IOException {
        Writer writer = makeWriter(out);
        output(comment, writer);  // output() flushes
    }

    /**
     * Print out a <code>{@link ProcessingInstruction}</code>.
     *
     * @param processingInstruction <code>ProcessingInstruction</code>
     *                              to output.
     * @param out <code>OutputStream</code> to use.
     */
    public void output(ProcessingInstruction pi, OutputStream out)
                                 throws IOException {
        Writer writer = makeWriter(out);
        output(pi, writer);  // output() flushes
    }

    /**
     * Print out a <code>{@link EntityRef}</code>.
     *
     * @param entity <code>EntityRef</code> to output.
     * @param out <code>OutputStream</code> to use.
     */
    public void output(EntityRef entity, OutputStream out) throws IOException {
        Writer writer = makeWriter(out);
        output(entity, writer);  // output() flushes
    }

    /**
     * Get an OutputStreamWriter, using prefered encoding
     * (see {@link #setEncoding}).
     */
    protected Writer makeWriter(OutputStream out)
                         throws java.io.UnsupportedEncodingException {
        return makeWriter(out, this.encoding);
    }

    /**
     * Get an OutputStreamWriter, use specified encoding.
     */
    protected Writer makeWriter(OutputStream out, String enc)
                         throws java.io.UnsupportedEncodingException {
        // "UTF-8" is not recognized before JDK 1.1.6, so we'll translate
        // into "UTF8" which works with all JDKs.
        if ("UTF-8".equals(enc)) {
            enc = "UTF8";
        }

        Writer writer = new BufferedWriter(
                            (new OutputStreamWriter(
                                new BufferedOutputStream(out), enc)
                            ));
        return writer;
    }

    // * * * * * * * * * * Output to a Writer * * * * * * * * * *
    // * * * * * * * * * * Output to a Writer * * * * * * * * * *

    /**
     * This will print the <code>Document</code> to the given Writer.
     *
     * <p>
     * Warning: using your own Writer may cause the outputter's
     * preferred character encoding to be ignored.  If you use
     * encodings other than UTF-8, we recommend using the method that
     * takes an OutputStream instead.
     * </p>
     *
     * @param doc <code>Document</code> to format.
     * @param out <code>Writer</code> to use.
     * @throws IOException - if there's any problem writing.
     */
    public void output(Document doc, Writer out) throws IOException {

        printDeclaration(doc, out, encoding);

        if (doc.getDocType() != null) {
            printDocType(doc.getDocType(), out);
        }

        // Print out root element, as well as any root level
        // comments and processing instructions,
        // starting with no indentation
        List content = doc.getContent();
        int size = content.size();
        for( int i = 0; i < size; i++) {
            Object obj = content.get( i);
            if (obj instanceof Element) {
                printElement(doc.getRootElement(), out, 0,
                             createNamespaceStack());
            }
            else if (obj instanceof Comment) {
                printComment((Comment) obj, out);
            }
            else if (obj instanceof ProcessingInstruction) {
                printProcessingInstruction((ProcessingInstruction) obj, out);
            }
            else {
                // XXX if we get here then we have a illegal content, for
                //     now we'll just ignore it
            }

            newline(out);
            indent(out, 0);
        }

        // Output final line separator
        // We output this no matter what the newline flags say
        out.write(currentFormat.lineSeparator);

        out.flush();
    }

    /**
     * Print out the <code>{@link DocType}</code>.
     *
     * @param doctype <code>DocType</code> to output.
     * @param out <code>Writer</code> to use.
     */
    public void output(DocType doctype, Writer out) throws IOException {
        printDocType(doctype, out);
        out.flush();
    }

    /**
     * Print out an <code>{@link Element}</code>, including
     * its <code>{@link Attribute}</code>s, and all
     * contained (child) elements, etc.
     *
     * @param element <code>Element</code> to output.
     * @param out <code>Writer</code> to use.
     */
    public void output(Element element, Writer out) throws IOException {
        // If this is the root element we could pre-initialize the
        // namespace stack with the namespaces
        printElement(element, out, 0, createNamespaceStack());
        out.flush();
    }

    /**
     * This will handle printing out an <code>{@link
     * Element}</code>'s content only, not including its tag, and
     * attributes.  This can be useful for printing the content of an
     * element that contains HTML, like "&lt;description&gt;JDOM is
     * &lt;b&gt;fun&gt;!&lt;/description&gt;".
     *
     * @param element <code>Element</code> to output.
     * @param out <code>Writer</code> to use.
     */
    public void outputElementContent(Element element, Writer out)
                    throws IOException {
        List content = element.getContent();
        printContentRange(content, 0, content.size(), out,
                          0, createNamespaceStack());
        out.flush();
    }

    /**
     * This will handle printing out a list of nodes.
     * This can be useful for printing the content of an element that
     * contains HTML, like "&lt;description&gt;JDOM is
     * &lt;b&gt;fun&gt;!&lt;/description&gt;".
     *
     * @param list <code>List</code> of nodes.
     * @param out <code>Writer</code> to use.
     */
    public void output(List list, Writer out)
                    throws IOException {
        printContentRange(list, 0, list.size(), out,
                          0, createNamespaceStack());
        out.flush();
    }

    /**
     * Print out a <code>{@link CDATA}</code> node.
     *
     * @param cdata <code>CDATA</code> to output.
     * @param out <code>Writer</code> to use.
     */
    public void output(CDATA cdata, Writer out) throws IOException {
        printCDATA(cdata, out);
        out.flush();
    }

    /**
     * Print out a <code>{@link Text}</code> node.  Perfoms
     * the necessary entity escaping and whitespace stripping.
     *
     * @param text <code>Text</code> to output.
     * @param out <code>Writer</code> to use.
     */
    public void output(Text text, Writer out) throws IOException {
        printText(text, out);
        out.flush();
    }

    /**
     * Print out a <code>{@link Comment}</code>.
     *
     * @param comment <code>Comment</code> to output.
     * @param out <code>Writer</code> to use.
     */
    public void output(Comment comment, Writer out) throws IOException {
        printComment(comment, out);
        out.flush();
    }

    /**
     * Print out a <code>{@link ProcessingInstruction}</code>.
     *
     * @param element <code>ProcessingInstruction</code> to output.
     * @param out <code>Writer</code> to use.
     */
    public void output(ProcessingInstruction pi, Writer out)
                                 throws IOException {
        printProcessingInstruction(pi, out);
        out.flush();
    }

    /**
     * Print out a <code>{@link EntityRef}</code>.
     *
     * @param entity <code>EntityRef</code> to output.
     * @param out <code>Writer</code> to use.
     */
    public void output(EntityRef entity, Writer out) throws IOException {
        printEntityRef(entity, out);
        out.flush();
    }

    // * * * * * * * * * * Output to a String * * * * * * * * * *
    // * * * * * * * * * * Output to a String * * * * * * * * * *

    /**
     * Return a string representing a document.  Uses an internal
     * StringWriter. Warning: a String is Unicode, which may not match
     * the outputter's specified encoding.
     *
     * @param doc <code>Document</code> to format.
     */
    public String outputString(Document doc) {
        StringWriter out = new StringWriter();
        try {
            output(doc, out);  // output() flushes
        } catch (IOException e) { }
        return out.toString();
    }

    /**
     * Return a string representing a DocType. Warning: a String is
     * Unicode, which may not match the outputter's specified
     * encoding.
     *
     * @param doctype <code>DocType</code> to format.
     */
    public String outputString(DocType doctype) {
        StringWriter out = new StringWriter();
        try {
            output(doctype, out);  // output() flushes
        } catch (IOException e) { }
        return out.toString();
    }

    /**
     * Return a string representing an element. Warning: a String is
     * Unicode, which may not match the outputter's specified
     * encoding.
     *
     * @param element <code>Element</code> to format.
     */
    public String outputString(Element element) {
        StringWriter out = new StringWriter();
        try {
            output(element, out);  // output() flushes
        } catch (IOException e) { }
        return out.toString();
    }

   /**
     * Return a string representing a list of nodes.  The list is
     * assumed to contain legal JDOM nodes.
     *
     * @param list <code>List</code> to format.
     */
    public String outputString(List list) {
        StringWriter out = new StringWriter();
        try {
            output(list, out);  // output() flushes
        } catch (IOException e) { }
        return out.toString();
    }

    /**
     * Return a string representing a CDATA node. Warning: a String is
     * Unicode, which may not match the outputter's specified
     * encoding.
     *
     * @param cdata <code>CDATA</code> to format.
     */
    public String outputString(CDATA cdata) {
        StringWriter out = new StringWriter();
        try {
            output(cdata, out);  // output() flushes
        } catch (IOException e) { }
        return out.toString();
    }

    /**
     * Return a string representing a Text node. Warning: a String is
     * Unicode, which may not match the outputter's specified
     * encoding.
     *
     * @param text <code>Text</code> to format.
     */
    public String outputString(Text text) {
        StringWriter out = new StringWriter();
        try {
            output(text, out);  // output() flushes
        } catch (IOException e) { }
        return out.toString();
    }

    /**
     * Return a string representing (with trimming, normalization, and
     * escaping possibly applied) a <code>String</code>. Warning: a
     * String is Unicode, which may not match the outputter's specified
     * encoding.
     *
     * @param str <code>String</code> to format.
     */
    public String outputString(String str) {
        StringWriter out = new StringWriter();
        try {
            output(str, out);  // output() flushes
        } catch (IOException e) { }
        return out.toString();
    }

    /**
     * Return a string representing a comment. Warning: a String is
     * Unicode, which may not match the outputter's specified
     * encoding.
     *
     * @param comment <code>Comment</code> to format.
     */
    public String outputString(Comment comment) {
        StringWriter out = new StringWriter();
        try {
            output(comment, out);  // output() flushes
        } catch (IOException e) { }
        return out.toString();
    }

    /**
     * Return a string representing a PI. Warning: a String is
     * Unicode, which may not match the outputter's specified
     * encoding.
     *
     * @param pi <code>ProcessingInstruction</code> to format.
     */
    public String outputString(ProcessingInstruction pi) {
        StringWriter out = new StringWriter();
        try {
            output(pi, out);  // output() flushes
        } catch (IOException e) { }
        return out.toString();
    }

   /**
     * Return a string representing an entity. Warning: a String is
     * Unicode, which may not match the outputter's specified
     * encoding.
     *
     * @param entity <code>EntityRef</code> to format.
     */
    public String outputString(EntityRef entity) {
        StringWriter out = new StringWriter();
        try {
            output(entity, out);  // output() flushes
        } catch (IOException e) { }
        return out.toString();
    }

    // * * * * * * * * * * Internal printing methods * * * * * * * * * *
    // * * * * * * * * * * Internal printing methods * * * * * * * * * *

    /**
     * This will handle printing of the declaration.
     * Assumes XML version 1.0 since we don't directly know.
     *
     * @param doc <code>Document</code> whose declaration to write.
     * @param out <code>Writer</code> to use.
     * @param encoding The encoding to add to the declaration
     */
    protected void printDeclaration(Document doc, Writer out,
                                    String encoding) throws IOException {

        // Only print the declaration if it's not being omitted
        if (!omitDeclaration) {
            // Assume 1.0 version
            out.write("<?xml version=\"1.0\"");
            if (!omitEncoding) {
                out.write(" encoding=\"" + encoding + "\"");
            }
            out.write("?>");

            // Print new line after decl always, even if no other new lines
            // Helps the output look better and is semantically
            // inconsequential
            out.write( currentFormat.lineSeparator);
        }
    }

    /**
     * This handle printing the DOCTYPE declaration if one exists.
     *
     * @param doc <code>Document</code> whose declaration to write.
     * @param out <code>Writer</code> to use.
     */
    protected void printDocType(DocType docType, Writer out)
                        throws IOException {

        String publicID = docType.getPublicID();
        String systemID = docType.getSystemID();
        String internalSubset = docType.getInternalSubset();
        boolean hasPublic = false;

        out.write("<!DOCTYPE ");
        out.write(docType.getElementName());
        if (publicID != null) {
            out.write(" PUBLIC \"");
            out.write(publicID);
            out.write("\"");
            hasPublic = true;
        }
        if (systemID != null) {
            if (!hasPublic) {
                out.write(" SYSTEM");
            }
            out.write(" \"");
            out.write(systemID);
            out.write("\"");
        }
        if ((internalSubset != null) && (!internalSubset.equals(""))) {
            out.write(" [\n");
            out.write(docType.getInternalSubset());
            out.write("]");
        }
        out.write(">");

        // Always print line separator after declaration, helps the
        // output look better and is semantically inconsequential
        out.write( currentFormat.lineSeparator);
    }

    /**
     * This will handle printing of comments.
     *
     * @param comment <code>Comment</code> to write.
     * @param out <code>Writer</code> to use.
     */
    protected void printComment(Comment comment, Writer out)
                       throws IOException {
        out.write("<!--");
        out.write(comment.getText());
        out.write("-->");
    }

    /**
     * This will handle printing of processing instructions.
     *
     * @param comment <code>ProcessingInstruction</code> to write.
     * @param out <code>Writer</code> to use.
     */
    protected void printProcessingInstruction(ProcessingInstruction pi,
                                              Writer out) throws IOException {
        String target = pi.getTarget();
        String rawData = pi.getData();

        // Write <?target data?> or if no data then just <?target?>
        if (!"".equals(rawData)) {
            out.write("<?");
            out.write(target);
            out.write(" ");
            out.write(rawData);
            out.write("?>");
        }
        else {
            out.write("<?");
            out.write(target);
            out.write("?>");
        }
    }

    /**
     * This will handle printing a <code>{@link EntityRef}</code>.
     * Only the entity reference such as <code>&amp;entity;</code>
     * will be printed. However, subclasses are free to override
     * this method to print the contents of the entity instead.
     *
     * @param entity <code>EntityRef</code> to output.
     * @param out <code>Writer</code> to use.  */
    protected void printEntityRef(EntityRef entity, Writer out)
                       throws IOException {
        out.write("&");
        out.write(entity.getName());
        out.write(";");
    }

    /**
     * This will handle printing of <code>{@link CDATA}</code> text.
     *
     * @param cdata <code>CDATA</code> to output.
     * @param out <code>Writer</code> to use.
     */
    protected void printCDATA(CDATA cdata, Writer out) throws IOException {
        String str = currentFormat.textNormalize
                     ? cdata.getTextNormalize()
                     : ((currentFormat.textTrim) ? cdata.getText().trim()
                                                 : cdata.getText());
        out.write("<![CDATA[");
        out.write( str);
        out.write("]]>");
    }

    /**
     * This will handle printing of <code>{@link Text}</code> strings.
     *
     * @param text <code>Text</code> to write.
     * @param out <code>Writer</code> to use.
     */
    protected void printText(Text text, Writer out) throws IOException {
        String str = currentFormat.textNormalize
                     ? text.getTextNormalize()
                     : ((currentFormat.textTrim) ? text.getText().trim()
                                                 : text.getText());
        out.write( escapeElementEntities( str));
    }

    /**
     * This will handle printing a string.  Escapes the element entities,
     * trims interior whitespace, etc. if necessary.
     */
    protected void printString(String str, Writer out) throws IOException {
        if (currentFormat.textNormalize) {
            str = Text.normalizeString( str);
        }
        else if (currentFormat.textTrim) {
            str = str.trim();
        }
        out.write( escapeElementEntities( str));
    }

    /**
     * This will handle printing of a <code>{@link Element}</code>,
     * its <code>{@link Attribute}</code>s, and all contained (child)
     * elements, etc.
     *
     * @param element <code>Element</code> to output.
     * @param out <code>Writer</code> to use.
     * @param indent <code>int</code> level of indention.
     * @param namespaces <code>List</code> stack of Namespaces in scope.
     */
    protected void printElement(Element element, Writer out,
                                int level, NamespaceStack namespaces)
                       throws IOException {

        List attributes = element.getAttributes();
        List content = element.getContent();

        // Check for xml:space and adjust format settings
        String space = null;
        if (attributes != null) {
            space = element.getAttributeValue( "space",
                                               Namespace.XML_NAMESPACE);
        }

        Format previousFormat = currentFormat;

        if ("default".equals( space)) {
            currentFormat = defaultFormat;
        }
        else if ("preserve".equals( space)) {
            currentFormat = noFormatting;
        }

        // Print the beginning of the tag plus attributes and any
        // necessary namespace declarations
        out.write("<");
        out.write(element.getQualifiedName());

        // Mark our namespace starting point
        int previouslyDeclaredNamespaces = namespaces.size();

        // Print the element's namespace, if appropriate
        printElementNamespace(element, out, namespaces);

        // Print out additional namespace declarations
        printAdditionalNamespaces(element, out, namespaces);

        // Print out attributes
        if (attributes != null)
            printAttributes( attributes, element, out, namespaces);

        // Depending on the settings (newlines, textNormalize, etc), we may
        // or may not want to print all of the content, so determine the
        // index of the start of the content we're interested
        // in based on the current settings.

        int start = skipLeadingWhite( content, 0);
        int size = content.size();
        if (start >= size) {
            // Case content is empty or all insignificant whitespace
            if (currentFormat.expandEmptyElements) {
                out.write("></");
                out.write(element.getQualifiedName());
                out.write(">");
            }
            else {
                out.write(" />");
            }
        }
        else {
            out.write(">");

            // For a special case where the content is only CDATA
            // or Text we don't want to indent after the start or
            // before the end tag.

            if (nextNonText( content, start) < size) {
                // Case Mixed Content - normal indentation
                newline(out);
                printContentRange(content, start, size, out,
                                  level + 1, namespaces);
                newline(out);
                indent(out, level);
            }
            else {
                // Case all CDATA or Text - no indentation
                printTextRange(content, start, size, out);
            }
            out.write("</");
            out.write(element.getQualifiedName());
            out.write(">");
        }

        // remove declared namespaces from stack
        while (namespaces.size() > previouslyDeclaredNamespaces) {
            namespaces.pop();
        }

        // Restore our format settings
        currentFormat = previousFormat;
    }

    /**
     * This will handle printing of content within a given range.
     * The range to print is specified in typical Java fashion; the
     * starting index is inclusive, while the ending index is
     * exclusive.
     *
     * @param content <code>List</code> of content to output
     * @param starting index of first content node (inclusive.
     * @param ending index of last content node (exclusive).
     * @param out <code>Writer</code> to use.
     * @param level <code>int</code> level of indentation.
     * @param namespaces <code>List</code> stack of Namespaces in scope.
     */
    protected void printContentRange(List content, int start, int end,
                                     Writer out, int level,
                                     NamespaceStack namespaces)
                       throws IOException {
        boolean firstNode; // Flag for 1st node in content
        Object next;       // Node we're about to print
        int first, index;  // Indexes into the list of content

        index = start;
        while( index < end) {
            firstNode = (index == start) ? true : false;
            next = content.get( index);

            //
            // Handle consecutive CDATA and Text nodes all at once
            //
            if (next instanceof Text) {
                first = skipLeadingWhite( content, index);
                // Set index to next node for loop
                index = nextNonText( content, first);

                // If it's not all whitespace - print it!
                if (first < index) {
                    if (!firstNode)
                        newline(out);
                    indent(out, level);
                    printTextRange( content, first, index, out);
                }
                continue;
            }

            //
            // Handle other nodes
            //
            if (!firstNode) {
                newline(out);
            }

            indent(out, level);

            if (next instanceof Comment) {
                printComment((Comment)next, out);
            }
            else if (next instanceof Element) {
                printElement((Element)next, out, level, namespaces);
            }
            else if (next instanceof EntityRef) {
                printEntityRef((EntityRef)next, out);
            }
            else if (next instanceof ProcessingInstruction) {
                printProcessingInstruction((ProcessingInstruction)next, out);
            }
            else {
                // XXX if we get here then we have a illegal content, for
                //     now we'll just ignore it (probably should throw
                //     a exception)
            }

            index++;
        } /* while */
    }

    /**
     * This will handle printing of a sequence of <code>{@link CDATA}</code>
     * or <code>{@link Text}</code> nodes.  It is a error to have any other
     * pass this method any other type of node.
     *
     * @param content <code>List</code> of content to output
     * @param starting index of first content node (inclusive).
     * @param ending index of last content node (exclusive).
     * @param out <code>Writer</code> to use.
     */
    protected void printTextRange(List content, int start, int end,
                                  Writer out) throws IOException {
        String previous; // Previous text printed
        Object node;     // Next node to print
        String next;     // Next text to print

        previous = null;

        // Remove leading whitespace-only nodes
        start = skipLeadingWhite( content, start);

        int size = content.size();
        if (start < size) {
            // And remove trialing whitespace-only nodes
            end = skipTrialingWhite( content, end);

            for( int i = start; i < end; i++) {
                node = content.get( i);

                // Get the unmangled version of the text
                // we are about to print
                next = ((Text) node).getText();

                // This may save a little time
                if (next == null || "".equals( next)) {
                    continue;
                }

                // Determine if we need to pad the output (padding is
                // only need in trim or normalizing mode)
                if (previous != null) { // Not 1st node
                    if (currentFormat.textNormalize ||
                        currentFormat.textTrim) {
                            if ((endsWithWhite(previous)) ||
                                (startsWithWhite(next))) {
                                    out.write(" ");
                            }
                    }
                }

                // Print the node
                if (node instanceof CDATA) {
                    printCDATA( (CDATA) node, out);
                }
                else {
                    printString( next, out);
                }

                previous = next;
            }
        }
    }

    /**
     * This will handle printing of any needed <code>{@link Namespace}</code>
     * declarations.
     *
     * @param ns <code>Namespace</code> to print definition of
     * @param out <code>Writer</code> to use.
     */
    private void printNamespace(Namespace ns, Writer out,
                                NamespaceStack namespaces)
                     throws IOException {
        String prefix = ns.getPrefix();
        String uri = ns.getURI();

        // Already printed namespace decl?
        if (uri.equals( namespaces.getURI(prefix))) {
            return;
        }

        out.write(" xmlns");
        if (!prefix.equals("")) {
            out.write(":");
            out.write(prefix);
        }
        out.write("=\"");
        out.write(uri);
        out.write("\"");
        namespaces.push(ns);
    }

    /**
     * This will handle printing of a <code>{@link Attribute}</code> list.
     *
     * @param attributes <code>List</code> of Attribute objcts
     * @param out <code>Writer</code> to use
     */
    protected void printAttributes(List attributes, Element parent,
                                   Writer out, NamespaceStack namespaces)
                       throws IOException {

        // I do not yet handle the case where the same prefix maps to
        // two different URIs. For attributes on the same element
        // this is illegal; but as yet we don't throw an exception
        // if someone tries to do this
        // Set prefixes = new HashSet();
        for( int i = 0; i < attributes.size(); i++) {
            Attribute attribute = (Attribute) attributes.get( i);
            Namespace ns = attribute.getNamespace();
            if ((ns != Namespace.NO_NAMESPACE) &&
                (ns != Namespace.XML_NAMESPACE)) {
                    printNamespace(ns, out, namespaces);
            }

            out.write(" ");
            out.write(attribute.getQualifiedName());
            out.write("=");

            out.write("\"");
            out.write(escapeAttributeEntities(attribute.getValue()));
            out.write("\"");
        }
    }

    private void printElementNamespace(Element element, Writer out,
                                       NamespaceStack namespaces)
                             throws IOException {
        // Add namespace decl only if it's not the XML namespace and it's
        // not the NO_NAMESPACE with the prefix "" not yet mapped
        // (we do output xmlns="" if the "" prefix was already used and we
        // need to reclaim it for the NO_NAMESPACE)
        Namespace ns = element.getNamespace();
        if (ns == Namespace.XML_NAMESPACE) {
            return;
        }
        if ( !((ns == Namespace.NO_NAMESPACE) &&
               (namespaces.getURI("") == null))) {
            printNamespace(ns, out, namespaces);
        }
    }

    private void printAdditionalNamespaces(Element element, Writer out,
                                           NamespaceStack namespaces)
                                throws IOException {
        List list = element.getAdditionalNamespaces();
        if (list != null) {
            for( int i = 0; i < list.size(); i++) {
                Namespace additional = (Namespace)list.get( i);
                printNamespace(additional, out, namespaces);
            }
        }
    }

    // * * * * * * * * * * Support methods * * * * * * * * * *
    // * * * * * * * * * * Support methods * * * * * * * * * *

    /**
     * This will print a new line only if the newlines flag was set to
     * true.
     *
     * @param out <code>Writer</code> to use
     */
    protected void newline(Writer out) throws IOException {
        if (currentFormat.newlines) {
            out.write(currentFormat.lineSeparator);
        }
    }

    /**
     * This will print indents (only if the newlines flag was
     * set to <code>true</code>, and indent is non-null).
     *
     * @param out <code>Writer</code> to use
     * @param level current indent level (number of tabs)
     */
    protected void indent(Writer out, int level) throws IOException {
        if (currentFormat.newlines) {
            if (currentFormat.indent == null ||
                currentFormat.indent.equals("")) {
                    return;
            }

            for (int i = 0; i < level; i++) {
                out.write(currentFormat.indent);
            }
        }
    }

    // Returns the index of the first non-all-whitespace CDATA or Text,
    // index = content.size() is returned if content contains
    // all whitespace.
    // @param start index to begin search (inclusive)
    private int skipLeadingWhite( List content, int start) {
        if (start < 0) {
            start = 0;
        }

        int index = start;
        int size = content.size();
        if (currentFormat.trimAllWhite
                || currentFormat.textNormalize
                || currentFormat.textTrim
                || currentFormat.newlines) {
            while( index < size) {
                if ( !isAllWhitespace( content.get(index))) {
                    return index;
                }
                index++;
            }
        }
        return index;
    }

    // Return the index + 1 of the last non-all-whitespace CDATA or
    // Text node,  index < 0 is returned
    // if content contains all whitespace.
    // @param start index to begin search (exclusive)
    private int skipTrialingWhite( List content, int start) {
        int size = content.size();
        if (start > size) {
            start = size;
        }

        int index = start;
        if (currentFormat.trimAllWhite
                || currentFormat.textNormalize
                || currentFormat.textTrim
                || currentFormat.newlines) {
            while( index >= 0) {
                if ( !isAllWhitespace( content.get(index - 1)))
                    break;
                --index;
            }
        }
        return index;
    }

    // Return the next non-CDATA or non-Text node, index = content.size()
    // is returned if there is no more non-CDATA or non-Text nodes
    // @param start index to begin search (inclusive)
    private int nextNonText( List content, int start) {
        if (start < 0) {
            start = 0;
        }

        int index = start;
        int size = content.size();
        while( index < size) {
            if ( !(content.get( index) instanceof Text)) {
                return index;
            }
            index++;
        }
        return size;
    }

    // Determine if a Object is all whitespace
    private boolean isAllWhitespace(Object obj) {
        String str = null;

        if (obj instanceof String) {
            str = (String) obj;
        }
        else if (obj instanceof Text) {
            str = ((Text) obj).getText();
        }
        else {
            return false;
        }

        for (int i = 0; i < str.length(); i++) {
            if ( !isWhitespace( str.charAt( i)))
                return false;
        }
        return true;
    }

    // Determine if a string starts with a XML whitespace.
    private boolean startsWithWhite(String str) {
        if ((str != null) &&
            (str.length() > 0) &&
            isWhitespace( str.charAt(0))) {
           return true;
        }
        return false;
    }

    // Determine if a string ends with a XML whitespace.
    private boolean endsWithWhite(String str) {
        if ((str != null) &&
            (str.length() > 0) &&
            isWhitespace( str.charAt(str.length() - 1))) {
           return true;
        }
        return false;
    }

    // Determine if a character is a XML whitespace.
    // XXX should this method be in Verifier
    private boolean isWhitespace(char ch) {
        if (" \t\n\r".indexOf(ch) < 0) {
            return false;
        }
        return true;
    }

    /**
     * This will take the pre-defined entities in XML 1.0 and
     * convert their character representation to the appropriate
     * entity reference, suitable for XML attributes.  It does
     * no converstion for ' because it's not necessary as the outputter
     * writes attributes surrounded by double-quotes.
     *
     * @param str <code>String</code> input to escape.
     * @return <code>String</code> with escaped content.
     */
    public String escapeAttributeEntities(String str) {
        StringBuffer buffer;
        char ch;
        String entity;

        buffer = null;
        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            switch(ch) {
                case '<' :
                    entity = "&lt;";
                    break;
                case '>' :
                    entity = "&gt;";
                    break;
/*
                case '\'' :
                    entity = "&apos;";
                    break;
*/
                case '\"' :
                    entity = "&quot;";
                    break;
                case '&' :
                    entity = "&amp;";
                    break;
                default :
                    entity = null;
                    break;
            }
            if (buffer == null) {
                if (entity != null) {
                    // An entity occurred, so we'll have to use StringBuffer
                    // (allocate room for it plus a few more entities).
                    buffer = new StringBuffer(str.length() + 20);
                    // Copy previous skipped characters and fall through
                    // to pickup current character
                    buffer.append(str.substring(0, i));
                    buffer.append(entity);
                }
            }
            else {
                if (entity == null) {
                    buffer.append(ch);
                }
                else {
                    buffer.append(entity);
                }
            }
        }

        // If there were any entities, return the escaped characters
        // that we put in the StringBuffer. Otherwise, just return
        // the unmodified input string.
        return (buffer == null) ? str : buffer.toString();
    }


    /**
     * This will take the three pre-defined entities in XML 1.0
     * (used specifically in XML elements) and convert their character
     * representation to the appropriate entity reference, suitable for
     * XML element content.
     *
     * @param st <code>String</code> input to escape.
     * @return <code>String</code> with escaped content.
     */
    public String escapeElementEntities(String str) {
        StringBuffer buffer;
        char ch;
        String entity;

        buffer = null;
        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            switch(ch) {
                case '<' :
                    entity = "&lt;";
                    break;
                case '>' :
                    entity = "&gt;";
                    break;
                case '&' :
                    entity = "&amp;";
                    break;
                default :
                    entity = null;
                    break;
            }
            if (buffer == null) {
                if (entity != null) {
                    // An entity occurred, so we'll have to use StringBuffer
                    // (allocate room for it plus a few more entities).
                    buffer = new StringBuffer(str.length() + 20);
                    // Copy previous skipped characters and fall through
                    // to pickup current character
                    buffer.append(str.substring(0, i));
                    buffer.append(entity);
                }
            }
            else {
                if (entity == null) {
                    buffer.append(ch);
                }
                else {
                    buffer.append(entity);
                }
            }
        }

        // If there were any entities, return the escaped characters
        // that we put in the StringBuffer. Otherwise, just return
        // the unmodified input string.
        return (buffer == null) ? str : buffer.toString();
    }

    /**
     * Parse command-line arguments of the form <code>-omitEncoding
     * -indentSize 3 &#133;</code>.
     *
     * @return int index of first parameter that we didn't understand
     */
    public int parseArgs(String[] args, int i) {
        for (; i<args.length; ++i) {
            if (args[i].equals("-omitDeclaration")) {
                setOmitDeclaration(true);
            }
            else if (args[i].equals("-omitEncoding")) {
                setOmitEncoding(true);
            }
            else if (args[i].equals("-indent")) {
                setIndent(args[++i]);
            }
            else if (args[i].startsWith("-expandEmpty")) {
                setExpandEmptyElements(true);
            }
            else if (args[i].equals("-encoding")) {
                setEncoding(args[++i]);
            }
            else if (args[i].equals("-newlines")) {
                setNewlines(true);
            }
            else if (args[i].equals("-lineSeparator")) {
                setLineSeparator(args[++i]);
            }
            else if (args[i].equals("-trimAllWhite")) {
                setTrimAllWhite(true);
            }
            else if (args[i].equals("-textTrim")) {
                setTextTrim(true);
            }
            else if (args[i].equals("-textNormalize")) {
                setTextNormalize(true);
            }
            else {
                return i;
            }
        }
        return i;
    }

    /**
     * Returns a copy of this XMLOutputter.
     */
    public Object clone() {
        // Implementation notes: Since all state of an XMLOutputter is
        // embodied in simple private instance variables, Object.clone
        // can be used.  Note that since Object.clone is totally
        // broken, we must catch an exception that will never be
        // thrown.
        try {
            return super.clone();
        }
        catch (java.lang.CloneNotSupportedException e) {
            // even though this should never ever happen, it's still
            // possible to fool Java into throwing a
            // CloneNotSupportedException.  If that happens, we
            // shouldn't swallow it.
            throw new RuntimeException(e.toString());
        }
    }

    /**
     * Return a string listing of the settings for this
     * XMLOutputter instance.
     *
     * @return a string listing the settings for this XMLOutputter instance
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < defaultFormat.lineSeparator.length(); i++) {
            char ch = defaultFormat.lineSeparator.charAt( i);
            switch (ch) {
            case '\r': buffer.append("\\r");
                       break;
            case '\n': buffer.append("\\n");
                       break;
            case '\t': buffer.append("\\t");
                       break;
            default:   buffer.append("[" + ((int)ch) + "]");
                       break;
            }
        }

        return (
            "XMLOutputter[omitDeclaration = " + omitDeclaration + ", " +
            "encoding = " + encoding + ", " +
            "omitEncoding = " + omitEncoding + ", " +
            "indent = '" + defaultFormat.indent + "'" + ", " +
            "expandEmptyElements = " + defaultFormat.expandEmptyElements + ", " +
            "newlines = " + defaultFormat.newlines + ", " +
            "lineSeparator = '" + buffer.toString() + "', " +
            "trimAllWhite = " + defaultFormat.trimAllWhite +
            "textTrim = " + defaultFormat.textTrim +
            "textNormalize = " + defaultFormat.textNormalize + "]"
        );
    }

    /**
     * Factory for making new NamespaceStack objects.  The NamespaceStack
     * created is actually an inner class extending the package protected
     * NamespaceStack, as a way to make NamespaceStack "friendly" toward
     * subclassers.
     */
    protected NamespaceStack createNamespaceStack() {
       // actually returns a XMLOutputter.NamespaceStack (see below)
       return new NamespaceStack();
    }

    /**
     * Our own null subclass of NamespaceStack.  This plays a little
     * trick with Java access protection.  We want subclasses of
     * XMLOutputter to be able to override protected methods that
     * declare a NamespaceStack parameter, but we don't want to
     * declare the parent NamespaceStack class as public.
     */
    protected class NamespaceStack
        extends org.jdom.output.NamespaceStack
    {
    }

    // * * * * * * * * * * Deprecated methods * * * * * * * * * *
    // * * * * * * * * * * Deprecated methods * * * * * * * * * *

    /**
     * Print out a <code>{@link java.lang.String}</code>.  Perfoms
     * the necessary entity escaping and whitespace stripping.  </p>
     *
     * @param string <code>String</code> to output.
     * @param out <code>OutputStream</code> to use.
     * @deprecated Deprecated in beta8, see {@link #output(Text,OutputStream)}
     */
    public void output(String string, OutputStream out) throws IOException {
        Writer writer = makeWriter(out);
        output(string, writer);  // output() flushes
    }

    /**
     * Print out a <code>{@link java.lang.String}</code>.  Perfoms
     * the necessary entity escaping and whitespace stripping.
     *
     * @param string <code>String</code> to output.
     * @param out <code>Writer</code> to use.
     * @deprecated Deprecated in beta8, see {@link #output(Text,Writer)}
     */
    public void output(String string, Writer out) throws IOException {
        printString(string, out);
        out.flush();
    }

    /**
     * Set the indent on or off, newlines must be set to <code>true</code>
     * for indentation to actually occur.  If setting on, will use the
     * value of STANDARD_INDENT, which is usually two spaces.
     * @deprecated Deprecated in beta9, use setIndent(String) instead
     *
     * @param doIndent if true, set indenting on; if false, set indenting off
     */
    public void setIndent(boolean doIndent) {
        if (doIndent) {
            defaultFormat.indent = STANDARD_INDENT;
        }
        else {
            defaultFormat.indent = null;
        }
    }

    /**
     * This will set the indent <code>String</code>'s size; a size
     * of 4 would result in the indentation being equivalent to the
     * <code>String</code> "&nbsp;&nbsp;&nbsp;&nbsp;" (four spaces).
     * @deprecated Deprecated in beta9, use setIndent(String) instead
     *
     * @param size <code>int</code> number of spaces in indentation.
     */
    public void setIndent(int size) {
        setIndentSize(size);
    }

    /**
     * This will set the indent <code>String</code>'s size; an indentSize
     * of 4 would result in the indentation being equivalent to the
     * <code>String</code> "&nbsp;&nbsp;&nbsp;&nbsp;" (four spaces).
     *
     * @deprecated Deprecated in beta9, use setIndent(String) instead
     *
     * @param indentSize <code>int</code> number of spaces in indentation.
     */
    public void setIndentSize(int indentSize) {
        StringBuffer indentBuffer = new StringBuffer();
        for (int i=0; i<indentSize; i++) {
            indentBuffer.append(" ");
        }
        defaultFormat.indent = indentBuffer.toString();
    }

}
