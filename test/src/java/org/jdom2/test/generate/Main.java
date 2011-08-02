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

/**
 * Main entry point for the test case generation.
 *
 * @author    Jools Enticknap
 * @version    1.00
 */
public final class Main {

	/** All the options an a synopsis */
	private final static String[] options = {
		"-ui",        // Run the Swing GUI.
		"-source",    // Specify the source class.
		"-rootdir",    // Specify the root directory of the jdom-test.
		"-outputFile", // Specify a single file to add all test methods to.
		"-report"    // Report on how upto date the test case is.
	};

	/** Name of the source class */
	private static String source;

	/** Root directory for the source code */
	private static String rootDir = ".";
	
	/** file name for combined source code */
	private static String outputFile = ".";

	/** Run the GUI */
	private static boolean ui;

	/**
	 * Main entry point.
	 *
	 * @param args    Arguments passed from the command line.
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			usage();
		}
		
		// Process the command line arguments.
		// No rocket science here :-)
		Properties props = new Properties();

		// Put in the defaults.
		props.put("rootdir", rootDir);

		for(int i=0; i<args.length; i++) {
			if (args.equals("-ui")) {
				ui = true;
			} else if (args[i].equals("-source")) {
				source = args[++i];
				props.put("source", source);
			} else if (args[i].equals("-rootdir")) {
				rootDir = args[++i];
				props.put("rootdir", rootDir);
			} else if (args[i].equals("-outputFile")) {
				outputFile = args[++i];
				props.put("outputFile", outputFile);
			} else {
				System.out.println( "unknown argument "+args[i]);
			}
		}
		
		// Must have a source directory and a root directory.
		if (source == null) {
			usage();
		}

		TestCaseGenerator tcg = new TestCaseGenerator(props);

		// Off we go.
		if (ui) {
			runUI(props);
		} else {
			try {
				tcg.generate();
			} catch(GeneratorException ge) {
				System.out.println(ge.getMessage());
				ge.printStackTrace();
			}
		}
	}
	/**
	 * Run the GUI passing in the command line options.
	 */
	private static void runUI(Properties props) {
	
	}
	/**
	 * Display a usage summary to the user.
	 */
	private static void usage() {

		System.out.println("JDOM test case generator.");
		System.out.println("Options are;");

		System.out.println("");
		
		System.out.println("-ui       <Run swing based GUI (1.2 only)>");
		System.out.println("-source   <Source class e.g. org.jdom.Element>");
		System.out.println("-rootdir  <Root directory for output files>");
		System.out.println("-report   <Check that -source class is upto date>");
		System.out.println("-outputFile <User specified appended output class>");

		System.out.println("");

		System.exit(1);
	}
}
