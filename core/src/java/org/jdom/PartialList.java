/*--

 Copyright 2000 Brett McLaughlin & Jason Hunter. All rights reserved.

 Redistribution and use in source and binary forms, with or without modifica-
 tion, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice,
    this list of conditions, and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions, the disclaimer that follows these conditions,
    and/or other materials provided with the distribution.

 3. The names "JDOM" and "Java Document Object Model" must not be used to
    endorse or promote products derived from this software without prior
    written permission. For written permission, please contact
    license@jdom.org.

 4. Products derived from this software may not be called "JDOM", nor may
    "JDOM" appear in their name, without prior written permission from the
    JDOM Project Management (pm@jdom.org).

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 JDOM PROJECT  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLUDING, BUT
 NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 This software  consists of voluntary contributions made  by many individuals
 on  behalf of the Java Document Object Model Project and was originally
 created by Brett McLaughlin <brett@jdom.org> and
 Jason Hunter <jhunter@jdom.org>. For more  information on the JDOM
 Project, please see <http://www.jdom.org/>.

 */
package org.jdom;

import java.util.LinkedList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * <p><code>PartialList</code> defines a <code>List</code>
 *   that contains only part of a larger <code>List</code>,
 *   yet is still completely backed by that <code>List</code>.
 * </p>
 *
 * @author Brett McLaughlin
 * @author Jason Hunter
 * @version 1.0
 */
class PartialList extends LinkedList {

    /** The actual backing <code>List</code> */
    protected List backingList;

    /** The parent <code>Element</code> (if applicable) of the list */
    protected Element parent;

    /**
     * <p>
     * As a starting point, take in the <code>List</code>
     *   that is the backing behind this <code>List</code>.
     *   It also takes the <code>Element</code> that is the
     *   parent of this <code>List</code> and the children
     *   that it contains.
     * </p>
     *
     * @param backingList <code>List</code> that backs this
     */
    public PartialList(List backingList, Element parent) {
        super();
        this.backingList = backingList;
        this.parent = parent;
    }

    /**
     * <p>
     * As a starting point, take in the <code>List</code>
     *   that is the backing behind this <code>List</code>.
     * </p>
     *
     * @param backingList <code>List</code> that backs this
     */
    public PartialList(List backingList) {
        this(backingList, null);
    }

    /**
     * Removes and returns the first element from this list.
     *
     * @return the first element from this list.
     */
    public Object removeFirst() {
        Object o = super.removeFirst();
        backingList.remove(o);
        if (o instanceof Element) {
            ((Element)o).setParent(null);
        }
	if (parent != null) {
            parent.clearContentCache();
	}
        return o;
    }

    /**
     * Removes and returns the last element from this list.
     *
     * @return the last element from this list.
     */
    public Object removeLast() {
        Object o = super.removeLast();
        backingList.remove(o);
        if (o instanceof Element) {
            ((Element)o).setParent(null);
        }
	if (parent != null) {
            parent.clearContentCache();
	}
        return o;
    }

    /**
     * Inserts the given element at the beginning of this list.
     */
    public void addFirst(Object o) {
        int index = backingList.indexOf(getFirst());
        super.addFirst(o);

        if (index != -1) {
            backingList.add(index, o);
        } else {
            backingList.add(o);
        }

        if (o instanceof Element) {
            ((Element)o).setParent(parent);  // null is OK
        }
        if (parent != null) {
	    parent.clearContentCache();
        }
    }

    /**
     * Appends the given element to the end of this list.  (Identical in
     * function to the <tt>add</tt> method; included only for consistency.)
     */
    public void addLast(Object o) {
        int index = backingList.indexOf(getLast());
        super.addLast(o);

        if ((index != -1) && (index < backingList.size())) {
            backingList.add(index+1, o);
        } else {
            backingList.add(o);
        }

        if (o instanceof Element) {
            ((Element)o).setParent(parent);  // null is OK
        }
        if (parent != null) {
	    parent.clearContentCache();
        }
    }

    /**
     * Appends the specified element to the end of this list.
     *
     * @param o element to be appended to this list.
     * @return <tt>true</tt> (as per the general contract of
     * <tt>Collection.add</tt>).
     */
    public boolean add(Object o) {
        backingList.add(o);

        if (o instanceof Element) {
            ((Element)o).setParent(parent);  // null is OK
        }

        if (parent != null) {
	    parent.clearContentCache();
        }

        return super.add(o);
    }

    /**
     * Removes the first occurrence of the specified element in this list.  If
     * the list does not contain the element, it is unchanged.  More formally,
     * removes the element with the lowest index <tt>i</tt> such that
     * <tt>(o==null ? get(i)==null : o.equals(get(i)))</tt> (if such an
     * element exists).
     *
     * @param o element to be removed from this list, if present.
     * @return <tt>true</tt> if the list contained the specified element.
     */
    public boolean remove(Object o) {
        backingList.remove(o);

        if (o instanceof Element) {
            ((Element)o).setParent(null);
        }
	if (parent != null) {
	    parent.clearContentCache();
	}

        return super.remove(o);
    }

    /**
     * Appends all of the elements in the specified collection to the end of
     * this list, in the order that they are returned by the specified
     * collection's iterator.  The behavior of this operation is undefined if
     * the specified collection is modified while the operation is in
     * progress.  (This implies that the behavior of this call is undefined if
     * the specified Collection is this list, and this list is nonempty.)
     *
     * @param index index at which to insert first element
     *              from the specified collection.
     * @param c elements to be inserted into this list.
     *
     * @throws IndexOutOfBoundsException if the specified index is out of
     *         range (<tt>index &lt; 0 || index &gt; size()</tt>).
     */
    public boolean addAll(Collection c) {
        int index = backingList.indexOf(getLast());

        if (index != -1) {
            backingList.addAll(index, c);
        } else {
            backingList.addAll(c);
        }

        for (Iterator i = c.iterator(); i.hasNext(); ) {
            Object o = i.next();
            if (o instanceof Element) {
                ((Element)o).setParent(parent);  // null is OK
            }
        }
        if (parent != null) {
	    parent.clearContentCache();
        }

        return super.addAll(c);
    }

    /**
     * Inserts all of the elements in the specified collection into this
     * list, starting at the specified position.  Shifts the element
     * currently at that position (if any) and any subsequent elements to
     * the right (increases their indices).  The new elements will appear
     * in the list in the order that they are returned by the
     * specified collection's iterator.
     *
     * @param index index at which to insert first element
     *            from the specified collection.
     * @param c elements to be inserted into this list.
     * @throws IndexOutOfBoundsException if the specified index is out of
     *            range (<tt>index &lt; 0 || index &gt; size()</tt>).
     */
    public boolean addAll(int index, Collection c) {
        int insertIndex = backingList.indexOf(get(index));

        if (insertIndex != -1) {
            backingList.addAll(insertIndex, c);
        } else {
            backingList.addAll(c);
        }

        for (Iterator i = c.iterator(); i.hasNext(); ) {
            Object o = i.next();
            if (o instanceof Element) {
                ((Element)o).setParent(parent);  // null is OK
            }
        }
        if (parent != null) {
	    parent.clearContentCache();
        }

        return super.addAll(index, c);
    }

    /**
     * Removes all of the elements from this list.
     */
    public void clear() {
        for (Iterator i = iterator(); i.hasNext(); ) {
            Object o = i.next();
            backingList.remove(o);
            if (o instanceof Element) {
                ((Element)o).setParent(null);
            }
        }
	if (parent != null) {
	    parent.clearContentCache();
	}
        super.clear();
    }

    /**
     * Replaces the element at the specified position in this list with the
     * specified element.
     *
     * @param index index of element to replace.
     * @param element element to be stored at the specified position.
     * @return the element previously at the specified position.
     * @throws IndexOutOfBoundsException if the specified index is out of
     *          range (<tt>index &lt; 0 || index &gt;= size()</tt>).
     */
    public Object set(int index, Object current) {
        Object old = get(index);
        int backingIndex = backingList.indexOf(old);
        if (backingIndex != -1) {
            backingList.set(backingIndex, current);
        }

        if (old instanceof Element) {
            ((Element)old).setParent(null);
        }
        if (current instanceof Element) {
            ((Element)current).setParent(parent);  // null is OK
        }
        if (parent != null) {
	    parent.clearContentCache();
        }

        return super.set(index, current);
    }

    /**
     * Inserts the specified element at the specified position in this list.
     * Shifts the element currently at that position (if any) and any
     * subsequent elements to the right (adds one to their indices).
     *
     * @param index index at which the specified element is to be inserted.
     * @param element element to be inserted.
     *
     * @throws IndexOutOfBoundsException if the specified index is out of
     *          range (<tt>index &lt; 0 || index &gt; size()</tt>).
     */
    public void add(int index, Object current) {
        if (index == size()) {
            // Adding to "length" spot is special case meaning to our end
            addLast(current);
        }
        else {
            backingList.add(backingList.indexOf(get(index)), current);

            if (current instanceof Element) {
                ((Element)current).setParent(parent);  // null is OK
            }
            if (parent != null) {
	        parent.clearContentCache();
            }
            super.add(index, current);
        }
    }

    /**
     * Removes the element at the specified position in this list.  Shifts any
     * subsequent elements to the left (subtracts one from their indices).
     * Returns the element that was removed from the list.
     *
     * @param index the index of the element to removed.
     * @return the element previously at the specified position.
     *
     * @throws IndexOutOfBoundsException if the specified index is out of
     *           range (<tt>index &lt; 0 || index &gt;= size()</tt>).
     */
    public Object remove(int index) {
        Object o = super.remove(index);
        backingList.remove(o);
        if (o instanceof Element) {
            ((Element)o).setParent(null);
        }
	if (parent != null) {
	    parent.clearContentCache();
	}
        return o;
    }

    /**
     * <p>
     * This adds an <code>Object</code> to this list, but does not
     *   push the <code>Object</code> back to the backing list.
     * </p>
     *
     * @param o <code>Object</code> to add to the list.
     */
   protected void addPartial(Object o) {
       super.add(o);
   }

    /**
     * <p>
     * This adds all supplied <code>Objects</code> to this list, but does not
     *   push the <code>Objects</code> back to the backing list.
     * </p>
     *
     * @param c <code>Collection</code> to add to the list.
     */
   protected boolean addAllPartial(Collection c) {
       for (Iterator i = c.iterator(); i.hasNext(); ) {
           addPartial(i.next());
       }
       return true;
   }

}
