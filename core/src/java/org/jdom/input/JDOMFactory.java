/*--

 $Id: JDOMFactory.java,v 1.4 2001/12/11 07:32:05 jhunter Exp $

 Copyright (C) 2000 Brett McLaughlin & Jason Hunter.
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
    written permission, please contact license@jdom.org.

 4. Products derived from this software may not be called "JDOM", nor
    may "JDOM" appear in their name, without prior written permission
    from the JDOM Project Management (pm@jdom.org).

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
 created by Brett McLaughlin <brett@jdom.org> and
 Jason Hunter <jhunter@jdom.org>.  For more information on the
 JDOM Project, please see <http://www.jdom.org/>.

 */

package org.jdom.input;

import org.jdom.*;
import java.util.Map;

/**
 * <p><code>JDOMFactory</code> is an interface to be used by builders
 *   in constructing JDOM objects.  The <code>DefaultJDOMFactory</code> 
 *   creates the standard top-level JDOM classes (Element, Document, 
 *   Comment, etc).  Another implementation of this factory could be used
 *   to create custom classes.
 * </p>
 *
 * @author Ken Rune Holland
 * @author Phil Nelson
 * @author Bradley S. Huffman
 * @version 1.0
 */
public interface JDOMFactory {

    // **** constructing Attributes ****

    /**
     * <p>
     * This will create a new <code>Attribute</code> with the
     *   specified (local) name and value, and in the provided
     *   <code>{@link Namespace}</code>.
     * </p>
     *
     * @param name <code>String</code> name of <code>Attribute</code>.
     * @param value <code>String</code> value for new attribute.
     */
    public Attribute attribute(String name, String value, Namespace namespace);

    /**
     * <p>
     * This will create a new <code>Attribute</code> with the
     *   specified (local) name, value, and type, and in the provided
     *   <code>{@link Namespace}</code>.
     * </p>
     *
     * @param name <code>String</code> name of <code>Attribute</code>.
     * @param value <code>String</code> value for new attribute.
     * @param type <code>int</code> type for new attribute.
     * @param namespace <code>Namespace</code> namespace for new attribute.
     */
    public Attribute attribute(String name, String value,
                                            int type, Namespace namespace);

    /**
     * <p>
     * This will create a new <code>Attribute</code> with the
     *   specified (local) name and value, and does not place
     *   the attribute in a <code>{@link Namespace}</code>.
     * </p><p>
     *  <b>Note</b>: This actually explicitly puts the
     *    <code>Attribute</code> in the "empty" <code>Namespace</code>
     *    (<code>{@link Namespace#NO_NAMESPACE}</code>).
     * </p>
     *
     * @param name <code>String</code> name of <code>Attribute</code>.
     * @param value <code>String</code> value for new attribute.
     */
    public Attribute attribute(String name, String value);

    /**
     * <p>
     * This will create a new <code>Attribute</code> with the
     *   specified (local) name, value and type, and does not place
     *   the attribute in a <code>{@link Namespace}</code>.
     * </p><p>
     *  <b>Note</b>: This actually explicitly puts the
     *    <code>Attribute</code> in the "empty" <code>Namespace</code>
     *    (<code>{@link Namespace#NO_NAMESPACE}</code>).
     * </p>
     *
     * @param name <code>String</code> name of <code>Attribute</code>.
     * @param value <code>String</code> value for new attribute.
     * @param type <code>int</code> type for new attribute.
     */
    public Attribute attribute(String name, String value, int type);

    // **** constructing CDATA ****

    /**
     * <p>
     * This creates the CDATA with the supplied
     *   text.
     * </p>
     *
     * @param str <code>String</code> content of CDATA.
     */
    public CDATA cdata(String str);

    // **** constructing Text ****

    /**
     * <p>
     * This creates the Text with the supplied
     *   text.
     * </p>
     *
     * @param data <code>String</code> content of Text.
     */
    public Text text(String str);

    // **** constructing Comment ****

    /**
     * <p>
     * This creates the comment with the supplied
     *   text.
     * </p>
     *
     * @param text <code>String</code> content of comment.
     */
    public Comment comment(String text);

    // **** constructing DocType

    /**
     * <p>
     * This will create the <code>DocType</code> with
     *   the specified element name and a reference to an
     *   external DTD.
     * </p>
     *
     * @param elementName <code>String</code> name of
     *        element being constrained.
     * @param publicID <code>String</code> public ID of
     *        referenced DTD
     * @param systemID <code>String</code> system ID of
     *        referenced DTD
     */
    public DocType docType(String elementName, 
                           String publicID, String systemID);

    /**
     * <p>
     * This will create the <code>DocType</code> with
     *   the specified element name and reference to an
     *   external DTD.
     * </p>
     *
     * @param elementName <code>String</code> name of
     *        element being constrained.
     * @param systemID <code>String</code> system ID of
     *        referenced DTD
     */
    public DocType docType(String elementName, String systemID);

    /**
     * <p>
     * This will create the <code>DocType</code> with
     *   the specified element name
     * </p>
     *
     * @param elementName <code>String</code> name of
     *        element being constrained.
     */
    public DocType docType(String elementName);

    // **** constructing Document

    /**
     * <p>
     * This will create a new <code>Document</code>,
     *   with the supplied <code>{@link Element}</code>
     *   as the root element and the supplied
     *   <code>{@link DocType}</code> declaration.
     * </p>
     *
     * @param rootElement <code>Element</code> for document root.
     * @param docType <code>DocType</code> declaration.
     */
    public Document document(Element rootElement, DocType docType);

    /**
     * <p>
     * This will create a new <code>Document</code>,
     *   with the supplied <code>{@link Element}</code>
     *   as the root element, and no <code>{@link DocType}</code>
     *   declaration.
     * </p>
     *
     * @param rootElement <code>Element</code> for document root
     */
    public Document document(Element rootElement);

    // **** constructing Elements ****

    /**
     * <p>
     * This will create a new <code>Element</code>
     *   with the supplied (local) name, and define
     *   the <code>{@link Namespace}</code> to be used.
     * </p>
     *
     * @param name <code>String</code> name of element.
     * @namespace <code>Namespace</code> to put element in.
         */
    public Element element(String name, Namespace namespace);

    /**
     * <p>
     *  This will create an <code>Element</code> in no
     *    <code>{@link Namespace}</code>.
     * </p>
     *
     * @param name <code>String</code> name of element.
     */
    public Element element(String name);

    /**
     * <p>
     *  This will create a new <code>Element</code> with
     *    the supplied (local) name, and specifies the URI
     *    of the <code>{@link Namespace}</code> the <code>Element</code>
     *    should be in, resulting it being unprefixed (in the default
     *    namespace).
     * </p>
     *
     * @param name <code>String</code> name of element.
     * @param uri <code>String</code> URI for <code>Namespace</code> element
     *        should be in.
     */
    public Element element(String name, String uri);

    /**
     * <p>
     *  This will create a new <code>Element</code> with
     *    the supplied (local) name, and specifies the prefix and URI
     *    of the <code>{@link Namespace}</code> the <code>Element</code>
     *    should be in.
     * </p>
     *
     * @param name <code>String</code> name of element.
     * @param uri <code>String</code> URI for <code>Namespace</code> element
     *        should be in.
     */
    public Element element(String name, String prefix, String uri);

    // **** constructing ProcessingInstruction ****

    /**
     * <p>
     * This will create a new <code>ProcessingInstruction</code>
     *   with the specified target and data.
     * </p>
     *
     * @param target <code>String</code> target of PI.
     * @param data <code>Map</code> data for PI, in
     *             name/value pairs
     */
    public ProcessingInstruction processingInstruction(String target, 
                                                       Map data);

    /**
     * <p>
     * This will create a new <code>ProcessingInstruction</code>
     *   with the specified target and data.
     * </p>
     *
     * @param target <code>String</code> target of PI.
     * @param rawData <code>String</code> data for PI.
     */
    public ProcessingInstruction processingInstruction(String target, 
                                                       String data);

    // **** constructing EntityRef ****

    /**
     * <p>
     * This will create a new <code>EntityRef</code>
     *   with the supplied name.
     * </p>
     *
     * @param name <code>String</code> name of element.
     */
    public EntityRef entityRef(String name);

    /**
     * <p>
     * This will create a new <code>EntityRef</code>
     *   with the supplied name, public ID, and system ID.
     * </p>
     *
     * @param name <code>String</code> name of element.
     * @param name <code>String</code> public ID of element.
     * @param name <code>String</code> system ID of element.
     */
    public EntityRef entityRef(String name, String publicID, String systemID);
}
