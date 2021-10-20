/*--

 Copyright (C) 2000-2012 Jason Hunter & Brett McLaughlin.
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

package org.jdom2.xpath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Comment;
import org.jdom2.Content;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.NamespaceAware;
import org.jdom2.Parent;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;
import org.jdom2.filter.Filters;

/**
 * Provides a set of utility methods to generate XPath expressions to select a
 * given node in a document. You can generate absolute XPath expressions to the
 * target node, or relative expressions from a specified start point. If you
 * request a relative expression, the start and target nodes must share some
 * common ancestry.
 * <p>
 * XPaths are required to be namespace-aware. Typically this is done by using
 * a namespace-prefixed query, with the actual namespace URI being set in the
 * context of the XPath expression. This is not possible to express using a
 * simple String return value. As a work-around, this method uses a potentially
 * slower, but more reliable, mechanism for ensuring the correct namespace
 * context is selected. The mechanism will appear like (for Elements):
 * <br>
 * <code>   .../*[local-name() = 'tag' and namespace-uri() = 'uri']</code>
 * <br>
 * Similarly, Attributes will have a syntax similar to:
 * <br>
 * <code>   .../@*[local-name() = 'attname' and namespace-uri() = 'uri'] </code>
 * <br>
 * This mechanism makes it possible to have a simple namespace context, and a
 * simple String value returned from the methods on this class.
 * <p>
 * This class does not provide ways to access document-level content. Nor does
 * it provide ways to access data relative to the Document level. Use absolute
 * methods to access data from the Document level.
 * <p>
 * The methods on this class assume that the Document is above the top-most
 * Element in the XML tree. The top-most Element is the one that does not have
 * a parent Element (although it may have a parent Document). As a result, you
 * can use Element data that is not attached to a JDOM Document.
 * <p>
 * Detached Attributes, and detached non-Element content are not treated the
 * same. If you try to get an Absolute path to a detached Attribute or
 * non-Element Content you will get an IllegalArgumentException. On the other
 * hand it is legal to get the relative XPath for a detached node to itself (
 * but to some other node will cause an IllegalArgumentException because the
 * nodes do not share a common ancestor).  
 * <p>
 * <strong>Note</strong>: As this class has no knowledge of the document
 * content, the generated XPath expression rely on the document structure. Hence
 * any modification of the structure of the document may invalidate the
 * generated XPaths.
 * </p>
 * 
 * @author Laurent Bihanic
 * @author Rolf Lear
 */
public final class XPathHelper {
	
	/**
	 * Private constructor.
	 */
	private XPathHelper () {
		// make constructor inaccessible.
	}

	/**
	 * Appends the specified path token to the provided buffer followed by the
	 * position specification of the target node in its siblings list (if
	 * needed).
	 * 
	 * @param node
	 *        the target node for the XPath expression.
	 * @param siblings
	 *        the siblings of the target node.
	 * @param pathToken
	 *        the path token identifying the target node.
	 * @param buffer
	 *        the buffer to which appending the XPath sub-expression or
	 *        <code>null</code> if the method shall allocate a new buffer.
	 * @return the XPath sub-expression to select the target node among its
	 *         siblings.
	 */
	private static StringBuilder getPositionPath(Object node, List<?> siblings,
			String pathToken, StringBuilder buffer) {

		buffer.append(pathToken);

		if (siblings != null) {
			int position = 0;
			final Iterator<?> i = siblings.iterator();
			while (i.hasNext()) {
				position++;
				if (i.next() == node)
					break;
			}
			if (position > 1 || i.hasNext()) {
				// the item is not at the first location, or there are more
				// locations. in other words, indexing is required.
				buffer.append('[').append(position).append(']');
			}
		}
		return buffer;
	}

	/**
	 * Calculate a single stage of an XPath query.
	 * 
	 * @param nsa
	 *        The token to get the relative-to-parent XPath for
	 * @param buffer
	 *        The buffer to append the relative stage to
	 * @return The same buffer as was input.
	 */
	private static final StringBuilder getSingleStep(final NamespaceAware nsa,
			final StringBuilder buffer) {
		if (nsa instanceof Content) {
			
			final Content content = (Content) nsa;
			
			final Parent pnt = content.getParent();

			if (content instanceof Text) { // OR CDATA!
				
				final List<?> sibs = pnt == null ? null :
					pnt.getContent(Filters.text()); // CDATA
				return getPositionPath(content, sibs, "text()", buffer);
				
			} else if (content instanceof Comment) {
				
				final List<?> sibs = pnt == null ? null :
					pnt.getContent(Filters.comment());
				return getPositionPath(content, sibs, "comment()", buffer);
				
			} else if (content instanceof ProcessingInstruction) {
				
				final List<?> sibs = pnt == null ? null : 
					pnt.getContent(Filters.processinginstruction());
				return getPositionPath(content, sibs,
						"processing-instruction()", buffer);
				
			} else if (content instanceof Element &&
					((Element) content).getNamespace() == Namespace.NO_NAMESPACE) {
				
				// simple XPath to a no-namespace Element.
				
				final String ename = ((Element) content).getName();
				final List<?> sibs = (pnt instanceof Element) ? ((Element)pnt)
						.getChildren(ename) : null;
				return getPositionPath(content, sibs, ename, buffer);
				
			} else if (content instanceof Element) {
				
				// complex XPath to an Element with Namespace...
				// we do not want to have to prefix namespaces because that is
				// essentially impossible to get right with the new JDOM2 API.
				final Element emt = (Element)content;
				
				// Note, the getChildren compares only the URI (not the prefix)
				// so the results are the same as an XPath would be.
				final List<?> sibs = (pnt instanceof Element) ? 
						((Element)pnt).getChildren(emt.getName(), emt.getNamespace()) : null;
				String xps = "*[local-name() = '" + emt.getName() + 
						"' and namespace-uri() = '" + 
						emt.getNamespaceURI() + "']";
				return getPositionPath(content, sibs, xps, buffer);
				
			} else {
				final List<?> sibs = pnt == null ? Collections
						.singletonList(nsa) : pnt.getContent();
				return getPositionPath(content, sibs, "node()", buffer);
				
			}
		} else if (nsa instanceof Attribute) {
			Attribute att = (Attribute) nsa;
			if (att.getNamespace() == Namespace.NO_NAMESPACE) {
				buffer.append("@").append(att.getName());
			} else {
				buffer.append("@*[local-name() = '").append(att.getName());
				buffer.append("' and namespace-uri() = '");
				buffer.append(att.getNamespaceURI()).append("']");
			}
		}

		// do nothing...
		return buffer;
	}

	/**
	 * Returns the path to the specified <code>to</code>Element from the
	 * specified <code>from</code> Element. The from Element must have a common
	 * ancestor Element with the to Element.
	 * <p>
	 * 
	 * @param from
	 *        the Element the generated path shall select relative to.
	 * @param to
	 *        the Content the generated path shall select.
	 * @param sb
	 *        the StringBuilder to append the path to.
	 * @return an XPath expression to select the specified node.
	 * @throws IllegalArgumentException
	 *         if the from and to Elements have no common ancestor.
	 */
	private static StringBuilder getRelativeElementPath(final Element from,
			final Parent to, final StringBuilder sb) {
		if (from == to) {
			sb.append(".");
			return sb;
		}

		// ToStack will be a chain of Elements from the to element to the
		// root element, but will be 'short' if it encounters the from Element
		// itself.
		final ArrayList<Parent> tostack = new ArrayList<Parent>();
		Parent p = to;
		while (p != null && p != from) {
			tostack.add(p);
			p = p.getParent();
		}
		
		// the number of steps we will have in the resulting path (potentially)
		int pos = tostack.size();

		if (p != from) {
			// the from is not a direct ancestor of the to.
			// we need to find where the common ancestor is between from and to
			// we use the 'pos' variable to locate the common ancestor.
			// pos is a pointer in to the tostack.
			Parent f = from;
			int fcnt = 0;
			// note that we search for 'pos' here... it will be set.
			while (f != null && (pos = locate(f, tostack)) < 0) {
				// go up the from ELement's ancestry until we intersect with the
				// to Element's Ancestry.
				fcnt++;
				f = f.getParent();
			}
			if (f == null) {
				throw new IllegalArgumentException(
						"The 'from' and 'to' Element have no common ancestor.");
			}
			// OK, we have counted how far up the ancestry we need to go, so
			// add the steps to the XPath.
			while (--fcnt >= 0) {
				sb.append("../");
			}
		}
		// we have the common point in the ancestry, indicated by 'pos'.
		// we walk down the 'to' side of the tree until we get to the target.
		while (--pos >= 0) {
			getSingleStep(tostack.get(pos), sb);
			sb.append("/");
		}
		// we automatically append '/' in the loop, so we remove the last '/'
		sb.setLength(sb.length() - 1);
		return sb;
	}

	/**
	 * Do an identity search in an array for a specific value.
	 * 
	 * @param f
	 *        The Element to search for.
	 * @param tostack
	 *        The list to search in.
	 * @return the position of the f value in the tostack.
	 */
	private static int locate(final Parent f, final List<Parent> tostack) {
		// a somewhat naive search... ArrayList it is fast enough though.
		int ret = tostack.size();
		while (--ret >= 0) {
			if (f == tostack.get(ret)) {
				return ret;
			}
		}
		return -1;
	}

	/**
	 * Returns the relative path from the given from Content to the specified to
	 * Content as an XPath expression.
	 * 
	 * @param from
	 *        the Content from which the the generated path shall be applied.
	 * @param to
	 *        the Content the generated path shall select.
	 * @return an XPath expression to select the specified node.
	 * @throws IllegalArgumentException
	 *         if <code>to</code> and <code>from</code> are not part of the same
	 *         XML tree
	 */
	public static String getRelativePath(final Content from, final Content to) {
		if (from == null) {
			throw new NullPointerException(
					"Cannot create a path from a null target");
		}
		if (to == null) {
			throw new NullPointerException(
					"Cannot create a path to a null target");
		}
		StringBuilder sb = new StringBuilder();
		if (from == to) {
			return ".";
		}
		final Element efrom = (from instanceof Element) ? (Element) from : from
				.getParentElement();
		if (from != efrom) {
			sb.append("../");
		}
		if (to instanceof Element) {
			getRelativeElementPath(efrom, (Element) to, sb);
		} else {
			final Parent telement = to.getParent();
			if (telement == null) {
				throw new IllegalArgumentException(
						"Cannot get a relative XPath to detached content.");
			}
			getRelativeElementPath(efrom, telement, sb);
			sb.append("/");
			getSingleStep(to, sb);
		}
		return sb.toString();
	}

	/**
	 * Returns the relative path from the given from Content to the specified to
	 * Attribute as an XPath expression.
	 * 
	 * @param from
	 *        the Content from which the the generated path shall be applied.
	 * @param to
	 *        the Attribute the generated path shall select.
	 * @return an XPath expression to select the specified node.
	 * @throws IllegalArgumentException
	 *         if <code>to</code> and <code>from</code> are not part of the same
	 *         XML tree
	 */
	public static String getRelativePath(final Content from, final Attribute to) {
		if (from == null) {
			throw new NullPointerException(
					"Cannot create a path from a null Content");
		}
		if (to == null) {
			throw new NullPointerException(
					"Cannot create a path to a null Attribute");
		}
		final Element t = to.getParent();
		if (t == null) {
			throw new IllegalArgumentException(
					"Cannot create a path to detached Attribute");
		}
		StringBuilder sb = new StringBuilder(getRelativePath(from, t));
		sb.append("/");
		getSingleStep(to, sb);
		return sb.toString();
	}

	/**
	 * Returns the relative path from the given from Attribute to the specified
	 * to Attribute as an XPath expression.
	 * 
	 * @param from
	 *        the Attribute from which the the generated path shall be applied.
	 * @param to
	 *        the Attribute the generated path shall select.
	 * @return an XPath expression to select the specified node.
	 * @throws IllegalArgumentException
	 *         if <code>to</code> and <code>from</code> are not part of the same
	 *         XML tree
	 */
	public static String getRelativePath(final Attribute from,
			final Attribute to) {
		if (from == null) {
			throw new NullPointerException(
					"Cannot create a path from a null 'from'");
		}
		if (to == null) {
			throw new NullPointerException(
					"Cannot create a path to a null target");
		}
		if (from == to) {
			return ".";
		}

		final Element f = from.getParent();
		if (f == null) {
			throw new IllegalArgumentException(
					"Cannot create a path from a detached attrbibute");
		}

		return "../" + getRelativePath(f, to);
	}

	/**
	 * Returns the relative path from the given from Attribute to the specified
	 * to Content as an XPath expression.
	 * 
	 * @param from
	 *        the Attribute from which the the generated path shall be applied.
	 * @param to
	 *        the Content the generated path shall select.
	 * @return an XPath expression to select the specified node.
	 * @throws IllegalArgumentException
	 *         if <code>to</code> and <code>from</code> are not part of the same
	 *         XML tree
	 */
	public static String getRelativePath(final Attribute from, final Content to) {
		if (from == null) {
			throw new NullPointerException(
					"Cannot create a path from a null 'from'");
		}
		if (to == null) {
			throw new NullPointerException(
					"Cannot create a path to a null target");
		}
		final Element f = from.getParent();
		if (f == null) {
			throw new IllegalArgumentException(
					"Cannot create a path from a detached attrbibute");
		}
		if (f == to) {
			return "..";
		}
		return "../" + getRelativePath(f, to);
	}

	/**
	 * Returns the absolute path to the specified to Content.
	 * 
	 * @param to
	 *        the Content the generated path shall select.
	 * @return an XPath expression to select the specified node.
	 * @throws IllegalArgumentException
	 *         if <code>to</code> is not an Element and it is detached.
	 */
	public static String getAbsolutePath(final Content to) {
		if (to == null) {
			throw new NullPointerException(
					"Cannot create a path to a null target");
		}

		final StringBuilder sb = new StringBuilder();

		final Element t = (to instanceof Element) ? (Element) to : to
				.getParentElement();
		if (t == null) {
			if (to.getParent() == null) {
				throw new IllegalArgumentException(
						"Cannot create a path to detached target");
			}
			// otherwise it must be at the document level.
			sb.append("/");
			getSingleStep(to, sb);
			return sb.toString();
		}
		Element r = t;
		while (r.getParentElement() != null) {
			r = r.getParentElement();
		}
		sb.append("/");
		getSingleStep(r, sb);
		if (r != t) {
			sb.append("/");
			getRelativeElementPath(r, t, sb);
		}
		if (t != to) {
			sb.append("/");
			getSingleStep(to, sb);
		}
		return sb.toString();
	}

	/**
	 * Returns the absolute path to the specified to Content.
	 * 
	 * @param to
	 *        the Content the generated path shall select.
	 * @return an XPath expression to select the specified node.
	 * @throws IllegalArgumentException
	 *         if <code>to</code> is detached.
	 */
	public static String getAbsolutePath(final Attribute to) {
		if (to == null) {
			throw new NullPointerException(
					"Cannot create a path to a null target");
		}

		final Element t = to.getParent();
		if (t == null) {
			throw new IllegalArgumentException(
					"Cannot create a path to detached target");
		}
		Element r = t;
		while (r.getParentElement() != null) {
			r = r.getParentElement();
		}
		final StringBuilder sb = new StringBuilder();

		sb.append("/");
		getSingleStep(r, sb);
		if (t != r) {
			sb.append("/");
			getRelativeElementPath(r, t, sb);
		}
		sb.append("/");
		getSingleStep(to, sb);
		return sb.toString();
	}

}
