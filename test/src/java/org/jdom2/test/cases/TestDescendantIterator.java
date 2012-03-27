package org.jdom2.test.cases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Test;

import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.test.util.UnitTestUtil;
import org.jdom2.util.IteratorIterable;

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
	
	private static final Element buildTestDoc() {
		Element hobbits = new Element(fellowship[0]);
		hobbits.addContent(new Element(fellowship[1]));
		hobbits.addContent(new Element(fellowship[2]));
		hobbits.addContent(new Element(fellowship[3]));
		Element humans = new Element(fellowship[4]);
		humans.addContent(new Element(fellowship[5]));
		humans.addContent(new Element(fellowship[6]));
		humans.addContent(new Element(fellowship[7]));
		hobbits.addContent(humans);
		// gandalf
		hobbits.addContent(new Element(fellowship[8]));
		return new Element("root").addContent(hobbits);
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
	
	
	@Test
	public void testRemoves() {
		for (int i = fellowship.length - 1; i >= 0; i--) {
			checkRemove(i);
		}
	}
	
	private void checkIterator(final Iterator<Content> it, final String...values) {
		for (int i = 0; i < values.length; i++) {
			assertTrue(it.hasNext());
			assertEquals(values[i], ((Element)it.next()).getName());
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

	private void checkRemove(final int remove) {
		Element doc = buildTestDoc();
		checkIterator(doc.getDescendants(), fellowship);
		Iterator<Content> it1 = doc.getDescendants();
		it1.next();
		for (int i = 0; i < remove; i++) {
			it1.next();
		}
		it1.remove();
		try {
			it1.remove();
			fail ("Should not be able to double-remove");
		} catch (Exception e) {
			UnitTestUtil.checkException(IllegalStateException.class, e);
		}
		ArrayList<String> al = new ArrayList<String>();
		Iterator<Content> it2 = doc.getDescendants();
		int i = remove;
		while (--i >= 0) {
			it2.next();
		}
		while (it2.hasNext()) {
			al.add(((Element)it2.next()).getName());
		}
		checkIterator(it1, al.toArray(new String[al.size()]));
	}
	
	@Test
	public void testDeepNesting() {
		ArrayList<String> names = new ArrayList<String>(64);
		Element p = new Element("name");
		names.add("name");
		Document doc = new Document(p);
		for (int i = 0; i < 64; i++) {
			names.add("name" + i);
			final Element e = new Element("name" + i);
			p.getContent().add(e);
			p = e;
		}
		
		checkIterator(doc.getDescendants(), names.toArray(new String[names.size()]));
	}
	
	@Test
	public void testSpecialCaseRemove() {
		// this is designed to test a special case:
		// the iterator's next move was to go down next, but, we did a remove(),
		// and now we can't go down next, but, there's no more siblings either,
		// so our next move will be up, but the level up is no more siblings
		// either, but some level above that has siblings, so we go there....
		/*
		 * <root>
		 *   <filler1>
		 *     <toremove>
		 *       <child />
		 *     </toremove>
		 *   </filler1>
		 *   <postremove />
		 * </root>
		 */
		Element root = new Element("root");
		Element filler1 = new Element("filler");
		root.addContent(filler1);
		
		// right, this will be the thing we remove.
		Element toremove = new Element("toremove");
		filler1.addContent(toremove);
		
		// but, this needs to have kids to go down to...
		toremove.addContent(new Element("child"));
		
		
		// this will be what next() returns after the remove.
		Element postremove = new Element("postremove");
		root.addContent(postremove);
		
		Document doc = new Document(root);
		Iterator<Content> it = doc.getDescendants();
		while (it.hasNext()) {
			Content c = it.next();
			if (c == toremove) {
				it.remove();
				assertTrue(it.hasNext());
				assertTrue(postremove == it.next());
			}
		}
		
	}
	
	

}
