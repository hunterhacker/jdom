package org.jdom.contrib.xpath.jdom;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.jdom.*;
import org.jdom.contrib.xpath.NodeSet;
import org.jdom.contrib.xpath.XPathElement;
import org.jdom.contrib.xpath.XPathParseException;
import org.jdom.contrib.xpath.impl.Step;
import org.jdom.contrib.xpath.impl.Predicate;
import org.jdom.contrib.xpath.impl.Axis;
import org.jdom.contrib.xpath.impl.Nodetype;

/**
 * An ordered collection of JDOM nodes.
 *
 * @author Michael Hinchey
 * @version 1.0
 */
public class JdomNodeSet extends NodeSet {

  /**
   * @see #elemext(Element)
   */
  private XPathElement reusableXPathElement;

  /**
   * Constructor.
   */
  public JdomNodeSet(Element element) {
    add(element);
  }

  /**
   * Constructor.
   */
  public JdomNodeSet(Attribute attribute) {
    add(attribute);
  }

  /**
   * Add to the set.
   */
  public void add(Element element) {
    super.add(element);
  }

  /**
   * Add to the set.
   */
  public void add(Attribute attribute) {
    super.add(attribute);
  }

  /**
   * Add to the set.
   */
  public void add(ProcessingInstruction pi) {
    super.add(pi);
  }

  /**
   * Add to the set.
   */
  public void add(Comment comment) {
    super.add(comment);
  }

  /**
   * Add to the set.
   */
  public void add(String text) {
    super.add(text);
  }

  /**
   * replace list with only the root element
   */
  public void applyAbsolute() {
    Element elem = getFirstElementInSet();
    list().clear();
    if (elem != null) {
      elem = elemext(elem).getRootElement();
      add(elem);
    }
  }

  /**
   * Do nodetest for axis==child.
   */
  protected void nodetestChild(final Object context, final Step step, List list)
  throws XPathParseException {

    if ((context instanceof Element) == false) {
      return;
    }
    Element element = (Element) context;

    switch (step.getNodetype().getCode()) {
        case Nodetype.NODE: {
          if (step.getLocalName() == null) {
            if (step.isAbsolute()) {
              list.add(element);
              // Should we and can we add the other nodes
              // which belong to the document?
            } else {
              list.addAll(element.getMixedContent());
            }
          } else {
            throw new XPathParseException("LocalName for nodes is meaningless.  (localName=" + step.getLocalName() + ")");
          }
          break;
        }
        case Nodetype.ELEMENT: {
          if (step.isAbsolute()) {
            if (equals(element.getNamespacePrefix(), step.getPrefix())
                && equals(element.getName(), step.getLocalName())) {
              // element is the root element (the only one for a document)
              list.add(element);
            }
          } else {
            list.addAll(elemext(element).getChildren(step.getPrefix(), step.getLocalName()) );
          }
          break;
        }
        case Nodetype.COMMENT: {
          if (step.getLocalName() == null) {
            if (step.isAbsolute()) {
              // Should/can we do anything?
            } else {
              list.addAll(elemext(element).getComments());
            }
          } else {
            throw new XPathParseException("LocalName for comments is meaningless.  (localName=" + step.getLocalName() + ")");
          }
          break;
        }
        case Nodetype.TEXT: {
          if (step.getLocalName() == null) {
            if (step.isAbsolute()) {
              // Should/can we do anything?
            } else {
              list.addAll(elemext(element).getTextChildren());
            }
          } else {
            throw new XPathParseException("LocalName for text is meaningless.  (localName=" + step.getLocalName() + ")");
          }
          break;
        }
        case Nodetype.PROCESSING_INSTRUCTION: {
          if (step.getLocalName() == null) {
            if (step.isAbsolute()) {
              // Should/can we do anything?
            } else {
              list.addAll(elemext(element).getProcessingInstructions());
            }
          } else {
            if (step.isAbsolute()) {
              // Should/can we do anything?
            } else {
              list.addAll(elemext(element).getProcessingInstructions(step.getLocalName()));
            }
          }
          break;
        }
    } // end-switch
  }

  /**
   * Returns a reusable XPathElement object.
   */
  private XPathElement elemext(Element element) {
    if (reusableXPathElement == null) {
      reusableXPathElement = new XPathElement(element);
    } else {
      reusableXPathElement.extend(element);
    }
    return reusableXPathElement;
  }

  private Element getFirstElementInSet() {
    Element result = null;
    for (Iterator iter = iterator(); iter.hasNext(); ) {
      Object each = iter.next();
      if (each instanceof Element) {
        return (Element) each;
      }
    }
    return result;
  }

}
