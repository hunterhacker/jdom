package org.jdom.test.cases;

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
import java.io.*;
import junit.framework.*;

import org.jdom.*;
import org.jdom.filter.ElementFilter;

public final class TestSerialization
extends junit.framework.TestCase
{
    /**
     *  Construct a new instance. 
     */
    public TestSerialization(String name) {
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
        // your code goes here.
    }
    /**
     * The suite method runs all the tests
     */
	public static Test suite () {
        TestSuite suite = new TestSuite(TestSerialization.class);
        return suite;
    }
    /**
     * This method is called after a test is executed.
     */
    public void tearDown() {
        // your code goes here.
    }

    private ByteArrayOutputStream serialize(Object o) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oout = new ObjectOutputStream(baos);
            oout.writeObject(o);
            return baos;
        }
        catch (Exception e) {
            fail("Exception on serializing: " + e);
            return null;
        }
    }

    private Object deserialize(ByteArrayOutputStream baos) {
        try {
            ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
            return oin.readObject();
        }
        catch (Exception e) {
            fail("Exception on deserializing: " + e);
            return null;
        }
    }

    private void outAndBack(ElementFilter filter) {
        ByteArrayOutputStream bytes = serialize(filter);
        ElementFilter filter2 = (ElementFilter) deserialize(bytes);
        assertTrue(filter.equals(filter2));
    }

    public void test_ElementFilterName() {
        outAndBack(new ElementFilter("name"));
    }

    public void test_ElementFilterNameNamespace() {
        outAndBack(new ElementFilter("name", Namespace.XML_NAMESPACE));
    }

    public void test_ElementFilterNamespace() {
        outAndBack(new ElementFilter(Namespace.XML_NAMESPACE));
    }

    public void test_ElementFilterEmpty() {
        outAndBack(new ElementFilter());
    }
}
