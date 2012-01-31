package org.jdom2.test.cases.filter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.RandomAccess;

import org.jdom2.filter.Filter;
import org.jdom2.filter.Filters;
import org.jdom2.test.util.UnitTestUtil;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class TestPassThroughFilter  {
	
	@Test
	public void testPassThroughFilterRandom() {
		Filter<Object> ef = Filters.fpassthrough();
		
		List<String> lst = new ArrayList<String>(3);
		lst.add("hello");
		lst.add("there");
		lst.add("world");
		List<Object> filtered = ef.filter(lst);
		assertTrue(filtered instanceof RandomAccess);
		assertTrue(filtered.size() == 3);
		assertEquals("hello", filtered.get(0));
		assertEquals("there", filtered.get(1));
		assertEquals("world", filtered.get(2));
		
		try {
			filtered.add("boo");
			UnitTestUtil.failNoException(UnsupportedOperationException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(UnsupportedOperationException.class, e);
		}
		
	}


	@Test
	public void testPassThroughFilterLinked() {
		Filter<Object> ef = Filters.fpassthrough();
		
		List<String> lst = new LinkedList<String>();
		lst.add("hello");
		lst.add("there");
		lst.add("world");
		List<Object> filtered = ef.filter(lst);
		assertTrue(filtered instanceof RandomAccess);
		assertTrue(filtered.size() == 3);
		assertEquals("hello", filtered.get(0));
		assertEquals("there", filtered.get(1));
		assertEquals("world", filtered.get(2));
		
		try {
			filtered.add("boo");
			UnitTestUtil.failNoException(UnsupportedOperationException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(UnsupportedOperationException.class, e);
		}
		
	}


	@Test
	public void testPassThroughFilterNull() {
		Filter<Object> ef = Filters.fpassthrough();
		
		List<String> lst = null;
		List<Object> filtered = ef.filter(lst);
		assertTrue(filtered instanceof RandomAccess);
		assertTrue(filtered.size() == 0);
		
		try {
			filtered.add("boo");
			UnitTestUtil.failNoException(UnsupportedOperationException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(UnsupportedOperationException.class, e);
		}
		
	}

	@Test
	public void testPassThroughFilterEmpty() {
		Filter<Object> ef = Filters.fpassthrough();
		
		List<String> lst = new LinkedList<String>();
		List<Object> filtered = ef.filter(lst);
		assertTrue(filtered instanceof RandomAccess);
		assertTrue(filtered.size() == 0);
		
		try {
			filtered.add("boo");
			UnitTestUtil.failNoException(UnsupportedOperationException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(UnsupportedOperationException.class, e);
		}
		
	}
}
