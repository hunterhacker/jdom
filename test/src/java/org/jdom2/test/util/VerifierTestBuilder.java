package org.jdom2.test.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jdom2.Verifier;

/**
 * This class builds jUnitTestCases for the Verifier character classes.
 * It is based on the JDOM 1.1.1 verifier. Whatever that verifier accepted,
 * this one will too. In other words, it is for regression purposes only, not
 * reference.
 * @author Rolf Lear
 *
 */
public class VerifierTestBuilder {

	/**
	 * @param args
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Class<Verifier> vclass = Verifier.class;
		Map<String, int[]> sequence = new LinkedHashMap<String, int[]>();
		
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
				q = (Boolean)meth.invoke(null, Character.valueOf((char)i));
				if (q != valid) {
					valid = q;
					if (flen >= flips.length) {
						flips = Arrays.copyOf(flips, flen + 1024); 
					}
					flips[flen++] = i;
				}
			}
			
			sequence.put(mname, Arrays.copyOf(flips, flen));
		}
		
		for (Map.Entry<String, int[]> me : sequence.entrySet()) {
			print("", "\t// Automated test built by VerifierTestBuilder", "\t@Test");
			String mname = me.getKey();
			String tname = mname;
			tname = "test" + tname.substring(0, 1).toUpperCase() + tname.substring(1);
			print("\tpublic void " + tname + "() {");
			print("\t\tint c = 0;");
			print("\t\tlong ms = System.currentTimeMillis();");
			boolean valid = false;
			
			for (int c : me.getValue()) {
				writeLoop(c, mname, valid);
				valid = !valid;
			}
			writeLoop(Character.MAX_VALUE + 1, mname, valid);
			print("\t\tSystem.out.printf(\"Completed test " + tname + " in %dms\\n\", System.currentTimeMillis() - ms);");
			
			print("\t}");
		}
	}
	
	private static final void writeLoop(int c, String mname, boolean valid) {
		print(String.format("\t\twhile (c < 0x%04x) {", c));
		if (valid) {
			print(String.format("\t\t\tif (!Verifier.%s((char)c)) {", mname));
			print(String.format("\t\t\t\tfail(\"Expected char 0x\" + Integer.toHexString(c) + \" to pass %s but it did not.\");", mname));
			print(String.format("\t\t\t}", mname));
		} else {
			print(String.format("\t\t\tif (Verifier.%s((char)c)) {", mname));
			print(String.format("\t\t\t\tfail(\"Expected char 0x\" + Integer.toHexString(c) + \" to fail %s but it did not.\");", mname));
			print(String.format("\t\t\t}", mname));
		}
		print("\t\t\tc++;", "\t\t}", "");
	}
	
	private static final void print(String...vals) {
		for (String v : vals) {
			System.out.println(v);
		}
	}

}
