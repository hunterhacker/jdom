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

package org.jdom.test.cases;

/**
 * Please put a description of your test here.
 * 
 * @author unascribed
 * @version 0.1
 */
import junit.framework.*;

import org.jdom.*;

public final class TestAttribute
extends junit.framework.TestCase
{
	/**
	 *  Construct a new instance. 
	 */
	public TestAttribute(String name) {
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
		TestSuite suite = new TestSuite(TestAttribute.class);
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
	 * Test the simple case of constructing an attribute without
	 * namespace or prefix
	 */
	public void test_TCC___String_String() {
		Attribute attr = new Attribute("test", "value");
		assert("incorrect attribute name", attr.getName().equals("test"));
		assert("incoorect attribute value", attr.getValue().equals("value"));
		//should have been put in the NO_NAMESPACE namespace
		assert("incorrect namespace", attr.getNamespace().equals(Namespace.NO_NAMESPACE));


		try {
			attr = new Attribute(null, "value");
			assert("didn't catch null attribute name", false);
		} catch (IllegalArgumentException e) {
			assert(true);
		} catch (NullPointerException e) {
			assert("NullPointerException with null attribute name", false);
		}

		try {
			attr = new Attribute("test", null);
			assert("didn't catch null attribute value", false);
		} catch (IllegalArgumentException e) {
			assert(true);
		} catch (NullPointerException e) {
			assert("NullPointerException with null attribute value", false);
		}

		try {
			attr = new Attribute("test" + (char)0x01, "value");
			assert("didn't catch invalid attribute name", false);
		} catch (IllegalArgumentException e) {
			assert(true);
		}

		try {
			attr = new Attribute("test", "test" + (char)0x01);
			assert("didn't catch invalid attribute value", false);
		} catch (IllegalArgumentException e) {
			assert(true);
		}

	}

	/**
	 * Test the constructor with name, value and namespace
	 */
	public void test_TCC___String_String_OrgJdomNamespace() {
		Namespace ns = Namespace.getNamespace("prefx", "http://some.other.place");

		Attribute attr = new Attribute("test", "value", ns);
		assert("incorrect attribute name", attr.getName().equals("test"));
		assert("incoorect attribute value", attr.getValue().equals("value"));
		assert("incorrect Namespace", attr.getNamespace().equals(ns));

		//now test that the attribute cannot be created with a namespace
		//without a prefix
		ns = Namespace.getNamespace("http://some.other.place");

		try {
			attr = new Attribute("test", "value", ns);
			assert("allowed creation of attribute with a default namespace", false);
		} catch (IllegalNameException e) {
			assert(true);
		}

		
		attr = new Attribute("test", "value", null);
		assert("expected null attribute namespace", true);



	}

	/**
 	 * Test that an Attribute returns it's correct name.
	 */
	public void test_TCM__String_getName() {
		Attribute attr = new Attribute("test", "value");
		assert("incorrect attribute name", attr.getName().equals("test"));

	}

	/**
	 * Test that an Attribute returns the correct Namespace URI.
	 */
	public void test_TCM__String_getNamespaceURI() {
		
		Namespace ns = Namespace.getNamespace("prefx", "http://some.other.place");

		Attribute attr = new Attribute("test", "value", ns);
		assert("incorrect URI", attr.getNamespaceURI().equals("http://some.other.place"));

	}

	/**
	 * Test the convienience method for returning a long from an Attribute
	 */
	public void test_TCM__long_getLongValue() {
		Attribute attr = new Attribute("test", "1000000");
		long longval = 1000000;
		try {
			assert("incorrect long conversion", attr.getLongValue() == longval);
		} catch (DataConversionException e) {
			assert("couldn't convert to long", false);
		}

		//test an invalid long

		attr.setValue("100000000000000000000000000");
		try {
			attr.getLongValue();
			assert("incorrect long conversion from non long", false);
		} catch (DataConversionException e) {
			assert("couldn't convert to long", true);
		}
	}

/**
 * Test floats returned from Attribute values.  Checks that
 * both correctly formatted (java style) and incorrectly formatted
 * float strings are returned or raise a DataConversionException
 */
public void test_TCM__float_getFloatValue() {
	Attribute attr= new Attribute("test", "1.00000009999e+10f");
	float flt= 1.00000009999e+10f;
	try {
		assert("incorrect float conversion", attr.getFloatValue() == flt);
	} catch (DataConversionException e) {
		assert("couldn't convert to float", false);
	}

	//test an invalid float

	attr.setValue("1.00000009999e");
	try {
		attr.getFloatValue();
		assert("incorrect float conversion from non float", false);
	} catch (DataConversionException e) {
		assert("couldn't convert to float", true);
	}

}

	/**
 	 * Tests that Attribute can convert value strings to ints and
 	 * that is raises DataConversionException if it is not an int.
	 */
	public void test_TCM__int_getIntValue() {
		Attribute attr = new Attribute("test", "1000");
		int intval = 1000;
		try {
			assert("incorrect int conversion", attr.getIntValue() == intval); 
		} catch (DataConversionException e) {
			assert("couldn't convert to int", false);
		}

		//test an invalid int

		attr.setValue("10000000.aq");
		try {
			attr.getIntValue();
			assert("incorrect int conversion from non int", false); 
		} catch (DataConversionException e) {
			assert("couldn't convert to int", true);
		}
	}

	/**
 	 * Test that an independantly created Namespace and one
  	 * retrieved from an Attribute create with the same namespace
 	 * parameters are the same namespace.
	 */
	public void test_TCM__OrgJdomNamespace_getNamespace() {
		Namespace ns = Namespace.getNamespace("prefx", "http://some.other.place");

		Attribute attr = new Attribute("test", "value", ns);
		Namespace ns2 = Namespace.getNamespace("prefx", "http://some.other.place");

		assert("incorrect Namespace", attr.getNamespace().equals(ns2));

	}

	/**
	 * Test convience method for getting doubles from an Attribute
	 */
	public void test_TCM__double_getDoubleValue() {
		Attribute attr = new Attribute("test", "11111111111111");
		try {
			assert("incorrect double value", attr.getDoubleValue() == 11111111111111d );

			attr.setValue("0");
			assert("incorrect double value", attr.getDoubleValue() == 0 );

			attr.setValue(Double.toString(java.lang.Double.MAX_VALUE));
			assert("incorrect double value", attr.getDoubleValue() == java.lang.Double.MAX_VALUE);

			attr.setValue(Double.toString(java.lang.Double.MIN_VALUE));
			assert("incorrect double value", attr.getDoubleValue() == java.lang.Double.MIN_VALUE);

		} catch (DataConversionException e) {
			assert("couldn't convert boolean value", false);
		}

		try {
			attr.setValue("foo");
			assert("incorrectly returned double from non double value" + attr.getDoubleValue(), false);

		} catch (DataConversionException e) {
			assert(true);
		}

	}

	/**
 	 * Test that an Attribute returns the correct Namespace prefix.
	 */
	public void test_TCM__String_getNamespacePrefix() {
		
		Namespace ns = Namespace.getNamespace("prefx", "http://some.other.place");

		Attribute attr = new Attribute("test", "value", ns);
		assert("incorrect prefix", attr.getNamespacePrefix().equals("prefx"));
	}

	/**
	 * Tests that an Attribute returns the correct Qualified Name.
	 */
	public void test_TCM__String_getQualifiedName() {

		Namespace ns = Namespace.getNamespace("prefx", "http://some.other.place");

		Attribute attr = new Attribute("test", "value", ns);
		assert("incorrect qualified name", attr.getQualifiedName().equals("prefx:test"));	

	}

	/**
	 * Test a simple object comparison
	 */
	public void test_TCM__boolean_equals_Object() {
		Attribute attr = new Attribute("test", "value");

	    Object ob = (Object)attr;

	    assert("object not equal to attribute", attr.equals(ob));
	}

	/**
 	 * Test that an Attribute can clone itself correctly.  The test
 	 * covers the simple case and with the attribute using a namespace
 	 * and prefix.
	 */
	public void test_TCM__Object_clone() {
		Attribute attr = new Attribute("test", "value");
		Attribute attr2 = (Attribute)attr.clone();

		assert("incorrect name in clone", attr2.getName().equals("test"));
		assert("incorrect value in clone", attr2.getValue().equals("value"));
		Namespace ns = Namespace.getNamespace("prefx", "http://some.other.place");
		attr = new Attribute("test", "value", ns);
		attr2 = (Attribute)attr.clone();
		assert("incorrect name in clone", attr2.getName().equals("test"));
		assert("incorrect value in clone", attr2.getValue().equals("value"));
		assert("incorrect prefix in clone", attr2.getNamespacePrefix().equals("prefx"));
		assert("incorrect qualified name in clone", attr2.getQualifiedName().equals("prefx:test"));
		assert("incorrect Namespace URI in clone", attr2.getNamespaceURI().equals("http://some.other.place"));

	}

	/**
 	 * Test that the toString function works according to the 
 	 * JDOM spec.
	 */
	public void test_TCM__String_toString() {
		//expected value
		Namespace ns = Namespace.getNamespace("prefx", "http://some.other.place");
		Attribute attr = new Attribute("test", "value", ns);
		String str= attr.toString();
		assert("incorrect toString form", str.equals("[Attribute: prefx:test=\"value\"]"));

	}

	/**
	 * test the convienience method getBooleanValue();
	 */
	public void test_TCM__boolean_getBooleanValue() {
		Attribute attr = new Attribute("test", "true");
		try {
			assert("incorrect boolean true value", attr.getBooleanValue());

			attr.setValue("false");
			assert("incorrect boolean false value", !attr.getBooleanValue());

			attr.setValue("TRUE");
			assert("incorrect boolean TRUE value", attr.getBooleanValue());

			attr.setValue("FALSE");
			assert("incorrect boolean FALSE value", !attr.getBooleanValue());

		} catch (DataConversionException e) {
			assert("couldn't convert boolean value", false);
		}

		try {
			attr.setValue("foo");
			assert("incorrectly returned boolean from non boolean value", attr.getBooleanValue());

		} catch (DataConversionException e) {
			assert(true);
		}


	}

	/**
	 * Test that Attribute returns the correct serialized form.
	 */
	public void test_TCM__String_getSerializedForm() {
		Namespace ns = Namespace.getNamespace("prefx", "http://some.other.place");
		Attribute attr = new Attribute("test", "value", ns);
		String serialized = attr.getSerializedForm();
		assert("incorrect serialized form", serialized.equals("prefx:test=\"value\""));

	}

	/**
	 * check that the attribute can return the correct parent element
	 */
	public void test_TCM__OrgJdomElement_getParent() {
		Attribute attr = new Attribute("test", "value");

		Element el = attr.getParent();

		assert("attribute returned parent when there was none", el == null);

		Element root = new Element("root");
		root.addAttribute(attr);
		assert("invalid root element", attr.getParent().equals(root));
	

	}

	/**
	 * Test that an Attribute returns the correct value.
	 */
	public void test_TCM__String_getValue() {

		Namespace ns = Namespace.getNamespace("prefx", "http://some.other.place");
		Attribute attr = new Attribute("test", "value", ns);
		assert("incorrect value", attr.getValue().equals("value"));

	}

/**
 * Test that setting an Attribute's value works correctly.
 */
public void test_TCM__OrgJdomAttribute_setValue_String() {

	Namespace ns = Namespace.getNamespace("prefx", "http://some.other.place");

	Attribute attr=
		new Attribute("test", "value", ns);

	assert("incorrect value before set", attr.getValue().equals("value"));
	attr.setValue("foo");

	assert("incorrect value after set", attr.getValue().equals("foo"));

	//test that the verifier is called
	try {
		attr.setValue(null);
		fail("Attribute setValue didn't catch null  string");
	} catch (IllegalDataException e) {

		assert(true);
	}
	try {
		char c= 0x11;
		StringBuffer b= new StringBuffer("hhhh");
		b.setCharAt(2, c);
		attr.setValue(b.toString());
		fail("Attribute setValue didn't catch invalid comment string");
	} catch (IllegalDataException e) {

		assert(true);
	}

}

	/**
	 * Test that Attribute returns a valid hashcode.
	 */
	public void test_TCM__int_hashCode() {
		Attribute attr = new Attribute("test", "value");
		//only an exception would be a problem
		int i = attr.hashCode();
		assert("bad hashCode", true);
		Attribute attr2 = new Attribute("test", "value");
		//different Attributes, same text
		int x = attr2.hashCode();
		assert("Different Attributes with same value have same hashcode", x != i);
		Attribute attr3 = new Attribute("test2", "value");
		//only an exception would be a problem
		int y = attr3.hashCode();
		assert("Different Attributes have same hashcode", y != x);
	}

}
