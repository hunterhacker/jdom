package org.jdom2.test.cases.filter;

import org.jdom2.Attribute;
import org.jdom2.Namespace;
import org.jdom2.filter.AttributeFilter;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class TestAtributeFilter extends AbstractTestFilter {

	@Test
	public void testAttributeFilter() {
		AttributeFilter ef = new AttributeFilter();
		CallBack cb = new CallBack() {
			@Override
			public boolean isValid(Object c) {
				return c != null && c instanceof Attribute;
			}
		};
		exerciseAtt(ef, getRoot(), cb);
		exerciseAtt(ef, getDocument(), cb);
	}

	@Test
	public void testElementFilterString() {
		final String name = "four";
		AttributeFilter ef = new AttributeFilter(name);
		CallBack cb = new CallBack() {
			@Override
			public boolean isValid(Object c) {
				return (c != null) && (c instanceof Attribute) &&
						name.equals(((Attribute)c).getName());
			}
		};
		exerciseAtt(ef, getRoot(), cb);
		exerciseAtt(ef, getDocument(), cb);
	}

	@Test
	public void testElementFilterNamespace() {
		AttributeFilter efa = new AttributeFilter(Namespace.NO_NAMESPACE);
		CallBack cba = new CallBack() {
			@Override
			public boolean isValid(Object c) {
				return (c != null) && (c instanceof Attribute) &&
						Namespace.NO_NAMESPACE.equals(((Attribute)c).getNamespace());
			}
		};
		exerciseAtt(efa, getRoot(), cba);
		exerciseAtt(efa, getDocument(), cba);

		AttributeFilter efb = new AttributeFilter(getTestNamespace());
		CallBack cbb = new CallBack() {
			@Override
			public boolean isValid(Object c) {
				return (c != null) && (c instanceof Attribute) &&
						getTestNamespace().equals(((Attribute)c).getNamespace());
			}
		};
		exerciseAtt(efb, getRoot(), cbb);
		exerciseAtt(efb, getDocument(), cbb);


	}

	@Test
	public void testElementFilterStringNamespace() {
		final String namea = "four";
		AttributeFilter efa = new AttributeFilter(namea, Namespace.NO_NAMESPACE);
		CallBack cba = new CallBack() {
			@Override
			public boolean isValid(Object c) {
				return (c != null) && (c instanceof Attribute) &&
						namea.equals(((Attribute)c).getName()) && 
						Namespace.NO_NAMESPACE.equals(((Attribute)c).getNamespace());
			}
		};
		exerciseAtt(efa, getRoot(), cba);
		exerciseAtt(efa, getDocument(), cba);

		final String nameb = "three";
		AttributeFilter efb = new AttributeFilter(nameb, getTestNamespace());
		CallBack cbb = new CallBack() {
			@Override
			public boolean isValid(Object c) {
				return (c != null) && (c instanceof Attribute) &&
						nameb.equals(((Attribute)c).getName()) && 
						getTestNamespace().equals(((Attribute)c).getNamespace());
			}
		};
		exerciseAtt(efb, getRoot(), cbb);
		exerciseAtt(efb, getDocument(), cbb);

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

		assertFilterEquals(new AttributeFilter("test"), 
				new AttributeFilter("test"));

		// same namespace
		assertFilterEquals(new AttributeFilter("test", ns1a), 
				new AttributeFilter("test", ns1a));

		// same namespace URI (different prefix)
		assertFilterEquals(new AttributeFilter("test", ns1a), 
				new AttributeFilter("test", ns1b));

		// same namespace URI (different prefix)
		assertFilterEquals(new AttributeFilter("test", ns1a), 
				new AttributeFilter("test", ns1c));

		// same namespace URI (different prefix)
		assertFilterEquals(new AttributeFilter(ns1a), 
				new AttributeFilter(ns1a));

		// same namespace URI (different prefix)
		assertFilterEquals(new AttributeFilter(ns1a), 
				new AttributeFilter(ns1b));

		// same namespace URI (different prefix)
		assertFilterEquals(new AttributeFilter(ns1a), 
				new AttributeFilter(ns1c));

		assertFilterNotEquals(new AttributeFilter("test", ns1a), 
				new AttributeFilter(ns1a));

		assertFilterNotEquals(new AttributeFilter("test"), 
				new AttributeFilter("testfoo"));

		assertFilterNotEquals(new AttributeFilter("test"), 
				new AttributeFilter("test", Namespace.NO_NAMESPACE));

		assertFilterNotEquals(new AttributeFilter("test", ns1a), 
				new AttributeFilter("test", Namespace.NO_NAMESPACE));

		assertFilterNotEquals(new AttributeFilter("test", Namespace.NO_NAMESPACE), 
				new AttributeFilter("test", ns1a));

		assertFilterNotEquals(new AttributeFilter(ns1a), 
				new AttributeFilter(ns2a));

		assertFilterNotEquals(new AttributeFilter(ns1c), 
				new AttributeFilter(ns2c));

		assertFilterNotEquals(new AttributeFilter(ns1c), 
				new AttributeFilter(ns1c).negate());

	}

}
