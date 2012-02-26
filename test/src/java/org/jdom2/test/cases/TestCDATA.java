/*-- 

 Copyright (C) 2006 Brett McLaughlin & Jason Hunter.
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
package org.jdom2.test.cases;

import org.jdom2.CDATA;
import org.jdom2.Content;
import org.jdom2.Element;
import org.jdom2.IllegalDataException;
import org.jdom2.Text;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import static org.junit.Assert.*;

/**
 * Test for {@link CDATA}.
 * 
 * @author Victor Toni
 * @version 1.0.0
 */
@SuppressWarnings("javadoc")
public final class TestCDATA {

    /**
	 * The main method runs all the tests in the text ui
	 */
	public static void main (final String args[]) {
		JUnitCore.runClasses(TestCDATA.class);
	}

    /**
     * Test the protected CDATA constructor.
     */
	@Test
    public void test_TCC() {
        new CDATA() {
            // check protected constructor via anonymous class
    		private static final long serialVersionUID = 200L;
        };
    }

    /**
	 * Test the CDATA constructor with a valid and an invalid string.
	 */
	@Test
	public void test_TCC___String() {
        final String text = "this is a CDATA section";        

        final CDATA cdata = new CDATA(text);

		assertEquals(
				"incorrect CDATA constructed", 
				"[CDATA: " + text + ']', 
				cdata.toString()); 

        try {
            new CDATA("");
        } catch (final IllegalDataException e) {
            fail("CDATA constructor did throw exception on empty string");
        }

        try {
            new CDATA(null);
        } catch (final IllegalDataException e) {
            fail("CDATA constructor did throw exception on null");
        }

        try {
            new CDATA("some valid <text> with a CDATA section ending ]]>");
            fail("CDATA constructor didn't catch invalid comment string");
        } catch (final IllegalDataException e) {
			// Do nothing
		} catch (Exception e) {
			fail("Unexpected exception " + e.getClass());
        }
	}

    /**
	 * Verify a simple object == object test
	 */
	@Test
	public void test_TCM__boolean_equals_Object() {
        final String text = "this is a CDATA section";
	    final CDATA cdata = new CDATA(text);

	    final Object object = cdata;

        assertTrue("object not equal to CDATA", cdata.equals(object));
        assertTrue("CDATA not equal to object", object.equals(cdata));
	}

    /**
	 * Test that a real hashcode is returned and that a different one is returned
	 * for a different CDATA.
	 */
	@Test
	public void test_TCM__int_hashCode() {
        final String text = "this is a CDATA section";
        final CDATA cdata = new CDATA(text);

		//only an exception would be a problem
        int hash = -1;
        try {
           hash = cdata.hashCode();
        }
        catch(final Exception exception) {
            fail("bad hashCode");
        }

        // same text but created out of parts to avoid object re-usage
		final CDATA similarCDATA = new CDATA("this"+ " is" +" a" + " CDATA" + " section");

        //different CDATA sections, same text
		final int similarHash = similarCDATA.hashCode();
		assertTrue("Different comments with same value have same hashcode", hash != similarHash);
        
		final CDATA otherCDATA = new CDATA("this is another CDATA section");

        //only an exception would be a problem
		int otherHash = otherCDATA.hashCode();
        assertTrue("Different comments have same hashcode", otherHash != hash);
        assertTrue("Different comments have same hashcode", otherHash != similarHash);
	}

    /**
     * Test setting and resetting the text value of this CDATA.
     */
	@Test
    public void test_TCM__orgJdomText_setText_String() {
        // simple text in CDATA section
        final String text = "this is a CDATA section";        
        final CDATA cdata = new CDATA(text);
        assertEquals("incorrect CDATA text", text, cdata.getText());

    	// set it to the empty string
        final String emptyString = "";
    	cdata.setText(emptyString);
        assertEquals("incorrect CDATA text", emptyString, cdata.getText());

        // set it to a another string
        final String otherString = "12345qwerty"; 
        cdata.setText(otherString);
        assertEquals("incorrect CDATA text", otherString, cdata.getText());

        // set it to the null (after it was set to another string so that we 
        // are sure something has changed)
        cdata.setText(null);
        assertEquals("incorrect CDATA text", emptyString, cdata.getText());

    	// the following test check for invalid data and transactional behavior
        // means the content must not be change on exceptions so we set a default

        // set text with some special characters
        final String specialText = "this is CDATA section with special characters as < > & &amp; &lt; &gt; [[ ]] > <![![";
        cdata.setText(specialText);    
        assertEquals("incorrect CDATA text", specialText, cdata.getText());
        
        
        cdata.setText(otherString);    
        try {
            final char c= 0x11;
            final StringBuilder buffer = new StringBuilder("hhhh");
            buffer.setCharAt(2, c);

            cdata.setText(buffer.toString());
            fail("Comment setText didn't catch invalid CDATA string");
        } catch (final IllegalDataException exception) {
            assertEquals("incorrect CDATA text after exception", otherString, cdata.getText());
        }
    
        cdata.setText(text);        
        try {
            final String invalidCDATAString = "some valid <text> with an invlaid CDATA section ending ]]>";

            cdata.setText(invalidCDATAString);
            fail("Comment setText didn't catch invalid CDATA string");
        } catch (final IllegalDataException exception) {
            assertEquals("incorrect CDATA text after exception", text, cdata.getText());
        }    
    }

    /**
     * Test appending text values to this CDATA.
     */
	@Test
    public void test_TCM___append_String() {
        final String emptyString = "";
        final String nullString = null;
        final String text = "this is a CDATA section";        
        final String otherString = "12345qwerty"; 
        final String specialCharactersText = "this is CDATA section with special characters as < > & &amp; &lt; &gt; [[ ]] > <![![";
        final String specialText = "> this is a CDATA section with special characters as ]]";
        final String cdataEndText = "this is aCDATA section with a CDATA end marke ]]> somewhere inside";

        {
            // simple text in CDATA section
            final CDATA cdata = new CDATA(text);
            assertEquals("incorrect CDATA text", text, cdata.getText());
            cdata.append(text);
            assertEquals("incorrect CDATA text", text+text, cdata.getText());
            try {
                cdata.append(cdataEndText);
                fail("failed to detect CDATA end marker");
            } catch (final IllegalDataException exception) {
    			// Do nothing
    		} catch (Exception e) {
    			fail("Unexpected exception " + e.getClass());
            }
        }

        {
            // set it to the empty string
            final CDATA cdata = new CDATA(emptyString);
            assertEquals("incorrect CDATA text", "", cdata.getText());
            cdata.append(emptyString);
            assertEquals("incorrect CDATA text", "", cdata.getText());
            cdata.append(text);
            assertEquals("incorrect CDATA text", text, cdata.getText());
            cdata.append(nullString);
            assertEquals("incorrect CDATA text", text, cdata.getText());
            cdata.append(specialCharactersText);
            assertEquals("incorrect CDATA text", text + specialCharactersText, cdata.getText());
            cdata.append(nullString);
            assertEquals("incorrect CDATA text", text + specialCharactersText, cdata.getText());
            cdata.append(emptyString);
            assertEquals("incorrect CDATA text", text + specialCharactersText, cdata.getText());
            cdata.append(otherString);
            assertEquals("incorrect CDATA text", text + specialCharactersText + otherString, cdata.getText());
            try {
                cdata.append(cdataEndText);
                fail("failed to detect CDATA end marker");
            } catch (final IllegalDataException exception) {
    			// Do nothing
    		} catch (Exception e) {
    			fail("Unexpected exception " + e.getClass());
            }
        }

        {
            // set it to a another string
            final CDATA cdata = new CDATA(otherString);
            assertEquals("incorrect CDATA text", otherString, cdata.getText());
            cdata.append(text);
            assertEquals("incorrect CDATA text", otherString + text, cdata.getText());
            cdata.append(nullString);
            assertEquals("incorrect CDATA text", otherString + text, cdata.getText());
            cdata.append(specialCharactersText);
            assertEquals("incorrect CDATA text", otherString + text + specialCharactersText, cdata.getText());
            cdata.append(nullString);
            assertEquals("incorrect CDATA text", otherString + text + specialCharactersText, cdata.getText());
            cdata.append(emptyString);
            assertEquals("incorrect CDATA text", otherString + text + specialCharactersText, cdata.getText());
            try {
                cdata.append(cdataEndText);
                fail("failed to detect CDATA end marker");
            } catch (final IllegalDataException exception) {
    			// Do nothing
    		} catch (Exception e) {
    			fail("Unexpected exception " + e.getClass());
            }
        }
        
        {
            // set it to the null (after it was set to another string so that we 
            // are sure something has changed)
            final CDATA cdata = new CDATA(nullString);
            assertEquals("incorrect CDATA text", "", cdata.getText());
            cdata.append(specialCharactersText);
            assertEquals("incorrect CDATA text", specialCharactersText, cdata.getText());
            cdata.append(nullString);
            assertEquals("incorrect CDATA text", specialCharactersText, cdata.getText());
            cdata.append(text);
            assertEquals("incorrect CDATA text", specialCharactersText + text, cdata.getText());
            cdata.append(nullString);
            assertEquals("incorrect CDATA text", specialCharactersText + text, cdata.getText());
            cdata.append(emptyString);
            assertEquals("incorrect CDATA text", specialCharactersText + text, cdata.getText());
            cdata.append(otherString);
            assertEquals("incorrect CDATA text", specialCharactersText + text + otherString, cdata.getText());
            try {
                cdata.append(cdataEndText);
                fail("failed to detect CDATA end marker");
            } catch (final IllegalDataException exception) {
    			// Do nothing
    		} catch (Exception e) {
    			fail("Unexpected exception " + e.getClass());
            }
        }

        {
            // set it to the null (after it was set to another string so that we 
            // are sure something has changed)
            final CDATA cdata = new CDATA(null);
            assertEquals("incorrect CDATA text", "", cdata.getText());
            cdata.append((String) null);
            assertEquals("incorrect CDATA text", "", cdata.getText());
            try {
                cdata.append(cdataEndText);
                fail("failed to detect CDATA end marker");
            } catch (final IllegalDataException exception) {
    			// Do nothing
    		} catch (Exception e) {
    			fail("Unexpected exception " + e.getClass());
            }
        }

        {
            // set it to the null (after it was set to another string so that we 
            // are sure something has changed)
            final CDATA cdata = new CDATA(null);
            assertEquals("incorrect CDATA text", "", cdata.getText());
            cdata.append((Text) null);
            assertEquals("incorrect CDATA text", "", cdata.getText());
            try {
                cdata.append(cdataEndText);
                fail("failed to detect CDATA end marker");
            } catch (final IllegalDataException exception) {
    			// Do nothing
    		} catch (Exception e) {
    			fail("Unexpected exception " + e.getClass());
            }
        }

        {
            // set text with some special characters
            final CDATA cdata = new CDATA(specialCharactersText);
            assertEquals("incorrect CDATA text", specialCharactersText, cdata.getText());
            cdata.append(specialCharactersText);
            assertEquals("incorrect CDATA text", specialCharactersText + specialCharactersText, cdata.getText());
            try {
                cdata.append(cdataEndText);
                fail("failed to detect CDATA end marker");
            } catch (final IllegalDataException exception) {
    			// Do nothing
    		} catch (Exception e) {
    			fail("Unexpected exception " + e.getClass());
            }
        }
        
        
        try {
            // set text with some special characters which should result into an exception
            final CDATA cdata = new CDATA(specialText);
            assertEquals("incorrect CDATA text", specialText, cdata.getText());
            cdata.append(specialText);
            
            fail("failed to detect CDATA end marker");
        } catch (final IllegalDataException exception) {
			// Do nothing
		} catch (Exception e) {
			fail("Unexpected exception " + e.getClass());
        }
    }

    /**
     * Test appending text values to this CDATA.
     */
	@Test
    public void test_TCM___append_Text() {
        final String emptyString = "";
        final String nullString = null;
        final String text = "this is a CDATA section";        
        final String otherString = "12345qwerty"; 
        final String specialCharactersText = "this is CDATA section with special characters as < > & &amp; &lt; &gt; [[ ]] > <![![";
        final String specialText = "> this is a CDATA section with special characters as ]]";
        final String cdataEndText = "this is aCDATA section with a CDATA end marke ]]> somewhere inside";

        {
            // simple text in CDATA section
            final CDATA cdata = new CDATA(text);
            assertEquals("incorrect CDATA text", text, cdata.getText());
            cdata.append(new Text(text));
            assertEquals("incorrect CDATA text", text+text, cdata.getText());
            try {
                cdata.append(new Text(cdataEndText));
                fail("failed to detect CDATA end marker");
            } catch (final IllegalDataException exception) {
    			// Do nothing
    		} catch (Exception e) {
    			fail("Unexpected exception " + e.getClass());
            }
        }

        {
            // set it to the empty string
            final CDATA cdata = new CDATA(emptyString);
            assertEquals("incorrect CDATA text", "", cdata.getText());
            cdata.append(new Text(emptyString));
            assertEquals("incorrect CDATA text", "", cdata.getText());
            cdata.append(new Text(text));
            assertEquals("incorrect CDATA text", text, cdata.getText());
            cdata.append(new Text(nullString));
            assertEquals("incorrect CDATA text", text, cdata.getText());
            cdata.append(new Text(specialCharactersText));
            assertEquals("incorrect CDATA text", text + specialCharactersText, cdata.getText());
            cdata.append(new Text(nullString));
            assertEquals("incorrect CDATA text", text + specialCharactersText, cdata.getText());
            cdata.append(new Text(emptyString));
            assertEquals("incorrect CDATA text", text + specialCharactersText, cdata.getText());
            cdata.append(new Text(otherString));
            assertEquals("incorrect CDATA text", text + specialCharactersText + otherString, cdata.getText());
            try {
                cdata.append(new Text(cdataEndText));
                fail("failed to detect CDATA end marker");
            } catch (final IllegalDataException exception) {
    			// Do nothing
    		} catch (Exception e) {
    			fail("Unexpected exception " + e.getClass());
            }
        }

        {
            // set it to a another string
            final CDATA cdata = new CDATA(otherString);
            assertEquals("incorrect CDATA text", otherString, cdata.getText());
            cdata.append(new Text(text));
            assertEquals("incorrect CDATA text", otherString + text, cdata.getText());
            cdata.append(new Text(nullString));
            assertEquals("incorrect CDATA text", otherString + text, cdata.getText());
            cdata.append(new Text(specialCharactersText));
            assertEquals("incorrect CDATA text", otherString + text + specialCharactersText, cdata.getText());
            cdata.append(new Text(nullString));
            assertEquals("incorrect CDATA text", otherString + text + specialCharactersText, cdata.getText());
            cdata.append(new Text(emptyString));
            assertEquals("incorrect CDATA text", otherString + text + specialCharactersText, cdata.getText());
            try {
                cdata.append(new Text(cdataEndText));
                fail("failed to detect CDATA end marker");
            } catch (final IllegalDataException exception) {
    			// Do nothing
    		} catch (Exception e) {
    			fail("Unexpected exception " + e.getClass());
            }
        }
        
        {
            // set it to the null (after it was set to another string so that we 
            // are sure something has changed)
            final CDATA cdata = new CDATA(nullString);
            assertEquals("incorrect CDATA text", "", cdata.getText());
            cdata.append(new Text(specialCharactersText));
            assertEquals("incorrect CDATA text", specialCharactersText, cdata.getText());
            cdata.append(new Text(nullString));
            assertEquals("incorrect CDATA text", specialCharactersText, cdata.getText());
            cdata.append(new Text(text));
            assertEquals("incorrect CDATA text", specialCharactersText + text, cdata.getText());
            cdata.append(new Text(nullString));
            assertEquals("incorrect CDATA text", specialCharactersText + text, cdata.getText());
            cdata.append(new Text(emptyString));
            assertEquals("incorrect CDATA text", specialCharactersText + text, cdata.getText());
            cdata.append(new Text(otherString));
            assertEquals("incorrect CDATA text", specialCharactersText + text + otherString, cdata.getText());
            try {
                cdata.append(new Text(cdataEndText));
                fail("failed to detect CDATA end marker");
            } catch (final IllegalDataException exception) {
    			// Do nothing
    		} catch (Exception e) {
    			fail("Unexpected exception " + e.getClass());
            }
        }

        {
            // set it to the null (after it was set to another string so that we 
            // are sure comething has changed)
            final CDATA cdata = new CDATA(null);
            assertEquals("incorrect CDATA text", "", cdata.getText());
            cdata.append((String) null);
            assertEquals("incorrect CDATA text", "", cdata.getText());
            try {
                cdata.append(new Text(cdataEndText));
                fail("failed to detect CDATA end marker");
            } catch (final IllegalDataException exception) {
    			// Do nothing
    		} catch (Exception e) {
    			fail("Unexpected exception " + e.getClass());
            }
        }

        {
            // set it to the null (after it was set to another string so that we 
            // are sure something has changed)
            final CDATA cdata = new CDATA(null);
            assertEquals("incorrect CDATA text", "", cdata.getText());
            cdata.append((Text) null);
            assertEquals("incorrect CDATA text", "", cdata.getText());
            try {
                cdata.append(new Text(cdataEndText));
                fail("failed to detect CDATA end marker");
            } catch (final IllegalDataException exception) {
    			// Do nothing
    		} catch (Exception e) {
    			fail("Unexpected exception " + e.getClass());
            }
        }

        {
            // set text with some special characters
            final CDATA cdata = new CDATA(specialCharactersText);
            assertEquals("incorrect CDATA text", specialCharactersText, cdata.getText());
            cdata.append(new Text(specialCharactersText));
            assertEquals("incorrect CDATA text", specialCharactersText + specialCharactersText, cdata.getText());
            try {
                cdata.append(new Text(cdataEndText));
                fail("failed to detect CDATA end marker");
            } catch (final IllegalDataException exception) {
    			// Do nothing
    		} catch (Exception e) {
    			fail("Unexpected exception " + e.getClass());
            }
        }
        
        
        try {
            // set text with some special characters which sould result into an exception
            final CDATA cdata = new CDATA(specialText);
            assertEquals("incorrect CDATA text", specialText, cdata.getText());
            cdata.append(new Text(specialText));
            
            fail("failed to detect CDATA end marker");
        } catch (final IllegalDataException exception) {
			// Do nothing
		} catch (Exception e) {
			fail("Unexpected exception " + e.getClass());
        }
    }

	/**
	 * Verify that the text of the CDATA matches expected value.
     * It assumes that the constructor is working correctly
	 */
	@Test
	public void test_TCM__String_getText() {
        {
            final String text = "this is a CDATA section";        
    
            final CDATA cdata = new CDATA(text);
            assertEquals("incorrect CDATA text", text, cdata.getText());
        }

        {
            //set it to the empty string
            final String emptyString = "";
            final CDATA cdata = new CDATA(emptyString);
            assertEquals("incorrect CDATA text", emptyString, cdata.getText());
        }

        {
            // set it to a another string
            final String otherString = "12345qwerty"; 
            final CDATA cdata = new CDATA(otherString);
            assertEquals("incorrect CDATA text", otherString, cdata.getText());
        }

        {
            // set it to the null
            final CDATA cdata = new CDATA(null);
            assertEquals("incorrect CDATA text", "", cdata.getText());
        }
	}

    /**
     * check for the expected toString text value of Comment.
     */
	@Test
	public void test_TCM__String_toString() {
        {
            final String text = "this is a simple CDATA section";        
            final CDATA cdata = new CDATA(text);

            assertEquals(
                    "incorrect CDATA constructed", 
                    "[CDATA: " + text + ']', 
                    cdata.toString()); 
        }
    
        {
            final String text = "this is CDATA section with special characters as < > & &amp; &lt; &gt; [[ ]] > <![![";        
            final CDATA cdata = new CDATA(text);

            assertEquals(
                    "incorrect CDATA constructed", 
                    "[CDATA: " + text + ']', 
                    cdata.toString()); 
        }
    
    }
	
    @Test
    public void testCloneDetatchParentCDATA() {
		Element parent = new Element("root");
		CDATA content = new CDATA("val");
		parent.addContent(content);
		CDATA clone = content.detach().clone();
		assertEquals(content.getValue(), clone.getValue());
		assertNull(content.getParent());
		assertNull(clone.getParent());
	}

    @Test
    public void testContentCType() {
    	assertTrue(Content.CType.CDATA == new CDATA("").getCType());
    }
}
