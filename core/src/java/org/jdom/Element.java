/*--

 $Id: Element.java,v 1.148 2004/02/27 21:27:27 jhunter Exp $

 Copyright (C) 2000-2004 Jason Hunter & Brett McLaughlin.
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

import java.io.*;
import java.util.*;

import org.jdom.filter.*;

/**
 * An XML element. Methods allow the user to get and manipulate its child
 * elements and content, directly access the element's textual content,
 * manipulate its attributes, and manage namespaces.
 *
 * @version $Revision: 1.148 $, $Date: 2004/02/27 21:27:27 $
 * @author  Brett McLaughlin
 * @author  Jason Hunter
 * @author  Lucas Gonze
 * @author  Kevin Regan
 * @author  Dan Schaffer
 * @author  Yusuf Goolamabbas
 * @author  Kent C. Johnson
 * @author  Jools Enticknap
 * @author  Alex Rosen
 * @author  Bradley S. Huffman
 */
public class Element extends Content implements Parent {

    private static final String CVS_ID =
    "@(#) $RCSfile: Element.java,v $ $Revision: 1.148 $ $Date: 2004/02/27 21:27:27 $ $Name:  $";

    private static final int INITIAL_ARRAY_SIZE = 5;

    /** The local name of the element */
    protected String name;

    /** The namespace of the element */
    protected transient Namespace namespace;

    /** Additional namespace declarations to store on this element; useful
     * during output */
    protected transient List additionalNamespaces;

    // See http://lists.denveronline.net/lists/jdom-interest/2000-September/003030.html
    // for a possible memory optimization here (using a RootElement subclass)

    /**
     *  The attributes of the element.  Subclassers have to
     * track attributes using their own mechanism.
     */
    AttributeList attributes = new AttributeList(this);

    /**
     * The content of the element.  Subclassers have to
     * track content using their own mechanism.
     */
    ContentList content = new ContentList(this);

    /**
     * This protected constructor is provided in order to support an Element
     * subclass that wants full control over variable initialization. It
     * intentionally leaves all instance variables null, allowing a lightweight
     * subclass implementation. The subclass is responsible for ensuring all the
     * get and set methods on Element behave as documented.
     * <p>
     * When implementing an Element subclass which doesn't require full control
     * over variable initialization, be aware that simply calling super() (or
     * letting the compiler add the implicit super() call) will not initialize
     * the instance variables which will cause many of the methods to throw a
     * NullPointerException. Therefore, the constructor for these subclasses
     * should call one of the public constructors so variable initialization is
     * handled automatically.
     */
    protected Element() { }

    /**
     * Creates a new element with the supplied (local) name and namespace. If
     * the provided namespace is null, the element will have no namespace.
     *
     * @param  name                 local name of the element
     * @param  namespace            namespace for the element
     * @throws IllegalNameException if the given name is illegal as an element
     *                              name
     */
    public Element(String name, Namespace namespace) {
        setName(name);
        setNamespace(namespace);
    }

    /**
     * Create a new element with the supplied (local) name and no namespace.
     *
     * @param  name                 local name of the element
     * @throws IllegalNameException if the given name is illegal as an element
     *                              name.
     */
    public Element(String name) {
        this(name, (Namespace) null);
    }

    /**
     * Creates a new element with the supplied (local) name and a namespace
     * given by a URI. The element will be put into the unprefixed (default)
     * namespace.
     *
     * @param  name                 name of the element
     * @param  uri                  namespace URI for the element
     * @throws IllegalNameException if the given name is illegal as an element
     *                              name or the given URI is illegal as a
     *                              namespace URI
     */
    public Element(String name, String uri) {
        this(name, Namespace.getNamespace("", uri));
    }

    /**
     * Creates a new element with the supplied (local) name and a namespace
     * given by the supplied prefix and URI combination.
     *
     * @param  name                 local name of the element
     * @param  prefix               namespace prefix
     * @param  uri                  namespace URI for the element
     * @throws IllegalNameException if the given name is illegal as an element
     *                              name, the given prefix is illegal as a
     *                              namespace prefix, or the given URI is
     *                              illegal as a namespace URI
     */
    public Element(String name, String prefix, String uri) {
        this(name, Namespace.getNamespace(prefix, uri));
    }

    /**
     * Returns the (local) name of the element (without any namespace prefix).
     *
     * @return                     local element name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the (local) name of the element.
     *
     * @param  name                 the new (local) name of the element
     * @return                      the target element
     * @throws IllegalNameException if the given name is illegal as an Element
     *                              name
     */
    public Element setName(String name) {
        String reason = Verifier.checkElementName(name);
        if (reason != null) {
            throw new IllegalNameException(name, "element", reason);
        }
        this.name = name;
        return this;
    }

    /**
     * Returns the element's {@link Namespace}.
     *
     * @return                     the element's namespace
     */
    public Namespace getNamespace() {
        return namespace;
    }

    /**
     * Sets the element's {@link Namespace}. If the provided namespace is null,
     * the element will have no namespace.
     *
     * @param  namespace           the new namespace
     * @return                     the target element
     */
    public Element setNamespace(Namespace namespace) {
        if (namespace == null) {
            namespace = Namespace.NO_NAMESPACE;
        }

        this.namespace = namespace;
        return this;
    }

    /**
     * Returns the namespace prefix of the element or an empty string if none
     * exists.
     *
     * @return                     the namespace prefix
     */
    public String getNamespacePrefix() {
        return namespace.getPrefix();
    }

    /**
     * Returns the namespace URI mapped to this element's prefix (or the
     * in-scope default namespace URI if no prefix). If no mapping is found, an
     * empty string is returned.
     *
     * @return                     the namespace URI for this element
     */
    public String getNamespaceURI() {
        return namespace.getURI();
    }

    /**
     * Returns the {@link Namespace} corresponding to the given prefix in scope
     * for this element. This involves searching up the tree, so the results
     * depend on the current location of the element. Returns null if there is
     * no namespace in scope with the given prefix at this point in the
     * document.
     *
     * @param  prefix              namespace prefix to look up
     * @return                     the Namespace for this prefix at this
     *                             location, or null if none
     */
    public Namespace getNamespace(String prefix) {
        if (prefix == null) {
            return null;
        }

        if (prefix.equals("xml")) {
            // Namespace "xml" is always bound.
            return Namespace.XML_NAMESPACE;
        }

        // Check if the prefix is the prefix for this element
        if (prefix.equals(getNamespacePrefix())) {
            return getNamespace();
        }

        // Scan the additional namespaces
        if (additionalNamespaces != null) {
            for (int i = 0; i < additionalNamespaces.size(); i++) {
                Namespace ns = (Namespace) additionalNamespaces.get(i);
                if (prefix.equals(ns.getPrefix())) {
                    return ns;
                }
            }
        }

        // If we still don't have a match, ask the parent
        if (parent instanceof Element) {
            return ((Element)parent).getNamespace(prefix);
        }

        return null;
    }

    /**
     * Returns the full name of the element, in the form
     * [namespacePrefix]:[localName]. If the element does not have a namespace
     * prefix, then the local name is returned.
     *
     * @return                     qualified name of the element (including
     *                             namespace prefix)
     */
    public String getQualifiedName() {
        if (namespace.getPrefix().equals("")) {
            return getName();
        }

        return new StringBuffer(namespace.getPrefix())
            .append(':')
            .append(name)
            .toString();
    }

    /**
     * Adds a namespace declarations to this element. This should <i>not</i> be
     * used to add the declaration for this element itself; that should be
     * assigned in the construction of the element. Instead, this is for adding
     * namespace declarations on the element not relating directly to itself.
     * It's used during output to for stylistic reasons move namespace
     * declarations higher in the tree than they would have to be.
     *
     * @param  additional          namespace to add
     * @throws IllegalAddException if the namespace prefix collides with another
     *                             namespace prefix on the element
     */
    public void addNamespaceDeclaration(Namespace additional) {

        // Verify the new namespace prefix doesn't collide with another
        // declared namespace, an attribute prefix, or this element's prefix
        String reason = Verifier.checkNamespaceCollision(additional, this);
        if (reason != null) {
            throw new IllegalAddException(this, additional, reason);
        }

        if (additionalNamespaces == null) {
            additionalNamespaces = new ArrayList(INITIAL_ARRAY_SIZE);
        }

        additionalNamespaces.add(additional);
    }

    /**
     * Removes an additional namespace declarations from this element. This
     * should <i>not</i> be used to remove the declaration for this element
     * itself; that should be handled in the construction of the element.
     * Instead, this is for removing namespace declarations on the element not
     * relating directly to itself. If the declaration is not present, this
     * method does nothing.
     *
     * @param additionalNamespace namespace to remove
     */
    public void removeNamespaceDeclaration(Namespace additionalNamespace) {
        if (additionalNamespaces == null) {
            return;
        }
        additionalNamespaces.remove(additionalNamespace);
    }

    /**
     * Returns a list of the additional namespace declarations on this element.
     * This includes only additional namespace, not the namespace of the element
     * itself, which can be obtained through {@link #getNamespace()}. If there
     * are no additional declarations, this returns an empty list. Note, the
     * returned list is unmodifiable.
     *
     * @return                     a List of the additional namespace
     *                             declarations
     */
    public List getAdditionalNamespaces() {
        // Not having the returned list be live allows us to avoid creating a
        // new list object when XMLOutputter calls this method on an element
        // with an empty list.
        if (additionalNamespaces == null) {
            return Collections.EMPTY_LIST;
        }
        return Collections.unmodifiableList(additionalNamespaces);
    }

    /**
     * Returns the XPath 1.0 string value of this element, which is the
     * complete, ordered content of all text node descendants of this element
     * (i.e. the text that’s left after all references are resolved and all
     * other markup is stripped out.)
     *
     * @return a concatentation of all text node descendants
     */
    public String getValue() {
        StringBuffer buffer = new StringBuffer();

        Iterator itr = getContent().iterator();
        while (itr.hasNext()) {
            Content child = (Content) itr.next();
            if (child instanceof Element || child instanceof Text) {
                buffer.append(child.getValue());
            }
        }
        return buffer.toString();
    }

    /**
     * Returns whether this element is a root element. This can be used in
     * tandem with {@link #getParent} to determine if an element has any
     * "attachments" to a parent element or document.
     *
     * @return                     whether this is a root element
     */
    public boolean isRootElement() {
        return parent instanceof Document;
    }

    public int getContentSize() {
        return content.size();
    }

    public int indexOf(Content child) {
        return content.indexOf(child);
    }

//    private int indexOf(int start, Filter filter) {
//        int size = getContentSize();
//        for (int i = start; i < size; i++) {
//            if (filter.matches(getContent(i))) {
//                return i;
//            }
//        }
//        return -1;
//    }


    /**
     * Returns the textual content directly held under this element as a string.
     * This includes all text within this single element, including whitespace
     * and CDATA sections if they exist. It's essentially the concatenation of
     * all {@link Text} and {@link CDATA} nodes returned by {@link #getContent}.
     * The call does not recurse into child elements. If no textual value exists
     * for the element, an empty string is returned.
     *
     * @return                     text content for this element, or empty
     *                             string if none
     */
    public String getText() {
        if (content.size() == 0) {
            return "";
        }

        // If we hold only a Text or CDATA, return it directly
        if (content.size() == 1) {
            Object obj = content.get(0);
            if (obj instanceof Text) {
                return ((Text) obj).getText();
            } else {
                return "";
            }
        }

        // Else build String up
        StringBuffer textContent = new StringBuffer();
        boolean hasText = false;

        for (int i = 0; i < content.size(); i++) {
            Object obj = content.get(i);
            if (obj instanceof Text) {
                textContent.append(((Text) obj).getText());
                hasText = true;
            }
        }

        if (!hasText) {
            return "";
        }
        else {
            return textContent.toString();
        }
    }

    /**
     * Returns the textual content of this element with all surrounding
     * whitespace removed. If no textual value exists for the element, or if
     * only whitespace exists, the empty string is returned.
     *
     * @return                     trimmed text content for this element, or
     *                             empty string if none
     */
    public String getTextTrim() {
        return getText().trim();
    }

    /**
     * Returns the textual content of this element with all surrounding
     * whitespace removed and internal whitespace normalized to a single space.
     * If no textual value exists for the element, or if only whitespace exists,
     * the empty string is returned.
     *
     * @return                     normalized text content for this element, or
     *                             empty string if none
     */
    public String getTextNormalize() {
        return Text.normalizeString(getText());
    }

    /**
     * Returns the textual content of the named child element, or null if
     * there's no such child. This method is a convenience because calling
     * <code>getChild().getText()</code> can throw a NullPointerException.
     *
     * @param  name                the name of the child
     * @return                     text content for the named child, or null if
     *                             no such child
     */
    public String getChildText(String name) {
        Element child = getChild(name);
        if (child == null) {
            return null;
        }
        return child.getText();
    }

    /**
     * Returns the trimmed textual content of the named child element, or null
     * if there's no such child. See <code>{@link #getTextTrim()}</code> for
     * details of text trimming.
     *
     * @param  name                the name of the child
     * @return                     trimmed text content for the named child, or
     *                             null if no such child
     */
    public String getChildTextTrim(String name) {
        Element child = getChild(name);
        if (child == null) {
            return null;
        }
        return child.getTextTrim();
    }

    /**
     * Returns the normalized textual content of the named child element, or
     * null if there's no such child. See <code>{@link
     * #getTextNormalize()}</code> for details of text normalizing.
     *
     * @param  name                the name of the child
     * @return                     normalized text content for the named child,
     *                             or null if no such child
     */
    public String getChildTextNormalize(String name) {
        Element child = getChild(name);
        if (child == null) {
            return null;
        }
        return child.getTextNormalize();
    }

    /**
     * Returns the textual content of the named child element, or null if
     * there's no such child.
     *
     * @param  name                the name of the child
     * @param  ns                  the namespace of the child
     * @return                     text content for the named child, or null if
     *                             no such child
     */
    public String getChildText(String name, Namespace ns) {
        Element child = getChild(name, ns);
        if (child == null) {
            return null;
        }
        return child.getText();
    }

    /**
     * Returns the trimmed textual content of the named child element, or null
     * if there's no such child.
     *
     * @param  name                the name of the child
     * @param  ns                  the namespace of the child
     * @return                     trimmed text content for the named child, or
     *                             null if no such child
     */
    public String getChildTextTrim(String name, Namespace ns) {
        Element child = getChild(name, ns);
        if (child == null) {
            return null;
        }
        return child.getTextTrim();
    }

    /**
     * Returns the normalized textual content of the named child element, or
     * null if there's no such child.
     *
     * @param  name                the name of the child
     * @param  ns                  the namespace of the child
     * @return                     normalized text content for the named child,
     *                             or null if no such child
     */
    public String getChildTextNormalize(String name, Namespace ns) {
        Element child = getChild(name, ns);
        if (child == null) {
            return null;
        }
        return child.getTextNormalize();
    }

    /**
     * Sets the content of the element to be the text given. All existing text
     * content and non-text context is removed. If this element should have both
     * textual content and nested elements, use <code>{@link #setContent}</code>
     * instead. Setting a null text value is equivalent to setting an empty
     * string value.
     *
     * @param  text                 new text content for the element
     * @return                      the target element
     * @throws IllegalDataException if the assigned text contains an illegal
     *                              character such as a vertical tab (as
     *                              determined by {@link
     *                              org.jdom.Verifier#checkCharacterData})
     */
    public Element setText(String text) {
        content.clear();

        if (text != null) {
            addContent(new Text(text));
        }

        return this;
    }

    /**
     * This returns the full content of the element as a List which
     * may contain objects of type <code>Text</code>, <code>Element</code>,
     * <code>Comment</code>, <code>ProcessingInstruction</code>,
     * <code>CDATA</code>, and <code>EntityRef</code>.
     * The List returned is "live" in document order and modifications
     * to it affect the element's actual contents.  Whitespace content is
     * returned in its entirety.
     *
     * <p>
     * Sequential traversal through the List is best done with an Iterator
     * since the underlying implement of List.size() may require walking the
     * entire list.
     * </p>
     *
     * @return a <code>List</code> containing the mixed content of the
     *         element: may contain <code>Text</code>,
     *         <code>{@link Element}</code>, <code>{@link Comment}</code>,
     *         <code>{@link ProcessingInstruction}</code>,
     *         <code>{@link CDATA}</code>, and
     *         <code>{@link EntityRef}</code> objects.
     */
    public List getContent() {
        return content;
    }

    /**
     * Return a filter view of this <code>Element</code>'s content.
     *
     * <p>
     * Sequential traversal through the List is best done with a Iterator
     * since the underlying implement of List.size() may require walking the
     * entire list.
     * </p>
     *
     * @param filter <code>Filter</code> to apply
     * @return <code>List</code> - filtered Element content
     */
    public List getContent(Filter filter) {
        return content.getView(filter);
    }

    /**
     * Removes all child content from this parent.
     *
     * @return list of the old children detached from this parent
     */
    public List removeContent() {
        List old = new ArrayList(content);
        content.clear();
        return old;
    }

    /**
     * Remove all child content from this parent matching the supplied filter.
     *
     * @param filter filter to select which content to remove
     * @return list of the old children detached from this parent
     */
    public List removeContent(Filter filter) {
        List old = new ArrayList();
        Iterator itr = content.getView(filter).iterator();
        while (itr.hasNext()) {
            Content child = (Content) itr.next();
            old.add(child);
            itr.remove();
        }
        return old;
    }

    /**
     * This sets the content of the element.  The supplied List should
     * contain only objects of type <code>Element</code>, <code>Text</code>,
     * <code>CDATA</code>, <code>Comment</code>,
     * <code>ProcessingInstruction</code>, and <code>EntityRef</code>.
     *
     * <p>
     * When all objects in the supplied List are legal and before the new
     * content is added, all objects in the old content will have their
     * parentage set to null (no parent) and the old content list will be
     * cleared. This has the effect that any active list (previously obtained
     * with a call to {@link #getContent} or {@link #getChildren}) will also
     * change to reflect the new content.  In addition, all objects in the
     * supplied List will have their parentage set to this element, but the
     * List itself will not be "live" and further removals and additions will
     * have no effect on this elements content. If the user wants to continue
     * working with a "live" list, then a call to setContent should be
     * followed by a call to {@link #getContent} or {@link #getChildren} to
     * obtain a "live" version of the content.
     * </p>
     *
     * <p>
     * Passing a null or empty List clears the existing content.
     * </p>
     *
     * <p>
     * In event of an exception the original content will be unchanged and
     * the objects in the supplied content will be unaltered.
     * </p>
     *
     * @param newContent <code>List</code> of content to set
     * @return this element modified
     * @throws IllegalAddException if the List contains objects of
     *         illegal types.
     */
    public Parent setContent(Collection newContent) {
        content.clearAndSet(newContent);
        return this;
    }

    /**
     * Replace the current child the given index with the supplied child.
     * <p>
     * In event of an exception the original content will be unchanged and
     * the supplied child will be unaltered.
     * </p>
     *
     * @param index - index of child to replace.
     * @param child - child to add.
     * @return object on which this method was invoked
     * @throws IllegalAddException if the supplied child is already attached
     *                             or not legal content for this parent.
     * @throws IndexOutOfBoundsException if index is negative or greater
     *         than the current number of children.
     */
    public Parent setContent(int index, Content child) {
        content.set(index, child);
        return this;
    }

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
     * @return object on which this method was invoked
     * @throws IllegalAddException if the collection contains objects of
     *         illegal types.
     * @throws IndexOutOfBoundsException if index is negative or greater
     *         than the current number of children.
     */
    public Parent setContent(int index, Collection collection) {
        content.remove(index);
        content.addAll(index, collection);
        return this;
    }

    /**
     * This adds text content to this element.  It does not replace the
     * existing content as does <code>setText()</code>.
     *
     * @param str <code>String</code> to add
     * @return this element modified
     * @throws IllegalDataException if <code>str</code> contains an
     *         illegal character such as a vertical tab (as determined
     *         by {@link org.jdom.Verifier#checkCharacterData})
     */
    public Parent addContent(String str) {
        return addContent(new Text(str));
    }

    /**
     * This appends a child to this element.
     *
     * @param child child to add
     * @return this element modified
     * @throws IllegalAddException if the <code>Text</code> object
     *         you're attempting to add already has a parent element.
     */
    public Parent addContent(Content child) {
        content.add(child);
        return this;
    }

    public Parent addContent(Collection collection) {
        content.addAll(collection);
        return this;
    }

    public Parent addContent(int index, Content child) {
        content.add(index, child);
        return this;
    }

    public Parent addContent(int index, Collection c) {
        content.addAll(index, c);
        return this;
    }

    public List cloneContent() {
        int size = getContentSize();
        List list = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            Content child = getContent(i);
            list.add(child.clone());
        }
        return list;
    }

    public Content getContent(int index) {
        return (Content) content.get(index);
    }

//    public Content getChild(Filter filter) {
//        int i = indexOf(0, filter);
//        return (i < 0) ? null : getContent(i);
//    }

    public boolean removeContent(Content child) {
        return content.remove(child);
    }

    public Content removeContent(int index) {
        return (Content) content.remove(index);
    }

    public Parent setContent(Content child) {
        content.clear();
        content.add(child);
        return this;
    }


    /**
     * Determines if this element is the ancestor of another element.
     *
     * @param element <code>Element</code> to check against
     * @return <code>true</code> if this element is the ancestor of the
     *         supplied element
     */
    public boolean isAncestor(Element element) {
        Object p = element.getParent();
        while (p instanceof Element) {
            if (p == this) {
                return true;
            }
            p = ((Element) p).getParent();
        }
        return false;
    }

    /**
     * <p>
     * This returns the complete set of attributes for this element, as a
     * <code>List</code> of <code>Attribute</code> objects in no particular
     * order, or an empty list if there are none.
     * The returned list is "live" and changes to it affect the
     * element's actual attributes.
     * </p>
     *
     * @return attributes for the element
     */
    public List getAttributes() {
        return attributes;
    }

    /**
     * <p>
     * This returns the attribute for this element with the given name
     * and within no namespace, or null if no such attribute exists.
     * </p>
     *
     * @param name name of the attribute to return
     * @return attribute for the element
     */
    public Attribute getAttribute(String name) {
        return getAttribute(name, Namespace.NO_NAMESPACE);
    }

    /**
     * <p>
     * This returns the attribute for this element with the given name
     * and within the given Namespace, or null if no such attribute exists.
     * </p>
     *
     * @param name name of the attribute to return
     * @param ns <code>Namespace</code> to search within
     * @return attribute for the element
     */
    public Attribute getAttribute(String name, Namespace ns) {
        return (Attribute) attributes.get(name, ns);
    }

    /**
     * <p>
     * This returns the attribute value for the attribute with the given name
     * and within no namespace, null if there is no such attribute, and the
     * empty string if the attribute value is empty.
     * </p>
     *
     * @param name name of the attribute whose value to be returned
     * @return the named attribute's value, or null if no such attribute
     */
    public String getAttributeValue(String name) {
        return getAttributeValue(name, Namespace.NO_NAMESPACE);
    }

    /**
     * <p>
     * This returns the attribute value for the attribute with the given name
     * and within no namespace, or the passed-in default if there is no
     * such attribute.
     * </p>
     *
     * @param name name of the attribute whose value to be returned
     * @param def a default value to return if the attribute does not exist
     * @return the named attribute's value, or the default if no such attribute
     */
    public String getAttributeValue(String name, String def) {
        return getAttributeValue(name, Namespace.NO_NAMESPACE, def);
    }

    /**
     * <p>
     * This returns the attribute value for the attribute with the given name
     * and within the given Namespace, null if there is no such attribute, and
     * the empty string if the attribute value is empty.
     * </p>
     *
     * @param name name of the attribute whose valud is to be returned
     * @param ns <code>Namespace</code> to search within
     * @return the named attribute's value, or null if no such attribute
     */
    public String getAttributeValue(String name, Namespace ns) {
        return getAttributeValue(name, ns, null);
    }

    /**
     * <p>
     * This returns the attribute value for the attribute with the given name
     * and within the given Namespace, or the passed-in default if there is no
     * such attribute.
     * </p>
     *
     * @param name name of the attribute whose valud is to be returned
     * @param ns <code>Namespace</code> to search within
     * @param def a default value to return if the attribute does not exist
     * @return the named attribute's value, or the default if no such attribute
     */
    public String getAttributeValue(String name, Namespace ns, String def) {
        Attribute attribute = (Attribute) attributes.get(name, ns);
        return (attribute == null) ? def : attribute.getValue();
    }

    /**
     * <p>
     * This sets the attributes of the element.  The supplied List should
     * contain only objects of type <code>Attribute</code>.
     * </p>
     *
     * <p>
     * When all objects in the supplied List are legal and before the new
     * attributes are added, all old attributes will have their
     * parentage set to null (no parent) and the old attribute list will be
     * cleared. This has the effect that any active attribute list (previously
     * obtained with a call to {@link #getAttributes}) will also change to
     * reflect the new attributes.  In addition, all attributes in the supplied
     * List will have their parentage set to this element, but the List itself
     * will not be "live" and further removals and additions will have no
     * effect on this elements attributes. If the user wants to continue
     * working with a "live" attribute list, then a call to setAttributes
     * should be followed by a call to {@link #getAttributes} to obtain a
     * "live" version of the attributes.
     * </p>
     *
     * <p>
     * Passing a null or empty List clears the existing attributes.
     * </p>
     *
     * <p>
     * In cases where the List contains duplicate attributes, only the last
     * one will be retained.  This has the same effect as calling
     * {@link #setAttribute(Attribute)} sequentially.
     * </p>
     *
     * <p>
     * In event of an exception the original attributes will be unchanged and
     * the attributes in the supplied attributes will be unaltered.
     * </p>
     *
     * @param newAttributes <code>List</code> of attributes to set
     * @return this element modified
     * @throws IllegalAddException if the List contains objects
     *         that are not instances of <code>Attribute</code>,
     *         or if any of the <code>Attribute</code> objects have
     *         conflicting namespace prefixes.
     */
    public Element setAttributes(List newAttributes) {
        attributes.clearAndSet(newAttributes);
        return this;
    }

    /**
     * <p>
     * This sets an attribute value for this element.  Any existing attribute
     * with the same name and namespace URI is removed.
     * </p>
     *
     * @param name name of the attribute to set
     * @param value value of the attribute to set
     * @return this element modified
     * @throws IllegalNameException if the given name is illegal as an
     *         attribute name.
     * @throws IllegalDataException if the given attribute value is
     *         illegal character data (as determined by
     *         {@link org.jdom.Verifier#checkCharacterData}).
     */
    public Element setAttribute(String name, String value) {
        return setAttribute(new Attribute(name, value));
    }

    /**
     * <p>
     * This sets an attribute value for this element.  Any existing attribute
     * with the same name and namespace URI is removed.
     * </p>
     *
     * @param name name of the attribute to set
     * @param value value of the attribute to set
     * @param ns namespace of the attribute to set
     * @return this element modified
     * @throws IllegalNameException if the given name is illegal as an
     *         attribute name, or if the namespace is an unprefixed default
     *         namespace
     * @throws IllegalDataException if the given attribute value is
     *         illegal character data (as determined by
     *         {@link org.jdom.Verifier#checkCharacterData}).
     * @throws IllegalAddException if the attribute namespace prefix
     *         collides with another namespace prefix on the element.
     */
    public Element setAttribute(String name, String value, Namespace ns) {
        return setAttribute(new Attribute(name, value, ns));
    }

    /**
     * <p>
     * This sets an attribute value for this element.  Any existing attribute
     * with the same name and namespace URI is removed.
     * </p>
     *
     * @param attribute <code>Attribute</code> to set
     * @return this element modified
     * @throws IllegalAddException if the attribute being added already has a
     *   parent or if the attribute namespace prefix collides with another
     *   namespace prefix on the element.
     */
    public Element setAttribute(Attribute attribute) {
        attributes.add(attribute);
        return this;
    }

    /**
     * <p>
     * This removes the attribute with the given name and within no
     * namespace. If no such attribute exists, this method does nothing.
     * </p>
     *
     * @param name name of attribute to remove
     * @return whether the attribute was removed
     */
    public boolean removeAttribute(String name) {
        return removeAttribute(name, Namespace.NO_NAMESPACE);
    }

    /**
     * <p>
     * This removes the attribute with the given name and within the
     * given Namespace.  If no such attribute exists, this method does
     * nothing.
     * </p>
     *
     * @param name name of attribute to remove
     * @param ns namespace URI of attribute to remove
     * @return whether the attribute was removed
     */
    public boolean removeAttribute(String name, Namespace ns) {
        return attributes.remove(name, ns);
    }

    /**
     * <p>
     * This removes the supplied Attribute should it exist.
     * </p>
     *
     * @param attribute Reference to the attribute to be removed.
     * @return whether the attribute was removed
     */
    public boolean removeAttribute(Attribute attribute) {
        return attributes.remove(attribute);
    }

    /**
     * <p>
     *  This returns a <code>String</code> representation of the
     *    <code>Element</code>, suitable for debugging. If the XML
     *    representation of the <code>Element</code> is desired,
     *    {@link org.jdom.output.XMLOutputter#outputString(Element)}
     *    should be used.
     * </p>
     *
     * @return <code>String</code> - information about the
     *         <code>Element</code>
     */
    public String toString() {
        StringBuffer stringForm = new StringBuffer(64)
            .append("[Element: <")
            .append(getQualifiedName());

        String nsuri = getNamespaceURI();
        if (!nsuri.equals("")) {
            stringForm
            .append(" [Namespace: ")
            .append(nsuri)
            .append("]");
        }
        stringForm.append("/>]");

        return stringForm.toString();
    }

    /**
     * <p>
     *  This returns a deep clone of this element.
     *  The new element is detached from its parent, and getParent()
     *  on the clone will return null.
     * </p>
     *
     * @return the clone of this element
     */
    public Object clone() {

        // Ken Rune Helland <kenh@csc.no> is our local clone() guru

        Element element = null;

        element = (Element) super.clone();

        // name and namespace are references to immutable objects
        // so super.clone() handles them ok

        // Reference to parent is copied by super.clone()
        // (Object.clone()) so we have to remove it
        // Actually, super is a Content, which has already detached in the clone().
        // element.parent = null;

        // Reference to content list and attribute lists are copyed by
        // super.clone() so we set it new lists if the original had lists
        element.content = new ContentList(element);
        element.attributes = new AttributeList(element);

        // Cloning attributes
        if (attributes != null) {
            for (int i = 0; i < attributes.size(); i++) {
                Object obj = attributes.get(i);
                Attribute attribute = (Attribute)((Attribute)obj).clone();
                element.attributes.add(attribute);
            }
        }

        // Cloning additional namespaces
        if (additionalNamespaces != null) {
            int additionalSize = additionalNamespaces.size();
            element.additionalNamespaces = new ArrayList(additionalSize);
            for (int i = 0; i < additionalSize; i++) {
                Object additional = additionalNamespaces.get(i);
                element.additionalNamespaces.add(additional);
            }
        }

        // Cloning content
        if (content != null) {
            for (int i = 0; i < content.size(); i++) {
                Object obj = content.get(i);
                if (obj instanceof Element) {
                    Element elt = (Element)((Element)obj).clone();
                    element.content.add(elt);
                }  else if (obj instanceof CDATA) {
                    CDATA cdata = (CDATA)((CDATA)obj).clone();
                    element.content.add(cdata);
                } else if (obj instanceof Text) {
                    Text text = (Text)((Text)obj).clone();
                    element.content.add(text);
                } else if (obj instanceof Comment) {
                    Comment comment = (Comment)((Comment)obj).clone();
                    element.content.add(comment);
                } else if (obj instanceof ProcessingInstruction) {
                    ProcessingInstruction pi = (ProcessingInstruction)
                        ((ProcessingInstruction)obj).clone();
                    element.content.add(pi);
                } else if (obj instanceof EntityRef) {
                    EntityRef entity = (EntityRef)((EntityRef)obj).clone();
                    element.content.add(entity);
                }
            }
        }

        // Handle additional namespaces
        if (additionalNamespaces != null) {
            // Avoid additionalNamespaces.clone() because List isn't Cloneable
            element.additionalNamespaces = new ArrayList();
            element.additionalNamespaces.addAll(additionalNamespaces);
        }

        return element;
    }

    // Support a custom Namespace serialization so no two namespace
    // object instances may exist for the same prefix/uri pair
    private void writeObject(ObjectOutputStream out) throws IOException {

        out.defaultWriteObject();

        // We use writeObject() and not writeUTF() to minimize space
        // This allows for writing pointers to already written strings
        out.writeObject(namespace.getPrefix());
        out.writeObject(namespace.getURI());

        if (additionalNamespaces == null) {
            out.write(0);
        }
        else {
            int size = additionalNamespaces.size();
            out.write(size);
            for (int i = 0; i < size; i++) {
                Namespace additional = (Namespace) additionalNamespaces.get(i);
                out.writeObject(additional.getPrefix());
                out.writeObject(additional.getURI());
            }
        }
    }

    private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException {

        in.defaultReadObject();

        namespace = Namespace.getNamespace(
            (String)in.readObject(), (String)in.readObject());

        int size = in.read();

        if (size != 0) {
            additionalNamespaces = new ArrayList(size);
            for (int i = 0; i < size; i++) {
                Namespace additional = Namespace.getNamespace(
                    (String)in.readObject(), (String)in.readObject());
                additionalNamespaces.add(additional);
            }
        }
    }

    /**
     * Returns an iterator that walks over all descendants in document order.
     *
     * @return an iterator to walk descendants
     */
    public Iterator getDescendants() {
        return new DescendantIterator(this);
    }

    /**
     * Returns an iterator that walks over all descendants in document order
     * applying the Filter to return only elements that match the filter rule.
     * With filters you can match only Elements, only Comments, Elements or
     * Comments, only Elements with a given name and/or prefix, and so on.
     *
     * @param filter filter to select which descendants to see
     * @return an iterator to walk descendants within a filter
     */
    public Iterator getDescendants(Filter filter) {
        return new FilterIterator(new DescendantIterator(this), filter);
    }



    /**
     * This returns a <code>List</code> of all the child elements
     * nested directly (one level deep) within this element, as
     * <code>Element</code> objects.  If this target element has no nested
     * elements, an empty List is returned.  The returned list is "live"
     * in document order and changes to it affect the element's actual
     * contents.
     *
     * <p>
     * Sequential traversal through the List is best done with a Iterator
     * since the underlying implement of List.size() may not be the most
     * efficient.
     * </p>
     *
     * <p>
     * No recursion is performed, so elements nested two levels deep
     * would have to be obtained with:
     * <pre>
     * <code>
     *   Iterator itr = (currentElement.getChildren()).iterator();
     *   while(itr.hasNext()) {
     *     Element oneLevelDeep = (Element)itr.next();
     *     List twoLevelsDeep = oneLevelDeep.getChildren();
     *     // Do something with these children
     *   }
     * </code>
     * </pre>
     * </p>
     *
     * @return list of child <code>Element</code> objects for this element
     */
    public List getChildren() {
        return content.getView(new ElementFilter());
    }

    /**
     * This returns a <code>List</code> of all the child elements
     * nested directly (one level deep) within this element with the given
     * local name and belonging to no namespace, returned as
     * <code>Element</code> objects.  If this target element has no nested
     * elements with the given name outside a namespace, an empty List
     * is returned.  The returned list is "live" in document order
     * and changes to it affect the element's actual contents.
     * <p>
     * Please see the notes for <code>{@link #getChildren}</code>
     * for a code example.
     * </p>
     *
     * @param name local name for the children to match
     * @return all matching child elements
     */
    public List getChildren(String name) {
        return getChildren(name, Namespace.NO_NAMESPACE);
    }

    /**
     * This returns a <code>List</code> of all the child elements
     * nested directly (one level deep) within this element with the given
     * local name and belonging to the given Namespace, returned as
     * <code>Element</code> objects.  If this target element has no nested
     * elements with the given name in the given Namespace, an empty List
     * is returned.  The returned list is "live" in document order
     * and changes to it affect the element's actual contents.
     * <p>
     * Please see the notes for <code>{@link #getChildren}</code>
     * for a code example.
     * </p>
     *
     * @param name local name for the children to match
     * @param ns <code>Namespace</code> to search within
     * @return all matching child elements
     */
    public List getChildren(String name, Namespace ns) {
        return content.getView(new ElementFilter(name, ns));
    }

    /**
     * This returns the first child element within this element with the
     * given local name and belonging to the given namespace.
     * If no elements exist for the specified name and namespace, null is
     * returned.
     *
     * @param name local name of child element to match
     * @param ns <code>Namespace</code> to search within
     * @return the first matching child element, or null if not found
     */
    public Element getChild(String name, Namespace ns) {
        List elements = content.getView(new ElementFilter(name, ns));
        Iterator i = elements.iterator();
        if (i.hasNext()) {
            return (Element) i.next();
        }
        return null;
    }

    /**
     * This returns the first child element within this element with the
     * given local name and belonging to no namespace.
     * If no elements exist for the specified name and namespace, null is
     * returned.
     *
     * @param name local name of child element to match
     * @return the first matching child element, or null if not found
     */
    public Element getChild(String name) {
        return getChild(name, Namespace.NO_NAMESPACE);
    }

    /**
     * <p>
     * This removes the first child element (one level deep) with the
     * given local name and belonging to no namespace.
     * Returns true if a child was removed.
     * </p>
     *
     * @param name the name of child elements to remove
     * @return whether deletion occurred
     */
    public boolean removeChild(String name) {
        return removeChild(name, Namespace.NO_NAMESPACE);
    }

    /**
     * <p>
     * This removes the first child element (one level deep) with the
     * given local name and belonging to the given namespace.
     * Returns true if a child was removed.
     * </p>
     *
     * @param name the name of child element to remove
     * @param ns <code>Namespace</code> to search within
     * @return whether deletion occurred
     */
    public boolean removeChild(String name, Namespace ns) {
        List old = content.getView(new ElementFilter(name, ns));
        Iterator i = old.iterator();
        if (i.hasNext()) {
            i.next();
            i.remove();
            return true;
        }

        return false;
    }

    /**
     * <p>
     * This removes all child elements (one level deep) with the
     * given local name and belonging to no namespace.
     * Returns true if any were removed.
     * </p>
     *
     * @param name the name of child elements to remove
     * @return whether deletion occurred
     */
    public boolean removeChildren(String name) {
        return removeChildren(name, Namespace.NO_NAMESPACE);
    }

    /**
     * <p>
     * This removes all child elements (one level deep) with the
     * given local name and belonging to the given namespace.
     * Returns true if any were removed.
     * </p>
     *
     * @param name the name of child elements to remove
     * @param ns <code>Namespace</code> to search within
     * @return whether deletion occurred
     */
    public boolean removeChildren(String name, Namespace ns) {
        boolean deletedSome = false;

        List old = content.getView(new ElementFilter(name, ns));
        Iterator i = old.iterator();
        while (i.hasNext()) {
            i.next();
            i.remove();
            deletedSome = true;
        }

        return deletedSome;
    }

}
