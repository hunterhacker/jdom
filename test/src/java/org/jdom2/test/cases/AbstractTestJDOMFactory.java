package org.jdom2.test.cases;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.jdom2.Attribute;
import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.EntityRef;
import org.jdom2.JDOMFactory;
import org.jdom2.Namespace;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;
import org.junit.Test;

@SuppressWarnings("javadoc")
public abstract class AbstractTestJDOMFactory {
	
	protected abstract JDOMFactory buildFactory();

	@Test
	public void testCdata() {
		CDATA cdata = buildFactory().cdata("foo");
		assertTrue(cdata != null);
		assertTrue("foo".equals(cdata.getValue()));
	}

	@Test
	public void testText() {
		Text text = buildFactory().text("foo");
		assertTrue(text != null);
		assertTrue("foo".equals(text.getValue()));
	}

	@Test
	public void testComment() {
		Comment comment = buildFactory().comment("foo");
		assertTrue(comment != null);
		assertTrue("foo".equals(comment.getText()));
	}

	@Test
	public void testAttributeStringStringNamespace() {
		Attribute att = buildFactory().attribute("att", "val", Namespace.getNamespace("p", "uri"));
		assertTrue("att".equals(att.getName()));
		assertTrue("val".equals(att.getValue()));
		assertTrue("uri".equals(att.getNamespace().getURI()));
		assertTrue(Attribute.UNDECLARED_TYPE == att.getAttributeType());
		
		att = buildFactory().attribute("att", "val", null);
		assertTrue("att".equals(att.getName()));
		assertTrue("val".equals(att.getValue()));
		assertTrue("".equals(att.getNamespace().getURI()));
		assertTrue(Attribute.UNDECLARED_TYPE == att.getAttributeType());
	}

	@Test
	public void testAttributeStringStringIntNamespace() {
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
	}

	@Test
	public void testDocTypeStringString() {
		DocType dt = buildFactory().docType("element", "system");
		assertTrue("element".equals(dt.getElementName()));
		assertTrue(null == dt.getPublicID());
		assertTrue("system".equals(dt.getSystemID()));
	}

	@Test
	public void testDocTypeString() {
		DocType dt = buildFactory().docType("element");
		assertTrue("element".equals(dt.getElementName()));
		assertTrue(null == dt.getPublicID());
		assertTrue(null == dt.getSystemID());
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
	}

	@Test
	public void testElementString() {
		Element root = buildFactory().element("root");
		assertEquals("root", root.getName());
		assertEquals("", root.getNamespace().getURI());
	}

	@Test
	public void testElementStringString() {
		Element root = buildFactory().element("root", "uri");
		assertEquals("root", root.getName());
		assertEquals("uri", root.getNamespace().getURI());
	}

	@Test
	public void testElementStringStringString() {
		Element root = buildFactory().element("root", "p", "uri");
		assertEquals("root", root.getName());
		assertEquals("p", root.getNamespace().getPrefix());
		assertEquals("uri", root.getNamespace().getURI());
	}

	@Test
	public void testProcessingInstructionString() {
		ProcessingInstruction pi = buildFactory().processingInstruction("target");
		assertEquals("target", pi.getTarget());
		assertEquals("", pi.getData());
	}

	@Test
	public void testProcessingInstructionStringMap() {
		Map<String,String> data = new HashMap<String, String>();
		data.put("key", "val");
		ProcessingInstruction pi = buildFactory().processingInstruction("target", data);
		assertEquals("target", pi.getTarget());
		assertEquals("key=\"val\"", pi.getData());
	}

	@Test
	public void testProcessingInstructionStringString() {
		ProcessingInstruction pi = buildFactory().processingInstruction("target", "data");
		assertEquals("target", pi.getTarget());
		assertEquals("data", pi.getData());
	}

	@Test
	public void testEntityRefString() {
		EntityRef er = buildFactory().entityRef("name");
		assertEquals("name", er.getName());
		assertEquals(null, er.getPublicID());
		assertEquals(null, er.getSystemID());
	}

	@Test
	public void testEntityRefStringStringString() {
		EntityRef er = buildFactory().entityRef("name", "public", "system");
		assertEquals("name", er.getName());
		assertEquals("public", er.getPublicID());
		assertEquals("system", er.getSystemID());
	}

	@Test
	public void testEntityRefStringString() {
		EntityRef er = buildFactory().entityRef("name", "system");
		assertEquals("name", er.getName());
		assertEquals(null, er.getPublicID());
		assertEquals("system", er.getSystemID());
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

}
