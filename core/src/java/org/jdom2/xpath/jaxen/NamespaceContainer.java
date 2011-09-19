package org.jdom2.xpath.jaxen;

import org.jdom2.Element;
import org.jdom2.Namespace;

final class NamespaceContainer {
	private final Namespace ns;
	private final Element emt;
	
	public NamespaceContainer(Namespace ns, Element emt) {
		this.ns = ns;
		this.emt = emt;
	}
	public Namespace getNamespace() {
		return ns;
	}
	public Element getParentElement() {
		return emt;
	}
	
	@Override
	public String toString() {
		return ns.getPrefix() + "=" + ns.getURI();
	}
}