/*--

 $Id: Document.java,v 1.55 2002/03/28 11:08:12 jhunter Exp $

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
import java.util.*;

import org.jdom.filter.Filter;

/**
 * <p>
 * <code>Document</code> defines behavior for an XML Document, modeled
 *   in Java.  Methods allow access to the root element as well
 *   as processing instructions and other document-level information.
 * </p>
 *
 * @author Brett McLaughlin
 * @author Jason Hunter
 * @author Jools Enticknap
 * @author Bradley S. Huffman
 * @version $Revision: 1.55 $, $Date: 2002/03/28 11:08:12 $
 */
public class Document implements Serializable, Cloneable {

    private static final String CVS_ID =
      "@(#) $RCSfile: Document.java,v $ $Revision: 1.55 $ $Date: 2002/03/28 11:08:12 $ $Name:  $";

    /**
     * This <code>Document</code>'s
     * <code>{@link Comment}</code>s,
     * <code>{@link ProcessingInstruction}</code>s and
     * the root <code>{@link Element}</code>.
     */
    protected ContentList content = new ContentList(this);

    /** The <code>{@link DocType}</code> declaration. */
    protected DocType docType;

    /**
     * <p>
     * Creates a new empty document.  A document must have a root element,
     * so this document will not be well-formed and accessor methods will
     * throw an IllegalStateException if this document is accessed before a
     * root element is added.  This method is most useful for build tools.
     * </p>
     */
    public Document() {}

    /**
     * <p>
     * This will create a new <code>Document</code>,
     * with the supplied <code>{@link Element}</code>
     * as the root element and the supplied
     * <code>{@link DocType}</code> declaration.
     * </p>
     *
     * @param rootElement <code>Element</code> for document root.
     * @param docType <code>DocType</code> declaration.
     */
    public Document(Element rootElement, DocType docType) {
        if (rootElement != null)
            setRootElement(rootElement);
        setDocType(docType);
    }

    /**
     * <p>
     * This will create a new <code>Document</code>,
     * with the supplied <code>{@link Element}</code>
     * as the root element, and no <code>{@link DocType}</code>
     * declaration.
     * </p>
     *
     * @param rootElement <code>Element</code> for document root
     */
    public Document(Element rootElement) {
        this(rootElement, null);
    }

    /**
     * <p>
     * This will create a new <code>Document</code>,
     * with the supplied list of content, and the supplied
     * <code>{@link DocType}</code> declaration.
     * </p>
     *
     * @param content <code>List</code> of starter content
     * @param docType <code>DocType</code> declaration.
     * @throws IllegalAddException if (1) the List contains more than
     *         one Element or objects of illegal types, or (2) if the
     *         given docType object is already attached to a document.
     */
    public Document(List newContent, DocType docType) {
        setContent(newContent);
        setDocType(docType);
    }

    /**
     * <p>
     * This will create a new <code>Document</code>,
     * with the supplied list of content, and no
     * <code>{@link DocType}</code> declaration.
     * </p>
     *
     * @param content <code>List</code> of starter content
     * @throws IllegalAddException if the List contains more than
     *         one Element or objects of illegal types.
     */
    public Document(List content) {
        this(content, null);
    }

    /**
     * <p>
     * This will return <code>true</code> if this document has a
     * root element, <code>false</code> otherwise.
     * </p>
     *
     * @return <code>true</code> if this document has a root element,
     *         <code>false</code> otherwise.
     */
    public boolean hasRootElement() {
        return (content.indexOfFirstElement() < 0) ? false : true;
    }

    /**
     * <p>
     * This will return the root <code>Element</code>
     * for this <code>Document</code>
     * </p>
     *
     * @return <code>Element</code> - the document's root element
     * @throws IllegalStateException if the root element hasn't been set
     */
    public Element getRootElement() {
        int index = content.indexOfFirstElement();
        if (index < 0) {
            throw new IllegalStateException("Root element not set"); 
        }
        return (Element) content.get(index);
    }

    /**
     * <p>
     * This sets the root <code>{@link Element}</code> for the
     * <code>Document</code>. If the document already has a root
     * element, it is replaced.
     * </p>
     *
     * @param rootElement <code>Element</code> to be new root.
     * @return <code>Document</code> - modified Document.
     * @throws IllegalAddException if the given rootElement already has
     *         a parent.
     */
    public Document setRootElement(Element rootElement) {
        int index = content.indexOfFirstElement();
        if (index < 0) {
            content.add(rootElement);
        }
        else {
            content.set(index, rootElement);
        }
        return this;
    }

    /**
     * <p>
     * Detach the root <code>{@link Element}</code> from this document.
     * </p>
     *
     * @return removed root <code>Element</code>
     */
    public Element detachRootElement() {
        int index = content.indexOfFirstElement();
        if (index < 0)
            return null;
        return (Element) removeContent(index);
    }

    // Remove Object at given index, or null if index is out of
    // range or content cannot be removed.
    private Object removeContent(int index) {
        return content.remove(index);
    }

    /**
     * <p>
     * This will return the <code>{@link DocType}</code>
     * declaration for this <code>Document</code>, or
     * <code>null</code> if none exists.
     * </p>
     *
     * @return <code>DocType</code> - the DOCTYPE declaration.
     */
    public DocType getDocType() {
        return docType;
    }

    /**
     * <p>
     * This will set the <code>{@link DocType}</code>
     * declaration for this <code>Document</code>. Note
     * that a DocType can only be attached to one Document.
     * Attempting to set the DocType to a DocType object
     * that already belongs to a Document will result in an
     * IllegalAddException being thrown.
     * </p>
     *
     * @param docType <code>DocType</code> declaration.
     * @throws IllegalAddException if the given docType is
     *   already attached to a Document.
     */
    public Document setDocType(DocType docType) {
        if (docType != null) {
            if (docType.getDocument() != null) {
                throw new IllegalAddException(this, docType,
                          "The docType already is attached to a document");
            }
            docType.setDocument(this);
        }

        if (this.docType != null) {
            this.docType.setDocument(null);
        }

        this.docType = docType;
        return this;
    }

    /**
     * <p>
     * Adds the specified PI to the document.
     * </p>
     *
     * @param pi the ProcessingInstruction to add.
     * @return <code>Document</code> this document modified.
     * @throws IllegalAddException if the given processing instruction
     *         already has a parent element.
     */
    public Document addContent(ProcessingInstruction pi) {
        content.add(pi);
        return this;
    }

    /**
     * <p>
     * This will add a comment to the <code>Document</code>.
     * </p>
     *
     * @param comment <code>Comment</code> to add.
     * @return <code>Document</code> - this object modified.
     * @throws IllegalAddException if the given comment already has a
     *         parent element.
     */
    public Document addContent(Comment comment) {
        content.add(comment);
        return this;
    }

    /**
     * <p>
     * This will return all content for the <code>Document</code>.
     * The returned list is "live" in document order and changes to it
     * affect the document's actual content.
     * </p>
     *
     * <p>
     * Sequential traversal through the List is best done with a Iterator
     * since the underlying implement of List.size() may require walking the
     * entire list.
     * </p>
     *
     * @return <code>List</code> - all Document content
     * @throws IllegalStateException if the root element hasn't been set
     */
    public List getContent() {
        if (!hasRootElement())
            throw new IllegalStateException("Root element not set"); 
        return content;
    }

    /**
     * <p>
     * Return a filtered view of this <code>Document</code>'s content.
     * </p>
     *
     * <p>
     * Sequential traversal through the List is best done with a Iterator
     * since the underlying implement of List.size() may require walking the
     * entire list.
     * </p>
     *
     * @param filter <code>Filter</code> to apply
     * @return <code>List</code> - filtered Document content
     * @throws IllegalStateException if the root element hasn't been set
     */
    public List getContent(Filter filter) {
        if (!hasRootElement())
            throw new IllegalStateException("Root element not set"); 
        return content.getView(filter);
    }
  
    /**
     * <p>
     * This sets the content of the <code>Document</code>.  The supplied
     * List should contain only objects of type <code>Element</code>,
     * <code>Comment</code>, and <code>ProcessingInstruction</code>.
     * </p>
     *
     * <p>
     * When all objects in the supplied List are legal and before the new
     * content is added, all objects in the old content will have their
     * parentage set to null (no parent) and the old content list will be
     * cleared. This has the effect that any active list (previously obtained
     * with a call to {@link #getContent}) will also
     * change to reflect the new content.  In addition, all objects in the
     * supplied List will have their parentage set to this document, but the
     * List itself will not be "live" and further removals and additions will
     * have no effect on this document content. If the user wants to continue
     * working with a "live" list, then a call to setContent should be
     * followed by a call to {@link #getContent} to
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
     * @parem newContent <code>List</code> of content to set
     * @return this document modified
     * @throws IllegalAddException if the List contains objects of
     *         illegal types.
     */
    public Document setContent(List newContent) {
        content.clearAndSet(newContent);
        return this;
    }

    /**
     * <p>
     * This removes the specified <code>ProcessingInstruction</code>.
     * If the specified <code>ProcessingInstruction</code> is not a child of
     * this <code>Document</code>, this method does nothing.
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
     * this <code>Document</code>, this method does nothing.
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
     * This returns a <code>String</code> representation of the
     * <code>Document</code>, suitable for debugging. If the XML
     * representation of the <code>Document</code> is desired,
     * {@link org.jdom.output.XMLOutputter#outputString(Document)}
     * should be used.
     * </p>
     *
     * @return <code>String</code> - information about the
     *         <code>Document</code>
     */
    public String toString() {
        StringBuffer stringForm = new StringBuffer()
            .append("[Document: ");

        if (docType != null) {
            stringForm.append(docType.toString())
                      .append(", ");
        } else {
            stringForm.append(" No DOCTYPE declaration, ");
        }

        Element rootElement = getRootElement();
        if (rootElement != null) {
            stringForm.append("Root is ")
                      .append(rootElement.toString());
        } else {
            stringForm.append(" No root element"); // shouldn't happen
        }

        stringForm.append("]");

        return stringForm.toString();
    }

    /**
     * <p>
     * This tests for equality of this <code>Document</code> to the supplied
     * <code>Object</code>.
     * </p>
     *
     * @param ob <code>Object</code> to compare to.
     * @return <code>boolean</code> - whether the <code>Document</code> is
     *         equal to the supplied <code>Object</code>.
     */
    public final boolean equals(Object ob) {
        return (ob == this);
    }

    /**
     * <p>
     * This returns the hash code for this <code>Document</code>.
     * </p>
     *
     * @return <code>int</code> - hash code.
     */
    public final int hashCode() {
        return super.hashCode();
    }

    /**
     * <p>
     * This will return a deep clone of this <code>Document</code>.
     * </p>
     *
     * @return <code>Object</code> - clone of this <code>Document</code>.
     */
    public Object clone() {
        Document doc = null;

        try {
            doc = (Document) super.clone();
        } catch (CloneNotSupportedException ce) {
            // Can't happen
        }

        if (docType != null) {
            doc.docType = (DocType)docType.clone();
        }

        // The clone has a reference to this object's content list, so
        // owerwrite with a empty list
        doc.content = new ContentList(doc);

        // Add the cloned content to clone

        for (int i = 0; i < content.size(); i++) {
            Object obj = content.get(i);
            if (obj instanceof Element) {
                Element element = (Element)((Element)obj).clone();
                doc.content.add(element);
            }
            else if (obj instanceof Comment) {
                Comment comment = (Comment)((Comment)obj).clone();
                doc.content.add(comment);
            }
            else if (obj instanceof ProcessingInstruction) {
                ProcessingInstruction pi = (ProcessingInstruction)
                           ((ProcessingInstruction)obj).clone();
                doc.content.add(pi);
            }
        }

        return doc;
    }
}
