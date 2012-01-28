/*--

 Copyright (C) 2000-2004 Jason Hunter & Brett McLaughlin.
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

package org.jdom2.contrib.output;

/**
 * A JTree outputter.
 * <p>
 * This outputter builds a JTree representation of the JDOM document for
 * easy visual navigation.  This is a full rewrite of the JTreeOutputter 
 * originally written by James Davies.
 * </p>
 *
 * @author Matthew MacKenzie [matt@xmlglobal.com]
 */
import java.util.Iterator;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jdom2.Document;
import org.jdom2.Attribute;
import org.jdom2.Element;

@SuppressWarnings("javadoc")
public class JTreeOutputter {

    public JTreeOutputter() {
    }

    /**
	 * @param toBeCompatible unused. 
	 */
    public JTreeOutputter(boolean toBeCompatible) {
        // just here to mimic the legacy JTreeOutputter
    }

    /**
     * Output a Document.
     * @param doc   The document to transform to TreeNode.
     * @param root  The root tree node.
     */
    public void output(Document doc, DefaultMutableTreeNode root) {
        processElement(doc.getRootElement(),root);
    }

    /**
     * Output an Element.
     * @param el    The element to transform to TreeNode.
     * @param root  The root tree node.
     */
    public void output(Element el, DefaultMutableTreeNode root) {
        processElement(el, root);
    }

    protected void processElement(Element el, DefaultMutableTreeNode dmtn) {
        DefaultMutableTreeNode dmtnLocal = 
            new DefaultMutableTreeNode(el.getName());
        String elText = el.getTextNormalize();
        if (elText != null && !elText.equals("")) {
            dmtnLocal.add(new DefaultMutableTreeNode(elText));
        }
        processAttributes(el, dmtnLocal);

        Iterator<Element> iter = el.getChildren().iterator();

        while (iter.hasNext()) {
            Element nextEl = iter.next();
            processElement(nextEl, dmtnLocal);
        }
        dmtn.add(dmtnLocal);
    }

    protected void processAttributes(Element el, DefaultMutableTreeNode dmtn) {
    	if (!el.hasAttributes()) {
    		return;
    	}
        Iterator<Attribute> atts = el.getAttributes().iterator();
        while (atts.hasNext()) {
            Attribute att = atts.next();
            DefaultMutableTreeNode node = 
                new DefaultMutableTreeNode("@" + att.getName());
            node.add(new DefaultMutableTreeNode(att.getValue()));
            dmtn.add(node);
        }
    }
}

