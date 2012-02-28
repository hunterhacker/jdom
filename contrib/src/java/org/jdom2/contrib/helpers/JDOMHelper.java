/*--

 Copyright (C) 2000-2012 Jason Hunter & Brett McLaughlin.
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

package org.jdom2.contrib.helpers;

import org.jdom2.*;
import java.util.*;

/**   <p>
 *    This class contains static helper methods. 
 *    </p>
 *    @author Alex Rosen
 *    @deprecated concept has been moved in to core:
 *    	{@link Element#sortContent(Comparator)}
 *    @see Element#sortChildren(Comparator)
 *    @see Element#sortContent(Comparator)
 *    @see Element#sortContent(org.jdom2.filter.Filter, Comparator)
 */
@Deprecated
public class JDOMHelper {
    /**
     * <p>
     * Sorts the child elements, using the specified comparator.
     * @param parent   The parent Element, whose child Elements should be sorted.
     * @param c        The Comparator to use for ordering the child Elements.
     *                 It will only be given Element objects to compare.
     * </p>
     * <p>
     * This method overcomes two problems with the standard Collections.sort():
     * <ul>
     * <li>Collections.sort() doesn't bother to remove an item from its old
     * location before placing it in its new location, which causes JDOM to
     * complain that the item has been added twice.
     * <li>This method will sort the child Elements without moving any other
     * content, such as formatting text nodes (newlines, indents, etc.)
     * Otherwise, all the formatting whitespace would move to the beginning
     * or end of the content list.
     * (Note that this means that the elements will now be in a different
     * order with respect to any comments, which may cause a problem
     * if the comments describe the elements.)
     * </ul>
     * </p>
     */
    public static void sortElements(Element parent, Comparator<Element> c) {
        // Create a new, static list of child elements, and sort it.
        List<Element> children = new ArrayList<Element>(parent.getChildren());
        Collections.sort(children, c);
        ListIterator<Element> childrenIter = children.listIterator();
        
        // Create a new, static list of all content items.
        List<Content> content = new ArrayList<Content>(parent.getContent());
        ListIterator<Content> contentIter = content.listIterator();

        // Loop through the content items, and whenever we find an Element,
        // we'll insert the next ordered Element in its place. Because the
        // content list is not live, it won't complain about an Element being
        // added twice.
        while(contentIter.hasNext()) {
            Object obj = contentIter.next();
            if (obj instanceof Element)
                contentIter.set(childrenIter.next());
        }

        // Finally, we set the content list back into the parent Element.
        parent.setContent((List<Content>)null);
        parent.setContent(content);
    }
}