package org.jdom2;

import java.util.List;

/**
 * This interface represents those methods used to query the Namespace context
 * in JDOM. {@link Content} and {@link Attribute} implement this interface
 * 
 * @author Rolf Lear
 *
 */
public interface NamespaceAware {

	/**
	 * Obtain a list of all namespaces that are in scope for the current content.
	 * <br>
	 * The contents of this list will always be the sum of getNamespacesIntroduced()
	 * and getNamespacesInherited().
	 * <br>
	 * The order of the content of the list is predefined, and is dictated by the
	 * Element content. The first member will be the Element's namespace, and the
	 * subsequent members will be the remaining namespaces in Namespace-prefix order.
	 * Non-Element content will return the Namespaces in the content's
	 * Element-parent's order.
	 * <br>
	 * @return a read-only list of Namespaces.
	 * 			The order is guaranteed to be consistent
	 */
	public List<Namespace> getNamespacesInScope();
	/**
	 * Obtain a list of all namespaces that are introduced to the XML tree by this
	 * node. Only Elements can introduce namespaces, so all other Content types
	 * will return an empty list.
	 * <br>
	 * The contents of this list will always be a subset (but in the same order)
	 * of getNamespacesInScope(), and will never intersect getNamspacesInherited() 
	 * @return a read-only list of Namespaces.
	 * 			The order is guaranteed to be consistent
	 */
	public List<Namespace> getNamespacesIntroduced();
	/**
	 * Obtain a list of all namespaces that are in scope for this content, but
	 * were not introduced by this content. This method is effectively the
	 * diffence between getNamespacesInScope() and getNamespacesIntroduced().
	 * <br>
	 * The contents of this list will always be a subset (but in the same order)
	 * of getNamespacesInScope(), and will never intersect getNamspacesIntroduced() 
	 * @return a read-only list of Namespaces.
	 * 			The order is guaranteed to be consistent
	 */
	public List<Namespace> getNamespacesInherited();
}
