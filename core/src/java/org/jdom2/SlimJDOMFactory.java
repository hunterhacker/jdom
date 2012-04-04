/*--

 Copyright (C) 2012 Jason Hunter & Brett McLaughlin.
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

import java.util.Map;


/**
 * This JDOMFactory instance reduces the amount of memory used by JDOM content.
 * It does this by reusing String instances instead of using new (but equals())
 * instances. It uses the {@link StringBin} class to provide a String cache.
 * 
 * @see StringBin
 * @author Rolf Lear
 *
 */
public class SlimJDOMFactory extends DefaultJDOMFactory {
	
	private StringBin cache = new StringBin();
	private final boolean cachetext;
	
	/**
	 * 
	 */
	public SlimJDOMFactory() {
		this(true);
	}
	
	/**
	 * Construct a SlimJDOMFactory which will optionally cache Text/CDATA/Comment/Attribute
	 * values. Caching these values is recommended because often XML documents have
	 * many instances of the same Text values (especially whitespace sequences...)
	 * @param cachetext should be true if you want the content of CDATA, Text,
	 * Comment and Attribute values cached as well.
	 */
	public SlimJDOMFactory(final boolean cachetext) {
		super();
		this.cachetext = cachetext;
	}


	/**
	 * Reset any Cached String instance data from this SlimJDOMFaxctory cache.
	 */
	public void clearCache() {
		cache = new StringBin();
	}

	@Override
	public Attribute attribute(final String name, final String value, final Namespace namespace) {
		return super.attribute(cache.reuse(name), 
				(cachetext ? cache.reuse(value) : value), 
				namespace);
	}

	@Override
	@Deprecated
	public Attribute attribute(final String name, final String value, final int type,
			final Namespace namespace) {
		return super.attribute(cache.reuse(name),
				(cachetext ? cache.reuse(value) : value), 
				type, namespace);
	}

	@Override
	public Attribute attribute(final String name, final String value, final AttributeType type,
			Namespace namespace) {
		return super.attribute(cache.reuse(name),
				(cachetext ? cache.reuse(value) : value),
				type, namespace);
	}

	@Override
	public Attribute attribute(final String name, final String value) {
		return super.attribute(cache.reuse(name), 
				(cachetext ? cache.reuse(value) : value));
	}

	@Override
	@Deprecated
	public Attribute attribute(final String name, final String value, final int type) {
		return super.attribute(cache.reuse(name),
				(cachetext ? cache.reuse(value) : value), 
				type);
	}

	@Override
	public Attribute attribute(final String name, final String value, final AttributeType type) {
		return super.attribute(cache.reuse(name),
				(cachetext ? cache.reuse(value) : value), 
				type);
	}

	@Override
	public CDATA cdata(final int line, final int col, final String str) {
		return super.cdata(line, col, (cachetext ? cache.reuse(str) : str));
	}

	@Override
	public Text text(final int line, final int col, final String str) {
		return super.text(line, col, (cachetext ? cache.reuse(str) : str));
	}

	@Override
	public Comment comment(final int line, final int col, final String text) {
		return super.comment(line, col, (cachetext ? cache.reuse(text) : text));
	}

	@Override
	public DocType docType(final int line, final int col, final String elementName, final String publicID, final String systemID) {
		return super.docType(line, col, cache.reuse(elementName), publicID, systemID);
	}

	@Override
	public DocType docType(final int line, final int col, final String elementName, final String systemID) {
		return super.docType(line, col, cache.reuse(elementName), systemID);
	}

	@Override
	public DocType docType(final int line, final int col, final String elementName) {
		return super.docType(line, col, cache.reuse(elementName));
	}

	@Override
	public Element element(final int line, final int col, final String name, final Namespace namespace) {
		return super.element(line, col, cache.reuse(name), namespace);
	}

	@Override
	public Element element(final int line, final int col, final String name) {
		return super.element(line, col, cache.reuse(name));
	}

	@Override
	public Element element(final int line, final int col, final String name, final String uri) {
		return super.element(line, col, cache.reuse(name), uri);
	}

	@Override
	public Element element(final int line, final int col, final String name, final String prefix, final String uri) {
		return super.element(line, col, cache.reuse(name), prefix, uri);
	}

	@Override
	public ProcessingInstruction processingInstruction(final int line, final int col, final String target,
			final Map<String, String> data) {
		return super.processingInstruction(line, col, cache.reuse(target), data);
	}

	@Override
	public ProcessingInstruction processingInstruction(final int line, final int col, final String target,
			final String data) {
		return super.processingInstruction(line, col, cache.reuse(target), data);
	}

	@Override
	public ProcessingInstruction processingInstruction(final int line, final int col, final String target) {
		return super.processingInstruction(line, col, cache.reuse(target));
	}

	@Override
	public EntityRef entityRef(final int line, final int col, final String name) {
		return super.entityRef(line, col, cache.reuse(name));
	}

	@Override
	public EntityRef entityRef(final int line, final int col, final String name, final String publicID, final String systemID) {
		return super.entityRef(line, col, cache.reuse(name), publicID, systemID);
	}

	@Override
	public EntityRef entityRef(final int line, final int col, final String name, final String systemID) {
		return super.entityRef(line, col, cache.reuse(name), systemID);
	}

}
