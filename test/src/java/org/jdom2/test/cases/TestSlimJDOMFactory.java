package org.jdom2.test.cases;

import static org.junit.Assert.*;
import org.junit.Test;

import org.jdom2.JDOMFactory;
import org.jdom2.SlimJDOMFactory;
import org.jdom2.Text;

@SuppressWarnings("javadoc")
public class TestSlimJDOMFactory extends AbstractTestJDOMFactory {

	/**
	 * @param located
	 */
	public TestSlimJDOMFactory() {
		super(false);
	}

	@Override
	protected JDOMFactory buildFactory() {
		return new SlimJDOMFactory();
	}

	@Test
	public void testCaching() {
		SlimJDOMFactory fac = new SlimJDOMFactory();
		Text ta = fac.text("hi");
		String hi = ta.getText();
		// we expect the StringBin to compact a string value... should no longer
		// be the intern value.
		assertTrue("hi" != hi);
		assertTrue("hi" == hi.intern());
		
		Text tb = fac.text("hi");
		assertTrue("hi" != tb.getText());
		assertTrue(hi == tb.getText());
		
		fac.clearCache();
		
		Text tc = fac.text("hi");
		assertTrue("hi" != tc.getText());
		assertTrue(hi != tc.getText());
		
		assertTrue(hi.equals(tc.getText()));
	}
}
