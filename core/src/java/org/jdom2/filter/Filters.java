/*-- 

 Copyright (C) 2011-2012 Jason Hunter & Brett McLaughlin.
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
