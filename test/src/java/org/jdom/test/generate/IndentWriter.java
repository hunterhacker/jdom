package org.jdom.test.generate;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.BufferedWriter;

public  class IndentWriter extends PrintWriter {

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
		 * Create a new instance using the specified file as the 
		 * target. and append if set
		 *
		 * @param f	The target file.
		 */
		IndentWriter(File f, boolean append) throws IOException {
			super(new BufferedWriter(new FileWriter(f.getAbsolutePath(), append)));
		}
		/**
		 * Decrement the indent.
		 */
		void decr() {
			indent--;
		}
		/**
		 * Increment the indent.
		 */
		void incr() {
			indent++;
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
		/**
		 * Set the indent back to 0.
		 */
		void reset() {
			indent = 0;
		}
}
