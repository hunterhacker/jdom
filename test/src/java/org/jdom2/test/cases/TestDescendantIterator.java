package org.jdom2.test.cases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.util.IteratorIterable;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class TestDescendantIterator {
	private static final String[] fellowship = new String[] {
			"frodo", "sam", "pippin", "merry",
			"legolas", "aragorn", "gimli", "boromir", "gandalf"
	};
	
	private static final IteratorIterable<Content> buildIterator() {
		Element root = new Element("root");
		Document doc = new Document(root);
		for (String c : fellowship) {
			root.addContent(new Element(c));
		}
		
		return doc.getDescendants();
	}
	
	@Test
	public void testIteration() {
		Iterator<Content> it = buildIterator();
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
	public void testIterable() {
		int i = 0;
		for (Content c : buildIterator()) {
			assertNotNull(c != null);
			assertTrue(c instanceof Element);
			Element e = (Element)c;
			if (i == 0) {
				assertEquals("root", e.getName());
			} else {
				assertEquals(fellowship[i - 1], e.getName());
			}
			i++;
		}
	}

	@Test
	public void testRemoveOne() {
		Iterator<Content> it = buildIterator();
		assertTrue(it.hasNext());
		Object f = it.next();
		assertNotNull(f != null);
		assertTrue(f instanceof Element);
		assertEquals("root", ((Element)f).getName());
		
		// this should remove the root element, which effectively should
		// make the descendant iterator empty.
		it.remove();
		
		
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

}
