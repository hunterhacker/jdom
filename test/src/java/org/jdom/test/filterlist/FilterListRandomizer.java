package org.jdom.test.filterlist;

import org.jdom.*;
import java.util.*;

public final class FilterListRandomizer
{
	private boolean			elementsOnly;				// True if we're checking Element.getChildren(), false if Element.getContent()
	private ArrayList		objects = new ArrayList();	// All the children of the parent Element.
	private ArrayList		elements = new ArrayList(); // Only the Element children of the parent Element.
	private ArrayList		sourceList;					// Where to get test objects from - points to "objects" or "elements".
	private Random			random;						// Source of randomness.
	private Element			parent;						// Parent Element.
	private List			referenceList;				// Reference list we're currently using.
	private List			testList;					// Test list we're currently using.

	private static int		finished;					// Total number of iterations finished.

	// Command-line entry point.
	public static void main(String[] args)
	{
		Random r = new Random(1234);

		System.out.println("Phase 1");
		for (int i = 0; i < 100; i++)
		{
			FilterListRandomizer tester = new FilterListRandomizer(true, r);
			tester.test(100000);
			FilterListRandomizer tester2 = new FilterListRandomizer(false, r);
			tester2.test(100000);
		}

		System.out.println("Phase 2");
		for (int i = 0; i < 100000; i++)
		{
			FilterListRandomizer tester = new FilterListRandomizer(true, r);
			tester.test(100);
			FilterListRandomizer tester2 = new FilterListRandomizer(false, r);
			tester2.test(100);
		}
	}

	// Constructor.
	public FilterListRandomizer(boolean elementsOnly, Random random)
	{
		this.elementsOnly = elementsOnly;
		this.random = random;
		init();
	}

	// Initialize our lists.
	private void init()
	{
		// Put some Elements into "elements" and "objects".
		for (int i = 1; i <= 9; i++)
		{
			Element el = new OurElement("Element" + i);
			objects.add(el);
			elements.add(el);
		}

		// Put some other objects into "objects" only.
		for (int i = 1; i <= 3; i++)
		{
			objects.add(new OurComment("Comment" + i));
			objects.add(new OurEntityRef("EntityRef" + i));
			objects.add(new OurProcessingInstruction("PI" + i, "data"));
			objects.add(new OurCDATA("CDATA" + i));
			objects.add(new OurText("Text" + i));
		}

		// Points to the list of items available for testing.
		// If we're testing getChildren(), then sourceList will point to the Elements.
		// If we're testing getContent(), then sourceList will
		// point to *all* the children.
		sourceList = (elementsOnly ? elements : objects);
	}

	// Populate the reference list and the parent Element with the same items.
	// (Actually not necessarily the same - the parent Element will be loaded
	// up with all types of children, but if elementsOnly is true, then only the
	// children that are Elements will go into the reference list.)
	private void populate()
	{
		// Clone the list of all objects, so we can take out one by one,
		// so we never add a duplicate.
		ArrayList clonedList = (ArrayList)objects.clone();

		// Add a random number of items.
		int count = random.nextInt(clonedList.size());
		for (int i = 0; i < count; i++)
		{
			// Pick a random item to add.
			Object obj = clonedList.remove(random.nextInt(clonedList.size())); 
			// Add it to the parent Element.
			addContent(parent, obj);
			// If we're doing elementsOnly, and this isn't an Element, then don't
			// add it to the reference list. Otherwise, do.
			if (!elementsOnly || obj instanceof Element)
			{
				// Special case: Element.addContent() will concatenate two adjacent Text nodes.
				// So we need to duplicate that behavior here.
				int last = referenceList.size() - 1;
				if (obj instanceof Text
						&& last >= 0 
						&& referenceList.get(last) instanceof Text)
				{
                    // We actually don't want to modify the Text node here. Since we're running
                    // the test list and the reference list in parallel, and since we're using
                    // the exact same objects in each one, then the addition of the Text node in
                    // the test list will cause the the same Text node that's at the end of
                    // *both* lists to be modified. i.e. they're both pointing to the same 
                    // Text node, so no need to modify it here, as it was already modified in 
                    // addContent() above.
				}
				else
				{
					referenceList.add(obj);
				}
			}
		}
	}

	// Adds the specified object to the parent Element.
	private static void addContent(Element parent, Object obj)
	{
		if (obj instanceof Element)
			parent.addContent((Element)obj);
		else if (obj instanceof Comment)
			parent.addContent((Comment)obj);
		else if (obj instanceof EntityRef)
			parent.addContent((EntityRef)obj);
		else if (obj instanceof ProcessingInstruction)
			parent.addContent((ProcessingInstruction)obj);
		else if (obj instanceof CDATA)
			parent.addContent((CDATA)obj);
		else if (obj instanceof Text)
			parent.addContent((Text)obj);
		else
			throw new RuntimeException("Unknown object type: " + obj);
	}

	// Do the tests.
	public void test(int iterations)
	{
		try
		{
			internalTest(iterations);
		}
		catch(RuntimeException ex)
		{
			System.out.println("finished = " + finished);
			throw ex;
		}
	}

	private void internalTest(int iterations)
	{
		// Setup the reference list and the parent Element.
		referenceList = new ReferenceList();
		parent = new Element("Parent");
		populate();
		// Get the list to be tested from the parent Element.
		testList = (elementsOnly ? parent.getChildren() : parent.getContent());
		// The two lists should *always* be equivalent from now on.
		checkLists();

		// Run the tests.
		for (int i = 0; i < iterations; i++)
		{
//			if (finished == 402) setDebug(true);
			if (debug) dumpLists();

			int test = random.nextInt(23);
			switch(test)
			{
				case 0:
					testHashCode();
					break;
				case 1:
					testEquals();
					break;
				case 2:
					testIndexOf();
					break;
				case 3:
					testLastIndexOf();
					break;
				case 4:
					testGet();
					break;
				case 5:
					testSet();
					break;
				case 6:
					testAdd();
					break;
				case 7:
					testAdd2();
					break;
				case 8:
					testSize();
					break;
				case 9:
					testRemove();
					break;
				case 10:
					testRemove2();
					break;
				case 11:
					testContains();
					break;
				case 12:
					testClear();
					break;
				case 13:
					testToArray();
					break;
				case 14:
					testToArray2();
					break;
				case 15:
					testIsEmpty();
					break;
				case 16:
					testIterator();
					break;
				case 17:
					testContainsAll();
					break;
				case 18:
					testAddAll();
					break;
				case 19:
					testAddAll2();
					break;
				case 20:
					testRemoveAll();
					break;
				case 21:
					testRetainAll();
					break;
				case 22:
					testSubList();
					break;
				default:
					throw new RuntimeException("Unknown test number " + test);
			}

			finished++;
			if (finished % 10000 == 0)
				System.out.println("FL:  Finished " + finished + " iterations; list size is " + referenceList.size());
		}
	}

	///////////////////////////////////////////////////////////

	private void testHashCode()
	{
		if (debug) System.out.println("## testHashCode");
		assertEquals(referenceList.hashCode(), testList.hashCode());
	}

	private void testEquals()
	{
		if (debug) System.out.println("## testEquals");
		assertTrue(referenceList.equals(testList));
	}

	private void testIndexOf()
	{
		Object obj = randomItem();
		if (debug) System.out.println("## testIndexOf");
		assertEquals(referenceList.indexOf(obj), testList.indexOf(obj));
	}

	private void testLastIndexOf()
	{
		Object obj = randomItem();
		if (debug) System.out.println("## testLastIndexOf");
		assertEquals(referenceList.lastIndexOf(obj), testList.lastIndexOf(obj));
	}

	private void testGet()
	{
		int index = random.nextInt(referenceList.size() + 1);
		if (debug) System.out.println("## testGet "+index);

		Object result1, result2;
		try
		{
			result1 = referenceList.get(index);
		}
		catch(Exception ex)
		{
			result1 = ex;
		}
		try
		{
			result2 = testList.get(index);
		}
		catch(Exception ex)
		{
			result2 = ex;
		}

		assertEquals(result1, result2);
	}

	private void testSet()
	{
		int index = random.nextInt(referenceList.size() + 1);
		Object obj = randomItem();
		if (debug) System.out.println("## testSet "+index+" "+obj);

		Object result1, result2;
		try
		{
			result1 = referenceList.set(index, obj);
		}
		catch(Exception ex)
		{
			result1 = ex;
		}
		try
		{
			result2 = testList.set(index, obj);
		}
		catch(Exception ex)
		{
			result2 = ex;
		}

		assertEquals(result1, result2);
		checkLists();
	}

	private void testAdd()
	{
		Object obj = randomItem();
		if (debug) System.out.println("## testAdd "+obj);

		Object result1, result2;
		try
		{
			result1 = new Boolean(referenceList.add(obj));
		}
		catch(Exception ex)
		{
			result1 = ex;
		}
		try
		{
			result2 = new Boolean(testList.add(obj));
		}
		catch(Exception ex)
		{
			result2 = ex;
		}

		assertEquals(result1, result2);
		checkLists();
	}

	private void testAdd2()
	{
		int index = random.nextInt(referenceList.size() + 1);
		Object obj = randomItem();
		if (debug) System.out.println("## testAdd2 "+index+" "+obj);

		Object result1 = null, result2 = null;
		try
		{
			referenceList.add(index, obj);
		}
		catch(Exception ex)
		{
			result1 = ex;
		}
		try
		{
			testList.add(index, obj);
		}
		catch(Exception ex)
		{
			result2 = ex;
		}

		assertEquals(result1, result2);
		checkLists();
	}

	private void testSize()
	{
		if (debug) System.out.println("## testSize");
		assertEquals(referenceList.size(), testList.size());
	}

	private void testRemove()
	{
		int index = random.nextInt(referenceList.size() + 1);
		if (debug) System.out.println("## testRemove "+index);

		Object result1, result2;
		try
		{
			result1 = referenceList.remove(index);
		}
		catch(Exception ex)
		{
			result1 = ex;
		}
		try
		{
			result2 = testList.remove(index);
		}
		catch(Exception ex)
		{
			result2 = ex;
		}

		assertEquals(result1, result2);
		checkLists();
	}

	private void testRemove2()
	{
		Object obj = randomItem();
		if (debug) System.out.println("## testRemove2 "+obj);

		Object result1, result2;
		try
		{
			result1 = new Boolean(referenceList.remove(obj));
		}
		catch(Exception ex)
		{
			result1 = ex;
		}
		try
		{
			result2 = new Boolean(testList.remove(obj));
		}
		catch(Exception ex)
		{
			result2 = ex;
		}

		assertEquals(result1, result2);
		checkLists();
	}

	private void testContains()
	{
		Object obj = randomItem();
		if (debug) System.out.println("## testContains "+obj);
		assertEquals(referenceList.contains(obj), testList.contains(obj));
	}

	private void testClear()
	{
		// Want to do this one less often - otherwise the list will
		// always be pretty short.
		if (random.nextDouble() < .9)
			return;

		if (debug) System.out.println("## testClear");

		referenceList.clear();
		testList.clear();
		checkLists();
	}

	private void testToArray()
	{
		if (debug) System.out.println("## testToArray");
		assertEquals(referenceList.toArray(), testList.toArray());
	}

	private void testToArray2()
	{
		if (debug) System.out.println("## testToArray2");

		// We test short, long, and just-right arrays.
		// And we test Object[], Element[], and (arbitrarily) CDATA[].
		Object[] param1, param2;
		int test = random.nextInt(9);
		switch(test)
		{
			case 0:
				param1 = new Object[0];
				param2 = new Object[0];
				break;
			case 1:
				param1 = new Object[referenceList.size()];
				param2 = new Object[referenceList.size()];
				break;
			case 2:
				param1 = new Object[100];
				param2 = new Object[100];
				break;
			case 3:
				param1 = new Element[0];
				param2 = new Element[0];
				break;
			case 4:
				param1 = new Element[referenceList.size()];
				param2 = new Element[referenceList.size()];
				break;
			case 5:
				param1 = new Element[100];
				param2 = new Element[100];
				break;
			case 6:
				param1 = new CDATA[0];
				param2 = new CDATA[0];
				break;
			case 7:
				param1 = new CDATA[referenceList.size()];
				param2 = new CDATA[referenceList.size()];
				break;
			case 8:
				param1 = new CDATA[100];
				param2 = new CDATA[100];
				break;
			default:
				throw new RuntimeException("Unknown toArray() test case: " + test);
		}

		Object result1, result2;
		try
		{
			result1 = referenceList.toArray(param1);
		}
		catch(Exception ex)
		{
			result1 = ex;
		}
		try
		{
			result2 = testList.toArray(param2);
		}
		catch(Exception ex)
		{
			result2 = ex;
		}

		assertEquals(result1, result2);
	}

	private void testIsEmpty()
	{
		if (debug) System.out.println("## testIsEmpty");
		assertEquals(referenceList.isEmpty(), testList.isEmpty());
	}

	private void testIterator()
	{
		if (debug) System.out.println("## testIterator");
	
		// Hand off to FilterListIteratorRandomizer.
		FilterListIteratorRandomizer r = new FilterListIteratorRandomizer(
					elementsOnly, random, objects, elements, parent, referenceList, testList);
		// This will end up testing the iterator a lot more than the list,
		// since the iterator is probably more complex (and more stateful).
		if (random.nextDouble() < .01)
			r.test(random.nextInt(5000));
		else
			r.test(random.nextInt(50));
		checkLists();
	}

	private void testContainsAll()
	{
		if (debug) System.out.println("## testContainsAll");

		ArrayList list = new ArrayList();
		int count = random.nextInt(4);
		for (int i = 0; i < count; i++)
			list.add(randomItem());

		assertEquals(referenceList.containsAll(list), testList.containsAll(list));
	}

	private void testAddAll()
	{
		if (debug) System.out.println("## testAddAll");

		ArrayList list = new ArrayList();
		int count = random.nextInt(4);
		for (int i = 0; i < count; i++)
			list.add(randomItem());

		Object result1, result2;
		try
		{
			result1 = new Boolean(referenceList.addAll(list));
		}
		catch(Exception ex)
		{
			result1 = ex;
		}
		try
		{
			result2 = new Boolean(testList.addAll(list));
		}
		catch(Exception ex)
		{
			result2 = ex;
			fixupObjects();
		}

		assertEquals(result1, result2);
		checkLists();
	}

	private void testAddAll2()
	{
		if (debug) System.out.println("## testAddAll2");

		int index = random.nextInt(referenceList.size() + 1);

		ArrayList list = new ArrayList();
		int count = random.nextInt(4);
		for (int i = 0; i < count; i++)
			list.add(randomItem());

		Object result1, result2;
		try
		{
			result1 = new Boolean(referenceList.addAll(index, list));
		}
		catch(Exception ex)
		{
			result1 = ex;
		}
		try
		{
			result2 = new Boolean(testList.addAll(index, list));
		}
		catch(Exception ex)
		{
			result2 = ex;
			fixupObjects();
		}

		assertEquals(result1, result2);
		checkLists();
	}

	private void testRemoveAll()
	{
		if (debug) System.out.println("## testRemoveAll");

		ArrayList list = new ArrayList();
		int count = random.nextInt(4);
		for (int i = 0; i < count; i++)
			list.add(randomItem());

		Object result1, result2;
		try
		{
			result1 = new Boolean(referenceList.removeAll(list));
		}
		catch(Exception ex)
		{
			result1 = ex;
		}
		try
		{
			result2 = new Boolean(testList.removeAll(list));
		}
		catch(Exception ex)
		{
			result2 = ex;
			fixupObjects();
		}

		assertEquals(result1, result2);
		checkLists();
	}

	private void testSubList()
	{
		if (debug) System.out.println("## testSubList");

		int index = random.nextInt(referenceList.size() + 1);
		int index2 = index + random.nextInt(referenceList.size() - index + 2);

		Object result1, result2;
		try
		{
			result1 = referenceList.subList(index, index2);
		}
		catch(Exception ex)
		{
			result1 = ex;
		}
		try
		{
			result2 = testList.subList(index, index2);
		}
		catch(Exception ex)
		{
			result2 = ex;
		}

		assertEquals(result1, result2);

		// This is not really a complete test...
	}

	private void testRetainAll()
	{
		if (debug) System.out.println("## testSubList");

		ArrayList list = new ArrayList();
		int count = random.nextInt(4);
		for (int i = 0; i < count; i++)
			list.add(randomItem());

		Object result1, result2;
		try
		{
			result1 = new Boolean(referenceList.retainAll(list));
		}
		catch(Exception ex)
		{
			result1 = ex;
		}
		try
		{
			result2 = new Boolean(testList.retainAll(list));
		}
		catch(Exception ex)
		{
			result2 = ex;
			fixupObjects();
		}

		assertEquals(result1, result2);
		checkLists();
	}

	///////////////////////////////////////////////////////////

	private void assertTrue(boolean value)
	{
		if (value == false)
			throw new RuntimeException("Expected: true   Actual: false");
	}

	private void assertEquals(boolean value1, boolean value2)
	{
		if (value1 != value2)
			throw new RuntimeException("Expected: " + value1 + "   Actual: " + value2);
	}

	private void assertEquals(int value1, int value2)
	{
		if (value1 != value2)
			throw new RuntimeException("Expected: " + value1 + "   Actual: " + value2);
	}

	private void assertEquals(Object value1, Object value2)
	{
		boolean areEqual;
		if (value1 == null)
		{
			areEqual = (value2 == null);
		}
		else if (value1 instanceof Object[])
		{
			areEqual = Arrays.equals((Object[])value1, (Object[])value2);
		}
		else if (value1 instanceof Exception)
		{
			areEqual = (value2 != null && value1.getClass().equals(value2.getClass()));
		}
		else
		{
			areEqual = (value1.equals(value2));
		}

		if (!areEqual)
		{
			if (value2 instanceof Exception)
				((Exception)value2).printStackTrace();
			throw new RuntimeException("Expected: " + value1 + "   Actual: " + value2);
		}
	}

	// Returns a random item from sourceList.
	private Object randomItem()
	{
		return sourceList.get(random.nextInt(sourceList.size()));
	}

	// Since our addAll() and removeAll() methods aren't perfect, we need
	// to do a little fixup if we want to be able to keep going.
	private void fixupObjects()
	{
		for (int i = 0; i < objects.size(); i++)
		{
			Object obj = objects.get(i);
			if (referenceList.contains(obj))
				setParent(obj, parent);
			else
				setParent(obj, null);
		}
	}

	// Sets the parent of the specified object.
	private static void setParent(Object obj, Element parent)
	{
		if (obj instanceof OurElement)
			((OurElement)obj).setParent(parent);
		else if (obj instanceof OurComment)
			((OurComment)obj).setParent(parent);
		else if (obj instanceof OurEntityRef)
			((OurEntityRef)obj).setParent(parent);
		else if (obj instanceof OurProcessingInstruction)
			((OurProcessingInstruction)obj).setParent(parent);
		else if (obj instanceof OurCDATA)
			((OurCDATA)obj).setParent(parent);
		else if (obj instanceof OurText)
			((OurText)obj).setParent(parent);
	}


	// After modification, call this method to ensure that the 
	// two lists are still equivalent.
	private void checkLists()
	{
		if (!referenceList.equals(testList))
		{
			dumpLists();
			throw new RuntimeException("Lists are different");
		}
	}

	// Dumps the contents of both lists to System.out.
	private void dumpLists()
	{
		System.out.println(" >> Reference List (size=" + referenceList.size() + "):");
		Iterator iter = referenceList.iterator();
		while(iter.hasNext())
			System.out.println("        " + iter.next());

		System.out.println(" >> Test List (size=" + testList.size() + "):");
		Iterator iter2 = testList.iterator();
		while(iter2.hasNext())
			System.out.println("        " + iter2.next());

		if (elementsOnly)
		{
			List content = parent.getContent();
			System.out.println(" >> parent (size=" + content.size() + "):");
			for (int i = 0; i < content.size(); i++)
				System.out.println("        " + content.get(i));
		}
	}

	private static void setDebug(boolean b)
	{
		debug = b;
	}

	private static boolean  debug = false;

	///////////////////////////////////////////////////////////
	//
	// This List class acts more like FilterList - it won't let you
	// add the same item twice. (FilterList will throw an exception
	// if you try to do this, because the item will already have
	// a parent. So we want to throw an exception too, to make the
	// lists completely equivalent.)

	private static class ReferenceList extends ArrayList
	{
		public Object set(int index, Object o)
		{
			if (this.get(index) != o)
				check(o);
			return super.set(index, o);
		}
		public boolean add(Object o)
		{
			check(o);
			return super.add(o);
		}
		public void add(int index, Object o)
		{
			check(o);
			super.add(index, o);
		}
		public boolean addAll(Collection c)
		{
			check(c);
			return super.addAll(c);
		}
		public boolean addAll(int index, Collection c)
		{
			check(c);
			return super.addAll(index, c);
		}

		private void check(Object o)
		{
			// TODO: According to the List interface spec, FilterList should probably
			// throw IllegalArgumentException, not IllegalAddException. But for now we just want
			// to be compatible with FilterList's behavior.
			if (this.contains(o))
				throw new IllegalAddException("Item is already in here.");
		}
		private void check(Collection c)
		{
			List list = (List)c; // cheat
			for (int i = 0; i < list.size(); i++)
			{
				Object obj = list.get(i);
				check(obj);
				for (int j = 0; j < list.size(); j++)
				{
					if (i != j && obj == list.get(j))
						throw new IllegalAddException("Can't add the same item twice.");
				}
			}
		}
	}

	public class OurElement extends Element
	{
		OurElement(String s)
		{
			super(s);
		}

		public Element setParent(Element parent)
		{
			return (Element) super.setParent(parent);
		}
	}

	public class OurComment extends Comment
	{
		OurComment(String s)
		{
			super(s);
		}

		public Comment setParent(Element parent)
		{
			return (Comment) super.setParent(parent);
		}
	}

	public class OurEntityRef extends EntityRef
	{
		OurEntityRef(String s)
		{
			super(s);
		}

		public EntityRef setParent(Element parent)
		{
			return (EntityRef) super.setParent(parent);
		}
	}

	public class OurProcessingInstruction extends ProcessingInstruction
	{
		OurProcessingInstruction(String s, String s2)
		{
			super(s, s2);
		}

		public ProcessingInstruction setParent(Element parent)
		{
			return (ProcessingInstruction) super.setParent(parent);
		}
	}

	public class OurCDATA extends CDATA
	{
		OurCDATA(String s)
		{
			super(s);
		}

		public Text setParent(Element parent)
		{
			return (Text) super.setParent(parent);
		}
	}

	public class OurText extends Text
	{
		OurText(String s)
		{
			super(s);
		}

		public Text setParent(Element parent)
		{
			return (Text) super.setParent(parent);
		}
	}
}
