/*-- 

 $Id: ElementContentFilter.java,v 1.3 2001/12/11 07:32:04 jhunter Exp $

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
 * The <code>ElementContentFilter</code> contains the rules for all
 * of the visible elements contained in a <code>Element</code>.
 * </p>
 *
 * @author Jools Enticknap
 * @version 0.01
 */
final class ElementContentFilter implements Filter {
    
    /** The parent of this Filter */
    private final Element parent;
    
    /**
     * <p>
     * Create a new instance of the Filter.
     * </p>
     *
     * @param parent The parent Element.
     */
    ElementContentFilter(Element parent) { 
        this.parent = parent;
    }
    
    /**
     * <p>
     * Check to see of the supplied object can be added to the list.
     * If it can be added, the object will have it parent set to 
     * reflect the change in parentage, if applicable.
     * The <code>FilterList</code> can then add the object to the 
     * backing list.
     * </p>
     *
     * @param obj The object to verify.
     * @return <code>true</code> if the object can be added.
     */
    public boolean canAdd(Object obj) {
        if (obj instanceof Element) {
            return canAdd(parent, (Element) obj);
        } else if (obj instanceof Text) {
            return canAdd(parent, (Text) obj);
        } else if (obj instanceof Comment) {
            return canAdd(parent, (Comment) obj);
        } else if (obj instanceof ProcessingInstruction) {
            return canAdd(parent, (ProcessingInstruction) obj);
        } else if (obj instanceof CDATA) {
            return canAdd(parent, (CDATA) obj);
        } else if (obj instanceof EntityRef) {
            return canAdd(parent, (EntityRef) obj);
        } 
        
        // FilterList will throw the exception for us.
        return false;
    }

    /**
     * <p>
     * Check to see if the object can be removed from the list.
     * If the object is a child of the parent it will be removed 
     * and its parent set to null, if applicable.
     * The <code>FilterList</code> can then add the object to the 
     * backing list.
     * </p>
     *
     * @param obj The object to verify.
     * @return <code>true</code> if the object can be removed.
     */
    public boolean canRemove(Object obj) {
        if (obj instanceof Element) {
            return canRemove(parent, (Element) obj);
        } else if (obj instanceof Text) {
            return canRemove(parent, (Text) obj);
        } else if (obj instanceof Comment) {
            return canRemove(parent, (Comment) obj);
        } else if (obj instanceof ProcessingInstruction) {
            return canRemove(parent, (ProcessingInstruction) obj);
        } else if (obj instanceof CDATA) {
            return canRemove(parent, (CDATA) obj);
        } else if (obj instanceof EntityRef) {
            return canRemove(parent, (EntityRef) obj);
        } 
        
        return true;
    }

    /**
     * <p>
     * Check the the supplied <code>Element</code> can be added to
     * the supplied parent <code>Element</code>.
     * </p>
     *
     * @param parent  The parent Element.
     * @param element The object to verify.
     * @return <code>true</code> if the object can be added.
     */    
    public static boolean canAdd(Element parent, Element element) {
        if (element.isRootElement()) {
            throw new IllegalAddException(parent, element,
                "The element already has an existing parent " +
                "(the document root)");
        } else if (element.getParent() != null) {
            throw new IllegalAddException(parent, element,
                "The element already has an existing parent \"" +
                element.getParent().getQualifiedName() + "\"");
        } else if (element == parent) {
            throw new IllegalAddException(parent, element,
                "The element cannot be added to itself");
        } else if (parent.isAncestor(element)) {
            throw new IllegalAddException(parent, element,
                "The element cannot be added as a descendent of itself");
        }

        element.setParent(parent);

        return true;
    }

    /**
     * <p>
     * Check the the supplied <code>Element</code> can be removed from
     * its parent.
     * </p>
     *
     * @param parent  The parent Element.
     * @param element The object to verify.
     * @return <code>true</code> if the object can be removed.
     */    
    public static boolean canRemove(Element parent, Element element) {
        if (parent == element.getParent()) {
            element.setParent(null);
        }
        
        return true;
    }

    /**
     * <p>
     * Ensure that the string can be added to the list.
     * </p>
     *
     * @param parent The parent Element.
     * @param text   The Text to verify.
     * @return <code>true</code> if the Text can be added.
     */    
    public static boolean canAdd(Element parent, Text text) {
        if (text.getParent() != null) {
            throw new IllegalAddException(parent, text,
                "The Text node already has an existing parent \"" +
                text.getParent().getQualifiedName() + "\"");
        }

        text.setParent(parent);
        return true;
    }

    /**
     * <p>
     * Ensure that the Text can be removed from the list.
     * </p>
     *
     * @param parent The parent Element.
     * @param text   The Text to verify.
     * @return <code>true</code> if the Text can be removed.
     */    
    public static boolean canRemove(Element parent, Text text) {
        if (parent == text.getParent()) {
            text.setParent(null);
        }

        return true;
    }
    
    /**
     * <p>
     * Ensure that the Comment can be added to the list.
     * </p>
     *
     * @param parent  The parent Element.
     * @param comment The Comment to verify.
     * @return <code>true</code> if the Comment can be added.
     */    
    public static boolean canAdd(Element parent, Comment comment) {
        if (comment.getParent() != null) {
            throw new IllegalAddException(parent, comment,
                "The comment already has an existing parent \"" +
                comment.getParent().getQualifiedName() + "\"");
        } else if (comment.getDocument() != null) {
            throw new IllegalAddException(parent, comment,
                "The comment already has an existing parent " +
                "(the document root)");
        }
        
        comment.setParent(parent);

        return true;
    }    

    /**
     * <p>
     * Ensure that the Comment can be removed from the list.
     * </p>
     *
     * @param parent  The parent Element.
     * @param comment The Comment to verify.
     * @return <code>true</code> if the Comment can be removed.
     */    
    public static boolean canRemove(Element parent, Comment comment) {
        if (parent == comment.getParent()) {
            comment.setParent(null);
        }

        return true;
    }

    /**
     * <p>
     * Ensure that the ProcessingInstruction can be added to the list.
     * </p>
     *
     * @param parent The parent Element.
     * @param pi     The ProcessingInstruction to add.
     * @return <code>true</code> if the ProcessingInstruction can be added.
     */    
    public static boolean canAdd(Element parent, ProcessingInstruction pi) {
        if (pi.getParent() != null) {
            throw new IllegalAddException(parent, pi,
                "The PI already has an existing parent \"" +
                pi.getParent().getQualifiedName() + "\"");
        } else if (pi.getDocument() != null) {
            throw new IllegalAddException(parent, pi,
                "The PI already has an existing parent " +
                "(the document root)");
        }

        pi.setParent(parent);
        return true;
    }

    /**
     * <p>
     * Ensure that the ProcessingInstruction can be removed from the list.
     * </p>
     *
     * @param parent The parent Element.
     * @param pi     The ProcessingInstruction to add.
     * @return <code>true</code> if the ProcessingInstruction can be removed.
     */    
    public static boolean canRemove(Element parent, ProcessingInstruction pi) {
        if (parent == pi.getParent()) {
            pi.setParent(null);
        }

        return true;
    }

    /**
     * <p>
     * Ensure that the CDATA can be added to the list.
     * </p>
     *
     * @param parent  The parent Element.
     * @param cdata   The CDATA to verify.
     * @return <code>true</code> if the CDATA can be added.
     */    
    public static boolean canAdd(Element parent, CDATA cdata) {
        if (cdata.getParent() != null) {
            throw new IllegalAddException(parent, cdata,
                "The CDATA section already has an existing parent \"" +
                cdata.getParent().getQualifiedName() + "\"");
        }

        cdata.setParent(parent);
        return true;
    }

    /**
     * <p>
     * Ensure that the CDATA can be removed from the list.
     * </p>
     *
     * @param parent  The parent Element.
     * @param cdata   The CDATA to verify.
     * @return <code>true</code> if the CDATA can be removed.
     */    
    public static boolean canRemove(Element parent, CDATA cdata) {
        if (parent == cdata.getParent()) {
            cdata.setParent(null);
        }

        return true;
    }

    /**
     * <p>
     * Ensure that the EntityRef can be added to the list.
     * </p>
     *
     * @param parent  The parent Element.
     * @param entity  The Entity to verify.
     * @return <code>true</code> if the EntityRef can be added.
     */    
    public static boolean canAdd(Element parent, EntityRef entity) {
        if (entity.getParent() != null) {
            throw new IllegalAddException(parent, entity,
                "The entity reference already has an existing parent \"" +
                entity.getParent().getQualifiedName() + "\"");
        }

        entity.setParent(parent);
        return true;
    }

    
    /**
     * <p>
     * Ensure that the EntityRef can be added to the list.
     * </p>
     *
     * @param parent  The parent Element.
     * @param entity  The Entity to verify.
     * @return <code>true</code> if the EntityRef can be removed.
     */    
    public static boolean canRemove(Element parent, EntityRef entity) {
        if (parent == entity.getParent()) {
            entity.setParent(null);
        }

        return true;
    }    

    /**
     * <p>
     * Check to see if the object is one which we allow in this type of
     * list.
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
        return parent.getContentBackingList(create);
    }
}
