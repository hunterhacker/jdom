/*--

 Copyright (C) 2000-2007 Jason Hunter & Brett McLaughlin.
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
    written permission, please contact <request_AT_jdom_DOT_org>.

 4. Products derived from this software may not be called "JDOM", nor
    may "JDOM" appear in their name, without prior written permission
    from the JDOM Project Management <request_AT_jdom_DOT_org).

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
 created by Jason Hunter <jhunter_AT_jdom_DOT_org> and
 Brett McLaughlin <brett_AT_jdom_DOT_org>.  For more information
 on the JDOM Project, please see <http://www.jdom.org/>.

 */

package org.jdom2;

import java.util.*;

import org.jdom2.filter.*;

/**
 * A non-public list implementation holding only legal JDOM content, including
 * content for Document or Element nodes. Users see this class as a simple List
 * implementation.
 *
 * @see     CDATA
 * @see     Comment
 * @see     Element
 * @see     EntityRef
 * @see     ProcessingInstruction
 * @see     Text
 *
 * @author  Alex Rosen
 * @author  Philippe Riand
 * @author  Bradley S. Huffman
 */
final class ContentList extends AbstractList<Content> implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private static final int INITIAL_ARRAY_SIZE = 5;

	/** Our backing list */
	private Content elementData[];
	private int size;
	
	/**
	 * modCount is used for concurrent modification,
	 * but dataModCount is used for refreshing filters. 
	 */
	private transient int dataModCount = Integer.MIN_VALUE;
	
	/** Document or Element this list belongs to */
	private final Parent parent;

	/** Force either a Document or Element parent */
	ContentList(Parent parent) {
		this.parent = parent;
	}

	/**
	 * Package internal method to support building from sources that are
	 * 100% trusted.
	 *
	 * @param c content to add without any checks
	 */
	final void uncheckedAddContent(Content c) {
		c.parent = parent;
		ensureCapacity(size + 1);
		elementData[size++] = c;
		modCount++;
		dataModCount++;
	}
	
	private final void checkIndex(final int index, final boolean excludes) {
		final int max = excludes ? size - 1 : size;
		
		if (index < 0 || index > max) {
			throw new IndexOutOfBoundsException("Index: " + index +
					" Size: " + size);
		}

	}
	
	private final void checkPreConditions(final Content child, final int index, 
			final boolean replace) {
		if (child == null) {
			throw new NullPointerException("Cannot add null object");
		}
		
		checkIndex(index, replace);
		
		if (child.getParent() != null) {
			// the content to be added already has a parent.
			Parent p = child.getParent();
			if (p instanceof Document) {
				throw new IllegalAddException((Element)child,
						"The Content already has an existing parent document");
			}
			throw new IllegalAddException(
					"The Content already has an existing parent \"" +
							((Element)p).getQualifiedName() + "\"");
		}

		if (child == parent) {
			throw new IllegalAddException(
					"The Element cannot be added to itself");
		}

		// Detect if we have <a><b><c/></b></a> and c.add(a)
		if ((parent instanceof Element && child instanceof Element) &&
				((Element) child).isAncestor((Element)parent)) {
			throw new IllegalAddException(
					"The Element cannot be added as a descendent of itself");
		}

	}

	/**
	 * Check and add the <code>Content</code> to this list at
	 * the given index.
	 * Inserts the specified object at the specified position in this list.
	 * Shifts the object currently at that position (if any) and any
	 * subsequent objects to the right (adds one to their indices).
	 *
	 * @param index index where to add <code>Element</code>
	 * @param child <code>Content</code> to add
	 */
	@Override
	public void add(int index, Content child) {
		// Confirm basic sanity of child.
		checkPreConditions(child, index, false);
		// Check to see whether this parent believes it can contain this content
		parent.canContainContent(child, index, false);

		child.setParent(parent);

		ensureCapacity(size+1);
		if( index==size ) {
			elementData[size++] = child;
		} else {
			System.arraycopy(elementData, index, elementData, index + 1, size - index);
			elementData[index] = child;
			size++;
		}
		// Successful add's increment the AbstractList's modCount
		modCount++;
		dataModCount++;
	}

	/**
	 * Add the specified collection to the end of this list.
	 *
	 * @param collection The collection to add to the list.
	 * @return <code>true</code> if the list was modified as a result of
	 *                           the add.
	 */
	@Override
	public boolean addAll(Collection<? extends Content> collection) {
		return addAll(size(), collection);
	}

	/**
	 * Inserts the specified collection at the specified position in this list.
	 * Shifts the object currently at that position (if any) and any
	 * subsequent objects to the right (adds one to their indices).
	 *
	 * @param index The offset to start adding the data in the collection
	 * @param collection The collection to insert into the list.
	 * @return <code>true</code> if the list was modified as a result of
	 *                           the add.
	 * throws IndexOutOfBoundsException if index < 0 || index > size()
	 */
	@Override
	public boolean addAll(int index, Collection<? extends Content> collection) {
		if ((collection == null)) {
			throw new NullPointerException(
					"Can not add a null collection to the ContentList");
		}
		
		checkIndex(index, false);

		int toadd = collection.size();
		
		if (toadd == 0) {
			return false;
		}
		
		ensureCapacity(size() + toadd);

		if (toadd == 1) {
			// fast short-cut for a single sized collection.
			add(index, collection.iterator().next());
			return true;
		}
		
		int count = 0;
		Content[] tmpc = new Content[toadd];
		int tmpmod = modCount;
		int tmpdmc = dataModCount;
		Content[] tmpdata = Arrays.copyOf(elementData, elementData.length);
		try {
			for (Content c : collection) {
				add(index + count, c);
				// only increment the count after a successful add.
				tmpc[count++] = c;
			}
		} finally {
			if (count < tmpc.length) {
				// something failed... remove all added content
				// Do this the 'hard' way, just force-null the content's parent
				while (--count >= 0) {
					tmpc[count].setParent(null);
				}
				System.arraycopy(tmpdata, 0, elementData, 0, tmpdata.length);
				modCount = tmpmod;
				dataModCount = tmpdmc;
			}
		}

		return true;
	}

	/**
	 * Clear the current list.
	 */
	@Override
	public void clear() {
		if (elementData != null) {
			for (int i = 0; i < size; i++) {
				Content obj = elementData[i];
				removeParent(obj);
			}
			elementData = null;
			size = 0;
		}
		modCount++;
		dataModCount++;
	}

	/**
	 * Clear the current list and set it to the contents
	 * of the <code>Collection</code>.
	 * object.
	 *
	 * @param collection The collection to use.
	 */
	void clearAndSet(Collection<? extends Content> collection) {
		if (collection == null || collection.isEmpty()) {
			clear();
			return;
		}
		
		Content[] old = elementData;
		int oldSize = size;
		int oldmod = modCount;
		int olddmc = dataModCount;

		boolean ok = false;
		try {
			elementData = null;
			size = 0;
			ensureCapacity(collection.size());
			modCount++;
			dataModCount++;
			addAll(0, collection);
			ok = true;
		} finally {
			if (!ok) {
				elementData = old;
				size = oldSize;
				modCount = oldmod;
				dataModCount = olddmc;
			}
		}

	}

	/**
	 * Increases the capacity of this <code>ContentList</code> instance,
	 * if necessary, to ensure that it can hold at least the number of
	 * items specified by the minimum capacity argument.
	 *
	 * @param minCapacity the desired minimum capacity.
	 */
	void ensureCapacity(int minCapacity) {
		if( elementData==null ) {
			elementData = new Content[Math.max(minCapacity, INITIAL_ARRAY_SIZE)];
		} else {
			int oldCapacity = elementData.length;
			if (minCapacity > oldCapacity) {
				Object oldData[] = elementData;
				int newCapacity = (oldCapacity * 3)/2 + 1;
				if (newCapacity < minCapacity)
					newCapacity = minCapacity;
				elementData = new Content[newCapacity];
				System.arraycopy(oldData, 0, elementData, 0, size);
			}
		}
	}

	/**
	 * Return the object at the specified offset.
	 *
	 * @param index The offset of the object.
	 * @return The Object which was returned.
	 */
	@Override
	public Content get(int index) {
		checkIndex(index, true);
		return elementData[index];
	}

	/**
	 * Return a view of this list based on the given filter.
	 *
	 * @param filter <code>Filter</code> for this view.
	 * @return a list representing the rules of the <code>Filter</code>.
	 */
	<E extends Content> List<E> getView(Filter<E> filter) {
		return new FilterList<E>(filter);
	}

	/**
	 * Return the index of the first Element in the list.  If the parent
	 * is a <code>Document</code> then the element is the root element.
	 * If the list contains no Elements, it returns -1.
	 *
	 * @return index of first element, or -1 if one doesn't exist
	 */
	int indexOfFirstElement() {
		if( elementData!=null ) {
			for (int i = 0; i < size; i++) {
				if (elementData[i] instanceof Element) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * Return the index of the DocType element in the list. If the list contains
	 * no DocType, it returns -1.
	 *
	 * @return                     index of the DocType, or -1 if it doesn't
	 *                             exist
	 */
	int indexOfDocType() {
		if (elementData != null) {
			for (int i = 0; i < size; i++) {
				if (elementData[i] instanceof DocType) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * Remove the object at the specified offset.
	 *
	 * @param index The offset of the object.
	 * @return The Object which was removed.
	 */
	@Override
	public Content remove(int index) {
		checkIndex(index, true);

		Content old = elementData[index];
		removeParent(old);
		int numMoved = size - index - 1;
		if (numMoved > 0)
			System.arraycopy(elementData, index+1, elementData, index,numMoved);
		elementData[--size] = null; // Let gc do its work
		modCount++;
		return old;
	}


	/** Remove the parent of a Object */
	private static void removeParent(Content c) {
		c.setParent(null);
	}

	/**
	 * Set the object at the specified location to the supplied
	 * object.
	 *
	 * @param index The location to set the value to.
	 * @param child The location to set the value to.
	 * @return The object which was replaced.
	 * throws IndexOutOfBoundsException if index < 0 || index >= size()
	 */
	@Override
	public Content set(final int index, final Content child) {
		// Confirm basic sanity of child.
		checkPreConditions(child, index, true);

		// Ensure the detail checks out OK too.
		parent.canContainContent(child, index, true);
		
		/*
		 * Do a special case of set() where we don't do a remove() then add()
		 * because that affects the modCount. We want to do a true set().
		 * See issue #15 
		 */

		final Content old = elementData[index];
		removeParent(old);
		child.setParent(parent);
		elementData[index] = child;
		// for set method we increment dataModCount, but not modCount
		// set does not change the structure of the List (size())
		dataModCount++;
		return old;
	}

	/**
	 * Return the number of items in this list
	 *
	 * @return The number of items in this list.
	 */
	@Override
	public int size() {
		return size;
	}
	
	@Override
	public Iterator<Content> iterator() {
		return new CLIterator();
	}

	/**
	 * Return this list as a <code>String</code>
	 *
	 * @return The number of items in this list.
	 */
	@Override
	public String toString() {
		return super.toString();
	}

	/* * * * * * * * * * * * * ContentListIterator * * * * * * * * * * * * * * * */
	/* * * * * * * * * * * * * ContentListIterator * * * * * * * * * * * * * * * */
	private final class CLIterator implements Iterator<Content> {
		private int expect = -1;
		private int cursor = 0;
		private boolean canremove = false;
		private CLIterator() {
			expect = modCount;
		}
		@Override
		public boolean hasNext() {
			return cursor < size;
		}

		@Override
		public Content next() {
			if (modCount != expect) {
				throw new ConcurrentModificationException("ContentList was " +
						"modified outside of this Iterator");
			}
			if (cursor >= size) {
				throw new NoSuchElementException("Iterated beyond the end of " +
						"the ContentList.");
			}
			canremove = true;
			return elementData[cursor++];
		}

		@Override
		public void remove() {
			if (modCount != expect) {
				throw new ConcurrentModificationException("ContentList was " +
						"modified outside of this Iterator");
			}
			if (!canremove) {
				throw new IllegalStateException("Can only remove() content " +
						"after a call to next()");
			}
			canremove = false;
			ContentList.this.remove(--cursor);
			expect = modCount;
		}
		
		
	}
	
	/* * * * * * * * * * * * * FilterList * * * * * * * * * * * * * * * */
	/* * * * * * * * * * * * * FilterList * * * * * * * * * * * * * * * */

	/**
	 * <code>FilterList</code> represents legal JDOM content, including content
	 * for <code>Document</code>s or <code>Element</code>s.
	 * <p>
	 * FilterList represents a dynamic view of the backing ContentList, changes
	 * to the backing list are reflected in the FilterList, and visa-versa.
	 */

	class FilterList<F extends Content> extends AbstractList<F>
			implements java.io.Serializable {

		// The filter to apply
		final Filter<F> filter;
		// correlate the position in the filtered list to the index in the
		// backing ContentList.
		int[] backingpos = new int[size + 5];
		int   backingsize = 0;
		// track data modifications in the backing ContentList.
		int xdata = -1;

		/**
		 * Create a new instance of the FilterList with the specified Filter.
		 */
		FilterList(Filter<F> filter) {
			this.filter = filter;
		}

        /**
         * Synchronise our view to the backing list.
         * Only synchronise the first <code>index</code> view elements. For want
         * of a better word, we'll call this a 'Lazy' implementation.
         * @param index how much we want to sync. Set to -1 to synchronise everything.
         * @return the index in the backing array of the <i>index'th</i> match.
         *         or the backing data size if there is no match for the index.
         */
        private final int resync(int index) {
        	if (xdata != dataModCount) {
        		// The underlying list was modified somehow...
        		// we need to invalidate our research...
        		xdata = dataModCount;
        		backingsize = 0;
        		if (size >= backingpos.length) {
        			backingpos = new int[size + 1];
        		}
        	}
        	
        	if (index >= 0 && index < backingsize) {
        		// we have already indexed far enough...
        		// return the backing index.
        		return backingpos[index];
        	}
        	
        	// the index in the backing list of the next value to check.
        	int bpi = 0;
        	if (backingsize > 0) {
        		bpi = backingpos[backingsize - 1] + 1;
        	}
        	
        	while (bpi < size) {
    			F gotit = filter.filter(elementData[bpi]);
    			if (gotit != null) {
    				backingpos[backingsize] = bpi;
    				if (backingsize++ == index) {
    					return bpi;
    				}
    			}
    			bpi++;
        	}
        	return size;
        }

		/**
		 * Inserts the specified object at the specified position in this list.
		 * Shifts the object currently at that position (if any) and any
		 * subsequent objects to the right (adds one to their indices).
		 *
		 * @param index The location to set the value to.
		 * @param obj The object to insert into the list.
		 * throws IndexOutOfBoundsException if index < 0 || index > size()
		 */
		@Override
		public void add(int index, Content obj) {
			if (index < 0) {
				throw new IndexOutOfBoundsException("Index: " + index + " Size: " + size());
			}
			int adj = resync(index);
			if (adj == size && index > size()) {
				throw new IndexOutOfBoundsException("Index: " + index + " Size: " + size());
			}
			if (filter.matches(obj)) {
				ContentList.this.add(adj, obj);
				
				// we can optimize the lazyness now by doing a partial reset on
				// the backing list... invalidate everything *after* the added
				// content
				if (backingpos.length <= size) {
					backingpos = Arrays.copyOf(backingpos, backingpos.length + 1);
				}
				backingpos[index] = adj;
				backingsize = index + 1;
				xdata = dataModCount;
				
			} else {
				throw new ClassCastException("Filter won't allow the " +
						obj.getClass().getName() +
						" '" + obj + "' to be added to the list");
			}
		}

		/**
		 * Return the object at the specified offset.
		 *
		 * @param index The offset of the object.
		 * @return The Object which was returned.
		 */
		@Override
		public F get(int index) {
			if (index < 0) {
				throw new IndexOutOfBoundsException("Index: " + index + " Size: " + size());
			}
			int adj = resync(index);
			if (adj == size) {
				throw new IndexOutOfBoundsException("Index: " + index + " Size: " + size());
			}
			return filter.filter(ContentList.this.get(adj));
		}

		@Override
		public Iterator<F> iterator() {
			return new FilterListIterator<F>(this, 0);
		}

		@Override
		public ListIterator<F> listIterator() {
			return new FilterListIterator<F>(this, 0);
		}

		@Override
		public ListIterator<F> listIterator(int index) {
			return new FilterListIterator<F>(this,  index);
		}

		/**
		 * Remove the object at the specified offset.
		 *
		 * @param index The offset of the object.
		 * @return The Object which was removed.
		 */
		@Override
		public F remove(int index) {
			if (index < 0) {
				throw new IndexOutOfBoundsException("Index: " + index + " Size: " + size());
			}
			int adj = resync(index);
			if (adj == size) {
				throw new IndexOutOfBoundsException("Index: " + index + " Size: " + size());
			}
			Content oldc = ContentList.this.get(adj);
			F old = filter.filter(oldc);
			if (old != null) {
				ContentList.this.remove(adj);
				// optimize the backing cache.
				backingsize = index;
				xdata = dataModCount;
				return old;
			}
			throw new IllegalAddException("Filter won't allow the " +
					(oldc.getClass()).getName() +
					" '" + oldc + "' (index " + index +
					") to be removed");
		}

		/**
		 * Set the object at the specified location to the supplied
		 * object.
		 *
		 * @param index The location to set the value to.
		 * @param obj The location to set the value to.
		 * @return The object which was replaced.
		 * throws IndexOutOfBoundsException if index < 0 || index >= size()
		 */
		@Override
		public F set(int index, F obj) {
			if (index < 0) {
				throw new IndexOutOfBoundsException("Index: " + index + " Size: " + size());
			}
			int adj = resync(index);
			if (adj == size) {
				throw new IndexOutOfBoundsException("Index: " + index + " Size: " + size());
			}
			F ins = filter.filter(obj);
			if (ins != null) {
				F oldc = filter.filter(ContentList.this.set(adj, ins));
				// optimize the backing....
				xdata = dataModCount;
				return oldc;
			}
			throw new ClassCastException("Filter won't allow index " +
					index + " to be set to " +
					(obj.getClass()).getName());
		}

		/**
		 * Return the number of items in this list
		 *
		 * @return The number of items in this list.
		 */
		@Override
		public int size() {
			resync(-1);
			return backingsize;
		}

	}

	/* * * * * * * * * * * * * FilterListIterator * * * * * * * * * * * */
	/* * * * * * * * * * * * * FilterListIterator * * * * * * * * * * * */

	final class FilterListIterator<F extends Content> implements ListIterator<F> {

		/** The Filter that applies */
		private final FilterList<F> filterlist;

		/** Whether this iterator is in forward or reverse. */
		private boolean forward = false;
		/** Whether a call to remove() is valid */
		private boolean canremove = false;
		/** Whether a call to set() is valid */
		private boolean canset = false;

		/** Expected modCount in our backing list */
		private int expectedmod = -1;
		
		private int cursor = -1;

		/**
		 * Default constructor
		 */
		FilterListIterator(final FilterList<F> flist, final int start) {
			filterlist = flist;
			expectedmod = modCount;
			// always start list iterators in backward mode ....
			// it makes sense... really.
			forward = false;

			if (start < 0) {
				throw new IndexOutOfBoundsException("Index: " + start + " Size: " + filterlist.size());
			}
			
			int adj = filterlist.resync(start);
			
			if (adj == size && start > filterlist.size()) {
				// the start point is after the end of the list.
				// it is only allowed to be the same as size(), no larger.
				throw new IndexOutOfBoundsException("Index: " + start + " Size: " + filterlist.size());
			}

			cursor = start;
		}
		
		private void checkConcurrent() {
			if (expectedmod != modCount) {
				throw new ConcurrentModificationException("The ContentList " +
						"supporting the FilterList this iterator is " +
						"processing has been modified by something other " +
						"than this Iterator.");
			}
		}

		/**
		 * Returns <code>true</code> if this list iterator has a next element.
		 */
		@Override
		public boolean hasNext() {
			int next = forward ? cursor + 1 : cursor;
			return filterlist.resync(next) < size;
		}

		/**
		 * Returns <code>true</code> if this list iterator has more elements
		 * when traversing the list in the reverse direction.
		 */
		@Override
		public boolean hasPrevious() {
			int prev = forward ? cursor : cursor - 1;
			return prev >= 0;
		}

		/**
		 * Returns the index of the element that would be returned by a
		 * subsequent call to <code>next</code>.
		 */
		@Override
		public int nextIndex() {
			return forward ? cursor + 1 : cursor;
		}

		/**
		 * Returns the index of the element that would be returned by a
		 * subsequent call to <code>previous</code>. (Returns -1 if the
		 * list iterator is at the beginning of the list.)
		 */
		@Override
		public int previousIndex() {
			return forward ? cursor : cursor - 1;
		}

		/**
		 * Returns the next element in the list.
		 */
		@Override
		public F next() {
			checkConcurrent();
			int next = forward ? cursor + 1 : cursor;
			
			if (filterlist.resync(next) >= size) {
				throw new NoSuchElementException("next() is beyond the end of the Iterator");
			}

			cursor = next;
			forward = true;
			canremove = true;
			canset = true;
			return filterlist.get(cursor);
		}

		/**
		 * Returns the previous element in the list.
		 */
		@Override
		public F previous() {
			checkConcurrent();
			int prev = forward ? cursor : cursor - 1;
			
			if (prev < 0) {
				throw new NoSuchElementException("previous() is beyond the beginning of the Iterator");
			}

			cursor = prev;
			forward = false;
			canremove = true;
			canset = true;
			return filterlist.get(cursor);
		}

		/**
		 * Inserts the specified element into the list .
		 */
		@Override
		public void add(Content obj) {
			checkConcurrent();
			// always add before what would normally be returned by next();
			int next = forward ? cursor + 1 : cursor;
			
			filterlist.add(next, obj);
			
			expectedmod = modCount;
			
			canremove = canset = false;
			
			// a call to next() should be unaffected, so, whatever was going to
			// be next will still be next, remember, what was going to be next
			// has been shifted 'right' by our insert.
			// we ensure this by setting the cursor to next(), and making it
			// forward
			cursor = next;
			forward = true;
		}

		/**
		 * Removes from the list the last element that was returned by
		 * the last call to <code>next</code> or <code>previous</code>.
		 */
		@Override
		public void remove() {
			checkConcurrent();
			if (!canremove)
				throw new IllegalStateException("Can not remove an "
						+ "element unless either next() or previous() has been called "
						+ "since the last remove()");
			// we are removing the last entry returned by either next() or previous().
			// the idea is to remove it, and pretend that we used to be at the
			// entry that happened *after* the removed entry.
			// so, get what would be the next entry (set at tmpcursor).
			// so call nextIndex to set tmpcursor to what would come after.
			filterlist.remove(cursor);
			forward = false;
			expectedmod = modCount;

			canremove = false;
			canset = false;
		}

		/**
		 * Replaces the last element returned by <code>next</code> or
		 * <code>previous</code> with the specified element.
		 */
		@Override
		public void set(F obj) {
			checkConcurrent();
			if (!canset) {
				throw new IllegalStateException("Can not set an element "
						+ "unless either next() or previous() has been called since the " 
						+ "last remove() or set()");
			}

			filterlist.set(cursor, obj);
			expectedmod = modCount;

		}

	}
	
}
