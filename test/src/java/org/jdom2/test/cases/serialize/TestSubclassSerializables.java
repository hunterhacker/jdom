package org.jdom2.test.cases.serialize;

import static org.jdom2.test.util.UnitTestUtil.deSerialize;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class TestSubclassSerializables {

	@Test
	public void testFilter() {
		deSerialize(new SFilter());
	}

	@Test
	public void testAttribute() {
		deSerialize(new SAttribute());
	}

	@Test
	public void testCDATA() {
		deSerialize(new SComment());
	}

	@Test
	public void testComment() {
		deSerialize(new SComment());
	}

	@Test
	public void testDocType() {
		deSerialize(new SDocType());
	}

	@Test
	public void testElement() {
		deSerialize(new SElement());
	}

	@Test
	public void testEntityRef() {
		deSerialize(new SEntityRef());
	}

	@Test
	public void testProcessingInstruction() {
		deSerialize(new SProcessingInstruction());
	}

	@Test
	public void testText() {
		deSerialize(new SText());
	}

}
