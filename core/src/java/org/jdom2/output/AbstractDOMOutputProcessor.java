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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.w3c.dom.Attr;

import org.jdom2.Attribute;
import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.EntityRef;
import org.jdom2.Namespace;
import org.jdom2.NamespaceStack;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;
import org.jdom2.Verifier;
import org.jdom2.output.Format.TextMode;

/**
 * This class provides a concrete implementation of {@link DOMOutputProcessor}
 * for supporting the {@link DOMOutputter}.
 * <p>
 * <h2>Overview</h2>
 * <p>
 * This class is marked abstract even though all methods are fully implemented.
 * The <code>process*(...)</code> methods are public because they match the
 * DOMOutputProcessor interface but the remaining methods are all protected.
 * <p>
 * People who want to create a custom DOMOutputProcessor for DOMOutputter
 * are able to extend this class and modify any functionality they want. Before
 * sub-classing this you should first check to see if the {@link Format} class
 * can get you the results you want.
 * <p>
 * <b><i>Subclasses of this should have reentrant methods.</i></b> This is
 * easiest to accomplish simply by not allowing any instance fields. If your
 * sub-class has an instance field/variable, then it's probably broken.
 * <p>
 * <h2>The Stacks</h2>
 * <p>
 * One significant feature of this implementation is that it creates and
 * maintains both a {@link NamespaceStack} and {@link FormatStack} that are
 * managed in the
 * {@link #printElement(FormatStack, NamespaceStack, org.w3c.dom.Document, Element)}
 * method. The stacks are pushed and popped in that method only. They
 * significantly improve the performance and readability of the code.
 * <p>
 * The NamespaceStack is only sent through to the
 * {@link #printElement(FormatStack, NamespaceStack, org.w3c.dom.Document, Element)}
 * and
 * {@link #helperContent(FormatStack, NamespaceStack, org.w3c.dom.Document, org.w3c.dom.Node, List)}
 * methods, but the FormatStack is pushed through to all print* Methods.
 * <p>
 * <h2>Text Processing</h2>
 * <p>
 * In XML the concept of 'Text' can be loosely defined as anything that can be
 * found between an Element's start and end tags, excluding Comments and
 * Processing Instructions. When considered from a JDOM perspective, this means
 * {@link Text}, {@link CDATA} and {@link EntityRef} content. This will be
 * referred to as 'text-type content'
 * <p>
 * In the context of DOMOutputter, {@link EntityRef} content is considered
 * to be text-type content. This happens because when XML is parsed the embedded
 * entity reference tokens are (normally) expanded to their replacement text. If
 * an EntityRef content has survived to this output stage it means that it
 * cannot (or should not) be expanded. As a result, it should simply be output
 * as it's escaped name in the form <code>&amp;refname;</code>.
 * <p>
 * Consecutive text-type JDOM content is an especially hard problem because the
 * complete set of consecutive values should be considered together. For
 * example, there is a significant difference between the 'trimmed' result of
 * two individual Text contents and the trimmed result of the same Text contents
 * which are combined before trimming: consider two consecutive Text contents
 * "&nbsp;X&nbsp;" and "&nbsp;Y&nbsp;" which (trimmed) should be output as:
 * "X&nbsp;&nbsp;Y" not "XY".
 * <p>
 * As a result, this class differentiates between stand-alone and consecutive
 * text-type JDOM Content. If the content is determined to be stand-alone, it is
 * fed to one of the
 * {@link #printEntityRef(FormatStack, org.w3c.dom.Document, EntityRef)},
 * {@link #printCDATA(FormatStack, org.w3c.dom.Document, CDATA)}, or
 * {@link #printText(FormatStack, org.w3c.dom.Document, Text)} methods. If there
 * is consecutive text-type content it is all fed together to the
 * {@link #helperTextConsecutive(FormatStack, org.w3c.dom.Document, org.w3c.dom.Node, List, int, int)}
 * method.
 * <p>
 * The above 4 methods for printing text-type content will all use the various
 * <code>text*(...)</code> methods to produce output to the XMLEventConsumer.
 * <p>
 * <b>Note:</b> If the Format's TextMode is PRESERVE then all text-type content
 * is considered to be stand-alone. In other words, printTextConsecutive() will
 * never be called when the TextMode is PRESERVE.
 * <p>
 * <h2>Non-Text Content</h2>
 * <p>
 * Non-text content is processed via the respective print* methods. The usage
 * should be logical based on the method name.
 * <p>
 * The general observations are:
 * <ul>
 * <li>printElement - maintains the Stacks, prints the element open tags, with
 * attributes and namespaces. It checks to see whether the Element is text-only,
 * or has non-text content. If it is text-only there is no indent/newline
 * handling and it delegates to the correct text-type print method, otherwise it
 * delegates to printContent.
 * <li>printContent is called if there is a 'mixed' content list to output. It
 * will always wrap each respective content call (one of a text-type print, a
 * printTextConsecutive, or a non-text type print) with an indent and a newline.
 * </ul>
 * <p>
 * There are two 'helper' methods which are simply there to reduce some code
 * redundancy in the class. They do nothing more than that. Have a look at the
 * source-code to see what they do. If you need them they may prove useful.
 * 
 * @see DOMOutputter
 * @see DOMOutputProcessor
 * @since JDOM2
 * @author Rolf Lear
 */
public abstract class AbstractDOMOutputProcessor extends
		AbstractOutputProcessor implements DOMOutputProcessor {

	/**
	 * This will handle adding any <code>{@link Namespace}</code> attributes to
	 * the DOM tree.
	 * 
	 * @param ns
	 *        <code>Namespace</code> to add definition of
	 */
	private static String getXmlnsTagFor(Namespace ns) {
		String attrName = "xmlns";
		if (!ns.getPrefix().equals("")) {
			attrName += ":";
			attrName += ns.getPrefix();
		}
		return attrName;
	}

	/* *******************************************
	 * DOMOutputProcessor implementation.
	 * *******************************************
	 */

	@Override
	public org.w3c.dom.Document process(org.w3c.dom.Document basedoc,
			Format format, Document doc) {
		return printDocument(new FormatStack(format), new NamespaceStack(),
				basedoc, doc);
	}

	@Override
	public org.w3c.dom.Element process(org.w3c.dom.Document basedoc,
			Format format, Element element) {
		return printElement(new FormatStack(format), new NamespaceStack(),
				basedoc, element);
	}

	@Override
	public List<org.w3c.dom.Node> process(org.w3c.dom.Document basedoc,
			Format format, List<? extends Content> list) {
		List<org.w3c.dom.Node> ret = new ArrayList<org.w3c.dom.Node>(
				list.size());
		FormatStack fstack = new FormatStack(format);
		NamespaceStack nstack = new NamespaceStack();
		for (Content c : list) {
			fstack.push();
			try {
				org.w3c.dom.Node node = helperContentDispatcher(fstack, nstack,
						basedoc, c);
				if (node != null) {
					ret.add(node);
				}
			} finally {
				fstack.pop();
			}
		}
		return ret;
	}

	@Override
	public org.w3c.dom.CDATASection process(org.w3c.dom.Document basedoc,
			Format format, CDATA cdata) {
		return printCDATA(new FormatStack(format), basedoc, cdata);
	}

	@Override
	public org.w3c.dom.Text process(org.w3c.dom.Document basedoc,
			Format format, Text text) {
		return printText(new FormatStack(format), basedoc, text);
	}

	@Override
	public org.w3c.dom.Comment process(org.w3c.dom.Document basedoc,
			Format format, Comment comment) {
		return printComment(new FormatStack(format), basedoc, comment);
	}

	@Override
	public org.w3c.dom.ProcessingInstruction process(
			org.w3c.dom.Document basedoc, Format format,
			ProcessingInstruction pi) {
		return printProcessingInstruction(new FormatStack(format), basedoc, pi);
	}

	@Override
	public org.w3c.dom.EntityReference process(org.w3c.dom.Document basedoc,
			Format format, EntityRef entity) {
		return printEntityRef(new FormatStack(format), basedoc, entity);
	}

	@Override
	public Attr process(org.w3c.dom.Document basedoc, Format format,
			Attribute attribute) {
		return printAttribute(new FormatStack(format), basedoc, attribute);
	}

	/* *******************************************
	 * Support methods for output. Should all be protected. All content-type
	 * print methods have a FormatStack. Only printContent is responsible for
	 * outputting appropriate indenting and newlines, which are easily available
	 * using the FormatStack.getLevelIndent() and FormatStack.getLevelEOL().
	 * *******************************************
	 */

	private static final void addIndent(final FormatStack fstack,
			final org.w3c.dom.Document basedoc, final org.w3c.dom.Node target) {
		final String indent = fstack.getLevelIndent();
		if (indent != null && fstack.getLevelEOL() != null) {
			org.w3c.dom.Text space = basedoc.createTextNode(indent);
			target.appendChild(space);
		}
	}

	private static final void addEOL(final FormatStack fstack,
			final org.w3c.dom.Document basedoc, final org.w3c.dom.Node target) {
		final String eol = fstack.getLevelEOL();
		if (eol != null && fstack.getLevelIndent() != null) {
			org.w3c.dom.Text space = basedoc.createTextNode(eol);
			target.appendChild(space);
		}
	}

	/**
	 * This will handle printing of a {@link Document}.
	 * 
	 * @param fstack
	 *        the FormatStack
	 * @param nstack
	 *        the NamespaceStack
	 * @param basedoc
	 *        The org.w3c.dom.Document for creating DOM Nodes
	 * @param doc
	 *        <code>Document</code> to write.
	 * @return The input JDOM document converted to a DOM document.
	 */
	protected org.w3c.dom.Document printDocument(final FormatStack fstack,
			final NamespaceStack nstack, final org.w3c.dom.Document basedoc,
			final Document doc) {

		if (!fstack.isOmitDeclaration()) {
			basedoc.setXmlVersion("1.0");
			// addEOL(fstack, basedoc, basedoc);
		}

		final int sz = doc.getContentSize();
		for (int i = 0; i < sz; i++) {
			org.w3c.dom.Node n = helperContentDispatcher(fstack, nstack,
					basedoc, doc.getContent(i));
			if (n != null) {
				basedoc.appendChild(n);
				// add a newline after each content... if newlines are needed.
				// addEOL(fstack, basedoc, basedoc);
			}

		}

		// Output final line separator
		// We output this no matter what the newline flags say
		// if (fstack.getIndent() == null && fstack.getLineSeparator() != null)
		// {
		// org.w3c.dom.Text newline =
		// basedoc.createTextNode(fstack.getLineSeparator());
		// basedoc.appendChild(newline);
		// }

		return basedoc;
	}

	/**
	 * This will handle printing of a {@link ProcessingInstruction}.
	 * 
	 * @param fstack
	 *        the FormatStack
	 * @param basedoc
	 *        The org.w3c.dom.Document for creating DOM Nodes
	 * @param pi
	 *        <code>ProcessingInstruction</code> to write.
	 * @return The input JDOM ProcessingInstruction converted to a DOM
	 *         ProcessingInstruction.
	 */
	protected org.w3c.dom.ProcessingInstruction printProcessingInstruction(
			final FormatStack fstack, final org.w3c.dom.Document basedoc,
			final ProcessingInstruction pi) {
		String target = pi.getTarget();
		String rawData = pi.getData();
		if (rawData == null || rawData.trim().length() == 0) {
			rawData = "";
		}
		return basedoc.createProcessingInstruction(target, rawData);
	}

	/**
	 * This will handle printing of a {@link Comment}.
	 * 
	 * @param fstack
	 *        the FormatStack
	 * @param basedoc
	 *        The org.w3c.dom.Document for creating DOM Nodes
	 * @param comment
	 *        <code>Comment</code> to write.
	 * @return The input JDOM Comment converted to a DOM Comment
	 */
	protected org.w3c.dom.Comment printComment(final FormatStack fstack,
			final org.w3c.dom.Document basedoc, final Comment comment) {
		return basedoc.createComment(comment.getText());
	}

	/**
	 * This will handle printing of an {@link EntityRef}.
	 * 
	 * @param fstack
	 *        the FormatStack
	 * @param basedoc
	 *        The org.w3c.dom.Document for creating DOM Nodes
	 * @param entity
	 *        <code>EntotyRef</code> to write.
	 * @return The input JDOM EntityRef converted to a DOM EntityReference
	 */
	protected org.w3c.dom.EntityReference printEntityRef(
			final FormatStack fstack, final org.w3c.dom.Document basedoc,
			final EntityRef entity) {
		return basedoc.createEntityReference(entity.getName());
	}

	/**
	 * This will handle printing of a {@link CDATA}.
	 * 
	 * @param fstack
	 *        the FormatStack
	 * @param basedoc
	 *        The org.w3c.dom.Document for creating DOM Nodes
	 * @param cdata
	 *        <code>CDATA</code> to write.
	 * @return The input JDOM CDATA converted to a DOM CDATASection
	 */
	protected org.w3c.dom.CDATASection printCDATA(final FormatStack fstack,
			final org.w3c.dom.Document basedoc, final CDATA cdata) {
		// CDATAs are treated like text, not indented/newline content.
		String text = cdata.getText();
		switch (fstack.getTextMode()) {
			case PRESERVE:
				break;
			case NORMALIZE:
				text = textCompact(text);
				if (text.length() == 0) {
					text = null;
				}
				break;
			case TRIM:
				text = textTrimBoth(text);
				if (text.length() == 0) {
					text = null;
				}
				break;
			case TRIM_FULL_WHITE:
				text = textTrimFullWhite(text);
				if (text.length() == 0) {
					text = null;
				}
				break;
		}
		if (text != null) {
			return basedoc.createCDATASection(text);
		}
		return null;
	}

	/**
	 * This will handle printing of a {@link Text}.
	 * 
	 * @param fstack
	 *        the FormatStack
	 * @param basedoc
	 *        The org.w3c.dom.Document for creating DOM Nodes
	 * @param text
	 *        <code>Text</code> to write.
	 * @return The input JDOM Text converted to a DOM Text
	 */
	protected org.w3c.dom.Text printText(final FormatStack fstack,
			final org.w3c.dom.Document basedoc, final Text text) {
		String str = text.getText();
		switch (fstack.getTextMode()) {
			case PRESERVE:
				break;
			case NORMALIZE:
				str = textCompact(str);
				break;
			case TRIM:
				str = textTrimBoth(str);
				break;
			case TRIM_FULL_WHITE:
				str = textTrimFullWhite(str);
				break;
		}
		if (str != null) {
			return basedoc.createTextNode(str);
		}
		return null;
	}

	/**
	 * This will handle printing of a {@link Attribute}.
	 * 
	 * @param fstack
	 *        the FormatStack
	 * @param basedoc
	 *        The org.w3c.dom.Document for creating DOM Nodes
	 * @param attribute
	 *        <code>Attribute</code> to write.
	 * @return The input JDOM Attribute converted to a DOM Attr
	 */
	protected org.w3c.dom.Attr printAttribute(final FormatStack fstack,
			final org.w3c.dom.Document basedoc, final Attribute attribute) {
		org.w3c.dom.Attr attr = basedoc.createAttributeNS(
				attribute.getNamespaceURI(), attribute.getQualifiedName());
		attr.setValue(attribute.getValue());
		return attr;
	}

	/**
	 * This will handle printing of an {@link Element}.
	 * <p>
	 * This method arranges for outputting the Element infrastructure including
	 * Namespace Declarations and Attributes.
	 * <p>
	 * There has to be a fair amount of inspection to determine whether there is
	 * any content and whether it is all text-type content, and based on the
	 * required Format output, how to deal with the content. Alternatives (in
	 * 'fall-through' order) are:
	 * <ol>
	 * <li>There is no content, then output just an empty element (depending on
	 * settings this may be either <code>&ltelement /&gt</code> or
	 * <code>&ltelement&gt&lt/element&gt</code>).
	 * <li>There is only text-type content and the current Format settings would
	 * condense the text-type content to nothing, then output just an empty
	 * element (depending on settings this may be either
	 * <code>&ltelement /&gt</code> or <code>&ltelement&gt&lt/element&gt</code>
	 * ).
	 * <li>There is just a single text-type content then delegate to that
	 * content's print method
	 * <li>There is only text-type content (more than one though) then delegate
	 * to
	 * {@link #helperTextConsecutive(FormatStack, org.w3c.dom.Document, org.w3c.dom.Node, List, int, int)}
	 * <li>There is content other than just text-type content, then delegate to
	 * {@link #helperContent(FormatStack, NamespaceStack, org.w3c.dom.Document, org.w3c.dom.Node, List)}
	 * </ol>
	 * <p>
	 * 
	 * @param fstack
	 *        the FormatStack
	 * @param nstack
	 *        the NamespaceStack
	 * @param basedoc
	 *        The org.w3c.dom.Document for creating DOM Nodes
	 * @param element
	 *        <code>Element</code> to write.
	 * @return The input JDOM Element converted to a DOM Element
	 */
	protected org.w3c.dom.Element printElement(final FormatStack fstack,
			final NamespaceStack nstack, final org.w3c.dom.Document basedoc,
			final Element element) {

		nstack.push(element);
		try {

			TextMode textmode = fstack.getTextMode();

			// Check for xml:space and adjust format settings
			final String space = element.getAttributeValue("space",
					Namespace.XML_NAMESPACE);

			if ("default".equals(space)) {
				textmode = fstack.getDefaultMode();
			} else if ("preserve".equals(space)) {
				textmode = TextMode.PRESERVE;
			}

			org.w3c.dom.Element ret = basedoc.createElementNS(
					element.getNamespaceURI(), element.getQualifiedName());

			for (Namespace ns : nstack.addedForward()) {
				if (ns == Namespace.XML_NAMESPACE) {
					continue;
				}
				ret.setAttribute(getXmlnsTagFor(ns), ns.getURI());
			}

			for (Attribute att : element.getAttributes()) {
				ret.setAttributeNodeNS(printAttribute(fstack, basedoc, att));
			}

			final List<Content> content = element.getContent();

			// is there only text content
			// text content is Text/CDATA/EntityRef
			boolean textonly = true;
			// is the text content only whitespace.
			// whiteonly is obvious....
			boolean whiteonly = true;

			for (Content k : content) {
				if (k instanceof Text) {
					if (whiteonly) {
						if (textmode == TextMode.PRESERVE) {
							whiteonly = false;
						} else {
							// look for non-whitespace.
							String val = k.getValue();
							int cc = val.length();
							while (--cc >= 0) {
								if (!Verifier.isXMLWhitespace(val.charAt(cc))) {
									// found non-whitespace;
									whiteonly = false;
									break;
								}
							}
						}
					}
				} else {
					whiteonly = false;
					if (!(k instanceof EntityRef)) {
						textonly = false;
						// we know everything we need.
						break;
					}
				}
				if (!whiteonly && !textonly) {
					break;
				}
			}

			// OK, now we print out the meat of the Element
			if (!content.isEmpty()) {
				if (textonly) {
					if (!whiteonly) {
						fstack.push();
						try {
							fstack.setTextMode(textmode);
							helperTextType(fstack, basedoc, ret, content, 0,
									content.size());
						} finally {
							fstack.pop();
						}
					}
				} else {
					fstack.push();
					try {
						fstack.setTextMode(textmode);
						addEOL(fstack, basedoc, ret);
						helperContent(fstack, nstack, basedoc, ret, content);

					} finally {
						fstack.pop();
					}
					addIndent(fstack, basedoc, ret);

				}
			}

			return ret;

		} finally {
			nstack.pop();
		}
	}

	/**
	 * This will handle printing of a List of {@link Content}.
	 * <p>
	 * The list of Content is basically processed as one of three types of
	 * content
	 * <ol>
	 * <li>Consecutive text-type (Text, CDATA, and EntityRef) content
	 * <li>Stand-alone text-type content
	 * <li>Non-text-type content.
	 * </ol>
	 * Although the code looks complex, the theory is conceptually simple:
	 * <ol>
	 * <li>identify one of the three types (consecutive, stand-alone, non-text)
	 * <li>do indent if any is specified.
	 * <li>send the type to the respective print* handler (e.g.
	 * {@link #helperTextConsecutive(FormatStack, org.w3c.dom.Document, org.w3c.dom.Node, List, int, int)}, {@link #printCDATA(FormatStack, org.w3c.dom.Document, CDATA)}, or
	 * {@link #printComment(FormatStack, org.w3c.dom.Document, Comment)},
	 * <li>do a newline if one is specified.
	 * <li>loop back to 1. until there's no more content to process.
	 * </ol>
	 * 
	 * @param fstack
	 *        the FormatStack
	 * @param nstack
	 *        the NamespaceStack
	 * @param basedoc
	 *        The org.w3c.dom.Document for creating DOM Nodes
	 * @param target
	 *        the DOM node this content should be appended to.
	 * @param content
	 *        <code>List</code> of <code>Content</code> to write.
	 */
	protected void helperContent(final FormatStack fstack,
			final NamespaceStack nstack, final org.w3c.dom.Document basedoc,
			final org.w3c.dom.Node target, final List<? extends Content> content) {

		// Basic theory of operation is as follows:
		// 0. raw-print for PRESERVE.
		// 1. print any non-Text content
		// 2. Accumulate all sequential text content and print them together
		// Do not do any newlines before or after content.
		if (fstack.getTextMode() == TextMode.PRESERVE) {
			for (Content c : content) {
				org.w3c.dom.Node n = helperContentDispatcher(fstack, nstack,
						basedoc, c);
				if (n != null) {
					target.appendChild(n);
				}
			}
			return;
		}

		int txti = -1;
		int index = 0;
		for (final Content c : content) {
			switch (c.getCType()) {
				case Text:
				case CDATA:
				case EntityRef:
					//
					// Handle consecutive CDATA, Text, and EntityRef nodes all
					// at once
					//
					if (txti < 0) {
						txti = index;
					}
					break;
				default:
					if (txti >= 0) {
						// we have text content we need to print.
						// we also know that we are 'mixed' content
						// so the text content should be indented.
						// but, since we have no PRESERVE content, only the
						// 'trimming' variants, if it is all whitespace we do
						// not want to even print the indent/newline.
						if (!isAllWhiteSpace(content, txti, index - txti)) {
							addIndent(fstack, basedoc, target);
							helperTextType(fstack, basedoc, target, content,
									txti, index - txti);
							addEOL(fstack, basedoc, target);
						}
					}
					txti = -1;

					addIndent(fstack, basedoc, target);
					target.appendChild(helperContentDispatcher(fstack, nstack,
							basedoc, c));
					addEOL(fstack, basedoc, target);
			}
			index++;
		}
		if (txti >= 0) {
			// we have text content we need to print.
			// we also know that we are 'mixed' content
			// so the text content should be indented.
			// but, since we have no PRESERVE content, only the
			// 'trimming' variants, if it is all whitespace we do
			// not want to even print the indent/newline.
			if (!isAllWhiteSpace(content, txti, index - txti)) {
				addIndent(fstack, basedoc, target);
				helperTextType(fstack, basedoc, target, content, txti, index
						- txti);
				addEOL(fstack, basedoc, target);
			}
		}

	}

	/**
	 * This will handle printing of a consecutive sequence of text-type
	 * {@link Content} (<code>{@link CDATA}</code> <code>{@link Text}</code>, or
	 * <code>{@link EntityRef}</code>) nodes. It is an error (unspecified
	 * Exception or unspecified behaviour) to pass this method any other type of
	 * node.
	 * <p>
	 * It is a requirement for this method that at least one of the specified
	 * text-type instances has non-whitespace, or, put the other way, if all
	 * specified text-type values are all white-space only, then you may get odd
	 * looking XML (empty lines).
	 * 
	 * @param fstack
	 *        the FormatStack
	 * @param basedoc
	 *        The org.w3c.dom.Document for creating DOM Nodes
	 * @param target
	 *        The DOM nodes the text content will be added to.
	 * @param content
	 *        <code>List</code> of <code>Content</code> to write.
	 * @param offset
	 *        index of first content node (inclusive).
	 * @param len
	 *        number of nodes that are text-type.
	 */
	protected void helperTextConsecutive(FormatStack fstack,
			final org.w3c.dom.Document basedoc, final org.w3c.dom.Node target,
			final List<? extends Content> content, final int offset,
			final int len) {

		// we are guaranteed that len >= 2
		assert len >= 2 : "Need at least two text-types to be 'Consecutive'";

		// here we have a sequence of Text nodes to print.
		// we follow the TextMode rules for it.
		// also guaranteed is that at least one of the nodes has some real text.

		final TextMode mode = fstack.getTextMode();

		// Right, at this point it is complex.
		// we have multiple nodes to output, and it could be in one of three
		// modes, TRIM, TRIM_FULL_WHITE, or NORMALIZE
		// we aggregate the text together, and dump it

		// Let's get TRIM_FULL_WHITE out of the way.
		// in this mode, if there is any real text content, we print it all.
		// otherwise we print nothing
		// since it is a requirement for this method that there has to be some
		// non-whitespace text, we can just print everything.
		if (TextMode.TRIM_FULL_WHITE == mode) {
			// Right, if we find text, we print it all.
			// If we don't we print nothing.
			int cnt = len;
			for (ListIterator<? extends Content> li = content
					.listIterator(offset); cnt > 0; cnt--) {
				helperRawTextType(fstack, basedoc, target, li.next());
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
			int cnt = len;
			for (Iterator<? extends Content> li = content.listIterator(offset); cnt > 0; cnt--) {
				boolean endspace = false;
				final Content c = li.next();
				if (c instanceof Text) {
					final String val = ((Text) c).getValue();
					if (val.length() == 0) {
						// ignore empty text.
						continue;
					}
					if (isAllWhitespace(val)) {
						// we skip all whitespace.
						// but record the space
						if (!first) {
							dospace = true;
						}
						continue;
					}
					if (!first && !dospace
							&& Verifier.isXMLWhitespace(val.charAt(0))) {
						// we are between content, and need to put in a space.
						dospace = true;
					}
					endspace = Verifier
							.isXMLWhitespace(val.charAt(val.length() - 1));
				}
				first = false;
				if (dospace) {
					target.appendChild(basedoc.createTextNode(" "));
				}

				switch (c.getCType()) {
					case Text:
						target.appendChild(basedoc
								.createTextNode(textCompact(((Text) c)
										.getText())));
						break;
					case CDATA:
						target.appendChild(basedoc
								.createCDATASection(textCompact(((Text) c)
										.getText())));
						break;
					case EntityRef:
						target.appendChild(printEntityRef(fstack, basedoc,
								(EntityRef) c));
					default:
						// do nothing
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
		// no need to check the mode, it can only be TRIM.
		if (TextMode.TRIM == fstack.getTextMode()) {

			int lcnt = 0;
			while (lcnt < len) {
				Content node = content.get(offset + lcnt);
				if (!(node instanceof Text)
						|| !isAllWhitespace(node.getValue())) {
					break;
				}
				lcnt++;
			}

			if (lcnt == len) {
				return;
			}

			final int left = offset + lcnt;
			int rcnt = len - 1;
			while (rcnt > lcnt) {
				Content node = content.get(offset + rcnt);
				if (!(node instanceof Text)
						|| !isAllWhitespace(node.getValue())) {
					break;
				}
				rcnt--;
			}
			final int right = offset + rcnt;

			if (left == right) {
				// came down to just one value to output.
				final Content node = content.get(left);
				switch (node.getCType()) {
					case Text:
						target.appendChild(basedoc
								.createTextNode(textTrimBoth(((Text) node)
										.getText())));
						break;
					case CDATA:
						target.appendChild(basedoc
								.createCDATASection(textTrimBoth(((CDATA) node)
										.getText())));
						break;
					case EntityRef:
						target.appendChild(printEntityRef(fstack, basedoc,
								(EntityRef) node));
					default:
						// do nothing
				}
				return;
			}
			final Content leftc = content.get(left);
			switch (leftc.getCType()) {
				case Text:
					target.appendChild(basedoc
							.createTextNode(textTrimLeft(((Text) leftc)
									.getText())));
					break;
				case CDATA:
					target.appendChild(basedoc
							.createCDATASection(textTrimLeft(((CDATA) leftc)
									.getText())));
					break;
				case EntityRef:
					target.appendChild(printEntityRef(fstack, basedoc,
							(EntityRef) leftc));
				default:
					// do nothing
			}

			int cnt = right - left - 1;
			for (ListIterator<? extends Content> li = content
					.listIterator(left + 1); cnt > 0; cnt--) {
				helperRawTextType(fstack, basedoc, target, li.next());
			}

			final Content rightc = content.get(right);
			switch (rightc.getCType()) {
				case Text:
					target.appendChild(basedoc
							.createTextNode(textTrimRight(((Text) rightc)
									.getText())));
					break;
				case CDATA:
					target.appendChild(basedoc
							.createCDATASection(textTrimRight(((CDATA) rightc)
									.getText())));
					break;
				case EntityRef:
					target.appendChild(printEntityRef(fstack, basedoc,
							(EntityRef) rightc));
				default:
					// do nothing
			}
			return;
		}
		// For when there's just one thing to print, or in RAW mode we just
		// print the contents...
		// ... its the simplest mode.
		// so simple, in fact that it is dealt with differently as a special
		// case.
		for (int i = 0; i < len; i++) {
			final Content rightc = content.get(offset + i);
			switch (rightc.getCType()) {
				case Text:
					target.appendChild(basedoc.createTextNode(((Text) rightc)
							.getText()));
					break;
				case CDATA:
					target.appendChild(basedoc
							.createCDATASection(((CDATA) rightc).getText()));
					break;
				case EntityRef:
					target.appendChild(printEntityRef(fstack, basedoc,
							(EntityRef) rightc));
				default:
					// do nothing
			}
		}
		// assert TextMode.PRESERVE != mode :
		// "PRESERVE text-types are not Consecutive";
	}

	/**
	 * This method contains code which is reused in a number of places. It
	 * simply determines what content is passed in, and dispatches it to the
	 * correct print* method.
	 * 
	 * @param fstack
	 *        The current FormatStack
	 * @param nstack
	 *        the NamespaceStack
	 * @param basedoc
	 *        The org.w3c.dom.Document for creating DOM Nodes
	 * @param content
	 *        The content to dispatch
	 * @return the input JDOM Content converted to a DOM Node.
	 */
	protected org.w3c.dom.Node helperContentDispatcher(
			final FormatStack fstack, final NamespaceStack nstack,
			final org.w3c.dom.Document basedoc, final Content content) {
		switch (content.getCType()) {
			case CDATA:
				return printCDATA(fstack, basedoc, (CDATA) content);
			case Comment:
				return printComment(fstack, basedoc, (Comment) content);
			case Element:
				return printElement(fstack, nstack, basedoc, (Element) content);
			case EntityRef:
				return printEntityRef(fstack, basedoc, (EntityRef) content);
			case ProcessingInstruction:
				return printProcessingInstruction(fstack, basedoc,
						(ProcessingInstruction) content);
			case Text:
				return printText(fstack, basedoc, (Text) content);
			case DocType:
				return null;
			default:
				throw new IllegalStateException("Unexpected Content "
						+ content.getCType());
		}
	}

	/**
	 * This helper determines how much text-type content there is to print. If
	 * there is a single node only, then it is dispatched to the respective
	 * print* method, otherwise it is dispatched to the printTextConsecutive
	 * method.
	 * <p>
	 * It is a requirement for this method that at least one of the specified
	 * text-type instances has non-whitespace, or, put the other way, if all
	 * specified text-type values are all white-space only, then you may get odd
	 * looking XML (empty lines).
	 * <p>
	 * Odd things can happen too if the len is <= 0;
	 * 
	 * @param fstack
	 *        the current FormatStack
	 * @param basedoc
	 *        The org.w3c.dom.Document for creating DOM Nodes
	 * @param target
	 *        The target to add the nodes to.
	 * @param content
	 *        The list of Content to process
	 * @param offset
	 *        The offset of the first text-type content in the sequence
	 * @param len
	 *        the number of text-type content in the sequence
	 */
	protected final void helperTextType(final FormatStack fstack,
			final org.w3c.dom.Document basedoc, final org.w3c.dom.Node target,
			final List<? extends Content> content, final int offset,
			final int len) {

		assert len > 0 : "All calls to this should have *some* content.";

		if (len == 1) {
			final Content node = content.get(offset);
			org.w3c.dom.Node domnode = null;
			// Print the node
			switch (node.getCType()) {
				case Text:
					domnode = printText(fstack, basedoc, (Text) node);
					break;
				case CDATA:
					domnode = printCDATA(fstack, basedoc, (CDATA) node);
					break;
				case EntityRef:
					domnode = printEntityRef(fstack, basedoc, (EntityRef) node);
				default:
					// do nothing
			}
			// snould never be nul coming from here... but just in case.
			if (domnode != null) {
				target.appendChild(domnode);
			}
		} else {
			// we have text content we need to print.
			// we also know that we are 'mixed' content
			// so the text content should be indented.
			// Additionally
			helperTextConsecutive(fstack, basedoc, target, content, offset, len);
		}
	}

	/**
	 * This method contains code which is reused in a number of places. It
	 * simply determines which of the text-type content is passed in, and
	 * dispatches it to the correct text method (textEscapeRaw, textCDATARaw, or
	 * textEntityRef).
	 * 
	 * @param fstack
	 *        The current FormatStack
	 * @param basedoc
	 *        The org.w3c.dom.Document for creating DOM Nodes
	 * @param target
	 *        The DOM nodes the text content will be added to.
	 * @param content
	 *        The content to dispatch
	 */
	protected final void helperRawTextType(final FormatStack fstack,
			final org.w3c.dom.Document basedoc, final org.w3c.dom.Node target,
			final Content content) {
		switch (content.getCType()) {
			case Text:
				target.appendChild(basedoc.createTextNode(content.getValue()));
				break;
			case CDATA:
				target.appendChild(basedoc.createCDATASection(content
						.getValue()));
				break;
			case EntityRef:
				target.appendChild(basedoc
						.createEntityReference(((EntityRef) content).getName()));
				break;
			default:
				// do nothing
		}
	}

}
