/*--
 
Copyright (C) 2000 Brett McLaughlin & Jason Hunter.
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:

 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions, and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions, and the disclaimer that follows
    these conditions in the documentation and/or other materials
    provided with the distribution.

 3. The name "JDOM" must not be used to endorse or promote products
    derived from this software without prior written permission.  For
    written permission, please contact license@jdom.org.

 4. Products derived from this software may not be called "JDOM", nor
    may "JDOM" appear in their name, without prior written permission
    from the JDOM Project Management (pm@jdom.org).

 In addition, we request (but do not require) that you include in the
 end-user documentation provided with the redistribution and/or in the
 software itself an acknowledgement equivalent to the following:
     "This product includes software developed by the
      JDOM Project (http://www.jdom.org/)."
 Alternatively, the acknowledgment may be graphical using the logos
 available at http://www.jdom.org/images/logos.

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED.  IN NO EVENT SHALL THE JDOM AUTHORS OR THE PROJECT
 CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.

 This software consists of voluntary contributions made by many
 individuals on behalf of the JDOM Project and was originally
 created by Brett McLaughlin <brett@jdom.org> and
 Jason Hunter <jhunter@jdom.org>.  For more information on the
 JDOM Project, please see <http://www.jdom.org/>.

 */


package org.jdom2.test.cases.input;

/**
 * Tests of SAXBuilder functionality.  Since most of these methods are tested in other parts
 * of the test suite, many tests are not filled.
 * 
 * @author Philip Nelson
 * @version 0.5
 */
import static org.jdom2.test.util.UnitTestUtil.checkException;
import static org.jdom2.test.util.UnitTestUtil.failNoException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.XMLFilterImpl;

import org.jdom2.Content;
import org.jdom2.DefaultJDOMFactory;
import org.jdom2.Document;
import org.jdom2.EntityRef;
import org.jdom2.JDOMException;
import org.jdom2.JDOMFactory;
import org.jdom2.UncheckedJDOMFactory;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.BuilderErrorHandler;
import org.jdom2.input.sax.SAXEngine;
import org.jdom2.input.sax.SAXHandler;
import org.jdom2.input.sax.SAXHandlerFactory;
import org.jdom2.input.sax.XMLReaderJDOMFactory;
import org.jdom2.input.sax.XMLReaderSAX2Factory;
import org.jdom2.input.sax.XMLReaders;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.test.util.FidoFetch;
import org.jdom2.test.util.UnitTestUtil;


@SuppressWarnings("javadoc")
public final class TestSAXBuilder {
	
	private static final String testxml = "<?xml version=\"1.0\"?><root/>";
	private static final String testpattern = "\\s*<\\?xml\\s+version=\"1.0\"\\s+encoding=\"UTF-8\"\\s*\\?>\\s*<root\\s*/>\\s*";

	private class MySAXBuilder extends SAXBuilder {
		public MySAXBuilder() {
			super();
		}
		
		@SuppressWarnings("deprecation")
		public MySAXBuilder(String driver) {
			super(driver);
		}
		
		public MySAXBuilder(XMLReaderJDOMFactory fac) {
			super(fac);
		}
		
		/**
		 * This sets and configures the parser (SAXBuilder just sets).
		 */
		@Override
		public XMLReader createParser() throws JDOMException {
			XMLReader reader = super.createParser();
			configureParser(reader, new SAXHandler());
			return reader;
		}
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testSAXBuilder() {
		SAXBuilder sb = new SAXBuilder();
		assertNull(sb.getDriverClass());
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertFalse(sb.isValidating());
		assertTrue(sb.getExpandEntities());		
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSAXBuilderBooleanFalse() {
		SAXBuilder sb = new SAXBuilder(false);
		assertNull(sb.getDriverClass());
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertFalse(sb.isValidating());
		assertTrue(sb.getExpandEntities());		
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSAXBuilderBooleanTrue() {
		SAXBuilder sb = new SAXBuilder(true);
		assertNull(sb.getDriverClass());
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertTrue(sb.isValidating());
		assertTrue(sb.getExpandEntities());		
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSAXBuilderString() {
		MySAXBuilder sb = new MySAXBuilder("org.apache.xerces.parsers.SAXParser");
		assertEquals("org.apache.xerces.parsers.SAXParser", sb.getDriverClass());
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertFalse(sb.isValidating());
		assertTrue(sb.getExpandEntities());		
		try {
			XMLReader reader = sb.createParser();
			assertNotNull(reader);
			assertEquals("org.apache.xerces.parsers.SAXParser", reader.getClass().getName());
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Could not create parser: " + e.getMessage());
		}

		sb = new MySAXBuilder("com.sun.org.apache.xerces.internal.parsers.SAXParser");
		assertEquals("com.sun.org.apache.xerces.internal.parsers.SAXParser", sb.getDriverClass());
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertFalse(sb.isValidating());
		assertTrue(sb.getExpandEntities());		
		try {
			XMLReader reader = sb.createParser();
			assertNotNull(reader);
			assertTrue(reader.getClass().getName().equals("com.sun.org.apache.xerces.internal.parsers.SAXParser"));
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Could not create parser: " + e.getMessage());
		}
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSAXBuilderStringNew() {
		XMLReaderSAX2Factory fac = new XMLReaderSAX2Factory(false, "org.apache.xerces.parsers.SAXParser");
		MySAXBuilder sb = new MySAXBuilder(fac);
		assertEquals("org.apache.xerces.parsers.SAXParser", sb.getDriverClass());
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertFalse(sb.isValidating());
		assertTrue(sb.getExpandEntities());		
		try {
			XMLReader reader = sb.createParser();
			assertNotNull(reader);
			assertEquals("org.apache.xerces.parsers.SAXParser", reader.getClass().getName());
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Could not create parser: " + e.getMessage());
		}

		fac = new XMLReaderSAX2Factory(false, "com.sun.org.apache.xerces.internal.parsers.SAXParser");
		sb = new MySAXBuilder("com.sun.org.apache.xerces.internal.parsers.SAXParser");
		assertEquals("com.sun.org.apache.xerces.internal.parsers.SAXParser", sb.getDriverClass());
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertFalse(sb.isValidating());
		assertTrue(sb.getExpandEntities());		
		try {
			XMLReader reader = sb.createParser();
			assertNotNull(reader);
			assertTrue(reader.getClass().getName().equals("com.sun.org.apache.xerces.internal.parsers.SAXParser"));
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Could not create parser: " + e.getMessage());
		}
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSAXBuilderStringTrue() {
		SAXBuilder sb = new SAXBuilder("org.apache.xerces.parsers.SAXParser", true);
		assertEquals("org.apache.xerces.parsers.SAXParser", sb.getDriverClass());
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertTrue(sb.getValidation());
		assertTrue(sb.getExpandEntities());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSAXBuilderStringFalse() {
		SAXBuilder sb = new SAXBuilder("org.apache.xerces.parsers.SAXParser", false);
		assertEquals("org.apache.xerces.parsers.SAXParser", sb.getDriverClass());
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertFalse(sb.isValidating());
		assertTrue(sb.getExpandEntities());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetJDOMFactory() {
		SAXBuilder sb = new SAXBuilder(true);
		assertNull(sb.getDriverClass());
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertTrue(sb.isValidating());
		assertTrue(sb.getExpandEntities());
		assertTrue(sb.getJDOMFactory() instanceof DefaultJDOMFactory);
		assertTrue(sb.getJDOMFactory() == sb.getFactory());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSetJDOMFactory() throws JDOMException {
		SAXBuilder sb = new SAXBuilder(true);
		assertNull(sb.getDriverClass());
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertTrue(sb.getValidation());
		assertTrue(sb.getExpandEntities());
		JDOMFactory fac = sb.getJDOMFactory();
		assertTrue(fac instanceof DefaultJDOMFactory);
		UncheckedJDOMFactory udf = new UncheckedJDOMFactory();
		sb.setJDOMFactory(udf);
		assertTrue(sb.getJDOMFactory() == udf);
		assertTrue(sb.buildEngine().getJDOMFactory() == udf);
		sb.setFactory(fac);
		assertTrue(sb.getJDOMFactory() == fac);
		assertTrue(sb.buildEngine().getJDOMFactory() == fac);
	}

	@Test
	public void testGetSAXHandlerFactory() {
		SAXBuilder sb = new SAXBuilder();
		assertTrue(sb.getSAXHandlerFactory() != null);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSetSAXHandlerFactory() {
		SAXBuilder sb = new SAXBuilder(true);
		SAXHandlerFactory fac = sb.getSAXHandlerFactory();
		sb.setSAXHandlerFactory(null);
		assertTrue(fac == sb.getSAXHandlerFactory());
		SAXHandlerFactory fbee = new SAXHandlerFactory() {
			@Override
			public SAXHandler createSAXHandler(JDOMFactory factory) {
				return new SAXHandler();
			}
		};
		sb.setSAXHandlerFactory(fbee);
		assertTrue(fbee == sb.getSAXHandlerFactory());
		sb.setSAXHandlerFactory(null);
		assertTrue(fac == sb.getSAXHandlerFactory());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSetValidation() {
		SAXBuilder sb = new SAXBuilder(true);
		assertNull(sb.getDriverClass());
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertTrue(sb.getValidation());
		assertTrue(sb.isValidating());
		assertTrue(sb.getExpandEntities());
		
		sb.setValidation(false);
		assertFalse(sb.getValidation());
		assertFalse(sb.isValidating());
		
		sb.setValidation(true);
		assertTrue(sb.getValidation());
		assertTrue(sb.isValidating());

	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetErrorHandler() throws JDOMException {
		SAXBuilder sb = new SAXBuilder(true);
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getErrorHandler() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertTrue(sb.isValidating());
		assertTrue(sb.getExpandEntities());
		
		assertTrue(sb.buildEngine().getErrorHandler() instanceof BuilderErrorHandler);
		
		ErrorHandler handler = new BuilderErrorHandler();
		
		sb.setErrorHandler(handler);
		assertTrue(handler == sb.getErrorHandler());		
		assertTrue(handler == sb.buildEngine().getErrorHandler());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetEntityResolver() {
		SAXBuilder sb = new SAXBuilder(true);
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getErrorHandler() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertTrue(sb.isValidating());
		assertTrue(sb.getExpandEntities());

		EntityResolver er = new EntityResolver() {
			@Override
			public InputSource resolveEntity(String arg0, String arg1) {
				return null;
			}
		};
		
		sb.setEntityResolver(er);
		assertTrue(er == sb.getEntityResolver());		
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetDTDHandler() {
		SAXBuilder sb = new SAXBuilder(true);
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getErrorHandler() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertTrue(sb.isValidating());
		assertTrue(sb.getExpandEntities());

		DTDHandler dtd = new DTDHandler() {
			@Override
			public void notationDecl(String arg0, String arg1, String arg2)
					throws SAXException {
				// do nothing
			}
			@Override
			public void unparsedEntityDecl(String arg0, String arg1,
					String arg2, String arg3) throws SAXException {
				// do nothing
			}
		};
		
		sb.setDTDHandler(dtd);
		assertTrue(dtd == sb.getDTDHandler());		
	}
	
	@Test
	public void testGetSetXMLReaderFactory() {
		SAXBuilder sb = new SAXBuilder();
		XMLReaderJDOMFactory xrjf = sb.getXMLReaderFactory();
		assertTrue(xrjf == XMLReaders.NONVALIDATING);
		sb.setXMLReaderFactory(XMLReaders.XSDVALIDATING);
		assertTrue(sb.getXMLReaderFactory() == XMLReaders.XSDVALIDATING);
		sb.setXMLReaderFactory(null);
		assertTrue(xrjf == XMLReaders.NONVALIDATING);
	}

	@Test
	public void testXMLFilter() {
		MySAXBuilder sb = new MySAXBuilder();
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getErrorHandler() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertTrue(sb.getExpandEntities());

		XMLFilter filter = new XMLFilterImpl() {
			@Override
			public void startElement(String arg0, String arg1, String arg2,
					Attributes arg3) throws SAXException {
				super.startElement(arg0, "f" + arg1, arg2, arg3);
			}
			@Override
			public void endElement(String arg0, String arg1, String arg2) throws SAXException {
				super.endElement(arg0, "f" + arg1, arg2);
			}
		};
		
		XMLFilter gilter = new XMLFilterImpl() {
			@Override
			public void startElement(String arg0, String arg1, String arg2,
					Attributes arg3) throws SAXException {
				super.startElement(arg0, "g" + arg1, arg2, arg3);
			}
			@Override
			public void endElement(String arg0, String arg1, String arg2) throws SAXException {
				super.endElement(arg0, "g" + arg1, arg2);
			}
		};
		
		filter.setParent(gilter);
		sb.setXMLFilter(filter);
		assertTrue(filter == sb.getXMLFilter());
		
		try {
			Document doc = sb.build(new CharArrayReader(testxml.toCharArray()));
			assertTrue(doc.hasRootElement());
			assertEquals("fgroot", doc.getRootElement().getName());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Could not parse XML " + testxml + ": " + e.getMessage());
		}
		
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetIgnoringElementContentWhitespace() throws JDOMException {
		SAXBuilder sb = new SAXBuilder(true);
		assertNull(sb.getDriverClass());
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getErrorHandler() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertTrue(sb.isValidating());
		assertTrue(sb.getExpandEntities());
		
		SAXEngine se = sb.buildEngine();
		assertFalse(se.getIgnoringBoundaryWhitespace());
		
		sb.setIgnoringElementContentWhitespace(true);
		assertTrue(sb.getIgnoringElementContentWhitespace());		
		se = sb.buildEngine();
		assertTrue(se.getIgnoringElementContentWhitespace());
		sb.setIgnoringElementContentWhitespace(false);
		assertFalse(sb.getIgnoringElementContentWhitespace());		
		se = sb.buildEngine();
		assertFalse(se.getIgnoringElementContentWhitespace());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetIgnoringBoundaryWhitespace() throws JDOMException {
		SAXBuilder sb = new SAXBuilder(true);
		SAXEngine se = sb.buildEngine();
		assertNull(sb.getDriverClass());
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getErrorHandler() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertTrue(sb.isValidating());
		assertTrue(sb.getExpandEntities());
		assertTrue(se.getEntityResolver() == null);
		assertTrue(se.getErrorHandler() != null);
		assertTrue(se.getDTDHandler() != null);
		assertTrue(se.isValidating());
		assertTrue(se.getExpandEntities());
		sb.setIgnoringBoundaryWhitespace(true);
		assertTrue(sb.getIgnoringBoundaryWhitespace());		
		se = sb.buildEngine();
		assertTrue(se.getIgnoringBoundaryWhitespace());		
		sb.setIgnoringBoundaryWhitespace(false);
		assertFalse(sb.getIgnoringBoundaryWhitespace());
		
		se = sb.buildEngine();
		assertFalse(se.getIgnoringBoundaryWhitespace());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetReuseParser() {
		SAXBuilder sb = new SAXBuilder(true);
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getErrorHandler() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertTrue(sb.isValidating());
		assertTrue(sb.getExpandEntities());
		
		sb.setReuseParser(true);
		assertTrue(sb.getReuseParser());		
		sb.setReuseParser(false);
		assertFalse(sb.getReuseParser());		
	}

	@Test
	public void testCreateParser() {
		MySAXBuilder sb = new MySAXBuilder();
		try {
			XMLReader reader = sb.createParser();
			assertNotNull(reader);
		} catch (JDOMException e) {
			e.printStackTrace();
			fail("Could not create parser: " + e.getMessage());
		}
	}

	@Test
	public void testSetFeature() {
		String feature = "http://javax.xml.XMLConstants/feature/secure-processing";
		MySAXBuilder sb = new MySAXBuilder();
		try {
			sb.setFeature(feature, true);
			XMLReader reader = sb.createParser();
			assertNotNull(reader);
			assertTrue(reader.getFeature(feature));
			sb.setFeature(feature, false);
			reader = sb.createParser();
			assertNotNull(reader);
			assertFalse(reader.getFeature(feature));
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Could not create parser: " + e.getMessage());
		}
	}

	@Test
	public void testSetProperty() {
		LexicalHandler lh = new LexicalHandler() {
			@Override
			public void startEntity(String arg0) throws SAXException {
				// Do nothing;
			}
			@Override
			public void startDTD(String arg0, String arg1, String arg2)
					throws SAXException {
				// Do nothing;
			}
			@Override
			public void startCDATA() throws SAXException {
				// Do nothing;
			}
			@Override
			public void endEntity(String arg0) throws SAXException {
				// Do nothing;
			}
			
			@Override
			public void endDTD() throws SAXException {
				// Do nothing;
			}
			
			@Override
			public void endCDATA() throws SAXException {
				// Do nothing;
			}
			
			@Override
			public void comment(char[] arg0, int arg1, int arg2) throws SAXException {
				// Do nothing;
			}
		};
		
		MySAXBuilder sb = new MySAXBuilder();
		String propname = "http://xml.org/sax/properties/lexical-handler";
		try {
			sb.setProperty(propname, lh);
			XMLReader reader = sb.createParser();
			assertNotNull(reader);
			assertTrue(lh == reader.getProperty(propname));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Could not create parser: " + e.getMessage());
		}
		sb.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "XMLSchema");

	}

	@Test
	public void testSetPropertyTwo() {
		MySAXBuilder sb = new MySAXBuilder();
		try {
			sb.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "XMLSchema");
			sb.createParser();
			fail("Should not be able to set the property");
		} catch (JDOMException jde) {
			// good
		} catch (Exception e) {
			e.printStackTrace();
			fail("Could not create parser: " + e.getMessage());
		}

	}

    /**
     * Test that when setExpandEntities is true, enties are
     * always expanded and when false, entities declarations
     * are added to the DocType
     */
    @Test
    public void test_TCM__void_setExpandEntities_boolean() throws JDOMException, IOException {
        //test entity exansion on internal entity
    	
    	URL src = FidoFetch.getFido().getURL("/SAXBuilderTestEntity.xml");

        SAXBuilder builder = new SAXBuilder();

        builder.setExpandEntities(true);
        assertTrue(builder.getExpandEntities());
        
        Document doc = builder.build(src);
        assertTrue("didn't get entity text", doc.getRootElement().getText().indexOf("simple entity") == 0);
        assertTrue("didn't get entity text", doc.getRootElement().getText().indexOf("another simple entity") > 1);        	

        //test that entity declaration appears in doctype
        //and EntityRef is created in content with internal entity
        builder.setExpandEntities(false);
        assertFalse(builder.getExpandEntities());

        doc = builder.build(src);
        assertTrue("got entity text", ! (doc.getRootElement().getText().indexOf("simple entity") > 1));
        assertTrue("got entity text", ! (doc.getRootElement().getText().indexOf("another simple entity") > 1));        	
		List<Content> content = doc.getRootElement().getContent();
		assertTrue("didn't get EntityRef for unexpanded entities",
			content.get(0) instanceof EntityRef);
		assertTrue("didn't get EntityRef for unexpanded entities",
			content.get(2) instanceof EntityRef);
		
        //test entity expansion on external entity
        URL src2 = FidoFetch.getFido().getURL("/SAXBuilderTestEntity2.xml");

        builder.setExpandEntities(true);
        assertTrue(builder.getExpandEntities());

        doc = builder.build(src2);
        assertTrue("didn't get entity text", doc.getRootElement().getText().indexOf("simple entity") == 0);
        assertTrue("didn't get entity text", doc.getRootElement().getText().indexOf("another simple entity") > 1);        	

        //test that entity declaration appears in doctype
        //and EntityRef is created in content with external entity
        builder.setExpandEntities(false);
        assertFalse(builder.getExpandEntities());
        doc = builder.build(src2);
        assertTrue("got entity text", ! (doc.getRootElement().getText().indexOf("simple entity") > 1));
        assertTrue("got entity text", ! (doc.getRootElement().getText().indexOf("another simple entity") > 1));        	
		content = doc.getRootElement().getContent();
		assertTrue("didn't get EntityRef for unexpanded entities",
			content.get(0) instanceof EntityRef);
		assertTrue("didn't get EntityRef for unexpanded entities",
			content.get(2) instanceof EntityRef);



    }

    /**
     * Test that when setExpandEntities is true, enties are
     * always expanded and when false, entities declarations
     * are added to the DocType
     */
    @Test
    public void test_TCU__DTDComments() throws JDOMException, IOException {
        //test entity exansion on internal entity

        SAXBuilder builder = new SAXBuilder();
        //test entity expansion on external entity
        URL file = FidoFetch.getFido().getURL("/SAXBuilderTestDecl.xml");

       //test that entity declaration appears in doctype
        //and EntityRef is created in content with external entity
        builder.setExpandEntities(false);
        Document doc = builder.build(file);

        assertTrue("didnt' get internal subset comments correctly", doc.getDocType().getInternalSubset().indexOf("foo") > 0);
		//assertTrue("didn't get EntityRef for unexpanded attribute entities",
		//	doc.getRootElement().getAttribute("test").getValue().indexOf("&simple") == 0);
		


    }

    /**
     * Test that when setExpandEntities is true, enties are
     * always expanded and when false, entities declarations
     * are added to the DocType
     */
    @Test
    public void test_TCU__InternalAndExternalEntities() throws JDOMException, IOException {
        //test entity exansion on internal entity

        SAXBuilder builder = new SAXBuilder();
        //test entity expansion on internal and external entity
        URL file = FidoFetch.getFido().getURL("/SAXBuilderTestIntExtEntity.xml");

        builder.setExpandEntities(true);
        Document doc = builder.build(file);
        assertTrue("didn't get internal entity text", doc.getRootElement().getText().indexOf("internal") >= 0);
        assertTrue("didn't get external entity text", doc.getRootElement().getText().indexOf("external") > 0);
		//the internal subset should be empty since entity expansion is off
		assertTrue("invalid characters in internal subset", doc.getDocType().getInternalSubset().length() == 0);
		assertTrue("incorrectly got entity declaration in internal subset for internal entity", 
			doc.getDocType().getInternalSubset().indexOf("internal") < 0);
		assertTrue("incorrectly got external entity declaration in internal subset", 
			doc.getDocType().getInternalSubset().indexOf("external") < 0);
		assertTrue("incorrectly got external entity declaration in internal subset", 
			doc.getDocType().getInternalSubset().indexOf("ldquo") < 0);
        //test that local entity declaration appears in internal subset
        //and EntityRef is created in content with external entity
        builder.setExpandEntities(false);
        doc = builder.build(file);
    	
		EntityRef internal = (EntityRef)doc.getRootElement().getContent().get(0);
		EntityRef external = (EntityRef)doc.getRootElement().getContent().get(6);
		assertNotNull("didn't get EntityRef for unexpanded internal entity", internal);
		assertNotNull("didn't get EntityRef for unexpanded external entity", external);
		assertTrue("didn't get local entity declaration in internal subset", 
			doc.getDocType().getInternalSubset().indexOf("internal") > 0);
		assertTrue("incorrectly got external entity declaration in internal subset", 
			doc.getDocType().getInternalSubset().indexOf("external") < 0);
		assertTrue("incorrectly got external entity declaration in internal subset", 
			doc.getDocType().getInternalSubset().indexOf("ldquo") < 0);
    }
    
    @Ignore
    @Test
    public void test_TCU__InternalSubset() throws JDOMException, IOException {
    
        SAXBuilder builder = new SAXBuilder();
        //test entity expansion on internal subset
        URL file = FidoFetch.getFido().getURL("/SAXBuilderTestEntity.xml");

        builder.setExpandEntities(true);
        Document doc = builder.build(file);
        String subset = doc.getDocType().getInternalSubset();
        assertEquals("didn't get correct internal subset when expand entities was on"
            , "  <!NOTATION n1 SYSTEM \"http://www.w3.org/\">\n  <!NOTATION n2 SYSTEM \"http://www.w3.org/\">\n  <!ENTITY anotation SYSTEM \"http://www.foo.org/image.gif\" NDATA n1>\n", 
            subset);
        //now do it with expansion off
        builder.setExpandEntities(false);
        doc = builder.build(file);
        String subset2 = doc.getDocType().getInternalSubset();
        final String expect = "<!NOTATION n2 SYSTEM \"http://www.w3.org/\">\n  <!ENTITY anotation SYSTEM \"http://www.foo.org/image.gif\" NDATA n1>\n";
        if (!expect.equals(subset2)) {
        	fail("didn't get correct internal subset when expand entities was off.\n" +
        			"Expect: " + expect + "\n" +
        			"Got:    " + subset2);
        }
    }

	@SuppressWarnings("deprecation")
	@Test
	public void testSetFastReconfigure() {
		SAXBuilder sb = new SAXBuilder(true);
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getErrorHandler() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertTrue(sb.isValidating());
		assertTrue(sb.getExpandEntities());
		
		sb.setFastReconfigure(true);
		
		// TODO - Now what?
	}
	
	private void assertXMLMatches(String baseuri, Document doc) {
		XMLOutputter out = new XMLOutputter(Format.getCompactFormat());
		try {
			CharArrayWriter caw = new CharArrayWriter();
			out.output(doc, caw);
			String output = caw.toString();
			if (!output.matches(testpattern)) {
				fail ("Failed to match output:\n  " + output + "\nwith pattern:\n  " + testpattern);
			}
		} catch (IOException e) {
			e.printStackTrace();
			UnitTestUtil.failException("Failed to write Document " + doc + " to CharArrayWriter.", e);
		}
		if (baseuri == null) {
			assertNull(doc.getBaseURI());
		} else {
			if (!baseuri.equals(doc.getBaseURI())) {
				try {
					final String moduri = baseuri.replaceFirst(":/", ":///");
					if (!moduri.equals(doc.getBaseURI())) {
						final String fileuri = new File(baseuri).toURI().toURL().toExternalForm();
						if (!fileuri.equals(doc.getBaseURI())) {
							final String modfileuri = fileuri.replaceFirst(":/", ":///");
							if (!modfileuri.equals(doc.getBaseURI())) {
								fail("Base URI " + doc.getBaseURI() + " is not one of " +
										Arrays.toString(new String[]{baseuri, moduri, fileuri, modfileuri}));
							}
						}
					}
				} catch (MalformedURLException mue) {
					UnitTestUtil.failException("Could not create File URL", mue);
				}
			}
		}
	}
	
	@Test
	public void testBuildInputSource() {
		try {
			SAXBuilder sb = new SAXBuilder();
			InputSource is = null;
			is = new InputSource(new CharArrayReader(testxml.toCharArray()));
			assertXMLMatches(null, sb.build(is));
			is = new InputSource(new CharArrayReader(testxml.toCharArray()));
			assertXMLMatches(null, sb.build(is));
			sb.setReuseParser(false);
			is = new InputSource(new CharArrayReader(testxml.toCharArray()));
			assertXMLMatches(null, sb.build(is));
			is = new InputSource(new CharArrayReader(testxml.toCharArray()));
			assertXMLMatches(null, sb.build(is));
			
			is = new InputSource(new CharArrayReader(testxml.toCharArray()));
			assertXMLMatches(null, sb.buildEngine().build(is));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed to parse document: " + e.getMessage());
		}
	}

	@Test
	public void testBuildInputStream() {
		byte[] bytes = testxml.getBytes();
		try {
			SAXBuilder sb = new SAXBuilder();
			assertXMLMatches(null, sb.build(new ByteArrayInputStream(bytes)));
			assertXMLMatches(null, sb.build(new ByteArrayInputStream(bytes)));
			sb.setReuseParser(false);
			assertXMLMatches(null, sb.build(new ByteArrayInputStream(bytes)));
			assertXMLMatches(null, sb.build(new ByteArrayInputStream(bytes)));
			
			assertXMLMatches(null, sb.buildEngine().build(new ByteArrayInputStream(bytes)));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed to parse document: " + e.getMessage());
		}
	}

	@Test
	public void testBuildFile() {
		File tmp = null;
		try {
			tmp = File.createTempFile("tst", ".xml");
			tmp.deleteOnExit();
			FileWriter fw = new FileWriter(tmp);
			fw.write(testxml.toCharArray());
			fw.flush();
			fw.close();
			SAXBuilder sb = new SAXBuilder();
			assertXMLMatches(tmp.toURI().toString(), sb.build(tmp));
			assertXMLMatches(tmp.toURI().toString(), sb.build(tmp));
			sb.setReuseParser(false);
			assertXMLMatches(tmp.toURI().toString(), sb.build(tmp));
			assertXMLMatches(tmp.toURI().toString(), sb.build(tmp));
			assertXMLMatches(tmp.toURI().toString(), sb.buildEngine().build(tmp));

		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed to write/parse document to file '" + tmp + "': " + e.getMessage());
		} finally {
			if (tmp != null) {
				tmp.delete();
			}
		}
	}

	@Test
	public void testBuildURL() {
		File tmp = null;
		try {
			tmp = File.createTempFile("tst", ".xml");
			tmp.deleteOnExit();
			FileWriter fw = new FileWriter(tmp);
			fw.write(testxml.toCharArray());
			fw.flush();
			fw.close();
			SAXBuilder sb = new SAXBuilder();
			assertXMLMatches(tmp.toURI().toString(), sb.build(tmp.toURI().toURL()));
			assertXMLMatches(tmp.toURI().toString(), sb.build(tmp.toURI().toURL()));
			sb.setReuseParser(false);
			assertXMLMatches(tmp.toURI().toString(), sb.build(tmp.toURI().toURL()));
			assertXMLMatches(tmp.toURI().toString(), sb.build(tmp.toURI().toURL()));
			
			assertXMLMatches(tmp.toURI().toString(), sb.buildEngine().build(tmp.toURI().toURL()));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed to write/parse document to file '" + tmp + "': " + e.getMessage());
		} finally {
			if (tmp != null) {
				tmp.delete();
			}
		}
	}

	@Test
	public void testBuildInputStreamString() {
		byte[] bytes = testxml.getBytes();
		
		try {
			SAXBuilder sb = new SAXBuilder();
			assertXMLMatches("baseID",
					sb.build(new ByteArrayInputStream(bytes), "baseID"));
			assertXMLMatches("baseID",
					sb.build(new ByteArrayInputStream(bytes), "baseID"));
			sb.setReuseParser(false);
			assertXMLMatches("baseID",
					sb.build(new ByteArrayInputStream(bytes), "baseID"));
			assertXMLMatches("baseID",
					sb.build(new ByteArrayInputStream(bytes), "baseID"));
			assertXMLMatches("baseID",
					sb.buildEngine().build(new ByteArrayInputStream(bytes), "baseID"));
		} catch (Exception e) {
			e.printStackTrace();
			UnitTestUtil.failException("Failed to parse document: " + e.getMessage(), e);
		}
	}

	@Test
	public void testBuildReader() {
		char[] chars = testxml.toCharArray();
		try {
			SAXBuilder sb = new SAXBuilder();
			assertXMLMatches(null, sb.build(new CharArrayReader(chars)));
			assertXMLMatches(null, sb.build(new CharArrayReader(chars)));
			sb.setReuseParser(false);
			assertXMLMatches(null, sb.build(new CharArrayReader(chars)));
			assertXMLMatches(null, sb.build(new CharArrayReader(chars)));

			assertXMLMatches(null, sb.buildEngine().build(new CharArrayReader(chars)));
		} catch (Exception e) {
			e.printStackTrace();
			UnitTestUtil.failException("Failed to parse document: " + e.getMessage(), e);
		}
	}

	@Test
	public void testBuildReaderString() {
		char[] chars = testxml.toCharArray();
		try {
			SAXBuilder sb = new SAXBuilder();
			assertXMLMatches("baseID",
					sb.build(new CharArrayReader(chars), "baseID"));
			assertXMLMatches("baseID",
					sb.build(new CharArrayReader(chars), "baseID"));
			sb.setReuseParser(false);
			assertXMLMatches("baseID",
					sb.build(new CharArrayReader(chars), "baseID"));
			assertXMLMatches("baseID",
					sb.build(new CharArrayReader(chars), "baseID"));
			
			assertXMLMatches("baseID",
					sb.buildEngine().build(new CharArrayReader(chars), "baseID"));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed to parse document: " + e.getMessage());
		}
	}

	@Test
	public void testBuildString() {
		File tmp = null;
		try {
			tmp = File.createTempFile("tst", ".xml");
			tmp.deleteOnExit();
			FileWriter fw = new FileWriter(tmp);
			fw.write(testxml.toCharArray());
			fw.flush();
			fw.close();
			SAXBuilder sb = new SAXBuilder();
			assertXMLMatches(tmp.getCanonicalFile().toURI().toURL().toString(), 
					sb.build(tmp.toString()));
			assertXMLMatches(tmp.getCanonicalFile().toURI().toURL().toString(), 
					sb.build(tmp.toString()));
			sb.setReuseParser(false);
			assertXMLMatches(tmp.getCanonicalFile().toURI().toURL().toString(), 
					sb.build(tmp.toString()));
			assertXMLMatches(tmp.getCanonicalFile().toURI().toURL().toString(), 
					sb.build(tmp.toString()));

			assertXMLMatches(tmp.getCanonicalFile().toURI().toURL().toString(), 
					sb.buildEngine().build(tmp.toString()));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed to write/parse document to file '" + tmp + "': " + e.getMessage());
		} finally {
			if (tmp != null) {
				tmp.delete();
			}
		}
	}
	
	@Test
	public void testBuildStringNegativeNull() {
		SAXBuilder sb = new SAXBuilder();
		try {
			String n = null;
			sb.build(n);
			failNoException(NullPointerException.class);
		} catch (Exception e) {
			checkException(NullPointerException.class, e);
		}
	}
	
	@Test
	public void testBuildStringNegativeBadURI() {
		SAXBuilder sb = new SAXBuilder();
		try {
			sb.build(" `!@#$%^&*() is not a valid URI ");
			failNoException(MalformedURLException.class);
		} catch (Exception e) {
			checkException(MalformedURLException.class, e);
			if (e.getCause() != null) {
				assertFalse(e.getCause() instanceof MalformedURLException);
			}
		}
	}
	
	@Test
	public void testBuildStringNegativeActualXML() {
		SAXBuilder sb = new SAXBuilder();
		try {
			sb.build("<root />");
			failNoException(IOException.class);
		} catch (Exception e) {
			checkException(IOException.class, e);
			// cause should also be a MalformedURLException
			checkException(IOException.class, e.getCause());
		}
	}
	
	@Test
	public void testBuildStringNegativePaddedXML() {
		SAXBuilder sb = new SAXBuilder();
		try {
			sb.build("   <!-- comment -->  ");
			failNoException(IOException.class);
		} catch (Exception e) {
			checkException(IOException.class, e);
			checkException(IOException.class, e.getCause());
		}
	}
	
	@Test
	public void testParserFactory() throws JDOMException, IOException {
		if (System.getProperty("org.jdom2.performance") == null) {
			// for android.
			//Assume.assumeNotNull(System.getProperty("org.jdom2.performance"));
			return;
		}
		long start = 0L, time = 0L;
		loopParser(false, false);
		loopParser(false, false);
		loopParser(false, false);
		start = System.nanoTime();
		loopParser(false, false);
		time = System.nanoTime() - start;
		System.out.printf("SimpleLoop Recreate %.3fms\n", time / 1000000.0);
		
		loopParser(true, false);
		loopParser(true, false);
		loopParser(true, false);
		start = System.nanoTime();
		loopParser(true, false);
		time = System.nanoTime() - start;
		System.out.printf("SimpleLoop Reuse    %.3fms\n", time / 1000000.0);
		
		loopParser(true, true);
		loopParser(true, true);
		loopParser(true, true);
		start = System.nanoTime();
		loopParser(true, true);
		time = System.nanoTime() - start;
		System.out.printf("SimpleLoop Fast     %.3fms\n", time / 1000000.0);
	}
	
	
	@SuppressWarnings("deprecation")
	private void loopParser(boolean reuse, boolean fast) throws JDOMException, IOException {
		if (fast) {
			System.out.println("Fast no longer means anything.");
		}
		SAXBuilder builderval = new SAXBuilder(true);
		SAXBuilder buildernoval = new SAXBuilder(false);
		builderval.setReuseParser(reuse);
		buildernoval.setReuseParser(reuse);
		String docstr = 
				"<?xml version='1.0'?><!DOCTYPE root [  <!ELEMENT root  (#PCDATA)>]><root />";
		char[] chars = docstr.toCharArray();
		ResetReader rr = new ResetReader(chars);
		for (int i = 0; i < 10000; i++) {
			parseMem(builderval, rr);
			parseMem(buildernoval, rr);
		}
		//JAXPFastParserFactory.printTimes();
	}
	
	private void parseMem(SAXBuilder builder, ResetReader reader) throws JDOMException, IOException {
		reader.reset();
		Document doc = builder.build(reader);
		assertTrue(doc.hasRootElement());
		assertEquals("root", doc.getRootElement().getName());
	}
	
	private static final class ResetReader extends Reader {
		
		private final char[] chars;
		private int pos = 0;
		
		public ResetReader(final char[] ch) {
			chars = ch;
		}

		@Override
		public int read(final CharBuffer target) throws IOException {
			final int got = chars.length - pos;
			if (got == 0) {
				return -1;
			}
			final int howmuch = target.remaining();
			final int ret = got > howmuch ? howmuch : got;
			target.put(chars, pos, ret);
			pos += ret;
			return ret;
		}

		@Override
		public int read() throws IOException {
			if (pos >= chars.length) {
				return -1;
			}
			return chars[pos++];
		}

		@Override
		public int read(final char[] cbuf) throws IOException {
			final int got = chars.length - pos;
			if (got == 0) {
				return -1;
			}
			final int ret = got > cbuf.length ? cbuf.length : got;
			System.arraycopy(chars, pos, cbuf, 0, ret);
			pos += ret;
			return ret;
		}

		@Override
		public int read(final char[] cbuf, final int off, final int howmuch) throws IOException {
			final int got = chars.length - pos;
			if (got == 0) {
				return -1;
			}
			final int ret = got > howmuch ? howmuch : got;
			System.arraycopy(chars, pos, cbuf, off, ret);
			pos += ret;
			return ret;
		}

		@Override
		public long skip(final long n) throws IOException {
			long got = chars.length - pos;
			if (got > n) {
				pos += (int)n;
				return n;
			}
			long ret = chars.length - pos;
			pos = chars.length;
			return ret;
		}

		@Override
		public boolean ready() throws IOException {
			return true;
		}

		@Override
		public boolean markSupported() {
			return false;
		}

		@Override
		public void mark(final int readAheadLimit) throws IOException {
			return;
		}

		@Override
		public void reset() throws IOException {
			pos = 0;
		}

		@Override
		public void close() throws IOException {
			return;
		}
		
	}

}
