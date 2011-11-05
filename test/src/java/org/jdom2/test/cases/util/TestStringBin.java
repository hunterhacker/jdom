package org.jdom2.test.cases.util;

import static org.junit.Assert.*;

import org.jdom2.test.util.UnitTestUtil;
import org.jdom2.util.StringBin;
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
		for (int i = 0; i < samehc.length; i++) {
			assertTrue(samehc[i] == bin.reuse(samehc[i]));
		}
		assertTrue(bin.size() == samehc.length);
		for (int i = 0; i < samehc.length; i++) {
			assertTrue(samehc[i] == bin.reuse(samehc[i]));
		}
		assertTrue(bin.size() == samehc.length);
		
		
		bin = new StringBin();
		for (int i = samehc.length - 1; i >= 0; i--) {
			assertTrue(samehc[i] == bin.reuse(samehc[i]));
		}
		for (int i = 0; i < samehc.length; i++) {
			assertTrue(samehc[i] == bin.reuse(samehc[i]));
		}
	}

	@Test
	public final void bulkIntern() {
		String[] tstvals = new String[1024 * 256];
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
			assertTrue(tstvals[i] == bin.reuse(tstvals[i]));
		}
		assertTrue(bin.size() == tstvals.length);
		
		for (int i = 0; i < tstvals.length; i++) {
			assertTrue(tstvals[i] == bin.reuse(tstvals[i]));
		}
		assertTrue(bin.size() == tstvals.length);
		
		for (int i = 0; i < tstvals.length; i++) {
			assertTrue(tstvals[i] == bin.reuse(dupvals[i]));
		}
		assertTrue(bin.size() == tstvals.length);
		
		for (int i = 0; i < samehc.length; i++) {
			assertTrue(samehc[i] == bin.reuse(samehc[i]));
		}
		assertTrue(bin.size() == tstvals.length + samehc.length);
		for (int i = 0; i < samehc.length; i++) {
			assertTrue(samehc[i] == bin.reuse(samehc[i]));
		}
		assertTrue(bin.size() == tstvals.length + samehc.length);
	}
	
}
