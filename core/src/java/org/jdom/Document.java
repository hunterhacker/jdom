/*-- 

 $Id: Document.java,v 1.33 2001/04/13 03:16:30 jhunter Exp $

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

/**
 * <p>
 * <code>Document</code> defines behavior for an XML Document, modeled
 *   in Java.  Methods allow the user to the root element as well
 *   as processing instructions and other document-level information.
 * </p>
 *
 * @author Brett McLaughlin
 * @author Jason Hunter
 * @version 1.0
 */
public class Document implements Serializable, Cloneable {

    /**
     * This <code>Document</code>'s
     *   <code>{@link Comment}</code>s,  
     *   <code>{@link ProcessingInstruction}</code>s and
     *   the root <code>{@link Element}</code>
     */
    protected List content = new LinkedList();

    /** The <code>{@link DocType}</code> declaration */
    protected DocType docType;

    /**
     * <p>
     * Default, no-args constructor for implementations
     *   to use if needed.
     * </p>
     */
    protected Document() {}

    /**
     * <p>
     * This will create a new <code>Document</code>,
     *   with the supplied <code>{@link Element}</code>
     *   as the root element and the supplied
     *   <code>{@link DocType}</code> declaration.
     * </p>
     *
     * @param rootElement <code>Element</code> for document root.
     * @param docType <code>DocType</code> declaration.
     */
    public Document(Element rootElement, DocType docType) {
        setRootElement(rootElement);
        setDocType(docType);
    }

    /**
     * <p>
     * This will create a new <code>Document</code>,
     *   with the supplied <code>{@link Element}</code>
     *   as the root element, and no <code>{@link DocType}</code>
     *   declaration.
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
     *   with the supplied list of content, and the supplied 
     *   <code>{@link DocType}</code> declaration.
     * </p>
     *
     * @param content <code>List</code> of starter content
     * @param docType <code>DocType</code> declaration.
     * @throws IllegalAddException if the List contains more than
     *         one Element or objects of illegal types
     */
    public Document(List content, DocType docType) {
        setMixedContent(content);
        setDocType(docType);
    }

    /**
     * <p>
     * This will create a new <code>Document</code>,
     *   with the supplied list of content, and no 
     *   <code>{@link DocType}</code> declaration.
     * </p>
     *
     * @param content <code>List</code> of starter content
     * @throws IllegalAddException if the List contains more than
     *         one Element or objects of illegal types
     */
    public Document(List content) {
        this(content, null);
    }

    /**
     * <p>
     * This will return the root <code>Element</code>
     *   for this <code>Document</code>
     * </p>
     *
     * @return <code>Element</code> - the document's root element
     */
    public Element getRootElement() {
        Iterator itr = content.iterator();
        while (itr.hasNext()) {
            Object obj = itr.next();
            if (obj instanceof Element) {
                return (Element) obj;
            }
        }

        return null;  // sometimes happens during internal manipulations
    }

    /**
     * <p>
     * This sets the root <code>{@link Element}</code> for the
     *   <code>Document</code>.
     * </p>
     *
     * @param rootElement <code>Element</code> to be new root.
     * @return <code>Document</code> - modified Document.
     */
    public Document setRootElement(Element rootElement) {
        // XXX Remove this special case pronto
        if (rootElement == null) return this;

        // Conduct some sanity checking
        if (rootElement.isRootElement()) {
            throw new IllegalAddException(this, rootElement,
                "The element already has an existing parent " +
                "(the document root)");
        }
        else if (rootElement.getParent() != null) {
            throw new IllegalAddException(this, rootElement,
                "The element already has an existing parent \"" +
                rootElement.getParent().getQualifiedName() + "\"");
        }

        boolean hadRoot = false;

        // Find the current root and replace it
        ListIterator itr = content.listIterator();
        while (itr.hasNext()) {
            Object obj = itr.next();
System.out.println("examining obj " + obj);
            if (obj instanceof Element) {
                Element departingRoot = (Element) obj;
                departingRoot.setDocument(null);
System.out.println("removing doc on " + departingRoot);
                itr.set(rootElement);  // replaces
                hadRoot = true;
            }
        }

        // No current root
        if (hadRoot == false) {
            itr.add(rootElement);  // adds at end
        }

        rootElement.setDocument(this);

        return this;
    }

    /**
     * <p>
     * This will return the <code>{@link DocType}</code>
     *   declaration for this <code>Document</code>, or
     *   <code>null</code> if none exists.
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
     *   declaration for this <code>Document</code>.
     * </p>
     *
     * @param docType <code>DocType</code> declaration.
     */
    public Document setDocType(DocType docType) {
        this.docType = docType;

        return this;
    }

    /**
     * <p>
     * This will return the list of
     *   <code>{@link ProcessingInstruction}</code>s
     *   for this <code>Document</code> located at the document level 
     *   (outside the root element).
     * The returned list is "live" and changes to it affect the
     * document's actual content.
     * </p>
     *
     * @return <code>List</code> - PIs for document.
     */
    public List getProcessingInstructions() {
        PartialList pis = new PartialList(content);

        for (Iterator i = content.iterator(); i.hasNext(); ) {
            Object obj = i.next();
            if (obj instanceof ProcessingInstruction) {
                pis.addPartial(obj);
            }
        }

        return pis;
    }

    /**
     * <p>
     * This returns the processing instructions for this
     *   <code>Document</code> located at the document level
     *   (outside the root element) which have the supplied target.
     * The returned list is "live" and changes to it affect the
     * document's actual content.
     * </p>
     *
     * @param target <code>String</code> target of PI to return.
     * @return <code>List</code> - all PIs with the specified
     *         target.
     */
    public List getProcessingInstructions(String target) {
        PartialList pis = new PartialList(content);

        for (Iterator i = content.iterator(); i.hasNext(); ) {
            Object obj = i.next();
            if (obj instanceof ProcessingInstruction) {
                if (((ProcessingInstruction)obj).getTarget().equals(target)) {
                    pis.addPartial(obj);
                }
            }
        }

        return pis;
    }

    /**
     * <p>
     * This returns the first processing instruction for this
     *   <code>Document</code> located at the document level 
     *   (outside the root element) for the supplied target, or null if
     *   no such processing instruction exists.
     * </p>
     *
     * @param target <code>String</code> target of PI to return.
     * @return <code>ProcessingInstruction</code> - the first PI
     *         with the specified target, or null if no such PI exists.
     */
    public ProcessingInstruction getProcessingInstruction(String target) {

        for (Iterator i = content.iterator(); i.hasNext(); ) {
            Object obj = i.next();
            if (obj instanceof ProcessingInstruction) {
                if (((ProcessingInstruction)obj).getTarget().equals(target)) {
                    return (ProcessingInstruction)obj;
                }
            }
        }

        // If we got here, none found
        return null;
    }

    /**
     * <p>
     * This will remove the first PI with the specified target.
     * </p>
     *
     * @param target <code>String</code> target of PI to remove.
     * @return <code>boolean</code> - whether the requested PI was removed.
     */
    public boolean removeProcessingInstruction(String target) {
        ProcessingInstruction pi = getProcessingInstruction(target);
        if (pi == null) {
            return false;
        }
        else {
            return removeContent(pi);
        }
    }

    /**
     * <p>
     * This will remove all PIs with the specified target.
     * </p>
     *
     * @param target <code>String</code> target of PI to remove.
     * @return <code>boolean</code> - whether the requested PIs were removed.
     */
    public boolean removeProcessingInstructions(String target) {
        boolean deletedSome = false;

        for (Iterator i = content.iterator(); i.hasNext(); ) {
            Object obj = i.next();
            if (obj instanceof ProcessingInstruction) {
                ProcessingInstruction pi = (ProcessingInstruction)obj;
                if (pi.getTarget().equals(target)) {
                    deletedSome = true;
                    i.remove();
                    pi.setDocument(null);
                }
            }
        }
        return deletedSome;
    }

    /**
     * <p>
     * This sets the PIs for this <code>Document</code> to those in the
     *   <code>List</code supplied (removing all other PIs).
     * </p>
     *
     * @param pis <code>List</code> of PIs to use.
     * @return <code>Document</code> - this Document modified.
     */
    public Document setProcessingInstructions(List pis) {
        // XXX Sanity check List contains only parentless and documentless PIs

        List current = getProcessingInstructions();
        for (Iterator i = current.iterator(); i.hasNext(); ) {
            i.remove();
        }

        content.addAll(pis);

        return this;
    }

    /**
     * <p>
     * Adds the specified PI to the document.
     * </p>
     *
     * @param pi the PI to add.
     * @return <code>Document</code> this document modified.
     */
    public Document addContent(ProcessingInstruction pi) {
        if (pi.getParent() != null) {
            throw new IllegalAddException(this, pi,
                "The PI already has an existing parent \"" +
                pi.getParent().getQualifiedName() + "\"");
        } else if (pi.getDocument() != null) {
            throw new IllegalAddException(this, pi,
                "The PI already has an existing parent " +
                "(the document root)");
        }

        content.add(pi);
        pi.setDocument(this);
        return this;
    }

    /**
     * <p>
     * This will add a comment to the <code>Document</code>.
     * </p>
     *
     * @param comment <code>Comment</code> to add.
     * @return <code>Document</code> - this object modified.
     */
    public Document addContent(Comment comment) {
        if (comment.getParent() != null) {
            throw new IllegalAddException(this, comment,
                "The comment already has an existing parent \"" +
                comment.getParent().getQualifiedName() + "\"");
        } else if (comment.getDocument() != null) {
            throw new IllegalAddException(this, comment,
                "The element already has an existing parent " +
                "(the document root)");
        }

        content.add(comment);
        comment.setDocument(this);
        return this;
    }

    /**
     * <p>
     * This will return all content for the <code>Document</code>.
     * The returned list is "live" and changes to it affect the
     * document's actual content.
     * </p>
     *
     * @return <code>List</code> - all Document content
     */
    public List getMixedContent() {
        // XXX This should be a PartialList/FilterList
        return content;
    }

    /**
     * <p>
     * This will set all content for the <code>Document</code>.
     * The List may contain only objects of type Element, Comment, and
     * ProcessingInstruction; and only one Element that becomes the root.
     * In event of an exception the original content will be unchanged
     * and the items in the added content will be unaltered.
     * </p>
     *
     * @param content the new mixed content
     * @return the modified Document
     * @throws IllegalAddException if the List contains more than
     *         one Element or objects of illegal types
     */
    public Document setMixedContent(List mixedContent) {

        if (mixedContent == null) {
            return this;
        }

        // Save original content and create a new list
        Element oldRoot = getRootElement();
        List oldContent = content;
        content = new LinkedList();

        RuntimeException ex = null;
        boolean didRoot = false;
        int itemsAdded = 0;

        try {
            for (Iterator i = mixedContent.iterator(); i.hasNext(); ) {
                Object obj = i.next();
                if (obj instanceof Element) {
                    if (didRoot == false) {
                        setRootElement((Element)obj);
                        // Manually remove old root's doc ref, because
                        // setRootElement() can't see the old content list to
                        // do it for us
                        oldRoot.setDocument(null);
                        didRoot = true;
                    }
                    else {
                        throw new IllegalAddException(
                          "A Document may contain only one root element");
                    }
                }
                else if (obj instanceof Comment) {
                    addContent((Comment)obj);
                }
                else if (obj instanceof ProcessingInstruction) {
                    addContent((ProcessingInstruction)obj);
                }
                else {
                    throw new IllegalAddException(
                      "A Document may directly contain only objects of type " +
                      "Element, Comment, and ProcessingInstruction: " +
                      (obj == null ? "null" : obj.getClass().getName()) + 
                      " is not allowed");
                }
                itemsAdded++;
            }
    
            // Make sure they set a root element
            if (didRoot == false) {
                throw new IllegalAddException(
                    "A Document must contain a root element");
            }
        }
        catch (RuntimeException e) {
            ex = e;
        }
        finally {
            if (ex != null) {
                // Restore the original state and parentage and throw
                content = oldContent;
                oldRoot.setDocument(this);
                // Unmodify all modified elements.  DO NOT change any later
                // elements tho because they may already have parents!
                Iterator itr = mixedContent.iterator();
                while (itemsAdded-- > 0) {
                    Object obj = itr.next();
                    if (obj instanceof Element) {
                        ((Element)obj).setDocument(null);
                    }
                    else if (obj instanceof Comment) {
                        ((Comment)obj).setDocument(null);
                    }
                    else if (obj instanceof ProcessingInstruction) {
                        ((ProcessingInstruction)obj).setDocument(null);
                    }
                }
                throw ex;
            }
        }

        // Remove parentage on the old content
        Iterator itr = oldContent.iterator();
        while (itr.hasNext()) {
            Object obj = itr.next();
            if (obj instanceof Element) {
                ((Element)obj).setDocument(null);
            }
            else if (obj instanceof Comment) {
                ((Comment)obj).setDocument(null);
            }
            else if (obj instanceof ProcessingInstruction) {
                ((ProcessingInstruction)obj).setDocument(null);
            }
        }

        return this;
    }

    /**
     * <p>
     *  This returns a <code>String</code> representation of the
     *    <code>Document</code>, suitable for debugging. If the XML
     *    representation of the <code>Comment</code> is desired,
     *    <code>{@link #getSerializedForm}</code> should be used.
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
                      .append(" ");
        } else {
            stringForm.append(" No DOCTYPE declaration. ");
        }

        Element rootElement = getRootElement();
        if (rootElement != null) {
            stringForm.append("Root - ")
                      .append(rootElement.toString());
        } else {
            stringForm.append(" No Root Element.");
        }

        stringForm.append("]");

        return stringForm.toString();
    }

    /**
     * <p>
     *  This will return the <code>Document</code> in XML format,
     *    usable in an XML document.
     * </p>
     *
     * @return <code>String</code> - the serialized form of the
     *         <code>Document</code>.
     */
    public final String getSerializedForm() {
        throw new RuntimeException(
          "Document.getSerializedForm() is not yet implemented");
    }

    /**
     * <p>
     *  This tests for equality of this <code>Document</code> to the supplied
     *    <code>Object</code>.
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
     *  This returns the hash code for this <code>Document</code>.
     * </p>
     *
     * @return <code>int</code> - hash code.
     */
    public final int hashCode() {
        return super.hashCode();
    }

    /**
     * <p>
     *  This will return a deep clone of this <code>Document</code>.
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

        // The clone has a reference to this object's content list, so
        // owerwrite with a empty list
        doc.content = new LinkedList();

        // Add the cloned content to clone

        for (Iterator i = content.iterator(); i.hasNext(); ) {
            Object obj = i.next();
            if (obj instanceof Element) {
                doc.setRootElement((Element)((Element)obj).clone());
            }
            else if (obj instanceof Comment) {
                doc.addContent((Comment)((Comment)obj).clone());
            }
            else if (obj instanceof ProcessingInstruction) {
                doc.addContent((ProcessingInstruction)
                          ((ProcessingInstruction)obj).clone());
            }
        }

        if (docType != null) {
            doc.docType = (DocType)docType.clone();
        }

        return doc;
    }

    /**
     * <p>
     * This removes the specified <code>ProcessingInstruction</code>.
     * </p>
     *
     * @param child <code>ProcessingInstruction</code> to delete
     * @return whether deletion occurred
     */
    public boolean removeContent(ProcessingInstruction pi) {
        if (content == null) {
            return false;
        }
        return content.remove(pi);
    }

    /**
     * <p>
     * This removes the specified <code>Comment</code>.
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
            comment.setDocument(null);
            return true;
        } else {
            return false;
        }
    }


    /**
     * @deprecated Deprecated in beta6, use setRootElement() instead
     */
    public Document addContent(Element element) {
        if (getRootElement() != null) {
            throw new IllegalAddException(
                this, element, "The document already has a root element");
        }
        setRootElement(element);

        return this;
    }
}

