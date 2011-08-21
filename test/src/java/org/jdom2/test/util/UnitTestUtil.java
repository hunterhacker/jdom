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
    
    public static void testReadIterator(Iterator<?> it, Object ... values) {
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

}
