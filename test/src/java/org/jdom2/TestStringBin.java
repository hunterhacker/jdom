package org.jdom2;

import static org.junit.Assert.*;

import org.jdom2.StringBin;
import org.jdom2.test.util.UnitTestUtil;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class TestStringBin {
	
	// The following 4 all have the same hashcode 31776 ..... clever... huh?
	private static final String[] samehc = new String[] {
		"\u03FA\u00DA",
		"\u0401\u0001",
		"\u0400\u0020",
		"\u03FF\u003F",
		"   ",
		"\u03FE\u005E",
		"\u03FD\u007D",
		"\u03FC\u009C",
		"\u03FB\u00BB",
	};
	
	@Test
	public void testNegativeCapacity() {
		try {
			new StringBin(-1);
			fail("excpect exception!");
		} catch (Exception e) {
			UnitTestUtil.checkException(IllegalArgumentException.class, e);
		}
	}
	
	@Test
	public void testSmallCapacity() {
		assertNotNull(new StringBin(1));
	}
	
	@Test
	public void testNull() {
		StringBin bin = new StringBin();
		assertNull(bin.reuse(null));
	}
	
	@Test
	public void testSameHashCode() {
		for (int i = 0; i < samehc.length; i++) {
			int hc = samehc[i].hashCode();
			if (hc != 31776) {
				fail("Missed '" + samehc[i] + "' -> " + samehc[i].hashCode());
			}
		}
		StringBin bin = new StringBin();
		String[] actuals = new String[samehc.length];
		for (int i = 0; i < samehc.length; i++) {
			actuals[i] = bin.reuse(samehc[i]);
			assertEquals(samehc[i], actuals[i]);
			// should have compacted things.
			assertTrue(samehc[i] != actuals[i]);
		}
		assertTrue(bin.size() == samehc.length);
		for (int i = 0; i < samehc.length; i++) {
			assertTrue(actuals[i] == bin.reuse(samehc[i]));
		}
		assertTrue(bin.size() == samehc.length);
		
		
		bin = new StringBin();
		for (int i = samehc.length - 1; i >= 0; i--) {
			actuals[i] = bin.reuse(samehc[i]);
			assertEquals(samehc[i], actuals[i]);
			assertTrue(samehc[i] != actuals[i]);
		}
		for (int i = 0; i < samehc.length; i++) {
			assertTrue(actuals[i] == bin.reuse(samehc[i]));
		}
	}

	@Test
	public final void bulkIntern() {
		String[] tstvals = new String[1024 * 256];
		String[] actvals = new String[tstvals.length];
		String[] dupvals = new String[tstvals.length];
		StringBin bin = new StringBin(2048);
		int hc = 0;
		for (int i = 0; i < tstvals.length; i++) {
			tstvals[i] = "value " + i;
			hc ^= tstvals[i].hashCode();
			dupvals[i] = "value " + i;
			hc ^= dupvals[i].hashCode();
		}
		assertTrue(hc == 0);
		for (int i = 0; i < tstvals.length; i++) {
			actvals[i] = bin.reuse(tstvals[i]);
			assertTrue(tstvals[i] != actvals[i]);
			assertEquals(tstvals[i], actvals[i]);
		}
		assertTrue(bin.size() == tstvals.length);
		
		for (int i = 0; i < tstvals.length; i++) {
			assertTrue(actvals[i] == bin.reuse(tstvals[i]));
		}
		assertTrue(bin.size() == tstvals.length);
		
		for (int i = 0; i < tstvals.length; i++) {
			assertTrue(actvals[i] == bin.reuse(dupvals[i]));
		}
		assertTrue(bin.size() == tstvals.length);
		
		String[] acthc  = new String[samehc.length];
		for (int i = 0; i < samehc.length; i++) {
			acthc[i] = bin.reuse(samehc[i]);
			assertTrue(acthc[i] != samehc[i]);
			assertEquals(samehc[i], acthc[i]);
		}
		assertTrue(bin.size() == tstvals.length + samehc.length);
		for (int i = 0; i < samehc.length; i++) {
			assertTrue(acthc[i] == bin.reuse(samehc[i]));
		}
		assertTrue(bin.size() == tstvals.length + samehc.length);
	}
	
}
