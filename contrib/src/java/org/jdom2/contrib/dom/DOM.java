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

package org.jdom2.contrib.dom;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import org.jdom2.Content;

/**
 * Access JDOM Content using a (Read-Only) DOM model
 * 
 * @author Rolf Lear
 *
 */
public final class DOM {
	/**
	 * Wrap a JDOM Document in a org.w3c.dom.Document instance.
	 * @param doc The JDOM Document to wrap.
	 * @return the wrapped Document
	 */
	public static final Document wrap(final org.jdom2.Document doc) {
		return wrap(doc, false);
	}

	/**
	 * Wrap a JDOM Document in a org.w3c.dom.Document instance.
	 * @param doc The JDOM Document to wrap.
	 * @param scan Whether the entire document should be pre-processed
	 * @return the wrapped Document
	 */
	public static final Document wrap(
			final org.jdom2.Document doc, final boolean scan) {
		final JDocument ret = new JDocument(doc);
		if (scan) {
			ret.scanAll();
		}
		return ret;
	}

	private static final JDocument makeDoc(final Content c) {
		final org.jdom2.Document doc = c.getDocument();
		return new JDocument(doc);
	}

	/**
	 * Wrap a JDOM Element in a org.w3c.dom.Element instance.
	 * @param emt The JDOM Element to wrap.
	 * @return the wrapped Element
	 */
	public static final Element wrap(final org.jdom2.Element emt) {
		return makeDoc(emt).find(emt);
	}


	/**
	 * Wrap a JDOM Attribute in a org.w3c.dom.Attr instance.
	 * @param att The JDOM Attribute to wrap.
	 * @return the wrapped Attribute
	 */
	public static final Attr wrap(final org.jdom2.Attribute att) {
		final org.jdom2.Document doc = att.getDocument();
		final JDocument jd = new JDocument(doc);
		return jd.find(att);
	}



	/**
	 * Wrap a JDOM Text in a org.w3c.dom.Text instance.
	 * @param text The JDOM Text to wrap.
	 * @return the wrapped Text
	 */
	public static final Text wrap(final org.jdom2.Text text) {
		return makeDoc(text).find(text);
	}

	/**
	 * Wrap a JDOM CDATA in a org.w3c.dom.CDATASection instance.
	 * @param cdata The JDOM CDATA to wrap.
	 * @return the wrapped CDATA
	 */
	public static final CDATASection wrap(final org.jdom2.CDATA cdata) {
		return makeDoc(cdata).find(cdata);
	}

	/**
	 * Wrap a JDOM EntityRef in a org.w3c.dom.EntityReference instance.
	 * @param eref The JDOM EntityRef to wrap.
	 * @return the wrapped EtityRef
	 */
	public static final EntityReference wrap(final org.jdom2.EntityRef eref) {
		return makeDoc(eref).find(eref);
	}

	/**
	 * Wrap a JDOM ProcessingInstruction in a org.w3c.dom.ProcessingInstruction
	 * instance.
	 * @param pi The JDOM ProcessingInstruction to wrap.
	 * @return the wrapped ProcessingInstrction
	 */
	public static final ProcessingInstruction wrap(
			final org.jdom2.ProcessingInstruction pi) {
		return makeDoc(pi).find(pi);
	}

	/**
	 * Wrap a JDOM Comment in a org.w3c.dom.Comment instance.
	 * @param comment The JDOM Comment to wrap.
	 * @return the wrapped Comment
	 */
	public static final Comment wrap(final org.jdom2.Comment comment) {
		return makeDoc(comment).find(comment);
	}

	/**
	 * Wrap a JDOM DocType in a org.w3c.dom.DocumentType instance.
	 * @param dt The JDOM DocType to wrap.
	 * @return the wrapped DocType
	 */
	public static final DocumentType wrap(final org.jdom2.DocType dt) {
		return makeDoc(dt).find(dt);
	}

}
