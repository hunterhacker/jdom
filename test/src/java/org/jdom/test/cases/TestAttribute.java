package org.jdom.test.cases;

/*--

 Copyright (C) 2000 Brett McLaughlin & Jason Hunter.
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
	written permission, please contact license@jdom.org.

 4. Products derived from this software may not be called "JDOM", nor
	may "JDOM" appear in their name, without prior written permission
	from the JDOM Project Management (pm@jdom.org).

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
 created by Brett McLaughlin <brett@jdom.org> and
 Jason Hunter <jhunter@jdom.org>.  For more information on the
 JDOM Project, please see <http://www.jdom.org/>.

 */


import java.io.*;
import junit.framework.*;

import org.jdom.*;

/**
 * Test the expected behavior of the Attribute class.
 *
 * @author unascribed
 * @version 0.1
 */
public final class TestAttribute
    extends junit.framework.TestCase
{

    /**
     *  Construct a new instance.
     */
    public TestAttribute(final String name) {
        super(name);
    }

    /**
     * The main method runs all the tests in the text ui
     */
    public static void main (final String args[]) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * This method is called before a test is executed.
     */
    public void setUp() {
        // your code goes here.
    }

    /**
     * The suite method runs all the tests
     */
    public static Test suite () {
        final TestSuite suite = new TestSuite(TestAttribute.class);
        return suite;
    }

    /**
     * This method is called after a test is executed.
     */
    public void tearDown() {
        // your code goes here.
    }

    /**
     * Test the simple case of constructing an attribute without name, value,
     * namespace or prefix
     */
    public void test_TCC() {
        final Attribute attribute = new Attribute(){
            // anonymous class
        };
    }

    /**
	 * Test the simple case of constructing an attribute without
	 * namespace or prefix
	 */
	public void test_TCC___String_String() {
		final Attribute attribute = new Attribute("test", "value");
		assertTrue("incorrect attribute name", attribute.getName().equals("test"));
        assertTrue("incoorect attribute value", attribute.getValue().equals("value"));
        assertEquals("incorrect attribute type", attribute.getAttributeType(), Attribute.UNDECLARED_TYPE);

        //should have been put in the NO_NAMESPACE namespace
		assertTrue("incorrect namespace", attribute.getNamespace().equals(Namespace.NO_NAMESPACE));


		try {
			final Attribute nullNameAttribute = new Attribute(null, "value");
			fail("didn't catch null attribute name");
		} catch (final IllegalArgumentException e) {
		} catch (final NullPointerException e) {
			fail("NullPointerException with null attribute name");
		}

		try {
            final Attribute nullValueAttribute  = new Attribute("test", null);
			fail("didn't catch null attribute value");
		} catch (final IllegalArgumentException e) {
		} catch (final NullPointerException e) {
			fail("NullPointerException with null attribute value");
		}

		try {
            final Attribute invalidNameAttribute = new Attribute("test" + (char)0x01, "value");
			fail("didn't catch invalid attribute name");
		} catch (final IllegalArgumentException e) {
		}

		try {
            final Attribute invalidValueAttribute = new Attribute("test", "test" + (char)0x01);
			fail("didn't catch invalid attribute value");
		} catch (final IllegalArgumentException e) {
		}

	}

    /**
	 * Test the constructor with name, value and namespace
	 */
	public void test_TCC___String_String_OrgJdomNamespace() {
        {
    		final Namespace namespace = Namespace.getNamespace("prefx", "http://some.other.place");

    		final Attribute attribute = new Attribute("test", "value", namespace);
    		assertTrue("incorrect attribute name", attribute.getName().equals("test"));
    		assertTrue("incoorect attribute value", attribute.getValue().equals("value"));
    		assertTrue("incorrect Namespace", attribute.getNamespace().equals(namespace));

            assertEquals("incoorect attribute type", attribute.getAttributeType(), Attribute.UNDECLARED_TYPE);
        }

		//now test that the attribute cannot be created with a namespace
		//without a prefix
        final Namespace defaultNamespace = Namespace.getNamespace("http://some.other.place");
		try {
            final Attribute attribute = new Attribute("test", "value", defaultNamespace);
			fail("allowed creation of attribute with a default namespace");
		} catch (final IllegalNameException e) {
		}


		try {
            final Attribute attribute = new Attribute("test", "value", null);
		} catch (final Exception e) {
        		fail("didn't handle null attribute namespace");
		}
	}


    /**
     * Test possible attribute values
     */
    public void test_TCM__Attribute_setAttributeType_int() {
        final Attribute attribute = new Attribute("test", "value");

        for(int attributeType = -10; attributeType < 15; attributeType++) {
            if (
                    Attribute.UNDECLARED_TYPE <= attributeType &&
                    attributeType <= Attribute.ENUMERATED_TYPE
            ) {
                continue;
            }
            try {
                attribute.setAttributeType(attributeType);
                fail("set unvalid attribute type: "+ attributeType);
            }
            catch(final IllegalDataException ignore) {
                // is expected
            }
            catch(final Exception exception) {
                fail("unknown exception throws: "+ exception);
            }
        }
    }

    /**
	 * Test a simple object comparison
	 */
	public void test_TCM__boolean_equals_Object() {
		final Attribute attribute = new Attribute("test", "value");

        assertFalse("attribute equal to null", attribute.equals(null));

        final Object object = (Object) attribute;
        assertTrue("object not equal to attribute", attribute.equals(object));
        assertTrue("attribute not equal to object", object.equals(attribute));

        // current implementation checks only for identity
//        final Attribute clonedAttribute = (Attribute) attribute.clone();
//        assertTrue("attribute not equal to its clone", attribute.equals(clonedAttribute));
//        assertTrue("clone not equal to attribute", clonedAttribute.equals(attribute));
	}

    /**
	 * test the convienience method getBooleanValue();
	 */
	public void test_TCM__boolean_getBooleanValue() {
		final Attribute attribute = new Attribute("test", "true");
		try {
			assertTrue("incorrect boolean true value", attribute.getBooleanValue());

            attribute.setValue("false");
			assertTrue("incorrect boolean false value", !attribute.getBooleanValue());

            attribute.setValue("TRUE");
			assertTrue("incorrect boolean TRUE value", attribute.getBooleanValue());

            attribute.setValue("FALSE");
			assertTrue("incorrect boolean FALSE value", !attribute.getBooleanValue());

		} catch (DataConversionException e) {
			fail("couldn't convert boolean value");
		}

		try {
            attribute.setValue("foo");
			assertTrue("incorrectly returned boolean from non boolean value", attribute.getBooleanValue());

		} catch (DataConversionException e) {
		}


	}
	/**
	 * Test convience method for getting doubles from an Attribute
	 */
	public void test_TCM__double_getDoubleValue() {
		Attribute attr = new Attribute("test", "11111111111111");
		try {
			assertTrue("incorrect double value", attr.getDoubleValue() == 11111111111111d );

			attr.setValue("0");
			assertTrue("incorrect double value", attr.getDoubleValue() == 0 );

			attr.setValue(Double.toString(java.lang.Double.MAX_VALUE));
			assertTrue("incorrect double value", attr.getDoubleValue() == java.lang.Double.MAX_VALUE);

			attr.setValue(Double.toString(java.lang.Double.MIN_VALUE));
			assertTrue("incorrect double value", attr.getDoubleValue() == java.lang.Double.MIN_VALUE);

		} catch (DataConversionException e) {
			fail("couldn't convert boolean value");
		}

		try {
			attr.setValue("foo");
			fail("incorrectly returned double from non double value" + attr.getDoubleValue());

		} catch (DataConversionException e) {
		}

	}

    /**
     * Test floats returned from Attribute values. Checks that both correctly
     * formatted (java style) and incorrectly formatted float strings are
     * returned or raise a DataConversionException
     */
    public void test_TCM__float_getFloatValue() {
        Attribute attr = new Attribute("test", "1.00000009999e+10f");
        float flt = 1.00000009999e+10f;
        try {
            assertTrue("incorrect float conversion",
                    attr.getFloatValue() == flt);
        } catch (DataConversionException e) {
            fail("couldn't convert to float");
        }

        // test an invalid float

        attr.setValue("1.00000009999e");
        try {
            attr.getFloatValue();
            fail("incorrect float conversion from non float");
        } catch (DataConversionException e) {
        }

    }

    /**
 	 * Tests that Attribute can convert value strings to ints and
 	 * that is raises DataConversionException if it is not an int.
	 */
	public void test_TCM__int_getIntValue() {
        final Attribute attribute = new Attribute("test", "");
        int summand = 3;
        for(int i = 0; i < 28; i++) {
            summand <<= 1;
            final int value = Integer.MIN_VALUE + summand;

            attribute.setValue("" + value);
            try {
                assertEquals("incorrect int conversion", attribute.getIntValue(), value);
            } catch (final DataConversionException e) {
                fail("couldn't convert to int");
            }
        }

		//test an invalid int
        TCM__int_getIntValue_invalidInt("" + Long.MIN_VALUE);
        TCM__int_getIntValue_invalidInt("" + Long.MAX_VALUE);
        TCM__int_getIntValue_invalidInt("" + Float.MIN_VALUE);
        TCM__int_getIntValue_invalidInt("" + Float.MAX_VALUE);
        TCM__int_getIntValue_invalidInt("" + Double.MIN_VALUE);
        TCM__int_getIntValue_invalidInt("" + Double.MAX_VALUE);
	}

    private void TCM__int_getIntValue_invalidInt(final String value) {
        final Attribute attribute = new Attribute("test", value);
        try {
            attribute.getIntValue();
            fail("incorrect int conversion from non int");
        } catch (final DataConversionException e) {
        }
    }
    /**
	 * Test that Attribute returns a valid hashcode.
	 */
	public void test_TCM__int_hashCode() {
	    final Attribute attr = new Attribute("test", "value");
		//only an exception would be a problem
		int i = -1;
		try {
		    i = attr.hashCode();
		}
		catch(Exception e) {
		    fail("bad hashCode");
		}
		final Attribute attr2 = new Attribute("test", "value");

        //different Attributes, same text
		final int x = attr2.hashCode();
		assertFalse("Different Attributes with same value have same hashcode", x == i);

        final Attribute attr3 = new Attribute("test2", "value");
		//only an exception would be a problem
		final int y = attr3.hashCode();
		assertFalse("Different Attributes have same hashcode", y == x);
	}

    /**
	 * Test the convienience method for returning a long from an Attribute
	 */
	public void test_TCM__long_getLongValue() {
        final Attribute attribute = new Attribute("test", "");
        long summand = 3;
	    for(int i = 0; i < 60; i++) {
            summand <<= 1;
            final long value = Long.MIN_VALUE + summand;

            attribute.setValue("" + value);
    		try {
    			assertEquals("incorrect long conversion", attribute.getLongValue(), value);
    		} catch (final DataConversionException e) {
    			fail("couldn't convert to long");
    		}
        }

		//test an invalid long
        attribute.setValue("100000000000000000000000000");
		try {
            attribute.getLongValue();
			fail("incorrect long conversion from non long");
		} catch (final DataConversionException e) {
		}
	}

    /**
 	 * Test that an Attribute can clone itself correctly.  The test
 	 * covers the simple case and with the attribute using a namespace
 	 * and prefix.
	 */
	public void test_TCM__Object_clone() {
        TCM__Object_clone__default();

        TCM__Object_clone__attributeType(Attribute.UNDECLARED_TYPE);
        TCM__Object_clone__attributeType(Attribute.CDATA_TYPE);
        TCM__Object_clone__attributeType(Attribute.ID_TYPE);
        TCM__Object_clone__attributeType(Attribute.IDREF_TYPE);
        TCM__Object_clone__attributeType(Attribute.IDREFS_TYPE);
        TCM__Object_clone__attributeType(Attribute.ENTITY_TYPE);
        TCM__Object_clone__attributeType(Attribute.ENTITIES_TYPE);
        TCM__Object_clone__attributeType(Attribute.NMTOKEN_TYPE);
        TCM__Object_clone__attributeType(Attribute.NMTOKENS_TYPE);
        TCM__Object_clone__attributeType(Attribute.NOTATION_TYPE);
        TCM__Object_clone__attributeType(Attribute.ENUMERATED_TYPE);


        TCM__Object_clone__Namespace_default();

        TCM__Object_clone__Namespace_attributeType(Attribute.UNDECLARED_TYPE);
        TCM__Object_clone__Namespace_attributeType(Attribute.CDATA_TYPE);
        TCM__Object_clone__Namespace_attributeType(Attribute.ID_TYPE);
        TCM__Object_clone__Namespace_attributeType(Attribute.IDREF_TYPE);
        TCM__Object_clone__Namespace_attributeType(Attribute.IDREFS_TYPE);
        TCM__Object_clone__Namespace_attributeType(Attribute.ENTITY_TYPE);
        TCM__Object_clone__Namespace_attributeType(Attribute.ENTITIES_TYPE);
        TCM__Object_clone__Namespace_attributeType(Attribute.NMTOKEN_TYPE);
        TCM__Object_clone__Namespace_attributeType(Attribute.NMTOKENS_TYPE);
        TCM__Object_clone__Namespace_attributeType(Attribute.NOTATION_TYPE);
        TCM__Object_clone__Namespace_attributeType(Attribute.ENUMERATED_TYPE);
	}

    /**
     * Test that an Attribute can clone itself correctly. The test covers the
     * simple case with only name and value.
     */
    private void TCM__Object_clone__default() {
        final String attributeName = "test";
        final String attributeValue = "value";

        final Attribute attribute = new Attribute(attributeName, attributeValue);
        final Attribute clonedAttribute = (Attribute) attribute.clone();

        assertTrue("incorrect name in clone", clonedAttribute.getName().equals(attributeName));
        assertTrue("incorrect value in clone", clonedAttribute.getValue().equals(attributeValue));
        assertEquals("incoorect attribute type in clone", clonedAttribute.getAttributeType(), Attribute.UNDECLARED_TYPE);
    }

    /**
     * Test that an Attribute can clone itself correctly. The test covers the
     * simple case with only name, value and a given attribute type.
     */
    private void TCM__Object_clone__attributeType(final int attributeType) {
        final String attributeName = "test";
        final String attributeValue = "value";

        final Attribute attribute = new Attribute(attributeName, attributeValue, attributeType);
        final Attribute clonedAttribute = (Attribute) attribute.clone();

        assertTrue("incorrect name in clone", clonedAttribute.getName().equals(attributeName));
        assertTrue("incorrect value in clone", clonedAttribute.getValue().equals(attributeValue));
        assertEquals("incoorect attribute type in clone", clonedAttribute.getAttributeType(), attributeType);
    }

    /**
     * Test that an Attribute can clone itself correctly. The test covers the
     * case with name, value, prefix, namespace and a given attribute type.
     */
    private void TCM__Object_clone__Namespace_default() {
        final String prefix = "prefx";
        final String uri = "http://some.other.place";

        final Namespace namespace = Namespace.getNamespace(prefix, uri);

        final String attributeName = "test";
        final String attributeValue = "value";

        final Attribute attribute = new Attribute(attributeName, attributeValue, namespace);
        final Attribute clonedAttribute = (Attribute) attribute.clone();

        assertTrue("incorrect name in clone", clonedAttribute.getName().equals(attributeName));
        assertTrue("incorrect value in clone", clonedAttribute.getValue().equals(attributeValue));
        assertEquals("incoorect attribute type in clone", clonedAttribute.getAttributeType(), Attribute.UNDECLARED_TYPE);

        assertTrue("incorrect prefix in clone", clonedAttribute.getNamespacePrefix().equals(prefix));
        assertTrue("incorrect qualified name in clone", clonedAttribute.getQualifiedName().equals(prefix + ':' + attributeName));
        assertTrue("incorrect Namespace URI in clone", clonedAttribute.getNamespaceURI().equals(uri));
    }

    /**
     * Test that an Attribute can clone itself correctly. The test covers the
     * case with name, value, prefix, namespace and a given attribute type.
     */
    private void TCM__Object_clone__Namespace_attributeType(final int attributeType) {
        final String prefix = "prefx";
        final String uri = "http://some.other.place";

        final Namespace namespace = Namespace.getNamespace(prefix, uri);

        final String attributeName = "test";
        final String attributeValue = "value";

        final Attribute attribute = new Attribute(attributeName, attributeValue, attributeType, namespace);
        final Attribute clonedAttribute = (Attribute) attribute.clone();

        assertTrue("incorrect name in clone", clonedAttribute.getName().equals(attributeName));
        assertTrue("incorrect value in clone", clonedAttribute.getValue().equals(attributeValue));
        assertEquals("incoorect attribute type in clone", clonedAttribute.getAttributeType(), attributeType);

        assertTrue("incorrect prefix in clone", clonedAttribute.getNamespacePrefix().equals(prefix));
        assertTrue("incorrect qualified name in clone", clonedAttribute.getQualifiedName().equals(prefix + ':' + attributeName));
        assertTrue("incorrect Namespace URI in clone", clonedAttribute.getNamespaceURI().equals(uri));
    }

    /**
     * Test that setting an Attribute's value works correctly.
     */
    public void test_TCM__OrgJdomAttribute_setValue_String() {
    	final Namespace namespace = Namespace.getNamespace("prefx", "http://some.other.place");

    	final Attribute attribute = new Attribute("test", "value", namespace);

    	assertTrue("incorrect value before set", attribute.getValue().equals("value"));

        attribute.setValue("foo");
    	assertTrue("incorrect value after set", attribute.getValue().equals("foo"));

    	//test that the verifier is called
    	try {
            attribute.setValue(null);
    		fail("Attribute setValue didn't catch null  string");
    	} catch (final IllegalDataException e) {
    	}

    	try {
    		final char c= 0x11;
    		final StringBuffer buffer = new StringBuffer("hhhh");
            buffer.setCharAt(2, c);
    		attribute.setValue(buffer.toString());
    		fail("Attribute setValue didn't catch invalid comment string");
    	} catch (final IllegalDataException e) {
    	}

    }

    /**
	 * check that the attribute can return the correct parent element
	 */
	public void test_TCM__OrgJdomElement_getParent() {
		final Attribute attribute = new Attribute("test", "value");
		assertNull("attribute returned parent when there was none", attribute.getParent());

		final Element element = new Element("element");
		element.setAttribute(attribute);

        assertNotNull("no parent element found", attribute.getParent());

        assertEquals("invalid parent element", attribute.getParent(), element);
	}

    /**
     * check that the attribute can return the correct document
     */
    public void test_TCM__OrgJdomDocument_getDocument() {
        final Attribute attribute = new Attribute("test", "value");
        assertNull("attribute returned document when there was none", attribute.getDocument());

        final Element element = new Element("element");
        element.setAttribute(attribute);
        assertNull("attribute returned document when there was none", attribute.getDocument());

        final Document document = new Document(element);

        assertEquals("invalid document", attribute.getDocument(), document);
    }

	/**
 	 * Test that an independantly created Namespace and one
  	 * retrieved from an Attribute create with the same namespace
 	 * parameters are the same namespace.
	 */
	public void test_TCM__OrgJdomNamespace_getNamespace() {
		final Namespace ns = Namespace.getNamespace("prefx", "http://some.other.place");

		final Attribute attr = new Attribute("test", "value", ns);
		final Namespace ns2 = Namespace.getNamespace("prefx", "http://some.other.place");

		assertTrue("incorrect Namespace", attr.getNamespace().equals(ns2));

	}

    /**
 	 * Test that an Attribute returns it's correct name.
	 */
	public void test_TCM__String_getName() {
		final Attribute attr = new Attribute("test", "value");
		assertTrue("incorrect attribute name", attr.getName().equals("test"));

	}

    /**
 	 * Test that an Attribute returns the correct Namespace prefix.
	 */
	public void test_TCM__String_getNamespacePrefix() {
		final Namespace namespace = Namespace.getNamespace("prefx", "http://some.other.place");

		final Attribute attribute = new Attribute("test", "value", namespace);
		assertTrue("incorrect prefix", attribute.getNamespacePrefix().equals("prefx"));
	}

    /**
	 * Test that an Attribute returns the correct Namespace URI.
	 */
	public void test_TCM__String_getNamespaceURI() {
		final Namespace namespace = Namespace.getNamespace("prefx", "http://some.other.place");

		final Attribute attribute = new Attribute("test", "value", namespace);
		assertTrue("incorrect URI", attribute.getNamespaceURI().equals("http://some.other.place"));

	}

    /**
	 * Tests that an Attribute returns the correct Qualified Name.
	 */
	public void test_TCM__String_getQualifiedName() {
        final String prefix = "prefx";
        final String uri = "http://some.other.place";

        final Namespace namespace = Namespace.getNamespace(prefix, uri);

        final String attributeName = "test";
        final String attributeQName = prefix + ':' + attributeName;
        final String attributeValue = "value";

        final Attribute qulifiedAttribute = new Attribute(attributeName, attributeValue, namespace);
        assertEquals("incorrect qualified name", qulifiedAttribute.getQualifiedName(), attributeQName);

        final Attribute attribute = new Attribute(attributeName, attributeValue);
        assertEquals("incorrect qualified name", attribute.getQualifiedName(), attributeName);

	}
	/**
	 * Test that Attribute returns the correct serialized form.
	 */
	public void test_TCM__String_getSerializedForm() {
		/* noop because the method is deprecated

		Namespace ns = Namespace.getNamespace("prefx", "http://some.other.place");
		Attribute attr = new Attribute("test", "value", ns);
		String serialized = attr.getSerializedForm();
		assertTrue("incorrect serialized form", serialized.equals("prefx:test=\"value\""));
		*/

	}

    /**
	 * Test that an Attribute returns the correct value.
	 */
	public void test_TCM__String_getValue() {
		final Namespace ns = Namespace.getNamespace("prefx", "http://some.other.place");
		final Attribute attr = new Attribute("test", "value", ns);
		assertTrue("incorrect value", attr.getValue().equals("value"));

	}

    /**
 	 * Test that the toString function works according to the
 	 * JDOM spec.
	 */
	public void test_TCM__String_toString() {
		//expected value
		final Namespace ns = Namespace.getNamespace("prefx", "http://some.other.place");
		final Attribute attr = new Attribute("test", "value", ns);
		String str= attr.toString();
		assertTrue("incorrect toString form", str.equals("[Attribute: prefx:test=\"value\"]"));

	}

    /**
     * Test that the detach() function works according to the JDOM spec.
     *
     * @see Attribute#detach()
     */
    public void test_TCM___detach() {
        final Element element = new Element("element");
        element.setAttribute("name", "value");

        final Attribute attribute = element.getAttribute("name");

        // should never occur, just to be sure
        assertNotNull("no attribute found", attribute);


        assertNotNull("no parent for attribute found", attribute.getParent());

        attribute.detach();

        assertNull("attribute has still a parent", attribute.getParent());
    }

    public void testSerialization() {
        serialization_default();
    }

    private void serialization_default() {
        final String attributeName = "test";
        final String attributeValue = "value";

        final Attribute attribute = new Attribute(attributeName, attributeValue);

        final Attribute serializedAttribute = deSerialize(attribute);

        assertTrue("incorrect name in serialized attribute", serializedAttribute.getName().equals(attributeName));
        assertTrue("incorrect value in serialized attribute", serializedAttribute.getValue().equals(attributeValue));
        assertEquals("incoorect attribute type in serialized attribute", serializedAttribute.getAttributeType(), Attribute.UNDECLARED_TYPE);

        assertEquals("incorrect Namespace in serialized attribute", serializedAttribute.getNamespace(), Namespace.NO_NAMESPACE);
    }

    private void serialization_Namespace() {
        final String prefix = "prefx";
        final String uri = "http://some.other.place";

        final Namespace namespace = Namespace.getNamespace(prefix, uri);

        final String attributeName = "test";
        final String attributeValue = "value";

        final Attribute attribute = new Attribute(attributeName, attributeValue, namespace);

        final Attribute serializedAttribute = deSerialize(attribute);

        assertTrue("incorrect name in serialized attribute", serializedAttribute.getName().equals(attributeName));
        assertTrue("incorrect value in serialized attribute", serializedAttribute.getValue().equals(attributeValue));
        assertEquals("incoorect attribute type in serialized attribute", serializedAttribute.getAttributeType(), Attribute.UNDECLARED_TYPE);

        assertTrue("incorrect prefix in serialized attribute", serializedAttribute.getNamespacePrefix().equals(prefix));
        assertTrue("incorrect qualified name in serialized attribute", serializedAttribute.getQualifiedName().equals(prefix + ':' + attributeName));
        assertTrue("incorrect Namespace URI in serialized attribute", serializedAttribute.getNamespaceURI().equals(uri));

        assertEquals("incorrect Namespace in serialized attribute", serializedAttribute.getNamespace(), namespace);
    }

    private Attribute deSerialize(final Attribute attribute) {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            final ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            try {
                objectOutputStream.writeObject(attribute);
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

        final InputStream inputStream = new ByteArrayInputStream(bytes);
        try {
            final ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            try {
                return (Attribute) objectInputStream.readObject();
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

        return null;
    }

    public void testInfinity() throws DataConversionException {
        Attribute infinity = new Attribute("name", "Infinity");
        Attribute neginfinity = new Attribute("name", "-Infinity");
        Attribute inf = new Attribute("name", "INF");
        Attribute neginf = new Attribute("name", "-INF");
        assertEquals(new Double(infinity.getDoubleValue()), new Double(Double.POSITIVE_INFINITY));
        assertEquals(new Double(neginfinity.getDoubleValue()), new Double(Double.NEGATIVE_INFINITY));
        assertEquals(new Double(inf.getDoubleValue()), new Double(Double.POSITIVE_INFINITY));
        assertEquals(new Double(neginf.getDoubleValue()), new Double(Double.NEGATIVE_INFINITY));
    }

}
