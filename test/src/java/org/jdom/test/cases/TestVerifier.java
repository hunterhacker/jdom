/* Please run replic.pl on me ! */
package org.jdom.test.cases;

/**
 * Please put a description of your test here.
 * 
 * @author unascribed
 * @version 0.1
 */
import junit.framework.*;

import org.jdom.*;
import java.io.*;
import java.util.*;
import org.jdom.input.*;

public final class TestVerifier extends junit.framework.TestCase {
	/**
	 *  Construct a new instance. 
	 */
	public TestVerifier(String name) {
		super(name);
	}

	/**
	 * This method is called before a test is executed.
	 */
	public void setUp() throws JDOMException {
		// your code goes here.

		Document allChars;
		SAXBuilder builder = new SAXBuilder();
		allChars = builder.build(new File("c:/temp/jdom-test/xmlchars.xml"));

		List els = allChars.getRootElement().getChild("prodgroup").getChildren("prod");

		Iterator it = els.iterator();
		while (it.hasNext()) {
			Element prod = (Element)it.next();
			if (prod.getAttribute("id").getValue().equals("NT-Char")) {
				//characters that must be accepted by processor
				allCharacters =  prod.getChild("rhs"); 
			} else if (prod.getAttribute("id").getValue().equals("NT-BaseChar")) {
				//build basechar
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
	 * This method is called after a test is executed.
	 */
	public void tearDown() {
		// your code goes here.
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
	 * The main method runs all the tests in the text ui
	 */
	public static void main (String args[]) 
	 {
		junit.textui.TestRunner.run(suite());
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__String_checkCharacterData_String() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__boolean_isXMLNameStartCharacter_char() {
		fail("implement me !");
	}

	/**
	 * Test that a character is a valid xml letter.
	 */
	public void test_TCM__boolean_isXMLLetter_char() {
		RangeIterator it = new RangeIterator(characters);
		
		while (it.hasNext()) {
			char c = ((Character) it.next()).charValue();
			assert("failed on valid xml character", Verifier.isXMLCharacter(c));
		}
		AntiRangeIterator ait = new AntiRangeIterator(characters);
		while (ait.hasNext()) {
			char c =ait.next();
			if (c == '\u3007' 
				|| (c >= '\u3021' && c <= '\u3029')
				|| (c >= '\u4E00' && c <= '\u9FA5'))
				continue; // ideographic characters accepted
				
			assert("didn't catch invalid XML Base Characters: 0x" + Integer.toHexString(c), ! Verifier.isXMLLetter(c));
		} 
	}

	/**
	 * Test that a char is either a letter or digit according to
	 * xml specs
	 */
	public void test_TCM__boolean_isXMLLetterOrDigit_char() {
		//this test can be simple because the underlying code
		//for letters and digits has already been tested.

		//valid
		assert("didn't accept valid letter: 0x0041", Verifier.isXMLLetterOrDigit('\u0041'));
		assert("didn't accept valid digit: 0x0030", Verifier.isXMLLetterOrDigit('\u0030'));
		assert("accepted invalid letter: 0x0040", Verifier.isXMLLetterOrDigit('\u0041'));
		assert("accepted invalid digit: 0x0029", Verifier.isXMLLetterOrDigit('\u0030'));
		
		
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__void_main_ArrayString() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__String_checkElementName_String() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__String_checkCDATASection_String() {
		fail("implement me !");
	}

	/**
	 * Test the screen for invalid xml characters, IE, non Unicode characters
	 */
	public void test_TCM__boolean_isXMLCharacter_char() {
		RangeIterator it = new RangeIterator(allCharacters);
		char c;
		while (it.hasNext()) {
			c = ((Character) it.next()).charValue();
			if (Character.getNumericValue(c) < 0) //xml characters can be 32 bit so c may = -1
				continue;
			assert("failed on valid xml character: 0x" + Integer.toHexString(c), Verifier.isXMLCharacter(c));
		}
		AntiRangeIterator ait = new AntiRangeIterator(allCharacters);
		while (ait.hasNext()) {
			c =ait.next();
			if (Character.getNumericValue(c) < 0) //xml characters can be 32 bit so c may = -1
				continue;
			assert("didn't catch invalid XML Characters: 0x" + Integer.toHexString(c), ! Verifier.isXMLCharacter(c));
		} 
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__String_checkNamespaceURI_String() {
		fail("implement me !");
	}

	/**
	 * Test that the Verifier accepts all valid and rejects
	 * all invalid XML digits
	 */
	public void test_TCM__boolean_isXMLDigit_char() {
		RangeIterator it = new RangeIterator(digits);
		
		while (it.hasNext()) {
			char c = ((Character) it.next()).charValue();
			assert("failed on valid xml digit", Verifier.isXMLDigit(c));
		}
		
		AntiRangeIterator ait = new AntiRangeIterator(digits);
		while (ait.hasNext()) {
			char c =ait.next();
			assert("didn't catch invalid XMLDigit: 0x" + Integer.toHexString(c), ! Verifier.isXMLDigit(c));
		} 
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__String_checkNamespacePrefix_String() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__String_checkAttributeName_String() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__boolean_isXMLExtender_char() {
		RangeIterator it = new RangeIterator(extenders);
		
		while (it.hasNext()) {
			char c = ((Character) it.next()).charValue();
			assert("failed on valid xml extender", Verifier.isXMLExtender(c));
		}

		it = new RangeIterator(extenders);
		int start = 0;
		int code = 0;
		while (it.hasNext()) {
			start = code;
			code = (int)((Character) it.next()).charValue();
			
			while (code > start && start != code - 1) {
				start ++;
				assert("didnt' catch bad xml extender: 0x" + Integer.toHexString(start), ! Verifier.isXMLExtender((char) start));
				
			}
		}
			
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__String_checkCommentData_String() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__String_checkProcessingInstructionTarget_String() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__boolean_isXMLCombiningChar_char() {
		RangeIterator it = new RangeIterator(combiningChars);
		
		while (it.hasNext()) {
			char c = ((Character) it.next()).charValue();
			assert("failed on valid xml combining character", Verifier.isXMLCombiningChar(c));
		}

		AntiRangeIterator ait = new AntiRangeIterator(combiningChars);
		while (ait.hasNext()) {
			char c =ait.next();
			assert("didn't catch invalid XMLCombiningChar: 0x" + Integer.toHexString(c), ! Verifier.isXMLCombiningChar(c));
		} 
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__boolean_isXMLNameCharacter_char() {
		fail("implement me !");
	}

	/**
	 * the all characters class that must be accepted by processor
	 */
	public org.jdom.Element allCharacters;
	/**
	 * XML Base Characters
	 */
	public org.jdom.Element characters;
	/**
	 * XML CombiningCharacters
	 */
	public org.jdom.Element combiningChars;
	/**
	 * XML Digits
	 */
	public org.jdom.Element digits;
	/**
	 * XML Extender characters
	 */
	public org.jdom.Element extenders;
	/**
	 * XML IdeoCharacters
	 */
	public Element ideochars;
	/**
	 * XML Letter characters
	 */
	public org.jdom.Element letters;

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
				if (range == null)
					return null;

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
}
