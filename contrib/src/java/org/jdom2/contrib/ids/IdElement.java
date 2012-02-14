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

import org.jdom2.Attribute;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Parent;
import org.jdom2.Namespace;

/**
 * A sub-class of the default JDOM <code>Element</code> to help
 * keeping up-to-date the element lookup table maintained by
 * <code>IdDocument</code>.
 *
 * @author Laurent Bihanic
 */
@SuppressWarnings("javadoc")
public class IdElement extends Element {

    /**
	 * Default.
	 */
	private static final long serialVersionUID = 1L;

   // Allow Javadocs to inherit from superclass

   public IdElement(String name, Namespace namespace) {
      super(name, namespace);
   }

   public IdElement(String name) {
      this(name, (Namespace)null);
   }

   public IdElement(String name, String uri) {
      this(name, Namespace.getNamespace("", uri));
   }

   public IdElement(String name, String prefix, String uri) {
      this(name, Namespace.getNamespace(prefix, uri));
   }

   @Override
protected Content setParent(Parent parent) {
      // Save previous owning document (if any).
      Document prevDoc = this.getDocument();
      Document newDoc  = (parent != null)? parent.getDocument(): null;

      // Attach to new parent element.
      super.setParent(parent);

      if (newDoc != prevDoc) {
         // New and previous owning documents are different.
         // => Remove all the IDs for the subtree this element is the root
         //    of from the previous owning document's lookup table and
         //    insert them into the new owning document's lookup table.
         transferIds(prevDoc, newDoc);
      }
      return this;
   }


   /**
    * <p>
    * Transfers all the IDs for the subtree this element is the root
    * of from the lookup table of the previous owning document to
    * the lookup table of the new owning document.</p>
    * <p>
    * If either of the documents is <code>null</code> or is not an
    * {@link IdDocument}, this method performs not action on the
    * document. Hence, this method supports being called with both
    * documents equal to <code>null</code>.</p>
    *
    * @param  prevDoc   the previous owning document.
    * @param  newDoc    the new owning document.
    */
   private void transferIds(Document prevDoc, Document newDoc)
   {
      if ((prevDoc instanceof IdDocument) || (newDoc instanceof IdDocument)) {
         // At least one of the documents supports lookup by ID.
         // => Walk subtree to collect ID mappings.
         Map<String,Element> ids = getIds(this, new HashMap<String, Element>());

         // Update previous owning document.
         if (prevDoc instanceof IdDocument) {
            // Document supports lookup by ID.
            // => Remove IDs from document's lookup table.
            IdDocument idDoc = (IdDocument)prevDoc;

            for (String key : ids.keySet()) {
               idDoc.removeId(key);
            }
         }
         // Else: Lookup by ID not supported. => Nothing to update!

         // Update new owning document.
         if (newDoc instanceof IdDocument) {
            // Document supports lookup by ID.
            // => Add IDs from document's lookup table.
            IdDocument idDoc = (IdDocument)newDoc;

            for (Map.Entry<String,Element> me : ids.entrySet()) {
               idDoc.addId(me.getKey(), me.getValue());
            }
         }
         // Else: Lookup by ID not supported. => Nothing to update!
      }
      // Else: None of the documents supports lookup by ID not supported.
      //       => Ignore call.
      return;
   }

   /**
    * <p>
    * Retrieves the ID attributes for the subtree rooted at
    * <code>root</code> to populate the ID mapping table
    * <code>ids</code>. In the mapping table, ID attribute values are
    * the keys to access the elements.</p>
    *
    * @param  root   the root element of the subtree to walk.
    * @param  ids    the ID lookup table to populate.
    *
    * @return the updated ID lookup table.
    */
   private static Map<String,Element> getIds(Element root, Map<String,Element> ids) {
      addIdAttributes(root, ids);

      for (Element emt : root.getChildren()) {
          getIds(emt, ids);
      }
      return ids;
   }

   /**
    * <p>
    * Gets the ID attribute for <code>elt</code> and adds the
    * correpsonding entries in the ID mapping table
    * <code>ids</code>.</p>
    *
    * @param  elt   the element to the ID attributes from.
    * @param  ids   the ID lookup table to populate.
    */
   private static void addIdAttributes(Element elt, Map<String,Element> ids) {
      for (Attribute attr : elt.getAttributes()) {
         if (attr.getAttributeType() == Attribute.ID_TYPE) {
            ids.put(attr.getValue(), elt);
            break;
         }
      }
      return;
   }
}

