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

package org.jdom2.contrib.dom;

import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.Content;
import org.jdom2.DocType;
import org.jdom2.Element;
import org.jdom2.EntityRef;
import org.jdom2.Namespace;
import org.jdom2.Parent;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;

abstract class JParent extends JNamespaceAware implements NodeList {

	private JNamespaceAware[] kids = null;
	protected final Parent shadow;

	public JParent(final JDocument topdoc, final JParent parent,
			final Parent shadow, final short nodetype,
			final Namespace[] nstack) {
		super(topdoc, parent, nodetype, nstack);
		this.shadow = shadow;
	}

	@Override
	public final Object getWrapped() {
		return shadow;
	}



	private final JNamespaceAware hydrate(final Content k) {
		switch(k.getCType()) {
			case CDATA:
				return topdoc.find((CDATA)k);
			case Comment:
				return topdoc.find((Comment)k);
			case DocType:
				return topdoc.find((DocType)k);
			case Element:
				return topdoc.find((Element)k);
			case EntityRef:
				return topdoc.find((EntityRef)k);
			case ProcessingInstruction:
				return topdoc.find((ProcessingInstruction)k);
			case Text:
				return topdoc.find((Text)k);
		}
		throw new IllegalStateException("Unexpected content " + k);
	}

	protected final JNamespaceAware[] checkKids() {
		if (kids != null) {
			return kids;
		}

		if (shadow == null) {
			kids = new JNamespaceAware[0];
			return kids;
		}

		final List<Content> content = shadow.getContent();
		kids = new JNamespaceAware[content.size()];
		for (int i = 0; i < kids.length; i++) {
			kids[i] = hydrate( content.get(i) );
		}

		return kids;
	}

	protected final JNode getPreviousSibling(final JNode jNode) {
		checkKids();
		for (int i = 0; i < kids.length; i++) {
			if (kids[i] == jNode) {
				return i > 0 ? kids[i - 1] : null;
			}
		}
		return null;
	}

	protected final JNode getNextSibling(final JNode jNode) {
		checkKids();
		for (int i = 0; i < kids.length; i++) {
			if (kids[i] == jNode) {
				return i < kids.length - 1 ? kids[i + 1] : null;
			}
		}
		return null;
	}

	@Override
	public final boolean hasChildNodes() {
		checkKids();
		return kids.length > 0;
	}


	@Override
	public final NodeList getChildNodes() {
		checkKids();
		return this;
	}

	@Override
	public final Node getFirstChild() {
		checkKids();
		return kids.length > 0 ? kids[0] : null;
	}

	@Override
	public final Node getLastChild() {
		checkKids();
		return kids.length > 0 ? kids[kids.length - 1] : null;
	}

	@Override
	public final Node item(final int index) {
		checkKids();
		return index >= 0 && index < kids.length ? kids[index] : null;
	}

	@Override
	public final int getLength() {
		checkKids();
		return kids.length;
	}


}
