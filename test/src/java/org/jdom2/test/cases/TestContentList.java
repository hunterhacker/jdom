package org.jdom2.test.cases;

import java.util.List;

import org.jdom2.Content;
import org.jdom2.Element;
import org.jdom2.test.util.AbstractTestList;
import org.junit.Before;

public class TestContentList extends AbstractTestList<Content> {

	public TestContentList() {
		super(Content.class, false);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Content> buildEmptyList() {
		Element e = new Element("dummy");
		return (List<Content>)e.getContent();
	}

	@Override
	public Content[] buildSampleContent() {
		return new Content[]{ new Element("zero"), 
				new Element("one"), new Element("two"), 
				new Element("three"), new Element("four"),
				new Element("five"), new Element("six")};
	}

	@Override
	public Object[] buildIllegalClassContent() {
		Object[] ret = new Object[] {
				new Integer(10),
				new StringBuilder("Hello!")
		};
		return ret;
	}
	
	@Override
	public Content[] buildIllegalArgumentContent() {
		return new Content[]{};
	}
	
	@Before
	public void detatchAll () {
		// make sure all content is detatched before each test.
		for (Content c : buildSampleContent()) {
			c.detach();
		}
	}
	
	
}
