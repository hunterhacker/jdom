package org.jdom2.test.cases;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.jdom2.Content;
import org.jdom2.Element;
import org.jdom2.IllegalDataException;
import org.jdom2.Text;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class TestText {

	@Test
	public void testText() {
		Text txt = new Text() {
			// nothing
    		private static final long serialVersionUID = 200L;
		};
		assertTrue(txt.getValue() == null);
		assertTrue(txt.getText() == null);
	}

	@Test
	public void testTextString() {
		Text txt = new Text("  frodo  baggins  ");
		assertTrue("  frodo  baggins  ".equals(txt.getValue()));
		assertTrue("  frodo  baggins  ".equals(txt.getText()));
		assertTrue("frodo baggins".equals(txt.getTextNormalize()));
		assertTrue("frodo  baggins".equals(txt.getTextTrim()));
	}
	
	@Test
	public void testNormalizeString() {
		assertEquals("", Text.normalizeString(null));
		assertEquals("", Text.normalizeString(""));
		assertEquals("boo", Text.normalizeString("boo"));
		assertEquals("boo", Text.normalizeString(" boo"));
		assertEquals("boo", Text.normalizeString("boo "));
		assertEquals("boo", Text.normalizeString("  boo  "));
		assertEquals("boo hoo", Text.normalizeString("boo hoo"));
		assertEquals("boo hoo", Text.normalizeString("boo\nhoo"));
		assertEquals("boo hoo", Text.normalizeString("boo \n hoo"));
		assertEquals("boo hoo", Text.normalizeString("boo \n hoo"));
		assertEquals("boo hoo", Text.normalizeString("\rboo\thoo\n"));
	}

	@Test
	public void testClone() {
		Text txt = new Text("frodo baggins");
		Text clone = txt.clone();
		assertTrue(clone != null);
		assertTrue(txt != clone);
		assertFalse(txt.equals(clone));
		assertFalse(clone.equals(txt));
		assertTrue("frodo baggins".equals(clone.getText()));
	}

	@Test
	public void testSetText() {
		Text txt = new Text("frodo baggins");
		assertTrue("frodo baggins".equals(txt.getText()));
		assertTrue(txt.setText("bilbo baggins") == txt);
		assertTrue("bilbo baggins".equals(txt.getText()));
	}

	@Test
	public void testAppendString() {
		Text txt = new Text("frodo baggins");
		assertTrue("frodo baggins".equals(txt.getText()));
		txt.append(" from the shire");
		assertTrue("frodo baggins from the shire".equals(txt.getText()));
		String app = null;
		txt.append(app);
		assertTrue("frodo baggins from the shire".equals(txt.getText()));
		txt.append("");
		assertTrue("frodo baggins from the shire".equals(txt.getText()));
		try {
			txt.append("New char data " + (char)0x05 + " with bad characters.");
		} catch (IllegalDataException iae) {
			// good
		} catch (Exception e) {
			fail ("Expected IllegalAddException, but got " + e.getClass().getName());
		}
	}

	@Test
	public void testAppendText() {
		Text txt = new Text("frodo baggins");
		assertTrue("frodo baggins".equals(txt.getText()));
		txt.append(new Text(" from the shire"));
		assertTrue("frodo baggins from the shire".equals(txt.getText()));
		Text app = null;
		txt.append(app);
		assertTrue("frodo baggins from the shire".equals(txt.getText()));
	}

	@Test
	public void testToString() {
		Text txt = new Text("frodo baggins");
		assertTrue(txt.toString() != null);
	}

    @Test
	public void testCloneDetatchParentText() {
		Element parent = new Element("root");
		Text content = new Text("val");
		parent.addContent(content);
		Text clone = content.detach().clone();
		assertEquals(content.getValue(), clone.getValue());
		assertNull(content.getParent());
		assertNull(clone.getParent());
	}

    @Test
    public void testContentCType() {
    	assertTrue(Content.CType.Text == new Text("").getCType());
    }
}
