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
 * @author Philip Nelson
 * @version 1.0
 */
import junit.framework.*;
import org.jdom.*;

public final class TestComment
extends junit.framework.TestCase
{
	/**
	 *  Construct a new instance. 
	 */
	public TestComment(String name) {
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
	public void setUp() {
		// your code goes here.
	}
	/**
	 * The suite method runs all the tests
	 */
	public static Test suite () {
		TestSuite suite = new TestSuite(TestComment.class);
		return suite;
	}
	/**
	 * This method is called after a test is executed.
	 */
	public void tearDown() {
		// your code goes here.
	}
	/**
	 * Test the comment constructor with a valid and an invalid string.
	 */
	public void test_TCC___String() {
		Comment theComment = new org.jdom.Comment("this is a comment");

		assertEquals(
				"incorrect Comment constructed", 
				"[Comment: <!--this is a comment-->]", 
				theComment.toString()); 
		try {
			theComment = new org.jdom.Comment(null);
			fail("Comment constructor didn't catch invalid comment string");
		} catch (IllegalDataException e) {
		}
	}
	/**
	 * Verify a simple object == object test
	 */
	public void test_TCM__boolean_equals_Object() {
	    Comment com = new Comment("test");

	    Object ob = (Object)com;

	    assertTrue("object not equal to comment", com.equals(ob));
	}
	/**
	 * Test that a real hashcode is returned and that a different one is returned
	 * for a different comment.
	 */
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
public void test_TCM__OrgJdomComment_setText_String() {
	Comment theComment= new org.jdom.Comment("this is a comment");

	assertEquals(
		"incorrect Comment constructed",
		"[Comment: <!--this is a comment-->]",
		theComment.toString());

	//set it to the empty string
	theComment.setText("");

	assertEquals("incorrect Comment text", "", theComment.getText());
	//set it to a new string
	theComment.setText("12345qwerty");

	assertEquals("incorrect Comment text", "12345qwerty", theComment.getText());

	//tests for invalid data but setText doesn't

	try {
		theComment.setText(null);
		fail("Comment setText didn't catch invalid comment string");
	} catch (IllegalDataException e) {
	}
	try {
		char c= 0x11;
		StringBuffer b= new StringBuffer("hhhh");
		b.setCharAt(2, c);
		theComment.setText(b.toString());
		fail("Comment setText didn't catch invalid comment string");
	} catch (IllegalDataException e) {
	}

}
	/**
	 * Match the XML fragment this comment produces.
	 */
	public void test_TCM__String_getSerializedForm() {

		/** No op because the method is deprecated
		Comment theComment = new org.jdom.Comment("this is a comment");

		assertEquals(
				"incorrect Comment constructed", 
				"<!--this is a comment-->", 
				theComment.getSerializedForm());
		*/

	}
	/**
	 * verify that the text of the Comment matches expected value.
	 */
	public void test_TCM__String_getText() {
		Comment theComment = new org.jdom.Comment("this is a comment");

		assertEquals(
				"incorrect Comment constructed", 
				"this is a comment", 
				theComment.getText()); 


	}
/**
 * check for the expected toString text value of Comment.
 */
public void test_TCM__String_toString() {
    Comment theComment= new org.jdom.Comment("this is a comment");

    assertEquals(
        "incorrect Comment constructed",
        "[Comment: <!--this is a comment-->]",
        theComment.toString());
    try {
        theComment= new org.jdom.Comment(null);
        fail("Comment constructor didn't catch invalid comment string");
    } catch (IllegalDataException e) {
    }

}
}
