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
 * @author Jools Enticknap
 * @version 1.00
 */
class ClassGenerator {

    /** Constructor prefix 'TestCaseConstructor' */
    private static String ctorPrefix   = "TTC_";

    /** Method prefix 'TestCaseMethod' */
    private static String methodPrefix = "TTM_";

    /** File object pointing to the directory in which to place the files */
    private File path;

    /** The package name of the class */
    private String packageName;

    /** The name of the class minus the package name */
    private String className;

    /**
     * Create a ClassGenerator which will output it's files into 'path'
     * and the class name will be denoted as 'className'.
     *
     * @param path        The location for the output files.
     * @param className    The name of the class.
     */
    ClassGenerator(File path, String className, String packageName) {
        this.path        = path;
        this.className   = className;
        this.packageName = packageName;
    }

    /**
     * Generate a test case for a method.
     *
     * @param method    The method for which the test case will be
     *                  generated.
     */
    void generate(Method method) throws GeneratorException {
        StringBuffer buffer = new StringBuffer(methodPrefix);
        buffer.append(className);
        String outputClass=NameMangler.getMangledName(method,buffer).toString();

        // Add the required prefixes.
        String fileName = buffer.append(".java")
                          .toString();
        
        File f = new File(path, fileName);
        try {
            f.createNewFile();
            writeClass(f, outputClass);
        } catch( IOException ioe ) {
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

        String outputClass = NameMangler.getMangledName(ctor,buffer).toString();
        
        String fileName = buffer.append(".java")
                          .toString();


        File f = new File(path, fileName);
        try {
            f.createNewFile();
            writeClass(f, outputClass);
        } catch( IOException ioe ) {
            throw new GeneratorException(ioe.getMessage());
        }
    }

    /**
     * 
     *
     */
    void writeClass(File f, String outputClass) throws IOException {
        ClassWriter out = new ClassWriter(f, outputClass, packageName);
        out.generate();
        out.close();
    }

    private static class ClassWriter extends IndentWriter {

        private String className;
        private String packageName;
        
        ClassWriter(File f, String className, String packageName) 
        throws IOException 
        {
            super(f);
            this.className = className;
            this.packageName = packageName;
        }

        void generate() {
            packageDef();

            classDef();
            writeCtor();
            writeMethod("setUp");
            writeMethod("tearDown");
            writeMethod("test");
        }

        private void packageDef() {
            println("package "+packageName+";");
            println();
        }

        private void classDef() {
            println(new StringBuffer("public final class ")
                    .append(className)
                    .toString());

            println("extends junit.framework.TestCase");
            println("{");

        }
        
        void writeCtor() {
            incr();
            println(new StringBuffer("public ")
                           .append(className)
                        .append("() {")
                        .toString() );
            incr();
            println("super(\"Test Case\");");
            decr();
            println("}");
            println();
            reset();
        }

        void writeMethod(String name) {
            incr();
            println(new StringBuffer("public void ")
                    .append(name)
                    .append("() {")
                    .toString());
            println("}");
            decr();
            println();
        }

        public void close() {
            reset();
            println("}");
            super.close();
        }

    }

    private static class IndentWriter extends PrintWriter {
        int indent;
        
        IndentWriter(File f) throws IOException {
            super(new BufferedWriter(new FileWriter(f)));
        }

        void incr() {
            indent++;
        }

        void decr() {
            indent--;
        }

        void reset() {
            indent = 0;
        }

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
