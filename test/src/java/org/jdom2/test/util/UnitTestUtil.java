package org.jdom2.test.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jdom2.Attribute;
import org.jdom2.Comment;
import org.jdom2.Content;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.EntityRef;
import org.jdom2.Namespace;
import org.jdom2.NamespaceAware;
import org.jdom2.ProcessingInstruction;

@SuppressWarnings("javadoc")
public class UnitTestUtil {
	
	private static final AtomicBoolean isandroid = new AtomicBoolean(false);
	
	public static final void setAndroid() {
		isandroid.set(true);
	}
	
	public static final void testNamespaceIntro(NamespaceAware content, Namespace...expect) {
		List<Namespace> intro = content.getNamespacesIntroduced();
		List<Namespace> exp = Arrays.asList(expect);
		
		if (!exp.equals(intro)) {
			fail("We expect Introduced equal the Expected Namespaces." +
					"\n   Introduced: " + intro.toString() +
					"\n   Expected:   " + exp.toString());
		}
	}
	
	public static final void testNamespaceScope(NamespaceAware content, Namespace...expect) {
		List<Namespace> introduced = content.getNamespacesIntroduced();
		List<Namespace> inscope = new ArrayList<Namespace>(content.getNamespacesInScope());
		List<Namespace> inherited = content.getNamespacesInherited();
		
		if (introduced.size() + inherited.size() != inscope.size()) {
			fail(String.format("InScope Namespaces do not add up: InScope=%d Inherited=%d, Introduced=%d",
					inscope.size(), inherited.size(), introduced.size()));
		}
		int inher = 0;
		int intro = 0;
		for (Namespace ns : inscope) {
			if (inher < inherited.size() && inherited.get(inher) == ns) {
				inher++;
			}
			if (intro < introduced.size() && introduced.get(intro) == ns) {
				intro++;
			}
		}
		if (intro < introduced.size()) {
			// we are missing content(or it is out of order) in the lists.
			fail("Introduced list contains out-of-order, or non-intersecting Namespaces." +
					"\n   InScope:    " + inscope.toString() +
					"\n   Introduced: " + introduced.toString());
		}
		if (inher < inherited.size()) {
			// we are missing content(or it is out of order) in the lists.
			fail("Inherited list contains out-of-order, or non-intersecting Namespaces." +
					"\n   InScope:   " + inscope.toString() +
					"\n   Inherited: " + inherited.toString());
		}
		
		
		int i = 0;
		for (Namespace ns : inscope) {
			if (i >= expect.length) {
				fail("We have additional Namespaces " + inscope.subList(i, inscope.size()));
			}
			if (expect[i] != ns) {
				assertEquals("InScope differs from Expected at index " + i, expect[i], ns);
			}
			i++;
		}
		if (i < expect.length) {
			ArrayList<Namespace> missing = new ArrayList<Namespace>(expect.length - i);
			while (i < expect.length) {
				missing.add(expect[i++]);
			}
			fail("InScope is missing Namespaces " + missing);
		}
		
		inscope.removeAll(introduced);
		if (!inscope.equals(inherited)) {
			fail("We expect InScope - Introduced to equal Inherited." +
					"\n   InScope:   " + inscope.toString() +
					"\n   Inherited: " + inherited.toString());
		}
		
	}
	
	/**
	 * Serialize, then deserialize an instance object.
	 * @param tclass
	 * @param input
	 * @return
	 */
    public static final <T extends Serializable> T deSerialize(final T input) {
    	if (input == null) {
    		return null;
    	}
    	final Class<?> tclass = input.getClass();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            final ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            try {
                objectOutputStream.writeObject(input);
            }
            catch(final IOException ioException) {
                failException("Unable to serialize object.", ioException);
            }
            finally {
                try {
                    objectOutputStream.close();
                }
                catch(final IOException ioException) {
                    failException("failed to close object stream while serializing object", ioException);
                }
            }
        }
        catch(final IOException ioException) {
            failException("unable to serialize object", ioException);
        }
        finally {
            try {
                outputStream.close();
            }
            catch(final IOException ioException) {
                failException("failed to close output stream while serializing object", ioException);
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
                    failException("failed to close object stream while deserializing object", ioException);
                }
            }
        }
        catch(final IOException ioException) {
        	failException("unable to deserialize object.", ioException);
        }
        catch(final ClassNotFoundException classNotFoundException) {
            failException("unable to deserialize object.", classNotFoundException);
        }
        finally {
            try{
                inputStream.close();
            }
            catch(final IOException ioException) {
                failException("failed to close output stream while serializing object.", ioException);
            }
        }
        
        assertTrue("Deserialized (non-null) object is null!", o != null);
        
        @SuppressWarnings("unchecked")
		final T t = (T)tclass.cast(o);
        
        assertTrue("Deserialized instance should not be the same instance as the pre-serialization",
        		t != input);
        
        return t;

    }

    /**
     * Attribute order is not significant in XML documents.
     * This method re-arranges the order of the Attributes (in somewhat
     * alphabetical order, but that's not important) so that you can easily
     * compare the attribute content of two different elements.
     * @param emt The element who's Attributes we should rearrange.
     */
	public static final void normalizeAttributes(Element emt) {
		if (!emt.hasAttributes()) {
			return;
		}
		emt.sortAttributes(new Comparator<Attribute>() {
			@Override
			public int compare(Attribute o1, Attribute o2) {
				return o1.getQualifiedName().compareTo(o2.getQualifiedName());
			}
		});
		for (Object o : emt.getChildren()) {
			normalizeAttributes((Element)o);
		}
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
    
    /**
     * Provide a standard way to fail when we expect an exception but did not
     * get one...
     */
    public static final void failNoException(Class<? extends Throwable> expect) {
    	fail("This code was expected to produce the exception " + 
    			expect.getName() + " but it was not thrown.");
    }

    /**
     * Provide a standard test for checking for an exception.
     * @param expect
     * @param got
     */
    public static final void checkException(Class<? extends Throwable> expect,
    		Throwable got) {
    	if (expect == null) {
        	fail("We can't expect a null exception (cryptic enough?)"); 
    	}
    	if (got == null) {
    		fail("We expected an exception of type " + expect.getName() + 
    				" but got a null value instead");
    	}
    	if (!expect.isInstance(got)) {
    		AssertionError ae = new AssertionError(
    				"We expected an exception of type " + expect.getName() + 
    				" but got a " + got.getClass().getName() + 
    				" instead with message " + got.getMessage());
    		if (isandroid.get()) {
    			System.out.println("ANDROID: About to throw AssertionError " +
    					"but we cannot initialize the cause... " +
    					"Here follows both the AssertionError and its cause");
    			ae.printStackTrace();
    			System.out.println("And the cause...");
    			got.printStackTrace();
    		} else {
    			ae.initCause(got);
    		}
    		throw ae;
    	}
    }

    /**
     * Call this if you have an exception you want to fail for!
     * @param message
     * @param got this exception
     */
	public static void failException(String message, Throwable got) {
		AssertionError ae = new AssertionError(message);
		if (isandroid.get()) {
			System.out.println("ANDROID: About to throw AssertionError " +
					"but we cannot initialize the cause... " +
					"Here follows both the AssertionError and its cause");
			ae.printStackTrace();
			System.out.println("And the cause...");
			got.printStackTrace();
		} else {
			ae.initCause(got);
		}
		throw ae;
	}
	
	public static final void compare(final NamespaceAware ap, final NamespaceAware bp) {
    	assertNotNull(ap);
    	assertNotNull(bp);
    	assertEquals(ap.getClass(), bp.getClass());
    	assertTrue(ap != bp);
    	if (ap instanceof Content) {
    		final Content a = (Content)ap;
    		final Content b = (Content)bp;
	    	if (a.getParent() != null) {
	    		assertTrue(a.getParent() != b.getParent());
	    	}
	    	
	    	switch (a.getCType()) {
	    		case Text:
	    			assertEquals(a.getValue(), b.getValue());
	    			break;
	    		case CDATA:
	    			assertEquals(a.getValue(), b.getValue());
	    			break;
	    		case Comment:
	    			assertEquals(((Comment)a).getText(), ((Comment)b).getText());
	    			break;
	    		case DocType:
	    			DocType da = (DocType)a;
	    			DocType db = (DocType)b;
	    			assertEquals(da.getElementName(), db.getElementName());
	    			assertEquals(da.getPublicID(), db.getPublicID());
	    			assertEquals(da.getSystemID(), db.getSystemID());
	    			assertEquals(da.getInternalSubset(), db.getInternalSubset());
	    			break;
	    		case Element:
	    			Element ea = (Element)a;
	    			Element eb = (Element)b;
	    			assertEquals(ea.getName(), eb.getName());
	    			compare(ea.getAttributes(), eb.getAttributes());
	    			assertEquals(ea.getNamespacesInScope(), eb.getNamespacesInScope());
	        		final int sz = ea.getContentSize();
	        		if (sz != eb.getContentSize()) {
	        			fail (String.format("Expected %d members but got %d for Element %s", sz, eb.getContentSize(), ea));
	        		}
	        		for (int i = 0; i < sz; i++) {
	        			compare(ea.getContent(i), eb.getContent(i));
	        		}
	    			break;
	    		case EntityRef:
	    			assertEquals(((EntityRef)a).getName(), ((EntityRef)b).getName());
	    			break;
	    		case ProcessingInstruction:
	    			ProcessingInstruction pa = (ProcessingInstruction)a;
	    			ProcessingInstruction pb = (ProcessingInstruction)b;
	    			assertEquals(pa.getTarget(), pb.getTarget());
	    			assertEquals(pa.getData(), pb.getData());
	    			break;
	    	}
    	} else if (ap instanceof Attribute) {
    		compare ((Attribute)ap, (Attribute)bp);
    	} else if (ap instanceof Document) {
    		Document a = (Document)ap;
    		Document b = (Document)bp;
    		assertEquals(a.getBaseURI(), b.getBaseURI());
    		final int sz = a.getContentSize();
    		if (sz != b.getContentSize()) {
    			fail (String.format("We expected %d members in the Document content, but got %d", sz, b.getContentSize()));
    		}
    		for (int i = 0; i < sz; i++) {
    			compare(a.getContent(i), b.getContent(i));
    		}
    	}
    }
    
    
    public static final void compare(List<Attribute> a, List<Attribute> b) {
		assertTrue(a != b);
		if(a.size() != b.size()) {
			fail(String.format("Expected same number of attributes (a has %d and b has %d): a=%s and b=%s", 
					a.size(), b.size(), a.toString(), b.toString()));
		}
		Iterator<Attribute> ait = a.iterator();
		Iterator<Attribute> bit = b.iterator();
		while (ait.hasNext() && bit.hasNext()) {
			Attribute aa = ait.next();
			Attribute bb = bit.next();
			compare(aa, bb);
		}
		assertFalse(ait.hasNext());
		assertFalse(bit.hasNext());
		
	}


	public static final void compare(Attribute aa, Attribute bb) {
		assertEquals(aa.getName(), bb.getName());
		assertTrue(aa.getNamespace() == bb.getNamespace());
		assertEquals(aa.getValue(), bb.getValue());
	}


}
