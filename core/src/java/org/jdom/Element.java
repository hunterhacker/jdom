/*-- 

 $Id: Element.java,v 1.93 2001/06/27 18:36:46 jhunter Exp $

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

import java.io.*;
import java.util.*;

/**
 * <p><code>Element</code> defines behavior for an XML
 *   element, modeled in Java.  Methods allow the user
 *   to obtain the value of the element's textual content,
 *   obtain its attributes, and get its children.
 * </p>
 *
 *
 * @author Brett McLaughlin
 * @author Jason Hunter
 * @author Lucas Gonze
 * @author Kevin Regan
 * @author Dan Schaffer
 * @author Yusuf Goolamabbas
 * @version 1.0
 */
public class Element implements Serializable, Cloneable {

    private static final String CVS_ID =
    "@(#) $RCSfile: Element.java,v $ $Revision: 1.93 $ $Date: 2001/06/27 18:36:46 $ $Name:  $";

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

    /** The attributes of the <code>Element</code> */
    protected List attributes;
    
    /** The mixed content of the <code>Element</code> */
    protected List content;
    
    /**
     * <p>
     * This protected constructor is provided in order to support an Element 
     * subclass that wants full control over variable initialization.  It 
     * intentionally leaves all instance variables null, allowing a 
     * lightweight subclass implementation.  The subclass is responsible for 
     * ensuring all the get and set methods on Element behave as documented.
     * </p>
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
     * <p>
     * This will create a new <code>Element</code>
     *   with the supplied (local) name, and define
     *   the <code>{@link Namespace}</code> to be used.
     * If the provided namespace is null, the element will have 
     * no namespace.
     * </p>
     *
     * @param name <code>String</code> name of element.
     * @namespace <code>Namespace</code> to put element in.
     */
    public Element(String name, Namespace namespace) {
        setName(name);
        setNamespace(namespace);
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
        this(name, (Namespace) null);
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
     * This sets the (local) name of the <code>Element</code>.
     * </p>
     *
     * @return <code>Element</code> - the element modified.
     * @throws IllegalNameException if the given name is invalid as an
     *         Element name.
     */
    public Element setName(String name) {
        String reason;
        if ((reason = Verifier.checkElementName(name)) != null) {
            throw new IllegalNameException(name, "element", reason);
        }
        this.name = name;
        return this;
    }

    /**
     * <p>
     *  This will return this <code>Element</code>'s
     *    <code>{@link Namespace}</code>.
     * </p>
     *
     * @return <code>Namespace</code> - Namespace object for this 
     *         <code>Element</code>
     */
    public Namespace getNamespace() {
        return namespace;
    }

    /**
     * <p>
     *  This sets this <code>Element</code>'s <code>{@link Namespace}</code>.
     *  If the provided namespace is null, the element will have no namespace.
     * </p>
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
     * @return <code>String</code> - namespace URI for this 
     * <code>Element</code>.
     */
    public String getNamespaceURI() {
        return namespace.getURI();
    }

    /**
     * <p>
     * This returns the Namespace in scope on this element for the given 
     * prefix (this involves searching up the tree, so the results depend 
     * on the current location of the element).  It returns null if there
     * is no Namespace in scope with the given prefix at this point in 
     * the document.
     * </p>
     *
     * @param prefix  namespace prefix to look up
     * @return <code>Namespace</code> - namespace in scope for the given 
     * prefix on this <code>Element</code>, or null if none.
     */
    public Namespace getNamespace(String prefix) {
        if (prefix == null) {
            return null;
        }

        // Check if the prefix is the prefix for this element
        if (prefix.equals(getNamespacePrefix())) {
            return getNamespace();
        }

        // Scan the additional namespaces
        List addl = getAdditionalNamespaces();
        if (addl.size() > 0) {
            Iterator itr = addl.iterator();
            while (itr.hasNext()) {
                Namespace ns = (Namespace) itr.next();
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
     *  This will add a namespace declarations to this element.
     *    This should <i>not</i> be used to add the declaration for
     *    this element itself; that should be assigned in the construction
     *    of the element. Instead, this is for adding namespace
     *    declarations on the element not relating directly to itself.
     * </p>
     *
     * @param additionalNamespace <code>Namespace</code> to add.
     * @throws IllegalAddException if the namespace prefix collides with 
     *   another namespace prefix on the element.
     */
    public void addNamespaceDeclaration(Namespace additionalNamespace) {

        // Verify the new namespace prefix doesn't collide with another 
        // declared namespace, an attribute prefix, or this element's prefix
        String prefix = additionalNamespace.getPrefix();
        String uri = additionalNamespace.getURI();
        if (prefix.equals(getNamespacePrefix()) && 
              !uri.equals(getNamespaceURI())) {
            throw new IllegalAddException(this, additionalNamespace,
                "The namespace prefix \"" + prefix + "\" collides " +
                "with the element namespace prefix");
        }
        if (additionalNamespaces != null && additionalNamespaces.size() > 0) {
            Iterator itr = additionalNamespaces.iterator();
            while (itr.hasNext()) {
                Namespace ns = (Namespace) itr.next();
                if (prefix.equals(ns.getPrefix()) && 
                      !uri.equals(ns.getURI())) {
                    throw new IllegalAddException(this, additionalNamespace,
                        "The namespace prefix \"" + prefix +
                        "\" collides with an additional namespace declared " +
                        "by the element");
                }
            }
        }
        if (attributes != null && attributes.size() > 0) {
            Iterator itr = attributes.iterator();
            while (itr.hasNext()) {
                Namespace ns = 
                   (Namespace) ((Attribute)itr.next()).getNamespace();
                if (prefix.equals(ns.getPrefix()) && 
                      !uri.equals(ns.getURI())) {
                    throw new IllegalAddException(this, additionalNamespace,
                        "The namespace prefix \"" + prefix +
                        "\" collides with an attribute namespace on " +
                        "the element");
                }
            }
        }

        if (additionalNamespaces == null) {
            additionalNamespaces = new ArrayList(INITIAL_ARRAY_SIZE);
        }

        additionalNamespaces.add(additionalNamespace);
    }

    /**
     * <p>
     *  This will remove a namespace declarations from this element.
     *    This should <i>not</i> be used to remove the declaration for
     *    this element itself; that should be handled in the construction
     *    of the element. Instead, this is for removing namespace
     *    declarations on the element not relating directly to itself.
     *    If the declaration is not present, this method does nothing.
     * </p>
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
     * <p>
     *  This will return any namespace declarations on this element
     *    that exist, <i>excluding</i> the namespace of the element
     *    itself, which can be obtained through
     *    <code>{@link #getNamespace()}</code>. If there are no additional
     *    declarations, this returns an empty list.  Note, the returned 
     *    list is not live, for performance reasons.
     * </p>
     *
     * @return <code>List</code> - the additional namespace declarations.
     */
    public List getAdditionalNamespaces() {
        // Not having the returned list be live allows us to avoid creating a
        // new list object when XMLOutputter calls this method on an elt
        // with an empty list.
        if (additionalNamespaces == null) {
            return Collections.EMPTY_LIST;
        }
        else {
            return Collections.unmodifiableList(additionalNamespaces);
        }
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
     * @return parent of this <code>Element</code>.
     */
    public Element getParent() {
        if (parent instanceof Element) {
            return (Element) parent;
        }
        else {
            return null;
        }
    }

    /**
     * <p>
     * This will set the parent of this <code>Element</code>.
     * The caller is responsible for handling pre-existing parentage.
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
     * This detaches the element from its parent, or does nothing if the
     * element has no parent.
     * </p>
     *
     * @return <code>Element</code> - this <code>Element</code> modified.
     */
    public Element detach() {
        if (parent instanceof Element) {
            ((Element)parent).removeContent(this);
        }
        else if (parent instanceof Document) {
            ((Document)parent).setRootElement(
                        new Element("root-element-was-detached"));
        }
        // A null parent is a no-op

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
        return parent instanceof Document;
    }

    /**
     * <p>
     * This sets the <code>{@link Document}</code> parent of this element
     *   and makes it the root element.  The caller is responsible for
     *   ensuring the element doesn't have a pre-existing parent.
     * </p>
     *
     * @param document <code>Document</code> parent
     * @return <code>Document</code> this <code>Element</code> modified
     */
    protected Element setDocument(Document document) {
        this.parent = document;

        return this;
    }

    /**
     * <p>
     * This retrieves the owning <code>{@link Document}</code> for 
     *   this Element, or null if not a currently a member of a
     *   <code>{@link Document}</code>.
     * </p>
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
     * <p>
     * This returns the textual content directly held under this
     *   element.  This will include all text within
     *   this single element, including whitespace and CDATA
     *   sections if they exist.  It's essentially the concatenation of
     *   all String nodes returned by getMixedContent().  The call does not
     *   recurse into child elements.  If no textual value exists for the 
     *   element, an empty <code>String</code> ("") is returned.
     * </p>
     *
     * @return text content for this element, or empty string if none
     */
    public String getText() {
        if ((content == null) || 
             (content.size() < 1) || 
             (content.get(0) == null)) {
            return "";
        }

        // If we hold only a String, return it directly
        if ((content.size() == 1) && (content.get(0) instanceof String)) {
            return (String)content.get(0);
        }

        // Else build String up
        StringBuffer textContent = new StringBuffer();
        boolean hasText = false;

        Iterator i = content.iterator();
        while (i.hasNext()) {
            Object obj = i.next();
            if (obj instanceof String) {
                textContent.append((String)obj);
                hasText = true;
            } else if (obj instanceof CDATA) {
                textContent.append(((CDATA)obj).getText());
                hasText = true;
            }
        }

        if (!hasText) {
            return "";
        } else {
            return textContent.toString();
        }
    }

    /**
     * <p>
     * This returns the textual content of this element with all 
     * surrounding whitespace removed.  If no textual value exists for 
     * the element, or if only whitespace exists, the empty string is
     * returned.
     * </p>
     *
     * @return trimmed text content for this element, or empty string
     * if none
     */
    public String getTextTrim() {
        return getText().trim();
    }

    /**
     * <p>
     * This returns the textual content of this element with all 
     * surrounding whitespace removed and internal whitespace normalized
     * to a single space.  If no textual value exists for the element,
     * or if only whitespace exists, the empty string is returned.
     * </p>
     *
     * @return normalized text content for this element, or empty string
     * if none
     */
    public String getTextNormalize() {
        return normalizeString(getText());
    }

    // Support method for fast string normalization
    // Per XML 1.0 Production 3 whitespace includes: #x20, #x9, #xD, #xA
    private static String normalizeString(String value) {
        char[] c = value.toCharArray();
        char[] n = new char[c.length];
        boolean white = true;
        int pos = 0;
        for (int i = 0; i < c.length; i++) {
            if (" \t\n\r".indexOf(c[i]) != -1) {
                if (!white) {
                    n[pos++] = ' ';
                    white = true;
                }
            }
            else {
                n[pos++] = c[i];
                white = false;
            }
        }
        if (white && pos > 0) {
            pos--;
        }
        return new String(n, 0, pos);
    }

    /**
     * <p>
     *  This convenience method returns the textual content of the named
     *    child element, or returns an empty <code>String</code> ("")
     *    if the child has no textual content. However, if the child does
     *    not exist, <code>null</code> is returned.
     * </p>
     *
     * @param name the name of the child
     * @return text content for the named child, or null if none
     */
    public String getChildText(String name) {
        Element child = getChild(name);
        if (child == null) {
            return null;
        } else {
            return child.getText();
        }
    }

    /**
     * <p>
     *  This convenience method returns the trimmed textual content of the 
     *    named child element, or returns null if there's no such child.  
     *    See <code>{@link #getTextTrim()}</code> for details of text trimming.
     * </p>
     *
     * @param name the name of the child
     * @return trimmed text content for the named child, or null if none
     */
    public String getChildTextTrim(String name) {
        Element child = getChild(name);
        if (child == null) {
            return null;
        } else {
            return child.getTextTrim();
        }
    }

    /**
     * <p>
     * This convenience method returns the textual content of the named
     * child element, or returns null if there's no such child.
     * </p>
     *
     * @param name the name of the child
     * @param ns the namespace of the child
     * @return text content for the named child, or null if none
     */
    public String getChildText(String name, Namespace ns) {
        Element child = getChild(name, ns);
        if (child == null) {
            return null;
        } else {
            return child.getText();
        }
    }

    /**
     * <p>
     *  This convenience method returns the trimmed textual content of the 
     *    named child element, or returns null if there's no such child.  
     *    See <code>{@link #getTextTrim()}</code> for
     *    details of text trimming.
     * </p>
     *
     * @param name the name of the child
     * @param ns the namespace of the child
     * @return trimmed text content for the named child, or null if none
     */
    public String getChildTextTrim(String name, Namespace ns) {
        Element child = getChild(name, ns);
        if (child == null) {
            return null;
        } else {
            return child.getTextTrim();
        }
    }

    /**
     * <p>
     * This sets the content of the element to be the text given.
     * All existing text content and non-text context is removed.
     * If this element should have both textual content and nested 
     * elements, use <code>{@link #setMixedContent}</code> instead.
     * Setting a null text value is equivalent to setting an empty string
     * value.
     * </p>
     *
     * @param text new content for the element
     * @return this element modified
     */
    public Element setText(String text) {
        if (content != null) {
            content.clear();
        } else {
            content = new ArrayList(INITIAL_ARRAY_SIZE);
        }

        if (text != null) {
            content.add(text);
        }

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
     *
     * @return <code>boolean</code> - indicating whether there
     *         is mixed content (both textual data and elements).
     *
     * @deprecated Deprecated in beta7 because hasMixedContent() is of
     * little use and isn't really implemented as mixed content is defined
     */
    public boolean hasMixedContent() {
        if (content == null) {
            return false;
        }

        Class prevClass = null;
        Iterator i = content.iterator();
        while (i.hasNext()) {
            Object obj = i.next();
            Class newClass = obj.getClass();

            if (newClass != prevClass) {
               if (prevClass != null) {
                  return true;
               }
               prevClass = newClass;
            }
        }

        return false;
    }

    /**
     * <p>
     * This returns the full content of the element as a List which
     * may contain objects of type <code>String</code>, <code>Element</code>,
     * <code>Comment</code>, <code>ProcessingInstruction</code>,
     * <code>CDATA</code>, and <code>EntityRef</code>.  
     * When there is technically no mixed content and
     * all contents are of the same type, then all objects returned in the
     * List will be of the same type.  The List returned is "live" in 
     * document order and modifications to it affect the element's actual 
     * contents.  Whitespace content is returned in its entirety.
     * </p>
     *
     * @return a <code>List</code> containing the mixed content of the
     *         element: may contain <code>String</code>,
     *         <code>{@link Element}</code>, <code>{@link Comment}</code>,
     *         <code>{@link ProcessingInstruction}</code>,
     *         <code>{@link CDATA}</code>, and
     *         <code>{@link EntityRef}</code> objects.
     */
    public List getMixedContent() {
        if (content == null) {
            content = new ArrayList(INITIAL_ARRAY_SIZE);
        }

        PartialList result = new PartialList(content, this);
        result.addAllPartial(content);
        return result;
    }

    /**
     * <p>
     * This sets the content of the element.  The passed in List should
     * contain only objects of type <code>String</code>, <code>Element</code>,
     * <code>Comment</code>, <code>ProcessingInstruction</code>, 
     * <code>CDATA</code>, and <code>EntityRef</code>.  Passing a null or
     * empty List clears the existing content.  In event of an exception 
     * the original content will be unchanged and the items in the added
     * content will be unaltered.
     * </p>
     *
     * @return this element modified
     * @throws IllegalAddException if the List contains objects of 
     *         illegal types.
     */
    public Element setMixedContent(List mixedContent) {

        if (mixedContent == null) {
            content = new ArrayList(INITIAL_ARRAY_SIZE);
            return this;
        }

        // Save list with original content and create a new list
        List oldContent = content;
        content = new ArrayList(INITIAL_ARRAY_SIZE);

        RuntimeException ex = null;
        int itemsAdded = 0;

        try {
            for (Iterator i = mixedContent.iterator(); i.hasNext(); ) {
                Object obj = i.next();
                if (obj instanceof Element) {
                    addContent((Element)obj);
                }
                else if (obj instanceof String) {
                    addContent((String)obj);
                }
                else if (obj instanceof Comment) {
                    addContent((Comment)obj);
                }
                else if (obj instanceof ProcessingInstruction) {
                    addContent((ProcessingInstruction)obj);
                }
                else if (obj instanceof CDATA) {
                    addContent((CDATA)obj);
                }
                else if (obj instanceof EntityRef) {
                    addContent((EntityRef)obj);
                }
                else {
                    throw new IllegalAddException(
                      "An Element may directly contain only objects of type " +
                      "String, Element, Comment, CDATA, EntityRef, and " + 
                      "ProcessingInstruction: " +
                      (obj == null ? "null" : obj.getClass().getName()) + 
                      " is not allowed");
                }
                itemsAdded++;
            }
        }
        catch (RuntimeException e) {
            ex = e;
        }
        finally {
            if (ex != null) {
                // Restore the original state
                content = oldContent;
                // Unmodify all modified elements.  DO NOT change any later 
                // elements tho because they may already have parents!
                Iterator i = mixedContent.iterator();
                while (itemsAdded-- > 0) {
                    Object obj = i.next();
                    if (obj instanceof Element) {
                        ((Element)obj).setParent(null);
                    }
                    else if (obj instanceof Comment) {
                        ((Comment)obj).setParent(null);
                    }
                    else if (obj instanceof ProcessingInstruction) {
                        ((ProcessingInstruction)obj).setParent(null);
                    }
                    else if (obj instanceof EntityRef) {
                        ((EntityRef)obj).setParent(null);
                    }
                }
                throw ex;
            }
        }

        // There was no old content, so just return
        if (oldContent == null) {
            return this;
        } 

        // Remove parentage on the old content
        Iterator itr = oldContent.iterator();
        while (itr.hasNext()) {
            Object obj = itr.next();
            if (obj instanceof Element) {
                ((Element)obj).setParent(null);
            }
            else if (obj instanceof Comment) {
                ((Comment)obj).setParent(null);
            }
            else if (obj instanceof ProcessingInstruction) {
                ((ProcessingInstruction)obj).setParent(null);
            }
            else if (obj instanceof EntityRef) {
                ((EntityRef)obj).setParent(null);
            }
        }

        return this;
    }

    public boolean hasChildren() {
        if (content == null || content.size() == 0) {
            return false;
        }
          
        Iterator i = content.iterator();
        while (i.hasNext()){
            if (i.next() instanceof Element) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>
     * This returns a <code>List</code> of all the child elements
     * nested directly (one level deep) within this element, as 
     * <code>Element</code> objects.  If this target element has no nested 
     * elements, an empty List is returned.  The returned list is "live"
     * in document order and changes to it affect the element's actual 
     * contents.
     * </p>
     * <p>
     * This performs no recursion, so elements nested two levels
     *   deep would have to be obtained with:
     * <pre>
     * <code>
     *   Iterator itr = currentElement.getChildren().iterator();
     *   while (itr.hasNext()) {
     *     Element oneLevelDeep = (Element)nestedElements.next();
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
        if (content == null) {
            content = new ArrayList(INITIAL_ARRAY_SIZE);
        }
 
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
     * This sets the content of the element to be the List of 
     * <code>Element</code> objects within the supplied <code>List</code>.
     * All existing element and non-element content of the element is removed.
     * In event of an exception the children may be partially added.
     * </p>
     *
     * @param children <code>List</code> of <code>Element</code> objects to add
     * @return this element modified
     */
    public Element setChildren(List children) {
        return setMixedContent(children);
    }

    /**
     * <p>
     * This returns a <code>List</code> of all the child elements
     * nested directly (one level deep) within this element with the given
     * local name and belonging to no namespace, returned as 
     * <code>Element</code> objects.  If this target element has no nested 
     * elements with the given name outside a namespace, an empty List 
     * is returned.  The returned list is "live" in document order
     * and changes to it affect the element's actual contents.
     * </p>
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
     * <p>
     * This returns a <code>List</code> of all the child elements
     * nested directly (one level deep) within this element with the given
     * local name and belonging to the given Namespace, returned as 
     * <code>Element</code> objects.  If this target element has no nested 
     * elements with the given name in the given Namespace, an empty List 
     * is returned.  The returned list is "live" in document order
     * and changes to it affect the element's actual contents.
     * </p>
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
        PartialList children = new PartialList(getChildren(), this);

        if (content != null) {
            String uri = ns.getURI();
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
        }

        return children;
    }

    /**
     * <p>
     * This returns the first child element within this element with the 
     * given local name and belonging to the given namespace.
     * If no elements exist for the specified name and namespace, null is 
     * returned.
     * </p>
     *
     * @param name local name of child element to match
     * @param ns <code>Namespace</code> to search within
     * @return the first matching child element, or null if not found
     */
    public Element getChild(String name, Namespace ns) {
        if (content == null) {
            return null;
        }

        String uri = ns.getURI();
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
        return null;
    }

    /**
     * <p>
     * This returns the first child element within this element with the 
     * given local name and belonging to no namespace.
     * If no elements exist for the specified name and namespace, null is 
     * returned.
     * </p>
     *
     * @param name local name of child element to match
     * @return the first matching child element, or null if not found
     */
    public Element getChild(String name) {
        return getChild(name, Namespace.NO_NAMESPACE);
    }

    /**
     * <p>
     * This adds text content to this element.  It does not replace the
     * existing content as does <code>setText()</code>.
     * </p>
     *
     * @param text <code>String</code> to add
     * @return this element modified
     */
    public Element addContent(String text) {
        if (content == null) {
            content = new ArrayList(INITIAL_ARRAY_SIZE);
        }

        int size = content.size();
        if (size > 0) {
            Object ob = content.get(size - 1);
            if (ob instanceof String) {
                text = (String)ob + text;
                content.remove(size - 1);
            }
        }
        content.add(text);
        return this;
    }

    /**
     * <p>
     * This adds element content to this element.
     * </p>
     *
     * @param element <code>Element</code> to add
     * @return this element modified
     * @throws IllegalAddException if the element you're attempting to
     *         add already has a parent element, or if you're attempting
     *         to add it as a descendent of itself (which would result in
     *         a recursive element definition!).
     */
    public Element addContent(Element element) {
        if (element.isRootElement()) {
            throw new IllegalAddException(this, element,
                "The element already has an existing parent " +
                "(the document root)");
        } else if (element.getParent() != null) {
            throw new IllegalAddException(this, element,
                "The element already has an existing parent \"" +
                element.getParent().getQualifiedName() + "\"");
        } else if (element == this) {
            throw new IllegalAddException(this, element,
                "The element cannot be added to itself");
        } else if (isAncestor(element)) {
            throw new IllegalAddException(this, element,
                "The element cannot be added as a descendent of itself");
        }

        if (content == null) {
            content = new ArrayList(INITIAL_ARRAY_SIZE);
        }

        element.setParent(this);
        content.add(element);
        return this;
    }

    // Scan ancestry looking for an element
    private boolean isAncestor(Element e) {
        Object p = parent;
        while (p instanceof Element) {
            if (p == e) {
                return true;
            }
            p = ((Element)p).getParent();
        }
        return false;
    }

    /**
     * <p>
     * This adds a processing instruction as content to this element.
     * </p>
     *
     * @param pi <code>ProcessingInstruction</code> to add
     * @return this element modified
     * @throws IllegalAddException if the given processing instruction,
     *         <code>pi</code>, already has a parent.
     */
    public Element addContent(ProcessingInstruction pi) {
        if (pi.getParent() != null) {
            throw new IllegalAddException(this, pi,
                "The PI already has an existing parent \"" +
                pi.getParent().getQualifiedName() + "\"");
        } else if (pi.getDocument() != null) {
            throw new IllegalAddException(this, pi,
                "The PI already has an existing parent " +
                "(the document root)");
        }

        if (content == null) {
            content = new ArrayList(INITIAL_ARRAY_SIZE);
        }

        content.add(pi);
        pi.setParent(this);
        return this;
    }

    /**
     * <p>
     * This adds entity content to this element.
     * </p>
     *
     * @param entity <code>EntityRef</code> to add
     * @return this element modified
     * @throws IllegalAddException if the given EntityRef already has a
     *         parent.
     */
    public Element addContent(EntityRef entity) {
        if (entity.getParent() != null) {
            throw new IllegalAddException(this, entity,
                "The entity reference already has an existing parent \"" +
                entity.getParent().getQualifiedName() + "\"");
        }

        if (content == null) {
            content = new ArrayList(INITIAL_ARRAY_SIZE);
        }

        content.add(entity);
        entity.setParent(this);
        return this;
    }

    /**
     * <p>
     * This adds a CDATA section as content to this element.
     * </p>
     *
     * @param cdata <code>CDATA</code> to add
     * @return this element modified
     */
    public Element addContent(CDATA cdata) {
        if (content == null) {
            content = new ArrayList(INITIAL_ARRAY_SIZE);
        }

        content.add(cdata);
        return this;
    }

    /**
     * <p>
     * This adds a comment as content to this element.
     * </p>
     *
     * @param comment <code>Comment</code> to add
     * @return this element modified
     * @throws IllegalAddException if the given Comment already has a
     *         parent.
     */
    public Element addContent(Comment comment) {
        if (comment.getParent() != null) {
            throw new IllegalAddException(this, comment,
                "The comment already has an existing parent \"" +
                comment.getParent().getQualifiedName() + "\"");
        } else if (comment.getDocument() != null) {
            throw new IllegalAddException(this, comment,
                "The comment already has an existing parent " +
                "(the document root)");
        }

        if (content == null) {
            content = new ArrayList(INITIAL_ARRAY_SIZE);
        }

        content.add(comment);
        comment.setParent(this);
        return this;
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
        if (content == null) {
            return false;
        }

        String uri = ns.getURI();
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
        if (content == null) {
            return false;
        }

        String uri = ns.getURI();
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
     * This removes all child elements.  Returns true if any were removed.
     * </p>
     *
     * @return whether deletion occurred
     */
    public boolean removeChildren() {
        boolean deletedSome = false;
        if (content != null) {
            Iterator i = content.iterator();
            while (i.hasNext()) {
                Object obj = i.next();
                if (obj instanceof Element) {
                    Element element = (Element)obj;
                    i.remove();
                    element.setParent(null);
                    deletedSome = true;
                }
            }
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
        if (attributes == null) {
            attributes = new ArrayList(INITIAL_ARRAY_SIZE);
        }

        PartialList atts = new PartialList(attributes, this);
        atts.addAllPartial(attributes);
        return atts;
    }

    /**
     * <p>
     * This returns the attribute for this element with the given name
     * and within no namespace.  
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
     * and within the given Namespace.  
     * </p>
     *
     * @param name name of the attribute to return
     * @param ns <code>Namespace</code> to search within
     * @return attribute for the element
     */
    public Attribute getAttribute(String name, Namespace ns) {
        if (attributes == null) {
            return null;
        }        

        String uri = ns.getURI();
        Iterator i = attributes.iterator();
        while (i.hasNext()) {
            Attribute att = (Attribute)i.next();
            if ((att.getNamespaceURI().equals(uri)) &&
                (att.getName().equals(name))) {
                return att;
            }
        }

        // If we got here, nothing found
        return null;
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
     * and within the given Namespace, null if there is no such attribute, and
     * the empty string if the attribute value is empty.
     * </p>
     *
     * @param name name of the attribute whose valud is to be returned
     * @param ns <code>Namespace</code> to search within
     * @return the named attribute's value, or null if no such attribute
     */
    public String getAttributeValue(String name, Namespace ns) {
        Attribute attrib = getAttribute(name, ns);
        if (attrib == null) {
            return null;
        } else {
            return attrib.getValue();
        }
    }

    /**
     * <p>
     * This sets all the attributes for this element to be those
     * in the given <code>List</code>; all existing attributes are removed.
     * </p>
     *
     * @param attributes <code>List</code> of attributes to set
     * @return this element modified
     */
    public Element setAttributes(List attributes) {
        // XXX Verify attributes are all parentless first
        this.attributes = attributes;
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
        if (attribute.getParent() != null) {
            throw new IllegalAddException(this, attribute,
                "The attribute already has an existing parent \"" +
                attribute.getParent().getQualifiedName() + "\"");
        }

        // Performance optimizing flag to help determine if we need to 
        // bother removing an old attribute
        boolean preExisting = false;

        // Verify the attribute's namespace prefix doesn't collide with
        // another attribute prefix or this element's prefix.
        // This is unfortunately pretty heavyweight but we don't need to do it
        // for attributes without a namespace, and a little testing shows no
        // real difference in build times anyway.
        String prefix = attribute.getNamespace().getPrefix();
        if (!prefix.equals("")) {
          String uri = attribute.getNamespace().getURI();
          if (prefix.equals(getNamespacePrefix()) && 
                !uri.equals(getNamespaceURI())) {
              throw new IllegalAddException(this, attribute,
                "The attribute namespace prefix \"" + prefix + 
                "\" collides with the element namespace prefix");
          }
          if (additionalNamespaces != null && additionalNamespaces.size() > 0) {
              Iterator itr = additionalNamespaces.iterator();
              while (itr.hasNext()) {
                  Namespace ns = (Namespace) itr.next();
                  if (prefix.equals(ns.getPrefix()) && 
                        !uri.equals(ns.getURI())) {
                      throw new IllegalAddException(this, attribute,
                        "The attribute namespace prefix \"" + prefix +
                        "\" collides with a namespace declared by the element");
                  }
              }
          }
          if (attributes != null && attributes.size() > 0) {
              Iterator itr = attributes.iterator();
              while (itr.hasNext()) {
                  Attribute att = (Attribute)itr.next();
                  Namespace ns = att.getNamespace();
                  // Keep track if we have an existing attribute
                  if (attribute.getName().equals(att.getName()) &&
                        ns.getURI().equals(att.getNamespaceURI())) {
                      preExisting = true;
                  }

                  if (prefix.equals(ns.getPrefix()) && 
                        !uri.equals(ns.getURI())) {
                      throw new IllegalAddException(this, attribute,
                          "The attribute namespace prefix \"" + prefix +
                          "\" collides with another attribute namespace on " +
                          "the element");
                  }
              }
          }
        }
  
        if (attributes == null) {
            attributes = new ArrayList(INITIAL_ARRAY_SIZE);
        }
        else if (preExisting) {
            // Remove any pre-existing attribute
            removeAttribute(attribute.getName(), attribute.getNamespace());
        }

        attributes.add(attribute);
        attribute.setParent(this);
        return this;
    }

    /**
     * <p>
     * This adds an attribute to this element with the given name and value.
     * To add attributes in namespaces using addAttribute(Attribute).
     * </p>
     *
     * @param name name of the attribute to add
     * @param value value of the attribute to add
     * @return this element modified
     * @deprecated Deprecated in beta7, use setAttribute(Attribute) instead
     */
    public Element addAttribute(String name, String value) {
        return addAttribute(new Attribute(name, value));
    }

    /**
     * <p>
     * This removes the attribute with the given name and within the
     * given namespace URI.
     * </p>
     *
     * @param name name of attribute to remove
     * @param uri namespace URI of attribute to remove
     * @return whether the attribute was removed
     * @deprecated Deprecated in beta7, use removeAttribute(String, Namespace)
     *             instead.
     */
    public boolean removeAttribute(String name, String uri) {
        Iterator i = attributes.iterator();
        while (i.hasNext()) {
            Attribute att = (Attribute)i.next();
            if ((att.getNamespaceURI().equals(uri)) &&
                (att.getName().equals(name))) {
                i.remove();
                att.setParent(null);
                return true;
            }
        }
        return false;
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
        if (attributes == null) {
            return false;
        }

        String uri = ns.getURI();
        Iterator i = attributes.iterator();
        while (i.hasNext()) {
            Attribute att = (Attribute)i.next();
            if ((att.getNamespaceURI().equals(uri)) &&
                (att.getName().equals(name))) {
                i.remove();
                att.setParent(null);
                return true;
            }
        }
        return false;
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

        // name and namespace are references to imutable objects
        // so super.clone() handles them ok

        // Reference to parent is copied by super.clone() 
        // (Object.clone()) so we have to remove it
        element.parent = null;

        // Reference to content list and attribute lists are copyed by 
        // super.clone() so we set it new lists if the original had lists
        element.content = (this.content == null) ? null :
                                  new ArrayList(INITIAL_ARRAY_SIZE);
        element.attributes = (this.attributes == null) ? null :
                                  new ArrayList(INITIAL_ARRAY_SIZE);

        // Cloning attributes
        if (attributes != null) {
            for (Iterator i = attributes.iterator(); i.hasNext(); ) {
                Attribute a = (Attribute)((Attribute)i.next()).clone();
                a.setParent(element);
                element.attributes.add(a);
            }
        }

        // Cloning content
        // No need to clone CDATA or String since they're immutable
        if (content != null) {
            for (Iterator i = content.iterator(); i.hasNext(); ) {
                Object obj = i.next();
                if (obj instanceof String) {
                    element.content.add(obj);
                } else if (obj instanceof Element) {
                    Element e = (Element)((Element)obj).clone();
                    e.setParent(element);
                    element.content.add(e);
                } else if (obj instanceof Comment) {
                    Comment c = (Comment)((Comment)obj).clone();
                    c.setParent(element);
                    element.content.add(c);
                } else if (obj instanceof CDATA) {
                    element.content.add(obj);
                } else if (obj instanceof ProcessingInstruction) {
                    ProcessingInstruction p = (ProcessingInstruction)
                        ((ProcessingInstruction)obj).clone();
                    element.content.add(p);
                } else if (obj instanceof EntityRef) {
                    EntityRef e = (EntityRef)((EntityRef)obj).clone();
                    e.setParent(element);
                    element.content.add(e);
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
     * This removes the specified <code>Element</code>.
     * If the specified <code>Element</code> is not a child of
     * this <code>Element</code>, this method does nothing.
     * </p>
     *
     * @param child <code>Element</code> to delete
     * @return whether deletion occurred
     */
    public boolean removeContent(Element element) {
        if (content == null) {
            return false;
        }

        if (content.remove(element)) {
            element.setParent(null);
            return true;
        } else {
            return false;
        }
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
        if (content == null) {
            return false;
        }
        if (content.remove(pi)) {
            pi.setParent(null);
            return true;
        } else {
            return false;
        }
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
        if (content == null) {
            return false;
        }
        if (content.remove(comment)) {
            comment.setParent(null);
            return true;
        } else {
            return false;
        }
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
     */
    public boolean removeContent(CDATA cdata) {
        if (content == null) {
            return false;
        }
        if (content.remove(cdata)) {
            return true;
        } else {
            return false;
        }
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
        if (content == null) {
            return false;
        }
        if (content.remove(entity)) {
            entity.setParent(null);
            return true;
        } else {
            return false;
        }
    }

    /**
     * <p>
     *  This creates a copy of this <code>Element</code>, with the new
     *    name specified, and in the specified <code>{@link Namespace}</code>.
     * </p>
     *
     * @param name <code>String</code> name of new <code>Element</code> copy.
     * @param ns <code>Namespace</code> to put copy in.
     * @return <code>Element</code> copy of this <code>Element</code>.
     *
     * @deprecated Deprecated in beta7, use clone(), setName(), and
     * setNamespace() instead
     */
    public Element getCopy(String name, Namespace ns) {
        Element clone = (Element)clone();
        clone.namespace = ns;
        clone.name = name;

        return clone;
    }

    /**
     * <p>
     *  This creates a copy of this <code>Element</code>, with the new
     *    name specified, and in no namespace.
     * </p>
     *
     * @param name <code>String</code> name of new <code>Element</code> copy.
     *
     * @deprecated Deprecated in beta7, use clone() and setName() instead
     */
    public Element getCopy(String name) {
        return getCopy(name, Namespace.NO_NAMESPACE);
    }

    /**
     * <p>
     * This adds an attribute to this element.  Any existing attribute with
     * the same name and namespace URI is removed.  (TODO: Code the
     * replacement logic.)
     * </p>
     *
     * @param attribute <code>Attribute</code> to add
     * @return this element modified
     * @throws IllegalAddException if the given attribute already exists
     *         within this element.
     *
     * @deprecated Deprecated in beta7, use setAttribute(Attribute) instead
     */
    public Element addAttribute(Attribute attribute) {
        if (getAttribute(attribute.getName(), attribute.getNamespace())
                 != null) {
            throw new IllegalAddException(
                this, attribute, "Duplicate attributes are not allowed");
        }

        if (attribute.getParent() != null) {
            throw new IllegalAddException(this, attribute,
                "The attribute already has an existing parent \"" +
                attribute.getParent().getQualifiedName() + "\"");
        }

        // XXX Should verify attribute ns prefix doesn't collide with
        // another attribute prefix or this element's prefix

        if (attributes == null) {
            attributes = new ArrayList(INITIAL_ARRAY_SIZE);
        }

        attributes.add(attribute);
        attribute.setParent(this);
        return this;
    }

    /**
     * <p>
     *  This will return the <code>Element</code> in XML format,
     *    usable in an XML document.
     * </p>
     *
     * @return <code>String</code> - the serialized form of the
     *         <code>Element</code>.
     * @throws RuntimeException always! This method is not yet
     *         implemented. It is also deprecated as of beta 7.
     *
     * @deprecated Deprecated in beta7, use XMLOutputter.outputString(Element)
     * instead
     */
    public final String getSerializedForm() {
        throw new RuntimeException(
          "Element.getSerializedForm() is not yet implemented");
    }

}
