package org.jdom.test.filterlist;

import org.jdom.*;
import java.util.*;

public final class FilterListIteratorRandomizer
{
	private boolean			elementsOnly;				// True if we're checking Element.getChildren(), false if Element.getContent()
	private ArrayList		objects = new ArrayList();	// All the children of the parent Element.
	private ArrayList		elements = new ArrayList(); // Only the Element children of the parent Element.
	private ArrayList		sourceList;					// Where to get test objects from - points to "objects" or "elements".
	private Random			random;						// Source of randomness.
	private Element			parent;						// Parent Element.
	private List			referenceList;				// Reference list we're currently using.
	private List			testList;					// Test list we're currently using.
	private ListIterator	referenceIter;				// Reference iterator we're currently using.
	private ListIterator	testIter;					// Test iterator we're currently using.
	private boolean			canModify;					// Can we call set() or remove() right now w/o throwing an exception.

	private static long		finished;					// Total number of iterations finished.
	private static long		lastRestart;				// Total number of iterations when we last restarted the iterators.

	// Constructor.
	public FilterListIteratorRandomizer(boolean elementsOnly, Random random,
										ArrayList objects, ArrayList elements,
										Element parent, List referenceList, List testList)
	{
		this.elementsOnly = elementsOnly;
		this.random = random;
		this.objects = objects;
		this.elements = elements;
		this.parent = parent;
		this.referenceList = referenceList;
		this.testList = testList;
		sourceList = (elementsOnly ? elements : objects);
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
			System.out.println("finished = " + finished + "; lastRestart = " + lastRestart);
			throw ex;
		}
	}

	private void internalTest(int iterations)
	{
		// The two lists should *always* be equivalent from now on.
		checkLists();
		//System.out.println("List size = " + referenceList.size());

		// Run the tests.
		for (int i = 0; i < iterations; i++)
		{
//			if (finished == 360165) setDebug(true);

			// Get the iterators. We get new ones if an exception occurred,
			// and every once in a while even if no exception occurred.
			if (random.nextDouble() < .01 || testIter == null)
			{
				// Test either listIterator() or listIterator(int).
				if (random.nextBoolean())
				{
					referenceIter = referenceList.listIterator();
					testIter = testList.listIterator();
				}
				else
				{
					int index = random.nextInt(testList.size() + 1);
					if (debug) System.out.println("index  = " + index );
					referenceIter = referenceList.listIterator(index);
					testIter = testList.listIterator(index);
				}
				canModify = false;
				lastRestart = finished;
			}

			if (debug) dumpLists();

			// Run one test.
			boolean exceptionOccurred = false;
			int test = random.nextInt(10);
			switch(test)
			{
				case 0:
					exceptionOccurred = testHasNext();
					break;
				case 1:
					exceptionOccurred = testNext();
					break;
				case 2:
					exceptionOccurred = testHasPrevious();
					break;
				case 3:
					exceptionOccurred = testPrevious();
					break;
				case 4:
					exceptionOccurred = testNextIndex();
					break;
				case 5:
					exceptionOccurred = testPreviousIndex();
					break;
				case 6:
					exceptionOccurred = testAdd();
					break;
				case 7:
					exceptionOccurred = testSet();
					break;
				default:
					// Note that this gets called more often than the others,
					// so that the list doesn't expand more than it contracts. 
					exceptionOccurred = testRemove();
					break;
			}

			// If an exception (correctly) occurred, then we start over with new
			// iterators. We make no guarantees as to the behavior of the iterators
			// after an exception. This is consistent with ArrayList's iterator:
			// If you call previous() when hasPrevious() would return false, it throws
			// an exception, and then gets stuff, so the iterator becomes unusable.
			// (Strangely, next() works better...)
			if (exceptionOccurred)
				referenceIter = testIter = null;

			finished++;
			if (finished % 10000 == 0)
				System.out.println("FLI: Finished " + finished + " iterations; list size is " + referenceList.size());
		}
	}

	///////////////////////////////////////////////////////////

	private boolean testHasNext()
	{
		if (debug) System.out.println("## testHasNext");
		assertEquals(referenceIter.hasNext(), testIter.hasNext());
		return true;
	}

	private boolean testNext()
	{
		// Most of the time, ensure that we don't throw an exception.
		// Otherwise, we'd be throwing away the iterator too often.
		if (!referenceIter.hasNext() && random.nextDouble() < .9)
			return false;

		if (debug) System.out.println("## testNext");
		Object result1, result2;
		try
		{
			result1 = referenceIter.next();
		}
		catch(Exception ex)
		{
			result1 = ex;
		}
		try
		{
			result2 = testIter.next();
		}
		catch(Exception ex)
		{
			result2 = ex;
		}

		assertEquals(result1, result2);

		canModify = true;
		return (result1 instanceof Exception);
	}

	private boolean testHasPrevious()
	{
		if (debug) System.out.println("## testHasPrevious");
		assertEquals(referenceIter.hasPrevious(), testIter.hasPrevious());
		return false;
	}

	private boolean testPrevious()
	{
		// Most of the time, ensure that we don't throw an exception.
		// Otherwise, we'd be throwing away the iterator too often.
		if (!referenceIter.hasPrevious() && random.nextDouble() < .9)
			return false;

		if (debug) System.out.println("## testPrevious");
		Object result1, result2;
		try
		{
			result1 = referenceIter.previous();
		}
		catch(Exception ex)
		{
			result1 = ex;
		}
		try
		{
			result2 = testIter.previous();
		}
		catch(Exception ex)
		{
			result2 = ex;
		}

		assertEquals(result1, result2);

		canModify = true;
		return (result1 instanceof Exception);
	}

	private boolean testNextIndex()
	{
		if (debug) System.out.println("## testNextIndex");
		assertEquals(referenceIter.nextIndex(), testIter.nextIndex());
		return false;
	}

	private boolean testPreviousIndex()
	{
		if (debug) System.out.println("## testPreviousIndex");
		assertEquals(referenceIter.previousIndex(), testIter.previousIndex());
		return false;
	}

	private boolean testAdd()
	{
		Object obj = randomItem();

		// Most of the time, ensure that we don't throw an exception.
		// Otherwise, we'd be throwing away the iterator too often.
		if (referenceList.contains(obj) && random.nextDouble() < .9)
			return false;

		if (debug) System.out.println("## testAdd");

		Object result1 = null, result2 = null;
		try
		{
			referenceIter.add(obj);
		}
		catch(Exception ex)
		{
			result1 = ex;
		}
		try
		{
			testIter.add(obj);
		}
		catch(Exception ex)
		{
			result2 = ex;
		}

		assertEquals(result1, result2);
		checkLists();

		canModify = false;
		return (result1 instanceof Exception);
	}

	private boolean testRemove()
	{
		// Most of the time, ensure that we don't throw an exception.
		// Otherwise, we'd be throwing away the iterator too often.
		if (!canModify && random.nextDouble() < .9)
			return false;

		if (debug) System.out.println("## testRemove");
		Object result1 = null, result2 = null;
		try
		{
			referenceIter.remove();
		}
		catch(Exception ex)
		{
			result1 = ex;
		}
		try
		{
			testIter.remove();
		}
		catch(Exception ex)
		{
			result2 = ex;
		}

		assertEquals(result1, result2);
		checkLists();

		canModify = false;
		return (result1 instanceof Exception);
	}

	private boolean testSet()
	{
		Object obj = randomItem();

		// Most of the time, ensure that we don't throw an exception.
		// Otherwise, we'd be throwing away the iterator too often.
		if ((!canModify || referenceList.contains(obj)) && random.nextDouble() < .9)
			return false;

		if (debug) System.out.println("## testSet");

		Object result1 = null, result2 = null;
		try
		{
			referenceIter.set(obj);
		}
		catch(Exception ex)
		{
			result1 = ex;
		}
		try
		{
			testIter.set(obj);
		}
		catch(Exception ex)
		{
			result2 = ex;
		}

		assertEquals(result1, result2);
		checkLists();

		return (result1 instanceof Exception);
	}

	///////////////////////////////////////////////////////////

	private void assertEquals(boolean value1, boolean value2)
	{
		if (debug) System.out.println("    [value  = " + value2 + "]");
		if (value1 != value2)
			throw new RuntimeException("Expected: " + value1 + "   Actual: " + value2);
	}

	private void assertEquals(int value1, int value2)
	{
		if (debug) System.out.println("    [value  = " + value2 + "]");
		if (value1 != value2)
			throw new RuntimeException("Expected: " + value1 + "   Actual: " + value2);
	}

	private void assertEquals(Object value1, Object value2)
	{
		if (debug) System.out.println("    [value  = " + value2 + "]");
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
			throw new RuntimeException("Expected: " + value1 + "   Actual: " + value2);
	}

	// Returns a random item from sourceList.
	private Object randomItem()
	{
		return sourceList.get(random.nextInt(sourceList.size()));
	}

	// After modification, call this method to ensure that the 
	// two lists are still equivalent.
	private void checkLists()
	{
		if (!ourEquals(referenceList, testList))
		{
			dumpLists();
			throw new RuntimeException("Lists are different");
		}
	}

	// Don't use iterators here, it messes up our debug printouts.
	private static boolean ourEquals(List list1, List list2)
	{
		int size = list1.size();
		if (list2.size() != size)
			return false;
		for (int i = 0; i < size; i++)
		{
			if (!list1.get(i).equals(list2.get(i)))
				return false;
		}
		return true;
	}

	// Dumps the contents of both lists to System.out.
	// Don't use iterators here, it messes up our debug printouts.
	private void dumpLists()
	{
		System.out.println(" >> Reference List (size=" + referenceList.size() + "):");
		for (int i = 0; i < referenceList.size(); i++)
			System.out.println("        " + referenceList.get(i));

		System.out.println(" >> Test List (size=" + testList.size() + "):");
		for (int i = 0; i < testList.size(); i++)
			System.out.println("        " + testList.get(i));

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

	private static boolean  debug;
}