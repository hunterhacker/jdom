package org.jdom2;

import static org.junit.Assert.*;

import org.jdom2.DefaultJDOMFactory;
import org.jdom2.IllegalAddException;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class TestIllegalAddExceptn {
	
	@Test
	public void testIllegalAddExceptionElementAttributeString() {
		DefaultJDOMFactory fac = new DefaultJDOMFactory();
		assertTrue (null != 
				new IllegalAddException(fac.element("emt"), 
						fac.attribute("att", "val"), "msg").getMessage());
	}

	@Test
	public void testIllegalAddExceptionElementElementString() {
		DefaultJDOMFactory fac = new DefaultJDOMFactory();
		assertTrue (null != 
				new IllegalAddException(fac.element("emt1"), 
						fac.element("emt2"), "msg").getMessage());
	}

	@Test
	public void testIllegalAddExceptionElementString() {
		DefaultJDOMFactory fac = new DefaultJDOMFactory();
		assertTrue (null != 
				new IllegalAddException(fac.element("emt"), 
						"msg").getMessage());
	}

	@Test
	public void testIllegalAddExceptionElementProcessingInstructionString() {
		DefaultJDOMFactory fac = new DefaultJDOMFactory();
		assertTrue (null != 
				new IllegalAddException(fac.element("emt"), 
						fac.processingInstruction("target", "data"), "msg").getMessage());
	}

	@Test
	public void testIllegalAddExceptionProcessingInstructionString() {
		DefaultJDOMFactory fac = new DefaultJDOMFactory();
		assertTrue (null != 
				new IllegalAddException(fac.processingInstruction("target", "data"), 
						"msg").getMessage());
	}

	@Test
	public void testIllegalAddExceptionElementCommentString() {
		DefaultJDOMFactory fac = new DefaultJDOMFactory();
		assertTrue (null != 
				new IllegalAddException(fac.element("emt"), 
						fac.comment("no comment!"), "msg").getMessage());
	}

	@Test
	public void testIllegalAddExceptionElementCDATAString() {
		DefaultJDOMFactory fac = new DefaultJDOMFactory();
		assertTrue (null != 
				new IllegalAddException(fac.element("emt"), 
						fac.cdata("cdata"), "msg").getMessage());
	}

	@Test
	public void testIllegalAddExceptionElementTextString() {
		DefaultJDOMFactory fac = new DefaultJDOMFactory();
		assertTrue (null != 
				new IllegalAddException(fac.element("emt"), 
						fac.text("text"), "msg").getMessage());
	}

	@Test
	public void testIllegalAddExceptionCommentString() {
		DefaultJDOMFactory fac = new DefaultJDOMFactory();
		assertTrue (null != 
				new IllegalAddException(fac.comment("no comment!"), "msg").getMessage());
	}

	@Test
	public void testIllegalAddExceptionElementEntityRefString() {
		DefaultJDOMFactory fac = new DefaultJDOMFactory();
		assertTrue (null != 
				new IllegalAddException(fac.element("emt"), 
						fac.entityRef("name"), "msg").getMessage());
	}

	@Test
	public void testIllegalAddExceptionElementNamespaceString() {
		DefaultJDOMFactory fac = new DefaultJDOMFactory();
		assertTrue (null != 
				new IllegalAddException(fac.element("emt"), 
						Namespace.getNamespace("prefix", "ns"), "msg").getMessage());
	}

	@Test
	public void testIllegalAddExceptionDocTypeString() {
		DefaultJDOMFactory fac = new DefaultJDOMFactory();
		assertTrue (null != 
				new IllegalAddException( 
						fac.docType("emt"), "msg").getMessage());
	}

	@Test
	public void testIllegalAddExceptionString() {
		assertTrue (null != 
				new IllegalAddException("msg").getMessage());
	}

}
