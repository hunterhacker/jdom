package org.jdom2.test.cases;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Text;
import org.junit.Before;
import org.junit.Test;

public class TestDescendantIterator {
	private static final String[] fellowship = new String[] {
			"frodo", "sam", "pippin", "merry",
			"legolas", "aragorn", "gimli", "boromir", "gandalf"
	};
	
	private static final Iterator buildIterator() {
		Element root = new Element("root");
		Document doc = new Document(root);
		for (String c : fellowship) {
			root.addContent(new Element(c));
		}
		
		return doc.getDescendants();
	}
	
	@Test
	public void testIteration() {
		Iterator it = buildIterator();
		assertTrue(it.hasNext());
		Object f = it.next();
		assertNotNull(f != null);
		assertTrue(f instanceof Element);
		assertEquals("root", ((Element)f).getName());
		for (int i = 0; i < fellowship.length; i++) {
			assertTrue(it.hasNext());
			assertEquals(fellowship[i], ((Element)it.next()).getName());
		}
		assertFalse(it.hasNext());
		try {
			assertTrue(null != it.next().toString());
			fail("Should not be able to iterate off the end of the descendants.");
		} catch (NoSuchElementException nse) {
			// good
		} catch (Exception e) {
			fail("Expected NoSuchElementException, but got " + e.getClass().getName());
		}
		
	}

	@Test
	public void testRemoveOne() {
		Iterator it = buildIterator();
		assertTrue(it.hasNext());
		Object f = it.next();
		assertNotNull(f != null);
		assertTrue(f instanceof Element);
		assertEquals("root", ((Element)f).getName());
		
		// this should remove the root element, which effectively should
		// make the descendant iterator empty.
		it.remove();
		
		// TODO This should be implemented to check the remove, but remove() is
		// broken. See https://github.com/hunterhacker/jdom/issues/24
		
//		assertFalse(it.hasNext());
//		try {
//			assertTrue(null != it.next().toString());
//			fail("Should not be able to iterate off the end of the descendants.");
//		} catch (NoSuchElementException nse) {
//			// good
//		} catch (Exception e) {
//			fail("Expected NoSuchElementException, but got " + e.getClass().getName());
//		}
		
	}

}
