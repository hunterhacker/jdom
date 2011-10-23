package org.jdom2.test.cases;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Comment;
import org.jdom2.Element;
import org.jdom2.IllegalAddException;
import org.jdom2.Namespace;
import org.jdom2.Text;
import org.jdom2.test.util.AbstractTestList;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.jdom2.test.util.UnitTestUtil.*;

@SuppressWarnings("javadoc")
public class TestAttributeList extends AbstractTestList<Attribute> {
	
	private static final Element base = new Element("dummy");


	public TestAttributeList() {
		super(Attribute.class, false);
	}
	
	@Override
	public List<Attribute> buildEmptyList() {
		base.getAttributes().clear();
		return base.getAttributes();
	}

	@Override
	public Attribute[] buildSampleContent() {
		return new Attribute[]{ new Attribute("zero", "val"), 
				new Attribute("one", "val"), new Attribute("two", "val"), 
				new Attribute("three", "val"), new Attribute("four", "val"),
				new Attribute("five", "val"), new Attribute("six", "val"),
				new Attribute("att", "val", Namespace.getNamespace("pfx", "nsX"))
		};
	}
	
	

	@Override
	public Attribute[] buildAdditionalContent() {
		return new Attribute[]{ new Attribute("seven", "val"),
				new Attribute("eight", "val")};
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
		// this is illegal because it redefines the namespace from uri nsY to nsZ
		return new Attribute[]{
				new Attribute("att", "val", Namespace.getNamespace("pfx", "nsY")),
				new Attribute("att", "val", Namespace.getNamespace("pfx", "nsZ"))
				
		};
	}
	
	@Before
	public void detatchAll () {
		// make sure all content is detatched before each test.
		for (Attribute c : buildSampleContent()) {
			c.detach();
		}
	}
	
	@Test
	public void testDuplicateAttribute() {
		Element emt = new Element("mine");
		List<Attribute> attlist = emt.getAttributes();
		Attribute att = new Attribute("hi", "there");
		Attribute frodo = new Attribute("hi", "frodo");
		Attribute bilbo = new Attribute("boo", "bilbo");
		attlist.add(att);
		try {
			Element e2 = new Element("gandalph");
			e2.setAttribute(frodo);
			attlist.add(frodo);
			failNoException(IllegalAddException.class);
		} catch (Exception e) {
			checkException(IllegalAddException.class, e);
		}
		frodo.detach();
		
		// adding one 'hi' attribute should displace the other.
		assertTrue(att.getParent() == emt);
		assertTrue(attlist.add(frodo));
		assertTrue(att.getParent() == null);
		assertTrue(attlist.add(att));
		assertTrue(att.getParent() == emt);
		assertTrue(att == att.detach());
		
		// list is now empty.
		assertTrue(attlist.isEmpty());
		assertTrue(attlist.add(frodo));
		assertTrue(attlist.add(bilbo));
		assertTrue(frodo == attlist.set(0, att));
		
		try {
			attlist.add(attlist.size(), frodo);
			failNoException(IllegalAddException.class);
		} catch (Exception e) {
			checkException(IllegalAddException.class, e);
		}
		try {
			attlist.set(1, frodo);
			failNoException(IllegalAddException.class);
		} catch (Exception e) {
			checkException(IllegalAddException.class, e);
		}
	}
	
	@Test
	public void testAttributeNamspaceCollision() {
		Element emt = new Element("mine");
		List<Attribute> attlist = emt.getAttributes();
		Attribute atta = new Attribute("hi", "there", Namespace.getNamespace("mypfx", "nsa"));
		Attribute attb = new Attribute("hi", "there", Namespace.getNamespace("mypfx", "nsb"));
		attlist.add(atta);
		try {
			attlist.add(attb);
			failNoException(IllegalAddException.class);
		} catch (Exception e) {
			checkException(IllegalAddException.class, e);
		}
		Attribute attc = new Attribute("bilbo", "baggins", Namespace.getNamespace("mypfc", "nsc"));
		attlist.add(attc);
		try {
			attlist.set(1, attb);
			failNoException(IllegalAddException.class);
		} catch (Exception e) {
			checkException(IllegalAddException.class, e);
		}
		
	}
	
	@Test
	public void testSetAttributes() {
		final Attribute[] extra = buildAdditionalContent();
		Assume.assumeTrue(extra.length > 0);
		final Attribute[] content = buildSampleContent();
		Assume.assumeTrue(content.length > 0);

		// populate the list.
		List<Attribute> list = buildEmptyList();
		assertTrue(list.addAll(0, Arrays.asList(content)));
		quickCheck(list, content);
		
		// OK, we have a list of attributes.... behind the scenes, we have an
		// an Element too... we need the element to get the setAttributes()
		// method which in turn accesses the clearAndSet().
		Element myelement = list.get(0).getParentElement();
		assertNotNull(myelement);
		
		ArrayList<Attribute> toset = new ArrayList<Attribute>(extra.length);
		toset.addAll(Arrays.asList(extra));
		
		// OK, test the setAttributes first.
		assertTrue(myelement == myelement.setAttributes(toset));
		// attributes should be the new ones.
		quickCheck(list, extra);
		// restore the old ones.
		assertTrue(myelement == myelement.setAttributes(Arrays.asList(content)));
		// ensure an empty list clears...
		toset.clear();
		assertTrue(myelement == myelement.setAttributes(toset));
		assertTrue(list.isEmpty());
		// restore the old ones.
		assertTrue(myelement == myelement.setAttributes(Arrays.asList(content)));
		// ensure a null list clears...
		toset = null;
		assertTrue(myelement == myelement.setAttributes(toset));
		assertTrue(list.isEmpty());
		
		
	}
	
	
	@Test
	public void testIllegalSetAttributes() {
		final Attribute[] illegal = buildIllegalArgumentContent();
		Assume.assumeTrue(illegal.length > 0);
		final Attribute[] extra = buildAdditionalContent();
		Assume.assumeTrue(extra.length > 0);
		final Attribute[] content = buildSampleContent();
		Assume.assumeTrue(content.length > 0);
		// the ' + 1' ensures a null value too!
		Attribute[] toadd = Arrays.copyOf(extra, extra.length + illegal.length + 1);
		System.arraycopy(illegal, 0, toadd, extra.length, illegal.length);
		
		// right, we have legal content in 'content', and then in 'illegal' we
		// have some legal content, and then some illegal content.
		
		// populate the list.
		List<Attribute> list = buildEmptyList();
		assertTrue(list.addAll(0, Arrays.asList(content)));
		quickCheck(list, content);
		
		// OK, we have a list of attributes.... behind the scenes, we have an
		// an Element too... we need the element to get the setAttributes()
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
			myelement.setAttributes(Arrays.asList(toadd));
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
		Iterator<Attribute> it = list.iterator();
		// move it along at least once.....
		assertTrue(content[0] == it.next());
		// now do a failed addAll.
		try {
			myelement.setAttributes(Arrays.asList(toadd));
			failNoException(IllegalArgumentException.class);
		} catch (Exception e) {
			checkException(IllegalArgumentException.class, e);
		}
		// we should be able to move the iterator because the modCount should
		// not have been affected.....
		assertTrue(content[1] == it.next());
	}
	
}
