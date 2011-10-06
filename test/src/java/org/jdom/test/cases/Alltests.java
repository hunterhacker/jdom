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


import junit.framework.*;
import org.jdom.test.cases.input.*;


public class Alltests extends junit.framework.TestCase {
/**
 * TestFiletaxesAlltests constructor comment.
 * @param arg1 java.lang.String
 */
public Alltests(String arg1) {
	super(arg1);
}
/**
 * Run the all tests method
 * 
 * @param args java.lang.String[]
 */
public static void main(String[] args) {
	if (args.length > 0 && args[0] != null && args[0].equals("-ui") ) {
		String newargs[] = {"org.jdom.test.cases.Alltests"};
		junit.swingui.TestRunner.main(newargs);
	} else {
		
		junit.textui.TestRunner.run(suite());
	}
}
/**
 * The suite method kicks off all of the tests
 * 
 * @return junit.framework.Test
 */
public static Test suite() {
		TestSuite suite= new TestSuite();

		suite.addTest(TestComment.suite());
		suite.addTest(TestVerifier.suite());
		suite.addTest(TestAttribute.suite());
		suite.addTest(TestNamespace.suite());
		suite.addTest(TestDocType.suite());
		suite.addTest(TestElement.suite());
		suite.addTest(TestDocument.suite());
		suite.addTest(TestFilterList.suite());
		suite.addTest(TestSAXBuilder.suite());
		suite.addTest(TestSAXHandler.suite());
		suite.addTest(TestSAXComplexSchema.suite());
		return suite;
}
}
