/*--

 $Id: ContentList.java,v 1.42 2007/11/10 05:28:58 jhunter Exp $

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

package org.jdom;

import java.util.*;

import org.jdom.filter.*;

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
 * @version $Revision: 1.42 $, $Date: 2007/11/10 05:28:58 $
 * @author  Alex Rosen
 * @author  Philippe Riand
 * @author  Bradley S. Huffman
 */
final class ContentList extends AbstractList implements java.io.Serializable {

	private static final String CVS_ID =
      "@(#) $RCSfile: ContentList.java,v $ $Revision: 1.42 $ $Date: 2007/11/10 05:28:58 $ $Name:  $";

	private static final long serialVersionUID = 1L;

	private static final int INITIAL_ARRAY_SIZE = 5;

    /** Our backing list */
    private Content elementData[];
    private int size;

    /** Document or Element this list belongs to */
    private Parent parent;

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
     * Inserts the specified object at the specified position in this list.
     * Shifts the object currently at that position (if any) and any
     * subsequent objects to the right (adds one to their indices).
     *
     * @param index The location to set the value to.
     * @param obj The object to insert into the list.
     * throws IndexOutOfBoundsException if index < 0 || index > size()
     */
    public void add(int index, Object obj) {
        if (obj == null) {
            throw new IllegalAddException("Cannot add null object");
        }
        if (obj instanceof String) {  // String is OK to add as special case
            obj = new Text(obj.toString());  // wrap it as a Content
        }
        if ((obj instanceof Content)) {
            add(index, (Content) obj);
        } else {
            throw new IllegalAddException("Class " +
                         obj.getClass().getName() +
                         " is of unrecognized type and cannot be added");
        }
    }

    /**
     * @see org.jdom.ContentList#add(int, org.jdom.Content)
     */
    private void documentCanContain(int index, Content child) throws IllegalAddException {
        if (child instanceof Element) {
            if (indexOfFirstElement() >= 0) {
                throw new IllegalAddException(
                        "Cannot add a second root element, only one is allowed");
            }
            if (indexOfDocType() >= index) {
                throw new IllegalAddException(
                        "A root element cannot be added before the DocType");
            }
        }
        if (child instanceof DocType) {
            if (indexOfDocType() >= 0) {
                throw new IllegalAddException(
                        "Cannot add a second doctype, only one is allowed");
            }
            int firstElt = indexOfFirstElement();
            if (firstElt != -1 && firstElt < index) {
                throw new IllegalAddException(
                        "A DocType cannot be added after the root element");
            }
        }
        if (child instanceof CDATA) {
            throw new IllegalAddException("A CDATA is not allowed at the document root");
        }

        if (child instanceof Text) {
            throw new IllegalAddException("A Text is not allowed at the document root");
        }

        if (child instanceof EntityRef) {
            throw new IllegalAddException("An EntityRef is not allowed at the document root");
        }
    }

    private static void elementCanContain(int index, Content child) throws IllegalAddException {
        if (child instanceof DocType) {
            throw new IllegalAddException(
                    "A DocType is not allowed except at the document level");
        }
    }

    /**
     * Check and add the <code>Element</code> to this list at
     * the given index.
     *
     * @param index index where to add <code>Element</code>
     * @param child <code>Element</code> to add
     */
    void add(int index, Content child) {
        if (child == null) {
            throw new IllegalAddException("Cannot add null object");
        }
        if (parent instanceof Document) {
          documentCanContain(index, child);
        }
        else {
          elementCanContain(index, child);
        }

        if (child.getParent() != null) {
            Parent p = child.getParent();
            if (p instanceof Document) {
                throw new IllegalAddException((Element)child,
                   "The Content already has an existing parent document");
            }
            else {
                throw new IllegalAddException(
                     "The Content already has an existing parent \"" +
                     ((Element)p).getQualifiedName() + "\"");
            }
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

        if (index<0 || index>size) {
            throw new IndexOutOfBoundsException("Index: " + index +
                                                " Size: " + size());
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
     * Add the specified collecton to the end of this list.
     *
     * @param collection The collection to add to the list.
     * @return <code>true</code> if the list was modified as a result of
     *                           the add.
     */
    public boolean addAll(Collection collection) {
        return addAll(size(), collection);
    }

    /**
     * Inserts the specified collecton at the specified position in this list.
     * Shifts the object currently at that position (if any) and any
     * subsequent objects to the right (adds one to their indices).
     *
     * @param index The offset to start adding the data in the collection
     * @param collection The collection to insert into the list.
     * @return <code>true</code> if the list was modified as a result of
     *                           the add.
     * throws IndexOutOfBoundsException if index < 0 || index > size()
     */
    public boolean addAll(int index, Collection collection) {
        if (index<0 || index>size) {
            throw new IndexOutOfBoundsException("Index: " + index +
                                                " Size: " + size());
        }

        if ((collection == null) || (collection.size() == 0)) {
            return false;
        }
        ensureCapacity(size() + collection.size());

        int count = 0;
        try {
            Iterator i = collection.iterator();
            while (i.hasNext()) {
                Object obj = i.next();
                add(index + count, obj);
                count++;
            }
        }
        catch (RuntimeException exception) {
            for (int i = 0; i < count; i++) {
                remove(index);
            }
            throw exception;
        }

        return true;
    }

    /**
     * Clear the current list.
     */
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
    void clearAndSet(Collection collection) {
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
    public Object get(int index) {
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
    List getView(Filter filter) {
        return new FilterList(filter);
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
    public Object remove(int index) {
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
    public Object set(int index, Object obj) {
        if (index<0 || index>=size)
            throw new IndexOutOfBoundsException("Index: " + index +
                                                 " Size: " + size());

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

        Object old = remove(index);
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
    public int size() {
        return size;
    }

    /**
     * Return this list as a <code>String</code>
     *
     * @return The number of items in this list.
     */
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

    class FilterList extends AbstractList implements java.io.Serializable {

        /** The Filter */
        Filter filter;

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
        FilterList(Filter filter) {
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
        public void add(int index, Object obj) {
            if (filter.matches(obj)) {
                int adjusted = getAdjustedIndex(index);
                ContentList.this.add(adjusted, obj);
                expected++;
                count++;
            }
            else throw new IllegalAddException("Filter won't allow the " +
                                obj.getClass().getName() +
                                " '" + obj + "' to be added to the list");
        }

        /**
         * Return the object at the specified offset.
         *
         * @param index The offset of the object.
         * @return The Object which was returned.
         */
        public Object get(int index) {
            int adjusted = getAdjustedIndex(index);
            return ContentList.this.get(adjusted);
        }

        public Iterator iterator() {
            return new FilterListIterator(filter, 0);
        }

        public ListIterator listIterator() {
            return new FilterListIterator(filter, 0);
        }

        public ListIterator listIterator(int index) {
            return new FilterListIterator(filter,  index);
        }

        /**
         * Remove the object at the specified offset.
         *
         * @param index The offset of the object.
         * @return The Object which was removed.
         */
        public Object remove(int index) {
            int adjusted = getAdjustedIndex(index);
            Object old = ContentList.this.get(adjusted);
            if (filter.matches(old)) {
                old = ContentList.this.remove(adjusted);
                expected++;
                count--;
            }
            else {
                throw new IllegalAddException("Filter won't allow the " +
                                             (old.getClass()).getName() +
                                             " '" + old + "' (index " + index +
                                             ") to be removed");
            }
            return old;
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
        public Object set(int index, Object obj) {
            Object old = null;
            if (filter.matches(obj)) {
                int adjusted = getAdjustedIndex(index);
                old = ContentList.this.get(adjusted);
                if (!filter.matches(old)) {
                    throw new IllegalAddException("Filter won't allow the " +
                                             (old.getClass()).getName() +
                                             " '" + old + "' (index " + index +
                                             ") to be removed");
                }
                old = ContentList.this.set(adjusted, obj);
                expected += 2;
            }
            else {
                throw new IllegalAddException("Filter won't allow index " +
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
                Object obj = ContentList.this.elementData[i];
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
                Object obj = ContentList.this.elementData[i];
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

    class FilterListIterator implements ListIterator {

        /** The Filter that applies */
        Filter filter;

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
        FilterListIterator(Filter filter, int start) {
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
        public boolean hasNext() {
        	return nextIndex() < fsize;
        }

        /**
         * Returns the next element in the list.
         */
        public Object next() {
        	if (!hasNext())
				throw new NoSuchElementException("next() is beyond the end of the Iterator");
			index = nextIndex();
			cursor = tmpcursor;
			forward = true;
			canremove = true;
			canset = true;
			return ContentList.this.get(cursor);
        }

        /**
		 * Returns <code>true</code> if this list iterator has more elements
		 * when traversing the list in the reverse direction.
		 */
        public boolean hasPrevious() {
        	return previousIndex() >= 0;
        }

        /**
         * Returns the previous element in the list.
         */
        public Object previous() {
			if (!hasPrevious())
				throw new NoSuchElementException("previous() is before the start of the Iterator");
			index = previousIndex();
			cursor = tmpcursor;
			forward = false;
			canremove = true;
			canset = true;
			return ContentList.this.get(cursor);
		}

        /**
		 * Returns the index of the element that would be returned by a
		 * subsequent call to <code>next</code>.
		 */
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
        public void add(Object obj) {
            if (!filter.matches(obj)) {
                throw new IllegalAddException("Filter won't allow the " +
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
        public void set(Object obj) {
			if (!canset)
				throw new IllegalStateException("Can not set an element "
						+ "unless either next() or previous() has been called since the " 
						+ "last remove() or set()");
			checkConcurrentModification();

			if (!filter.matches(obj)) {
				throw new IllegalAddException("Filter won't allow index " + index + " to be set to "
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
