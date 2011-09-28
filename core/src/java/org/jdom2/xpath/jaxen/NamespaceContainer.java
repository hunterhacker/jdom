package org.jdom2.xpath.jaxen;

import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 * XPath requires that the namespace nodes are linked to an Element.
 * JDOM does not have such a relationship, so we improvise.
 * Jaxen uses the identity equals ( == ) when comparing
 * one 'node' to another, so we have to make sure that within the context of
 * Jaxen we always return the same instances of NamespaceContainer each time.
 * Further, for jaxen the instance of a Namespace for one Node cannot be the
 * same instance of the same Namespace on a different Node...
 * <p>
 * This all also means that all Jaxen interaction is done with NamespaceContainer
 * and not the JDOM Namespace, which in turn means that when Jaxen returns
 * a NamespaceContainer node, it has to be unwrapped to a simple Namespace.
 * 
 * @author rolf
 *
 */
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