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

import java.lang.reflect.Method;
import java.lang.reflect.Constructor;

import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;


/**
 * <code>ClassGenerator</code> produces the java source code for the test
 * cases.
 *
 * @author  Jools Enticknap
 * @version 1.00
 */
final class ClassGenerator {

	/** Constructor prefix 'TestCaseConstructor' */
	private static String ctorPrefix   = "TCC_";

	/** Method prefix 'TestCaseMethod' */
	private static String methodPrefix = "TCM_";

	/** File object pointing to the directory in which to place the files */
	private File path;

	/** File object pointing to the outputFile in which to place methods */
	private File outputFile;

	/** The package name of the class */
	private String packageName;

	/** The name of the class minus the package name */
	private String className;

	private TestCaseClassWriter tsWriter;
	
	/** Ignore methods already in a test case if we are appending*/
	private static Method[] ignoreTests = null;
	
	/** Reference to the class to be appended to */
	private Class appendCls = null;

	/**
	 * Indicates whether or not the output class's closing
	 * brace is there or not.  If not the file is appendable
	 * and will need to have the closing brace added.
	 */

	private boolean outputClassIsAppendable = false;
	/**
	 * Create a ClassGenerator which will output it's files into 'path'
	 * and the class name will be denoted as 'className', and placed
	 * in the package 'packageName'.
	 *
	 * @param path        The location for the output files.
	 * @param className   The name of the class.
	 * @param packageName The name of the package this class lives in.
	 */
	ClassGenerator(File path, String className, String packageName) {
		setPath(path);
		setClassName(className);
		setPackageName(packageName);
	}
	/**
	 * Create a ClassGenerator which will output it's files into 'path'
	 * and the class name will be denoted as 'className', and placed
	 * in the package 'packageName'.
	 *
	 * @param path        The location for the output files.
	 * @param className   The name of the class.
	 * @param packageName The name of the package this class lives in.
	 * @param outputClassFile The name of the file to put test methods in.
	 */
	ClassGenerator(File path, String className, String packageName, String outputClassFile) 
	throws GeneratorException {
		this(path, className, packageName);
		outputFile = new File(path, outputClassFile);

		//Attempt to load the test class so as to not regenerate existing methods
		int dotLocation = outputClassFile.lastIndexOf(".");
		String outputClass = outputClassFile.substring(0, dotLocation);

		
		loadTestClass(outputClass);
		if (appendCls != null)
			ignoreTests = appendCls.getMethods();
	}
/**
 * 
 *
 * @param method    The constructor for which the test case will be
 *                  generated.
 * @throws GeneratorException 
 */
void append(Constructor ctor) throws GeneratorException, IOException {

	// The name for an appended method is derived as follows;
	//
	// test_TCM_<mangled method name>
	//
	StringBuffer buffer = new StringBuffer("test_");
	buffer.append(ctorPrefix);
	buffer.append("_");
	NameMangler.getMangledName(ctor, buffer);

	// The fully mangled test  name.
	String clazz = buffer.toString();

	append(clazz);
	


}
/**
 * 
 *
 * @param method    The method for which the test case will be
 *                  generated.
 * @throws GeneratorExceptio 
 */
void append(Method method) throws GeneratorException, IOException {

	// The name for an appended method is derived as follows;
	//
	// test_TCM_<mangled method name>
	//
	StringBuffer buffer = new StringBuffer("test_");
	buffer.append(methodPrefix);
	buffer.append("_");
	NameMangler.getMangledName(method, buffer);

	// The fully mangled test method name.
	String clazz = buffer.toString();
	append(clazz);

}
/**
 * 
 *
 * @param testName  The method for which the test case will be
 *                  generated.
 * @throws GeneratorExceptio 
 */
void append(String testName) throws GeneratorException, IOException {

	if (isRequired(testName)) {

	int dotLocation = outputFile.getName().indexOf(".");
	String outputClass = outputFile.getName().substring(0, dotLocation);

	// Check that the files does  exist.
	// If it does not, create a new appendable file.
	try {
		if (!outputFile.exists()) {

			tsWriter = new TestCaseClassWriter(outputFile, outputClass, packageName, "");
			tsWriter.generateAppendable();
			tsWriter.addTest(testName);
			outputClassIsAppendable = true;
		} else if (!outputClassIsAppendable ) {
			//find the end of the file, back up before the closing brace and remove it

			makeAppendable();
			
			if (tsWriter == null) {
				tsWriter = new TestCaseClassWriter(outputFile, true, outputClass, packageName, null, "");
			}
			tsWriter.addTest(testName);
			
		} else {
			tsWriter.addTest(testName);
		}
	} catch (Exception e) {
		throw new GeneratorException(e.getMessage());
	}
	}

}
/**
 * Close the open Writer thus inserting the closing brace
 *
 */
public void closeAppendable() {
	
	if (tsWriter != null)
		tsWriter.close();
	outputClassIsAppendable = false;
}
	/**
	 * Generate a test case for a Constructor.
	 *
	 * @param constructor    The constructor for which the test case will be
	 *                      generated.
	 */
	void generate(Constructor ctor) throws GeneratorException {
		StringBuffer buffer = new StringBuffer(ctorPrefix);
		
		buffer.append(className);
		buffer.append("_");

		String outputClass = NameMangler.getMangledName(ctor,buffer).toString();
		
		String fileName = buffer.append(".java")
						  .toString();


		
		// Check that the files does __not__ exist.
		// If it does cowardly throw an exception.
		File f = new File(path, fileName);
		if (f.exists()) {
			throw new GeneratorException(new StringBuffer("File (")
			                                 .append(fileName)
											 .append(") exists ")
											 .toString());
		}

		try {
			writeClass(f, outputClass, ctor.toString());
		} catch( IOException ioe ) {
			throw new GeneratorException(ioe.getMessage());
		}
	}
	/**
	 * 
	 *
	 * @param method    The method for which the test case will be
	 *                  generated.
	 * @throws GeneratorExceptio 
	 */
	void generate(Method method) throws GeneratorException {
		
		// The name for a class is derived as follows;
		//
		// TCM_<class>_<mangled method name>
		//
		StringBuffer buffer = new StringBuffer(methodPrefix);
		buffer.append(className);
		buffer.append("_");
		
		NameMangler.getMangledName(method, buffer);
		
		// The fully mangled class name.
		String clazz = buffer.toString();

		String fileName = buffer.append(".java")
						  .toString();
		
		// Check that the files does __not__ exist.
		// If it does cowardly throw an exception.
		File f = new File(path, fileName);
		if (f.exists()) {
			throw new GeneratorException(new StringBuffer("File (")
			                                 .append(fileName)
											 .append(") exists ")
											 .toString());
		}

		// Create a new file and generated the source code.
		try {
			writeClass(f, clazz, method.toString());
		} catch(IOException ioe) {
			throw new GeneratorException(ioe.getMessage());
		}
	}
	/**
	 * Obtain the name of the target class.
	 *
	 * @return The name of the target class.
	 */
	String getClassName() {
		return className;
	}
	/**
	 * Obtain the name of the package this class resides in.
	 *
	 * @return The package this class resides in.
	 */
	String getPackageName() {
		return packageName;
	}
	/**
	 * Obtain the root path where files generated files will be placed.
	 *
	 * @return The root location for generated files.
	 */
	File getPath() {
		return path;
	}
	/**
	 * Check to see if the named method is already present.
	 *
	 * @param method    The method to check.
	 */
	boolean isRequired(Method method) {

		for(int i=0; i<ignoreTests.length; i++) {
			if(ignoreTests[i].equals(method)) {
				return false;
			}
		}

		return true;
	}
	/**
	 * Check to see if the named method is already present.
	 *
	 * @param method    The method to check.
	 */
	boolean isRequired(String testMethod) {
		
		//if null there wasn't any tests in the class or there was no class
		if (ignoreTests == null)
			return true;
			
		for(int i=0; i<ignoreTests.length; i++) {
			if(ignoreTests[i].getName().equals(testMethod)) {
				return false;
			}
		}

		return true;
	}
	/**
	 * Load a Test class if we are appending.
	 *
	 * @throws GeneratorException If there was an error during creation.
	 */
	void loadTestClass(String testClass) throws GeneratorException {


		StringBuffer buffer = new StringBuffer(packageName);
		buffer.append(".");
		buffer.append(testClass);

		String name = buffer.toString();
		// Now load the class.
		try {
			appendCls = Class.forName(name);
		} catch(Exception e ) {
			//the class is not in the classpath so just ignore it
			appendCls = null;
		}

	}
/**
 * 
 * 
 */
public void makeAppendable() throws IOException, GeneratorException {
	java.io.RandomAccessFile rand = new java.io.RandomAccessFile(outputFile, "rw");

	rand.seek(rand.length());
	long filePointer = rand.getFilePointer();
	boolean found = false;
	long foundPos = 0;
	while (!found) {
		filePointer = filePointer - 1;
		foundPos = filePointer;
		rand.seek(filePointer);
		byte aByte = rand.readByte();
		if ((char) aByte == '}') {
			found = true;
			break;
		}

		//give up if the closing brace isn't found within 200 chars
		if (rand.length() - filePointer > 200)
			throw new GeneratorException("Invalid class file: " + outputFile.getAbsolutePath());
	}
	rand.seek(foundPos);
	rand.writeByte(' ');
	rand.close();
	outputClassIsAppendable = true;

}
	/**
	 * Set the name of the target class.
	 *
	 * @param className The name of the target class.
	 */
	void setClassName(String className) {
		this.className = className;
	}
	/**
	 * Set the name of the package this class will reside in.
	 *
	 * @param packageName The name of the target class.
	 */
	void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	/**
	 * Set the root path for class files to be generated in.
	 *
	 * @param file The directory where the class files will be generated.
	 */
	void setPath(File path) {
		this.path = path;
	}
	/**
	 * Write the test case out to disk.
	 *
	 * @param f				The Newly created file.
	 * @param clazz			The name of the class to generate.
	 * @param msg			The test case message.
	 * @throws IOException 	Throw if a IO error occurs when writing.
	 */
	void writeClass(File f, String clazz, String msg) throws IOException {
		TestCaseClassWriter out = 
			new TestCaseClassWriter(f, clazz, packageName, msg);
		out.generate();
		out.close();
	}
}
