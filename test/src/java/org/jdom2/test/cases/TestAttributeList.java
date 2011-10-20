package org.jdom2.test.cases;

import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Comment;
import org.jdom2.Element;
import org.jdom2.IllegalAddException;
import org.jdom2.Namespace;
import org.jdom2.Text;
import org.jdom2.test.util.AbstractTestList;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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
			fail ("expect exception");
		} catch (IllegalAddException iae) {
			// good - attribute already has parent.
		} catch (Exception e) {
			fail ("Wrong exception");
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
			fail ("expect exception");
		} catch (IllegalAddException iae) {
			// good - can not have two 'hi' attributes.
		} catch (Exception e) {
			fail ("Wrong exception");
		}
		try {
			attlist.set(1, frodo);
			fail ("expect exception");
		} catch (IllegalAddException iae) {
			// good - can not have two 'hi' attributes.
		} catch (Exception e) {
			fail ("Wrong exception");
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
			fail ("expect exception");
		} catch (IllegalAddException iae) {
			// good - attribute already has parent.
		} catch (Exception e) {
			fail ("Wrong exception");
		}
		Attribute attc = new Attribute("bilbo", "baggins", Namespace.getNamespace("mypfc", "nsc"));
		attlist.add(attc);
		try {
			attlist.set(1, attb);
			fail ("expect exception");
		} catch (IllegalAddException iae) {
			// good - attribute already has parent.
		} catch (Exception e) {
			fail ("Wrong exception");
		}
		
	}
	
}
