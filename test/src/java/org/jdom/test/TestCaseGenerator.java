/*--

 Copyright 2000 Brett McLaughlin & Jason Hunter. All rights reserved.

 Redistribution and use in source and binary forms, with or without modifica-
 tion, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice,
    this list of conditions, and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions, the disclaimer that follows these conditions,
    and/or other materials provided with the distribution.

 3. The names "JDOM" and "Java Document Object Model" must not be used to
    endorse or promote products derived from this software without prior
    written permission. For written permission, please contact
    license@jdom.org.

 4. Products derived from this software may not be called "JDOM", nor may
    "JDOM" appear in their name, without prior written permission from the
    JDOM Project Management (pm@jdom.org).

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 JDOM PROJECT  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLUDING, BUT
 NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 This software  consists of voluntary contributions made  by many individuals
 on  behalf of the Java Document Object Model Project and was originally
 created by Brett McLaughlin <brett@jdom.org> and
 Jason Hunter <jhunter@jdom.org>. For more  information on the JDOM
 Project, please see <http://www.jdom.org/>.

 */

package org.jdom.test;

import java.io.File;
import java.io.IOException;

import java.lang.reflect.Method;
import java.lang.reflect.Constructor;

/**
 * <p><code>TestCaseGenerator</code> generates JUnit test cases for all
 * the classes within the core JDOM distribution.
 *
 * 
 * @author Jools Enticknap
 * @version	1.00 
 */
public final class TestCaseGenerator {

	/** The base directory for all the test case files to be generated */
	private String baseDir;

	public TestCaseGenerator() {
		this( "./" );
	}

	/**
	 * Create an instance of the TestCaseGenerator, specifying the base 
	 * directory for the Java testcase files.
	 *
	 * @param baseDir	The base directory for the Java files.
	 */
	public TestCaseGenerator( String baseDir ) {
		this.baseDir=baseDir;
	}
	
	/**
	 * Generate a TestCase file for the given Class.
	 *
	 * @param cls		The class to generate a TestCase for.
	 * @param owrite	Overwrite the existing files.
	 */
	public void generate( String className ) throws IOException {
		Class cls = null;
		try {
			cls = Class.forName(className);
		}
		catch( Exception e ) {
			System.out.println( "Sorry but I could'nt load class("+
								className +
								") check your classpath");
			System.out.println( "The Exception was:"+e);
			return;
		}


		// Mangle the class name.
		String mangledClassName = NameMangler.getMangledName( cls );

		// Get the method names.
		Method[] methods = cls.getMethods();
		Constructor[] constructors = cls.getConstructors();
		
		String[] mangled = new String[ methods.length +
									 constructors.length ];

		// Now do a bit of mangling.
		for ( int i=0; i <methods.length; i++ ) {
			mangled[i] = NameMangler.getMangledName( methods[i] );
		}
		
		for ( int i=methods.length; i < mangled.length; i++ ) {
			mangled[i] = NameMangler.getMangledName( 
											constructors[i-methods.length] );
		}
		
		File file = new File( baseDir+mangledClassName+".java" );
		ClassGenerator.generate( file, mangledClassName, 
										"org.jdom.test.cases", mangled );
	}

	public static void main( String[] args ) throws IOException {
		TestCaseGenerator tcg = new TestCaseGenerator();
		for( int i = 0; i<ClassNames.classNames.length; i++ ) {
			tcg.generate( ClassNames.classNames[i] );		
		}
	}
}

