package org.jdom2.test.cases.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.util.NamespaceStack;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class TestNamespaceStack {
	
	// We do a little cheat in this test class.
	// NamespaceStack is supposed to make the concept of the
	// Content.getNamespacesInScope() type methods more efficient.
	// so, we simply cheat by comparing the one against the other.
	// We have confidence in the Content.getNamespaces* methods because they are
	// tested elsewhere.
	
	private static final void checkIterable(Iterable<Namespace> abl, Namespace...values) {
		int cnt = 0;
		for (Namespace ns : abl) {
			if (cnt >= values.length) {
				fail("Unexpected extra Namespace: " + ns);
			}
			if (ns != values[cnt]) {
				fail("We expected Namespace " + values[cnt] + " but instead we got " + ns);
			}
			cnt++;
		}
		if (cnt <values.length) {
			fail("We expected an additional " + (values.length - cnt) + " Namespaces, starting with " + values[cnt]);
		}
		Iterator<Namespace> it = abl.iterator();
		while (--cnt >= 0) {
			it.next();
		}
		
		assertFalse(it.hasNext());
		
		try {
			it.remove();
			fail("Should not be able to remove content from this iterator.");
		} catch (UnsupportedOperationException uoe) {
			// good.
		} catch (Exception e) {
			e.printStackTrace();
			fail("expected UnsupportedOperationException but got :" + e.getClass());
		}
		
		try {
			it.next();
			fail("Should not be able to iterate beyond the iterator.");
		} catch (NoSuchElementException nsee) {
			// good.
		} catch (Exception e) {
			e.printStackTrace();
			fail("expected NoSuchElementException but got :" + e.getClass());
		}
	}
	
	private static final void reverse(Namespace[] data) {
		Namespace tmp = null;
		int left = 0, right = data.length - 1;
		while (left < right) {
			tmp = data[left];
			data[left] = data[right];
			data[right] = tmp;
			left++;
			right--;
		}
	}
	
	private static final void checkIterators(NamespaceStack stack, 
			List<Namespace> lscope, List<Namespace> lintro) {
		
		Namespace[] scope = lscope.toArray(new Namespace[0]);
		checkIterable(stack, scope);
		Namespace[] intro = lintro.toArray(new Namespace[0]);
		checkIterable(stack.addedForward(), intro);
		reverse(intro);
		checkIterable(stack.addedReverse(), intro);
	}
	
	private void exercise(Element emt, NamespaceStack stack) {
		List<Namespace> scope = emt.getNamespacesInScope();
		List<Namespace> intro = emt.getNamespacesIntroduced();
		
		for (Namespace ns : intro) {
			assertFalse(stack.isInScope(ns));
		}
		
		stack.push(emt);
		
		for (Namespace ns : intro) {
			assertTrue(stack.isInScope(ns));
		}
		
		
		checkIterators(stack, scope, intro);
		

		for (Element e : emt.getChildren()) {
			exercise(e, stack);
		}
		
		checkIterators(stack, scope, intro);
		stack.pop();
	}
	
	@Test
	public void testEmptyStack() {
		Namespace[] scopea = new Namespace[] {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
		List<Namespace> scopel = Arrays.asList(scopea);
		
		NamespaceStack stack = new NamespaceStack();
		
		checkIterators(stack, scopel, scopel);
		
		try {
			stack.pop();
			fail("Should not be able to over-pop the stack."); 
		} catch (IllegalStateException ise) {
			// good.
		} catch (Exception e) {
			e.printStackTrace();
			fail("Expected IllegalStateException but got " + e.getClass());
		}
		
	}
	
	@Test
	public void testSimpleEment() {
		Element root = new Element("root");
		exercise(root, new NamespaceStack());
	}

	@Test
	public void testTwentyDeepEment() {
		Element root = new Element("root");
		int cnt = 20;
		Element e = root;
		while (--cnt >= 0) {
			Element c = new Element("kid", Namespace.getNamespace("Level:" + cnt));
			e.addContent(c);
			e = c;
		}
		exercise(root, new NamespaceStack());
	}
	
	@Test
	public void testChangeMainNoOthers() {
		Element root = new Element("root", Namespace.getNamespace("rooturl"));
		// child is in a different namespace but with same "" prefix.
		Element child = new Element("child", Namespace.getNamespace("childurl"));
		// leaf is in the NO_NAMESPACE namespace (also prefix "");
		Element leaf = new Element("leaf");
		
		root.addContent(child);
		child.addContent(leaf);
		exercise(root, new NamespaceStack());
	}

	@Test
	public void testChangeMainWithOthers() {
		Namespace nsa = Namespace.getNamespace("pfxa", "nsurla");
		Namespace nsb = Namespace.getNamespace("pfxb", "nsurlb");
		Namespace nsc = Namespace.getNamespace("pfxc", "nsurlc");
		Namespace nsd = Namespace.getNamespace("pfxd", "nsurld");
		
		Element root = new Element("root", Namespace.getNamespace("rooturl"));
		// child is in a different namespace but with same "" prefix.
		Element child = new Element("child", Namespace.getNamespace("childurl"));
		// leaf is in the NO_NAMESPACE namespace (also prefix "");
		Element leaf = new Element("leaf");
		
		root.addNamespaceDeclaration(nsa);
		root.addNamespaceDeclaration(nsb);
		root.addNamespaceDeclaration(nsc);
		root.addNamespaceDeclaration(nsd);
		
		child.addNamespaceDeclaration(nsa);
		child.addNamespaceDeclaration(nsb);
		child.addNamespaceDeclaration(nsc);
		child.addNamespaceDeclaration(nsd);

		// On the leaf we try the varaint of adding the namespace
		// via the attributes.
		leaf.setAttribute("att", "val", nsa);
		leaf.setAttribute("att", "val", nsb);
		leaf.setAttribute("att", "val", nsc);
		leaf.setAttribute("att", "val", nsd);
		
		root.addContent(child);
		child.addContent(leaf);
		exercise(root, new NamespaceStack());
	}

	@Test
	public void testChangeAltNoOthers() {
		// note, they all have same prefix.
		Namespace nsa = Namespace.getNamespace("pfxa", "nsurla");
		Namespace nsb = Namespace.getNamespace("pfxa", "nsurlb");
		Namespace nsc = Namespace.getNamespace("pfxa", "nsurlc");
		
		Element root = new Element("root");
		Element child = new Element("child");
		Element leaf = new Element("leaf");
		
		root.addNamespaceDeclaration(nsa);
		
		child.addNamespaceDeclaration(nsb);

		// On the leaf we try the varaint of adding the namespace
		// via the attributes.
		leaf.setAttribute("att", "val", nsc);
		
		root.addContent(child);
		child.addContent(leaf);
		exercise(root, new NamespaceStack());
	}

	@Test
	public void testChangeAltWithOthers() {
		// note, they all have same prefix.
		Namespace nsa = Namespace.getNamespace("pfxa", "nsurla");
		Namespace nsb = Namespace.getNamespace("pfxa", "nsurlb");
		Namespace nsc = Namespace.getNamespace("pfxa", "nsurlc");
		Namespace alta = Namespace.getNamespace("alta", "nsalturla");
		Namespace altb = Namespace.getNamespace("altb", "nsalturlb");
		
		Element root = new Element("root");
		Element child = new Element("child");
		Element leaf = new Element("leaf");
		
		root.addNamespaceDeclaration(nsa);
		root.addNamespaceDeclaration(alta);
		root.addNamespaceDeclaration(altb);
		
		child.addNamespaceDeclaration(nsb);
		child.addNamespaceDeclaration(alta);
		child.addNamespaceDeclaration(altb);

		// On the leaf we try the varaint of adding the namespace
		// via the attributes.
		leaf.setAttribute("att", "val", nsc);
		leaf.addNamespaceDeclaration(alta);
		leaf.addNamespaceDeclaration(altb);
		
		root.addContent(child);
		child.addContent(leaf);
		exercise(root, new NamespaceStack());
	}
	
	
	@Test
	public void testSwapMainAlt() {
		// note, they all have same prefix.
		Namespace nsa = Namespace.getNamespace("pfxa", "nsurla");
		Namespace nsb = Namespace.getNamespace("pfxb", "nsurlb");
		
		// Note how they have the same namespaces, just swap between
		// Element and Attribute.
		Element root = new Element("root", nsa);
		root.setAttribute("att", "val", nsb);
		
		Element child = new Element("child", nsb);
		child.setAttribute("att", "val", nsa);
		
		root.addContent(child);

		exercise(root, new NamespaceStack());
	}

	@Test
	public void testAttributeSpecialCases() {
		// note, they all have same prefix.
		Namespace nsa = Namespace.getNamespace("pfxa", "nsurla");
		Namespace nsb = Namespace.getNamespace("pfxb", "nsurlb");
		
		// Note how they have the same namespaces, just swap between
		// Element and Attribute.
		Element root = new Element("root", nsa);
		// attribute has same Namespace as element
		root.setAttribute("att", "val", nsa);
		
		Element child = new Element("child", nsb);
		// Attribute has no namespace.
		child.setAttribute("att", "val");
		
		root.addContent(child);

		exercise(root, new NamespaceStack());
	}
	
	@Test
	public void testAdditionalSpecialCases() {
		// note, they all have same prefix.
		Namespace nsa = Namespace.getNamespace("pfxa", "nsurla");
		
		// Note how they have the same namespaces, just swap between
		// Element and Attribute.
		Element root = new Element("root", nsa);
		// additional has same Namespace as element
		root.addNamespaceDeclaration(nsa);
		
		exercise(root, new NamespaceStack());
	}
	
	@Test
	public void testSeededConstructor() {
		Namespace x = Namespace.getNamespace("X");
		Namespace y = Namespace.getNamespace("y", "Y");
		Namespace[] nsa = new Namespace[] {
				x, Namespace.XML_NAMESPACE
		};
		NamespaceStack nstack = new NamespaceStack(nsa);
		checkIterable(nstack, nsa);
		checkIterable(nstack.addedForward(), nsa);
		Element emt = new Element("root", y);
		emt.addNamespaceDeclaration(Namespace.NO_NAMESPACE);
		nstack.push(emt);
		checkIterable(nstack, y, Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE);
		checkIterable(nstack.addedForward(), y, Namespace.NO_NAMESPACE);
	}
}
