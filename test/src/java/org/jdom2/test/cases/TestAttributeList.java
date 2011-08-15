package org.jdom2.test.cases;

import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Comment;
import org.jdom2.Element;
import org.jdom2.Text;
import org.jdom2.test.util.AbstractTestList;
import org.junit.Before;

public class TestAttributeList extends AbstractTestList<Attribute> {
	
	private static final Element base = new Element("dummy");


	public TestAttributeList() {
		super(Attribute.class, false);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Attribute> buildEmptyList() {
		base.getAttributes().clear();
		return (List<Attribute>)base.getAttributes();
	}

	@Override
	public Attribute[] buildSampleContent() {
		return new Attribute[]{ new Attribute("zero", "val"), 
				new Attribute("one", "val"), new Attribute("two", "val"), 
				new Attribute("three", "val"), new Attribute("four", "val"),
				new Attribute("five", "val"), new Attribute("six", "val")};
	}

	@Override
	public Object[] buildIllegalClassContent() {
		Object[] ret = new Object[] {
				new Text("Hello"),
				new Comment("Hello!")
		};
		return ret;
	}
	
	@Override
	public Attribute[] buildIllegalArgumentContent() {
		return new Attribute[]{};
	};
	
	@Before
	public void detatchAll () {
		// make sure all content is detatched before each test.
		for (Attribute c : buildSampleContent()) {
			c.detach();
		}
	}
	
}
