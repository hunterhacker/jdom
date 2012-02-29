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

package org.jdom2.located;

import java.util.Map;

import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.DefaultJDOMFactory;
import org.jdom2.DocType;
import org.jdom2.Element;
import org.jdom2.EntityRef;
import org.jdom2.Namespace;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;

/**
 * All Content instances (Element, Comment, CDATA, DocType, Text, EntityRef,
 * and ProcessingInstruction) will implement {@link Located}, and will
 * have the values set appropriately.
 * <p>
 * You can set an instance of this LocatedJDOMFactory as the factory for a
 * SAXBuilder, and the JDOM document produced will have the SAX Location data
 * embedded. Note though, that SAX Location data indicates the position of the
 * <strong>end</strong> of the SAX Event. 
 * 
 * @author Rolf Lear
 *
 */
public class LocatedJDOMFactory extends DefaultJDOMFactory {

	@Override
	public CDATA cdata(int line, int col, String text) {
		final LCDATA ret = new LCDATA(text);
		ret.setLine(line);
		ret.setColumn(col);
		return ret;
	}

	@Override
	public Text text(int line, int col, String text) {
		final LText ret = new LText(text);
		ret.setLine(line);
		ret.setColumn(col);
		return ret;
	}

	@Override
	public Comment comment(int line, int col, String text) {
		final LComment ret = new LComment(text);
		ret.setLine(line);
		ret.setColumn(col);
		return ret;
	}

	@Override
	public DocType docType(int line, int col, String elementName,
			String publicID, String systemID) {
		final LDocType ret = new LDocType(elementName, publicID, systemID);
		ret.setLine(line);
		ret.setColumn(col);
		return ret;
	}

	@Override
	public DocType docType(int line, int col, String elementName,
			String systemID) {
		final LDocType ret = new LDocType(elementName, systemID);
		ret.setLine(line);
		ret.setColumn(col);
		return ret;
	}

	@Override
	public DocType docType(int line, int col, String elementName) {
		final LDocType ret = new LDocType(elementName);
		ret.setLine(line);
		ret.setColumn(col);
		return ret;
	}

	@Override
	public Element element(int line, int col, String name, Namespace namespace) {
		final LElement ret = new LElement(name, namespace);
		ret.setLine(line);
		ret.setColumn(col);
		return ret;
	}

	@Override
	public Element element(int line, int col, String name) {
		final LElement ret = new LElement(name);
		ret.setLine(line);
		ret.setColumn(col);
		return ret;
	}

	@Override
	public Element element(int line, int col, String name, String uri) {
		final LElement ret = new LElement(name, uri);
		ret.setLine(line);
		ret.setColumn(col);
		return ret;
	}

	@Override
	public Element element(int line, int col, String name, String prefix,
			String uri) {
		final LElement ret = new LElement(name, prefix, uri);
		ret.setLine(line);
		ret.setColumn(col);
		return ret;
	}

	@Override
	public ProcessingInstruction processingInstruction(int line, int col,
			String target) {
		final LProcessingInstruction ret = new LProcessingInstruction(target);
		ret.setLine(line);
		ret.setColumn(col);
		return ret;
	}

	@Override
	public ProcessingInstruction processingInstruction(int line, int col,
			String target, Map<String, String> data) {
		final LProcessingInstruction ret = new LProcessingInstruction(target, data);
		ret.setLine(line);
		ret.setColumn(col);
		return ret;
	}

	@Override
	public ProcessingInstruction processingInstruction(int line, int col,
			String target, String data) {
		final LProcessingInstruction ret = new LProcessingInstruction(target, data);
		ret.setLine(line);
		ret.setColumn(col);
		return ret;
	}

	@Override
	public EntityRef entityRef(int line, int col, String name) {
		final LEntityRef ret = new LEntityRef(name);
		ret.setLine(line);
		ret.setColumn(col);
		return ret;
	}

	@Override
	public EntityRef entityRef(int line, int col, String name, String publicID,
			String systemID) {
		final LEntityRef ret = new LEntityRef(name, publicID, systemID);
		ret.setLine(line);
		ret.setColumn(col);
		return ret;
	}

	@Override
	public EntityRef entityRef(int line, int col, String name, String systemID) {
		final LEntityRef ret = new LEntityRef(name, systemID);
		ret.setLine(line);
		ret.setColumn(col);
		return ret;
	}
	

}
