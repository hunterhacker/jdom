/*--

 Copyright (C) 2000-2012 Jason Hunter & Brett McLaughlin.
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
    written permission, please contact <request_AT_jdom_DOT_org>.

 4. Products derived from this software may not be called "JDOM", nor
    may "JDOM" appear in their name, without prior written permission
    from the JDOM Project Management <request_AT_jdom_DOT_org>.

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
 created by Jason Hunter <jhunter_AT_jdom_DOT_org> and
 Brett McLaughlin <brett_AT_jdom_DOT_org>.  For more information
 on the JDOM Project, please see <http://www.jdom.org/>.

 */
package org.jdom2.test.cases.jaxb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.xml.stream.XMLStreamReader;

import org.junit.Test;

import org.jdom2.CDATA;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.Text;
import org.jdom2.input.StAXStreamBuilder;
import org.jdom2.output.StAXStreamReader;
import org.jdom2.output.support.AbstractStAXStreamReader;

/**
 * Tests for {@link AbstractStAXStreamReader}
 * @author gordon burgett https://github.com/gburgett
 */
@SuppressWarnings("javadoc")
public class TestJDOMStreamReader {
    
    public TestJDOMStreamReader() {
    }
    
    @Test
    public void testSimpleDocument_BuildsFromReader_SameDocument() throws Exception {
        System.out.println("testSimpleDocument_BuildsFromReader_SameDocument");
        
        Document doc = new Document();
        doc.setRootElement(new Element("simple"));
        
        StAXStreamReader reader = new StAXStreamReader();
        XMLStreamReader instance = reader.output(doc);
        Document read;
        try{
            StAXStreamBuilder builder = new StAXStreamBuilder();
            read = builder.build(instance);
        }
        finally{
            instance.close();
        }
        
        assertNotNull("Should have built a doc", read);
        assertEquals("Should have correct root element", "simple", read.getRootElement().getName());
    }//end testSimpleDocument_BuildsFromReader_SameDocument
    
    @Test
    public void testSimpleDocWithNamespace_BuildsFromReader_SameDocument() throws Exception {
        System.out.println("testSimpleDocWithNamespace_BuildsFromReader_SameDocument");
        
        Document doc = new Document();
        doc.setRootElement(new Element("simple", Namespace.getNamespace("testns")));
        doc.getRootElement().addNamespaceDeclaration(Namespace.getNamespace("tst", "test2"));
        
        Element content = new Element("test2ns", Namespace.getNamespace("tst", "test2"));
        content.setText("some text");
        doc.getRootElement().addContent(content);
        
        content = new Element("testNoNs", Namespace.NO_NAMESPACE);
        content.setText("other text");
        doc.getRootElement().addContent(content);
        
        Document read;
        StAXStreamReader reader = new StAXStreamReader();
        XMLStreamReader instance = reader.output(doc);
        try{
            StAXStreamBuilder builder = new StAXStreamBuilder();
            read = builder.build(instance);
        }
        finally{
            instance.close();
        }
        
        assertNotNull("Should have built a doc", read);
        assertEquals("Should have correct root element", "simple", read.getRootElement().getName());
        assertEquals("Should have correct root element", "testns", read.getRootElement().getNamespaceURI());
        
        List<Namespace> additional = read.getRootElement().getAdditionalNamespaces();
        assertEquals("Should have declared the additional ns", 2, additional.size());
        assertEquals("Should be right ns", "test2", additional.get(1).getURI());
        assertEquals("Should be right prefix", "tst", additional.get(1).getPrefix());
        
        List<Element> children = read.getRootElement().getChildren();
        assertEquals("Should have 2 kids", 2, children.size());
        assertEquals("Should be right kids in right order", "test2ns", children.get(0).getName());
        assertEquals("Should be right kids in right order", "test2", children.get(0).getNamespaceURI());
        assertEquals("Should be right kids in right order", "tst", children.get(0).getNamespacePrefix());
        assertEquals("Should be right kids in right order", "some text", children.get(0).getText());
        
        assertEquals("Should be right kids in right order", "testNoNs", children.get(1).getName());
        assertEquals("Should be right kids in right order", "", children.get(1).getNamespaceURI());
        assertEquals("Should be right kids in right order", "", children.get(1).getNamespacePrefix());
        assertEquals("Should be right kids in right order", "other text", children.get(1).getText());
    }//end testSimpleDocWithNamespace_BuildsFromReader_SameDocument
    
    @Test
    public void testDocWithAttributes_HasAttributes() throws Exception {
        System.out.println("testDocWithAttributes_HasAttributes");
        
        Document doc = new Document();
        doc.setRootElement(new Element("simple"));
        
        Element content = new Element("hasAtt");
        content.setAttribute("nons", "nons value");
        content.setAttribute("testns", "testns value", Namespace.getNamespace("tst", "testNs"));
        doc.getRootElement().addContent(content);
        
        Document read;
        StAXStreamReader reader = new StAXStreamReader();
        XMLStreamReader instance = reader.output(doc);
        try{
            StAXStreamBuilder builder = new StAXStreamBuilder();
            read = builder.build(instance);
        }
        finally{
            instance.close();
        }
        
        assertNotNull("Should have built a doc", read);
        assertEquals("Should have correct root element", "simple", read.getRootElement().getName());
        
        Element hasAtt = read.getRootElement().getChild("hasAtt");
        assertEquals("should have no-ns attr", "nons value", hasAtt.getAttributeValue("nons"));
        assertEquals("should have test-ns attr", "testns value", hasAtt.getAttributeValue("testns", Namespace.getNamespace("testNs")));
    }//end testDocWithAttributes_HasAttributes
    
    
    @Test
    public void testDocWithMixedContent_HandlesMixedContent() throws Exception {
        System.out.println("testDocWithMixedContent_HandlesMixedContent");
        
        
        Document doc = new Document();
        doc.setRootElement(new Element("simple"));
        
        doc.getRootElement().addContent(new Text("pre-element text"));
        Element el = new Element("junk");
        el.setText("in-element text");
        doc.getRootElement().addContent(el);
        doc.getRootElement().addContent(new CDATA("post-element text"));
        
        Document read;
        StAXStreamReader reader = new StAXStreamReader();
        XMLStreamReader instance = reader.output(doc);
        try{
            StAXStreamBuilder builder = new StAXStreamBuilder();
            read = builder.build(instance);
        }
        finally{
            instance.close();
        }
        
        assertNotNull("Should have built a doc", read);
        assertEquals("Should have correct root element", "simple", read.getRootElement().getName());
        
        List<Content> content = read.getRootElement().getContent();
        assertEquals("first content should be text", Content.CType.Text, content.get(0).getCType());
        assertEquals("second content should be element", Content.CType.Element, content.get(1).getCType());
        assertEquals("third content should be CDATA", Content.CType.CDATA, content.get(2).getCType());
        
        assertEquals("pre-element text", content.get(0).getValue());
        assertEquals("in-element text", content.get(1).getValue());
        assertEquals("post-element text", content.get(2).getValue());
    }//end testDocWithMixedContent_HandlesMixedContent
    
}


