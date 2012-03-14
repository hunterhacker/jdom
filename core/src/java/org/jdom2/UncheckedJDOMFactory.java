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

package org.jdom2;

import java.util.*;

/**
 * Special factory for building documents without any content or structure
 * checking.  This should only be used when you are 100% positive that the
 * input is absolutely correct.  This factory can speed builds, but any
 * problems in the input will be uncaught until later when they could cause
 * infinite loops, malformed XML, or worse.  Use with extreme caution.
 * 
 * @author Various Authors - history is not complete
 */
public class UncheckedJDOMFactory extends DefaultJDOMFactory {

	// =====================================================================
	// Element Factory
	// =====================================================================

	@Override
	public Element element(final int line, final int col, final String name, Namespace namespace) {
		Element e = new Element();
		e.name = name;
		if (namespace == null) {
			namespace = Namespace.NO_NAMESPACE;
		}
		e.namespace = namespace;
		return e;
	}

	@Override
	public Element element(final int line, final int col, final String name) {
		Element e = new Element();
		e.name = name;
		e.namespace = Namespace.NO_NAMESPACE;
		return e;
	}

	@Override
	public Element element(final int line, final int col, final String name, String uri) {
		return element(name, Namespace.getNamespace("", uri));
	}

	@Override
	public Element element(final int line, final int col, final String name, String prefix, String uri) {
		return element(name, Namespace.getNamespace(prefix, uri));
	}

	// =====================================================================
	// Attribute Factory
	// =====================================================================

	@Override
	public Attribute attribute(String name, String value, Namespace namespace) {
		Attribute a = new Attribute();
		a.name = name;
		a.value = value;
		if (namespace == null) {
			namespace = Namespace.NO_NAMESPACE;
		}
		a.namespace = namespace;
		return a;
	}

	@Override
	@Deprecated
	public Attribute attribute(String name, String value, int type, Namespace namespace) {
		return attribute(name, value, AttributeType.byIndex(type), namespace);
	}

	@Override
	public Attribute attribute(String name, String value, AttributeType type, Namespace namespace) {
		Attribute a = new Attribute();
		a.name = name;
		a.type = type;
		a.value = value;
		if (namespace == null) {
			namespace = Namespace.NO_NAMESPACE;
		}
		a.namespace = namespace;
		return a;
	}

	@Override
	public Attribute attribute(String name, String value) {
		Attribute a = new Attribute();
		a.name = name;
		a.value = value;
		a.namespace = Namespace.NO_NAMESPACE;
		return a;
	}

	
	@Override
	@Deprecated
	public Attribute attribute(String name, String value, int type) {
		return attribute(name, value, AttributeType.byIndex(type));
	}

	@Override
	public Attribute attribute(String name, String value, AttributeType type) {
		Attribute a = new Attribute();
		a.name = name;
		a.type = type;
		a.value = value;
		a.namespace = Namespace.NO_NAMESPACE;
		return a;
	}

	// =====================================================================
			// Text Factory
			// =====================================================================

	@Override
	public Text text(final int line, final int col, final String str) {
		Text t = new Text();
		t.value = str;
		return t;
	}

	// =====================================================================
	// CDATA Factory
	// =====================================================================

	@Override
	public CDATA cdata(final int line, final int col, final String str) {
		CDATA c = new CDATA();
		c.value = str;
		return c;
	}

	// =====================================================================
	// Comment Factory
	// =====================================================================

	@Override
	public Comment comment(final int line, final int col, final String str) {
		Comment c = new Comment();
		c.text = str;
		return c;
	}

	// =====================================================================
	// Processing Instruction Factory
	// =====================================================================

	@Override
	public ProcessingInstruction processingInstruction(final int line, final int col, final String target, Map<String,String> data) {
		ProcessingInstruction p = new ProcessingInstruction();
		p.target = target;
		p.setData(data);
		return p;
	}

	@Override
	public ProcessingInstruction processingInstruction(final int line, final int col, final String target, String data) {
		ProcessingInstruction p = new ProcessingInstruction();
		p.target = target;
		p.setData(data);
		return p;
	}

	@Override
	public ProcessingInstruction processingInstruction(final int line, final int col, final String target) {
		ProcessingInstruction p = new ProcessingInstruction();
		p.target = target;
		p.rawData = "";
		return p;
	}

	// =====================================================================
	// Entity Ref Factory
	// =====================================================================

	@Override
	public EntityRef entityRef(final int line, final int col, final String name) {
		EntityRef e = new org.jdom2.EntityRef();
		e.name = name;
		return e;
	}

	@Override
	public EntityRef entityRef(final int line, final int col, final String name, String systemID) {
		EntityRef e = new EntityRef();
		e.name = name;
		e.systemID = systemID;
		return e;
	}

	@Override
	public EntityRef entityRef(final int line, final int col, final String name, String publicID, String systemID) {
		EntityRef e = new EntityRef();
		e.name = name;
		e.publicID = publicID;
		e.systemID = systemID;
		return e;
	}

	// =====================================================================
	// DocType Factory
	// =====================================================================

	@Override
	public DocType docType(final int line, final int col, final String elementName, String publicID, String systemID) {
		DocType d = new DocType();
		d.elementName = elementName;
		d.publicID = publicID;
		d.systemID = systemID;
		return d;
	}

	@Override
	public DocType docType(final int line, final int col, final String elementName, String systemID) {
		return docType(elementName, null, systemID);
	}

	@Override
	public DocType docType(final int line, final int col, final String elementName) {
		return docType(elementName, null, null);
	}

	// =====================================================================
	// Document Factory
	// =====================================================================

	@Override
	public Document document(Element rootElement, DocType docType, String baseURI) {
		Document d = new Document();
		if (docType != null) {
			addContent(d, docType);
		}
		if (rootElement != null) {
			addContent(d, rootElement);
		}
		if (baseURI != null) {
			d.baseURI = baseURI;
		}
		return d;
	}

	@Override
	public Document document(Element rootElement, DocType docType) {
		return document(rootElement, docType, null);
	}

	@Override
	public Document document(Element rootElement) {
		return document(rootElement, null, null);
	}

	// =====================================================================
	// List manipulation
	// =====================================================================

	@Override
	public void addContent(Parent parent, Content child) {
		if (parent instanceof Element) {
			Element elt = (Element) parent;
			elt.content.uncheckedAddContent(child);
		}
		else {
			Document doc = (Document) parent;
			doc.content.uncheckedAddContent(child);
		}
	}

	@Override
	public void setAttribute(Element parent, Attribute a) {
		parent.getAttributeList().uncheckedAddAttribute(a);
	}

	@Override
	public void addNamespaceDeclaration(Element parent, Namespace additional) {
		if (parent.additionalNamespaces == null) {
			parent.additionalNamespaces = new ArrayList<Namespace>(5); //Element.INITIAL_ARRAY_SIZE
		}
		parent.additionalNamespaces.add(additional);
	}
	
	@Override
	public void setRoot(Document doc, Element root) {
		doc.content.uncheckedAddContent(root);
	}

}
