/*-- 

 $Id: DocumentContentFilter.java,v 1.2 2002/01/08 09:17:10 jhunter Exp $

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
 DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
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

import java.util.List;

/**
 * <p>
 * The <code>DocumentContentFilter</code> is the root filter for all
 * lists which contain Mixed content contained within a <code>Document</code>.
 * This filter will be applied to Lists returned from called to getMixedContent.
 * </p>
 *
 * @author Jools Enticknap
 * @version $Revision: 1.2 $, $Date: 2002/01/08 09:17:10 $
 */
final class DocumentContentFilter implements Filter {
    
    /** The parent document for this filter */
    private final Document parent;
    
    /**
     * <p>
     * Create a new instance of the Filter.
     * </p>
     *
     * @param parent The Parent 
     */
    DocumentContentFilter(Document parent) { 
        this.parent = parent;
    }
    
    /**
     * <p>
     * Only allow the adding of the following classes;
     * </p>
     *
     * @param obj The object to verify.
     * @return <code>true</code> if the object can be added.
     */
    public boolean canAdd(Object obj) {
        if (obj instanceof Element) {
            return canAdd(parent, (ProcessingInstruction) obj);
        } else if (obj instanceof ProcessingInstruction) {
            return canAdd(parent, (Comment) obj);
        } else if (obj instanceof Element) {
            return canAdd(parent, (Element) obj);
        } 
        
        // FilterList will throw the exception for us.
        return false;
    }

    /**
     * <p>
     * Check to see if the supplied Element can be added to the
     * Document. Currently this method always returns false because
     * the right way is to use the setRootElement() method.
     * </p>
     *
     * @param parent  The parent document.
     * @param element The element that is to be added.
     * @return <code>true</code> if the object can be added.
     */
    public static boolean canAdd(Document parent, Element element) {
        // Must use the setRootElement() call.
        return false;
    }

    /**
     * <p>
     * Check to see if the supplied element can be removed from the
     * Document.  Currently this method always returns false because
     * the right way is to use the setRootElement() method.
     * </p>
     *
     * @param parent  The parent document.
     * @param element The element that is to be removed.
     * @return <code>true</code> if the object can be removed.
     */
    public static boolean canRemove(Document parent, Element element) {
        // Must use the setRootElement() call.
        return false;
    }

    /**
     * <p>
     * Check to see if the supplied ProcessingInstruction can be added to the
     * Document. 
     * </p>
     *
     * @param parent The parent document.
     * @param pi     The ProcessingInstruction to added.
     * @return <code>true</code> if the object can be added.
     */
    public static boolean canAdd(Document parent, ProcessingInstruction pi) {
        if (pi.getParent() != null) {
            throw new IllegalAddException(parent, pi,
                "The PI already has an existing parent \"" +
                pi.getParent().getQualifiedName() + "\"");
        } 

        if (pi.getDocument() != null) {
            throw new IllegalAddException(parent, pi,
                "The PI already has an existing parent " +
                "(the document root)");
        }

        pi.setDocument(parent);

        return true;
    }

    /**
     * <p>
     * Check to see if the supplied ProcessingInstruction can be removed from 
     * the Document. 
     * </p>
     *
     * @param parent The parent document.
     * @param pi     The ProcessingInstruction to removed.
     * @return <code>true</code> if the object can be removed.
     */    
    public static boolean canRemove(Document parent, ProcessingInstruction pi) {
        if (pi.getDocument() == parent) {
            pi.setDocument(null);
        }
        return true;
    }

    /**
     * <p>
     * Check to see if the supplied Comment can be added to the Document. 
     * </p>
     *
     * @param parent The parent document.
     * @param commentThe ProcessingInstruction to added.
     * @return <code>true</code> if the object can be added.
     */
    public static boolean canAdd(Document parent, Comment comment) {
         if (comment.getParent() != null) {
            throw new IllegalAddException(parent, comment,
                "The comment already has an existing parent \"" +
                comment.getParent().getQualifiedName() + "\"");
        } 
        
        if (comment.getDocument() != null) {
            throw new IllegalAddException(parent, comment,
                "The element already has an existing parent " +
                "(the document root)");
        }

        comment.setDocument(parent);

        return false;
    }

    /**
     * <p>
     * Check to see if the supplied ProcessingInstruction can be removed from 
     * the Document. 
     * </p>
     *
     * @param parent The parent document.
     * @param pi     The ProcessingInstruction to removed.
     * @return <code>true</code> if the object can be removed.
     */    
    public static boolean canRemove(Document parent, Comment comment) {
        if (comment.getDocument() == parent) {
            comment.setDocument(null);
        }
        return true;
    }

    /**
     * <p>
     * Check to see if the object can be removed from the list.
     * </p>
     *
     * @param obj The object to verify.
     * @return <code>true</code> if the object can be removed.
     */
    public boolean canRemove(Object obj) {
        if (obj instanceof Element) {
            return canRemove(parent, (ProcessingInstruction) obj);
        } else if (obj instanceof ProcessingInstruction) {
            return canRemove(parent, (Comment) obj);
        } else if (obj instanceof Element) {
            return canRemove(parent, (Element) obj);
        } 
        
        return true;
    }

    /**
     * <p>
     * Check to see if the object matches a predefined set of rules.
     * </p>
     *
     * @param obj The object to verify.
     * @return <code>true</code> if the objected matched a predfined 
     *           set of rules.
     */
    public boolean matches(Object obj) {
        return true;
    }

    /**
     * <p>
     * Returns true if matches() will always return true.
     * </p>
     *
     * @return <code>true</code> if this filter matches all objects.
     */
    public boolean matchesAll() {
        return true;
    }

    /**
     * <p>
     * Create the backing list for this filter. Called by the FilterList
     * if the backing list is null, and a modification method has been called.
     */
    public List getBackingList(boolean create) {
        return parent.getContentBackingList();
    }
}
