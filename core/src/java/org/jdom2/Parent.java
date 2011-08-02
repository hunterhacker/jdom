/*--

 $Id: Parent.java,v 1.13 2007/11/10 05:28:59 jhunter Exp $

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

import java.io.Serializable;
import java.util.*;
import org.jdom.filter.Filter;

/**
 * Superclass for JDOM objects which are allowed to contain
 * {@link Content} content.
 *
 * @see org.jdom.Content
 * @see org.jdom.Document
 * @see org.jdom.Element
 *
 * @author Bradley S. Huffman
 * @author Jason Hunter
 * @version $Revision: 1.13 $, $Date: 2007/11/10 05:28:59 $
 */
public interface Parent extends Cloneable, Serializable {

    /**
     * Returns the number of children in this parent's content list.
     * Children may be any {@link Content} type.
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
    int indexOf(Content child);

//    /**
//     * Starting at the given index (inclusive), returns the index of
//     * the first child matching the supplied filter, or -1
//     * if none is found.
//     *
//     * @return index of child, or -1 if none found
//     */
//    int indexOf(int index, Filter filter);

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
    Content getContent(int index);

    /**
     * Returns the full content of this parent as a {@link java.util.List}
     * which contains objects of type {@link Content}. The returned list is
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
    boolean removeContent(Content child);

    /**
     * Removes and returns the child at the given
     * index, or returns null if there's no such child.
     *
     * @param index index of child to remove
     * @return detached child at given index or null if no
     * @throws IndexOutOfBoundsException if index is negative or beyond
     *             the current number of children
     */
    Content removeContent(int index);

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
     * @param filter filter to select which descendants to see
     * @return an iterator to walk descendants that match a filter
     */
    Iterator getDescendants(Filter filter);

    /**
     * Return this parent's parent, or null if this parent is currently
     * not attached to another parent. This is the same method as in Content but
     * also added to Parent to allow more easy up-the-tree walking.
     *
     * @return this parent's parent or null if none
     */
    Parent getParent();

    /**
     * Return this parent's owning document or null if the branch containing
     * this parent is currently not attached to a document.
     *
     * @return this child's owning document or null if none
     */
    Document getDocument();

}
