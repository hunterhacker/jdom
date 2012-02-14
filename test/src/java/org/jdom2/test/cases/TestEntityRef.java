package org.jdom2.test.cases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jdom2.Element;
import org.jdom2.EntityRef;
import org.jdom2.IllegalDataException;
import org.jdom2.IllegalNameException;
import org.jdom2.test.util.UnitTestUtil;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class TestEntityRef {

	@Test
	public void testEntityRef() {
		EntityRef er = new EntityRef() {
			// nothing
    		private static final long serialVersionUID = 200L;
		};
		assertTrue(null == er.getPublicID());
		assertTrue(null == er.getSystemID());
		assertTrue(null == er.getName());
	}
	
	@Test
	public void testEntityRefString() {
		EntityRef er = new EntityRef("name");
		assertTrue("name".equals(er.getName()));
		assertTrue(null == er.getSystemID());
		assertTrue(null == er.getPublicID());
		assertTrue(null == er.getParent());
		assertTrue(null == er.getParentElement());
		assertTrue(null == er.getDocument());
		assertTrue(null != er.toString());
	}

	@Test
	public void testEntityRefStringString() {
		EntityRef er = new EntityRef("name", "systemid");
		assertTrue("name".equals(er.getName()));
		assertTrue("systemid".equals(er.getSystemID()));
		assertTrue(null == er.getPublicID());
		assertTrue(null == er.getParent());
		assertTrue(null == er.getParentElement());
		assertTrue(null == er.getDocument());
		assertTrue(null != er.toString());
	}

	@Test
	public void testEntityRefStringStringString() {
		EntityRef er = new EntityRef("name", "publicid", "systemid");
		assertTrue("name".equals(er.getName()));
		assertTrue("systemid".equals(er.getSystemID()));
		assertTrue("publicid".equals(er.getPublicID()));
		assertTrue(null == er.getParent());
		assertTrue(null == er.getParentElement());
		assertTrue(null == er.getDocument());
		assertTrue(null != er.toString());
	}

	@Test
	public void testGetValue() {
		assertTrue("".equals(new EntityRef("name").getValue()));
		assertTrue("".equals(new EntityRef("name", "systemid").getValue()));
		assertTrue("".equals(new EntityRef("name", "publicid", "systemid").getValue()));
	}

	@Test
	public void testSetName() {
		EntityRef er = new EntityRef("name", "publicid", "systemid");
		assertTrue("name".equals(er.getName()));
		assertTrue("systemid".equals(er.getSystemID()));
		assertTrue("publicid".equals(er.getPublicID()));
		assertTrue(er == er.setName("myname"));
		assertTrue("myname".equals(er.getName()));
		assertTrue("systemid".equals(er.getSystemID()));
		assertTrue("publicid".equals(er.getPublicID()));
	}

	@Test
	public void testSetPublicID() {
		EntityRef er = new EntityRef("name", "publicid", "systemid");
		assertTrue("name".equals(er.getName()));
		assertTrue("systemid".equals(er.getSystemID()));
		assertTrue("publicid".equals(er.getPublicID()));
		assertTrue(er == er.setPublicID("mypublicid"));
		assertTrue("name".equals(er.getName()));
		assertTrue("systemid".equals(er.getSystemID()));
		assertTrue("mypublicid".equals(er.getPublicID()));
	}

	@Test
	public void testSetSystemID() {
		EntityRef er = new EntityRef("name", "publicid", "systemid");
		assertTrue("name".equals(er.getName()));
		assertTrue("systemid".equals(er.getSystemID()));
		assertTrue("publicid".equals(er.getPublicID()));
		assertTrue(er == er.setSystemID("mysystemid"));
		assertTrue("name".equals(er.getName()));
		assertTrue("mysystemid".equals(er.getSystemID()));
		assertTrue("publicid".equals(er.getPublicID()));
	}

	@Test
	public void testToString() {
		EntityRef er = new EntityRef("name", "publicid", "systemid");
		assertTrue("name".equals(er.getName()));
		assertTrue("systemid".equals(er.getSystemID()));
		assertTrue("publicid".equals(er.getPublicID()));
		assertTrue(er.toString() != null);
	}
	
	@Test
	public void setIllegals() {
		EntityRef er = new EntityRef("name", "publicid", "systemid");

		try {
			er.setName("1234");
			fail("Should throw Exception");
		} catch (IllegalNameException ine) {
			// good
		} catch (Exception e) {
			fail("Expeced IllegalNameException, but got " + e.getClass().getName());
		}
	
		try {
			er.setPublicID("1!2!" + (char)0x0c + "3!4");
			fail("Should throw Exception");
		} catch (IllegalDataException ine) {
			// good
		} catch (Exception e) {
			fail("Expeced IllegalNameException, but got " + e.getClass().getName());
		}

		try {
			er.setSystemID("12" + (char)0x0c + "34");
			UnitTestUtil.failNoException(IllegalDataException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(IllegalDataException.class, e);
		}
	}

    @Test
	public void testCloneDetatchParentEntityRef() {
		Element parent = new Element("root");
		EntityRef content = new EntityRef("val");
		parent.addContent(content);
		EntityRef clone = content.detach().clone();
		assertEquals(content.getValue(), clone.getValue());
		assertNull(content.getParent());
		assertNull(clone.getParent());
	}

}
