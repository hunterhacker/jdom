package org.jdom.test.cases.input;

/**
 * Please put a description of your test here.
 * 
 * @author unascribed
 * @version 0.1
 */
import junit.framework.*;
import java.util.*;
import java.io.*;

import org.jdom.*;
import org.jdom.input.*;


public final class TestSAXBuilder
extends junit.framework.TestCase
{
	/**
	 * Resource Bundle for various testing resources
	 */
	private ResourceBundle rb = ResourceBundle.getBundle("org.jdom.test.Test");

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
    public void test_TCM__void_setExpandEntities_boolean() throws JDOMException {
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
    public void test_TCU__DTDComments() throws JDOMException {
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
    public void test_TCU__InternalAndExternalEntities() throws JDOMException {
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
		assertTrue("didn't get EntityRef for unexpanded internal entity",
			internal != null);
		assertTrue("didn't get EntityRef for unexpanded external entity",
			external != null);
		assertTrue("didn't get local entity declaration in internal subset", 
			doc.getDocType().getInternalSubset().indexOf("internal") > 0);
		assertTrue("incorrectly got external entity declaration in internal subset", 
			doc.getDocType().getInternalSubset().indexOf("external") < 0);
		assertTrue("incorrectly got external entity declaration in internal subset", 
			doc.getDocType().getInternalSubset().indexOf("ldquo") < 0);
    }

}
