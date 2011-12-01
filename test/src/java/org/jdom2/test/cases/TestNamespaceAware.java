package org.jdom2.test.cases;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.Text;
import org.jdom2.test.util.UnitTestUtil;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class TestNamespaceAware {

	@Test
	public void testNamespacesScopeSimple() {
		Element emt = new Element("root");
		
		UnitTestUtil.testNamespaceIntro(emt);
		UnitTestUtil.testNamespaceScope(emt, Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE);
		
	}

	@Test
	public void testNamespacesAttributeDetach() {
		Attribute att = new Attribute("att", "value");
		
		UnitTestUtil.testNamespaceIntro(att, Namespace.NO_NAMESPACE);
		UnitTestUtil.testNamespaceScope(att, Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE);
		
	}

	@Test
	public void testNamespacesAttributeDetachNS() {
		Namespace ns = Namespace.getNamespace("pfx", "nspfx");
		Attribute att = new Attribute("att", "value", ns);
		
		UnitTestUtil.testNamespaceIntro(att, ns);
		UnitTestUtil.testNamespaceScope(att, ns, Namespace.XML_NAMESPACE);
		
	}

	@Test
	public void testNamespacesAttributeDetachZZZ() {
		// order should not change with zzz prefix
		Namespace ns = Namespace.getNamespace("zzz", "nspfx");
		Attribute att = new Attribute("att", "value", ns);
		
		UnitTestUtil.testNamespaceIntro(att, ns);
		UnitTestUtil.testNamespaceScope(att, ns, Namespace.XML_NAMESPACE);
		
	}

	@Test
	public void testNamespacesText() {
		// order should not change with zzz prefix
		Text txt = new Text("txt");
		
		UnitTestUtil.testNamespaceIntro(txt);
		UnitTestUtil.testNamespaceScope(txt, Namespace.XML_NAMESPACE);
		
	}

	@Test
	public void testNamespacesScopeSimpleAdded() {
		Element emt = new Element("root");
		Namespace pfx = Namespace.getNamespace("pfx", "nsuri");
		emt.addNamespaceDeclaration(pfx);
		
		UnitTestUtil.testNamespaceIntro(emt, pfx);
		UnitTestUtil.testNamespaceScope(emt, Namespace.NO_NAMESPACE, pfx, Namespace.XML_NAMESPACE);
		
	}

	@Test
	public void testNamespacesScopeSimpleElement() {
		Namespace pfx = Namespace.getNamespace("pfx", "nsuri");
		Element emt = new Element("root", pfx);
		
		// just to mix it up, double-up the declaration.
		// cover a condition in getNamespacesInScope();
		emt.addNamespaceDeclaration(pfx);
		
		UnitTestUtil.testNamespaceIntro(emt, pfx);
		UnitTestUtil.testNamespaceScope(emt, pfx, Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE);
		
	}
	
	@Test
	public void testNamespacesScopeDeepElement() {
		Namespace pfx = Namespace.getNamespace("pfx", "nsuri");
		Namespace pfy = Namespace.getNamespace("pfy", "nsyyy");
		Element emt = new Element("root", pfx);
		Element kid = new Element("kid", pfy);
		emt.addContent(kid);
		
		UnitTestUtil.testNamespaceIntro(emt, pfx);
		UnitTestUtil.testNamespaceScope(emt, pfx, Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE);
		
		UnitTestUtil.testNamespaceIntro(kid, pfy);
		UnitTestUtil.testNamespaceScope(kid, pfy, Namespace.NO_NAMESPACE, pfx, Namespace.XML_NAMESPACE);
		
	}
	
	@Test
	public void testNamespacesScopeSimpleAttribute() {
		Namespace pfx = Namespace.getNamespace("pfx", "nsuri");
		Element emt = new Element("root");
		Attribute att = new Attribute("att", "val", pfx);
		emt.setAttribute(att);

		UnitTestUtil.testNamespaceIntro(emt, pfx);
		UnitTestUtil.testNamespaceScope(emt, Namespace.NO_NAMESPACE, pfx, Namespace.XML_NAMESPACE);
		
		UnitTestUtil.testNamespaceIntro(att);
		UnitTestUtil.testNamespaceScope(att, pfx, Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE);
				
	}


	@Test
	public void testNamespacesScopeSimpleAttributeNoNs() {
		Namespace pfx = Namespace.getNamespace("pfx", "nsuri");
		Element emt = new Element("root", pfx);
		Attribute att = new Attribute("att", "val");
		emt.setAttribute(att);

		UnitTestUtil.testNamespaceIntro(emt, pfx);
		UnitTestUtil.testNamespaceScope(emt, pfx, Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE);
				
		UnitTestUtil.testNamespaceIntro(att);
		UnitTestUtil.testNamespaceScope(att, Namespace.NO_NAMESPACE, pfx, Namespace.XML_NAMESPACE);
	}

	@Test
	public void testNamespacesScopeDocument() {
		Element emt = new Element("root");
		Document doc = new Document(emt);

		UnitTestUtil.testNamespaceIntro(doc, Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE);
		UnitTestUtil.testNamespaceScope(doc, Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE);
				
	}

	@Test
	public void testNamespacesScopeTreeAttributeNoNs() {
		Namespace pfx = Namespace.getNamespace("pfx", "nsuri");
		Element emt = new Element("root", pfx);
		// note that 'kid' is in the NO_NAMESPACE namespace.
		Element kid = new Element("kid");
		Text txt = new Text("txt");
		kid.addContent(txt);
		emt.addContent(kid);
		emt.setAttribute("att", "val");
		
		Namespace kfx = Namespace.getNamespace("kfx", "nskid");
		kid.addNamespaceDeclaration(kfx);

		UnitTestUtil.testNamespaceIntro(emt, pfx);
		UnitTestUtil.testNamespaceScope(emt, pfx, Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE);
				
		
		UnitTestUtil.testNamespaceIntro(kid, kfx);
		UnitTestUtil.testNamespaceScope(kid, Namespace.NO_NAMESPACE, kfx, pfx, Namespace.XML_NAMESPACE);
		
		UnitTestUtil.testNamespaceIntro(txt);
		UnitTestUtil.testNamespaceScope(txt, Namespace.NO_NAMESPACE, kfx, pfx, Namespace.XML_NAMESPACE);
		
	}
	
}
