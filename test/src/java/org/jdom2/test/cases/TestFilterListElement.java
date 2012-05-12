package org.jdom2.test.cases;

import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.test.util.AbstractTestList;

@SuppressWarnings("javadoc")
public class TestFilterListElement extends AbstractTestList<Element> {

	public TestFilterListElement() {
		super(Element.class, false);
	}

	@Override
	public List<Element> buildEmptyList() {
		Element root = new Element("root");
		return root.getChildren(null, Namespace.getNamespace("kidnamespace"));
	}

	@Override
	public Element[] buildSampleContent() {
		Namespace kns = Namespace.getNamespace("kidnamespace");
		return new Element[] {
				new Element("kida", kns),
				new Element("kidb", kns),
				new Element("kidc", kns),
				new Element("kidd", kns),
				new Element("kide", kns),
				new Element("kidf", kns),
				new Element("kidg", kns),
				new Element("kidh", kns),
				new Element("kidi", kns),
				new Element("kidj", kns),
		};
	}

	@Override
	public Element[] buildAdditionalContent() {
		Namespace kns = Namespace.getNamespace("kidnamespace");
		return new Element[]{ new Element("kidk", kns), 
				new Element("kidl", kns)};
	}

	@Override
	public Element[] buildIllegalArgumentContent() {
		// illegal for the filter because it has the wrong namespace.
		Element kid = new Element("kid");
		return new Element[] { kid };
	}

	@Override
	public Object[] buildIllegalClassContent() {
		return new Object[] {};
	}


	/**
	 * Issue #81 - multiple concurrent 'open' FilterLIsts do not re-sync on remove....
	 */
	@Test
	public void testMultiLists() {
		Element root = new Element("root");
		root.addContent(new Element("A"));
		root.addContent(new Element("B"));
		root.addContent(new Element("C"));
		root.addContent(new Element("A"));
		root.addContent(new Element("B"));
		root.addContent(new Element("C"));
		root.addContent(new Element("A"));
		root.addContent(new Element("B"));
		root.addContent(new Element("C"));
		root.addContent(new Element("A"));
		root.addContent(new Element("B"));
		root.addContent(new Element("C"));

		List<Element> as = root.getChildren("A");
		List<Element> bs = root.getChildren("B");
		List<Element> cs = root.getChildren("C");
		
		for (Element f : as) {
			assertTrue("A".equals(f.getName()));
		}
		for (Element f : bs) {
			assertTrue("B".equals(f.getName()));
		}
		for (Element f : cs) {
			assertTrue("C".equals(f.getName()));
		}
		
		final int bsz = bs.size();
		final int csz = cs.size();
		
		while (!as.isEmpty()) {
			
			final int sz = as.size() - 1;
			Element e = as.get(0);
			as.remove(e);
			
			assertTrue(sz == as.size());
			assertTrue(bsz == bs.size());
			assertTrue(csz == cs.size());
			
			for (Element f : as) {
				assertTrue("A".equals(f.getName()));
			}
			for (Iterator<Element> bi = bs.iterator(); bi.hasNext();) {
				assertTrue("B".equals(bi.next().getName()));
			}
			for (Element f : cs) {
				assertTrue("C".equals(f.getName()));
			}
		}
		
		
	}
}
