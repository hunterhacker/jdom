/*--

 $Id: Parent.java,v 1.5 2003/06/18 02:59:44 jhunter Exp $

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
 * @see org.jdom.Child
 * @see org.jdom.Document
 * @see org.jdom.Element
 *
 * @author Bradley S. Huffman
 * @author Jason Hunter
 * @version $Revision: 1.5 $, $Date: 2003/06/18 02:59:44 $
 */
public interface Parent extends Cloneable, Serializable {

    /**
     * Returns the number of children in this parent's content list.
     * Children may be any {@link Child} type.
     *
     * @return number of children
     */
    int getContentSize();

    /**
     * Returns the index of the supplied child in the content list,
     * or -1 if not a child of this parent.
     *
     * @param child  child to search for
     * @return       index of child, or -1 if not found
     */
    int indexOf(Child child);

//    /**
//     * Starting at the given index (inclusive), returns the index of
//     * the first child matching the supplied filter, or -1
//     * if none is found.
//     *
//     * @return index of child, or -1 if none found
//     */
//    int indexOf(int index, Filter filter);

    /**
     * Appends the child to the end of the content list.
     *
     * @param child   child to append to end of content list
     * @return        the parent on which the method was called
     */
    Parent addContent(Child child);

    /**
     * Appends all children in the given collection to the end of
     * the content list.  In event of an exception during add the
     * original content will be unchanged and the objects in the supplied
     * collection will be unaltered.
     *
     * @param collection collection to append
     * @return           the parent on which the method was called
     */
    Parent addContent(Collection collection);

    /**
     * Inserts the child into the content list at the given index.
     *
     * @param child      child to insert
     * @return           the parent on which the method was called
     * @throws IndexOutOfBoundsException if index is negative or beyond
     *         the current number of children
     */
    Parent addContent(int index, Child child);

    /**
     * Inserts the content in a collection into the content list
     * at the given index.  In event of an exception the original content
     * will be unchanged and the objects in the supplied collection will be
     * unaltered.
     *
     * @param collection  collection to insert
     * @return            the parent on which the method was called
     * @throws IndexOutOfBoundsException if index is negative or beyond
     *         the current number of children
     */
    Parent addContent(int index, Collection collection);

    /**
     * Returns a list containing detached clones of this parent's content list.
     *
     * @return list of cloned child content
     */
    List cloneContent();

    /**
     * Returns the child at the given index.
     *
     * @param index location of desired child
     * @return child at the given index
     * @throws IndexOutOfBoundsException if index is negative or beyond
     *         the current number of children
     * @throws IllegalStateException if parent is a Document
     *         and the root element is not set
     */
    Child getContent(int index);

    /**
     * Returns the full content of this parent as a {@link java.util.List}
     * which contains objects of type {@link Child}. The returned list is
     * <b>"live"</b> and in document order. Any modifications
     * to it affect the element's actual contents. Modifications are checked
     * for conformance to XML 1.0 rules.
     * <p>
     * Sequential traversal through the List is best done with an Iterator
     * since the underlying implement of {@link java.util.List#size} may
     * require walking the entire list and indexed lookups may require
     * starting at the beginning each time.
     *
     * @return a list of the content of the parent
     * @throws IllegalStateException if parent is a Document
     *         and the root element is not set
     */
    List getContent();

    /**
     * Returns as a {@link java.util.List} the content of
     * this parent that matches the supplied filter. The returned list is
     * <b>"live"</b> and in document order. Any modifications to it affect
     * the element's actual contents. Modifications are checked for
     * conformance to XML 1.0 rules.
     * <p>
     * Sequential traversal through the List is best done with an Iterator
     * since the underlying implement of {@link java.util.List#size} may
     * require walking the entire list and indexed lookups may require
     * starting at the beginning each time.
     *
     * @param  filter filter to apply
     * @return a list of the content of the parent matching the filter
     * @throws IllegalStateException if parent is a Document
     *         and the root element is not set
     */
    List getContent(Filter filter);

    /**
     * Removes all content from this parent and returns the detached
     * children.
     *
     * @return list of the old content detached from this parent
     */
    List removeContent();

    /**
     * Removes from this parent all child content matching the given filter
     * and returns a list of the detached children.
     *
     * @param  filter filter to apply
     * @return list of the detached children matching the filter
     */
    List removeContent(Filter filter);

    /**
     * Removes a single child node from the content list.
     *
     * @param  child  child to remove
     * @return whether the removal occurred
     */
    boolean removeContent(Child child);

    /**
     * Removes and returns the child at the given
     * index, or returns null if there's no such child.
     *
     * @param index index of child to remove
     * @return detached child at given index or null if no
     * @throws IndexOutOfBoundsException if index is negative or beyond
     *             the current number of children
     */
    Child removeContent(int index);

    /**
     * Set this parent's content to the supplied child.
     * <p>
     * If the supplied child is legal content for this parent and before
     * it is added, all content in the current content list will
     * be cleared and all current children will have their parentage set to
     * null.
     * <p>
     * This has the effect that any active list (previously obtained with
     * a call to one of the {@link #getContent} methods will also change
     * to reflect the new content.  In addition, all content in the supplied
     * collection will have their parentage set to this parent.  If the user
     * wants to continue working with a <b>"live"</b> list of this parent's
     * child, then a call to setContent should be followed by a call to one
     * of the {@link #getContent} methods to obtain a <b>"live"</b>
     * version of the children.
     * <p>
     * Passing a null child clears the existing content.
     * <p>
     * In event of an exception the original content will be unchanged and
     * the supplied child will be unaltered.
     *
     * @param child new content to replace existing content
     * @return           the parent on which the method was called
     * @throws IllegalAddException if the supplied child is already attached
     *                             or not legal content for this parent
     */
    Parent setContent(Child child);

    /**
     * Sets this parent's content to the supplied content list.  The supplied
     * collection must contain only objects of type {@link Child}.
     * <p>
     * If the supplied content is legal content for this parent and before
     * it is added, all content in the current content list will
     * be cleared and all current children will have their parentage set to
     * null.
     * <p>
     * This has the effect that any active list (previously obtained with
     * a call to one of the {@link #getContent} methods will also change
     * to reflect the new content.  In addition, all content in the supplied
     * collection will have their parentage set to this parent.  If the user
     * wants to continue working with a <b>"live"</b> list of this parent's
     * child, then a call to setContent should be followed by a call to one
     * of the {@link #getContent} methods to obtain a <b>"live"</b>
     * version of the children.
     * <p>
     * Passing a null or empty Collection clears the existing content.
     * <p>
     * In event of an exception the original content will be unchanged and
     * the content in the supplied collection will be unaltered.
     *
     * @param collection new collection of content to replace existing content
     * @return           the parent on which the method was called
     * @throws IllegalAddException if any object in the supplied content is
     *            already attached or not legal content for this parent
     */
    Parent setContent(Collection collection);

    /**
     * Replaces the current child the given index with the supplied child.
     * <p>
     * In event of an exception the original content will be unchanged and
     * the supplied child will be unaltered.
     *
     * @param index index of child to replace
     * @param child new content to replace the existing content
     * @return           the parent on which the method was called
     * @throws IllegalAddException if the supplied child is already attached
     *                             or not legal content for this parent
     * @throws IndexOutOfBoundsException if index is negative or beyond
     *         the current number of children
     */
    Parent setContent(int index, Child child);

    /**
     * Replaces the child at the given index whith the supplied
     * collection.
     * <p>
     * In event of an exception the original content will be unchanged and
     * the content in the supplied collection will be unaltered.
     *
     * @param index index of child to replace
     * @param collection collection of content to add
     * @return           the parent on which the method was called
     * @throws IllegalAddException if any object in the supplied content is
     *            already attached or not legal content for this parent
     * @throws IndexOutOfBoundsException if index is negative or beyond
     *         the current number of children
     */
    Parent setContent(int index, Collection collection);

    /**
     * Obtain a deep, unattached copy of this parent and it's children.
     *
     * @return a deep copy of this parent and it's children.
     */
    Object clone();

    /**
     * Returns an {@link java.util.Iterator} that walks over all descendants
     * in document order.
     *
     * @return an iterator to walk descendants
     */
    Iterator getDescendants();

    /**
     * Returns an {@link java.util.Iterator} that walks over all descendants
     * in document order applying the Filter to return only elements that
     * match the filter rule.  With filters you can match only Elements,
     * only Comments, Elements or Comments, only Elements with a given name
     * and/or prefix, and so on.
     *
     * @return an iterator to walk descendants that match a filter
     */
    Iterator getDescendants(Filter filter);
}
