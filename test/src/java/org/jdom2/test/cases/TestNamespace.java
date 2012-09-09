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
 * @author unascribed
 * @version 0.1
 */
import javax.xml.XMLConstants;

import org.jdom2.*;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import static org.junit.Assert.*;

@SuppressWarnings("javadoc")
public final class TestNamespace {

	/**
	 * The main method runs all the tests in the text ui
	 */
	public static void main (String args[]) 
	{
		JUnitCore.runClasses(TestNamespace.class);
	}

	/**
	 * Test the object comparison method.
	 */
	@Test
	public void test_TCM__boolean_equals_Object() {
		Namespace ns = Namespace.getNamespace("prefx", "http://some.other.place");
		Object ob = ns;
		assertTrue("object not equal to attribute", ns.equals(ob));

		ns = Namespace.NO_NAMESPACE;
		ob = ns;
		assertTrue("object not equal to attribute", ns.equals(ob));

		//ns = Namespace.EMPTY_NAMESPACE;
		//ob = (Object)ns;
		//assertTrue("object not equal to attribute", ns.equals(ob));


	}

	/**
	 * Verify that a namespace will produce a hashcode.
	 */
	@Test
	public void test_TCM__int_hashCode() {
		Namespace ns = Namespace.getNamespace("test", "value");
		//only an exception would be a problem
		int i = -1;
		try {
			i = ns.hashCode();
		}
		catch(Exception e) {
			fail("bad hashCode");
		}

		//make sure a new one doesn't have the same value
		Namespace ns2 = Namespace.getNamespace("test", "value2");
		int x = ns2.hashCode();
		assertTrue("duplicate hashCode", i!=x );

		//test hashcode for NO_NAMESPACE
		//only an exception would be a problem
		try {
			// Namespace.hashCode() is uri.hashCode().
			// NO_NAMESPACE has URI ""
			assertTrue(Namespace.NO_NAMESPACE.hashCode() == "".hashCode());
		}
		catch(Exception e) {
			fail("bad hashCode");
		}

		//test hashcode for NO_NAMESPACE
		//y = Namespace.EMPTY_NAMESPACE.hashCode();
		//only an exception would be a problem
		//assertTrue("bad hashcode" , true);
	}

	/**
	 * Test the URI only Namespace.
	 */
	@Test
	public void test_TCM__OrgJdomNamespace_getNamespace_String() {
		Namespace ns = Namespace.getNamespace("http://some.new.place");
		assertTrue("Incorrect namespace created", ns.toString().equals("[Namespace: prefix \"\" is mapped to URI \"http://some.new.place\"]"));
		//the is really the default NO_NAMESPACE version
		Namespace ns2 = Namespace.getNamespace("");
		assertTrue("Incorrect no namespace namespace created", ns2.toString().equals("[Namespace: prefix \"\" is mapped to URI \"\"]"));

	}

	/**
	 * Test the prefix, uri version of getNamespace.
	 */
	@Test
	public void test_TCM__OrgJdomNamespace_getNamespace_String_String() {
		Namespace ns = Namespace.getNamespace("prefx", "http://some.other.place");
		assertTrue("Incorrect namespace created", ns.toString().equals("[Namespace: prefix \"prefx\" is mapped to URI \"http://some.other.place\"]"));

		assertTrue(Namespace.NO_NAMESPACE == Namespace.getNamespace(""));
		assertTrue(Namespace.NO_NAMESPACE == Namespace.getNamespace(null));
		assertTrue(Namespace.NO_NAMESPACE == Namespace.getNamespace(null, null));
		assertTrue(Namespace.NO_NAMESPACE == Namespace.getNamespace(null, ""));
		assertTrue(Namespace.NO_NAMESPACE == Namespace.getNamespace("", ""));
		assertTrue(Namespace.NO_NAMESPACE == Namespace.getNamespace("", null));
		
		assertTrue(Namespace.XML_NAMESPACE == Namespace.getNamespace(
				XMLConstants.XML_NS_PREFIX, XMLConstants.XML_NS_URI));
		
		try {
			assertTrue(null != Namespace.getNamespace("xml", "myuri"));
			fail("Should not be able to re-brand Namespace prefix 'xml'");
		} catch (IllegalNameException ine) {
			// good
		} catch (Exception e) {
			fail("expected IllegalNameException, not " + e.getClass().getName());
		}
		
		try {
			assertTrue(null != Namespace.getNamespace("pfx", XMLConstants.XML_NS_URI));
			fail("Should not be able to re-brand xml namespace URI to another prefix too.");
		} catch (IllegalNameException ine) {
			// good
		} catch (Exception e) {
			fail("expected IllegalNameException, not " + e.getClass().getName());
		}
		
		try {
			assertTrue(null != Namespace.getNamespace("pfx", "-this is illegal..."));
			fail("Should not be able to have a URI starting with '-'.");
		} catch (IllegalNameException ine) {
			// good
		} catch (Exception e) {
			fail("expected IllegalNameException, not " + e.getClass().getName());
		}
		
		try {
			assertTrue(null != Namespace.getNamespace("p:x", "myuri"));
			fail("Should not be able to create Namespace with illegal prefix 'p:x'");
		} catch (IllegalNameException ine) {
			// good
		} catch (Exception e) {
			fail("expected IllegalNameException, not " + e.getClass().getName());
		}
		
		try {
			assertTrue(null != Namespace.getNamespace("pfx", " "));
			fail("Should not be able to create Namespace with no URI");
		} catch (IllegalNameException ine) {
			// good
		} catch (Exception e) {
			fail("expected IllegalNameException, not " + e.getClass().getName());
		}
		
		try {
			assertTrue(null != Namespace.getNamespace("pfx", null));
			fail("Should not be able to create Namespace with no URI");
		} catch (IllegalNameException ine) {
			// good
		} catch (Exception e) {
			fail("expected IllegalNameException, not " + e.getClass().getName());
		}
		
	}

	/**
	 * Test getPrefix()
	 */
	@Test
	public void test_TCM__String_getPrefix() {
		Namespace ns = Namespace.getNamespace("prefx","http://foo");
		assertTrue("Incorrect namespace prefix", ns.getPrefix().equals("prefx"));

		//ns = Namespace.EMPTY_NAMESPACE;
		//assertTrue("Incorrect empty namespace prefix", ns.getPrefix().equals(""));

		ns = Namespace.NO_NAMESPACE;
		assertTrue("Incorrect empty namespace prefix", ns.getPrefix().equals(""));

	}

	/**
	 * Test than a namespace returns the correct URI
	 */
	@Test
	public void test_TCM__String_getURI() {
		Namespace ns = Namespace.getNamespace("prefx","http://foo");
		assertTrue("Incorrect namespace prefix", ns.getURI().equals("http://foo"));

	}

	/**
	 * Test that toString() operates according to JDOM specs
	 */
	@Test
	public void test_TCM__String_toString() {
		Namespace ns = Namespace.getNamespace("http://some.new.place");
		assertTrue("Incorrect namespace created", ns.toString().equals("[Namespace: prefix \"\" is mapped to URI \"http://some.new.place\"]"));
		//the is really the default NO_NAMESPACE version
		Namespace ns2 = Namespace.getNamespace("");
		assertTrue("Incorrect no namespace namespace created", ns2.toString().equals("[Namespace: prefix \"\" is mapped to URI \"\"]"));
		ns2 = Namespace.getNamespace("prefx","http://foo");
		assertTrue("Incorrect namespace created", ns2.toString().equals("[Namespace: prefix \"prefx\" is mapped to URI \"http://foo\"]"));

	}
	
	@Test
	public void testXMLNamespacePrefix() {
		try {
			Namespace.getNamespace("xml", "not right");
			fail("Should not be able to redefine 'xml' prefix.");
		} catch (IllegalNameException ine) {
			// good
		} catch (Exception e) {
			e.printStackTrace();
			fail("We expect IllegalNameException not " + e.getClass());
		}
	}
	
}
