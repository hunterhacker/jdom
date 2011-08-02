package org.jdom.test.cases;

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

/**
 * Please put a description of your test here.
 *
 * @author unascribed
 * @version 0.1
 */

import junit.framework.*;
import java.util.*;

import org.jdom.*;
import java.io.*;
import java.util.*;
import org.jdom.output.*;
import org.omg.PortableInterceptor.SUCCESSFUL;

public final class TestElement
        extends junit.framework.TestCase {

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
    public TestElement(String name) {
        super(name);
    }

    /**
     * The main method runs all the tests in the text ui
     */
    public static void main(String args[]) {
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
    public static Test suite() {
        TestSuite suite = new TestSuite(TestElement.class);
        /*TestSuite suite = new TestSuite();
        suite.addTest(new TestElement("test_TCU__testAttributeNamespaces"));
        suite.addTest(new TestElement("test_TCU__testDefaultNamespaces"));
        suite.addTest(new TestElement("test_TCU__testSerialization"));
        */
        return suite;
    }

    /**
     * This method is called after a test is executed.
     */
    public void tearDown() {
        // your code goes here.
    }

    /**
     * Test the constructor for an empty element
     */
    public void test_TCC___String() {

        //create a new empty element
        Element el = new Element("theElement");
        assertTrue("wrong element name after constructor", el.getName().equals("theElement"));
        assertTrue("expected NO_NAMESPACE", el.getNamespace().equals(Namespace.NO_NAMESPACE));
        assertTrue("expected no child elements", el.getChildren().equals(Collections.EMPTY_LIST));
        assertTrue("expected no attributes", el.getAttributes().equals(Collections.EMPTY_LIST));

        //must have a name
        try {
            el = new Element("");
            fail("allowed creation of an element with no name");
        }
        catch (IllegalNameException e) {
        }

        //name can't be null
        try {
            el = new Element(null);
            fail("allowed creation of an element with null name");
        }
        catch (IllegalNameException e) {
        }

        //we can assume the Verifier has been called by now so we are done
    }

    /**
     * Test the Element constructor with an name and namespace
     */
    public void test_TCC___String_OrgJdomNamespace() {
        //create a new empty element with a namespace

        Namespace ns = Namespace.getNamespace("urn:foo");
        Element el = new Element("theElement", ns);
        assertTrue("wrong element name after constructor", el.getName().equals("theElement"));
        assertTrue("expected urn:foo namespace", el.getNamespace().equals(Namespace.getNamespace("urn:foo")));
        assertTrue("expected no child elements", el.getChildren().equals(Collections.EMPTY_LIST));
        assertTrue("expected no attributes", el.getAttributes().equals(Collections.EMPTY_LIST));

        //must have a name
        try {
            el = new Element("", ns);
            fail("allowed creation of an element with no name");
        }
        catch (IllegalNameException e) {
        }

        //name can't be null
        try {
            el = new Element(null, ns);
            fail("allowed creation of an element with null name");
        }
        catch (IllegalNameException e) {
        }

        //we can assume the Verifier has been called by now so we are done
    }

    /**
     * Test the Element constructor with a string default namespace
     */
    public void test_TCC___String_String() {
        //create a new empty element with a namespace

        Element el = new Element("theElement", "urn:foo");
        assertTrue("wrong element name after constructor", el.getName().equals("theElement"));
        assertTrue("expected urn:foo namespace", el.getNamespace().equals(Namespace.getNamespace("urn:foo")));
        assertTrue("expected no child elements", el.getChildren().equals(Collections.EMPTY_LIST));
        assertTrue("expected no attributes", el.getAttributes().equals(Collections.EMPTY_LIST));

        //must have a name
        try {
            el = new Element("", "urn:foo");
            fail("allowed creation of an element with no name");
        }
        catch (IllegalNameException e) {
        }

        //name can't be null
        try {
            el = new Element(null, "urn:foo");
            fail("allowed creation of an element with null name");
        }
        catch (IllegalNameException e) {
        }

        //we can assume the Verifier has been called by now so we are done
    }

    /**
     * Test the Element constructor with a namespace uri and prefix
     */
    public void test_TCC___String_String_String() {
        //create a new empty element with a namespace


        Element el = new Element("theElement", "x", "urn:foo");
        assertTrue("wrong element name after constructor", el.getName().equals("theElement"));
        assertTrue("expected urn:foo namespace", el.getNamespace().equals(Namespace.getNamespace("x", "urn:foo")));
        assertTrue("expected no child elements", el.getChildren().equals(Collections.EMPTY_LIST));
        assertTrue("expected no attributes", el.getAttributes().equals(Collections.EMPTY_LIST));

        //must have a name
        try {
            el = new Element("", "x", "urn:foo");
            fail("allowed creation of an element with no name");
        }
        catch (IllegalNameException e) {
        }

        //name can't be null
        try {
            el = new Element(null, "x", "urn:foo");
            fail("allowed creation of an element with null name");
        }
        catch (IllegalNameException e) {
        }

        //we can assume the Verifier has been called by now so we are done

    }

    /**
     * Test the equals compares only object instances
     */
    public void test_TCM__boolean_equals_Object() {
        Element el = new Element("theElement", "x", "urn:foo");
        Element el2 = new Element("theElement", "x", "urn:foo");

        assertTrue("incorrect equals evaluation", ((Object) el).equals(el));
        assertTrue("incorrect equals evaluation", !((Object) el2).equals(el));
    }

    /**
     * Test that hasChildren only reports true for actual child elements.
     */
/*
    public void test_TCM__boolean_hasChildren() {
        //set up an element to test with
        Element element = new Element("element", Namespace.getNamespace("http://foo"));
        assertTrue("reported children when there are none", !element.hasChildren());

        Attribute att1 = new Attribute("anAttribute", "boo");
        element.setAttribute(att1);
        assertTrue("reported children when there are none", !element.hasChildren());

        //add some text
        element.addContent("the text");
        assertTrue("reported children when there are none", !element.hasChildren());

        //add some CDATA
        element.addContent(new CDATA("the text"));
        assertTrue("reported children when there are none", !element.hasChildren());

        //add a PI
        element.addContent(new ProcessingInstruction("pi", "the text"));
        assertTrue("reported children when there are none", !element.hasChildren());

        //add Comment
        element.addContent(new Comment("the text"));
        assertTrue("reported children when there are none", !element.hasChildren());

        //finally a child element
        Element child1 = new Element("child1");
        element.addContent(child1);
        assertTrue("reported no children when there is a child element", element.hasChildren());
    }
*/

    /**
     * Test that hasMixedContent works for varying types of possible
     * child and other content
     */
    public void test_TCM__boolean_hasMixedContent() {
        /** No op because the method is deprecated

         //set up an element to test with
         Element element= new Element("element", Namespace.getNamespace("http://foo"));
         assertTrue("reported mixed content when there is none", ! element.hasMixedContent());


         Attribute att1 = new Attribute("anAttribute", "boo");
         element.setAttribute(att1);
         assertTrue("reported mixed content when there is none", ! element.hasMixedContent());

         //add some text
         element.addContent("the text");
         assertTrue("reported mixed content when there is none", ! element.hasMixedContent());

         //add some CDATA
         element.addContent(new CDATA("the text"));
         assertTrue("reported no mixed content when there is", element.hasMixedContent());

         element= new Element("element");
         //add a PI
         element.addContent(new ProcessingInstruction("pi", "the text"));
         assertTrue("reported mixed content when there is none", ! element.hasMixedContent());

         //add Comment
         element.addContent(new Comment("the text"));
         assertTrue("reported no mixed content when there is", element.hasMixedContent());

         element= new Element("element");
         //finally a child element
         Element child1= new Element("child1");
         element.addContent(child1);
         assertTrue("reported mixed content when there is none", ! element.hasMixedContent());

         element.addContent("some text");
         assertTrue("reported no mixed content when there is",  element.hasMixedContent());

         */
    }

    /**
     * Test that an Element can determine if it is a root element
     */
    public void test_TCM__boolean_isRootElement() {
        Element element = new Element("element");
        assertTrue("incorrectly identified element as root", !element.isRootElement());

        Document doc = new Document(element);
        assertTrue("incorrectly identified element as non root", element.isRootElement());
    }

    /**
     * Test than an Element can remove an Attribute by name
     */
    public void test_TCM__boolean_removeAttribute_String() {
        Element element = new Element("test");
        Attribute att = new Attribute("anAttribute", "test");
        element.setAttribute(att);

        //make sure it's there
        assertNotNull("attribute not found after add", element.getAttribute("anAttribute"));

        //and remove it
        assertTrue("attribute not removed", element.removeAttribute("anAttribute"));
        //make sure it's not there
        assertNull("attribute found after remove", element.getAttribute("anAttribute"));
    }

    /**
     * Test removeAttribute with a namespace
     */
    public void test_TCM__boolean_removeAttribute_String_OrgJdomNamespace() {
        Element element = new Element("test");
        Namespace ns = Namespace.getNamespace("x", "urn:test");
        Attribute att = new Attribute("anAttribute", "test", ns);
        element.setAttribute(att);

        //make sure it's there
        assertNotNull("attribute not found after add", element.getAttribute("anAttribute", ns));
        //and remove it
        assertTrue("attribute not removed", element.removeAttribute("anAttribute", ns));
        //make sure it's not there
        assertNull("attribute found after remove", element.getAttribute("anAttribute", ns));
    }

    /**
     * Test removeAtttribute with a namespace uri.
     */
    public void test_TCM__boolean_removeAttribute_String_String() {
        /** No op because the method is deprecated

         Element element = new Element("test");
         Namespace ns = Namespace.getNamespace("x", "urn:test");
         Attribute att = new Attribute("anAttribute", "test", ns);
         element.setAttribute(att);

         //make sure it's there
         assertNotNull("attribute not found after add", element.getAttribute("anAttribute", ns));
         //and remove it
         assertTrue("attribute not removed", element.removeAttribute("anAttribute", "urn:test"));
         //make sure it's not there
         assertNull("attribute found after remove", element.getAttribute("anAttribute", ns));
         */
    }

    /**
     * Test removeChild by name
     */
    public void test_TCM__boolean_removeChild_String() {
        Element element = new Element("element");
        Element child = new Element("child");
        element.addContent(child);

        assertTrue("couldn't remove child content", element.removeChild("child"));
        assertNull("child not removed", element.getChild("child"));
    }

    /**
     * Test removeChild by name and namespace
     */
    public void test_TCM__boolean_removeChild_String_OrgJdomNamespace() {
        Namespace ns = Namespace.getNamespace("x", "urn:fudge");
        Element element = new Element("element");
        Element child = new Element("child", ns);
        Element child2 = new Element("child", ns);
        element.addContent(child);

        assertTrue("couldn't remove child content", element.removeChild("child", ns));
        assertNull("child not removed", element.getChild("child", ns));
        //now test that only the first child is removed
        element.addContent(child);
        element.addContent(child2);
        assertTrue("couldn't remove child content", element.removeChild("child", ns));
        assertNotNull("child not removed", element.getChild("child", ns));

    }

    /**
     * Test removeChildren which removes all child elements
     */
/*
    public void test_TCM__boolean_removeChildren() {
        Namespace ns = Namespace.getNamespace("x", "urn:fudge");
        Element element = new Element("element");

        assertTrue("incorrectly returned true when deleting no content", element.removeChildren() == false);

        Element child = new Element("child", ns);
        Element child2 = new Element("child", ns);
        element.addContent(child);
        element.addContent(child2);

        assertTrue("couldn't remove child content", element.removeChildren());
        assertNull("child not removed", element.getContent("child", ns));
    }
*/

    /**
     * Test removeChildren by name.
     */
/*
    public void test_TCM__boolean_removeChildren_String() {
        Element element = new Element("element");

        assertTrue("incorrectly returned true when deleting no content", element.removeChildren() == false);

        Element child = new Element("child");
        Element child2 = new Element("child");
        element.addContent(child);
        element.addContent(child2);

        assertTrue("incorrect return on bogus child", !element.removeChildren("test"));
        assertNotNull("child incorrectly removed", element.getContent("child"));
        assertTrue("couldn't remove child content", element.removeChildren("child"));
        assertNull("children not removed", element.getContent("child"));
    }
*/

    /**
     * Test removeChildren with a name and namespace
     */
/*
    public void test_TCM__boolean_removeChildren_String_OrgJdomNamespace() {
        Namespace ns = Namespace.getNamespace("x", "urn:fudge");
        Element element = new Element("element");

        assertTrue("incorrectly returned true when deleting no content", element.removeChildren() == false);

        Element child = new Element("child", ns);
        Element child2 = new Element("child", ns);
        element.addContent(child);
        element.addContent(child2);

        assertTrue("incorrect return on bogus child", !element.removeChildren("child"));
        assertNotNull("child incorrectly removed", element.getContent("child", ns));
        assertTrue("couldn't remove child content", element.removeChildren("child", ns));
        assertNull("children not removed", element.getContent("child", ns));
    }
*/

    /**
     * Test removeContent for a Comment
     */
    public void test_TCM__boolean_removeContent_OrgJdomComment() {
        Element element = new Element("element");
        Comment comm = new Comment("a comment");
        element.addContent(comm);

        assertTrue("couldn't remove comment content", element.removeContent(comm));
        assertTrue("didn't remove comment content", element.getContent().equals(Collections.EMPTY_LIST));
    }

    /**
     * Test removeContent for an Element.
     */
    public void test_TCM__boolean_removeContent_OrgJdomElement() {
        Element element = new Element("element");
        Element child = new Element("child");
        element.addContent(child);

        assertTrue("couldn't remove element content", element.removeContent(child));
        assertTrue("didn't remove element content", element.getContent().equals(Collections.EMPTY_LIST));
    }

    /**
     * Test removeContent for entities.
     */
    public void test_TCM__boolean_removeContent_OrgJdomEntity() {
        Element element = new Element("element");
        EntityRef ent = new EntityRef("anEntity");
        element.addContent(ent);

        assertTrue("couldn't remove entity content", element.removeContent(ent));
        assertTrue("didn't remove entity content", element.getContent().equals(Collections.EMPTY_LIST));
    }

    /**
     * Test removeContent for processing instructions.
     */
    public void test_TCM__boolean_removeContent_OrgJdomProcessingInstruction() {
        Element element = new Element("element");
        ProcessingInstruction pi = new ProcessingInstruction("aPi", "something");
        element.addContent(pi);

        assertTrue("couldn't remove entity content", element.removeContent(pi));
        assertTrue("didn't remove entity content", element.getContent().equals(Collections.EMPTY_LIST));
    }

    /**
     * Test hashcode functions.
     */
    public void test_TCM__int_hashCode() {
        Element element = new Element("test");
        //only an exception would be a problem
        int i = -1;
        try {
            i = element.hashCode();
        }
        catch(Exception e) {
            fail("bad hashCode");
        }


        Element element2 = new Element("test");
        //different Elements, same text
        int x = element2.hashCode();
        assertTrue("Different Elements with same value have same hashcode", x != i);
        Element element3 = new Element("test2");
        //only an exception would be a problem
        int y = element3.hashCode();
        assertTrue("Different Elements have same hashcode", y != x);

    }

    /**
     * Test that additionalNamespaces are returned.
     */
    public void test_TCM__List_getAdditionalNamespaces() {
        Element element = new Element("element");
        element.addNamespaceDeclaration(Namespace.getNamespace("x", "urn:foo"));
        element.addNamespaceDeclaration(Namespace.getNamespace("y", "urn:bar"));
        element.addNamespaceDeclaration(Namespace.getNamespace("z", "urn:baz"));

        List list = element.getAdditionalNamespaces();

        Namespace ns = (Namespace) list.get(0);
        assertTrue("didn't return added namespace", ns.getURI().equals("urn:foo"));
        ns = (Namespace) list.get(1);
        assertTrue("didn't return added namespace", ns.getURI().equals("urn:bar"));
        ns = (Namespace) list.get(2);
        assertTrue("didn't return added namespace", ns.getURI().equals("urn:baz"));
    }

    /**
     * Test that getAttribute returns all attributes of this element.
     */
    public void test_TCM__List_getAttributes() {
        Element element = new Element("test");
        Attribute att = new Attribute("anAttribute", "test");
        Attribute att2 = new Attribute("anotherAttribute", "test");
        Attribute att3 = new Attribute("anotherAttribute", "test", Namespace.getNamespace("x", "urn:JDOM"));
        element.setAttribute(att);
        element.setAttribute(att2);
        element.setAttribute(att3);

        List list = element.getAttributes();

        assertEquals("incorrect size returned", list.size(), 3);
        assertEquals("incorrect attribute returned", list.get(0), att);
        assertEquals("incorrect attribute returned", list.get(1), att2);
    }

    /**
     * Test getChildren to return all children from all namespaces.
     */
    public void test_TCM__List_getChildren() {
        Element element = new Element("el");
        assertEquals("did not return Collections.EMPTY_LIST on empty element", Collections.EMPTY_LIST, element.getChildren("child"));
        Element child1 = new Element("child");
        child1.setAttribute(new Attribute("name", "first"));

        Element child2 = new Element("child");
        child2.setAttribute(new Attribute("name", "second"));
        Element child3 = new Element("child", Namespace.getNamespace("ftp://wombat.stew"));

        element.addContent(child1);
        element.addContent(child2);
        element.addContent(child3);

        //should only get the child elements in NO_NAMESPACE
        List list = element.getChildren();
        assertEquals("incorrect number of children returned", list.size(), 3);
        assertEquals("incorrect child returned", list.get(0), child1);
        assertEquals("incorrect child returned", list.get(1), child2);
        assertEquals("incorrect child returned", list.get(2), child3);
    }

    /**
     * Test that Element returns a List of children by name
     */
    public void test_TCM__List_getChildren_String() {
        Element element = new Element("el");
        assertEquals("did not return Collections.EMPTY_LIST on empty element", Collections.EMPTY_LIST, element.getChildren("child"));
        Element child1 = new Element("child");
        child1.setAttribute(new Attribute("name", "first"));

        Element child2 = new Element("child");
        child2.setAttribute(new Attribute("name", "second"));
        Element child3 = new Element("child", Namespace.getNamespace("ftp://wombat.stew"));

        element.addContent(child1);
        element.addContent(child2);
        element.addContent(child3);

        //should only get the child elements in NO_NAMESPACE
        List list = element.getChildren("child");
        assertEquals("incorrect number of children returned", list.size(), 2);
        assertEquals("incorrect child returned", ((Element) list.get(0)).getAttribute("name").getValue(), "first");
        assertEquals("incorrect child returned", ((Element) list.get(1)).getAttribute("name").getValue(), "second");
    }

    /**
     * Test that Element returns a List of children by name and namespace
     */
    public void test_TCM__List_getChildren_String_OrgJdomNamespace() {
        Element element = new Element("el");
        assertEquals("did not return Collections.EMPTY_LIST on empty element", Collections.EMPTY_LIST, element.getChildren("child"));
        Namespace ns = Namespace.getNamespace("urn:hogwarts");
        Element child1 = new Element("child", ns);
        child1.setAttribute(new Attribute("name", "first"));

        Element child2 = new Element("child", ns);
        child2.setAttribute(new Attribute("name", "second"));
        Element child3 = new Element("child", Namespace.getNamespace("ftp://wombat.stew"));

        element.addContent(child1);
        element.addContent(child2);
        element.addContent(child3);

        //should only get the child elements in the hogwarts namespace
        List list = element.getChildren("child", ns);
        assertEquals("incorrect number of children returned", list.size(), 2);
        assertEquals("incorrect child returned", ((Element) list.get(0)).getAttribute("name").getValue(), "first");
        assertEquals("incorrect child returned", ((Element) list.get(1)).getAttribute("name").getValue(), "second");
    }

    /**
     * Test that getContent returns all the content for the element
     */
    public void test_TCM__List_getContent() {
        Element element = new Element("el");
        assertEquals("did not return Collections.EMPTY_LIST on empty element", Collections.EMPTY_LIST, element.getContent());
        Namespace ns = Namespace.getNamespace("urn:hogwarts");
        Element child1 = new Element("child", ns);
        child1.setAttribute(new Attribute("name", "first"));

        Element child2 = new Element("child", ns);
        child2.setAttribute(new Attribute("name", "second"));
        Element child3 = new Element("child", Namespace.getNamespace("ftp://wombat.stew"));

        element.addContent(child1);
        element.addContent(child2);
        element.addContent(child3);

        Comment comment = new Comment("hi");
        element.addContent(comment);
        CDATA cdata = new CDATA("gotcha");
        element.addContent(cdata);
        ProcessingInstruction pi = new ProcessingInstruction("tester", "do=something");
        element.addContent(pi);
        EntityRef entity = new EntityRef("wizards");
        element.addContent(entity);
        Text text = new Text("finally a new wand!");
        element.addContent(text);

        List list = element.getContent();

        assertEquals("incorrect number of content items", 8, list.size());
        assertEquals("wrong child element", child1, list.get(0));
        assertEquals("wrong child element", child2, list.get(1));
        assertEquals("wrong child element", child3, list.get(2));
        assertEquals("wrong comment", comment, list.get(3));
        assertEquals("wrong CDATA", cdata, list.get(4));
        assertEquals("wrong ProcessingInstruction", pi, list.get(5));
        assertEquals("wrong EntityRef", entity, list.get(6));
        assertEquals("wrong text", text, list.get(7));
    }

    /**
     * Test that clone returns a disconnected copy of the original element.
     * Since clone is a deep copy, that must be tested also
     */
    public void test_TCM__Object_clone() {

        //first the simple case
        Element element = new Element("simple");
        Element clone = (Element) element.clone();

        assertTrue("clone should not be the same object", element != clone);
        assertEquals("clone should not have a parent", null, clone.getParent());
        assertEquals("names do not match", element.getName(), clone.getName());

        //now do the content tests to 2 levels deep to verify recursion
        element = new Element("el");
        Namespace ns = Namespace.getNamespace("urn:hogwarts");
        element.setAttribute(new Attribute("name", "anElement"));
        Element child1 = new Element("child", ns);
        child1.setAttribute(new Attribute("name", "first"));

        Element child2 = new Element("firstChild", ns);
        child2.setAttribute(new Attribute("name", "second"));
        Element child3 = new Element("child", Namespace.getNamespace("ftp://wombat.stew"));
        child1.addContent(child2);
        element.addContent(child1);
        element.addContent(child3);

        element.addNamespaceDeclaration(Namespace.getNamespace("foo", "http://test1"));
        element.addNamespaceDeclaration(Namespace.getNamespace("bar", "http://test2"));

        //add mixed content to the nested child2 element
        Comment comment = new Comment("hi");
        child2.addContent(comment);
        CDATA cdata = new CDATA("gotcha");
        child2.addContent(cdata);
        ProcessingInstruction pi = new ProcessingInstruction("tester", "do=something");
        child2.addContent(pi);
        EntityRef entity = new EntityRef("wizards");
        child2.addContent(entity);
        child2.addContent("finally a new wand!");

        //a little more for the element
        element.addContent("top level element text");

        clone = (Element) element.clone();
        element = null;
        child3 = null;
        child2 = null;
        child1 = null;

        // additional namespaces
        List additional = clone.getAdditionalNamespaces();
        assertEquals("incorrect deep clone additional namespace",
                     (Namespace) additional.get(0),
                     Namespace.getNamespace("foo", "http://test1"));
        assertEquals("incorrect deep clone additional namespace",
                     (Namespace) additional.get(1),
                     Namespace.getNamespace("bar", "http://test2"));

        List list = clone.getContent();

        //finally the test

        assertEquals("wrong child element", ((Element) list.get(0)).getName(), "child");
        assertEquals("wrong child element", ((Element) list.get(1)).getName(), "child");
        Element deepClone = ((Element) list.get(0)).getChild("firstChild", Namespace.getNamespace("urn:hogwarts"));

        assertEquals("wrong nested element", "firstChild", deepClone.getName());
        //comment
        assertTrue("deep clone comment not a clone", deepClone.getContent().get(0) != comment);
        comment = null;
        assertEquals("incorrect deep clone comment", "hi", ((Comment) deepClone.getContent().get(0)).getText());
        //CDATA
        assertTrue("deep clone CDATA not a clone", ((CDATA) deepClone.getContent().get(1)).getText().equals(cdata.getText()));
        cdata = null;
        assertEquals("incorrect deep clone CDATA", "gotcha", ((CDATA) deepClone.getContent().get(1)).getText());
        //PI
        assertTrue("deep clone PI not a clone", deepClone.getContent().get(2) != pi);
        pi = null;
        assertEquals("incorrect deep clone PI", "do=something", ((ProcessingInstruction) deepClone.getContent().get(2)).getData());
        //entity
        assertTrue("deep clone Entity not a clone", deepClone.getContent().get(3) != entity);
        entity = null;
        assertEquals("incorrect deep clone EntityRef", "wizards", ((EntityRef) deepClone.getContent().get(3)).getName());
        //text
        assertEquals("incorrect deep clone test", "finally a new wand!", ((Text) deepClone.getContent().get(4)).getText());
    }

    /**
     * Test getAttribute by name
     */
    public void test_TCM__OrgJdomAttribute_getAttribute_String() {
        Element element = new Element("el");
        Attribute att = new Attribute("name", "first");
        element.setAttribute(att);
        assertEquals("incorrect Attribute returned", element.getAttribute("name"), att);
    }

    /**
     * Test getAttribute by name and namespace
     */
    public void test_TCM__OrgJdomAttribute_getAttribute_String_OrgJdomNamespace() {
        Element element = new Element("el");
        Namespace ns = Namespace.getNamespace("x", "urn:foo");
        Attribute att = new Attribute("name", "first", ns);
        element.setAttribute(att);
        element.setAttribute(new Attribute("name", "first"));
        assertEquals("incorrect Attribute returned", element.getAttribute("name", ns), att);

    }

    /**
     * Test that an element returns the reference to it's enclosing document
     */
    public void test_TCM__OrgJdomDocument_getDocument() {
        Element element = new Element("element");

        assertNull("incorrectly returned document before there is one", element.getDocument());

        Element child = new Element("child");
        Element child2 = new Element("child");
        element.addContent(child);
        element.addContent(child2);

        Document doc = new Document(element);

        assertEquals("didn't return correct Document", element.getDocument(), doc);
        assertEquals("didn't return correct Document", child.getDocument(), doc);
    }

    /**
     * Test addContent for CDATA.
     */
    public void test_TCM__OrgJdomElement_addContent_OrgJdomCDATA() {
        //positive test is covered in test__List_getContent()

        //test with null
        Element element = new Element("element");
        CDATA cdata = null;
        try {
            element.addContent(cdata);
            fail("didn't catch null CDATA element");
        }
        catch (IllegalAddException e) {
        }
        catch (NullPointerException e) {
            fail("NullPointer  Exception");
        }
    }

    /**
     * Test adding comment content.
     */
    public void test_TCM__OrgJdomElement_addContent_OrgJdomComment() {
        Element element = new Element("element");
        Comment comm = new Comment("a comment");
        element.addContent(comm);

        assertEquals("didn't add comment content", element.getContent().get(0), comm);
        try {
            comm = null;
            element.addContent(comm);
            fail("didn't catch null Comment");
        }
        catch (IllegalAddException e) {
        }
        catch (NullPointerException e) {
        }
    }

    /**
     * Test addContent for Element.
     */
    public void test_TCM__OrgJdomElement_addContent_OrgJdomElement() {
        //positive test is covered in test__List_getContent()

        //test with null
        Element element = new Element("element");
        try {
            Element el = null;
            element.addContent(el);
            fail("didn't catch null Element");
        }
        catch (IllegalAddException e) {
        }
        catch (NullPointerException e) {
        }
    }

    /**
     * Test addContent for Entity
     */
    public void test_TCM__OrgJdomElement_addContent_OrgJdomEntityRef() {
        //positive test is covered in test__List_getContent()

        //try with null
        Element element = new Element("element");
        try {
            EntityRef entity = null;
            element.addContent(entity);
            fail("didn't catch null EntityRef");
        }
        catch (IllegalAddException e) {
        }
        catch (NullPointerException e) {
        }
    }

    /**
     * Test addContent for ProcessingInstruction.
     */
    public void test_TCM__OrgJdomElement_addContent_OrgJdomProcessingInstruction() {
        //positive test is covered in test__List_getContent()

        //try with null data
        Element element = new Element("element");
        try {
            ProcessingInstruction pi = null;
            element.addContent(pi);
            fail("didn't catch null ProcessingInstruction");
        }
        catch (IllegalAddException e) {
        }
        catch (NullPointerException e) {
        }
    }

    /**
     * Test that string based content is added correctly to an Element.
     */
    public void test_TCM__OrgJdomElement_addContent_String() {
        Element element = new Element("element");
        element.addContent("the first part");
        assertEquals("incorrect string value", "the first part", element.getText());
        element.addContent("the second part");
        assertEquals("incorrect string value", "the first partthe second part", element.getText());
        //make sure it still works with mixed content
        element.addContent(new Element("child"));
        element.addContent("the third part");
        assertEquals("incorrect string value", "the first partthe second partthe third part", element.getText());

        //test that add content null clears the content
        try {
            String data = null;
            element.addContent(data);
            List content = element.getContent();
        }
        catch (IllegalAddException e) {
        }
        catch (NullPointerException e) {
            fail("didn't handle null String content");
        }

    }

    /**
     * Test getContent by child name.
     */
    public void test_TCM__OrgJdomElement_getChild_String() {
        Element element = new Element("element");
        Element child = new Element("child");
        Element childNS = new Element("child", Namespace.getNamespace("urn:foo"));
        element.addContent(child);
        assertEquals("incorrect child returned", element.getChild("child"), child);
    }

    /**
     * Test getContent by child name and namespace.
     */
    public void test_TCM__OrgJdomElement_getChild_String_OrgJdomNamespace() {
        Element element = new Element("element");
        Element child = new Element("child");
        Namespace ns = Namespace.getNamespace("urn:foo");
        Element childNS = new Element("child", ns);
        element.addContent(child);
        element.addContent(childNS);
        assertEquals("incorrect child returned", element.getChild("child", ns), childNS);
    }

    /**
     * Test getCopy with only the name argument. Since the copy is just a clone,
     * the clone test covers most of the testing.
     */
    public void test_TCM__OrgJdomElement_getCopy_String() {
        /** No op because the method is deprecated

         Element element = new Element("element", Namespace.getNamespace("urn:foo"));
         element.addContent("text");

         Element newElement = element.getCopy("new");
         assertEquals("incorrect name", "new", newElement.getName());
         assertEquals("incorrect namespace", Namespace.NO_NAMESPACE, newElement.getNamespace());
         assertEquals("incorrect content", "text", newElement.getText());
         */
    }

    /**
     * Test getCopy with the name and namespace arguments.
     * Since the copy is just a clone, the clone test
     * covers most of the testing.
     */
    public void test_TCM__OrgJdomElement_getCopy_String_OrgJdomNamespace() {
        /** No op because the method is deprecated

         Element element = new Element("element", Namespace.getNamespace("urn:foo"));
         element.addContent("text");
         Namespace ns = Namespace.getNamespace("urn:test:new");
         Element newElement = element.getCopy("new", ns);
         assertEquals("incorrect name", "new", newElement.getName());
         assertEquals("incorrect namespace", ns, newElement.getNamespace());
         assertEquals("incorrect content", "text", newElement.getText());
         */
    }

    /**
     * Test test that a child element can return it's parent.
     */
    public void test_TCM__OrgJdomElement_getParent() {
        Element element = new Element("el");
        Element child = new Element("child");
        element.addContent(child);

        assertEquals("parent not found", element, child.getParent());
    }

    /**
     * Test addAttribute with an Attribute
     */
    public void test_TCM__OrgJdomElement_setAttribute_OrgJdomAttribute() {
        Element element = new Element("el");
        Attribute att = new Attribute("name", "first");
        element.setAttribute(att);
        assertEquals("incorrect Attribute returned", element.getAttribute("name"), att);

        //update the value
        Attribute att2 = new Attribute("name", "replacefirst");
        element.setAttribute(att2);
        assertEquals("incorrect Attribute value returned", "replacefirst", element.getAttribute("name").getValue());

        //test with bad data
        try {
            element.setAttribute(null);
            fail("didn't catch null attribute");
        }
        catch (IllegalArgumentException e) {
        }
        catch (NullPointerException e) {
        }
    }

    /**
     * Test addAttribute with a supplied name and value
     */
    public void test_TCM__OrgJdomElement_setAttribute_String_String__invalidCharacters() {
        final Element element = new Element("el");

		//try with null name
        try { 
            element.setAttribute(null, "value");
            fail("didn't catch null attribute name");
        }
        catch (final IllegalArgumentException ignore) {
        }
        catch (NullPointerException ignore) {
        }

		//try with null value
        try {
            element.setAttribute("name2", null);
            fail("didn't catch null attribute value");
        }
        catch (IllegalArgumentException e) {
        }
        catch (NullPointerException e) {
        }

        //try with bad characters
        try {
            element.setAttribute("\n", "value");
            fail("didn't catch bad characters in attribute name");
        }
        catch (IllegalArgumentException e) {
        }

        try {
            element.setAttribute("name2", "" + (char) 0x01);
            fail("didn't catch bad characters  in attribute value");
        }
        catch (IllegalArgumentException e) {
        }
    }

    /**
     * Test addAttribute with a supplied name and value
     */
    public void test_setAttribute_String_String__attributeTypes() {
    	test_setAttribute_String_String__attributeType(Attribute.UNDECLARED_TYPE);    
    	test_setAttribute_String_String__attributeType(Attribute.CDATA_TYPE);    
    	test_setAttribute_String_String__attributeType(Attribute.ID_TYPE);    
    	test_setAttribute_String_String__attributeType(Attribute.IDREF_TYPE);    
    	test_setAttribute_String_String__attributeType(Attribute.IDREFS_TYPE);    
    	test_setAttribute_String_String__attributeType(Attribute.ENTITY_TYPE);    
    	test_setAttribute_String_String__attributeType(Attribute.ENTITIES_TYPE);    
    	test_setAttribute_String_String__attributeType(Attribute.NMTOKEN_TYPE);    
    	test_setAttribute_String_String__attributeType(Attribute.NMTOKENS_TYPE);    
    	test_setAttribute_String_String__attributeType(Attribute.NOTATION_TYPE);    
    	test_setAttribute_String_String__attributeType(Attribute.ENUMERATED_TYPE);    
    }

    /**
     * Test setAttribute with a supplied name and value
     */
    private void test_setAttribute_String_String__attributeType(final int attributeType) {
        final Element element = new Element("el");

        final String attributeName = "name";
		final String attributeValue = "value";
        final String attributeNewValue = "newValue";

		final Attribute attribute = new Attribute(attributeName, attributeValue, attributeType);

		// add attribute to element
		element.setAttribute(attribute);

		final Attribute foundAttribute = element.getAttribute(attributeName);
        assertNotNull("no Attribute found", foundAttribute);
        
        assertEquals("incorrect Attribute name returned", attributeName, foundAttribute.getName());
        assertEquals("incorrect Attribute value returned", attributeValue, foundAttribute.getValue());
        assertEquals("incorrect Attribute type returned", attributeType, foundAttribute.getAttributeType());

		//update the value
        element.setAttribute(attributeName, attributeNewValue);

		final Attribute changedAttribute = element.getAttribute(attributeName);
        assertNotNull("no Attribute found", changedAttribute);
        
        assertEquals("incorrect Attribute name returned", attributeName, changedAttribute.getName());
        assertEquals("incorrect Attribute value returned", attributeNewValue, changedAttribute.getValue());
        assertEquals("incorrect Attribute type returned", attributeType, changedAttribute.getAttributeType());
    }

    /**
     * Test addAttribute with a supplied name and value
     */
    public void test_setAttribute_String_String_String__attributeTypes() {
        test_setAttribute_String_String_String__attributeType(Attribute.UNDECLARED_TYPE);    
        test_setAttribute_String_String_String__attributeType(Attribute.CDATA_TYPE);    
        test_setAttribute_String_String_String__attributeType(Attribute.ID_TYPE);    
        test_setAttribute_String_String_String__attributeType(Attribute.IDREF_TYPE);    
        test_setAttribute_String_String_String__attributeType(Attribute.IDREFS_TYPE);    
        test_setAttribute_String_String_String__attributeType(Attribute.ENTITY_TYPE);    
        test_setAttribute_String_String_String__attributeType(Attribute.ENTITIES_TYPE);    
        test_setAttribute_String_String_String__attributeType(Attribute.NMTOKEN_TYPE);    
        test_setAttribute_String_String_String__attributeType(Attribute.NMTOKENS_TYPE);    
        test_setAttribute_String_String_String__attributeType(Attribute.NOTATION_TYPE);    
        test_setAttribute_String_String_String__attributeType(Attribute.ENUMERATED_TYPE);    
    }

    /**
     * Test setAttribute with a supplied name and value
     * 
     * @author Victor Toni
     */
    private void test_setAttribute_String_String_String__attributeType(final int attributeType) {
        try {
            final Namespace defaultNamespace = Namespace.getNamespace(null, "http://test.org/default");
            test_setAttribute_String_String_String__attributeType(defaultNamespace, attributeType);
            fail("didn't catch empty prefix for attribute ");
        }
        catch( final IllegalNameException ignore) {
            
        }

        final Namespace testNamespace = Namespace.getNamespace("test", "http://test.org/test");
        test_setAttribute_String_String_String__attributeType(testNamespace, attributeType);
    }

    /**
     * Test setAttribute with a supplied name, namespace and value
     * 
     * @author Victor Toni
     */
    private void test_setAttribute_String_String_String__attributeType(final Namespace namespace, final int attributeType) {
        final Element element = new Element("el");

        final String attributeName = "name";
        final String attributeValue = "value";
        final String attributeNewValue = "newValue";

        final Attribute attribute = new Attribute(attributeName, attributeValue, attributeType, namespace);

        // add attribute to element
        element.setAttribute(attribute);

        final Attribute foundAttribute = element.getAttribute(attributeName, namespace);
        assertNotNull("no Attribute found", foundAttribute);
        
        assertEquals("incorrect Attribute name returned", attributeName, foundAttribute.getName());
        assertEquals("incorrect Attribute value returned", attributeValue, foundAttribute.getValue());
        assertEquals("incorrect Attribute type returned", attributeType, foundAttribute.getAttributeType());

        //update the value
        element.setAttribute(attributeName, attributeNewValue, namespace);

        final Attribute changedAttribute = element.getAttribute(attributeName, namespace);
        assertNotNull("no Attribute found", changedAttribute);
        
        assertEquals("incorrect Attribute name returned", attributeName, changedAttribute.getName());
        assertEquals("incorrect Attribute value returned", attributeNewValue, changedAttribute.getValue());
        assertEquals("incorrect Attribute type returned", attributeType, changedAttribute.getAttributeType());
    }

    /**
     * Test that attributes are added correctly as a list
     */
    public void test_TCM__OrgJdomElement_setAttributes_List() {
        Element element = new Element("element");
        Attribute one = new Attribute("one", "value");
        Attribute two = new Attribute("two", "value");
        Attribute three = new Attribute("three", "value");
        ArrayList list = new ArrayList();
        list.add(one);
        list.add(two);
        list.add(three);

        //put in an attribute that will get blown away later
        element.setAttribute(new Attribute("type", "test"));
        element.setAttributes(list);
        assertEquals("attribute not found", one, element.getAttribute("one"));
        assertEquals("attribute not found", two, element.getAttribute("two"));
        assertEquals("attribute not found", three, element.getAttribute("three"));
        assertNull("attribute should not have been found", element.getAttribute("type"));


        //now try to add something that isn't an attribute which should still add those
        //attributes added before the mistake.
        Element bogus = new Element("bogus");
        Attribute four = new Attribute("four", "value");

        ArrayList newList = new ArrayList();
        newList.add(four);
        newList.add(bogus);
        try {
            element.setAttributes(newList);
            fail("didn't catch bad data in list");
        }
        catch (IllegalAddException e) {
        }
        //should be an atomic operation so the original state should be preserved
        assertEquals("wrong number of attributes after failed add", 3, element.getAttributes().size());
        assertEquals("attribute not found", one, element.getAttribute("one"));
        assertEquals("attribute not found", two, element.getAttribute("two"));
        assertEquals("attribute not found", three, element.getAttribute("three"));

        try {
            element.setAttributes(null);
            List attList = element.getAttributes();
            assertTrue("null didn't clear attributes", attList.size() == 0);
        }
        catch (IllegalArgumentException e) {
            fail("didn't handle null String content");
        }
        catch (NullPointerException e) {
            fail("didn't handle null String content");
        }
    }

    /**
     * Test setting child elements in a list.
     */
/*
    public void test_TCM__OrgJdomElement_setChildren_List() {
        Element element = new Element("el");
        assertEquals("did not return Collections.EMPTY_LIST on empty element", Collections.EMPTY_LIST, element.getChildren("child"));
        Element child1 = new Element("child");
        child1.setAttribute(new Attribute("name", "first"));

        Element child2 = new Element("child");
        child2.setAttribute(new Attribute("name", "second"));
        Element child3 = new Element("child", Namespace.getNamespace("ftp://wombat.stew"));

        ArrayList list = new ArrayList();
        list.add(child1);
        list.add(child2);
        list.add(child3);

        element.setChildren(list);

        //should only get the child elements in all namespaces
        List contentList = element.getChildren();
        assertEquals("incorrect number of children returned", 3, contentList.size());
        assertEquals("incorrect child returned", contentList.get(0), child1);
        assertEquals("incorrect child returned", contentList.get(1), child2);
        assertEquals("incorrect child returned", contentList.get(2), child3);

        ArrayList newList = new ArrayList();
        newList.add(child1);
        newList.add(new Attribute("name", "bogus"));

        try {
            element.setChildren(newList);
            fail("didn't catch a bad object in list");
        }
        catch (IllegalArgumentException e) {
        }
        //should be an atomic operation
        contentList = element.getChildren();
        assertEquals("wrong number of children after failed add", 3, contentList.size());
        assertEquals("incorrect child returned after failed add", contentList.get(0), child1);
        assertEquals("incorrect child returned after failed add", contentList.get(1), child2);
        assertEquals("incorrect child returned after failed add", contentList.get(2), child3);


        //nulls should reset the list
        element.setContent(null);
        assertTrue("didn't reset children List", element.getContent().isEmpty());
    }
*/

    /**
     * Test adding mixed content in a List
     */
    public void test_TCM__OrgJdomElement_setContent_List() {
        Element element = new Element("el");
        assertEquals("did not return Collections.EMPTY_LIST on empty element", Collections.EMPTY_LIST, element.getContent());
        Namespace ns = Namespace.getNamespace("urn:hogwarts");
        Element child1 = new Element("child", ns);

        Element child2 = new Element("child", ns);
        Element child3 = new Element("child", Namespace.getNamespace("ftp://wombat.stew"));

        LinkedList list = new LinkedList();
        list.add(child1);
        list.add(child2);
        list.add(child3);

        Comment comment = new Comment("hi");
        list.add(comment);
        CDATA cdata = new CDATA("gotcha");
        list.add(cdata);
        ProcessingInstruction pi = new ProcessingInstruction("tester", "do=something");
        list.add(pi);
        EntityRef entity = new EntityRef("wizards");
        list.add(entity);
        Text text = new Text("finally a new wand!");
        list.add(text);

        element.setContent(list);
        List contentList = element.getContent();

        assertEquals("incorrect number of content items", 8, contentList.size());
        assertEquals("wrong child element", child1, contentList.get(0));
        assertEquals("wrong child element", child2, contentList.get(1));
        assertEquals("wrong child element", child3, contentList.get(2));
        assertEquals("wrong comment", comment, contentList.get(3));
        assertEquals("wrong CDATA", cdata, contentList.get(4));
        assertEquals("wrong ProcessingInstruction", pi, contentList.get(5));
        assertEquals("wrong EntityRef", entity, contentList.get(6));
        assertEquals("wrong text", text, contentList.get(7));

        ArrayList newList = new ArrayList();
        //test adding a bad object type in the list
        newList.add(child1);
        newList.add(new Integer(7));

        try {
            element.setContent(list);
            fail("didn't catch bad object type in list");
        }
        catch (IllegalAddException e) {
        }

        //should add content up to the point of the bad object in the list
        contentList = element.getContent();
        assertEquals("wrong child element after failed add", child1, contentList.get(0));
        assertEquals("wrong child element after failed add", child2, contentList.get(1));
        assertEquals("wrong child element after failed add", child3, contentList.get(2));
        assertEquals("wrong comment after failed add", comment, contentList.get(3));
        assertEquals("wrong CDATA after failed add", cdata, contentList.get(4));
        assertEquals("wrong ProcessingInstruction after failed add", pi, contentList.get(5));
        assertEquals("wrong EntityRef after failed add", entity, contentList.get(6));
        assertEquals("wrong text after failed add", text, contentList.get(7));


        //nulls should reset the list
        element.removeContent();
        assertTrue("didn't reset mixed content List", element.getContent().isEmpty());
    }

    /**
     * Test setting the content of the Element with just a string
     * This should wipe out all other content the element has
     */
    public void test_TCM__OrgJdomElement_setText_String() {
        Element element = new Element("el");
        Element child = new Element("child");
        element.addContent(child);

        element.setText("it's all gone");
        assertEquals("incorrect text returned", "it's all gone", element.getText());
        assertEquals("incorrect number of content items found", 1, element.getContent().size());


        element.setText(null);
        assertTrue("didn't clear content with null text", element.getContent().isEmpty());

        //bad string test

        /** skip this test unless we determine that it is required
         to check textual content for validity.  We have the means to do
         it in the validator but don't have it turned on right now for
         performance reasons where the parser has already checked this
         in the majority of cases.


         try {
         element.setText("test" + (char)0x01);
         fail("didn't catch text with invalid character");
         } catch (IllegalArgumentException e) {
         } catch (NullPointerException e) {
         fail("NullPointerException");
         }

         */
    }

    /**
     * Test that the Element returns it's primary namespace
     */
    public void test_TCM__OrgJdomNamespace_getNamespace() {
        Namespace ns = Namespace.getNamespace("urn:test:foo");
        Element element = new Element("element", ns);

        assertEquals("wrong namespace returned", ns, element.getNamespace());
    }

    /**
     * Test that Element can return a namespace given a uri
     */
    public void test_TCM__OrgJdomNamespace_getNamespace_String() {
        Namespace ns = Namespace.getNamespace("x", "urn:test:foo");
        Element element = new Element("element", ns);

        assertEquals("wrong namespace returned", ns, element.getNamespace("x"));
        assertNull("no namespace should have been found", element.getNamespace("bogus"));

        //now make sure it can return the namespace from the additional namespaces
        Namespace newNs = Namespace.getNamespace("y", "urn:test:new");
        element.addNamespaceDeclaration(newNs);
        assertEquals("wrong namespace returned", newNs, element.getNamespace("y"));

        //now make sure the same thing works from a child
        Element child = new Element("child");
        element.addContent(child);

        assertEquals("wrong namespace returned", ns, child.getNamespace("x"));
        assertNull("no namespace should have been found", child.getNamespace("bogus"));
        assertEquals("wrong namespace returned", newNs, child.getNamespace("y"));
    }

    /**
     * Test getAttributeValue by attribute name.
     */
    public void test_TCM__String_getAttributeValue_String() {
        Element element = new Element("el");
        element.setAttribute(new Attribute("name", "first"));
        assertEquals("incorrect value returned", element.getAttributeValue("name"), "first");
    }

    /**
     * Test getAttributeValue with name and namespace
     */
    public void test_TCM__String_getAttributeValue_String_OrgJdomNamespace() {
        Element element = new Element("el");
        element.setAttribute(new Attribute("name", "first", Namespace.getNamespace("x", "urn:WombatsRUS")));
        assertEquals("incorrect value returned", element.getAttributeValue("name", Namespace.getNamespace("x", "urn:WombatsRUS")), "first");
    }

    /**
     * Test the convience method for retrieving child text.
     */
    public void test_TCM__String_getChildText_String() {
        Element element = new Element("element");
        Element child = new Element("child");
        child.addContent("  some text \nboo  ");
        element.addContent(child);

        assertEquals("incorrect text returned", "  some text \nboo  ", element.getChildText("child"));
    }

    /**
     * Test the convience method for retrieving child text for a child
     * retrieved by name and namespace
     */
    public void test_TCM__String_getChildText_String_OrgJdomNamespace() {
        Element element = new Element("element");
        Namespace ns = Namespace.getNamespace("urn:test:foo");
        Element child = new Element("child", ns);
        child.addContent("  some text \nboo  ");
        element.addContent(child);

        assertEquals("incorrect text returned", "  some text \nboo  ", element.getChildText("child", ns));

    }

    /**
     * Test the convience method for retrieving trimmed child text.
     */
    public void test_TCM__String_getChildTextTrim_String() {
        Element element = new Element("element");
        Element child = new Element("child");
        child.addContent("  some text  \n ");
        element.addContent(child);

        assertEquals("incorrect text returned", "some text", element.getChildTextTrim("child"));
    }

    /**
     * Test the convience method for retrieving trimmed child text for the
     * child in the given namespace
     */
    public void test_TCM__String_getChildTextTrim_String_OrgJdomNamespace() {
        Element element = new Element("element");
        Namespace ns = Namespace.getNamespace("urn:test:foo");
        Element child = new Element("child", ns);
        child.addContent("  some text  \n ");
        element.addContent(child);

        assertEquals("incorrect text returned", "some text", element.getChildTextTrim("child", ns));
    }

    /**
     * Test getName.
     */
    public void test_TCM__String_getName() {
        Element element = new Element("element", Namespace.getNamespace("x", "ftp://wombat.stew"));
        assertEquals("incorrect name", element.getName(), "element");
    }

    /**
     * Test getNamespacePrefix.
     */
    public void test_TCM__String_getNamespacePrefix() {
        Element element = new Element("element", Namespace.getNamespace("x", "ftp://wombat.stew"));
        assertEquals("incorrect namespace prefix", element.getNamespacePrefix(), "x");
    }

    /**
     * Test code goes here. Replace this comment.
     */
    public void test_TCM__String_getNamespaceURI() {
        Element element = new Element("element", Namespace.getNamespace("x", "ftp://wombat.stew"));
        assertEquals("incorrect uri", element.getNamespaceURI(), "ftp://wombat.stew");
    }

    /**
     * Test that Element returns the correct qualified name.
     */
    public void test_TCM__String_getQualifiedName() {
        Element element = new Element("element", Namespace.getNamespace("x", "ftp://wombat.stew"));
        assertEquals("incorrect qualified name", element.getQualifiedName(), "x:element");
    }

    /**
     * Test getSerializedForm
     */
    public void test_TCM__String_getSerializedForm() {
//        fail("method not implemented");
    }

    /**
     * Test getText returns that full text of the element
     */
    public void test_TCM__String_getText() {
        Element element = new Element("element");
        Element child = new Element("child");
        element.addContent("  some text \nboo  ");

        assertEquals("incorrect text returned", "  some text \nboo  ", element.getText());
    }

    /**
     * Test getTextTrim.
     */
    public void test_TCM__String_getTextTrim() {
        Element element = new Element("element");
        element.addContent("  some text  \n ");

        assertEquals("incorrect text returned", "some text", element.getTextTrim());
    }

    /**
     * Test the toString method which should return a useful string
     * for debugging purposes only
     */
    public void test_TCM__String_toString() {
        Element element = new Element("element", Namespace.getNamespace("urn:foo"));
        element.setAttribute(new Attribute("name", "aName"));

        assertEquals("wrong toString text found", "[Element: <element [Namespace: urn:foo]/>]", element.toString());
    }

    /**
     * Test addNamespaceDeclaration for prefix and uri.
     */
    public void test_TCM__void_addNamespaceDeclaration_OrgJdomNamespace() {
        Element element = new Element("element");
        element.addNamespaceDeclaration(Namespace.getNamespace("x", "urn:foo"));
        List list = element.getAdditionalNamespaces();

        Namespace ns = (Namespace) list.get(0);
        assertTrue("didn't return added namespace", ns.getURI().equals("urn:foo"));
        assertTrue("didn't return added namespace prefix", ns.getPrefix().equals("x"));
    }

    /**
     * Test that attributes will be added and retrieved according
     * to specs. with namespaces and prefixes intact
     *
     */
    public void test_TCU__testAttributeNamespaces() {

        //set up an element and namespaces to test with

        Element el = new Element("test");
        Namespace one = Namespace.getNamespace("test", "http://foo");
        Namespace two = Namespace.getNamespace("test2", "http://foo");

        Attribute att = new Attribute("first", "first", one);
        Attribute att2 = new Attribute("first", "second", two);

        //should overwrite attributes with same names even if prefixes are different
        try {
            el.setAttribute(att);
            el.setAttribute(att2);
            assertTrue("didn't catch duplicate setAttribute with different prefixes",
                       el.getAttribute("first", one).getValue().equals("second"));
            assertTrue("didn't catch duplicate setAttribute with different prefixes",
                       el.getAttribute("first", two).getNamespace().equals(two));
        }
        catch (IllegalAddException e) {
        }

        //set up some unique namespaces for later tests
        Namespace testDefault = Namespace.getNamespace("http://bar");
        Namespace testNS = Namespace.getNamespace("test", "http://foo");
        Namespace testNS2 = Namespace.getNamespace("test2", "http://foo2");

        //this should cause an error since the prefix will be discarded
        Namespace testNS3;
        try {
            testNS3 = Namespace.getNamespace("test", "");
            fail("didn't catch bad \"\" uri name");
        }
        catch (Exception e) {
        }

        //show how you can have an empty namespace with the current scheme
        testNS3 = Namespace.getNamespace("");

        el = new Element("test", testDefault);
        el.addNamespaceDeclaration(testNS2);
        try {

            el.addNamespaceDeclaration(testNS3);
        }
        catch (IllegalAddException e) {
        }

        att = new Attribute("prefixNS", "test");

        //Test for default namespace check for attribute
        att2 = new Attribute("prefixNS", "empty namespace");


        //test prefixes with same name but different namespaces
        Attribute att3 = new Attribute("prefixNS", "test", testNS);
        Attribute att4 = new Attribute("prefixNS", "test2", testNS2);
        el.setAttribute(att2);
        el.setAttribute(att3);
        el.setAttribute(att4);

        Attribute attback = el.getAttribute("prefixNS");
        assertTrue(
                "failed to get attribute from empty default namespace",
                attback.getNamespaceURI().equals(""));
        attback = el.getAttribute("prefixNS", testNS3);
        assertTrue(
                "failed to get attribute from http://bar namespace",
                attback.getNamespaceURI().equals(""));
        attback = el.getAttribute("prefixNS", testNS);
        assertTrue(
                "failed to get attribute from http://foo namespace",
                attback.getNamespaceURI().equals("http://foo"));
        attback = el.getAttribute("prefixNS", testNS2);
        assertTrue(
                "failed to get attribute from http://foo2 namespace",
                attback.getNamespaceURI().equals("http://foo2"));
    }

    /**
     * Test that an Element properly handles default namespaces
     *
     */
    public void test_TCU__testDefaultNamespaces() throws IOException {

        //set up an element to test with
        Element element = new Element("element", Namespace.getNamespace("http://foo"));
        Element child1 = new Element("child1", Namespace.getNamespace("http://foo"));
        Element child2 = new Element("child2", Namespace.getNamespace("http://foo"));

        element.addContent(child1);
        element.addContent(child2);

        //here is what we expect in these two scenarios
        String bufWithNoNS = "<element xmlns=\"http://foo\"><child1 /><child2 /></element>";

        String bufWithEmptyNS = "<element xmlns=\"http://foo\"><child1 xmlns=\"\" /><child2 xmlns=\"\" /></element>";

        StringWriter sw = new StringWriter();
        XMLOutputter op = new XMLOutputter();
        op.output(element, sw);
        assertTrue("Incorrect output for NO_NAMESPACE in a default namespace", sw.toString().equals(bufWithNoNS));

        //new try setting a new empty default namespace for children
        element = new Element("element", Namespace.getNamespace("http://foo"));
        child1 = new Element("child1");
        child2 = new Element("child2");

        element.addContent(child1);
        element.addContent(child2);
        sw = new StringWriter();
        op = new XMLOutputter();
        op.output(element, sw);
        assertTrue("Incorrect output for empty default namespace", sw.toString().equals(bufWithEmptyNS));


        //this code tests where multiple default namespaces disallowed
        try {
            Element el = new Element("test");
            el.addNamespaceDeclaration(Namespace.getNamespace("", "foo:bar"));
            el.addNamespaceDeclaration(Namespace.getNamespace("", "foo2:bar"));
            el.addNamespaceDeclaration(Namespace.NO_NAMESPACE);
            fail("didn't catch multiple default namespaces added to an element");

        }
        catch (IllegalAddException e) {
        }
    }

    /**
     * Test that Element serialization works, including with namespaces
     *
     */
    public void test_TCU__testSerialization() throws IOException, ClassNotFoundException {

        //set up an element to test with
        Element element = new Element("element", Namespace.getNamespace("http://foo"));
        Element child1 = new Element("child1");
        child1.setAttribute(new Attribute("anAttribute", "no namespace"));
        Element child2 = new Element("child2");
        Attribute att1 = new Attribute("anAttribute", "with namespace", Namespace.getNamespace("x", "http://foo"));
        child2.setAttribute(att1);
        //add another child level deep
        Element descendent = new Element("descendent");
        child2.addContent(descendent);
        element.addContent(child1);
        element.addContent(child2);



        //here is what we expect back after serialization
        String bufWithEmptyNS =
                "<element xmlns=\"http://foo\"><child1 xmlns=\"\" anAttribute=\"no namespace\" /><child2 xmlns=\"\" xmlns:x=\"http://foo\" x:anAttribute=\"with namespace\"><descendent /></child2></element>";

        File dir = new File(scratchDir);
        dir.mkdirs();

        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(scratchDir + "/object.ser"));
        out.writeObject(element);

        ObjectInputStream in = new ObjectInputStream(new FileInputStream(scratchDir + "/object.ser"));

        Element elIn = (Element) in.readObject();

        StringWriter sw = new StringWriter();
        XMLOutputter op = new XMLOutputter();
        op.output(elIn, sw);
        assertTrue("Incorrect data after serialization", sw.toString().equals(bufWithEmptyNS));

        //set up an element to test with
        Element element2 = new Element("element", Namespace.getNamespace("http://foo"));
        element2.addNamespaceDeclaration(Namespace.getNamespace("foo", "http://test1"));
        element2.addNamespaceDeclaration(Namespace.getNamespace("bar", "http://test2"));
        Element child21 = new Element("child1");
        child21.setAttribute(new Attribute("anAttribute", "no namespace"));
        Element child22 = new Element("child2");
        Attribute att21 = new Attribute("anAttribute", "with namespace", Namespace.getNamespace("x", "http://foo"));
        child22.setAttribute(att21);
        //add another child level deep
        Element descendent2 = new Element("descendent");
        child22.addContent(descendent2);
        element2.addContent(child21);
        element2.addContent(child22);

        //here is what we expect back after serialization
        String bufWithEmptyNS2 =
                "<element xmlns=\"http://foo\" xmlns:foo=\"http://test1\" xmlns:bar=\"http://test2\"><child1 xmlns=\"\" anAttribute=\"no namespace\" /><child2 xmlns=\"\" xmlns:x=\"http://foo\" x:anAttribute=\"with namespace\"><descendent /></child2></element>";

        File dir2 = new File(scratchDir);
        dir2.mkdirs();

        ObjectOutputStream out2 = new ObjectOutputStream(new FileOutputStream(scratchDir + "/object.ser"));
        out2.writeObject(element2);

        ObjectInputStream in2 = new ObjectInputStream(new FileInputStream(scratchDir + "/object.ser"));

        Element elIn2 = (Element) in2.readObject();

        StringWriter sw2 = new StringWriter();
        XMLOutputter op2 = new XMLOutputter();
        op.output(elIn2, sw2);
        assertTrue("Incorrect data after serialization", sw2.toString().equals(bufWithEmptyNS2));
    }

  public void test_AddingString() {
    Vector v = new Vector();
    v.add("one");
    Element e = new Element("e");
    e.setContent(v);
  }
}
