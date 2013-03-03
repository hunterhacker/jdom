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

//import javax.xml.stream.XMLOutputFactory;
//import javax.xml.stream.XMLStreamWriter;

import org.junit.Test;

import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.input.StAXStreamWriter;

/**
 * Tests for {@link StAXStreamWriter}
 * @author gordon burgett https://github.com/gburgett
 */
@SuppressWarnings("javadoc")
public class TestJDOMStreamWriter {
    
	public TestJDOMStreamWriter() {
    }
    
    @Test
    public void testEmptyRootElement_DocumentHasEmptyRoot() throws Exception {
        System.out.println("testEmptyRootElement_DocumentHasEmptyRoot");
        
        Document doc;
        StAXStreamWriter writer = new StAXStreamWriter();
        try{
            writer.writeStartDocument();
            
            writer.writeEmptyElement("testroot");
            
            writer.writeEndDocument();
            
            doc = writer.getDocument();
        }
        finally{
            writer.close();
        }
        
        assertNotNull("Should have root element", doc.getRootElement());
        assertEquals("Should have correct name", "testroot", doc.getRootElement().getName());
        assertEquals("Should have no content", 0, doc.getRootElement().getContentSize());
    }//end testEmptyRootElement_DocumentHasEmptyRoot
    
    @Test
    public void testMultipleElementsWithText_DocHasElements() throws Exception {
        System.out.println("testMultipleElementsWithText_DocHasElements");
        
        Document doc;
        StAXStreamWriter writer = new StAXStreamWriter();
        try{
            writer.writeStartDocument();
            writer.writeStartElement("testroot");
            
                writer.writeStartElement("testdata");
                    writer.writeAttribute("id", "1");
                writer.writeEndElement();
                
                writer.writeStartElement("testdata");
                    writer.writeAttribute("id", "2");
                    writer.writeStartElement("deep");
                        writer.writeStartElement("deeper");
                            writer.writeCData("Textual CDATA");
                        writer.writeEndElement();
                    writer.writeEndElement();
                writer.writeEndElement();
                
            writer.writeEndElement();
            writer.writeEndDocument();
            
            doc = writer.getDocument();
        }
        finally{
            writer.close();
        }
        
        assertNotNull("Should have root element", doc.getRootElement());
        assertEquals("Should have correct name", "testroot", doc.getRootElement().getName());
        assertEquals("should have content", 2, doc.getRootElement().getContentSize());
        
        List<Element> content = doc.getRootElement().getChildren();
        assertEquals("should have correct names", "testdata", content.get(0).getName());
        assertEquals("should have correct ID attributes", "1", content.get(0).getAttributeValue("id"));
        assertEquals("should have correct names", "testdata", content.get(1).getName());
        assertEquals("should have correct ID attributes", "2", content.get(1).getAttributeValue("id"));
        
        List<Content> deepContent = content.get(1).getChild("deep").getChild("deeper").getContent();
        assertEquals("should have CDATA element", Content.CType.CDATA, deepContent.get(0).getCType());
        assertEquals("should have super deep data", "Textual CDATA", deepContent.get(0).getValue());
    }//end testMultipleElementsWithText_DocHasElements
    
    @Test
    public void testElementsWithNamespace() throws Exception {
        System.out.println("testElementsWithNamespace");
        
        Document doc;
        //XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(System.out);
        StAXStreamWriter writer = new StAXStreamWriter();
        try{
            writer.writeStartDocument();
            writer.setPrefix("", "testUri");
            writer.writeStartElement("testUri", "root");
                writer.writeDefaultNamespace("testUri");
                writer.writeNamespace("tst", "testUri2");
            
                writer.writeStartElement("tst", "element", "testUri2");
                    writer.writeAttribute("tst", "testUri2", "attr", "1");
                    writer.writeCharacters("prefixed ns");
                writer.writeEndElement();
                
                writer.writeStartElement("testUri", "element");
                    writer.writeCharacters("same ns");
                writer.writeEndElement();
                
                writer.setPrefix("", "");
                writer.writeStartElement("element");
                    writer.writeDefaultNamespace("");
                    writer.writeCharacters("no ns");
                writer.writeEndElement();
            
            writer.writeEndElement();
            writer.writeEndDocument();
            
            doc = writer.getDocument();
            //doc = null;
        }
        finally{
            writer.close();
        }
        
        assertNotNull("Should have root element", doc.getRootElement());
        assertEquals("Should have correct ns", "testUri", doc.getRootElement().getNamespaceURI());
        
        List<Namespace> declared = doc.getRootElement().getNamespacesIntroduced();
        assertEquals("Should have declared prefixed ns", "testUri2", declared.get(1).getURI());
        assertEquals("Should have declared prefixed ns", "tst", declared.get(1).getPrefix());
        
        List<Element> content = doc.getRootElement().getChildren();
        assertEquals("Should have correct ns", "testUri2", content.get(0).getNamespaceURI());
        assertEquals("Should have correct prefix", "tst", content.get(0).getNamespacePrefix());
        
        assertEquals("Should have correct ns", "testUri", content.get(1).getNamespaceURI());
        assertEquals("Should have no prefix", "", content.get(1).getNamespacePrefix());
        
        assertEquals("Should have no ns", "", content.get(2).getNamespaceURI());
        assertEquals("Should have no prefix", "", content.get(2).getNamespacePrefix());
        
    }//end testElementsWithNamespace
}
