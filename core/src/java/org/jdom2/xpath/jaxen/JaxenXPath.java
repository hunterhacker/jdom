package org.jdom2.xpath.jaxen;

import java.util.List;
import java.util.ListIterator;

import org.jaxen.BaseXPath;
import org.jaxen.JaxenException;
import org.jaxen.Navigator;

final class JaxenXPath extends BaseXPath {
	
	private static List unWrap(List results) {
		for (ListIterator it = results.listIterator(); it.hasNext(); ) {
			Object o = it.next();
			if (o instanceof NamespaceContainer) {
				it.set(((NamespaceContainer)o).getNamespace());
			}
		}
		return results;
	}
	
//	private static Object unWrap(Object o) {
//		if (o instanceof NamespaceContainer) {
//			return ((NamespaceContainer)o).getNamespace();
//		}
//		return o;
//	}
	
	public JaxenXPath(String path) throws JaxenException {
		super(path, new JDOMNavigator());
	}
	
	@Override
	public List selectNodes(Object node) throws JaxenException {
		return unWrap(super.selectNodes(node));
	}

}


