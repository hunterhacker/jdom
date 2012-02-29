/*--

 Copyright (C) 2001-2004 Jason Hunter & Brett McLaughlin.
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

package org.jdom2.contrib.ids;

import org.jdom2.Attribute;
import org.jdom2.AttributeType;
import org.jdom2.Document;
import org.jdom2.DocType;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.DefaultJDOMFactory;

/**
 * The <code>IdFactory</code> extends the default JDOM factory to
 * build documents that support looking up elements using their ID
 * attribute.
 * <p>
 * Looking-up elements by ID only works if a DTD is associated to
 * the XML document as the information defining some attributes as
 * IDs is only available from the DTD and not from the XML document
 * itself.</p>
 * <p>
 * The Documents created by this factory are instances of
 * {@link IdDocument} which provides the method
 * {@link IdDocument#getElementById} to look up an element given the
 * value of its ID attribute.</p>
 * <p>
 * The following code snippet demonstrates how to use the
 * <code>IdFactory</code> with JDOM's SAXBuilder to create an
 * <code>IdDocument</code>.</p>
 * <blockquote><pre>
 *  SAXBuilder builder = new SAXBuilder();
 *  builder.setFactory(new IdFactory());
 *
 *  IdDocument doc = (IdDocument)(builder.build(xmlDocument));
 *
 *  Element elt = doc.getElementById(idValue);
 * </pre></blockquote>
 *
 * @author Laurent Bihanic
 */
public class IdFactory extends DefaultJDOMFactory {

    /**
     * Creates a new IdFactory object.
     */
    public IdFactory() {
       super();
    }

    // Allow Javadocs to inherit from superclass

    @Override
	public Attribute attribute(String name, String value, Namespace namespace) {
        return new IdAttribute(name, value, namespace);
    }

    @Override
	public Attribute attribute(String name, String value,
                                            AttributeType type, Namespace namespace) {
        return new IdAttribute(name, value, type, namespace);
    }

    @Override
	public Attribute attribute(String name, String value) {
        return new IdAttribute(name, value);
    }

    @Override
	public Attribute attribute(String name, String value, AttributeType type) {
        return new IdAttribute(name, value, type);
    }

    @Override
	public Document document(Element rootElement, DocType docType) {
        return new IdDocument(rootElement, docType);
    }
    @Override
	public Document document(Element rootElement) {
        return new IdDocument(rootElement);
    }

    @Override
	public Element element(final int line, final int col, String name, Namespace namespace) {
        return new IdElement(name, namespace);
    }
    @Override
	public Element element(final int line, final int col, String name) {
        return new IdElement(name);
    }
    @Override
	public Element element(final int line, final int col, String name, String uri) {
        return new IdElement(name, uri);
    }
    @Override
	public Element element(final int line, final int col, String name, String prefix, String uri) {
        return new IdElement(name, prefix, uri);
    }
}

