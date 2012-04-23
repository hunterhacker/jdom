package org.jdom2.contrib.android;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


/**
 * This class is a translation layer that  translates JUnit4 tests to
 * JUnit3 tests suitable for running with the Android Test harness.
 * <p/>
 * This class is a small part of an automated process described in the
 * wiki page https://github.com/hunterhacker/jdom/wiki/JDOM2-and-Android
 * 
 * @author Rolf Lear
 *
 */

@SuppressWarnings("javadoc")
public class TranslateTests {
	
	private static final String[] skipclasses = new String[] {".*StAX.*"};
	
	private static final String[] skipmethods = new String[] {
		".*HighSurrogateAttPair.*", "bulkIntern", "testBuildString"};

	private static final Pattern pat = Pattern.compile("^(.+/(\\w+))\\.class$");

	private static String potentialTest(String zename) {
		final Matcher mat = pat.matcher(zename);
		if (mat.matches()) {
			final String cname = mat.group(2);
			if (cname.startsWith("Test") || cname.endsWith("Test")) {
				for (String cm : skipclasses) {
					if (Pattern.matches(cm, cname)) {
						return null;
					}
				}
				final String cp = mat.group(1);
				return cp.replace('/', '.');
			}
		}
		return null;
	}

	/**
	 * Convert the Jar file to a set of new JUnit3 tests.
	 * @param args The two args are expected to be a Jar file (unit tests), and an output directory 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			throw new IllegalArgumentException("Usage: Jar SrcOutDir");
		}
		final String jarname = args[0];
		
		final File srcoutdir = new File(args[1]);
		if (!srcoutdir.exists()) {
			srcoutdir.mkdirs();
		}
		if (!srcoutdir.isDirectory()) {
			throw new IllegalArgumentException("Could not create/use SrcOutput directory: " + srcoutdir);
		}
		
		final ArrayList<String> classes = new ArrayList<String>();
		
		final ZipFile zfile = new ZipFile(jarname);
		try {
			final Enumeration<? extends ZipEntry> e = zfile.entries();
			while (e.hasMoreElements()) {
				final ZipEntry ze = e.nextElement();
				if (ze.isDirectory()) {
					continue;
				}
				final String zename = ze.getName();
				if (zename.endsWith(".class")) {
					final String classname = potentialTest(zename);
					if (classname != null) {
						TranslateTests tt = new TranslateTests(srcoutdir, classname);
						classes.add(tt.translate());
					}
				}
			}
		} finally {
			zfile.close();
		}
		
	}

	private final Class<?> tclass;
	private final File outf;

	public TranslateTests(File outdir, String zename) {
		System.out.println("Contemplating " + zename);
		try {
			tclass = Class.forName(zename);
		} catch (Exception e) {
			throw new IllegalStateException("Unable to load class " + zename, e);
		}
		final String path = zename.replace('.', '/') + "TT.java";
		outf = new File(outdir,path);
	}
	
/*


package com.example.android.skeletonapp.test;

import org.jdom2.test.cases.TestElement;

import android.test.AndroidTestCase;

@SuppressWarnings("javadoc")
public class JDOMMainTest extends AndroidTestCase {

	
	public void testElement() {
		assertTrue(Integer.valueOf(1) > 0);
		TestElement te = new TestElement();
		te.testCoalesceTextNested();
	}
}


 */

	private String translate() throws IOException {
		final String sname = tclass.getSimpleName();
		final StringBuilder sb = new StringBuilder();
		final String ret = tclass.getPackage().getName() + "." + sname + "TT";
		
		sb.append("package ").append(tclass.getPackage().getName()).append(";\n");
		sb.append("@SuppressWarnings(\"javadoc\")\n");
		sb.append("public class ").append(sname).append("TT extends android.test.AndroidTestCase {\n");
		
		if (tclass.getAnnotation(Ignore.class) != null) {
			sb.append("\n\n    // Class has @Ignore set\n\n");
		} else {
		
			final Method[] methods = tclass.getMethods();
			
			final ArrayList<Method> pretest = new ArrayList<Method>();
			final ArrayList<Method> posttest = new ArrayList<Method>();
			final TreeSet<String> excepts = new TreeSet<String>();
			
			for (Method m : methods) {
				if (m.getAnnotation(Before.class) != null) {
					pretest.add(m);
					excepts.addAll(getExceptions(m));
				}
				if (m.getAnnotation(After.class) != null) {
					posttest.add(m);
					excepts.addAll(getExceptions(m));
				}
			}
			
			sb.append("  private final ").append(sname).append(" test = new ").append(sname).append("();\n");
			
			sb.append("\n  @Override\n");
			sb.append("  public void setUp() throws Exception {\n");
			sb.append("    super.setUp();\n");
			sb.append("    // tests run when class starts...\n");
			sb.append("    org.jdom2.test.util.UnitTestUtil.setAndroid();\n");
			sb.append("    System.setProperty(\"javax.xml.validation.SchemaFactory:http://www.w3.org/2001/XMLSchema\",\n");
			sb.append("        \"org.apache.xerces.jaxp.validation.XMLSchemaFactory\");\n");
			for (Method m : methods) {
				if (m.getAnnotation(BeforeClass.class) != null) {
					sb.append("    test.").append(m.getName()).append("();\n");
				}
			}
			sb.append("  }\n");
			
			sb.append("\n  @Override\n");
			sb.append("  public void tearDown() throws Exception {\n");
			sb.append("    super.tearDown();\n");
			sb.append("    // tests run when class completes...\n");
			for (Method m : methods) {
				if (m.getAnnotation(AfterClass.class) != null) {
					sb.append("    test.").append(m.getName()).append("();\n");
				}
			}
			sb.append("  }\n");
			
			for (Method m : methods) {
				Test tanno = null;
				if ((tanno = m.getAnnotation(Test.class)) != null && m.getAnnotation(Ignore.class) == null) {
					final String tname = getTestName(m);
					sb.append("\n");
					sb.append("  public void ").append(tname).append("()").append(buildThrows(excepts, m)).append("{\n");
					for (Method pre : pretest) {
						sb.append("    // pre test\n");
						sb.append("    test.").append(pre.getName()).append("();\n");
					}
					if (posttest.isEmpty()) {
						sb.append("    // actual test\n");
						if (tanno.expected() == Test.None.class) {
							sb.append("    test.").append(m.getName()).append("();\n");
						} else {
							sb.append("    try {\n");
							sb.append("      test.").append(m.getName()).append("();\n");
							sb.append("        org.jdom2.test.util.UnitTestUtil.failNoException(").append(tanno.expected().getName()).append(".class);\n");
							sb.append("    } catch (").append(tanno.expected().getName()).append(" e) {\n");
							sb.append("        org.jdom2.test.util.UnitTestUtil.checkException(").append(tanno.expected().getName()).append(".class, e);\n");
							sb.append("    }\n");
						}
					} else {
						sb.append("    try {\n");
						sb.append("      // actual test\n");
						if (tanno.expected() == Test.None.class) {
							sb.append("      test.").append(m.getName()).append("();\n");
						} else {
							sb.append("      try {\n");
							sb.append("        test.").append(m.getName()).append("();\n");
							sb.append("        org.jdom2.test.util.UnitTestUtil.failNoException(").append(tanno.expected().getName()).append(".class);\n");
							sb.append("      } catch (Exception e) {\n");
							sb.append("        org.jdom2.test.util.UnitTestUtil.checkException(").append(tanno.expected().getName()).append(".class, e);\n");
							sb.append("      }\n");
						}
						sb.append("    } finally {\n");
						for (Method post : posttest) {
							sb.append("    // post test\n");
							sb.append("    test.").append(post.getName()).append("();\n");
						}
						sb.append("    }\n");
						
					}
					sb.append("  }\n");
				}
			}
			
		}
		
		sb.append("\n}\n");
		final File outd = outf.getParentFile().getAbsoluteFile();
		if (!outd.isDirectory()) {
			outd.mkdirs();
		}
		FileWriter fw = new FileWriter(outf);
		fw.write(sb.toString());
		fw.flush();
		fw.close();
		return ret;
	}

	private String getTestName(final Method m) {
		final String mname = m.getName();
		for (String mm : skipmethods) {
			if (Pattern.matches(mm, mname)) {
				return "/* Skip test on Android */ do_not_" + mname;
			}
		}
		return mname.startsWith("test") ? mname : ("test_" + mname);
	}

	private Set<String> getExceptions(Method m) {
		TreeSet<String> hs = new TreeSet<String>();
		if (m != null) {
			for (Class<?> ec : m.getExceptionTypes()) {
				hs.add(ec.getName());
			}
		}
		return hs;
	}
	
	private String buildThrows(Set<String> current, Method m) {
		final Set<String> tothrow = getExceptions(m);
		if (current != null) {
			tothrow.addAll(current);
		}
		final Iterator<String> it = tothrow.iterator();
		if (!it.hasNext()) {
			return " ";
		}
		final StringBuilder sb = new StringBuilder();
		sb.append(" throws ").append(it.next());
		while (it.hasNext()) {
			sb.append(", ").append(it.next());
		}
		sb.append(" ");
		return sb.toString();
	}
	
}
