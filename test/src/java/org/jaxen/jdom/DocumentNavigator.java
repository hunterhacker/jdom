package org.jaxen.jdom;
/*
 * $Header$
 * $Revision: 1198 $
 * $Date: 2006-09-21 04:48:05 -0700 (Thu, 21 Sep 2006) $
 *
 * ====================================================================
 *
 * Copyright 2000-2005 bob mcwhirter & James Strachan.
 * All rights reserved.
 *
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 * 
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 * 
 *   * Neither the name of the Jaxen Project nor the names of its
 *     contributors may be used to endorse or promote products derived 
 *     from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * ====================================================================
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Jaxen Project and was originally
 * created by bob mcwhirter <bob@werken.com> and
 * James Strachan <jstrachan@apache.org>.  For more information on the
 * Jaxen Project, please see <http://www.jaxen.org/>.
 *
 * $Id: DocumentNavigator.java 1198 2006-09-21 11:48:05Z elharo $
*/

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jaxen.DefaultNavigator;
import org.jaxen.FunctionCallException;
import org.jaxen.NamedAccessNavigator;
import org.jaxen.Navigator;
import org.jaxen.XPath;
import org.jaxen.JaxenConstants;
import org.jaxen.saxpath.SAXPathException;
import org.jaxen.util.SingleObjectIterator;
import org.jdom2.Attribute;
import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;
import org.jdom2.input.SAXBuilder;

/** 
 * Interface for navigating around the JDOM object model.
 *
 * <p>
 * This class is not intended for direct usage, but is
 * used by the Jaxen engine during evaluation.
 * </p>
 *
 * @see XPath
 *
 * @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 * @author Stephen Colebourne
 */
public class DocumentNavigator extends DefaultNavigator implements NamedAccessNavigator
{
	/** Wrapper for JDOM namespace nodes to give them a parent, as required
	 *  by the XPath data model.
	 *
	 *  @author Erwin Bolwidt
	 */
	public static class XPathNamespace
	{
	    private Element jdomElement;

	    private Namespace jdomNamespace;

	    /** Creates a namespace-node wrapper for a namespace node that hasn't been
	     *  assigned to an element yet.
	     */
	    public XPathNamespace( Namespace jdomNamespace )
	    {
	        this.jdomNamespace = jdomNamespace;
	    }

	    /** Creates a namespace-node wrapper for a namespace node that is assigned
	     *  to the given JDOM element.
	     */
	    public XPathNamespace( Element jdomElement, Namespace jdomNamespace )
	    {
	        this.jdomElement = jdomElement;
	        this.jdomNamespace = jdomNamespace;
	    }

	    /** Returns the JDOM element from which this namespace node has been 
	     *  retrieved. The result may be null when the namespace node has not yet
	     *  been assigned to an element.
	     */
	    public Element getJDOMElement()
	    {
	        return jdomElement;
	    }

	    /** Sets or changes the element to which this namespace node is assigned.
	     */
	    public void setJDOMElement( Element jdomElement )
	    {
	        this.jdomElement = jdomElement;
	    }

	    /** Returns the JDOM namespace object of this namespace node; the JDOM
	     *  namespace object contains the prefix and URI of the namespace.
	     */
	    public Namespace getJDOMNamespace()
	    {
	        return jdomNamespace;
	    }

	    public String toString()
	    {
	        return ( "[xmlns:" + jdomNamespace.getPrefix() + "=\"" +
	                 jdomNamespace.getURI() + "\", element=" +
	                 jdomElement.getName() + "]" );
	    }
	} 

	
	/**
     * 
     */
    private static final long serialVersionUID = -1636727587303584165L;

    /** Singleton implementation.
     */
    private static class Singleton
    {
        /** Singleton instance.
         */
        private static DocumentNavigator instance = new DocumentNavigator();
    }

    public static Navigator getInstance()
    {
        return Singleton.instance;
    }

    public boolean isElement(Object obj)
    {
        return obj instanceof Element;
    }

    public boolean isComment(Object obj)
    {
        return obj instanceof Comment;
    }

    public boolean isText(Object obj)
    {
        return ( obj instanceof Text
                 ||
                 obj instanceof CDATA );
    }

    public boolean isAttribute(Object obj)
    {
        return obj instanceof Attribute;
    }

    public boolean isProcessingInstruction(Object obj)
    {
        return obj instanceof ProcessingInstruction;
    }

    public boolean isDocument(Object obj)
    {
        return obj instanceof Document;
    }

    public boolean isNamespace(Object obj)
    {
        return obj instanceof Namespace || obj instanceof XPathNamespace;
    }

    public String getElementName(Object obj)
    {
        Element elem = (Element) obj;

        return elem.getName();
    }

    public String getElementNamespaceUri(Object obj)
    {
        Element elem = (Element) obj;
        
        String uri = elem.getNamespaceURI();
        if ( uri != null && uri.length() == 0 ) 
            return null;
        else
            return uri;
    }

    public String getAttributeName(Object obj)
    {
        Attribute attr = (Attribute) obj;

        return attr.getName();
    }

    public String getAttributeNamespaceUri(Object obj)
    {
        Attribute attr = (Attribute) obj;

        String uri = attr.getNamespaceURI();
        if ( uri != null && uri.length() == 0 ) 
            return null;
        else
            return uri;
    }

    public Iterator getChildAxisIterator(Object contextNode)
    {
        if ( contextNode instanceof Element )
        {
            return ((Element)contextNode).getContent().iterator();
        }
        else if ( contextNode instanceof Document )
        {
            return ((Document)contextNode).getContent().iterator();
        }

        return JaxenConstants.EMPTY_ITERATOR;
    }

    /**
     * Retrieves an <code>Iterator</code> over the child elements that
     * match the supplied local name and namespace URI.
     *
     * @param contextNode      the origin context node
     * @param localName        the local name of the children to return, always present
     * @param namespacePrefix  ignored; prefixes are not used when matching in XPath
     * @param namespaceURI     the URI of the namespace of the children to return
     * @return an Iterator     that traverses the named children, or null if none
     */
    public Iterator getChildAxisIterator(
            Object contextNode, String localName, String namespacePrefix, String namespaceURI) {

        if ( contextNode instanceof Element ) {
            Element node = (Element) contextNode;
            if (namespaceURI == null) {
                return node.getChildren(localName).iterator();
            }
            return node.getChildren(localName, Namespace.getNamespace(namespacePrefix, namespaceURI)).iterator();
        }
        if ( contextNode instanceof Document ) {
            Document node = (Document) contextNode;
            
            Element el = node.getRootElement();
            if (el.getName().equals(localName) == false) {
                return JaxenConstants.EMPTY_ITERATOR;
            }
            if (namespaceURI != null) {
                // JDOM's equals method does not consider the prefix when comparing namespace objects
                if (!Namespace.getNamespace(namespacePrefix, namespaceURI).equals(el.getNamespace())) {
                    return JaxenConstants.EMPTY_ITERATOR;
                }
            }
            else if(el.getNamespace() != Namespace.NO_NAMESPACE) { 
                return JaxenConstants.EMPTY_ITERATOR; 
            }
            
            return new SingleObjectIterator(el);
        }

        return JaxenConstants.EMPTY_ITERATOR;
    }
    
    public Iterator getNamespaceAxisIterator(Object contextNode)
    {
        if ( ! ( contextNode instanceof Element ) )
        {
            return JaxenConstants.EMPTY_ITERATOR;
        }

        Element elem = (Element) contextNode;

        Map nsMap = new HashMap();

        Element current = elem;

        while ( current != null ) {
        
            Namespace ns = current.getNamespace();
            
            if ( ns != Namespace.NO_NAMESPACE ) {
                if ( !nsMap.containsKey(ns.getPrefix()) )
                    nsMap.put( ns.getPrefix(), new XPathNamespace(elem, ns) );
            }
        
            Iterator additional = current.getAdditionalNamespaces().iterator();

            while ( additional.hasNext() ) {

                ns = (Namespace)additional.next();
                if ( !nsMap.containsKey(ns.getPrefix()) )
                    nsMap.put( ns.getPrefix(), new XPathNamespace(elem, ns) );
            }

            Iterator attributes = current.getAttributes().iterator();

            while ( attributes.hasNext() ) {

                Attribute attribute = (Attribute)attributes.next();

                Namespace attrNS = attribute.getNamespace();
            
                if ( attrNS != Namespace.NO_NAMESPACE ) {
                    if ( !nsMap.containsKey(attrNS.getPrefix()) )
                        nsMap.put( attrNS.getPrefix(), new XPathNamespace(elem, attrNS) );
                }
            }

            if (current.getParent() instanceof Element) {
                current = (Element)current.getParent();
            } else {
                current = null;
            }
        }

        nsMap.put( "xml", new XPathNamespace(elem, Namespace.XML_NAMESPACE) );

        return nsMap.values().iterator();
    }

    public Iterator getParentAxisIterator(Object contextNode)
    {
        Object parent = null;

        if ( contextNode instanceof Document )
        {
            return JaxenConstants.EMPTY_ITERATOR;
        }
        else if ( contextNode instanceof Element )
        {
            parent = ((Element)contextNode).getParent();

            if ( parent == null )
            {
                if ( ((Element)contextNode).isRootElement() )
                {
                    parent = ((Element)contextNode).getDocument();
                }
            }
        }
        else if ( contextNode instanceof Attribute )
        {
            parent = ((Attribute)contextNode).getParent();
        }
        else if ( contextNode instanceof XPathNamespace )
        {
            parent = ((XPathNamespace)contextNode).getJDOMElement();
        }
        else if ( contextNode instanceof ProcessingInstruction )
        {
            parent = ((ProcessingInstruction)contextNode).getParent();
        }
        else if ( contextNode instanceof Comment )
        {
            parent = ((Comment)contextNode).getParent();
        }
        else if ( contextNode instanceof Text )
        {
            parent = ((Text)contextNode).getParent();
        }
        
        if ( parent != null )
        {
            return new SingleObjectIterator( parent );
        }

        return JaxenConstants.EMPTY_ITERATOR;
    }

    public Iterator getAttributeAxisIterator(Object contextNode)
    {
        if ( ! ( contextNode instanceof Element ) )
        {
            return JaxenConstants.EMPTY_ITERATOR;
        }

        Element elem = (Element) contextNode;

        return elem.getAttributes().iterator();
    }

    /**
     * Retrieves an <code>Iterator</code> over the attribute elements that
     * match the supplied name.
     *
     * @param contextNode      the origin context node
     * @param localName        the local name of the attributes to return, always present
     * @param namespacePrefix  the prefix of the namespace of the attributes to return
     * @param namespaceURI     the URI of the namespace of the attributes to return
     * @return an Iterator     that traverses the named attributes, not null
     */
    public Iterator getAttributeAxisIterator(
            Object contextNode, String localName, String namespacePrefix, String namespaceURI) {

        if ( contextNode instanceof Element ) {
            Element node = (Element) contextNode;
            Namespace namespace = (namespaceURI == null ? Namespace.NO_NAMESPACE : 
                                    Namespace.getNamespace(namespacePrefix, namespaceURI));
            Attribute attr = node.getAttribute(localName, namespace);
            if (attr != null) {
                return new SingleObjectIterator(attr);
            }
        }
        return JaxenConstants.EMPTY_ITERATOR;
    }

    /** Returns a parsed form of the given XPath string, which will be suitable
     *  for queries on JDOM documents.
     */
    public XPath parseXPath (String xpath) throws SAXPathException
    {
        return new JDOMXPath(xpath);
    }

    public Object getDocumentNode(Object contextNode)
    {
        if ( contextNode instanceof Document )
        {
            return contextNode;
        }

        Element elem = (Element) contextNode;

        return elem.getDocument();
    }

    public String getElementQName(Object obj)
    {
        Element elem = (Element) obj;

        String prefix = elem.getNamespacePrefix();

        if ( prefix == null || prefix.length() == 0 )
        {
            return elem.getName();
        }

        return prefix + ":" + elem.getName();
    }

    public String getAttributeQName(Object obj)
    {
        Attribute attr = (Attribute) obj;

        String prefix = attr.getNamespacePrefix();

        if ( prefix == null || "".equals( prefix ) )
        {
            return attr.getName();
        }

        return prefix + ":" + attr.getName();
    }

    public String getNamespaceStringValue(Object obj)
    {
        if (obj instanceof Namespace) {

            Namespace ns = (Namespace) obj;
            return ns.getURI();
        } else {

            XPathNamespace ns = (XPathNamespace) obj;
            return ns.getJDOMNamespace().getURI();
        }
        
    }

    public String getNamespacePrefix(Object obj)
    {
        if (obj instanceof Namespace) {

            Namespace ns = (Namespace) obj;
            return ns.getPrefix();
        } else {

            XPathNamespace ns = (XPathNamespace) obj;
            return ns.getJDOMNamespace().getPrefix();
        }
    }

    public String getTextStringValue(Object obj)
    {
        if ( obj instanceof Text )
        {
            return ((Text)obj).getText();
        }

        if ( obj instanceof CDATA )
        {
            return ((CDATA)obj).getText();
        }

        return "";
    }

    public String getAttributeStringValue(Object obj)
    {
        Attribute attr = (Attribute) obj;

        return attr.getValue();
    }

    public String getElementStringValue(Object obj)
    {
        Element elem = (Element) obj;

        StringBuffer buf = new StringBuffer();

        List     content     = elem.getContent();
        Iterator contentIter = content.iterator();
        Object   each        = null;

        while ( contentIter.hasNext() )
        {
            each = contentIter.next();

            if ( each instanceof Text )
            {
                buf.append( ((Text)each).getText() );
            }
            else if ( each instanceof CDATA )
            {
                buf.append( ((CDATA)each).getText() );
            }
            else if ( each instanceof Element )
            {
                buf.append( getElementStringValue( each ) );
            }
        }

        return buf.toString();
    }

    public String getProcessingInstructionTarget(Object obj)
    {
        ProcessingInstruction pi = (ProcessingInstruction) obj;

        return pi.getTarget();
    }

    public String getProcessingInstructionData(Object obj)
    {
        ProcessingInstruction pi = (ProcessingInstruction) obj;

        return pi.getData();
    }

    public String getCommentStringValue(Object obj)
    {
        Comment cmt = (Comment) obj;

        return cmt.getText();
    }

    public String translateNamespacePrefixToUri(String prefix, Object context)
    {
        Element element = null;
        if ( context instanceof Element ) 
        {
            element = (Element) context;
        }
        else if ( context instanceof Text )
        {
            element = (Element)((Text)context).getParent();
        }
        else if ( context instanceof Attribute )
        {
            element = ((Attribute)context).getParent();
        }
        else if ( context instanceof XPathNamespace )
        {
            element = ((XPathNamespace)context).getJDOMElement();
        }
        else if ( context instanceof Comment )
        {
            element = (Element)((Comment)context).getParent();
        }
        else if ( context instanceof ProcessingInstruction )
        {
            element = (Element)((ProcessingInstruction)context).getParent();
        }

        if ( element != null )
        {
            Namespace namespace = element.getNamespace( prefix );

            if ( namespace != null ) 
            {
                return namespace.getURI();
            }
        }
        return null;
    }

    public Object getDocument(String url) throws FunctionCallException
    {
        try
        {
            SAXBuilder builder = new SAXBuilder();
            
            return builder.build( url );
        }
        catch (Exception e)
        {
            throw new FunctionCallException( e.getMessage() );
        }
    }
}
