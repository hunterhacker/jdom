package org.jdom2.test.cases.input;

import static org.junit.Assert.*;

import org.jdom2.DocType;
import org.jdom2.JDOMException;
import org.jdom2.JDOMFactory;
import org.jdom2.DefaultJDOMFactory;
import org.jdom2.input.stax.DTDParser;
import org.jdom2.test.util.UnitTestUtil;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class TestDTDParser {

	private static final JDOMFactory factory = new DefaultJDOMFactory();
	
	@Test
	public void testParseSimple() throws JDOMException {
		DocType dt = DTDParser.parse(
				"<!DOCTYPE root >", 
				factory);
		
		assertEquals("root", dt.getElementName());
		assertEquals(null, dt.getPublicID());
		assertEquals(null, dt.getSystemID());
		assertEquals(null, dt.getInternalSubset());
	}

	@Test
	public void testParseSimpleCompact() throws JDOMException {
		DocType dt = DTDParser.parse(
				"<!DOCTYPE root>", 
				factory);
		
		assertEquals("root", dt.getElementName());
		assertEquals(null, dt.getPublicID());
		assertEquals(null, dt.getSystemID());
		assertEquals(null, dt.getInternalSubset());
	}

	@Test
	public void testParseSimpleCompactInternal() throws JDOMException {
		DocType dt = DTDParser.parse(
				"<!DOCTYPE root[internal]>", 
				factory);
		
		assertEquals("root", dt.getElementName());
		assertEquals(null, dt.getPublicID());
		assertEquals(null, dt.getSystemID());
		assertEquals("internal", dt.getInternalSubset());
	}

	@Test
	public void testParseSYSTEMquotNONE() throws JDOMException {
		DocType dt = DTDParser.parse(
				"<!DOCTYPE root SYSTEM \"system\"   >", 
				factory);
		
		assertEquals("root", dt.getElementName());
		assertEquals(null, dt.getPublicID());
		assertEquals("system", dt.getSystemID());
		assertEquals(null, dt.getInternalSubset());
	}

	@Test
	public void testParseSYSTEMaposNONE() throws JDOMException {
		DocType dt = DTDParser.parse(
				"<!DOCTYPE root SYSTEM 'system'   >", 
				factory);
		
		assertEquals("root", dt.getElementName());
		assertEquals(null, dt.getPublicID());
		assertEquals("system", dt.getSystemID());
		assertEquals(null, dt.getInternalSubset());
	}

	@Test
	public void testParseSYSTEMquotSimple() throws JDOMException {
		DocType dt = DTDParser.parse(
				"<!DOCTYPE root SYSTEM \"system\"  [internal] >", 
				factory);
		
		assertEquals("root", dt.getElementName());
		assertEquals(null, dt.getPublicID());
		assertEquals("system", dt.getSystemID());
		assertEquals("internal", dt.getInternalSubset());
	}

	@Test
	public void testParseSYSTEMaposSimple() throws JDOMException {
		DocType dt = DTDParser.parse(
				"<!DOCTYPE root SYSTEM 'system'  [internal] >", 
				factory);
		
		assertEquals("root", dt.getElementName());
		assertEquals(null, dt.getPublicID());
		assertEquals("system", dt.getSystemID());
		assertEquals("internal", dt.getInternalSubset());
	}

	@Test
	public void testParsePUBLICquotenullNONE() throws JDOMException {
		DocType dt = DTDParser.parse(
				"<!DOCTYPE root PUBLIC \"public\"   >", 
				factory);
		
		assertEquals("root", dt.getElementName());
		assertEquals("public", dt.getPublicID());
		assertEquals(null, dt.getSystemID());
		assertEquals(null, dt.getInternalSubset());
	}

	@Test
	public void testParsePUBLICaposnullNONE() throws JDOMException {
		DocType dt = DTDParser.parse(
				"<!DOCTYPE root PUBLIC 'public'   >", 
				factory);
		
		assertEquals("root", dt.getElementName());
		assertEquals("public", dt.getPublicID());
		assertEquals(null, dt.getSystemID());
		assertEquals(null, dt.getInternalSubset());
	}

	@Test
	public void testParsePUBLICquotquotNONE() throws JDOMException {
		DocType dt = DTDParser.parse(
				"<!DOCTYPE root PUBLIC \"public\"  \"system\"   >", 
				factory);
		
		assertEquals("root", dt.getElementName());
		assertEquals("public", dt.getPublicID());
		assertEquals("system", dt.getSystemID());
		assertEquals(null, dt.getInternalSubset());
	}

	@Test
	public void testParsePUBLICquotaposNONE() throws JDOMException {
		DocType dt = DTDParser.parse(
				"<!DOCTYPE root PUBLIC \"public\"  'system'   >", 
				factory);
		
		assertEquals("root", dt.getElementName());
		assertEquals("public", dt.getPublicID());
		assertEquals("system", dt.getSystemID());
		assertEquals(null, dt.getInternalSubset());
	}

	@Test
	public void testParsePUBLICaposquotNONE() throws JDOMException {
		DocType dt = DTDParser.parse(
				"<!DOCTYPE root PUBLIC 'public'  \"system\"   >", 
				factory);
		
		assertEquals("root", dt.getElementName());
		assertEquals("public", dt.getPublicID());
		assertEquals("system", dt.getSystemID());
		assertEquals(null, dt.getInternalSubset());
	}

	@Test
	public void testParsePUBLICaposaposNONE() throws JDOMException {
		DocType dt = DTDParser.parse(
				"<!DOCTYPE root PUBLIC 'public'  'system'   >", 
				factory);
		
		assertEquals("root", dt.getElementName());
		assertEquals("public", dt.getPublicID());
		assertEquals("system", dt.getSystemID());
		assertEquals(null, dt.getInternalSubset());
	}

	@Test
	public void testParsePUBLICaposaposSimple() throws JDOMException {
		DocType dt = DTDParser.parse(
				"<!DOCTYPE root PUBLIC 'public'  'system'  [internal] >", 
				factory);
		
		assertEquals("root", dt.getElementName());
		assertEquals("public", dt.getPublicID());
		assertEquals("system", dt.getSystemID());
		assertEquals("internal", dt.getInternalSubset());
	}

	@Test
	public void testParsePUBLICaposaposSimpleCompact() throws JDOMException {
		DocType dt = DTDParser.parse(
				"<!DOCTYPE root PUBLIC 'public' 'system'[internal]>", 
				factory);
		
		assertEquals("root", dt.getElementName());
		assertEquals("public", dt.getPublicID());
		assertEquals("system", dt.getSystemID());
		assertEquals("internal", dt.getInternalSubset());
	}

	@Test
	public void testParsePUBLICaposaposSimpleSpacy() throws JDOMException {
		DocType dt = DTDParser.parse(
				" <!DOCTYPE root PUBLIC ' public ' ' system ' [   <!ENTITY   " +
				" ent\n EntityDef > ] > ", 
				factory);
		
		assertEquals("root", dt.getElementName());
		assertEquals(" public ", dt.getPublicID());
		assertEquals(" system ", dt.getSystemID());
		assertEquals("  <!ENTITY ent EntityDef>\n", dt.getInternalSubset());
	}

	@Test
	public void testParseInternalA() throws JDOMException {
		DocType dt = DTDParser.parse(
				"<!DOCTYPE root [<!ELEMENT root (#PCDATA)><!ENTITY xpd 'Expand Me!' >]>", 
				factory);
		
		assertEquals("root", dt.getElementName());
		assertEquals(null, dt.getPublicID());
		assertEquals(null, dt.getSystemID());
		assertEquals("  <!ELEMENT root (#PCDATA)>\n  <!ENTITY xpd 'Expand Me!'>\n", dt.getInternalSubset());
	}

	@Test
	public void testParseInternalEmbeddedNewlines() throws JDOMException {
		DocType dt = DTDParser.parse(
				"<!DOCTYPE root  \t \r \n [  \r \n <!ELEMENT root\n     (#PCDATA)>  \n <!ENTITY xpd \n   'Expand Me!' >\n ] \n >", 
				factory);
		
		assertEquals("root", dt.getElementName());
		assertEquals(null, dt.getPublicID());
		assertEquals(null, dt.getSystemID());
		assertEquals("  <!ELEMENT root (#PCDATA)>\n  <!ENTITY xpd 'Expand Me!'>\n", dt.getInternalSubset());
	}

	@Test
	public void testParseIncomplete() {
		try {
			DTDParser.parse("<!DOCTYPE root",factory);
			UnitTestUtil.failNoException(JDOMException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(JDOMException.class, e);
		}
		
	}

	@Test
	public void testParseSpace() throws JDOMException {
		DocType dt = DTDParser.parse("<!DOCTYPE root>",factory);
		assertEquals("root", dt.getElementName());
		assertEquals(null, dt.getPublicID());
		assertEquals(null, dt.getSystemID());
		assertEquals(null, dt.getInternalSubset());
	}

	@Test
	public void testParseTab() throws JDOMException {
		DocType dt = DTDParser.parse("<!DOCTYPE\troot>",factory);
		assertEquals("root", dt.getElementName());
		assertEquals(null, dt.getPublicID());
		assertEquals(null, dt.getSystemID());
		assertEquals(null, dt.getInternalSubset());
	}

	@Test
	public void testParseNewline() throws JDOMException {
		DocType dt = DTDParser.parse("<!DOCTYPE\nroot>",factory);
		assertEquals("root", dt.getElementName());
		assertEquals(null, dt.getPublicID());
		assertEquals(null, dt.getSystemID());
		assertEquals(null, dt.getInternalSubset());
	}

	@Test
	public void testParseCarriageReturn() throws JDOMException {
		DocType dt = DTDParser.parse("<!DOCTYPE\rroot>",factory);
		assertEquals("root", dt.getElementName());
		assertEquals(null, dt.getPublicID());
		assertEquals(null, dt.getSystemID());
		assertEquals(null, dt.getInternalSubset());
	}

	@Test
	public void testParseInternalSpace() throws JDOMException {
		DocType dt = DTDParser.parse("<!DOCTYPE root [ <!ENTITY  ent   'entity' > ] >",factory);
		assertEquals("root", dt.getElementName());
		assertEquals(null, dt.getPublicID());
		assertEquals(null, dt.getSystemID());
		assertEquals("  <!ENTITY ent 'entity'>\n", dt.getInternalSubset());
	}

	@Test
	public void testParseInternalTab() throws JDOMException {
		DocType dt = DTDParser.parse("<!DOCTYPE root [\t<!ENTITY\tent\t'entity'\t>\t]\t>",factory);
		assertEquals("root", dt.getElementName());
		assertEquals(null, dt.getPublicID());
		assertEquals(null, dt.getSystemID());
		assertEquals("  <!ENTITY ent 'entity'>\n", dt.getInternalSubset());
	}

	@Test
	public void testParseInternalNewline() throws JDOMException {
		DocType dt = DTDParser.parse("<!DOCTYPE root [\n<!ENTITY\nent\n'entity'\n>\n]\n>",factory);
		assertEquals("root", dt.getElementName());
		assertEquals(null, dt.getPublicID());
		assertEquals(null, dt.getSystemID());
		assertEquals("  <!ENTITY ent 'entity'>\n", dt.getInternalSubset());
	}

	@Test
	public void testParseInternalCarriageReturn() throws JDOMException {
		DocType dt = DTDParser.parse("<!DOCTYPE root [\r<!ENTITY\rent\r'entity'\r>\r]\r>",factory);
		assertEquals("root", dt.getElementName());
		assertEquals(null, dt.getPublicID());
		assertEquals(null, dt.getSystemID());
		assertEquals("  <!ENTITY ent 'entity'>\n", dt.getInternalSubset());
	}

	@Test
	public void testParseInternalWithAPosSpace() throws JDOMException {
		DocType dt = DTDParser.parse("<!DOCTYPE root [<!ENTITY ent 'entity with spaces\nand newlines,\ttabs, and crs\r' >]>",factory);
		assertEquals("root", dt.getElementName());
		assertEquals(null, dt.getPublicID());
		assertEquals(null, dt.getSystemID());
		assertEquals("  <!ENTITY ent 'entity with spaces\nand newlines,\ttabs, and crs\r'>\n", dt.getInternalSubset());
	}

	@Test
	public void testParseInternalWithQuoteSpace() throws JDOMException {
		DocType dt = DTDParser.parse("<!DOCTYPE root [<!ENTITY  ent   \"entity with spaces\nand newlines,\ttabs, and crs\r\" >]>",factory);
		assertEquals("root", dt.getElementName());
		assertEquals(null, dt.getPublicID());
		assertEquals(null, dt.getSystemID());
		assertEquals("  <!ENTITY ent \"entity with spaces\nand newlines,\ttabs, and crs\r\">\n", dt.getInternalSubset());
	}

}
