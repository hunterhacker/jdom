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

public final class TestNamespace
extends junit.framework.TestCase
{
	/**
	 *  Construct a new instance. 
	 */
	public TestNamespace(String name) {
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
		TestSuite suite = new TestSuite(TestNamespace.class);
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
	 * Test the prefix, uri version of getNamespace.
	 */
	public void test_TCM__OrgJdomNamespace_getNamespace_String_String() {
		Namespace ns = Namespace.getNamespace("prefx", "http://some.other.place");
		assert("Incorrect namespace created", ns.toString().equals("[Namespace: prefix \"prefx\" is mapped to URI \"http://some.other.place\"]"));

	}

	/**
	 * Test that toString() operates according to JDOM specs
	 */
	public void test_TCM__String_toString() {
		Namespace ns = Namespace.getNamespace("http://some.new.place");
		assert("Incorrect namespace created", ns.toString().equals("[Namespace: prefix \"\" is mapped to URI \"http://some.new.place\"]"));
		//the is really the default NO_NAMESPACE version
		Namespace ns2 = Namespace.getNamespace("");
		assert("Incorrect no namespace namespace created", ns2.toString().equals("[Namespace: prefix \"\" is mapped to URI \"\"]"));
		ns2 = Namespace.getNamespace("prefx","http://foo");
		assert("Incorrect namespace created", ns2.toString().equals("[Namespace: prefix \"prefx\" is mapped to URI \"http://foo\"]"));

	}

	/**
	 * Test than a namespace returns the correct URI
	 */
	public void test_TCM__String_getURI() {
		Namespace ns = Namespace.getNamespace("prefx","http://foo");
		assert("Incorrect namespace prefix", ns.getURI().equals("http://foo"));

	}

	/**
	 * Test the URI only Namespace.
	 */
	public void test_TCM__OrgJdomNamespace_getNamespace_String() {
		Namespace ns = Namespace.getNamespace("http://some.new.place");
		assert("Incorrect namespace created", ns.toString().equals("[Namespace: prefix \"\" is mapped to URI \"http://some.new.place\"]"));
		//the is really the default NO_NAMESPACE version
		Namespace ns2 = Namespace.getNamespace("");
		assert("Incorrect no namespace namespace created", ns2.toString().equals("[Namespace: prefix \"\" is mapped to URI \"\"]"));

	}

	/**
	 * Test retrieval of a namespace with a prefix and Element.
	 */
	public void test_TCM__OrgJdomNamespace_getNamespace_String_OrgJdomElement() {
		Namespace ns = Namespace.getNamespace("prefx", "http://some.other.place");
		Element test = new Element("root",ns);
		ns = null;

		ns = Namespace.getNamespace("prefx", test);
		assertEquals("invalid namespace prefix", "prefx", ns.getPrefix());
		assertEquals("invalid namespace URI", "http://some.other.place", ns.getURI());

	}

	/**
	 * Test the object comparison method.
	 */
	public void test_TCM__boolean_equals_Object() {
		Namespace ns = Namespace.getNamespace("prefx", "http://some.other.place");
		Object ob = (Object)ns;
	    assert("object not equal to attribute", ns.equals(ob));

		ns = Namespace.NO_NAMESPACE;
		ob = (Object)ns;
	    assert("object not equal to attribute", ns.equals(ob));

		//ns = Namespace.EMPTY_NAMESPACE;
		//ob = (Object)ns;
	    //assert("object not equal to attribute", ns.equals(ob));
	    
	    
	}

	/**
	 * Test getPrefix()
	 */
	public void test_TCM__String_getPrefix() {
		Namespace ns = Namespace.getNamespace("prefx","http://foo");
		assert("Incorrect namespace prefix", ns.getPrefix().equals("prefx"));

		//ns = Namespace.EMPTY_NAMESPACE;
		//assert("Incorrect empty namespace prefix", ns.getPrefix().equals(""));

		ns = Namespace.NO_NAMESPACE;
		assert("Incorrect empty namespace prefix", ns.getPrefix().equals(""));

	}

	/**
	 * Verify that a namespace will produce a hashcode.
	 */
	public void test_TCM__int_hashCode() {
		Namespace ns = Namespace.getNamespace("test", "value");
		//only an exception would be a problem
		int i = ns.hashCode();
		assert("bad hashCode", true);

		//make sure a new one doesn't have the same value
		Namespace ns2 = Namespace.getNamespace("test", "value2");
		//only an exception would be a problem
		int x = ns2.hashCode();
		assert("duplicate hashCode", i!=x );

		//test hashcode for NO_NAMESPACE
		int y = Namespace.NO_NAMESPACE.hashCode();
		//only an exception would be a problem
		assert("bad hashcode" , true);

		//test hashcode for NO_NAMESPACE
		//y = Namespace.EMPTY_NAMESPACE.hashCode();
		//only an exception would be a problem
		//assert("bad hashcode" , true);

			
		
	}

}
