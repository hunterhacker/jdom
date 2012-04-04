/*--

 Copyright (C) 2011 Jason Hunter & Brett McLaughlin.
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:

 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions, and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions, and the disclaimer that follows
    these conditions in the documentation and/or other materials
    provided with the distribution.

 3. The name "JDOM" must not be used to endorse or promote products
    derived from this software without prior written permission.  For
    written permission, please contact <request_AT_jdom_DOT_org>.

 4. Products derived from this software may not be called "JDOM", nor
    may "JDOM" appear in their name, without prior written permission
    from the JDOM Project Management <request_AT_jdom_DOT_org>.

 In addition, we request (but do not require) that you include in the
 end-user documentation provided with the redistribution and/or in the
 software itself an acknowledgement equivalent to the following:
     "This product includes software developed by the
      JDOM Project (http://www.jdom.org/)."
 Alternatively, the acknowledgment may be graphical using the logos
 available at http://www.jdom.org/images/logos.

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED.  IN NO EVENT SHALL THE JDOM AUTHORS OR THE PROJECT
 CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.

 This software consists of voluntary contributions made by many
 individuals on behalf of the JDOM Project and was originally
 created by Jason Hunter <jhunter_AT_jdom_DOT_org> and
 Brett McLaughlin <brett_AT_jdom_DOT_org>.  For more information
 on the JDOM Project, please see <http://www.jdom.org/>.

 */

package org.jdom2;

import org.jdom2.internal.ArrayCopy;


/**
 * This is a mechanism for storing and reusing unique instances of Strings.
 * The idea is that in XML the tag names, attribute names, and other String
 * content is often repeated a lot. Each repeat is typically done as a new
 * String instance. This class makes it possible to substantially reduce memory
 * usage by reusing String instances instead of keeping the new ones.
 * <p>
 * This class is not the same as String.intern() because String.intern() uses
 * the PermGen memory space (very limited in size), whereas this uses the heap.
 * <p>
 * The primary goal of this class is to be as memory efficient as possible. This
 * has the interesting side effect of reducing the amount of time spent in
 * garbage-collection cycles. While this does increase the amount of time to
 * process a String, it means that subsequent String values can be 'recycled'
 * fast, and, ideally, never need to leave the 'eden space' in the memory model
 * which in turn means that the duplicate strings do not even hit the GC
 * overhead. It is easy to measure that this process takes longer than simply
 * keeping the duplicate String values, but it is much harder to measure the
 * decreased cost of GC. It would be somewhat fair to say that the memory
 * benefit is substantial, and the cost of allocation is offset by the savings
 * in garbage collection. This trade-off is dependent on the amount of duplicate
 * data you have. In XML where there are lots of repeating patterns of element
 * and attribute names this can add up pretty fast.
 * <p>
 * This class is not thread-safe.
 * 
 * @author Rolf Lear
 *
 */
final class StringBin {
	
	// Here are some magic numbers:
	
	/** Default bucket-size growth factor */
	private static final int GROW = 4;
	/**
	 * How many buckets to start with
	 * <p>
	 * Actually, this just sets the initial capacity which is used to calculate
	 * the number of buckets. This implementation will turn the default capacity
	 * of 1023 in to 
	 */
	private static final int DEFAULTCAP = 1023;
	/** How big to let the largest bucket grow before a rehash */
	private static final int MAXBUCKET = 64;
	
	/** 
	 * The actual buckets.
	 * There is a really good reason to use a two-dimensional array: a 1
	 * dimensional array would require us to do an inordinate amount of shifting
	 * of the data as we insert new values in to the middle.
	 * By having 'x' number of buckets, the average shift would be 1/x th of the
	 * amount, which is much faster.
	 */
	private String[][] buckets;
	private int lengths[];
	
	/** The bit mask and bit shift */
	private int mask = 0;
	
	/**
	 * Create a default instance of the StringBin with the default capacity.
	 */
	public StringBin() {
		this(DEFAULTCAP);
	}
	
	/**
	 * Create a StringBin instance with a specified initial capacity.
	 * @param capacity the capacity to set.
	 */
	public StringBin(int capacity) {
		if (capacity < 0) {
			throw new IllegalArgumentException("Can not have a negative capacity");
		}
		capacity--;
		if (capacity < DEFAULTCAP) {
			capacity = DEFAULTCAP;
		}
		// aim for 'GROW - 1' Strings per bucket...
		capacity /= (GROW - 1);
		int shift = 0;
		while (capacity != 0) {
			capacity >>>= 1;
			shift++;
		}
		mask = (1 << shift) - 1;
		buckets = new String[mask + 1][];
		lengths = new int[buckets.length];
	}
	
	/**
	 * This code effectively does a binary search for a value.
	 * The order of the data in a bucket is increasing-by-hashcode, and then
	 * for values with the same hashcode, it is increasing by alphabetical. 
	 * @param hash
	 * @param value
	 * @param bucket
	 * @param length
	 * @return
	 */
	private final int locate(final int hash, final String value, final String[] bucket, final int length) {
		int left = 0;
		int right = length -1;
		int mid = 0;
		while (left <= right) {
			mid = (left + right) >>> 1;
			if (bucket[mid].hashCode() > hash) {
				right = mid - 1;
			} else if (bucket[mid].hashCode() < hash) {
				left = mid + 1;
			} else {
				// have the same hashcode
				// do a string-compare.
				int cmp = value.compareTo(bucket[mid]);
				if (cmp == 0) {
					// equals.
					return mid;
				} else if (cmp < 0) {
					// our input value comes before the bucket value, search
					// backwards
					while (--mid >= left && bucket[mid].hashCode() == hash) {
						// we have gone back one, and we still have the same
						// hash code... let's compare.
						cmp = value.compareTo(bucket[mid]);
						if (cmp == 0) {
							// equals, found it.
							return mid;
						} else if (cmp > 0) {
							// we were searching backwards because we started at
							// the mid point which was after the input value.
							// now that we have found a value that comes
							// before the input value it means we have gone too
							// far... which in turn means the insertion point
							// is one place after where we are.
							return - (mid + 1) - 1;
						}
					}
					// this must mean that we ran out of data, or ran out of
					// values with the same hashcode...
					return - (mid + 1) - 1;
				} else {
					// we have a value that comes before the value with the
					// same hash as us.
					while (++mid <= right && bucket[mid].hashCode() == hash) {
						//the next value exists and has the same hash code.
						cmp = value.compareTo(bucket[mid]);
						if (cmp == 0) {
							// found our value.
							return mid;
						} else if (cmp < 0) {
							// we were searching forwards because we started at
							// the mid point which was before the input value.
							// now that we have found a value that comes
							// after the input value it means we have gone too
							// far... which in turn means the insertion point
							// is at the point where we are.
							return - mid - 1;
						}
					}
					// we have run out of values, or the value we are on has a
					// different hashcode.
					return - mid - 1;
				}
			}
		}
		// nothing had the same hashcode.
		return -left - 1;
	}
	
	/**
	 * Get a String instance that is equal to the input value. This may or may
	 * not be the same instance as the input value. Null input values will
	 * reuse() as null.
	 * @param value The value to check.
	 * @return a String that is equals() to the input value, or null if the
	 * input was null
	 */
	public String reuse(final String value) {
		if (value == null) {
			return null;
		}
		final int hash = value.hashCode();
		/*
		 *  we use a special masking routine here. This is important.
		 *  we always do a 16-bit shift, XOR it with the unshifted value, and
		 *  then apply the mask.
		 *  The reason is relatively simple: it makes rehashing much faster
		 *  because you never need to shift values in the rehash, they are always
		 *  just appended.
		 *  Further, there is no real need to get a true random distribution in
		 *  the buckets... it's not important. The String.hashCode() is a good
		 *  enough hash function so we do not lose much by doing it this way.
		 *  
		 *  In detail:
		 *  Normally for a bucketing/hashing system we will try to use as many
		 *  bits as possible to create the bucket hash for the value. In this
		 *  case though, we XOR the high 16 bits with the low 16 bits (and keep
		 *  the high 16 bits unchanged. Lets call this the 'interim result'.
		 *  We then apply our bit mask to that interim result to get the bucket
		 *  id. The important thing here is that the interim result is the same
		 *  no matter how many buckets there are.
		 *  
		 *  If we need to rehash the buckets, the mask will include more bits,
		 *  and that guarantees that the values in any one 'original' bucket
		 *  will be divided in to different buckets, and never merged with
		 *  values from a different original bucket.
		 *  
		 *  Thus, in a rehash, both because of the way we calculate the bucketid
		 *  and also because the values in a bucket are stored in increasing
		 *  hash value order, we never need to insert a value in to the middle
		 *  of the rehashed bucket, we can always add to the end.
		 */
		final int bucketid = ((hash >>> 16) ^ hash) & mask;
		
		final int length = lengths[bucketid];
		if (length == 0) {
			// start a new bucket
			final String v = compact(value);
			buckets[bucketid] = new String[GROW];
			buckets[bucketid][0] = v;
			lengths[bucketid] = 1;
			return v;
		}
		
		// get the existing bucket.
		String[] bucket = buckets[bucketid];
		
		// note the final value calculated as -val - 1
		final int ip = - locate(hash, value, bucket, length) - 1;
		if (ip < 0) {
			// this means we have found the value.
			return bucket[- ip - 1];
		}
		if (length >= MAXBUCKET) {
			// need to rehash, so we do, and then add our value
			rehash();
			return reuse(value);
		}
		if (length == bucket.length) {
			// there is no space for our value.
			bucket = ArrayCopy.copyOf(bucket, length + GROW);
			buckets[bucketid] = bucket;
		}
		System.arraycopy(bucket, ip, bucket, ip + 1, length - ip);
		final String v = compact(value);
		bucket[ip] = v;
		lengths[bucketid]++;
		return v;
	}
	
	/**
	 * Store the existing values in a new and larger set of buckets.
	 * This reduces the number of values in each bucket, which improves insert
	 * time.
	 * <p>
	 * The data is stored in hashCode() order, and then alphabetically for
	 * those instances where two String values have the same hashCode().
	 * <p>
	 * The bucketing hash key is calculated in a specific way that guarantees
	 * that when we rehash there values in a bucket will be divided between
	 * a new set of buckets, and no other source bucket will ever add values
	 * to a bucket that we are dividing our bucket to.
	 * <p> 
	 * The combination of the bucket hash key, and the bucket ordering means
	 * that during a rehash we never have to insert values in to the middle of
	 * the bucket.
	 */
	private void rehash() {
		String[][] olddata = buckets;
		// magic numbers ... we make 4-times as many buckets.
		mask = ((mask + 1) << 2) - 1;
		buckets = new String[mask + 1][];
		lengths = new int[buckets.length];
		int hash = 0, bucketid = 0, length = 0;
		for (String[] ob : olddata) {
			if (ob == null) {
				// was an empty bucket.
				continue;
			}
			for (String val : ob) {
				if (val == null) {
					// there are no more values to rehash in this bucket.
					break;
				}
				hash = val.hashCode();
				bucketid = ((hash >>> 16) ^ hash) & mask;
				length = lengths[bucketid];
				if (length == 0) {
					buckets[bucketid] = new String[(ob.length + GROW) / GROW];
					buckets[bucketid][0] = val;
				} else {
					if (buckets[bucketid].length == length) {
						buckets[bucketid] = ArrayCopy.copyOf(
								buckets[bucketid], lengths[bucketid] + GROW);
					}
					buckets[bucketid][length] = val;
				}
				lengths[bucketid]++;
			}
		}
	}
	
	/**
	 * Compact a Java String to its smallest char[] backing array.
	 * Java often reuses the char[] array that backs String classes. If you have
	 * one String value and substring it, or some other methods, then instead of
	 * creating a new char[] array it reuses the original one. This can lead to 
	 * small String values being backed by very large arrays. We do not want to
	 * be caching these large arrays... just the smallest.
	 * @param input The String to compact
	 * @return a Compacted version of the String.
	 */
	private static final String compact(final String input) {
		return new String(input.toCharArray());
	}
	
	/**
	 * Number of registered Strings
	 * @return the number of registered String values.
	 */
	public int size() {
		int sum = 0;
		for (int l : lengths) {
			sum += l;
		}
		return sum;
	}

}
