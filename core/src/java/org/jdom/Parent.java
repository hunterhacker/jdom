/*--

 $Id: Parent.java,v 1.2 2003/05/21 09:17:45 jhunter Exp $

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

import java.io.Serializable;
import java.util.*;
import org.jdom.filter.Filter;

/**
 * Superclass for JDOM objects which are allowed to contain
 * {@link Child} content.
 *
 * @author Bradley S. Huffman
 * @author Jason Hunter
 * @version $Revision: 1.2 $, $Date: 2003/05/21 09:17:45 $
 * @see org.jdom.Child
 * @see org.jdom.Document
 * @see org.jdom.Element
 */
public interface Parent extends Cloneable, Serializable {

    /**
     * Returns the number of child in this parent's current content list.
     *
     * @return number of children.
     */
    int getChildCount();

    /**
     * Returns the index of the supplied child in the current content list,
     * or -1 if not a child of this parent.
     *
     * @param child child to search for.
     * @return index of child, or -1 if not found.
     */
    int childIndex(Child child);

//    /**
//     * Starting at the given index (inclusive), returns the index of
//     * the first child matching the supplied filter, or -1
//     * if none is found.
//     *
//     * @return index of child, or -1 if none found.
//     */
//    int childIndex(int index, Filter filter);

    /**
     * Appends the child to the end of the current content list.
     *
     * @param child - child to append to end of current content list
     */
    Parent addContent(Child child);

    /**
     * Appends all children in a collection to the end of
     * the current content list.
     * <p>
     * In event of an exception the original content will be unchanged and
     * the objects in the supplied collection will be unaltered.
     *
     * @param collection - collection to append to end of current content list
     */
    Parent addContent(Collection collection);

    /**
     * Inserts the child into the current content list at the given index.
     *
     * @param child - child to insert into the current content list
     * @throws IndexOutOfBoundsException if index is negative or greater
     *         than the current number of children.
     */
    Parent addContent(int index, Child child);

    /**
     * Inserts all children in a collection into the current content list
     * at the given index.
     * <p>
     * In event of an exception the original content will be unchanged and
     * the objects in the supplied collection will be unaltered.
     *
     * @param collection - collection to insert into the current content list
     * @throws IndexOutOfBoundsException if index is negative or greater
     *         than the current number of children.
     */
    Parent addContent(int index, Collection collection);

    /**
     * Returns a list containing detached clones of this parent's content list.
     *
     * @return list of the cloned content.
     */
    List cloneContent();

    /**
     * Returns the child at the given index.
     *
     * @param index index of desired child
     * @return child at the given index
     * @throws IndexOutOfBoundsException if index is negative or greater
     *         than the current number of children.
     * @throws IllegalStateException if parent is a <code>Document</code>
     *         and the root element has not been set.
     */
    Child getContent(int index);

    /**
     * Returns the full content of this parent as a {@link java.util.List}
     * which contains objects of type {@link Child}. The returned list is
     * <b>"live"</b> and in document order. Any modifications
     * to it affect the element's actual contents.
     *
     * <p>
     * Sequential traversal through the List is best done with an Iterator
     * since the underlying implement of <code>List.size()</code> may
     * require walking the entire list.
     * </p>
     *
     * @return a <code>List</code> containing the mixed content of the
     *         parent.
     * @throws IllegalStateException if parent is a <code>Document</code>
     *         and the root element has not been set.
     */
    List getContent();

    /**
     * Returns as a {@link java.util.List} the content of
     * this parent matching the supplied filter. The returned list is
     * <b>"live"</b> and in document order. Any modifications to it affect
     * the element's actual contents.
     * <p>
     * Sequential traversal through the List is best done with an Iterator
     * since the underlying implement of List.size() probably requires
     * walking the entire list.
     * </p>
     *
     * @return a <code>List</code> containing the matching content of the
     *         parent.
     * @throws IllegalStateException if parent is a <code>Document</code>
     *         and the root element has not been set.
     */
    List getContent(Filter filter);

    /**
     * Removes all child content from this parent and returns the detached
     * children.
     *
     * @return list of the old child detached from this parent.
     */
    List removeContent();

    /**
     * Removes all child content from this parent matching the supplied filter
     * and returns the detached children.
     *
     * @return list of the old child detached from this parent.
     */
    List removeContent(Filter filter);

    /**
     * Removes a single child node from the current content list.
     *
     * @return whether the removal occurred
     */
    boolean removeContent(Child child);

    /**
     * Removes and returns the child at the given
     * index, or return null if there's no such child.
     *
     * @param index index of child to remove
     * @return detached child at given index
     */
    Child removeContent(int index);

    /**
     * Set this parent's content to the supplied child.
     * <p>
     * If the supplied child is legal content for this parent and before
     * it is added, all content in the current child list will
     * be cleared and all current children will have there parent set to null.
     * </p>
     * <p>
     * This has the effect that any active list (previously obtained with
     * a call to one of the <code>getContent</code> methods will also change
     * to reflect the new content.  In addition, all content in the supplied
     * collection will have their parentage set to this parent.  If the user
     * wants to continue working with a <b>"live"</b> list of this parent's
     * child, then a call to setContent should be followed by a call to one
     * of the <code>getContent</code> methods to obtain a <b>"live"</b>
     * version of the children.
     * </p>
     * <p>
     * Passing a null child clears the existing content.
     * </p>
     * <p>
     * In event of an exception the original content will be unchanged and
     * the supplied child will be unaltered.
     * </p>
     *
     * @param child new content
     * @throws IllegalAddException if the supplied child is already attached
     *                             or not legal content for this parent.
     */
    Parent setContent(Child child);

    /**
     * Sets the children of this parent to the contents of the supplied
     * collection.  The supplied collection should contain only objects
     * of type {@link Child}.
     * <p>
     * When all objects in the supplied Collection are legal and before
     * the new content is added, all content in the current child list will
     * be cleared and all children will have there parent set to null.
     * </p>
     * <p>
     * This has the effect that any active list (previously obtained with
     * a call to one of the <code>getContent</code> methods will also change
     * to reflect the new content.  In addition, all content in the supplied
     * collection will have their parentage set to this parent, but the
     * collection itself is not <b>"live"</b> and further additions and removals
     * will have no effect on this parent's content. If the user wants to
     * continue working with a <b>"live"</b> list of this parent's child, then a
     * call to setContent should be followed by a call to one of the
     * <code>getContent</code> methods to obtain a <b>"live"</b> version of
     * the children.
     * </p>
     * <p>
     * Passing a null or empty Collection clears the existing content.
     * </p>
     * <p>
     * In event of an exception the original content will be unchanged and
     * the content in the supplied collection will be unaltered.
     * </p>
     *
     * @param collection collection of content to set
     * @throws IllegalAddException if the collection contains objects of
     *         illegal types.
     */
    Parent setContent(Collection collection);

    /**
     * Replace the current child the given index with the supplied child.
     * <p>
     * In event of an exception the original content will be unchanged and
     * the supplied child will be unaltered.
     * </p>
     *
     * @param index - index of child to replace.
     * @param child - child to add.
     * @throws IllegalAddException if the supplied child is already attached
     *                             or not legal content for this parent.
     * @throws IndexOutOfBoundsException if index is negative or greater
     *         than the current number of children.
     */
    Parent setContent(int index, Child child);

    /**
     * Replace the child at the given index whith the supplied
     * collection.
     * <p>
     * In event of an exception the original content will be unchanged and
     * the content in the supplied collection will be unaltered.
     * </p>
     *
     * @param index - index of child to replace.
     * @param collection - collection of content to add.
     * @throws IllegalAddException if the collection contains objects of
     *         illegal types.
     * @throws IndexOutOfBoundsException if index is negative or greater
     *         than the current number of children.
     */
    Parent setContent(int index, Collection collection);

    /**
     * Obtain a deep, unattached copy of this parent and it's children.
     *
     * @return a deep copy of this parent and it's children.
     */
    Object clone();

    /**
     * Returns an iterator that walks over all descendants in document order.
     *
     * @return an iterator to walk descendants
     */
    Iterator getDescendants();

    /**
     * Returns an iterator that walks over all descendants in document order
     * applying the Filter to return only elements that match the filter rule.
     * With filters you can match only Elements, only Comments, Elements or
     * Comments, only Elements with a given name and/or prefix, and so on.
     *
     * @return an iterator to walk descendants within a filter
     */
    Iterator getDescendants(Filter filter);
}
