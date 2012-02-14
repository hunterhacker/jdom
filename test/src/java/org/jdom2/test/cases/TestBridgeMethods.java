package org.jdom2.test.cases;

import static org.jdom2.test.util.UnitTestUtil.compare;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.Test;

import org.jdom2.Attribute;
import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.EntityRef;
import org.jdom2.NamespaceAware;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;

/**
 * When you use Generics Java automatically creates 'bridge' methods.
 * <p>
 * Additionally, when you use the concept of 'co-variant return values' you
 * create 'bridge' methods. By way of example, because we change the return
 * type of clone() from Object to 'ZZZZ', Java is forced to put in a
 * 'bridge' method that has an Object return type, even though we never
 * actually call it.
 * <p>
 * This has an impact on the code coverage tool Cobertura, which reports
 * that there is missed code (and there is, the bridge method). It reports
 * it as being '0' calls to the 'class' line (the class line is marked red).
 * <p>
 * This test class exercises many of these 'bridge' methods, and thus improves
 * the code coverage.
 * 
 * @author Rolf Lear
 *
 */
public class TestBridgeMethods {
	
	private static final Object[] invokeAll(final Object o, final String name) {
		final ArrayList<Object> al = new ArrayList<Object>();
		for (Method m : o.getClass().getMethods()) {
			if (m.getName().equals(name) && m.isBridge() && 
					m.getParameterTypes().length == 0) {
				try {
					al.add(m.invoke(o));
				} catch (Exception e) {
					throw new IllegalStateException(e);
				}
			}
		}
		return al.toArray();
	}

	/**
	 * Test various known Bridge Methods. This improves code coverage.
	 */
	@Test
	public void testDetachBridges() {
		final NamespaceAware[] cnt = {
				new Text("Text"),
				new CDATA("cdata"),
				new Comment("comment"),
				new DocType("root"),
				new EntityRef("ref"),
				new ProcessingInstruction("pi"),
				new Attribute("att", "val"),
				new Document()
		};
		for (NamespaceAware c : cnt) {
			Object[] a = invokeAll(c, "clone");
			for (Object o : a) {
				compare(c, (NamespaceAware)o);
			}
			Object[] d = invokeAll(c, "detach");
			for (Object o : d) {
				assertTrue(o == c);
			}
			Object[] e = invokeAll(c, "getParent");
			for (Object o : e) {
				assertTrue(o == null);
			}
		}
	}

}
