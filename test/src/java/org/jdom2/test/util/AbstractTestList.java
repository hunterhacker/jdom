package org.jdom2.test.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.jdom2.test.util.UnitTestUtil.*;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.junit.Test;

import org.jdom2.internal.ArrayCopy;

/**
 * This base class can be used to test multiple implementations of List<?>
 * It is reusable for different list types, and does the 'normal' type testing.
 * 
 * 
 * 
 * @author Rolf Lear
 *
 */
@SuppressWarnings({ "unchecked", "javadoc" })
public abstract class AbstractTestList<T> {
	
	/**
	 * The following is a list of somewhat binary-progressive prime numbers.
	 * sequence is +2, +2, +2, +2, +4, +4, +4, +4, +8, +8, +8, +8,
	 *            +16, +16, +16, +16, +32, ......
	 * given the above sequence, get the first prime number that is larger than
	 * the sequence value.
	 */
	private static final int[] BIGPRIMES = new int[] {3, 5, 7, 11, 13, 17, 23, 
		29, 37, 41, 53, 59, 73, 89, 107, 127, 157, 191, 223, 251, 313, 379, 443, 
		509, 641, 761, 907, 1019, 1277, 1531, 1787, 2053, 2557, 3067, 3581, 
		4091, 5113, 6143, 7177, 8191, 10243, 12281, 14341, 16381, 20477, 24571, 
		28669, 32771, 40961, 49157, 57347, 65537, 81919, 98297, 114689, 131071, 
		163841, 196613, 229373, 262139, 327673, 393209, 458747, 524287, 655357, 
		786431, 917503, 1048571};
	
	private static final int[][] SMALLSHUFFLE = new int[][] {
		new int[]{},
		new int[]{0},
		new int[]{1,0},
		new int[]{2, 0, 1},
		new int[]{2, 0, 3, 1},
		new int[]{4, 1, 2, 0, 3},
		new int[]{3, 0, 2, 4, 5, 1}
	};
	
	private final boolean i_nullok;
	private final Class<T> i_tclass;

	/**
	 * Generalised constructor for testing a list.
	 * @param tclass all content should be an instance (or subtype) of this class 
	 * @param nullok are null values allowed on the list.
	 * @param samplecontent some sample data to load in to the list. The more, the better.
	 */
	public AbstractTestList(Class<T> tclass, boolean nullok) {
		super();
		i_nullok = nullok;
		i_tclass = tclass;
	}

	/**
	 * Create a list of the type to test. It should be empty.
	 * @return the list to test.
	 */
	public abstract List<T> buildEmptyList();
	
	/**
	 * This is sample data that should be legal in the list.
	 * There should be at least 10 or so elements.
	 * @return the sample data.
	 */
	public abstract T[] buildSampleContent();
	
	/**
	 * This should contain at least one additional valid entry
	 * that can be legally be added to the list after it is already populated
	 * with the sample data.
	 * @return the extra data.
	 */
	public abstract T[] buildAdditionalContent();
	
	/**
	 * This should contain data that is of the correct generic type of the data
	 * but has some property that makes it illegal. This can be empty.... 
	 * @return the IllegalArgument test data.
	 */
	public abstract T[] buildIllegalArgumentContent();
	/**
	 * This list should be of content that is of the wrong class (a class not
	 * compatible with <T>
	 * @return the ClassCast test data.
	 */
	public abstract Object[] buildIllegalClassContent();

	
	/* *********************************
	 * Private support methods
	 * ********************************* */
	
	/**
	 * Create an array of the required type with the required length
	 * @param length The length of the array to create.
	 * @return the created array.
	 */
	protected final T[] buildArray(int length) {
		return (T[])Array.newInstance(i_tclass, length);
	}
	
	/**
	 * Remove an element from an array.
	 * @param content The original content.
	 * @param index The element to remove
	 * @return a new array with the specified element removed.
	 */
	protected final T[] arrayRemove(T[] content, int index) {
		if (index < 0 || index >= content.length) {
			throw new IllegalArgumentException("Can not have index " + index 
					+ " when there are only " + content.length + " elements.");
		}
		T[] ret = ArrayCopy.copyOf(content, content.length - 1);
		System.arraycopy(content, index + 1, ret, index, ret.length - index);
		return ret;
	}
	
	/**
	 * Insert an element in to the content at the specified index.
	 * @param content The base content to add to.
	 * @param index The position to insert (items from this position will be 
	 * 				moved to the right). Using content.lenght will add to the end.
	 * @param insert The value to insert
	 * @return The amended content array
	 */
	protected final T[] arrayInsert(T[] content, int index, T...insert) {
		if (index < 0 || index > content.length) {
			throw new IllegalArgumentException("Can not use index " + index 
					+ " when there is only " + content.length + " content.");
		}
		if (insert == null) {
			throw new NullPointerException("Can not have a null insert vararg array");
		}
		T[] ret = ArrayCopy.copyOf(content, content.length + insert.length);
		System.arraycopy(ret, index, ret, index+insert.length, content.length - index);
		System.arraycopy(insert, 0, ret, index, insert.length);
		return ret;
	}
	
	/**
	 * Reverse the contents of an array.
	 * @param content The array to reverse.
	 * @return a new array with the same content as the input, but in reverse
	 * 				order
	 */
	protected final T[] arrayReverse(T[] content) {
		T[] ret = ArrayCopy.copyOf(content, content.length);
		for (int i = (content.length - 1) / 2; i >= 0; i--) {
			final T tmp = ret[i];
			ret[i] = ret[ret.length - 1 - i];
			ret[ret.length - 1 - i] = tmp;
		}
		return ret;
	}
	
	private final int pickPrime(final int len) {
		if (len < SMALLSHUFFLE.length) {
			throw new IllegalStateException("Should have a prime set already");
		}
		int pindex = 0;
		while (pindex < BIGPRIMES.length && BIGPRIMES[pindex] <= (len / 2)) {
			pindex++;
		}
		if (pindex >= BIGPRIMES.length) {
			throw new IllegalStateException("Unable to create a shuffled order " +
					"for that many elements: " + len);
		}
		return BIGPRIMES[pindex];
	}
	
	/*
	 * helper method for shuffle().
	 */
	private final int shuffleCompute(final int offset, final int len, final int prime) {
		// Integer.MAX_INT is a prime number... but too big
		// Using a Prime in this way guarantees that we loop through all the values
		// in the sequence without having duplicates.
		return (offset + prime) % len;
	}
	
	/**
	 * Generate a (very) pseudo-random order of a given length.
	 * The shuffled order is always repeatable, predictable, but also
	 * appears 'random'. This is useful so that we can compare strange-ordered
	 * inserts/removes/sets.
	 * @param len How many elements to include.
	 * @return An array of int. Each value will be unique, from 0 to len-1
	 */
	protected final int[] shuffle(final int len) {
		if (len < SMALLSHUFFLE.length) {
			return ArrayCopy.copyOf(SMALLSHUFFLE[len], len);
		}
		final int prime = pickPrime(len);
		int[] ret = new int[len];
		int c = shuffleCompute(0, len, prime);
		for (int i = len - 1; i >= 0; i--) {
			c = shuffleCompute(c, len, prime);
			if (ret[c] != 0) {
				// this should never happen, but this is a double-check
				// guarantee that we never miss values in the sequence.
				throw new IllegalStateException("Oops");
			}
			ret[c] = i;
		}
		return ret;
	}
	
	/**
	 * Run a list though the basic paces of operation.
	 * @param list the List<T> to exercise.
	 * @param content the content (in the order) that we expect in the list.
	 */
	protected final void exercise(List<T> list, T...content) {
		assertTrue("List is null", list != null);
		assertTrue("Content is null", content != null);
		assertTrue(content.length == list.size());
		assertTrue(list.toString() != null);
		if (content.length == 0) {
			assertTrue(list.size() == 0);
			assertTrue(list.isEmpty());
		} else {
			assertTrue(list.size() > 0);
			assertFalse(list.isEmpty());
		}
		
		for (int i = 0; i < content.length; i++) {
			if (list.get(i) != content[i]) {
				fail(String.format("We expect element in list at position %d to be %s",
						i, content[i]));
			}
			int pos = list.indexOf(content[i]);
			assertTrue(pos >= 0);
			if (pos != i) {
				// may be duplicates in the list....
				assertTrue(pos < i); // but pos must be first...
				assertEquals(content[pos], content[i]);
			}
			assertTrue(list.contains(content[i]));
		}
		
		for (int i = content.length - 1; i >= 0; i--) {
			assertTrue(list.get(i) == content[i]);
			int pos = list.lastIndexOf(content[i]);
			assertTrue(pos >= 0);
			if (pos != i) {
				// may be duplicates in the list....
				assertTrue(pos > i); // but pos must be later...
				assertEquals(content[pos], content[i]);
			}
		}
		
		quickCheck(list, content);
		
		{
			// Check the iteration code.
			// the iterator implementation can be different to the ListIterator
			Iterator<T> it = list.iterator();
			try {
				it.remove();
				failNoException(IllegalStateException.class);
			} catch (Exception e) {
				checkException(IllegalStateException.class, e);
			}
			int i = 0;
			while (i < content.length) {
				assertTrue(it.hasNext());
				assertTrue(it.next() == content[i]);
				it.remove();
				try {
					it.remove();
					failNoException(IllegalStateException.class);
				} catch (Exception e) {
					checkException(IllegalStateException.class, e);
				}
				i++;
			}
			assertFalse(it.hasNext());
			try {
				it.next();
				failNoException(NoSuchElementException.class);
			} catch (Exception e) {
				checkException(NoSuchElementException.class, e);
			}
			
			try {
				it.remove();
				failNoException(IllegalStateException.class);
			} catch (Exception e) {
				checkException(IllegalStateException.class, e);
			}
			
			for (T d : content) {
				list.add(d);
			}
			
		}
		
		quickCheck(list, content);
		
		{
			//Read-Only List iteration through list contents.
			for (int origin = 0; origin <= content.length; origin++) {
				// start at every place 
				ListIterator<T> li = list.listIterator(origin);
				
				for (int i = origin; i < content.length; i++) {
					assertTrue(li.hasNext());
					assertTrue(li.nextIndex() == i);
					assertTrue(li.previousIndex() == i - 1);
					T emt = li.next();
					assertTrue(content[i] == emt);
				}
				
				assertTrue(li.nextIndex() == content.length);
				assertFalse(li.hasNext());
				try {
					li.next();
					failNoException(NoSuchElementException.class);
				} catch (Exception e) {
					checkException(NoSuchElementException.class, e);
				}
				
				assertTrue(li.previousIndex() == (content.length - 1));
				for (int i = content.length - 1; i >= 0; i--) {
					assertTrue(li.previousIndex() == i);
					assertTrue(li.nextIndex() == i + 1);
					assertTrue(li.hasPrevious());
					T emt = li.previous();
					assertTrue(content[i] == emt);
				}
				assertTrue(li.previousIndex() == -1);
				assertFalse(li.hasPrevious());
				try {
					li.previous();
					failNoException(NoSuchElementException.class);
				} catch (Exception e) {
					checkException(NoSuchElementException.class, e);
				}
				
				assertTrue(li.nextIndex() == 0);
				for (int i = 0; i < origin; i++) {
					assertTrue(li.hasNext());
					assertTrue(li.nextIndex() == i);
					assertTrue(content[i] == li.next());
				}
				
				ListIterator<T> fbli = list.listIterator(origin);
				assertTrue(fbli.nextIndex() == origin);
				assertTrue(fbli.previousIndex() == (origin - 1));
				
				
			}
		}
		{
			//Read/write List iteration through list contents.
			for (int origin = 0; origin <= content.length; origin++) {
				// start at every place 
				ListIterator<T> li = list.listIterator(origin);
				
				for (int i = origin; i < content.length; i++) {
					assertTrue(li.hasNext());
					assertTrue(li.nextIndex() == i);
					assertTrue(li.previousIndex() == i - 1);
					T emt = li.next();
					assertTrue(content[i] == emt);
					int n = li.nextIndex();
					int p = li.previousIndex();
					li.remove();
					assertTrue(p - 1 == li.previousIndex());
					assertTrue(n - 1 == li.nextIndex());
					try {
						li.remove();
						failNoException(IllegalStateException.class);
					} catch (Exception e) {
						checkException(IllegalStateException.class, e);
					}
					li.add(emt);
					assertTrue(p == li.previousIndex());
					assertTrue(n == li.nextIndex());
				}
				
				assertTrue(li.nextIndex() == content.length);
				assertFalse(li.hasNext());
				try {
					li.next();
					failNoException(NoSuchElementException.class);
				} catch (Exception e) {
					checkException(NoSuchElementException.class, e);
				}
				
				assertTrue(li.previousIndex() == (content.length - 1));
				for (int i = content.length - 1; i >= 0; i--) {
					assertTrue(li.previousIndex() == i);
					assertTrue(li.nextIndex() == i + 1);
					assertTrue(li.hasPrevious());
					T emt = li.previous();
					assertTrue(content[i] == emt);
					int p = li.previousIndex();
					int n = li.nextIndex();
					li.remove();
					assertTrue(p == li.previousIndex());
					assertTrue(n == li.nextIndex());
					try {
						li.remove();
						failNoException(IllegalStateException.class);
					} catch (Exception e) {
						checkException(IllegalStateException.class, e);
					}
					li.add(emt);
					assertTrue(content[i] == li.previous());
					assertTrue(p == li.previousIndex());
					assertTrue(n == li.nextIndex());
				}
				assertTrue(li.previousIndex() == -1);
				assertFalse(li.hasPrevious());
				try {
					li.previous();
					failNoException(NoSuchElementException.class);
				} catch (Exception e) {
					checkException(NoSuchElementException.class, e);
				}
				
				assertTrue(li.nextIndex() == 0);
				for (int i = 0; i < origin; i++) {
					assertTrue(li.hasNext());
					assertTrue(li.nextIndex() == i);
					assertTrue(content[i] == li.next());
				}
				
				ListIterator<T> fbli = list.listIterator(origin);
				assertTrue(fbli.nextIndex() == origin);
				assertTrue(fbli.previousIndex() == (origin - 1));
				
				
			}
			
			quickCheck(list, content);
		}
		
		{
			// remove all, and re-add them.
			ListIterator<T> it = list.listIterator(0);
			for (int i = 0; i < content.length; i++) {
				assertTrue(it.hasNext());
				assertTrue(it.next() == content[i]);
				it.remove();
			}
			for (int i = 0; i < content.length; i++) {
				assertFalse(it.hasNext());
				it.add(content[i]);
				assertTrue(it.hasPrevious());
				assertFalse(it.hasNext());
				assertTrue(content[i] == it.previous());
				assertTrue(content[i] == it.next());
				assertFalse(it.hasNext());
			}
			assertFalse(it.hasNext());
			quickCheck(list, content);
			
			// then do it backwards.
			it = list.listIterator(content.length);
			for (int i = content.length - 1; i >= 0; i--) {
				assertTrue(it.hasPrevious());
				assertTrue(it.previous() == content[i]);
				it.remove();
			}
			for (int i = content.length - 1; i >= 0; i--) {
				assertFalse(it.hasPrevious());
				it.add(content[i]);
				assertTrue(it.hasPrevious());
				assertTrue(content[i] == it.previous());
				assertFalse(it.hasPrevious());
				assertTrue(it.hasNext());
			}
			assertFalse(it.hasPrevious());
			quickCheck(list, content);
							
		}
		
		
		if (content.length > 0) {
			// Check the iterator set() method.
			// first forward....
			
			ListIterator<T> li = list.listIterator();
			
			try {
				li.set(content[0]);
				failNoException(IllegalStateException.class);
			} catch (Exception e) {
				checkException(IllegalStateException.class, e);
			}
			
			
			T tmpa = li.next();
			li.remove();
			for (int i = 1; i < content.length; i++) {
				T tmpb = li.next();
				assertTrue(content[i] == tmpb);
				li.set(tmpa);
				tmpa = tmpb;
			}
			li.add(tmpa);
			quickCheck(list, content);
			
			// then backward ....
			try {
				li.set(content[0]);
				failNoException(IllegalStateException.class);
			} catch (Exception e) {
				checkException(IllegalStateException.class, e);
			}
			
			
			tmpa = li.previous();
			li.remove();
			for (int i = content.length - 2; i >= 0; i--) {
				T tmpb = li.previous();
				assertTrue(content[i] == tmpb);
				li.set(tmpa);
				tmpa = tmpb;
			}
			li.add(tmpa);
			quickCheck(list, content);
		}
		
	}
	
	private void illegalExercise(List<T> list, T[] content, T d,
			Class<? extends Exception> eclass) {
		
		quickCheck(list, content);
		
		try {
			list.add(d);
			failNoException(eclass);
		} catch (Exception e) {
			checkException(eclass, e);
		}
		
		try {
			list.addAll(Collections.singleton(d));
			failNoException(eclass);
		} catch (Exception e) {
			checkException(eclass, e);
		}
		
		for (int i = list.size() - 1; i >= 0; i--) {
			try {
				list.set(i, d);
				failNoException(eclass);
			} catch (Exception e) {
				checkException(eclass, e);
			}
		}
		
		for (int i = list.size(); i >= 0; i--) {
			try {
				list.add(i, d);
				failNoException(eclass);
			} catch (Exception e) {
				checkException(eclass, e);
			}

			try {
				list.addAll(i, Collections.singletonList(d));
				failNoException(eclass);
			} catch (Exception e) {
				checkException(eclass, e);
			}

			try {
				list.addAll(i, Collections.singletonList(d));
				failNoException(eclass);
			} catch (Exception e) {
				checkException(eclass, e);
			}
		}

		
		quickCheck(list, content);
		
		ListIterator<T> li = list.listIterator();
		for (int i = 0; i < list.size(); i++) {
			assertTrue(li.hasNext());
			assertTrue(content[i] == li.next());
			try {
				li.add(d);
				failNoException(eclass);
			} catch (Exception e) {
				checkException(eclass, e);
			}
			// The AbstractList class increments the cursor on the iterator
			// even if the add fails...
			// This poor accounting means that th FilterList (which does the right thing)
			// can't be tested as well... Create this artificail moveTo() to
			// fix the AbstractList failings.
			li = moveTo(list, i);
			
			T prev = li.previous();
			assertTrue(content[i] == prev);
			assertTrue(content[i] == li.next());
			
			try {
				li.set(d);
				failNoException(eclass);
			} catch (Exception e) {
				checkException(eclass, e);
			}
			// The ContentList class modifies the modcount for set()
			// even if the set() fails...
			// This poor accounting means that the FilterList (which does the right thing)
			// can't be tested as well... Create this artificail moveTo() to
			// fix the AbstractList failings.
			li = moveTo(list, i);
			
		}

		quickCheck(list, content);
		
	}

	private ListIterator<T> moveTo(List<T> list, int i) {
		ListIterator<T> li = list.listIterator();
		while (i-- >= 0) {
			assertTrue(li.hasNext());
			li.next();
		}
		return li;
	}

	protected void quickCheck(List<T> list, T[] content) {
		// Simple iteration through the list contents.
		Iterator<T> it = list.iterator();
		int i = 0;
		while (i < content.length) {
			assertTrue(it.hasNext());
			assertTrue(content[i] == it.next());
			i++;
		}
		assertFalse(it.hasNext());
		try {
			T overflow = it.next();
			fail("There should be no element after hasNext() fails, but we got: " + overflow);
		} catch (NoSuchElementException nsee) {
			// this is what should happen!
		}
	}
	
	

	/* *********************************
	 * Test the quality of the input data
	 * ********************************* */
	
	@Test
	public void testSamples() {
		for (T s : buildSampleContent()) {
			if (s != null && !i_tclass.isInstance(s)) {
				fail("We expect all sample data to be an instance of " + i_tclass.getName());
			}
		}
	}
	
	@Test
	public void testIllegalClassData() {
		for (Object o : buildIllegalClassContent()) {
			if (o == null || i_tclass.isInstance(o)) {
				fail("We expect all IllegalClass data to be something other than " + i_tclass.getName());
			}
		}
	}
	
	@Test
	public void testIllegalArgumentData() {
		for (T s : buildIllegalArgumentContent()) {
			if (s != null && !i_tclass.isInstance(s)) {
				fail("We expect all IllegalArgument data to be an instance of " + i_tclass.getName());
			}
		}
	}
	
	/* *********************************
	 * The actual tests to run.
	 * ********************************* */
	
	@Test
	public void testToString() {
		// basic run
		assertTrue(buildEmptyList().toString() != null);
	}
	
	@Test
	public void testEmpty() {
		List<T> empty = buildEmptyList();
		assertTrue(empty.size() == 0);
		assertTrue(empty.isEmpty());
		exercise(empty, buildArray(0));
	}
	
	@Test
	public void testAdd() {
		for (T s : buildSampleContent()) {
			List<T> list = buildEmptyList();
			assertTrue(list.isEmpty());
			assertTrue(list.add(s));
			exercise(list, s);
		}
		List<T> list = buildEmptyList();
		T[] content = buildSampleContent();
		for (T s : content) {
			assertTrue(list.add(s));
		}
		exercise(list, content);
	}
	
	@Test
	public void testAddAll() {
		for (T s : buildSampleContent()) {
			List<T> list = buildEmptyList();
			assertTrue(list.isEmpty());
			assertTrue(list.addAll(Arrays.asList(s)));
			exercise(list, s);
			list.clear();
		}
		List<T> list = buildEmptyList();
		T[] content = buildSampleContent();
		assertTrue(list.addAll(Arrays.asList(content)));
		exercise(list, content);
		T[] data = buildArray(0);
		assertFalse(list.addAll(Arrays.asList(data)));
	}
	
	@Test
	public void testAddAllInt() {
		for (T s : buildSampleContent()) {
			List<T> list = buildEmptyList();
			assertTrue(list.isEmpty());
			assertTrue(list.addAll(0, Arrays.asList(s)));
			exercise(list, s);
			list.clear();
		}
		List<T> list = buildEmptyList();
		T[] content = buildSampleContent();
		assertTrue(list.addAll(0, Arrays.asList(content)));
		exercise(list, content);
		T[] data = buildArray(0);
		assertFalse(list.addAll(0, Arrays.asList(data)));
	}
	
	@Test
	public void testIllegalAddAllInt() {
		final T[] illegal = buildIllegalArgumentContent();
		if (illegal.length <= 0) {
			// for android.
			//Assume.assumeTrue(illegal.length > 0);
			return;
		}
		final T[] extra = buildAdditionalContent();
		if (extra.length <= 0) {
			// for android.
			//Assume.assumeTrue(extra.length > 0);
			return;
		}
		final T[] content = buildSampleContent();
		if (content.length <= 0) {
			// for android.
			//Assume.assumeTrue(content.length > 0);
			return;
		}
		T[] toadd = ArrayCopy.copyOf(extra, extra.length + illegal.length);
		System.arraycopy(illegal, 0, toadd, extra.length, illegal.length);
		
		// right, we have legal content in 'content', and then in 'illegal' we
		// have some legal content, and then some illegal content.
		
		// populate the list.
		List<T> list = buildEmptyList();
		assertTrue(list.addAll(0, Arrays.asList(content)));
		quickCheck(list, content);
		
		// check that the first to-add can be added.
		list.add(0, toadd[0]);
		//then remove it again.
		assertTrue(toadd[0] == list.remove(0));
		
		for (int i = 0; i <= content.length; i++) {
		
			quickCheck(list, content);
			
			// now, add the illegal, and then inspect the list...
			try {
				list.addAll(i, Arrays.asList(toadd));
				failNoException(IllegalArgumentException.class);
			} catch (Exception e) {
				checkException(IllegalArgumentException.class, e);
			}
			
			// make sure that the member that previously could be added can
			// still be added.
			list.add(0, toadd[0]);
			//then remove it again.
			assertTrue(toadd[0] == list.remove(0));
			
			// make sure it's all OK.
			exercise(list, content);
			
			if (content.length < 2) {
				// for android.
				// Assume.assumeTrue(content.length >= 2);
				return;
			}
			
			// now check to make sure that concurrency is not affected....
			Iterator<T> it = list.iterator();
			// move it along at least once.....
			assertTrue(content[0] == it.next());
			// now do a failed addAll.
			try {
				list.addAll(i, Arrays.asList(toadd));
				failNoException(IllegalArgumentException.class);
			} catch (Exception e) {
				checkException(IllegalArgumentException.class, e);
			}
			// we should be able to move the iteratoe because the modcount should
			// not have been affected.....
			assertTrue(content[1] == it.next());
			
			
			
		}
		
	}
	
	@Test
	public void testClear() {
		List<T> list = buildEmptyList();
		assertTrue(list.addAll(Arrays.asList(buildSampleContent())));
		assertFalse(list.isEmpty());
		list.clear();
		assertTrue(list.isEmpty());
	}
	
	@Test
	public void testInsert() {
		for (T s : buildSampleContent()) {
			List<T> list = buildEmptyList();
			assertTrue(list.isEmpty());
			list.add(0, s);
			exercise(list, s);
		}
		List<T> list = buildEmptyList();
		T[] content = buildSampleContent();
		for (T s : content) {
			list.add(0, s);
		}
		exercise(list, arrayReverse(content));
		list.clear();
		
		int[] order = shuffle(content.length);
		T[] mix = buildArray(order.length);
		for (int i = 0; i < order.length; i++) {
			mix[i] = content[order[i]];
		}
		for (int i = 0; i < content.length; i++) {
			int pos = 0;
			for (int j = 0; j < order.length; j++) {
				if (order[j] < i) {
					pos++;
				}
				if (order[j] == i) {
					break;
				}
			}
			list.add(pos, content[i]);
		}
		exercise(list, mix);
		
	}
	
	@Test
	public void testNullAdd() {
		if (i_nullok) {
			List<T> list = buildEmptyList();
			assertTrue(list.add(null));
			assertTrue(list.get(0) == null);
			list.add(1, null);
			assertTrue(list.get(1) == null);
			assertTrue(list.addAll(Arrays.asList(buildArray(10))));
			assertTrue(list.size() == 12);
		} else {
			try {
				List<T> list = buildEmptyList();
				assertFalse(list.add(null));
				failNoException(NullPointerException.class);
			} catch (Exception e) {
				checkException(NullPointerException.class, e);
			}
			try {
				List<T> list = buildEmptyList();
				list.add(0, null);
				failNoException(NullPointerException.class);
			} catch (Exception e) {
				checkException(NullPointerException.class, e);
			}
			
			try {
				List<T> list = buildEmptyList();
				T[] data = buildSampleContent();
				for (int i = 0; i < data.length; i+= 2) {
					list.add(data[i]);
					data[i] = null;
				}
				if (data.length == 1) {
					data[0] = null;
				}
				list.addAll(0, Arrays.asList(data));
				failNoException(NullPointerException.class);
			} catch (Exception e) {
				checkException(NullPointerException.class, e);
			}
			
			try {
				List<T> list = buildEmptyList();
				T[] data = buildSampleContent();
				for (int i = 0; i < data.length; i+= 2) {
					list.add(data[i]);
					data[i] = null;
				}
				if (data.length == 1) {
					data[0] = null;
				}
				list.addAll(Arrays.asList(data));
				failNoException(NullPointerException.class);
			} catch (Exception e) {
				checkException(NullPointerException.class, e);
			}
			
			
		}
		
		try {
			List<T> list = buildEmptyList();
			Collection<T> data = null;
			list.addAll(0, data);
			failNoException(NullPointerException.class);
		} catch (Exception e) {
			checkException(NullPointerException.class, e);
		}
		
		try {
			List<T> list = buildEmptyList();
			Collection<T> data = null;
			list.addAll(Arrays.asList(buildSampleContent()));
			list.addAll(data);
			failNoException(NullPointerException.class);
		} catch (Exception e) {
			checkException(NullPointerException.class, e);
		}
	}
	
	@Test
	public void testIllegalIndex() {
		T[] data = buildSampleContent();
		try {
			List<T> list = buildEmptyList();
			list.add(-10, data[0]);
			failNoException(IndexOutOfBoundsException.class);
		} catch (Exception e) {
			checkException(IndexOutOfBoundsException.class, e);
		}
		try {
			List<T> list = buildEmptyList();
			list.add(1, data[0]);
			failNoException(IndexOutOfBoundsException.class);
		} catch (Exception e) {
			checkException(IndexOutOfBoundsException.class, e);
		}
		try {
			List<T> list = buildEmptyList();
			list.addAll(-10, Arrays.asList(data));
			failNoException(IndexOutOfBoundsException.class);
		} catch (Exception e) {
			checkException(IndexOutOfBoundsException.class, e);
		}
		try {
			List<T> list = buildEmptyList();
			list.addAll(1, Arrays.asList(data));
			failNoException(IndexOutOfBoundsException.class);
		} catch (Exception e) {
			checkException(IndexOutOfBoundsException.class, e);
		}
		try {
			List<T> list = buildEmptyList();
			list.set(-1, data[0]);
			failNoException(IndexOutOfBoundsException.class);
		} catch (Exception e) {
			checkException(IndexOutOfBoundsException.class, e);
		}
		try {
			List<T> list = buildEmptyList();
			list.set(1, data[0]);
			failNoException(IndexOutOfBoundsException.class);
		} catch (Exception e) {
			checkException(IndexOutOfBoundsException.class, e);
		}
		try {
			List<T> list = buildEmptyList();
			list.get(-1);
			failNoException(IndexOutOfBoundsException.class);
		} catch (Exception e) {
			checkException(IndexOutOfBoundsException.class, e);
		}
		try {
			List<T> list = buildEmptyList();
			list.get(1);
			failNoException(IndexOutOfBoundsException.class);
		} catch (Exception e) {
			checkException(IndexOutOfBoundsException.class, e);
		}

		try {
			List<T> list = buildEmptyList();
			list.remove(-10);
			failNoException(IndexOutOfBoundsException.class);
		} catch (Exception e) {
			checkException(IndexOutOfBoundsException.class, e);
		}
		try {
			List<T> list = buildEmptyList();
			list.remove(1);
			failNoException(IndexOutOfBoundsException.class);
		} catch (Exception e) {
			checkException(IndexOutOfBoundsException.class, e);
		}
	}
	
	@Test
	public void testRemove() {
		for (T s : buildSampleContent()) {
			List<T> list = buildEmptyList();
			assertTrue(list.isEmpty());
			assertTrue(list.add(s));
			exercise(list, s);
			assertTrue(list.remove(0) == s);
			exercise(list);
		}
		T[] samplecontent = buildSampleContent();
		for (int i = 0; i < samplecontent.length; i++) {
			T[] content = samplecontent;
			List<T> list = buildEmptyList();
			for (T s : content) {
				assertTrue(list.add(s));
			}
			exercise(list, content);
			assertTrue(content[i] == list.remove(i));
			content = arrayRemove(content, i);
			exercise(list, content);
			
			while (list.size() > i) {
				assertTrue(content[i] == list.remove(i));
				content = arrayRemove(content, i);
			}
			while (!list.isEmpty()) {
				assertTrue(content[content.length -1] == list.remove(list.size() - 1));
				content = arrayRemove(content, content.length - 1);
			}
			assertTrue(list.isEmpty());
		}
		List<T> list = buildEmptyList();
		for (T s : samplecontent) {
			assertTrue(list.add(s));
		}
		exercise(list, samplecontent);
		if (i_nullok) {
			list = buildEmptyList();
			assertTrue(list.add(null));
			assertTrue(list.get(0) == null);
		}
	}
	
	
	@Test
	public void testIllegalArgumentContent() {
		for (T d : buildIllegalArgumentContent()) {
			List<T> list = buildEmptyList();
			T[] content = buildSampleContent();
			for (T i : content) {
				list.add(i);
			}
			illegalExercise(list, content, d, IllegalArgumentException.class);
		}
	}

	@Test
	public void testIllegalClassContent() {
		for (Object d : buildIllegalClassContent()) {
			List<T> list = buildEmptyList();
			T[] content = buildSampleContent();
			for (T i : content) {
				list.add(i);
			}
			illegalExercise(list, content, (T)d, ClassCastException.class);
		}
	}

	@Test
	public void testNullContent() {
		if (i_nullok) {
			return;
		}
		List<T> list = buildEmptyList();
		T[] content = buildSampleContent();
		for (T i : content) {
			list.add(i);
		}
		illegalExercise(list, content, (T)null, NullPointerException.class);
	}


	@Test
	public void testConcurrentMod() {
		List<T> list = buildEmptyList();
		
		T[] sample = buildSampleContent();
		assertTrue("Not enough sample data " + sample.length, sample.length > 2);
		
		list.add(sample[0]);
		
		ListIterator<T> it = list.listIterator();
		Iterator<T> si = list.iterator();
		
		// It is important to add some content, and do a next()
		// before testing concurrency, because some iterator methods
		// check for the Iterator state before checking concurrency.
		// this puts the iterator in to a valid state for remove(), set(), etc.
		assertTrue(sample[0] == it.next());
		assertTrue(sample[0] == si.next());
		
		// create a concurrent issue.
		// we have checked for more than 2 elements so this should be fine.
		list.add(sample[1]);
		list.add(sample[2]);
		
		// hasNext() should never throw CME.
		assertTrue(it.hasNext());
		assertTrue(1 == it.nextIndex());
		
		// hasNext() should never throw CME.
		assertTrue(it.hasNext());
		try {
			it.next();
			failNoException(ConcurrentModificationException.class);
		} catch (Exception e) {
			checkException(ConcurrentModificationException.class, e);
		}

		
		it = list.listIterator(3);
		assertTrue(sample[2] == it.previous());
		
		// create a concurrent issue.
		list.remove(2);
		
		// hasPrevious() should never throw CME.
		assertTrue(it.hasPrevious());
		
		assertTrue(1 == it.previousIndex());

		// hasPrevious() should never throw CME.
		assertTrue(it.hasPrevious());
		try {
			it.previous();
			failNoException(ConcurrentModificationException.class);
		} catch (Exception e) {
			checkException(ConcurrentModificationException.class, e);
		}
		
		// hasPrevious() should never throw CME.
		assertTrue(it.hasPrevious());
		try {
			it.set(sample[sample.length - 1]);
			failNoException(ConcurrentModificationException.class);
		} catch (Exception e) {
			checkException(ConcurrentModificationException.class, e);
		}

		// hasPrevious() should never throw CME.
		assertTrue(it.hasPrevious());
		try {
			it.add(sample[sample.length - 1]);
			failNoException(ConcurrentModificationException.class);
		} catch (Exception e) {
			checkException(ConcurrentModificationException.class, e);
		}

		// hasPrevious() should never throw CME.
		assertTrue(it.hasPrevious());
		try {
			it.remove();
			failNoException(ConcurrentModificationException.class);
		} catch (Exception e) {
			checkException(ConcurrentModificationException.class, e);
		}

		
		
		// Now test the Simple Iterator concurrency
		
		// hasNext() should never throw CME.
		assertTrue(si.hasNext());
		
		try {
			si.next();
			failNoException(ConcurrentModificationException.class);
		} catch (Exception e) {
			checkException(ConcurrentModificationException.class, e);
		}

		try {
			si.remove();
			failNoException(ConcurrentModificationException.class);
		} catch (Exception e) {
			checkException(ConcurrentModificationException.class, e);
		}
		
	}
	
	@Test
	public void testConcurrentSetMod() {
		List<T> list = buildEmptyList();
		
		T[] sample = buildSampleContent();
		assertTrue("Not enough sample data " + sample.length, sample.length > 2);
		
		list.addAll(Arrays.asList(sample));
		quickCheck(list, sample);
		
		T tmp = list.remove(0);
		
		T[] tmpsamp = ArrayCopy.copyOfRange(sample, 1, sample.length);
		quickCheck(list, tmpsamp);

		// get a handle on our iterator....
		ListIterator<T> it = list.listIterator();
		
		// make sure a set of the underlying does not effect the iterator.
		tmpsamp[0] = tmp;
		list.set(0, tmp);
		quickCheck(list, tmpsamp);
		
		// next() should not throw CME because set() should not change modCount.
		assertTrue(tmp == it.next());
		
		// then, for kicks, restore the original list with
		it.add(sample[1]);
		quickCheck(list, sample);
	}
	
	
}
