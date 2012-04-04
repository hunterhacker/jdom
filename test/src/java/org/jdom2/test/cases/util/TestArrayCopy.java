package org.jdom2.test.cases.util;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import org.jdom2.internal.ArrayCopy;
import org.jdom2.test.util.UnitTestUtil;

@SuppressWarnings("javadoc")
public class TestArrayCopy {

	// UnitTests/compile is done from Java6 .... so
	// we can compare with Arrays.copyOf()
	
	@Test
	public void testCopyOfEArrayInt() {
		final String[] val = {"a", "b", "c", "d"};
		assertTrue(Arrays.equals(ArrayCopy.copyOf(val, 3), Arrays.copyOf(val, 3)));
		assertTrue(Arrays.equals(ArrayCopy.copyOf(val, 0), Arrays.copyOf(val, 0)));
		assertTrue(Arrays.equals(ArrayCopy.copyOf(val, 5), Arrays.copyOf(val, 5)));
	}

	@Test
	public void testCopyOfRange() {
		final String[] val = {"a", "b", "c", "d"};
		assertTrue(Arrays.equals(ArrayCopy.copyOfRange(val, 1, 2), Arrays.copyOfRange(val, 1, 2)));
		assertTrue(Arrays.equals(ArrayCopy.copyOfRange(val, 1, 5), Arrays.copyOfRange(val, 1, 5)));
		try {
			ArrayCopy.copyOfRange(val, 3, 2);
			UnitTestUtil.failNoException(IllegalArgumentException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(IllegalArgumentException.class, e);
		}
	}

	@Test
	public void testCopyOfCharArrayInt() {
		final char[] val = {'a', 'b', 'c', 'd'};
		assertTrue(Arrays.equals(ArrayCopy.copyOf(val, 3), Arrays.copyOf(val, 3)));
		assertTrue(Arrays.equals(ArrayCopy.copyOf(val, 0), Arrays.copyOf(val, 0)));
		assertTrue(Arrays.equals(ArrayCopy.copyOf(val, 5), Arrays.copyOf(val, 5)));
	}

	@Test
	public void testCopyOfIntArrayInt() {
		final int[] val = {1, 2, 3, 4};
		assertTrue(Arrays.equals(ArrayCopy.copyOf(val, 3), Arrays.copyOf(val, 3)));
		assertTrue(Arrays.equals(ArrayCopy.copyOf(val, 0), Arrays.copyOf(val, 0)));
		assertTrue(Arrays.equals(ArrayCopy.copyOf(val, 5), Arrays.copyOf(val, 5)));
	}

	@Test
	public void testCopyOfBooleanArrayInt() {
		final boolean[] val = {true, false, true, false};
		assertTrue(Arrays.equals(ArrayCopy.copyOf(val, 3), Arrays.copyOf(val, 3)));
		assertTrue(Arrays.equals(ArrayCopy.copyOf(val, 0), Arrays.copyOf(val, 0)));
		assertTrue(Arrays.equals(ArrayCopy.copyOf(val, 5), Arrays.copyOf(val, 5)));
	}

}
