package org.jdom2.test.cases.xpath;

import org.junit.Test;

import org.jdom2.contrib.xpath.xalan.XalanXPathFactory;
import org.jdom2.xpath.XPathFactory;

@SuppressWarnings({"javadoc"})
public class TestXalanCompiled extends AbstractTestXPathCompiled {
	
	public TestXalanCompiled() {
		super(true);
	}

	private static final XPathFactory myfac = new XalanXPathFactory();
	
	@Override
	XPathFactory getFactory() {
		return myfac;
	}
	
	@Override
	public void testAncestorOrSelfFromNamespace() {
		// nothing... can't set a Namespace as a context in Xalan;
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
