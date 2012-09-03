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
	public static void main(final String[] args) throws InterruptedException {
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		if (args.length != 1) {
			throw new IllegalArgumentException("We expect a single directory argument.");
		}
		final File dir = new File(args[0]);
		if (!dir.isDirectory()) {
			throw new IllegalArgumentException("We expect a single directory argument.");
		}
		
		final int bestcnt = 50;
		
		long[] sattnanos = new long[bestcnt];
		long[] semtnanos = new long[bestcnt];
		long[] schrnanos = new long[bestcnt];
		long start = 0L;

		System.out.println("Loading data");
		
		final String[] attnames = parseFile(new File(dir, "checkAttributeName.txt"));
		final String[] emtnames = parseFile(new File(dir, "checkElementName.txt"));
		final String[] chardata = parseFile(new File(dir, "checkCharacterData.txt"));
		
		
		System.out.println("Stabilize");
		Thread.sleep(5000);
		final long prebytes = getMemUsed();
		
		System.out.println("Launch");
		
		int i = 0;
		int cnt = bestcnt * 4;
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
			
			System.out.printf("   Loop   %2d took: att=%.3fms emt=%.3fms char=%.3fms\n", cnt,
					attnanos / 1000000.0, emtnanos / 1000000.0, chrnanos / 1000000.0);
			
			insertTime(sattnanos, attnanos);
			insertTime(semtnanos, emtnanos);
			insertTime(schrnanos, chrnanos);
			
		}
		
		final long memused = getMemUsed() - prebytes;
		
		System.out.printf("    Validating took: att=%.3fms emt=%.3fms char=%.3fms mem=%.3fKB\n",
				avg(sattnanos) / 1000000.0, avg(semtnanos) / 1000000.0, avg(schrnanos) / 1000000.0,
				memused / 1024.0);
				
		Verifier.isAllXMLWhitespace("  ");
		System.out.println("Checks " + (chardata.length + emtnames.length + attnames.length));
	}
	
	private static final void insertTime(final long[] array, final long time) {
		int index = array.length -1;
		while (index >= 0 && (array[index] == 0L || time < array[index])) {
			index--;
		}
		index++;
		if (index < array.length) {
			System.arraycopy(array, index, array, index+1, array.length - index - 1);
			array[index] = time;
		}
	}
	
	private static final double avg(final long[] values) {
		long ret = 0L;
		for (long v : values) {
			ret += v;
		}
		return ret / (double)values.length;
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
