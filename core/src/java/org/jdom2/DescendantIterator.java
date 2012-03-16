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

package org.jdom2;

import java.util.*;

import org.jdom2.Content;
import org.jdom2.Parent;
import org.jdom2.util.IteratorIterable;

/**
 * Traverse all a parent's descendants (all children at any level below
 * the parent - excludes the parent itself).
 *
 * @author Bradley S. Huffman
 * @author Jason Hunter
 * @author Rolf Lear
 */
class DescendantIterator implements IteratorIterable<Content> {

	private final Parent parent;
	private LinkedList<ListIterator<Content>> stack = new LinkedList<ListIterator<Content>>();
	ListIterator<Content> current = null;
	ListIterator<Content> following = null;

	/**
	 * Iterator for the descendants of the supplied object.
	 *
	 * @param parent document or element whose descendants will be iterated
	 */
	DescendantIterator(Parent parent) {
		this.parent = parent;
		// can trust that parent is not null, DescendantIterator is package-private.
		current = parent.getContent().listIterator();
	}
	
	@Override
	public DescendantIterator iterator() {
		return new DescendantIterator(parent);
	}

	/**
	 * Returns <b>true</b> if the iteration has more {@link Content} descendants.
	 *
	 * @return true is the iterator has more descendants
	 */
	@Override
	public boolean hasNext() {
		if (following != null && following.hasNext()) {
			// the last content returned has un-processed child content
			return true;
		}
		if (current.hasNext()) {
			// the last content has un-iterated siblings.
			return true;
		}
		for (Iterator<Content> it : stack) {
			if (it.hasNext()) {
				// an ancestor has un-iterated siblings.
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the next {@link Content} descendant.
	 *
	 * @return the next descendant
	 */
	@Override
	public Content next() {
		Content ret = null;
		if (following != null && following.hasNext()) {
			// The last returned content is a parent with unprocessed content
			ret = following.next();
			stack.addFirst(current);
			current = following;
		} else if (current.hasNext()) {
			// the last content returned has un-iterated siblings.
			ret = current.next();
		} else {
			// check up the ancestry for the next unprocessed sibling...
			while (ret == null && !stack.isEmpty()) {
				// while we go we pop the stack.
				current = stack.removeFirst();
				if (current.hasNext()) {
					ret = current.next();
				}
			}
		}
		if (ret == null) {
			throw new NoSuchElementException("Iterated off the end of the " +
					"DescendantIterator");
		}
		if (ret instanceof Parent) {
			following = ((Parent)ret).getContent().listIterator();
		} else {
			following = null;
		}
		return ret;
	}

	/**
	 * Detaches the last {@link org.jdom2.Content} returned by the last call to
	 * next from it's parent.  <b>Note</b>: this <b>does not</b> affect
	 * iteration and all children, siblings, and any node following the
	 * removed node (in document order) will be visited.
	 */
	@Override
	public void remove() {
		current.remove();
		following = null;
	}

}
