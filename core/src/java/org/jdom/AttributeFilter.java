/*-- 

 $Id: AttributeFilter.java,v 1.2 2001/12/19 04:49:36 jhunter Exp $

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
import java.util.Iterator;

/**
 * <p>
 * The <code>AttributeFilter</code> class is a <code>Filter</code>
 * which ensures the correct values are returned depending on how
 * the Filter is instantiated.
 * </p>
 *
 * @author Jools Enticknap
 * @version 0.01
 */
final class AttributeFilter implements Filter {

    /** Reference to the parent Element */
    private final Element parent;

    /** Reference to the filtering namespace */
    private final Namespace ns;
    
    /** The element name */
    private final String name;

    /**
     * <p>
     * </p>
     *
     * @param parent The parent element reference.
     */
    AttributeFilter(Element parent) {
        //this(parent, null, null);
        // Some JDK 1.1.x compilers choke on the above
        this.parent = parent;
        this.name   = null;
        this.ns     = null;
    }    

    /**
     * <p>
     * Create an AttributeFilter which will filter based on
     * an attribute name and namespace.
     * </p>
     *
     * @param parent The parent element reference.
     * @param name   The name of the Attribute.
     * @param ns     The namespace of the Attribute.
     */
    AttributeFilter(Element parent, String name, Namespace ns) { 
        this.parent = parent;
        this.name   = name;
        this.ns     = ns;
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
        if (obj instanceof Attribute) {
            return canAdd(parent, (Attribute) obj);
        }

        return false;
    }
    
    /**
     * <p>
     * Ensure that the supplied attribute can be added to the parent
     * Element.
     * </p>
     *
     * @param parent The parent Element.
     * @param attrib The attribute to be added to the parent.
     * @return <code>true</code> if the attribute can be added.
     */
    public static boolean canAdd(Element parent, Attribute attribute) {
        if (attribute.getParent() != null) {
            throw new IllegalAddException(parent, attribute,
                "The attribute already has an existing parent \"" +
                attribute.getParent().getQualifiedName() + "\"");
        }

        // Verify the attribute's namespace prefix doesn't collide with
        // another attribute prefix or this element's prefix.
        // This is unfortunately pretty heavyweight but we don't need to do it
        // for attributes without a namespace, and a little testing shows no
        // real difference in build times anyway.
        String prefix = attribute.getNamespace().getPrefix();
        String uri = attribute.getNamespace().getURI();
        if (!prefix.equals("")) {
            if (prefix.equals(parent.getNamespacePrefix()) && 
                !uri.equals(parent.getNamespaceURI())) {
                    throw new IllegalAddException(parent, attribute,
                            "The attribute namespace prefix \"" + prefix + 
                            "\" collides with the element namespace prefix");
            }

            if (parent.additionalNamespaces != null && 
                parent.additionalNamespaces.size() > 0) 
            {
                Iterator itr = parent.additionalNamespaces.iterator();
                while (itr.hasNext()) {
                    Namespace ns = (Namespace) itr.next();
                    if (prefix.equals(ns.getPrefix()) && 
                        !uri.equals(ns.getURI())) 
                    {
                        throw new IllegalAddException(parent, attribute,
                            "The attribute namespace prefix \"" + prefix +
                            "\" collides with a namespace declared "+
                            "by the element");
                    }
                }
            }
        }
        
        // Walk the attributes looking for one to replace, or for a ns conflict
        if (parent.attributes != null && parent.attributes.size() > 0) {
            Iterator itr = parent.attributes.iterator();
            while (itr.hasNext()) {
                Attribute att = (Attribute)itr.next();
                Namespace ns = att.getNamespace();

                if (!prefix.equals("") && prefix.equals(ns.getPrefix()) && 
                      !uri.equals(ns.getURI())) {
                    throw new IllegalAddException(parent, attribute,
                        "The attribute namespace prefix \"" + prefix +
                        "\" collides with another attribute namespace on " +
                        "the element");
                }
            }
        }        
        
        attribute.setParent(parent);

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
        if (obj instanceof Attribute) {
            return canRemove(parent, (Attribute)obj);
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
    public static boolean canRemove(Element parent, Attribute attribute) {
        if (parent == attribute.getParent()) {
            attribute.setParent(null);
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
        if (name == null && ns == null) {
            return true;
        }
        else {
            return matches((Attribute) obj, name, ns);
        }
    }

    /**
     * <p>
     * Returns true if matches() will always return true.
     * </p>
     *
     * @return <code>true</code> if this filter matches all objects.
     */
    public boolean matchesAll() {
        return (name == null && ns == null);
    }

    /**
     * <p>
     * Create the backing list for this filter. Called by the FilterList
     * if the backing list is null, and a modification method has been called.
     */
    public List getBackingList(boolean create) {
        return parent.getAttributeBackingList(create);
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
    public static boolean matches(Attribute attribute, 
                                  String    name, 
                                  Namespace ns)
    {
        return attribute.getName().equals(name) &&
               attribute.getNamespace().equals(ns);    
    }
}
