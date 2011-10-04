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
		if (child == null) {
			throw new NullPointerException("Cannot add null object");
		}
		
		if (index<0 || index>size) {
			throw new IndexOutOfBoundsException("Index: " + index +
					" Size: " + size());
		}

		if (child.getParent() != null) {
			// the content to be added already has a parent.
			Parent p = child.getParent();
			if (child instanceof Element && p instanceof Document) {
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

		// Check to see whether this parent believes it can contain this content
		parent.canContainContent(child, index, false);

		// Detect if we have <a><b><c/></b></a> and c.add(a)
		if ((parent instanceof Element && child instanceof Element) &&
				((Element) child).isAncestor((Element)parent)) {
			throw new IllegalAddException(
					"The Element cannot be added as a descendent of itself");
		}

		child.setParent(parent);

		ensureCapacity(size+1);
		if( index==size ) {
			elementData[size++] = child;
		} else {
			System.arraycopy(elementData, index, elementData, index + 1, size - index);
			elementData[index] = child;
			size++;
		}
		modCount++;
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
		if (index<0 || index>size) {
			throw new IndexOutOfBoundsException("Index: " + index +
					" Size: " + size());
		}

		if ((collection == null)) {
			throw new NullPointerException(
					"Can not add a null collection to the ContentList");
		}
		
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
		Content[] tmpdata = Arrays.copyOf(elementData, elementData.length);
		try {
			for (Content c : collection) {
				add(index + count, c);
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
	}

	/**
	 * Clear the current list and set it to the contents
	 * of the <code>Collection</code>.
	 * object.
	 *
	 * @param collection The collection to use.
	 */
	void clearAndSet(Collection<? extends Content> collection) {
		Content[] old = elementData;
		int oldSize = size;

		elementData = null;
		size = 0;

		if ((collection != null) && (collection.size() != 0)) {
			ensureCapacity(collection.size());
			try {
				addAll(0, collection);
			}
			catch (RuntimeException exception) {
				elementData = old;
				size = oldSize;
				throw exception;
			}
		}

		if (old != null) {
			for (int i = 0; i < oldSize; i++) {
				removeParent(old[i]);
			}
		}
		modCount++;
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
		if (index<0 || index>=size) {
			throw new IndexOutOfBoundsException("Index: " + index +
					" Size: " + size());
		}
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
		if (index<0 || index>=size)
			throw new IndexOutOfBoundsException("Index: " + index +
					" Size: " + size());

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
	 * @param obj The location to set the value to.
	 * @return The object which was replaced.
	 * throws IndexOutOfBoundsException if index < 0 || index >= size()
	 */
	@Override
	public Content set(int index, Content obj) {
		if (index<0 || index>=size)
			throw new IndexOutOfBoundsException("Index: " + index +
					" Size: " + size());

		if (obj == null) {
			throw new NullPointerException("Can not set a Content entry to null");
		}

		if ((obj instanceof Element) && (parent instanceof Document)) {
			int root = indexOfFirstElement();
			if ((root >= 0) && (root != index)) {
				throw new IllegalAddException(
						"Cannot add a second root element, only one is allowed");
			}
		}

		if ((obj instanceof DocType) && (parent instanceof Document)) {
			int docTypeIndex = indexOfDocType();
			if ((docTypeIndex >= 0) && (docTypeIndex != index)) {
				throw new IllegalAddException(
						"Cannot add a second doctype, only one is allowed");
			}
		}

		Content old = remove(index);
		try {
			add(index, obj);
		}
		catch (RuntimeException exception) {
			add(index, old);
			throw exception;
		}
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

	/**
	 * Return this list as a <code>String</code>
	 *
	 * @return The number of items in this list.
	 */
	@Override
	public String toString() {
		return super.toString();
	}

	/** Give access of ContentList.modCount to FilterList */
	private int getModCount() {
		return modCount;
	}

	/* * * * * * * * * * * * * FilterList * * * * * * * * * * * * * * * */
	/* * * * * * * * * * * * * FilterList * * * * * * * * * * * * * * * */

	/**
	 * <code>FilterList</code> represents legal JDOM content, including content
	 * for <code>Document</code>s or <code>Element</code>s.
	 */

	class FilterList<F extends Content> extends AbstractList<F> implements java.io.Serializable {

		/** The Filter */
		Filter<F> filter;

		/** Current number of items in this view */
		int count = 0;

		/** Expected modCount in our backing list */
		int expected = -1;

		// Implementation Note: Directly after size() is called, expected
		//       is sync'd with ContentList.modCount and count provides
		//       the true size of this view.  Before the first call to
		//       size() or if the backing list is modified outside this
		//       FilterList, both might contain bogus values and should
		//       not be used without first calling size();

		/**
		 * Create a new instance of the FilterList with the specified Filter.
		 */
		FilterList(Filter<F> filter) {
			this.filter = filter;
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
			if (filter.matches(obj)) {
				int adjusted = getAdjustedIndex(index);
				ContentList.this.add(adjusted, obj);
				expected++;
				count++;
			}
			else throw new ClassCastException("Filter won't allow the " +
					obj.getClass().getName() +
					" '" + obj + "' to be added to the list");
		}

		/**
		 * Return the object at the specified offset.
		 *
		 * @param index The offset of the object.
		 * @return The Object which was returned.
		 */
		@Override
		public F get(int index) {
			int adjusted = getAdjustedIndex(index);
			return filter.filter(ContentList.this.get(adjusted));
		}

		@Override
		public Iterator<F> iterator() {
			return new FilterListIterator<F>(filter, 0);
		}

		@Override
		public ListIterator<F> listIterator() {
			return new FilterListIterator<F>(filter, 0);
		}

		@Override
		public ListIterator<F> listIterator(int index) {
			return new FilterListIterator<F>(filter,  index);
		}

		/**
		 * Remove the object at the specified offset.
		 *
		 * @param index The offset of the object.
		 * @return The Object which was removed.
		 */
		@Override
		public F remove(int index) {
			int adjusted = getAdjustedIndex(index);
			Content oldc = ContentList.this.get(adjusted);
			F old = filter.filter(oldc);
			if (old != null) {
				ContentList.this.remove(adjusted);
				expected++;
				count--;
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
			F old = null;
			if (filter.matches(obj)) {
				int adjusted = getAdjustedIndex(index);
				Content oldc = filter.filter(ContentList.this.get(adjusted));
				if (!filter.matches(oldc)) {
					throw new IllegalAddException("Filter won't allow the " +
							(oldc.getClass()).getName() +
							" '" + oldc + "' (index " + index +
							") to be removed");
				}
				old = filter.filter(ContentList.this.set(adjusted, obj));
				expected += 2;
			}
			else {
				throw new ClassCastException("Filter won't allow index " +
						index + " to be set to " +
						(obj.getClass()).getName());
			}
			return old;
		}

		/**
		 * Return the number of items in this list
		 *
		 * @return The number of items in this list.
		 */
		@Override
		public int size() {
			// Implementation Note: Directly after size() is called, expected
			//       is sync'd with ContentList.modCount and count provides
			//       the true size of this view.  Before the first call to
			//       size() or if the backing list is modified outside this
			//       FilterList, both might contain bogus values and should
			//       not be used without first calling size();

			if (expected == ContentList.this.getModCount()) {
				return count;
			}

			count = 0;
			for (int i = 0; i < ContentList.this.size(); i++) {
				Content obj = ContentList.this.elementData[i];
				if (filter.matches(obj)) {
					count++;
				}
			}
			expected = ContentList.this.getModCount();
			return count;
		}

		/**
		 * Return the adjusted index
		 *
		 * @param index Index of in this view.
		 * @return True index in backing list
		 */
		final private int getAdjustedIndex(int index) {
			int adjusted = 0;
			for (int i = 0; i < ContentList.this.size; i++) {
				Content obj = ContentList.this.elementData[i];
				if (filter.matches(obj)) {
					if (index == adjusted) {
						return i;
					}
					adjusted++;
				}
			}

			if (index == adjusted) {
				return ContentList.this.size;
			}

			return ContentList.this.size + 1;
		}
	}

	/* * * * * * * * * * * * * FilterListIterator * * * * * * * * * * * */
	/* * * * * * * * * * * * * FilterListIterator * * * * * * * * * * * */

	class FilterListIterator<F extends Content> implements ListIterator<F> {

		/** The Filter that applies */
		Filter<F> filter;

		/** Whether this iterator is in forward or reverse. */
		private boolean forward = false;
		/** Whether a call to remove() is valid */
		private boolean canremove = false;
		/** Whether a call to set() is valid */
		private boolean canset = false;

		/** Index in backing list of next object */
		private int cursor = -1;
		/** the backing index to use if we actually DO move */
		private int tmpcursor = -1;
		/** Index in ListIterator */
		private int index = -1;

		/** Expected modCount in our backing list */
		private int expected = -1;

		/** Number of elements matching the filter. */
		private int fsize = 0;

		/**
		 * Default constructor
		 */
		FilterListIterator(Filter<F> filter, int start) {
			this.filter = filter;
			expected = ContentList.this.getModCount();
			// always start list iterators in backward mode ....
			// it makes sense... really.
			forward = false;

			if (start < 0) {
				throw new IndexOutOfBoundsException("Index: " + start);
			}

			// the number of matching elements....
			fsize = 0;

			// go through the list, count the matching elements...
			for (int i = 0; i < ContentList.this.size(); i++) {
				if (filter.matches(ContentList.this.get(i))) {
					if (start == fsize) {
						// set the back-end cursor to the matching element....
						cursor = i;
						// set the front-end cursor too.
						index = fsize;
					}
					fsize++;
				}
			}

			if (start > fsize) {
				throw new IndexOutOfBoundsException("Index: " + start + " Size: " + fsize);
			}
			if (cursor == -1) {
				// implies that start == fsize (i.e. after the last element
				// put the insertion point at the end of the Underlying
				// content list ....
				// i.e. an add() at this point may potentially end up with
				// filtered content between previous() and next()
				// the alternative is to put the cursor on the Content after
				// the last Content that the filter passed
				// The implications are ambiguous.
				cursor = ContentList.this.size();
				index = fsize;
			}

		}

		/**
		 * Returns <code>true</code> if this list iterator has a next element.
		 */
		@Override
		public boolean hasNext() {
			return nextIndex() < fsize;
		}

		/**
		 * Returns the next element in the list.
		 */
		@Override
		public F next() {
			if (!hasNext())
				throw new NoSuchElementException("next() is beyond the end of the Iterator");
			index = nextIndex();
			cursor = tmpcursor;
			forward = true;
			canremove = true;
			canset = true;
			return filter.filter(ContentList.this.get(cursor));
		}

		/**
		 * Returns <code>true</code> if this list iterator has more elements
		 * when traversing the list in the reverse direction.
		 */
		@Override
		public boolean hasPrevious() {
			return previousIndex() >= 0;
		}

		/**
		 * Returns the previous element in the list.
		 */
		@Override
		public F previous() {
			if (!hasPrevious())
				throw new NoSuchElementException("previous() is before the start of the Iterator");
			index = previousIndex();
			cursor = tmpcursor;
			forward = false;
			canremove = true;
			canset = true;
			return filter.filter(ContentList.this.get(cursor));
		}

		/**
		 * Returns the index of the element that would be returned by a
		 * subsequent call to <code>next</code>.
		 */
		@Override
		public int nextIndex() {
			checkConcurrentModification();
			if (forward) {
				// Starting with next possibility ....
				for (int i = cursor + 1; i < ContentList.this.size(); i++) {
					if (filter.matches(ContentList.this.get(i))) {
						tmpcursor = i;
						return index + 1;
					}
				}
				// Never found another match.... put the insertion point at
				// the end of the list....
				tmpcursor = ContentList.this.size();
				return index + 1;
			}

			// We've been going back... so nextIndex() returns the same
			// element.
			tmpcursor = cursor;
			return index;
		}

		/**
		 * Returns the index of the element that would be returned by a
		 * subsequent call to <code>previous</code>. (Returns -1 if the
		 * list iterator is at the beginning of the list.)
		 */
		@Override
		public int previousIndex() {
			checkConcurrentModification();
			if (!forward) {
				// starting with next possibility ....
				for (int i = cursor - 1; i >= 0; i--) {
					if (filter.matches(ContentList.this.get(i))) {
						tmpcursor = i;
						return index - 1;
					}
				}
				// Never found another match.... put the insertion point at
				// the start of the list....
				tmpcursor = -1;
				return index - 1;
			}

			// We've been going forwards... so previousIndex() returns same
			// element.
			tmpcursor = cursor;
			return index;
		}

		/**
		 * Inserts the specified element into the list.
		 */
		@Override
		public void add(Content obj) {
			if (!filter.matches(obj)) {
				throw new ClassCastException("Filter won't allow the " +
						obj.getClass().getName() +
						" '" + obj + "' to be added to the list");
			}

			// Call to nextIndex() will check concurrent.
			nextIndex();
			// tmpcursor is the backing cursor of the next element
			// Remember that List.add(index,obj) is really an insert....
			ContentList.this.add(tmpcursor, obj);
			expected = ContentList.this.getModCount();
			canremove = canset = false;

			if (forward) {
				index++;
			} else {
				forward = true;
			}
			fsize++;
			cursor = tmpcursor;
		}

		/**
		 * Removes from the list the last element that was returned by
		 * the last call to <code>next</code> or <code>previous</code>.
		 */
		@Override
		public void remove() {
			if (!canremove)
				throw new IllegalStateException("Can not remove an "
						+ "element unless either next() or previous() has been called "
						+ "since the last remove()");
			// we are removing the last entry reuned by either next() or previous().
			// the idea is to remove it, and pretend that we used to be at the
			// entry that happened *after* the removed entry.
			// so, get what would be the next entry (set at tmpcursor).
			// so call nextIndex to set tmpcursor to what would come after.
			boolean dir = forward;
			forward = true;
			try {
				nextIndex();
				ContentList.this.remove(cursor);
			} finally {
				forward = dir;
			}
			cursor = tmpcursor - 1;
			expected = ContentList.this.getModCount();

			forward = false;
			canremove = false;
			canset = false;
			fsize--;
		}

		/**
		 * Replaces the last element returned by <code>next</code> or
		 * <code>previous</code> with the specified element.
		 */
		@Override
		public void set(Content obj) {
			if (!canset)
				throw new IllegalStateException("Can not set an element "
						+ "unless either next() or previous() has been called since the " 
						+ "last remove() or set()");
			checkConcurrentModification();

			if (!filter.matches(obj)) {
				throw new ClassCastException("Filter won't allow index " + index + " to be set to "
						+ (obj.getClass()).getName());
			}

			ContentList.this.set(cursor, obj);
			expected = ContentList.this.getModCount();

		}

		/**
		 * Check if are backing list is being modified by someone else.
		 */
		private void checkConcurrentModification() {
			if (expected != ContentList.this.getModCount()) {
				throw new ConcurrentModificationException();
			}
		}
	}
}
