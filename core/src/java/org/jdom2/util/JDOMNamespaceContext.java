/*--

 Copyright (C) 2000-2012 Jason Hunter & Brett McLaughlin.
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

package org.jdom2.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.NamespaceContext;

import org.jdom2.JDOMConstants;
import org.jdom2.Namespace;
import org.jdom2.internal.ArrayCopy;

/**
 * A Read-Only {@link NamespaceContext} that describes namespaces found
 * in a JDOM node.
 * @author gordon burgett https://github.com/gburgett
 */
public final class JDOMNamespaceContext implements NamespaceContext {
    
    private final Namespace[] namespacearray;
    
    /**
     * Create a read-only representation of the input namespace list.
     * @param namespaces the Namespace instances to represent.
     */
    public JDOMNamespaceContext(final Namespace[] namespaces){
    	if (namespaces == null) {
    		throw new IllegalArgumentException("Cannot process a null Namespace list");
    	}
        this.namespacearray = ArrayCopy.copyOf(namespaces, namespaces.length);
        for (int i = 1; i < namespacearray.length; i++) {
    		final Namespace n = namespacearray[i];
    		if (n == null) {
    			throw new IllegalArgumentException("Cannot process null namespace at position " + i);
    		}
    		final String p = n.getPrefix();
        	for (int j = 0; j < i; j++) {
        		if (p.equals(namespacearray[j].getPrefix())) {
        			throw new IllegalArgumentException("Cannot process multiple namespaces with the prefix '" + p + "'.");
        		}
        	}
        }
    }
    
    @Override
    public String getNamespaceURI(final String prefix) {
    	if (prefix == null) {
    		throw new IllegalArgumentException("NamespaceContext requires a non-null prefix");
    	}
    	if (JDOMConstants.NS_PREFIX_XML.equals(prefix)) {
    		return JDOMConstants.NS_URI_XML;
    	}
    	if (JDOMConstants.NS_PREFIX_XMLNS.equals(prefix)) {
    		return JDOMConstants.NS_URI_XMLNS;
    	}
    	
        for(final Namespace n : namespacearray){
            if(n.getPrefix().equals(prefix)){
                return n.getURI();
            }
        }

        return "";
    }

    @Override
    public String getPrefix(final String namespaceURI) {
    	if (namespaceURI == null) {
    		throw new IllegalArgumentException("NamespaceContext requires a non-null Namespace URI");
    	}
    	if (JDOMConstants.NS_URI_XML.equals(namespaceURI)) {
    		return JDOMConstants.NS_PREFIX_XML;
    	}
    	if (JDOMConstants.NS_URI_XMLNS.equals(namespaceURI)) {
    		return JDOMConstants.NS_PREFIX_XMLNS;
    	}
    	
        for(final Namespace n : namespacearray){
            if(n.getURI().equals(namespaceURI)){
                return n.getPrefix();
            }
        }

        return null;
    }

    @SuppressWarnings("rawtypes")
	@Override
    public Iterator getPrefixes(final String namespaceURI) {
    	if (namespaceURI == null) {
    		throw new IllegalArgumentException("NamespaceContext requires a non-null Namespace URI");
    	}
    	if (JDOMConstants.NS_URI_XML.equals(namespaceURI)) {
    		return Collections.singleton(JDOMConstants.NS_PREFIX_XML).iterator();
    	}
    	if (JDOMConstants.NS_URI_XMLNS.equals(namespaceURI)) {
    		return Collections.singleton(JDOMConstants.NS_PREFIX_XMLNS).iterator();
    	}
    	
        final List<String> ret = new ArrayList<String>();
        for(final Namespace n : namespacearray){
            if(n.getURI().equals(namespaceURI)){
                ret.add(n.getPrefix());
            }
        }

        return Collections.unmodifiableCollection(ret).iterator();
    }

}
