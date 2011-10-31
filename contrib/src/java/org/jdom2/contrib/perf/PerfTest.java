package org.jdom2.contrib.perf;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("javadoc")
public class PerfTest {
	
	public static final long timeRun(TimeRunnable torun) throws Exception {
		long min = Long.MAX_VALUE;
		long max = Long.MIN_VALUE;
		long sum = 0L;
		final int cnt = 12;
		for (int i = 0 ; i < cnt; i++) {
			System.gc();
			long time = System.nanoTime();
			torun.run();
			time = System.nanoTime() - time;
			if (time > max) {
				max = time;
			}
			if (time < min) {
				min = time;
			}
			sum += time;
		}
		return (sum - (min + max)) / (cnt - 2);
	}


	public static final long usedMem() {
		long mem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		boolean stable = false;
		Thread gct = new Thread("GCPrompt") {
			@Override
			public void run() {
				System.gc();
			}
		};
		gct.setDaemon(true);
		try {
			gct.start();
			Thread.sleep(100);
			gct.join();
		} catch (Exception e) {
			// ignore;
		}
		do {
			int cnt = 3;
			while (--cnt >= 0) {
				System.gc();
			}
			long tmpmem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
			long diff = mem - tmpmem;
			mem = tmpmem;
			if (diff >= 0 && diff < 128) {
				stable = true;
			}
		} while (!stable);
		return mem;
	}
	
	private static final String[] suffix = new String[]{"Bytes", "KiB", "MiB", "GiB", "TiB" };
	
	private static final String formatMem(long mem) {
		double frac = mem;
		int cnt = 0;
		while (frac > 1024.0) {
			frac /= 1024.0;
			cnt++;
		}
		return String.format("%.2f%s", frac, suffix[cnt]);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<File> infiles = new ArrayList<File>();
		
		final FileFilter filefilter = new FileFilter() {
			@Override
			public boolean accept(File path) {
				return path.isFile() && path.getName().toLowerCase().endsWith(".xml");
			}
		};
		
		for (String arg : args) {
			File f = new File(arg);
			if (f.exists()) {
				if (f.isFile() && f.getName().toLowerCase().endsWith(".xml")) {
					infiles.add(f);
				} else if (f.isDirectory()) {
					File[] files = f.listFiles(filefilter);
					if (files.length > 0) {
						for (File sf : files) {
							infiles.add(sf);
						}
					} else {
						System.err.println("Ignoring File " + f + ": Directory has no XML files.");
					}
				} else {
					System.err.println("Ignoring File " + f + ": not .xml and not Directory.");
				}
			} else {
				System.err.println("Ignoring File " + f + ": does not exist.");
			}
		}
		
		if (infiles.isEmpty()) {
			File tryhamlet = new File("contrib/src/resources/hamlet.xml");
			if (tryhamlet.exists()) {
				System.out.println("Using default file " + tryhamlet);
				infiles.add(tryhamlet);
			}
		}
		
		System.out.printf("Processing %d files.\n", infiles.size());

		StringBuilder html = new StringBuilder();
		
		perfloop(infiles, html);
		perfloop(infiles, html);
		System.out.println("Ignore the above warm-up results!\n\n");
		
		html.setLength(0);
		html.append("\n\t<hr/>\n\t<p/>\n\tDescription - change me\n\t<br />\n\t<table border=\"1\">\n\t\t<tr>");
		for (String h : new String[] {"Input", "JDOM", "SAX", "SAXJ", "DOM", "DOMJ", 
				"StAXS", "StAXSJ", "StAXE", "StAXEJ", "Scan", "Dump", 
				"Dupe", "XPath", "Checked", "UnChecked"}) {
			html.append("<th>").append(h).append("</th>");
		}
		html.append("</tr>\n");
		
		for (int i = 0; i < 5; i++) {
			perfloop(infiles, html);
		}
		
		html.append("\t</table>\n");
		
		System.out.println(html.toString());
	}
	
	private static final void perfloop(List<File> infiles, StringBuilder html) {
		
		double mstime = 1000000.0;
		
		PerfDoc[] docs = new PerfDoc[infiles.size()];
		int cnt = 0;
		long bytecnt = 0L;
		for (File f : infiles) {
			try {
				docs[cnt++] = new PerfDoc(f);
			} catch (IOException e) {
				throw new IllegalStateException("Unable to load " + f, e);
			}
			bytecnt += f.length();
		}
		
		long loadmem = 0L;
		long saxtime = 0L;
		long saxdtime = 0L;
		
		
		long startmem = usedMem();
		for (PerfDoc pd : docs) {
			try {
				saxtime += pd.saxLoad();
				loadmem += pd.getLoadMem();
				saxdtime += pd.getSaxDTime();
//				System.out.println("Loaded " + pd.getFile());
			} catch (Exception e) {
				System.err.println("Failed to load " + pd);
				e.printStackTrace();
			}
		}
		
		long actloadmem = usedMem() - startmem;
		
		double pctdiff = ((actloadmem - loadmem) * 100.0) / actloadmem;
		if (pctdiff < 0.0) {
			pctdiff = -1.0 * pctdiff;
		}
		if (pctdiff > 1.0) {
			throw new IllegalStateException(
					String.format("Memory calculations do not add up direct=%s sum=%s.",
							formatMem(actloadmem), formatMem(loadmem)));
		}
		
		usedMem();
		
		long domtime = 0L;
		long domdtime = 0L;
		for (PerfDoc pd : docs) {
			try {
				domtime += pd.domLoad();
				domdtime += pd.getDomDTime();
			} catch (Exception e) {
				System.err.println("Failed to DOM " + pd);
				e.printStackTrace();
			}
		}
		
		usedMem();
		
		long staxtime = 0L;
		long staxdtime = 0L;
		for (PerfDoc pd : docs) {
			try {
				staxtime += pd.staxLoad();
				staxdtime += pd.getStaxDTime();
			} catch (Exception e) {
				System.err.println("Failed to STAX " + pd);
				e.printStackTrace();
			}
		}

		usedMem();
		
		long staxetime = 0L;
		long staxdetime = 0L;
		for (PerfDoc pd : docs) {
			try {
				staxetime += pd.staxELoad();
				staxdetime += pd.getStaxDETime();
			} catch (Exception e) {
				System.err.println("Failed to STAX " + pd);
				e.printStackTrace();
			}
		}

		long scantime = 0L;
		for (PerfDoc pd : docs) {
			try {
				scantime += pd.scan();
			} catch (Exception e) {
				System.err.println("Failed to scan " + pd);
				e.printStackTrace();
			}
		}
		
		usedMem();
		
		long dumptime = 0L;
		for (PerfDoc pd : docs) {
			try {
				dumptime += pd.dump();
			} catch (Exception e) {
				System.err.println("Failed to dump " + pd);
				e.printStackTrace();
			}
		}
		
		usedMem();
		
		long dupetime = 0L;
		for (PerfDoc pd : docs) {
			try {
				dupetime += pd.duplicate();
			} catch (Exception e) {
				System.err.println("Failed to dupe " + pd);
				e.printStackTrace();
			}
		}
		
		usedMem();
		
		long xpathtime = 0L;
		for (PerfDoc pd : docs) {
			try {
				xpathtime += pd.xpath();
			} catch (Exception e) {
				System.err.println("Failed to xpath " + pd);
				e.printStackTrace();
			}
		}
		
		usedMem();
		
		long checkedtime = 0L;
		for (PerfDoc pd : docs) {
			try {
				checkedtime += pd.checkedParse();
			} catch (Exception e) {
				System.err.println("Failed to checked-parse " + pd);
				e.printStackTrace();
			}
		}
		
		long uncheckedtime = 0L;
		for (PerfDoc pd : docs) {
			try {
				uncheckedtime += pd.unCheckedParse();
			} catch (Exception e) {
				System.err.println("Failed to unchecked-parse " + pd);
				e.printStackTrace();
			}
		}
		
		System.out.printf ("PERF: loadbytes=%s loadmem=%s sax=%.2fb(%.2fb) dom=%.2fb(%.2fb) " +
				"staxs=%.2fb(%.2fb)  staxe=%.2fb(%.2fb) " +
				"scan=%.2fb dump=%.2fb dupe=%.2fb xpath=%.2fb checked=%.2fb unchecked=%.2fb \n", 
				formatMem(bytecnt), formatMem(loadmem), saxtime / mstime, saxdtime / mstime, 
				domtime / mstime, domdtime / mstime, staxtime / mstime, staxdtime / mstime,
				staxetime / mstime, staxdetime / mstime,
				scantime / mstime, dumptime / mstime, dupetime / mstime, 
				xpathtime / mstime, checkedtime / mstime, uncheckedtime / mstime);

		html.append("\t\t<tr><td>").append(formatMem(bytecnt)).append("</td><td>")
			.append(formatMem(loadmem)).append("</td>");
		
		long[] times = new long[] {saxtime, saxtime - saxdtime, domtime, domtime - domdtime, 
				staxtime, staxtime - staxdtime, staxetime, staxetime - staxdetime, 
				scantime, dumptime, dupetime, 
					xpathtime, checkedtime, uncheckedtime};
		for (long t : times) {
			html.append("<td>").append(String.format("%.2fms", t / mstime)).append("</td>");
		}
		html.append("</tr>\n");
		
		
	}

}
