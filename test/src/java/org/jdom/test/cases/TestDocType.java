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

public final class TestDocType
extends junit.framework.TestCase
{
	/**
	 *  Construct a new instance. 
	 */
	public TestDocType(String name) {
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
		TestSuite suite = new TestSuite(TestDocType.class);
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
	 * Test a simple DocType with a name.
	 */
	public void test_TCC___String() {
		DocType theDocType = new DocType("anElement");

		assertEquals("incorrect element name", "anElement", theDocType.getElementName());
	}

	/**
	 * test both the setting of the element name and systemID.
	 */
	public void test_TCC___String_String() {
		String systemID = "FILE://temp/test.dtd";
		DocType theDocType = new DocType("anElement", systemID);

		assertEquals("incorrect element name", "anElement", theDocType.getElementName());
		assertEquals("incorrect system ID", systemID, theDocType.getSystemID());
	}

	/**
	 * test with element name, public and systemIDs.
	 */
	public void test_TCC___String_String_String() {
		String publicID = "-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN";
		String systemID = "FILE://temp/test.dtd";
		DocType theDocType = new DocType("anElement", publicID, systemID);

		assertEquals("incorrect element name", "anElement", theDocType.getElementName());
		assertEquals("incorrect public ID", publicID, theDocType.getPublicID());
		assertEquals("incorrect system ID", systemID, theDocType.getSystemID());

	}

	/**
	 * use toString to test the clone.
	 */
	public void test_TCM__Object_clone() {
		String publicID = "-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN";
		String systemID = "FILE://temp/test.dtd";
		DocType theDocType = new DocType("anElement", publicID, systemID);
		
		DocType theDocType2 = (DocType)theDocType.clone();
		
		//assuming toString works as advertised....
		
		assertEquals(theDocType.toString(), theDocType2.toString());
	}

	/**
	 * Test toString returns the expected string.
	 */
	public void test_TCM__String_toString() {
		String publicID = "-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN";
		String systemID = "FILE://temp/test.dtd";
		DocType theDocType = new DocType("anElement", publicID, systemID);

		String result = theDocType.toString();
		String compareTo =
		"[DocType: <!DOCTYPE anElement PUBLIC \"-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN\"" +
		" \"FILE://temp/test.dtd\">]";

		assertEquals("incorrect toString form", result, compareTo);
	}

	/**
	 * Test the setter for publicID.
	 */
	public void test_TCM__OrgJdomDocType_setPublicID_String() {
		String publicID = "-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN";
		
		DocType theDocType = new DocType("anElement");

		theDocType.setPublicID(publicID);

		assertEquals(publicID, theDocType.getPublicID());
	}

	/**
	 * Test that getSystemID returns the same value as set in the constructor.
	 */
	public void test_TCM__String_getSystemID() {
		String systemID = "FILE://temp/doodah.dtd";
		
		DocType theDocType = new DocType("anElement", systemID);

		assertEquals(systemID, theDocType.getSystemID());
	}

	/**
	 * Test that getSerializedForm works as expected.
	 */
	public void test_TCM__String_getSerializedForm() {
		String publicID = "-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN";
		String systemID = "FILE://temp/test.dtd";
		DocType theDocType = new DocType("anElement", publicID, systemID);

		String result = theDocType.getSerializedForm();
		String compareTo =
		"<!DOCTYPE anElement PUBLIC \"-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN\"" +
		" \"FILE://temp/test.dtd\">";

		assertEquals("incorrect serialized form", result, compareTo);
	}

	/**
	 * Test that getPublicID matches the value from the constructor.
	 */
	public void test_TCM__String_getPublicID() {
		String publicID = "-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN";
		
		DocType theDocType = new DocType("anElement", publicID, "");

		assertEquals(publicID, theDocType.getPublicID());
	}

	/**
	 * Test getElementName.
	 */
	public void test_TCM__String_getElementName() {
		DocType theDocType = new DocType("anElement");

		assertEquals("incorrect element name", "anElement", theDocType.getElementName());
	}

	/**
	 * Do an object comparison with itself to confirm boolean equals works.
	 */
	public void test_TCM__boolean_equals_Object() {
		String publicID = "-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN";
		String systemID = "FILE://temp/test.dtd";
		DocType theDocType = new DocType("anElement", publicID, systemID);
		
		Object ob = (Object)theDocType;
		assertEquals(theDocType, ob);
	}

	/**
	 * Test the setter for SystemID
	 */
	public void test_TCM__OrgJdomDocType_setSystemID_String() {
		String systemID = "FILE://temp/doodah.dtd";
		
		DocType theDocType = new DocType("anElement");

		theDocType.setSystemID(systemID);

		assertEquals(systemID, theDocType.getSystemID());
	}

	/**
	 * look for a integer hashCode
	 */
	public void test_TCM__int_hashCode() {
		String publicID = "-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN";
		String systemID = "FILE://temp/test.dtd";
		DocType theDocType = new DocType("anElement", publicID, systemID);
		int i = theDocType.hashCode();
		// assuming no exception was thrown, an integer was created.
	}

}
