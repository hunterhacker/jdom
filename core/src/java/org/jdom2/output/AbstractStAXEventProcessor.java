package org.jdom2.output;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.util.XMLEventConsumer;

import org.jdom2.Attribute;
import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.Content;
import org.jdom2.DocType;
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
 * This class provides a concrete implementation of {@link StAXEventProcessor}
 * for supporting the {@link StAXEventOutputter}.
 * <p>
 * <h2>Overview</h2>
 * <p>
 * This class is marked abstract even though all methods are fully implemented.
 * The <code>process*(...)</code> methods are public because they match the
 * StAXEventProcessor interface but the remaining methods are all protected.
 * <p>
 * People who want to create a custom StAXEventProcessor for StAXEventOutputter are
 * able to extend this class and modify any functionality they want. Before
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
 * {@link #printElement(XMLEventConsumer, FormatStack, NamespaceStack, XMLEventFactory, Element)} method.
 * The stacks are pushed and popped in that method only. They significantly
 * improve the performance and readability of the code.
 * <p>
 * The NamespaceStack is only sent through to the
 * {@link #printElement(XMLEventConsumer, FormatStack, NamespaceStack, XMLEventFactory, Element)} and
 * {@link #printContent(XMLEventConsumer, FormatStack, NamespaceStack, XMLEventFactory, List)} methods, but
 * the FormatStack is pushed through to all print* Methods.
 * <p>
 * <h2>Text Processing</h2>
 * <p>
 * In XML the concept of 'Text' can be loosely defined as anything that can be
 * found between an Element's start and end tags, excluding Comments and
 * Processing Instructions. When considered from a JDOM perspective, this means
 * {@link Text}, {@link CDATA} and {@link EntityRef} content. This will be
 * referred to as 'text-type content'
 * <p>
 * In the context of StAXEventOutputter, {@link EntityRef} content is considered to be
 * text-type content. This happens because when XML is parsed the embedded
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
 * fed to one of the {@link #printEntityRef(XMLEventConsumer, FormatStack, XMLEventFactory, EntityRef)},
 * {@link #printCDATA(XMLEventConsumer, FormatStack, XMLEventFactory, CDATA)}, or
 * {@link #printText(XMLEventConsumer, FormatStack, XMLEventFactory, Text)} methods. If there is
 * consecutive text-type content it is all fed together to the
 * {@link #printTextConsecutive(XMLEventConsumer, FormatStack, XMLEventFactory, List, int, int)} method.
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
 * @see StAXEventOutputter
 * @see StAXEventProcessor
 * @author Rolf Lear
 */
public abstract class AbstractStAXEventProcessor implements StAXEventProcessor {
	
	private static final class NSIterator implements Iterator<javax.xml.stream.events.Namespace> {
		private final Iterator<Namespace> source;
		private final XMLEventFactory fac;
		
		public NSIterator(Iterator<Namespace> source, XMLEventFactory fac) {
			super();
			this.source = source;
			this.fac = fac;
		}

		@Override
		public boolean hasNext() {
			return source.hasNext();
		}

		@Override
		public javax.xml.stream.events.Namespace next() {
			Namespace ns = source.next();
			return fac.createNamespace(ns.getPrefix(), ns.getURI());
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Cannot remove Namespaces");
			
		}
		
	}
	private static final class AttIterator implements Iterator<javax.xml.stream.events.Attribute> {
		private final Iterator<Attribute> source;
		private final XMLEventFactory fac;
		
		public AttIterator(Iterator<Attribute> source, XMLEventFactory fac) {
			super();
			this.source = source;
			this.fac = fac;
		}

		@Override
		public boolean hasNext() {
			return source.hasNext();
		}

		@Override
		public javax.xml.stream.events.Attribute next() {
			Attribute att = source.next();
			final Namespace ns = att.getNamespace();
			if (ns == Namespace.NO_NAMESPACE) {
				return fac.createAttribute(att.getName(), att.getValue());
			}
			return fac.createAttribute(ns.getPrefix(), ns.getURI(), 
					att.getName(), att.getValue());
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Cannot remove attributes");
			
		}
		
	}

	

	/* *******************************************
	 * StAXEventProcessor implementation.
	 * *******************************************
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.StAXEventProcessor#process(java.io.XMLEventConsumer,
	 * org.jdom2.Document, org.jdom2.output.Format)
	 */
	@Override
	public void process(final XMLEventConsumer out, final Format format,
			final XMLEventFactory eventfactory, final Document doc) throws XMLStreamException {
		printDocument(out, new FormatStack(format), new NamespaceStack(), eventfactory, doc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.StAXEventProcessor#process(java.io.XMLEventConsumer,
	 * org.jdom2.DocType, org.jdom2.output.Format)
	 */
	@Override
	public void process(final XMLEventConsumer out, final Format format,
			final XMLEventFactory eventfactory, final DocType doctype) throws XMLStreamException {
		printDocType(out, new FormatStack(format), eventfactory, doctype);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.StAXEventProcessor#process(java.io.XMLEventConsumer,
	 * org.jdom2.Element, org.jdom2.output.Format)
	 */
	@Override
	public void process(final XMLEventConsumer out, final Format format,
			final XMLEventFactory eventfactory, final Element element) throws XMLStreamException {
		// If this is the root element we could pre-initialize the
		// namespace stack with the namespaces
		printElement(out, new FormatStack(format), new NamespaceStack(),
				eventfactory, element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.StAXEventProcessor#process(java.io.XMLEventConsumer,
	 * java.util.List, org.jdom2.output.Format)
	 */
	@Override
	public void process(final XMLEventConsumer out, final Format format,
			final XMLEventFactory eventfactory, final List<? extends Content> list)
			throws XMLStreamException {
		printContent(out, new FormatStack(format), new NamespaceStack(), eventfactory, list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.StAXEventProcessor#process(java.io.XMLEventConsumer,
	 * org.jdom2.CDATA, org.jdom2.output.Format)
	 */
	@Override
	public void process(final XMLEventConsumer out, final Format format,
			final XMLEventFactory eventfactory, final CDATA cdata) throws XMLStreamException {
		printCDATA(out, new FormatStack(format), eventfactory, cdata);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.StAXEventProcessor#process(java.io.XMLEventConsumer,
	 * org.jdom2.Text, org.jdom2.output.Format)
	 */
	@Override
	public void process(final XMLEventConsumer out, final Format format,
			final XMLEventFactory eventfactory, final Text text) throws XMLStreamException {
		printText(out, new FormatStack(format), eventfactory, text);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.StAXEventProcessor#process(java.io.XMLEventConsumer,
	 * org.jdom2.Comment, org.jdom2.output.Format)
	 */
	@Override
	public void process(final XMLEventConsumer out, final Format format,
			final XMLEventFactory eventfactory, final Comment comment) throws XMLStreamException {
		printComment(out, new FormatStack(format), eventfactory, comment);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.StAXEventProcessor#process(java.io.XMLEventConsumer,
	 * org.jdom2.ProcessingInstruction, org.jdom2.output.Format)
	 */
	@Override
	public void process(final XMLEventConsumer out, final Format format,
			final XMLEventFactory eventfactory, final ProcessingInstruction pi) throws XMLStreamException {
		FormatStack fstack = new FormatStack(format);
		// Output PI verbatim, disregarding TrAX escaping PIs.
		fstack.setIgnoreTrAXEscapingPIs(true);
		printProcessingInstruction(out, fstack, eventfactory, pi);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdom2.output.StAXEventProcessor#process(java.io.XMLEventConsumer,
	 * org.jdom2.EntityRef, org.jdom2.output.Format)
	 */
	@Override
	public void process(final XMLEventConsumer out, final Format format,
			final XMLEventFactory eventfactory, final EntityRef entity) throws XMLStreamException {
		printEntityRef(out, new FormatStack(format), eventfactory, entity);
	}

	/*
	 * ========================================================================
	 * Support methods for Text-content formatting. Should all be protected. The
	 * following are used when printing Text-based data. Because of complicated
	 * multi-sequential text sometimes the requirements are odd. All Text
	 * content will be output using these methods, which is why there is the Raw
	 * version.
	 * ========================================================================
	 */

	/**
	 * Write a CDATA's value to the destination. The value will first be
	 * compacted (all leading and trailing whitespace will be removed and all
	 * internal whitespace will be be represented with a single ' ' character).
	 * If the CDATA contains only whitespace then nothing is output.
	 * <p>
	 * The results (if any) will be wrapped in the <code>&lt;![CDATA[</code> and
	 * <code>]]&gt;</code> delimiters.
	 * 
	 * @param str
	 *        the CDATA's value.
	 * @return The input String in a compact form
	 */
	protected String textCompact(final String str) {
		final char[] chars = str.toCharArray();
		int right = chars.length - 1;
		while (right >= 0 && Verifier.isXMLWhitespace(chars[right])) {
			right--;
		}
		if (right < 0) {
			return "";
		}

		int left = 0;
		while (Verifier.isXMLWhitespace(chars[left])) {
			// we do not need to check left < right because
			// we know we will find some non-white char first.
			left++;
		}

		int from = left;
		boolean wspace = false;
		left++;
		int ip = left;
		while (left <= right) {
			if (Verifier.isXMLWhitespace(chars[left])) {
				if (!wspace) {
					chars[ip++] = ' ';
				}
				wspace = true;
			} else {
				chars[ip++] = chars[left];
				wspace = false;
			}
			left++;
		}

		return String.valueOf(chars, from, ip - from);
	}

	/**
	 * Write a CDATA's value to the destination. The value will first be
	 * inspected. If the CDATA contains only whitespace then nothing is output,
	 * otherwise the entire input <code>str</code> will be wrapped in the
	 * <code>&lt;![CDATA[</code> and <code>]]&gt;</code> delimiters.
	 * 
	 * @param str
	 *        the CDATA's value.
	 * @return the input str if there's at least one non-whitespace char in it.
	 *         otherwise returns ""
	 */
	protected String textTrimFullWhite(final String str) {
		int right = str.length();
		while (--right >= 0 && Verifier.isXMLWhitespace(str.charAt(right))) {
			// decrement is in the condition.
		}
		if (right < 0) {
			return "";
		}

		return str;
	}

	/**
	 * Write a CDATA's value to the destination. The value will first be trimmed
	 * (all leading and trailing whitespace will be removed). If the CDATA
	 * contains only whitespace then nothing is output. The results (if any)
	 * will be wrapped in the <code>&lt;![CDATA[</code> and <code>]]&gt;</code>
	 * delimiters.
	 * 
	 * @param str
	 *        the CDATA's value.
	 * @return the input str value trimmed left and right.
	 */
	protected String textTrimBoth(final String str) {
		int right = str.length() - 1;
		while (right >= 0 && Verifier.isXMLWhitespace(str.charAt(right))) {
			right--;
		}
		if (right < 0) {
			return "";
		}

		int left = 0;
		while (Verifier.isXMLWhitespace(str.charAt(left))) {
			// we do not need to check left < right because
			// we know we will find some non-white char first.
			left++;
		}
		return str.substring(left, right + 1);
	}

	/**
	 * Write a CDATA's value to the destination. The value will first be
	 * right-trimmed (all trailing whitespace will be removed). If the CDATA
	 * contains only whitespace then nothing is output. The results (if any)
	 * will be wrapped in the <code>&lt;![CDATA[</code> and <code>]]&gt;</code>
	 * delimiters.
	 * 
	 * @param str
	 *        the CDATA's value.
	 * @return the input str trimmed on the right.
	 */
	protected String textTrimRight(final String str) {
		int right = str.length() - 1;
		while (right >= 0 && Verifier.isXMLWhitespace(str.charAt(right))) {
			right--;
		}
		if (right < 0) {
			return "";
		}

		return str.substring(0, right + 1);
	}

	/**
	 * Write a CDATA's value to the destination. The value will first be
	 * left-trimmed (all leading whitespace will be removed). If the CDATA
	 * contains only whitespace then nothing is output. The results (if any)
	 * will be wrapped in the <code>&lt;![CDATA[</code> and <code>]]&gt;</code>
	 * delimiters.
	 * 
	 * @param str
	 *        the CDATA's value.
	 * @return the input str trimmed on the left.
	 */
	protected String textTrimLeft(final String str) {
		final int right = str.length();
		int left = 0;
		while (left < right && Verifier.isXMLWhitespace(str.charAt(left))) {
			left++;
		}
		if (left >= right) {
			return "";
		}

		return str.substring(left, right);
	}

	/* *******************************************
	 * Support methods for output. Should all be protected. All content-type
	 * print methods have a FormatStack. Only printContent is responsible for
	 * outputting appropriate indenting and newlines, which are easily available
	 * using the FormatStack.getLevelIndent() and FormatStack.getLevelEOL().
	 * *******************************************
	 */

	/**
	 * This will handle printing of a {@link Document}.
	 * 
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param nstack
	 *        the NamespaceStack
	 * @param eventfactory
	 * 		  The XMLEventFactory for creating XMLEvents 
	 * @param doc
	 *        <code>Document</code> to write.
	 * @throws XMLStreamException
	 *         if the destination XMLEventConsumer fails
	 */
	protected void printDocument(final XMLEventConsumer out, final FormatStack fstack,
			final NamespaceStack nstack, final XMLEventFactory eventfactory, final Document doc) throws XMLStreamException {

		if (fstack.isOmitDeclaration()) {
			// this actually writes the declaration as version 1, UTF-8
			out.add(eventfactory.createStartDocument(null, null));
		} else if (fstack.isOmitEncoding()) {
			out.add(eventfactory.createStartDocument(null, "1.0"));
			if (fstack.getLineSeparator() != null) {
				out.add(eventfactory.createCharacters(fstack.getLineSeparator()));
			}
		} else {
			out.add(eventfactory.createStartDocument(fstack.getEncoding(), "1.0"));
			if (fstack.getLineSeparator() != null) {
				out.add(eventfactory.createCharacters(fstack.getLineSeparator()));
			}
		}
		
		if (doc.hasRootElement()) {
			printContent(out, fstack, nstack, eventfactory, doc.getContent());
		} else {
			final int sz = doc.getContentSize();
			for (int i = 0; i < sz; i++) {
				helperContentDispatcher(out, fstack, nstack, eventfactory, doc.getContent(i));
			}
		}
		
		// Output final line separator
		// We output this no matter what the newline flags say
		if (fstack.getIndent() == null && fstack.getLineSeparator() != null) {
			out.add(eventfactory.createCharacters(fstack.getLineSeparator()));
		}
		out.add(eventfactory.createEndDocument());
		
	}

	/**
	 * This will handle printing of a {@link DocType}.
	 * 
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param eventfactory
	 * 		  The XMLEventFactory for creating XMLEvents 
	 * @param docType
	 *        <code>DocType</code> to write.
	 * @throws XMLStreamException
	 *         if the destination XMLEventConsumer fails
	 */
	protected void printDocType(final XMLEventConsumer out, final FormatStack fstack,
			final XMLEventFactory eventfactory, final DocType docType) throws XMLStreamException {

		final String publicID = docType.getPublicID();
		final String systemID = docType.getSystemID();
		final String internalSubset = docType.getInternalSubset();
		boolean hasPublic = false;

		// Declaration is never indented.
		// write(out, fstack.getLevelIndent());
		
		StringWriter sw = new StringWriter();

		sw.write("<!DOCTYPE ");
		sw.write(docType.getElementName());
		if (publicID != null) {
			sw.write(" PUBLIC \"");
			sw.write(publicID);
			sw.write("\"");
			hasPublic = true;
		}
		if (systemID != null) {
			if (!hasPublic) {
				sw.write(" SYSTEM");
			}
			sw.write(" \"");
			sw.write(systemID);
			sw.write("\"");
		}
		if ((internalSubset != null) && (!internalSubset.equals(""))) {
			sw.write(" [");
			sw.write(fstack.getLineSeparator());
			sw.write(docType.getInternalSubset());
			sw.write("]");
		}
		sw.write(">");
		
		// DocType does not write it's own EOL
		// for compatibility reasons. Only
		// when output from inside a Content set.
		// write(out, fstack.getLineSeparator());
		out.add(eventfactory.createDTD(sw.toString()));
	}

	/**
	 * This will handle printing of a {@link ProcessingInstruction}.
	 * 
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param eventfactory
	 * 		  The XMLEventFactory for creating XMLEvents 
	 * @param pi
	 *        <code>ProcessingInstruction</code> to write.
	 * @throws XMLStreamException
	 *         if the destination XMLEventConsumer fails
	 */
	protected void printProcessingInstruction(final XMLEventConsumer out,
			final FormatStack fstack, final XMLEventFactory eventfactory, final ProcessingInstruction pi)
			throws XMLStreamException {
		String target = pi.getTarget();
		String rawData = pi.getData();
		if (rawData != null && rawData.trim().length() > 0) {
			out.add(eventfactory.createProcessingInstruction(target, rawData));
		} else {
			out.add(eventfactory.createProcessingInstruction(target, ""));
		}
	}

	/**
	 * This will handle printing of a {@link Comment}.
	 * 
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param eventfactory
	 * 		  The XMLEventFactory for creating XMLEvents 
	 * @param comment
	 *        <code>Comment</code> to write.
	 * @throws XMLStreamException
	 *         if the destination XMLEventConsumer fails
	 */
	protected void printComment(final XMLEventConsumer out, final FormatStack fstack,
			final XMLEventFactory eventfactory, final Comment comment) throws XMLStreamException {
		out.add(eventfactory.createComment(comment.getText()));
	}

	/**
	 * This will handle printing of an {@link EntityRef}.
	 * 
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param eventfactory
	 * 		  The XMLEventFactory for creating XMLEvents 
	 * @param entity
	 *        <code>EntotyRef</code> to write.
	 * @throws XMLStreamException
	 *         if the destination XMLEventConsumer fails
	 */
	protected void printEntityRef(final XMLEventConsumer out, final FormatStack fstack,
			final XMLEventFactory eventfactory, final EntityRef entity) throws XMLStreamException {
		out.add(eventfactory.createEntityReference(entity.getName(), null));
	}

	/**
	 * This will handle printing of a {@link CDATA}.
	 * 
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param eventfactory
	 * 		  The XMLEventFactory for creating XMLEvents 
	 * @param cdata
	 *        <code>CDATA</code> to write.
	 * @throws XMLStreamException
	 *         if the destination XMLEventConsumer fails
	 */
	protected void printCDATA(final XMLEventConsumer out, final FormatStack fstack,
			final XMLEventFactory eventfactory, final CDATA cdata) throws XMLStreamException {
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
			out.add(eventfactory.createCData(text));
		}
	}

	/**
	 * This will handle printing of a {@link Text}.
	 * 
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param eventfactory
	 * 		  The XMLEventFactory for creating XMLEvents 
	 * @param text
	 *        <code>Text</code> to write.
	 * @throws XMLStreamException
	 *         if the destination XMLEventConsumer fails
	 */
	protected void printText(final XMLEventConsumer out, final FormatStack fstack,
			final XMLEventFactory eventfactory, final Text text) throws XMLStreamException {
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
		out.add(eventfactory.createCharacters(str));
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
	 * to {@link #printTextConsecutive(XMLEventConsumer, FormatStack, XMLEventFactory, List, int, int)}
	 * <li>There is content other than just text-type content, then delegate to
	 * {@link #printContent(XMLEventConsumer, FormatStack, NamespaceStack, XMLEventFactory, List)}
	 * </ol>
	 * <p>
	 * 
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param nstack
	 *        the NamespaceStack
	 * @param eventfactory
	 * 		  The XMLEventFactory for creating XMLEvents 
	 * @param element
	 *        <code>Element</code> to write.
	 * @throws XMLStreamException
	 *         if the destination XMLEventConsumer fails
	 */
	protected void printElement(final XMLEventConsumer out, final FormatStack fstack,
			final NamespaceStack nstack, final XMLEventFactory eventfactory, 
			final Element element) throws XMLStreamException {

		nstack.push(element);
		try {
			
			TextMode textmode = fstack.getTextMode();
			
			// Check for xml:space and adjust format settings
			final String space = element.getAttributeValue("space",
					Namespace.XML_NAMESPACE);

			if ("default".equals(space)) {
				textmode = fstack.getDefaultMode();
			}
			else if ("preserve".equals(space)) {
				textmode = TextMode.PRESERVE;
			}

			final List<Content> content = element.getContent();
			
			// Three conditions that determine the required output.
			// do we have an expanded element( <emt></emt> or an single <emt />
			// if there is any printable content, or if expandempty is set
			// then we must expand.
			boolean expandit = fstack.isExpandEmptyElements();
			
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
							expandit = true;
							whiteonly = false;
						} else {
							// look for non-whitespace.
							String val = k.getValue();
							int cc = val.length();
							while (--cc >= 0) {
								if (!Verifier.isXMLWhitespace(val.charAt(cc))) {
									// found non-whitespace;
									expandit = true;
									whiteonly = false;
									break;
								}
							}
						}
					}
				} else {
					expandit = true;
					whiteonly = false;
					if (!(k instanceof EntityRef)) {
						textonly = false;
						// we know everything we need.
						break;
					}
				}
				if (expandit && !whiteonly && !textonly) {
					break;
				}
			}

			
			Namespace ns = element.getNamespace();
			if (ns == Namespace.NO_NAMESPACE) {
				out.add(eventfactory.createStartElement("", "", element.getName(), 
						new AttIterator(element.getAttributes().iterator(), eventfactory), 
						new NSIterator(nstack.addedForward().iterator(), eventfactory)));
			} else if ("".equals(ns.getPrefix())) {
				out.add(eventfactory.createStartElement("", ns.getURI(), element.getName(), 
						new AttIterator(element.getAttributes().iterator(), eventfactory), 
						new NSIterator(nstack.addedForward().iterator(), eventfactory)));
			} else {
				out.add(eventfactory.createStartElement(ns.getPrefix(), ns.getURI(), element.getName(), 
						new AttIterator(element.getAttributes().iterator(), eventfactory), 
						new NSIterator(nstack.addedForward().iterator(), eventfactory)));
			}
			
			if (expandit) {
				
				// OK, now we print out the meat of the Element
				if (!content.isEmpty()) {
					if (textonly) {
						if (!whiteonly) {
							fstack.push();
							try {
								fstack.setTextMode(textmode);
								helperTextType(out, fstack, eventfactory, content, 0, content.size());
							} finally {
								fstack.pop();
							}
						}
					} else {
						fstack.push();
						try {
							fstack.setTextMode(textmode);
							
							if (fstack.getLevelIndent() != null && fstack.getLineSeparator() != null) {
								out.add(eventfactory.createCharacters(fstack.getLineSeparator()));
							}
							printContent(out, fstack, nstack, eventfactory, content);
							
						} finally {
							fstack.pop();
						}
						if (fstack.getLevelIndent() != null  && fstack.getLineSeparator() != null) {
							out.add(eventfactory.createCharacters(fstack.getLevelIndent()));
						}
						
					}
				}
			
			}
		
			
			out.add(eventfactory.createEndElement(element.getNamespacePrefix(), 
					element.getNamespaceURI(), element.getName(), 
					new NSIterator(nstack.addedReverse().iterator(), eventfactory)));
		

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
	 * {@link #printTextConsecutive(XMLEventConsumer, FormatStack, XMLEventFactory, List, int, int)},
	 * {@link #printCDATA(XMLEventConsumer, FormatStack, XMLEventFactory, CDATA)}, or
	 * {@link #printComment(XMLEventConsumer, FormatStack, XMLEventFactory, Comment)},
	 * <li>do a newline if one is specified.
	 * <li>loop back to 1. until there's no more content to process.
	 * </ol>
	 * 
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param nstack
	 *        the NamespaceStack
	 * @param eventfactory
	 * 		  The XMLEventFactory for creating XMLEvents 
	 * @param content
	 *        <code>List</code> of <code>Content</code> to write.
	 * @throws XMLStreamException
	 *         if the destination XMLEventConsumer fails
	 */
	protected void printContent(final XMLEventConsumer out,
			final FormatStack fstack, final NamespaceStack nstack,
			final XMLEventFactory eventfactory, final List<? extends Content> content)
			throws XMLStreamException {

		// Basic theory of operation is as follows:
		// 0. raw-print for PRESERVE.
		// 1. print any non-Text content
		// 2. Accumulate all sequential text content and print them together
		// Do not do any newlines before or after content.
		if (fstack.getTextMode() == TextMode.PRESERVE) {
			for (Content c : content) {
				helperContentDispatcher(out, fstack, nstack, eventfactory, c);
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
							if (fstack.getLevelIndent() != null) {
								out.add(eventfactory.createCharacters(fstack.getLevelIndent()));
							}
							helperTextType(out, fstack, eventfactory, content, txti, index - txti);
							if (fstack.getLevelIndent() != null && fstack.getLineSeparator() != null) {
								out.add(eventfactory.createCharacters(fstack.getLineSeparator()));
							}
						}
					}
					txti = -1;

					if (fstack.getLevelIndent() != null) {
						out.add(eventfactory.createCharacters(fstack.getLevelIndent()));
					}
					helperContentDispatcher(out, fstack, nstack, eventfactory, c);
					if (fstack.getLevelIndent() != null && fstack.getLineSeparator() != null) {
						out.add(eventfactory.createCharacters(fstack.getLineSeparator()));
					}
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
				if (fstack.getLevelIndent() != null) {
					out.add(eventfactory.createCharacters(fstack.getLevelIndent()));
				}
				helperTextType(out, fstack, eventfactory, content, txti, index - txti);
				if (fstack.getLevelIndent() != null && fstack.getLineSeparator() != null) {
					out.add(eventfactory.createCharacters(fstack.getLineSeparator()));
				}
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
	 * specified text-type values are all white-space only, then you may
	 * get odd looking XML (empty lines). 
	 * 
	 * @param out
	 *        <code>XMLEventConsumer</code> to use.
	 * @param fstack
	 *        the FormatStack
	 * @param eventfactory
	 * 		  The XMLEventFactory for creating XMLEvents 
	 * @param content
	 *        <code>List</code> of <code>Content</code> to write.
	 * @param offset
	 *        index of first content node (inclusive).
	 * @param len
	 *        number of nodes that are text-type.
	 * @throws XMLStreamException
	 *         if the destination XMLEventConsumer fails
	 */
	protected void printTextConsecutive(final XMLEventConsumer out, FormatStack fstack,
			final XMLEventFactory eventfactory, final List<? extends Content> content,
			final int offset, final int len) throws XMLStreamException {

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
			for (ListIterator<? extends Content> li =
					content.listIterator(offset); cnt > 0; cnt--) {
				helperRawTextType(out, fstack, eventfactory, li.next());
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
					if (!first && !dospace &&
							Verifier.isXMLWhitespace(val.charAt(0))) {
						// we are between content, and need to put in a space.
						dospace = true;
					}
					endspace = Verifier.isXMLWhitespace(val.charAt(val.length() - 1));
				}
				first = false;
				if (dospace) {
					out.add(eventfactory.createCharacters(" "));
				}

				switch (c.getCType()) {
					case Text:
						out.add(eventfactory.createCharacters(textCompact(((Text) c).getText())));
						break;
					case CDATA:
						out.add(eventfactory.createCData(textCompact(((Text) c).getText())));
						break;
					case EntityRef:
						printEntityRef(out, fstack, eventfactory, (EntityRef) c);
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
				if (!(node instanceof Text) || !isAllWhitespace(node.getValue())) {
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
				if (!(node instanceof Text) || !isAllWhitespace(node.getValue())) {
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
						out.add(eventfactory.createCharacters(textTrimBoth(((Text) node).getText())));
						break;
					case CDATA:
						out.add(eventfactory.createCData(textTrimBoth(((CDATA) node).getText())));
						break;
					case EntityRef:
						printEntityRef(out, fstack, eventfactory, (EntityRef) node);
					default:
						// do nothing
				}
				return;
			}
			final Content leftc = content.get(left);
			switch (leftc.getCType()) {
				case Text:
					out.add(eventfactory.createCharacters(textTrimLeft(((Text) leftc).getText())));
					break;
				case CDATA:
					out.add(eventfactory.createCData(textTrimLeft(((CDATA) leftc).getText())));
					break;
				case EntityRef:
					printEntityRef(out, fstack, eventfactory, (EntityRef) leftc);
				default:
					// do nothing
			}

			int cnt = right - left - 1;
			for (ListIterator<? extends Content> li =
					content.listIterator(left + 1); cnt > 0; cnt--) {
				helperRawTextType(out, fstack, eventfactory, li.next());
			}

			final Content rightc = content.get(right);
			switch (rightc.getCType()) {
				case Text:
					out.add(eventfactory.createCharacters(textTrimRight(((Text) rightc).getText())));
					break;
				case CDATA:
					out.add(eventfactory.createCData(textTrimRight(((CDATA) rightc).getText())));
					break;
				case EntityRef:
					printEntityRef(out, fstack, eventfactory, (EntityRef) rightc);
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
					out.add(eventfactory.createCharacters(((Text) rightc).getText()));
					break;
				case CDATA:
					out.add(eventfactory.createCData(((CDATA) rightc).getText()));
					break;
				case EntityRef:
					printEntityRef(out, fstack, eventfactory, (EntityRef) rightc);
				default:
					// do nothing
			}
		}
		//assert TextMode.PRESERVE != mode : "PRESERVE text-types are not Consecutive";
	}

	/**
	 * This method contains code which is reused in a number of places. It
	 * simply determines what content is passed in, and dispatches it to the
	 * correct print* method.
	 * 
	 * @param out
	 *        The destination to write to.
	 * @param fstack
	 *        The current FormatStack
	 * @param nstack
	 *        the NamespaceStack
	 * @param eventfactory
	 * 		  The XMLEventFactory for creating XMLEvents 
	 * @param content
	 *        The content to dispatch
	 * @throws XMLStreamException
	 *         if the output fails
	 */
	protected void helperContentDispatcher(final XMLEventConsumer out,
			final FormatStack fstack, final NamespaceStack nstack,
			final XMLEventFactory eventfactory, final Content content) throws XMLStreamException {
		switch (content.getCType()) {
			case CDATA:
				printCDATA(out, fstack, eventfactory, (CDATA) content);
				break;
			case Comment:
				printComment(out, fstack, eventfactory, (Comment) content);
				break;
			case Element:
				printElement(out, fstack, nstack, eventfactory, (Element) content);
				break;
			case EntityRef:
				printEntityRef(out, fstack, eventfactory, (EntityRef) content);
				break;
			case ProcessingInstruction:
				printProcessingInstruction(out, fstack, eventfactory,
						(ProcessingInstruction) content);
				break;
			case Text:
				printText(out, fstack, eventfactory, (Text) content);
				break;
			case DocType:
				printDocType(out, fstack, eventfactory, (DocType) content);
				if (fstack.getLineSeparator() != null) {
					out.add(eventfactory.createCharacters(fstack.getLineSeparator()));
				}
				break;
			default:
				throw new IllegalStateException(
						"Unexpected Content " + content.getCType());
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
	 * specified text-type values are all white-space only, then you may
	 * get odd looking XML (empty lines). 
	 * <p>
	 * Odd things can happen too if the len is <= 0;
	 * 
	 * @param out
	 *        where to write
	 * @param fstack
	 *        the current FormatStack
	 * @param eventfactory
	 * 		  The XMLEventFactory for creating XMLEvents 
	 * @param content
	 *        The list of Content to process
	 * @param offset
	 *        The offset of the first text-type content in the sequence
	 * @param len
	 *        the number of text-type content in the sequence
	 * @throws XMLStreamException
	 *         if the output fails.
	 */
	protected final void helperTextType(final XMLEventConsumer out,
			final FormatStack fstack, final XMLEventFactory eventfactory, 
			final List<? extends Content> content,
			final int offset, final int len) throws XMLStreamException {

		assert len > 0 : "All calls to this should have *some* content.";

		if (len == 1) {
			final Content node = content.get(offset);
			// Print the node
			switch (node.getCType()) {
				case Text:
					printText(out, fstack, eventfactory, (Text) node);
					break;
				case CDATA:
					printCDATA(out, fstack, eventfactory, (CDATA) node);
					break;
				case EntityRef:
					printEntityRef(out, fstack, eventfactory, (EntityRef) node);
				default:
					// do nothing
			}
		} else {
			// we have text content we need to print.
			// we also know that we are 'mixed' content
			// so the text content should be indented.
			// Additionally
			printTextConsecutive(out, fstack, eventfactory, content, offset, len);
		}
	}

	/**
	 * This method contains code which is reused in a number of places. It
	 * simply determines which of the text-type content is passed in, and
	 * dispatches it to the correct text method (textEscapeRaw, textCDATARaw, or
	 * textEntityRef).
	 * 
	 * @param out
	 *        The destination to write to.
	 * @param fstack
	 *        The current FormatStack
	 * @param eventfactory
	 * 		  The XMLEventFactory for creating XMLEvents 
	 * @param content
	 *        The content to dispatch
	 * @throws XMLStreamException
	 *         if the output fails
	 */
	protected final void helperRawTextType(final XMLEventConsumer out,
			final FormatStack fstack, final XMLEventFactory eventfactory, 
			final Content content) throws XMLStreamException {
		switch (content.getCType()) {
			case Text:
				out.add(eventfactory.createCharacters(((Text) content).getText()));
				break;
			case CDATA:
				out.add(eventfactory.createCData(((CDATA) content).getText()));
				break;
			case EntityRef:
				printEntityRef(out, fstack, eventfactory, (EntityRef) content);
			default:
				// do nothing
		}
	}

	/**
	 * Inspect the specified content range of text-type for any that have actual
	 * text.
	 * @param content a <code>List</code> containing Content.
	 * @param offset the start offset to check
	 * @param len how much content to check
	 * @return true if there's no actual text in the specified content range.
	 */
	protected boolean isAllWhiteSpace(final List<? extends Content> content, 
			final int offset, final int len) {
		for (int i = offset + len - 1; i >= offset; i--) {
			// do the harder check for non-whitespace.
			final Content c = content.get(i);
			switch (c.getCType()) {
				case CDATA:
				case Text:
					// both return the text as getValue()
					for (char ch : c.getValue().toCharArray()) {
						if (!Verifier.isXMLWhitespace(ch)) {
							return false;
						}
					}
					break;
				case EntityRef:
					return false;
				default:
					throw new IllegalStateException(
							"isWhiteSpace was given a " + c.getCType() + 
							" to check, which is illegal");
			}
		}
		return true;
	}

	/**
	 * Inspect the input String for whitespace characters.
	 * 
	 * @param value
	 *        The value to inspect
	 * @return true if all characters in the input value are all whitespace
	 */
	protected boolean isAllWhitespace(final String value) {
		for (char ch : value.toCharArray()) {
			if (!Verifier.isXMLWhitespace(ch)) {
				return false;
			}
		}
		return true;
	}

}
