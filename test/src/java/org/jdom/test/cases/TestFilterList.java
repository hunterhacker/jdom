package org.jdom.test.cases;

/*-- 

 Copyright (C) 2000 Brett McLaughlin & Jason Hunter.
 All rights reserved.
 
 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:
 
 1. Redistributions of source code must retain the above copyright
	notice, this list of conditions, and the following disclaimer.
 
 2. Redistributions in binary form must reproduce the above copyright
	notice, this list of conditions, and the disclaimer that follows 
	these conditions in the documentation and/or other materials 
	provided with the distribution.

 3. The name "JDOM" must not be used to endorse or promote products
	derived from this software without prior written permission.  For
	written permission, please contact license@jdom.org.
 
 4. Products derived from this software may not be called "JDOM", nor
	may "JDOM" appear in their name, without prior written permission
	from the JDOM Project Management (pm@jdom.org).
 
 In addition, we request (but do not require) that you include in the 
 end-user documentation provided with the redistribution and/or in the 
 software itself an acknowledgement equivalent to the following:
	 "This product includes software developed by the
	  JDOM Project (http://www.jdom.org/)."
 Alternatively, the acknowledgment may be graphical using the logos 
 available at http://www.jdom.org/images/logos.

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED.  IN NO EVENT SHALL THE JDOM AUTHORS OR THE PROJECT
 CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.

 This software consists of voluntary contributions made by many 
 individuals on behalf of the JDOM Project and was originally 
 created by Brett McLaughlin <brett@jdom.org> and 
 Jason Hunter <jhunter@jdom.org>.  For more information on the 
 JDOM Project, please see <http://www.jdom.org/>.
 
 */

/**
 * Please put a description of your test here.
 * 
 * @author unascribed
 * @version 0.1
 */
import junit.framework.*;
import org.jdom.*;
import java.util.*;

public final class TestFilterList
extends junit.framework.TestCase
{
		Element foo;
		Element bar;
		Element baz;
		Element quux;
		Comment comment;
		Comment comment2;
		Comment comment3;
        Text text1;
        Text text2;
        Text text3;
        Text text4;

    /**
     *  Construct a new instance. 
     */
    public TestFilterList(String name) {
        super(name);
    }

    /**
     * This method is called before a test is executed.
     */
    public void setUp() {
		foo = new Element("foo");
		bar = new Element("bar");
		baz = new Element("baz");
		quux = new Element("quux");
		comment = new Comment("comment");
		comment2 = new Comment("comment2");
		comment3 = new Comment("comment3");
        text1 = new Text("\n");
        text2 = new Text("\n");
        text3 = new Text("\n");
        text4 = new Text("\n");

		foo.addContent(text1);
		foo.addContent(bar);
		foo.addContent(text2);
		foo.addContent(baz);
		foo.addContent(text3);
		foo.addContent(comment);
		foo.addContent(quux);
		foo.addContent(text4);

		// Contents of foo are now:
		// \n, bar, \n, baz, \n, comment, quux, \n
    }

    /**
     * This method is called after a test is executed.
     */
    public void tearDown() {
        // your code goes here.
    }

    /**
     * The suite method runs all the tests
     */
public static Test suite () {
        TestSuite suite = new TestSuite(TestFilterList.class);
        return suite;
    }

    /**
     * The main method runs all the tests in the text ui
     */
    public static void main (String args[]) 
     {
        junit.textui.TestRunner.run(suite());
    }

    public void test_TCM__int_hashCode() {
		List content = foo.getContent();
		List content2 = new ArrayList();
		content2.addAll(content);
		assertEquals("bad hashcode", content2.hashCode(), content.hashCode());
    }

    public void test_TCM__boolean_equals_Object() {
		List content = foo.getContent();
		List content2 = new ArrayList();
		content2.addAll(content);
		assertTrue("bad equals", content.equals(content2));
    }

    public void xtest_TCM__Object_clone() {
        fail("implement me !");
    }

    public void test_TCM__int_indexOf_Object() {
		List children = foo.getChildren();
		assertEquals("wrong result from indexOf", 0, children.indexOf(bar));
		assertEquals("wrong result from indexOf", 1, children.indexOf(baz));
		assertEquals("wrong result from indexOf", 2, children.indexOf(quux));

		List content = foo.getContent();
		assertEquals("wrong result from indexOf", 0, content.indexOf(text1));
		assertEquals("wrong result from indexOf", 1, content.indexOf(bar));
		assertEquals("wrong result from indexOf", 2, content.indexOf(text2));
		assertEquals("wrong result from indexOf", 3, content.indexOf(baz));
		assertEquals("wrong result from indexOf", 4, content.indexOf(text3));
		assertEquals("wrong result from indexOf", 5, content.indexOf(comment));
		assertEquals("wrong result from indexOf", 6, content.indexOf(quux));
		assertEquals("wrong result from indexOf", 7, content.indexOf(text4));
    }

    public void test_TCM__int_lastIndexOf_Object() {
		List children = foo.getChildren();
		assertEquals("wrong result from lastIndexOf", 0, children.lastIndexOf(bar));
		assertEquals("wrong result from lastIndexOf", 1, children.lastIndexOf(baz));
		assertEquals("wrong result from lastIndexOf", 2, children.lastIndexOf(quux));

		List content = foo.getContent();
		assertEquals("wrong result from lastIndexOf", 0, content.lastIndexOf(text1));
		assertEquals("wrong result from lastIndexOf", 1, content.lastIndexOf(bar));
		assertEquals("wrong result from lastIndexOf", 2, content.lastIndexOf(text2));
		assertEquals("wrong result from lastIndexOf", 3, content.lastIndexOf(baz));
		assertEquals("wrong result from lastIndexOf", 4, content.lastIndexOf(text3));
		assertEquals("wrong result from lastIndexOf", 5, content.lastIndexOf(comment));
		assertEquals("wrong result from lastIndexOf", 6, content.lastIndexOf(quux));
		assertEquals("wrong result from lastIndexOf", 7, content.lastIndexOf(text4));
    }

    public void test_TCM__Object_get_int() {
		List children = foo.getChildren();
		assertEquals("wrong element from get", bar, children.get(0));
		assertEquals("wrong element from get", baz, children.get(1));
		assertEquals("wrong element from get", quux, children.get(2));

		List content = foo.getContent();
		assertEquals("wrong element from get", text1, content.get(0));
		assertEquals("wrong element from get", bar, content.get(1));
		assertEquals("wrong element from get", text2, content.get(2));
		assertEquals("wrong element from get", baz, content.get(3));
		assertEquals("wrong element from get", text3, content.get(4));
		assertEquals("wrong element from get", comment, content.get(5));
		assertEquals("wrong element from get", quux, content.get(6));
		assertEquals("wrong element from get", text4, content.get(7));

		try {
			children.get(48);
			fail("Should have thrown an IndexOutOfBoundsException");
		} catch(IndexOutOfBoundsException ex) {}
		try {
			children.get(-3);
			fail("Should have thrown an IndexOutOfBoundsException");
		} catch(IndexOutOfBoundsException ex) {}
		try {
			content.get(48);
			fail("Should have thrown an IndexOutOfBoundsException");
		} catch(IndexOutOfBoundsException ex) {}
		try {
			content.get(-3);
			fail("Should have thrown an IndexOutOfBoundsException");
		} catch(IndexOutOfBoundsException ex) {}
    }

    public void test_TCM__Object_set_int_Object() {
		List children = foo.getChildren();
		List content = foo.getContent();

		Element blah = new Element("blah");
        Text text5 = new Text("this was bar");
		content.set(1, text5);
		children.set(1, blah);

		assertTrue("parent is not correct", blah.getParent() == foo);

		assertEquals("wrong size", 2, children.size());
		assertEquals("wrong element from set", baz, children.get(0));
		assertEquals("wrong element from set", blah, children.get(1));

		assertEquals("wrong size", 8, content.size());
		assertEquals("wrong element from set", text1, content.get(0));
		assertEquals("wrong element from set", text5, content.get(1));
		assertEquals("wrong element from set", text2, content.get(2));
		assertEquals("wrong element from set", baz, content.get(3));
		assertEquals("wrong element from set", text3, content.get(4));
		assertEquals("wrong element from set", comment, content.get(5));
		assertEquals("wrong element from set", blah, content.get(6));
		assertEquals("wrong element from set", text4, content.get(7));

		try {
			children.set(48, new Element("test"));
			fail("Should have thrown an IndexOutOfBoundsException");
		} catch(IndexOutOfBoundsException ex) {}
		try {
			children.set(-3, new Element("test"));
			fail("Should have thrown an IndexOutOfBoundsException");
		} catch(IndexOutOfBoundsException ex) {}
		try {
			content.set(48, new Element("test"));
			fail("Should have thrown an IndexOutOfBoundsException");
		} catch(IndexOutOfBoundsException ex) {}
		try {
			content.set(-3, new Element("test"));
			fail("Should have thrown an IndexOutOfBoundsException");
		} catch(IndexOutOfBoundsException ex) {}
    }

    public void test_TCM__void_add_int_Object() {
		List children = foo.getChildren();
		List content = foo.getContent();

		Element blah = new Element("blah");
        Text text5 = new Text("this is before bar");
		content.add(1, text5);
		children.add(1, blah);

		assertTrue("parent is not correct", blah.getParent() == foo);

		assertEquals("wrong size", 4, children.size());
		assertEquals("wrong element from add", bar, children.get(0));
		assertEquals("wrong element from add", blah, children.get(1));
		assertEquals("wrong element from add", baz, children.get(2));
		assertEquals("wrong element from add", quux, children.get(3));

		assertEquals("wrong size", 10, content.size());
		assertEquals("wrong element from add", text1, content.get(0));
		assertEquals("wrong element from add", text5, content.get(1));
		assertEquals("wrong element from add", bar, content.get(2));
		assertEquals("wrong element from add", blah, content.get(3));
		assertEquals("wrong element from add", text2, content.get(4));
		assertEquals("wrong element from add", baz, content.get(5));
		assertEquals("wrong element from add", text3, content.get(6));
		assertEquals("wrong element from add", comment, content.get(7));
		assertEquals("wrong element from add", quux, content.get(8));
		assertEquals("wrong element from add", text4, content.get(9));

		try {
			children.add(48, new Element("test"));
			fail("Should have thrown an IndexOutOfBoundsException");
		} catch(IndexOutOfBoundsException ex) {}
		try {
			children.add(-3, new Element("test"));
			fail("Should have thrown an IndexOutOfBoundsException");
		} catch(IndexOutOfBoundsException ex) {}
		try {
			content.add(48, new Element("test"));
			fail("Should have thrown an IndexOutOfBoundsException");
		} catch(IndexOutOfBoundsException ex) {}
		try {
			content.add(-3, new Element("test"));
			fail("Should have thrown an IndexOutOfBoundsException");
		} catch(IndexOutOfBoundsException ex) {}
    }

    public void test_TCM__boolean_add_Object() {
		List children = foo.getChildren();
		List content = foo.getContent();

		Element blah = new Element("blah");
        Text text5 = new Text("this is last");
		content.add(text5);
		children.add(blah);

		assertTrue("parent is not correct", blah.getParent() == foo);

		assertEquals("wrong size", 4, children.size());
		assertEquals("wrong element from add", bar, children.get(0));
		assertEquals("wrong element from add", baz, children.get(1));
		assertEquals("wrong element from add", quux, children.get(2));
		assertEquals("wrong element from add", blah, children.get(3));

		assertEquals("wrong size", 10, content.size());
		assertEquals("wrong element from add", text1, content.get(0));
		assertEquals("wrong element from add", bar, content.get(1));
		assertEquals("wrong element from add", text2, content.get(2));
		assertEquals("wrong element from add", baz, content.get(3));
		assertEquals("wrong element from add", text3, content.get(4));
		assertEquals("wrong element from add", comment, content.get(5));
		assertEquals("wrong element from add", quux, content.get(6));
		assertEquals("wrong element from add", text4, content.get(7));
		assertEquals("wrong element from add", text5, content.get(8));
		assertEquals("wrong element from add", blah, content.get(9));
		assertTrue("parent is not correct", comment.getParent() == foo);
    }

	public void testModification() {
		List children = foo.getChildren();
		List content = foo.getContent();

		modifyFoo();

		// New contents of foo:
		// \n, comment2, comment3, \n, \n, comment, quux, \n

		assertEquals("wrong size", 1, children.size());
		assertEquals("wrong element", quux, children.get(0));

		assertEquals("wrong size", 7, content.size());
		assertEquals("wrong element", text1, content.get(0));
		assertEquals("wrong element", comment2, content.get(1));
		assertEquals("wrong element", comment3, content.get(2));
		assertEquals("wrong element", text3, content.get(3));
		assertEquals("wrong element", comment, content.get(4));
		assertEquals("wrong element", quux, content.get(5));
		assertEquals("wrong element", text4, content.get(6));

		// Make sure that parentage was adjusted correctly.
		assertTrue("parent is not correct", bar.getParent() == null);
		assertTrue("parent is not correct", baz.getParent() == null);
		assertTrue("parent is not correct", comment.getParent() == foo);
		assertTrue("parent is not correct", comment2.getParent() == foo);
		assertTrue("parent is not correct", comment3.getParent() == foo);
		assertTrue("parent is not correct", quux.getParent() == foo);
	}
		

    public void test_TCM__int_size() {
		// Test size on lists.
		List children = foo.getChildren();
		assertEquals("wrong size", 3, children.size());
		List content = foo.getContent();
		assertEquals("wrong size", 8, content.size());
		
		// Modify 
		modifyFoo();

		// New contents of foo:
		// \n, comment2, comment3, \n, \n, comment, quux, \n

		// Test size on already-created lists.
		assertEquals("wrong size", 1, children.size());
		assertEquals("wrong size", 7, content.size());

		// Test size on newly-created lists.
		children = foo.getChildren();
		assertEquals("wrong size", 1, children.size());
		content = foo.getContent();
		assertEquals("wrong size", 7, content.size());
		
    }

	public void testConcurrentModification() {
		// Get lists.
		List children = foo.getChildren();
		List content = foo.getContent();
		// Get iterators.
		Iterator iter = children.iterator();
		Iterator iter2 = content.iterator();

		// Modify 
		modifyFoo();

		// Try to access an already-existing iterator.
		try {
			iter.hasNext();
			fail("No concurrent modification exception.");
		} catch(ConcurrentModificationException ex) {
			assertTrue(true);
		}

		// Try to access an already-existing iterator.
		try {
			iter2.next();
			fail("No concurrent modification exception.");
		} catch(ConcurrentModificationException ex) {
			assertTrue(true);
		}

		// Try to access a newly-created iterator.
		iter = children.iterator();
		iter.hasNext();
		iter2 = content.iterator();
		iter2.next();

		assertEquals("wrong size", 1, children.size());
		assertEquals("wrong size", 7, content.size());

		// Test iterator.remove().
System.out.println("*****************************");
		iter2 = children.iterator();
		while(iter2.hasNext()) {
			iter2.next();
			iter2.remove();
		}

		assertEquals("wrong size", 0, children.size());
		assertEquals("wrong size", 6, content.size());

		// Test iterator.remove().
		iter2 = content.iterator();
		while(iter2.hasNext()) {
			iter2.next();
			iter2.remove();
		}

		assertEquals("wrong size", 0, children.size());
		assertEquals("wrong size", 0, content.size());
	}

	// Modify "foo" a bit.
	private void modifyFoo() {
		List children = foo.getChildren();
		List content = foo.getContent();
		// \n, bar, \n, baz, \n, comment, quux, \n

		children.remove(1); // remove baz
		assertEquals("wrong size", 2, children.size());
		assertEquals("wrong size", 7, content.size());
		// \n, bar, \n, \n, comment, quux, \n

		content.add(1, comment2);
		assertEquals("wrong size", 2, children.size());
		assertEquals("wrong size", 8, content.size());
		// \n, comment2, bar, \n, \n, comment, quux, \n

		content = foo.getContent();

		content.remove(3); // remove \n
		assertEquals("wrong size", 2, children.size());
		assertEquals("wrong size", 7, content.size());
		// \n, comment2, bar, \n, comment, quux, \n


		content.set(2, comment3);
		assertEquals("wrong size", 1, children.size());
		assertEquals("wrong size", 7, content.size());
		// \n, comment2, comment3, \n, comment, quux, \n
	}

    public void test_TCM__ArrayObject_toArray() {
		List children = foo.getChildren();
		List content = foo.getContent();

		Object[] childrenArray = children.toArray();
		Object[] contentArray = content.toArray();

		// Make sure they're not live.
		children.remove(1);
		content.remove(comment);

		assertArrays(childrenArray, contentArray);
    }

    public void test_TCM__ArrayObject_toArray_ArrayObject() {
		List children = foo.getChildren();
		List content = foo.getContent();

		// These arrays are big enough, and don't need to be expanded.
		Object[] childrenArray = new Object[children.size()];
		Object[] contentArray = new Object[99];
		children.toArray(childrenArray);
		content.toArray(contentArray);

		assertEquals("bad toArray size", childrenArray.length, 3);
		assertEquals("bad toArray size", contentArray.length, 99);
		assertArrays(childrenArray, contentArray);


		// These arrays aren't big enough, and do need to be expanded.
		childrenArray = new Object[1];
		contentArray = new Object[2];
		childrenArray = children.toArray(childrenArray);
		contentArray = content.toArray(contentArray);

		// Make sure they're not live.
		children.remove(baz);
		content.remove(1);

		assertEquals("bad toArray size", childrenArray.length, 3);
		assertEquals("bad toArray size", contentArray.length, 8);
		assertArrays(childrenArray, contentArray);
	}

	private void assertArrays(Object[] childrenArray, Object[] contentArray)
	{
		assertEquals("bad toArray", bar, childrenArray[0]);
		assertEquals("bad toArray", baz, childrenArray[1]);
		assertEquals("bad toArray", quux, childrenArray[2]);

		assertEquals("bad toArray", text1, contentArray[0]);
		assertEquals("bad toArray", bar, contentArray[1]);
		assertEquals("bad toArray", text2, contentArray[2]);
		assertEquals("bad toArray", baz, contentArray[3]);
		assertEquals("bad toArray", text3, contentArray[4]);
		assertEquals("bad toArray", comment, contentArray[5]);
		assertEquals("bad toArray", quux, contentArray[6]);
		assertEquals("bad toArray", text4, contentArray[7]);
    }

    public void test_TCM__boolean_contains_Object() {
		List content = foo.getContent();
		List children = foo.getChildren();

		assertTrue("bad contains", !content.contains(foo));
		assertTrue("bad contains", content.contains(bar));
		assertTrue("bad contains", content.contains(baz));
		assertTrue("bad contains", content.contains(quux));
		assertTrue("bad contains", content.contains(comment));
		assertTrue("bad contains", content.contains(text1));
		assertTrue("bad contains", content.contains(text2));
		assertTrue("bad contains", content.contains(text3));
		assertTrue("bad contains", content.contains(text4));

		assertTrue("bad contains", !children.contains(foo));
		assertTrue("bad contains", children.contains(bar));
		assertTrue("bad contains", children.contains(baz));
		assertTrue("bad contains", children.contains(quux));
		assertTrue("bad contains", !children.contains(comment));
		assertTrue("bad contains", !children.contains(text1));
		assertTrue("bad contains", !children.contains(text2));
		assertTrue("bad contains", !children.contains(text3));
		assertTrue("bad contains", !children.contains(text4));
    }

    public void test_TCM__void_clear() {
		List content = foo.getContent();
		List children = foo.getChildren();

		children.clear();

		assertEquals("bad clear", 0, children.size());
		assertEquals("bad clear", 5, content.size());
		assertTrue("bad clear", content.get(0).equals(text1));
		assertTrue("bad clear", content.get(1).equals(text2));
		assertTrue("bad clear", content.get(2).equals(text3));
		assertTrue("bad clear", content.get(3) == comment);
		assertTrue("bad clear", content.get(4).equals(text4));

		assertTrue("parent is not correct", comment.getParent() == foo);
		assertTrue("parent is not correct", bar.getParent() == null);
		assertTrue("parent is not correct", baz.getParent() == null);
		assertTrue("parent is not correct", quux.getParent() == null);

 		content.clear();

		assertTrue("bad clear", children.size() == 0);
		assertTrue("bad clear", content.size() == 0);

		assertTrue("parent is not correct", comment.getParent() == null);
		assertTrue("parent is not correct", bar.getParent() == null);
		assertTrue("parent is not correct", baz.getParent() == null);
		assertTrue("parent is not correct", quux.getParent() == null);

   }

    public void test_TCM__Object_remove_int() {
		List content = foo.getContent();
		List children = foo.getChildren();
		
		// \n, bar, \n, baz, \n, comment, quux, \n
		content.remove(4); // third /n
		children.remove(0); // bar
		content.remove(3); // comment
		content.remove(0); // first /n
		// \n, baz, quux, \n

		assertTrue("bad removal", children.size() == 2);
		assertTrue("bad removal", children.get(0) == baz);
		assertTrue("bad removal", children.get(1) == quux);
		assertTrue("bad removal", content.size() == 4);
		assertTrue("bad removal", content.get(0).equals(text2));
		assertTrue("bad removal", content.get(1) == baz);
		assertTrue("bad removal", content.get(2) == quux);
		assertTrue("bad removal", content.get(3).equals(text4));

		assertTrue("parent is not correct", bar.getParent() == null);
		assertTrue("parent is not correct", baz.getParent() == foo);
		assertTrue("parent is not correct", quux.getParent() == foo);
		assertTrue("parent is not correct", comment.getParent() == null);

		try {
			children.remove(48);
			fail("Should have thrown an IndexOutOfBoundsException");
		} catch(IndexOutOfBoundsException ex) {}
		try {
			children.remove(-3);
			fail("Should have thrown an IndexOutOfBoundsException");
		} catch(IndexOutOfBoundsException ex) {}
		try {
			content.remove(48);
			fail("Should have thrown an IndexOutOfBoundsException");
		} catch(IndexOutOfBoundsException ex) {}
		try {
			content.remove(-3);
			fail("Should have thrown an IndexOutOfBoundsException");
		} catch(IndexOutOfBoundsException ex) {}
    }

    public void test_TCM__boolean_remove_Object() {
		List content = foo.getContent();
		List children = foo.getChildren();
		
		// \n, bar, \n, baz, \n, comment, quux, \n
		content.remove(text1); // first /n
		children.remove(bar); // bar
		content.remove(comment); // comment
		content.remove(text2); // second /n
		// baz, \n, quux, \n

		assertTrue("bad removal", children.size() == 2);
		assertTrue("bad removal", children.get(0) == baz);
		assertTrue("bad removal", children.get(1) == quux);
		assertTrue("bad removal", content.size() == 4);
		assertTrue("bad removal", content.get(0) == baz);
		assertTrue("bad removal", content.get(1).equals(text3));
		assertTrue("bad removal", content.get(2) == quux);
		assertTrue("bad removal", content.get(3).equals(text4));

		assertTrue("parent is not correct", bar.getParent() == null);
		assertTrue("parent is not correct", baz.getParent() == foo);
		assertTrue("parent is not correct", quux.getParent() == foo);
		assertTrue("parent is not correct", comment.getParent() == null);
    }

    public void xtest_TCM__boolean_isEmpty() {
        fail("implement me !");
    }

    public void xtest_TCM__boolean_containsAll_Collection() {
        fail("implement me !");
    }

    public void xtest_TCM__boolean_addAll_Collection() {
        fail("implement me !");
    }

    public void xtest_TCM__boolean_addAll_int_Collection() {
        fail("implement me !");
    }

    public void xtest_TCM__boolean_removeAll_Collection() {
        fail("implement me !");
    }

    public void xtest_TCM__boolean_retainAll_Collection() {
        fail("implement me !");
    }

    public void xtest_TCM__List_subList_int_int() {
        fail("implement me !");
    }

	// We'll assume that this is a decent test of iterator(), 
	// listIterator(), and listIterator(int).
    public void test_TCM__ListIterator_listIterator_int() {
		List children = foo.getChildren();
		ListIterator iter = children.listIterator(1);

		// next
		assertTrue("hasPrevious is false", iter.hasPrevious() == true);
		assertTrue("hasNext is false", iter.hasNext() == true);
		assertEquals("wrong element from get", baz, iter.next());
		assertTrue("hasNext is false", iter.hasNext() == true);
		assertEquals("wrong element from get", quux, iter.next());
		assertTrue("hasNext is true", iter.hasNext() == false);

		// prev
		assertTrue("hasPrevious is false", iter.hasPrevious() == true);
		assertEquals("wrong element from get", quux, iter.previous());
		assertTrue("hasPrevious is false", iter.hasPrevious() == true);
		assertEquals("wrong element from get", baz, iter.previous());
		assertTrue("hasPrevious is false", iter.hasPrevious() == true);
		assertEquals("wrong element from get", bar, iter.previous());
		assertTrue("hasPrevious is true", iter.hasPrevious() == false);
		assertTrue("hasNext is false", iter.hasNext() == true);

		List content = foo.getContent();
		ListIterator iter2 = content.listIterator(1);

		// next
		assertTrue("hasPrevious is false", iter2.hasPrevious() == true);
		assertTrue("hasNext is false", iter2.hasNext() == true);
		assertEquals("wrong element from get", bar, iter2.next());
		assertTrue("hasNext is false", iter2.hasNext() == true);
		assertEquals("wrong element from get", text2, iter2.next());
		assertTrue("hasNext is false", iter2.hasNext() == true);
		assertEquals("wrong element from get", baz, iter2.next());
		assertTrue("hasNext is false", iter2.hasNext() == true);
		assertEquals("wrong element from get", text3, iter2.next());
		assertTrue("hasNext is false", iter2.hasNext() == true);
		assertEquals("wrong element from get", comment, iter2.next());
		assertTrue("hasNext is false", iter2.hasNext() == true);
		assertEquals("wrong element from get", quux, iter2.next());
		assertTrue("hasNext is false", iter2.hasNext() == true);
		assertEquals("wrong element from get", text4, iter2.next());
		assertTrue("hasNext is true", iter2.hasNext() == false);

		// prev
		assertTrue("hasPrevious is false", iter2.hasPrevious() == true);
		assertEquals("wrong element from get", text4, iter2.previous());
		assertTrue("hasPrevious is false", iter2.hasPrevious() == true);
		assertEquals("wrong element from get", quux, iter2.previous());
		assertTrue("hasPrevious is false", iter2.hasPrevious() == true);
		assertEquals("wrong element from get", comment, iter2.previous());
		assertTrue("hasPrevious is false", iter2.hasPrevious() == true);
		assertEquals("wrong element from get", text3, iter2.previous());
		assertTrue("hasPrevious is false", iter2.hasPrevious() == true);
		assertEquals("wrong element from get", baz, iter2.previous());
		assertTrue("hasPrevious is false", iter2.hasPrevious() == true);
		assertEquals("wrong element from get", text2, iter2.previous());
		assertTrue("hasPrevious is false", iter2.hasPrevious() == true);
		assertEquals("wrong element from get", bar, iter2.previous());
		assertTrue("hasPrevious is false", iter2.hasPrevious() == true);
		assertEquals("wrong element from get", text1, iter2.previous());
		assertTrue("hasPrevious is true", iter2.hasPrevious() == false);
		assertTrue("hasNext is false", iter2.hasNext() == true);

		try {
			children.listIterator(48);
			fail("Should have thrown an IndexOutOfBoundsException");
		} catch(IndexOutOfBoundsException ex) {}
		try {
			children.listIterator(-3);
			fail("Should have thrown an IndexOutOfBoundsException");
		} catch(IndexOutOfBoundsException ex) {}
		try {
			content.listIterator(48);
			fail("Should have thrown an IndexOutOfBoundsException");
		} catch(IndexOutOfBoundsException ex) {}
		try {
			content.listIterator(-3);
			fail("Should have thrown an IndexOutOfBoundsException");
		} catch(IndexOutOfBoundsException ex) {}
    }

    public void test_TCM__ListIterator_listIterator_int2() {
		List children = foo.getChildren();
		ListIterator iter = children.listIterator(1);

		assertTrue("hasPrevious is false", iter.hasPrevious() == true);
		assertTrue("hasPrevious is false", iter.hasPrevious() == true);
		assertTrue("hasNext is false", iter.hasNext() == true);
		assertTrue("hasNext is false", iter.hasNext() == true);
		assertEquals("wrong element from get", baz, iter.next());
		assertEquals("wrong element from get", baz, iter.previous());
		assertEquals("wrong element from get", baz, iter.next());
		assertEquals("wrong element from get", baz, iter.previous());
		assertTrue("hasPrevious is false", iter.hasPrevious() == true);
		assertTrue("hasPrevious is false", iter.hasPrevious() == true);
		assertEquals("wrong element from get", bar, iter.previous());
		assertEquals("wrong element from get", bar, iter.next());
		assertEquals("wrong element from get", bar, iter.previous());
		assertTrue("hasPrevious is true", iter.hasPrevious() == false);
		assertTrue("hasPrevious is true", iter.hasPrevious() == false);
		assertTrue("hasNext is false", iter.hasNext() == true);
		assertTrue("hasNext is false", iter.hasNext() == true);

		List content = foo.getContent();
		ListIterator iter2 = content.listIterator(1);

		// next
		assertEquals("wrong element from get", bar, iter2.next());
		assertEquals("wrong element from get", text2, iter2.next());
		assertTrue("hasNext is false", iter2.hasNext() == true);
		assertTrue("hasNext is false", iter2.hasNext() == true);
		assertEquals("wrong element from get", baz, iter2.next());
		assertTrue("hasNext is false", iter2.hasNext() == true);
		assertTrue("hasNext is false", iter2.hasNext() == true);
		assertEquals("wrong element from get", text3, iter2.next());
		assertEquals("wrong element from get", comment, iter2.next());
		assertTrue("hasNext is false", iter2.hasNext() == true);
		assertTrue("hasNext is false", iter2.hasNext() == true);
		assertEquals("wrong element from get", quux, iter2.next());
		assertEquals("wrong element from get", quux, iter2.previous());
		assertEquals("wrong element from get", comment, iter2.previous());
		assertEquals("wrong element from get", text3, iter2.previous());
		assertEquals("wrong element from get", baz, iter2.previous());
		assertTrue("hasPrevious is false", iter2.hasPrevious() == true);
		assertTrue("hasPrevious is false", iter2.hasNext() == true);
		assertEquals("wrong element from get", text2, iter2.previous());
		assertTrue("hasPrevious is false", iter2.hasPrevious() == true);
		assertTrue("hasPrevious is false", iter2.hasNext() == true);
		assertEquals("wrong element from get", text2, iter2.next());
		assertTrue("hasPrevious is false", iter2.hasPrevious() == true);
		assertTrue("hasPrevious is false", iter2.hasNext() == true);
		assertEquals("wrong element from get", text2, iter2.previous());
		assertTrue("hasPrevious is false", iter2.hasPrevious() == true);
		assertTrue("hasPrevious is false", iter2.hasPrevious() == true);
		assertEquals("wrong element from get", bar, iter2.previous());
		assertEquals("wrong element from get", text1, iter2.previous());
		assertTrue("hasNext is false", iter2.hasNext() == true);
		assertTrue("hasNext is false", iter2.hasNext() == true);
    }

	// When we add the first attribute or child, or when we replace the list of
	// attributes or children, we may replace the underlying list reference 
	// in the Element. We need to make sure that any previously-created FilterLists
	// somehow get updated.
    public void test_list_replacement() {
		Element el = new Element("parent");

		List attr = el.getAttributes();
		el.setAttribute("test", "test");
		assertEquals("wrong list size after adding attribute", 1, attr.size());
		ArrayList attr2 = new ArrayList();
		attr2.add(new Attribute("test", "test"));
		attr2.add(new Attribute("test2", "test2"));
		el.setAttributes(attr2);
		assertEquals("wrong list size after replacing attribute", 2, attr.size());

		List content = el.getContent();
		el.addContent(new Element("test"));
		assertEquals("wrong list size after adding content", 1, content.size());
		ArrayList content2 = new ArrayList();
		content2.add(new Element("test"));
		content2.add(new Element("test2"));
		el.setContent(content2);
		content.size();
		assertEquals("wrong list size after replacing content", 2, content.size());
	}

	private void dump(List list) {
		System.out.println("---");
		Iterator iter = list.iterator();
		while(iter.hasNext()) {
			System.out.println("> " + iter.next());
		}
	}
}
