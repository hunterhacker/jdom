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

package org.jdom2.contrib.helpers;


import java.util.Iterator;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Comment;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;
import org.jdom2.filter.Filters;


/**
 * Provides a set of utility methods to generate XPath expressions
 * to select a given node in a (subtree of a) document.
 * <p>
 * <strong>Note</strong>: As this class has no knowledge of the
 * document content, the generated XPath expression rely on the
 * document structure.  Hence any modification of the structure
 * of the document may invalidate the generated XPaths.</p>
 *
 * @author Laurent Bihanic
 * @deprecated moved in to core: org.jdom2.xpath.XPathHelper
 * @see org.jdom2.xpath.XPathHelper
 */
@Deprecated
public class XPathHelper {

    /**
     * Returns the path to the specified Element from the document
     * root as an XPath expression.
     *
     * @param  to   the Element the generated path shall select.
     *
     * @return an XPath expression to select the specified node.
     *
     * @throws JDOMException if the XPath generation failed.
     * @throws IllegalArgumentException if <code>to</code> is
     *         <code>null</code>.
     */
    public static String getPathString(Element to) throws JDOMException {
        return getPathString(null, to);
    }

    /**
     * Returns the path from a given JDOM node to the specified
     * Element as an XPath expression.
     *
     * @param  from the Document or Element node at which the
     *              the generated path shall be applied. Use
     *              <code>null</code> to specify the topmost
     *              ancestor of the <code>to</code> node.
     * @param  to   the Element the generated path shall select.
     *
     * @return an XPath expression to select the specified node.
     *
     * @throws JDOMException if the XPath generation failed.
     * @throws IllegalArgumentException if <code>to</code> is
     *         <code>null</code> or <code>from</code> is not
     *         a {@link Document} or a {@link Element} node.
     */
    public static String getPathString(Object from, Element to)
                                                throws JDOMException {
        checkPathStringArguments(from, to);

        if (to == from) {
            return "node()";
        }
        return getElementPath(from, to, true).toString();
    }

    /**
     * Returns the path to the specified Attribute from the document
     * root as an XPath expression.
     *
     * @param  to   the Attribute the generated path shall select.
     *
     * @return an XPath expression to select the specified node.
     *
     * @throws JDOMException if the XPath generation failed.
     * @throws IllegalArgumentException if <code>to</code> is
     *         <code>null</code>.
     */
    public static String getPathString(Attribute to) throws JDOMException {
        return getPathString(null, to);
    }

    /**
     * Returns the path from a given JDOM node to the specified
     * Attribute as an XPath expression.
     *
     * @param  from the Document or Element node at which the
     *              the generated path shall be applied. Use
     *              <code>null</code> to specify the topmost
     *              ancestor of the <code>to</code> node.
     * @param  to   the Attribute the generated path shall select.
     *
     * @return an XPath expression to select the specified node.
     *
     * @throws JDOMException if the XPath generation failed.
     * @throws IllegalArgumentException if <code>to</code> is
     *         <code>null</code> or <code>from</code> is not
     *         a {@link Document} or a {@link Element} node.
     */
    public static String getPathString(Object from, Attribute to)
                                                        throws JDOMException {
        checkPathStringArguments(from, to);

        if (to == from) {
            return "node()";
        }
		StringBuilder path = getElementPath(from, to.getParent(), false);
		path.append('@').append(to.getName()).toString();
		return path.toString();
    }

    /**
     * Returns the path to the specified Text node from the document
     * root as an XPath expression.
     *
     * @param  to   the Text node the generated path shall select.
     *
     * @return an XPath expression to select the specified node.
     *
     * @throws JDOMException if the XPath generation failed.
     * @throws IllegalArgumentException if <code>to</code> is
     *         <code>null</code>.
     */
    public static String getPathString(Text to) throws JDOMException {
        return getPathString(null, to);
    }

    /**
     * Returns the path from a given JDOM node to the specified
     * Text node as an XPath expression.
     *
     * @param  from the Document or Element node at which the
     *              the generated path shall be applied. Use
     *              <code>null</code> to specify the topmost
     *              ancestor of the <code>to</code> node.
     * @param  to   the Text node the generated path shall select.
     *
     * @return an XPath expression to select the specified node.
     *
     * @throws JDOMException if the XPath generation failed.
     * @throws IllegalArgumentException if <code>to</code> is
     *         <code>null</code> or <code>from</code> is not
     *         a {@link Document} or a {@link Element} node.
     */
    public static String getPathString(Object from, Text to)
                                                      throws JDOMException {
        checkPathStringArguments(from, to);

        if (to == from) {
            return "node()";
        }
		Element parent = to.getParentElement();
		List<Text> siblings = null;
		StringBuilder path = getElementPath(from, parent, false);

		if (parent != null) {
		    siblings = parent.getContent(Filters.text());
		}
		else {
		    Document doc = to.getDocument();
		    if (doc != null) {
		        siblings = doc.getContent(Filters.text());
		    }
		}
		return getPositionPath(to, siblings, "text()", path).toString();
    }

    /**
     * Returns the path to the specified Comment from the document
     * root as an XPath expression.
     *
     * @param  to   the Comment the generated path shall select.
     *
     * @return an XPath expression to select the specified node.
     *
     * @throws JDOMException if the XPath generation failed.
     * @throws IllegalArgumentException if <code>to</code> is
     *         <code>null</code>.
     */
    public static String getPathString(Comment to) throws JDOMException {
        return getPathString(null, to);
    }

    /**
     * Returns the path from a given JDOM node to the specified
     * Comment as an XPath expression.
     *
     * @param  from the Document or Element node at which the
     *              the generated path shall be applied. Use
     *              <code>null</code> to specify the topmost
     *              ancestor of the <code>to</code> node.
     * @param  to   the Comment the generated path shall select.
     *
     * @return an XPath expression to select the specified node.
     *
     * @throws JDOMException if the XPath generation failed.
     * @throws IllegalArgumentException if <code>to</code> is
     *         <code>null</code> or <code>from</code> is not
     *         a {@link Document} or a {@link Element} node.
     */
    public static String getPathString(Object from, Comment to)
                                                  throws JDOMException {
        checkPathStringArguments(from, to);

        if (to == from) {
            return "node()";
        }
		Element parent = to.getParentElement();
		List<Comment> siblings = null;
		StringBuilder path = getElementPath(from, parent, false);

		if (parent != null) {
		    siblings = parent.getContent(Filters.comment());
		}
		else {
		    Document doc = to.getDocument();
		    if (doc != null) {
		        siblings = doc.getContent(Filters.comment());
		    }
		}
		return getPositionPath(to, siblings, "comment()", path).toString();
    }

    /**
     * Returns the path to the specified ProcessingInstruction node
     * from the document root as an XPath expression.
     *
     * @param  to   the ProcessingInstruction node the generated path
     *              shall select.
     *
     * @return an XPath expression to select the specified node.
     *
     * @throws JDOMException if the XPath generation failed.
     * @throws IllegalArgumentException if <code>to</code> is
     *         <code>null</code>.
     */
    public static String getPathString(ProcessingInstruction to)
                                            throws JDOMException {
        return getPathString(null, to);
    }

    /**
     * Returns the path from a given JDOM node to the specified
     * ProcessingInstruction node as an XPath expression.
     *
     * @param  from the Document or Element node at which the
     *              the generated path shall be applied. Use
     *              <code>null</code> to specify the topmost
     *              ancestor of the <code>to</code> node.
     * @param  to   the ProcessingInstruction node the generated
     *              path shall select.
     *
     * @return an XPath expression to select the specified node.
     *
     * @throws JDOMException if the XPath generation failed.
     * @throws IllegalArgumentException if <code>to</code> is
     *         <code>null</code> or <code>from</code> is not
     *         a {@link Document} or a {@link Element} node.
     */
    public static String getPathString(Object from, ProcessingInstruction to)
                                                 throws JDOMException {
        checkPathStringArguments(from, to);

        if (to == from) {
            return "node()";
        }
		Element parent = to.getParentElement();
		List<ProcessingInstruction> siblings = null;
		StringBuilder path = getElementPath(from, parent, false);

		if (parent != null) {
		    siblings = parent.getContent(Filters.processinginstruction());
		}
		else {
		    Document doc = to.getDocument();
		    if (doc != null) {
		        siblings = doc.getContent(Filters.processinginstruction());
		    }
		}
		return getPositionPath(to, siblings,
		                       "processing-instruction()", path).toString();
    }

    /**
     * Returns the path to the specified JDOM node from the document
     * root as an XPath expression.
     *
     * @param  to   the JDOM node the generated path shall select.
     *
     * @return an XPath expression to select the specified node.
     *
     * @throws JDOMException if the XPath generation failed.
     * @throws IllegalArgumentException if <code>to</code> is
     *         <code>null</code> or is not a JDOM node selectable
     *         by XPath expressions (Element, Attribute, Text,
     *         Comment, ProcessingInstruction).
     */
    public static String getPathString(Object to) throws JDOMException {
        return getPathString(null, to);
    }

    /**
     * Returns the path from a JDOM node to another JDOM node
     * as an XPath expression.
     *
     * @param  from the Document or Element node at which the
     *              the generated path shall be applied. Use
     *              <code>null</code> to specify the topmost
     *              ancestor of the <code>to</code> node.
     * @param  to   the JDOM node the generated path shall select.
     *
     * @return an XPath expression to select the specified node.
     *
     * @throws JDOMException if the XPath generation failed.
     * @throws IllegalArgumentException if <code>from</code> is not
     *         a {@link Document} or a {@link Element} node or
     *         <code>to</code> is <code>null</code> or is not a JDOM
     *         node selectable by XPath expressions (Element,
     *         Attribute, Text, Comment, ProcessingInstruction).
     */
    public static String getPathString(Object from, Object to)
            throws JDOMException {
        if (to instanceof Element) {
            return getPathString(from, (Element) to);
        }
        else if (to instanceof Attribute) {
            return getPathString(from, (Attribute) to);
        }
        else if (to instanceof Text) {
            return getPathString(from, (Text) to);
        }
        else if (to instanceof Comment) {
            return getPathString(from, (Comment) to);
        }
        else if (to instanceof ProcessingInstruction) {
            return getPathString(from, (ProcessingInstruction) to);
        }
        else {
            throw new IllegalArgumentException(
                    "\"to \" shall be an Element, Attribute," +
                    " Text, Comment or ProcessingInstruction node");
        }
    }


    /**
     * Checks that the two arguments of a <code>getPathString()</code>
     * call are valid.
     *
     * @param  from   the <code>from</code> node.
     * @param  to     the <code>to</code> node.
     *
     * @throws IllegalArgumentException if one of the arguments is
     *                                  invalid.
     */
    private static void checkPathStringArguments(Object from, Object to) {
        if (!((from == null) || (from instanceof Element)
                || (from instanceof Document))) {
            throw new IllegalArgumentException("from");
        }
        if (to == null) {
            throw new IllegalArgumentException("to");
        }
    }

    /**
     * Returns the XPath expression to select the <code>to</code>
     * element relatively to the <code>from</code> element.
     *
     * @param  from   the <code>from</code> element.
     * @param  to     the <code>to</code> element.
     * @param  leaf   whether the <code>to</code> is the last element
     *                of the path to return.
     *
     * @return an XPath expression to select the <code>to</code>
     *         element.
     *
     * @throws JDOMException if the XPath generation failed.
     */
    private static StringBuilder getElementPath(Object from,
                                               Element to, boolean leaf)
                                                  throws JDOMException {
        if (from instanceof Document) {
            from = null;
        }
        return getElementPath((Element) from, to, leaf, new StringBuilder());
    }

    /**
     * Returns the XPath expression to select the <code>to</code>
     * element relatively to the <code>from</code> element.
     *
     * @param  from   the <code>from</code> element.
     * @param  to     the <code>to</code> element.
     * @param  leaf   whether the <code>to</code> is the last element
     *                of the path to return.
     * @param  path   the buffer to which append the path.
     *
     * @return an XPath expression to select the <code>to</code>
     *         element.
     *
     * @throws JDOMException if the XPath generation failed.
     */
    private static StringBuilder getElementPath(Element from, Element to,
                                               boolean leaf, StringBuilder path)
                                                         throws JDOMException {
        if (to != from) {
            List<Element> siblings = null;

            Element parent = to.getParentElement();
            if (parent == null) {
                // Oops! No more parent but I haven't yet reached the from node.
                if (parent != from) {
                    // Ouch! from node is not an ancestor.
                    throw new JDOMException(
                     "The \"from\" node is not an ancestor of the \"to\" node");
                }
                if (to.isRootElement()) {
                    path.append('/');
                }
            }
            else {
                siblings = parent.getChildren(to.getName(), null);
            }

            if (parent != from) {
                path = getElementPath(from, parent, false, path);
            }

            Namespace ns = to.getNamespace();
            if (ns == Namespace.NO_NAMESPACE) {
                // No namespace => Use local name only.
                path = getPositionPath(to, siblings, to.getName(), path);
            }
            else {
                // Elements belongs to a namespace => Check prefix.
                String prefix = to.getNamespacePrefix();
                if ("".equals(prefix)) {
                    // No prefix (default namespace in scope
                    //  => Use wildcard & local name combination.
                    path.append("*[local-name()='").
                            append(to.getName()).append("']");

                    path = getPositionPath(to, siblings, null, path);
                }
                else {
                    // Not the default namespace => Use prefix.
                    path.append(to.getNamespacePrefix()).append(':');

                    path = getPositionPath(to, siblings, to.getName(), path);
                }
            }

            if ((!leaf) && (path.length() != 0)) {
                path.append('/');
            }
        }
        return (path);
    }

    /**
     * Appends the specified path token to the provided buffer
     * followed by the position specification of the target node in
     * its siblings list.
     *
     * @param  node        the target node for the XPath expression.
     * @param  siblings    the siblings of the target node.
     * @param  pathToken   the path token identifying the target node.
     * @param  buffer      the buffer to which appending the XPath
     *                     sub-expression or <code>null</code> if the
     *                     method shall allocate a new buffer.
     *
     * @return the XPath sub-expression to select the target node
     *         among its siblings.
     */
    private static StringBuilder getPositionPath(Object node, List<?> siblings,
                                                String pathToken,
                                                StringBuilder buffer) {
        if (buffer == null) {
            buffer = new StringBuilder();
        }
        if (pathToken != null) {
            buffer.append(pathToken);
        }

        if ((siblings != null) && (siblings.size() != 1)) {
            int position = 0;
            for (Iterator<?> i = siblings.iterator(); i.hasNext();) {
                position++;
                if (i.next() == node) break;
            }
            buffer.append('[').append(position).append(']');
        }
        return buffer;
    }
}

