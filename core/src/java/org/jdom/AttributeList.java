/*--

 $Id: AttributeList.java,v 1.10 2002/04/05 09:05:46 jhunter Exp $

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
 created by Brett McLaughlin <brett@jdom.org> and
 Jason Hunter <jhunter@jdom.org>.  For more information on the
 JDOM Project, please see <http://www.jdom.org/>.

 */

package org.jdom;

import java.util.*;

import org.jdom.filter.Filter;

/**
 * <p>
 * <code>AttributeList</code> represents legal JDOM <code>Attribute</code>
 * content.  This class is NOT PUBLIC; users should see it as a simple List
 * implementation.
 * </p>
 *
 * @author Alex Rosen
 * @author Philippe Riand
 * @author Bradley S. Huffman
 * @version $Revision: 1.10 $, $Date: 2002/04/05 09:05:46 $
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
      "@(#) $RCSfile: AttributeList.java,v $ $Revision: 1.10 $ $Date: 2002/04/05 09:05:46 $ $Name:  $";

    private static final int INITIAL_ARRAY_SIZE = 3;

    /** The backing list */
    private Attribute elementData[];
    private int size;

    /** The parent Element */
    protected Element parent;

    /** Force an Element parent */
    private AttributeList() {}

    /**
     * <p>
     * Create a new instance of the AttributeList representing
     * Element content
     * </p>
     */
    public AttributeList(Element parent) {
        this.parent = parent;
    }

    /**
     * <p>
     * Add a attribute to the end of the list or replace a existing
     * attribute with the same name and <code>Namespace</code>.
     * </p>
     *
     * @param index The location to set the value to.
     * @param obj The object to insert into the list.
     * throws IndexOutOfBoundsException if index < 0 || index > size()
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
     * <p>
     * Inserts the specified attribute at the specified position in this list.
     * Shifts the attribute currently at that position (if any) and any
     * subsequent attributes to the right (adds one to their indices).
     * </p>
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
     * <p>
     * Check and add the <code>Attribute</code> to this list at
     * the given index. Note: does not check for duplicate
     * attributes.
     * </p>
     *
     * @param index index where to add <code>Attribute</code>
     * @param attribute <code>Attribute</code> to add
     */
    protected void add(int index, Attribute attribute) {
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
     * <p>
     * Add all the objects in the specified collection.
     * </p>
     *
     * @param collection The collection containing all the objects to add.
     * @return <code>true</code> if the list was modified as a result of
     * the add.
     */
    public boolean addAll(Collection collection) {
        return addAll(size(), collection);
    }

    /**
     * <p>
     * Inserts the specified collecton at the specified position in this list.
     * Shifts the attribute currently at that position (if any) and any
     * subsequent attributes to the right (adds one to their indices).
     * </p>
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
     * Clear the current list.
     * </p>
     */
    public void clear() {
        if (elementData != null) {
            for (int i = 0; i < size; i++) {
                Attribute attribute = elementData[i];
                attribute.setParent(null);
            }
            elementData = null;
        }
        modCount++;
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
    public void clearAndSet(Collection collection) {
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
     * <p>
     * Increases the capacity of this <code>AttributeList</code> instance,
     * if necessary, to ensure that it can hold at least the number of
     * items specified by the minimum capacity argument.
     * </p>
     *
     * @param minCapacity the desired minimum capacity.
     */
    protected void ensureCapacity(int minCapacity) {
        if (elementData == null) {
            elementData = new Attribute[minCapacity];
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
     * <p>
     * Return the object at the specified offset.
     * </p>
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
     * <p>
     * Return the <code>Attribute</code> with the
     * given <code>Namespace</code>.
     * </p>
     *
     * @param namespace <code>Namespace</code> to match
     * @return the <code>Attribute</code>, or null if one doesn't exist.
     */
    protected Object get(String name, Namespace namespace) {
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
    protected int indexOf(String name, Namespace namespace) {
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
     * <p>
     * Remove the object at the specified offset.
     * </p>
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
     * <p>
     * Remove the <code>Attribute</code> with the
     * given name and <code>Namespace</code>.
     * </p>
     *
     * @param namespace <code>Namespace</code> to match
     * @return the <code>true</code> if attribute was removed,
     *             <code>false</code> otherwise
     */
    protected boolean remove(String name, Namespace namespace) {
        int index = indexOf(name, namespace);
        if (index < 0) {
            return false;
        }
        remove(index);
        return true;
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
     * <p>
     * Set the object at the specified location to the supplied
     * object. Note: does not check for duplicate attributes.
     * </p>
     *
     * @param index The location to set the value to.
     * @param obj The location to set the value to.
     * @return The object which was replaced.
     * throws IndexOutOfBoundsException if index < 0 || index >= size()
     */
    protected Object set(int index, Attribute attribute) {
        if (index<0 || index>=size)
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
     * <p>
     * Return the number of items in this list
     * </p>
     *
     * @return The number of items in this list.
     */
    public int size() {
        return size;
    }
 
    /**
     * <p>
     * Return this list as a <code>String</code>
     * </p>
     */
    public String toString() {
        return super.toString();
    }
}
