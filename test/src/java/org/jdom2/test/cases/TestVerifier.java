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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.Verifier;
import org.junit.Test;
import org.junit.runner.JUnitCore;

@SuppressWarnings("javadoc")
public final class TestVerifier {
	
	private final char BADCHAR = (char)0x0b;

    /**
     * The main method runs all the tests in the text ui
     */
    public static void main (String args[]) 
     {
        JUnitCore.runClasses(TestVerifier.class);
    }
    
	/**
	 * Test checkElementName such that a name is validated as an
	 * xml name with the following caveats.
	 * The name must not start with "-" or ":".
	 */
    @Test
	public void testCheckElementName() {
		//check out of range values
		assertNotNull("validated invalid null", Verifier.checkElementName(null));
		assertNotNull("validated invalid name with null char", Verifier.checkElementName("test" + (char)0x0));
		assertNotNull("validated invalid name with null char", Verifier.checkElementName("test" + (char)0x0 + "ing"));
		assertNotNull("validated invalid name with null char", Verifier.checkElementName((char)0x0 + "test"));
		assertNotNull("validated invalid name with 0x01", Verifier.checkElementName((char)0x01 + "test"));
		assertNotNull("validated invalid name with 0xD800", Verifier.checkElementName("test" + (char)0xD800));
		assertNotNull("validated invalid name with 0xD800", Verifier.checkElementName("test" + (char)0xD800 + "ing"));
		assertNotNull("validated invalid name with 0xD800", Verifier.checkElementName((char)0xD800 + "test"));
		assertNotNull("validated invalid name with :", Verifier.checkElementName("test" + ':' + "local"));
		assertNotNull("validated invalid name with :", Verifier.checkElementName("abcd:"));
		assertNotNull("validated invalid name with :", Verifier.checkElementName("abc:d"));
		assertNotNull("validated invalid name with :", Verifier.checkElementName("ab:cd"));
		assertNotNull("validated invalid name with :", Verifier.checkElementName("a:bcd"));
		assertNotNull("validated invalid name with :", Verifier.checkElementName(":abcd"));

		//invalid start characters
		assertNotNull("validated invalid name with startin -", Verifier.checkElementName('-' + "test"));
		assertNotNull("validated invalid name with startin :", Verifier.checkElementName(':' + "test"));

		//valid tests
		assertNull("invalidated valid name with starting _", Verifier.checkElementName('_' + "test"));
		assertNull("invalidated valid name with _", Verifier.checkElementName("test" + '_'));
		assertNull("invalidated valid name with .", Verifier.checkElementName("test" + '.' + "name"));
		assertNull("invalidated valid name with 0x00B7", Verifier.checkElementName("test" + (char)0x00B7));
		assertNull("invalidated valid name with 0x4E01", Verifier.checkElementName("test" + (char)0x4E01));
		assertNull("invalidated valid name with 0x0301", Verifier.checkElementName("test" + (char)0x0301));

	}
    
	/**
	 * Test for a valid Attribute name.  A valid Attribute name is
	 * an xml name (xml start character + xml name characters) with
	 * some special considerations.  "xml:lang" and "xml:space" are
	 * allowed.  No ':' are allowed since prefixes are defined with
	 * Namespace objects.  The name must not be "xmlns"
	 */
    @Test
	public void testCheckAttributeName() {
		//check out of range values
		assertNotNull("validated invalid null", Verifier.checkAttributeName(null));
		assertNotNull("validated invalid name with null", Verifier.checkAttributeName("test" + (char)0x0));
		assertNotNull("validated invalid name with null", Verifier.checkAttributeName("test" + (char)0x0 + "ing"));
		assertNotNull("validated invalid name with null", Verifier.checkAttributeName((char)0x0 + "test"));
		assertNotNull("validated invalid name with 0x01", Verifier.checkAttributeName((char)0x01 + "test"));
		assertNotNull("validated invalid name with 0xD800", Verifier.checkAttributeName("test" + (char)0xD800));
		assertNotNull("validated invalid name with 0xD800", Verifier.checkAttributeName("test" + (char)0xD800 + "ing"));
		assertNotNull("validated invalid name with 0xD800", Verifier.checkAttributeName((char)0xD800 + "test"));
		assertNotNull("validated invalid name with :", Verifier.checkAttributeName("test" + ':' + "local"));
		assertNotNull("validated invalid name with xml:lang", Verifier.checkAttributeName("xml:lang"));
		assertNotNull("validated invalid name with xml:space", Verifier.checkAttributeName("xml:space"));

		//invalid start characters
		assertNotNull("validated invalid name with startin -", Verifier.checkAttributeName('-' + "test"));
		assertNotNull("validated invalid name with xmlns", Verifier.checkAttributeName("xmlns"));
		assertNotNull("validated invalid name with startin :", Verifier.checkAttributeName(':' + "test"));

		//valid tests
		assertNull("invalidated valid name with starting _", Verifier.checkAttributeName('_' + "test"));
		assertNull("invalidated valid name with _", Verifier.checkAttributeName("test" + '_'));
		assertNull("invalidated valid name with .", Verifier.checkAttributeName("test" + '.' + "name"));
		assertNull("invalidated valid name with 0x00B7", Verifier.checkAttributeName("test" + (char)0x00B7));
		assertNull("invalidated valid name with 0x4E01", Verifier.checkAttributeName("test" + (char)0x4E01));
		assertNull("invalidated valid name with 0x0301", Verifier.checkAttributeName("test" + (char)0x0301));

    	assertNull(Verifier.checkAttributeName("hi"));
    	assertNull(Verifier.checkAttributeName("hi2you"));
    	assertNull(Verifier.checkAttributeName("hi_you"));
    	
    	assertNotNull(Verifier.checkAttributeName(null));
    	assertNotNull(Verifier.checkAttributeName(""));
    	assertNotNull(Verifier.checkAttributeName("   "));
    	assertNotNull(Verifier.checkAttributeName("  hi  "));
    	assertNotNull(Verifier.checkAttributeName("hi "));
    	assertNotNull(Verifier.checkAttributeName(" hi"));
    	assertNotNull(Verifier.checkAttributeName("2bad"));

    }
    
	/**
	 * Test that a String contains only xml characters.  The method under
	 * only checks for null values and then character by character scans
	 * the string so this test is not exhaustive
	 */
    @Test
	public void testCheckCharacterData() {
		//check out of range values
		assertNotNull("validated invalid null", Verifier.checkCharacterData(null));
		assertNotNull("validated invalid string with null", Verifier.checkCharacterData("test" + (char)0x0));
		assertNotNull("validated invalid string with null", Verifier.checkCharacterData("test" + (char)0x0 + "ing"));
		assertNotNull("validated invalid string with null", Verifier.checkCharacterData((char)0x0 + "test"));
		assertNotNull("validated invalid string with 0x01", Verifier.checkCharacterData((char)0x01 + "test"));
		assertNotNull("validated invalid string with 0xD800", Verifier.checkCharacterData("test" + (char)0xD800));
		assertNotNull("validated invalid string with 0xD800", Verifier.checkCharacterData("test" + (char)0xD800 + "ing"));
		assertNotNull("validated invalid string with 0xD800", Verifier.checkCharacterData((char)0xD800 + "test"));

		//various valid strings
		assertNull("invalidated valid string with \n", Verifier.checkCharacterData("test" + '\n' + "ing"));
		assertNull("invalidated valid string with 0x29", Verifier.checkCharacterData("test" +(char)0x29));
		//a few higher values
		assertNull("invalidated valid string with 0x0B08", Verifier.checkCharacterData("test" + (char)0x0B08));
		assertNull("invalidated valid string with \t", Verifier.checkCharacterData("test" + '\t'));
		//xml letter
		assertNull("invalidated valid string with 0x42", Verifier.checkCharacterData("test" + (char)0x42));
		assertNull("invalidated valid string with 0x4E01", Verifier.checkCharacterData("test" + (char)0x4E01));

	}
    
	/**
	 * Test that checkCDATASection verifies CDATA excluding
	 * the closing delimiter.
	 */
    @Test
	public void testCheckCDATASection() {
		//check out of range values
		assertNotNull("validated invalid null", Verifier.checkCDATASection(null));
		assertNotNull("validated invalid string with null", Verifier.checkCDATASection("test" + (char)0x0));
		assertNotNull("validated invalid string with null", Verifier.checkCDATASection("test" + (char)0x0 + "ing"));
		assertNotNull("validated invalid string with null", Verifier.checkCDATASection((char)0x0 + "test"));
		assertNotNull("validated invalid string with 0x01", Verifier.checkCDATASection((char)0x01 + "test"));
		assertNotNull("validated invalid string with 0xD800", Verifier.checkCDATASection("test" + (char)0xD800));
		assertNotNull("validated invalid string with 0xD800", Verifier.checkCDATASection("test" + (char)0xD800 + "ing"));
		assertNotNull("validated invalid string with 0xD800", Verifier.checkCDATASection((char)0xD800 + "test"));
		assertNotNull("validated invalid string with ]]>", Verifier.checkCDATASection("test]]>"));

		//various valid strings
		assertNull("invalidated valid string with \n", Verifier.checkCDATASection("test" + '\n' + "ing"));
		assertNull("invalidated valid string with 0x29", Verifier.checkCDATASection("test" +(char)0x29));
		assertNull("invalidated valid string with ]", Verifier.checkCDATASection("test]"));
		assertNull("invalidated valid string with [", Verifier.checkCDATASection("test["));
		//a few higher values
		assertNull("invalidated valid string with 0x0B08", Verifier.checkCDATASection("test" + (char)0x0B08));
		assertNull("invalidated valid string with \t", Verifier.checkCDATASection("test" + '\t'));
		//xml letter
		assertNull("invalidated valid string with 0x42", Verifier.checkCDATASection("test" + (char)0x42));
		assertNull("invalidated valid string with 0x4E01", Verifier.checkCDATASection("test" + (char)0x4E01));

	}
    
	/**
	 * Test that checkNamespacePrefix validates against xml names
	 * with the following exceptions.  Prefix names must not start
	 * with "-", "xmlns", digits, "$", or "." and must not contain
	 * ":"
	 */
    @Test
	public void testCheckNamespacePrefix() {
		//check out of range values
		assertNotNull("validated invalid name with null", Verifier.checkNamespacePrefix("test" + (char)0x0));
		assertNotNull("validated invalid name with null", Verifier.checkNamespacePrefix("test" + (char)0x0 + "ing"));
		assertNotNull("validated invalid name with null", Verifier.checkNamespacePrefix((char)0x0 + "test"));
		assertNotNull("validated invalid name with 0x01", Verifier.checkNamespacePrefix((char)0x01 + "test"));
		assertNotNull("validated invalid name with 0xD800", Verifier.checkNamespacePrefix("test" + (char)0xD800));
		assertNotNull("validated invalid name with 0xD800", Verifier.checkNamespacePrefix("test" + (char)0xD800 + "ing"));
		assertNotNull("validated invalid name with 0xD800", Verifier.checkNamespacePrefix((char)0xD800 + "test"));
		assertNotNull("validated invalid name with :", Verifier.checkNamespacePrefix("test" + ':' + "local"));

		//invalid start characters
		assertNotNull("validated invalid name with startin -", Verifier.checkNamespacePrefix('-' + "test"));
		assertNotNull("validated invalid name with xmlns", Verifier.checkNamespacePrefix("xmlns"));
		assertNotNull("validated invalid name with startin :", Verifier.checkNamespacePrefix(':' + "test"));
		assertNotNull("validated invalid name with starting digit", Verifier.checkNamespacePrefix("9"));
		assertNotNull("validated invalid name with starting $", Verifier.checkNamespacePrefix("$"));
		assertNotNull("validated invalid name with starting .", Verifier.checkNamespacePrefix("."));
		
		// cannot start with xml (case insensitive).
		assertNotNull("validated invalid name beginning with xml", Verifier.checkNamespacePrefix("xmlabc"));
		assertNotNull("validated invalid name beginning with xml", Verifier.checkNamespacePrefix("xmLabc"));
		assertNotNull("validated invalid name beginning with xml", Verifier.checkNamespacePrefix("xMlabc"));
		assertNotNull("validated invalid name beginning with xml", Verifier.checkNamespacePrefix("Xmlabc"));
		

		//valid tests
		assertNull("invalidated valid null", Verifier.checkNamespacePrefix(null));
		assertNull("invalidated valid empty String", Verifier.checkNamespacePrefix(""));
		assertNull("invalidated valid name with starting _", Verifier.checkNamespacePrefix('_' + "test"));
		assertNull("invalidated valid name with _", Verifier.checkNamespacePrefix("test" + '_'));
		assertNull("invalidated valid name with .", Verifier.checkNamespacePrefix("test" + '.' + "name"));
		assertNull("invalidated valid name with digit", Verifier.checkNamespacePrefix("test9"));
		assertNull("invalidated valid name with 0x00B7", Verifier.checkNamespacePrefix("test" + (char)0x00B7));
		assertNull("invalidated valid name with 0x4E01", Verifier.checkNamespacePrefix("test" + (char)0x4E01));
		assertNull("invalidated valid name with 0x0301", Verifier.checkNamespacePrefix("test" + (char)0x0301));
		assertNull("invalidated valid name with xml embedded", Verifier.checkNamespacePrefix("txml"));
		assertNull("invalidated valid name with xml embedded", Verifier.checkNamespacePrefix("xmml"));

	}

    /**
	 * Tests that checkNamespaceURI validates xml uri's.
	 * A valid URI is alphanumeric characters and the reserved characters:
	 * ";" | "/" | "?" | ":" | "@" | "&" | "=" | "+" |  "$" | ","
	 *            
	 * The URI cannot begin with a digit, "-" or "$".  It must have at least
	 * one ":" separating the scheme from the scheme specific part
	 *
	 * XXX:TODO make this match the eventual specs for the Verifier class which is incomplete
	 */
    @Test
	public void testCheckNamespaceURI() {
		//invalid start characters
		assertNotNull("validated invalid URI with startin -", Verifier.checkNamespaceURI('-' + "test"));
		assertNotNull("validated invalid URI with starting digit", Verifier.checkNamespaceURI("9"));
		assertNotNull("validated invalid URI with starting $", Verifier.checkNamespaceURI("$"));

		//valid tests
		assertNull("invalidated valid null", Verifier.checkNamespaceURI(null));
		assertNull("invalidated valid null", Verifier.checkNamespaceURI(""));
		assertNull("invalidated valid URI with :", Verifier.checkNamespaceURI("test" + ':' + "local"));
		assertNull("invalidated valid URI with _", Verifier.checkNamespaceURI("test" + '_'));
		assertNull("invalidated valid URI with .", Verifier.checkNamespaceURI("test" + '.' + "URI"));
		assertNull("invalidated valid URI with digit", Verifier.checkNamespaceURI("test9"));
		assertNull("invalidated valid URI with 0x00B7", Verifier.checkNamespaceURI("test" + (char)0x00B7));
		assertNull("invalidated valid URI with 0x4E01", Verifier.checkNamespaceURI("test" + (char)0x4E01));
		assertNull("invalidated valid URI with 0x0301", Verifier.checkNamespaceURI("test" + (char)0x0301));

		//check out of range values

		/** skip these tests until the time the checks are implemented
		assertNull("validated invalid URI with xmlns", Verifier.checkNamespaceURI("xmlns"));
		assertNull("validated invalid URI with startin :", Verifier.checkNamespaceURI(':' + "test"));
		assertNull("validated invalid URI with starting .", Verifier.checkNamespaceURI("."));
		
		assertNull("validated invalid URI with null", Verifier.checkNamespaceURI("test" + (char)0x0));
		assertNull("validated invalid URI with null", Verifier.checkNamespaceURI("test" + (char)0x0 + "ing"));
		assertNull("validated invalid URI with null", Verifier.checkNamespaceURI((char)0x0 + "test"));
		assertNull("validated invalid URI with 0x01", Verifier.checkNamespaceURI((char)0x01 + "test"));
		assertNull("validated invalid URI with 0xD800", Verifier.checkNamespaceURI("test" + (char)0xD800));
		assertNull("validated invalid URI with 0xD800", Verifier.checkNamespaceURI("test" + (char)0xD800 + "ing"));
		assertNull("validated invalid URI with 0xD800", Verifier.checkNamespaceURI((char)0xD800 + "test"));
		*/
	}

    /**
	 * Test that checkProcessintInstructionTarget validates the name
	 * of a processing instruction.  This name must be a normal xml
	 * and cannot have ":" or "xml" in the name.
	 */
    @Test
	public void testCheckProcessingInstructionTarget() {
		//check out of range values
		assertNotNull("validated invalid null", Verifier.checkProcessingInstructionTarget(null));
		assertNotNull("validated invalid name with null", Verifier.checkProcessingInstructionTarget("test" + (char)0x0));
		assertNotNull("validated invalid name with null", Verifier.checkProcessingInstructionTarget("test" + (char)0x0 + "ing"));
		assertNotNull("validated invalid name with null", Verifier.checkProcessingInstructionTarget((char)0x0 + "test"));
		assertNotNull("validated invalid name with 0x01", Verifier.checkProcessingInstructionTarget((char)0x01 + "test"));
		assertNotNull("validated invalid name with 0xD800", Verifier.checkProcessingInstructionTarget("test" + (char)0xD800));
		assertNotNull("validated invalid name with 0xD800", Verifier.checkProcessingInstructionTarget("test" + (char)0xD800 + "ing"));
		assertNotNull("validated invalid name with 0xD800", Verifier.checkProcessingInstructionTarget((char)0xD800 + "test"));
		assertNotNull("validated invalid name with :", Verifier.checkProcessingInstructionTarget("test" + ':' + "local"));
		assertNotNull("validated invalid name with xml:space", Verifier.checkProcessingInstructionTarget("xml:space"));
		assertNotNull("validated invalid name with xml:lang", Verifier.checkProcessingInstructionTarget("xml:lang"));
		assertNotNull("validated invalid name with xml", Verifier.checkProcessingInstructionTarget("xml"));
		assertNotNull("validated invalid name with xMl", Verifier.checkProcessingInstructionTarget("xMl"));

		//invalid start characters
		assertNotNull("validated invalid name with startin -", Verifier.checkProcessingInstructionTarget('-' + "test"));
		assertNotNull("validated invalid name with startin :", Verifier.checkProcessingInstructionTarget(':' + "test"));
		//valid tests
		assertNull("invalidated valid name with starting _", Verifier.checkProcessingInstructionTarget('_' + "test"));
		assertNull("invalidated valid name with _", Verifier.checkProcessingInstructionTarget("test" + '_'));
		assertNull("invalidated valid name with .", Verifier.checkProcessingInstructionTarget("test" + '.' + "name"));
		assertNull("invalidated valid name with 0x00B7", Verifier.checkProcessingInstructionTarget("test" + (char)0x00B7));
		assertNull("invalidated valid name with 0x4E01", Verifier.checkProcessingInstructionTarget("test" + (char)0x4E01));
		assertNull("invalidated valid name with 0x0301", Verifier.checkProcessingInstructionTarget("test" + (char)0x0301));

	}
    
    @Test
    public void testCheckProcessingInstructionData() {
    	assertNull(Verifier.checkProcessingInstructionData(""));
    	assertNull(Verifier.checkProcessingInstructionData("  "));
    	assertNull(Verifier.checkProcessingInstructionData("hi"));
    	assertNull(Verifier.checkProcessingInstructionData(" h i "));
    	
    	assertNotNull(Verifier.checkProcessingInstructionData(null));
    	assertNotNull(Verifier.checkProcessingInstructionData("hi" + (char)0x0b + " there"));
    	assertNotNull(Verifier.checkProcessingInstructionData("can't have '?>' in text."));
    }

	/**
	 * Test checkCommentData such that a comment is validated as an
	 * xml comment consisting of xml characters with the following caveats.
	 * The comment must not contain a double hyphen.
	 */
    @Test
	public void testCheckCommentData() {
		//check out of range values
		assertNotNull("validated invalid null", Verifier.checkCommentData(null));
		assertNotNull("validated invalid string with null", Verifier.checkCommentData("test" + (char)0x0));
		assertNotNull("validated invalid string with null", Verifier.checkCommentData("test" + (char)0x0 + "ing"));
		assertNotNull("validated invalid string with null", Verifier.checkCommentData((char)0x0 + "test"));
		assertNotNull("validated invalid string with 0x01", Verifier.checkCommentData((char)0x01 + "test"));
		assertNotNull("validated invalid string with 0xD800", Verifier.checkCommentData("test" + (char)0xD800));
		assertNotNull("validated invalid string with 0xD800", Verifier.checkCommentData("test" + (char)0xD800 + "ing"));
		assertNotNull("validated invalid string with 0xD800", Verifier.checkCommentData((char)0xD800 + "test"));
		assertNotNull("validated invalid string with --", Verifier.checkCommentData("--test"));
		assertNotNull("validated invalid string ending with -", Verifier.checkCommentData("test-"));

		//various valid strings
		assertNull("invalidated valid string with \n", Verifier.checkCommentData("test" + '\n' + "ing"));
		assertNull("invalidated valid string with 0x29", Verifier.checkCommentData("test" +(char)0x29));
		//a few higher values
		assertNull("invalidated valid string with 0x0B08", Verifier.checkCommentData("test" + (char)0x0B08));
		assertNull("invalidated valid string with \t", Verifier.checkCommentData("test" + '\t'));
		//xml letter
		assertNull("invalidated valid string with 0x42", Verifier.checkCommentData("test" + (char)0x42));
		assertNull("invalidated valid string with 0x4E01", Verifier.checkCommentData("test" + (char)0x4E01));

	}
    
    @Test
    public void testCheckNamespaceCollision() {
    	try {
    		Namespace ns1 = Namespace.getNamespace("aaa", "http://acme.com/aaa");
    		Element e = new Element("aaa", ns1);
    		e.setAttribute("att1", "att1");
    		Namespace defns = Namespace.getNamespace("http://acme.com/default");
    		e.addNamespaceDeclaration(defns);
    		// pass
    	} catch (Exception e) {
    		// also see http://markmail.org/message/hvzz73em7ztt5i5k
    		fail("Bug http://www.junlu.com/msg/166290.html");
    	}
    	
    	Namespace nsnp = Namespace.getNamespace("rootns");
    	Namespace nswp = Namespace.getNamespace("p", nsnp.getURI());
    	Namespace mscp = Namespace.getNamespace("p", "childns");
    	Namespace ans  = Namespace.getNamespace("a", "attns");
    	
    	// no collision between Namespace and itself
    	assertNull(Verifier.checkNamespaceCollision(nsnp, nsnp));
    	
    	// no collision between Namespace and other namespace with different prefix
    	assertNull(Verifier.checkNamespaceCollision(nsnp, nswp));
    	
    	// but collision between same prefix, but different URI
    	assertNotNull(Verifier.checkNamespaceCollision(nswp, mscp));
    	
    	Element root = new Element("root", nsnp);
    	root.addNamespaceDeclaration(nswp);
    	Attribute att = new Attribute("att", "val", nswp);
    	
    	// should be able to add 
    	assertNull(Verifier.checkNamespaceCollision(att, root));
    	root.setAttribute(att);
    	
    	root.setAttribute(new Attribute("ans", "v", ans));
    	
    	// cnns is child no namespace
    	Element cnns = new Element ("cnns");
    	root.addContent(cnns);
    	// cwns is child with namespace
    	Element cwns = new Element ("cwns", nsnp);
    	root.addContent(cwns);
    	// cpns is child prefixed namespace
    	Element cpns = new Element ("cpns", nswp);
    	root.addContent(cpns);
    	// cpms is child prefixed different namespace
    	Element cpms = new Element ("cpms", mscp);
    	root.addContent(cpms);
    	
    	//printlement(root);
//    	XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
//    	try {
//    		CharArrayWriter  w = new CharArrayWriter();
//			out.output(root, w);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

    	
    	// Check Namespace against elements.
    	assertNull   (Verifier.checkNamespaceCollision(att, root));
    	assertNull   (Verifier.checkNamespaceCollision(att, cnns));
    	assertNull   (Verifier.checkNamespaceCollision(att, cwns));
    	assertNull   (Verifier.checkNamespaceCollision(att, cpns));
    	
    	// now, check it against the child that redefines the 'p' prefix.
    	assertNotNull(Verifier.checkNamespaceCollision(nswp, cpms));
    	// root has an attribute with the namspace a->ans, cannot then have a->dummy
    	assertNotNull(Verifier.checkNamespaceCollision(Namespace.getNamespace("a", "dummy"), root));
    	// root has an additional namespace p->rootns, can't then have a different 'p'
    	assertNotNull(Verifier.checkNamespaceCollision(Namespace.getNamespace("p", "dummy"), root));
    	
    	// Check Namespace against Attributes
    	// we do the same tests as above, only we wrap it in an Attribute
    	// first, one that passes.
    	assertNull(Verifier.checkNamespaceCollision(att, cwns));
    	// now, check it against the child that redefines the 'p' prefix.
    	assertNotNull(Verifier.checkNamespaceCollision(att, cpms));
    	// root has an attribute with the namspace a->ans, cannot then have a->dummy
    	assertNotNull(Verifier.checkNamespaceCollision(
    			new Attribute("ax", "v", Namespace.getNamespace("a", "dummy")), root));
    	// root has an additional namespace p->rootns, can't then have a different 'p'
    	assertNotNull(Verifier.checkNamespaceCollision(
    			new Attribute("ax", "v", Namespace.getNamespace("p", "dummy")), root));
    	
    	
    	// now we need to check the namespaces against Attributes not Elements
    	// first, one that works
    	assertNull(Verifier.checkNamespaceCollision(nswp, new Attribute("foo", "bar")));
    	// now, check it against the child that redefines the 'p' prefix.
    	assertNotNull(Verifier.checkNamespaceCollision(nswp, new Attribute("foo", "bar", mscp)));
    	
    	// Now test some List structures.
    	// first, one that passes.
    	assertNull(Verifier.checkNamespaceCollision(nswp, root.getAdditionalNamespaces()));
    	// then, a passing one... with junk...
    	List<Object> c = null;
    	assertNull(Verifier.checkNamespaceCollision(nswp, c));
    	c = new ArrayList<Object>();
    	c.addAll(Arrays.asList(root.getAdditionalNamespaces().toArray()));
    	c.add(Integer.valueOf(1));
    	c.add(0, "dummy");
    	assertNull(Verifier.checkNamespaceCollision(nswp, c));
    	
    	// now, check something that will conflict... check against a conflicting attribute.
    	// which is already in there.
    	assertNotNull(Verifier.checkNamespaceCollision(mscp, c));
    	
    	// now check against an Element.
    	// replace prefixed namespace with prefixed Element
    	c.set(c.indexOf(nswp), cpns);
    	assertNotNull(Verifier.checkNamespaceCollision(mscp, c));
    	
    	// now replace prefixed Element with prefixed Attribute
    	c.set(c.indexOf(cpns), new Attribute("a", "b", nswp));
    	assertNotNull(Verifier.checkNamespaceCollision(mscp, c));
    	
    	
    	// some basic namespace/attribute tests similar to the ones done for the bug test up top.
    	// attributes with no prefix can never collide... because attributes are always
    	// in the NO_NAMESPACE unless prefixed.
    	assertNull(Verifier.checkNamespaceCollision(new Attribute("a", "b"), root));
    	// att is already on root, can't fail.
    	assertNull(Verifier.checkNamespaceCollision(att, root));
    	// but, we can fail an attribute on the child with changed p prefix
    	assertNotNull(Verifier.checkNamespaceCollision(new Attribute("a", "b", mscp), root));
    	
    }
    
    @Test
    public void testCheckPublicID() {
    	assertNull("invalidated valid publicid: null", Verifier.checkPublicID(null));
    	assertNull("invalidated valid publicid: ''",   Verifier.checkPublicID(""));
    	assertNull("invalidated valid publicid: '  '", Verifier.checkPublicID("  "));
    	assertNull("invalidated valid publicid: contains \"'\"", 
    			Verifier.checkPublicID("shroedinger's cat was here"));
    	
    	assertNotNull(Verifier.checkPublicID("cannot have " + BADCHAR + " characters here"));
    	
    }

    @Test
    public void testCheckXMLName() {
    	assertNull(Verifier.checkXMLName("hi"));
    	assertNull(Verifier.checkXMLName("hi2you"));
    	assertNull(Verifier.checkXMLName("hi_you"));
    	assertNull(Verifier.checkXMLName("hi:you"));
    	
    	assertNotNull(Verifier.checkXMLName(null));
    	assertNotNull(Verifier.checkXMLName(""));
    	assertNotNull(Verifier.checkXMLName("   "));
    	assertNotNull(Verifier.checkXMLName("  hi  "));
    	assertNotNull(Verifier.checkXMLName("hi "));
    	assertNotNull(Verifier.checkXMLName(" hi"));
    	assertNotNull(Verifier.checkXMLName("2bad"));
    }
    
    @Test
    public void testCheckSystemLiteral() {
    	assertNull(Verifier.checkSystemLiteral(null));
    	assertNull(Verifier.checkSystemLiteral(""));
    	assertNull(Verifier.checkSystemLiteral("  "));
    	assertNull(Verifier.checkSystemLiteral("frodo's theme "));
    	assertNull(Verifier.checkSystemLiteral("frodo has a \"theme\" "));
    	
    	assertNotNull(Verifier.checkSystemLiteral("frodo's \"theme\" "));
    	
    }


    @Test
    public void testCheckURI() {
    	assertNull(Verifier.checkURI(null));
    	assertNull(Verifier.checkURI(""));
    	assertNull(Verifier.checkURI("http://www.jdom.org/index.html"));
    	assertNull(Verifier.checkURI("http://www.jdom.org:321/index.html"));
    	assertNull(Verifier.checkURI("http://www.jdom.org%32%01/index.html?%ab"));
    	assertNull(Verifier.checkURI("http://www.jdom.org/index.html%31"));
    	
    	assertNotNull(Verifier.checkURI("http://www.jdom.org/ index.html"));
    	assertNotNull(Verifier.checkURI("  http://www.jdom.org/index.html  "));
    	assertNotNull(Verifier.checkURI("http://www.jdom.org%3.21/index.html"));
    	assertNotNull(Verifier.checkURI("http://www.jdom.org%.21/index.html"));
    	assertNotNull(Verifier.checkURI("http://www.jdom.org%3g21/index.html"));
    	assertNotNull(Verifier.checkURI("http://www.jdom.org%3/index.html"));
    	assertNotNull(Verifier.checkURI("http://www.jdom.org" + BADCHAR + "/index.html"));
    	assertNotNull(Verifier.checkURI("http://www.jdom.org" + (char)0x05 + "/index.html"));
    	assertNotNull(Verifier.checkURI("http://www.jdom.org/index.html%3"));
    }
    
    @Test
    public void testIsXMLCharacter() {
    	// this test is not part of the automatic tests because it takes an int
    	// as an argument, instead of a char.
    	// cherry-pick some tests.
    	assertTrue(Verifier.isXMLCharacter('\n'));
    	assertTrue(Verifier.isXMLCharacter('\r'));
    	assertTrue(Verifier.isXMLCharacter('\t'));
    	assertTrue(Verifier.isXMLCharacter(' '));
    	assertTrue(Verifier.isXMLCharacter(0xd7ff));
    	assertTrue(Verifier.isXMLCharacter(0xe000));
    	assertTrue(Verifier.isXMLCharacter(0x10000));
    	
    	//cherry-pick values we know will fill out the coverage report.
    	assertFalse(Verifier.isXMLCharacter(0));
    	assertFalse(Verifier.isXMLCharacter(0x19));
    	assertFalse(Verifier.isXMLCharacter(0xd800));
    	assertFalse(Verifier.isXMLCharacter(0xffff));
    	assertFalse(Verifier.isXMLCharacter(0x110000));
    	
    }
    
    @Test
    public void testIsAllXMLWhitespace() {
    	assertTrue(Verifier.isAllXMLWhitespace(""));
    	assertTrue(Verifier.isAllXMLWhitespace(" "));
    	assertTrue(Verifier.isAllXMLWhitespace(" \r\n\t "));
    	try {
    		Verifier.isAllXMLWhitespace(null);
    		fail("Expected a NullPointerException, but it did not happen");
    	} catch (NullPointerException npe) {
    		// good.
    	}
    	assertFalse(Verifier.isAllXMLWhitespace(" a "));
    	assertFalse(Verifier.isAllXMLWhitespace(" &nbsp; "));
    	// \u00A0 is Non-Break space. 
    	assertFalse(Verifier.isAllXMLWhitespace("\u00A0"));
    }

}
