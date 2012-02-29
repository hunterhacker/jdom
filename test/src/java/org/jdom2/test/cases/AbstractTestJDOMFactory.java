package org.jdom2.test.cases;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.jdom2.Attribute;
import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.Content;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.EntityRef;
import org.jdom2.JDOMFactory;
import org.jdom2.Namespace;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;
import org.jdom2.located.Located;

import org.junit.Test;

@SuppressWarnings("javadoc")
public abstract class AbstractTestJDOMFactory {
	
	private final boolean located;
	
	public AbstractTestJDOMFactory(boolean located) {
		this.located = located;
	}
	
	protected abstract JDOMFactory buildFactory();
	
	private void checkLocated(final Content c) {
		if (located) {
			assertTrue("Expected " + c + " to be Located", c instanceof Located);
			Located loc = (Located)c;
			assertTrue(1 == loc.getLine());
			assertTrue(2 == loc.getColumn());
		} else {
			assertFalse(c instanceof Located);
		}
	}

	@Test
	public void testCdata() {
		CDATA cdata = buildFactory().cdata("foo");
		assertTrue(cdata != null);
		assertTrue("foo".equals(cdata.getValue()));
		
		CDATA ldata = buildFactory().cdata(1,2,"foo");
		assertTrue(ldata != null);
		assertTrue("foo".equals(ldata.getValue()));
		checkLocated(ldata);
	}

	@Test
	public void testText() {
		Text text = buildFactory().text("foo");
		assertTrue(text != null);
		assertTrue("foo".equals(text.getValue()));
		
		Text ltext = buildFactory().text(1, 2, "foo");
		assertTrue(ltext != null);
		assertTrue("foo".equals(ltext.getValue()));
		checkLocated(ltext);
	}

	@Test
	public void testComment() {
		Comment comment = buildFactory().comment("foo");
		assertTrue(comment != null);
		assertTrue("foo".equals(comment.getText()));
		
		Comment lcomment = buildFactory().comment(1, 2, "foo");
		assertTrue(lcomment != null);
		assertTrue("foo".equals(lcomment.getText()));
		checkLocated(lcomment);
	}

	@Test
	public void testAttributeStringStringNamespace() {
		Attribute att = buildFactory().attribute("att", "val", Namespace.getNamespace("p", "uri"));
		assertTrue("att".equals(att.getName()));
		assertTrue("val".equals(att.getValue()));
		assertTrue("uri".equals(att.getNamespace().getURI()));
		assertTrue(Attribute.UNDECLARED_TYPE == att.getAttributeType());
		
		att = buildFactory().attribute("att", "val", (Namespace)null);
		assertTrue("att".equals(att.getName()));
		assertTrue("val".equals(att.getValue()));
		assertTrue("".equals(att.getNamespace().getURI()));
		assertTrue(Attribute.UNDECLARED_TYPE == att.getAttributeType());
	}

	@Test
	public void testAttributeStringStringIntNamespace() {
		@SuppressWarnings("deprecation")
		Attribute att = buildFactory().attribute("att", "val", Attribute.ID_TYPE.ordinal(), Namespace.getNamespace("p", "uri"));
		assertTrue("att".equals(att.getName()));
		assertTrue("val".equals(att.getValue()));
		assertTrue("uri".equals(att.getNamespace().getURI()));
		assertTrue(Attribute.ID_TYPE == att.getAttributeType());

		att = buildFactory().attribute("att", "val", Attribute.ID_TYPE, null);
		assertTrue("att".equals(att.getName()));
		assertTrue("val".equals(att.getValue()));
		assertTrue("".equals(att.getNamespace().getURI()));
		assertTrue(Attribute.ID_TYPE == att.getAttributeType());
	}


	@Test
	public void testAttributeStringStringAttributeTypeNamespace() {
		Attribute att = buildFactory().attribute("att", "val", Attribute.ID_TYPE, Namespace.getNamespace("p", "uri"));
		assertTrue("att".equals(att.getName()));
		assertTrue("val".equals(att.getValue()));
		assertTrue("uri".equals(att.getNamespace().getURI()));
		assertTrue(Attribute.ID_TYPE == att.getAttributeType());

		att = buildFactory().attribute("att", "val", Attribute.ID_TYPE, null);
		assertTrue("att".equals(att.getName()));
		assertTrue("val".equals(att.getValue()));
		assertTrue("".equals(att.getNamespace().getURI()));
		assertTrue(Attribute.ID_TYPE == att.getAttributeType());
	}
	
	@Test
	public void testAttributeStringString() {
		Attribute att = buildFactory().attribute("att", "val");
		assertTrue("att".equals(att.getName()));
		assertTrue("val".equals(att.getValue()));
		assertTrue("".equals(att.getNamespace().getURI()));
		assertTrue(Attribute.UNDECLARED_TYPE == att.getAttributeType());
	}

	@Test
	public void testAttributeStringStringInt() {
		@SuppressWarnings("deprecation")
		Attribute att = buildFactory().attribute("att", "val", Attribute.ID_TYPE.ordinal());
		assertTrue("att".equals(att.getName()));
		assertTrue("val".equals(att.getValue()));
		assertTrue("".equals(att.getNamespace().getURI()));
		assertTrue(Attribute.ID_TYPE == att.getAttributeType());
	}

	@Test
	public void testAttributeStringStringType() {
		Attribute att = buildFactory().attribute("att", "val", Attribute.ID_TYPE);
		assertTrue("att".equals(att.getName()));
		assertTrue("val".equals(att.getValue()));
		assertTrue("".equals(att.getNamespace().getURI()));
		assertTrue(Attribute.ID_TYPE == att.getAttributeType());
	}

	@Test
	public void testDocTypeStringStringString() {
		DocType dt = buildFactory().docType("element", "public", "system");
		assertTrue("element".equals(dt.getElementName()));
		assertTrue("public".equals(dt.getPublicID()));
		assertTrue("system".equals(dt.getSystemID()));
		
		DocType ldt = buildFactory().docType(1, 2, "element", "public", "system");
		assertTrue("element".equals(ldt.getElementName()));
		assertTrue("public".equals(ldt.getPublicID()));
		assertTrue("system".equals(ldt.getSystemID()));
		checkLocated(ldt);
	}

	@Test
	public void testDocTypeStringString() {
		DocType dt = buildFactory().docType("element", "system");
		assertTrue("element".equals(dt.getElementName()));
		assertTrue(null == dt.getPublicID());
		assertTrue("system".equals(dt.getSystemID()));
		DocType ldt = buildFactory().docType(1, 2, "element", "system");
		assertTrue("element".equals(ldt.getElementName()));
		assertTrue(null == ldt.getPublicID());
		assertTrue("system".equals(ldt.getSystemID()));
		checkLocated(ldt);
	}

	@Test
	public void testDocTypeString() {
		DocType dt = buildFactory().docType("element");
		assertTrue("element".equals(dt.getElementName()));
		assertTrue(null == dt.getPublicID());
		assertTrue(null == dt.getSystemID());
		
		DocType ldt = buildFactory().docType(1, 2, "element");
		assertTrue("element".equals(ldt.getElementName()));
		assertTrue(null == ldt.getPublicID());
		assertTrue(null == ldt.getSystemID());
		checkLocated(ldt);
	}

	@Test
	public void testElementStringNamespace() {
		Element root = buildFactory().element("root", Namespace.getNamespace("uri"));
		assertEquals("root", root.getName());
		assertEquals("uri", root.getNamespace().getURI());
		
		Namespace n = null;
		root = buildFactory().element("root", n);
		assertEquals("root", root.getName());
		assertEquals("", root.getNamespace().getURI());

		Element lroot = buildFactory().element(1, 2, "root", Namespace.getNamespace("uri"));
		checkLocated(lroot);
		
	}

	@Test
	public void testElementString() {
		Element root = buildFactory().element("root");
		assertEquals("root", root.getName());
		assertEquals("", root.getNamespace().getURI());
		
		Element lroot = buildFactory().element(1, 2, "root");
		checkLocated(lroot);
	}

	@Test
	public void testElementStringString() {
		Element root = buildFactory().element("root", "uri");
		assertEquals("root", root.getName());
		assertEquals("uri", root.getNamespace().getURI());
		
		Element lroot = buildFactory().element(1, 2, "root", "uri");
		checkLocated(lroot);
	}

	@Test
	public void testElementStringStringString() {
		Element root = buildFactory().element("root", "p", "uri");
		assertEquals("root", root.getName());
		assertEquals("p", root.getNamespace().getPrefix());
		assertEquals("uri", root.getNamespace().getURI());

		Element lroot = buildFactory().element(1, 2, "root", "p", "uri");
		checkLocated(lroot);
	}

	@Test
	public void testProcessingInstructionString() {
		ProcessingInstruction pi = buildFactory().processingInstruction("target");
		assertEquals("target", pi.getTarget());
		assertEquals("", pi.getData());
		
		ProcessingInstruction lpi = buildFactory().processingInstruction(1, 2, "target");
		checkLocated(lpi);
	}

	@Test
	public void testProcessingInstructionStringMap() {
		Map<String,String> data = new HashMap<String, String>();
		data.put("key", "val");
		ProcessingInstruction pi = buildFactory().processingInstruction("target", data);
		assertEquals("target", pi.getTarget());
		assertEquals("key=\"val\"", pi.getData());
		
		ProcessingInstruction lpi = buildFactory().processingInstruction(1, 2, "target", data);
		checkLocated(lpi);
	}

	@Test
	public void testProcessingInstructionStringString() {
		ProcessingInstruction pi = buildFactory().processingInstruction("target", "data");
		assertEquals("target", pi.getTarget());
		assertEquals("data", pi.getData());
		
		ProcessingInstruction lpi = buildFactory().processingInstruction(1, 2, "target", "data");
		checkLocated(lpi);
	}

	@Test
	public void testEntityRefString() {
		EntityRef er = buildFactory().entityRef("name");
		assertEquals("name", er.getName());
		assertEquals(null, er.getPublicID());
		assertEquals(null, er.getSystemID());

		EntityRef ler = buildFactory().entityRef(1, 2, "name");
		checkLocated(ler);
	}

	@Test
	public void testEntityRefStringStringString() {
		EntityRef er = buildFactory().entityRef("name", "public", "system");
		assertEquals("name", er.getName());
		assertEquals("public", er.getPublicID());
		assertEquals("system", er.getSystemID());

		EntityRef ler = buildFactory().entityRef(1, 2, "name", "public", "system");
		checkLocated(ler);
	}

	@Test
	public void testEntityRefStringString() {
		EntityRef er = buildFactory().entityRef("name", "system");
		assertEquals("name", er.getName());
		assertEquals(null, er.getPublicID());
		assertEquals("system", er.getSystemID());

		EntityRef ler = buildFactory().entityRef(1, 2, "name", "system");
		checkLocated(ler);
	}

	@Test
	public void testDocumentElementDocTypeString() {
		JDOMFactory fac = buildFactory();
		Element root = fac.element("root");
		DocType dt = fac.docType("root");
		Document doc = buildFactory().document(root, dt, "baseuri");
		assertTrue(doc.getRootElement() == root);
		assertTrue(doc.getDocType() == dt);
		assertEquals("baseuri", doc.getBaseURI());
		
		root.detach();
		dt.detach();

		root = null;
		doc = buildFactory().document(root, dt, "baseuri");
		assertFalse(doc.hasRootElement());
		assertTrue(doc.getDocType() == dt);
		assertEquals("baseuri", doc.getBaseURI());
	}

	@Test
	public void testDocumentElementDocType() {
		JDOMFactory fac = buildFactory();
		Element root = fac.element("root");
		DocType dt = fac.docType("root");
		Document doc = buildFactory().document(root, dt);
		assertTrue(doc.getRootElement() == root);
		assertTrue(doc.getDocType() == dt);
		assertEquals(null, doc.getBaseURI());
	}

	@Test
	public void testDocumentElement() {
		JDOMFactory fac = buildFactory();
		Element root = fac.element("root");
		Document doc = buildFactory().document(root);
		assertTrue(doc.getRootElement() == root);
		assertTrue(doc.getDocType() == null);
		assertEquals(null, doc.getBaseURI());
	}

	@Test
	public void testAddContent() {
		JDOMFactory fac = buildFactory();
		Element root = fac.element("root");
		fac.addContent(root, new Text("foo"));
		assertEquals(root.getTextNormalize(), "foo");
	}

	@Test
	public void testSetAttribute() {
		JDOMFactory fac = buildFactory();
		Element root = fac.element("root");
		fac.setAttribute(root, fac.attribute("att", "val"));
		assertEquals(root.getAttributeValue("att"), "val");
	}

	@Test
	public void testAddNamespaceDeclaration() {
		JDOMFactory fac = buildFactory();
		Element root = fac.element("root");
		Namespace nsa = Namespace.getNamespace("p", "uri");
		fac.addNamespaceDeclaration(root, nsa);
		assertTrue(root.getAdditionalNamespaces().contains(nsa));
		Namespace nsb = Namespace.getNamespace("p", "uri");
		fac.addNamespaceDeclaration(root, nsb);
		assertTrue(root.getAdditionalNamespaces().contains(nsb));
		assertTrue(root.getAdditionalNamespaces().contains(nsa));
	}
	
	@Test
	public void testSetRoot() {
		JDOMFactory fac = buildFactory();
		Document doc = fac.document(null);
		Element root = fac.element("root");
		assertFalse(doc.hasRootElement());
		assertTrue(root.getParent() == null);
		fac.setRoot(doc, root);
		assertTrue(doc.hasRootElement());
		assertTrue(doc.getRootElement() == root);
		assertTrue(root.getParent() == doc);
	}

}
