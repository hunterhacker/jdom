package org.jdom2.test.cases.util;

import static org.junit.Assert.*;

import org.junit.Test;

import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.util.TextHelper;

@SuppressWarnings("javadoc")
public class TestTextHelper {
	
	private final Namespace ns = Namespace.getNamespace("jdomtest");
	
	private final Element root = new Element("root");
	private final Element childa = new Element("child");
	private final Element childb = new Element("child", ns);
	private final Element childc = new Element("child");
	private final Element childd = new Element("child", ns);
	private final Element childe = new Element("kid");
	private final Element childf = new Element("kid", ns);
	private final String teststr = "  this  has  space ";
	private final String comp = Format.compact(teststr);
	private final String trim = Format.trimBoth(teststr);
	private final String plain = teststr; 
	
	public TestTextHelper() {
		childa.setText(teststr);
		childb.setText(teststr);
		childc.setText(teststr);
		childd.setText(teststr);
		root.addContent(childa);
		root.addContent(childb);
		root.addContent(childc);
		root.addContent(childd);
		root.addContent(childe);
		root.addContent(childf);
	}

	@Test
	public void testGetChildTextElementString() {
		assertEquals(plain, TextHelper.getChildText(root, "child"));
		assertEquals(null, TextHelper.getChildText(root, "dummy"));
		assertEquals("", TextHelper.getChildText(root, "kid"));
	}

	@Test
	public void testGetChildTextElementStringNamespace() {
		assertEquals(plain, TextHelper.getChildText(root, "child", ns));
		assertEquals(null, TextHelper.getChildText(root, "dummy", ns));
		assertEquals("", TextHelper.getChildText(root, "kid", ns));
	}

	@Test
	public void testGetChildTextTrimElementString() {
		assertEquals(trim, TextHelper.getChildTextTrim(root, "child"));
		assertEquals(null, TextHelper.getChildTextTrim(root, "dummy"));
		assertEquals("", TextHelper.getChildTextTrim(root, "kid"));
	}

	@Test
	public void testGetChildTextTrimElementStringNamespace() {
		assertEquals(trim, TextHelper.getChildTextTrim(root, "child", ns));
		assertEquals(null, TextHelper.getChildTextTrim(root, "dummy", ns));
		assertEquals("", TextHelper.getChildTextTrim(root, "kid", ns));
	}

	@Test
	public void testGetChildTextNormalizeElementString() {
		assertEquals(comp, TextHelper.getChildTextNormalize(root, "child"));
		assertEquals(null, TextHelper.getChildTextNormalize(root, "dummy"));
		assertEquals("", TextHelper.getChildTextNormalize(root, "kid"));
	}

	@Test
	public void testGetChildTextNormalizeElementStringNamespace() {
		assertEquals(comp, TextHelper.getChildTextNormalize(root, "child", ns));
		assertEquals(null, TextHelper.getChildTextNormalize(root, "dummy", ns));
		assertEquals("", TextHelper.getChildTextNormalize(root, "kid", ns));
	}

}
