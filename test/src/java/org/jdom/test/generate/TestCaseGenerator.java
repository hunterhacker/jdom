package org.jdom.test.generate;

/*-- 

 Copyright (C) 2000 Brett McLaughlin & Jason Hunter. All rights reserved.
 
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

import java.util.Properties;

import java.io.File;
import java.io.IOException;

import java.lang.reflect.Method;
import java.lang.reflect.Constructor;

/**
 * Central point for creating TestCases.
 *
 * @author Julian David Enticknap
 * @version    1.00
 */
final class TestCaseGenerator {

	/** Don't generate test cases for methods in java.lang.Object */
	private static final Method[] ignore = Object.class.getMethods();


	/** Only generate test cases for classes in this package.*/
	private  String packageName = "org.jdom";

	/** This string denotes the root directory for test cases.*/
	private  String caseDir = "/src/java/org/jdom/test/cases/";

	/** The name of the test case package */
	private String casePackage = "org.jdom.test.cases";

	/** Reference to the properties used to create the test cases */
	private Properties props;    

	/** Reference to the class to be parsed */
	private Class cls;


	/** A String representing the class name minus the package */
	private String nameMinusPackage;

	/** The package name for the class */
	private String classPackageName;
	

	/**
	 * Create a new instance of the generator using the supplied properties.
	 *
	 * @param props    Properties defining how the test case should be created.
	 */
	TestCaseGenerator(Properties props) {
		this.props = props;
	}
/**
* Generate the test cases source file.
*
* @throws GeneratorException If there was an error during creation.
*/
void generate() throws GeneratorException {
	// Load the class for parsing.
	loadClass();
	//check to see if there was an outputFile to add the tests to

	if (props.getProperty("outputFile") == null) {
		// Now generate the file.
		generateTestCases(getOutputDir());
	} else {
		try {
			generateTestCases(getOutputDir(), props.getProperty("outputFile"));
		} catch (IOException e) {
			throw new GeneratorException(e.getMessage());
		}
	}
}
	/**
	 * Generate the test cases source file.
	 *
	 * @param outDir The directory to place the test cases.
	 * @throws GeneratorException If there was an error during creation.
	 */
	void generateTestCases(File outDir) throws GeneratorException {

		// Will generate the TestCase classes into the supplied directory.
		ClassGenerator clsGen = new ClassGenerator(outDir, 
												   nameMinusPackage,
												   classPackageName);

		// Generate the classes for the public constructors.
		Constructor[] ctors = cls.getConstructors();
		for(int i=0;i <ctors.length; i++) {
			clsGen.generate(ctors[i]);    
		}

		// Generate the classes for the public methods.
		Method[] methods = cls.getMethods();
		for(int i=0; i<methods.length; i++) {
			if (isRequired(methods[i])) {
				clsGen.generate(methods[i]);    
			}
		}
	}
	/**
	 * Generate the test cases source file.
	 *
	 * @param outDir The directory to place the test cases.
	 * @throws GeneratorException If there was an error during creation.
	 */
	void generateTestCases(File outDir, String outputClassFile) throws GeneratorException, IOException {

		// Will generate the TestCase classes into the supplied directory.
		ClassGenerator clsGen = new ClassGenerator(outDir, 
												   nameMinusPackage,
												   classPackageName,
												   outputClassFile);


		
		// Generate the classes for the public constructors.
		Constructor[] ctors = cls.getConstructors();
		for(int i=0;i <ctors.length; i++) {
			clsGen.append(ctors[i]);
		}

		// Generate the classes for the public methods.
		Method[] methods = cls.getMethods();
		for(int i=0; i<methods.length; i++) {
			if (isRequired(methods[i])) {
				clsGen.append(methods[i]);    
			}
		}
		clsGen.closeAppendable();
	}
	/**
	 * Get the directory where the output files will go.
	 *
	 * @throws GeneratorException If there was an error during creation.
	 */
	File getOutputDir() throws GeneratorException {
		String rootDir = props.getProperty("rootdir");

		File root = new File(rootDir);
		if (!root.isDirectory()) {
			throw new GeneratorException(rootDir+" is not a directory.");
		}
		
		// Check that the src/java/org/jdom/test/cases dir exists.
		try {
			String absPath = root.getCanonicalPath();
			absPath+=caseDir;
			
			// Make sure that the cases directory exists first.
			File f = new File(absPath);
			if (!f.exists()) {
				String msg = new StringBuffer(absPath)
								 .append(" Does not exist.")
								 .toString();
				throw new GeneratorException(msg);
			}

			
			// Now see if there is a directory for the current class.
			// This is basically the class minus the org.jdom prefix.
			// All the core classes will end up in the root of the cases
			// directory.
			String s = cls.getName().substring(packageName.length()+1);

			// Create the name of the test package.
			classPackageName = casePackage;
			
			// Any sub-packages ? add them to the path.
			int j = 0;
			int i = s.indexOf('.');
			while(i>0) {
				String str = s.substring(j, i);
				classPackageName += "."+str;
				absPath += str+"/";
				j = i+1;
				i = s.indexOf('.',j);
			}

			System.out.println(classPackageName);
			
			// Does this directory exists ?
			f = new File(absPath);
			if (!f.exists()) {
				if (!f.mkdirs()) {
					String msg = new StringBuffer(absPath)
								 .append(" could no be created.")
								 .toString();
					throw new GeneratorException(msg);
				}
			}

			// Set the new root directory.
			root = new File(absPath);

		} catch(IOException ioe) {
			throw new GeneratorException(ioe.getMessage());
		}
		
		return root;
	}
	/**
	 * Check to see if the named method is already present.
	 *
	 * @param method    The method to check.
	 */
	boolean isRequired(Method method) {
		for(int i=0; i<ignore.length; i++) {
			if(ignore[i].equals(method)) {
				return false;
			}
		}

		return true;
	}
	/**
	 * Load a class.
	 *
	 * @throws GeneratorException If there was an error during creation.
	 */
	void loadClass() throws GeneratorException {
		String name = props.getProperty("source");

		// Make sure that the prefix is 'org.jdom'
		if (!name.startsWith(packageName)) {
			String msg = new StringBuffer("Failed to create test case")
							 .append("because ")
							 .append(name)
							 .append(" is not in the package ")
							 .append(packageName)
							 .toString();

			throw new GeneratorException(msg);
		}

		// Now load the class.
		try {
			cls = Class.forName(name);
		} catch(Exception e ) {
			StringBuffer msg = new StringBuffer("Failed to load class,");
			msg.append("name=")
			   .append(name)
			   .append(",Exception class=")
			   .append(e.getClass().getName())
			   .append(",Message=")
			   .append(e.getMessage());
			throw new GeneratorException(msg.toString());
		}

		int i = name.lastIndexOf(".");
		nameMinusPackage = name.substring(i+1);
	}
}
