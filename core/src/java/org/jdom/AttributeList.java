/*--

 $Id: AttributeList.java,v 1.3 2002/02/05 10:07:43 jhunter Exp $

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

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * <code>AttributeList</code> represents valid JDOM <code>Attribute</code>
 * content.  This class is NOT PUBLIC; users should see it as a simple List
 * implementation.
 * </p>
 *
 * @author Alex Rosen
 * @author Philippe Riand
 * @author Bradley S. Huffman
 * @version $Revision: 1.3 $, $Date: 2002/02/05 10:07:43 $
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
      "@(#) $RCSfile: AttributeList.java,v $ $Revision: 1.3 $ $Date: 2002/02/05 10:07:43 $ $Name:  $";

    private static final int INITIAL_ARRAY_SIZE = 3;

    /** The backing list */
    protected ArrayList list;

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
        int duplicate = indexOfDuplicate(obj);
        if (duplicate < 0) {
            add(size(), obj);
        }
        else {
            set(duplicate, obj);
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
            add(index, (Attribute) obj);
        }
        else {
            if (obj == null) {
                throw new IllegalAddException("Cannot add null attribute");
            }
            else {
                throw new IllegalAddException("Class " +
                                              obj.getClass().getName() + 
                                              " is not an attribute");
            }
        }
        modCount++;
    }

    /**
     * <p>
     * Check and add the <code>Attribute</code> to this list at
     * the given index.
     * </p>
     *
     * @param index index where to add <code>Attribute</code>
     * @param attribute <code>Attribute</code> to add
     */
    protected void add(int index, Attribute attribute) {
        if (attribute == null) {
            throw new IllegalAddException("Cannot add null attribute");
        }

        if (attribute.getParent() != null) {
            throw new IllegalAddException(
                          "The attribute already has an existing parent \"" +
                          attribute.getParent().getQualifiedName() + "\"");
        }

        String reason = Verifier.checkNamespaceCollision(attribute, parent);
        if (reason != null) {
            throw new IllegalAddException(parent, attribute, reason);
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

        int duplicate = indexOfDuplicate(attribute);
        if (duplicate < 0) {
            list.add(index, attribute);
        }
        else {
            throw new IllegalAddException("Cannot add duplicate attribute");
        }

        attribute.setParent(parent);
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
     * Clear the current list.
     * </p>
     */
    public void clear() {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                removeParent(list.get(i));
            }
            list = null;
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
     * Increases the capacity of this <code>AttributeList</code> instance,
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
        return list.get(index);
    }

    /**
     * Return index of the <code>Attribute</code> with the
     * given name and uri.
     */
    protected int indexOf(String name, Namespace namespace) {
        String uri = namespace.getURI();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                Attribute old = (Attribute) list.get(i);
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
        if (list == null)
            throw new IndexOutOfBoundsException("Index: " + index +
                                                " Size: " + size());

        Object old = list.get(index);
        removeParent(old);
        modCount++;
        list.remove(index);
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
        return (list.remove(index) == null) ? false : true;
    }

    /** Remove the parent of a Attribute */
    private void removeParent(Object obj) {
        if (obj instanceof Attribute) {
            Attribute attribute = (Attribute) obj;
            attribute.setParent(null);
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

        int duplicate = indexOfDuplicate(obj);
        if ((duplicate >= 0) && (duplicate != index)) {
            throw new IllegalAddException("Cannot set duplicate attribute");
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
     * Return index of attribute with same name and Namespace, or
     * -1 if one doesn't exist
     */
    private int indexOfDuplicate(Object obj) {
        int duplicate = -1;
        if (obj instanceof Attribute) {
            Attribute attribute = (Attribute) obj;
            String name = attribute.getName();
            Namespace namespace = attribute.getNamespace();
            duplicate = indexOf(name, namespace);
        }
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
        if (list == null) {
            return 0;
        }
        return list.size();
    }
 
    /**
     * <p>
     * Return this list as a <code>String</code>
     * </p>
     */
    public String toString() {
        if ((list != null) && (list.size() > 0)) {
            return list.toString();
        }
        else return "[]";
    }
}
