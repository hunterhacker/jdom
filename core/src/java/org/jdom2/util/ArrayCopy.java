package org.jdom2.util;

import java.lang.reflect.Array;

/**
 * The copyOf methods on java.util.Arrays are introduced in Java6. Need an
 * alternative to support Java5.
 * 
 * @author Rolf Lear
 *
 */
public final class ArrayCopy {

	/**
	 * Arrays.copyOf(...) is a Java6 thing. This is a replacement.
	 * @param <E> The generic type of the array we are copying.
	 * @param source the source array.
	 * @param len the length of the new array copy.
	 * @return a new array that has the same elements as the source.
	 */
	public static final <E> E[] copyOf(final E[] source, final int len) {
		@SuppressWarnings("unchecked")
		final E[] dest = (E[])Array.newInstance(source.getClass().getComponentType(), len);
		System.arraycopy(source, 0, dest, 0, len < source.length ? len : source.length);
		return dest;
	}

	/**
	 * Arrays.copyOf(...) is a Java6 thing. This is a replacement.
	 * @param <E> The generic type of the array we are copying.
	 * @param source the source array.
	 * @param from the start point of the copy (inclusive).
	 * @param to the end point of the copy (exclusive).
	 * @return a new array that has the same elements as the source.
	 */
	public static final <E> E[] copyOfRange(final E[] source, final int from, int to) {
		final int len = to - from;
		@SuppressWarnings("unchecked")
		final E[] dest = (E[])Array.newInstance(source.getClass().getComponentType(), len);
		System.arraycopy(source, from, dest, 0, len < source.length ? len : source.length);
		return dest;
	}

	/**
	 * Arrays.copyOf(...) is a Java6 thing. This is a replacement.
	 * @param source the source array.
	 * @param len the length of the new array copy.
	 * @return a new array that has the same elements as the source.
	 */
	public static final char[] copyOf(final char[] source, final int len) {
		final char[] dest = new char[len];
		System.arraycopy(source, 0, dest, 0, len < source.length ? len : source.length);
		return dest;
	}

	/**
	 * Arrays.copyOf(...) is a Java6 thing. This is a replacement.
	 * @param source the source array.
	 * @param len the length of the new array copy.
	 * @return a new array that has the same elements as the source.
	 */
	public static final int[] copyOf(final int[] source, final int len) {
		final int[] dest = new int[len];
		System.arraycopy(source, 0, dest, 0, len < source.length ? len : source.length);
		return dest;
	}

	/**
	 * Arrays.copyOf(...) is a Java6 thing. This is a replacement.
	 * @param source the source array.
	 * @param len the length of the new array copy.
	 * @return a new array that has the same elements as the source.
	 */
	public static final boolean[] copyOf(final boolean[] source, final int len) {
		final boolean[] dest = new boolean[len];
		System.arraycopy(source, 0, dest, 0, len < source.length ? len : source.length);
		return dest;
	}


}
