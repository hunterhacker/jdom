/*--

 Copyright 2000 Brett McLaughlin & Jason Hunter. All rights reserved.

 Redistribution and use in source and binary forms, with or without modifica-
 tion, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice,
    this list of conditions, and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions, the disclaimer that follows these conditions,
    and/or other materials provided with the distribution.

 3. The names "JDOM" and "Java Document Object Model" must not be used to
    endorse or promote products derived from this software without prior
    written permission. For written permission, please contact
    license@jdom.org.

 4. Products derived from this software may not be called "JDOM", nor may
    "JDOM" appear in their name, without prior written permission from the
    JDOM Project Management (pm@jdom.org).

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 JDOM PROJECT  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLUDING, BUT
 NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 This software  consists of voluntary contributions made  by many individuals
 on  behalf of the Java Document Object Model Project and was originally
 created by Brett McLaughlin <brett@jdom.org> and
 Jason Hunter <jhunter@jdom.org>. For more  information on the JDOM
 Project, please see <http://www.jdom.org/>.

 */
package org.jdom.contrib.xpath;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import org.jdom.*;
import org.jdom.contrib.xpath.XPathParseException;

/**
 * Wraps an Element to add convenience methods related to XPath.
 *
 * @author Michael Hinchey
 * @version 1.0
 */
public class XPathElement {

  public static boolean DEBUG = false;

  private Element element;

  /**
   * Construct with no element to extend.
   */
  public XPathElement() {
  }

  /**
   * Construct with an element to extend.
   */
  public XPathElement(final Element element) {
    extend(element);
  }

  /**
   * For debug.
   */
  public String toString() {
    return "XPathElement{" + element + "}";
  }

  /**
   * Return <code>this</code> so call can be chained.
   */
  public XPathElement extend(final Element element) {
    this.element = element;
    return this;
  }

  /**
   * Return the extended element.
   */
  public Element getElement() {
    return element;
  }

  /**
   * Get the matches for the xpath.
   */
  public List getMatches(String xpath) throws XPathParseException {
    org.jdom.contrib.xpath.a.JDOMLocator locator =
      new org.jdom.contrib.xpath.a.JDOMLocator();
    locator.setContextElement(element);
    List result = locator.match(xpath);
    if (DEBUG) {
      System.out.println(this + ".getMatches(" + xpath + "): " + result);
    }
    return result;
  }

  /**
   * Get the first match for the xpath.
   */
  public Object getFirstMatch(String xpath) throws XPathParseException {
    List matches = getMatches(xpath);
    if (matches != null) {
      if (matches.size() > 0) {
        return matches.get(0);
      }
    }
    return null;
  }

  /**
   * Get the first text match for the xpath.
   */
  public String getTextMatch(String xpath) throws XPathParseException {
    List matches = getMatches(xpath);
    if ( (matches != null) && (matches.size() > 0) ) {
      for (Iterator iter = matches.iterator(); iter.hasNext(); ) {
        Object each = iter.next();
        if (each instanceof String) {
          return each.toString();
        }
      }
    }
    return null;
  }

  /**
   * Get the first Element match for the xpath.
   */
  public Element getElementMatch(String xpath) throws XPathParseException {
    List matches = getMatches(xpath);
    if ( (matches != null) && (matches.size() > 0) ) {
      for (Iterator iter = matches.iterator(); iter.hasNext(); ) {
        Object each = iter.next();
        if (each instanceof Element) {
          return (Element) each;
        }
      }
    }
    return null;
  }

  /**
   * Get the first Attribute match for the xpath.
   */
  public Attribute getAttributeMatch(String xpath) throws XPathParseException {
    List matches = getMatches(xpath);
    if ( (matches != null) && (matches.size() > 0) ) {
      for (Iterator iter = matches.iterator(); iter.hasNext(); ) {
        Object each = iter.next();
        if (each instanceof Attribute) {
          return (Attribute) each;
        }
      }
    }
    return null;
  }

  /**
   * Return the element which is the root for the given element's document.
   */
  public Element getRootElement() {
    for (Element result = element; result != null; element = result.getParent()) {
      if (element == null) {
        return result;
      }
      result = element;
    }
    return null;
  }

  /**
   * Return all of the element's ancestors (parent and
   * parent's parent, et cetera).
   */
  public List getAncestors() {
    List result = new ArrayList();
    for (Element each = element.getParent(); each != null; each = each.getParent()) {
      result.add(each);
    }
    return result;
  }

  /**
   * Get the namespaces for the context element and all ancestors.
   */
  public List getNamespaces() {
    List elements = getAncestors();
    elements.add(element);
    List result = new ArrayList();
    for (Iterator iter = elements.iterator(); iter.hasNext(); ) {
      Element each = (Element) iter.next();
      addUnique(result, each.getNamespace());
    }
    return result;
  }

  /**
   * Return the first child comment.
   */
  public Comment getComment() {
    List content = element.getMixedContent();
    for (Iterator iter = content.iterator(); iter.hasNext(); ) {
      Object each = iter.next();
      if (each instanceof Comment) {
        return (Comment) each;
      }
    }
    return null;
  }

  /**
   * Return the subset of <code>element.getMixedContent()</code>
   * which are {@link Comment}s.
   * @return List of type <code>{@link Comment}</code>.
   */
  public List getComments() {
    List result = new ArrayList();
    for (Iterator iter = element.getMixedContent().iterator(); iter.hasNext(); ) {
      Object each = iter.next();
      if (each instanceof Comment) {
        result.add(each);
      }
    }
    return result;
  }

  /**
   * Return the subset of <code>element.getMixedContent()</code>
   * which are {@link String}s.
   * @return List of type <code>{@link String}</code>.
   */
  public List getTextChildren() {
    List result = new ArrayList();
    for (Iterator iter = element.getMixedContent().iterator(); iter.hasNext(); ) {
      Object each = iter.next();
      if (each instanceof String) {
        result.add(each);
      }
    }
    return result;
  }

  /**
   * Return the subset of <code>element.getMixedContent()</code>
   * which are {@link ProcessingInstruction}s.
   * @return List of type <code>{@link ProcessingInstruction}</code>.
   */
  public List getProcessingInstructions() {
    List result = new ArrayList();
    for (Iterator iter = element.getMixedContent().iterator(); iter.hasNext(); ) {
      Object each = iter.next();
      if (each instanceof ProcessingInstruction) {
        result.add(each);
      }
    }
    return result;
  }

  /**
   * Return the subset of <code>element.getMixedContent()</code>
   * which are {@link ProcessingInstruction}s and have the given <code>target</code>.
   * @return List of type <code>{@link ProcessingInstruction}</code>.
   */
  public List getProcessingInstructions(String target) {
    List result = getProcessingInstructions();
    for (Iterator iter = result.iterator(); iter.hasNext(); ) {
      ProcessingInstruction each = (ProcessingInstruction) iter.next();
      if (each.getTarget().equals(target) == false) {
        iter.remove();
      }
    }
    return result;
  }

  /**
   * Get the children with the given name and prefix.
   * @param prefix The namespace prefix; null matches all.
   * @param localName The local name; null matches all.
   */
  public List getChildren(String prefix, String localName) {
    if (prefix == null) {
      // faster impl if params are null ...
      if (localName == null) {
        return element.getChildren();
      } else {
        return element.getChildren(localName);
      }
    }

    List result = new ArrayList();
    for (Iterator iter = element.getChildren().iterator(); iter.hasNext(); ) {
      Element each = (Element) iter.next();
      if ( (localName == null) || (localName.equals(each.getName())) ) {
        if ( (prefix == null) || (prefix.equals(each.getNamespacePrefix())) ) {
          result.add(each);
        }
      }
    }
    return result;
  }

  /**
   * Returns the attribute with the given prefix and localName.
   * @param prefix The namespace prefix; null matches all.
   * @param localName The local name.
   */
  public Attribute getAttribute(String prefix, String localName) {
    Attribute result = element.getAttribute(localName);
    if ( (result != null) && (prefix != null) ) {
      if (prefix.equals(result.getNamespacePrefix()) == false) {
        result = null;
      }
    }
    return result;
  }

  /**
   * Get all descendants (no duplicates).
   * @param prefix The namespace prefix; null matches all.
   * @param localName The local name; null matches all.
   * @param self If true, also add <code>element</code> if it matches.
   */
  public List getDescendants(String prefix, String localName, boolean self) {
    List result = new ArrayList();
    addDescendants(result, element, prefix, localName, self);
    return result;
  }

  /**
   * Add all descendants to the list (no duplicates).
   * @param list Add elements to given list.
   * @param element The element to start with.
   * @param prefix The namespace prefix; null matches all.
   * @param localName The local name; null matches all.
   * @param self If true, also add <code>element</code> if it matches.
   */
  protected static void addDescendants(final List list, Element element, String prefix, String localName, boolean self) {
    if (self) {
      if ( (localName == null) || (localName.equals(element.getName())) ) {
        if ( (prefix == null) || (prefix.equals(element.getNamespacePrefix())) ) {
          addUnique(list, element);
        }
      }
    }

    for (Iterator iter = element.getChildren().iterator(); iter.hasNext(); ) {
      Element each = (Element) iter.next();
      // recursive call with self==true
      addDescendants(list, each, prefix, localName, true);
    }
  }

  /**
   * Add item to list only if not already contained in list.
   */
  private static boolean addUnique(final List list, final Object item) {
    if (list.contains(item) == false) {
      return list.add(item);
    }
    return false;
  }

}

