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

import org.jdom2.Attribute;
import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.Content;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.EntityRef;
import org.jdom2.Namespace;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;

/**
 * Factory class of convenience methods to create Filter instances of common
 * types. Methods that return Filters that act on core JDOM classes (Element,
 * Text, etc.) are simply named after the content they return.
 * <p>
 * Filters that
 * match non-core classes (Boolean, Object, etc.) are all prefixed with the
 * letter 'f' (for <strong>f</strong>ilter).
 * <p>
 * The Filter returned by {@link #fpassthrough()} is not really a filter in the
 * sense that it will never filter anything out - everything matches. This can
 * be useful to accomplish some tasks, for example the JDOM XPath API uses it
 * extensively.
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
	
	private static final Filter<Text> ftextonly = new TextOnlyFilter();
	
	private static final Filter<Element> felement = 
			new ClassFilter<Element>(Element.class);
	
	private static final Filter<Document> fdocument = 
			new ClassFilter<Document>(Document.class);

	private static final Filter<Double> fdouble = 
			new ClassFilter<Double>(Double.class);

	private static final Filter<Boolean> fboolean = 
			new ClassFilter<Boolean>(Boolean.class);

	private static final Filter<String> fstring = 
			new ClassFilter<String>(String.class);
	
	private static final Filter<Object> fpassthrough =
			new PassThroughFilter();


	private Filters() {
		// do nothing... make instances impossible.
	}

	/**
	 * Return a Filter that matches any {@link Content} data.
	 * 
	 * @return a Filter that matches any {@link Content} data.
	 */
	public static final Filter<Content> content() {
		return fcontent;
	}

	/**
	 * Return a Filter that matches any {@link Attribute} data.
	 * 
	 * @return a Filter that matches any {@link Attribute} data.
	 */
	public static final Filter<Attribute> attribute() {
		return fattribute;
	}

	/**
	 * Return a Filter that matches any {@link Attribute} data with the 
	 * specified name.
	 * 
	 * @param name The name for all the Attributes to have (these can be in any
	 * Namespace).
	 * @return a Filter that matches any {@link Attribute} data with the 
	 * specified name.
	 */
	public static final Filter<Attribute> attribute(String name) {
		return new AttributeFilter(name);
	}

	/**
	 * Return a Filter that matches any {@link Attribute} data with the
	 * specified name and namespace.
	 * 
	 * @param name The name for all the Attributes to have.
	 * @param ns The Namespace for all the Attributes to have.
	 * @return a Filter that matches any {@link Attribute} data with the
	 * specified name and namespace.
	 */
	public static final Filter<Attribute> attribute(String name, Namespace ns) {
		return new AttributeFilter(name, ns);
	}

	/**
	 * Return a Filter that matches any {@link Attribute} data with the
	 * specified namespace.
	 * 
	 * @param ns The Namespace for all the Attributes to have.
	 * @return a Filter that matches any {@link Attribute} data with the
	 * specified namespace.
	 */
	public static final Filter<Attribute> attribute(Namespace ns) {
		return new AttributeFilter(ns);
	}

	/**
	 * Return a Filter that matches any {@link Comment} data.
	 * 
	 * @return a Filter that matches any {@link Comment} data.
	 */
	public static final Filter<Comment> comment() {
		return fcomment;
	}

	/**
	 * Return a Filter that matches any {@link CDATA} data.
	 * 
	 * @return a Filter that matches any {@link CDATA} data.
	 */
	public static final Filter<CDATA> cdata() {
		return fcdata;
	}

	/**
	 * Return a Filter that matches any {@link DocType} data.
	 * 
	 * @return a Filter that matches any {@link DocType} data.
	 */
	public static final Filter<DocType> doctype() {
		return fdoctype;
	}

	/**
	 * Return a Filter that matches any {@link EntityRef} data.
	 * 
	 * @return a Filter that matches any {@link EntityRef} data.
	 */
	public static final Filter<EntityRef> entityref() {
		return fentityref;
	}

	/**
	 * Return a Filter that matches any {@link Element} data.
	 * 
	 * @return a Filter that matches any {@link Element} data.
	 */
	public static final Filter<Element> element() {
		return felement;
	}

	/**
	 * Return a Filter that matches any {@link Document} data.
	 * 
	 * @return a Filter that matches any {@link Document} data.
	 */
	public static final Filter<Document> document() {
		return fdocument;
	}

	/**
	 * Return a Filter that matches any {@link Element} data with the specified
	 * name.
	 * 
	 * @param name The name of Elements to match.
	 * @return a Filter that matches any {@link Element} data with the specified
	 * name.
	 */
	public static final Filter<Element> element(String name) {
		return new ElementFilter(name, Namespace.NO_NAMESPACE);
	}

	/**
	 * Return a Filter that matches any {@link Element} data with the specified
	 * name and Namespace.
	 * 
	 * @param name The name of Elements to match.
	 * @param ns The Namespace to match
	 * @return a Filter that matches any {@link Element} data with the specified
	 * name and Namespace.
	 */
	public static final Filter<Element> element(String name, Namespace ns) {
		return new ElementFilter(name, ns);
	}

	/**
	 * Return a Filter that matches any {@link Element} data with the specified
	 * Namespace.
	 * 
	 * @param ns The Namespace to match
	 * @return a Filter that matches any {@link Element} data with the specified
	 * Namespace.
	 */
	public static final Filter<Element> element(Namespace ns) {
		return new ElementFilter(null, ns);
	}

	/**
	 * Return a Filter that matches any {@link ProcessingInstruction} data.
	 * 
	 * @return a Filter that matches any {@link ProcessingInstruction} data.
	 */
	public static final Filter<ProcessingInstruction> processinginstruction() {
		return fpi;
	}

	/**
	 * Return a Filter that matches any {@link Text} data (which includes 
	 * {@link CDATA} since that is a subclass of Text).
	 * 
	 * @return a Filter that matches any {@link Text} data (which includes 
	 * {@link CDATA} since that is a subclass of Text).
	 */
	public static final Filter<Text> text() {
		return ftext;
	}

	/**
	 * Return a Filter that matches any {@link Text} data (excludes 
	 * {@link CDATA} instances).
	 * 
	 * @return a Filter that matches any {@link Text} data (which excludes 
	 * {@link CDATA} instances).
	 */
	public static final Filter<Text> textOnly() {
		return ftextonly;
	}

	/**
	 * Return a Filter that matches any Boolean data.
	 * 
	 * @return a Filter that matches any Boolean data.
	 */
	public static final Filter<Boolean> fboolean() {
		return fboolean;
	}

	/**
	 * Return a Filter that matches any String data.
	 * 
	 * @return a Filter that matches any String data.
	 */
	public static final Filter<String> fstring() {
		return fstring;
	}

	/**
	 * Return a Filter that matches any Double data.
	 * 
	 * @return a Filter that matches any Double data.
	 */
	public static final Filter<Double> fdouble() {
		return fdouble;
	}
	
	/**
	 * Return a Filter that matches any data of the specified Class.
	 * 
	 * @param <F> The generic type of the content returned by this Filter
	 * @param clazz the Class type to match in the filter
	 * @return a Filter that matches any data of the specified Class.
	 */
	public static final <F> Filter<F> fclass(Class<F> clazz) {
		return new ClassFilter<F>(clazz);
	}

	/**
	 * Return a filter that does no filtering at all - everything matches.
	 * @return A Pass-Through Filter.
	 */
	public static final Filter<Object> fpassthrough() {
		return fpassthrough;
	}

}
