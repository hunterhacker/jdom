package org.jdom2.filter;

import org.jdom2.*;

/**
 * Factory class of convenience methods to create Filter instances of common
 * types.
 * 
 * @author Rolf Lear
 *
 */
public final class Filters {

	private static final Filter<Content> fcontent = 
			new ClassFilter<Content>(Content.class);

	private static final Filter<Attribute> fattribute = 
			new AttributeFilter();
	private static final Filter<Comment> fcomment = 
			new ClassFilter<Comment>(Comment.class);
	private static final Filter<CDATA> fcdata = 
			new ClassFilter<CDATA>(CDATA.class);
	private static final Filter<DocType> fdoctype = 
			new ClassFilter<DocType>(DocType.class);
	private static final Filter<EntityRef> fentityref = 
			new ClassFilter<EntityRef>(EntityRef.class);
	private static final Filter<ProcessingInstruction> fpi = 
			new ClassFilter<ProcessingInstruction>(ProcessingInstruction.class);
	private static final Filter<Text> ftext = 
			new ClassFilter<Text>(Text.class);
	private static final Filter<Element> felement = 
			new ClassFilter<Element>(Element.class);

	private static final Filter<Double> fdouble = 
			new ClassFilter<Double>(Double.class);

	private static final Filter<Boolean> fboolean = 
			new ClassFilter<Boolean>(Boolean.class);

	private static final Filter<String> fstring = 
			new ClassFilter<String>(String.class);


	private Filters() {
		// do nothing... make instances impossible.
	}

	/**
	 * @return a Filter that matches any {@link Content} data.
	 */
	public static final Filter<Content> content() {
		return fcontent;
	}

	/**
	 * @return a Filter that matches any {@link Attribute} data.
	 */
	public static final Filter<Attribute> attribute() {
		return fattribute;
	}

	/**
	 * @param name The name for all the Attributes to have (these can be in any
	 * Namespace).
	 * @return a Filter that matches any {@link Attribute} data with the 
	 * specified name.
	 */
	public static final Filter<Attribute> attribute(String name) {
		return new AttributeFilter(name);
	}

	/**
	 * @param name The name for all the Attributes to have.
	 * @param ns The Namespace for all the Attributes to have.
	 * @return a Filter that matches any {@link Attribute} data with the
	 * specified name and namespace.
	 */
	public static final Filter<Attribute> attribute(String name, Namespace ns) {
		return new AttributeFilter(name, ns);
	}

	/**
	 * @param ns The Namespace for all the Attributes to have.
	 * @return a Filter that matches any {@link Attribute} data with the
	 * specified namespace.
	 */
	public static final Filter<Attribute> attribute(Namespace ns) {
		return new AttributeFilter(ns);
	}

	/**
	 * @return a Filter that matches any {@link Comment} data.
	 */
	public static final Filter<Comment> comment() {
		return fcomment;
	}

	/**
	 * @return a Filter that matches any {@link CDATA} data.
	 */
	public static final Filter<CDATA> cdata() {
		return fcdata;
	}

	/**
	 * @return a Filter that matches any {@link DocType} data.
	 */
	public static final Filter<DocType> doctype() {
		return fdoctype;
	}

	/**
	 * @return a Filter that matches any {@link EntityRef} data.
	 */
	public static final Filter<EntityRef> entityref() {
		return fentityref;
	}

	/**
	 * @return a Filter that matches any {@link Element} data.
	 */
	public static final Filter<Element> element() {
		return felement;
	}

	/**
	 * @param name The name of Elements to match.
	 * @return a Filter that matches any {@link Element} data with the specified
	 * name.
	 */
	public static final Filter<Element> element(String name) {
		return new ElementFilter(name, Namespace.NO_NAMESPACE);
	}

	/**
	 * @param name The name of Elements to match.
	 * @param ns The Namespace to match
	 * @return a Filter that matches any {@link Element} data with the specified
	 * name and Namespace.
	 */
	public static final Filter<Element> element(String name, Namespace ns) {
		return new ElementFilter(name, ns);
	}

	/**
	 * @param ns The Namespace to match
	 * @return a Filter that matches any {@link Element} data with the specified
	 * Namespace.
	 */
	public static final Filter<Element> element(Namespace ns) {
		return new ElementFilter(null, ns);
	}

	/**
	 * @return a Filter that matches any {@link ProcessingInstruction} data.
	 */
	public static final Filter<ProcessingInstruction> processinginstruction() {
		return fpi;
	}

	/**
	 * @return a Filter that matches any {@link Text} data.
	 */
	public static final Filter<Text> text() {
		return ftext;
	}

	/**
	 * @return a Filter that matches any Boolean data.
	 */
	public static final Filter<Boolean> fboolean() {
		return fboolean;
	}

	/**
	 * @return a Filter that matches any String data.
	 */
	public static final Filter<String> fstring() {
		return fstring;
	}

	/**
	 * @return a Filter that matches any Double data.
	 */
	public static final Filter<Double> fdouble() {
		return fdouble;
	}
	
	/**
	 * @param <F> The generic type of the content returned by this Filter
	 * @param clazz the Class type to match in the filter
	 * @return a Filter that matches any data of the specified Class.
	 */
	public static final <F> Filter<F> fclass(Class<F> clazz) {
		return new ClassFilter<F>(clazz);
	}


}
