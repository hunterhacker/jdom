/*-- 

 $Id: ContentFilter.java,v 1.2 2002/04/13 04:16:42 jhunter Exp $

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

package org.jdom.filter;

import org.jdom.*;

/**
 * <p>
 * <code>ContentFilter</code> is a general purpose <code>Filter</code>
 * representing all legal JDOM objects and allows the ability to set
 * and unset the visiblity of these objects.  Filtering is accomplished by
 * way of a filtering mask in which each bit represents whether a JDOM
 * object is visible or not.
 * </p>
 *
 * <p>
 * For example to view all Text and CDATA nodes in the content of element x.
 * <pre><code>
 *      Filter filter = new ContentFilter(ContentFilter.TEXT |
 *                                        ContentFilter.CDATA);
 *      List content = x.getContent(filter);
 * </code></pre>
 * </p>
 *
 * <p>
 * For those who don't like bit-masking, set methods are provided as an 
 * alternative.  For example to allow everything except Comment nodes.
 * <pre><code>
 *      Filter filter =  new ContentFilter();
 *      filter.setCommentVisible(false);
 *      List content = x.getContent(filter);
 * </code></pre>
 * </p>
 *
 * <p>
 * The default is to allow all valid JDOM objects.
 * </p>
 *
 * @author Bradley S. Huffman
 * @version $Revision: 1.2 $, $Date: 2002/04/13 04:16:42 $
 */
public class ContentFilter implements Filter {

    private static final String CVS_ID = 
      "@(#) $RCSfile: ContentFilter.java,v $ $Revision: 1.2 $ $Date: 2002/04/13 04:16:42 $ $Name:  $";

    /** Mask for JDOM <code>Element</code> objects */
    public static final int ELEMENT   = 1;

    /** Mask for JDOM <code>CDATA</code> objects */
    public static final int CDATA     = 2;

    /** Mask for JDOM <code>Text</code> objects */
    public static final int TEXT      = 4;

    /** Mask for JDOM <code>Comment</code> objects */
    public static final int COMMENT   = 8;

    /** Mask for JDOM <code>ProcessingInstruction</code> objects */
    public static final int PI        = 16;

    /** Mask for JDOM <code>EntityRef</code> objects */
    public static final int ENTITYREF = 32;

    /** Mask for JDOM <code>Document</code> object */
    public static final int DOCUMENT  = 64;

    /** The JDOM object mask */
    protected int filterMask;

    /**
     * <p>
     * Default constructor that allows any legal JDOM objects.
     * </p>
     */
    public ContentFilter() {
        setDefaultMask();
    }

    /**
     * <p>
     * Set whether all JDOM objects are visible or not.
     * </p>
     *
     * @param allVisible <code>true</code> all JDOM objects are visible,
     *                   <code>false</code> all JDOM objects are hidden.
     */
    public ContentFilter(boolean allVisible) {
        if (allVisible) {
            setDefaultMask();
        }
        else {
            filterMask &= ~filterMask;
        }
    }

    /**
     * <p>
     * Filter out JDOM objects according to a filtering mask.
     * </p>
     *
     * @param mask Mask of JDOM objects to allow.
     */
    public ContentFilter(int mask) {
        setFilterMask(mask);
    }

    /**
     * <p>
     * Return current filtering mask.
     * </p>
     */
    public int getFilterMask() {
        return filterMask;
    }

    /**
     * <p>
     * Set filtering mask.
     * </p>
     */
    public void setFilterMask(int mask) {
        setDefaultMask();
        filterMask &= mask;
    }

    /**
     * <p>
     * Set this filter to allow all legal JDOM objects.
     * </p>
     */
    public void setDefaultMask() {
        filterMask = ELEMENT | CDATA | TEXT | COMMENT |
                     PI | ENTITYREF | DOCUMENT;
    }

    /**
     * <p>
     * Set filter to match only JDOM objects that are legal
     * document content.
     * </p>
     */
    public void setDocumentContent() {
        filterMask = ELEMENT | COMMENT | PI;
    }

    /**
     * <p>
     * Set filter to match only JDOM objects that are legal
     * element content.
     * </p>
     */
    public void setElementContent() {
        filterMask = ELEMENT | CDATA | TEXT |
                     COMMENT | PI | ENTITYREF;
    }

    /**
     * <p>
     * Set visiblity of <code>Element</code> objects.
     * </p>
     *
     * @param visible whether Elements are visible, <code>true</code>
     *        if yes, <code>false</code> if not
     */
    public void setElementVisible(boolean visible) {
        if (visible) {
            filterMask |= ELEMENT;
        }
        else {
            filterMask &= ~ELEMENT;
        }
    }

    /**
     * <p>
     * Set visiblity of <code>CDATA</code> objects.
     * </p>
     *
     * @param visible whether CDATA nodes are visible, <code>true</code>
     *        if yes, <code>false</code> if not
     */
    public void setCDATAVisible(boolean visible) {
        if (visible) {
            filterMask |= CDATA;
        }
        else {
            filterMask &= ~CDATA;
        }
    }

    /**
     * <p>
     * Set visiblity of <code>Text</code> objects.
     * </p>
     *
     * @param visible whether Text nodes are visible, <code>true</code>
     *        if yes, <code>false</code> if not
     */
    public void setTextVisible(boolean visible) {
        if (visible) {
            filterMask |= TEXT;
        }
        else {
            filterMask &= ~TEXT;
        }
    }

    /**
     * <p>
     * Set visiblity of <code>Comment</code> objects.
     * </p>
     *
     * @param visible whether Comments are visible, <code>true</code>
     *        if yes, <code>false</code> if not
     */
    public void setCommentVisible(boolean visible) {
        if (visible) {
            filterMask |= COMMENT;
        }
        else {
            filterMask &= ~COMMENT;
        }
    }

    /**
     * <p>
     * Set visiblity of <code>ProcessingInstruction</code> objects.
     * </p>
     *
     * @param visible whether ProcessingInstructions are visible,
     *        <code>true</code> if yes, <code>false</code> if not
     */
    public void setPIVisible(boolean visible) {
        if (visible) {
            filterMask |= PI;
        }
        else {
            filterMask &= ~PI;
        }
    }

    /**
     * <p>
     * Set visiblity of <code>EntityRef</code> objects.
     * </p>
     *
     * @param visible whether EntityRefs are visible, <code>true</code>
     *        if yes, <code>false</code> if not
     */
    public void setEntityRefVisible(boolean visible) {
        if (visible) {
            filterMask |= ENTITYREF;
        }
        else {
            filterMask &= ~ENTITYREF;
        }
    }

    /**
     * <p>
     * Check to see if the object can be added to the list.
     * </p>
     *
     * @param obj The object to verify.
     * @return <code>true</code> if the object can be added.
     */
    public boolean canAdd(Object obj) {
        return matches(obj);
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
        return matches(obj);
    }

    /**
     * <p>
     * Check to see if the object matches according to the filter mask.
     * </p>
     *
     * @param obj The object to verify.
     * @return <code>true</code> if the objected matched a predfined 
     *           set of rules.
     */
    public boolean matches(Object obj) {
        if (obj instanceof Element) {
            return (filterMask & ELEMENT) != 0;
        }
        else if (obj instanceof CDATA) {
            return (filterMask & CDATA) != 0;
        }
        else if (obj instanceof Text) {
            return (filterMask & TEXT) != 0;
        }
        else if (obj instanceof Comment) {
            return (filterMask & COMMENT) != 0;
        }
        else if (obj instanceof ProcessingInstruction) {
            return (filterMask & PI) != 0;
        }
        else if (obj instanceof EntityRef) {
            return (filterMask & ENTITYREF) != 0;
        }
        else if (obj instanceof Document) {
            return (filterMask & DOCUMENT) != 0;
        }

        //XXX throw exception instead????
        return false;
    }

    /**
     * <p>
     * Returns true if object is instance of ContentFilter and has
     * the same filtering mask as this one.
     * </p>
     *
     * @return <code>true</code> if the Filters are equal
     */
    public boolean equals(Object obj) {
        if (obj instanceof ContentFilter) {
            ContentFilter filter = (ContentFilter) obj;
            return (filterMask == filter.filterMask);
        }
        return false;
    }
} 
