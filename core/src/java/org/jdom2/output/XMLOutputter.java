/*--

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

package org.jdom2.output;

import java.io.*;
import java.util.*;

import javax.xml.transform.Result;

import org.jdom2.*;
import org.jdom2.output.Format.TextMode;

/**
 * Outputs a JDOM document as a stream of bytes. The outputter can manage many
 * styles of document formatting, from untouched to pretty printed. The default
 * is to output the document content exactly as created, but this can be changed
 * by setting a new Format object. For pretty-print output, use
 * <code>{@link Format#getPrettyFormat()}</code>. For whitespace-normalized
 * output, use <code>{@link Format#getCompactFormat()}</code>.
 * <p>
 * There are <code>{@link #output output(...)}</code> methods to print any of
 * the standard JDOM classes, including Document and Element, to either a Writer
 * or an OutputStream. <b>Warning</b>: When outputting to a Writer, make sure
 * the writer's encoding matches the encoding setting in the Format object. This
 * ensures the encoding in which the content is written (controlled by the
 * Writer configuration) matches the encoding placed in the document's XML
 * declaration (controlled by the XMLOutputter). Because a Writer cannot be
 * queried for its encoding, the information must be passed to the Format
 * manually in its constructor or via the
 * <code>{@link Format#setEncoding}</code> method. The default encoding is
 * UTF-8.
 * <p>
 * The methods <code>{@link #outputString outputString(...)}</code> are for
 * convenience only; for top performance you should call one of the <code>{@link
 * #output output(...)}</code> methods and pass in your own Writer or
 * OutputStream if possible.
 * <p>
 * XML declarations are always printed on their own line followed by a line
 * seperator (this doesn't change the semantics of the document). To omit
 * printing of the declaration use
 * <code>{@link Format#setOmitDeclaration}</code>. To omit printing of the
 * encoding in the declaration use <code>{@link Format#setOmitEncoding}</code>.
 * Unfortunatly there is currently no way to know the original encoding of the
 * document.
 * <p>
 * Empty elements are by default printed as &lt;empty/&gt;, but this can be
 * configured with <code>{@link Format#setExpandEmptyElements}</code> to cause
 * them to be expanded to &lt;empty&gt;&lt;/empty&gt;.
 *
 * @author  Brett McLaughlin
 * @author  Jason Hunter
 * @author  Jason Reid
 * @author  Wolfgang Werner
 * @author  Elliotte Rusty Harold
 * @author  David &amp; Will (from Post Tool Design)
 * @author  Dan Schaffer
 * @author  Alex Chaffee
 * @author  Bradley S. Huffman
 */

public class XMLOutputter implements Cloneable {

	// For normal output
	private Format userFormat = Format.getRawFormat();

	// For xml:space="preserve"
	protected static final Format preserveFormat = Format.getRawFormat();

	// What's currently in use
	protected Format currentFormat = userFormat;

	/** Whether output escaping is enabled for the being processed
	 * Element - default is <code>true</code> */
	private boolean escapeOutput = true;

	// * * * * * * * * * * Constructors * * * * * * * * * *
	// * * * * * * * * * * Constructors * * * * * * * * * *

	/**
	 * This will create an <code>XMLOutputter</code> with the default
	 * {@link Format} matching {@link Format#getRawFormat}.
	 */
	public XMLOutputter() {
	}

	/**
	 * This will create an <code>XMLOutputter</code> with the specified
	 * format characteristics.  Note the format object is cloned internally
	 * before use.
	 */
	public XMLOutputter(Format format) {
		userFormat = format.clone();
		currentFormat = userFormat;
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
		this.userFormat = that.userFormat.clone();
		currentFormat = userFormat;
	}

	// * * * * * * * * * * Set parameters methods * * * * * * * * * *
	// * * * * * * * * * * Set parameters methods * * * * * * * * * *

	/**
	 * Sets the new format logic for the outputter.  Note the Format
	 * object is cloned internally before use.
	 *
	 * @param newFormat the format to use for output
	 */
	public void setFormat(Format newFormat) {
		this.userFormat = newFormat.clone();
		this.currentFormat = userFormat;
	}

	/**
	 * Returns the current format in use by the outputter.  Note the
	 * Format object returned is a clone of the one used internally.
	 */
	public Format getFormat() {
		return userFormat.clone();
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
	public void output(List<? extends Content> list, OutputStream out)
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
	 * @param pi <code>ProcessingInstruction</code> to output.
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
	 * (see {@link Format#setEncoding}).
	 */
	private Writer makeWriter(OutputStream out)
			throws java.io.UnsupportedEncodingException {
		return makeWriter(out, userFormat.encoding);
	}

	/**
	 * Get an OutputStreamWriter, use specified encoding.
	 */
	private static Writer makeWriter(OutputStream out, String enc)
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

		printDeclaration(out, doc, userFormat.encoding);
		
		NamespaceStack stack = new NamespaceStack();

		// Print out root element, as well as any root level
		// comments and processing instructions,
		// starting with no indentation
		for (Content obj : doc.getContent()) {

			if (obj instanceof Element) {
				printElement(out, doc.getRootElement(), 0, stack);
			}
			else if (obj instanceof Comment) {
				printComment(out, (Comment) obj);
			}
			else if (obj instanceof ProcessingInstruction) {
				printProcessingInstruction(out, (ProcessingInstruction) obj);
			}
			else if (obj instanceof DocType) {
				printDocType(out, doc.getDocType());
				// Always print line separator after declaration, helps the
				// output look better and is semantically inconsequential
				out.write(currentFormat.lineSeparator);
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
		printDocType(out, doctype);
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
		printElement(out, element, 0, new NamespaceStack());
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
		List<Content> content = element.getContent();
		printContentRange(out, content, 0, new NamespaceStack());
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
	public void output(List<? extends Content> list, Writer out)
			throws IOException {
		printContentRange(out, list, 0, new NamespaceStack());
		out.flush();
	}

	/**
	 * Print out a <code>{@link CDATA}</code> node.
	 *
	 * @param cdata <code>CDATA</code> to output.
	 * @param out <code>Writer</code> to use.
	 */
	public void output(CDATA cdata, Writer out) throws IOException {
		printCDATA(out, cdata, currentFormat.mode);
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
		printText(out, text, currentFormat.mode);
		out.flush();
	}

	/**
	 * Print out a <code>{@link Comment}</code>.
	 *
	 * @param comment <code>Comment</code> to output.
	 * @param out <code>Writer</code> to use.
	 */
	public void output(Comment comment, Writer out) throws IOException {
		printComment(out, comment);
		out.flush();
	}

	/**
	 * Print out a <code>{@link ProcessingInstruction}</code>.
	 *
	 * @param pi <code>ProcessingInstruction</code> to output.
	 * @param out <code>Writer</code> to use.
	 */
	public void output(ProcessingInstruction pi, Writer out)
			throws IOException {
		boolean currentEscapingPolicy = currentFormat.ignoreTrAXEscapingPIs;

		// Output PI verbatim, disregarding TrAX escaping PIs.
		currentFormat.setIgnoreTrAXEscapingPIs(true);
		printProcessingInstruction(out, pi);
		currentFormat.setIgnoreTrAXEscapingPIs(currentEscapingPolicy);

		out.flush();
	}

	/**
	 * Print out a <code>{@link EntityRef}</code>.
	 *
	 * @param entity <code>EntityRef</code> to output.
	 * @param out <code>Writer</code> to use.
	 */
	public void output(EntityRef entity, Writer out) throws IOException {
		printEntityRef(out, entity);
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
		} catch (IOException e) {
			// swallow - will never happen.
		}
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
		} catch (IOException e) {
			// swallow - will never happen.
		}
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
		} catch (IOException e) {
			// swallow - will never happen.
		}
		return out.toString();
	}

	/**
	 * Return a string representing a list of nodes.  The list is
	 * assumed to contain legal JDOM nodes.
	 *
	 * @param list <code>List</code> to format.
	 */
	public String outputString(List<? extends Content> list) {
		StringWriter out = new StringWriter();
		try {
			output(list, out);  // output() flushes
		} catch (IOException e) {
			// swallow - will never happen.
		}
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
		} catch (IOException e) {
			// swallow - will never happen.
		}
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
		} catch (IOException e) {
			// swallow - will never happen.
		}
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
		} catch (IOException e) {
			// swallow - will never happen.
		}
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
		} catch (IOException e) {
			// swallow - will never happen.
		}
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
		} catch (IOException e) {
			// swallow - will never happen.
		}
		return out.toString();
	}

	/**
	 * This will handle printing out an <code>{@link
	 * Element}</code>'s content only, not including its tag, and
	 * attributes.  This can be useful for printing the content of an
	 * element that contains HTML, like "&lt;description&gt;JDOM is
	 * &lt;b&gt;fun&gt;!&lt;/description&gt;".
	 *
	 * @param element <code>Element</code> to output.
	 */
	public String outputElementContentString(Element element) {
		StringWriter out = new StringWriter();
		try {
			outputElementContent(element, out);  // output() flushes
		} catch (IOException e) {
			// swallow - will never happen.
		}
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
	protected void printDeclaration(Writer out, Document doc,
			String encoding) throws IOException {

		// Only print the declaration if it's not being omitted
		if (!userFormat.omitDeclaration) {
			// Assume 1.0 version
			out.write("<?xml version=\"1.0\"");
			if (!userFormat.omitEncoding) {
				out.write(" encoding=\"" + encoding + "\"");
			}
			out.write("?>");

			// Print new line after decl always, even if no other new lines
			// Helps the output look better and is semantically
			// inconsequential
			out.write(currentFormat.lineSeparator);
		}
	}

	/**
	 * This handle printing the DOCTYPE declaration if one exists.
	 *
	 * @param docType <code>Document</code> whose declaration to write.
	 * @param out <code>Writer</code> to use.
	 */
	protected void printDocType(Writer out, DocType docType)
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
			out.write(" [");
			out.write(currentFormat.lineSeparator);
			out.write(docType.getInternalSubset());
			out.write("]");
		}
		out.write(">");
	}

	/**
	 * This will handle printing of comments.
	 *
	 * @param comment <code>Comment</code> to write.
	 * @param out <code>Writer</code> to use.
	 */
	protected void printComment(Writer out, Comment comment)
			throws IOException {
		out.write("<!--");
		out.write(comment.getText());
		out.write("-->");
	}

	/**
	 * This will handle printing of processing instructions.
	 *
	 * @param pi <code>ProcessingInstruction</code> to write.
	 * @param out <code>Writer</code> to use.
	 */
	protected void printProcessingInstruction(Writer out, ProcessingInstruction pi
			) throws IOException {
		String target = pi.getTarget();
		boolean piProcessed = false;

		if (currentFormat.ignoreTrAXEscapingPIs == false) {
			if (target.equals(Result.PI_DISABLE_OUTPUT_ESCAPING)) {
				escapeOutput = false;
				piProcessed  = true;
			}
			else if (target.equals(Result.PI_ENABLE_OUTPUT_ESCAPING)) {
				escapeOutput = true;
				piProcessed  = true;
			}
		}
		if (piProcessed == false) {
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
	}

	/**
	 * This will handle printing a <code>{@link EntityRef}</code>.
	 * Only the entity reference such as <code>&amp;entity;</code>
	 * will be printed. However, subclasses are free to override
	 * this method to print the contents of the entity instead.
	 *
	 * @param entity <code>EntityRef</code> to output.
	 * @param out <code>Writer</code> to use.  */
	protected void printEntityRef(Writer out, EntityRef entity)
			throws IOException {
		out.write("&");
		out.write(entity.getName());
		out.write(";");
	}

	/**
	 * This will handle printing of <code>{@link CDATA}</code> text.
	 * @param out <code>Writer</code> to use.
	 * @param cdata <code>CDATA</code> to output.
	 * @param mode The mechanism to use to format the text.
	 */
	protected void printCDATA(Writer out, CDATA cdata, TextMode mode) throws IOException {
		String str = (mode == Format.TextMode.NORMALIZE)
				? cdata.getTextNormalize()
						: ((mode == Format.TextMode.TRIM) ?
								cdata.getText().trim() : cdata.getText());
				out.write("<![CDATA[");
				out.write(str);
				out.write("]]>");
	}

	/**
	 * This will handle printing of <code>{@link Text}</code> strings.
	 * @param out <code>Writer</code> to use.
	 * @param text <code>Text</code> to write.
	 * @param mode The mechanism to use to format the text.
	 */
	protected void printText(Writer out, Text text, TextMode mode) throws IOException {
		String str = (mode == Format.TextMode.NORMALIZE)
				? text.getTextNormalize()
						: ((mode == Format.TextMode.TRIM) ?
								text.getText().trim() : text.getText());
				out.write(escapeElementEntities(str));
	}

	/**
	 * This will handle printing a string.  Escapes the element entities,
	 * trims interior whitespace, etc. if necessary.
	 * @param mode TODO
	 */
	private void printString(Writer out, String str, TextMode mode) throws IOException {
		if (mode == Format.TextMode.NORMALIZE) {
			str = Text.normalizeString(str);
		}
		else if (mode == Format.TextMode.TRIM) {
			str = str.trim();
		}
		out.write(escapeElementEntities(str));
	}

	/**
	 * This will handle printing of a <code>{@link Element}</code>,
	 * its <code>{@link Attribute}</code>s, and all contained (child)
	 * elements, etc.
	 *
	 * @param element <code>Element</code> to output.
	 * @param out <code>Writer</code> to use.
	 * @param level <code>int</code> level of indention.
	 * @param namespaces <code>List</code> stack of Namespaces in scope.
	 */
	protected void printElement(Writer out, Element element,
			int level, NamespaceStack stack)
					throws IOException {

		List<Attribute> attributes = element.getAttributes();
		List<Content> content = element.getContent();

		// Check for xml:space and adjust format settings
		String space = null;
		if (attributes != null) {
			space = element.getAttributeValue("space",
					Namespace.XML_NAMESPACE);
		}

		Format previousFormat = currentFormat;

		if ("default".equals(space)) {
			currentFormat = userFormat;
		}
		else if ("preserve".equals(space)) {
			currentFormat = preserveFormat;
		}

		// Print the beginning of the tag plus attributes and any
		// necessary namespace declarations
		out.write("<");
		printQualifiedName(out, element);

		// Print the element's namespace, if appropriate
		stack.push(element);
		try {
			printElementNamespaces(out, element, stack);
			// Print out attributes
			if (attributes != null)
				printAttributes(out, attributes);
	
			// Depending on the settings (newlines, textNormalize, etc), we may
			// or may not want to print all of the content, so determine the
			// index of the start of the content we're interested
			// in based on the current settings.
	
			int start = skipLeadingWhite(content, 0);
			final int size = content.size();
			if (start >= size) {
				// Case content is empty or all insignificant whitespace
				if (currentFormat.expandEmptyElements) {
					out.write("></");
					printQualifiedName(out, element);
					out.write(">");
				}
				else {
					out.write(" />");
				}
			}
			else {
				out.write(">");
				
				boolean donewlines = nextNonText(content, 0) < size;
				
				if (donewlines) {
					newline(out);
				}
	
				printContentRange(out, content, level + 1, stack);
				
				if (donewlines) {
					newline(out);
					indent(out, level);
				}
				
				out.write("</");
				printQualifiedName(out, element);
				out.write(">");
			}
	
		} finally {
			stack.pop();
		}

		// Restore our format settings
		currentFormat = previousFormat;
	}

	/**
	 * This will handle printing of content.
	 *
	 * @param content <code>List</code> of content to output
	 * @param out <code>Writer</code> to use.
	 * @param level <code>int</code> level of indentation.
	 * @param namespaces <code>List</code> stack of Namespaces in scope.
	 */
	private final void printContentRange(final Writer out, 
			final List<? extends Content> content,
			final int level, NamespaceStack stack)
					throws IOException {
		
		// Basic theory of operation is as follows:
		//    1. print any non-Text content
		//    2. Accumulate all sequential text content and print them together
		// Do not do any newlines before or after content.

		boolean donewline = false;
		int txti = -1;
		int index = 0;
		for (Content c : content) {
			if ((c instanceof Text) || (c instanceof EntityRef)) {
				//
				// Handle consecutive CDATA, Text, and EntityRef nodes all at once
				//
				if (txti < 0) {
					txti = index;
				}
			} else {
				if (txti >= 0) {
					// we have text content we need to print.
					// we also know that we are 'mixed' content
					// so the text content should be indented.
					// Additionally
					if (donewline) {
						newline(out);
					}
					donewline = true;
					indent(out,level);
					printTextRange(out, content, txti, index, currentFormat.mode);
					txti = -1;
				}

				if (donewline) {
					newline(out);
				}
				donewline = true;
				indent(out, level);
	
				if (c instanceof Comment) {
					printComment(out, (Comment)c);
				}
				else if (c instanceof Element) {
					printElement(out, (Element)c, level, stack);
				}
				else if (c instanceof ProcessingInstruction) {
					printProcessingInstruction(out, (ProcessingInstruction)c);
				}
				else {
					// XXX if we get here then we have a illegal content, for
					//     now we'll just ignore it (probably should throw
					//     a exception)
				}
			}
			index++;
		} /* while */
		if (txti >= 0) {
			if (donewline) {
				newline(out);
				indent(out,level);
			}
			printTextRange(out, content, txti, index, currentFormat.mode);
		}
	}

	/**
	 * This will handle printing of a sequence of <code>{@link CDATA}</code>
	 * <code>{@link Text}</code>, or <code>{@link EntityRef}</code> nodes. 
	 * It is an error to pass this method any other type of node.
	 * @param out <code>Writer</code> to use.
	 * @param content <code>List</code> of content to output
	 * @param start index of first content node (inclusive).
	 * @param end index of last content node (exclusive).
	 * @param mode The format to use for this text content.
	 */
	private final void printTextRange(final Writer out, 
			final List<? extends Content> content, 
			final int start, final int end, final TextMode mode) throws IOException {
		
		// here we have a sequence of Text nodes to print.
		// we follow the TextMode rules for it.
		
		if (start >= end) {
			// nothing to do.
			return;
		}
		
		// For when there's just one thing to print, or in RAW mode we just
		// print the contents...
		// ... its the simplest mode.
		if (start + 1 == end || TextMode.PRESERVE == mode) {
			for (int i = start; i < end; i++) {
				Content node = content.get(i);

				// Print the node
				if (node instanceof CDATA) {
					printCDATA(out, (CDATA) node, mode);
				}
				else if (node instanceof EntityRef) {
					printEntityRef(out, (EntityRef) node);
				}
				else {
					printText(out, (Text) node, mode);
				}
			}
			return;
		}
		
		// Right, at this point it is complex.
		// we have multiple nodes to output, and it could be in one of three
		// modes, TRIM, TRIM_FULL_WHITE, or NORMALIZE
		
		// first, let's check for real text content.
		boolean gottext = false;
		for (int i = start; i < end; i++) {
			if (!isAllWhitespace(content.get(i))) {
				gottext = true;
				break;
			}
		}
		
		
		// Let's get TRIM_FULL_WHITE out of the way.
		// in this mode, if there is any real text content, we print it all.
		// otherwise we print nothing
		if (TextMode.TRIM_FULL_WHITE == mode) {
			// Right, if we find text, we print it all.
			// If we don't we print nothing.
			if (gottext) {
				for (int i = start; i < end; i++) {
					Content node = content.get(i);

					// Print the node
					if (node instanceof CDATA) {
						printCDATA(out, (CDATA) node, TextMode.PRESERVE);
					}
					else if (node instanceof EntityRef) {
						printEntityRef(out, (EntityRef) node);
					}
					else {
						printText(out, (Text) node, TextMode.PRESERVE);
					}
				}
			}
			return;
		}
		
		// OK, now we are left with just TRIM and NORMALIZE
		// Normalize is now actually easier.
		if (TextMode.NORMALIZE == mode) {
			boolean dospace = false;
			// we know there's at least 2 content nodes...
			// we process the 'gaps' to see if we need spaces, and print
			// all the content in NORMALIZED form.
			boolean first = true;
			for (int i = start; i < end; i++) {
				boolean endspace = false;
				Content c = content.get(i);
				if (c instanceof Text) {
					String val = ((Text)c).getValue();
					if (val.length() == 0) {
						// ignore empty text.
						continue;
					}
					if (isAllWhitespace(c)) {
						// we skip all whitespace.
						// but record the space
						if (!first) {
							dospace = true;
						}
						continue;
					}
					if (!first && !dospace && 
							Verifier.isXMLWhitespace(val.charAt(0))) {
						// we are between content, and need to put in a space.
						dospace = true;
					}
					endspace = Verifier.isXMLWhitespace(val.charAt(val.length() - 1));
				}
				first = false;
				if (dospace) {
					printString(out, " ", TextMode.PRESERVE);
				}
					
				if (c instanceof CDATA) {
					printCDATA(out, (CDATA)c, TextMode.NORMALIZE);
				} else if (c instanceof Text) {
					printText(out, (Text)c, TextMode.NORMALIZE);
				} else {
					printEntityRef(out, (EntityRef)c);
				}
				dospace = endspace;
			}
			return;
		}
		
		// All that's left now is multi-content that needs to be trimmed.
		// For this, we do a cheat.... 
		// we trim the left and right content, and print any middle content
		// as raw.
		// we have to preserve the right-whitespace of the left content, and the
		// left whitespace of the right content.
		if (TextMode.TRIM == mode) {
		
			int left = start;
			while (left < end && isAllWhitespace(content.get(left))) {
				left ++;
			}
			int right = end - 1;
			while (right > left && isAllWhitespace(content.get(right))) {
				right--;
			}
			if (left == right) {
				// came down to just one value to output.
				printTextRange(out, content, left, right + 1, mode);
				return;
			}
			Content leftc = content.get(left);
			if (leftc instanceof EntityRef) {
				printEntityRef(out, (EntityRef)leftc);
			} else {
				String val = ((Text)leftc).getText();
				char[] chars = val.toCharArray();
				int cx = chars.length;
				while (cx > 0 && Verifier.isXMLWhitespace(chars[cx - 1])) {
					cx--;
				}
				if (leftc instanceof CDATA) {
					printCDATA(out, (CDATA)leftc, TextMode.TRIM);
				} else {
					printText(out, (Text)leftc, TextMode.TRIM);
				}
				if (cx < chars.length) {
					printString(out, new String(chars, cx, chars.length - cx), TextMode.PRESERVE);
				}
			}
			
			// Right, we have dealt with the left.
			// now deal with the middle.
			printTextRange(out, content, left + 1, right, TextMode.PRESERVE);
			
			Content rightc = content.get(right);
			if (rightc instanceof EntityRef) {
				printEntityRef(out, (EntityRef)rightc);
			} else {
				String val = ((Text)rightc).getText();
				char[] chars = val.toCharArray();
				int cx = 0;
				while (cx < chars.length && Verifier.isXMLWhitespace(chars[cx])) {
					cx++;
				}
				if (cx > 0) {
					printString(out, new String(chars, 0, cx), TextMode.PRESERVE);
				}
				if (rightc instanceof CDATA) {
					printCDATA(out, (CDATA)rightc, TextMode.TRIM);
				} else {
					printText(out, (Text)rightc, TextMode.TRIM);
				}
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
	private void printNamespace(Writer out, Namespace ns)
			throws IOException {
		String prefix = ns.getPrefix();
		String uri = ns.getURI();

		out.write(" xmlns");
		if (!prefix.equals("")) {
			out.write(":");
			out.write(prefix);
		}
		out.write("=\"");
		out.write(escapeAttributeEntities(uri));
		out.write("\"");
	}

	/**
	 * This will handle printing of a <code>{@link Attribute}</code> list.
	 *
	 * @param attributes <code>List</code> of Attribute objcts
	 * @param out <code>Writer</code> to use
	 */
	protected void printAttributes(Writer out, List<Attribute> attributes)
			throws IOException {

		// I do not yet handle the case where the same prefix maps to
		// two different URIs. For attributes on the same element
		// this is illegal; but as yet we don't throw an exception
		// if someone tries to do this
		// Set prefixes = new HashSet();
		for (Attribute attribute : attributes) {

			out.write(" ");
			printQualifiedName(out, attribute);
			out.write("=");

			out.write("\"");
			out.write(escapeAttributeEntities(attribute.getValue()));
			out.write("\"");
		}
	}

	private void printElementNamespaces(Writer out, Element element, NamespaceStack stack)
			throws IOException {
		// Add namespace decl only if it's not the XML namespace and it's
		// not the NO_NAMESPACE with the prefix "" not yet mapped
		// (we do output xmlns="" if the "" prefix was already used and we
		// need to reclaim it for the NO_NAMESPACE)
		for (Namespace ns : stack.addedForward()) {
			// use the getParentElement() call here, because isRootElement()
			// returns 'false' for detached elements
			if (ns == Namespace.NO_NAMESPACE && element.getParentElement() == null) {

				// never print the NO-Namespace, unless it is overridden
				// back at a lower level.
				continue;
			}
			printNamespace(out, ns);
		}
	}


	// * * * * * * * * * * Support methods * * * * * * * * * *
	// * * * * * * * * * * Support methods * * * * * * * * * *

	/**
	 * This will print a newline only if indent is not null.
	 *
	 * @param out <code>Writer</code> to use
	 */
	private void newline(Writer out) throws IOException {
		if (currentFormat.indent != null) {
			out.write(currentFormat.lineSeparator);
		}
	}

	/**
	 * This will print indents only if indent is not null or the empty string.
	 *
	 * @param out <code>Writer</code> to use
	 * @param level current indent level
	 */
	private void indent(Writer out, int level) throws IOException {
		if (currentFormat.indent == null ||
				currentFormat.indent.equals("")) {
			return;
		}

		for (int i = 0; i < level; i++) {
			out.write(currentFormat.indent);
		}
	}

	// Returns the index of the first non-all-whitespace CDATA or Text,
	// index = content.size() is returned if content contains
	// all whitespace.
	// @param start index to begin search (inclusive)
	private int skipLeadingWhite(List<? extends Content> content, int start) {
		if (start < 0) {
			start = 0;
		}

		int index = start;
		int size = content.size();
		if (currentFormat.mode == Format.TextMode.TRIM_FULL_WHITE
				|| currentFormat.mode == Format.TextMode.NORMALIZE
				|| currentFormat.mode == Format.TextMode.TRIM) {
			while (index < size) {
				if (!isAllWhitespace(content.get(index))) {
					return index;
				}
				index++;
			}
		}
		return index;
	}

	// Return the next non-CDATA, non-Text, or non-EntityRef node,
	// index = content.size() is returned if there is no more non-CDATA,
	// non-Text, or non-EntiryRef nodes
	// @param start index to begin search (inclusive)
	private static int nextNonText(List<? extends Content> content, int start) {
		if (start < 0) {
			start = 0;
		}

		int index = start;
		int size = content.size();
		while (index < size) {
			Object node =  content.get(index);
			if (!((node instanceof Text) || (node instanceof EntityRef))) {
				return index;
			}
			index++;
		}
		return size;
	}

	// Determine if a Object is all whitespace
	private boolean isAllWhitespace(Object obj) {
		String str = null;

		if (obj instanceof Text) {
			str = ((Text) obj).getText();
		}
		else if (obj instanceof EntityRef) {
			return false;
		}
		else {
			return false;
		}

		for (int i = 0; i < str.length(); i++) {
			if (!Verifier.isXMLWhitespace(str.charAt(i)))
				return false;
		}
		return true;
	}

	/**
	 * This will take the pre-defined entities in XML 1.0 and
	 * convert their character representation to the appropriate
	 * entity reference, suitable for XML attributes.  It does not convert
	 * the single quote (') because it's not necessary as the outputter
	 * writes attributes surrounded by double-quotes.
	 *
	 * @param str <code>String</code> input to escape.
	 * @return <code>String</code> with escaped content.
	 * @throws IllegalArgumentException if an entity can not be escaped
	 */
	public String escapeAttributeEntities(String str) {
		StringBuffer buffer;
		int ch, pos;
		String entity;
		EscapeStrategy strategy = currentFormat.escapeStrategy;

		buffer = null;
		for (int i = 0; i < str.length(); i++) {
			ch = str.charAt(i);
			pos = i;
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
				case '\r' :
					entity = "&#xD;";
					break;
				case '\t' :
					entity = "&#x9;";
					break;
				case '\n' :
					entity = "&#xA;";
					break;
				default :

					if (strategy.shouldEscape((char) ch)) {       
						// Make sure what we are escaping is not the
						// Beginning of a multi-byte character.
						if (Verifier.isHighSurrogate((char) ch)) {
							// This is a the high of a surrogate pair
							i++;                    		
							if (i < str.length()) {
								char low = str.charAt(i);
								if(!Verifier.isLowSurrogate(low)) {
									throw new IllegalDataException("Could not decode surrogate pair 0x" +
											Integer.toHexString(ch) + " / 0x" + Integer.toHexString(low));
								}
								ch = Verifier.decodeSurrogatePair((char) ch, low);
							} else {
								throw new IllegalDataException("Surrogate pair 0x" +
										Integer.toHexString(ch) + " truncated");
							}
						}
						entity = "&#x" + Integer.toHexString(ch) + ";";
					}
					else {
						entity = null;
					}
					break;
			}
			if (buffer == null) {
				if (entity != null) {
					// An entity occurred, so we'll have to use StringBuffer
					// (allocate room for it plus a few more entities).
					buffer = new StringBuffer(str.length() + 20);
					// Copy previous skipped characters and fall through
					// to pickup current character
					buffer.append(str.substring(0, pos));
					buffer.append(entity);
				}
			}
			else {
				if (entity == null) {
					buffer.append((char) ch);
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
	 * @param str <code>String</code> input to escape.
	 * @return <code>String</code> with escaped content.
	 * @throws IllegalArgumentException if an entity can not be escaped
	 */
	public String escapeElementEntities(String str) {
		if (escapeOutput == false) return str;

		StringBuffer buffer;
		int ch, pos;
		String entity;
		EscapeStrategy strategy = currentFormat.escapeStrategy;

		buffer = null;
		for (int i = 0; i < str.length(); i++) {
			ch = str.charAt(i);
			pos = i;
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
				case '\r' :
					entity = "&#xD;";
					break;
				case '\n' :
					entity = currentFormat.lineSeparator;
					break;
				default :

					if (strategy.shouldEscape((char) ch)) {

						//make sure what we are escaping is not the 
						//beginning of a multi-byte character. 
						if(Verifier.isHighSurrogate((char) ch)) {
							//this is a the high of a surrogate pair
							i++;
							if (i < str.length()) {
								char low = str.charAt(i);
								if(!Verifier.isLowSurrogate(low)) {
									throw new IllegalDataException("Could not decode surrogate pair 0x" +
											Integer.toHexString(ch) + " / 0x" + Integer.toHexString(low));
								}
								ch = Verifier.decodeSurrogatePair((char) ch, low);
							} else {
								throw new IllegalDataException("Surrogate pair 0x" +
										Integer.toHexString(ch) + " truncated");
							}
						}
						entity = "&#x" + Integer.toHexString(ch) + ";";
					}
					else {
						entity = null;
					}
					break;
			}
			if (buffer == null) {
				if (entity != null) {
					// An entity occurred, so we'll have to use StringBuffer
					// (allocate room for it plus a few more entities).
					buffer = new StringBuffer(str.length() + 20);
					// Copy previous skipped characters and fall through
					// to pickup current character
					buffer.append(str.substring(0, pos));
					buffer.append(entity);
				}
			}
			else {
				if (entity == null) {
					buffer.append((char) ch);
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
	 * Returns a copy of this XMLOutputter.
	 */
	@Override
	public XMLOutputter clone() {
		// Implementation notes: Since all state of an XMLOutputter is
		// embodied in simple private instance variables, Object.clone
		// can be used.  Note that since Object.clone is totally
		// broken, we must catch an exception that will never be
		// thrown.
		try {
			return (XMLOutputter)super.clone();
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
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < userFormat.lineSeparator.length(); i++) {
			char ch = userFormat.lineSeparator.charAt(i);
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
				"XMLOutputter[omitDeclaration = " + userFormat.omitDeclaration + ", " +
						"encoding = " + userFormat.encoding + ", " +
						"omitEncoding = " + userFormat.omitEncoding + ", " +
						"indent = '" + userFormat.indent + "'" + ", " +
						"expandEmptyElements = " + userFormat.expandEmptyElements + ", " +
						"lineSeparator = '" + buffer.toString() + "', " +
						"textMode = " + userFormat.mode + "]"
				);
	}

	// Support method to print a name without using elt.getQualifiedName()
	// and thus avoiding a StringBuffer creation and memory churn
	private void printQualifiedName(Writer out, Element e) throws IOException {
		if (e.getNamespace().getPrefix().length() == 0) {
			out.write(e.getName());
		}
		else {
			out.write(e.getNamespace().getPrefix());
			out.write(':');
			out.write(e.getName());
		}
	}

	// Support method to print a name without using att.getQualifiedName()
	// and thus avoiding a StringBuffer creation and memory churn
	private void printQualifiedName(Writer out, Attribute a) throws IOException {
		String prefix = a.getNamespace().getPrefix();
		if ((prefix != null) && (!prefix.equals(""))) {
			out.write(prefix);
			out.write(':');
			out.write(a.getName());
		}
		else {
			out.write(a.getName());
		}
	}

	// * * * * * * * * * * Deprecated methods * * * * * * * * * *

	/* The methods below here are deprecations of protected methods.  We
	 * don't usually deprecate protected methods, so they're commented out.
	 * They're left here in case this mass deprecation causes people trouble.
	 * Since we're getting close to 1.0 it's actually better for people to
	 * raise issues early though.
	 */

}
