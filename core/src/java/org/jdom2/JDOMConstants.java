package org.jdom2;

import javax.xml.transform.TransformerFactory;

import org.jdom2.output.LineSeparator;
import org.jdom2.transform.JDOMResult;
import org.jdom2.transform.JDOMSource;
import org.jdom2.xpath.XPathFactory;


/**
 * A collection of constants that may be useful to JDOM users.
 * <p>
 * JDOM attempts to make knowing these 'magic' constants unnecessary, but it is
 * not always possible. In an attempt to make it easier though, common constants
 * are defined here. This is not a comprehensive list of all the constants that
 * may be useful when processing XML, but it should cover most of those occasions
 * where JDOM does not automatically do the 'right thing'.
 * <p>
 * Many of these constants are already referenced inside the JDOM code.
 * 
 * @author Rolf Lear
 *
 */
public final class JDOMConstants {
	
	/**
	 * Keep out of public reach.
	 */
	private JDOMConstants () {
		// private default constructor.
	}
	
	/*
	 * XML Namespace constants
	 */
	
	/** Defined as {@value} */
	public static final String NS_PFX_DEFAULT = "";
	/** Defined as {@value} */
	public static final String NS_URI_DEFAULT = "";

	/** Defined as {@value} */
	public static final String NS_PFX_XML = "xml";
	/** Defined as {@value} */
	public static final String NS_URI_XML = "http://www.w3.org/XML/1998/namespace";
	
	/** Defined as {@value} */
	public static final String NS_PFX_XMLNS = "xmlns";
	/** Defined as {@value} */
	public static final String NS_URI_XMLNS = "http://www.w3.org/XML/1998/namespace";
	
	
	/** Defined as {@value} */
	public static final String SAX_PROPERTY_DECLARATION_HANDLER = 
			"http://xml.org/sax/properties/declaration-handler";
	
	/** Defined as {@value} */
	public static final String SAX_PROPERTY_DECLARATION_HANDLER_ALT = 
			"http://xml.org/sax/handlers/DeclHandler";
	
	/** Defined as {@value} */
	public static final String SAX_PROPERTY_LEXICAL_HANDLER = 
			"http://xml.org/sax/properties/lexical-handler";
	
	/** Defined as {@value} */
	public static final String SAX_PROPERTY_LEXICAL_HANDLER_ALT = 
			"http://xml.org/sax/handlers/LexicalHandler";

	
	
	/** Defined as {@value} */
	public static final String SAX_FEATURE_EXTERNAL_ENT = 
			"http://xml.org/sax/features/external-general-entities";
	
	/** Defined as {@value} */
	public static final String SAX_FEATURE_VALIDATION =
			"http://xml.org/sax/features/validation";
	
	/** Defined as {@value} */
	public static final String SAX_FEATURE_NAMESPACES =
			"http://xml.org/sax/features/namespaces";
	
	/** Defined as {@value} */
	public static final String SAX_FEATURE_NAMESPACE_PREFIXES =
			"http://xml.org/sax/features/namespace-prefixes";

	
	/**
	 * Constant used to define the {@link JDOMSource} with JAXP.
	 * Defined as {@value}
	 * @see JDOMSource
	 */
	public static final String JDOM2_FEATURE_JDOMSOURCE = 
			"http://jdom.org/jdom2/transform/JDOMSource/feature";
	
	/**
	 * Constant used to define the {@link JDOMResult} with {@link TransformerFactory}.
	 * Defined as {@value}
	 * @see JDOMResult
	 */
	public static final String JDOM2_FEATURE_JDOMRESULT = 
			"http://jdom.org/jdom2/transform/JDOMResult/feature";
	
	/**
	 * System Property queried to obtain an alternate default XPathFactory.
	 * Defined as {@value}
	 * @see XPathFactory
	 */
	public static final String JDOM2_PROPERTY_XPATH_FACTORY =
			"org.jdom2.xpath.XPathFactory";
	
	/**
	 * System Property queried to obtain an alternate default Line Separator.
	 * <p>
	 * Defined as {@value}
	 * @see LineSeparator
	 */
	public static final String JDOM2_PROPERTY_LINE_SEPARATOR =
			"org.jdom2.output.LineSeparator";
	
}
