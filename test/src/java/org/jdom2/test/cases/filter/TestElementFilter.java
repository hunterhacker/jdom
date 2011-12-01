package org.jdom2.test.cases.filter;

import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.filter.ElementFilter;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class TestElementFilter extends AbstractTestFilter {
	
	@Test
	public void testElementFilter() {
		ElementFilter ef = new ElementFilter();
		CallBack cb = new CallBack() {
			@Override
			public boolean isValid(Object c) {
				return c != null && c instanceof Element;
			}
		};
		exercise(ef, getRoot(), cb);
		exercise(ef, getDocument(), cb);
	}

	@Test
	public void testElementFilterString() {
		final String name = "four";
		ElementFilter ef = new ElementFilter(name);
		CallBack cb = new CallBack() {
			@Override
			public boolean isValid(Object c) {
				return (c != null) && (c instanceof Element) &&
						name.equals(((Element)c).getName());
			}
		};
		exercise(ef, getRoot(), cb);
		exercise(ef, getDocument(), cb);
	}

	@Test
	public void testElementFilterNamespace() {
		ElementFilter efa = new ElementFilter(Namespace.NO_NAMESPACE);
		CallBack cba = new CallBack() {
			@Override
			public boolean isValid(Object c) {
				return (c != null) && (c instanceof Element) &&
						Namespace.NO_NAMESPACE.equals(((Element)c).getNamespace());
			}
		};
		exercise(efa, getRoot(), cba);
		exercise(efa, getDocument(), cba);

		ElementFilter efb = new ElementFilter(getTestNamespace());
		CallBack cbb = new CallBack() {
			@Override
			public boolean isValid(Object c) {
				return (c != null) && (c instanceof Element) &&
						getTestNamespace().equals(((Element)c).getNamespace());
			}
		};
		exercise(efb, getRoot(), cbb);
		exercise(efb, getDocument(), cbb);
	
		
	}

	@Test
	public void testElementFilterStringNamespace() {
		final String namea = "four";
		ElementFilter efa = new ElementFilter(namea, Namespace.NO_NAMESPACE);
		CallBack cba = new CallBack() {
			@Override
			public boolean isValid(Object c) {
				return (c != null) && (c instanceof Element) &&
						namea.equals(((Element)c).getName()) && 
						Namespace.NO_NAMESPACE.equals(((Element)c).getNamespace());
			}
		};
		exercise(efa, getRoot(), cba);
		exercise(efa, getDocument(), cba);

		final String nameb = "three";
		ElementFilter efb = new ElementFilter(nameb, getTestNamespace());
		CallBack cbb = new CallBack() {
			@Override
			public boolean isValid(Object c) {
				return (c != null) && (c instanceof Element) &&
						nameb.equals(((Element)c).getName()) && 
						getTestNamespace().equals(((Element)c).getNamespace());
			}
		};
		exercise(efb, getRoot(), cbb);
		exercise(efb, getDocument(), cbb);
	
	}

	@Test
	public void testEqualsObject() {
		final String URI1 = "http://jdom.org/test1";
		final String URI2 = "http://jdom.org/test2";
		Namespace ns1a = Namespace.getNamespace("pfxa", URI1);
		Namespace ns1b = Namespace.getNamespace("pfxb", URI1);
		Namespace ns1c = Namespace.getNamespace(URI1);
		
		Namespace ns2a = Namespace.getNamespace("pfxa", URI2);
		Namespace ns2c = Namespace.getNamespace(URI2);
		
		assertFilterEquals(new ElementFilter("test"), 
				new ElementFilter("test"));
		
		// same namespace
		assertFilterEquals(new ElementFilter("test", ns1a), 
				new ElementFilter("test", ns1a));
		
		// same namespace URI (different prefix)
		assertFilterEquals(new ElementFilter("test", ns1a), 
				new ElementFilter("test", ns1b));
		
		// same namespace URI (different prefix)
		assertFilterEquals(new ElementFilter("test", ns1a), 
				new ElementFilter("test", ns1c));
		
		// same namespace URI (different prefix)
		assertFilterEquals(new ElementFilter(ns1a), 
				new ElementFilter(ns1a));
		
		// same namespace URI (different prefix)
		assertFilterEquals(new ElementFilter(ns1a), 
				new ElementFilter(ns1b));
		
		// same namespace URI (different prefix)
		assertFilterEquals(new ElementFilter(ns1a), 
				new ElementFilter(ns1c));
		
		assertFilterNotEquals(new ElementFilter("test", ns1a), 
				new ElementFilter(ns1a));
		
		assertFilterNotEquals(new ElementFilter("test"), 
				new ElementFilter("testfoo"));

		assertFilterNotEquals(new ElementFilter("test"), 
				new ElementFilter("test", Namespace.NO_NAMESPACE));

		assertFilterNotEquals(new ElementFilter("test", ns1a), 
				new ElementFilter("test", Namespace.NO_NAMESPACE));

		assertFilterNotEquals(new ElementFilter("test", Namespace.NO_NAMESPACE), 
				new ElementFilter("test", ns1a));

		assertFilterNotEquals(new ElementFilter(ns1a), 
				new ElementFilter(ns2a));
		
		assertFilterNotEquals(new ElementFilter(ns1c), 
				new ElementFilter(ns2c));
		
		assertFilterNotEquals(new ElementFilter(ns1c), 
				new ElementFilter(ns1c).negate());
		
}

}
