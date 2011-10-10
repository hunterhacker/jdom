package org.jdom2.test.cases;

import java.util.List;

import org.jdom2.Element;
import org.jdom2.Text;
import org.jdom2.test.util.AbstractTestList;

public class TestFilterListElement extends AbstractTestList<Element> {
	
	public TestFilterListElement() {
		super(Element.class, false);
	}

	@Override
	public List<Element> buildEmptyList() {
		Element root = new Element("root");
		return root.getChildren();
	}

	@Override
	public Element[] buildSampleContent() {
		return new Element[] {
				new Element("kida"),
				new Element("kidb"),
				new Element("kidc"),
				new Element("kidd"),
				new Element("kide"),
				new Element("kidf"),
				new Element("kidg"),
				new Element("kidh"),
				new Element("kidi"),
				new Element("kidj"),
		};
	}

	@Override
	public Element[] buildIllegalArgumentContent() {
		Element pnt = new Element("parent");
		Element kid = new Element("kid");
		pnt.addContent(kid);
		return new Element[] { kid };
	}

	@Override
	public Object[] buildIllegalClassContent() {
		return new Object[] {
				new Text("text"),
				new Object()
		};
	}


}
