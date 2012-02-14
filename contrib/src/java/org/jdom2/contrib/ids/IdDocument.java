/*--

 Copyright (C) 2001-2004 Jason Hunter & Brett McLaughlin.
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

package org.jdom2.contrib.ids;

import java.util.*;

import org.jdom2.Document;
import org.jdom2.DocType;
import org.jdom2.Element;


/**
 * IdDocument extends the default JDOM document to support looking
 * up elements using their ID attribute.
 * <p>
 * Instances of this class should not be created directly but through
 * the {@link IdFactory}.  Using this factory ensures that the
 * document is made of {@link IdElement} instances which update the
 * look-up table when the element attribute list is updated.</p>
 * <p>
 * The method {@link #getElementById} allows looking up an element
 * in the document given the value of its ID attribute.  Instead of
 * scanning the whole document when searching for an element,
 * <code>IdDocument</code> uses a lookup table for fast direct
 * access to the elements.  Hence, applications using this method
 * should see their performances improved compared to walking the
 * document tree.</p>
 *
 * @author Laurent Bihanic
 */
@SuppressWarnings("javadoc")
public class IdDocument extends Document {

    /**
	 * Default.
	 */
	private static final long serialVersionUID = 1L;

   /**
    * <p>The ID lookup table for the document.</p>
    */
   private Map<String,Element> ids = new HashMap<String, Element>();

   /**
    * <p>
    * Creates a new <code>IdDocument</code>, with the supplied
    * <code>{@link Element}</code> as the root element and the
    * supplied <code>{@link DocType}</code> declaration.</p>
    *
    * @param  rootElement   <code>Element</code> for document root.
    * @param  docType       <code>DocType</code> declaration.
    */
   public IdDocument(Element root, DocType docType) {
      super(root, docType);
   }

   /**
    * <p>
    * Creates a new <code>Document</code>, with the supplied
    * <code>{@link Element}</code> as the root element, and no
    * <code>{@link DocType document type}</code> declaration.
    * </p>
    *
    * @param  rootElement   <code>Element</code> for document root.
    */
   public IdDocument(Element root) {
      super(root);
   }

   /**
    * <p>
    * Retrieves an element using the value of its ID attribute as
    * key.</p>
    *
    * @param  id   the value of the ID attribute of the element to
    *              retrieve.
    * @return the <code>Element</code> associated to <code>id</code>
    *         or <code>null</code> if none was found.
    */
   public Element getElementById(String id) {
      return ids.get(id);
   }

   /**
    * <p>
    * Adds the specified ID to the ID lookup table of this document
    * and make it point to the associated element.</p>
    *
    * @param  id   the ID to add.
    * @param  elt  the <code>Element</code> associated to the ID.
    */
   protected void addId(String id, Element elt) {
      ids.put(id, elt);
      return;
   }

   /**
    * <p>
    * Removes the specified ID from the ID lookup table of this
    * document.</p>
    *
    * @param  id   the ID to remove.
    * @return <code>true</code> if the ID was found and removed;
    *         <code>false</code> otherwise.
    */
   protected boolean removeId(String id) {
      return (ids.remove(id) != null);
   }
}

