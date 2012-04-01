/*--

 Copyright (C) 2011-2012 Jason Hunter & Brett McLaughlin.
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

package org.jdom2.input.stax;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom2.DocType;
import org.jdom2.JDOMException;
import org.jdom2.JDOMFactory;

/**
 * Parses out key information from a single String representing a DOCTYPE
 * declaration. StAX parsers supply a single string representing the DOCTYPE and
 * this needs to be processed to get items like the SystemID, etc. Additionally
 * it needs to be reformatted to create a standardised representation.
 * <p>
 * The assumption is that the DTD is valid.
 * <p>
 * We need to pull out 4 elements of data:
 * <ol>
 * <li>The root element name
 * <li>The SystemID (if available)
 * <li>The PublicID (if available)
 * <li>The internal subset (if available)
 * </ol>
 * 
 * The internal-subset should be re-formatted to conform to the JDOM 'standard'
 * where each declaration starts on a new line indented with 2 spaces. This
 * 'standard' is defined by the way that JDOM formats the DTD declarations in the 
 * SAX parse process, which fires individual events for the content in the DTD.
 * <p>
 * We can do this all with a well-structured regular expression, which is
 * actually simpler than trying to fish out all the components ourselves....
 * <p>
 * 
 * @author Rolf Lear
 *
 */
public class DTDParser {
	
	/* 
	 * =======================================================================
	 * 
	 *              READ THIS...
	 *      
	 * 
	 * This code works by using a reg-ex to parse a valid DTD document.
	 * The pattern is complicated (not as complicated as an actual parser).
	 * 
	 * Because the pattern is complicated, this code creates a pattern 'database'
	 * and then 'pulls' patterns from the database to create the final regex.
	 * The database patterns are pulled to transform a pattern template into a
	 * final regular expression. This template is called the 'meta-pattern'.
	 * 
	 * So, the pattern is not kept in its final form, but rather it is built
	 * up at class initialization time based on the meta-pattern, and the
	 * pattern database in the map.
	 * 
	 * This is the final pattern: (broken over a few lines)
	 * 
	 * [\s\r\n\t]*<!DOCTYPE[\s\r\n\t]+([^\s\r\n\t\[>]+)([\s\r\n\t]+
	 * ((SYSTEM[\s\r\n\t]+(('([^']*)')|("([^"]*)")))|
	 * (PUBLIC[\s\r\n\t]+(('([^']*)')|("([^"]*)"))([\s\r\n\t]+
	 * (('([^']*)')|("([^"]*)")))?)))?([\s\r\n\t]*\[(.*)\])?
	 * [\s\r\n\t]*>[\s\r\n\t]*
	 * 
	 * You will agree that it's simpler to build the pattern than to read it....
	 * 
	 * With the above in mind, you can easily follow the way the pattern is
	 * built as it is simply a repeating use of some of the base constructs.
	 * =======================================================================
	 */

	/**
	 * This is the meta-pattern.
	 * <p>
	 * <ul>
	 * <li>Where you see ' os ' there is optional space.
	 * <li>Where you see ' name ' there is the element name.
	 * <li>Where you see ' ms ' there is mandatory space.
	 * <li>Where you see ' id ' there is some quoted identifier.
	 * <li>Where you see ' internal ' there is the internal subset.
	 * </ul>
	 * Anything else will become part of the final regex.
	 * <p>
	 * Space ('&nbsp;') was chosen for the token delimiter because it
	 * makes the meta-pattern easy to read. There are a couple of places in
	 * this expression where there are two ' ' together, and it is critical
	 * that it does not change because there will be missed token matches then.
	 */
	private static final String metapattern =
			// The lead-in and the Element name
			" os <!DOCTYPE ms ( name )" + 
			// The Public/System references, if any
			"( ms ((SYSTEM ms  id )|(PUBLIC ms  id ( ms  id )?)))?" +
			// The Internal Subset, if any.
			"( os \\[( internal )\\])?" +
			// The lead-out.
			" os > os ";
	
	/**
	 * This builds a substitution map containing the raw patterns for
	 * certain types of content we expect.
	 * @return The populated map.
	 */
	private static final HashMap<String,String> populatePatterns() {
		HashMap<String,String> p = new HashMap<String, String>();
		// The name is important to understand. The assumption is that the
		// doctype is valid, hence it is easier to search for what the name is
		// not, and not what it is. The name will be terminated with either
		// white-space, [ or >
		p.put("name",     "[^ \\n\\r\\t\\[>]+"); // element name.

		// whitespace: S  ::= (#x20 | #x9 | #xD | #xA)+
		p.put("ms",       "[ \\n\\r\\t]+");      // mandatory whitespace.
		p.put("os",       "[ \\n\\r\\t]*");      // optional whitespace.
		
		// A quoted 'id'/"id" is anything except the quote
		// we need to do parenthesis in this to get grouping to work.
		// also need parenthesis to make the | or condition work
		p.put("id",       "(('([^']*)')|(\"([^\"]*)\"))"); // quoted id.
		
		// The internal subset is treated differently by the code, and the
		// [ ] bracing around the internal subset is specified in the main regex
		p.put("internal", ".*"); // internal subset.
		return p;
	}
	
	/**
	 * This method substitutes the simple tokens in the meta-pattern with
	 * the declared values in the map.
	 * @param map The map containing substitution tokens/patterns
	 * @param input The meta-pattern to do the substitutions on.
	 * @return The substituted pattern
	 */
	private static final Pattern buildPattern(
			HashMap<String,String> map, String input) {
		// we are going to search for tokens. Each token is marked by a space.
		// space was chosen because it makes the meta-pattern easy to read.
		final Pattern search = Pattern.compile(" (\\w+) ");
		final Matcher mat = search.matcher(input);
		StringBuilder sb = new StringBuilder();
		int pos = 0;
		while (mat.find()) {
			String rep = map.get(mat.group(1));
//          we wrote this, it can't happen ;-). Live with a 'null' append.
//			if (rep == null) {
//				throw new IllegalArgumentException(
//						"No definition of token '" + mat.group() + "'.");
//			}
			// can't use appendReplacement as we have to escape '\' chars.
			// and Pattern.quote() does not help
			// mat.appendReplacement(sb, rep);
			sb.append(input.substring(pos, mat.start()));
			sb.append(rep);
			pos = mat.end();
		}
		sb.append(input.substring(pos));
		return Pattern.compile(sb.toString(), Pattern.DOTALL);
	}
	
	/**
	 * The following Pattern is the final result after
	 * parsing/tokenizing/substituting the meta-pattern.
	 */
	private static final Pattern pattern = 
			buildPattern(populatePatterns(), metapattern);

	/*
	 * This pattern relies on pattern grouping to easily pull the values from
	 * the Matcher. Look at the following to get an idea of the groups that
	 * come from the reg-ex
	 * 
	 * 0 -> <!DOCTYPE root SYSTEM "system"  [internal] >
	 * 1 -> root
	 * 2 ->  SYSTEM "system"
	 * 3 -> SYSTEM "system"
	 * 4 -> SYSTEM "system"
	 * 5 -> "system"
	 * 6 -> null
	 * 7 -> null
	 * 8 -> "system"
	 * 9 -> system
	 * 10 -> null
	 * 11 -> null
	 * 12 -> null
	 * 13 -> null
	 * 14 -> null
	 * 15 -> null
	 * 16 -> null
	 * 17 -> null
	 * 18 -> null
	 * 19 -> null
	 * 20 -> null
	 * 21 -> null
	 * 22 ->   [internal]
	 * 23 -> internal
	 * 
	 * 
	 * 0 -> <!DOCTYPE root PUBLIC 'public'  'system'  [internal] >
	 * 1 -> root
	 * 2 ->  PUBLIC 'public'  'system'
	 * 3 -> PUBLIC 'public'  'system'
	 * 4 -> null
	 * 5 -> null
	 * 6 -> null
	 * 7 -> null
	 * 8 -> null
	 * 9 -> null
	 * 10 -> PUBLIC 'public'  'system'
	 * 11 -> 'public'
	 * 12 -> 'public'
	 * 13 -> public
	 * 14 -> null
	 * 15 -> null
	 * 16 ->   'system'
	 * 17 -> 'system'
	 * 18 -> 'system'
	 * 19 -> system
	 * 20 -> null
	 * 21 -> null
	 * 22 ->   [internal]
	 * 23 -> internal
	 * 
	 * 
	 */
	
	/**
	 * Looks in any number of matched groups for a value. Returns the first set
	 * value. The assumption is that, depending on the pattern matches, the
	 * value could be in a few different locations.
	 * @param mat The match that has succeeded
	 * @param groups The groups to check for a value.
	 * @return The first found value.
	 */
	private static final String getGroup(final Matcher mat, final int...groups) {
		for (final int g : groups) {
			final String s = mat.group(g);
			if (s != null) {
				return s;
			}
		}
		return null;
	}
	
	/**
	 * return true if the input character is one of the types recognized in the
	 * DTD spec.
	 * @param ch The char to check
	 * @return true if it is a space, tab, newline, or carriage-return.
	 */
	private static final boolean isWhite(char ch) {
		return ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r';
	}

	/**
	 * Reformat an internal subset.... Each declaration starts on an indented
	 * newline.
	 * @param internal the input DocType declaration as found in a StAX Reader.
	 * @return the formatted input.
	 */
	private static String formatInternal(String internal) {
		StringBuilder sb = new StringBuilder(internal.length());
		char quote = ' ';
		boolean white = true;
		for (char ch : internal.toCharArray()) {
			if (quote == ' ') {
				// we are not in a quoted value...
				if (isWhite(ch)) {
					if (!white) {
						// this will be the first whitespace.
						// replace it with a single ' '
						sb.append(' ');
						white = true;
					}
					// subsequent (unquoted) whitespace is ignored
				} else {
					if (ch == '\'' || ch == '"') {
						// we are entering a quoted value.
						quote = ch;
					} else if (ch == '<') {
						// we are starting some form of declaration.
						sb.append("  ");
					}
					
					if (ch == '>') {
						// we are ending a declaration.
						if (white) {
							// the declaration ended with whitespace, which we
							// remove.
							sb.setCharAt(sb.length() - 1, ch);
						} else {
							// the declaration had no whitespace at the end. OK
							sb.append(ch);
						}
						// all declarations end with a new-line.
						sb.append('\n');
						// and subsequent lines start as trimmed whitespace.
						white = true;
					} else {
						sb.append(ch);
						white = false;
					}
				}
			} else {
				// we are in a quoted value...
				if (ch == quote) {
					//we are leaving the quoted value.
					quote = ' ';
				}
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	/**
	 * Parse out a DOCTYPE declaration as supplied by the standard StAX
	 * readers.
	 * <p>
	 * Using 'XML' terminology, this method assumes that the input is
	 * both 'well-formed' and 'valid'. The assumptions that this class makes
	 * ensure that the 'right thing' is done for valid content, but invalid
	 * content may or may not fail with a JDOMException. The behaviour of this
	 * method with invalid input is 'undefined'.
	 * 
	 * @param input the input DOCTYPE string to parse. Must be valid.
	 * @param factory The JDOM factory to use to build the JDOM DocType.
	 * @return The input string as a DocType.
	 * @throws JDOMException if the DocType is not generated. 
	 */
	public static DocType parse(final String input, final JDOMFactory factory) 
			throws JDOMException {
		
		// Match the input to the DOCTYPE pattern matcher.
		final Matcher mat = pattern.matcher(input);
		if (!mat.matches()) {
			throw new JDOMException("Doctype input does not appear to be valid: " + input);
		}
		
		// Get the four data components.
		final String docemt = mat.group(1);
		final String sysid = getGroup(mat, 7, 9, 19, 21);
		final String pubid = getGroup(mat, 13, 15);
		final String internal = getGroup(mat, 23);
		
		// Use the appropriate constructor for the DocType.
		DocType dt = null;
		if (pubid != null) {
			dt = factory.docType(docemt, pubid, sysid);
		} else if (sysid != null) {
			dt = factory.docType(docemt, sysid);
		} else {
			dt = factory.docType(docemt);
		}
		// Set the internal subset, if any.
		if (internal != null) {
			dt.setInternalSubset(formatInternal(internal));
		}
		return dt;
	}
	
	/**
	 * Make instances 'impossible'. Everything is static.
	 */
	private DTDParser() {
		// nothing, you are not allowed instances of this class.
	}
	
}
