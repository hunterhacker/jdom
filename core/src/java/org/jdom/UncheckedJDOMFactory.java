package org.jdom;

import java.util.*;

/**
 * Special factory for building documents without any content or structure
 * checking.  This should only be used when you are 100% positive that the
 * input is absolutely correct.  This factory can speed builds, but any
 * problems in the input will be uncaught until later when they could cause
 * infinite loops, malformed XML, or worse.  Use with extreme caution.
 */
public class UncheckedJDOMFactory implements JDOMFactory {

    // =====================================================================
    // Element Factory
    // =====================================================================

    public Element element(String name, Namespace namespace) {
        Element e = new Element();
        e.name = name;
        if (namespace == null) {
            namespace = Namespace.NO_NAMESPACE;
        }
        e.namespace = namespace;
        return e;
    }

    public Element element(String name) {
        Element e = new Element();
        e.name = name;
        e.namespace = Namespace.NO_NAMESPACE;
        return e;
    }

    public Element element(String name, String uri) {
        return element(name, Namespace.getNamespace("", uri));
    }

    public Element element(String name, String prefix, String uri) {
        return element(name, Namespace.getNamespace(prefix, uri));
    }

    // =====================================================================
    // Attribute Factory
    // =====================================================================

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

    public Attribute attribute(String name, String value, int type, Namespace namespace) {
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

    public Attribute attribute(String name, String value) {
        Attribute a = new Attribute();
        a.name = name;
        a.value = value;
        a.namespace = Namespace.NO_NAMESPACE;
        return a;
    }

    public Attribute attribute(String name, String value, int type) {
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

    public Text text(String str) {
        Text t = new Text();
        t.value = str;
        return t;
    }

    // =====================================================================
    // CDATA Factory
    // =====================================================================

    public CDATA cdata(String str) {
        CDATA c = new CDATA();
        c.value = str;
        return c;
    }

    // =====================================================================
    // Comment Factory
    // =====================================================================

    public Comment comment(String str) {
        Comment c = new Comment();
        c.text = str;
        return c;
    }

    // =====================================================================
    // Processing Instruction Factory
    // =====================================================================

    public ProcessingInstruction processingInstruction(String target, Map data) {
        ProcessingInstruction p = new ProcessingInstruction();
        p.target = target;
        p.setData(data);
        return p;
    }

    public ProcessingInstruction processingInstruction(String target, String data) {
        ProcessingInstruction p = new ProcessingInstruction();
        p.target = target;
        p.setData(data);
        return p;
    }

    // =====================================================================
    // Entity Ref Factory
    // =====================================================================

    public EntityRef entityRef(String name) {
        EntityRef e = new org.jdom.EntityRef();
        e.name = name;
        return e;
    }

    public EntityRef entityRef(String name, String systemID) {
        EntityRef e = new EntityRef();
        e.name = name;
        e.systemID = systemID;
        return e;
    }

    public EntityRef entityRef(String name, String publicID, String systemID) {
        EntityRef e = new EntityRef();
        e.name = name;
        e.publicID = publicID;
        e.systemID = systemID;
        return e;
    }

    // =====================================================================
    // DocType Factory
    // =====================================================================

    public DocType docType(String elementName, String publicID, String systemID) {
        DocType d = new DocType();
        d.elementName = elementName;
        d.publicID = publicID;
        d.systemID = systemID;
        return d;
    }

    public DocType docType(String elementName, String systemID) {
        return docType(elementName, null, systemID);
    }

    public DocType docType(String elementName) {
        return docType(elementName, null, null);
    }

    // =====================================================================
    // Document Factory
    // =====================================================================

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

    public Document document(Element rootElement, DocType docType) {
        return document(rootElement, docType, null);
    }

    public Document document(Element rootElement) {
        return document(rootElement, null, null);
    }

    // =====================================================================
    // List manipulation
    // =====================================================================

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

    public void setAttribute(Element parent, Attribute a) {
        parent.attributes.uncheckedAddAttribute(a);
    }

    public void addNamespaceDeclaration(Element parent, Namespace additional) {
        if (parent.additionalNamespaces == null) {
            parent.additionalNamespaces = new ArrayList(5); //Element.INITIAL_ARRAY_SIZE
        }
        parent.additionalNamespaces.add(additional);
    }
}
