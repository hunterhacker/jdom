/*--

 Copyright (C) 2012 Jason Hunter & Brett McLaughlin.
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

package org.jdom2.contrib.xpath.xalan;

import java.util.Collections;
import java.util.List;

import javax.xml.transform.SourceLocator;

import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.dtm.ref.DTMDefaultBaseIterators;
import org.apache.xml.dtm.ref.DTMManagerDefault;
import org.apache.xml.dtm.ref.ExpandedNameTable;
import org.apache.xml.res.XMLErrorResources;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.utils.XMLString;
import org.apache.xml.utils.XMLStringFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

import org.jdom2.Attribute;
import org.jdom2.Content;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.NamespaceAware;
import org.jdom2.Parent;
import org.jdom2.internal.ArrayCopy;
import org.jdom2.transform.JDOMSource;
import org.jdom2.util.NamespaceStack;

/**
 * ***********************************************
 * THIS CODE IS NOT COMPLETE... DO NOT USE IT
 * Marked Deprecated
 * ***********************************************
 * 
 * 
 * 
 * 
 * Create a DTM navigator to a JDOM document
 * 
 * @author Rolf Lear
 *
 */
@Deprecated
public class JDOM2DTM extends DTMDefaultBaseIterators {

	private static final int getType(NamespaceAware nsa) {
		if (nsa instanceof Content) {
			switch (((Content)nsa).getCType()) {
				case CDATA:
					return DTM.CDATA_SECTION_NODE;
				case Comment:
					return DTM.COMMENT_NODE;
				case DocType:
					return DTM.DOCUMENT_TYPE_NODE;
				case Element:
					return DTM.ELEMENT_NODE;
				case EntityRef:
					return DTM.ENTITY_REFERENCE_NODE;
				case ProcessingInstruction:
					return DTM.PROCESSING_INSTRUCTION_NODE;
				case Text:
					return DTM.TEXT_NODE;
			}
		} else if (nsa instanceof Document) {
			return DTM.DOCUMENT_NODE;
		} else if (nsa instanceof Attribute) {
			return DTM.ATTRIBUTE_NODE;
		} else if (nsa instanceof NamespacePointer) {
			return DTM.NAMESPACE_NODE;
		}
		throw new IllegalStateException("Unknonw node type " + nsa);
	}
	
	private NamespaceAware[] i_nodes = new NamespaceAware[1024];
	
	private final String systemId, publicID;
	
	/**
	 * @param mgr The DTMManager
	 * @param source The Source
	 * @param dtmIdentity The dtmIdentity
	 * @param whiteSpaceFilter the whitespace filter
	 * @param xstringfactory the xstringfactory
	 * @param doIndexing the indexing flag.
	 */
	public JDOM2DTM(DTMManager mgr, JDOMSource source, int dtmIdentity,
			DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory,
			boolean doIndexing) {
		super(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing);
		String pid = null, sid = null;
		Parent root = source.getDocument();
		final NamespaceStack nstack = new NamespaceStack();
		// we do not play around with 'on-demand' building of the tree.
		// we build the whole thing 'one-shot'. maybe at some point we can
		// make the process on-demand.
		if (root != null) {
			// the root is a document node.
			final Document doc = (Document)root;
			setDocumentBaseURI(doc.getBaseURI());
			DocType dt = doc.getDocType();
			if (dt != null) {
				sid = dt.getSystemID();
				pid = dt.getPublicID();
			}
			if (sid == null) {
				sid = doc.getBaseURI();
			}
			
			addNodes(nstack, NULL, Collections.singletonList(doc));
		} else {
			addNodes(nstack, NULL, source.getNodes());
		}
		systemId = sid;
		publicID = pid;
	}
	
	private int addNode(final NamespaceStack nstack, final int parent, 
			final int prevsib, final NamespaceAware nsa) {
		
		final int nodeIndex = m_size++;
		if (nodeIndex >= i_nodes.length) {
			// add 50%
			i_nodes = ArrayCopy.copyOf(i_nodes, nodeIndex + (nodeIndex >> 1) + 1);
		}
		i_nodes[nodeIndex] = nsa;

		// copied from DOM2DTM.java .. not sure what this does....
		// Have we overflowed a DTM Identity's addressing range?
		if(m_dtmIdent.size() == (nodeIndex>>>DTMManager.IDENT_DTM_NODE_BITS)) {
			if(m_mgr==null) {
				//"No more DTM IDs are available";
				error(XMLMessages.createXMLMessage(XMLErrorResources.ER_NO_DTMIDS_AVAIL, null));
			}

			// Handle as Extended Addressing
			final DTMManagerDefault mgrD=(DTMManagerDefault)m_mgr;
			final int id=mgrD.getFirstFreeDTMID();
			mgrD.addDTM(this,id,nodeIndex);
			m_dtmIdent.addElement(id<<DTMManager.IDENT_DTM_NODE_BITS);
		}
		
	    m_firstch.setElementAt(NOTPROCESSED,nodeIndex);
	    m_nextsib.setElementAt(NOTPROCESSED,nodeIndex);
	    m_prevsib.setElementAt(prevsib,nodeIndex);
	    m_parent.setElementAt(parent,nodeIndex);
	    
	    if (((nsa instanceof Content) || (nsa instanceof Document)) && 
	    		NOTPROCESSED == m_firstch.elementAt(parent)) {
	    	// we are the first child.
	    	// more than that, the 'Content/Document' test excludes Namespace
	    	// and Attribute nodes... which do not form part of the XPath tree
	    	m_firstch.setElementAt(nodeIndex, parent);
	    }
	    
	    String localname = null;
	    String nsuri = null;
	    if (nsa instanceof Element) {
	    	localname = ((Element)nsa).getName();
	    	nsuri = ((Element)nsa).getNamespaceURI();
	    } else if (nsa instanceof Attribute) {
	    	localname = ((Attribute)nsa).getName();
	    	nsuri = ((Attribute)nsa).getNamespaceURI();
	    }
	    		
	    final ExpandedNameTable exnt = m_expandedNameTable;
	    final int ntype = getType(nsa);

	    final int expandedNameID = (localname == null) ? 
	    		exnt.getExpandedTypeID(ntype) : 
	    			exnt.getExpandedTypeID(nsuri, localname, ntype);
	    		
	    m_exptype.setElementAt(expandedNameID,nodeIndex);
	    indexNode(expandedNameID, nodeIndex);
	    
	    if (nsa instanceof Parent) {
	    	// recurse through the child content.
	    	final Parent pn = (Parent)nsa;
	    	if (pn instanceof Element) {
	    		final Element element = (Element)pn;
	    		nstack.push(element);
	    		try {
	    			int pns = NULL;
	    			int nsid = NULL;
	    			for (Namespace ns : nstack.addedForward()) {
	    				nsid = addNode(nstack, nodeIndex, pns, 
	    						new NamespacePointer(ns));
	    				if (pns != NULL) {
	    					m_nextsib.setElementAt(nsid, pns);
	    				}
	    				pns = nsid;
	    			}
	    			if (nsid != NULL) {
	    				// Set the last namespace sibling to NULL next. 
	    				m_nextsib.setElementAt(NULL, nsid);
	    			}
	    			if (element.hasAttributes()) {
	    				int patt = NULL;
	    				int attid = NULL;
	    				for (Attribute att : element.getAttributes()) {
	    					attid = addNode(nstack, nodeIndex, patt, att);
	    					if (patt != NULL) {
	    						m_nextsib.setElementAt(attid, patt);
	    					}
	    					patt = attid;
	    				}
	    				if (attid != NULL) {
	    					// set the last attribute sibling to NULL next
	    					m_nextsib.setElementAt(NULL, attid);
	    				}
	    			}
	    			if (element.getContentSize() > 0) {
	    				addNodes(nstack, nodeIndex, element.getContent());
	    			}
	    		} finally {
	    			nstack.pop();
	    		}
	    	} else {
	    		// must be Document.
    			if (pn.getContentSize() > 0) {
    				addNodes(nstack, nodeIndex, pn.getContent());
    			}
	    	}
	    }
	    
	    return nodeIndex;
	}

	private void addNodes(NamespaceStack nstack, int parentIndex,
			List<? extends NamespaceAware> list) {
    	final int cs = list.size();
    	if (cs > 0) {
    		int pvid = NULL;
    		int cid = NULL;
    		for (int i = 0; i < cs; i++) {
    			final NamespaceAware c = list.get(i);
    			cid = addNode(nstack, parentIndex, pvid, c);
    			if (pvid == NULL) {
    				m_firstch.setElementAt(cid, parentIndex);
    			} else {
    				m_nextsib.setElementAt(cid, pvid);
    			}
    			pvid = cid;
    		}
    		if (cid != NULL) {
    			m_nextsib.setElementAt(NULL, cid);
    		}
    	}
		
	}

	@Override
	protected int getNextNodeIdentity(int identity) {
	    identity += 1;

	    if (identity >= m_size) {
	    	identity = DTM.NULL;
	    }

	    return identity;
	}

	@Override
	protected boolean nextNode() {
		// we pre-build the entire node, so this is always false.
		return false;
	}

	@Override
	protected int getNumberOfNodes() {
		// we prebuild, so always false.
		return m_size;
	}

	@Override
	public int getAttributeNode(int nodeHandle, String namespaceURI, String name) {
		
		return 0;
	}

	@Override
	public XMLString getStringValue(int nodeHandle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNodeName(int nodeHandle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLocalName(int nodeHandle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPrefix(int nodeHandle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNamespaceURI(int nodeHandle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNodeValue(int nodeHandle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDocumentTypeDeclarationSystemIdentifier() {
		return systemId;
	}

	@Override
	public String getDocumentTypeDeclarationPublicIdentifier() {
		return publicID;
	}

	@Override
	public int getElementById(String elementId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getUnparsedEntityURI(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAttributeSpecified(int attributeHandle) {
		// all attributes are specified
		return true;
	}

	@Override
	public SourceLocator getSourceLocatorFor(int node) {
		return null;
	}

	@Override
	public void setProperty(String property, Object value) {
		// nothing.
	}

	@Override
	public boolean needsTwoThreads() {
		return false;
	}
	
	
	/* ***************************************************
	 * Unneeded SAX-Based methods.
	 * *************************************************** */

	@Override
	public ContentHandler getContentHandler() {
		// nothing
		return null;
	}

	@Override
	public LexicalHandler getLexicalHandler() {
		// nothing
		return null;
	}

	@Override
	public EntityResolver getEntityResolver() {
		// nothing
		return null;
	}

	@Override
	public DTDHandler getDTDHandler() {
		// nothing
		return null;
	}

	@Override
	public ErrorHandler getErrorHandler() {
		// nothing
		return null;
	}

	@Override
	public DeclHandler getDeclHandler() {
		// nothing
		return null;
	}

	@Override
	public void dispatchCharactersEvents(int nodeHandle, ContentHandler ch,
			boolean normalize) throws SAXException {
		// nothing
	}

	@Override
	public void dispatchToEvents(int nodeHandle, ContentHandler ch)
			throws SAXException {
		// nothing
	}

}
