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
package org.jdom.output;

import java.util.Iterator;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;

/**
 * <p><code>JTreeOutputter</code> outputs a JDOM <code>{@link Document}</code>
 *   to a Swing JTree.</p>
 *
 * @author James Davies
 * @author Brett McLaughlin
 * @version 1.0
 */
public class JTreeOutputter {

    /** Show captions on attributes? */
    boolean showAttributeCaptions = true;

    public JTreeOutputter(boolean showCaptionsOnAttributes) {
        this.showAttributeCaptions = showAttributeCaptions;
    }

  public void output(Document doc, DefaultMutableTreeNode parent) {
    Iterator i = doc.getMixedContent().iterator();

    while (i.hasNext()) {
      Object obj = i.next();

      if (obj instanceof Element) {
        outputElement((Element)obj, parent);
      }
    }
  }

  // Protected methods.
  protected String AttributeText(Element elem) {
    Attribute attrib;
    String s = "";

    List attribs = elem.getAttributes();
    int size = attribs.size();

    for (int i = 0; i < size; i++) {
      attrib = (Attribute)attribs.get(i);

      if (i == 0)
        s = s + ": ";
      else
        s = s + ", ";

      s = s + attrib.getName() + " = '" + attrib.getValue() + "'";
    }

    return s;
  }

  protected void outputAttributes(Element elem, DefaultMutableTreeNode parent) {
    Attribute attrib;
    List attribs;
    DefaultMutableTreeNode attNode, attsNode;
    String s;

    attribs = elem.getAttributes();
    int size = attribs.size();

    if (size > 0) {
      attsNode = new DefaultMutableTreeNode("Attributes");
      parent.add(attsNode);

      for (int i = 0; i < size; i++) {
        attrib = (Attribute)attribs.get(i);
        s = attrib.getName() + " = '" + attrib.getValue() + "'";
        attNode = new DefaultMutableTreeNode(s);
        attsNode.add(attNode);
      }
    }
  }

  protected void outputElement(Element elem, DefaultMutableTreeNode parent) {
    DefaultMutableTreeNode child;
    Object content;
    ElementTreeNode node;
    String s;

    // Create a new node for this Element.
    node = new ElementTreeNode();
    node.setUserObject(elem);
    parent.add(node);

    // Output any Attributes.
    if (showAttributeCaptions) {
      s = elem.getName() + AttributeText(elem);
      node.setCaption(s);
    }
    else {
      node.setCaption(elem.getName());
      outputAttributes(elem, node);
    }

    // Iterate through the content.
    List mixedContent = elem.getMixedContent();
    int size = mixedContent.size();

    for (int i = 0; i < size; i++) {
      content = mixedContent.get(i);

      if (content instanceof Element)
        outputElement((Element)content, node);
      else if (content instanceof String) {
        // Create a new child node.
        child = new DefaultMutableTreeNode("'" + content.toString() + "'");
        node.add(child);
      }
    }
  }
}


// Utility class.
class ElementTreeNode extends DefaultMutableTreeNode {
  String fCaption = "";

  public String getCaption() {
    return fCaption;
  }

  public void setCaption(String s) {
    fCaption = s;
  }

  public String toString() {
    return fCaption;
  }
}

