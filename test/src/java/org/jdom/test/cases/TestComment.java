/* Please run replic.pl on me ! */
package org.jdom.test.cases;

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
	 * This method is called before a test is executed.
	 */
	public void setUp() {
		// your code goes here.
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
		TestSuite suite = new TestSuite(TestComment.class);
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

			assert(true);
		}
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

		assert(true);
	}

}

	/**
	 * Match the XML fragment this comment produces.
	 */
	public void test_TCM__String_getSerializedForm() {
		Comment theComment = new org.jdom.Comment("this is a comment");

		assertEquals(
				"incorrect Comment constructed", 
				"<!--this is a comment-->", 
				theComment.getSerializedForm()); 

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

		assert(true);
	}
	try {
		char c= 0x11;
		StringBuffer b= new StringBuffer("hhhh");
		b.setCharAt(2, c);
		theComment.setText(b.toString());
		fail("Comment setText didn't catch invalid comment string");
	} catch (IllegalDataException e) {

		assert(true);
	}

}

	/**
	 * Verify a simple object == object test
	 */
	public void test_TCM__boolean_equals_Object() {
	    Comment com = new Comment("test");

	    Object ob = (Object)com;

	    assert("object not equal to comment", com.equals(ob));
	}

	/**
	 * Test that a real hashcode is returned and that a different one is returned
	 * for a different comment.
	 */
	public void test_TCM__int_hashCode() {
	//not sure what to test!

		Comment com = new Comment("test");
		//only an exception would be a problem
		int i = com.hashCode();
		assert("bad hashCode", true);
		Comment com2 = new Comment("test");
		//different comments, same text
		int x = com2.hashCode();
		assert("Different comments with same value have same hashcode", x != i);
		Comment com3 = new Comment("test2");
		//only an exception would be a problem
		int y = com3.hashCode();
		assert("Different comments have same hashcode", y != x);
	}

}
