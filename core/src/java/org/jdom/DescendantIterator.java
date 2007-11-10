/*--

 $Id: DescendantIterator.java,v 1.6 2007/11/10 05:28:58 jhunter Exp $

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
import org.jdom.Content;
import org.jdom.Element;
import org.jdom.Parent;

/**
 * Traverse all a parent's descendants (all children at any level below
 * the parent).
 *
 * @author Bradley S. Huffman
 * @author Jason Hunter
 * @version $Revision: 1.6 $, $Date: 2007/11/10 05:28:58 $
 */
class DescendantIterator implements Iterator {

    private Iterator iterator;
    private Iterator nextIterator;
    private List stack = new ArrayList();

    private static final String CVS_ID =
            "@(#) $RCSfile: DescendantIterator.java,v $ $Revision: 1.6 $ $Date: 2007/11/10 05:28:58 $ $Name:  $";

    /**
     * Iterator for the descendants of the supplied object.
     *
     * @param parent document or element whose descendants will be iterated
     */
    DescendantIterator(Parent parent) {
        if (parent == null) {
            throw new IllegalArgumentException("parent parameter was null");
        }
        this.iterator = parent.getContent().iterator();
    }

    /**
     * Returns true> if the iteration has more {@link Content} descendants.
     *
     * @return true is the iterator has more descendants
     */
    public boolean hasNext() {
        if (iterator != null && iterator.hasNext()) return true;
        if (nextIterator != null && nextIterator.hasNext()) return true;
        if (stackHasAnyNext()) return true;
        return false;
    }

    /**
     * Returns the next {@link Content} descendant.
     *
     * @return the next descendant
     */
    public Object next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        // If we need to descend, go for it and record where we are.
        // We do the shuffle here on the next next() call so remove() is easy
        // to code up.
        if (nextIterator != null) {
            push(iterator);
            iterator = nextIterator;
            nextIterator = null;
        }

        // If this iterator is finished, try moving up the stack
        while (!iterator.hasNext()) {
            if (stack.size() > 0) {
                iterator = pop();
            }
            else {
              throw new NoSuchElementException("Somehow we lost our iterator");
            }
        }

        Content child = (Content) iterator.next();
        if (child instanceof Element) {
            nextIterator = ((Element)child).getContent().iterator();
        }
        return child;
    }

    /**
     * Detaches the last {@link org.jdom.Content} returned by the last call to
     * next from it's parent.  <b>Note</b>: this <b>does not</b> affect
     * iteration and all children, siblings, and any node following the
     * removed node (in document order) will be visited.
     */
    public void remove() {
        iterator.remove();
    }

    private Iterator pop() {
        int stackSize = stack.size();
        if (stackSize == 0) {
            throw new NoSuchElementException("empty stack");
        }
        return (Iterator) stack.remove(stackSize - 1);
    }

    private void push(Iterator itr) {
        stack.add(itr);
    }

    private boolean stackHasAnyNext() {
        int size = stack.size();
        for (int i = 0; i < size; i++) {
            Iterator itr = (Iterator) stack.get(i);
            if (itr.hasNext()) {
                return true;
            }
        }
        return false;
    }
}
