package org.jdom2.contrib.perf;

import java.io.IOException;
import java.io.Writer;

final class DevNull extends Writer {
	
	int counter = 0;
	
	public void reset() {
		counter = 0;
	}
	
	public int getCounter() {
		return counter;
	}
	
	@Override
	public void write(final char[] cbuf, final int off, final int len) throws IOException {
		// do nothing
		counter++;
	}
	
	@Override
	public void flush() throws IOException {
		// do nothing
		counter++;
	}
	
	@Override
	public void close() throws IOException {
		// do nothing
		counter++;
	}

	@Override
	public void write(final int c) throws IOException {
		// do nothing
		counter++;
	}

	@Override
	public void write(final char[] cbuf) throws IOException {
		// do nothing
		counter++;
	}

	@Override
	public void write(final String str) throws IOException {
		// do nothing
		counter++;
	}

	@Override
	public void write(final String str, final int off, final int len) throws IOException {
		// do nothing
		counter++;
	}

	@Override
	public Writer append(final CharSequence csq) throws IOException {
		counter++;
		return this;
	}

	@Override
	public Writer append(final CharSequence csq, final int start, final int end) throws IOException {
		counter++;
		return this;
	}

	@Override
	public Writer append(final char c) throws IOException {
		counter++;
		return this;
	}
	
}