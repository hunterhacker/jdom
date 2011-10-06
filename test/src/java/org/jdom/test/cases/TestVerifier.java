package org.jdom.test.cases;

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

/**
 * Please put a description of your test here.
 * 
 * @author unascribed
 * @version 0.1
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.IllegalAddException;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.Verifier;
import org.jdom.input.SAXBuilder;

public final class TestVerifier extends junit.framework.TestCase {

	/**
	 * the all characters class that must be accepted by processor
	 */
	private org.jdom.Element allCharacters;
	/**
	 * XML Base Characters
	 */
	private org.jdom.Element characters;
	/**
	 * XML CombiningCharacters
	 */
	private org.jdom.Element combiningChars;
	/**
	 * XML Digits
	 */
	private org.jdom.Element digits;
	/**
	 * XML Extender characters
	 */
	private org.jdom.Element extenders;
	/**
	 * XML IdeoCharacters
	 */
	private Element ideochars;
	/**
	 * XML Letter characters
	 */
	private org.jdom.Element letters;

	/**
	 * resourceRoot is the directory relative to running root where
	 * resources are found
	 */

	private String resourceRoot;

	/**
	 * AntiRangeIterator is the opposite of RangeIterator
	 * it iterates up to and between all the valid values
	 * for the JDOM Element passed in the constructor and
	 * returns invalid xml char values for the specified
	 * type
	 */
	class AntiRangeIterator {
		int start= 0;
		int code= 0;
		RangeIterator it;

		public AntiRangeIterator(Element el) {
			super();
			it= new RangeIterator(el);
			code= (int) ((Character) it.next()).charValue();
		}

		/**
		 * return the next invalid char
		 */
		public char next() {

			//have we caught up to the current valid code?
			if (code - start > 1) {
				++start;
				return (char) start;
			}
			//must have been a sequential number so loop until we get
			//past this range
			while (code - start == 1 && it.hasNext()) {

				start= code;

				code= (int) ((Character) it.next()).charValue();
			}
			if (it.hasNext()) {
				return (char) ++start;
			} else {
				//all done! return a value above the highest in the range
				return (char) (code + 2);
			}

		}

		/**
		 * is there another invalid char
		 */
		public boolean hasNext() {
			//first pass

			if (it.hasNext())
				return true;
			//all the ranges have been read so now we just iterate
			//to the last invalid char
			return (code > start && start != code - 1);
		}
		/**
		 * iterator candy - unimplemented
		 */
		public void remove() {
		}
	}

	/**
	 * RangeIterator iterates over the ranges of valid
	 * xml characters based on the type passed in as
	 * a JDOM Element.  The xml was originally part of the xml
	 * spec..
	 */
	class RangeIterator implements Iterator {

		StringTokenizer ranges;

		/**
		 * the code for the current char
		 */
		int current= 0;

		/**
		 * the code for the starting char in the current range
		 */
		int start= 0;

		/**
		 * the code for the last char in the current range
		 */
		int end= 0;

		public RangeIterator(Element el) {
			String content= el.getText();

			ranges= new StringTokenizer(content, "|");

		}
		/**
		 * is the next valid char available
		 */
		public boolean hasNext() {

			if (current +1 >= 0x10000) {
				//java can't handle 32 bit unicode chars
				return false;
			} else if (ranges.hasMoreElements()) {
				return true;
			} else if (end - current > 1) {
				return true;
			} else {
				return false;
			}

		}

		/**
		 * return the next valid char
		 */
		public Object next() {
			if (current == end) {
				//get the next token and load a new range
				String range= ranges.nextToken();
				if (range == null) {
					return null;
                                }

				//now parse the ranges into hex strings
				if (range.indexOf('[') >= 0) {
					String startText=
						"0x" + range.substring(range.indexOf('[') + 3, range.indexOf('-'));
					String endText=
						"0x" + range.substring(range.indexOf('-') + 3, range.indexOf(']'));
					//was there a range?
					if (endText.equals("0x")) {
						start= Integer.decode(startText).intValue();
						end= start;
						current= start;
					} else {
						start= Integer.decode(startText).intValue();
						end= Integer.decode(endText).intValue();
						current= start;

					}

					return new Character((char) start);
				} else {
					//character is not in a range
					String startText= range.trim();
					startText= startText.replace('#', '0');
					start= Integer.decode(startText).intValue();
					end= start;
					current= start;
					return new Character((char) current);

				}

			} else {
				// just increment withing the range
				current++;
				return new Character((char) current);
			}
		}

		/**
		 * iterator candy - unimplemented
		 */
		public void remove() {
		}

	}
    /**
     *  Construct a new instance. 
     */
    public TestVerifier(String name) {
        super(name);
    }
    /**
     * The main method runs all the tests in the text ui
     */
    public static void main (String args[]) 
     {
        junit.textui.TestRunner.run(suite());
    }
	/**
	 * This method is called before a test is executed.
	 */
	public void setUp() throws IOException, JDOMException {
		
		// get the ranges of valid characters from the xmlchars.xml resource
		resourceRoot = "resources";
		File rbdir = new File(resourceRoot);
		if (!rbdir.exists()) {
			resourceRoot = "test/" + resourceRoot;
			rbdir = new File(resourceRoot);
			if (!rbdir.exists()) {
				throw new IllegalStateException("Could not find Resource root: "
						+ " (with or without the test/): " + resourceRoot);
			}
		}
		Document allChars;
		SAXBuilder builder = new SAXBuilder();
		InputStream in = new FileInputStream(resourceRoot + "/xmlchars.xml");
		
		allChars = builder.build(in);

		List els = allChars.getRootElement().getChild("prodgroup").getChildren("prod");

		Iterator it = els.iterator();
		while (it.hasNext()) {
			Element prod = (Element)it.next();
			if (prod.getAttribute("id").getValue().equals("NT-Char")) {
				//characters that must be accepted by processor
				allCharacters =  prod.getChild("rhs");
			} else if (prod.getAttribute("id").getValue().equals("NT-BaseChar")) {
				//build base characters
				characters = prod.getChild("rhs");
			} else if (prod.getAttribute("id").getValue().equals("NT-Ideographic")) {
				//build Ideographic characters
				ideochars = prod.getChild("rhs");
			} else if (prod.getAttribute("id").getValue().equals("NT-CombiningChar")) {
				//build CombiningChar
				combiningChars = prod.getChild("rhs");
			} else if (prod.getAttribute("id").getValue().equals("NT-Digit")) {
				//build Digits
				digits = prod.getChild("rhs");
			} else if (prod.getAttribute("id").getValue().equals("NT-Extender")) {
				//build Extenders
				extenders = prod.getChild("rhs");
			}
	

		}
	}
	/**
	 * The suite method runs all the tests
	 */
public static Test suite () {
		TestSuite suite = new TestSuite(TestVerifier.class);
		//TestSuite suite = new TestSuite();
		//suite.addTest(new TestVerifier("test_TCM__boolean_isXMLDigit_char"));
		return suite;
	}
    /**
     * This method is called after a test is executed.
     */
    public void tearDown() {
        // your code goes here.
    }
//	/**
//	 * Test the screen for invalid xml characters, IE, non Unicode characters
//	 */
//	public void test_TCM__boolean_isXMLCharacter_char() {
//		RangeIterator it = new RangeIterator(allCharacters);
//		char c;
//		while (it.hasNext()) {
//			c = ((Character) it.next()).charValue();
//			if (Character.getNumericValue(c) < 0) //xml characters can be 32 bit so c may = -1
//				continue;
//			assertTrue("failed on valid xml character: 0x" + Integer.toHexString(c), Verifier.isXMLCharacter(c));
//		}
//		AntiRangeIterator ait = new AntiRangeIterator(allCharacters);
//		while (ait.hasNext()) {
//			c =ait.next();
//			if (Character.getNumericValue(c) < 0) //xml characters can be 32 bit so c may = -1
//				continue;
//			assertTrue("didn't catch invalid XML Characters: 0x" + Integer.toHexString(c), ! Verifier.isXMLCharacter(c));
//		}
//	}
//	/**
//	 * Test code goes here. Replace this comment.
//	 */
//	public void test_TCM__boolean_isXMLCombiningChar_char() {
//		RangeIterator it = new RangeIterator(combiningChars);
//
//		while (it.hasNext()) {
//			char c = ((Character) it.next()).charValue();
//			assertTrue("failed on valid xml combining character", Verifier.isXMLCombiningChar(c));
//		}
//
//		AntiRangeIterator ait = new AntiRangeIterator(combiningChars);
//		while (ait.hasNext()) {
//			char c =ait.next();
//			assertTrue("didn't catch invalid XMLCombiningChar: 0x" + Integer.toHexString(c), ! Verifier.isXMLCombiningChar(c));
//		}
//	}
//	/**
//	 * Test that the Verifier accepts all valid and rejects
//	 * all invalid XML digits
//	 */
//	public void test_TCM__boolean_isXMLDigit_char() {
//		RangeIterator it = new RangeIterator(digits);
//
//		while (it.hasNext()) {
//			char c = ((Character) it.next()).charValue();
//			assertTrue("failed on valid xml digit", Verifier.isXMLDigit(c));
//		}
//
//		AntiRangeIterator ait = new AntiRangeIterator(digits);
//		while (ait.hasNext()) {
//			char c =ait.next();
//			assertTrue("didn't catch invalid XMLDigit: 0x" + Integer.toHexString(c), ! Verifier.isXMLDigit(c));
//		}
//	}
//	/**
//	 * Test code goes here. Replace this comment.
//	 */
//	public void test_TCM__boolean_isXMLExtender_char() {
//		RangeIterator it = new RangeIterator(extenders);
//
//		while (it.hasNext()) {
//			char c = ((Character) it.next()).charValue();
//			assertTrue("failed on valid xml extender", Verifier.isXMLExtender(c));
//		}
//
//		it = new RangeIterator(extenders);
//		int start = 0;
//		int code = 0;
//		while (it.hasNext()) {
//			start = code;
//			code = (int)((Character) it.next()).charValue();
//
//			while (code > start && start != code - 1) {
//				start ++;
//				assertTrue("didnt' catch bad xml extender: 0x" + Integer.toHexString(start), ! Verifier.isXMLExtender((char) start));
//
//			}
//		}
//
//	}
//	/**
//	 * Test that a character is a valid xml letter.
//	 */
//	public void test_TCM__boolean_isXMLLetter_char() {
//		RangeIterator it = new RangeIterator(characters);
//
//		while (it.hasNext()) {
//			char c = ((Character) it.next()).charValue();
//			assertTrue("failed on valid xml character", Verifier.isXMLCharacter(c));
//		}
//		AntiRangeIterator ait = new AntiRangeIterator(characters);
//		while (ait.hasNext()) {
//			char c =ait.next();
//			if (c == '\u3007'
//				|| (c >= '\u3021' && c <= '\u3029')
//				|| (c >= '\u4E00' && c <= '\u9FA5'))
//				continue; // ideographic characters accepted
//
//			assertTrue("didn't catch invalid XML Base Characters: 0x" + Integer.toHexString(c), ! Verifier.isXMLLetter(c));
//		}
//	}
//	/**
//	 * Test that a char is either a letter or digit according to
//	 * xml specs
//	 */
//	public void test_TCM__boolean_isXMLLetterOrDigit_char() {
//		//this test can be simple because the underlying code
//		//for letters and digits has already been tested.
//
//		//valid
//		assertTrue("didn't accept valid letter: 0x0041", Verifier.isXMLLetterOrDigit('\u0041'));
//		assertTrue("didn't accept valid digit: 0x0030", Verifier.isXMLLetterOrDigit('\u0030'));
//		assertTrue("accepted invalid letter: 0x0040", Verifier.isXMLLetterOrDigit('\u0041'));
//		assertTrue("accepted invalid digit: 0x0029", Verifier.isXMLLetterOrDigit('\u0030'));
//
//
//	}
//	/**
//	 * Test the test for valid xml name characters.  This test only checks
//	 * that the method dispatches correctly to other checks which have already
//	 * been tested for completeness except where specific name characters are
//	 * allowed such as '-'. '_', ':' and '.'.  The other valid name characters are
//	 * xml letters, digits, extenders and combining characters.
//	 */
//	public void test_TCM__boolean_isXMLNameCharacter_char() {
//
//		//check in the low ascii range
//		assertTrue("validated invalid char 0x20", Verifier.isXMLNameCharacter(' '));
//		assertTrue("validated invalid char \t", Verifier.isXMLNameCharacter('\t'));
//		assertTrue("validated invalid char null", Verifier.isXMLNameCharacter((char)0x0));
//		assertTrue("validated invalid char \n", Verifier.isXMLNameCharacter('\n'));
//		assertTrue("validated invalid char 0x29", Verifier.isXMLNameCharacter((char)0x29));
//		//a few higher values
//		assertTrue("validated invalid char 0x00B8", Verifier.isXMLNameCharacter((char)0x00B8));
//		assertTrue("validated invalid char 0x02FF", Verifier.isXMLNameCharacter((char)0x02FF));
//		assertTrue("validated invalid char 0x04DFF", Verifier.isXMLNameCharacter((char)0x4DFF));
//
//		//exceptional characters for names
//		assertTrue("invalidated valid char :", Verifier.isXMLNameCharacter(':'));
//		assertTrue("invalidated valid char -", Verifier.isXMLNameCharacter('-'));
//		assertTrue("invalidated valid char _", Verifier.isXMLNameCharacter('_'));
//		assertTrue("invalidated valid char .", Verifier.isXMLNameCharacter('.'));
//		//xml letter
//		assertTrue("invalidated valid char 0x42", Verifier.isXMLNameCharacter((char)0x42));
//		assertTrue("invalidated valid char 0x4E01", Verifier.isXMLNameCharacter((char)0x4E01));
//		//xml digit
//		assertTrue("invalidated valid char 0x0031", Verifier.isXMLNameCharacter((char)0x0031));
//		//xml combining character
//		assertTrue("invalidated valid char 0x0301", Verifier.isXMLNameCharacter((char)0x0301));
//		//xml extender
//		assertTrue("invalidated valid char 0x00B7", Verifier.isXMLNameCharacter((char)0x00B7));
//	}
//	/**
//	 * Test that this character is a valid name start character.
//	 * Valid name start characters are xml letters, ':' and '_'
//	 */
//	public void test_TCM__boolean_isXMLNameStartCharacter_char() {
//		//check in the low ascii range
//		assertTrue("validated invalid char 0x20", Verifier.isXMLNameCharacter(' '));
//		assertTrue("validated invalid char \t", Verifier.isXMLNameCharacter('\t'));
//		assertTrue("validated invalid char null", Verifier.isXMLNameCharacter((char)0x0));
//		assertTrue("validated invalid char \n", Verifier.isXMLNameCharacter('\n'));
//		assertTrue("validated invalid char 0x29", Verifier.isXMLNameCharacter((char)0x29));
//		//a few higher values
//		assertTrue("validated invalid char 0x00B8", Verifier.isXMLNameCharacter((char)0x00B8));
//		assertTrue("validated invalid char 0x02FF", Verifier.isXMLNameCharacter((char)0x02FF));
//		assertTrue("validated invalid char 0x04DFF", Verifier.isXMLNameCharacter((char)0x4DFF));
//
//		//exceptional characters for names
//		assertTrue("invalidated valid char :", Verifier.isXMLNameCharacter(':'));
//		assertTrue("invalidated valid char _", Verifier.isXMLNameCharacter('_'));
//		//xml letter
//		assertTrue("invalidated valid char 0x42", Verifier.isXMLNameCharacter((char)0x42));
//		assertTrue("invalidated valid char 0x4E01", Verifier.isXMLNameCharacter((char)0x4E01));
//
//	}

	/**
	 * Test for a valid Attribute name.  A valid Attribute name is
	 * an xml name (xml start character + xml name characters) with
	 * some special considerations.  "xml:lang" and "xml:space" are
	 * allowed.  No ':' are allowed since prefixes are defined with
	 * Namespace objects.  The name must not be "xmlns"
	 */
	public void test_TCM__String_checkAttributeName_String() {
		//check out of range values
		assertNotNull("validated invalid null", Verifier.checkAttributeName(null));
		assertNotNull("validated invalid name with null", Verifier.checkAttributeName("test" + (char)0x0));
		assertNotNull("validated invalid name with null", Verifier.checkAttributeName("test" + (char)0x0 + "ing"));
		assertNotNull("validated invalid name with null", Verifier.checkAttributeName((char)0x0 + "test"));
		assertNotNull("validated invalid name with 0x01", Verifier.checkAttributeName((char)0x01 + "test"));
		assertNotNull("validated invalid name with 0xD800", Verifier.checkAttributeName("test" + (char)0xD800));
		assertNotNull("validated invalid name with 0xD800", Verifier.checkAttributeName("test" + (char)0xD800 + "ing"));
		assertNotNull("validated invalid name with 0xD800", Verifier.checkAttributeName((char)0xD800 + "test"));
		assertNotNull("validated invalid name with :", Verifier.checkAttributeName("test" + ':' + "local"));
		assertNotNull("validated invalid name with xml:lang", Verifier.checkAttributeName("xml:lang"));
		assertNotNull("validated invalid name with xml:space", Verifier.checkAttributeName("xml:space"));

		//invalid start characters
		assertNotNull("validated invalid name with startin -", Verifier.checkAttributeName('-' + "test"));
		assertNotNull("validated invalid name with xmlns", Verifier.checkAttributeName("xmlns"));
		assertNotNull("validated invalid name with startin :", Verifier.checkAttributeName(':' + "test"));

		//valid tests
		assertNull("invalidated valid name with starting _", Verifier.checkAttributeName('_' + "test"));
		assertNull("invalidated valid name with _", Verifier.checkAttributeName("test" + '_'));
		assertNull("invalidated valid name with .", Verifier.checkAttributeName("test" + '.' + "name"));
		assertNull("invalidated valid name with 0x00B7", Verifier.checkAttributeName("test" + (char)0x00B7));
		assertNull("invalidated valid name with 0x4E01", Verifier.checkAttributeName("test" + (char)0x4E01));
		assertNull("invalidated valid name with 0x0301", Verifier.checkAttributeName("test" + (char)0x0301));

	}
	/**
	 * Test that checkCDATASection verifies CDATA excluding
	 * the closing delimiter.
	 */
	public void test_TCM__String_checkCDATASection_String() {
		//check out of range values
		assertNotNull("validated invalid null", Verifier.checkCDATASection(null));
		assertNotNull("validated invalid string with null", Verifier.checkCDATASection("test" + (char)0x0));
		assertNotNull("validated invalid string with null", Verifier.checkCDATASection("test" + (char)0x0 + "ing"));
		assertNotNull("validated invalid string with null", Verifier.checkCDATASection((char)0x0 + "test"));
		assertNotNull("validated invalid string with 0x01", Verifier.checkCDATASection((char)0x01 + "test"));
		assertNotNull("validated invalid string with 0xD800", Verifier.checkCDATASection("test" + (char)0xD800));
		assertNotNull("validated invalid string with 0xD800", Verifier.checkCDATASection("test" + (char)0xD800 + "ing"));
		assertNotNull("validated invalid string with 0xD800", Verifier.checkCDATASection((char)0xD800 + "test"));
		assertNotNull("validated invalid string with ]]>", Verifier.checkCDATASection("test]]>"));

		//various valid strings
		assertNull("invalidated valid string with \n", Verifier.checkCDATASection("test" + '\n' + "ing"));
		assertNull("invalidated valid string with 0x29", Verifier.checkCDATASection("test" +(char)0x29));
		assertNull("invalidated valid string with ]", Verifier.checkCDATASection("test]"));
		assertNull("invalidated valid string with [", Verifier.checkCDATASection("test["));
		//a few higher values
		assertNull("invalidated valid string with 0x0B08", Verifier.checkCDATASection("test" + (char)0x0B08));
		assertNull("invalidated valid string with \t", Verifier.checkCDATASection("test" + '\t'));
		//xml letter
		assertNull("invalidated valid string with 0x42", Verifier.checkCDATASection("test" + (char)0x42));
		assertNull("invalidated valid string with 0x4E01", Verifier.checkCDATASection("test" + (char)0x4E01));

	}
	/**
	 * Test that a String contains only xml characters.  The method under
	 * only checks for null values and then character by character scans
	 * the string so this test is not exhaustive
	 */
	public void test_TCM__String_checkCharacterData_String() {
		//check out of range values
		assertNotNull("validated invalid null", Verifier.checkCharacterData(null));
		assertNotNull("validated invalid string with null", Verifier.checkCharacterData("test" + (char)0x0));
		assertNotNull("validated invalid string with null", Verifier.checkCharacterData("test" + (char)0x0 + "ing"));
		assertNotNull("validated invalid string with null", Verifier.checkCharacterData((char)0x0 + "test"));
		assertNotNull("validated invalid string with 0x01", Verifier.checkCharacterData((char)0x01 + "test"));
		assertNotNull("validated invalid string with 0xD800", Verifier.checkCharacterData("test" + (char)0xD800));
		assertNotNull("validated invalid string with 0xD800", Verifier.checkCharacterData("test" + (char)0xD800 + "ing"));
		assertNotNull("validated invalid string with 0xD800", Verifier.checkCharacterData((char)0xD800 + "test"));

		//various valid strings
		assertNull("invalidated valid string with \n", Verifier.checkCharacterData("test" + '\n' + "ing"));
		assertNull("invalidated valid string with 0x29", Verifier.checkCharacterData("test" +(char)0x29));
		//a few higher values
		assertNull("invalidated valid string with 0x0B08", Verifier.checkCharacterData("test" + (char)0x0B08));
		assertNull("invalidated valid string with \t", Verifier.checkCharacterData("test" + '\t'));
		//xml letter
		assertNull("invalidated valid string with 0x42", Verifier.checkCharacterData("test" + (char)0x42));
		assertNull("invalidated valid string with 0x4E01", Verifier.checkCharacterData("test" + (char)0x4E01));

	}
	/**
	 * Test checkCommentData such that a comment is validated as an
	 * xml comment consisting of xml characters with the following caveats.
	 * The comment must not contain a double hyphen.
	 */
	public void test_TCM__String_checkCommentData_String() {
		//check out of range values
		assertNotNull("validated invalid null", Verifier.checkCommentData(null));
		assertNotNull("validated invalid string with null", Verifier.checkCommentData("test" + (char)0x0));
		assertNotNull("validated invalid string with null", Verifier.checkCommentData("test" + (char)0x0 + "ing"));
		assertNotNull("validated invalid string with null", Verifier.checkCommentData((char)0x0 + "test"));
		assertNotNull("validated invalid string with 0x01", Verifier.checkCommentData((char)0x01 + "test"));
		assertNotNull("validated invalid string with 0xD800", Verifier.checkCommentData("test" + (char)0xD800));
		assertNotNull("validated invalid string with 0xD800", Verifier.checkCommentData("test" + (char)0xD800 + "ing"));
		assertNotNull("validated invalid string with 0xD800", Verifier.checkCommentData((char)0xD800 + "test"));
		assertNotNull("validated invalid string with --", Verifier.checkCommentData("--test"));

		//various valid strings
		assertNull("invalidated valid string with \n", Verifier.checkCommentData("test" + '\n' + "ing"));
		assertNull("invalidated valid string with 0x29", Verifier.checkCommentData("test" +(char)0x29));
		//a few higher values
		assertNull("invalidated valid string with 0x0B08", Verifier.checkCommentData("test" + (char)0x0B08));
		assertNull("invalidated valid string with \t", Verifier.checkCommentData("test" + '\t'));
		//xml letter
		assertNull("invalidated valid string with 0x42", Verifier.checkCommentData("test" + (char)0x42));
		assertNull("invalidated valid string with 0x4E01", Verifier.checkCommentData("test" + (char)0x4E01));

	}
	/**
	 * Test checkElementName such that a name is validated as an
	 * xml name with the following caveats.
	 * The name must not start with "-" or ":".
	 */
	public void test_TCM__String_checkElementName_String() {
		//check out of range values
		assertNotNull("validated invalid null", Verifier.checkElementName(null));
		assertNotNull("validated invalid name with null", Verifier.checkElementName("test" + (char)0x0));
		assertNotNull("validated invalid name with null", Verifier.checkElementName("test" + (char)0x0 + "ing"));
		assertNotNull("validated invalid name with null", Verifier.checkElementName((char)0x0 + "test"));
		assertNotNull("validated invalid name with 0x01", Verifier.checkElementName((char)0x01 + "test"));
		assertNotNull("validated invalid name with 0xD800", Verifier.checkElementName("test" + (char)0xD800));
		assertNotNull("validated invalid name with 0xD800", Verifier.checkElementName("test" + (char)0xD800 + "ing"));
		assertNotNull("validated invalid name with 0xD800", Verifier.checkElementName((char)0xD800 + "test"));
		assertNotNull("validated invalid name with :", Verifier.checkElementName("test" + ':' + "local"));

		//invalid start characters
		assertNotNull("validated invalid name with startin -", Verifier.checkElementName('-' + "test"));
		assertNotNull("validated invalid name with startin :", Verifier.checkElementName(':' + "test"));

		//valid tests
		assertNull("invalidated valid name with starting _", Verifier.checkElementName('_' + "test"));
		assertNull("invalidated valid name with _", Verifier.checkElementName("test" + '_'));
		assertNull("invalidated valid name with .", Verifier.checkElementName("test" + '.' + "name"));
		assertNull("invalidated valid name with 0x00B7", Verifier.checkElementName("test" + (char)0x00B7));
		assertNull("invalidated valid name with 0x4E01", Verifier.checkElementName("test" + (char)0x4E01));
		assertNull("invalidated valid name with 0x0301", Verifier.checkElementName("test" + (char)0x0301));

	}
	/**
	 * Test that checkNamespacePrefix validates against xml names
	 * with the following exceptions.  Prefix names must not start
	 * with "-", "xmlns", digits, "$", or "." and must not contain
	 * ":"
	 */
	public void test_TCM__String_checkNamespacePrefix_String() {
		//check out of range values
		assertNotNull("validated invalid name with null", Verifier.checkNamespacePrefix("test" + (char)0x0));
		assertNotNull("validated invalid name with null", Verifier.checkNamespacePrefix("test" + (char)0x0 + "ing"));
		assertNotNull("validated invalid name with null", Verifier.checkNamespacePrefix((char)0x0 + "test"));
		assertNotNull("validated invalid name with 0x01", Verifier.checkNamespacePrefix((char)0x01 + "test"));
		assertNotNull("validated invalid name with 0xD800", Verifier.checkNamespacePrefix("test" + (char)0xD800));
		assertNotNull("validated invalid name with 0xD800", Verifier.checkNamespacePrefix("test" + (char)0xD800 + "ing"));
		assertNotNull("validated invalid name with 0xD800", Verifier.checkNamespacePrefix((char)0xD800 + "test"));
		assertNotNull("validated invalid name with :", Verifier.checkNamespacePrefix("test" + ':' + "local"));

		//invalid start characters
		assertNotNull("validated invalid name with startin -", Verifier.checkNamespacePrefix('-' + "test"));
		assertNotNull("validated invalid name with xmlns", Verifier.checkNamespacePrefix("xmlns"));
		assertNotNull("validated invalid name with startin :", Verifier.checkNamespacePrefix(':' + "test"));
		assertNotNull("validated invalid name with starting digit", Verifier.checkNamespacePrefix("9"));
		assertNotNull("validated invalid name with starting $", Verifier.checkNamespacePrefix("$"));
		assertNotNull("validated invalid name with starting .", Verifier.checkNamespacePrefix("."));

		//valid tests
		assertNull("invalidated valid null", Verifier.checkNamespacePrefix(null));
		assertNull("invalidated valid name with starting _", Verifier.checkNamespacePrefix('_' + "test"));
		assertNull("invalidated valid name with _", Verifier.checkNamespacePrefix("test" + '_'));
		assertNull("invalidated valid name with .", Verifier.checkNamespacePrefix("test" + '.' + "name"));
		assertNull("invalidated valid name with digit", Verifier.checkNamespacePrefix("test9"));
		assertNull("invalidated valid name with 0x00B7", Verifier.checkNamespacePrefix("test" + (char)0x00B7));
		assertNull("invalidated valid name with 0x4E01", Verifier.checkNamespacePrefix("test" + (char)0x4E01));
		assertNull("invalidated valid name with 0x0301", Verifier.checkNamespacePrefix("test" + (char)0x0301));

	}
	/**
	 * Tests that checkNamespaceURI validates xml uri's.
	 * A valid URI is alphanumeric characters and the reserved characters:
	 * ";" | "/" | "?" | ":" | "@" | "&" | "=" | "+" |  "$" | ","
	 *            
	 * The URI cannot begin with a digit, "-" or "$".  It must have at least
	 * one ":" separating the scheme from the scheme specific part
	 *
	 * XXX:TODO make this match the eventual specs for the Verifier class which is incomplete
	 */
	public void test_TCM__String_checkNamespaceURI_String() {
		//invalid start characters
		assertNotNull("validated invalid URI with startin -", Verifier.checkNamespaceURI('-' + "test"));
		assertNotNull("validated invalid URI with starting digit", Verifier.checkNamespaceURI("9"));
		assertNotNull("validated invalid URI with starting $", Verifier.checkNamespaceURI("$"));

		//valid tests
		assertNull("invalidated valid null", Verifier.checkNamespaceURI(null));
		assertNull("invalidated valid URI with :", Verifier.checkNamespaceURI("test" + ':' + "local"));
		assertNull("invalidated valid URI with _", Verifier.checkNamespaceURI("test" + '_'));
		assertNull("invalidated valid URI with .", Verifier.checkNamespaceURI("test" + '.' + "URI"));
		assertNull("invalidated valid URI with digit", Verifier.checkNamespaceURI("test9"));
		assertNull("invalidated valid URI with 0x00B7", Verifier.checkNamespaceURI("test" + (char)0x00B7));
		assertNull("invalidated valid URI with 0x4E01", Verifier.checkNamespaceURI("test" + (char)0x4E01));
		assertNull("invalidated valid URI with 0x0301", Verifier.checkNamespaceURI("test" + (char)0x0301));

		//check out of range values

		/** skip these tests until the time the checks are implemented
		assertNull("validated invalid URI with xmlns", Verifier.checkNamespaceURI("xmlns"));
		assertNull("validated invalid URI with startin :", Verifier.checkNamespaceURI(':' + "test"));
		assertNull("validated invalid URI with starting .", Verifier.checkNamespaceURI("."));
		
		assertNull("validated invalid URI with null", Verifier.checkNamespaceURI("test" + (char)0x0));
		assertNull("validated invalid URI with null", Verifier.checkNamespaceURI("test" + (char)0x0 + "ing"));
		assertNull("validated invalid URI with null", Verifier.checkNamespaceURI((char)0x0 + "test"));
		assertNull("validated invalid URI with 0x01", Verifier.checkNamespaceURI((char)0x01 + "test"));
		assertNull("validated invalid URI with 0xD800", Verifier.checkNamespaceURI("test" + (char)0xD800));
		assertNull("validated invalid URI with 0xD800", Verifier.checkNamespaceURI("test" + (char)0xD800 + "ing"));
		assertNull("validated invalid URI with 0xD800", Verifier.checkNamespaceURI((char)0xD800 + "test"));
		*/


	}
	/**
	 * Test that checkProcessintInstructionTarget validates the name
	 * of a processing instruction.  This name must be a normal xml
	 * and cannot have ":" or "xml" in the name.
	 */
	public void test_TCM__String_checkProcessingInstructionTarget_String() {
		//check out of range values
		assertNotNull("validated invalid null", Verifier.checkProcessingInstructionTarget(null));
		assertNotNull("validated invalid name with null", Verifier.checkProcessingInstructionTarget("test" + (char)0x0));
		assertNotNull("validated invalid name with null", Verifier.checkProcessingInstructionTarget("test" + (char)0x0 + "ing"));
		assertNotNull("validated invalid name with null", Verifier.checkProcessingInstructionTarget((char)0x0 + "test"));
		assertNotNull("validated invalid name with 0x01", Verifier.checkProcessingInstructionTarget((char)0x01 + "test"));
		assertNotNull("validated invalid name with 0xD800", Verifier.checkProcessingInstructionTarget("test" + (char)0xD800));
		assertNotNull("validated invalid name with 0xD800", Verifier.checkProcessingInstructionTarget("test" + (char)0xD800 + "ing"));
		assertNotNull("validated invalid name with 0xD800", Verifier.checkProcessingInstructionTarget((char)0xD800 + "test"));
		assertNotNull("validated invalid name with :", Verifier.checkProcessingInstructionTarget("test" + ':' + "local"));
		assertNotNull("validated invalid name with xml:space", Verifier.checkProcessingInstructionTarget("xml:space"));
		assertNotNull("validated invalid name with xml:lang", Verifier.checkProcessingInstructionTarget("xml:lang"));
		assertNotNull("validated invalid name with xml", Verifier.checkProcessingInstructionTarget("xml"));
		assertNotNull("validated invalid name with xMl", Verifier.checkProcessingInstructionTarget("xMl"));

		//invalid start characters
		assertNotNull("validated invalid name with startin -", Verifier.checkProcessingInstructionTarget('-' + "test"));
		assertNotNull("validated invalid name with startin :", Verifier.checkProcessingInstructionTarget(':' + "test"));
		//valid tests
		assertNull("invalidated valid name with starting _", Verifier.checkProcessingInstructionTarget('_' + "test"));
		assertNull("invalidated valid name with _", Verifier.checkProcessingInstructionTarget("test" + '_'));
		assertNull("invalidated valid name with .", Verifier.checkProcessingInstructionTarget("test" + '.' + "name"));
		assertNull("invalidated valid name with 0x00B7", Verifier.checkProcessingInstructionTarget("test" + (char)0x00B7));
		assertNull("invalidated valid name with 0x4E01", Verifier.checkProcessingInstructionTarget("test" + (char)0x4E01));
		assertNull("invalidated valid name with 0x0301", Verifier.checkProcessingInstructionTarget("test" + (char)0x0301));

	}

  public void test_Namespace_Attribute_collision() {
    try {
      Namespace ns1 = Namespace.getNamespace("aaa", "http://acme.com/aaa");
      Element e = new Element("aaa", ns1);
      e.setAttribute("att1", "att1");
      Namespace defns = Namespace.getNamespace("http://acme.com/default");
      e.addNamespaceDeclaration(defns);
			return;  // pass
    }
    catch (IllegalAddException e) {
      fail("Bug http://www.junlu.com/msg/166290.html");
    }
  }
	/**
	 * This test is a noop.  The method is for development only
	 * It will remain in the suite until it is removed from the
	 * Verifier class
	 */
	public void test_TCM__void_main_ArrayString() {
		//this test is a noop.  The method is for development only
	}
}
