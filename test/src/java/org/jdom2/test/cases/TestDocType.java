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
import org.jdom2.*;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import static org.junit.Assert.*;

@SuppressWarnings("javadoc")
public final class TestDocType {
    /**
     * The main method runs all the tests in the text ui
     */
    public static void main (String args[]) 
     {
        JUnitCore.runClasses(TestDocType.class);
    }


	/**
	 * Test a simple DocType with a name.
	 */
    @Test
	public void test_TCC() {
		DocType theDocType = new DocType() {
			// change nothing
    		private static final long serialVersionUID = 200L;
		};

		assertNull("incorrect element name", theDocType.getElementName());
	}
	
	/**
	 * Test a simple DocType with a name.
	 */
    @Test
	public void test_TCC___String() {
		DocType theDocType = new DocType("anElement");

		assertEquals("incorrect element name", "anElement", theDocType.getElementName());
	}
	
	/**
	 * test both the setting of the element name and systemID.
	 */
    @Test
	public void test_TCC___String_String() {
		String systemID = "FILE://temp/test.dtd";
		DocType theDocType = new DocType("anElement", systemID);

		assertEquals("incorrect element name", "anElement", theDocType.getElementName());
		assertEquals("incorrect system ID", systemID, theDocType.getSystemID());
	}

    /**
	 * test with element name, public and systemIDs.
	 */
    @Test
	public void test_TCC___String_String_String() {
		String publicID = "-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN";
		String systemID = "FILE://temp/test.dtd";
		DocType theDocType = new DocType("anElement", publicID, systemID);

		assertEquals("incorrect element name", "anElement", theDocType.getElementName());
		assertEquals("incorrect public ID", publicID, theDocType.getPublicID());
		assertEquals("incorrect system ID", systemID, theDocType.getSystemID());

	}

    /**
	 * Do an object comparison with itself to confirm boolean equals works.
	 */
    @Test
	public void test_TCM__boolean_equals_Object() {
		String publicID = "-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN";
		String systemID = "FILE://temp/test.dtd";
		DocType theDocType = new DocType("anElement", publicID, systemID);
		
		Object ob = theDocType;
		assertEquals(theDocType, ob);
	}

    /**
	 * look for a integer hashCode
	 */
    @Test
	public void test_TCM__int_hashCode() {
		String publicID = "-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN";
		String systemID = "FILE://temp/test.dtd";
		DocType theDocType = new DocType("anElement", publicID, systemID);
		assertTrue(theDocType.hashCode() == theDocType.hashCode());
		// assuming no exception was thrown, an integer was created.
	}

    /**
	 * use toString to test the clone.
	 */
    @Test
	public void test_TCM__Object_clone() {
		String publicID = "-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN";
		String systemID = "FILE://temp/test.dtd";
		DocType theDocType = new DocType("anElement", publicID, systemID);
		
		DocType theDocType2 = theDocType.clone();
		
		//assuming toString works as advertised....
		
		assertEquals(theDocType.toString(), theDocType2.toString());
	}
    
	/**
	 * Test the setter for publicID.
	 */
    @Test
	public void test_TCM__OrgJdomDocType_setPublicID_String() {
		String publicID = "-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN";
		
		DocType theDocType = new DocType("anElement");

		theDocType.setPublicID(publicID);

		assertEquals(publicID, theDocType.getPublicID());
	}

    /**
	 * Test the setter for SystemID
	 */
    @Test
	public void test_TCM__OrgJdomDocType_setSystemID_String() {
		String systemID = "FILE://temp/doodah.dtd";
		
		DocType theDocType = new DocType("anElement");

		theDocType.setSystemID(systemID);

		assertEquals(systemID, theDocType.getSystemID());
	}

    /**
	 * Test getElementName.
	 */
    @Test
	public void test_TCM__String_getElementName() {
		DocType theDocType = new DocType("anElement");

		assertEquals("incorrect element name", "anElement", theDocType.getElementName());
	}

    /**
	 * Test that getPublicID matches the value from the constructor.
	 */
    @Test
	public void test_TCM__String_getPublicID() {
		String publicID = "-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN";
		
		DocType theDocType = new DocType("anElement", publicID, "");

		assertEquals(publicID, theDocType.getPublicID());
		assertTrue("".equals(theDocType.getValue()));
	}

    /**
	 * Test that getSerializedForm works as expected.
	 */
    @Test
	public void test_TCM__String_getSerializedForm() {
		/** No op because the method is deprecated
		String publicID = "-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN";
		String systemID = "FILE://temp/test.dtd";
		DocType theDocType = new DocType("anElement", publicID, systemID);

		String result = theDocType.getSerializedForm();
		String compareTo =
		"<!DOCTYPE anElement PUBLIC \"-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN\"" +
		" \"FILE://temp/test.dtd\">";

		assertEquals("incorrect serialized form", result, compareTo);
		*/
	}

    /**
	 * Test that getSystemID returns the same value as set in the constructor.
	 */
    @Test
	public void test_TCM__String_getSystemID() {
		String systemID = "FILE://temp/doodah.dtd";
		DocType theDocType = new DocType("anElement", systemID);

		assertEquals(systemID, theDocType.getSystemID());
		assertTrue("".equals(theDocType.getValue()));
	}

    /**
	 * Test toString returns the expected string.
	 */
    @Test
	public void test_TCM__String_toString() {
		String publicID = "-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN";
		String systemID = "FILE://temp/test.dtd";
		DocType theDocType = new DocType("anElement", publicID, systemID);
		String result = theDocType.toString();
		String compareTo =
		"[DocType: <!DOCTYPE anElement PUBLIC \"-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN\"" +
		" \"FILE://temp/test.dtd\">]";

		assertEquals("incorrect toString form", compareTo, result);
		assertTrue("".equals(theDocType.getValue()));
	}
    
    @Test (expected=IllegalNameException.class)
    public void testIllegalNameA() {
    	new DocType("f0$0");
    }
    
    @Test (expected=IllegalNameException.class)
    public void testIllegalNameB() {
    	new DocType("f0$0", "systemid");
    }
    
    @Test (expected=IllegalNameException.class)
    public void testIllegalNameC() {
    	new DocType("f0$0", "publicid", "systemid");
    }
    
    @Test (expected=IllegalNameException.class)
    public void testIllegalNameD() {
    	DocType dt = new DocType("foo", "publicid", "systemid");
    	dt.setElementName("f0$0");
    }
    
    @Test (expected=IllegalDataException.class)
    public void testIllegalPublicA() {
    	new DocType("foo", "pub~id", null);
    }
    
    @Test (expected=IllegalDataException.class)
    public void testIllegalPublicB() {
    	DocType dt = new DocType("foo", "pubid", null);
    	dt.setPublicID("pub~id");
    }
    
    @Test (expected=IllegalDataException.class)
    public void testIllegalSystemA() {
    	char illegal = 0x0B;
    	new DocType("foo", "pubid", "sys" + illegal + "id");
    }
    
    @Test (expected=IllegalDataException.class)
    public void testIllegalSystemB() {
    	DocType dt = new DocType("foo", "pubid", "sysid");
    	char illegal = 0x0B;
    	dt.setSystemID("sys" + illegal + "id");
    }
    
    @Test (expected=IllegalDataException.class)
    public void testIllegalSystemC() {
    	DocType dt = new DocType("foo", "pubid", "sysid");
    	dt.setSystemID("sys'id \" with quote");
    }
    
    @Test
	public void testCloneDetatchParentDocType() {
		Document parent = new Document();
		DocType content = new DocType("val");
		parent.addContent(content);
		DocType clone = content.detach().clone();
		assertEquals(content.getValue(), clone.getValue());
		assertNull(content.getParent());
		assertNull(clone.getParent());
	}

    @Test
    public void testContentCType() {
    	assertTrue(Content.CType.DocType == new DocType("root").getCType());
    }
}
