/*--

 $Id: ContentList.java,v 1.35 2004/02/10 21:38:13 jhunter Exp $

 Copyright (C) 2000-2004 Jason Hunter & Brett McLaughlin.
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
 * @version $Revision: 1.35 $, $Date: 2004/02/10 21:38:13 $
 * @author  Alex Rosen
 * @author  Philippe Riand
 * @author  Bradley S. Huffman
 */
final class ContentList extends AbstractList implements java.io.Serializable {

    private static final String CVS_ID =
      "@(#) $RCSfile: ContentList.java,v $ $Revision: 1.35 $ $Date: 2004/02/10 21:38:13 $ $Name:  $";

    private static final int INITIAL_ARRAY_SIZE = 5;

    /**
     * Used inner class FilterListIterator to help hasNext and
     * hasPrevious the next index of our cursor (must be here
     * for JDK1.1).
     */
    private static final int CREATE  = 0;
    private static final int HASPREV = 1;
    private static final int HASNEXT = 2;
    private static final int PREV    = 3;
    private static final int NEXT    = 4;
    private static final int ADD     = 5;
    private static final int REMOVE  = 6;

    /** Our backing list */
//    protected ArrayList list;
    private Content elementData[];
    private int size;

    /** Document or Element this list belongs to */
    private Parent parent;

    /** Force either a Document or Element parent */
    ContentList(Parent parent) {
        this.parent = parent;
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
        if ((obj instanceof Content)) {
            add(index, (Content) obj);
        } else {
            throw new IllegalAddException("Class " +
                         obj.getClass().getName() +
                         " is of unrecognized type and cannot be added");
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
        parent.canContain(child, index);

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
                Object obj = elementData[i];
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

        Object old = elementData[index];
        removeParent(old);
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index+1, elementData, index,numMoved);
        elementData[--size] = null; // Let gc do its work
        modCount++;
        return old;
    }


    /** Remove the parent of a Object */
    private void removeParent(Object obj) {
        if (obj instanceof Element) {
            Element element = (Element) obj;
            element.setParent(null);
        }
        else if (obj instanceof Text) {
            Text text = (Text) obj;
            text.setParent(null);
        }
        else if (obj instanceof Comment) {
            Comment comment = (Comment) obj;
            comment.setParent(null);
        }
        else if (obj instanceof ProcessingInstruction) {
            ProcessingInstruction pi = (ProcessingInstruction) obj;
            pi.setParent(null);
        }
        else if (obj instanceof EntityRef) {
            EntityRef entity = (EntityRef) obj;
            entity.setParent(null);
        }
        else if (obj instanceof DocType) {
            DocType dt = (DocType) obj;
            dt.setParent(null);
        }
        else {
            // Should never happen.
            throw new IllegalArgumentException("Object '" + obj + "' unknown");
        }
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

        /** The last operation performed */
        int lastOperation;

        /** Initial start index in backing list */
        int initialCursor;

        /** Index in backing list of next object */
        int cursor;

        /** Index in backing list of last object returned */
        int last;

        /** Expected modCount in our backing list */
        int expected;

        /**
         * Default constructor
         */
        FilterListIterator(Filter filter, int start) {
            this.filter = filter;
            initialCursor = initializeCursor(start);
            last = -1;
            expected = ContentList.this.getModCount();
            lastOperation = CREATE;
        }

        /**
         * Returns <code>true</code> if this list iterator has a next element.
         */
        public boolean hasNext() {
            checkConcurrentModification();

            switch(lastOperation) {
            case CREATE:  cursor = initialCursor;
                          break;
            case PREV:    cursor = last;
                          break;
            case ADD:
            case NEXT:    cursor = moveForward(last + 1);
                          break;
            case REMOVE:  cursor = moveForward(last);
                          break;
            case HASPREV: cursor = moveForward(cursor + 1);
                          break;
            case HASNEXT: break;
            default:      throw new IllegalStateException("Unknown operation");
            }

            if (lastOperation != CREATE) {
                lastOperation = HASNEXT;
            }

            return (cursor < ContentList.this.size()) ? true : false;
        }

        /**
         * Returns the next element in the list.
         */
        public Object next() {
            checkConcurrentModification();

            if (hasNext()) {
                last = cursor;
            }
            else {
                last = ContentList.this.size();
                throw new NoSuchElementException();
            }

            lastOperation = NEXT;
            return ContentList.this.get(last);
        }

        /**
         * Returns <code>true</code> if this list iterator has more
         * elements when traversing the list in the reverse direction.
         */
        public boolean hasPrevious() {
            checkConcurrentModification();

            switch(lastOperation) {
            case CREATE:  cursor = initialCursor;
                          int size = ContentList.this.size();
                          if (cursor >= size) {
                              cursor = moveBackward(size - 1);
                          }
                          break;
            case PREV:
            case REMOVE:  cursor = moveBackward(last - 1);
                          break;
            case HASNEXT: cursor = moveBackward(cursor - 1);
                          break;
            case ADD:
            case NEXT:    cursor = last;
                          break;
            case HASPREV: break;
            default:      throw new IllegalStateException("Unknown operation");
            }

            if (lastOperation != CREATE) {
                lastOperation = HASPREV;
            }

            return (cursor < 0) ? false : true;
        }

        /**
         * Returns the previous element in the list.
         */
        public Object previous() {
            checkConcurrentModification();

            if (hasPrevious()) {
                last = cursor;
            }
            else {
                last = -1;
                throw new NoSuchElementException();
            }

            lastOperation = PREV;
            return ContentList.this.get(last);
        }

        /**
         * Returns the index of the element that would be returned by a
         * subsequent call to <code>next</code>.
         */
        public int nextIndex() {
            checkConcurrentModification();
            hasNext();

            int count = 0;
            for (int i = 0; i < ContentList.this.size(); i++) {
                if (filter.matches(ContentList.this.get(i))) {
                    if (i == cursor) {
                        return count;
                    }
                    count++;
                }
            }
            expected = ContentList.this.getModCount();
            return count;
        }

        /**
         * Returns the index of the element that would be returned by a
         * subsequent call to <code>previous</code>. (Returns -1 if the
         * list iterator is at the beginning of the list.)
         */
        public int previousIndex() {
            checkConcurrentModification();

            if (hasPrevious()) {
                int count = 0;
                for (int i = 0; i < ContentList.this.size(); i++) {
                    if (filter.matches(ContentList.this.get(i))) {
                        if (i == cursor) {
                            return count;
                        }
                        count++;
                    }
                }
            }
            return -1;
        }

        /**
         * Inserts the specified element into the list.
         */
        public void add(Object obj) {
            checkConcurrentModification();

            if (filter.matches(obj)) {
                last = cursor + 1;
                ContentList.this.add(last, obj);
            }
            else {
                throw new IllegalAddException("Filter won't allow add of " +
                                              (obj.getClass()).getName());
            }
            expected = ContentList.this.getModCount();
            lastOperation = ADD;
        }

        /**
         * Removes from the list the last element that was returned by
         * <code>next</code> or <code>previous</code>.
         * the last call to <code>next</code> or <code>previous</code>.
         */
        public void remove() {
            checkConcurrentModification();

            if ((last < 0) || (lastOperation == REMOVE)) {
                throw new IllegalStateException("no preceeding call to " +
                                                "prev() or next()");
            }

            if (lastOperation == ADD) {
                throw new IllegalStateException("cannot call remove() " +
                                                "after add()");
            }

            Object old = ContentList.this.get(last);
            if (filter.matches(old)) {
                ContentList.this.remove(last);
            }
            else throw new IllegalAddException("Filter won't allow " +
                                                (old.getClass()).getName() +
                                                " (index " + last +
                                                ") to be removed");
            expected = ContentList.this.getModCount();
            lastOperation = REMOVE;
        }

        /**
         * Replaces the last element returned by <code>next</code> or
         * <code>previous</code> with the specified element.
         */
        public void set(Object obj) {
            checkConcurrentModification();

            if ((lastOperation == ADD) || (lastOperation == REMOVE)) {
                throw new IllegalStateException("cannot call set() after " +
                                                "add() or remove()");
            }

            if (last < 0) {
                throw new IllegalStateException("no preceeding call to " +
                                                "prev() or next()");
            }

            if (filter.matches(obj)) {
                Object old = ContentList.this.get(last);
                if (!filter.matches(old)) {
                    throw new IllegalAddException("Filter won't allow " +
                                  (old.getClass()).getName() + " (index " +
                                  last + ") to be removed");
                }
                ContentList.this.set(last, obj);
            }
            else {
                throw new IllegalAddException("Filter won't allow index " +
                                              last + " to be set to " +
                                              (obj.getClass()).getName());
            }

            expected = ContentList.this.getModCount();
            // Don't set lastOperation
        }

        /**
         * Returns index in the backing list by moving forward start
         * objects that match our filter.
         */
        private int initializeCursor(int start) {
            if (start < 0) {
                throw new IndexOutOfBoundsException("Index: " + start);
            }

            int count = 0;
            for (int i = 0; i < ContentList.this.size(); i++) {
                Object obj = ContentList.this.get(i);
                if (filter.matches(obj)) {
                    if (start == count) {
                        return i;
                    }
                    count++;
                }
            }

            if (start > count) {
                throw new IndexOutOfBoundsException("Index: " + start +
                                                    " Size: " + count);
            }

            return ContentList.this.size();
        }

        /**
         * Returns index in the backing list of the next object matching
         * our filter, starting at the given index and moving forwards.
         */
        private int moveForward(int start) {
            if (start < 0) {
                start = 0;
            }
            for (int i = start; i < ContentList.this.size(); i++) {
                Object obj = ContentList.this.get(i);
                if (filter.matches(obj)) {
                    return i;
                }
            }
            return ContentList.this.size();
        }

        /**
         * Returns index in the backing list of the next object matching
         * our filter, starting at the given index and moving backwards.
         */
        private int moveBackward(int start) {
            if (start >= ContentList.this.size()) {
                start = ContentList.this.size() - 1;
            }

            for (int i = start; i >= 0; --i) {
                Object obj = ContentList.this.get(i);
                if (filter.matches(obj)) {
                    return i;
                }
            }
            return -1;
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
