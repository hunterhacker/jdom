/*--

 $Id: AttributeList.java,v 1.24 2007/11/10 05:28:58 jhunter Exp $

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
    from the JDOM Project Management <request_AT_jdom_DOT_org>.

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

/**
 * <code>AttributeList</code> represents legal JDOM <code>Attribute</code>
 * content.  This class is NOT PUBLIC; users should see it as a simple List
 * implementation.
 *
 * @author Alex Rosen
 * @author Philippe Riand
 * @author Bradley S. Huffman
 * @version $Revision: 1.24 $, $Date: 2007/11/10 05:28:58 $
 * @see CDATA
 * @see Comment
 * @see Element
 * @see EntityRef
 * @see ProcessingInstruction
 * @see Text
 */
class AttributeList extends AbstractList
                    implements List, java.io.Serializable {

    private static final String CVS_ID =
      "@(#) $RCSfile: AttributeList.java,v $ $Revision: 1.24 $ $Date: 2007/11/10 05:28:58 $ $Name:  $";

    private static final int INITIAL_ARRAY_SIZE = 5;

    /** The backing list */
    private Attribute elementData[];
    private int size;

    /** The parent Element */
    private Element parent;

    /** Force an Element parent */
    private AttributeList() {}

    /**
     * Create a new instance of the AttributeList representing
     * Element content
     *
     * @param parent element whose attributes are to be held
     */
    AttributeList(Element parent) {
        this.parent = parent;
    }

    /**
     * Package internal method to support building from sources that are
     * 100% trusted.
     *
     * @param a attribute to add without any checks
     */ 
    final void uncheckedAddAttribute(Attribute a) {
        a.parent = parent;
        ensureCapacity(size + 1);
        elementData[size++] = a;
        modCount++;
    }

    /**
     * Add a attribute to the end of the list or replace a existing
     * attribute with the same name and <code>Namespace</code>.
     *
     * @param obj The object to insert into the list.
     * @return true (as per the general contract of Collection.add).
     * @throws IndexOutOfBoundsException if index < 0 || index > size()
     */
    public boolean add(Object obj) {
        if (obj instanceof Attribute) {
            Attribute attribute = (Attribute) obj;
            int duplicate = indexOfDuplicate(attribute);
            if (duplicate < 0) {
                add(size(), attribute);
            }
            else {
                set(duplicate, attribute);
            }
        }
        else if (obj == null) {
            throw new IllegalAddException("Cannot add null attribute");
        }
        else {
            throw new IllegalAddException("Class " +
                                          obj.getClass().getName() +
                                          " is not an attribute");
        }
        return true;
    }

    /**
     * Inserts the specified attribute at the specified position in this list.
     * Shifts the attribute currently at that position (if any) and any
     * subsequent attributes to the right (adds one to their indices).
     *
     * @param index The location to set the value to.
     * @param obj The object to insert into the list.
     * throws IndexOutOfBoundsException if index < 0 || index > size()
     */
    public void add(int index, Object obj) {
        if (obj instanceof Attribute) {
            Attribute attribute = (Attribute) obj;
            int duplicate = indexOfDuplicate(attribute);
            if (duplicate >= 0) {
                throw new IllegalAddException("Cannot add duplicate attribute");
            }
            add(index, attribute);
        }
        else if (obj == null) {
            throw new IllegalAddException("Cannot add null attribute");
        }
        else {
            throw new IllegalAddException("Class " +
                                          obj.getClass().getName() +
                                          " is not an attribute");
        }
        modCount++;
    }

    /**
     * Check and add the <code>Attribute</code> to this list at
     * the given index. Note: does not check for duplicate
     * attributes.
     *
     * @param index index where to add <code>Attribute</code>
     * @param attribute <code>Attribute</code> to add
     */
    void add(int index, Attribute attribute) {
        if (attribute.getParent() != null) {
            throw new IllegalAddException(
                          "The attribute already has an existing parent \"" +
                          attribute.getParent().getQualifiedName() + "\"");
        }

        String reason = Verifier.checkNamespaceCollision(attribute, parent);
        if (reason != null) {
            throw new IllegalAddException(parent, attribute, reason);
        }

        if (index<0 || index>size) {
            throw new IndexOutOfBoundsException("Index: " + index +
                                                " Size: " + size());
        }

        attribute.setParent(parent);

        ensureCapacity(size+1);
        if( index==size ) {
            elementData[size++] = attribute;
        } else {
            System.arraycopy(elementData, index, elementData, index + 1, size - index);
            elementData[index] = attribute;
            size++;
        }
        modCount++;
    }

    /**
     * Add all the objects in the specified collection.
     *
     * @param collection The collection containing all the objects to add.
     * @return <code>true</code> if the list was modified as a result of
     * the add.
     */
    public boolean addAll(Collection collection) {
        return addAll(size(), collection);
    }

    /**
     * Inserts the specified collecton at the specified position in this list.
     * Shifts the attribute currently at that position (if any) and any
     * subsequent attributes to the right (adds one to their indices).
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
                Attribute attribute = elementData[i];
                attribute.setParent(null);
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
        Attribute[] old = elementData;
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
                Attribute attribute = old[i];
                attribute.setParent(null);
            }
        }
        modCount++;
    }

    /**
     * Increases the capacity of this <code>AttributeList</code> instance,
     * if necessary, to ensure that it can hold at least the number of
     * items specified by the minimum capacity argument.
     *
     * @param minCapacity the desired minimum capacity.
     */
    private void ensureCapacity(int minCapacity) {
        if (elementData == null) {
            elementData = new Attribute[Math.max(minCapacity, INITIAL_ARRAY_SIZE)];
        }
        else {
            int oldCapacity = elementData.length;
            if (minCapacity > oldCapacity) {
                Attribute oldData[] = elementData;
                int newCapacity = (oldCapacity * 3)/2 + 1;
                if (newCapacity < minCapacity)
                    newCapacity = minCapacity;
                elementData = new Attribute[newCapacity];
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
     * Return the <code>Attribute</code> with the
     * given name and <code>Namespace</code>.
     *
     * @param name name of attribute to return
     * @param namespace <code>Namespace</code> to match
     * @return the <code>Attribute</code>, or null if one doesn't exist.
     */
    Object get(String name, Namespace namespace) {
        int index = indexOf(name, namespace);
        if (index < 0) {
            return null;
        }
        return elementData[index];
    }

    /**
     * Return index of the <code>Attribute</code> with the
     * given name and uri.
     */
    int indexOf(String name, Namespace namespace) {
        String uri = namespace.getURI();
        if (elementData != null) {
            for (int i = 0; i < size; i++) {
                Attribute old = elementData[i];
                String oldURI = old.getNamespaceURI();
                String oldName = old.getName();
                if (oldURI.equals(uri) && oldName.equals(name)) {
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

        Attribute old = elementData[index];
        old.setParent(null);
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index+1, elementData, index,numMoved);
        elementData[--size] = null; // Let gc do its work
        modCount++;
        return old;
    }

    /**
     * Remove the <code>Attribute</code> with the
     * given name and <code>Namespace</code>.
     *
     * @param namespace <code>Namespace</code> to match
     * @return the <code>true</code> if attribute was removed,
     *             <code>false</code> otherwise
     */
    boolean remove(String name, Namespace namespace) {
        int index = indexOf(name, namespace);
        if (index < 0) {
            return false;
        }
        remove(index);
        return true;
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
        if (obj instanceof Attribute) {
            Attribute attribute = (Attribute) obj;
            int duplicate = indexOfDuplicate(attribute);
            if ((duplicate >= 0) && (duplicate != index)) {
                throw new IllegalAddException("Cannot set duplicate attribute");
            }
            return set(index, attribute);
        }
        else if (obj == null) {
            throw new IllegalAddException("Cannot add null attribute");
        }
        else {
            throw new IllegalAddException("Class " +
                                          obj.getClass().getName() +
                                          " is not an attribute");
        }
    }

    /**
     * Set the object at the specified location to the supplied
     * object. Note: does not check for duplicate attributes.
     *
     * @param index The location to set the value to.
     * @param attribute The attribute to set.
     * @return The object which was replaced.
     * throws IndexOutOfBoundsException if index < 0 || index >= size()
     */
    Object set(int index, Attribute attribute) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index +
                                                " Size: " + size());

        if (attribute.getParent() != null) {
            throw new IllegalAddException(
                          "The attribute already has an existing parent \"" +
                          attribute.getParent().getQualifiedName() + "\"");
        }

        String reason = Verifier.checkNamespaceCollision(attribute, parent);
        if (reason != null) {
            throw new IllegalAddException(parent, attribute, reason);
        }

        Attribute old = (Attribute) elementData[index];
        old.setParent(null);

        elementData[index] = attribute;
        attribute.setParent(parent);
        return old;
    }

    /**
     * Return index of attribute with same name and Namespace, or
     * -1 if one doesn't exist
     */
    private int indexOfDuplicate(Attribute attribute) {
        int duplicate = -1;
        String name = attribute.getName();
        Namespace namespace = attribute.getNamespace();
        duplicate = indexOf(name, namespace);
        return duplicate;
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
     */
    public String toString() {
        return super.toString();
    }
}
