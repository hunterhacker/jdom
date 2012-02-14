package org.jdom2.test.cases;

/*-- 

 Copyright (C) 2007 Jason Hunter.
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
import static org.jdom2.test.util.UnitTestUtil.compare;
import static org.jdom2.test.util.UnitTestUtil.deSerialize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Test;
import org.junit.runner.JUnitCore;

import org.jdom2.Attribute;
import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.Content;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.EntityRef;
import org.jdom2.Namespace;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;
import org.jdom2.filter.ElementFilter;

@SuppressWarnings("javadoc")
public final class TestSerialization {

   /**
     * The main method runs all the tests in the text ui
     */
    public static void main(String args[]) {
        JUnitCore.runClasses(TestSerialization.class);
    }


    private void outAndBack(ElementFilter filter) {
        ElementFilter filter2 = deSerialize(filter);
        assertTrue(filter.equals(filter2));
    }

    @Test
    public void test_ElementFilterName() {
        outAndBack(new ElementFilter("name"));
    }

    @Test
    public void test_ElementFilterNameNamespace() {
        outAndBack(new ElementFilter("name", Namespace.XML_NAMESPACE));
    }

    @Test
    public void test_ElementFilterNamespace() {
        outAndBack(new ElementFilter(Namespace.XML_NAMESPACE));
    }

    @Test
    public void test_ElementFilterEmpty() {
        outAndBack(new ElementFilter());
    }
    
    
	@Test
    public void testDocumentSerialization() {
		// test serialization of all JDOM core types.
		// Document, Element, DocType, Comment, CDATA, Text,
		// ProcessingInstruction, EntityRef, Namespace, and Attribute.
		// Additionally, test ContentList and AttributeList
    	Document doc = new Document();
    	doc.setDocType(new DocType("root", "pubid", "sysid"));
    	doc.getDocType().setInternalSubset(" internalss  with space ");
    	doc.addContent(new Comment("doccomment1"));
    	doc.addContent(new ProcessingInstruction("target1"));
    	doc.addContent(new ProcessingInstruction("target2").setData("key=value"));
    	doc.addContent(new Comment("doccomment2"));
    	Element root = new Element("root");
    	doc.setRootElement(root);
    	root.setAttribute(new Attribute("att", "value"));
    	root.addContent(new Text("  "));
    	root.addNamespaceDeclaration(Namespace.getNamespace("pfx", "uriupfx"));
    	root.addContent(new Element("child", Namespace.getNamespace("nopfxuri")));
    	root.addContent(new EntityRef("name"));
    	root.addContent(new CDATA("cdata"));
    	
    	
    	Document ser = deSerialize(doc);
    	Iterator<Content> sit = ser.getDescendants();
    	Iterator<Content> dit = doc.getDescendants();
    	while (sit.hasNext() && dit.hasNext()) {
    		compare(sit.next(), dit.next());
    	}
    	assertFalse(sit.hasNext());
    	assertFalse(dit.hasNext());
    	
    }
    
    @Test
	public void testAttribute() {
		Element root = new Element("root");
		Attribute att = new Attribute(
				"name", "value", Namespace.getNamespace("ans", "attnamespace"));
		root.setAttribute(att);
		
		Attribute attc = deSerialize(att);
		
		assertTrue(attc.getParent() == null);
		
		compare(att, attc);
	}
	
	
    
}
