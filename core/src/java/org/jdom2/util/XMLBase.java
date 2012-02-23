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

package org.jdom2.util;

import java.net.URI;
import java.net.URISyntaxException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.Parent;

/**
 * Class used to calculate the XMLBase URI for an Element as per the XMLBase
 * specification here: http://www.w3.org/TR/xmlbase/
 * <p>
 * This class assumes that all values in <code>xml:base</code> attributes are
 * valid URI values according to the <code>java.net.URI</code> implementation.
 * The same implementation is used to resolve relative URI values, and thus this
 * code follows the assumptions in java.net.URI.
 * 
 * @author Rolf Lear
 */
public final class XMLBase {

	private static final URI resolve(String uri, URI relative)
			throws URISyntaxException {
		if (uri == null) {
			return relative;
		}
		final URI n = new URI(uri);
		if (relative == null) {
			return n;
		}
		return n.resolve(relative);
	}

	/**
	 * Calculate the XMLBase URI for an Element using the rules defined in the
	 * XMLBase specification, as well as the values supplied in the xml:base
	 * attributes on the supplied Element and the Element's ancestry.
	 * <p>
	 * This method
	 * assumes that all values in <code>xml:base</code> attributes are valid URI
	 * values according to the <code>java.net.URI</code> implementation. The
	 * same implementation is used to resolve relative URI values, and thus this
	 * code follows the assumptions in java.net.URI.
	 * <p>
	 * This technically deviates from the XMLBase spec because to fully support
	 * legacy HTML the xml:base attribute could contain what is called a 'LIERI'
	 * which is a superset of true URI values, but for practical purposes JDOM
	 * users should never encounter such values because they are not processing
	 * raw HTML (but xhtml maybe). 
	 * 
	 * @param element
	 *        The Element for which to calculate the XMLBase
	 * @return a URI representing the XMLBase value for the supplied Element, or
	 *         null if one could not be calculated.
	 * @throws URISyntaxException
	 *         if it is not possible to create java.new.URI values from the data
	 *         in the <code>xml:base</code> attributes.
	 */
	public static URI xmlBase(final Element element) throws URISyntaxException {
		Parent p = element;
		URI ret = null;
		while (p != null) {
			if (p instanceof Element) {
				ret = resolve(((Element) p).getAttributeValue("base",
						Namespace.XML_NAMESPACE), ret);
			} else {
				ret = resolve(((Document) p).getBaseURI(), ret);
			}
			if (ret != null && ret.isAbsolute()) {
				return ret;
			}
			p = p.getParent();
		}
		return ret;
	}

	/**
	 * Inaccessible constructor
	 */
	private XMLBase() {
		// nothing
	}
}
