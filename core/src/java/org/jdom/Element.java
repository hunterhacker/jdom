/*--

 Copyright 2000 Brett McLaughlin & Jason Hunter. All rights reserved.

 Redistribution and use in source and binary forms, with or without modifica-
 tion, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice,
    this list of conditions, and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions, the disclaimer that follows these conditions,
    and/or other materials provided with the distribution.

 3. The names "JDOM" and "Java Document Object Model" must not be used to
    endorse or promote products derived from this software without prior
    written permission. For written permission, please contact
    license@jdom.org.

 4. Products derived from this software may not be called "JDOM", nor may
    "JDOM" appear in their name, without prior written permission from the
    JDOM Project Management (pm@jdom.org).

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 JDOM PROJECT  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLUDING, BUT
 NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 This software  consists of voluntary contributions made  by many individuals
 on  behalf of the Java Document Object Model Project and was originally
 created by Brett McLaughlin <brett@jdom.org> and
 Jason Hunter <jhunter@jdom.org>. For more  information on the JDOM
 Project, please see <http://www.jdom.org/>.

 */
package org.jdom;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p><code>Attribute</code> defines behavior for an XML
 *   attribute, modeled in Java.  Methods allow the user
 *   to obtain the value of the element's textual content,
 *   obtain its attributes, and get its children.
 * </p>
 *
 *
 * @author Brett McLaughlin
 * @author Jason Hunter
 * @version 1.0
 */
public class Element implements Serializable, Cloneable {

    /** The local name of the <code>Element</code> */
    protected String name;

    /** The <code>{@link Namespace}</code> of the <code>Element</code> */
    protected Namespace namespace;

    /** The parent of this <code>Element</code> */
    protected Element parent;

    /** Whether this <code>Element</code> is the root (Document is the parent) */
    protected boolean isRootElement;

    /** The attributes of the <code>Element</code> */
    protected List attributes;

    /** The mixed content of the <code>Element</code> */
    protected List content;

    /*
     * XXX: We need to build in a "cache" with the child elements -
     *  They are accessed so frequently we could improve out
     *  performance big time with this. (brett)
     */

    /**
     * <p>
     * Default, no-args constructor for implementations
     *   to use if needed.
     * </p>
     */
    protected Element() { }

    /**
     * <p>
     * This will create a new <code>Element</code>
     *   with the supplied (local) name, and define
     *   the <code>{@link Namespace}</code> to be used.
     * </p>
     *
     * @param name <code>String</code> name of element.
     * @namespace <code>Namespace</code> to put element in.
     */
    public Element(String name, Namespace namespace) {
        String reason;
        if ((reason = Verifier.checkElementName(name)) != null) {
            throw new IllegalNameException(name, "element", reason);
        }

        this.name = name;

        if (namespace == null) {
            namespace = Namespace.NO_NAMESPACE;
        }

        this.namespace = namespace;
        attributes = new LinkedList();
        content = new LinkedList();
        isRootElement = false;
    }

    /**
     * <p>
     *  This will create an <code>Element</code> in no
     *    <code>{@link Namespace}</code>.
     * </p>
     *
     * @param name <code>String</code> name of element.
     */
    public Element(String name) {
        this(name, Namespace.NO_NAMESPACE);
    }

    /**
     * <p>
     *  This will create a new <code>Element</code> with
     *    the supplied (local) name, and specifies the URI
     *    of the <code>{@link Namespace}</code> the <code>Element</code>
     *    should be in, resulting it being unprefixed (in the default
     *    namespace).
     * </p>
     *
     * @param name <code>String</code> name of element.
     * @param uri <code>String</code> URI for <code>Namespace</code> element
     *        should be in.
     */
    public Element(String name, String uri) {
        this(name, Namespace.getNamespace("", uri));
    }

    /**
     * <p>
     *  This will create a new <code>Element</code> with
     *    the supplied (local) name, and specifies the prefix and URI
     *    of the <code>{@link Namespace}</code> the <code>Element</code>
     *    should be in.
     * </p>
     *
     * @param name <code>String</code> name of element.
     * @param uri <code>String</code> URI for <code>Namespace</code> element
     *        should be in.
     */
    public Element(String name, String prefix, String uri) {
        this(name, Namespace.getNamespace(prefix, uri));
    }

    /**
     * <p>
     * This returns the (local) name of the
     *   <code>Element</code>, without any
     *   namespace prefix, if one exists.
     * </p>
     *
     * @return <code>String</code> - element name.
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     *  This will return this <code>Element</code>'s
     *    <code>{@link Namespace}</code>.
     * </p>
     *
     * @return <code>Namespace</code> - Namespace object for this <code>Element</code>
     */
    public Namespace getNamespace() {
        return namespace;
    }

    /**
     * <p>
     * This returns the namespace prefix
     *   of the <code>Element</code>, if
     *   one exists.  Otherwise, an empty
     *   <code>String</code> is returned.
     * </p>
     *
     * @return <code>String</code> - namespace prefix.
     */
    public String getNamespacePrefix() {
        return namespace.getPrefix();
    }

    /**
     * <p>
     * This returns the URI mapped to this <code>Element</code>'s
     *   prefix (or the default namespace if no prefix). If no
     *   mapping is found, an empty <code>String</code> is returned.
     * </p>
     *
     * @return <code>String</code> - namespace URI for this <code>Element</code>.
     */
    public String getNamespaceURI() {
        return namespace.getURI();
    }

    /**
     * <p>
     * This returns the full name of the
     *   <code>Element</code>, in the form
     *   [namespacePrefix]:[localName].  If
     *   no namespace prefix exists for the
     *   <code>Element</code>, simply the
     *   local name is returned.
     * </p>
     *
     * @return <code>String</code> - full name of element.
     */
    public String getQualifiedName() {
        if (namespace.getPrefix().equals("")) {
            return getName();
        }

        return new StringBuffer(namespace.getPrefix())
            .append(":")
            .append(name)
            .toString();
    }

    /**
     * <p>
     * This will return the parent of this <code>Element</code>.
     *   If there is no parent, then this returns <code>null</code>.
     *   Also note that on its own, this is not 100% sufficient to
     *   see if the <code>Element</code> is not in use - this should
     *   be used in tandem with <code>{@link #isRootElement}</code>
     *   to determine this.
     * </p>
     *
     * @return <code>Element</code> - parent of this <code>Element</code>.
     */
    public Element getParent() {
        return parent;
    }

    /**
     * <p>
     * This will set the parent of this <code>Element</code>.
     * </p>
     *
     * @param parent <code>Element</code> to be new parent.
     * @return <code>Element</code> - this <code>Element</code> modified.
     */
    protected Element setParent(Element parent) {
        this.parent = parent;

        return this;
    }

    /**
     * <p>
     * This returns a <code>boolean</code> value indicating
     *   whether this <code>Element</code> is a root
     *   <code>Element</code> for a JDOM <code>{@link Document}</code>.
     *   This should be used in tandem with
     *   <code>{@link #getParent}</code> to determine
     *   if an <code>Element</code> has no "attachments" to
     *   parents.
     * </p>
     *
     * @return <code>boolean</code> - whether this is a root element.
     */
    public boolean isRootElement() {
        return isRootElement;
    }

    /**
     * <p>
     * This sets if this <code>Element</code> is a root element
     *   for a JDOM <code>{@link Document}</code>.
     * </p>
     *
     * @param isRootElement <code>boolean</code> indicator
     * @return <code>Element</code> - this <code>Element</code> modified.
     */
    protected Element setIsRootElement(boolean isRootElement) {
        this.isRootElement = isRootElement;

        return this;
    }

    /**
     * <p>
     * This will return the actual textual content of this
     *   <code>Element</code>.  This will include all text within
     *   this single element, including <code>CDATA</code> sections
     *   if they exist.  If no textual value exists for the
     *   <code>Element</code>, an empty <code>String</code> is returned.
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
     * This will set the textual content of the <code>Element</code>.
     *   If this <code>Element</code> will have both textual content
     *   and nexted elements, <code>{@link #setMixedContent}</code>
     *   should be used instead,
     * </p>
     *
     * @param textContent <code>String</code> content for <code>Element</code>.
     * @return <code>Element</code> - this element modified.
     */
    public Element setContent(String textContent) {
        content.clear();
        content.add(textContent);

        return this;
    }

    /**
     * <p>
     * This will indicate whether the element has mixed content or not.
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
        boolean hasEntities = false;
        boolean hasPIs = false;

        Iterator i = content.iterator();
        while (i.hasNext()) {
            Object obj = i.next();
            if (obj instanceof String) {
                if (hasElements ||
                    hasComments ||
                    hasEntities ||
                    hasPIs) {
                    return true;
                }
                hasText = true;
            } else if (obj instanceof Element) {
                if (hasText ||
                    hasComments ||
                    hasEntities ||
                    hasPIs) {
                    return true;
                }
                hasElements = true;
            } else if (obj instanceof Comment) {
                if (hasText ||
                    hasElements ||
                    hasEntities ||
                    hasPIs) {
                    return true;
                }
                hasComments = true;
            } else if (obj instanceof Entity) {
                if (hasText ||
                    hasElements ||
                    hasComments ||
                    hasPIs) {
                    return true;
                }
                hasEntities = true;
            } else if (obj instanceof ProcessingInstruction) {
                if (hasText ||
                    hasElements ||
                    hasComments ||
                    hasEntities) {
                    return true;
                }
            }
        }

        return false;
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
     * @return <code>List</code> - the mixed content of the
     *         <code>Element</code>: contains <code>String</code>,
     *         <code>Element</code>, <code>{@link Comment}</code>,
     *         and <code>{@link Entity}</code> objects.
     */
    public List getMixedContent() {
        PartialList result = new PartialList(content, this);
        result.addAllPartial(content);
        return result;
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
     * @return <code>Element</code> - this element modified.
     */
    public Element setMixedContent(List mixedContent) {
        content.clear();
        content.addAll(mixedContent);

        return this;
    }

    /**
     * <p>
     * This will return a <code>List</code> of all the XML
     *   elements nested directly (one level deep) within
     *   this <code>Element</code>, each in <code>Element</code>
     *   form.  If the <code>Element</code> has no nested elements,
     *   an empty list will be returned.
     * </p><p>
     * This performs no recursion, so an elements nested two levels
     *   deep would have to be obtained with:
     * <pre>
     * <code>
     *   List nestedElements = currentElement.getChildren();
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
     *                               instances for this element.
     */
    public List getChildren() {
        PartialList elements = new PartialList(content, this);

        Iterator i = content.iterator();
        while (i.hasNext()) {
            Object obj = i.next();
            if (obj instanceof Element) {
                elements.addPartial(obj);
            }
        }

        return elements;
    }

    /**
     * <p>
     * This will set the children of this <code>Element</code> to the
     *   <code>Element</code>s within the supplied <code>:ost</code>.
     *   All existing children of this <code>Element</code> are replaced.
     * </p>
     *
     * @param children <code>List</code> of <code>Element</code>s to add.
     * @return <code>Element</code> - this element modified.
     */
    public Element setChildren(List children) {
        return setMixedContent(children);
    }

    /**
     * <p>
     * This will return a <code>List</code> of all the XML
     *   elements nested directly (one level deep) within
     *   this <code>Element</code> whose names match the name
     *   specified, each in <code>Element</code>
     *   form.  If the <code>Element</code> has no nested elements
     *   that match the requested name, an empty list will be returned.
     * </p><p>
     * The local name of the <code>Element</code> should be used - the
     *   supplied URI defines the <code>{@link Namespace}</code> that
     *   retrieved <code>Element</code>s should be in.
     * </p><p>
     * Please see the notes for <code>{@link #getChildren}</code>
     *   regarding how deep this search will go within element
     *   nestings. Only elements directly nested within this <code>Element</code>
     *   will be returned.
     * </p>
     *
     *
     * @param name <code>String</code> name of attribute to return.
     * @return <code>List</code> - all matching child elements.
     */
    public List getChildren(String name, String uri) {
        PartialList children = new PartialList(getChildren(), this);

        Iterator i = content.iterator();
        while (i.hasNext()) {
            Object obj = i.next();
            if (obj instanceof Element) {
                Element element = (Element)obj;
                if ((element.getNamespaceURI().equals(uri)) &&
                    (element.getName().equals(name))) {
                    children.addPartial(element);
                }
            }
        }

        return children;
    }

    /**
     * <p>
     * This will return a <code>List</code> of all the XML
     *   elements nested directly (one level deep) within
     *   this <code>Element</code> whose names match the name
     *   specified, each in <code>Element</code>
     *   form.  If the <code>Element</code> has no nested elements
     *   that match the requested name, an empty list will be returned.
     * </p><p>
     * The local name of the <code>Element</code> should be used - this
     *   assumes that the <code>Element</code> is not in
     *   a <code>{@link Namespace}</code>.
     * </p><p>
     * Please see the notes for <code>{@link #getChildren}</code>
     *   regarding how deep this search will go within element
     *   nestings. Only elements directly nested within this <code>Element</code>
     *   will be returned.
     * </p>
     *
     *
     * @param name <code>String</code> name of attribute to return.
     * @return <code>List</code> - all matching child elements.
     */
    public List getChildren(String name) {
        return getChildren(name, "");
    }

    /**
     * <p>
     * This will return a <code>List</code> of all the XML
     *   elements nested directly (one level deep) within
     *   this <code>Element</code> whose names match the name
     *   specified, each in <code>Element</code>
     *   form.  If the <code>Element</code> has no nested elements
     *   that match the requested name, an empty list will be returned.
     * </p><p>
     * The local name of the <code>Element</code> should be used - the
     *   supplied <code>{@link Namespace}</code> defines the <code>Namespace</code> that
     *   retrieved <code>Element</code>s should be in.
     * </p><p>
     * Please see the notes for <code>{@link #getChildren}</code>
     *   regarding how deep this search will go within element
     *   nestings. Only elements directly nested within this <code>Element</code>
     *   will be returned.
     * </p>
     *
     *
     * @param name <code>String</code> name of attribute to return.
     * @return <code>List</code> - all matching child elements.
     */
    public List getChildren(String name, Namespace ns) {
        return getChildren(name, ns.getURI());
    }


    /**
     * <p>
     * This will return the child <code>{@link Element}</code>s for the
     *   specified element name on this <code>Element</code>.  If
     *   multiple elements exist for the specified name, only the first is
     *   returned.
     * </p><p>
     * The local name of the <code>Element</code> should be used - the
     *   supplied URI defines the <code>{@link Namespace}</code> that
     *   retrieved <code>Element</code>s should be in.
     * </p><p>
     * Please see the notes for <code>{@link #getChildren}</code>
     *   regarding how deep this search will go within element
     *   nestings. Only elements directly nested within this <code>Element</code>
     *   will be returned.
     * </p>
     *
     * @param name <code>String</code> name of child elements to return.
     * @param uri <code>String</code> namespace URI of elements to find.
     * @return <code>Element</code> - the first matching child element.
     * @throws <code>NoSuchElementException</code> - when the requested
     *         XML element does not exist within this <code>Element</code>.
     */
    public Element getChild(String name, String uri)
        throws NoSuchElementException {

        Iterator i = content.iterator();
        while (i.hasNext()) {
            Object obj = i.next();
            if (obj instanceof Element) {
                Element element = (Element)obj;
                if ((element.getNamespaceURI().equals(uri)) &&
                    (element.getName().equals(name))) {
                    return element;
                }
            }
        }

        // If we got here, none found
        throw new NoSuchElementException(name);
    }

    /**
     * <p>
     * This will return the child <code>{@link Element}</code>s for the
     *   specified element name on this <code>Element</code>.  If
     *   multiple elements exist for the specified name, only the first is
     *   returned.
     * </p><p>
     * The local name of the <code>Element</code> should be used - the
     *   supplied <code>{@link Namespace}</code> defines the <code>Namespace</code> that
     *   retrieved <code>Element</code>s should be in.
     * </p><p>
     * Please see the notes for <code>{@link #getChildren}</code>
     *   regarding how deep this search will go within element
     *   nestings. Only elements directly nested within this <code>Element</code>
     *   will be returned.
     * </p>
     *
     * @param name <code>String</code> name of child elements to return.
     * @param uri <code>String</code> namespace URI of elements to find.
     * @return <code>Element</code> - the first matching child element.
     * @throws <code>NoSuchElementException</code> - when the requested
     *         XML element does not exist within this <code>Element</code>.
     */
    public Element getChild(String name, Namespace ns) throws NoSuchElementException {
        return getChild(name, ns.getURI());
    }

    /**
     * <p>
     * This will return the child <code>{@link Element}</code>s for the
     *   specified element name on this <code>Element</code>.  If
     *   multiple elements exist for the specified name, only the first is
     *   returned.
     * </p><p>
     * The local name of the <code>Element</code> should be used - this
     *   assumes that the <code>Element</code> is not in
     *   a <code>{@link Namespace}</code>.
     * </p><p>
     * Please see the notes for <code>{@link #getChildren}</code>
     *   regarding how deep this search will go within element
     *   nestings. Only elements directly nested within this <code>Element</code>
     *   will be returned.
     * </p>
     *
     * @param name <code>String</code> name of child elements to return.
     * @param uri <code>String</code> namespace URI of elements to find.
     * @return <code>Element</code> - the first matching child element.
     * @throws <code>NoSuchElementException</code> - when the requested
     *         XML element does not exist within this <code>Element</code>.
     */
    public Element getChild(String name) throws NoSuchElementException {
        return getChild(name, "");
    }

    /**
     * <p>
     * This will add text to the content of this
     *   <code>Element</code>.
     * </p>
     *
     * @param text <code>String</code> to add as content.
     * @return <code>Element</code> - this element modified.
     */
    public Element addChild(String text) {
        if (content.size() > 0) {
            Object ob = content.get(content.size() - 1);
            if (ob instanceof String) {
                text = (String)ob + text;
                content.remove(ob);
            }
        }
        content.add(text);

        return this;
    }

    /**
     * <p>
     * This will add an <code>Element</code> as a child of this
     *   <code>Element</code>.
     * </p>
     *
     * @param element <code>Element</code> to add as a child.
     * @return <code>Element</code> - this element modified.
     */
    public Element addChild(Element element) {
        if (element.isRootElement()) {
            throw new IllegalAddException(element, this);
        } else if (element.getParent() != null) {
            throw new IllegalAddException(element, this);
        }

        element.setParent(this);
        content.add(element);

        return this;
    }

    /**
     * <p>
     * This will add an <code>Entity</code> as a child of this
     *   <code>Element</code>.
     * </p>
     *
     * @param entity <code>Entity</code> to add as a child.
     * @return <code>Element</code> - this element modified.
     */
    public Element addChild(Entity entity) {
        content.add(entity);

        return this;
    }

    /**
     * <p>
     * This will add the supplied
     *   <code>{@link ProcessingInstruction}</code>
     *   as a child of this <code>Element</code>.
     * </p>
     *
     * @param processingInstruction <code>ProcessingInstruction</code> to add.
     * @return <code>Element</code> - this element modified.
     */
    public Element addChild(ProcessingInstruction processingInstruction) {
        content.add(processingInstruction);

        return this;
    }

    /**
     * <p>
     * This will remove the specified <code>ProcessingInstruction</code>.
     * </p>
     *
     * @param child <code>ProcessingInstruction</code> to delete.
     * @return <code>boolean</code> - whether deletion occurred.
     */
    public boolean removeChild(ProcessingInstruction processingInstruction) {
        return content.remove(processingInstruction);
    }

    /**
     * <p>
     * This will remove the specified <code>Element</code>.
     * </p>
     *
     * @param child <code>Element</code> to delete.
     * @return <code>boolean</code> - whether deletion occurred.
     */
    public boolean removeChild(Element element) {
        if (content.remove(element)) {
            element.setParent(null);
            return true;
        } else {
            return false;
        }
    }

    /**
     * <p>
     * This will remove the specified <code>Entity</code>.
     * </p>
     *
     * @param child <code>Entity</code> to delete.
     * @return <code>boolean</code> - whether deletion occurred.
     */
    public boolean removeChild(Entity entity) {
        return content.remove(entity);
    }

    /**
     * <p>
     * This will remove the <code>Element</code> with the
     *   specified name, in the <code>{@link Namespace}</code>
     *   identified by the supplied URI.
     *   If multiple <code>Element</code>s
     *   exist with that name, the first matching child is
     *   removed.
     * </p>
     *
     * @param name <code>String</code> name of child element to remove.
     * @param uri <code>String</code> namespace URI of element to remove.
     * @return <code>boolean</code> - whether deletion occurred.
     */
    public boolean removeChild(String name, String uri) {
        Iterator i = content.iterator();
        while (i.hasNext()) {
            Object obj = i.next();
            if (obj instanceof Element) {
                Element element = (Element)obj;
                if ((element.getNamespaceURI().equals(uri)) &&
                    (element.getName().equals(name))) {
                    element.setParent(null);
                    i.remove();
                    return true;
                }
            }
        }

        // If we got here, none found
        return false;
    }

    /**
     * <p>
     * This will remove the <code>Element</code> with the
     *   specified name.  If multiple <code>Element</code>s
     *   exist with that name, the first matching child is
     *   removed. It searches for <code>Element</code>s
     *   that are not in any <code>{@link Namespace}</code>.
     * </p>
     *
     * @param name <code>String</code> name of child to delete.
     * @return <code>boolean</code> - whether deletion occurred.
     */
    public boolean removeChild(String name) {
        return removeChild(name, "");
    }

    /**
     * <p>
     * This will remove the <code>Element</code> with the
     *   specified name, in the <code>{@link Namespace}</code>
     *   supplied. If multiple <code>Element</code>s
     *   exist with that name, the first matching child is
     *   removed. It searches for <code>Element</code>s
     *   that are not in any <code>{@link Namespace}</code>.
     * </p>
     *
     * @param name <code>String</code> name of child to delete.
     * @param ns <code>Namespace</code> to search within.
     * @return <code>boolean</code> - whether deletion occurred.
     */
    public boolean removeChild(String name, Namespace ns) {
        return removeChild(name, ns.getURI());
    }


    /**
     * <p>
     * This will remove the <code>Element</code> with the
     *   specified name.  If multiple <code>Element</code>s
     *   exist with that name, the first matching child is
     *   removed. It searches for <code>Element</code>s within
     *   the <code>{@link Namespace}</code> identified by the
     *   supplied URI.
     * </p>
     *
     * @param name <code>String</code> name of child elements to remove.
     * @param uri <code>String</code> namespace URI of elements to remove.
     * @return <code>boolean</code> - whether deletion occurred.
     */
    public boolean removeChildren(String name, String uri) {
        boolean deletedSome = false;

        Iterator i = content.iterator();
        while (i.hasNext()) {
            Object obj = i.next();
            if (obj instanceof Element) {
                Element element = (Element)obj;
                if ((element.getNamespaceURI().equals(uri)) &&
                    (element.getName().equals(name))) {
                    element.setParent(null);
                    i.remove();
                    deletedSome = true;
                }
            }
        }

        return deletedSome;
    }

    /**
     * <p>
     * This will remove the <code>Element</code> with the
     *   specified name.  If multiple <code>Element</code>s
     *   exist with that name, all matching children are
     *   removed. It searches for <code>Element</code>s
     *   that in the specified <code>{@link Namespace}</code>.
     * </p>
     *
     * @param name <code>String</code> name of child to delete.
     * @return <code>boolean</code> - whether deletion occurred.
     */
    public boolean removeChildren(String name) {
        return removeChildren(name, "");
    }

    /**
     * <p>
     * This will remove the <code>Element</code> with the
     *   specified name.  If multiple <code>Element</code>s
     *   exist with that name, all matching children are
     *   removed.
     * </p>
     *
     * @param name <code>String</code> name of child to delete.
     * @param ns <code>Namespace</code> to search within.
     * @return <code>boolean</code> - whether deletion occurred.
     */
    public boolean removeChildren(String name, Namespace ns) {
        return removeChildren(name, ns.getURI());
    }

    /**
     * <p>
     * This removes all children for this <code>Element</code>.
     * </p>
     *
     * @return <code>boolean</code> - whether deletion occurred.
     */
    public boolean removeChildren() {
        Iterator i = content.iterator();
        while (i.hasNext()) {
            Object obj = i.next();
            if (obj instanceof Element) {
                i.remove();
            }
        }

        return true;
    }

    /**
     * <p>
     * This will add a <code>{@link Comment}</code> as a child of this
     *   <code>Element</code>.
     * </p>
     *
     * @param comment <code>Comment</code> to add as a child.
     * @return <code>Element</code> - this element modified.
     */
    public Element addChild(Comment comment) {
        content.add(comment);

        return this;
    }

    /**
     * <p>
     * This will remove the specified <code>Comment</code>.
     * </p>
     *
     * @param comment <code>Comment</code> to delete.
     * @return <code>boolean</code> - whether deletion occurred.
     */
    public boolean removeChild(Comment comment) {
        return content.remove(comment);
    }

    /**
     * <p>
     * This returns the complete set of
     *   <code>{@link Attribute}</code>s for
     *   this <code>Element</code>, in no
     *   particular order.
     * </p>
     *
     * @return <code>List</code> - attributes for the element.
     */
    public List getAttributes() {
        PartialList atts = new PartialList(attributes, this);
        atts.addAllPartial(attributes);
        return atts;

    }

    /**
     * <p>
     * This will return the <code>{@link Attribute}</code> for the
     *   specified attribute name on this <code>Element</code>, within
     *   the <code>{@link Namespace}</code> defined by the supplied URI.
     * </p>
     *
     * @param name <code>String</code> name of child attribute to return.
     * @param uri <code>String</code> namespace URI of attribute to find.
     * @return <code>Attribute</code> - the requested attribute.
     * @throws <code>NoSuchAttributeException</code> - when the requested
     *         XML attribute does not exist for this <code>Element</code>.
     */
    public Attribute getAttribute(String name, String uri)
        throws NoSuchAttributeException {

        Iterator i = attributes.iterator();
        while (i.hasNext()) {
            Attribute att = (Attribute)i.next();
            if ((att.getNamespaceURI().equals(uri)) &&
                (att.getName().equals(name))) {
                return att;
            }
        }

        // If we got here, nothing found
        throw new NoSuchAttributeException(name);
    }

    /**
     * <p>
     * This will return the <code>{@link Attribute}</code> for the
     *   specified attribute name on this <code>Element</code>.
     *   It searches for <code>{@link Attribute}</code>s not in
     *   any <code>{@link Namespace}</code>.
     * </p>
     *
     * @param name <code>String</code> name of attribute to return.
     * @return <code>Attribute</code> - the requested attribute.
     * @throws <code>NoSuchAttributeException</code> - when the requested
     *         XML attribute does not exist for this <code>Element</code>.
     */
    public Attribute getAttribute(String name)
        throws NoSuchAttributeException {

        return getAttribute(name, "");
    }

    /**
     * <p>
     * This will return the <code>{@link Attribute}</code> for the
     *   specified attribute name on this <code>Element</code>.
     *   It searches for <code>{@link Attribute}</code>s in
     *   the supplied <code>{@link Namespace}</code>.
     * </p>
     *
     * @param name <code>String</code> name of attribute to return.
     * @param ns <code>Namespace</code> to search within.
     * @return <code>Attribute</code> - the requested attribute.
     * @throws <code>NoSuchAttributeException</code> - when the requested
     *         XML attribute does not exist for this <code>Element</code>.
     */
    public Attribute getAttribute(String name, Namespace ns)
        throws NoSuchAttributeException {

        return getAttribute(name, ns.getURI());
    }

    /**
     * <p>
     * This will set all the attributes for this <code>Element</code>.
     * </p>
     *
     * @param attributes <code>List</code> of attributes to add.
     * @return <code>Element</code> - this element modified.
     */
    public Element setAttributes(List attributes) {
        this.attributes = attributes;

        return this;
    }

    /**
     * <p>
     * This will add an <code>{@link Attribute}</code> to this
     *   <code>Element</code>.
     * </p>
     *
     * @param attribute <code>Attribute</code> to add.
     * @return <code>Element</code> - this element modified.
     */
    public Element addAttribute(Attribute attribute) {
        attributes.add(attribute);

        return this;
    }

    /**
     * <p>
     * This will add an <code>{@link Attribute}</code> to this
     *   <code>Element</code>.
     * </p>
     *
     * @param name name of the attribute to add.
     * @param value value of the attribute to add.
     * @return <code>Element</code> - this element modified.
     */
    public Element addAttribute(String name, String value) {
        Attribute attribute = new Attribute(name, value);
        attributes.add(attribute);

        return this;
    }

    /**
     * <p>
     * This will remove the <code>{@link Attribute}</code> with the given name, within
     *   the <code>{@link Namespace}</code> identified by the supplied URI.
     * </p>
     *
     * @param name <code>String</code> name of attribute to remove.
     * @param uri <code>String</code> namespace URI of attribute to remove.
     */
    public void removeAttribute(String name, String uri) {
        Iterator i = attributes.iterator();
        while (i.hasNext()) {
            Attribute att = (Attribute)i.next();
            if ((att.getNamespaceURI().equals(uri)) &&
                (att.getName().equals(name))) {
                i.remove();
                return;
            }
        }
    }

    /**
     * <p>
     * This will remove the <code>{@link Attribute}</code> with the given name.
     *   It searches only within <code>Attribute</code>s that are not
     *   in any <code>{@link Namespace}</code>.
     * </p>
     *
     * @param name <code>String</code> name of attribute to remove.
     */
    public void removeAttribute(String name) {
        removeAttribute(name, "");
    }

    /**
     * <p>
     * This will remove the <code>{@link Attribute}</code> with the given name.
     *   It searches only within <code>Attribute</code>s the supplied
     *   <code>{@link Namespace}</code>.
     * </p>
     *
     * @param name <code>String</code> name of attribute to remove.
     * @param ns <code>Namespace</code> to search within.
     */
    public void removeAttribute(String name, Namespace ns) {
        removeAttribute(name, ns.getURI());
    }

    /**
     * <p>
     *  This returns a <code>String</code> representation of the
     *    <code>Element</code>, suitable for debugging. If the XML
     *    representation of the <code>Element</code> is desired,
     *    <code>{@link #getSerializedForm}</code> should be used.
     * </p>
     *
     * @return <code>String</code> - information about the
     *         <code>Element</code>
     */
    public final String toString() {
        StringBuffer stringForm = new StringBuffer()
            .append("[Element: <")
            .append(getQualifiedName())
            .append(" />]");

        return stringForm.toString();
    }

    /**
     * <p>
     *  This will return the <code>Element</code> in XML format,
     *    usable in an XML document.
     * </p>
     *
     * @return <code>String</code> - the serialized form of the
     *         <code>Element</code>.
     */
    public final String getSerializedForm() {
        throw new UnsupportedOperationException();
    }

    /**
     * <p>
     *  This tests for equality of this <code>Element</code> to the supplied
     *    <code>Object</code>.
     * </p>
     *
     * @param ob <code>Object</code> to compare to.
     * @return <code>boolean</code> - whether the <code>Element</code> is
     *         equal to the supplied <code>Object</code>.
     */
    public final boolean equals(Object ob) {
        return super.equals(ob);
    }

    /**
     * <p>
     *  This returns the hash code for this <code>Element</code>.
     * </p>
     *
     * @return <code>int</code> - hash code.
     */
    public final int hashCode() {
        return super.hashCode();
    }

    /**
     * <p>
     *  This will return a deep clone of this <code>Element</code>.
     * </p>
     *
     * @return <code>Object</code> - clone of this <code>Element</code>.
     */
    public final Object clone() {
        Element element = new Element(name, namespace);
        element.attributes = (List)((LinkedList)attributes).clone();
        element.content = (List)((LinkedList)content).clone();

        // Remove out the parent
        element.setParent(null);

        return element;
    }

}