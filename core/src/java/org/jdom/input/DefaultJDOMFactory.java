/*--

 $Id: DefaultJDOMFactory.java,v 1.7 2002/04/28 08:44:29 jhunter Exp $

 Copyright (C) 2000 Jason Hunter & Brett McLaughlin.
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
    written permission, please contact <pm AT jdom DOT org>.

 4. Products derived from this software may not be called "JDOM", nor
    may "JDOM" appear in their name, without prior written permission
    from the JDOM Project Management <pm AT jdom DOT org>.

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
 created by Jason Hunter <jhunter AT jdom DOT org> and
 Brett McLaughlin <brett AT jdom DOT org>.  For more information
 on the JDOM Project, please see <http://www.jdom.org/>.

 */

package org.jdom.input;

import org.jdom.*;
import java.util.*;

/**
 * <p><code>DefaultJDOMFactory</code> creates the standard
 *   top-level JDOM classes (Element, Document, Comment, etc).
 *   A subclass of this factory might construct custom classes.
 * </p>
 *
 * @author Ken Rune Holland
 * @author Phil Nelson
 * @author Bradley S. Huffman
 * @version $Revision: 1.7 $, $Date: 2002/04/28 08:44:29 $
 */
public class DefaultJDOMFactory implements JDOMFactory {

    private static final String CVS_ID =
    "@(#) $RCSfile: DefaultJDOMFactory.java,v $ $Revision: 1.7 $ $Date: 2002/04/28 08:44:29 $ $Name:  $";

    public DefaultJDOMFactory() { }

    // Allow Javadocs to inherit from JDOMFactory

    public Attribute attribute(String name, String value, Namespace namespace) {
        return new Attribute(name, value, namespace);
    }

    public Attribute attribute(String name, String value,
                                            int type, Namespace namespace) {
        return new Attribute(name, value, type, namespace);
    }

    public Attribute attribute(String name, String value) {
        return new Attribute(name, value);
    }

    public Attribute attribute(String name, String value, int type) {
        return new Attribute(name, value, type);
    }

    public CDATA cdata(String text) {
        return new CDATA(text);
    }

    public Text text(String text) {
        return new Text(text);
    }

    public Comment comment(String text) {
        return new Comment(text);
    }

    public DocType docType(String elementName, 
                           String publicID, String systemID) {
        return new DocType(elementName, publicID, systemID);
    }

    public DocType docType(String elementName, String systemID) {
        return new DocType(elementName, systemID);
    }

    public DocType docType(String elementName) {
        return new DocType(elementName);
    }

    public Document document(Element rootElement, DocType docType) {
        return new Document(rootElement, docType);
    }

    public Document document(Element rootElement) {
        return new Document(rootElement);
    }

    public Element element(String name, Namespace namespace) {
        return new Element(name, namespace);
    }

    public Element element(String name) {
        return new Element(name);
    }

    public Element element(String name, String uri) {
        return new Element(name, uri);
    }

    public Element element(String name, String prefix, String uri) {
        return new Element(name, prefix, uri);
    }

    public ProcessingInstruction processingInstruction(String target, 
                                                       Map data) {
        return new ProcessingInstruction(target, data);
    }

    public ProcessingInstruction processingInstruction(String target, 
                                                       String data) {
        return new ProcessingInstruction(target, data);
    }

    public EntityRef entityRef(String name) {
        return new EntityRef(name);
    }

    public EntityRef entityRef(String name, String publicID, String systemID) {
        return new EntityRef(name, publicID, systemID);
    }
}
