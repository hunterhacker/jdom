package org.jdom2.test.cases;

import java.util.List;

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


}
