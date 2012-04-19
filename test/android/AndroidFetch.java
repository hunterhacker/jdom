package org.jdom2.test.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

/**
 * This class is a hack class that adapts the JDOM Unit test environment to work on android.
 * See the wiki page: https://github.com/hunterhacker/jdom/wiki/JDOM2-and-Android
 * 
 * @author Rolf Lear
 *
 */
public class AndroidFetch extends FidoFetch{
	
	private final AtomicReference<File> folder = new AtomicReference<File>();
	
	private void writeCache(File cf, byte[] data) throws IOException {
		final File d = cf.getParentFile();
		if (!d.exists()) {
			d.mkdirs();
		}
		final FileOutputStream fos = new FileOutputStream(cf);
		fos.write(data);
		fos.flush();
		fos.close();
	}
	
	private byte[] readAsset(AssetManager am, String name) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final InputStream is = am.open(name);
		final byte[] buffer = new byte[2048];
		int len = 0;
		while ((len = is.read(buffer)) >= 0) {
			baos.write(buffer, 0, len);
		}
		is.close();
		return baos.toByteArray();
	}

	private String[] parseAssets(byte[] data) throws IOException {
		final BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data)));
		final ArrayList<String> al = new ArrayList<String>();
		String line = null;
		while ((line = br.readLine()) != null) {
			if (line.trim().length() > 0) {
				al.add(line.trim());
			}
		}
		return al.toArray(new String[al.size()]);
	}


	/**
	 * @param o This should be a context object
	 */
	public static final void check(Context o) {
		final FidoFetch ff = getFido();
		final AndroidFetch af = (AndroidFetch)ff;
		af.checkFolder(o);
		
	}

	private void checkFolder(Context ctx) {
		if (folder.get() == null) {
			// first time in....
			
			// OK, now set the cache folder.
			final File dir = new File(ctx.getCacheDir(), "jdomassets");
			if (!dir.exists()) {
				dir.mkdirs();
			}
			
			// Then, get the Asset Manager.
			final AssetManager am = ctx.getAssets();
			
			try {
			
				// we want the special asset 'assetlist.txt' which is created by the
				// JDOM 'android' build target, listing all the required assets.
				final String[] assets = parseAssets(readAsset(am, "assetlist.txt"));
				for (String asset : assets) {
					final byte[] data = readAsset(am, asset);
					final File f = new File(dir, asset);
					writeCache(f, data);
					Log.i("JDOM", "Transfer asset " + asset + " to cache.");
				}
			} catch (Exception e) {
				throw new IllegalStateException("Unable to transfer assets to " + dir, e);
			}
			
			folder.set(dir.getAbsoluteFile());
		}
	}

	@Override
	public URL getURL(String name) {
		try {
			final File f = new File(folder.get(), name);
			return f.toURI().toURL();
		} catch (Exception e) {
			throw new IllegalStateException("Unable to get resource " + name, e);
		}
	}

	@Override
	public InputStream getStream(String name) {
		try {
			final File f = new File(folder.get(), name);
			return new FileInputStream(f);
		} catch (Exception e) {
			throw new IllegalStateException("Unable to get resource " + name, e);
		}
	}
	
}
