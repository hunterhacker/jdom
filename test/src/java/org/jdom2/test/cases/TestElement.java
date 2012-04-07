package org.jdom2.test.cases;

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

import static org.jdom2.test.util.UnitTestUtil.checkException;
import static org.jdom2.test.util.UnitTestUtil.failNoException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.JUnitCore;

import org.jdom2.Attribute;
import org.jdom2.AttributeType;
import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.Content;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.EntityRef;
import org.jdom2.IllegalAddException;
import org.jdom2.IllegalNameException;
import org.jdom2.Namespace;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;
import org.jdom2.Content.CType;
import org.jdom2.filter.ContentFilter;
import org.jdom2.filter.ElementFilter;
import org.jdom2.filter.Filters;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.test.util.UnitTestUtil;

@SuppressWarnings("javadoc")
public final class TestElement {

    /**
     * The main method runs all the tests in the text ui
     */
    public static void main(String args[]) {
        JUnitCore.runClasses(TestElement.class);
    }

    /**
     * Test the constructor for a subclass element
     */
    @Test
    public void test_TCC() {
    	Element emt = new Element() {
    		// change nothing
    		private static final long serialVersionUID = 200L;
    	};
    	assertNull(emt.getName());
    }

    /**
     * Test the constructor for an empty element
     */
    @Test
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
			// Do nothing
		} catch (Exception e) {
			fail("Unexpected exception " + e.getClass());
        }

        //name can't be null
        try {
            el = new Element(null);
            fail("allowed creation of an element with null name");
        }
        catch (IllegalNameException e) {
			// Do nothing
		} catch (Exception e) {
			fail("Unexpected exception " + e.getClass());
        }

        //we can assume the Verifier has been called by now so we are done
    }

    /**
     * Test the Element constructor with an name and namespace
     */
    @Test
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
			// Do nothing
		} catch (Exception e) {
			fail("Unexpected exception " + e.getClass());
        }

        //name can't be null
        try {
            el = new Element(null, ns);
            fail("allowed creation of an element with null name");
        }
        catch (IllegalNameException e) {
			// Do nothing
		} catch (Exception e) {
			fail("Unexpected exception " + e.getClass());
        }

        //we can assume the Verifier has been called by now so we are done
    }

    /**
     * Test the Element constructor with a string default namespace
     */
    @Test
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
			// Do nothing
		} catch (Exception e) {
			fail("Unexpected exception " + e.getClass());
        }

        //name can't be null
        try {
            el = new Element(null, "urn:foo");
            fail("allowed creation of an element with null name");
        }
        catch (IllegalNameException e) {
			// Do nothing
		} catch (Exception e) {
			fail("Unexpected exception " + e.getClass());
        }

        //we can assume the Verifier has been called by now so we are done
    }

    /**
     * Test the Element constructor with a namespace uri and prefix
     */
    @Test
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
			// Do nothing
		} catch (Exception e) {
			fail("Unexpected exception " + e.getClass());
        }

        //name can't be null
        try {
            el = new Element(null, "x", "urn:foo");
            fail("allowed creation of an element with null name");
        }
        catch (IllegalNameException e) {
			// Do nothing
		} catch (Exception e) {
			fail("Unexpected exception " + e.getClass());
        }

        //we can assume the Verifier has been called by now so we are done

    }

    /**
     * Test the equals compares only object instances
     */
    @Test
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
    @Test
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
    @Test
    public void test_TCM__boolean_isRootElement() {
        Element element = new Element("element");
        assertTrue("incorrectly identified element as root", !element.isRootElement());

        new Document(element);
        assertTrue("incorrectly identified element as non root", element.isRootElement());
    }

    /**
     * Test than an Element can remove an Attribute by name
     */
    @Test
    public void test_TCM__boolean_removeAttribute_String() {
        Element element = new Element("test");
        assertFalse(element.removeAttribute("att"));
        Attribute att = new Attribute("anAttribute", "test");
        element.setAttribute(att);

        //make sure it's there
        assertNotNull("attribute not found after add", element.getAttribute("anAttribute"));

        //and remove it
        assertTrue("attribute not removed", element.removeAttribute("anAttribute"));
        //make sure it's not there
        assertNull("attribute found after remove", element.getAttribute("anAttribute"));
        assertFalse("non-existing attribute remove returned true", element.removeAttribute("anAttribute"));
    }

    /**
     * Test than an Element can remove an Attribute by name
     */
    @Test
    public void test_TCM__boolean_removeAttribute_Attribute() {
        Element element = new Element("test");
        Attribute att = new Attribute("anAttribute", "test");
        assertFalse(element.removeAttribute(att));
        
        element.setAttribute(att);

        //make sure it's there
        assertNotNull("attribute not found after add", element.getAttribute("anAttribute"));

        //and remove it
        assertTrue("attribute not removed", element.removeAttribute(att));
        //make sure it's not there
        assertNull("attribute found after remove", element.getAttribute("anAttribute"));
        assertFalse("non-existing attribute remove returned true", element.removeAttribute(att));
    }

    /**
     * Test removeAttribute with a namespace
     */
    @Test
    public void test_TCM__boolean_removeAttribute_String_OrgJdomNamespace() {
        Element element = new Element("test");
        Namespace ns = Namespace.getNamespace("x", "urn:test");
        assertFalse(element.removeAttribute("anAttribute", ns));
        
        Attribute att = new Attribute("anAttribute", "test", ns);
        element.setAttribute(att);

        //make sure it's there
        assertNotNull("attribute not found after add", element.getAttribute("anAttribute", ns));
        //make sure we check namespaces - a different namespace
        assertFalse("wrong attribute removed", element.removeAttribute("anAttribute", Namespace.NO_NAMESPACE));
        //and remove it
        assertTrue("attribute not removed", element.removeAttribute("anAttribute", ns));
        //make sure it's not there
        assertNull("attribute found after remove", element.getAttribute("anAttribute", ns));
    }

    /**
     * Test removeAtttribute with a namespace uri.
     */
    @Test
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
    @Test
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
    @Test
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
    @Test
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
    @Test
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
    @Test
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
    @Test
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
    @Test
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
    @Test
    public void test_TCM__List_getAdditionalNamespaces() {
        Element element = new Element("element");
        element.addNamespaceDeclaration(Namespace.getNamespace("x", "urn:foo"));
        element.addNamespaceDeclaration(Namespace.getNamespace("y", "urn:bar"));
        element.addNamespaceDeclaration(Namespace.getNamespace("z", "urn:baz"));

        List<Namespace> list = element.getAdditionalNamespaces();

        Namespace ns = list.get(0);
        assertTrue("didn't return added namespace", ns.getURI().equals("urn:foo"));
        ns = list.get(1);
        assertTrue("didn't return added namespace", ns.getURI().equals("urn:bar"));
        ns = list.get(2);
        assertTrue("didn't return added namespace", ns.getURI().equals("urn:baz"));
    }

    /**
     * Test that getAttribute returns all attributes of this element.
     */
    @Test
    public void test_TCM__List_getAttributes() {
        Element element = new Element("test");
        Attribute att = new Attribute("anAttribute", "test");
        Attribute att2 = new Attribute("anotherAttribute", "test");
        Attribute att3 = new Attribute("anotherAttribute", "test", Namespace.getNamespace("x", "urn:JDOM"));
        element.setAttribute(att);
        element.setAttribute(att2);
        element.setAttribute(att3);

        List<Attribute> list = element.getAttributes();

        assertEquals("incorrect size returned", list.size(), 3);
        assertEquals("incorrect attribute returned", list.get(0), att);
        assertEquals("incorrect attribute returned", list.get(1), att2);
    }

    /**
     * Test getChildren to return all children from all namespaces.
     */
    @Test
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
        List<Element> list = element.getChildren();
        assertEquals("incorrect number of children returned", list.size(), 3);
        assertEquals("incorrect child returned", list.get(0), child1);
        assertEquals("incorrect child returned", list.get(1), child2);
        assertEquals("incorrect child returned", list.get(2), child3);
    }

    /**
     * Test that Element returns a List of children by name
     */
    @Test
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
        List<Element> list = element.getChildren("child");
        assertEquals("incorrect number of children returned", list.size(), 2);
        assertEquals("incorrect child returned", list.get(0).getAttribute("name").getValue(), "first");
        assertEquals("incorrect child returned", list.get(1).getAttribute("name").getValue(), "second");
    }

    /**
     * Test that Element returns a List of children by name and namespace
     */
    @Test
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
        List<Element> list = element.getChildren("child", ns);
        assertEquals("incorrect number of children returned", list.size(), 2);
        assertEquals("incorrect child returned", list.get(0).getAttribute("name").getValue(), "first");
        assertEquals("incorrect child returned", list.get(1).getAttribute("name").getValue(), "second");
    }

    /**
     * Test that getContent returns all the content for the element
     */
    @Test
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

        List<Content> list = element.getContent();

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
    @Test
    public void test_TCM__Object_clone() {

        //first the simple case
        Element element = new Element("simple");
        Element clone = element.clone();

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

        clone = element.clone();
        element = null;
        child3 = null;
        child2 = null;
        child1 = null;

        // additional namespaces
        List<Namespace> additional = clone.getAdditionalNamespaces();
        assertEquals("incorrect deep clone additional namespace",
                     additional.get(0),
                     Namespace.getNamespace("foo", "http://test1"));
        assertEquals("incorrect deep clone additional namespace",
                     additional.get(1),
                     Namespace.getNamespace("bar", "http://test2"));

        List<Content> list = clone.getContent();

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
    @Test
    public void test_TCM__OrgJdomAttribute_getAttribute_String() {
        Element element = new Element("el");
        Attribute att = new Attribute("name", "first");
        element.setAttribute(att);
        assertEquals("incorrect Attribute returned", element.getAttribute("name"), att);
    }

    /**
     * Test getAttribute by name and namespace
     */
    @Test
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
    @Test
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
    @Test
    public void test_TCM__OrgJdomElement_addContent_OrgJdomCDATA() {
        //positive test is covered in test__List_getContent()

        //test with null
        Element element = new Element("element");
        CDATA cdata = null;
        try {
            element.addContent(cdata);
            fail("didn't catch null CDATA element");
        }
        catch (NullPointerException e) {
        	// this is what List interface expects
        }
        catch (Exception e) {
            fail("Expect NPE, not Exception " + e.getClass().getName() 
            		+ ": " + e.getMessage());
        }
    }

    /**
     * Test adding comment content.
     */
    @Test
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
        catch (NullPointerException e) {
			// OK
		} catch (Exception e) {
			fail("Unexpected exception " + e.getClass());
        }
    }

    /**
     * Test addContent for Element.
     */
    @Test
    public void test_TCM__OrgJdomElement_addContent_OrgJdomElement() {
        //positive test is covered in test__List_getContent()

        //test with null
        Element element = new Element("element");
        try {
            Element el = null;
            element.addContent(el);
            fail("didn't catch null Element");
        }
        catch (NullPointerException e) {
			// OK
		} catch (Exception e) {
			fail("Unexpected exception " + e.getClass());
        }
    }

    /**
     * Test addContent for Entity
     */
    @Test
    public void test_TCM__OrgJdomElement_addContent_OrgJdomEntityRef() {
        //positive test is covered in test__List_getContent()

        //try with null
        Element element = new Element("element");
        try {
            EntityRef entity = null;
            element.addContent(entity);
            fail("didn't catch null EntityRef");
        }
        catch (NullPointerException e) {
			// Do nothing
		} catch (Exception e) {
			fail("Unexpected exception " + e.getClass());
        }
    }

    /**
     * Test addContent for ProcessingInstruction.
     */
    @Test
    public void test_TCM__OrgJdomElement_addContent_OrgJdomProcessingInstruction() {
        //positive test is covered in test__List_getContent()

        //try with null data
        Element element = new Element("element");
        try {
            ProcessingInstruction pi = null;
            element.addContent(pi);
            fail("didn't catch null ProcessingInstruction");
        }
        catch (NullPointerException e) {
			// Do nothing
		} catch (Exception e) {
			fail("Unexpected exception " + e.getClass());
        }
    }

    /**
     * Test that string based content is added correctly to an Element.
     */
    @Test
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

        // RolfL: I'v tried not to mess with the 'original' JUnit tests, but the
        // following comment does not match the code, or the tests.
        // vvvvvvvvvv
        //test that add content null clears the content
        // ^^^^^^^^^^
        // Adding a null String appears to have always had the effect of adding
        // a new 'empty' Text content...: new Text("")
        // I am updating this test to match the current reality.
        try {
        	// we expect addContent(null) to add a new Empty Text element.
        	// thus 'last' will be accurate after the add.
            int last = element.getContentSize();
            
            String data = null;
            element.addContent(data);
            
            // ensure there is a new content at 'last', and that it is an empty string.
            assertEquals("", element.getContent().get(last).getValue());
        }
        catch (IllegalAddException e) {
        	fail("didn't handle null String content");
        }
        catch (NullPointerException e) {
            fail("didn't handle null String content");
        }

    }

    /**
     * Test getContent by child name.
     */
    @Test
    public void test_TCM__OrgJdomElement_getChild_String() {
        Element element = new Element("element");
        Element child = new Element("child");
        Element childNS = new Element("child", Namespace.getNamespace("urn:foo"));
        element.addContent(child);
        element.addContent(childNS);
        assertEquals("incorrect child returned", element.getChild("child"), child);
    }

    /**
     * Test getContent by child name and namespace.
     */
    @Test
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
    @Test
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
    @Test
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
    @Test
    public void test_TCM__OrgJdomElement_getParent() {
        Element element = new Element("el");
        Element child = new Element("child");
        element.addContent(child);

        assertEquals("parent not found", element, child.getParent());
    }

    /**
     * Test addAttribute with an Attribute
     */
    @Test
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
        catch (NullPointerException e) {
			// Do nothing
		} catch (Exception e) {
			fail("Unexpected exception " + e.getClass());
        }
        
        try {
        	Element emt = new Element("dummy");
        	Attribute ma = new Attribute("hi", "there");
        	emt.setAttribute(ma);
        	element.setAttribute(ma);
        	fail("Should not be able to add already added Attribute.");
        } catch (IllegalAddException iae) {
        	// good
        } catch (Exception e) {
        	fail("Expect IllegalAddException, not " + e.getClass().getName());
        }
    }

    /**
     * Test addAttribute with a supplied name and value
     */
    @Test
    public void test_TCM__OrgJdomElement_setAttribute_String_String__invalidCharacters() {
        final Element element = new Element("el");

		//try with null name
        try { 
            element.setAttribute(null, "value");
            fail("didn't catch null attribute name");
        }
        catch (NullPointerException ignore) {
			// Do nothing
		} catch (Exception e) {
			fail("Unexpected exception " + e.getClass());
        }

		//try with null value
        try {
            element.setAttribute("name2", null);
            fail("didn't catch null attribute value");
        }
        catch (NullPointerException e) {
			// Do nothing
		} catch (Exception e) {
			fail("Unexpected exception " + e.getClass());
        }

        //try with bad characters
        try {
            element.setAttribute("\n", "value");
            fail("didn't catch bad characters in attribute name");
        }
        catch (IllegalArgumentException e) {
			// Do nothing
		} catch (Exception e) {
			fail("Unexpected exception " + e.getClass());
        }

        try {
            element.setAttribute("name2", "" + (char) 0x01);
            fail("didn't catch bad characters  in attribute value");
        }
        catch (IllegalArgumentException e) {
			// Do nothing
		} catch (Exception e) {
			fail("Unexpected exception " + e.getClass());
        }
    }

    /**
     * Test addAttribute with a supplied name and value
     */
    @Test
    public void test_setAttribute_String_String__attributeTypes() {
    	helper_setAttribute_String_String__attributeType(Attribute.UNDECLARED_TYPE);    
    	helper_setAttribute_String_String__attributeType(Attribute.CDATA_TYPE);    
    	helper_setAttribute_String_String__attributeType(Attribute.ID_TYPE);    
    	helper_setAttribute_String_String__attributeType(Attribute.IDREF_TYPE);    
    	helper_setAttribute_String_String__attributeType(Attribute.IDREFS_TYPE);    
    	helper_setAttribute_String_String__attributeType(Attribute.ENTITY_TYPE);    
    	helper_setAttribute_String_String__attributeType(Attribute.ENTITIES_TYPE);    
    	helper_setAttribute_String_String__attributeType(Attribute.NMTOKEN_TYPE);    
    	helper_setAttribute_String_String__attributeType(Attribute.NMTOKENS_TYPE);    
    	helper_setAttribute_String_String__attributeType(Attribute.NOTATION_TYPE);    
    	helper_setAttribute_String_String__attributeType(Attribute.ENUMERATED_TYPE);    
    }

    /**
     * Test setAttribute with a supplied name and value
     */
    private void helper_setAttribute_String_String__attributeType(final AttributeType attributeType) {
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
    @Test
    public void test_setAttribute_String_String_String__attributeTypes() {
        helper_setAttribute_String_String_String__attributeType(Attribute.UNDECLARED_TYPE);    
        helper_setAttribute_String_String_String__attributeType(Attribute.CDATA_TYPE);    
        helper_setAttribute_String_String_String__attributeType(Attribute.ID_TYPE);    
        helper_setAttribute_String_String_String__attributeType(Attribute.IDREF_TYPE);    
        helper_setAttribute_String_String_String__attributeType(Attribute.IDREFS_TYPE);    
        helper_setAttribute_String_String_String__attributeType(Attribute.ENTITY_TYPE);    
        helper_setAttribute_String_String_String__attributeType(Attribute.ENTITIES_TYPE);    
        helper_setAttribute_String_String_String__attributeType(Attribute.NMTOKEN_TYPE);    
        helper_setAttribute_String_String_String__attributeType(Attribute.NMTOKENS_TYPE);    
        helper_setAttribute_String_String_String__attributeType(Attribute.NOTATION_TYPE);    
        helper_setAttribute_String_String_String__attributeType(Attribute.ENUMERATED_TYPE);    
    }

    /**
     * Test setAttribute with a supplied name and value
     * 
     * @author Victor Toni
     */
    private void helper_setAttribute_String_String_String__attributeType(final AttributeType attributeType) {
        try {
            final Namespace defaultNamespace = Namespace.getNamespace(null, "http://test.org/default");
            helper_setAttribute_String_String_String__attributeType(defaultNamespace, attributeType);
            fail("didn't catch empty prefix for attribute ");
        }
        catch( final IllegalNameException ignore) {
			// Do nothing
		} catch (Exception e) {
			fail("Unexpected exception " + e.getClass());
        }

        final Namespace testNamespace = Namespace.getNamespace("test", "http://test.org/test");
        helper_setAttribute_String_String_String__attributeType(testNamespace, attributeType);
    }

    /**
     * Test setAttribute with a supplied name, namespace and value
     * 
     * @author Victor Toni
     */
    private void helper_setAttribute_String_String_String__attributeType(final Namespace namespace, final AttributeType attributeType) {
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
    @Test
    public void test_TCM__OrgJdomElement_setAttributes_List() {
        Element element = new Element("element");
        Attribute one = new Attribute("one", "value");
        Attribute two = new Attribute("two", "value");
        Attribute three = new Attribute("three", "value");
        ArrayList<Attribute> list = new ArrayList<Attribute>();
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
//        Element bogus = new Element("bogus");
//        Attribute four = new Attribute("four", "value");
//
//        ArrayList<Object> newList = new ArrayList<Object>();
//        newList.add(four);
//        newList.add(bogus);
//        try {
//            element.setAttributes(newList);
//            fail("didn't catch bad data in list");
//        }
//        catch (ClassCastException e) {
//        }
        //should be an atomic operation so the original state should be preserved
        assertEquals("wrong number of attributes after failed add", 3, element.getAttributes().size());
        assertEquals("attribute not found", one, element.getAttribute("one"));
        assertEquals("attribute not found", two, element.getAttribute("two"));
        assertEquals("attribute not found", three, element.getAttribute("three"));

        try {
            element.setAttributes(null);
            List<?> attList = element.getAttributes();
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
    @Test
    public void test_TCM__OrgJdomElement_setContent_List() {
        Element element = new Element("el");
        assertEquals("did not return Collections.EMPTY_LIST on empty element", Collections.EMPTY_LIST, element.getContent());
        Namespace ns = Namespace.getNamespace("urn:hogwarts");
        Element child1 = new Element("child", ns);

        Element child2 = new Element("child", ns);
        Element child3 = new Element("child", Namespace.getNamespace("ftp://wombat.stew"));

        LinkedList<Content> list = new LinkedList<Content>();
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
        List<Content> contentList = element.getContent();

        assertEquals("incorrect number of content items", 8, contentList.size());
        assertEquals("wrong child element", child1, contentList.get(0));
        assertEquals("wrong child element", child2, contentList.get(1));
        assertEquals("wrong child element", child3, contentList.get(2));
        assertEquals("wrong comment", comment, contentList.get(3));
        assertEquals("wrong CDATA", cdata, contentList.get(4));
        assertEquals("wrong ProcessingInstruction", pi, contentList.get(5));
        assertEquals("wrong EntityRef", entity, contentList.get(6));
        assertEquals("wrong text", text, contentList.get(7));

        ArrayList<Content> newList = new ArrayList<Content>();
        //test adding a bad object type in the list
        newList.add(child1);
        addBrokenContent(newList, new Integer(7));

        try {
        	// this is a sensitive test.
        	// The first member in the newList is already in the element, so,
        	// normally adding it would cause anissue with the child already
        	// having a parent, and thus an IllegalAddException, so, we could
        	// expect that, but, setContent first detaches the previous content
        	// so, in this case, the child element first gets detached, and then
        	// re-added... so, the effect is that we get a ClassCastException
        	// by adding the Integer...
            element.setContent(newList);
            failNoException(ClassCastException.class);
		} catch (Exception e) {
			checkException(ClassCastException.class, e);
        }

        //should add no content... if setContent fails it should restore previous
        // content.
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
        assertNull(child1.getParent());
        assertNull(child2.getParent());
        assertNull(child3.getParent());
        assertNull(comment.getParent());
        assertNull(cdata.getParent());
        assertNull(pi.getParent());
        assertNull(entity.getParent());
        assertNull(text.getParent());
        
        // test an empty list clears....
        newList.clear();
        assertTrue(element == element.setContent(newList));
        assertTrue(element.getContent().isEmpty());
        assertTrue(element == element.setContent(list));
        assertTrue(element.getContentSize() == list.size());
        // test a null list clears.
        newList = null;
        assertTrue(element == element.setContent(newList));
        assertTrue(element.getContent().isEmpty());
        assertTrue(element == element.setContent(list));
        assertTrue(element.getContentSize() == list.size());
        
        // get a 1-sized list.
        for (int i = list.size() - 1; i >= 1; i--) {
        	list.remove(i);
        }
        assertTrue(element == element.setContent(list));
        assertTrue(element.getContentSize() == 1);
    }

    /**
     * Test setting the content of the Element with just a string
     * This should wipe out all other content the element has
     */
    @Test
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
    @Test
    public void test_TCM__OrgJdomNamespace_getNamespace() {
        Namespace ns = Namespace.getNamespace("urn:test:foo");
        Element element = new Element("element", ns);

        assertEquals("wrong namespace returned", ns, element.getNamespace());
    }

    /**
     * Test that Element can return a namespace given a uri
     */
    @Test
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
        
        //Now make sure we can pick up namespaces on attributes too that are not
        //explicitly added with addAdditionalNamespace()
        assertNull(child.getNamespace("attns"));
        child.setAttribute("att", "value", Namespace.getNamespace("attns", "atturi"));
        assertEquals("atturi", child.getNamespace("attns").getURI());
        child.setAttribute("att", "value", Namespace.getNamespace("attnt", "atturt"));
        assertEquals("atturt", child.getNamespace("attnt").getURI());

        assertNull(element.getNamespace("attns"));
}

    /**
     * Test getAttributeValue by attribute name.
     */
    @Test
    public void test_TCM__String_getAttributeValue_String() {
        Element element = new Element("el");
        assertEquals("incorrect value returned", element.getAttributeValue("name"), null);
        element.setAttribute(new Attribute("name", "first"));
        assertEquals("incorrect value returned", element.getAttributeValue("name"), "first");
    }

    /**
     * Test getAttributeValue by attribute name.
     */
    @Test
    public void test_TCM__String_getAttributeValue_String_String() {
        Element element = new Element("el");
        assertEquals("incorrect value returned", element.getAttributeValue("name", ""), "");
        element.setAttribute(new Attribute("name", "first"));
        assertEquals("incorrect value returned", element.getAttributeValue("name", ""), "first");
        assertEquals("incorrect value returned", element.getAttributeValue("namex", ""), "");
    }

    /**
     * Test getAttributeValue with name and namespace
     */
    @Test
    public void test_TCM__String_getAttributeValue_String_OrgJdomNamespace() {
        Element element = new Element("el");
        assertEquals("incorrect value returned", element.getAttributeValue("name", Namespace.getNamespace("x", "urn:WombatsRUS")), null);
        element.setAttribute(new Attribute("name", "first", Namespace.getNamespace("x", "urn:WombatsRUS")));
        assertEquals("incorrect value returned", element.getAttributeValue("name", Namespace.getNamespace("x", "urn:WombatsRUS")), "first");
        assertEquals("incorrect value returned", element.getAttributeValue("name", Namespace.getNamespace("y", "urn:WombatsRUS2")), null);
    }

    /**
     * Test getAttributeValue with name and namespace
     */
    @Test
    public void test_TCM__String_getAttributeValue_String_OrgJdomNamespace_String() {
        Element element = new Element("el");
        assertEquals("incorrect value returned", element.getAttributeValue("name", Namespace.getNamespace("x", "urn:WombatsRUS"), ""), "");
        element.setAttribute(new Attribute("name", "first", Namespace.getNamespace("x", "urn:WombatsRUS")));
        assertEquals("incorrect value returned", element.getAttributeValue("name", Namespace.getNamespace("x", "urn:WombatsRUS"), ""), "first");
        assertEquals("incorrect value returned", element.getAttributeValue("name", Namespace.getNamespace("y", "urn:WombatsRUS2"), "x"), "x");
    }

    /**
     * Test the convience method for retrieving child text.
     */
    @Test
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
    @Test
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
    @Test
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
    @Test
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
    @Test
    public void test_TCM__String_getName() {
        Element element = new Element("element", Namespace.getNamespace("x", "ftp://wombat.stew"));
        assertEquals("incorrect name", element.getName(), "element");
    }

    /**
     * Test getNamespacePrefix.
     */
    @Test
    public void test_TCM__String_getNamespacePrefix() {
        Element element = new Element("element", Namespace.getNamespace("x", "ftp://wombat.stew"));
        assertEquals("incorrect namespace prefix", element.getNamespacePrefix(), "x");
    }

    /**
     * Test code goes here. Replace this comment.
     */
    @Test
    public void test_TCM__String_getNamespaceURI() {
        Element element = new Element("element", Namespace.getNamespace("x", "ftp://wombat.stew"));
        assertEquals("incorrect uri", element.getNamespaceURI(), "ftp://wombat.stew");
    }

    /**
     * Test that Element returns the correct qualified name.
     */
    @Test
    public void test_TCM__String_getQualifiedName() {
        Element element = new Element("element", Namespace.getNamespace("x", "ftp://wombat.stew"));
        assertEquals("incorrect qualified name", element.getQualifiedName(), "x:element");
    }

    /**
     * Test getSerializedForm
     */
    @Test
    public void test_TCM__String_getSerializedForm() {
//        fail("method not implemented");
    }

    /**
     * Test getText returns that full text of the element
     */
    @Test
    public void test_TCM__String_getText() {
        Element element = new Element("element");
        element.addContent("  some text \nboo  ");

        assertEquals("incorrect text returned", "  some text \nboo  ", element.getText());
    }

    /**
     * Test getTextTrim.
     */
    @Test
    public void test_TCM__String_getTextTrim() {
        Element element = new Element("element");
        element.addContent("  some text  \n ");

        assertEquals("incorrect text returned", "some text", element.getTextTrim());
    }

    /**
     * Test the toString method which should return a useful string
     * for debugging purposes only
     */
    @Test
    public void test_TCM__String_toString() {
        Element element = new Element("element", Namespace.getNamespace("urn:foo"));
        element.setAttribute(new Attribute("name", "aName"));

        assertEquals("wrong toString text found", "[Element: <element [Namespace: urn:foo]/>]", element.toString());
    }

    /**
     * Test addNamespaceDeclaration for prefix and uri.
     */
    @Test
    public void test_TCM__void_addNamespaceDeclaration_OrgJdomNamespace() {
        Element element = new Element("element");
        element.addNamespaceDeclaration(Namespace.getNamespace("x", "urn:foo"));
        List<Namespace> list = element.getAdditionalNamespaces();

        Namespace ns = list.get(0);
        assertTrue("didn't return added namespace", ns.getURI().equals("urn:foo"));
        assertTrue("didn't return added namespace prefix", ns.getPrefix().equals("x"));
    }

    /**
     * Test that attributes will be added and retrieved according
     * to specs. with namespaces and prefixes intact
     *
     */
    @Test
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
			// Do nothing
		} catch (Exception e) {
			fail("Unexpected exception " + e.getClass());
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
        catch (IllegalNameException e) {
			// Do nothing
		} catch (Exception e) {
			fail("Unexpected exception " + e.getClass());
        }

        //show how you can have an empty namespace with the current scheme
        testNS3 = Namespace.getNamespace("");

        el = new Element("test", testDefault);
        el.addNamespaceDeclaration(testNS2);
        try {

            el.addNamespaceDeclaration(testNS3);
        }
        catch (IllegalAddException e) {
			// Do nothing
		} catch (Exception e) {
			fail("Unexpected exception " + e.getClass());
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
    @Test
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
        assertEquals("Incorrect output for NO_NAMESPACE in a default namespace", bufWithNoNS, sw.toString());

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
			// Do nothing
		} catch (Exception e) {
			fail("Unexpected exception " + e.getClass());
        }
    }

    /**
     * Test that Element serialization works, including with namespaces
     * @throws IOException 
     *
     */
    @Test
    public void test_TCU__testSerialization() throws IOException {

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

        Element elIn = UnitTestUtil.deSerialize(element);

        StringWriter sw = new StringWriter();
        XMLOutputter op = new XMLOutputter();
        op.output(elIn, sw);
        assertEquals("Incorrect data after serialization", sw.toString(), bufWithEmptyNS);

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
                "<element xmlns=\"http://foo\" xmlns:bar=\"http://test2\" xmlns:foo=\"http://test1\"><child1 xmlns=\"\" anAttribute=\"no namespace\" /><child2 xmlns=\"\" xmlns:x=\"http://foo\" x:anAttribute=\"with namespace\"><descendent /></child2></element>";

        Element elIn2 = UnitTestUtil.deSerialize(element2);

        StringWriter sw2 = new StringWriter();
        XMLOutputter op2 = new XMLOutputter();
        op2.output(elIn2, sw2);
        assertTrue("Incorrect data after serialization", sw2.toString().equals(bufWithEmptyNS2));
    }

//    @Test
//    public void test_AddingString() {
//    	Vector<Object> v = new Vector<Object>();
//    	v.add("one");
//    	Element e = new Element("e");
//    	try {
//    		e.setContent(v);
//    		fail("Should not be avle to add String content");
//    	} catch (ClassCastException cce) {
//    		// good.
//    	} catch (Exception ex) {
//    		fail("Expected ClassCastException, not " + ex.getClass());
//    	}
//    }
    
	@Test
	public void testElementAddDocType() {
		try {
			List<Content> list = (new Element("tag").getContent());
			list.add(new DocType("elementname"));
			fail ("Should not be able to add DocType to an Element");
		} catch (IllegalAddException iae) {
			// good!
		} catch (Exception e) {
			fail ("We expect an IllegalAddException, but got " + e.getClass().getName());
		}
	}
	
//	@Test
//	public void testElementAddAttribute() {
//		try {
//			List<Content> list = (new Element("tag").getContent());
//			list.add(new Attribute("att", "value"));
//			fail ("Should not be able to add Attribute to an Element's ContentList");
//		} catch (IllegalAddException iae) {
//			// good!
//		} catch (Exception e) {
//			fail ("We expect an IllegalAddException, but got " + e.getClass().getName());
//		}
//	}
	
	@Test
	public void testGetNamespace() {
		Element emt = new Element("mine");
		Namespace nsa = Namespace.getNamespace("tstada", "uri");
		emt.setAttribute("name", "value", nsa);
		assertTrue(Namespace.NO_NAMESPACE == emt.getNamespace());
		assertTrue(emt.getNamespace(null) == null);
		assertTrue(emt.getNamespace("none") == null);
		assertTrue(emt.getNamespace("xml") == Namespace.XML_NAMESPACE);
		assertTrue(emt.getNamespace("tstada") == nsa);
	}
	
	@Test
	public void testSetNamespaceAdditional() {
		Namespace nsa = Namespace.getNamespace("pfx","URIA");
		Namespace nsb = Namespace.getNamespace("pfx","URIB");
		Namespace nsc = Namespace.getNamespace("pfx","URIC");

		Element emt = new Element("emt", nsa);
		try {
			// cannot add a second namespace with the same prefix as the Element.
			emt.addNamespaceDeclaration(nsb);
			fail("Expected Namespace Collision Exception.");
		} catch (IllegalAddException iae) {
			// good
		} catch (Exception e) {
			e.printStackTrace();
			fail("Expected IllegalAddException, but got " + e.getClass());
		}

		// and we can change the Element's Namespace to nsc
		emt.setNamespace(nsc);
		// and back again.
		emt.setNamespace(nsa);
		
		// further we can add nsa as an additional namespace because it is the
		// same as the Element's namespace.
		assertTrue(emt.addNamespaceDeclaration(nsa));
		
		try {
			// but, now we can't change the Element's Namespace.
			emt.setNamespace(nsb);
			fail("Expected Namespace Collision Exception.");
		} catch (IllegalAddException iae) {
			// good
		} catch (Exception e) {
			e.printStackTrace();
			fail("Expected IllegalAddException, but got " + e.getClass());
		}

		// false because it's already added.
		assertFalse(emt.addNamespaceDeclaration(nsa));
	}
	
	@Test
	public void testSetNamespaceAttribute() {
		Namespace nsa = Namespace.getNamespace("pfx","URIA");
		Namespace nsb = Namespace.getNamespace("pfx","URIB");
		Namespace nsc = Namespace.getNamespace("pfx","URIC");
		Namespace nsd = Namespace.getNamespace("pfy","URID");

		Element emt = new Element("emt", nsa);
		try {
			// cannot add a second namespace with the same prefix as the Element.
			emt.setAttribute("att", "val", nsb);
			fail("Expected Namespace Collision Exception.");
		} catch (IllegalAddException iae) {
			// good
		} catch (Exception e) {
			e.printStackTrace();
			fail("Expected IllegalAddException, but got " + e.getClass());
		}

		// and we can change the Element's Namespace to nsc
		emt.setNamespace(nsc);
		assertTrue(nsc == emt.getNamespace());
		// and back again.
		emt.setNamespace(nsa);
		assertTrue(nsa == emt.getNamespace());
		
		// further we can add nsa as an additional namespace because it is the
		// same as the Element's namespace.
		assertTrue(emt == emt.setAttribute("att", "value", nsa));
		
		try {
			// but, now we can't change the Element's Namespace.
			emt.setNamespace(nsb);
			fail("Expected Namespace Collision Exception.");
		} catch (IllegalAddException iae) {
			// good
		} catch (Exception e) {
			e.printStackTrace();
			fail("Expected IllegalAddException, but got " + e.getClass());
		}
		
		// but we can change it to something with a different prefix:
		emt.setNamespace(nsd);
		assertTrue(nsd == emt.getNamespace());
		
		assertTrue(emt.setAttribute("tst", "val1", null) == emt);
		assertEquals("val1", emt.getAttributeValue("tst"));
		assertEquals("val1", emt.getAttributeValue("tst", (Namespace)null));
		assertEquals("val1", emt.getAttributeValue("tst", Namespace.NO_NAMESPACE));
		assertTrue(emt.setAttribute("tst", "val2", Namespace.NO_NAMESPACE) == emt);
		assertEquals("val2", emt.getAttributeValue("tst"));
		assertEquals("val2", emt.getAttributeValue("tst", (Namespace)null));
		assertEquals("val2", emt.getAttributeValue("tst", Namespace.NO_NAMESPACE));
		

	}
	
	@Test
	public void testRemoveNamespace() {
		Element emt = new Element("mine");
		Namespace myuri = Namespace.getNamespace("pfx", "myuri");
		assertTrue(emt.getNamespace("pfx") == null);
		emt.removeNamespaceDeclaration(myuri);
		emt.addNamespaceDeclaration(myuri);
		assertTrue(emt.getNamespace("pfx") == myuri);
		emt.removeNamespaceDeclaration(myuri);
		assertTrue(emt.getNamespace("pfx") == null);
	}

	@Test
	public void testGetValue() {
		Element emt = new Element("root");
		emt.addContent("See ");
		assertTrue("See ".equals(emt.getValue()));
		emt.addContent(new Element("k1").addContent("Spot "));
		assertTrue("See Spot ".equals(emt.getValue()));
		emt.addContent(new Comment("skip "));
		assertTrue("See Spot ".equals(emt.getValue()));
		emt.addContent(new CDATA("run!"));
		assertTrue("See Spot run!".equals(emt.getValue()));
	}

	@Test
	public void testGetText() {
		Element emt = new Element("root");
		assertTrue("".equals(emt.getText()));
		emt.addContent(new Comment("skip "));
		assertTrue("".equals(emt.getText()));
		emt.addContent(new ProcessingInstruction("dummy", "nodata"));
		assertTrue("".equals(emt.getText()));
		emt.removeContent();
		assertTrue("".equals(emt.getText()));
		emt.addContent(" See   ");
		assertTrue(" See   ".equals(emt.getText()));
		emt.addContent(new Element("k1").addContent(" Spot  the \n dog  "));
		assertTrue(" See   ".equals(emt.getText()));
		emt.addContent(new CDATA(" run!   "));
		assertTrue(" See    run!   ".equals(emt.getText()));
		assertTrue("See    run!".equals(emt.getTextTrim()));
		assertTrue("See run!".equals(emt.getTextNormalize()));
		
		assertTrue(" Spot  the \n dog  ".equals(emt.getChildText("k1")));
		assertTrue("Spot  the \n dog".equals(emt.getChildTextTrim("k1")));
		assertTrue("Spot the dog".equals(emt.getChildTextNormalize("k1")));
		
		assertTrue(" Spot  the \n dog  ".equals(emt.getChildText("k1", Namespace.NO_NAMESPACE)));
		assertTrue("Spot  the \n dog".equals(emt.getChildTextTrim("k1", Namespace.NO_NAMESPACE)));
		assertTrue("Spot the dog".equals(emt.getChildTextNormalize("k1", Namespace.NO_NAMESPACE)));

		assertTrue(null == emt.getChildText("x1"));
		assertTrue(null == emt.getChildTextTrim("x1"));
		assertTrue(null == emt.getChildTextNormalize("x1"));
		
		assertTrue(null == emt.getChildText("x1", Namespace.NO_NAMESPACE));
		assertTrue(null == emt.getChildTextTrim("x1", Namespace.NO_NAMESPACE));
		assertTrue(null == emt.getChildTextNormalize("x1", Namespace.NO_NAMESPACE));

		Namespace xx = Namespace.getNamespace("nouri");
		assertTrue(null == emt.getChildText("k1", xx));
		assertTrue(null == emt.getChildTextTrim("k1", xx));
		assertTrue(null == emt.getChildTextNormalize("k1", xx));
	}

	@Test
	public void testElementContent() {
		Document doc = new Document();
		Element root = new Element("root");
		assertTrue(root.getContentSize() == 0);
		assertFalse(root.isRootElement());
		doc.addContent(root);
		assertTrue(root.isRootElement());
		
		assertTrue(root.cloneContent().size() == 0);
		
		final Text text = new Text("text");
		final Comment comment1 = new Comment("comment1");
		final Comment comment2 = new Comment("comment2");
		final Element child = new Element("child");
		
		root.addContent(text);
		assertTrue(root.getContentSize() == 1);
		assertTrue(root.getText().equals(text.getText()));
		assertTrue(root.cloneContent().size() == 1);
		assertTrue(root.cloneContent().get(0) instanceof Text);
		assertTrue(root.indexOf(text) == 0);
		
		assertTrue(text.getParent() == root);
		assertTrue(root.removeContent().get(0) == text);
		
		assertTrue(text.getParent() == null);
		assertTrue(root.getContentSize() == 0);
		assertTrue(root.cloneContent().size() == 0);
		
		root.addContent(comment1);
		root.addContent(comment2);
		assertTrue(comment1.getParent() == root);
		assertTrue(comment2.getParent() == root);
		assertTrue(text.getParent() == null);
		assertTrue(root.getContentSize() == 2);
		root.setContent(1, text);
		assertTrue(comment1.getParent() == root);
		assertTrue(comment2.getParent() == null);
		assertTrue(text.getParent() == root);
		assertTrue(root.getContentSize() == 2);
		
		root.setContent(comment2);
		assertTrue(comment1.getParent() == null);
		assertTrue(comment2.getParent() == root);
		assertTrue(text.getParent() == null);
		assertTrue(root.getContentSize() == 1);
		assertTrue(comment2 == root.removeContent(0));
		
		root.addContent(Collections.singleton(comment2));
		assertTrue(comment1.getParent() == null);
		assertTrue(comment2.getParent() == root);
		assertTrue(text.getParent() == null);
		assertTrue(root.getContentSize() == 1);
		
		root.addContent(0, Collections.singleton(comment1));
		assertTrue(comment1.getParent() == root);
		assertTrue(comment2.getParent() == root);
		assertTrue(text.getParent() == null);
		assertTrue(root.getContentSize() == 2);
		
		root.addContent(2, Collections.singleton(text));
		assertTrue(comment1.getParent() == root);
		assertTrue(comment2.getParent() == root);
		assertTrue(text.getParent() == root);
		assertTrue(root.getContentSize() == 3);
		assertTrue(root.indexOf(comment1) == 0);
		assertTrue(root.indexOf(comment2) == 1);
		assertTrue(root.indexOf(text) == 2);
		
		root.setContent(2, Collections.singleton(text));
		assertTrue(comment1.getParent() == root);
		assertTrue(comment2.getParent() == root);
		assertTrue(text.getParent() == root);
		assertTrue(root.getContentSize() == 3);
		assertTrue(root.indexOf(comment1) == 0);
		assertTrue(root.indexOf(comment2) == 1);
		assertTrue(root.indexOf(text) == 2);
		
		Set<Content> empty = Collections.emptySet();
		root.setContent(2, empty);
		assertTrue(comment1.getParent() == root);
		assertTrue(comment2.getParent() == root);
		assertTrue(text.getParent() == null);
		assertTrue(root.getContentSize() == 2);
		assertTrue(root.indexOf(comment1) == 0);
		assertTrue(root.indexOf(comment2) == 1);
		assertTrue(root.indexOf(text) == -1);
		
		root.addContent(2, text);
		assertTrue(comment1.getParent() == root);
		assertTrue(comment2.getParent() == root);
		assertTrue(text.getParent() == root);
		assertTrue(root.getContentSize() == 3);
		assertTrue(root.indexOf(comment1) == 0);
		assertTrue(root.indexOf(comment2) == 1);
		assertTrue(root.indexOf(text) == 2);
		
		root.addContent(child);
		assertTrue(root.indexOf(child) == 3);
		assertTrue(root.getContentSize() == 4);
		assertTrue(child.getParent() == root);
		assertTrue(child.getParentElement() == root);
		assertTrue(root.getContent().size() == 4);
		assertTrue(root.getContent(new ElementFilter()).size() == 1);
		assertTrue(root.getContent(new ContentFilter(ContentFilter.COMMENT)).size() == 2);
		assertFalse(root.removeChild("child", Namespace.XML_NAMESPACE));
		assertTrue(root.removeChild("child", Namespace.NO_NAMESPACE));

		assertTrue(root.getContentSize() == 3);
		assertTrue(root.getContent(new ElementFilter()).isEmpty());
		
		assertTrue(root.removeContent(new ContentFilter(ContentFilter.COMMENT)).size() == 2);
		
		assertTrue(root.getContentSize() == 1);
		assertTrue(root == root.addContent(child));
		assertTrue(root.getContentSize() == 2);
		assertFalse(root.removeChildren("nothing"));
		assertFalse(root.removeChildren("child", Namespace.getNamespace("nothing")));
		assertTrue(root.removeChildren("child"));
		assertTrue(root.getContentSize() == 1);
		
		// some negative cases not yet covered....
		try {
			Element n = null;
			root.addContent(0, n);
			fail("Should not be able to add null Element content.");
		} catch (NullPointerException npe) {
			// good
		} catch (Exception e) {
			fail("Expected NullPointerException, but got " + e.getClass().getName());
		}
		
		try {
			// try to add ourself.
			child.addContent(0, child);
			fail("Should not be able to add ourself as Element content.");
		} catch (IllegalAddException npe) {
			// good
		} catch (Exception e) {
			fail("Expected NullPointerException, but got " + e.getClass().getName());
		}
		
		root.detach();
		root.addContent(child);
		
		try {
			// try to add ourself.
			child.addContent(0, root);
			fail("Should not be able to add circular Element content.");
		} catch (IllegalAddException npe) {
			// good
		} catch (Exception e) {
			fail("Expected NullPointerException, but got " + e.getClass().getName());
		}
	}
	
	@Test
	public void testAttributes() {
		Element emt = new Element("element");
		Namespace myns = Namespace.getNamespace("pfxa", "attns");
		Namespace mynsz = Namespace.getNamespace("pfxz", "attns");
		assertTrue(emt.getAttributes().isEmpty());
		assertTrue(emt.getAttribute("att") == null);
		assertTrue(emt.getAttributeValue("att") == null);
		assertTrue(emt.getAttributeValue("att", Namespace.NO_NAMESPACE) == null);
		assertTrue(emt.getAttributeValue("att", (Namespace)null) == null);
		assertTrue(emt.getAttributeValue("att", myns) == null);
		assertTrue("def".equals(emt.getAttributeValue("att", "def")));
		assertTrue("def".equals(emt.getAttributeValue("att", myns, "def")));
		assertTrue(emt.getAdditionalNamespaces().isEmpty());
		
		emt.setAttribute(new Attribute("att", "val"));
		assertFalse(emt.getAttributes().isEmpty());
		assertTrue(emt.getAttribute("att") != null);
		assertTrue("val".equals(emt.getAttributeValue("att")));
		assertTrue("val".equals(emt.getAttributeValue("att", Namespace.NO_NAMESPACE)));
		assertTrue("val".equals(emt.getAttributeValue("att", (Namespace)null)));
		assertTrue(emt.getAttributeValue("att", myns) == null);
		assertTrue("val".equals(emt.getAttributeValue("att", "def")));
		assertTrue("val".equals(emt.getAttributeValue("att", Namespace.NO_NAMESPACE, "def")));
		assertTrue("val".equals(emt.getAttributeValue("att", null, "def")));
		assertTrue("def".equals(emt.getAttributeValue("att", myns, "def")));
		assertTrue(emt.getAdditionalNamespaces().isEmpty());
		
		emt.setAttribute(new Attribute("att", "nsval", myns));
		assertFalse(emt.getAttributes().isEmpty());
		assertTrue(emt.getAttribute("att") != null);
		assertTrue(emt.getAttribute("att", myns) != null);
		assertTrue(emt.getAttribute("att") != emt.getAttribute("att", myns));
		assertTrue("val".equals(emt.getAttributeValue("att")));
		assertTrue("val".equals(emt.getAttributeValue("att", Namespace.NO_NAMESPACE)));
		assertTrue("val".equals(emt.getAttributeValue("att", (Namespace)null)));
		assertTrue("nsval".equals(emt.getAttributeValue("att", myns)));
		assertTrue("nsval".equals(emt.getAttributeValue("att", mynsz)));
		assertTrue("val".equals(emt.getAttributeValue("att", "def")));
		assertTrue("nsval".equals(emt.getAttributeValue("att", myns, "def")));
		assertTrue("def".equals(emt.getAttributeValue("att", Namespace.XML_NAMESPACE, "def")));

		assertTrue(emt.getAdditionalNamespaces().isEmpty());
		
		Attribute att = emt.getAttribute("att");
		Attribute na = new Attribute("xx", "xval");
		assertFalse(emt.removeAttribute(na));
		assertTrue(emt.removeAttribute(att));
		assertTrue(att.getParent() == null);
		emt.setAttribute(att);
		assertTrue(att.getParent() == emt);
		assertTrue(emt.setAttribute("att", "nval") == emt);
		assertTrue(att.getParent() == emt);
		assertTrue("nval".equals(att.getValue()));
		assertTrue("nval".equals(emt.getAttributeValue("att")));
		assertTrue(emt.setAttribute(new Attribute("att", "zval")) == emt);
		assertTrue(att.getParent() == null);
		assertTrue("nval".equals(att.getValue()));
		assertTrue("zval".equals(emt.getAttributeValue("att")));
		assertTrue(emt.setAttribute(att) == emt);
		assertTrue(att.getParent() == emt);
		

		att = emt.getAttribute("att", myns);
		na = new Attribute("xx", "xval", myns);
		assertFalse(emt.removeAttribute(na));
		assertTrue(emt.removeAttribute(att));
		assertTrue(emt.setAttribute("att", "aval", myns) == emt);
		assertTrue("nsval".equals(att.getValue()));
		assertTrue("aval".equals(emt.getAttributeValue("att", myns)));
		assertTrue(att.getParent() == null);
		emt.setAttribute(att);
		assertTrue(att.getParent() == emt);
		assertTrue(emt.setAttribute("att", "nval", myns) == emt);
		assertTrue(att.getParent() == emt);
		assertTrue("nval".equals(att.getValue()));
		assertTrue("nval".equals(emt.getAttributeValue("att", myns)));
		assertTrue(emt.setAttribute(new Attribute("att", "zval", myns)) == emt);
		assertTrue(att.getParent() == null);
		assertTrue("nval".equals(att.getValue()));
		assertTrue("zval".equals(emt.getAttributeValue("att", myns)));
		assertTrue(emt.setAttribute(att) == emt);
		assertTrue(att.getParent() == emt);
		assertTrue("nval".equals(emt.getAttributeValue("att", myns)));
	
	}
	
	@SuppressWarnings("unchecked")
	private final void addBrokenContent(Collection<?> cn, Object val) {
		// this method is intentionally broken.
		Collection<Object> ocn = (Collection<Object>)cn;
		ocn.add(val);
	}
	
    @Test
	public void testCloneDetatchParentElement() {
		Element parent = new Element("root");
		Element content = new Element("val");
		parent.addContent(content);
		Element clone = content.detach().clone();
		assertEquals(content.getValue(), clone.getValue());
		assertNull(content.getParent());
		assertNull(clone.getParent());
	}

    @Test
    public void testContentCType() {
    	assertTrue(Content.CType.Element == new Element("root").getCType());
    }

	private final Comparator<Text> alphaText = new Comparator<Text>() {
		@Override
		public int compare(Text o1, Text o2) {
			return o1.getText().compareTo(o2.getText());
		}
	};

	private final Comparator<Content> alphaContent = new Comparator<Content>() {
		@Override
		public int compare(Content o1, Content o2) {
			final int ctd = o1.getCType().ordinal() - o2.getCType().ordinal();
			if (ctd != 0) {
				return ctd;
			}
			final CType ct = o1.getCType();
			switch (ct) {
				case Comment:
					return ((Comment)o1).getText().compareTo(((Comment)o2).getText());
				case CDATA:
					return ((CDATA)o1).getText().compareTo(((CDATA)o2).getText());
				case DocType:
					return ((DocType)o1).getElementName()
							.compareTo(((DocType)o2).getElementName());
				case Element:
					return ((Element)o1).getName()
							.compareTo(((Element)o2).getName());
				case ProcessingInstruction:
					return ((ProcessingInstruction)o1).getTarget()
							.compareTo(((ProcessingInstruction)o2).getTarget());
				case EntityRef:
					return ((EntityRef)o1).getName()
							.compareTo(((EntityRef)o2).getName());
				case Text:
					return ((Text)o1).getText().compareTo(((Text)o2).getText());
			}
			return 0;
		}
	};

	@Test
	public void testSort() {
		Element emt = new Element("root");
		emt.addContent(new Text("d"));
		emt.addContent(new Text("c"));
		emt.addContent(new Text("b"));
		emt.addContent(new Text("a"));
		assertEquals("dcba", emt.getText());
		
		emt.sortContent(Filters.text(), alphaText);
		
		assertEquals("abcd", emt.getText());
	}

	@Test
	public void testSortOnlyContent() {
		Element emt = new Element("root");
		emt.addContent(new Text("a"));
		assertEquals("a", emt.getText());
		emt.sortContent(alphaContent);
		assertEquals("a", emt.getText());
	}
	
	@Test
	public void testSortOnlyFiltered() {
		Element emt = new Element("root");
		emt.addContent(new Text("a"));
		assertEquals("a", emt.getText());
		emt.sortContent(Filters.text(), alphaText);
		assertEquals("a", emt.getText());
	}
	
	@Test
	public void testSortAllSameContent() {
		Element emt = new Element("root");
		emt.addContent(new Text("a"));
		emt.addContent(new Text("a"));
		emt.addContent(new Text("a"));
		emt.addContent(new Text("a"));
		assertEquals("aaaa", emt.getText());
		emt.sortContent(alphaContent);
		assertEquals("aaaa", emt.getText());
	}
	
	@Test
	public void testSortAllSameFiltered() {
		Element emt = new Element("root");
		emt.addContent(new Text("a"));
		emt.addContent(new Text("a"));
		emt.addContent(new Text("a"));
		emt.addContent(new Text("a"));
		assertEquals("aaaa", emt.getText());
		emt.sortContent(Filters.text(), alphaText);
		assertEquals("aaaa", emt.getText());
	}
	
	@Test
	public void testSortContent() {
		Element emt = new Element("root");
		CDATA cdata = new CDATA("XXX");
		emt.addContent(new Text("d"));
		emt.addContent(cdata);
		emt.addContent(new Text("c"));
		emt.addContent(new Text("b"));
		emt.addContent(new Text("a"));
		assertEquals("dXXXcba", emt.getText());
		
		emt.sortContent(alphaContent);
		
		assertEquals("abcdXXX", emt.getText());
		assertEquals(cdata, emt.getContent(4));
	}
	
	@Test
	public void testSortElementContent() {
		final Element emt = new Element("root");
		final CDATA cdata = new CDATA("XXX");
		final Element a = new Element("a");
		final Element b = new Element("b");
		final Element c = new Element("c");
		final Element d = new Element("d");
		
		emt.addContent(d);
		emt.addContent(cdata);
		emt.addContent(c);
		emt.addContent(b);
		emt.addContent(a);
		
		assertTrue(emt.getContent(0) == d);
		assertTrue(emt.getContent(1) == cdata);
		assertTrue(emt.getContent(2) == c);
		assertTrue(emt.getContent(3) == b);
		assertTrue(emt.getContent(4) == a);
		
		emt.sortChildren(alphaContent);
		assertTrue(emt.getContent(0) == a);
		assertTrue(emt.getContent(1) == cdata);
		assertTrue(emt.getContent(2) == b);
		assertTrue(emt.getContent(3) == c);
		assertTrue(emt.getContent(4) == d);
		
		emt.sortContent(alphaContent);
		
		assertTrue(emt.getContent(0) == a);
		assertTrue(emt.getContent(1) == b);
		assertTrue(emt.getContent(2) == c);
		assertTrue(emt.getContent(3) == d);
		assertTrue(emt.getContent(4) == cdata);
	}
	
	@Test
	public void testSortEqualsContent() {
		Element emt = new Element("root");
		final CDATA cdata = new CDATA("XXX");
		final Text t1 = new Text("a");
		final Text t2 = new Text("a");
		final Text t3 = new Text("a");
		final Text t4 = new Text("a");
		emt.addContent(t1);
		emt.addContent(cdata);
		emt.addContent(t2);
		emt.addContent(t3);
		emt.addContent(t4);
		assertEquals("aXXXaaa", emt.getText());
		
		emt.sortContent(alphaContent);
		
		assertEquals("aaaaXXX", emt.getText());
		assertEquals(t1, emt.getContent(0));
		assertEquals(t2, emt.getContent(1));
		assertEquals(t3, emt.getContent(2));
		assertEquals(t4, emt.getContent(3));
		assertEquals(cdata, emt.getContent(4));
	}
	
	@Test
	public void testSortInterleavedContent() {
		Element emt = new Element("root");
		emt.addContent(new Text("d"));
		emt.addContent(new CDATA("ZZZ"));
		emt.addContent(new Text("c"));
		emt.addContent(new CDATA("YYY"));
		emt.addContent(new Text("b"));
		emt.addContent(new CDATA("XXX"));
		emt.addContent(new Text("a"));
		assertEquals("dZZZcYYYbXXXa", emt.getText());
		
		// we can use Text comparator for CDATA too.
		emt.sortContent(Filters.cdata(), alphaText);
		assertEquals("dXXXcYYYbZZZa", emt.getText());
		
		// we can use Text comparator for CDATA too.
		emt.sortContent(new ContentFilter(ContentFilter.TEXT), alphaContent);
		assertEquals("aXXXbYYYcZZZd", emt.getText());

		// we can use Text comparator for CDATA too.... and Filters.text() does Text and CDATA
		emt.sortContent(Filters.text(), alphaText);
		assertEquals("XXXYYYZZZabcd", emt.getText());
	}
	
	@Test
	public void testSortInterleavedEqualContent() {
		Element emt = new Element("root");
		final CDATA cd1 = new CDATA("ZZZ");
		final CDATA cd2 = new CDATA("ZZZ");
		final CDATA cd3 = new CDATA("ZZZ");
		emt.addContent(new Text("d"));
		emt.addContent(cd1);
		emt.addContent(new Text("c"));
		emt.addContent(cd2);
		emt.addContent(new Text("b"));
		emt.addContent(cd3);
		emt.addContent(new Text("a"));
		assertEquals("dZZZcZZZbZZZa", emt.getText());
		
		assertTrue(emt.getContent(1) == cd1);
		assertTrue(emt.getContent(3) == cd2);
		assertTrue(emt.getContent(5) == cd3);
		
		// we can use Text comparator for CDATA too.
		// check sort does not reorder comp==0 content
		emt.sortContent(Filters.cdata(), alphaText);
		assertEquals("dZZZcZZZbZZZa", emt.getText());
		assertTrue(emt.getContent(1) == cd1);
		assertTrue(emt.getContent(3) == cd2);
		assertTrue(emt.getContent(5) == cd3);
		
		
		// we can use Text comparator for CDATA too.
		emt.sortContent(new ContentFilter(ContentFilter.TEXT), alphaContent);
		assertEquals("aZZZbZZZcZZZd", emt.getText());
		assertTrue(emt.getContent(1) == cd1);
		assertTrue(emt.getContent(3) == cd2);
		assertTrue(emt.getContent(5) == cd3);

		// we can use Text comparator for CDATA too.... and Filters.text() does Text and CDATA
		emt.sortContent(Filters.text(), alphaText);
		assertEquals("ZZZZZZZZZabcd", emt.getText());
		assertTrue(emt.getContent(0) == cd1);
		assertTrue(emt.getContent(1) == cd2);
		assertTrue(emt.getContent(2) == cd3);
	}

	@Test
	public void testSortAttributes() {
		final Element emt = new Element("root");
		final Attribute att1 = new Attribute("one",   "001", Namespace.getNamespace("z", "uri1"));
		final Attribute att2 = new Attribute("two",   "002", Namespace.getNamespace("y", "uri1"));
		final Attribute att3 = new Attribute("three", "003", Namespace.getNamespace("x", "uri1"));
		final Attribute att4 = new Attribute("four",  "004", Namespace.getNamespace("w", "uri2"));
		final Attribute att5 = new Attribute("five",  "005", Namespace.getNamespace("v", "uri2"));
		emt.setAttribute(att5);
		emt.setAttribute(att4);
		emt.setAttribute(att3);
		emt.setAttribute(att2);
		emt.setAttribute(att1);
		
		checkAttOrder(emt.getAttributes(), att5, att4, att3, att2, att1);
		
		emt.sortAttributes(new Comparator<Attribute>() {
			@Override
			public int compare(Attribute o1, Attribute o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		
		// alphabetic by string name.
		checkAttOrder(emt.getAttributes(), att5, att4, att1, att3, att2);
		
		emt.sortAttributes(new Comparator<Attribute>() {
			@Override
			public int compare(Attribute o1, Attribute o2) {
				return o1.getNamespacePrefix().compareTo(o2.getNamespacePrefix());
			}
		});
		
		// Namespace Prefixes's are reverse order
		checkAttOrder(emt.getAttributes(), att5, att4, att3, att2, att1);
		
		emt.sortAttributes(new Comparator<Attribute>() {
			@Override
			public int compare(Attribute o1, Attribute o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		});
		
		// Values are in order
		checkAttOrder(emt.getAttributes(), att1, att2, att3, att4, att5);
		
		// Namespace URI's have some common items.... and are in same order
		// as a result, we should have no change at all.
		emt.sortAttributes(new Comparator<Attribute>() {
			@Override
			public int compare(Attribute o1, Attribute o2) {
				return o1.getNamespaceURI().compareTo(o2.getNamespaceURI());
			}
		});
		
		// Values are in order
		checkAttOrder(emt.getAttributes(), att1, att2, att3, att4, att5);
		
		// Namespace URI's have some common items.... and are in same order
		// as a result, we should have no change at all.... except, this time
		// we do the inverse of the result... so, this moves 4&5 to the front
		// but relative order is maintained for equal values....
		emt.sortAttributes(new Comparator<Attribute>() {
			@Override
			public int compare(Attribute o1, Attribute o2) {
				return - o1.getNamespaceURI().compareTo(o2.getNamespaceURI());
			}
		});
		
		// Values are in order
		checkAttOrder(emt.getAttributes(), att4, att5, att1, att2, att3);
		
	}
	
	@Test
	public void testSortAttributesNone() {
		Element emt = new Element("root");
		emt.sortAttributes(new Comparator<Attribute>() {
			@Override
			public int compare(Attribute o1, Attribute o2) {
				return - o1.getNamespaceURI().compareTo(o2.getNamespaceURI());
			}
		});
		// this ends up creating the Attribute array inside the Element.
		checkAttOrder(emt.getAttributes());
	}

	private void checkAttOrder(List<Attribute> attributes, Attribute...atts) {
		assertTrue(atts.length == attributes.size());
		for (int i = atts.length - 1; i >= 0; i--) {
			assertTrue(atts[i] == attributes.get(i));
		}
		
	}

	private String getTestString() {
		return "  this  has  space ";
	}
	
	private String getComp() {
		return Format.compact(getTestString());
	}
	
	private String getTrim() {
		return Format.trimBoth(getTestString());
	}
	
	private String getPlain() {
		return getTestString(); 
	}
	
	private Namespace getNamespace() {
		return Namespace.getNamespace("jdomtest");
	}

	public Element getTextHelperRoot() {
		final Namespace ns = getNamespace();
		
		final Element root = new Element("root");
		final Element childa = new Element("child");
		final Element childb = new Element("child", ns);
		final Element childc = new Element("child");
		final Element childd = new Element("child", ns);
		final Element childe = new Element("kid");
		final Element childf = new Element("kid", ns);
		
		childa.setText(getTestString());
		childb.setText(getTestString());
		childc.setText(getTestString());
		childd.setText(getTestString());
		root.addContent(childa);
		root.addContent(childb);
		root.addContent(childc);
		root.addContent(childd);
		root.addContent(childe);
		root.addContent(childf);
		
		return root;
	}

	@Test
	public void testGetChildTextElementString() {
		Element root = getTextHelperRoot();
		assertEquals(getPlain(), root.getChildText("child"));
		assertEquals(null, root.getChildText("dummy"));
		assertEquals("", root.getChildText("kid"));
	}

	@Test
	public void testGetChildTextElementStringNamespace() {
		Element root = getTextHelperRoot();
		Namespace ns = getNamespace();
		assertEquals(getPlain(), root.getChildText("child", ns));
		assertEquals(null, root.getChildText("dummy", ns));
		assertEquals("", root.getChildText("kid", ns));
	}

	@Test
	public void testGetChildTextTrimElementString() {
		Element root = getTextHelperRoot();
		assertEquals(getTrim(), root.getChildTextTrim("child"));
		assertEquals(null, root.getChildTextTrim("dummy"));
		assertEquals("", root.getChildTextTrim("kid"));
	}

	@Test
	public void testGetChildTextTrimElementStringNamespace() {
		Element root = getTextHelperRoot();
		Namespace ns = getNamespace();
		assertEquals(getTrim(), root.getChildTextTrim("child", ns));
		assertEquals(null, root.getChildTextTrim("dummy", ns));
		assertEquals("", root.getChildTextTrim("kid", ns));
	}

	@Test
	public void testGetChildTextNormalizeElementString() {
		Element root = getTextHelperRoot();
		assertEquals(getComp(), root.getChildTextNormalize("child"));
		assertEquals(null, root.getChildTextNormalize("dummy"));
		assertEquals("", root.getChildTextNormalize("kid"));
	}

	@Test
	public void testGetChildTextNormalizeElementStringNamespace() {
		Element root = getTextHelperRoot();
		Namespace ns = getNamespace();
		assertEquals(getComp(), root.getChildTextNormalize("child", ns));
		assertEquals(null, root.getChildTextNormalize("dummy", ns));
		assertEquals("", root.getChildTextNormalize("kid", ns));
	}


	@Test
	public void testCoalesceTextSimple() {
		Element root = new Element("root");
		root.addContent("one");
		root.addContent(" ");
		root.addContent("two");
		root.addContent(" ");
		root.addContent("three");
		assertTrue(5 == root.getContentSize());
		assertEquals("one two three", root.getText());
		
		assertTrue(root.coalesceText(false));
		assertTrue(1 == root.getContentSize());
		assertEquals("one two three", root.getText());
		assertFalse(root.coalesceText(false));
		assertEquals("one two three", root.getText());
	}
	
	@Test
	public void testCoalesceTextCDATA() {
		Element root = new Element("root");
		root.addContent("one");
		root.addContent(" ");
		root.addContent(new CDATA("two"));
		root.addContent(" ");
		root.addContent("three");
		assertTrue(5 == root.getContentSize());
		assertEquals("one two three", root.getText());
		
		assertTrue(root.coalesceText(false));
		assertTrue(3 == root.getContentSize());
		assertEquals("one two three", root.getText());
		assertFalse(root.coalesceText(false));
		assertEquals("one two three", root.getText());
	}
	
	@Test
	public void testCoalesceTextNested() {
		Element root = new Element("root");
		root.addContent("one");
		root.addContent(" ");
		Element kid = new Element("kid");
		root.addContent(kid);
		kid.addContent("two");
		root.addContent(" ");
		root.addContent("three");
		assertTrue(5 == root.getContentSize());
		assertEquals("one  three", root.getText());
		
		assertTrue(root.coalesceText(false));
		assertTrue(3 == root.getContentSize());
		assertEquals("one  three", root.getText());
		assertFalse(root.coalesceText(false));
		assertEquals("one  three", root.getText());
	}
	
	@Test
	public void testCoalesceTextEmpty() {
		Element root = new Element("root");
		root.addContent("");
		root.addContent("one");
		root.addContent("");
		root.addContent(" ");
		root.addContent("");
		
		root.addContent("two");
		
		root.addContent("");
		root.addContent(" ");
		root.addContent("");
		root.addContent("three");
		root.addContent("");
		assertTrue(11 == root.getContentSize());
		assertEquals("one two three", root.getText());
		
		assertTrue(root.coalesceText(false));
		assertTrue(1 == root.getContentSize());
		assertEquals("one two three", root.getText());
		assertFalse(root.coalesceText(false));
		assertEquals("one two three", root.getText());
	}
	
	@Test
	public void testCoalesceTextSingle() {
		Element root = new Element("root");
		root.addContent("");
		assertEquals("", root.getText());
		
		assertTrue(root.coalesceText(false));
		assertTrue(0 == root.getContentSize());
		assertEquals("", root.getText());
		assertFalse(root.coalesceText(false));
		assertEquals("", root.getText());
	}


	@Test
	public void testCoalesceTextSimpleRec() {
		Element root = new Element("root");
		root.addContent("one");
		root.addContent(" ");
		root.addContent("two");
		root.addContent(" ");
		root.addContent("three");
		assertTrue(5 == root.getContentSize());
		assertEquals("one two three", root.getText());
		
		assertTrue(root.coalesceText(true));
		assertTrue(1 == root.getContentSize());
		assertEquals("one two three", root.getText());
		assertFalse(root.coalesceText(true));
		assertEquals("one two three", root.getText());
	}
	
	@Test
	public void testCoalesceTextCDATARec() {
		Element root = new Element("root");
		root.addContent("one");
		root.addContent(" ");
		root.addContent(new CDATA("two"));
		root.addContent(" ");
		root.addContent("three");
		assertTrue(5 == root.getContentSize());
		assertEquals("one two three", root.getText());
		
		assertTrue(root.coalesceText(true));
		assertTrue(3 == root.getContentSize());
		assertEquals("one two three", root.getText());
		assertFalse(root.coalesceText(true));
		assertEquals("one two three", root.getText());
	}
	
	@Test
	public void testCoalesceTextNestedRec() {
		Element root = new Element("root");
		root.addContent("one");
		root.addContent(" ");
		Element kid = new Element("kid");
		root.addContent(kid);
		kid.addContent("two");
		root.addContent(" ");
		root.addContent("three");
		assertTrue(5 == root.getContentSize());
		assertEquals("one  three", root.getText());
		
		assertTrue(root.coalesceText(true));
		assertTrue(3 == root.getContentSize());
		assertEquals("one  three", root.getText());
		assertFalse(root.coalesceText(true));
		assertEquals("one  three", root.getText());
	}
	
	@Test
	public void testCoalesceTextEmptyRec() {
		Element root = new Element("root");
		root.addContent("");
		root.addContent("one");
		root.addContent("");
		root.addContent(" ");
		root.addContent("");
		
		root.addContent("two");
		
		root.addContent("");
		root.addContent(" ");
		root.addContent("");
		root.addContent("three");
		root.addContent("");
		assertTrue(11 == root.getContentSize());
		assertEquals("one two three", root.getText());
		
		assertTrue(root.coalesceText(true));
		assertTrue(1 == root.getContentSize());
		assertEquals("one two three", root.getText());
		assertFalse(root.coalesceText(true));
		assertEquals("one two three", root.getText());
	}
	
	@Test
	public void testCoalesceTextSingleRec() {
		Element root = new Element("root");
		root.addContent("");
		assertEquals("", root.getText());
		
		assertTrue(root.coalesceText(true));
		assertTrue(0 == root.getContentSize());
		assertEquals("", root.getText());
		assertFalse(root.coalesceText(true));
		assertEquals("", root.getText());
	}


	@Test
	public void testXmlBaseNone() throws URISyntaxException {
		Document doc = new Document();
		Element root = new Element("root");
		doc.setRootElement(root);
		assertTrue(null == root.getXMLBaseURI());
	}

	@Test
	public void testXmlBaseDocument() throws URISyntaxException {
		Document doc = new Document();
		doc.setBaseURI("http://jdom.org/");
		Element root = new Element("root");
		doc.setRootElement(root);
		URI uri = root.getXMLBaseURI();
		assertTrue(uri != null);
		assertEquals("http://jdom.org/", uri.toASCIIString());
	}

	@Test
	public void testXmlBaseRelative() throws URISyntaxException {
		Document doc = new Document();
		Element root = new Element("root");
		doc.setRootElement(root);
		root.setAttribute("base", "./sub/", Namespace.XML_NAMESPACE);
		URI uri = root.getXMLBaseURI();
		assertTrue(uri != null);
		assertEquals("./sub/", uri.toASCIIString());
	}

	@Test
	public void testXmlBaseRelativeToDoc() throws URISyntaxException {
		Document doc = new Document();
		doc.setBaseURI("http://jdom.org/");
		Element root = new Element("root");
		doc.setRootElement(root);
		root.setAttribute("base", "./sub/", Namespace.XML_NAMESPACE);
		URI uri = root.getXMLBaseURI();
		assertTrue(uri != null);
		assertEquals("http://jdom.org/sub/", uri.toASCIIString());
	}

	@Test
	public void testXmlBaseAbsolute() throws URISyntaxException {
		Document doc = new Document();
		doc.setBaseURI("http://jdom.org/some/path/to/low/level");
		Element root = new Element("root");
		doc.setRootElement(root);
		root.setAttribute("base", "/sub/", Namespace.XML_NAMESPACE);
		URI uri = root.getXMLBaseURI();
		assertTrue(uri != null);
		assertEquals("http://jdom.org/sub/", uri.toASCIIString());
	}

	@Test
	public void testXmlBaseSubAbsolute() throws URISyntaxException {
		Document doc = new Document();
		doc.setBaseURI("http://jdom.org/some/path/to/low/level");
		Element root = new Element("root");
		doc.setRootElement(root);
		root.setAttribute("base", "http://jdom2.org/sub/", Namespace.XML_NAMESPACE);
		URI uri = root.getXMLBaseURI();
		assertTrue(uri != null);
		assertEquals("http://jdom2.org/sub/", uri.toASCIIString());
	}

	@Test
	public void testXmlBaseBroken() {
		Document doc = new Document();
		doc.setBaseURI("http://jdom.org/");
		Element root = new Element("root");
		doc.setRootElement(root);
		root.setAttribute("base", "../  /sub/", Namespace.XML_NAMESPACE);
		try {
			root.getXMLBaseURI();
			UnitTestUtil.failNoException(URISyntaxException.class);
		} catch (Exception e) {
			UnitTestUtil.checkException(URISyntaxException.class, e);
		}
	}

}
