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
import junit.framework.*;
import java.util.*;
import java.io.*;

import org.jdom2.*;
import org.jdom2.input.*;


public final class TestSAXBuilder
extends junit.framework.TestCase
{
	/**
	 * Resource Bundle for various testing resources
	 */
	private ResourceBundle rb = ResourceBundle.getBundle("org.jdom2.test.Test");

	/**
	 * the directory where needed resource files will be kept
	 */
	private String resourceDir = "";

	/**
	 *  a directory for temporary storage of files
	 */
	private String scratchDir = "";
    /**
     *  Construct a new instance. 
     */
    public TestSAXBuilder(String name) {
        super(name);
    }
    /**
     * The main method runs all the tests in the text ui
     */
    public static void main (String args[]) 
     {
        junit.textui.TestRunner.run(suite());
    }
    /**
     * This method is called before a test is executed.
     */
    public void setUp() {
	    
 		resourceDir = rb.getString("test.resourceRoot");
		scratchDir = rb.getString("test.scratchDirectory");

    }
	
    /**
     * The suite method runs all the tests
     */
    public static Test suite () {
        TestSuite suite = new TestSuite();
        suite.addTest(new TestSAXBuilder("test_TCU__DTDComments"));
        suite.addTest(new TestSAXBuilder("test_TCM__void_setExpandEntities_boolean"));
    	suite.addTest(new TestSAXBuilder("test_TCU__InternalAndExternalEntities"));
    	//suite.addTest(new TestSAXBuilder("test_TCU__InternalSubset"));
        
        return suite;
    }
    /**
     * This method is called after a test is executed.
     */
    public void tearDown() {
        // your code goes here.
    }
    /**
     * Test code goes here. Replace this comment.
     */
    public void test_TCC__() {
        fail("implement me !");
    }
    /**
     * Test code goes here. Replace this comment.
     */
    public void test_TCC___boolean() {
        fail("implement me !");
    }
    /**
     * Test code goes here. Replace this comment.
     */
    public void test_TCC___String() {
        fail("implement me !");
    }
    /**
     * Test code goes here. Replace this comment.
     */
    public void test_TCC___String_boolean() {
        fail("implement me !");
    }
    /**
     * Test code goes here. Replace this comment.
     */
    public void test_TCM__OrgJdomDocument_build_File() {
        fail("implement me !");
    }
    /**
     * Test code goes here. Replace this comment.
     */
    public void test_TCM__OrgJdomDocument_build_InputStream() {
        fail("implement me !");
    }
    /**
     * Test code goes here. Replace this comment.
     */
    public void test_TCM__OrgJdomDocument_build_InputStream_String() {
        fail("implement me !");
    }
    /**
     * Test code goes here. Replace this comment.
     */
    public void test_TCM__OrgJdomDocument_build_Reader() {
        fail("implement me !");
    }
    /**
     * Test code goes here. Replace this comment.
     */
    public void test_TCM__OrgJdomDocument_build_Reader_String() {
        fail("implement me !");
    }
    /**
     * Test code goes here. Replace this comment.
     */
    public void test_TCM__OrgJdomDocument_build_String() {
        fail("implement me !");
    }
    /**
     * Test code goes here. Replace this comment.
     */
    public void test_TCM__OrgJdomDocument_build_URL() {
        fail("implement me !");
    }
    /**
     * Test code goes here. Replace this comment.
     */
    public void test_TCM__void_setDTDHandler_OrgXmlSaxDTDHandler() {
        fail("implement me !");
    }
    /**
     * Test code goes here. Replace this comment.
     */
    public void test_TCM__void_setEntityResolver_OrgXmlSaxEntityResolver() {
        fail("implement me !");
    }
    /**
     * Test code goes here. Replace this comment.
     */
    public void test_TCM__void_setErrorHandler_OrgXmlSaxErrorHandler() {
        fail("implement me !");
    }
    /**
     * Test that when setExpandEntities is true, enties are
     * always expanded and when false, entities declarations
     * are added to the DocType
     */
    public void test_TCM__void_setExpandEntities_boolean() throws JDOMException, IOException {
        //test entity exansion on internal entity

        SAXBuilder builder = new SAXBuilder();
        File file = new File(resourceDir + "/SAXBuilderTestEntity.xml");

        builder.setExpandEntities(true);
        Document doc = builder.build(file);
        assertTrue("didn't get entity text", doc.getRootElement().getText().indexOf("simple entity") == 0);
        assertTrue("didn't get entity text", doc.getRootElement().getText().indexOf("another simple entity") > 1);        	

        //test that entity declaration appears in doctype
        //and EntityRef is created in content with internal entity
        builder.setExpandEntities(false);
        doc = builder.build(file);
        assertTrue("got entity text", ! (doc.getRootElement().getText().indexOf("simple entity") > 1));
        assertTrue("got entity text", ! (doc.getRootElement().getText().indexOf("another simple entity") > 1));        	
		List content = doc.getRootElement().getContent();
		assertTrue("didn't get EntityRef for unexpanded entities",
			content.get(0) instanceof EntityRef);
		assertTrue("didn't get EntityRef for unexpanded entities",
			content.get(2) instanceof EntityRef);
		
        //test entity expansion on external entity
        file = new File(resourceDir + "/SAXBuilderTestEntity2.xml");

        builder.setExpandEntities(true);
        doc = builder.build(file);
        assertTrue("didn't get entity text", doc.getRootElement().getText().indexOf("simple entity") == 0);
        assertTrue("didn't get entity text", doc.getRootElement().getText().indexOf("another simple entity") > 1);        	

        //test that entity declaration appears in doctype
        //and EntityRef is created in content with external entity
        builder.setExpandEntities(false);
        doc = builder.build(file);
        assertTrue("got entity text", ! (doc.getRootElement().getText().indexOf("simple entity") > 1));
        assertTrue("got entity text", ! (doc.getRootElement().getText().indexOf("another simple entity") > 1));        	
		content = doc.getRootElement().getContent();
		assertTrue("didn't get EntityRef for unexpanded entities",
			content.get(0) instanceof EntityRef);
		assertTrue("didn't get EntityRef for unexpanded entities",
			content.get(2) instanceof EntityRef);



    }
    /**
     * Test code goes here. Replace this comment.
     */
    public void test_TCM__void_setFactory_OrgJdomInputJDOMFactory() {
        fail("implement me !");
    }
    /**
     * Test code goes here. Replace this comment.
     */
    public void test_TCM__void_setIgnoringElementContentWhitespace_boolean() {
        fail("implement me !");
    }
    /**
     * Test code goes here. Replace this comment.
     */
    public void test_TCM__void_setValidation_boolean() {
        fail("implement me !");
    }
    /**
     * Test code goes here. Replace this comment.
     */
    public void test_TCM__void_setXMLFilter_OrgXmlSaxXMLFilter() {
        fail("implement me !");
    }
    /**
     * Test that when setExpandEntities is true, enties are
     * always expanded and when false, entities declarations
     * are added to the DocType
     */
    public void test_TCU__DTDComments() throws JDOMException, IOException {
        //test entity exansion on internal entity

        SAXBuilder builder = new SAXBuilder();
        //test entity expansion on external entity
        File file = new File(resourceDir + "/SAXBuilderTestDecl.xml");

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
    public void test_TCU__InternalAndExternalEntities() throws JDOMException, IOException {
        //test entity exansion on internal entity

        SAXBuilder builder = new SAXBuilder();
        //test entity expansion on internal and external entity
        File file = new File(resourceDir + "/SAXBuilderTestIntExtEntity.xml");

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
    
    public void test_TCU__InternalSubset() throws JDOMException, IOException {
    
        SAXBuilder builder = new SAXBuilder();
        //test entity expansion on internal subset
        File file = new File(resourceDir + "/SAXBuilderTestEntity.xml");

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
        assertEquals("didn't get correct internal subset when expand entities was off"
            , "<!NOTATION n2 SYSTEM \"http://www.w3.org/\">\n  <!ENTITY anotation SYSTEM \"http://www.foo.org/image.gif\" NDATA n1>\n", subset2);    
        	
    }
}
