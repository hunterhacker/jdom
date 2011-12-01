/*--

 Copyright (C) 2007 Jason Hunter & Brett McLaughlin.
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

import java.io.*;
import java.util.Collections;
import java.util.List;

/**
 * Superclass for JDOM objects which can be legal child content
 * of {@link org.jdom2.Parent} nodes.
 *
 * @see org.jdom2.Comment
 * @see org.jdom2.DocType
 * @see org.jdom2.Element
 * @see org.jdom2.EntityRef
 * @see org.jdom2.Parent
 * @see org.jdom2.ProcessingInstruction
 * @see org.jdom2.Text
 *
 * @author Bradley S. Huffman
 * @author Jason Hunter
 */
public abstract class Content implements Cloneable, Serializable, NamespaceAware {

	/**
	 * An enumeration useful for identifying content types without
	 * having to do <code>instanceof</code> type conditionals. Tese can be used
	 * in switch-type statements too. 
	 */
	public static enum CType {
		/** Represents {@link Comment} content */
		Comment,
		/** Represents {@link Element} content */
		Element,
		/** Represents {@link ProcessingInstruction} content */
		ProcessingInstruction,
		/** Represents {@link EntityRef} content */
		EntityRef,
		/** Represents {@link Text} content */
		Text,
		/** Represents {@link CDATA} content */
		CDATA,
		/** Represents {@link DocType} content */
		DocType
	}
	
	/** The parent {@link Parent} of this Content */
	protected Parent parent = null;
	/** The content type enumerate value for this Content */
	protected final CType ctype;

	/** 
	 * Create a new Content instance of a particular type.
	 * @param type The {@link CType} of this Content 
	 */
	protected Content(CType type) {
		ctype = type;
	}
	
	
	/**
	 * All content has an enumerated type expressing the type of content.
	 * This makes it possible to use switch-type statements on the content.
	 * @return A CType enumerated value representing this content.
	 */
	public final CType getCType() {
		return ctype;
	}

	/**
	 * Detaches this child from its parent or does nothing if the child
	 * has no parent.
	 * <p>
	 * This method can be overridden by particular Content subclasses to return
	 * a specific type of Content (co-variant return type). All overriding
	 * subclasses <b>must</b> call <code>super.detach()</code>;
	 *
	 * @return this child detached
	 */
	public Content detach() {
		if (parent != null) {
			parent.removeContent(this);
		}
		return this;
	}

	/**
	 * Return this child's parent, or null if this child is currently
	 * not attached. The parent can be either an {@link Element}
	 * or a {@link Document}.
	 * <p>
	 * This method can be overridden by particular Content subclasses to return
	 * a specific type of Parent (co-variant return type). All overriding
	 * subclasses <b>must</b> call <code>super.getParent()</code>;
	 *
	 * @return this child's parent or null if none
	 */
	public Parent getParent() {
		return parent;
	}

	/**
	 * A convenience method that returns any parent element for this element,
	 * or null if the element is unattached or is a root element.  This was the
	 * original behavior of getParent() in JDOM Beta 9 which began returning
	 * Parent in Beta 10.  This method provides a convenient upgrade path for
	 * JDOM Beta 10 and 1.0 users.
	 *
	 * @return the containing Element or null if unattached or a root element
	 */
	final public Element getParentElement() {
		Parent pnt = getParent();
		return (Element) ((pnt instanceof Element) ? pnt : null);
	}

	/**
	 * Sets the parent of this Content. The caller is responsible for removing
	 * any pre-existing parentage.
	 * <p>
	 * This method can be overridden by particular Content subclasses to return
	 * a specific type of Content (co-variant return type). All overriding
	 * subclasses <b>must</b> call <code>super.setParent(Parent)</code>;
	 *
	 * @param  parent              new parent element
	 * @return                     the target element
	 */
	protected Content setParent(Parent parent) {
		this.parent = parent;
		return this;
	}

	/**
	 * Return this child's owning document or null if the branch containing
	 * this child is currently not attached to a document.
	 *
	 * @return this child's owning document or null if none
	 */
	public Document getDocument() {
		if (parent == null) return null;
		return parent.getDocument();
	}


	/**
	 * Returns the XPath 1.0 string value of this child.
	 *
	 * @return xpath string value of this child.
	 */
	public abstract String getValue();

	/**
	 * Returns a deep, unattached copy of this child and its descendants
	 * detached from any parent or document.
	 * <p>
	 * This method can be overridden by particular Content subclasses to return
	 * a specific type of Content (co-variant return type). All overriding
	 * subclasses <b>must</b> call <code>super.clone()</code>;
	 *
	 * @return a detached deep copy of this child and descendants
	 */
	@Override
	public Content clone() {
		try {
			Content c = (Content)super.clone();
			c.parent = null;
			return c;
		} catch (CloneNotSupportedException e) {
			//Can not happen ....
			//e.printStackTrace();
			return null;
		}
	}

	/**
	 * This tests for equality of this Content object to the supplied object.
	 * Content items are considered equal only if they are referentially equal
	 * (i&#46;e&#46; the same object).  User code may choose to compare objects
	 * based on their properties instead.
	 *
	 * @param ob <code>Object</code> to compare to.
	 * @return <code>boolean</code> - whether the <code>Content</code> is
	 *         equal to the supplied <code>Object</code>.
	 */
	@Override
	public final boolean equals(Object ob) {
		return (ob == this);
	}

	/**
	 * This returns the hash code for this <code>Content</code> item.
	 *
	 * @return <code>int</code> - hash code.
	 */
	@Override
	public final int hashCode() {
		return super.hashCode();
	}

	/**
	 * Obtain a list of all namespaces that are in scope for the current content.
	 * <br>
	 * The contents of this list will always be the sum of getNamespacesIntroduced()
	 * and getNamespacesInherited().
	 * <br>
	 * The order of the content of the list is predefined, and is dictated by the
	 * Element content. The first member will be the Element's namespace, and the
	 * subsequent members will be the remaining namespaces in Namespace-prefix order.
	 * Non-Element content will return the Namespaces in the content's
	 * Element-parent's order.
	 * <br>
	 * @return a read-only list of Namespaces.
	 * 			The order is guaranteed to be consistent
	 */
	@Override
	public List<Namespace> getNamespacesInScope() {
		// Element class will override this method to do it differently.
		Element emt = getParentElement();
		if (emt == null) {
			return Collections.singletonList(Namespace.XML_NAMESPACE);
		}
		return emt.getNamespacesInScope();
	}

	/**
	 * Obtain a list of all namespaces that are introduced to the XML tree by this
	 * node. Only Elements can introduce namespaces, so all other Content types
	 * will return an empty list.
	 * <br>
	 * The contents of this list will always be a subset (but in the same order)
	 * of getNamespacesInScope(), and will never intersect getNamspacesInherited() 
	 * @return a read-only list of Namespaces.
	 * 			The order is guaranteed to be consistent
	 */
	@Override
	public List<Namespace> getNamespacesIntroduced() {
		return Collections.emptyList();
	}

	/**
	 * Obtain a list of all namespaces that are in scope for this content, but
	 * were not introduced by this content. This method is effectively the
	 * diffence between getNamespacesInScope() and getNamespacesIntroduced().
	 * <br>
	 * The contents of this list will always be a subset (but in the same order)
	 * of getNamespacesInScope(), and will never intersect getNamspacesIntroduced() 
	 * @return a read-only list of Namespaces.
	 * 			The order is guaranteed to be consistent
	 */
	@Override
	public List<Namespace> getNamespacesInherited() {
		// Element class will override
		return getNamespacesInScope();
	}

}
