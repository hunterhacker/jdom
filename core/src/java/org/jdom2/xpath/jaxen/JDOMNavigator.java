package org.jdom2.xpath.jaxen;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.jaxen.DefaultNavigator;
import org.jaxen.FunctionCallException;
import org.jaxen.JaxenConstants;
import org.jaxen.UnsupportedAxisException;
import org.jaxen.XPath;
import org.jaxen.saxpath.SAXPathException;
import org.jaxen.util.SingleObjectIterator;
import org.jdom2.Attribute;
import org.jdom2.Comment;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.Parent;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;
import org.jdom2.input.SAXBuilder;

final class JDOMNavigator extends DefaultNavigator {
	
	private final IdentityHashMap<Element, NamespaceContainer[]> emtnsmap = 
			new IdentityHashMap<Element, NamespaceContainer[]>();
	
	@Override
	public XPath parseXPath(String path) throws SAXPathException {
		return new JaxenXPath(path);
	}

	@Override
	public Object getDocument(String url) throws FunctionCallException {
		SAXBuilder sb = new SAXBuilder();
		try {
			return sb.build(url);
		} catch (JDOMException e) {
			throw new FunctionCallException("Failed to parse " + url, e);
		} catch (IOException e) {
			throw new FunctionCallException("Failed to access " + url, e);
		}
	}

	@Override
	public boolean isText(Object isit) {
		return isit instanceof Text;
	}

	@Override
	public boolean isProcessingInstruction(Object isit) {
		return isit instanceof ProcessingInstruction;
	}

	@Override
	public boolean isNamespace(Object isit) {
		return (isit instanceof NamespaceContainer);
	}

	@Override
	public boolean isElement(Object isit) {
		return isit instanceof Element;
	}

	@Override
	public boolean isDocument(Object isit) {
		return isit instanceof Document;
	}

	@Override
	public boolean isComment(Object isit) {
		return isit instanceof Comment;
	}

	@Override
	public boolean isAttribute(Object isit) {
		return isit instanceof Attribute;
	}

	@Override
	public String getTextStringValue(Object text) {
		// CDATA is a subclass of Text
		return ((Text)text).getText();
	}

	@Override
	public String getNamespaceStringValue(Object namespace) {
		return ((NamespaceContainer)namespace).getNamespace().getURI();
	}

	@Override
	public String getNamespacePrefix(Object namespace) {
		return ((NamespaceContainer)namespace).getNamespace().getPrefix();
	}

	private void recurseElementText(Element element, StringBuilder sb) {
		for (Iterator<?> it = element.getContent().iterator(); it.hasNext(); ) {
			Content c = (Content)it.next();
			if (c instanceof Element) {
				recurseElementText((Element)c, sb);
			} else if (c instanceof Text) {
				sb.append(((Text)c).getText());
			}
		}
	}

	@Override
	public String getElementStringValue(Object element) {
		StringBuilder sb = new StringBuilder();
		recurseElementText((Element)element, sb);
		return sb.toString();
	}

	@Override
	public String getElementQName(Object element) {
		Element e = (Element)element;
		if (e.getNamespace().getPrefix().length() == 0) {
			return e.getName();
		}
		return e.getNamespacePrefix() + ":" + e.getName();
	}

	@Override
	public String getElementNamespaceUri(Object element) {
		return ((Element)element).getNamespaceURI();
	}

	@Override
	public String getElementName(Object element) {
		return ((Element)element).getName();
	}

	@Override
	public String getCommentStringValue(Object comment) {
		return ((Comment)comment).getValue();
	}

	@Override
	public String getAttributeStringValue(Object attribute) {
		return ((Attribute)attribute).getValue();
	}

	@Override
	public String getAttributeQName(Object att) {
		Attribute attribute = (Attribute)att;
		if (attribute.getNamespacePrefix().length() == 0) {
			return attribute.getName();
		}
		return attribute.getNamespacePrefix() + ":" + attribute.getName();
	}

	@Override
	public String getAttributeNamespaceUri(Object attribute) {
		return ((Attribute)attribute).getNamespaceURI();
	}

	@Override
	public String getAttributeName(Object attribute) {
		return ((Attribute)attribute).getName();
	}

	@Override
	public String getProcessingInstructionTarget(Object pi) {
		return ((ProcessingInstruction)pi).getTarget();
	}

	@Override
	public String getProcessingInstructionData(Object pi) {
		return ((ProcessingInstruction)pi).getData();
	}

	@Override
	public Object getDocumentNode(Object contextNode) {
		if (contextNode instanceof Document) {
			return contextNode;
		}
		if (contextNode instanceof Attribute) {
			Element p = ((Attribute)contextNode).getParent();
			if (p == null) {
				return null;
			}
			return ((Content)p).getDocument();
		}
		if (contextNode instanceof NamespaceContainer) {
			return ((NamespaceContainer)contextNode).getParentElement().getDocument();
		}
		return ((Content)contextNode).getDocument();
	}

	@Override
	public Object getParentNode(Object contextNode) throws UnsupportedAxisException {
		if (contextNode instanceof Document) {
			return null;
		}
		if (contextNode instanceof Attribute) {
			return ((Attribute)contextNode).getParent();
		}
		if (contextNode instanceof NamespaceContainer) {
			return ((NamespaceContainer)contextNode).getParentElement();
		}
		if (contextNode instanceof Content) {
			return ((Content)contextNode).getParent();
		}
		return null;
	}

	@Override
	public Iterator<?> getAttributeAxisIterator(Object contextNode) throws UnsupportedAxisException {
		if (isElement(contextNode)) {
			return ((Element)contextNode).getAttributes().iterator();
		}
		return JaxenConstants.EMPTY_ITERATOR;
	}

	@Override
	public Iterator<?> getChildAxisIterator(Object contextNode) throws UnsupportedAxisException {
		if (contextNode instanceof Parent) {
			return ((Parent)contextNode).getContent().iterator();
		}
		return JaxenConstants.EMPTY_ITERATOR;
	}

	private void accumulate(HashMap<String,NamespaceContainer> current,
			Element context, Namespace ns) {
		if (!current.containsKey(ns.getPrefix())) {
			current.put(ns.getPrefix(), 
					new NamespaceContainer(ns, context));
		}
	}

	@Override
	public Iterator<?> getNamespaceAxisIterator(final Object contextNode) throws UnsupportedAxisException {
        if ( ! isElement(contextNode) ) {
            return JaxenConstants.EMPTY_ITERATOR;
        }
        
        Element elem = (Element) contextNode;
        
        NamespaceContainer[] namespaces = emtnsmap.get(elem);
        if (namespaces == null) {

	        LinkedHashMap<String,NamespaceContainer> current = 
	        		new LinkedHashMap<String, NamespaceContainer>();
	        accumulate(current, elem, Namespace.XML_NAMESPACE);
	        
	        while (elem != null) {
	        	accumulate(current, elem, elem.getNamespace());
	        	for (Object ns : elem.getAdditionalNamespaces()) {
	        		accumulate(current, elem, (Namespace)ns);
	        	}
	        	for (Object att : elem.getAttributes()) {
	        		accumulate(current, elem, ((Attribute)att).getNamespace());
	        	}
	        	elem = elem.getParentElement();
	        }
		    namespaces = current.values().toArray(new NamespaceContainer[current.size()]);
		    // Cannot use elem because that is used to walk up the hierarchy.
		    emtnsmap.put((Element)contextNode, namespaces);
        }
        return Arrays.asList(namespaces).iterator();
	}

	@Override
	public Iterator<?> getParentAxisIterator(Object contextNode) throws UnsupportedAxisException {
		
		Parent p = null;
		if (contextNode instanceof Content) {
			p = ((Content)contextNode).getParent();
		} else if (contextNode instanceof Attribute) {
			p = ((Attribute)contextNode).getParent();
		} else if (contextNode instanceof NamespaceContainer) {
			p = ((NamespaceContainer)contextNode).getParentElement();
		}
		if (p != null) {
			return new SingleObjectIterator(p);
		}
		return JaxenConstants.EMPTY_ITERATOR;
	}
}