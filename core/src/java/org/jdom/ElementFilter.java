/*-- 

 $Id: ElementFilter.java,v 1.1 2001/12/11 07:32:04 jhunter Exp $

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
 * The <code>ElementFilter</code> when applied to a <code>FilterList</code>
 * will only allow <code>Elements</code> to be visible.
 * </p>
 *
 * @author Jools Enticknap
 * @version 0.01
 */
class ElementFilter implements Filter {
    /** The parent Element */
    private Element parent;
    
    /** The element name */
    private String name;

    /** The element namespace */
    private Namespace ns;
    
    /**
     * <p>
     * Just filter on the Element class.
     * </p>
     *
     * @param parent The parent Element.
     */
    ElementFilter(Element parent) { 
        this(parent, null, null);
    }
    
    /**
     * <p>
     * Filter out the Elements with the supplied name and Namespace.
     * </p>
     *
     * @param parent The parent element.
     * @param name   The name of the Attribute.
     * @param ns     The namespace the Attribute lives in.
     */
    ElementFilter(Element parent, String name, Namespace ns) {
        this.parent = parent;
        this.name   = name;
        this.ns     = ns;
    }

    /**
     * <p>
     * Only allow the adding of Element objects.
     * </p>
     *
     * @param obj The object to verify.
     * @return <code>true</code> if the object can be added.
     */
    public boolean canAdd(Object obj) {
        return (obj instanceof Element) && 
                ElementContentFilter.canAdd(parent, (Element) obj);
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
            return ElementContentFilter.canRemove(parent, (Element) obj);
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
        if (obj instanceof Element) {
            if (name == null && ns == null) {
                return true;
            } else {
                return matches((Element) obj, name, ns);
            }
        }

        return false;
    }

    /**
     * <p>
     * Returns true if matches() will always return true.
     * </p>
     *
     * @return <code>true</code> if this filter matches all objects.
     */
    public boolean matchesAll() {
        return false;
    }

    /**
     * <p>
     * Create the backing list for this filter. Called by the FilterList
     * if the backing list is null, and a modification method has been called.
     */
    public List getBackingList(boolean create) {
        return parent.getContentBackingList(create);
    }

    /**
     * <p>
     * Check to see if the supplied Element matches the name and namespace
     * supplied.
     * </p>
     *
     * @param element The Element to check.
     * @param name    The name of the element.
     * @param ns      The Elements namespace.
     * @return <code>true</code> if the objected matched a predfined 
     *           set of rules.
     */    
    public static boolean matches(Element element, String name, Namespace ns) {
        return ((name != null) && element.getName().equals(name)) &&
                ((ns != null)  && ns.equals(element.getNamespace()));
    }
}
