/*--

 $Id: Element.java,v 1.126 2003/02/27 00:03:19 jhunter Exp $

 Copyright (C) 2000 Jason Hunter & Brett McLaughlin.
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

import org.jdom.filter.ElementFilter;
import org.jdom.filter.Filter;

/**
 * <code>Element</code> defines behavior for an XML
 * element, modeled in Java.  Methods allow the user
 * to obtain the value of the element's textual content,
 * obtain its attributes, and get its children.
 *
 *
 * @author Brett McLaughlin
 * @author Jason Hunter
 * @author Lucas Gonze
 * @author Kevin Regan
 * @author Dan Schaffer
 * @author Yusuf Goolamabbas
 * @author Kent C. Johnson
 * @author Jools Enticknap
 * @author Alex Rosen
 * @author Bradley S. Huffman
 * @version $Revision: 1.126 $, $Date: 2003/02/27 00:03:19 $
 */
public class Element implements Serializable, Cloneable {

    private static final String CVS_ID =
    "@(#) $RCSfile: Element.java,v $ $Revision: 1.126 $ $Date: 2003/02/27 00:03:19 $ $Name:  $";

    private static final int INITIAL_ARRAY_SIZE = 5;

    /** The local name of the <code>Element</code> */
    protected String name;

    /** The <code>{@link Namespace}</code> of the <code>Element</code> */
    protected transient Namespace namespace;

    /** Additional <code>{@link Namespace}</code> declarations on this
        element */
    protected transient List additionalNamespaces;

    // See http://lists.denveronline.net/lists/jdom-interest/2000-September/003030.html
    // for a possible memory optimization here (using a RootElement subclass)

    /** Parent element, document, or null if none */
    protected Object parent;

    /** The attributes of the <code>Element</code>. */
    protected AttributeList attributes = new AttributeList(this);

    /** The mixed content of the <code>Element</code>. */
    protected ContentList content = new ContentList(this);

    /**
     * This protected constructor is provided in order to support an Element
     * subclass that wants full control over variable initialization.  It
     * intentionally leaves all instance variables null, allowing a
     * lightweight subclass implementation.  The subclass is responsible for
     * ensuring all the get and set methods on Element behave as documented.
     *
     * <p>
     * When implementing an <code>Element</code> subclass which doesn't
     * require full control over variable initialization, be aware that
     * simply calling super() (or letting the compiler add the implicit
     * super() call) will not initialize the instance variables which will
     * cause many of the methods to throw a
     * <code>NullPointerException</code>.  Therefore, the
     * constructor for these subclasses should call one of the public
     * constructors so variable initialization is handled automatically.
     * </p>
     */
    protected Element() { }

    /**
     * This will create a new <code>Element</code>
     * with the supplied (local) name, and define
     * the <code>{@link Namespace}</code> to be used.
     * If the provided namespace is null, the element will have
     * no namespace.
     *
     * @param name <code>String</code> name of element.
     * @param namespace <code>Namespace</code> to put element in.
     * @throws IllegalNameException if the given name is illegal as an
     *         element name.
     */
    public Element(String name, Namespace namespace) {
        setName(name);
        setNamespace(namespace);
    }

    /**
     * This will create an <code>Element</code> in no
     * <code>{@link Namespace}</code>.
     *
     * @param name <code>String</code> name of element.
     * @throws IllegalNameException if the given name is illegal as an
     *         element name.
     */
    public Element(String name) {
        this(name, (Namespace) null);
    }

    /**
     * This will create a new <code>Element</code> with
     * the supplied (local) name, and specifies the URI
     * of the <code>{@link Namespace}</code> the <code>Element</code>
     * should be in, resulting it being unprefixed (in the default
     * namespace).
     *
     * @param name <code>String</code> name of element.
     * @param uri <code>String</code> URI for <code>Namespace</code> element
     *        should be in.
     * @throws IllegalNameException if the given name is illegal as an
     *         element name or the given URI is illegal as a
     *         namespace URI
     */
    public Element(String name, String uri) {
        this(name, Namespace.getNamespace("", uri));
    }

    /**
     * This will create a new <code>Element</code> with
     * the supplied (local) name, and specifies the prefix and URI
     * of the <code>{@link Namespace}</code> the <code>Element</code>
     * should be in.
     *
     * @param name <code>String</code> name of element.
     * @param uri <code>String</code> URI for <code>Namespace</code> element
     *        should be in.
     * @throws IllegalNameException if the given name is illegal as an
     *         element name, the given prefix is illegal as a namespace
     *         prefix, or the given URI is illegal as a namespace URI
     */
    public Element(String name, String prefix, String uri) {
        this(name, Namespace.getNamespace(prefix, uri));
    }

    /**
     * This returns the (local) name of the
     * <code>Element</code>, without any
     * namespace prefix, if one exists.
     *
     * @return <code>String</code> - element name.
     */
    public String getName() {
        return name;
    }

    /**
     * This sets the (local) name of the <code>Element</code>.
     *
     * @return <code>Element</code> - the element modified.
     * @throws IllegalNameException if the given name is illegal as an
     *         Element name.
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
     * This will return this <code>Element</code>'s
     * <code>{@link Namespace}</code>.
     *
     * @return <code>Namespace</code> - Namespace object for this
     *         <code>Element</code>
     */
    public Namespace getNamespace() {
        return namespace;
    }

    /**
     * This sets this <code>Element</code>'s <code>{@link Namespace}</code>.
     * If the provided namespace is null, the element will have no namespace.
     *
     * @return <code>Element</code> - the element modified.
     */
    public Element setNamespace(Namespace namespace) {
        if (namespace == null) {
            namespace = Namespace.NO_NAMESPACE;
        }

        this.namespace = namespace;
        return this;
    }

    /**
     * This returns the namespace prefix
     * of the <code>Element</code>, if
     * one exists.  Otherwise, an empty
     * <code>String</code> is returned.
     *
     * @return <code>String</code> - namespace prefix.
     */
    public String getNamespacePrefix() {
        return namespace.getPrefix();
    }

    /**
     * This returns the URI mapped to this <code>Element</code>'s
     * prefix (or the default namespace if no prefix). If no
     * mapping is found, an empty <code>String</code> is returned.
     *
     * @return <code>String</code> - namespace URI for this
     * <code>Element</code>.
     */
    public String getNamespaceURI() {
        return namespace.getURI();
    }

    /**
     * This returns the Namespace in scope on this element for the given
     * prefix (this involves searching up the tree, so the results depend
     * on the current location of the element).  It returns null if there
     * is no Namespace in scope with the given prefix at this point in
     * the document.
     *
     * @param prefix  namespace prefix to look up
     * @return <code>Namespace</code> - namespace in scope for the given
     * prefix on this <code>Element</code>, or null if none.
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
     * This returns the full name of the
     * <code>Element</code>, in the form
     * [namespacePrefix]:[localName].  If
     * the <code>Element</code> does not have a namespace prefix,
     * then the local name is returned.
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
     * This will add a namespace declarations to this element.
     * This should <i>not</i> be used to add the declaration for
     * this element itself; that should be assigned in the construction
     * of the element. Instead, this is for adding namespace
     * declarations on the element not relating directly to itself.
     *
     * @param additional <code>Namespace</code> to add.
     * @throws IllegalAddException if the namespace prefix collides with
     *   another namespace prefix on the element.
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
     * This will remove a namespace declarations from this element.
     * This should <i>not</i> be used to remove the declaration for
     * this element itself; that should be handled in the construction
     * of the element. Instead, this is for removing namespace
     * declarations on the element not relating directly to itself.
     * If the declaration is not present, this method does nothing.
     *
     * @param additionalNamespace <code>Namespace</code> to remove.
     */
    public void removeNamespaceDeclaration(Namespace additionalNamespace) {
        if (additionalNamespaces == null) {
            return;
        }
        additionalNamespaces.remove(additionalNamespace);
    }

    /**
     * This will return any namespace declarations on this element
     * that exist, <i>excluding</i> the namespace of the element
     * itself, which can be obtained through
     * <code>{@link #getNamespace()}</code>. If there are no additional
     * declarations, this returns an empty list.  Note, the returned
     * list is not live, for performance reasons.
     *
     * @return <code>List</code> - the additional namespace declarations.
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
     * This will return the parent of this <code>Element</code>.
     * If there is no parent, then this returns <code>null</code>.
     * Also note that on its own, this is not 100% sufficient to
     * see if the <code>Element</code> is not in use - this should
     * be used in tandem with <code>{@link #isRootElement}</code>
     * to determine this.
     *
     * @return parent of this <code>Element</code>.
     */
    public Element getParent() {
        if (parent instanceof Element) {
            return (Element) parent;
        }
        return null;
    }

    /**
     * This will set the parent of this <code>Element</code>.
     * The caller is responsible for handling pre-existing parentage.
     *
     * @param parent <code>Element</code> to be new parent.
     * @return <code>Element</code> - this <code>Element</code> modified.
     */
    protected Element setParent(Element parent) {
        this.parent = parent;
        return this;
    }

    /**
     * This detaches the element from its parent, or does nothing if the
     * element has no parent.
     *
     * @return <code>Element</code> - this <code>Element</code> modified.
     */
    public Element detach() {
        if (parent instanceof Element) {
            ((Element) parent).removeContent(this);
        }
        else if (parent instanceof Document) {
            ((Document) parent).detachRootElement();
        }
        return this;
    }

    /**
     * This returns a <code>boolean</code> value indicating
     * whether this <code>Element</code> is a root
     * <code>Element</code> for a JDOM <code>{@link Document}</code>.
     * This should be used in tandem with
     * <code>{@link #getParent}</code> to determine
     * if an <code>Element</code> has no "attachments" to
     * parents.
     *
     * @return <code>boolean</code> - whether this is a root element.
     */
    public boolean isRootElement() {
        return parent instanceof Document;
    }

    /**
     * This sets the <code>{@link Document}</code> parent of this element
     * and makes it the root element.  The caller is responsible for
     * ensuring the element doesn't have a pre-existing parent.
     *
     * @param document <code>Document</code> parent
     * @return <code>Document</code> this <code>Element</code> modified
     */
    protected Element setDocument(Document document) {
        this.parent = document;
        return this;
    }

    /**
     * This retrieves the owning <code>{@link Document}</code> for
     * this Element, or null if not a currently a member of a
     * <code>{@link Document}</code>.
     *
     * @return <code>Document</code> owning this Element, or null.
     */
    public Document getDocument() {
        if (parent instanceof Document) {
            return (Document) parent;
        }
        if (parent instanceof Element) {
            return (Document) ((Element)parent).getDocument();
        }

        return null;
    }

    /**
     * This returns the textual content directly held under this
     * element.  This will include all text within
     * this single element, including whitespace and CDATA
     * sections if they exist.  It's essentially the concatenation of
     * all <code>Text</code> and <code>CDATA</code> nodes returned by
     * <code>getContent()</code>.  The call does not recurse into child elements.
     * If no textual value exists for the
     * element, an empty <code>String</code> ("") is returned.
     *
     * @return text content for this element, or empty string if none
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
     * This returns the textual content of this element with all
     * surrounding whitespace removed.  If no textual value exists for
     * the element, or if only whitespace exists, the empty string is
     * returned.
     *
     * @return trimmed text content for this element, or empty string
     * if none
     */
    public String getTextTrim() {
        return getText().trim();
    }

    /**
     * This returns the textual content of this element with all
     * surrounding whitespace removed and internal whitespace normalized
     * to a single space.  If no textual value exists for the element,
     * or if only whitespace exists, the empty string is returned.
     *
     * @return normalized text content for this element, or empty string
     * if none
     */
    public String getTextNormalize() {
        return Text.normalizeString(getText());
    }

    /**
     * This convenience method returns the textual content of the named
     * child element, or returns an empty <code>String</code> ("")
     * if the child has no textual content. However, if the child does
     * not exist, <code>null</code> is returned.
     *
     * @param name the name of the child
     * @return text content for the named child, or null if none
     */
    public String getChildText(String name) {
        Element child = getChild(name);
        if (child == null) {
            return null;
        }
        return child.getText();
    }

    /**
     * This convenience method returns the trimmed textual content of the
     * named child element, or returns null if there's no such child.
     * See <code>{@link #getTextTrim()}</code> for details of text trimming.
     *
     * @param name the name of the child
     * @return trimmed text content for the named child, or null if none
     */
    public String getChildTextTrim(String name) {
        Element child = getChild(name);
        if (child == null) {
            return null;
        }
        return child.getTextTrim();
    }

    /**
     * This convenience method returns the normalized textual content of the
     * named child element, or returns null if there's no such child.
     * See <code>{@link #getTextNormalize()}</code> for details of text
     * normalizing.
     *
     * @param name the name of the child
     * @return normalized text content for the named child, or null if none
     */
    public String getChildTextNormalize(String name) {
        Element child = getChild(name);
        if (child == null) {
            return null;
        }
        return child.getTextNormalize();
    }

    /**
     * This convenience method returns the textual content of the named
     * child element, or returns null if there's no such child.
     *
     * @param name the name of the child
     * @param ns the namespace of the child
     * @return text content for the named child, or null if none
     */
    public String getChildText(String name, Namespace ns) {
        Element child = getChild(name, ns);
        if (child == null) {
            return null;
        }
        return child.getText();
    }

    /**
     * This convenience method returns the trimmed textual content of the
     * named child element, or returns null if there's no such child.
     * See <code>{@link #getTextTrim()}</code> for
     * details of text trimming.
     *
     * @param name the name of the child
     * @param ns the namespace of the child
     * @return trimmed text content for the named child, or null if none
     */
    public String getChildTextTrim(String name, Namespace ns) {
        Element child = getChild(name, ns);
        if (child == null) {
            return null;
        }
        return child.getTextTrim();
    }

    /**
     * This convenience method returns the normalized textual content of the
     * named child element, or returns null if there's no such child.
     * See <code>{@link #getTextNormalize()}</code> for
     * details of text normalizing.
     *
     * @param name the name of the child
     * @param ns the namespace of the child
     * @return normalized text content for the named child, or null if none
     */
    public String getChildTextNormalize(String name, Namespace ns) {
        Element child = getChild(name, ns);
        if (child == null) {
            return null;
        }
        return child.getTextNormalize();
    }

    /**
     * This sets the content of the element to be the text given.
     * All existing text content and non-text context is removed.
     * If this element should have both textual content and nested
     * elements, use <code>{@link #setContent}</code> instead.
     * Setting a null text value is equivalent to setting an empty string
     * value.
     *
     * @param text new content for the element
     * @return this element modified
     * @throws IllegalDataException if <code>text</code> contains an 
     *         illegal character such as a vertical tab (as determined
     *         by {@link org.jdom.Verifier#checkCharacterData})
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
    public Element setContent(List newContent) {
        content.clearAndSet(newContent);
        return this;
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

    /** Remove a range of items from a list */
    private void removeRange(List list, int start, int end) {
        ListIterator i = list.listIterator(start);
        for (int j = 0; j < (end - start); j++) {
            i.next();
            i.remove();
        }
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
     * This adds text content to this element.  It does not replace the
     * existing content as does <code>setText()</code>.
     *
     * @param str <code>String</code> to add
     * @return this element modified
     * @throws IllegalDataException if <code>str</code> contains an 
     *         illegal character such as a vertical tab (as determined
     *         by {@link org.jdom.Verifier#checkCharacterData})
     */
    public Element addContent(String str) {
        return addContent(new Text(str));
    }

    /**
     * This adds text content to this element.  It does not replace the
     * existing content as does <code>setText()</code>.
     *
     * @param text <code>Text</code> to add
     * @return this element modified
     * @throws IllegalAddException if the <code>Text</code> object 
     *         you're attempting to add already has a parent element.
     */
    public Element addContent(Text text) {
        content.add(text);
        return this;
    }

    /**
     * This adds element content to this element.
     *
     * @param element <code>Element</code> to add
     * @return this element modified
     * @throws IllegalAddException if the element you're attempting to
     *         add already has a parent element, or if you're attempting
     *         to add it as a descendent of itself (which would result in
     *         a recursive element definition!).
     */
    public Element addContent(Element element) {
        content.add(element);
        return this;
    }

    /**
     * This adds a processing instruction as content to this element.
     *
     * @param pi <code>ProcessingInstruction</code> to add
     * @return this element modified
     * @throws IllegalAddException if the given processing instruction,
     *         <code>pi</code>, already has a parent.
     */
    public Element addContent(ProcessingInstruction pi) {
        content.add(pi);
        return this;
    }

    /**
     * This adds entity content to this element.
     *
     * @param entity <code>EntityRef</code> to add
     * @return this element modified
     * @throws IllegalAddException if the given EntityRef already has a
     *         parent.
     */
    public Element addContent(EntityRef entity) {
        content.add(entity);
        return this;
    }

    /**
     * This adds a comment as content to this element.
     *
     * @param comment <code>Comment</code> to add
     * @return this element modified
     * @throws IllegalAddException if the given Comment already has a
     *         parent.
     */
    public Element addContent(Comment comment) {
        content.add(comment);
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
        Object p = parent;
        while (p instanceof Element) {
            if (p == element) {
                return true;
            }
            p = ((Element) p).getParent();
        }
        return false;
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
        return (Attribute) attributes.get(name, Namespace.NO_NAMESPACE);
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
        Attribute attribute = (Attribute) attributes.get(name,
                                              Namespace.NO_NAMESPACE);
        return (attribute == null) ? null : attribute.getValue();
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
        Attribute attribute = (Attribute) attributes.get(name,
                                              Namespace.NO_NAMESPACE);
        return (attribute == null) ? def : attribute.getValue();
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
        Attribute attribute = (Attribute) attributes.get(name, ns);
        return (attribute == null) ? null : attribute.getValue();
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
     * @param attributes <code>List</code> of attributes to set
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
        return attributes.remove(name, Namespace.NO_NAMESPACE);
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
     * This removes the specified <code>Element</code>.
     * If the specified <code>Element</code> is not a child of
     * this <code>Element</code>, this method does nothing.
     * </p>
     *
     * @param child <code>Element</code> to delete
     * @return whether deletion occurred
     */
    public boolean removeContent(Element element) {
        return content.remove(element);
    }

    /**
     * <p>
     * This removes the specified <code>ProcessingInstruction</code>.
     * If the specified <code>ProcessingInstruction</code> is not a child of
     * this <code>Element</code>, this method does nothing.
     * </p>
     *
     * @param child <code>ProcessingInstruction</code> to delete
     * @return whether deletion occurred
     */
    public boolean removeContent(ProcessingInstruction pi) {
        return content.remove(pi);
    }

    /**
     * <p>
     * This removes the specified <code>Comment</code>.
     * If the specified <code>Comment</code> is not a child of
     * this <code>Element</code>, this method does nothing.
     * </p>
     *
     * @param comment <code>Comment</code> to delete
     * @return whether deletion occurred
     */
    public boolean removeContent(Comment comment) {
        return content.remove(comment);
    }

    /**
     * <p>
     * This removes the specified <code>Text</code>.
     * If the specified <code>Text</code> is not a child of
     * this <code>Element</code>, this method does nothing.
     * </p>
     *
     * @param text <code>Text</code> to delete
     * @return whether deletion occurred
     */
    public boolean removeContent(Text text) {
        return content.remove(text);
    }

    /**
     * <p>
     * This removes the specified <code>EntityRef</code>.
     * If the specified <code>EntityRef</code> is not a child of
     * this <code>Element</code>, this method does nothing.
     * </p>
     *
     * @param child <code>EntityRef</code> to delete
     * @return whether deletion occurred
     */
    public boolean removeContent(EntityRef entity) {
        return content.remove(entity);
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
     *  This tests for equality of this <code>Element</code> to the supplied
     *    <code>Object</code>, explicitly using the == operator.
     * </p>
     *
     * @param ob <code>Object</code> to compare to
     * @return whether the elements are equal
     */
    public final boolean equals(Object ob) {
        return (this == ob);
    }

    /**
     * <p>
     *  This returns the hash code for this <code>Element</code>.
     * </p>
     *
     * @return inherited hash code
     */
    public final int hashCode() {
        return super.hashCode();
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

        try {
            element = (Element) super.clone();
        } catch (CloneNotSupportedException ce) {
            // Can't happen
        }

        // name and namespace are references to immutable objects
        // so super.clone() handles them ok

        // Reference to parent is copied by super.clone()
        // (Object.clone()) so we have to remove it
        element.parent = null;

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
    }

    private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException {

        in.defaultReadObject();

        namespace = Namespace.getNamespace(
            (String)in.readObject(), (String)in.readObject());
    }

    /**
     * <p>
     * This adds a CDATA section as content to this element.
     * </p>
     *
     * @param cdata <code>CDATA</code> to add
     * @return this element modified
     *
     * @deprecated Deprecated in Beta 9, since addContent(Text) is
     * equivalent.
     */
    public Element addContent(CDATA cdata) {
        content.add(cdata);
        return this;
    }

    /**
     * <p>
     * This removes the specified <code>CDATA</code>.
     * If the specified <code>CDATA</code> is not a child of
     * this <code>Element</code>, this method does nothing.
     * </p>
     *
     * @param cdata <code>CDATA</code> to delete
     * @return whether deletion occurred
     *
     * @deprecated Deprecated in Beta 9, since addContent(Text) is
     * equivalent.
     */
    public boolean removeContent(CDATA cdata) {
        return content.remove(cdata);
    }

    /**
     * <p>
     * This removes all child elements.  Returns true if any were removed.
     * </p>
     * @deprecated Deprecated in Beta 9, instead of this method you can call
     * <code>clear()</code> on the list returned by <code>getChildren()</code> or by 
     * <code>getContent()</code>
     *
     * @return whether deletion occurred
     */
    public boolean removeChildren() {
        boolean deletedSome = false;

        List old = content.getView(new ElementFilter());
        Iterator i = old.iterator();
        while (i.hasNext()) {
            i.next();
            i.remove();
            deletedSome = true;
        }

        return deletedSome;
    }

    /**
     * Test whether this element has a child element.
     * This method can be used before a call to {@link #getContent},
     * which always creates a "live" list, to improve performance.
     *
     * @deprecated Deprecated in Beta 9, instead of this method you can check
     * the size of a getChildren() call
     *
     * @return <code>true</code> if this element has at least
     *         one child element
     */
    public boolean hasChildren() {
        for (int i = 0; i < content.size(); i++) {
            if (content.get(i) instanceof Element) {
                return true;
            }
        }
        return false;
    }

    /**
     * This sets the content of the element the same as {@link #setContent},
     * except only <code>Element</code> objects are allowed in the supplied
     * list.
     *
     * @deprecated Deprecated in Beta 9, use setContent(List) instead
     *
     * @param children <code>List</code> of <code>Element</code> objects to add
     * @return this element modified
     */
    public Element setChildren(List children) {
        List list = content.getView(new ElementFilter());
        // Save initial size
        int size = list.size();
        try {
            // Try adding the list
            list.addAll(children);
        }
        catch(RuntimeException exception) {
            // Restore to original state by removing
            // items that where appended
            removeRange(list, size, list.size());
            throw exception;
        }

        // Add was sucessful, remove old children
        removeRange(list, 0, size);
        return this;
    }

}
