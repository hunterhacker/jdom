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
 * Test the setText method of Comment.
 * setText should replace the value of the Comment after checking for valid
 * data.
 *
 * I don't understand why the constructor checks for valid data and not setText()
 * 
 * @author Philip Nelson
 * @version 0.1
 */
import org.jdom.*;

public final class TCM_Comment_void_setText_String
extends junit.framework.TestCase
{
	/**
	 *  Construct a new instance. 
	 */
	public TCM_Comment_void_setText_String() {
		super("public void org.jdom.Comment.setText(java.lang.String)");
	}
	/**
	 *  Construct a new instance. 
	 */
	public TCM_Comment_void_setText_String(String name) {
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
	 * Test setting and resetting the text value of this Comment.
	 */
public void test() {
	Comment theComment = new org.jdom.Comment("this is a comment");

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
	
	/*Not sure why the constructor tests for invalid data but setText doesn't
	
	try {
		theComment.setText(null);
		fail("Comment constructor didn't catch invalid comment string");
	} catch (IllegalDataException e) {
	
		assert(true);
	}
	*/

}
}
