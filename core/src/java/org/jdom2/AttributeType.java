package org.jdom2;

import org.xml.sax.Attributes;

/**
 * Use a simple enumeration for the Attribute Types
 * 
 * @author Rolf Lear
 *
 */
public enum AttributeType {
	
	/**
	 * Attribute type: the attribute has not been declared or type
	 * is unknown.
	 *
	 * @see #getAttributeType
	 */
	UNDECLARED,
	
	/**
	 * Attribute type: the attribute value is a string.
	 *
	 * @see Attribute#getAttributeType
	 */
	CDATA,
	
	/**
	 * Attribute type: the attribute value is a unique identifier.
	 *
	 * @see #getAttributeType
	 */
	ID,

	/**
	 * Attribute type: the attribute value is a reference to a
	 * unique identifier.
	 *
	 * @see #getAttributeType
	 */
	IDREF,

	/**
	 * Attribute type: the attribute value is a list of references to
	 * unique identifiers.
	 *
	 * @see #getAttributeType
	 */
	IDREFS,
	/**
	 * Attribute type: the attribute value is the name of an entity.
	 *
	 * @see #getAttributeType
	 */
	ENTITY,
	/**
	 * <p>
	 * Attribute type: the attribute value is a list of entity names.
	 * </p>
	 *
	 * @see #getAttributeType
	 */
	ENTITIES,
	
	/**
	 * Attribute type: the attribute value is a name token.
	 * <p>
	 * According to SAX 2.0 specification, attributes of enumerated
	 * types should be reported as "NMTOKEN" by SAX parsers.  But the
	 * major parsers (Xerces and Crimson) provide specific values
	 * that permit to recognize them as {@link #ENUMERATION}.
	 *
	 * @see Attribute#getAttributeType
	 */
	NMTOKEN,
	/**
	 * Attribute type: the attribute value is a list of name tokens.
	 *
	 * @see #getAttributeType
	 */
	NMTOKENS,
	/**
	 * Attribute type: the attribute value is the name of a notation.
	 *
	 * @see #getAttributeType
	 */
	NOTATION,
	/**
	 * Attribute type: the attribute value is a name token from an
	 * enumeration.
	 *
	 * @see #getAttributeType
	 */
	ENUMERATION;
	
	/**
	 * Obtain a AttributeType by a int constant.
	 * This goes against the logic of enums, but this is used for backward
	 * compatibility. Thus, this method is marked Deprecated.
	 * @param index The AttributeType int equivalent to retrieve
	 * @return The AttributeType corresponding to the specified equivalent
	 * @throws IllegalArgumentException if there is no equivalent
	 * @deprecated Use normal Enums instead of int's
	 */
	@Deprecated
	public static final AttributeType byIndex(int index) {
		if (index < 0) {
			throw new IllegalDataException("No such AttributeType " + index);
		}
		if (index >= values().length)  {
			throw new IllegalDataException("No such AttributeType " + 
					index + ", max is " + (values().length - 1));
		}
		return values()[index];
	}
	
	/**
	 * Returns the the JDOM AttributeType value from the SAX 2.0
	 * attribute type string provided by the parser.
	 *
	 * @param typeName <code>String</code> the SAX 2.0 attribute
	 * type string.
	 *
	 * @return <code>int</code> the JDOM attribute type.
	 *
	 * @see Attribute#setAttributeType
	 * @see Attributes#getType
	 */
	public static final AttributeType getAttributeType(String typeName) {
		if (typeName == null) {
			return UNDECLARED;
		}
		
		try {
			return valueOf(typeName);
		} catch (IllegalArgumentException iae) {
			if (typeName.length() > 0 &&
					typeName.trim().charAt(0) == '(') {
				// Xerces 1.4.X reports attributes of enumerated type with
				// a type string equals to the enumeration definition, i.e.
				// starting with a parenthesis.
				return ENUMERATION;
			}
			return UNDECLARED;
		}
		
	}

}
