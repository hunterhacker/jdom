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

import org.jdom2.util.StringBin;

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
	public SlimJDOMFactory(boolean cachetext) {
		super();
		this.cachetext = cachetext;
	}



	@Override
	public Attribute attribute(String name, String value, Namespace namespace) {
		return super.attribute(cache.reuse(name), 
				(cachetext ? cache.reuse(value) : value), 
				namespace);
	}

	@Override
	@Deprecated
	public Attribute attribute(String name, String value, int type,
			Namespace namespace) {
		return super.attribute(cache.reuse(name),
				(cachetext ? cache.reuse(value) : value), 
				type, namespace);
	}

	@Override
	public Attribute attribute(String name, String value, AttributeType type,
			Namespace namespace) {
		return super.attribute(cache.reuse(name),
				(cachetext ? cache.reuse(value) : value),
				type, namespace);
	}

	@Override
	public Attribute attribute(String name, String value) {
		return super.attribute(cache.reuse(name), 
				(cachetext ? cache.reuse(value) : value));
	}

	@Override
	@Deprecated
	public Attribute attribute(String name, String value, int type) {
		return super.attribute(cache.reuse(name),
				(cachetext ? cache.reuse(value) : value), 
				type);
	}

	@Override
	public Attribute attribute(String name, String value, AttributeType type) {
		return super.attribute(cache.reuse(name),
				(cachetext ? cache.reuse(value) : value), 
				type);
	}

	@Override
	public CDATA cdata(String str) {
		return super.cdata((cachetext ? cache.reuse(str) : str));
	}

	@Override
	public Text text(String str) {
		return super.text((cachetext ? cache.reuse(str) : str));
	}

	@Override
	public Comment comment(String text) {
		return super.comment((cachetext ? cache.reuse(text) : text));
	}

	@Override
	public DocType docType(String elementName, String publicID, String systemID) {
		return super.docType(cache.reuse(elementName), publicID, systemID);
	}

	@Override
	public DocType docType(String elementName, String systemID) {
		return super.docType(cache.reuse(elementName), systemID);
	}

	@Override
	public DocType docType(String elementName) {
		return super.docType(cache.reuse(elementName));
	}

	@Override
	public Element element(String name, Namespace namespace) {
		return super.element(cache.reuse(name), namespace);
	}

	@Override
	public Element element(String name) {
		return super.element(cache.reuse(name));
	}

	@Override
	public Element element(String name, String uri) {
		return super.element(cache.reuse(name), uri);
	}

	@Override
	public Element element(String name, String prefix, String uri) {
		return super.element(cache.reuse(name), prefix, uri);
	}

	@Override
	public ProcessingInstruction processingInstruction(String target,
			Map<String, String> data) {
		return super.processingInstruction(cache.reuse(target), data);
	}

	@Override
	public ProcessingInstruction processingInstruction(String target,
			String data) {
		return super.processingInstruction(cache.reuse(target), data);
	}

	@Override
	public ProcessingInstruction processingInstruction(String target) {
		return super.processingInstruction(cache.reuse(target));
	}

	@Override
	public EntityRef entityRef(String name) {
		return super.entityRef(cache.reuse(name));
	}

	@Override
	public EntityRef entityRef(String name, String publicID, String systemID) {
		return super.entityRef(cache.reuse(name), publicID, systemID);
	}

	@Override
	public EntityRef entityRef(String name, String systemID) {
		return super.entityRef(cache.reuse(name), systemID);
	}

}
