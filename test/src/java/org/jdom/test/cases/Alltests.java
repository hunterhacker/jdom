package org.jdom.test.cases;


/**
 * Run all unit tests for JDOM
 * 
 * @author: Philip Nelson
 */
import com.nelco.filetaxes.*;
import junit.framework.*;

public class Alltests extends junit.framework.TestCase {
/**
 * TestFiletaxesAlltests constructor comment.
 * @param arg1 java.lang.String
 */
public Alltests(String arg1) {
	super(arg1);
}
/**
 * Insert the method's description here.
 * Creation date: (8/22/00 1:13:57 PM)
 * @param args java.lang.String[]
 */
public static void main(String[] args) {
	
	junit.textui.TestRunner.run(suite());
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
		return suite;
}
}
