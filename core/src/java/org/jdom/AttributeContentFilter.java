/*-- 

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

/**
 * <p>
 * </p>
 *
 * @author Jools Enticknap
 * @version 0.01
 */
final class AttributeContentFilter implements Filter {
	/** Reference to the filtering namespace */
	private Namespace ns;
	
	/** The element name */
	private String name;
	
	/**
	 * <p>
	 * Only Elements allowed.
	 * </p>
	 */
	AttributeContentFilter() { 
	}
	
	/**
	 * <p>
	 * Create an AttributeContentFilter which will filter based on
	 * an attribute name and namespace.
	 * </p>
	 *
	 * @param name The name of the Attribute.
	 * @param ns   The namespace of the Attribute.
	 */
	AttributeContentFilter(String name, Namespace ns) { 
		this.name = name;
		this.ns   = ns;
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
		return obj instanceof Attribute;
	}

	/**
	 * <p>
	 * Check to see if the object can be removed from the list.
	 * </p>
	 *
	 * @param obj The object to verify.
	 * @return <code>true</code> if the object can be added.
	 */
	public boolean canRemove(Object obj) {
		return true;
	}

	/**
	 * <p>
	 * Check to see if the object matches a predefined set of rules.
	 * </p>
	 *
	 * @param obj The object to verify.
	 * @return <code>true</code> if the objected matched a predfined 
	 *		   set of rules.
	 */
	public boolean matches(Object obj) {
		if (obj instanceof Attribute) {
			if (name != null) {
				Attribute attrib = (Attribute) obj;
				return attrib.getName().equals(name) &&
				       attrib.getNamespace().equals(ns);
			}
		}
		return false;
	}
}
