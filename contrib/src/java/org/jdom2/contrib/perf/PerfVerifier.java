package org.jdom2.contrib.perf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.jdom2.Verifier;

/**
 * This class is designed to test the performance of the JDOM Verifier.
 * It does that by repeatedly parsing a document through a verifierd JDOM sequence, and then
 * comparing the time to a non-verified parsing. The time differences is accounted for by
 * verifying overhead.
 * 
 * @author Rolf Lear
 *
 */
public class PerfVerifier {
	
	@SuppressWarnings("javadoc")
	public static void main(final String[] args) {
		if (args.length != 1) {
			throw new IllegalArgumentException("We expect a single directory argument.");
		}
		final File dir = new File(args[0]);
		if (!dir.isDirectory()) {
			throw new IllegalArgumentException("We expect a single directory argument.");
		}
		
		long sattnanos = 0L;
		long semtnanos = 0L;
		long schrnanos = 0L;
		long start = 0L;

		System.out.println("Loading data");
		
		final String[] attnames = parseFile(new File(dir, "checkAttributeName.txt"));
		final String[] emtnames = parseFile(new File(dir, "checkElementName.txt"));
		final String[] chardata = parseFile(new File(dir, "checkCharacterData.txt"));
		
		
		System.out.println("Stabilize");
		final long prebytes = getMemUsed();
		
		System.out.println("Launch");
		
		int i = 0;
		int cnt = 18;
		while (--cnt >= 0) {
			long attnanos = 0L;
			long emtnanos = 0L;
			long chrnanos = 0L;

			start = System.nanoTime();
			for (i = attnames.length - 1; i >= 0; i--) {
				Verifier.checkAttributeName(attnames[i]);
			}
			attnanos = System.nanoTime() - start;
			
			start = System.nanoTime();
			for (i = emtnames.length - 1; i >= 0; i--) {
				Verifier.checkElementName(emtnames[i]);
			}
			emtnanos = System.nanoTime() - start;

			start = System.nanoTime();
			for (i = chardata.length - 1; i >= 0; i--) {
				Verifier.checkCharacterData(chardata[i]);
			}
			chrnanos = System.nanoTime() - start;
			
			if (cnt >= 10) {
				System.out.printf("   Warmup %2d took: att=%.3fms emt=%.3fms char=%.3fms\n", cnt,
						attnanos / 1000000.0, emtnanos / 1000000.0, chrnanos / 1000000.0);
			} else {
				System.out.printf("   Loop   %2d took: att=%.3fms emt=%.3fms char=%.3fms\n", cnt,
						attnanos / 1000000.0, emtnanos / 1000000.0, chrnanos / 1000000.0);
				
				sattnanos += attnanos;
				semtnanos += emtnanos;
				schrnanos += chrnanos;
			}
			
		}
		
		final long memused = getMemUsed() - prebytes;
		
		System.out.printf("Validating took: att=%.3fms emt=%.3fms char=%.3fms mem=%.3fKB\n",
				sattnanos / 1000000.0, semtnanos / 1000000.0, schrnanos / 1000000.0,
				memused / 1024.0);
				
		Verifier.isAllXMLWhitespace("  ");
		System.out.println("Checks " + (chardata.length + emtnames.length + attnames.length));
	}

	private static long getMemUsed() {
		long minused = Long.MAX_VALUE;
		int cnt = 0;
		final Runtime rt = Runtime.getRuntime();
		try {
			while (cnt < 3) {
				System.gc();
				Thread.yield();
				Thread.sleep(100);
				long used = rt.totalMemory() - rt.freeMemory();
				if (used < minused) {
					cnt = 0;
					minused = used;
				} else {
					cnt++;
				}
			}
		} catch (InterruptedException ie) {
			throw new IllegalStateException("Interrupted", ie);
		}
		return minused;
	}

	private static final String[] parseFile(File file) {
		try {
			final StringBuilder sb = new StringBuilder(1024);
			final ArrayList<String> vals = new ArrayList<String>(10240);
			final FileInputStream fis = new FileInputStream(file);
			final InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
			System.out.println("Loading " + file.getPath());
			int c = 0;
			while ((c = isr.read()) >= 0) {
				if (c == 0) {
					vals.add(sb.toString());
					sb.setLength(0);
				} else {
					sb.append((char)c);
				}
			}
			fis.close();
			
			final String[] ret = new String[vals.size()];
			vals.toArray(ret);
			return ret;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		}
	}

}
