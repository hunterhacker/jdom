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

package org.jdom.test.generate;

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

    /** The package name of the class */
    private String packageName;

    /** The name of the class minus the package name */
    private String className;

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
	 * Set the root path for class files to be generated in.
	 *
	 * @param file The directory where the class files will be generated.
	 */
	void setPath(File path) {
		this.path = path;
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
	 * Set the name of the target class.
	 *
	 * @param className The name of the target class.
	 */
	void setClassName(String className) {
		this.className = className;
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
	 * Set the name of the package this class will reside in.
	 *
	 * @param packageName The name of the target class.
	 */
	void setPackageName(String packageName) {
		this.packageName = packageName;
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

	/**
	 * 
	 *
	 */
    private static class TestCaseClassWriter extends IndentWriter {

		/** The test case message */
        private String msg;

		/** The name of the class */
        private String className;

		/** The package in which the test will reside */
        private String packageName;
        
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

		private void copyrightComment() {
			println("/* Please run replic.pl on me ! */");
		}

		/**
		 * Package must be the first thing in the file.
		 */
        private void packageDef() {
            println("package "+packageName+";");
            println();
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

            println(new StringBuffer("public final class ")
                    .append(className)
                    .toString());

            println("extends junit.framework.TestCase");
            println("{");

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
                        .append("() {")
                        .toString());
            incr();
            println("super(\""+msg+"\");");
            decr();
            println("}");
            println();
            reset();
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

        public void close() {
            reset();
            println("}");
            super.close();
        }


		public void startComment() {
			println("/**");
		}

		public void addComment(String comment) {
			println(" * " + comment);
		}

		public void endComment() {
			println(" */");
		}
    }

	/**
	 *
	 *
	 */
    private static class IndentWriter extends PrintWriter {

		/** The number of indents to use per line */
        int indent;
        
		/**
		 * Create a new instance using the specified file as the 
		 * target.
		 *
		 * @param f	The target file.
		 */
        IndentWriter(File f) throws IOException {
            super(new BufferedWriter(new FileWriter(f)));
        }

		/**
		 * Increment the indent.
		 */
        void incr() {
            indent++;
        }

		/**
		 * Decrement the indent.
		 */
        void decr() {
            indent--;
        }

		/**
		 * Set the indent back to 0.
		 */
        void reset() {
            indent = 0;
        }

		/**
		 * Print a line of text with the appropriate indent.
		 *
		 * @param s The String to print.
		 */
        public void println(String s) {
            StringBuffer out = new StringBuffer();
            if (indent>0) {
                for(int i=0; i<indent; i++) {
                    out.append("    ");
                }
            }

            out.append(s);
            super.println(out.toString());
        }
    }
}
