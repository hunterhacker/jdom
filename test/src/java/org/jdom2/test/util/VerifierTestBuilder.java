package org.jdom2.test.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import org.jdom2.Verifier;
import org.jdom2.internal.ArrayCopy;

/**
 * This class builds jUnitTestCases for the Verifier character classes.
 * It is based on the JDOM 1.1.1 verifier. Whatever that verifier accepted,
 * this one will too. In other words, it is for regression purposes only, not
 * reference.
 * @author Rolf Lear
 *
 */
@SuppressWarnings("javadoc")
public class VerifierTestBuilder {

	/**
	 * @param args
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Class<Verifier> vclass = Verifier.class;

		// Hunt for all Verifier methods that are of the form static boolean ...(char)
		for (Method meth : vclass.getMethods()) {
			if (meth.getReturnType() != Boolean.TYPE) {
				// we only want methods that return boolean.
				continue;
			}
			Type[] types = meth.getParameterTypes();
			if (types.length != 1 || types[0] != Character.TYPE) {
				// we want methods that take a single char argument
				continue;
			}
			if (!Modifier.isPublic(meth.getModifiers()) || !Modifier.isStatic(meth.getModifiers())) {
				// we want the public static methods only
				continue;
			}
			
			String mname = meth.getName();
			
			boolean valid = false;
			boolean q = false;
			int[] flips = new int[1024];
			int flen = 0;
			
			for (char i = 0; i < Character.MAX_VALUE; i++) {
				q = (Boolean)meth.invoke(null, Character.valueOf(i));
				if (q != valid) {
					valid = q;
					if (flen >= flips.length) {
						flips = ArrayCopy.copyOf(flips, flen + 1024); 
					}
					flips[flen++] = i;
				}
			}
			
			buildTest(mname, ArrayCopy.copyOf(flips, flen));
		}
	}
	
	private static final void buildTest(final String mname, int[] flips) {
		
		final String tname = "test" + mname.substring(0, 1).toUpperCase() + mname.substring(1);

		StringBuilder sb = new StringBuilder();
		appendLine(0, sb, "");
		appendLine(1, sb, "// Automated test built by VerifierTestBuilder");
		appendLine(1, sb, "@Test");
				
		appendLine(1, sb, "public void ", tname ,"() {");
		appendLine(2, sb,   "final int[] flips = new int[] {");
		for (int i = 0; i < flips.length; i++) {
			if ((i & 0x0f) == 0) {
				sb.append("\n\t\t\t\t");
			}
			sb.append(String.format(" 0x%04x,", flips[i]));
		}
		sb.setCharAt(sb.length() - 1, '\n');
		appendLine(2, sb,   "};");
		appendLine(2, sb,   "int c = 0;");
		appendLine(2, sb,   "int fcnt = 0;");
		appendLine(2, sb,   "boolean valid = false;");
		appendLine(2, sb,   "final long ms = System.currentTimeMillis();");
		
		appendLine(2, sb,     "while (c <= Character.MAX_VALUE) {");
		appendLine(3, sb,     "if (fcnt < flips.length && flips[fcnt] == c) {");
		appendLine(4, sb,       "valid = !valid;");
		appendLine(4, sb,       "fcnt++;");
		appendLine(3, sb,     "}");
		appendLine(3, sb,      "if (valid) {");
		appendLine(4, sb,       "if (!Verifier." + mname + "((char)c)) {");
		appendLine(5, sb,         "fail(\"Expected char 0x\" + Integer.toHexString(c) + \" to pass " + mname + " but it failed.\");");
		appendLine(4, sb, 		"}");
		appendLine(3, sb, 	   "} else {");
		appendLine(4, sb,       "if (Verifier." + mname + "((char)c)) {");
		appendLine(5, sb,         "fail(\"Expected char 0x\" + Integer.toHexString(c) + \" to fail " + mname + " but it passed.\");");
		appendLine(4, sb, 		"}");
		appendLine(3, sb, 	   "}");
		appendLine(3, sb, 	   "c++;");
		appendLine(2, sb,    "}");
		appendLine(2, sb,   "System.out.printf(\"Completed test", tname, "in %dms\\n\", System.currentTimeMillis() - ms);");
		
		appendLine(1, sb, "}");
		System.out.println(sb);
	}
	
	private static final void appendLine(int indent, StringBuilder sb, String...vals) {
		while (--indent >= 0) {
			sb.append("\t");
		}
		for (String s : vals) {
			sb.append(s).append(" ");
		}
		sb.setCharAt(sb.length() - 1, '\n');
	}

}
