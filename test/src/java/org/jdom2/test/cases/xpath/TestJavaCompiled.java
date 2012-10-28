package org.jdom2.test.cases.xpath;

import org.junit.Test;

import org.jdom2.contrib.xpath.java.JavaXPathFactory;
import org.jdom2.xpath.XPathFactory;

@SuppressWarnings({"javadoc"})
public class TestJavaCompiled extends AbstractTestXPathCompiled {
	
	public TestJavaCompiled() {
		super(false);
	}

	private static final XPathFactory myfac = new JavaXPathFactory();
	
	@Override
	XPathFactory getFactory() {
		return myfac;
	}
	
	@Override
	public void testAncestorOrSelfFromNamespace() {
		// nothing
	}

	@Override
	public void getXPathDouble() {
		// nothing
	}

	@Override
	public void getXPathString() {
		// nothing
	}

	@Override
	public void getXPathBoolean() {
		// nothing
	}

	@Override
	@Test
	public void testXPathPrecedingNode() {
		// we do not get items outside the root node for Document stuff.
		checkXPath("preceding::node()", child2emt, null, 
				maincomment, mainpi, maintext1, child1emt, child1text, maintext2);
	}
	
	
	@Override
	public void testDetachedAttribute() {
		// TODO Not Supported
	}

	@Override
	public void testDetachedText() {
		// TODO Not Supported
	}

	@Override
	public void testDetachedCDATA() {
		// TODO Not Supported
	}

	@Override
	public void testDetachedProcessingInstruction() {
		// TODO Not Supported
	}

	@Override
	public void testDetachedEntityRef() {
		// TODO Not Supported
	}

	@Override
	public void testDetachedComment() {
		// TODO Not Supported
	}

	@Override
	public void testDetachedElement() {
		// TODO Not Supported
	}
	
}
