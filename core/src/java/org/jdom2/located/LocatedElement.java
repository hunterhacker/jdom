/*-- 

 Copyright (C) 2012 Jason Hunter & Brett McLaughlin.
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
    written permission, please contact <request_AT_jdom_DOT_org>.

 4. Products derived from this software may not be called "JDOM", nor
    may "JDOM" appear in their name, without prior written permission
    from the JDOM Project Management <request_AT_jdom_DOT_org>.

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
 created by Jason Hunter <jhunter_AT_jdom_DOT_org> and
 Brett McLaughlin <brett_AT_jdom_DOT_org>.  For more information
 on the JDOM Project, please see <http://www.jdom.org/>.

 */

package org.jdom2.located;

import org.jdom2.Element;
import org.jdom2.IllegalNameException;
import org.jdom2.Namespace;

/**
 * This Element specialization contains the location information as parsed.
 * 
 * @author Rolf Lear
 *
 */
public class LocatedElement extends Element implements Located {
	
	/**
	 * Creates a new element with the supplied (local) name and namespace. If
	 * the provided namespace is null, the element will have no namespace.
	 *
	 * @param  name                 local name of the element
	 * @param  namespace            namespace for the element
	 * @throws IllegalNameException if the given name is illegal as an element
	 *                              name
	 */
	public LocatedElement(final String name, final Namespace namespace) {
		super(name, namespace);
	}

	/**
	 * Create a new element with the supplied (local) name and no namespace.
	 *
	 * @param  name                 local name of the element
	 * @throws IllegalNameException if the given name is illegal as an element
	 *                              name.
	 */
	public LocatedElement(final String name) {
		super(name);
	}

	/**
	 * Creates a new element with the supplied (local) name and a namespace
	 * given by a URI. The element will be put into the unprefixed (default)
	 * namespace.
	 *
	 * @param  name                 name of the element
	 * @param  uri                  namespace URI for the element
	 * @throws IllegalNameException if the given name is illegal as an element
	 *                              name or the given URI is illegal as a
	 *                              namespace URI
	 */
	public LocatedElement(final String name, final String uri) {
		super(name, uri);
	}

	/**
	 * Creates a new element with the supplied (local) name and a namespace
	 * given by the supplied prefix and URI combination.
	 *
	 * @param  name                 local name of the element
	 * @param  prefix               namespace prefix
	 * @param  uri                  namespace URI for the element
	 * @throws IllegalNameException if the given name is illegal as an element
	 *                              name, the given prefix is illegal as a
	 *                              namespace prefix, or the given URI is
	 *                              illegal as a namespace URI
	 */
	public LocatedElement(final String name, final String prefix, final String uri) {
		super(name, prefix, uri);
	}

	/**
	 * JDOM2 Serialization. In this case, DocType is simple. 
	 */
	private static final long serialVersionUID = 200L;
	
	private int line, col;

	@Override
	public int getLine() {
		return line;
	}

	@Override
	public int getColumn() {
		return col;
	}

	@Override
	public void setLine(int line) {
		this.line = line;
	}

	@Override
	public void setColumn(int col) {
		this.col = col;
	}

	
}
