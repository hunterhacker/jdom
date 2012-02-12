package org.jdom2.test.cases;

import static org.jdom2.test.util.UnitTestUtil.checkException;
import static org.jdom2.test.util.UnitTestUtil.failNoException;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Content;
import org.jdom2.DocType;
import org.jdom2.Element;
import org.jdom2.test.util.AbstractTestList;
import org.jdom2.test.util.UnitTestUtil;
import org.jdom2.util.ArrayCopy;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class TestContentList extends AbstractTestList<Content> {

	public TestContentList() {
		super(Content.class, false);
	}
	
	@Override
	public List<Content> buildEmptyList() {
		Element e = new Element("dummy");
		return e.getContent();
	}

	@Override
	public Content[] buildSampleContent() {
		return new Content[]{ new Element("zero"), 
				new Element("one"), new Element("two"), 
				new Element("three"), new Element("four"),
				new Element("five"), new Element("six")};
	}
	
	@Override
	public Content[] buildAdditionalContent() {
		return new Content[]{ new Element("seven"), 
				new Element("eight")};
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
		return new Content[]{new DocType("root")};
	}
	
	@Before
	public void detatchAll () {
		// make sure all content is detatched before each test.
		for (Content c : buildSampleContent()) {
			c.detach();
		}
	}
	
	@Test
	public void testIllegalSetContent() {
		final Content[] illegal = buildIllegalArgumentContent();
		Assume.assumeTrue(illegal.length > 0);
		final Content[] extra = buildAdditionalContent();
		Assume.assumeTrue(extra.length > 0);
		final Content[] content = buildSampleContent();
		Assume.assumeTrue(content.length > 0);
		// the ' + 1' ensures a null value too!
		Content[] toadd = ArrayCopy.copyOf(extra, extra.length + illegal.length + 1);
		System.arraycopy(illegal, 0, toadd, extra.length, illegal.length);
		
		// right, we have legal content in 'content', and then in 'illegal' we
		// have some legal content, and then some illegal content.
		
		// populate the list.
		List<Content> list = buildEmptyList();
		assertTrue(list.addAll(0, Arrays.asList(content)));
		quickCheck(list, content);
		
		// OK, we have a list of attributes.... behind the scenes, we have an
		// an Element too... we need the element to get the setContents()
		// method which in turn accesses the clearAndSet().
		Element myelement = list.get(0).getParentElement();
		assertNotNull(myelement);
		
		// check that the first to-add can be added.
		list.add(0, toadd[0]);
		//then remove it again.
		assertTrue(toadd[0] == list.remove(0));
		
		quickCheck(list, content);
		
		// now, add the illegal, and then inspect the list...
		try {
			myelement.setContent(Arrays.asList(toadd));
			failNoException(IllegalArgumentException.class);
		} catch (Exception e) {
			checkException(IllegalArgumentException.class, e);
		}
		
		// make sure that the member that previously could be added can
		// still be added.
		list.add(0, toadd[0]);
		//then remove it again.
		assertTrue(toadd[0] == list.remove(0));
		
		// make sure it's all OK.
		exercise(list, content);
		
		Assume.assumeTrue(content.length >= 2);
		
		// now check to make sure that concurrency is not affected....
		Iterator<Content> it = list.iterator();
		// move it along at least once.....
		assertTrue(content[0] == it.next());
		// now do a failed addAll.
		try {
			myelement.setContent(Arrays.asList(toadd));
			failNoException(IllegalArgumentException.class);
		} catch (Exception e) {
			checkException(IllegalArgumentException.class, e);
		}
		// we should be able to move the iterator because the modCount should
		// not have been affected.....
		assertTrue(content[1] == it.next());
	}
	
	@Test
	public void testSerializable() {
		Element root = new Element("root");
		Element child = new Element("child");
		root.addContent(child);
		assertTrue(root.getContent() instanceof Serializable);
		assertTrue(root.getChildren() instanceof Serializable);
		
		Serializable from = (Serializable)(root.getChildren());
		
		Serializable clone = UnitTestUtil.deSerialize(from);
		
		assertTrue(clone instanceof List);
		
		for (Object o : (List<?>)clone) {
			assertTrue(o instanceof Element);
			assertTrue(root.indexOf((Element)o) < 0);
			// Issue #47 - broken serialization.
			// the following should pass, but does not.
			assertTrue(((Element)o).getParent() == null);
		}
		
	}
	
}
