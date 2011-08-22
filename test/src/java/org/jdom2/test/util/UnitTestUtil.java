package org.jdom2.test.util;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class UnitTestUtil {
	
	/**
	 * Serialize, then deserialize an instance object.
	 * @param tclass
	 * @param input
	 * @return
	 */
    public static final <T extends Serializable> T deSerialize(T input) {
    	if (input == null) {
    		return null;
    	}
    	Class<?> tclass = input.getClass();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            final ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            try {
                objectOutputStream.writeObject(input);
            }
            catch(final IOException ioException) {
                fail("unable to serialize object" + ioException);
            }
            finally {
                try {
                    objectOutputStream.close();
                }
                catch(final IOException ioException) {
                    fail("failed to close object stream while serializing object" + ioException);
                }
            }
        }
        catch(final IOException ioException) {
            fail("unable to serialize object" + ioException);
        }
        finally {
            try {
                outputStream.close();
            }
            catch(final IOException ioException) {
                fail("failed to close output stream while serializing object" + ioException);
            }
        }

        final byte[] bytes = outputStream.toByteArray();
        
        Object o = null;

        final InputStream inputStream = new ByteArrayInputStream(bytes);
        try {
            final ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            try {
                o = objectInputStream.readObject();
            }
            finally {
                try {
                    objectInputStream.close();
                }
                catch(final IOException ioException) {
                    fail("failed to close object stream while deserializing object" + ioException);
                }
            }
        }
        catch(final IOException ioException) {
            fail("unable to deserialize object" + ioException);
        }
        catch(final ClassNotFoundException classNotFoundException) {
            fail("unable to deserialize object" + classNotFoundException);
        }
        finally {
            try{
                inputStream.close();
            }
            catch(final IOException ioException) {
                fail("failed to close output stream while serializing object" + ioException);
            }
        }
        
        @SuppressWarnings("unchecked")
		T t = (T)tclass.cast(o);
        return t;

    }
    
    /**
     * Test whether two values are equals, in addition, check the hashCode() values
     * This method will pass if both values are null.
     * @param a The first value to check
     * @param b The other value
     */
    public static final void checkEquals(Object a, Object b) {
    	if (a == null) {
    		if (b != null) {
    			fail("Second value is not null like the first: " + b.toString());
    		}
    		return;
    	}
    	if (b == null) {
    		fail ("First value is not null like the second: " + a.toString());
    	}
    	assertTrue(a.equals(a));
    	assertTrue(b.equals(b));
    	if (!a.equals(b)) {
    		fail("First value '" + a + "' does not equals() second value '" + b + "'.");
    	}
    	if (!b.equals(a)) {
    		fail("Second value '" + b + "' does not equals() first value '" + a + "'.");
    	}
    	if (a.hashCode() != b.hashCode()) {
    		fail("Hashcodes of equals() values are different. " +
    				"First value '" + a + "' (hashcode=" + a.hashCode() + ") " +
    				"not same as " +
    				"second value '" + b + "' (hashcode=" + b.hashCode() + ").");
    	}
    }
    
    public static final void testReadIterator(Iterator<?> it, Object ... values) {
    	assertTrue(it != null);
    	assertTrue(values != null);
    	for (int i = 0; i < values.length; i++) {
    		assertTrue(it.hasNext());
    		assertTrue(values[i] == it.next());
    	}
    	assertFalse("Not enough values in the test set.", it.hasNext());
    	try {
    		it.next();
    		fail("Should not be able to have next after values have run out.");
    	} catch (NoSuchElementException nsee) {
    		// the way it should be
    	} catch (Exception e) {
    		fail("next() should fail with NoSuchElementException, not " + e.getClass().getName());
    	}
    	
    }
    
    /**
     * Create a new String instance based on an input String.
     * The String class tends to have these fancy ways of re-using internal
     * data structures when creating one string from another. This method
     * circumvents those optimisations.
     * @param value The value to clone
     * @return The cloned version of the input value.
     */
    public static final String cloneString(String value) {
    	char[] chars = value.toCharArray();
    	char[] ret = new char[chars.length];
    	System.arraycopy(chars, 0, ret, 0, chars.length);
    	return new String(ret);
    }

}
