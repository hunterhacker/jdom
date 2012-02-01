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
import java.util.Iterator;
import java.util.List;

import org.jdom2.*;
import org.jdom2.filter.ElementFilter;
import org.jdom2.test.util.UnitTestUtil;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import static org.junit.Assert.*;

@SuppressWarnings("javadoc")
public final class TestSerialization {

   /**
     * The main method runs all the tests in the text ui
     */
    public static void main(String args[]) {
        JUnitCore.runClasses(TestSerialization.class);
    }


    private void outAndBack(ElementFilter filter) {
        ElementFilter filter2 = UnitTestUtil.deSerialize(filter);
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
    
    
    public void compare(final Content a, final Content b) {
    	assertNotNull(a);
    	assertNotNull(b);
    	assertEquals(a.getClass(), b.getClass());
    	assertTrue(a != b);
    	
    	switch (a.getCType()) {
    		case Text:
    			assertEquals(a.getValue(), b.getValue());
    			break;
    		case CDATA:
    			assertEquals(a.getValue(), b.getValue());
    			break;
    		case Comment:
    			assertEquals(((Comment)a).getText(), ((Comment)b).getText());
    			break;
    		case DocType:
    			DocType da = (DocType)a;
    			DocType db = (DocType)b;
    			assertEquals(da.getElementName(), db.getElementName());
    			assertEquals(da.getPublicID(), db.getPublicID());
    			assertEquals(da.getSystemID(), db.getSystemID());
    			assertEquals(da.getInternalSubset(), db.getInternalSubset());
    			break;
    		case Element:
    			Element ea = (Element)a;
    			Element eb = (Element)b;
    			assertEquals(ea.getName(), eb.getName());
    			compare(ea.getAttributes(), eb.getAttributes());
    			assertEquals(ea.getNamespacesInScope(), eb.getNamespacesInScope());
    			break;
    		case EntityRef:
    			assertEquals(((EntityRef)a).getName(), ((EntityRef)b).getName());
    			break;
    		case ProcessingInstruction:
    			ProcessingInstruction pa = (ProcessingInstruction)a;
    			ProcessingInstruction pb = (ProcessingInstruction)b;
    			assertEquals(pa.getTarget(), pb.getTarget());
    			assertEquals(pa.getData(), pb.getData());
    			break;
    	}
    }
    
    
    private void compare(List<Attribute> a, List<Attribute> b) {
		assertTrue(a != b);
		assertTrue(a.size() == b.size());
		Iterator<Attribute> ait = a.iterator();
		Iterator<Attribute> bit = b.iterator();
		while (ait.hasNext() && bit.hasNext()) {
			Attribute aa = ait.next();
			Attribute bb = bit.next();
			assertEquals(aa.getName(), bb.getName());
			assertEquals(aa.getNamespace(), bb.getNamespace());
			assertEquals(aa.getValue(), bb.getValue());
		}
		assertFalse(ait.hasNext());
		assertFalse(bit.hasNext());
		
	}


	@Test
    public void testDocumentSerialization() {
		// test serialization of all JDOM core types.
		// Document, Element, DocType, Comment, CDATA, Text,
		// ProcessingInstruction, EntityRef, Namespace, and Attribute.
		// Additionally, test ContentList and AttributeList
    	Document doc = new Document();
    	doc.setDocType(new DocType("root", "pubid", "sysid"));
    	doc.getDocType().setInternalSubset("internalss");
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
    	
    	
    	Document ser = UnitTestUtil.deSerialize(doc);
    	Iterator<Content> sit = ser.getDescendants();
    	Iterator<Content> dit = doc.getDescendants();
    	while (sit.hasNext() && dit.hasNext()) {
    		compare(sit.next(), dit.next());
    	}
    	assertFalse(sit.hasNext());
    	assertFalse(dit.hasNext());
    	
    }
    
    
    
}
