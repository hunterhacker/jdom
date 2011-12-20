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

import java.util.List;

import org.jdom2.Content;
import org.jdom2.Verifier;

/**
 * Methods common/useful for all Outputter processors.
 * 
 * @since JDOM2
 * @author Rolf Lear
 */
public abstract class AbstractOutputProcessor {

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

	/**
	 * Inspect the specified content range of text-type for any that have actual
	 * text.
	 * 
	 * @param content
	 *        a <code>List</code> containing Content.
	 * @param offset
	 *        the start offset to check
	 * @param len
	 *        how much content to check
	 * @return true if there's no actual text in the specified content range.
	 */
	protected final boolean isAllWhiteSpace(
			final List<? extends Content> content, final int offset,
			final int len) {
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
					throw new IllegalStateException("isWhiteSpace was given a "
							+ c.getCType() + " to check, which is illegal");
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
	protected final boolean isAllWhitespace(final String value) {
		for (char ch : value.toCharArray()) {
			if (!Verifier.isXMLWhitespace(ch)) {
				return false;
			}
		}
		return true;
	}

}
