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

package org.jdom.output;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jdom.Attribute;
import org.jdom.Comment;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Entity;
import org.jdom.Namespace;
import org.jdom.ProcessingInstruction;

/**
 * <p><code>XMLOutputter</code> takes a JDOM tree and
 *   formats it to a stream as XML.  This formatter performs typical
 *   document formatting.  The XML declaration
 *   and processing instructions are always on their own lines.  Empty
 *   elements are printed as &lt;empty/&gt; and text-only contents are printed as
 *   &lt;tag&gt;content&lt;/tag&gt; on a single line.  Constructor parameters control the
 *   indent amount and whether new lines are printed between elements.  For
 *   compact machine-readable output pass in an empty string indent and a
 *   <code>false</code> for printing new lines.
 * </p>
 *
 * @author Brett McLaughlin
 * @author Jason Hunter
 * @author Jason Reid
 * @author Wolfgang Werner
 * @author Elliotte Rusty Harold
 * @version 1.0
 */
public class XMLOutputter {

    /** The default indent is no spaces (as original document) */
    private String indent = "";

    /** The default new line flag, set to do new lines only as in original document */
    private boolean newlines = false;

    /** The encoding format */
    private String enc = "UTF8";

    /** Namespaces on the document */
    private LinkedList namespaces;

    /**
     * <p>
     * This will create an <code>XMLOutputter</code> with
     *   no additional whitespace (indent or new lines) added;
     *   the whitespace from the element text content is fully preserved.
     * </p>
     */
    public XMLOutputter() {
        namespaces = new LinkedList();
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
       namespaces = new LinkedList();
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
       namespaces = new LinkedList();
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
     * @param encoding set encoding format.
     */
    public XMLOutputter(String indent, boolean newlines, String encoding) {
       this.indent = indent;
       this.newlines = newlines;
       this.enc = encoding;
       namespaces = new LinkedList();
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
        for (int i = 0; i < level; i++) {
            out.write(indent);
        }
    }

    // We could change this to the System default, 
    // but I prefer not to make output platform dependent.
    // A carriage return, linefeed pair is the most generally
    // acceptable linebreak.  Another possibility is to use
    // only a line feed, which is XML's preferred (but not required)
    // solution. However both carriage return and linefeed are
    // required for many network protocols, and the parser on the
    // other end should normalize this. 
    private static String newline = "\r\n";

    /**
     * <p>
     * This will print a new line only if the newlines flag was set to true
     * </p>
     *
     * @param out <code>Writer</code> to write to
     */
    protected void maybePrintln(Writer out) throws IOException  {
        if (newlines) out.write(newline);
    }

    /**
     * <p>
     * This will print the <code>Document</code> to the given Writer.
     *   The characters are printed using the specified encoding.
     * </p>
     *
     * @param doc <code>Document</code> to format.
     * @param out <code>OutputStream</code> to write to.
     * @param encoding encoding format to use
     * @throws <code>IOException</code> - if there's any problem writing.
     */
    public void output(Document doc, OutputStream out, String encoding)
                                           throws IOException {
        /**
         * Get an OutputStreamWriter, use specified encoding.
         */
        Writer writer = new OutputStreamWriter(
                         new BufferedOutputStream(out), encoding);

        // Print out XML declaration
        printDeclaration(doc, writer, encoding);

        printDocType(doc.getDocType(), writer);

        // Print out root element, as well as any root level
        // comments and processing instructions, 
        // starting with no indentation
        Iterator i = doc.getMixedContent().iterator();
        while (i.hasNext()) {
            Object obj = i.next();
            if (obj instanceof Element) {
                // 0 is indentation
                printElement(doc.getRootElement(), writer, 0);
            } else if (obj instanceof Comment) {
                printComment((Comment) obj, writer, 0);
            } else if (obj instanceof ProcessingInstruction) {
                printProcessingInstruction((ProcessingInstruction) obj, writer, 0);
            }
        }

        // Flush the output
        writer.flush();
    }

    /**
     * <p>
     * This will print the <code>Document</code> to the given output stream.
     *   The characters are printed using the encoding specified in the
     *   constructor, or a default of UTF-8.
     * </p>
     *
     * @param doc <code>Document</code> to format.
     * @param out <code>Writer</code> to write to.
     * @throws <code>IOException</code> - if there's any problem writing.
     */
    public void output(Document doc, OutputStream out)
                                           throws IOException {
        output(doc, out, this.enc);  
    }

    /**
     * <p>
     * This will write the comment to the specified writer.
     * </p>
     *
     * @param comment <code>Comment</code> to write.
     * @param out <code>Writer</code> to write to.
     * @param indentLevel Current depth in hierarchy.
     */
    protected void printComment(Comment comment,
                                    Writer out, int indentLevel) throws IOException {
                                        
        indent(out, indentLevel);
        out.write(comment.getSerializedForm());
        maybePrintln(out);

    }
      
    /**
     * <p>
     * This will write the processing instruction to the specified writer.
     * </p>
     *
     * @param comment <code>ProcessingInstruction</code> to write.
     * @param out <code>Writer</code> to write to.
     * @param indentLevel Current depth in hierarchy.
     */
    protected void printProcessingInstruction(ProcessingInstruction pi,
                                    Writer out, int indentLevel) throws IOException {
                                        
        indent(out, indentLevel);
        out.write(pi.getSerializedForm());
        maybePrintln(out);

    }
    

    /**
     * <p>
     * This will write the declaration to the given Writer.
     *   Assumes XML version 1.0 since we don't directly know.
     * </p>
     *
     * @param docType <code>DocType</code> whose declaration to write.
     * @param out <code>Writer</code> to write to.
     */
    protected void printDeclaration(Document doc,
                                    Writer out,
                                    String encoding)  throws IOException {
        // Assume 1.0 version
        if (encoding.equals("UTF8")) {
            out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        }
        else {
            out.write("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>");
        }
        maybePrintln(out);
        
    }    

    /**
     * <p>
     * This will write the DOCTYPE declaration if one exists.
     * </p>
     *
     * @param doc <code>Document</code> whose declaration to write.
     * @param out <code>Writer</code> to write to.
     */
    protected void printDocType(DocType docType, Writer out)  throws IOException {
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
        out.write(">");
        maybePrintln(out);
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
     */
    protected void printElement(Element element, Writer out,
                                int indentLevel)  throws IOException {

        List mixedContent = element.getMixedContent();

        boolean empty = mixedContent.size() == 0;
        boolean stringOnly =
            !empty &&
            mixedContent.size() == 1 &&
            mixedContent.get(0) instanceof String;

        // Print beginning element tag
        /* maybe the doctype, xml declaration, and processing instructions 
           should only break before and not after; then this check is unnecessary,
           or maybe the println should only come after and never before. Then the
           output always ends with a newline */
           
        indent(out, indentLevel);

        // Print the beginning of the tag plus attributes and any
        // necessary namespace declarations
        out.write("<");
        out.write(element.getQualifiedName());
        Namespace ns = element.getNamespace();
        boolean printedNS = false;
        if ((ns != Namespace.NO_NAMESPACE) && 
            (ns != Namespace.XML_NAMESPACE) &&
            (!namespaces.contains(ns))) {
            printNamespace(element.getNamespace(), out);
            printedNS = true;
            namespaces.add(ns);
        }

        printAttributes(element.getAttributes(), out);

        if (empty) {
            // Simply close up
            out.write(" />");
            maybePrintln(out);
        } else if (stringOnly) {
            // Print the tag  with String on same line
            // Example: <tag name="value">content</tag>
            out.write(">");
            out.write(escapeElementEntities(element.getText()));
            out.write("</");
            out.write(element.getQualifiedName());
            out.write(">");
            maybePrintln(out);
        } else {
            /**
             * Print with children on future lines
             * Rather than check for mixed content or not, just print
             * Example: <tag name="value">
             *             <child/>
             *          </tag>
             */
            out.write(">");
            maybePrintln(out);
            // Iterate through children
            Object content = null;
            for (int i=0, size=mixedContent.size(); i<size; i++) {
                content = mixedContent.get(i);
                // See if text, an element, a processing instruction or a comment
                if (content instanceof Comment) {
                    printComment((Comment) content, out, indentLevel + 1);
                } else if (content instanceof String) {
                    out.write(escapeElementEntities(content.toString()));
                } else if (content instanceof Element) {
                    printElement((Element) content, out, indentLevel + 1);
                } else if (content instanceof Entity) {
                    printEntity((Entity) content, out);
                } else if (content instanceof ProcessingInstruction) {
                    printProcessingInstruction((ProcessingInstruction) content, out, indentLevel + 1);
                } // Unsupported types are *not* printed
            }

            indent(out, indentLevel);
            out.write("</");
            out.write(element.getQualifiedName());
            out.write(">");
            maybePrintln(out);

           // After recursion, remove the namespace defined on the element (if any)
           if (printedNS) {
              namespaces.removeLast();
           }
        }
    }
    
    /**
     * <p>
     * This will handle printing out an <code>{@link Entity}</code>.
     * Only the entity reference such as <code>&amp;entity;</code>
     * will be printed. However, subclasses are free to override 
     * this method to print the contents of the entity instead.
     * </p>
     *
     * @param entity <code>Entity</code> to output.
     * @param out <code>Writer</code> to write to.
     */
    protected void printEntity(Entity entity, Writer out) throws IOException {
        out.write(entity.getSerializedForm());
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
    protected void printNamespace(Namespace ns, Writer out) throws IOException {
        out.write(" xmlns");
        if (!ns.getPrefix().equals("")) {
            out.write(":");
            out.write(ns.getPrefix());
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
    protected void printAttributes(List attributes, Writer out) throws IOException {
        for (int i=0, size=attributes.size(); i<size; i++) {
            Attribute attribute = (Attribute)attributes.get(i);
            Namespace ns = attribute.getNamespace();
            if ((ns != Namespace.NO_NAMESPACE) && 
                (ns != Namespace.XML_NAMESPACE) &&
                (!namespaces.contains(ns))) {
                printNamespace(attribute.getNamespace(), out);
            }
            out.write(" ");
            out.write(attribute.getQualifiedName());
            out.write("=");

            out.write("\"");
            out.write(escapeAttributeEntities(attribute.getValue()));
            out.write("\"");
        }
    }

    /**
     * <p>
     * This will take the five pre-defined entities in XML 1.0 and
     *   convert their character representation to the appropriate
     *   entity reference, suitable for XML attributes.
     * </p>
     *
     * @param st <code>String</code> input to escape.
     * @return <code>String</code> with escaped content.
     */
    private String escapeAttributeEntities(String st) {
        StringBuffer buff = new StringBuffer();
        char[] block = st.toCharArray();
        String stEntity = null;
        int i, last;

        for (i=0, last=0; i < block.length; i++) {
            switch(block[i]) {
                case '<' :
                    stEntity = "&lt;";
                    break;
                case '>' :
                    stEntity = "&gt;";
                    break;
                case '\'' :
                    stEntity = "&apos;";
                    break;
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
                buff.append(block, last, i - last);
                buff.append(stEntity);
                stEntity = null;
                last = i + 1;
            }
        }
        if(last < block.length) {
            buff.append(block, last, i - last);
        }

        return buff.toString();
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
    private String escapeElementEntities(String st) {
        StringBuffer buff = new StringBuffer();
        char[] block = st.toCharArray();
        String stEntity = null;
        int i, last;

        for (i=0, last=0; i < block.length; i++) {
            switch(block[i]) {
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
                buff.append(block, last, i - last);
                buff.append(stEntity);
                stEntity = null;
                last = i + 1;
            }
        }
        if(last < block.length) {
            buff.append(block, last, i - last);
        }

        return buff.toString();
    }

}
