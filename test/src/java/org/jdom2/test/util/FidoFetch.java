package org.jdom2.test.util;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Android does not have a reliable ClassLoader.getResource() set of methods.
 * We do a poor-man's hack that works on Android too.
 * On Android, we need to make a new class org.jdom2.test.util.AndroidFetch.
 * 
 * @author Rolf Lear
 *
 */

public class FidoFetch {

	/**
	 * @param name The resource name to get.
	 * @return The URL to the name.
	 */
	public URL getURL(String name) {
		return this.getClass().getResource(name);
	}
		
	/**
	 * @param name The resource name to get.
	 * @return The Resource as a stream
	 */
	public InputStream getStream(String name) {
		return this.getClass().getResourceAsStream(name);
	}
	
	private static final AtomicReference<FidoFetch> fetch = new AtomicReference<FidoFetch>();
	
	/**
	 * A singleton instance type method.
	 * @return the singleton Fido instance.
	 */
	public static final FidoFetch getFido() {
		FidoFetch ret = fetch.get();
		if (ret == null) {
//			if ("Dalvik".equalsIgnoreCase(System.getProperty("java.vm.name", "junk"))) {
//				ret = ReflectionConstructor.construct("org.jdom2.test.util.AndroidFetch", FidoFetch.class);
//			} else {
				ret = new FidoFetch();
//			}
			fetch.set(ret);
		}
		return ret;
	}
	
}
