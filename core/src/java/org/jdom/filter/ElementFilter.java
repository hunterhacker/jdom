/*-- 

 $Id: ElementFilter.java,v 1.1 2002/03/12 06:53:57 jhunter Exp $

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
 * The <code>ElementFilter</code> when applied to a <code>FilterList</code>
 * will only allow <code>Elements</code> to be visible.
 * </p>
 *
 * @author Jools Enticknap
 * @author Bradley S. Huffman
 * @version $Revision: 1.1 $, $Date: 2002/03/12 06:53:57 $
 */
public class ElementFilter implements Filter {

    private static final String CVS_ID = 
      "@(#) $RCSfile: ElementFilter.java,v $ $Revision: 1.1 $ $Date: 2002/03/12 06:53:57 $ $Name:  $";

    /** The element name */
    protected String name;

    /** The element namespace */
    protected Namespace namespace;
    
    /**
     * <p>
     * Filter out the Elements.
     * </p>
     */
    public ElementFilter() {}

    /**
     * <p>
     * Filter out the Elements with the supplied name in any Namespace.
     * </p>
     *
     * @param name   The name of the Element.
     */
    public ElementFilter(String name) {
        this.name   = name;
    }

    /**
     * <p>
     * Filter out the Elements with the supplied Namespace.
     * </p>
     *
     * @param namespace The namespace the Element lives in.
     */
    public ElementFilter(Namespace namespace) {
        this.namespace = namespace;
    }

    /**
     * <p>
     * Filter out the Elements with the supplied name and Namespace.
     * </p>
     *
     * @param name   The name of the Element.
     * @param namespace The namespace the Element lives in.
     */
    public ElementFilter(String name, Namespace namespace) {
        this.name   = name;
        this.namespace = namespace;
    }

    /**
     * <p>
     * Only allow the adding of Element objects.
     * </p>
     *
     * @param obj The object to verify.
     * @return <code>true</code> if the object can be added.
     * @throws IllegalAddException if the object can be added.
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
        if (obj instanceof Element) {
            return true;
        }
        return false;
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
            Element element = (Element) obj;
            if (name == null) {
                if (namespace == null)
                     return true;
                else namespace.equals(element.getNamespace());
            }
            else {
                if (name.equals(element.getName())) {
                    if (namespace == null)
                         return true;
                    else return namespace.equals(element.getNamespace());
                }
            }
        }
        return false;
    }

    /**
     * <p>
     * Returns true if object is instance of ElementFilter and has
     * the same parent Element, name, and namespace as this filter.
     * </p>
     *
     * @return <code>true</code> if the Filters are equal
     */
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj instanceof ElementFilter) {
            ElementFilter filter = (ElementFilter) obj;
            if ((name == filter.name) &&
                (namespace == filter.namespace))
                    return true;
        }
        return false;
    }
}
