/*-- 

 $Id: XMLOutputter.java,v 1.66 2001/11/26 14:53:28 bmclaugh Exp $

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
 created by Brett McLaughlin <brett@jdom.org> and 
 Jason Hunter <jhunter@jdom.org>.  For more information on the 
 JDOM Project, please see <http://www.jdom.org/>.
 
 */

package org.jdom.output;

import java.io.*;
import java.util.*;
   
import org.jdom.*;

/**
 * <p><code>XMLOutputter</code> takes a JDOM tree and formats it to a
 * stream as XML.  The outputter can manage many styles of document
 * formatting, from untouched to pretty printed.  The default constructor
 * creates an outputter to output the document exactly as created.  
 * Constructor parameters control the indent amount and whether new lines 
 * are printed between elements.  The other parameters are configurable
 * through the <code>set*</code> methods.
 * The XML declaration is always printed on its own line.  Empty elements 
 * are by default printed as &lt;empty/&gt but that can be configured.
 * Text-only contents are printed as &lt;tag&gt;content&lt;/tag&gt; on a 
 * single line.</p>
 *
 * <p>For compact machine-readable output create a default
 * XMLOutputter and call setTextNormalize(true) to normalize any whitespace
 * that was preserved from the source.</p>
 *
 * <p>For pretty output, set the indent to "  ", set the new lines feature 
 * to true, and set text trimming to true.</p>
 *
 * <p> There are <code>output(...)</code> methods to print any of the
 * standard JDOM classes, including <code>Document</code> and
 * <code>Element</code>, to either a <code>Writer</code> or an
 * <code>OutputStream</code>.  Warning: When outputting to a Writer, make
 * sure the writer's encoding matches the encoding setting in the
 * XMLOutputter.  This ensures the encoding in which the content is written
 * (controlled by the Writer configuration) matches the encoding placed in
 * the document's XML declaration (controlled by the XMLOutputter).  Because a
 * Writer cannot be queried for its encoding, the information must be passed 
 * to the XMLOutputter manually in its constructor or via the setEncoding() 
 * method.  The default XMLOutputter encoding is UTF-8.
 * </p>
 *
 * <p> The methods <code>outputString(...)</code> are for convenience
 * only; for top performance you should call <code>output(...)</code>
 * and pass in your own <code>Writer</code> or
 * <code>OutputStream</code> to if possible.  </p>
 *
 * @author Brett McLaughlin
 * @author Jason Hunter
 * @author Jason Reid
 * @author Wolfgang Werner
 * @author Elliotte Rusty Harold
 * @author David &amp; Will (from Post Tool Design)
 * @author Dan Schaffer
 * @author Alex Chaffee (alex@jguru.com)
 * @version 1.0 
 */
public class XMLOutputter implements Cloneable {

    private static final String CVS_ID = 
      "@(#) $RCSfile: XMLOutputter.java,v $ $Revision: 1.66 $ $Date: 2001/11/26 14:53:28 $ $Name:  $";

    /** standard value to indent by, if we are indenting **/
    protected static final String STANDARD_INDENT = "  ";
    
    /** standard string with which to end a line **/
    protected static final String STANDARD_LINE_SEPARATOR = "\r\n";

    /** Whether or not to output the XML declaration 
      * - default is <code>false</code> */
    private boolean omitDeclaration = false;

    /** The encoding format */
    private String encoding = "UTF-8";

    /** Whether or not to output the encoding in the XML declaration 
      * - default is <code>false</code> */
    private boolean omitEncoding = false;

    /** The default indent is no spaces (as original document) */
    private String indent = null;

    /** Whether or not to expand empty elements to 
      * &lt;tagName&gt;&lt;/tagName&gt; - default is <code>false</code> */
    private boolean expandEmptyElements = false;

    /** The default new line flag, set to do new lines only as in 
      * original document */
    private boolean newlines = false;

    /** New line separator */
    private String lineSeparator = STANDARD_LINE_SEPARATOR;

    /** Should we preserve whitespace or not in text nodes */
    private boolean textNormalize = false;

    /**
     * <p>
     * This will create an <code>XMLOutputter</code> with
     *   no additional whitespace (indent or new lines) added;
     *   the whitespace from the element text content is fully preserved.
     * </p>
     */
    public XMLOutputter() {
    }

    /**
     * <p>
     * This will create an <code>XMLOutputter</code> with
     *   the given indent added but no new lines added;
     *   all whitespace from the element text content is included as well.
     * </p>
     *
     * @param indent  the indent string, usually some number of spaces
     */
    public XMLOutputter(String indent) {
       this.indent = indent;
    }

    /**
     * <p>
     * This will create an <code>XMLOutputter</code> with
     *   the given indent that prints newlines only if <code>newlines</code> is
     *   <code>true</code>; 
     *   all whitespace from the element text content is included as well.
     * </p>
     *
     * @param indent the indent <code>String</code>, usually some number
     *        of spaces
     * @param newlines <code>true</code> indicates new lines should be
     *                 printed, else new lines are ignored (compacted).
     */
    public XMLOutputter(String indent, boolean newlines) {
       this.indent = indent;
       this.newlines = newlines;
    }

    /**
     * <p>
     * This will create an <code>XMLOutputter</code> with
     *   the given indent and new lines printing only if newlines is
     *   <code>true</code>, and encoding format <code>encoding</code>.
     * </p>
     *
     * @param indent the indent <code>String</code>, usually some number
     *        of spaces
     * @param newlines <code>true</code> indicates new lines should be
     *                 printed, else new lines are ignored (compacted).
     * @param encoding set encoding format.  Use XML-style names like
     *                 "UTF-8" or "ISO-8859-1" or "US-ASCII"
     */
    public XMLOutputter(String indent, boolean newlines, String encoding) {
       this.indent = indent;
       this.newlines = newlines;
       this.encoding = encoding;
    }

    /**
     * <p> This will create an <code>XMLOutputter</code> with all the
     * options as set in the given <code>XMLOutputter</code>.  Note
     * that <code>XMLOutputter two = (XMLOutputter)one.clone();</code>
     * would work equally well.  </p>
     *
     * @param that the XMLOutputter to clone
     **/
    public XMLOutputter(XMLOutputter that) {
        this.omitDeclaration = that.omitDeclaration;
        this.omitEncoding = that.omitEncoding;
        this.indent = that.indent;
        this.expandEmptyElements = that.expandEmptyElements;
        this.newlines = that.newlines;
        this.encoding = that.encoding;
        this.lineSeparator = that.lineSeparator;
        this.textNormalize = that.textNormalize;
    }

    /**
     * Returns a copy of this XMLOutputter.
     **/
    public Object clone() {
 // Implementation notes: Since all state of an XMLOutputter is
 // embodied in simple private instance variables, Object.clone
 // can be used.  Note that since Object.clone is totally
 // broken, we must catch an exception that will never be
 // thrown.
 // (See 
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
     * <p>This will set the new-line separator. The default is
     * <code>\r\n</code>. Note that if the "newlines" property is
     * false, this value is irrelevant.  To make it output the system
     * default line ending string, call
     * <code>setLineSeparator(System.getProperty("line.separator"))</code>
     * </p>
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
     **/
    public void setLineSeparator(String separator) {
        lineSeparator = separator;
    }

    /**
     * @see #setLineSeparator(String)
     * @param newlines <code>true</code> indicates new lines should be
     *                 printed, else new lines are ignored (compacted).
     **/
    public void setNewlines(boolean newlines) {
        this.newlines = newlines;
    }

    /**
     * Sets the output encoding.  The name should be an accepted XML
     * encoding.
     *
     * @param encoding the encoding format.  Use XML-style names like
     *                 "UTF-8" or "ISO-8859-1" or "US-ASCII"
     **/
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * <p>
     *  This will set whether the XML declaration 
     *  (<code>&lt;?xml version="1.0" encoding="UTF-8"?&gt;</code>)
     *  includes the encoding of the document. It is common to omit 
     *  this in uses such as WML and other wireless device protocols.
     * </p>
     *
     * @param omitEncoding <code>boolean</code> indicating whether or not
     *        the XML declaration should indicate the document encoding.
     */
    public void setOmitEncoding(boolean omitEncoding) {
        this.omitEncoding = omitEncoding;
    }

    /**
     * <p>
     *  This will set whether the XML declaration 
     *  (<code>&lt;?xml version="1.0"?&gt;</code>)
     *  will be omitted or not. It is common to omit this in uses such
     *  as SOAP and XML-RPC calls.
     * </p>
     *
     * @param omitDeclaration <code>boolean</code> indicating whether or not
     *        the XML declaration should be omitted.
     */
    public void setOmitDeclaration(boolean omitDeclaration) {
        this.omitDeclaration = omitDeclaration;
    }

    /**
     * <p>
     *  This will set whether empty elements are expanded from 
     *  <code>&lt;tagName&gt;</code> to
     *  <code>&lt;tagName&gt;&lt;/tagName&gt;</code>.
     * </p>
     *
     * @param expandEmptyElements <code>boolean</code> indicating whether or not
     *        empty elements should be expanded.
     */
    public void setExpandEmptyElements(boolean expandEmptyElements) {
        this.expandEmptyElements = expandEmptyElements;
    }

    /**
     * <p> This will set whether the text is output verbatim (false)
     *  or with whitespace normalized as per <code>{@link
     *  org.jdom.Element#getTextNormalize()}</code>.<p>
     *
     * <p>Default: false </p>
     *
     * @param textNormalize <code>boolean</code> true=>normalize the 
     * whitespace, false=>use text verbatim
     **/
    public void setTextNormalize(boolean textNormalize) {
        this.textNormalize = textNormalize;
    }

    /**
     * <p> This will set whether the text is output verbatim (false)
     *  or with whitespace stripped.<p>
     *
     * <p>Default: false </p>
     *
     * @param trimText <code>boolean</code> true=>trim the whitespace, 
     * false=>use text verbatim
     *
     * @deprecated Deprecated in beta7, use setTextNormalize() instead
     **/
    public void setTrimText(boolean textTrim) {
        this.textNormalize = textTrim;
    }

    /**
     * <p> This will set the indent <code>String</code> to use; this
     *   is usually a <code>String</code> of empty spaces. If you pass
     *   null, or the empty string (""), then no indentation will
     *   happen. </p>
     * Default: none (null)
     *
     * @param indent <code>String</code> to use for indentation.
     **/
    public void setIndent(String indent) {
        // if passed the empty string, change it to null, for marginal
        // performance gains later (can compare to null first instead
        // of calling equals())
        if ("".equals(indent))
            indent = null;
        this.indent = indent;
    }

    /**
     * Set the indent on or off.  If setting on, will use the value of
     * STANDARD_INDENT, which is usually two spaces.
     *
     * @param doIndent if true, set indenting on; if false, set indenting off
     **/
    public void setIndent(boolean doIndent) {
        if (doIndent) { 
            this.indent = STANDARD_INDENT;
        }
        else {
            this.indent = null;
        }
    }

    /**
     * <p>
     *   This will set the indent <code>String</code>'s size; an indentSize
     *   of 4 would result in the indentation being equivalent to the 
     *   <code>String</code> "&nbsp;&nbsp;&nbsp;&nbsp;" (four space chars).
     * </p>
     *
     * @param indentSize <code>int</code> number of spaces in indentation.
     */
    public void setIndentSize(int indentSize) {
        StringBuffer indentBuffer = new StringBuffer();
        for (int i=0; i<indentSize; i++) {
            indentBuffer.append(" ");
        }
        this.indent = indentBuffer.toString();
    }

    /**
     * <p>
     * This will print the proper indent characters for the given indent level.
     * </p>
     *
     * @param out <code>Writer</code> to write to
     * @param level <code>int</code> indentation level
     */
    protected void indent(Writer out, int level) throws IOException {
        if (indent != null && !indent.equals("")) {
            for (int i = 0; i < level; i++) {
                out.write(indent);
            }
        }
    }
    
    /**
     * <p>
     * This will print a new line only if the newlines flag was set to true
     * </p>
     *
     * @param out <code>Writer</code> to write to
     */
    protected void maybePrintln(Writer out) throws IOException  {
        maybePrintln(out, 0);
    }

    /**
     * <p>     
     * This will print a new line only if the newlines flag was set to
     * true, and then print indents (only if indent is non-null)
     * </p>
     *
     * @param out <code>Writer</code> to write to
     * @param indentLevel current indent level (number of tabs)
     */
    protected void maybePrintln(Writer out, int indentLevel) throws IOException {
        if (newlines) {
            out.write(lineSeparator);
        }
        indent(out, indentLevel);
    }

    /**
     * Get an OutputStreamWriter, use preferred encoding.
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
        // "UTF-8" is not recognized before JDK 1.1.6, so we'll translate into 
        // "UTF8" which works with all JDKs. 
        if ("UTF-8".equals(enc)) {
            enc = "UTF8";
        }

        Writer writer = new BufferedWriter
            (new OutputStreamWriter
            (new BufferedOutputStream(out), enc));
        return writer;
    }
    
    /**
     * <p>
     * This will print the <code>Document</code> to the given output stream.
     *   The characters are printed using the encoding specified in the
     *   constructor, or a default of UTF-8.
     * </p>
     *
     * @param doc <code>Document</code> to format.
     * @param out <code>OutputStream</code> to write to.
     * @throws IOException - if there's any problem writing.
     */
    public void output(Document doc, OutputStream out)
                                           throws IOException {
        Writer writer = makeWriter(out);
        output(doc, writer);  // output() flushes
    }

    /**
     * <p> This will print the <code>Document</code> to the given
     * Writer.
     * </p>
     *
     * <p> Warning: using your own Writer may cause the outputter's
     * preferred character encoding to be ignored.  If you use
     * encodings other than UTF-8, we recommend using the method that
     * takes an OutputStream instead.  </p>
     *
     * @param doc <code>Document</code> to format.
     * @param out <code>Writer</code> to write to.
     * @throws IOException - if there's any problem writing.
     **/
    public void output(Document doc, Writer out) throws IOException {

        printDeclaration(doc, out, encoding);

        if (doc.getDocType() != null) {
            printDocType(doc.getDocType(), out);
        }
        
        // Print out root element, as well as any root level
        // comments and processing instructions, 
        // starting with no indentation
        Iterator i = doc.getContent().iterator();
        while (i.hasNext()) {
            Object obj = i.next();
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
            maybePrintln(out);
        }

        // Output final line separator
        out.write(lineSeparator);

        out.flush();
    }

    // * * * * * Element * * * * *
    
    /**
     * <p>
     * Print out an <code>{@link Element}</code>, including
     *   its <code>{@link Attribute}</code>s, and its value, and all
     *   contained (child) elements etc.
     * </p>
     *
     * @param element <code>Element</code> to output.
     * @param out <code>Writer</code> to write to.
     **/
    public void output(Element element, Writer out) throws IOException {
        // If this is the root element we could pre-initialize the 
        // namespace stack with the namespaces
        printElement(element, out, 0, createNamespaceStack());
        out.flush();
    }
    
    /**
     * <p>
     * Print out an <code>{@link Element}</code>, including
     *   its <code>{@link Attribute}</code>s, and its value, and all
     *   contained (child) elements etc.
     * </p>
     *
     * @param element <code>Element</code> to output.
     * @param out <code>Writer</code> to write to.
     **/
    public void output(Element element, OutputStream out) throws IOException {
        Writer writer = makeWriter(out);
        output(element, writer);  // output() flushes
    }

    /**
     * <p> This will handle printing out an <code>{@link
     * Element}</code>'s content only, not including its tag, and
     * attributes.  This can be useful for printing the content of an
     * element that contains HTML, like "&lt;description&gt;JDOM is
     * &lt;b&gt;fun&gt;!&lt;/description&gt;".  </p>
     *
     * @param element <code>Element</code> to output.
     * @param out <code>Writer</code> to write to.
     **/
    public void outputElementContent(Element element, Writer out)
                      throws IOException {
        List eltContent = element.getContent();
        printElementContent(element, out, 0, createNamespaceStack(),
                            eltContent);
        out.flush();
    }
    
    /**
     * <p> This will handle printing out an <code>{@link
     * Element}</code>'s content only, not including its tag, and
     * attributes.  This can be useful for printing the content of an
     * element that contains HTML, like "&lt;description&gt;JDOM is
     * &lt;b&gt;fun&gt;!&lt;/description&gt;".  </p>
     *
     * @param element <code>Element</code> to output.
     * @param out <code>OutputStream</code> to write to.
     **/
    public void outputElementContent(Element element, OutputStream out)
                                         throws IOException {
        Writer writer = makeWriter(out);
        outputElementContent(element, writer);  // output() flushes
    }

    // * * * * * CDATA * * * * *

    /**
     * <p>
     * Print out a <code>{@link CDATA}</code>
     * </p>
     *
     * @param cdata <code>CDATA</code> to output.
     * @param out <code>Writer</code> to write to.
     **/
    public void output(CDATA cdata, Writer out) throws IOException {
        printCDATA(cdata, out);
        out.flush();
    }
    
    /**
     * <p>
     * Print out a <code>{@link CDATA}</code>
     * </p>
     *
     * @param cdata <code>CDATA</code> to output.
     * @param out <code>OutputStream</code> to write to.
     **/
    public void output(CDATA cdata, OutputStream out) throws IOException {
        Writer writer = makeWriter(out);
        output(cdata, writer);  // output() flushes
    }

    // * * * * * Comment * * * * *

    /**
     * <p>
     * Print out a <code>{@link Comment}</code>
     * </p>
     *
     * @param comment <code>Comment</code> to output.
     * @param out <code>Writer</code> to write to.
     **/
    public void output(Comment comment, Writer out) throws IOException {
        printComment(comment, out);
        out.flush();
    }
    
    /**
     * <p>
     * Print out a <code>{@link Comment}</code>
     * </p>
     *
     * @param comment <code>Comment</code> to output.
     * @param out <code>OutputStream</code> to write to.
     **/
    public void output(Comment comment, OutputStream out) throws IOException {
        Writer writer = makeWriter(out);
        output(comment, writer);  // output() flushes
    }
    

    // * * * * * String * * * * *

    /**
     * <p> Print out a <code>{@link java.lang.String}</code>.  Perfoms
     * the necessary entity escaping and whitespace stripping.  </p>
     *
     * @param string <code>String</code> to output.
     * @param out <code>Writer</code> to write to.
     **/
    public void output(String string, Writer out) throws IOException {
        printString(string, out);
        out.flush();
    }
    
    /**
     * <p>
     * <p> Print out a <code>{@link java.lang.String}</code>.  Perfoms
     * the necessary entity escaping and whitespace stripping.  </p>
     * </p>
     *
     * @param string <code>String</code> to output.
     * @param out <code>OutputStream</code> to write to.
     **/
    public void output(String string, OutputStream out) throws IOException {
        Writer writer = makeWriter(out);
        output(string, writer);  // output() flushes
    }


    // * * * * * Text * * * * *

    /**
     * <p> Print out a <code>{@link Text}</code>.  Perfoms
     * the necessary entity escaping and whitespace stripping.  </p>
     *
     * @param text <code>Text</code> to output.
     * @param out <code>Writer</code> to write to.
     **/
    public void output(Text text, Writer out) throws IOException {
        printString(text.getValue(), out);
        out.flush();
    }

    /**
     * <p>
     * <p> Print out a <code>{@link Text}</code>.  Perfoms
     * the necessary entity escaping and whitespace stripping.  </p>
     * </p>
     *
     * @param text <code>Text</code> to output.
     * @param out <code>OutputStream</code> to write to.
     **/
    public void output(Text text, OutputStream out) throws IOException {
        Writer writer = makeWriter(out);
        output(text, writer);  // output() flushes
    }
    
    // * * * * * EntityRef * * * * *

    /**
     * <p> Print out an <code>{@link EntityRef}</code>.  
     * </p>
     *
     * @param entity <code>EntityRef</code> to output.
     * @param out <code>Writer</code> to write to.
     **/
    public void output(EntityRef entity, Writer out) throws IOException {
        printEntityRef(entity, out);
        out.flush();
    }
    
    /**
     * <p>
     * Print out an <code>{@link EntityRef}</code>. 
     * </p>
     *
     * @param entity <code>EntityRef</code> to output.
     * @param out <code>OutputStream</code> to write to.
     **/
    public void output(EntityRef entity, OutputStream out) throws IOException {
        Writer writer = makeWriter(out);
        output(entity, writer);  // output() flushes
    }
    

    // * * * * * ProcessingInstruction * * * * *

    /**
     * <p>
     * Print out a <code>{@link ProcessingInstruction}</code>
     * </p>
     *
     * @param element <code>ProcessingInstruction</code> to output.
     * @param out <code>Writer</code> to write to.
     **/
    public void output(ProcessingInstruction pi, Writer out)
                                 throws IOException {
        printProcessingInstruction(pi, out);
        out.flush();
    }
    
    /**
     * <p>
     * Print out a <code>{@link ProcessingInstruction}</code>
     * </p>
     *
     * @param processingInstruction <code>ProcessingInstruction</code> 
     * to output.
     * @param out <code>OutputStream</code> to write to.
     **/
    public void output(ProcessingInstruction pi, OutputStream out)
                                 throws IOException {
        Writer writer = makeWriter(out);
        output(pi, writer);  // output() flushes
    }
    
    /**
     * <p>
     * Print out a <code>{@link DocType}</code>
     * </p>
     *
     * @param doctype <code>DocType</code> to output.
     * @param out <code>Writer</code> to write to.
     **/
    public void output(DocType doctype, Writer out) throws IOException {
        printDocType(doctype, out);
        out.flush();
    }
    
    /**
     * <p>
     * Print out a <code>{@link DocType}</code>
     * </p>
     *
     * @param doctype <code>DocType</code> to output.
     * @param out <code>OutputStream</code> to write to.
     **/
    public void output(DocType doctype, OutputStream out) throws IOException {
        Writer writer = makeWriter(out);
        output(doctype, writer);  // output() flushes
    }
    

    // * * * * * String * * * * *
    
    /**
     * Return a string representing a document.  Uses an internal
     * StringWriter. Warning: a String is Unicode, which may not match
     * the outputter's specified encoding.
     *
     * @param doc <code>Document</code> to format.
     **/
    public String outputString(Document doc) {
        StringWriter out = new StringWriter();
        try {
            output(doc, out);  // output() flushes
        } catch (IOException e) { }
        return out.toString();
    }

    /**
     * Return a string representing an element. Warning: a String is
     * Unicode, which may not match the outputter's specified
     * encoding.
     *
     * @param doc <code>Element</code> to format.
     **/
    public String outputString(Element element) {
        StringWriter out = new StringWriter();
        try {
            output(element, out);  // output() flushes
        } catch (IOException e) { }
        return out.toString();
    }

    /**
     * Return a string representing a comment. Warning: a String is
     * Unicode, which may not match the outputter's specified
     * encoding.
     *
     * @param comment <code>Comment</code> to format.
     **/
    public String outputString(Comment comment) {
        StringWriter out = new StringWriter();
        try {
            output(comment, out);  // output() flushes
        } catch (IOException e) { }
        return out.toString();
    }

    /**
     * Return a string representing a CDATA section. Warning: a String is
     * Unicode, which may not match the outputter's specified
     * encoding.
     *
     * @param cdata <code>CDATA</code> to format.
     **/
    public String outputString(CDATA cdata) {
        StringWriter out = new StringWriter();
        try {
            output(cdata, out);  // output() flushes
        } catch (IOException e) { }
        return out.toString();
    }

    /**
     * Return a string representing a PI. Warning: a String is
     * Unicode, which may not match the outputter's specified
     * encoding.
     *
     * @param doc <code>ProcessingInstruction</code> to format.
     **/
    public String outputString(ProcessingInstruction pi) {
        StringWriter out = new StringWriter();
        try {
            output(pi, out);  // output() flushes
        } catch (IOException e) { }
        return out.toString();
    }

    /**
     * Return a string representing a DocType. Warning: a String is
     * Unicode, which may not match the outputter's specified
     * encoding.
     *
     * @param doc <code>DocType</code> to format.
     **/
    public String outputString(DocType doctype) {
        StringWriter out = new StringWriter();
        try {
            output(doctype, out);  // output() flushes
        } catch (IOException e) { }
        return out.toString();
    }

   /**
     * Return a string representing an entity. Warning: a String is
     * Unicode, which may not match the outputter's specified
     * encoding.
     *
     * @param doc <code>EntityRef</code> to format.
     **/
    public String outputString(EntityRef entity) {
        StringWriter out = new StringWriter();
        try {
            output(entity, out);  // output() flushes
        } catch (IOException e) { }
        return out.toString();
    }

    // * * * * * Internal printing methods * * * * *
    
    /**
     * <p>
     * This will write the declaration to the given Writer.
     *   Assumes XML version 1.0 since we don't directly know.
     * </p>
     *
     * @param doc <code>Document</code> whose declaration to write.
     * @param out <code>Writer</code> to write to.
     * @param encoding The encoding to add to the declaration
     */
    protected void printDeclaration(Document doc,
                                    Writer out,
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
            out.write(lineSeparator);
        }
    }

    /**
     * <p>
     * This will write the DOCTYPE declaration if one exists.
     * </p>
     *
     * @param doc <code>Document</code> whose declaration to write.
     * @param out <code>Writer</code> to write to.
     */
    protected void printDocType(DocType docType, Writer out)
                        throws IOException {
        if (docType == null) {
            return;
        }

        String publicID = docType.getPublicID();
        String systemID = docType.getSystemID();
        boolean hasPublic = false;

        out.write("<!DOCTYPE ");
        out.write(docType.getElementName());
        if ((publicID != null) && (!publicID.equals(""))) {
            out.write(" PUBLIC \"");
            out.write(publicID);
            out.write("\"");
            hasPublic = true;
        }
        if ((systemID != null) && (!systemID.equals(""))) {
            if (!hasPublic) {
                out.write(" SYSTEM");
            }
            out.write(" \"");
            out.write(systemID);
            out.write("\"");
        }
        if (docType.getInternalSubset() != null) {
            out.write(" [\n");
            out.write(docType.getInternalSubset());
            out.write("]");
        }
        out.write(">");

        // Print new line after decl always, even if no other new lines
        // Helps the output look better and is semantically
        // inconsequential
        out.write(lineSeparator);
    }

    /**
     * <p>
     * This will write the comment to the specified writer.
     * </p>
     *
     * @param comment <code>Comment</code> to write.
     * @param out <code>Writer</code> to write to.
     */
    protected void printComment(Comment comment, Writer out)
                                  throws IOException {
        out.write("<!--");
        out.write(comment.getText());
        out.write("-->");
    }
      
    /**
     * <p>
     * This will write the processing instruction to the specified writer.
     * </p>
     *
     * @param comment <code>ProcessingInstruction</code> to write.
     * @param out <code>Writer</code> to write to.
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
     * <p>
     * This will handle printing out an <code>{@link CDATA}</code>,
     *   and its value.
     * </p>
     *
     * @param cdata <code>CDATA</code> to output.
     * @param out <code>Writer</code> to write to.
     */
    protected void printCDATA(CDATA cdata, Writer out)
                                       throws IOException {
        out.write("<![CDATA[");
        out.write(cdata.getText());
        out.write("]]>");
    }

    /**
     * <p>
     * This will handle printing out an <code>{@link Element}</code>,
     *   its <code>{@link Attribute}</code>s, and its value.
     * </p>
     *
     * @param element <code>Element</code> to output.
     * @param out <code>Writer</code> to write to.
     * @param indent <code>int</code> level of indention.
     * @param namespaces <code>List</code> stack of Namespaces in scope.
     */
    protected void printElement(Element element, Writer out,
                                int indentLevel, NamespaceStack namespaces)
                                throws IOException {

        // This method prints from the beginning < to the trailing >.
        // (no leading or trailing whitespace!)

        List eltContent = element.getContent();

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

        printAttributes(element.getAttributes(), element, out, namespaces);

        // Calculate if the contents are String/CDATA only
        // This helps later with the "empty" check
        boolean stringOnly = true;
        Iterator itr = eltContent.iterator();
        while (itr.hasNext()) {
            Object o = itr.next();
            if (!(o instanceof String) && !(o instanceof CDATA)) {
                stringOnly = false;
                break;
            }
        }

        // Calculate if the content is empty
        // We can handle "" string same as empty (CDATA has no effect here)
        boolean empty = false;
        if (stringOnly) {
            String elementText =
                textNormalize ? element.getTextNormalize() : element.getText();
            if (elementText == null || elementText.equals("")) {
                empty = true;
            }
        }

        // If empty, print closing; if not empty, print content
        if (empty) {
            // Simply close up
            if (!expandEmptyElements) {
                out.write(" />");
            }
            else {
                out.write("></");
                out.write(element.getQualifiedName());
                out.write(">");
            }
        }
        else {
            // We know it's not null or empty from above
            out.write(">");
            printElementContent(element, out, indentLevel + 1,
                                namespaces, eltContent);
            out.write("</");
            out.write(element.getQualifiedName());
            out.write(">");
        }

        // remove declared namespaces from stack
        while (namespaces.size() > previouslyDeclaredNamespaces) {
            namespaces.pop();
        }
    }
    
    /**
     * <p> This will handle printing out an <code>{@link
     * Element}</code>'s content only, not including its tag,
     * attributes, and namespace info.  </p>
     *
     * @param element <code>Element</code> to output.
     * @param out <code>Writer</code> to write to.
     * @param indent <code>int</code> level of indentation.
     **/
    protected void printElementContent(Element element, Writer out,
                                       int indentLevel,
                                       NamespaceStack namespaces,
                                       List eltContent) throws IOException {
        // get same local flags as printElement does
        // a little redundant code-wise, but not performance-wise
        boolean empty = eltContent.size() == 0;

        // Calculate if the content is String/CDATA only
        boolean stringOnly = true;
        if (!empty) {
            stringOnly = isStringOnly(eltContent);
        }

        if (stringOnly) {
            Class justOutput = null;
            boolean endedWithWhite = false;
            Iterator itr = eltContent.iterator();
            while (itr.hasNext()) {
                Object content = itr.next();
                if (content instanceof String) {
                    String scontent = (String) content;
                    if ((justOutput == CDATA.class) && 
                        (textNormalize) &&
                        (startsWithWhite(scontent))) {
                        out.write(" ");
                    }
                    printString(scontent, out);
                    endedWithWhite = endsWithWhite(scontent);
                    justOutput = String.class;
                } else {
                    // We're in a CDATA section
                    if ((justOutput == String.class) &&
                        (textNormalize) &&
                        (endedWithWhite)) {
                        out.write(" ");  // padding
                    }
                    printCDATA((CDATA)content, out);
                    justOutput = CDATA.class;
                }
            }
        } else {
            // Iterate through children
            Object content = null;
            Class justOutput = null;
            boolean endedWithWhite = false;
            boolean wasFullyWhite = false;
            Iterator itr = eltContent.iterator();
            while (itr.hasNext()) {
                content = itr.next();
                // See if text, an element, a PI or a comment
                if (content instanceof Comment) {
                    if (!((justOutput == String.class) && (wasFullyWhite))) {
                        maybePrintln(out, indentLevel);
                    }
                    printComment((Comment) content, out);
                    justOutput = Comment.class;
                } else if (content instanceof String) {
                    String scontent = (String) content;

     // bugfix: don't print lines for whitespace that's
     // only sticking between close tags
     if (textNormalize && isWhitespace(scontent)) {
 wasFullyWhite = true;
 continue;
     }
     
                    if ((justOutput == CDATA.class) && 
                        (textNormalize) &&
                        (startsWithWhite(scontent))) {
                        out.write(" ");
                    } else if ((justOutput != CDATA.class) && 
                               (justOutput != String.class) &&
        (justOutput != null)
                               // (justOutput != Element.class)
        )
     {
                        maybePrintln(out, indentLevel);
                    }
     // if scontent is not a single-character newline
                    if (!((scontent.length() == 1) && 
                         ("\r\n".indexOf(scontent.charAt(0)) != -1))) {
                        printString(scontent, out);
                        endedWithWhite = endsWithWhite(scontent);
                        justOutput = String.class;
                        wasFullyWhite = (scontent.trim().length() == 0);
                    }
                } else if (content instanceof Element) {
                    if (!((justOutput == String.class) && (wasFullyWhite))) {
                        maybePrintln(out, indentLevel);
                    }
                    printElement((Element) content, out,
                                 indentLevel, namespaces);
                    justOutput = Element.class;
                } else if (content instanceof EntityRef) {
                    if (!((justOutput == String.class) && (wasFullyWhite))) {
                        maybePrintln(out, indentLevel);
                    }
                    printEntityRef((EntityRef) content, out);
                    justOutput = EntityRef.class;
                } else if (content instanceof ProcessingInstruction) {
                    if (!((justOutput == String.class) && (wasFullyWhite))) {
                        maybePrintln(out, indentLevel);
                    }
                    printProcessingInstruction((ProcessingInstruction) content,
                                               out);
                    justOutput = ProcessingInstruction.class;
                } else if (content instanceof CDATA) {
                    if ((justOutput == String.class) &&
                        (textNormalize) &&
                        (endedWithWhite)) {
                        out.write(" ");  // padding
                    }
                    else if (justOutput != String.class &&
                             justOutput != CDATA.class) {
                        maybePrintln(out, indentLevel);
                    }
                    printCDATA((CDATA)content, out);
                    justOutput = CDATA.class;
                }
                // Unsupported types are *not* printed, nor should they exist
            }
            maybePrintln(out, indentLevel - 1);
        }
    }  // printElementContent


    /**
     * Print a string.  Escapes the element entities, trims interior
     * whitespace if necessary.
     **/     
    protected void printString(String s, Writer out) throws IOException {
        s = escapeElementEntities(s);
        // patch by Brad Morgan to strip interior whitespace
        // (Brad.Morgan@e-pubcorp.com)
        if (textNormalize) {
            StringTokenizer tokenizer = new StringTokenizer(s);
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                out.write(token);
                if (tokenizer.hasMoreTokens()) {
                    out.write(" ");
                }
            }
        }
        else {                    
            out.write(s);
        }
    }
    
    /**
     * <p>
     * This will handle printing out an <code>{@link EntityRef}</code>.
     * Only the entity reference such as <code>&amp;entity;</code>
     * will be printed. However, subclasses are free to override 
     * this method to print the contents of the entity instead.
     * </p>
     *
     * @param entity <code>EntityRef</code> to output.
     * @param out <code>Writer</code> to write to.  */
    protected void printEntityRef(EntityRef entity, Writer out) 
                                                   throws IOException {
        out.write(new StringBuffer()
            .append("&")
            .append(entity.getName())
            .append(";")
            .toString());
    }

    /**
     * <p>
     *  This will handle printing out any needed <code>{@link Namespace}</code>
     *    declarations.
     * </p>
     *
     * @param ns <code>Namespace</code> to print definition of
     * @param out <code>Writer</code> to write to.
     */
    private void printNamespace(Namespace ns, Writer out) throws IOException {
        out.write(" xmlns");
        String prefix = ns.getPrefix();
        if (!prefix.equals("")) {
            out.write(":");
            out.write(prefix);
        }
        out.write("=\"");
        out.write(ns.getURI());
        out.write("\"");
    }

    /**
     * <p>
     * This will handle printing out an <code>{@link Attribute}</code> list.
     * </p>
     *
     * @param attributes <code>List</code> of Attribute objcts
     * @param out <code>Writer</code> to write to
     */
    protected void printAttributes(List attributes, Element parent, 
                                   Writer out, NamespaceStack namespaces) 
      throws IOException {

        // I do not yet handle the case where the same prefix maps to
        // two different URIs. For attributes on the same element
        // this is illegal; but as yet we don't throw an exception
        // if someone tries to do this
        Iterator itr = attributes.iterator();
        while (itr.hasNext()) {
            Attribute attribute = (Attribute)itr.next();
            Namespace ns = attribute.getNamespace();
            if (ns != Namespace.NO_NAMESPACE && ns != Namespace.XML_NAMESPACE) {
                String prefix = ns.getPrefix();           
                String uri = namespaces.getURI(prefix);
                if (!ns.getURI().equals(uri)) { // output a new namespace decl
                    printNamespace(ns, out);
                    namespaces.push(ns);
                }
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
        if (ns != Namespace.XML_NAMESPACE &&
            !(ns == Namespace.NO_NAMESPACE && namespaces.getURI("") == null)) {
            String prefix = ns.getPrefix();        
            String uri = namespaces.getURI(prefix);
            if (!ns.getURI().equals(uri)) { // output a new namespace decl
                namespaces.push(ns);
                printNamespace(ns, out);
            }
        }
    }

    private void printAdditionalNamespaces(Element element, Writer out, 
                                           NamespaceStack namespaces)
                                throws IOException {
        List additionalNamespaces = element.getAdditionalNamespaces();
        if (additionalNamespaces != null) {
            Iterator itr = additionalNamespaces.iterator();
            while (itr.hasNext()) {
                Namespace additional = (Namespace)itr.next();
                String prefix = additional.getPrefix();        
                String uri = namespaces.getURI(prefix);
                if (!additional.getURI().equals(uri)) {
                    namespaces.push(additional);
                    printNamespace(additional, out);
                }
            }
        }
    }

    /**
     * <p>
     * This will take the pre-defined entities in XML 1.0 and
     *   convert their character representation to the appropriate
     *   entity reference, suitable for XML attributes.  It does
     *   no converstion for ' because it's not necessary as the outputter 
     *   writes attributes surrounded by double-quotes.
     * </p>
     *
     * @param st <code>String</code> input to escape.
     * @return <code>String</code> with escaped content.
     */
    protected String escapeAttributeEntities(String st) {
        StringBuffer buff = null;
        int length = st.length();
        String stEntity = null;
        int i, last;

        for (i=0, last=0; i < length; i++) {
            switch(st.charAt(i)) {
                case '<' :
                    stEntity = "&lt;";
                    break;
                case '>' :
                    stEntity = "&gt;";
                    break;
/*
                case '\'' :
                    stEntity = "&apos;";
                    break;
*/
                case '\"' :
                    stEntity = "&quot;";
                    break;
                case '&' :
                    stEntity = "&amp;";
                    break;
                default :
                    /* no-op */ ;
            }
            if (stEntity != null) {
                // An entity occurred, so we'll have to use the StringBuffer.
                if (buff == null) {
                    buff = new StringBuffer(length + 20); // allow room for a few more entities
                }
                buff.append(st.substring(last, i));
                buff.append(stEntity);
                stEntity = null;
                last = i + 1;
            }
        }

        // If there were any entities, return the escaped charactes that we 
        // put in the StringBuffer. Otherwise, just return the unmodified 
        // input string.
        if (buff != null) {
            if(last < length) {
                buff.append(st.substring(last, i));
            }
            return buff.toString();
        } else {
            return st;
        }
    }


    /**
     * <p>
     * This will take the three pre-defined entities in XML 1.0
     *   (used specifically in XML elements) and
     *   convert their character representation to the appropriate
     *   entity reference, suitable for XML element.
     * </p>
     *
     * @param st <code>String</code> input to escape.
     * @return <code>String</code> with escaped content.
     */
    protected String escapeElementEntities(String st) {
        StringBuffer buff = null;
        int length = st.length();
        String stEntity = null;
        int i, last;

        for (i=0, last=0; i < length; i++) {
            switch(st.charAt(i)) {
                case '<' :
                    stEntity = "&lt;";
                    break;
                case '>' :
                    stEntity = "&gt;";
                    break;
                case '&' :
                    stEntity = "&amp;";
                    break;
                default :
                    /* no-op */ ;
            }
            if (stEntity != null) {
                // An entity occurred, so we'll have to use the StringBuffer.
                if (buff == null) {
                    buff = new StringBuffer(length + 20); // allow room for a few more entities
                }
                buff.append(st.substring(last, i));
                buff.append(stEntity);
                stEntity = null;
                last = i + 1;
            }
        }

        // If there were any entities, return the escaped charactes that we 
        // put in the StringBuffer. Otherwise, just return the unmodified 
        // input string.
        if (buff != null) {
            if (last < length) {
                buff.append(st.substring(last, i));
            }
            return buff.toString();
        } else {
            return st;
        }
    }

    /**
     * parse command-line arguments of the form <code>-omitEncoding
     * -indentSize 3 ...</code>
     * @return int index of first parameter that we didn't understand
     **/      
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
            else if (args[i].equals("-indentSize")) {
                setIndentSize(Integer.parseInt(args[++i]));
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
            else if (args[i].equals("-textNormalize")) {
                setTextNormalize(true);
            }
            else {
                return i;
            }
        }
        return i;
    } // parseArgs

    private boolean startsWithWhite(String s) {
        return (s.length() > 0 && s.charAt(0) <= ' ');
    }

    private boolean endsWithWhite(String s) {
        return (s.length() > 0 && s.charAt(s.length() - 1) <= ' ');
    }

    // true if string is all whitespace (space, tab, cr, lf only)
    private boolean isWhitespace(String s) {
        int length = s.length();
        for (int i=0; i<length; ++i) {
            if (" \t\n\r".indexOf(s.charAt(i)) == -1) {
                return false;
            }
        }
        return true;
    }

    // returns the text of the element, taking "textNormalize" into account
    private String getElementText(Element element) {
       return textNormalize ? element.getTextNormalize() : element.getText();
    }
 
    // returns true if this is an empty element (meaning no content, or only 
    // whitespace content with textNormalize true)
    private boolean isEmpty(Element element) {
        // Calculate if the content is empty
        // We can handle "" string same as empty (CDATA has no effect here)    
        List eltContent = element.getContent();
        // An element with absolutely no content is empty
        if (eltContent.size() == 0) {
            return true;
        }
        // An element with only string content, whose string content
        // adds up to nothing, is empty
        if (isStringOnly(eltContent)) {
             String elementText = getElementText(element);
             if (elementText == null || elementText.equals("")) {
                return true;
            }
        }
        // anything else is not empty
        return false;
    }

    // Return true if the element's content list consists only of
    // String or CDATA nodes (or is empty)
    private boolean isStringOnly(List eltContent) {
        // Calculate if the contents are String/CDATA only
        Iterator itr = eltContent.iterator();
        while (itr.hasNext()) {
            Object o = itr.next();
            if (!(o instanceof String) && !(o instanceof CDATA)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return a string listing the settings for this XMLOutputter instance
     **/
    public String toString() {
        return
            "XMLOutputter[omitDeclaration = " + omitDeclaration + ", " +
            "encoding = " + encoding + ", " +
            "omitEncoding = " + omitEncoding + ", " +
            "indent = '" + indent + "'" + ", " +
            "expandEmptyElements = " + expandEmptyElements + ", " +
            "newlines = " + newlines + ", " +
            "lineSeparator = '" + toBytes(lineSeparator) + "', " +
            "textNormalize = " + textNormalize + "]";
    }

    private String toBytes(String x) {
        StringBuffer buf = new StringBuffer();
        for (int i=0; i<x.length(); ++i) {
            buf.append(toByte(x.charAt(i)));
        }
        return buf.toString();
    }

    private String toByte(char ch) {
        switch (ch) {
        case '\r':
            return "\\r";
        case '\n':
            return "\\n";
        case '\t':
            return "\\t";
        default:
            return ("[" + ((int)ch) + "]");
        }
    }
    
    /**
     * Factory for making new NamespaceStack objects.  The NamespaceStack
     * created is actually an inner class extending the package protected
     * NamespaceStack, as a way to make NamespaceStack "friendly" toward
     * subclassers.
     **/
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
     **/
    protected class NamespaceStack
       extends org.jdom.output.NamespaceStack
    {
    }

    /**
     * <p> Ensure that text immediately preceded by or followed by an
     * element will be "padded" with a single space.  </p>
     * 
     * @deprecated Deprecated in beta7, because this is no longer necessary
     */
    public void setPadText(boolean padText) { }

    /**
     * Set the initial indentation level.  
     *
     * @deprecated Deprecated in beta7, because this is better done with a
     *             stacked FilterOutputStream
     */
    public void setIndentLevel(int indentLevel) { }

    /**
     * <p>
     *  This will set whether the XML declaration 
     *  (<code>&lt;?xml version="1.0"?&gt;</code>)
     *  will be suppressed or not. It is common to suppress this in uses such
     *  as SOAP and XML-RPC calls.
     * </p>
     *
     * @param suppressDeclaration <code>boolean</code> indicating whether or not
     *        the XML declaration should be suppressed.
     * @deprecated Deprecated in beta7, use setOmitDeclaration() instead
     */
    public void setSuppressDeclaration(boolean suppressDeclaration) {
        this.omitDeclaration = suppressDeclaration;
    }

}
