package org.jdom.test.generate;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;


public class TestCaseClassWriter extends IndentWriter {

		/** The test case message */
		private String msg;

		/** The name of the class */
		private String className;

		/** The package in which the test will reside */
		private String packageName;
		


		
		public java.lang.String sourcePackage;
		/**
		 * Create a new TestCaseClassWriter.
		 *
		 * @param f           The file to write to.
		 * @param className   The name of the class.
		 * @param packageName The package in which the file will reside.
		 * @param msg         The Test case message.
		 */
		TestCaseClassWriter(File f, 
							String className, String packageName, String msg) 
		throws IOException 
		{
			super(f);
			this.className = className;
			this.packageName = packageName;
			this.msg = msg;
		}
		/**
		 * Create a new TestCaseClassWriter.
		 *
		 * @param f           The file to write to.
		 * @param append      boolean indicating whether or not to append
		 * @param className   The name of the class.
		 * @param packageName The package in which the file will reside.
		 * @param sourcePackage The package in which the source file resides.
		 * @param msg         The Test case message.
		 */
		TestCaseClassWriter(File f, boolean append,
							String className, 
							String packageName,
							String sourcePackage, 
							String msg) 
		throws IOException 
		{
			super(f, append);
			this.className = className;
			this.packageName = packageName;
			this.sourcePackage = sourcePackage;
			this.msg = msg;
		}
		public void addComment(String comment) {
			println(" * " + comment);
		}
/**
 * add this test to the class
 * 
 * @param testName name of the test to add
 */
public void addTest(String testName) {
	
				writeMethod(testName,
			            "Test code goes here. Replace this comment.",
						"fail(\"implement me !\");");
}
		/**
		 * Write out the class definition.
		 */
		private void classDef() {
			startComment();
			addComment("Please put a description of your test here.");
			addComment("");
			addComment("@author unascribed");
			addComment("@version 0.1");
			endComment();
			println("import junit.framework.*;");
			if (sourcePackage != null)
				println("import " + sourcePackage + ".*;");
			println();
			println(new StringBuffer("public final class ")
					.append(className)
					.toString());

			println("extends junit.framework.TestCase");
			println("{");

		}
		public void close() {
			reset();
			println("}");
			super.close();
		}
		private void copyrightComment() {
			println("/* Please run replic.pl on me ! */");
		}
		public void endComment() {
			println(" */");
		}
		/**
		 * Generate the class.
		 */
		void generate() {
			copyrightComment();
			packageDef();

			classDef();
			writeCtor();
			writeMethod("setUp", 
			            "This method is called before a test is executed.");
			writeMethod("tearDown",
						"This method is called after a test is executed.");
			writeMethod("test",
			            "Test code goes here. Replace this comment.",
						"fail(\"implement me !\");");
		}
		/**
		 * Generate the class so that methods can be appended.
		 */
		
		void generateAppendable() {

			
			copyrightComment();
			packageDef();

			classDef();
			writeCtor();
			writeMethod("setUp", 
			            "This method is called before a test is executed.");
			writeMethod("tearDown",
						"This method is called after a test is executed.");

			writeSuiteMethod();
			writeMainMethod();

		}
		/**
		 * Package must be the first thing in the file.
		 */
		private void packageDef() {
			println("package "+packageName+";");
			println();
		}
		public void startComment() {
			println("/**");
		}
		/**
		 * Write out the constructor
		 */
		void writeCtor() {
			incr();
			startComment();
			addComment(" Construct a new instance. ");
			endComment();

			println(new StringBuffer("public ")
						.append(className)
						.append("(String name) {")
						.toString());
			incr();
			println("super(name);");
			decr();
			println("}");
			println();
			reset();
		}
		void writeMainMethod() {
			incr();

			startComment();
			addComment("The main method runs all the tests in the text ui");
			endComment();

			println("public static void main (String args[]) ");
			println(" {");
			incr();
			println("junit.textui.TestRunner.run(suite());");
			decr();


			println("}");
			decr();
			println();
		}
		/**
		 * Write a method to the file.
		 *
		 * @param name     The name of the method.
		 * @param comment  The Javadoc comment.
		 */
		void writeMethod(String name, String comment) {
			writeMethod(name, comment, "// your code goes here.");
		}
		/**
		 * Write a method to the file.
		 *
		 * @param name     The name of the method.
		 * @param comment  The Javadoc comment.
		 * @param content  The content of the method.
		 */
		void writeMethod(String name, String comment, String content) {
			incr();

			startComment();
			addComment(comment);
			endComment();

			println(new StringBuffer("public void ")
					.append(name)
					.append("() {")
					.toString());

			if (content != null && content.length() >0) {
				incr();
				println(content);
				decr();
			}
			println("}");
			decr();
			println();
		}
		void writeSuiteMethod() {
			incr();

			startComment();
			addComment("The suite method runs all the tests");
			endComment();
			incr();
			println(new StringBuffer("public static Test suite ")
					.append("() {"));

			println(new StringBuffer("TestSuite suite = new TestSuite(")
					.append(className)
					.append(".class);").toString());
			println("return suite;");
			decr();


			println("}");
			decr();
			println();
		}
}
