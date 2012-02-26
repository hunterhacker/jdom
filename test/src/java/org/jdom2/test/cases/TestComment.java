package org.jdom2.test.cases;

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
 * @author Philip Nelson
 * @version 1.0
 */
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jdom2.Comment;
import org.jdom2.Content;
import org.jdom2.Element;
import org.jdom2.IllegalDataException;
import org.junit.Test;
import org.junit.runner.JUnitCore;

@SuppressWarnings("javadoc")
public final class TestComment {

	/**
	 * The main method runs all the tests in the text ui
	 */
	public static void main (String args[]) 
	{
		JUnitCore.runClasses(TestComment.class);
	}

	@Test
	public void test_TCC() {
		// test creating a subclass with an anonymous instance
		final Comment theComment = new Comment() {
			// no modifications.
    		private static final long serialVersionUID = 200L;
		};
		assertTrue(null == theComment.getText());
	}
	/**
	 * Test the comment constructor with a valid and an invalid string.
	 */
	@Test
	public void test_TCC___String() {
		Comment theComment = new org.jdom2.Comment("this is a comment");

		assertEquals(
				"incorrect Comment constructed", 
				"[Comment: <!--this is a comment-->]", 
				theComment.toString()); 
		try {
			theComment = new org.jdom2.Comment(null);
			fail("Comment constructor didn't catch invalid comment string");
		} catch (IllegalDataException e) {
			// Do nothing
		} catch (Exception e) {
			fail("Unexpected exception " + e.getClass());
		}
	}
	/**
	 * Verify a simple object == object test
	 */
	@Test
	public void test_TCM__boolean_equals_Object() {
		Comment com = new Comment("test");

		Object ob = com;

		assertTrue("object not equal to comment", com.equals(ob));
	}
	/**
	 * Test that a real hashcode is returned and that a different one is returned
	 * for a different comment.
	 */
	@Test
	public void test_TCM__int_hashCode() {
		//not sure what to test!

		Comment com = new Comment("test");
		//only an exception would be a problem
		int i = -1;
		try {
			i = com.hashCode();
		}
		catch(Exception e) {
			fail("bad hashCode");
		}
		Comment com2 = new Comment("test");
		//different comments, same text
		int x = com2.hashCode();
		assertTrue("Different comments with same value have same hashcode", x != i);
		Comment com3 = new Comment("test2");
		//only an exception would be a problem
		int y = com3.hashCode();
		assertTrue("Different comments have same hashcode", y != x);
	}
	
	/**
	 * Test setting and resetting the text value of this Comment.
	 */
	@Test
	public void test_TCM__OrgJdomComment_setText_String() {
		Comment theComment= new org.jdom2.Comment("this is a comment");

		assertEquals(
				"incorrect Comment constructed",
				"[Comment: <!--this is a comment-->]",
				theComment.toString());

		//set it to the empty string
		theComment.setText("");

		assertEquals("incorrect Comment text", "", theComment.getText());
		assertEquals(theComment.getText(), theComment.getValue());
		//set it to a new string
		theComment.setText("12345qwerty");

		assertEquals("incorrect Comment text", "12345qwerty", theComment.getText());
		assertEquals(theComment.getText(), theComment.getValue());

		//tests for invalid data but setText doesn't

		try {
			theComment.setText(null);
			fail("Comment setText didn't catch invalid comment string");
		} catch (IllegalDataException e) {
			// Do nothing
		} catch (Exception e) {
			fail("Unexpected exception " + e.getClass());
		}
		try {
			char c= 0x11;
			StringBuilder b= new StringBuilder("hhhh");
			b.setCharAt(2, c);
			theComment.setText(b.toString());
			fail("Comment setText didn't catch invalid comment string");
		} catch (IllegalDataException e) {
			// Do nothing
		} catch (Exception e) {
			fail("Unexpected exception " + e.getClass());
		}

	}
	
	/**
	 * Match the XML fragment this comment produces.
	 */
	@Test
	public void test_TCM__String_getSerializedForm() {

		/** No op because the method is deprecated
		Comment theComment = new org.jdom2.Comment("this is a comment");

		assertEquals(
				"incorrect Comment constructed", 
				"<!--this is a comment-->", 
				theComment.getSerializedForm());
		 */

	}
	
	/**
	 * verify that the text of the Comment matches expected value.
	 */
	@Test
	public void test_TCM__String_getText() {
		Comment theComment = new org.jdom2.Comment("this is a comment");

		assertEquals(
				"incorrect Comment constructed", 
				"this is a comment", 
				theComment.getText()); 


	}
	/**
	 * check for the expected toString text value of Comment.
	 */
	@Test
	public void test_TCM__String_toString() {
		Comment theComment= new org.jdom2.Comment("this is a comment");

		assertEquals(
				"incorrect Comment constructed",
				"[Comment: <!--this is a comment-->]",
				theComment.toString());
		try {
			theComment= new org.jdom2.Comment(null);
			fail("Comment constructor didn't catch invalid comment string");
		} catch (IllegalDataException e) {
			// Do nothing
		} catch (Exception e) {
			fail("Unexpected exception " + e.getClass());
		}
	}
	
    @Test
	public void testCloneDetatchParentComment() {
		Element parent = new Element("root");
		Comment content = new Comment("val");
		parent.addContent(content);
		Comment clone = content.detach().clone();
		assertEquals(content.getValue(), clone.getValue());
		assertNull(content.getParent());
		assertNull(clone.getParent());
	}

    @Test
    public void testContentCType() {
    	assertTrue(Content.CType.Comment == new Comment("").getCType());
    }
}
