/*-- 

 $Id: FilterList.java,v 1.1 2001/12/11 07:32:04 jhunter Exp $

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

import java.util.List;
import java.util.Iterator;
import java.util.Collection;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

import java.lang.reflect.Array;

import java.io.Serializable;

/**
 * <p>
 * The <code>FilterList</code> class is a wrapper around a list which
 * is filtertered according the the <code>Filter<code> applied to the
 * list.
 * </p>
 *
 * @author Jools Enticknap
 * @author Alex Rosen
 * @version 1.0
 */
public class FilterList implements List, Serializable {

    /** The filter */
    protected Filter filter;

    /**
     * <p>
     * Create a new instance of the FilterList using the
     * supplied Filter.
     * </p>
     *
     * @param listFilter The filter to apply to the list.
     */
    public FilterList(Filter filter) {
        setFilter(filter);
    }

    // Ensure that our backing list is created. This is called before any
    // modifications are made. For getters, we treat a null list the same
    // as an empty list; but for setters, we need to make sure that the list
    // is not null, so we can make the requested change. (For the most part
    // this only makes sense for add(), not remove() or set(), since if the
    // list is empty/null, then there's nothing to remove or replace.)
    private List getBackingList(boolean create) {
        return filter.getBackingList(create);
    }

    /**
     * <p>
     * Set a <code>Filter</code> for this list.
     * </p>
     *
     * @param listFilter The filter to apply to the list.
     */
    void setFilter(Filter filter) {
        this.filter = filter;
    }
    
    /**
     * <p>
     * Get the current Filter assigned to this list.
     * </p>
     *
     * @return The current <code>Filter</code>.
     */
    Filter getFilter() {
        return filter;
    }
    
    /**
     * <p>
     * Return the size of the list.
     * </p>
     *
     * @return An <code>int</code> representing the number of items in the list.
     */
    public int size() {
        List list = getBackingList(false);

        if (list == null) {
            // No backing list, our size must be zero.
            return 0;
        } else if (filter.matchesAll()) {
            // No filtering, just use the list size.
            return list.size();
        } else {
            // Calculate how many items are in the view.
            int size = 0;
            Iterator iter = list.iterator(); 
            while (iter.hasNext()) {
                // Optimized call for speed as have already called
                // isNull.
                if (filter.matches(iter.next())) {
                    size++;
                }
            }
        return size;
        }
    }

    /**
     * <p>
     * Returns <code>true</code> if this collection contains no elements.
     * </p>
     *
     * @return <code>true</code> if this collection contains no elements
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * <p>
     * Returns <code>true</code> if this collection contains the specified
     * object. Unlike standard collections <code>null</code>'s are not 
     * allowed.
     * </p>
     *
     * @param obj Object whose presence in this collection is to be tested.
     * @return <code>true</code> if this collection contains the specified
     *         element
     */
    public boolean contains(Object obj) {
        List list = getBackingList(false);
        if (list == null || obj == null) {
            return false;
        }
        
        // Check the filter before the list.
        if (!filter.matches(obj)) {
            return false;
        }

        return list.contains(obj);
    }

    /**
     * <p>
     * Check this collection against the supplied one to see if contains all the
     * same objects.
     * </p>
     * 
     * @param c Contains the object to check against this collection.
     */
    public boolean containsAll(Collection c) {
        List list = getBackingList(false);

        if (list == null) {
            return false;
        }
        
        if (filter.matchesAll()) {
            return list.containsAll(c);
        }

        for (Iterator iter = c.iterator(); iter.hasNext(); ) {
            Object obj = iter.next();
            if (obj == null || 
                    !filter.matches(obj) || 
                    !list.contains(obj)) {
                return false;
            }
        }

        return true;
    }

    /**
     * <p>
     * Return an <code>Iterator</code> starting at offset 0.
     * </p>
     *
     * @return An end to end <code>Iterator</code>
     */
    public Iterator iterator() {
        return listIterator();
    }

    /**
     * <p>
     * Return an <code>Object[]</code> containing add the data objects in the
     * list.
     * </p>
     *
     * @return An array containing all the objects in this collection.
     */
    public Object[] toArray() {
        List list = getBackingList(false);

        if (list == null) {
            return new Object[0];
        }

        if (filter.matchesAll()) {
            return list.toArray();
        }

        Object[] ret = new Object[size()];

        int i = 0;
        for(Iterator iter = iterator(); iter.hasNext();) {
            ret[i++] = iter.next();
        }
        
        return ret;
    }
    
    /**
     * <p>
     * Returns an array containing all of the elements in this list in proper 
     * sequence, the runtime type of the returned array is that of the specified
     * array
     * </p>
     *
     * @param a The array to populate.
     * @return The populated array.
     */ 
    public Object[] toArray(Object a[]) {
        List list = getBackingList(false);

        if (a == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }

        if (list == null) {
            // toArray() contract says that the list is null-terminated.
            if (a.length > 0) {
                a[0] = null;
            }
            return a;
        }

        if (filter.matchesAll()) {
            return list.toArray(a);
        }

        int s = size();

        // Create an array big enough to hold all the elements
        if (a.length < s) {
            a = (Object[])Array.newInstance(a.getClass().getComponentType(), s);
        }
        
        // Loop through all the entries and add them to the array.
        int i = 0;
        for(Iterator iter = iterator(); iter.hasNext();) {
            a[i++] = iter.next();
        }

        // toArray() contract says that the list is null-terminated.
        if (a.length > s) {
            a[s] = null;
        }

        return a;
    }

    /**
     * <p>
     * Adds an object to the end of the list.
     * </p>
     *
     * @param o The object to add.
     * @return <code>true</code> if the list was modified as a result of 
     * the add.
     */
    public boolean add(Object obj) {
        checkAdd(obj);
        List list = getBackingList(true);
        return list.add(obj);
    }
    
    /**
     * <p>
     * Check that an item can be added to the list.
     * </p>
     *
     * @param obj The object to be added.
     */
    private void checkAdd(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("null items not allowed in list");
        }
        
        if (!filter.canAdd(obj)) {
            throw new IllegalArgumentException(
                "The Filter assigned to this list will not allow the " +
                "supplied object ("+obj.getClass().getName()+") to be "+
                "added to the list.");
        }
    }
    
    /**
     * <p>
     * Check that an item can be removed from the list.
     * </p>
     *
     * @param obj The object to be added.
     */
    private void checkRemove(Object obj) {
        if (!filter.canRemove(obj)) {
            throw new IllegalArgumentException(
                "The Filter assigned to this list will not allow the " +
                "supplied object ("+obj.getClass().getName()+") to be "+
                "removed from the list.");
        }
    }
    
    /**
     * <p>
     * Check that the supplied collection contains objects which can be
     * added to the list.
     * </p>
     *
     * @param c Check that the collection only contains objects which can 
     *          be added.
     */
    private boolean checkAdd(Collection c) {
        if (c.size() == 0) {
            return false;
        }
        
        // XXX leaves list in an inconsistent state if checkAdd() fails
        Iterator iter = c.iterator(); 
        while (iter.hasNext()) {
            checkAdd(iter.next());
        }
        
        return true;
    }

    /**
     * <p>
     * Add all the objects in the specified collection.
     * </p>
     * 
     * @param c The collection containing all the objects to add.
     * @return <code>true</code> if the list was modified as a result of 
     * the add.
     */
    public boolean addAll(Collection c) {
        checkAdd(c);
        List list = getBackingList(true);
        return list.addAll(c);
    }

    /**
     * <p>
     * Adds an object to the end of the list.
     * </p>
     * 
     * @param index The offset to start adding the data in the collection
     * @return <code>true</code> if the list was modified as a result of 
     * the add.
     */
    public boolean addAll(int index, Collection c) {
        checkAdd(c);
        List list = getBackingList(true);

        if (filter.matchesAll() || index == 0) {
            return list.addAll(index, c);
        }

        // cnt is the index into our list; cnt2 is the index into the 
        // backing list.
        int cnt = 0, cnt2 = 0;
        Object ret = null;
        ListIterator iter = list.listIterator(); 
        while (iter.hasNext()) {
            Object obj = iter.next();
            cnt2++;
            if (filter.matches(obj)) {
                cnt++;
                if (cnt == index) {
                    ret = obj;
                    break;
                }
            }
        }

        // If we never found the requested index (cnt), then the requested
        // index was too large (index > size()).
        if (ret == null) {
            throw new IndexOutOfBoundsException(
                "Index: "+index+", Size: "+size());
        }

        // Add all the items to this location.
        return list.addAll(cnt2, c);
    }
    
    /**
     * <p>
     * Remove the specifed object from the list if the list contains it.
     * </p>
     *
     * @return <code>true</code> if the object was removed.
     */
    public boolean remove(Object obj) {
        if (obj == null) {
            return false;
        }
        
        checkRemove(obj);
        List list = getBackingList(true);
        return list.remove(obj);
    }
    
    /**
     * <p>
     * Remove all the items in the supplied collection.
     * </p>
     *
     * @param c The collection containing the items to remove.
     * @return <code>true</code> if this list changed as a result of the call.
     */
    public boolean removeAll(Collection c) {
        Object obj = null;

        // XXX leaves list in an inconsistent state if checkRemove() fails
        for (Iterator iter = c.iterator(); iter.hasNext();) {
            obj = iter.next();
            checkRemove(obj);
        }
        
        List list = getBackingList(true);
        return list.removeAll(c);
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
        List list = getBackingList(true);

        if (filter.matchesAll()) {
            Object ret = list.get(index);
            checkRemove(ret);
            list.remove(index);
            return ret;
        }

        Object ret = null;
        Iterator iter = list.iterator(); 
        int cnt = 0;
        while(iter.hasNext()) {
            Object obj = iter.next();
            if (filter.matches(obj)) {
                if (cnt == index) {
                    checkRemove(obj);
                    iter.remove();
                    ret = obj;
                    break;
                }
                cnt++;
            }
        }
        
        // If we never found the requested item (ret), then the requested
        // index was too large (index > size()).
        if (ret == null) {
            throw new IndexOutOfBoundsException(
                "Index: "+index+", Size: "+size());
        }

        return ret;
    }

    /**
     * <p>
     * Retain only the items in the supplied collection.
     * </p>
     * <p>
     * NOTE: Items in the backing list that do not match our filter
     * are not removed from the backing list (but of course that fact
     * is not visible in our list).
     * </p>
     *
     * @return <code>true</code> if this list changed as a result of the call.
     */
    public boolean retainAll(Collection c) {
        // XXX leaves list in an inconsistent state if checkRemove() fails
        boolean removed = false;
        List list = getBackingList(true);

        Iterator iter = list.iterator(); 
        while(iter.hasNext()) {
            Object obj = iter.next();
            if (filter.matches(obj)) {
                if (!c.contains(obj)) {
                    checkRemove(obj);
                    iter.remove();
                    removed = true;
                }
            }
        }
        
        return removed;
    }
    
    /**
     * <p>
     * Remove all the items in the list.
     * </p>
     * <p>
     * NOTE: Items in the backing list that do not match our filter
     * are not removed from the backing list (but of course that fact
     * is not visible in our list).
     * </p>
     */
    public void clear() {
        List list = getBackingList(false);

        if (list == null) {
            // If no backing list, then we're already done.
            return;
        }
        
        // XXX leaves list in an inconsistent state if checkRemove() fails
        Iterator iter = list.iterator(); 
        while(iter.hasNext()) {
            Object obj = iter.next();
            if (filter.matches(obj)) {
                checkRemove(obj);
                iter.remove();
            }
        }
    }

    /**
     * <p>
     * Performs the equality test described in the List contract.
     * </p>
     */
    public boolean equals(Object obj) {
        // Quick checks.
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof List)) {
            return false;
        }

        List other = (List)obj;
        List list = getBackingList(false);

        // If no backing list, then we're equal to a zero-length list.
        if (list == null) {
            return (other.size() == 0);
        }

        // Get an iterator for each list.
        Iterator iter = iterator();
        Iterator iter2 = other.iterator();
        while(iter.hasNext()) {
            // If the other list is shorter, return false.
            if (!iter2.hasNext()) {
                return false;
            }
            // See if the two current elements are equal.
            Object child = iter.next();
            Object child2 = iter2.next();
            if ((child == null && child2 != null) 
                        || (child != null && child2 == null) 
                        || !child.equals(child2)) {
                return false;
            }
        }

        // If this list is shorter, return false.
        if (iter2.hasNext()) {
            return false;
        }

        return true;
    }
    
    /**
     * <p>
     * Performs the hashcode computation described in the List contract.
     * </p>
     */
    public int hashCode() {
        List list = getBackingList(false);

        if (list == null) {
            return 1;
        }

        // Copied from the JavaDoc of List.hashCode()
        int hashCode = 1;
        Iterator i = iterator();
        while (i.hasNext()) {
            Object obj = i.next();
            hashCode = 31*hashCode + (obj==null ? 0 : obj.hashCode());
        }
        return hashCode;
     }

    /**
     * <p>
     * Get the object at a specific location.
     * </p>
     *
     * @param index The offset of the object to locate.
     * @return The object at the specified location.
     */
    public Object get(int index) {
        List list = getBackingList(false);

        if (list == null) {
            throw new IndexOutOfBoundsException(
                "Index: "+index+", Size: "+size());
        }

        if (filter.matchesAll()) {
            return list.get(index);
        }

        if (index < 0) {
            throw new IndexOutOfBoundsException("Index "+index+" is negative");
        }
        
        int cnt = 0;
        Object ret = null;
        Iterator iter = list.iterator(); 
        while (iter.hasNext()) {
            Object obj = iter.next();
            if (filter.matches(obj)) {
                if (cnt == index) {
                    ret = obj;
                    break;
                }
                cnt++;
            }
        }

        // If we never found the requested item (ret), then the requested
        // index was too large (index > size()).
        if (ret == null) {
            throw new IndexOutOfBoundsException(
                "Index: "+index+", Size: "+size());
        }

        return ret;
    }
    
    /**
     * <p>
     * Set the object at the specified location to the supplied
     * object.
     * </p>
     *
     * @param index The location to set the value to.
     * @param newobj The location to set the value to.
     * @return The object which was replaced.
     */
    public Object set(int index, Object newobj) {
        List list = getBackingList(true);

        if (filter.matchesAll()) {
            Object ret = list.get(index);
            if (ret != newobj) {
                checkAdd(newobj);
                checkRemove(ret); // XXX if this throws, then "newobj" will be in an inconsistent state.
                list.set(index, newobj);
            }
            return ret;
        }

        int cnt = 0;
        Object ret = null;
        ListIterator iter = list.listIterator(); 
        while (iter.hasNext()) {
            Object obj = iter.next();
            if (filter.matches(obj)) {
                if (cnt == index) {
                    if (obj != newobj) {
                        checkAdd(newobj);
                        checkRemove(obj);
                        iter.set(newobj); // XXX if this throws, then "newobj" will be in an inconsistent state.
                    }
                    ret = obj;
                    break;
                }
                cnt++;
            }
        }

        // If we never found the requested item (ret), then the requested
        // index was too large (index > size()).
        if (ret == null) {
            throw new IndexOutOfBoundsException(
                "Index: "+index+", Size: "+size());
        }

        return ret;
    }
    
    /**
     * <p>
     * Inserts the specified element at the specified position in this list. 
     * Shifts the element currently at that position (if any) and any 
     * subsequent elements to the right (adds one to their indices).
     * </p>
     *
     * @param index The location to set the value to.
     * @param obj The location to set the value to.
     */
    public void add(int index, Object newobj) {
        checkAdd(newobj);
        List list = getBackingList(true);

        if (filter.matchesAll() || index == 0) {
            list.add(index, newobj);
            return;
        }

        int cnt = 0;
        Object ret = null;
        ListIterator iter = list.listIterator(); 
        while (iter.hasNext()) {
            Object obj = iter.next();
            if (filter.matches(obj)) {
                cnt++;
                if (cnt == index) {
                    iter.add(newobj);
                    ret = obj;
                    break;
                }
            }
        }

        // If we never found the requested item (ret), then the requested
        // index was too large (index > size()).
        if (ret == null) {
            throw new IndexOutOfBoundsException(
                "Index: "+index+", Size: "+size());
        }
    }
    
    /**
     * <p>
     * Get the index (offset) of the object in the list.
     * </p>
     *
     * @param obj The Object to find locate.
     * @return The location of the object or -1
     */
    public int indexOf(Object obj) {
        List list = getBackingList(false);

        if (list == null) {
            return -1;
        }

        if (filter.matchesAll()) {
            return list.indexOf(obj);
        }

        if (!filter.matches(obj)) {
            return -1;
        }
        
        int cnt = 0;
        Object ret = null;
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            ret = iter.next();
            if (filter.matches(ret)) {
                if (ret.equals(obj)) {
                    return cnt;
                }
                cnt++;
            }
        }

        return -1;
    }

    /**
     * <p>
     * Get the index of the last occurance of the object in the list.
     * </p>
     * 
     * @return The index of the last location of the item in the list.
     */
    public int lastIndexOf(Object obj) {
        List list = getBackingList(false);

        if (list == null) {
            return -1;
        }

        if (filter.matchesAll()) {
            return list.lastIndexOf(obj);
        }

        if (!filter.matches(obj)) {
            return -1;
        }
        
        int cnt = size();
        Object ret = null;
        ListIterator iter = list.listIterator(list.size());
        while (iter.hasPrevious()) {
            ret = iter.previous();
            if (filter.matches(ret)) {
                cnt--;
                if (ret.equals(obj)) {
                   return cnt;
                }
            }
        }
        
        return -1;
    }

    /**
     * <p>
     * Obtain a list iterator.
     * </p>
     *
     * @return A ListIterator.
     */
    public ListIterator listIterator() {
        return listIterator(0);
    }

    /**
     * <p>
     * Crete a listIterator starting at the specified location.
     * </p>
     *
     * @param index The starting point for the Iterator.
     * @return A ListIterator starting at the specified index.
     */
    public ListIterator listIterator(int index) {
        // Note that even if filter.matchesAll() is true, we can't just
        // return the iterator of our backing list, because ListIterator 
        // has add() and remove() methods that we have to check.
        return new FilterListIterator(index);
    }

    /**
     * <p>
     * Create a live list containing only items between the two indexes.
     * Changes to the returned list will affect this list.
     * </p>
     *
     * @param fromIndex The starting index, inclusive.
     * @param toIndex   The end index, exclusive.
     * @return A live list containing the items between the start and end index.
     */
    public List subList(int fromIndex, int toIndex) {
        List list = getBackingList(false);

        if (list == null) {
            // If we have no backing list (or, in fact, any time our length is
            // zero), then fromIndex and toIndex must both be zero.
            if (fromIndex != 0 || toIndex != 0) {
                throw new IndexOutOfBoundsException(
                    "fromIndex: "+fromIndex+", toIndex: "+toIndex+
                    ", Size: "+size());
            }
            // ... and if they are, then the desired sublist is 
            // indistinguishable from this. So we return this.
            return this;
        }

        if (filter.matchesAll()) {
            Filter subListFilter =
              new PassThroughFilter(list.subList(fromIndex, toIndex), filter);
            return new FilterList(subListFilter);
        }

        if (fromIndex < 0) {
            throw new IndexOutOfBoundsException(
                "Index: "+fromIndex+", Size: "+size());
        }
        if (toIndex > size()) {
            throw new IndexOutOfBoundsException(
                "Index: "+toIndex+", Size: "+size());
        }
        if (fromIndex > toIndex) {
            throw new IndexOutOfBoundsException(
                "fromIndex: "+fromIndex+", toIndex: "+toIndex);
        }

        // fromIndex, toIndex, and cnt are indexes into our list.
        // fromIndex2, toIndex2, and cnt2 are indexes into our backing list.
        int cnt = 0, cnt2 = 0, fromIndex2 = -1, toIndex2 = -1;

        Object ret = null;
        ListIterator iter = list.listIterator(); 
        while (iter.hasNext()) {
            ret = iter.next();
            if (filter.matches(ret)) {
                // If we've found the desired index in our list, then
                // record this corresponding index in the backing list.
                if (cnt == fromIndex) {
                    fromIndex2 = cnt2;
                }
                if (cnt == toIndex) {
                    toIndex2 = cnt2;
                    break;
                }
                cnt++;
            }
            cnt2++;
        }

        // If fromIndex == size(), then fromIndex2 was never set above.
        if (fromIndex2 < 0)
            fromIndex2 = list.size();
        // If toIndex == size(), then toIndex2 was never set above.
        if (toIndex2 < 0)
            toIndex2 = list.size();

        // Add all the items to this location.
        Filter subListFilter =
          new PassThroughFilter(list.subList(fromIndex2, toIndex2), filter);
        return new FilterList(subListFilter);
    }
    
    private class PassThroughFilter implements Filter {
        private List list;
        private Filter filter;

        PassThroughFilter(List list, Filter filter) {
            this.list = list;
            this.filter = filter;
        }

        public boolean matches(Object obj) {
            return filter.matches(obj);
        }
        public boolean matchesAll() {
            return filter.matchesAll();
        }
        public boolean canAdd(Object obj) {
            return filter.canAdd(obj);
        }
        public boolean canRemove(Object obj) {
            return filter.canRemove(obj);
        }
        public List getBackingList(boolean create) {
            return list;
        }
    }

    /**
     * <p>
     * A bi-directional Iterator.
     * </p>
     *
     * @author Jools Enticknap
     * @author Alex Rosen
     * @version 1.0
     */
    private class FilterListIterator implements ListIterator {

        /** Reference to the object to be returned on next call to next(). */
        protected Object next;
        
        /** Reference to the object to be returned on next call to
            previous(). */
        protected Object previous;

        /** Reference to the last object we returned from either next() or 
            previous(). */
        protected Object ret;

        /** Indicates the number of steps, and the direction, that would be 
            needed to resynchronize our backing iterator's state with our 
            state. */
        protected int stepsNeededToSync;

        /** Positive if our last move was forward, negative if backward. */
        protected int lastMoveDirection;

        /** The backing list */
        protected List list;
        
        /** The backing iterator */
        protected ListIterator iterator;
        
        /** Current index */
        protected int index;

        /**
         * <p>
         * Create a new iterator using the supplied iterator.
         * </p>
         *
         * @param iterator The backing iterator.
         */
        FilterListIterator(int index) {
            list = getBackingList(false);

            if (list != null) {
                iterator = list.listIterator();
            }

            if (index < 0) {
                throw new IndexOutOfBoundsException(
                    "Index: "+index+", Size: "+size());
            }

            // Advance the iterator to the requested spot.
            for (int i = 0; i < index; i++) {
                if (!hasNext()) {
                    throw new IndexOutOfBoundsException(
                        "Index: "+index+", Size: "+size());
                }

                next();
            }

            // To be completely compatible with the standard ListIterator,
            // we must throw an exception if the user tries to do a set() 
            // or remove() immediately after creation, so we set ret to null.
            ret = null;
        }
        
        /**
         * <p>
         * Check to see if a call to next will succeed.
         * </p>
         *
         * @return <code>true</code> if a call to next will succeed.
         */
        public boolean hasNext() {
            checkList();

            // We have already located the next item.
            // so we do have a next.
            if (next != null) {
                return true;
            }

            if (list == null) {
                return false;
            }

            sync();
           
            // Use the filter to located the next item
            while (iterator.hasNext()) {
                next = iterator.next();
                stepsNeededToSync--;

                // Get the filter to check the item
                if (FilterList.this.filter.matches(next)) {
                    return true;
                }
            } 
            
            // Didn't find another matching item, so it's game over.
            next = null;
            return false;
        }
        
        /**
         * <p>
         * Get the next item from the iterator.
         * </p>
         *
         * @return The next item.
         */
        public Object next() {
            checkList();

            // See if we can get another item.
            if (next == null) {
                hasNext();
            }
            
            ret = next;
            next = null;

            if (ret == null) {
                throw new NoSuchElementException();
            }

            // We're now in sync, and our last move was forwards.
            stepsNeededToSync = 0;
            lastMoveDirection = 1;

            // Adjust the index.
            index++;

            return ret;
        }
        
        /**
         * <p>
         * Check to see if a call to previous() will succeed.
         * </p>
         *
         * @return <code>true</code> if a call to previous() will succeed.
         */
        public boolean hasPrevious() {
            checkList();

            // We have already located the previous item.
            // so we do have a previous.
            if (previous != null) {
                return true;
            }

            if (list == null) {
                return false;
            }

            sync();

            // Iterate backwards until we find 
            while (iterator.hasPrevious()) {
                previous = iterator.previous();
                stepsNeededToSync++;

                 // Found one. 
                if (FilterList.this.filter.matches(previous)) {
                    return true;
                }
            } 
            
            // Didn't find another matching item, so it's game over.
            previous = null;
            return false;
        }
        
        /**
         * <p>
         * Get the previous item.
         * </p>
         *
         * @return The Object at the previous location.
         */
        public Object previous() {
            checkList();

            if (previous == null) {
                hasPrevious();
            }
           
            ret = previous;
            previous = null;

            if (ret == null) {
                throw new NoSuchElementException();
            }

            // We're now in sync, and our last move was backwards.
            lastMoveDirection = -1;
            stepsNeededToSync = 0;

            // Adjust the index.
            index--;

            return ret;
        }

        /**
         * <p>
         * Get the index of the next item.
         * </p>
         *
         * @return The index of the next item.
         */
        public int nextIndex() {
            checkList();
            return index;
        }
        
        /**
         * <p>
         * Get the index of the previous object.
         * </p>
         *
         * @return The index of the previous object.
         */
        public int previousIndex() {
            checkList();
            return index - 1;
        }

        /**
         * <p>
         * Add the supplied object at the current location.
         * </p>
         *
         * @param obj Object to add after the current object.
         */
        public void add(Object o) {
            ensureIteratorBackingList();
            sync();

            FilterList.this.checkAdd(o);
            iterator.add(o);

            ret = null;
            next = null;
            previous = null;

            // Adjust the index.
            index++;
        }

        /**
         * <p>
         * Remove the current item.
         * </p>
         */
        public void remove() {
            ensureIteratorBackingList();
            sync();

            if (ret == null) {
                // See JavaDoc for ListIterator.remove()
                throw new IllegalStateException("Cannot call remove() now.");
            }

            FilterList.this.checkRemove(ret);
            iterator.remove();
            ret = null;
            next = null;
            previous = null;

            // Adjust the index.
            if (lastMoveDirection == 1) {
                index--;
            }
        }

        /**
         * <p>
         * Set the object at the current location.
         * </p>
         *
         * @param o The Object to set to the current location.
         */
        public void set(Object o) {
            ensureIteratorBackingList();
            sync();

            if (ret == null) {
                // See JavaDoc for ListIterator.set()
                throw new IllegalStateException("Cannot call set() now.");
            }

            if (ret != o) {
                FilterList.this.checkAdd(o);
                // XXX if this throws, "o" will be in an inconsistent state.
                FilterList.this.checkRemove(ret);
                iterator.set(o);

                ret = o;
                next = null;
                previous = null;
            }
        }

        /**
         * <p>
         * When we do a hasNext() or hasPrevious(), we perform a next() or 
         * previous() on our backing iterator. Thus, temporarily, our state 
         * is out of sync with the backing store's state. If the user 
         * subsequently calls our next() or previous(), then our state catches
         * up, so we're back in sync. But if they call something else, we're
         * in trouble, because we're out of sync. This method re-synchronizes,
         * by futzing with the backing iterator until its state matches our
         * own. (The vast majority of the time, the user will probably call
         * methods in a more sensible order, so this method will actually do
         * nothing.)
         * </p>
         * <p>
         * There are two pieces of state that need to be synchonized. One is
         * the iterator's "cursor". The cursor position always lies between the 
         * element that would be returned by a call to previous() and the 
         * element that would be returned by a call to next(). All methods 
         * except set() and remove() care only about the cursor position. 
         * Suppose in hasNext() we performed iterator.next() 3 times before 
         * we found a matching object; to resync the cursor, we simply perform
         * iterator.previous() 3 times.
         * </p>
         * <p>
         * The second piece of state is the last returned object. set() and
         * remove() operate on the last returned object; they don't care about
         * the cursor position. To resync this, we need to make sure that the
         * last move of our backing iterator was in the same direction as our
         * last move.
         * </p>
         * <p>
         * Example: Suppose the backing list contains { A, B, c, d, e, F, G },
         * where uppercase letter match our filter and lowercase letters do
         * not. Starting at the beginning of the list, the user calls next(),
         * next(), hasNext(), remove().  (This would be a somewhat realistic
         * example if F and G weren't there - this would remove the last item
         * in the list.) After the second call to next(), both our cursor and
         * our backing cursor are positioned after B. The call to hasNext()
         * shouldn't affect our cursor at all, but it does cause our backing
         * cursor to move to just after F (because we had to call 
         * iterator.next() 4 times to find out if there's another matching 
         * item). On the call to remove(), if we didn't resync, we'd remove 
         * F instead of B. 
         * </p>
         * <p>
         * To resync, we call iterator.previous() 4 times to sync up the 
         * cursor position.  But if we called remove() now, then c would be
         * removed instead of B. So we have get the backing cursor moving in
         * the right direction, so we call iterator.previous() and 
         * iterator.next(), and then we're back in sync.
         * </p>
         */
        private void sync() {
            // stepsNeededToSync tells us the number of steps, and the 
            // direction, that we need to resync. A positive number means we
            // need to move forward, and a negative number means we need to 
            // move backward.
            if (stepsNeededToSync != 0) {

                if (stepsNeededToSync > 0) {
                    // Need to move forward to resync the cursor.
                    for (int i = 0; i < stepsNeededToSync; i++) {
                        iterator.next();
                    }
                    // See if we need to get the backing iterator moving 
                    // backwards, so that its last-returned object is the same
                    // as ours.  (This hasNext() call should always be true,
                    // unless the item on the end of the list was just removed.)
                    if (lastMoveDirection == -1 && iterator.hasNext()) {
                        iterator.next();
                        iterator.previous();
                    }
                }
                
                else {
                    // Need to move backward to resync the cursor.
                    for (int i = 0; i < -stepsNeededToSync; i++) {
                        iterator.previous();
                    }
                    // See if we need to get the backing iterator moving
                    // forwards, so that its last-returned object is the same
                    // as ours.  (This hasPrevious() call should always be 
                    // true, unless the item at the beginning of the list was
                    // just removed.)
                    if (lastMoveDirection == 1 && iterator.hasPrevious()) {
                        iterator.previous();
                        iterator.next();
                    }
                }
                
                // Invalidate any next or previous items we found.
                next = null;
                previous = null;

                // We're now in sync.
                stepsNeededToSync = 0;
            }
        }

        // Make sure that the backing list is created, as is our iterator,
        // because a modification is about to occur.
        private void ensureIteratorBackingList() {
            checkList();
            if (list == null) {
                list = getBackingList(true);
                iterator = list.listIterator();
            }
        }

        // If the backing list has been modified out from under us, then our
        // backing iterator will throw a ConcurrentModificationException
        // for us. However, if the list has been *replaced*, we need to check
        // this ourselves. Here, we check to make sure that the list is still
        // the same one that was there when we were created.
        private void checkList() {
            if (list != getBackingList(false)) {
                throw new ConcurrentModificationException();
            }
        }
    }
}
