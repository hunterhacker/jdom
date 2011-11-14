package org.jdom2.output;

import java.util.Arrays;

import org.jdom2.output.Format.TextMode;

/**
 * FormatStack implements a mechanism where the formatting details can be
 * changed mid-tree, but then get reverted when that tree segment is
 * complete.
 * <p>
 * This class is intended as a working-class for in the various outputter
 * implementations. It is inly public so that people extending the
 * Abstract*Processor classes can take advantage of it's functionality.
 * <p>
 * The value this class adds is:
 * <ul>
 * <li>Fast - 
 * </ul>
 * 
 * @author Rolf Lear
 */
public final class FormatStack {

	private int capacity = 16; // can grow if more than 16 levels in XML
	private int depth = 0; // current level in XML

	/*
	 * ====================================================================
	 * The following values cannot be changed mid-way through the output
	 * ====================================================================
	 */

	private final TextMode defaultMode; // the base/initial Text mode

	/** The default indent is no spaces (as original document) */
	private final String indent;

	/** The encoding format */
	private final String encoding;

	/** New line separator */
	private final String lineSeparator;

	/**
	 * Whether or not to output the XML declaration - default is
	 * <code>false</code>
	 */
	private final boolean omitDeclaration;

	/**
	 * Whether or not to output the encoding in the XML declaration -
	 * default is <code>false</code>
	 */
	private final boolean omitEncoding;

	/**
	 * Whether or not to expand empty elements to
	 * &lt;tagName&gt;&lt;/tagName&gt; - default is <code>false</code>
	 */
	private final boolean expandEmptyElements;

	/** entity escape logic */
	private EscapeStrategy escapeStrategy;

	/*
	 * ====================================================================
	 * The following values can be changed mid-way through the output, hence
	 * they are arrays.
	 * ====================================================================
	 */

	/** The 'current' accumulated indent */
	private String[] levelIndent = new String[capacity];

	/** The 'current' End-Of-Line */
	private String[] levelEOL = new String[capacity];

	/**
	 * Whether TrAX output escaping disabling/enabling PIs are ignored or
	 * processed - default is <code>false</code>
	 */
	private boolean[] ignoreTrAXEscapingPIs = new boolean[capacity];

	/** text handling mode */
	private TextMode[] mode = new TextMode[capacity];

	/** escape Output logic - can be changed by */
	private boolean[] escapeOutput = new boolean[capacity];

	/**
	 * Creates a new FormatStack seeded with the specified Format
	 * 
	 * @param format
	 *        the Format instance to seed the stack with.
	 */
	public FormatStack(Format format) {
		indent = format.getIndent();
		lineSeparator = format.getLineSeparator();

		encoding = format.getEncoding();
		omitDeclaration = format.getOmitDeclaration();
		omitEncoding = format.getOmitEncoding();
		expandEmptyElements = format.getExpandEmptyElements();
		escapeStrategy = format.getEscapeStrategy();
		defaultMode = format.getTextMode();

		levelIndent[depth] = format.getIndent() == null
				? null : "";
		levelEOL[depth] = format.getIndent() == null
				? null : format.getLineSeparator();
		ignoreTrAXEscapingPIs[depth] = format.getIgnoreTrAXEscapingPIs();
		mode[depth] = format.getTextMode();
		escapeOutput[depth] = true;
	}

	/**
	 * @return the original {@link Format#getIndent()}, may be null
	 */
	public String getIndent() {
		return indent;
	}

	/**
	 * @return the original {@link Format#getLineSeparator()}
	 */
	public String getLineSeparator() {
		return lineSeparator;
	}

	/**
	 * @return the original {@link Format#getEncoding()}
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @return the original {@link Format#getOmitDeclaration()}
	 */
	public boolean isOmitDeclaration() {
		return omitDeclaration;
	}

	/**
	 * @return the original {@link Format#getOmitEncoding()}
	 */
	public boolean isOmitEncoding() {
		return omitEncoding;
	}

	/**
	 * @return the original {@link Format#getExpandEmptyElements()}
	 */
	public boolean isExpandEmptyElements() {
		return expandEmptyElements;
	}

	/**
	 * @return the original {@link Format#getEscapeStrategy()}
	 */
	public EscapeStrategy getEscapeStrategy() {
		return escapeStrategy;
	}

	/**
	 * @return the current depth's {@link Format#getIgnoreTrAXEscapingPIs()}
	 */
	public boolean isIgnoreTrAXEscapingPIs() {
		return ignoreTrAXEscapingPIs[depth];
	}

	/**
	 * Set the current depth's {@link Format#getIgnoreTrAXEscapingPIs()}
	 * 
	 * @param ignoreTrAXEscapingPIs
	 *        the boolean value to set.
	 */
	public void setIgnoreTrAXEscapingPIs(boolean ignoreTrAXEscapingPIs) {
		this.ignoreTrAXEscapingPIs[depth] = ignoreTrAXEscapingPIs;
	}

	/**
	 * The escapeOutput flag can be set or unset. When set, Element text and
	 * Attribute values are 'escaped' so that the output is valid XML. When
	 * unset, the Element text and Attribute values are not escaped.
	 * 
	 * @return the current depth's escapeOutput flag.
	 */
	public boolean getEscapeOutput() {
		return escapeOutput[depth];
	}

	/**
	 * The escapeOutput flag can be set or unset. When set, Element text and
	 * Attribute values are 'escaped' so that the output is valid XML. When
	 * unset, the Element text and Attribute values are not escaped.
	 * 
	 * @param escape
	 *        what to set the current level's escapeOutput flag to.
	 */
	public void setEscapeOutput(boolean escape) {
		escapeOutput[depth] = escape;
	}

	/**
	 * @return the TextMode that was originally set for this stack before
	 *         any modifications.
	 */
	public TextMode getDefaultMode() {
		return defaultMode;
	}

	/**
	 * @return the current depth's accumulated/maintained indent, may be null
	 */
	public String getLevelIndent() {
		return levelIndent[depth];
	}

	/**
	 * Override the current depth's accumulated line indent.
	 * 
	 * @param indent
	 *        the indent to set.
	 */
	public void setLevelIndent(String indent) {
		this.levelIndent[depth] = indent;
	}

	/**
	 * @return the current depth's End-Of-Line sequence, may be null
	 */
	public String getLevelEOL() {
		return levelEOL[depth];
	}

	/**
	 * Set the current depth's End-Of-Line sequence
	 * 
	 * @param newline
	 *        the new End-Of-Line sequence to set.
	 */
	public void setLevelEOL(String newline) {
		this.levelEOL[depth] = newline;
	}

	/**
	 * @return the current depth's {@link Format#getTextMode()}
	 */
	public TextMode getTextMode() {
		return mode[depth];
	}

	/**
	 * Change the current level's TextMode
	 * 
	 * @param mode
	 *        the new mode to set.
	 */
	public void setTextMode(TextMode mode) {
		if (this.mode[depth] == mode) {
			return;
		}
		this.mode[depth] = mode;
		switch (mode) {
			case PRESERVE:
				levelEOL[depth] = null;
				levelIndent[depth] = null;
				break;
			default:
				levelEOL[depth] = lineSeparator;
				if (indent != null) {
					if (depth > 0) {
						if (levelIndent[depth - 1] == null) {
							StringBuilder sb = new StringBuilder(indent.length() * depth);
							for (int i = 0; i < depth; i++) {
								sb.append(indent);
							}
							levelIndent[depth] = sb.toString();
						} else {
							levelIndent[depth] = levelIndent[depth - 1] + indent;
						}
					} else {
						levelIndent[depth] = "";
					}
				}
		}
	}

	/**
	 * Create a new depth level on the stack. The previous level's details
	 * are copied to this level, and the accumulated indent (if any) is
	 * indented further.
	 */
	public void push() {
		final int prev = depth++;
		if (depth >= capacity) {
			capacity *= 2;
			levelIndent = Arrays.copyOf(levelIndent, capacity);
			levelEOL = Arrays.copyOf(levelEOL, capacity);
			ignoreTrAXEscapingPIs = Arrays.copyOf(ignoreTrAXEscapingPIs, capacity);
			mode = Arrays.copyOf(mode, capacity);
			escapeOutput = Arrays.copyOf(escapeOutput, capacity);
		}
		levelIndent[depth] = indent == null
				? null
				: (levelIndent[prev] + indent);
		levelEOL[depth] = indent == null
				? null : lineSeparator;
		ignoreTrAXEscapingPIs[depth] = ignoreTrAXEscapingPIs[prev];
		mode[depth] = mode[prev];
		escapeOutput[depth] = escapeOutput[prev];
	}

	/**
	 * Move back a level on the stack.
	 */
	public void pop() {
		// no need to clear previously used members in the stack.
		// the stack is short-lived, and does not create new instances for
		// the depth levels, in other words, it does not affect GC and does
		// not save memory to clear the stack.
		depth--;
	}

}