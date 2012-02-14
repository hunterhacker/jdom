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
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.Parent;


/**
 * A sub-class of the default JDOM <code>Attribute</code> to help
 * keeping up-to-date the element lookup table maintained by
 * <code>IdDocument</code>.
 *
 * @author Laurent Bihanic
 */
@SuppressWarnings("javadoc")
public class IdAttribute extends Attribute {

    /**
	 * Default.
	 */
	private static final long serialVersionUID = 1L;

	public IdAttribute(String name, String value, Namespace namespace) {
        super(name, value, namespace);
    }

    public IdAttribute(String name, String value,
                                        AttributeType type, Namespace namespace) {
        super(name, value, type, namespace);
    }

    public IdAttribute(String name, String value) {
        this(name, value, UNDECLARED_TYPE, Namespace.NO_NAMESPACE);
    }

    public IdAttribute(String name, String value, AttributeType type) {
        this(name, value, type, Namespace.NO_NAMESPACE);
    }

    @Override
	protected Attribute setParent(Element parent) {
        Parent oldParent = this.getParent();

        super.setParent(parent);

        if (this.getAttributeType() == Attribute.ID_TYPE) {
            Document doc;

            // Udpate the owning document's lookup table.
            if (oldParent != null) {
                doc = oldParent.getDocument();
                if (doc instanceof IdDocument) {
                    ((IdDocument)doc).removeId(this.getValue());
                }
            }
            doc = this.getDocument();
            if (doc instanceof IdDocument) {
                ((IdDocument)doc).addId(this.getValue(), this.getParent());
            }
        }
        return this;
    }

    @Override
	public Attribute setValue(String value) {
        String oldValue = this.getValue();

        super.setValue(value);

        if (this.getAttributeType() == Attribute.ID_TYPE) {
            // Udpate the owning document's lookup table.
            Document doc = this.getDocument();
            if (doc instanceof IdDocument) {
                ((IdDocument)doc).removeId(oldValue);
                ((IdDocument)doc).addId(this.getValue(), this.getParent());
            }
        }
        return this;
    }

    @Override
	public Attribute setAttributeType(AttributeType type) {
        AttributeType oldType = this.getAttributeType();

        if (type != oldType) {
            super.setAttributeType(type);

            // Udpate the owning document's lookup table.
            Document doc = this.getDocument();
            if (doc instanceof IdDocument) {
                if (oldType == Attribute.ID_TYPE) {
                    ((IdDocument)doc).removeId(this.getValue());
                }
                if (type == Attribute.ID_TYPE) {
                    ((IdDocument)doc).addId(this.getValue(), this.getParent());
                }
            }
        }
        return this;
    }
}

