/* Please run replic.pl on me ! */
package org.jdom.test.cases;

/**
 * Please put a description of your test here.
 * 
 * @author unascribed
 * @version 0.1
 */
import junit.framework.*;

import org.jdom.*;
import java.io.*;
import java.util.*;
import org.jdom.output.*;
import org.jdom.input.*;

public final class TestElement
extends junit.framework.TestCase
{
	/**
	 *  Construct a new instance. 
	 */
	public TestElement(String name) {
		super(name);
	}

	/**
	 * This method is called before a test is executed.
	 */
	public void setUp() {
		// your code goes here.
	}

	/**
	 * This method is called after a test is executed.
	 */
	public void tearDown() {
		// your code goes here.
	}

	/**
	 * The suite method runs all the tests
	 */
public static Test suite () {
		//TestSuite suite = new TestSuite(TestElement.class);
		TestSuite suite = new TestSuite();
		suite.addTest(new TestElement("test_TCU__testAttributeNamespaces"));
		suite.addTest(new TestElement("test_TCU__testDefaultNamespaces"));
		return suite;
	}

	/**
	 * The main method runs all the tests in the text ui
	 */
	public static void main (String args[]) 
	 {
		junit.textui.TestRunner.run(suite());
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCC___String_OrgJdomNamespace() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCC___String_String() {
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
	public void test_TCC___String_String_String() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__String_getAttributeValue_String_OrgJdomNamespace() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__boolean_hasChildren() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__boolean_removeChild_String_OrgJdomNamespace() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__boolean_removeAttribute_String_String() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__OrgJdomAttribute_getAttribute_String_OrgJdomNamespace() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__OrgJdomElement_setAttributes_List() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__OrgJdomElement_addContent_OrgJdomEntity() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__OrgJdomElement_addContent_OrgJdomCDATA() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__OrgJdomElement_addAttribute_OrgJdomAttribute() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__List_getChildren_String() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__OrgJdomNamespace_getNamespace_String() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__void_addNamespaceDeclaration_OrgJdomNamespace() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__OrgJdomElement_getCopy_String_OrgJdomNamespace() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__OrgJdomElement_addContent_String() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__boolean_equals_Object() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__List_getChildren() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__String_getChildText_String() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__OrgJdomElement_setMixedContent_List() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__OrgJdomElement_getChild_String() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__List_getChildren_String_OrgJdomNamespace() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__String_getText() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__OrgJdomElement_setText_String() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__boolean_removeContent_OrgJdomElement() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__OrgJdomElement_addContent_OrgJdomComment() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__boolean_removeAttribute_String() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__List_getMixedContent() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__OrgJdomDocument_getDocument() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__boolean_removeChildren_String() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__List_getAdditionalNamespaces() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__boolean_removeContent_OrgJdomEntity() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__int_hashCode() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__boolean_removeContent_OrgJdomComment() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__String_getName() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__String_getNamespaceURI() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__OrgJdomElement_addContent_OrgJdomProcessingInstruction() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__OrgJdomElement_setChildren_List() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__String_getAttributeValue_String() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__OrgJdomNamespace_getNamespace() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__OrgJdomAttribute_getAttribute_String() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__OrgJdomElement_addAttribute_String_String() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__String_getNamespacePrefix() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__OrgJdomElement_getCopy_String() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__boolean_removeAttribute_String_OrgJdomNamespace() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__boolean_isRootElement() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__String_getChildTextTrim_String_OrgJdomNamespace() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__String_getQualifiedName() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__boolean_removeContent_OrgJdomProcessingInstruction() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__boolean_hasMixedContent() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__OrgJdomElement_addContent_OrgJdomElement() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__boolean_removeChildren_String_OrgJdomNamespace() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__String_getChildText_String_OrgJdomNamespace() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__boolean_removeChildren() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__OrgJdomElement_getChild_String_OrgJdomNamespace() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__Object_clone() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__String_toString() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__String_getTextTrim() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__String_getSerializedForm() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__OrgJdomElement_getParent() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__List_getAttributes() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__String_getChildTextTrim_String() {
		fail("implement me !");
	}

	/**
	 * Test code goes here. Replace this comment.
	 */
	public void test_TCM__boolean_removeChild_String() {
		fail("implement me !");
	}

/**
 * Test that attributes will be added and retrieved according
 * to specs. with namespaces and prefixes intact
 * 
 */
public void test_TCU__testAttributeNamespaces() {

	//set up an element and namespaces to test with

	Element el= new Element("test");
	Namespace one = Namespace.getNamespace("test", "http://foo");
	Namespace two = Namespace.getNamespace("test2", "http://foo");
		
	Attribute att= new Attribute("first", "first", one);
	Attribute att2= new Attribute("first", "second", two);

	//shouldn't accept attributes with different names even if prefixes are different
	try {
		el.addAttribute(att);
		el.addAttribute(att2);
		assert("didn't catch duplicate addAttribute with different prefixes", false);
	} catch (IllegalAddException e) {
	}

	//set up some unique namespaces for later tests
	Namespace testDefault= Namespace.getNamespace("http://bar");
	Namespace testNS= Namespace.getNamespace("test", "http://foo");
	Namespace testNS2= Namespace.getNamespace("test2", "http://foo2");

	//this should cause an error since the prefix will be discarded
	Namespace testNS3;
	try {
		testNS3= Namespace.getNamespace("test", "");
		assert("didn't catch bad \"\" uri name", false);
	} catch (Exception e) {	}

	//XXX show that you can't have an empty namespace with the current scheme
	//testNS3= Namespace.EMPTY_NAMESPACE;
	testNS3= Namespace.getNamespace("");

	el= new Element("test", testDefault);
	el.addNamespaceDeclaration(testNS2);
	el.addNamespaceDeclaration(testNS3);

	att= new Attribute("prefixNS", "test");

	//Test for default namespace check for attribute
	try {
		att2= new Attribute("prefixNS", "test", testNS3);
		assert("didn't catch illegal default namespace for attribute", false);
	} catch (IllegalNameException e) {
		att2 = new Attribute("prefixNS", "no namespace");
	}

	//test prefixes with same name but different namespaces
	Attribute att3= new Attribute("prefixNS", "test", testNS);
	Attribute att4= new Attribute("prefixNS", "test2", testNS2);
	el.addAttribute(att2);
	el.addAttribute(att3);
	el.addAttribute(att4);

	Attribute attback= el.getAttribute("prefixNS");
	assert(
		"failed to get attribute from empty default namespace",
		attback.getNamespaceURI().equals(""));
	attback= el.getAttribute("prefixNS", testNS3);
	assert(
		"failed to get attribute from http://bar namespace",
		attback.getNamespaceURI().equals(""));
	attback= el.getAttribute("prefixNS", testNS);
	assert(
		"failed to get attribute from http://foo namespace",
		attback.getNamespaceURI().equals("http://foo"));
	attback= el.getAttribute("prefixNS", testNS2);
	assert(
		"failed to get attribute from http://foo2 namespace",
		attback.getNamespaceURI().equals("http://foo2"));


}

/**
 * Test that an Element properly handles default namespaces
 * 
 */
public void test_TCU__testDefaultNamespaces() throws IOException {

	//set up an element to test with
	Element element= new Element("element", Namespace.getNamespace("http://foo"));
	Element child1 = new Element("child1");
	Element child2 = new Element("child2");

	element.addContent(child1);
	element.addContent(child2);
	
	//here is what we expect in these two scenarios
	String bufWithNoNS = "<element xmlns=\"http://foo\"><child1 /><child2 /></element>";
	
	String bufWithEmptyNS = "<element xmlns=\"http://foo\"><child1 xmlns=\"\" /><child2 xmlns=\"\" /></element>";

	StringWriter sw = new StringWriter();
	XMLOutputter op= new XMLOutputter("", false);
	op.output(element, sw);
	assert("Incorrect output for NO_NAMESPACE in a default namespace", sw.toString().equals(bufWithNoNS));
/*
	//new try setting a new empty default namespace for children
	element= new Element("element", Namespace.getNamespace("http://foo"));
	child1= new Element("child1", Namespace.EMPTY_NAMESPACE);
	child2= new Element("child2", Namespace.EMPTY_NAMESPACE);


	element.addContent(child1);
	element.addContent(child2);
	sw = new StringWriter();
	op= new XMLOutputter("", false);
	op.output(element, sw);
	assert("Incorrect output for EMPTY_NAMESPACE in a default namespace", sw.toString().equals(bufWithEmptyNS));

	
	//XXX this code shows an error in either xmloutputter or element where multiple default namespaces are output
	Element el = new Element("test");
	el.addNamespaceDeclaration(Namespace.getNamespace("", "foo:bar"));
	el.addNamespaceDeclaration(Namespace.getNamespace("", "foo2:bar"));
	el.addNamespaceDeclaration(Namespace.EMPTY_NAMESPACE);	
	XMLOutputter out = new XMLOutputter("  ", true);
	sw = new StringWriter();
	try {
		out.output(el, sw);
		sw.toString();
	} catch (IOException e) {}

	*/
}
}
