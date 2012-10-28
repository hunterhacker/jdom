package org.jdom2.test.cases.xpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Test;

import org.jdom2.Attribute;
import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.EntityRef;
import org.jdom2.JDOMException;
import org.jdom2.NamespaceAware;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;
import org.jdom2.input.SAXBuilder;
import org.jdom2.test.util.FidoFetch;
import org.jdom2.test.util.UnitTestUtil;
import org.jdom2.xpath.XPathDiagnostic;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.jdom2.xpath.XPathHelper;

@SuppressWarnings("javadoc")
public abstract class AbstractTestXPathHepler {
	
	abstract XPathFactory getFactory();

	@Test
	public void testGetPathStringElement() {
		Element emt = new Element("root");
		assertEquals("/root", XPathHelper.getAbsolutePath(emt));
		Element kid = new Element("kid");
		assertEquals("/kid", XPathHelper.getAbsolutePath(kid));
		emt.addContent(kid);
		assertEquals("/root/kid", XPathHelper.getAbsolutePath(kid));
	}
	
	private static final void checkAbsolute(final XPathFactory xfac, final NamespaceAware nsa) {
		String xq = null;
		if (nsa instanceof Attribute) {
			xq = XPathHelper.getAbsolutePath((Attribute)nsa);
		} else if (nsa instanceof Content) {
			xq = XPathHelper.getAbsolutePath((Content)nsa);
		} else {
			xq = "/";
		}
		System.out.println("Running XPath for " + nsa + ": " + xq);
		try {
			final XPathExpression<Object> xp = xfac.compile(xq);
			final XPathDiagnostic<Object> xd = xp.diagnose(nsa, false);
			if (xd.getResult().size() != 1) {
				fail ("expected exactly one result, not " + xd.getResult().size());
			}
			if (nsa != xd.getResult().get(0)) {
				fail ("Expect the only result for '" + xq + "' to be " + nsa + " but it was " + 
							xd.getResult().get(0));
			}
		} catch (IllegalArgumentException e) {
			String xxq = null;
			if (nsa instanceof Attribute) {
				xxq = XPathHelper.getAbsolutePath((Attribute)nsa);
			} else if (nsa instanceof Content) {
				xxq = XPathHelper.getAbsolutePath((Content)nsa);
			}
			
			AssertionError ae = new AssertionError("Unable to compile expression '" + xxq + "' to node " + nsa);
			ae.initCause(e);
			throw ae;
		}

		
	}

	private static final void checkRelative(final XPathFactory xfac, final NamespaceAware nsa, final NamespaceAware nsb) {
		String xq = null;
		if (nsa instanceof Attribute) {
			if (nsb instanceof Attribute) {
				xq = XPathHelper.getRelativePath((Attribute)nsa, (Attribute)nsb);
			} else {
				xq = XPathHelper.getRelativePath((Attribute)nsa, (Content)nsb);
			}
		} else if (nsa instanceof Content) {
			if (nsb instanceof Attribute) {
				xq = XPathHelper.getRelativePath((Content)nsa, (Attribute)nsb);
			} else {
				xq = XPathHelper.getRelativePath((Content)nsa, (Content)nsb);
			}
		} else {
			xq = "/";
		}
		final XPathExpression<Object> xp = xfac.compile(xq);
		final XPathDiagnostic<Object> xd = xp.diagnose(nsa, false);
		if(xd.getResult().size() != 1) {
			fail ("Expected single result from " + nsa + " to " + nsb + " but got " + xd);
		}
		if (nsb != xd.getResult().get(0)) {
			fail ("Expected single result to be " + nsb + " not " + xd.getResult());
		}
		
	}

	/**
	 * This test loads up an XML document and calculates the absolute expression
	 * for each node, and also the expression for each node relative to *every*
	 * other node. It then runs every expression and ensures that the exact
	 * right node is selected.
	 * The input XML document is designed to have all sorts of tricky nodes to
	 * process. This ensures that all (for a limited set of 'all') combinations
	 * of valid input data are tested.
	 * 
	 * @throws JDOMException If the document fails to parse.
	 * @throws IOException
	 */
	@Test
	public void testComplex() throws JDOMException, IOException {
		SAXBuilder sb = new SAXBuilder();
		sb.setExpandEntities(false);
		Document doc = sb.build(FidoFetch.getFido().getURL("/complex.xml"));
		final Iterator<Content> des = doc.getDescendants();
		final ArrayList<NamespaceAware> allc = new ArrayList<NamespaceAware>();
		final XPathFactory fac = getFactory();
		while (des.hasNext()) {
			final Content c = des.next();
//			if (c.getParent() == doc && c != doc.getRootElement()) {
//				// ignore document level content (except root element.
//				continue;
//			}
			checkAbsolute(fac, c);
			allc.add(c);
			if (c instanceof Element) {
				if (((Element) c).hasAttributes()) {
					for (Attribute a : ((Element)c).getAttributes()) {
						checkAbsolute(fac, a);
						allc.add(a);
					}
				}
			}
		}
		for (NamespaceAware nsa : allc) {
			for (NamespaceAware nsb : allc) {
				checkRelative(fac, nsa, nsb);
			}
		}
	}

	@Test
	public void testGetAbsolutePathAttribute() {
		// testComplex() covers working case. We need to do negative testing
		try {
			final Attribute att = null;
			XPathHelper.getAbsolutePath(att);
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
		try {
			final Attribute att = new Attribute("detached", "value");
			XPathHelper.getAbsolutePath(att);
			UnitTestUtil.failNoException(IllegalArgumentException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(IllegalArgumentException.class, e);
		}
	}

	@Test
	public void testGetAbsolutePathContent() {
		// testComplex() covers working case. We need to do negative testing
		try {
			final Text att = null;
			XPathHelper.getAbsolutePath(att);
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
		try {
			final Text att = new Text("detached");
			XPathHelper.getAbsolutePath(att);
			UnitTestUtil.failNoException(IllegalArgumentException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(IllegalArgumentException.class, e);
		}
	}

	@Test
	public void testGetRelativePathAttributeAttribute() {
		// testComplex() covers working case. We need to do negative testing
		try {
			final Attribute atta = new Attribute("att", "value");
			final Attribute attb = null;
			XPathHelper.getRelativePath(atta, attb);
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
		try {
			final Attribute atta = new Attribute("att", "value");
			final Attribute attb = new Attribute("detached", "value");
			XPathHelper.getRelativePath(atta, attb);
			UnitTestUtil.failNoException(IllegalArgumentException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(IllegalArgumentException.class, e);
		}
		try {
			final Attribute atta = null;
			final Attribute attb = new Attribute("att", "value");
			XPathHelper.getRelativePath(atta, attb);
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
	}

	@Test
	public void testGetRelativePathContentAttribute() {
		// testComplex() covers working case. We need to do negative testing
		try {
			final Element root = new Element("root");
			final Attribute att = null;
			XPathHelper.getRelativePath(root, att);
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
		try {
			final Element root = new Element("root");
			final Attribute att = new Attribute("detached", "value");
			XPathHelper.getRelativePath(root, att);
			UnitTestUtil.failNoException(IllegalArgumentException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(IllegalArgumentException.class, e);
		}
		// testComplex() covers working case. We need to do negative testing
		try {
			final Element root = null;
			final Attribute att = new Attribute("att", "value");
			XPathHelper.getRelativePath(root, att);
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
	}

	@Test
	public void testGetRelativePathAttributeContent() {
		// testComplex() covers working case. We need to do negative testing
		try {
			final Attribute att = null;
			final Element root = new Element("root");
			XPathHelper.getRelativePath(att, root);
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
		try {
			final Attribute att = new Attribute("detached", "value");
			final Element root = new Element("root");
			XPathHelper.getRelativePath(att, root);
			UnitTestUtil.failNoException(IllegalArgumentException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(IllegalArgumentException.class, e);
		}
		try {
			final Attribute att = new Attribute("detached", "value");
			final Element root = null;
			XPathHelper.getRelativePath(att, root);
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
	}

	@Test
	public void testGetRelativePathContentContent() {
		// testComplex() covers working case. We need to do negative testing
		try {
			final Element root = new Element("root");
			final Text att = null;
			XPathHelper.getRelativePath(root, att);
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
		try {
			final Element root = new Element("root");
			final Text att = new Text("detached");
			XPathHelper.getRelativePath(root, att);
			UnitTestUtil.failNoException(IllegalArgumentException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(IllegalArgumentException.class, e);
		}
		try {
			// no common ancestor
			final Element roota = new Element("root");
			final Element rootb = new Element("root");
			XPathHelper.getRelativePath(roota, rootb);
			UnitTestUtil.failNoException(IllegalArgumentException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(IllegalArgumentException.class, e);
		}
		try {
			final Element roota = null;
			final Element rootb = new Element("root");
			XPathHelper.getRelativePath(roota, rootb);
			UnitTestUtil.failNoException(NullPointerException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(NullPointerException.class, e);
		}
	}
	
	private void checkDetached(final XPathFactory fac, final NamespaceAware nsa) {
		try {
			checkAbsolute(fac, nsa);
			UnitTestUtil.failNoException(IllegalArgumentException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(IllegalArgumentException.class, e);
		}
		checkRelative(fac, nsa, nsa);
	}
	
	@Test
	public void testDetached() {
		// non-Element content...
		final XPathFactory fac = getFactory();
		checkDetached(fac, new Text("detached"));
		checkDetached(fac, new CDATA("detached"));
		checkDetached(fac, new Attribute("detached", "value"));
		checkDetached(fac, new ProcessingInstruction("detached"));
		checkDetached(fac, new EntityRef("detached"));
		checkDetached(fac, new Comment("detached"));
		
	}

}
