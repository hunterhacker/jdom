/*--

 $Id: ContentList.java,v 1.3 2002/02/13 16:58:47 jhunter Exp $

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
 DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
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

package org.jdom;

import java.util.*;

/**
 * <p>
 * <code>ContentList</code> represents valid JDOM content, including content
 * for <code>Document</code>s or <code>Element</code>s.
 * This class is NOT PUBLIC; users should see it as a simple List
 * implementation.
 * </p>
 *
 * @author Alex Rosen
 * @author Philippe Riand
 * @author Bradley S. Huffman
 * @version $Revision: 1.3 $, $Date: 2002/02/13 16:58:47 $
 * @see CDATA
 * @see Comment
 * @see Element
 * @see EntityRef
 * @see ProcessingInstruction
 * @see Text
 */
class ContentList extends AbstractList
                         implements List, Cloneable, java.io.Serializable {

    private static final String CVS_ID =
      "@(#) $RCSfile: ContentList.java,v $ $Revision: 1.3 $ $Date: 2002/02/13 16:58:47 $ $Name:  $";

    private static final int INITIAL_ARRAY_SIZE = 5;

    /** Our backing list */
    protected ArrayList list;

    /** Document or Element this list belongs to */
    protected Object parent;

    /** Force either a Document or Element parent */
    private ContentList() { }

    /**
     * <p>
     * Create a new instance of the ContentList representing
     * Document content
     * </p>
     */
    protected ContentList(Document document) {
        this.parent = document;
        ensureCapacity(INITIAL_ARRAY_SIZE);
    }

    /**
     * <p>
     * Create a new instance of the ContentList representing
     * Element content
     * </p>
     */
    protected ContentList(Element parent) {
        this.parent = parent;
        ensureCapacity(INITIAL_ARRAY_SIZE);
    }

    /**
     * <p>
     * Inserts the specified object at the specified position in this list.
     * Shifts the object currently at that position (if any) and any
     * subsequent objects to the right (adds one to their indices).
     * </p>
     *
     * @param index The location to set the value to.
     * @param obj The object to insert into the list.
     * throws IndexOutOfBoundsException if index < 0 || index > size()
     */
    public void add(int index, Object obj) {
        if (obj instanceof Element) {
            add(index, (Element) obj);
        }
        else if (obj instanceof Comment) {
            add(index, (Comment) obj);
        }
        else if (obj instanceof ProcessingInstruction) {
            add(index, (ProcessingInstruction) obj);
        }
        else if (obj instanceof CDATA) {
            add(index, (CDATA) obj);
        }
        else if (obj instanceof Text) {
            add(index, (Text) obj);
        }
        else if (obj instanceof EntityRef) {
            add(index, (EntityRef) obj);
        }
        else {
            if (obj == null) {
                throw new IllegalAddException("Cannot add null object");
            }
            else {
                throw new IllegalAddException("Class " +
                             obj.getClass().getName() + 
                             " is of unrecognized type and cannot be added");
            }
        }
    }

    /**
     * <p>
     * Check and add the <code>Element</code> to this list at
     * the given index.
     * </p>
     *
     * @param index index where to add <code>Element</code>
     * @param element <code>Element</code> to add
     */
    protected void add(int index, Element element) {
        if (element == null) {
            throw new IllegalAddException("Cannot add null object");
        }

        if (element.getParent() != null) {
            throw new IllegalAddException(
                          "The element already has an existing parent \"" +
                          element.getParent().getQualifiedName() + "\"");
        }

        if (element == parent) {
            throw new IllegalAddException(
                "The element cannot be added to itself");
        }

        if ((parent instanceof Element) &&
                ((Element) parent).isAncestor(element)) {
            throw new IllegalAddException(
                "The element cannot be added as a descendent of itself");
        }

        if (list == null) {
            if (index == 0) {
                ensureCapacity(INITIAL_ARRAY_SIZE);
            }
            else {
                throw new IndexOutOfBoundsException("Index: " + index +
                                                    " Size: " + size());
            }
        }

        if (parent instanceof Document) {
            if (indexOfFirstElement() >= 0) {
                throw new IllegalAddException(
                  "Cannot add a second root element, only one is allowed");
            }
        }

        list.add(index, element);
        element.parent = parent;

        modCount++;
    }

    /**
     * <p>
     * Check and add the <code>Comment</code> to this list at
     * the given index.
     * </p>
     *
     * @param index index where to add <code>Comment</code>
     * @param comment <code>Comment</code> to add
     */
    protected void add(int index, Comment comment) {
        if (comment == null) {
            throw new IllegalAddException("Cannot add null object");
        }

        if (comment.getParent() != null) {
            throw new IllegalAddException(
                          "The comment already has an existing parent \"" +
                          comment.getParent().getQualifiedName() + "\"");
        }

        if (list == null) {
            if (index == 0) {
                ensureCapacity(INITIAL_ARRAY_SIZE);
            }
            else {
                throw new IndexOutOfBoundsException("Index: " + index +
                                                    " Size: " + size());
            }
        }

        list.add(index, comment);
        comment.parent = parent;

        modCount++;
    }

    /**
     * <p>
     * Check and add the <code>ProcessingInstruction</code> to this list at
     * the given index.
     * </p>
     *
     * @param index index where to add <code>ProcessingInstruction</code>
     * @param pi <code>ProcessingInstruction</code> to add
     */
    protected void add(int index, ProcessingInstruction pi) {
        if (pi == null) {
            throw new IllegalAddException("Cannot add null object");
        }

        if (pi.getParent() != null) {
            throw new IllegalAddException(
                          "The PI already has an existing parent \"" +
                          pi.getParent().getQualifiedName() + "\"");
        }

        if (list == null) {
            if (index == 0) {
                ensureCapacity(INITIAL_ARRAY_SIZE);
            }
            else {
                throw new IndexOutOfBoundsException("Index: " + index +
                                                    " Size: " + size());
            }
        }

        list.add(index, pi);
        pi.parent = parent;

        modCount++;
    }

    /**
     * <p>
     * Check and add the <code>CDATA</code> to this list at
     * the given index.
     * </p>
     *
     * @param index index where to add <code>CDATA</code>
     * @param cdata <code>CDATA</code> to add
     */
    protected void add(int index, CDATA cdata) {
        if (cdata == null) {
            throw new IllegalAddException("Cannot add null object");
        }

        if (parent instanceof Document) {
            throw new IllegalAddException(
                          "A CDATA is not allowed at the document root");
        }

        if (cdata.getParent() != null) {
            throw new IllegalAddException(
                          "The CDATA already has an existing parent \"" +
                          cdata.getParent().getQualifiedName() + "\"");
        }

        if (list == null) {
            if (index == 0) {
                ensureCapacity(INITIAL_ARRAY_SIZE);
            }
            else {
                throw new IndexOutOfBoundsException("Index: " + index +
                                                    " Size: " + size());
            }
        }
        list.add(index, cdata);
        cdata.parent = (Element) parent;

        modCount++;
    }

    /**
     * <p>
     * Check and add the <code>Text</code> to this list at
     * the given index.
     * </p>
     *
     * @param index index where to add <code>Text</code>
     * @param text <code>Text</code> to add
     */
    protected void add(int index, Text text) {
        if (text == null) {
            throw new IllegalAddException("Cannot add null object");
        }

        if (parent instanceof Document) {
            throw new IllegalAddException(
                          "A Text not allowed at the document root");
        }

        if (text.getParent() != null) {
            throw new IllegalAddException(
                          "The Text already has an existing parent \"" +
                          text.getParent().getQualifiedName() + "\"");
        }

        if (list == null) {
            if (index == 0) {
                ensureCapacity(INITIAL_ARRAY_SIZE);
            }
            else {
                throw new IndexOutOfBoundsException("Index: " + index +
                                                    " Size: " + size());
            }
        }
        list.add(index, text);
        text.parent = (Element) parent;

        modCount++;
    }

    /**
     * <p>
     * Check and add the <code>EntityRef</code> to this list at
     * the given index.
     * </p>
     *
     * @param index index where to add <code>Entity</code>
     * @param entity <code>Entity</code> to add
     */
    protected void add(int index, EntityRef entity) {
        if (entity == null) {
            throw new IllegalAddException("Cannot add null object");
        }

        if (parent instanceof Document) {
            throw new IllegalAddException(
                        "An EntityRef is not allowed at the document root");
        }

        if (entity.getParent() != null) {
            throw new IllegalAddException(
                          "The EntityRef already has an existing parent \"" +
                          entity.getParent().getQualifiedName() + "\"");
        }

        if (list == null) {
            if (index == 0) {
                ensureCapacity(INITIAL_ARRAY_SIZE);
            }
            else {
                throw new IndexOutOfBoundsException("Index: " + index +
                                                    " Size: " + size());
            }
        }
        list.add(index, entity);
        entity.parent = (Element) parent;

        modCount++;
    }

    /**
     * <p>
     * Add the specified collecton to the end of this list.
     * </p>
     *
     * @param collection The collection to add to the list.
     * @return <code>true</code> if the list was modified as a result of
     *                           the add.
     */
    public boolean addAll(Collection collection) {
        return addAll(size(), collection);
    }

    /**
     * <p>
     * Inserts the specified collecton at the specified position in this list.
     * Shifts the object currently at that position (if any) and any
     * subsequent objects to the right (adds one to their indices).
     * </p>
     *
     * @param index The offset to start adding the data in the collection
     * @param collection The collection to insert into the list.
     * @return <code>true</code> if the list was modified as a result of
     *                           the add.
     * throws IndexOutOfBoundsException if index < 0 || index > size()
     */
    public boolean addAll(int index, Collection collection) {
        if ((list == null) && (index != 0)) {
            throw new IndexOutOfBoundsException("Index: " + index +
                                                " Size: " + size());
        }

        if ((collection == null) || (collection.size() == 0)) {
            return false;
        }

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
                remove(index + i);
            }
            throw exception;
        }

        return true;
    }

    /**
     * <p>
     * Clear the current list and set it to the contents
     * of the <code>Collection</code>.
     * object.
     * </p>
     *
     * @param collection The collection to use.
     */
    protected void clearAndSet(Collection collection) {
        ArrayList old = list;
        list = null;

        if ((collection != null) && (collection.size() != 0)) {
            ensureCapacity(collection.size());
            try {
                addAll(0, collection);
            }
            catch (RuntimeException exception) {
                list = old;
                throw exception;
            }
        }

        if (old != null) {
            for (int i = 0; i < old.size(); i++) {
                removeParent(old.get(i));
            }
        }
    }

    /**
     * <p>
     * Increases the capacity of this <code>ContentList</code> instance,
     * if necessary, to ensure that it can hold at least the number of
     * items specified by the minimum capacity argument.
     * </p>
     *
     * @param minCapacity the desired minimum capacity.
     */
    protected void ensureCapacity(int minCapacity) {
        if (list == null) {
            list = new ArrayList(minCapacity);
        }
        else {
            list.ensureCapacity(minCapacity);
        }
    }

    /**
     * <p>
     * Return the object at the specified offset.
     * </p>
     *
     * @param index The offset of the object.
     * @return The Object which was returned.
     */
    public Object get(int index) {
        if (list == null) {
            throw new IndexOutOfBoundsException("Index: " + index +
                                                " Size: " + size());
        }

        return list.get(index);
    }

    /**
     * <p>
     * Return a view of this list based on the given filter.
     * </p>
     *
     * @param filter <code>Filter</code> for this view.
     * @return a list representing the rules of the <code>Filter</code>.
     */
    protected List getView(Filter filter) {
        return new FilterList(filter);
    }

    /**
     * <p>
     * Return the index of the first Element in the list.  If the parent
     * is a <code>Document</code> then the element is the root element.
     * If the list contains no Elements, it returns -1.
     * </p>
     *
     * @return index of first element, or -1 if one doesn't exist
     */
    protected int indexOfFirstElement() {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) instanceof Element) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * <p>
     * Remove the object at the specified offset.
     * </p>
     *
     * @param index The offset of the object.
     * @return The Object which was removed.
     */
    public Object remove(int index) {
        if (list == null)
            throw new IndexOutOfBoundsException("Index: " + index +
                                                 " Size: " + size());

        Object old = list.get(index);
        removeParent(old);
        list.remove(index);
        modCount++;
        return old;
    }

    /** Remove the parent of a Object */
    private void removeParent(Object obj) {
        if (obj instanceof Element) {
            Element element = (Element) obj;
            element.parent = null;
        }
        else if (obj instanceof Comment) {
            Comment comment = (Comment) obj;
            comment.parent = null;
        }
        else if (obj instanceof ProcessingInstruction) {
            ProcessingInstruction pi = (ProcessingInstruction) obj;
            pi.parent = null;
        }
        else if (obj instanceof CDATA) {
            CDATA cdata = (CDATA) obj;
            cdata.parent = null;
        }
        else if (obj instanceof Text) {
            Text text = (Text) obj;
            text.parent = null;
        }
        else if (obj instanceof EntityRef) {
            EntityRef entity = (EntityRef) obj;
            entity.parent = null;
        }
    }

    /**
     * <p>
     * Set the object at the specified location to the supplied
     * object.
     * </p>
     *
     * @param index The location to set the value to.
     * @param obj The location to set the value to.
     * @return The object which was replaced.
     * throws IndexOutOfBoundsException if index < 0 || index >= size()
     */
    public Object set(int index, Object obj) {
        if (list == null)
            throw new IndexOutOfBoundsException("Index: " + index +
                                                 " Size: " + size());

        if ((obj instanceof Element) && (parent instanceof Document)) {
            int root = indexOfFirstElement();
            if ((root >= 0) && (root != index)) {
                throw new IllegalAddException(
                  "Cannot add a second root element, only one is allowed");
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
     * <p>
     * Return the number of items in this list
     * </p>
     *
     * @return The number of items in this list.
     */
    public int size() {
        if (list == null)
            return 0;
        return list.size();
    }

    /**
     * <p>
     * Return this list as a <code>String</code>
     * </p>
     *
     * @return The number of items in this list.
     */
    public String toString() {
        if ((list != null) && (list.size() > 0))
             return list.toString();
        else return "[]";
    }

    /** Give access of ContentList.modCount to FilterList */
    private int getModCount() {
        return modCount;
    }

    /* * * * * * * * * * * * * FilterList * * * * * * * * * * * * * * * */
    /* * * * * * * * * * * * * FilterList * * * * * * * * * * * * * * * */

    /**
     * <p>
     * <code>FilterList</code> represents valid JDOM content, including content
     * for <code>Document</code>s or <code>Element</code>s.
     * </p>
     */

    class FilterList extends AbstractList {

        /** The Filter */
        protected Filter filter;

        /** Current number of items in this view */
        int count = 0;

        /** Expected modCount in our backing list */
        int expected = 0;

        // Implementation Note: Directly after size() is called, expected
        //       is sync'd with ContentList.modCount and count provides
        //       the true size of this view.  Before the first call to
        //       size() or if the backing list is modified outside this
        //       FilterList, both might contain bogus values and should
        //       not be used without first calling size();

        /**
         * <p>
         * Create a new instance of the FilterList with the specified Filter.
         * </p>
         */
        FilterList(Filter filter) {
            this.filter = filter;
        }

        /**
         * <p>
         * Inserts the specified object at the specified position in this list.
         * Shifts the object currently at that position (if any) and any
         * subsequent objects to the right (adds one to their indices).
         * </p>
         *
         * @param index The location to set the value to.
         * @param obj The object to insert into the list.
         * throws IndexOutOfBoundsException if index < 0 || index > size()
         */
        public void add(int index, Object obj) {
            if (filter.canAdd(obj)) {
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
         * <p>
         * Return the object at the specified offset.
         * </p>
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
         * <p>
         * Remove the object at the specified offset.
         * </p>
         *
         * @param index The offset of the object.
         * @return The Object which was removed.
         */
        public Object remove(int index) {
            int adjusted = getAdjustedIndex(index);
            Object old = ContentList.this.get(adjusted);
            if (filter.canRemove(old)) {
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
         * <p>
         * Set the object at the specified location to the supplied
         * object.
         * </p>
         *
         * @param index The location to set the value to.
         * @param obj The location to set the value to.
         * @return The object which was replaced.
         * throws IndexOutOfBoundsException if index < 0 || index >= size()
         */
        public Object set(int index, Object obj) {
            Object old = null;
            if (filter.canAdd(obj)) {
                int adjusted = getAdjustedIndex(index);
                old = ContentList.this.get(adjusted);
                if (!filter.canRemove(old)) {
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
         * <p>
         * Return the number of items in this list
         * </p>
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
                Object obj = ContentList.this.list.get(i);
                if (filter.matches(obj)) {
                    count++;
                }
            }
            expected = ContentList.this.getModCount();
            return count;
        }

        /**
         * <p>
         * Return the adjusted index
         * </p>
         *
         * @param index Index of in this view.
         * @return True index in backing list
         */
        final private int getAdjustedIndex(int index) {
            int adjusted = 0;
            for (int i = 0; i < ContentList.this.list.size(); i++) {
                Object obj = ContentList.this.list.get(i);
                if (filter.matches(obj)) {
                    if (index == adjusted) {
                        return i;
                    }
                    adjusted++;
                }
            }

            if (index == adjusted) {
                return list.size();
            }

            return list.size() + 1;
        }
    }

    /* * * * * * * * * * * * * FilterListIterator * * * * * * * * * * * */
    /* * * * * * * * * * * * * FilterListIterator * * * * * * * * * * * */

    class FilterListIterator implements ListIterator {

        /**
         * Used to help hasNext and hasPrevious the next
         * index of our cursor
         */
        static final int CREATE  = 0;
        static final int HASPREV = 1;
        static final int HASNEXT = 2;
        static final int PREV    = 3;
        static final int NEXT    = 4;
        static final int ADD     = 5;
        static final int REMOVE  = 6;
        static final int SET     = 7;

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
            case NEXT:    cursor = moveForward(last + 1);
                          break;
            case ADD:
            case REMOVE:  cursor = moveForward(last);
                          break;
            case HASPREV: cursor = moveForward(cursor + 1);
                          break;
            default:      break;
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
                throw new IllegalStateException("no next item");
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
                          if (cursor >= ContentList.this.size()) {
                               cursor = moveBackward(initialCursor);
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
            default:      break;
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
                throw new IllegalStateException("no previous item");
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

            if ((lastOperation != PREV) && (lastOperation != NEXT)) {
                throw new IllegalStateException("no preceeding call to " +
                                                 "prev() or next()");
            }

            if (filter.canAdd(obj)) {
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

            if ((lastOperation != PREV) && (lastOperation != NEXT)) {
                throw new IllegalStateException("no preceeding call to " +
                                                 "prev() or next()");
            }

            Object old = ContentList.this.get(last);
            if (filter.canRemove(old)) {
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

            if ((lastOperation != PREV) && (lastOperation != NEXT)) {
                throw new IllegalStateException("no preceeding call to " +
                                                 "prev() or next()");
            }

            if (filter.canAdd(obj)) {
                Object old = ContentList.this.get(last);
                if (!filter.canRemove(old)) {
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
            lastOperation = REMOVE;
        }

        /**
         * Returns index in the backing list by moving forward start + 1
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
