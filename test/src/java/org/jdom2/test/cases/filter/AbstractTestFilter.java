package org.jdom2.test.cases.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.RandomAccess;

import org.jdom2.Attribute;
import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.Content;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.EntityRef;
import org.jdom2.Namespace;
import org.jdom2.Parent;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;
import org.jdom2.filter.AttributeFilter;
import org.jdom2.filter.ContentFilter;
import org.jdom2.filter.Filter;
import org.jdom2.filter.Filters;
import org.jdom2.test.util.UnitTestUtil;

@SuppressWarnings("javadoc")
public class AbstractTestFilter {
	
	protected static final void assertFilterNotEquals(Filter<?> a, Filter<?> b) {
		assertTrue("A Filter is null.", a != null);
		assertTrue("B Filter is null.", b != null);
		assertTrue (!a.equals(null));
		assertTrue (!b.equals(null));
		if (a.equals(b) || b.equals(a)) {
			fail("Filters are equals(), but they are not supposed to be: " + 
					a.toString() + " and " + b.toString());
		}
		if (a.hashCode() == b.hashCode()) {
			System.out.println("Two different (not equals() ) Filters have " +
					"the same hashCode(): " + a.hashCode() + "\n   " + 
					a.toString() + " \n   " + b.toString());
		}
		Filter<Content> base = new ContentFilter();
		assertFalse(base.refine(a).equals(base.refine(b)));
	}
	
	protected static final void assertFilterEquals(Filter<?> a, Filter<?> b) {
		assertTrue("A Filter is null.", a != null);
		assertTrue("B Filter is null.", b != null);
		assertTrue (!a.equals(null));
		assertTrue (!b.equals(null));
		if (!a.equals(a)) {
			fail("Filter " + a.toString() + " is not equals() to itself");
		}
		if (!b.equals(b)) {
			fail("Filter " + b.toString() + " is not equals() to itself");
		}
		if (!a.equals(b)) {
			fail("Filters are not equals(), but they are supposed to be: " + 
					a.toString() + " and " + b.toString());
		}
		if (!b.equals(a)) {
			fail("Filters a.equals(b), but not b.equals(a) : " + 
					a.toString() + " and " + b.toString());
		}
		if (a.hashCode() != b.hashCode()) {
			fail("Both filters are equals(), but their hashCode() values differ: " + 
					a.toString() + " and " + b.toString());
		}
		Filter<Content> base = new ContentFilter();
		assertTrue(base.refine(a).equals(base.refine(b)));
	}
	
	protected interface CallBack {
		boolean isValid(Object c);
	}
	
	protected class NegateCallBack implements CallBack {
		private final CallBack basecallback;
		public NegateCallBack(CallBack base) {
			basecallback = base;
		}
		@Override
		public boolean isValid(Object c) {
			return !basecallback.isValid(c);
		}
	}
	
	protected class AndCallBack implements CallBack {
		private final CallBack onecallback, twocallback;
		public AndCallBack(CallBack one, CallBack two) {
			onecallback = one;
			twocallback = two;
		}
		@Override
		public boolean isValid(Object c) {
			// do not want to do short-circuit || logic.
			// Make seperate statements
			boolean one = onecallback.isValid(c);
			boolean two = twocallback.isValid(c);
			return one && two;
		}
	}
	
	protected class OrCallBack implements CallBack {
		private final CallBack onecallback, twocallback;
		public OrCallBack(CallBack one, CallBack two) {
			onecallback = one;
			twocallback = two;
		}
		@Override
		public boolean isValid(Object c) {
			// do not want to do short-circuit || logic.
			// Make seperate statements
			boolean one = onecallback.isValid(c);
			boolean two = twocallback.isValid(c);
			return one || two;
		}
	}
	
	private class TrueCallBack implements CallBack {
		@Override
		public boolean isValid(Object c) {
			return true;
		}
	}
	
	private class FalseCallBack implements CallBack {
		@Override
		public boolean isValid(Object c) {
			return false;
		}
	}
	
	private final Document doc;
	private final Element root;
	private final Namespace testns;
	private final Content[] rootcontent;
	private final Content[] doccontent;
	
	protected AbstractTestFilter() {
	
		root = new Element("root");
		testns = Namespace.getNamespace("testns", "http://jdom.org/testns");
		
		Element zero = new Element("zero");
		Element one = new Element("one");
		Element two = new Element("two");
		Element three = new Element("three", testns);
		Element four = new Element("four");
		Element five = new Element("five", testns);
		Element six = new Element("six");
		Element seven = new Element("seven");
		
		EntityRef e0 = new EntityRef("erent", "ERSystemID");
		
		Text t0 = new Text("t0");
		Text t1 = new Text("t1");
		Text t2 = new Text("t2");
		Text t3 = new Text("t3");
		Text t4 = new Text("t4");
		Text t5 = new Text("t5");
		Text t6 = new Text("t6");
		Text t7 = new Text("t7");
		Text t8 = new Text("t8");
		Text t9 = new Text("t9");
		
		CDATA c0 = new CDATA("c0");
		CDATA c1 = new CDATA("c1");
		
		Comment com0 = new Comment("Comment0");
		Comment com1 = new Comment("Comment1");
		Comment com2 = new Comment("Comment2");
		
		DocType doctype = new DocType("root");
		ProcessingInstruction pi = new ProcessingInstruction ("dummy", "name=value");
		Comment doccom = new Comment("DocComment");
		doc = new Document();
		
		
		doc.addContent(doctype);
		doc.addContent(pi);
		doc.addContent(doccom);
		doc.addContent(root);
		
		doccontent = new Content[] {doctype, pi, doccom, root};
	
		rootcontent = new Content[] {t0, com0, t1, zero, t2, one, t3, com1, 
				t4, two, t5, three, c0, four, t6, e0, five, t7, six, c1, t8,
				seven, t9, com2};
		
		for (Content c : rootcontent ) {
			root.addContent(c);
		}
		
	}
	
	protected Document getDocument() {
		return doc;
	}
	
	protected Element getRoot() {
		return root;
	}
	
	protected Content[] getDocumentContent() {
		return doccontent;
	}
	
	protected Content[] getRootContent() {
		return rootcontent;
	}
	
	protected Namespace getTestNamespace() {
		return testns;
	}
	
	protected Content[] filter(Content[] input, Class<?>...types) {
		ArrayList<Content> al = new ArrayList<Content>(input.length);
		content: for (Content c : input) {
			for (Class<?> cclass : types) {
				if (cclass.isInstance(c)) {
					al.add(c);
					continue content;
				}
			}
		}
		return al.toArray(new Content[al.size()]);
	}
	
	
	protected <F extends Content> void exercise(Filter<F> af, Parent parent, CallBack callback) {
		assertTrue("filter is null", af != null);
		assertTrue("list is null", parent != null);
		assertTrue("callback is null", callback != null);
		assertTrue(af.toString() != null); // basic test to ensure toString is run.
		// can never match null if it returns a <F extends Content>
		assertFalse(af.matches(null));
		// can never match Object if it returns a <F extends Content>
		assertFalse(af.matches(new Object()));
		exerciseCore(af, parent, callback);
		
		// test the deserialized version of the Filter.
		final Filter<F> des = UnitTestUtil.deSerialize(af);
		assertTrue(des != af);
		assertFilterEquals(af, des);
		exerciseCore(des, parent, callback);
		
		try {
			Filter<?> or = af.or(null); 
			fail  ("expected an exception from " + or);
		} catch (RuntimeException re) {
			// good
		} catch (Exception e) {
			fail ("Expected a RuntimeException.");
		}
		
		try {
			Filter<F> and = af.and(null); 
			fail  ("expected an exception from " + and);
		} catch (RuntimeException re) {
			// good
		} catch (Exception e) {
			fail ("Expected a RuntimeException.");
		}
		
		exerciseCore(af.negate().refine(Filters.content()), parent, new NegateCallBack(callback));
		exerciseCore(af.or(af.negate()).refine(Filters.content()), parent, new TrueCallBack());
		exerciseCore(af.or(UnitTestUtil.deSerialize(af)).refine(Filters.content()), parent, callback);
		exerciseCore(af.negate().and(af).refine(Filters.content()), parent, new FalseCallBack());
		exerciseCore(af.and(af).refine(Filters.content()), parent, callback);
		exerciseCore(af.and(UnitTestUtil.deSerialize(af)).refine(Filters.content()), parent, callback);
		
		Filter<?> nf = af.negate();
		exerciseCore(nf.negate().refine(Filters.content()), parent, callback);
		exerciseCore(nf.or(nf.negate()).refine(Filters.content()), parent, new TrueCallBack());
		exerciseCore(nf.and(nf.negate()).refine(Filters.content()), parent, new FalseCallBack());
		

		Filter<?> afor = UnitTestUtil.deSerialize(af).or(nf);
		Filter<?> bfor = nf.or(af);
		assertFilterEquals(afor, bfor);
		
		Filter<F> afand = UnitTestUtil.deSerialize(af).and(nf);
		Filter<?> bfand = nf.and(af);
		assertFilterEquals(afand, bfand);
		
		assertFalse(af.equals(null));
		assertFalse(nf.equals(null));
		assertFalse(afor.equals(null));
		assertFalse(bfor.equals(null));
		assertFalse(afand.equals(null));
		assertFalse(bfand.equals(null));

	}
	
	private final void exerciseCore(Filter<? extends Content> ef, Parent parent, CallBack callback) {
		// exercise the toString()
		assertTrue(ef.toString() != null);
		LinkedList<Content> oc = new LinkedList<Content>();
		ArrayList<Content> mc = new ArrayList<Content>();
		List<Content> cont = parent.getContent();
		for (Content c : cont) {
			oc.add(c);
			assertTrue(parent == c.getParent());
			if (parent instanceof Document) {
				assertTrue(null == c.getParentElement());
			} else {
				assertTrue(parent == c.getParentElement());
			}
			boolean mat = ef.matches(c);
			if (mat) {
				mc.add(c);
			}
			boolean cbv = callback.isValid(c);
			if (mat != cbv) {
				fail ("Filter " + ef + " returned " + mat 
						+ " but isValid CallBack returned " + cbv 
						+ " for value " + c);
			}
		}
		List<?> fc = ef.filter(oc);
		assertTrue(fc instanceof RandomAccess);
		assertTrue(fc.size() == mc.size());
		for (int i = 0; i < fc.size(); i++) {
			assertTrue(fc.get(i) == mc.get(i));
		}
		Filter<? extends Content> cf = UnitTestUtil.deSerialize(ef);
		assertFilterEquals(cf, ef);
		ContentFilter xf = new ContentFilter();
		assertFilterEquals(cf.refine(xf), ef.refine(xf));
		assertFilterEquals(xf.refine(cf), xf.refine(ef));
		assertFalse(ef.equals(null));
		assertFilterNotEquals(ef, ef.negate());
		assertFilterNotEquals(ef.refine(xf), ef.negate().refine(xf));
		assertFilterNotEquals(xf.refine(ef), xf.refine(ef.negate()));
		List<Content> depth = new ArrayList<Content>();
		depth.addAll(cont);
		int sz = depth.size();
		for (int i = 0; i < sz; i++) {
			if (depth.get(i) instanceof Element) {
				List<Content> kdata = ((Element)depth.get(i)).getContent();
				depth.addAll(i+1, kdata);
				sz += kdata.size();
			}
		}
		Iterator<? extends Content> di = parent.getDescendants();
		
		// confirm that the DepthIterator iterates over all content.
		UnitTestUtil.testReadIterator(di, depth.toArray());
		
		List<Content> filtered = new ArrayList<Content>(depth.size());
		for (Iterator<Content> it = depth.iterator(); it.hasNext(); ) {
			Content c = it.next();
			if (callback.isValid(c)) {
				filtered.add(c);
			}
		}
		
		// 
		di = parent.getDescendants(ef);
		UnitTestUtil.testReadIterator(di, filtered.toArray());
		assertEquals(filtered, ef.filter(depth));
	}
	

	
	
	protected <F extends Attribute> void exerciseAtt(Filter<F> af, Parent parent, CallBack callback) {
		assertTrue("filter is null", af != null);
		assertTrue("list is null", parent != null);
		assertTrue("callback is null", callback != null);
		assertTrue(af.toString() != null); // basic test to ensure toString is run.
		// can never match null if it returns a <F extends Content>
		assertFalse(af.matches(null));
		// can never match Object if it returns a <F extends Content>
		assertFalse(af.matches(new Object()));
		exerciseCoreAtt(af, parent, callback);
		try {
			Filter<?> or = af.or(null); 
			fail  ("expected an exception from " + or);
		} catch (RuntimeException re) {
			// good
		} catch (Exception e) {
			fail ("Expected a RuntimeException.");
		}
		
		try {
			Filter<F> and = af.and(null); 
			fail  ("expected an exception from " + and);
		} catch (RuntimeException re) {
			// good
		} catch (Exception e) {
			fail ("Expected a RuntimeException.");
		}
		
		//exerciseCoreAtt(af.negate().refine(Filters.attribute()), parent, new NegateCallBack(callback));
		//exerciseCoreAtt(af.or(af.negate()).refine(Filters.attribute()), parent, new TrueCallBack());
		exerciseCoreAtt(af.or(UnitTestUtil.deSerialize(af)).refine(Filters.attribute()), parent, callback);
		//exerciseCoreAtt(af.negate().and(af).refine(Filters.attribute()), parent, new FalseCallBack());
		exerciseCoreAtt(af.and(af).refine(Filters.attribute()), parent, callback);
		exerciseCoreAtt(af.and(UnitTestUtil.deSerialize(af)).refine(Filters.attribute()), parent, callback);
		
		Filter<?> nf = af.negate();
//		exerciseCore(nf.negate().refine(Filters.content()), parent, callback);
//		exerciseCore(nf.or(nf.negate()).refine(Filters.content()), parent, new TrueCallBack());
//		exerciseCore(nf.and(nf.negate()).refine(Filters.content()), parent, new FalseCallBack());
		

		Filter<?> afor = UnitTestUtil.deSerialize(af).or(nf);
		Filter<?> bfor = nf.or(af);
		assertFilterEquals(afor, bfor);
		
		Filter<F> afand = UnitTestUtil.deSerialize(af).and(nf);
		Filter<?> bfand = nf.and(af);
		assertFilterEquals(afand, bfand);
		
		assertFalse(af.equals(null));
		assertFalse(nf.equals(null));
		assertFalse(afor.equals(null));
		assertFalse(bfor.equals(null));
		assertFalse(afand.equals(null));
		assertFalse(bfand.equals(null));

	}
	
	private final void exerciseCoreAtt(Filter<? extends Attribute> ef, Parent parent, CallBack callback) {
		// exercise the toString()
		assertTrue(ef.toString() != null);
		List<Content> cont = parent.getContent();
		for (Content c : cont) {
			assertTrue(parent == c.getParent());
			if (parent instanceof Document) {
				assertTrue(null == c.getParentElement());
			} else {
				assertTrue(parent == c.getParentElement());
			}
			boolean mat = ef.matches(c);
			boolean cbv = callback.isValid(c);
			if (mat != cbv) {
				fail ("Filter " + ef + " returned " + mat 
						+ " but isValid CallBack returned " + cbv 
						+ " for value " + c);
			}
		}
		Filter<? extends Attribute> cf = UnitTestUtil.deSerialize(ef);
		assertFilterEquals(cf, ef);
		AttributeFilter xf = new AttributeFilter();
		assertFilterEquals(cf.refine(xf), ef.refine(xf));
		assertFilterEquals(xf.refine(cf), xf.refine(ef));
		assertFalse(ef.equals(null));
		assertFilterNotEquals(ef, ef.negate());
		assertFilterNotEquals(ef.refine(xf), ef.negate().refine(xf));
		assertFilterNotEquals(xf.refine(ef), xf.refine(ef.negate()));
		
		List<Object> cnt = new ArrayList<Object>();
		List<Attribute> atts = new ArrayList<Attribute>();
		for (Iterator<Content> itc = parent.getDescendants(); itc.hasNext();) {
			Content c = itc.next();
			cnt.add(c);
			if (c instanceof Element) {
				cnt.addAll(((Element)c).getAttributes());
				for (Attribute a : ((Element)c).getAttributes()) {
					if (ef.matches(a)) {
						atts.add(a);
					}
				}
			}
		}
		
		List<? extends Attribute> filtered = ef.filter(cnt);
		
		assertEquals(atts, filtered);
	}
	
	
}
