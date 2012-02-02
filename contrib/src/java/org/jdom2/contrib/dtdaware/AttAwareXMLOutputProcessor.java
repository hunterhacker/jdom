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

package org.jdom2.contrib.dtdaware;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.output.support.AbstractXMLOutputProcessor;
import org.jdom2.output.support.FormatStack;

/**
 * Implement a class that is sensitive to an input document's defaulted/implied
 * attributes, and will not output attributes that are implied.
 * <p>
 * You create an instance of this Processor, then tell it what attribute values
 * to ignore. You do that by calling the ignore() methods with the details of
 * what attributes (and what the required value of that attribute would be) to
 * ignore.
 * <p>
 * When the XML is output this processor will ignore those attributes which
 * match the 'ignore' specs.
 * 
 * @author Rolf Lear
 *
 */
public class AttAwareXMLOutputProcessor extends AbstractXMLOutputProcessor {
	/**
	 * Yeah, this is complicated, but it is basically a search tree index.
	 * This is a nested index of attribute details:
	 * <p>
	 * <ol>
	 * <li> at the top is the attribute name
	 * <li> then the Attribute value.
	 * <li> next is the Element name. We only check this level if the attribute
	 *      name and value matches to something that is ignored.
	 * <li> next we check to make sure that we are in the correct Attribute Namespace
	 * <li> then finally the Element namespace.
	 * </ol>
	 * <p>
	 * If we search this index tree and all the details match existing entries,
	 * then it is an ignored attribute. The order is set up in such a way that
	 * there should not need to be many checks in HashMaps for non-ignored
	 * content... essentiually we have to have the same attname and attvalue
	 * before the system even checks the Attribute's parent details.
	 */
	//            attname    attval     emtname    attns      emtns
	private final Map<String,Map<String,Map<String,Map<String,Set<String>>>>>
		ignores = new HashMap<String,Map<String,Map<String,Map<String,Set<String>>>>>();
	
	/**
	 * Construct this AttAwareXMLOutputProcessor.
	 * Add attribute to exclude using the 
	 * {@link #ignore(String, String, String, String, String)} method
	 */
	public AttAwareXMLOutputProcessor() {
		super();
	}
	

	/**
	 * Ignore an attribute (where the attribute and element are both in the 
	 * no-uri namespace).
	 * @param elementname The name of the Attribute's parent element.
	 * @param attname The name of the attribute.
	 * @param attvalue The attribute value to ignore.
	 */
	public void ignore(String elementname, String attname, String attvalue) {
		ignore("", elementname, "", attname, attvalue);
	}
	
	/**
	 * Ignore an attribute.
	 * @param elementuri The NamespaceURI of the Attribute's parent Element
	 * @param elementname The name of the Attribute's parent Element.
	 * @param atturi The Attribute's namespace URI.
	 * @param attname The name of the attribute.
	 * @param attvalue The attribute value.
	 */
	public void ignore(String elementuri, String elementname, 
			String atturi, String attname, String attvalue) {
		Map<String, Map<String, Map<String, Set<String>>>> av = 
				ignores.get(attname);
		if (av == null) {
			av = new HashMap<String, Map<String,Map<String,Set<String>>>>();
			ignores.put(attname, av);
		}
		Map<String, Map<String,Set<String>>> en = av.get(attvalue);
		if (en == null) {
			en = new HashMap<String, Map<String,Set<String>>>();
			av.put(attvalue, en);
		}
		Map<String, Set<String>> ans = en.get(elementname);
		if (ans == null) {
			ans = new HashMap<String, Set<String>>();
			en.put(elementname, ans);
		}
		Set<String> ens = ans.get(atturi);
		if (ens == null) {
			ens = new HashSet<String>();
			ans.put(atturi, ens);
		}
		ens.add(elementuri);
	}
	
	/**
	 * This extends the printAttribute code to search for attributes to ignore.
	 */
	@Override
	protected void printAttribute(Writer out, FormatStack fstack,
			Attribute attribute) throws IOException {
		// do we have anything to ignore.
		if (!ignores.isEmpty()) {
			// yes, there are ignores.
			final Map<String, Map<String, Map<String, Set<String>>>> av =
					ignores.get(attribute.getName());
			if (av != null) {
				// we are ignoring at least one attribute with this name.
				final Map<String, Map<String, Set<String>>> en = 
						av.get(attribute.getValue());
				if (en != null) {
					// we ignore something with the attribute name and value
					final Element e = attribute.getParent();
					final Map<String, Set<String>> ans = 
							en.get(e.getName());
					if (ans != null) {
						// and the same Element name
						final Set<String> ens = ans.get(attribute.getNamespaceURI());
						if (ens != null && ens.contains(e.getNamespaceURI())) {
							// and the same Attribute and Element namespace....
							// we match the ignored content...
							// skip this attribute.
							return;
						}
					}
				}
			}
		}
		// we are not ignoring this attribute.
		super.printAttribute(out, fstack, attribute);
	}
}
