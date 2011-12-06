package org.jdom2.output;

import org.jdom2.JDOMConstants;

/**
 * An enumeration of common separators that are used for JDOM output.
 * <p>
 * These enumerated values can be used as input to the
 * {@link Format#setLineSeparator(LineSeparator)} method. Additionally, the
 * names of these constants can be also be used in the System Property
 * {@link JDOMConstants#JDOM2_PROPERTY_LINE_SEPARATOR} which is used to
 * define the default Line Separator sequence for JDOM output. See
 * {@link #DEFAULT} Javadoc.
 * 
 * <p>
 * JDOM has historically used the CR/NL sequence '\r\n' as a line-terminator.
 * This sequence has the advantage that the output is easily opened in the 
 * 'Notepad' editor on Windows. Other editors on other platforms are typically
 * smart enough to automatically adjust to whatever termination sequence is
 * used in the document. The XML specification requires that the CR/NL sequence
 * should be 'normalized' to a single newline '\n' when the document is parsed
 * (<a href="http://www.w3.org/TR/xml11/#sec-line-ends">XML 1.1 End-Of-Line
 * Handling</a>). As a result there is no XML issue with the JDOM default CR/NL
 * end-of-line sequence.
 * <p>
 * JDOM has always allowed the line-terminating sequence to be customised (or
 * even disabled) for each {@link XMLOutputter} operation by using this Format
 * class.
 * <p>
 * JDOM2 introduces two new features in relation to the end-of-line sequence.
 * Firstly, it introduces this new {@link LineSeparator} enumeration which
 * formalises the common line separators that can be used. In addition to the
 * simple String-based {@link Format#setLineSeparator(String)} method you can
 * now also call {@link Format#setLineSeparator(LineSeparator)} with one of the
 * common enumerations.
 * <p>
 * The second new JDOM2 feature is the ability to set a global default
 * end-of-line sequence. JDOM 1.x forced the default sequence to be the CRLF
 * sequence, but JDOM2 allows you to set the system property
 * {@link JDOMConstants#JDOM2_PROPERTY_LINE_SEPARATOR} which will be used as the
 * default sequence for Format. You can set the property to be the name of one
 * of these LineSeparator enumerations too. For example, the following will
 * cause all default Format instances to use the System-dependent end-of-line
 * sequence instead of always CRLF:
 * <p>
 * <pre>
 * java -D org.jdom2.output.LineSeparator=SYSTEM ...
 * </pre>
 * 
 * @author Rolf Lear
 *
 */
public enum LineSeparator {
	/**
	 * The Separator sequence CRNL which is '\r\n'. 
	 * This is the default sequence.
	 */
	CRNL("\r\n"),

	/**
	 * The Separator sequence NL which is '\n'. 
	 */
	NL("\n"),
	/**
	 * The Separator sequence RC which is '\r'. 
	 */
	CR("\r"),
	
	/** The 'DOS' Separator sequence CRLF (CRNL) which is '\r\n'. */
	DOS("\r\n"),
	
	/** The 'UNIX' Separator sequence NL which is '\n'. */
	UNIX("\n"),
	
	
	/**
	 * The system-dependent Separator sequence NL which is obtained from
	 * <code>System.getProperty("line.separator")</code>. This should be
	 * the equivalent of {@link #DOS} on windows platforms, and
	 * of {@link #UNIX} on UNIX and Apple systems (after Mac OSX).
	 */
	SYSTEM(System.getProperty("line.separator")),
	
	/** Perform no end-of-line processing. */
	NONE(null),
	
	/**
	 * Use the sequence '\r\n' unless the System property
	 * {@link JDOMConstants#JDOM2_PROPERTY_LINE_SEPARATOR} is defined, at which
	 * point use the value specified in that property. If the value in that
	 * property matches one of the Enumeration names (e.g. SYSTEM) then use the
	 * sequence specified in that enumeration. 
	 */
	// DEFAULT must be declared last so that you can specify enum names
	// in the system property.
	DEFAULT(getDefaultLineSeparator());
	
	
	
	private static String getDefaultLineSeparator() {
		String def = LineSeparator.DOS.value();
		String prop = System.getProperty(JDOMConstants.JDOM2_PROPERTY_LINE_SEPARATOR, def);
		if ("DEFAULT".equals(prop)) {
			// need to do this to catch an unlikelye instance when someone sets
			// -Dorg.jdom2.output.LineSeparator=DEFAULT
			// which would create some sort of loop to happen....
			prop = "\r\n";
		} else {
			try {
				LineSeparator sep = Enum.valueOf(LineSeparator.class, prop);
				prop = sep.value();
			} catch (Exception e) {
				// ignore it, just use the prop.
			}
		}
		return prop;
	}

	
	
	private final String value;
	
	LineSeparator(String value) {
		this.value = value;
	}
	
	/**
	 * The String sequence used for this Separator
	 * @return an End-Of-Line String
	 */
	public String value() {
		return value;
	}
	
}