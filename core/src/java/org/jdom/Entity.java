/*-- 

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
import java.util.LinkedList;
import java.util.Iterator;
import java.util.List;

/**
 * <p><code>Entity</code> Defines an XML entity in Java.</p>
 *
 * @author Brett McLaughlin
 * @author Jason Hunter
 * @version 1.0
 */
public class Entity implements Serializable, Cloneable {

    /** The name of the <code>Entity</code> */
    protected String name;

    /** The mixed content of the <code>Entity</code> */
    protected List content;

    /** Parent element, or null if none */
    protected Element parent;

    /** Containing document node, or null if none */
    protected Document document;

    /**
     * <p>
     * Default, no-args constructor for implementations
     *   to use if needed.
     * </p>
     */
    protected Entity() {}

    /**
     * <p>
     * This will create a new <code>Entity</code>
     *   with the supplied name.
     * </p>
     *
     * @param name <code>String</code> name of element.
     */
    public Entity(String name) {
        this.name = name;
        content = new LinkedList();
    }

    /**
     * <p>
     * This returns the name of the
     *   <code>Entity</code>.
     * </p>
     *
     * @return <code>String</code> - entity name.
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     * This will return the parent of this <code>Entity</code>.
     *   If there is no parent, then this returns <code>null</code>.
     * </p>
     *
     * @return parent of this <code>Entity</code>
     */
    public Element getParent() {
        return parent;
    }

    /**
     * <p>
     * This will set the parent of this <code>Entity</code>.
     * </p>
     *
     * @param parent <code>Element</code> to be new parent.
     * @return this <code>Entity</code> modified.
     */
    protected Entity setParent(Element parent) {
        this.parent = parent;
        return this;
    }

    /**
     * <p>
     * This detaches the <code>Entity</code> from its parent, or does nothing 
     * if the <code>Entity</code> has no parent.
     * </p>
     *
     * @return <code>Entity</code> - this <code>Entity</code> modified.
     */
    public Entity detach() {
        Element p = getParent();
        if (p != null) {
            p.removeContent(this);
        }
        return this;
    }

    /**
     * <p>
     * This retrieves the owning <code>{@link Document}</code> for
     *   this Entity, or null if not a currently a member of a
     *   <code>{@link Document}</code>.
     * </p>
     *
     * @return <code>Document</code> owning this Entity, or null.
     */
    public Document getDocument() {
        if (document != null) {
            return document;
        }

        Element p = getParent();
        if (p != null) {
            return p.getDocument();
        }

        return null;
    }

    /**
     * <p>
     * This sets the <code>{@link Document}</code> parent of this entity.
     * </p>
     *
     * @param document <code>Document</code> parent
     * @return this <code>Entity</code> modified
     */
    protected Entity setDocument(Document document) {
        this.document = document;
        return this;
    }

    /**
     * <p>
     * This will return the actual textual content of this
     *   <code>Entity</code>.  This will include all text within
     *   this single element, including <code>CDATA</code> sections
     *   if they exist.  If no textual value exists for the
     *   <code>Entity</code>, an empty <code>String</code> is returned.
     * </p>
     *
     * @return <code>String</code> - value for this element.
     */
    public String getContent() {
        StringBuffer textContent = new StringBuffer();

        Iterator i = content.iterator();
        while (i.hasNext()) {
            Object obj = i.next();
            if (obj instanceof String) {
                textContent.append((String)obj);
            }
        }

        return textContent.toString();
    }

    /**
     * <p>
     * This will set the textual content of the <code>Entity</code>.
     *   If this <code>Entity</code> will have both textual content
     *   and nested elements, <code>{@link #setMixedContent}</code>
     *   should be used instead,
     * </p>
     *
     * @param textContent <code>String</code> content for <code>Entity</code>.
     * @return <code>Entity</code> - this element modified.
     */
    public Entity setContent(String textContent) {
        content.clear();
        content.add(textContent);

        return this;
    }

    /**
     * <p>
     * This will indicate whether the entity has mixed content or not.
     *   Mixed content is when an element contains both textual and
     *   element data within it.  When this evaluates to <code>true</code>,
     *   <code>{@link #getMixedContent}</code> should be used for getting
     *   element data.
     * </p>
     * <p>
     * <em>Note:</em>I think there's a better way to do this,
     *   maybe store a member variable, but I'm too tired
     *   to really think of what it is... (brett).
     * </p>
     *
     * @return <code>boolean</code> - indicating whether there
     *         is mixed content (both textual data and elements).
     */
    public boolean hasMixedContent() {
        boolean hasText = false;
        boolean hasElements = false;
        boolean hasComments = false;

        Iterator i = content.iterator();
        while (i.hasNext()) {
            Object obj = i.next();
            if (obj instanceof String) {
                if (hasElements ||
                    hasComments) {
                    return true;
                }
                hasText = true;
            } else if (obj instanceof Element) {
                if (hasText ||
                    hasComments) {
                    return true;
                }
                hasElements = true;
            } else if (obj instanceof Comment) {
                if (hasText ||
                    hasElements) {
                    return true;
                }
                hasComments = true;
            }
        }

        return false;
    }

    /**
     * <p>
     * This will return the content of the entity.  This should be
     *   used when the <code>{@link #hasMixedContent}</code>
     *   evaluates to <code>true</code>.  When there is no mixed
     *   content, it returns a <code>List</code> with a single
     *   <code>String</code> (when only data is present) or a
     *   <code>List</code> with only elements (when only nested
     *   elements are present).
     * </p>
     *
     * @return <code>List</code> - the mixed content of the
     *         <code>Element</code>: contains <code>String</code>,
     *         <code>Element</code>, and <code>{@link Comment}</code>,
     *         objects.
     */
    public List getMixedContent() {
        return content;
    }

    /**
     * <p>
     * This will return the content of the element.  This should be
     *   used when the <code>{@link #hasMixedContent}</code>
     *   evaluates to <code>true</code>.  When there is no mixed
     *   content, it returns a <code>List</code> with a single
     *   <code>String</code> (when only data is present) or a
     *   <code>List</code> with only elements (when only nested
     *   elements are present).
     * </p>
     *
     * @return <code>Entity</code> - this entity modified.
     */
    public Entity setMixedContent(List mixedContent) {
        this.content = mixedContent;

        return this;
    }

    /**
     * <p>
     * This will return a <code>List</code> of all the XML
     *   elements nested directly (one level deep) within
     *   this <code>Entity</code>, each in <code>Element</code>
     *   form.  If the <code>Wntity</code> has no nested elements,
     *   an empty list will be returned.
     * </p><p>
     * This performs no recursion, so an elements nested two levels
     *   deep would have to be obtained with:
     * <pre>
     * <code>
     *   List nestedElements = currentEntity.getChildren();
     *   for (int i=0; i&lt;nestedElements.size(); i++) {
     *     Element oneLevelDeep = (Element)nestedElements.get(i);
     *     List twoLevelsDeep = oneLevelDeep.getChildren();
     *     // Do something with these children
     *   }
     * </code>
     * </pre>
     * </p>
     *
     * @return <code>List</code> - list of nested
     *                               <code>Element</code>
     *                               instances for this entity.
     */
    public List getChildren() {
        List childElements = new LinkedList();

        Iterator i = content.iterator();
        while (i.hasNext()) {
            Object obj = i.next();
            if (obj instanceof Element) {
                childElements.add(obj);
            }
        }

        return childElements;
    }

    /**
     * <p>
     * This will set the children of this <code>Entity</code> to the
     *   <code>Element</code>s within the supplied <code>:ost</code>.
     *   All existing children of this <code>Entity</code> are replaced.
     * </p>
     *
     * @param children <code>List</code> of <code>Element</code>s to add.
     * @return <code>Entity</code> - this element modified.
     */
    public Entity setChildren(List children) {
        return setMixedContent(children);
    }

    /**
     * <p>
     * This will add text to the content of this
     *   <code>Entity</code>.
     * </p>
     *
     * @param text <code>String</code> to add as content.
     * @return <code>Entity</code> - this Entity modified.
     */
    public Entity addText(String text) {
        content.add(text);

        return this;
    }

    /**
     * <p>
     * This will add an <code>Element</code> as a child of this
     *   <code>Entity</code>.
     * </p>
     *
     * @param element <code>Element</code> to add as a child.
     * @return <code>Entity</code> - this Entity modified.
     */
    public Entity addChild(Element element) {
        content.add(element);

        return this;
    }

    /**
     * <p>
     * This will add a <code>String</code> as a child of this
     *   <code>Entity</code>.
     * </p>
     *
     * @param s <code>String</code> to add as a child.
     * @return <code>Entity</code> - this Entity modified.
     */
    public Entity addChild(String s) {
        content.add(s);

        return this;
    }

    /**
     * <p>
     *  This returns a <code>String</code> representation of the
     *    <code>Entity</code>, suitable for debugging. If the XML
     *    representation of the <code>Entity</code> is desired,
     *    <code>{@link #getSerializedForm}</code> should be used.
     * </p>
     *
     * @return <code>String</code> - information about the
     *         <code>Entity</code>
     */
    public String toString() {
        return new StringBuffer()
            .append("[Entity: ")
            .append(getSerializedForm())
            .append("]")
            .toString();
    }

    /**
     * <p>
     *  This will return the <code>Entity</code> in XML format,
     *    usable in an XML document.
     * </p>
     *
     * @return <code>String</code> - the serialized form of the
     *         <code>Comment</code>.
     */
    public final String getSerializedForm() {
        return new StringBuffer()
            .append("&")
            .append(name)
            .append(";")
            .toString();
    }

    /**
     * <p>
     *  This tests for equality of this <code>Entity</code> to the supplied
     *    <code>Object</code>.
     * </p>
     *
     * @param ob <code>Object</code> to compare to.
     * @return <code>boolean</code> - whether the <code>Entity</code> is
     *         equal to the supplied <code>Object</code>.
     */
    public final boolean equals(Object ob) {
        return (ob == this);
    }

    /**
     * <p>
     *  This returns the hash code for this <code>Entity</code>.
     * </p>
     *
     * @return <code>int</code> - hash code.
     */
    public final int hashCode() {
        return super.hashCode();
    }

    /**
     * <p>
     *  This will return a clone of this <code>Entity</code>.
     * </p>
     *
     * @return <code>Object</code> - clone of this <code>Entity</code>.
     */
    public Object clone() {
        Entity entity = new Entity(name);
        entity.content = (List)((LinkedList)content).clone();

        return entity;
    }

}
