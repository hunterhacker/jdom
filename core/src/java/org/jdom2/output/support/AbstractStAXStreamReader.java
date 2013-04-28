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

package org.jdom2.output.support;

import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.jdom2.Attribute;
import org.jdom2.Content;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.EntityRef;
import org.jdom2.Namespace;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Verifier;
import org.jdom2.internal.ArrayCopy;
import org.jdom2.output.Format;
import org.jdom2.output.StAXStreamOutputter;
import org.jdom2.output.StAXStreamReader;
import org.jdom2.output.XMLOutputter;
import org.jdom2.output.Format.TextMode;
import org.jdom2.util.JDOMNamespaceContext;
import org.jdom2.util.NamespaceStack;

/**
 * An {@link XMLStreamReader} implementation that reads the XML document
 * out of a JDOM {@link Document}. This class is marked as abstract even though it is
 * a full implementation of the XMLReader. This class can be overridden if needed.
 * If you extend this class, you will need to also extend {@link AbstractStAXStreamReaderProcessor}
 * and supply an instance of that customised class to the {@link StAXStreamReader} class.
 * <p>
 * The reader reads XML Events by walking the JDOM tree, reporting all XML stream
 * events as it encounters them in the JDOM.
 * <p>
 * This class is the inverse of the class {@link StAXStreamOutputter} in the sense that
 * this class is read from (it's an XMLStreamReader implementation) by interpreting a JDOM
 * Document whereas the StAXStreamOutputter interprets a JDOM Document and <strong>writes to</strong>
 *  a user-supplied XMLStreamWriter. It is the difference between a 'pull' concept and a
 *  'push' concept.
 * 
 * @author gordon burgett https://github.com/gburgett
 * @author Rolf Lear
 * @since JDOM 2.1
 */
public abstract class AbstractStAXStreamReader extends AbstractOutputProcessor implements XMLStreamReader {
	
	private static final class DocumentWalker implements Walker {
		
		private final Content[] data;
		private int pos = 0;
		
		public DocumentWalker(final Document doc) {
			data = doc.getContent().toArray(new Content[doc.getContentSize()]);
		}

		@Override
		public boolean isAllText() {
			return false;
		}

		@Override
		public boolean isAllWhitespace() {
			return false;
		}

		@Override
		public boolean hasNext() {
			return pos < data.length;
		}

		@Override
		public Content next() {
			return data[pos++];
		}

		@Override
		public String text() {
			return null;
		}

		@Override
		public boolean isCDATA() {
			return false;
		}
		
	}

	private final FormatStack formatstack;
	private final NamespaceStack nsstack = new NamespaceStack();
	
    private Document document;
    
    private String curi = null, clocalname = null, cprefix = null, 
    		ctext = null, ctarget = null, cdata = null;
    private Element [] emtstack = new Element[32];
    private Walker[] stack = new Walker[32];
    private int depth = 0;
    
    private int currentEvt = START_DOCUMENT;
    
    /**
     * Create a new AbstractStAXStreamReader that outputs a JDOM Document as an XMLStream.
     * @param document the document to output.
     * @param format The output format to use. 
     */
    public AbstractStAXStreamReader(Document document, Format format){
        this.document = document;
        this.formatstack = new FormatStack(format);
        stack[0] = new DocumentWalker(document);
    }
    
    /**
     * Create a new AbstractStAXStreamReader that outputs a JDOM Document as an XMLStream using
     * the Format.getRawFormat() format.
     * @param document the document to output.
     */
    public AbstractStAXStreamReader(Document document) {
    	this(document, Format.getRawFormat());
    }
    
    @Override
    public boolean hasNext() throws XMLStreamException {
        return depth >= 0;
    }

    @Override
    public int next() throws XMLStreamException {
    	if (depth < 0) {
    		throw new NoSuchElementException("No more data available.");
    	}
    	
        curi = null;
        clocalname = null;
        cprefix = null;
        ctext = null;
        ctarget = null;
        cdata = null;
        
        if (currentEvt == END_ELEMENT) {
        	nsstack.pop();
        	formatstack.pop();
        	emtstack[depth + 1] = null;
        }
        
        // confirm next walker item.
        if (!stack[depth].hasNext()) {
        	// no more items at this level.
        	stack[depth] = null;
        	// we kill the element stack at the end of the END_ELEMENT event.
        	// emtstack[depth] = null
        	depth--;
        	return currentEvt = (depth < 0 ? END_DOCUMENT : END_ELEMENT);
        }
        
        final Content c = stack[depth].next();
        if (c == null) {
        	// formatted text or CDATA.
        	ctext = stack[depth].text();
        	return currentEvt = stack[depth].isCDATA() ? CDATA : CHARACTERS;
        }
        
        switch (c.getCType()) {
        	case CDATA:
        		ctext = c.getValue();
        		return currentEvt = CDATA;
        	case Text:
        		ctext = c.getValue();
        		return currentEvt = CHARACTERS;
        	case Comment:
        		ctext = c.getValue();
        		return currentEvt = COMMENT;
        	case DocType:
        		// format doctype appropriately.
        		XMLOutputter xout = new XMLOutputter();
        		ctext = xout.outputString((DocType)c);
        		return currentEvt = DTD;
        	case EntityRef:
        		clocalname = ((EntityRef)c).getName();
        		ctext = "";
        		return currentEvt = ENTITY_REFERENCE;
        	case ProcessingInstruction:
        		final ProcessingInstruction pi = (ProcessingInstruction)c;
        		ctarget = pi.getTarget();
        		cdata = pi.getData();
        		return currentEvt = PROCESSING_INSTRUCTION;
        	case Element:
        		// great
        		// we deal with Element outside the switch statement.
        		break;
        	default:
        		throw new IllegalStateException("Unexpected content " + c);
        }
        // OK, we break out here if we are an Element start.
        final Element emt = (Element)c;
    	clocalname = emt.getName();
    	cprefix = emt.getNamespacePrefix();
    	curi = emt.getNamespaceURI();
        
        nsstack.push(emt);
        formatstack.push();
        final String space = emt.getAttributeValue("space", Namespace.XML_NAMESPACE);
		// Check for xml:space and adjust format settings
		if ("default".equals(space)) {
			formatstack.setTextMode(formatstack.getDefaultMode());
		}
		else if ("preserve".equals(space)) {
			formatstack.setTextMode(TextMode.PRESERVE);
		}
		
		depth++;
		if (depth >= stack.length) {
			stack = ArrayCopy.copyOf(stack, depth + 32);
			emtstack = ArrayCopy.copyOf(emtstack, depth+32);
		}
		
		emtstack[depth] = emt;
		stack[depth] = buildWalker(formatstack, emt.getContent(), false);

		return currentEvt = START_ELEMENT;
        
    }

    @Override
    public int getEventType() {
        return currentEvt;
    }

    @Override
    public boolean isStartElement() {
        return currentEvt == START_ELEMENT;
    }

    @Override
    public boolean isEndElement() {
        return currentEvt == END_ELEMENT;
    }

    @Override
    public boolean isCharacters() {
        return currentEvt == CHARACTERS;
    }

    @Override
    public boolean isWhiteSpace() {
    	switch (currentEvt) {
    		case SPACE :
    			return true;
    		case CDATA:
    		case CHARACTERS:
    			return Verifier.isAllXMLWhitespace(ctext);
    		default:
    			return false;
    	}
    }

    @Override
    public void require(int type, String namespaceURI, String localName) throws XMLStreamException {
        if(type != getEventType()){
            throw new XMLStreamException("required event " + type + " but got event " + getEventType());
        }
        
        if(localName != null){
            if(!localName.equals(clocalname)){
                throw new XMLStreamException("required name " + localName + " but got name " + clocalname);
            }
        }
        
        if(namespaceURI != null){
            if(!namespaceURI.equals(curi)){
                throw new XMLStreamException("required namespace " + namespaceURI + " but got namespace " + curi);
            }
        }
    }

    @Override
    public QName getName() {
    	switch (currentEvt) {
    		case START_ELEMENT:
    			final Element emts = emtstack[depth];
    			return new QName(emts.getNamespaceURI(), emts.getName(), emts.getNamespacePrefix());
    		case END_ELEMENT:
    			final Element emte = emtstack[depth + 1];
    			return new QName(emte.getNamespaceURI(), emte.getName(), emte.getNamespacePrefix());
    		default:
    			throw new IllegalStateException("getName not supported for event " + currentEvt);
        }
    }

    @Override
    public String getLocalName() {
    	switch (currentEvt) {
    		case START_ELEMENT:
    		case END_ELEMENT:
            case ENTITY_REFERENCE:
    			return clocalname;
    		default:
    			throw new IllegalStateException("getLocalName not supported for event " + currentEvt);
        }
    }

    @Override
    public boolean hasName() {
        return currentEvt == START_ELEMENT || currentEvt == END_ELEMENT;
    }

    @Override
    public String getNamespaceURI() {
    	switch (currentEvt) {
    		case START_ELEMENT:
    		case END_ELEMENT:
    			return curi;
    		default:
                throw new IllegalStateException("getNamespaceURI not supported for event " + currentEvt);
        }
    }

    @Override
    public String getPrefix() {
    	switch (currentEvt) {
    		case START_ELEMENT:
    		case END_ELEMENT:
    			return cprefix;
    		default:
                throw new IllegalStateException("getPrefix not supported for event " + currentEvt);
        }
    }
    
    @Override
    public String getPITarget() {
    	switch (currentEvt) {
    		case PROCESSING_INSTRUCTION:
    			return ctarget;
    		default:
                throw new IllegalStateException("getPITarget not supported for event " + currentEvt);
        }
    }

    @Override
    public String getPIData() {
    	switch (currentEvt) {
    		case PROCESSING_INSTRUCTION:
    			return cdata;
    		default:
                throw new IllegalStateException("getPIData not supported for event " + currentEvt);
        }
    }
    
    
    @Override
    public String getElementText() throws XMLStreamException {
        // copied from documentation.
        if(getEventType() != XMLStreamConstants.START_ELEMENT) {
            throw new XMLStreamException("parser must be on START_ELEMENT to read next text");
        }
        
        int eventType = next();
        StringBuilder buf = new StringBuilder();
        while(eventType != XMLStreamConstants.END_ELEMENT ) {
            if(eventType == XMLStreamConstants.CHARACTERS
                    || eventType == XMLStreamConstants.CDATA
                    || eventType == XMLStreamConstants.SPACE
                    || eventType == XMLStreamConstants.ENTITY_REFERENCE) {
                buf.append(getText());
            } else if(eventType == XMLStreamConstants.PROCESSING_INSTRUCTION
                    || eventType == XMLStreamConstants.COMMENT) {
                // skipping
            } else if(eventType == XMLStreamConstants.END_DOCUMENT) {
                throw new XMLStreamException("unexpected end of document when reading element text content", getLocation());
            } else if(eventType == XMLStreamConstants.START_ELEMENT) {
                throw new XMLStreamException("element text content may not contain START_ELEMENT", getLocation());
            } else {
                throw new XMLStreamException("Unexpected event type "+eventType, getLocation());
            }
            
            eventType = next();
        }
        return buf.toString();
    }

    @Override
    public int nextTag() throws XMLStreamException {
        int eventType = next();
        while((eventType == XMLStreamConstants.CHARACTERS && isWhiteSpace()) // skip whitespace
                    || (eventType == XMLStreamConstants.CDATA && isWhiteSpace())
                    // skip whitespace
                    || eventType == XMLStreamConstants.SPACE
                    || eventType == XMLStreamConstants.PROCESSING_INSTRUCTION
                    || eventType == XMLStreamConstants.COMMENT
                )
        {
            eventType = next();
        }
        
        if (eventType != XMLStreamConstants.START_ELEMENT && eventType != XMLStreamConstants.END_ELEMENT) {
            throw new XMLStreamException("expected start or end tag", getLocation());
        }
        return eventType;
    }

    @Override
    public void close() throws XMLStreamException {
        currentEvt = END_DOCUMENT;
        while (depth >= 0) {
        	stack[depth] = null;
        	emtstack[depth] = null;
        	depth--;
        }
    	cdata = null;
    	clocalname = null;
    	cprefix = null;
    	ctarget = null;
    	ctext = null;
    	curi = null;
        this.document = null;
    }

    @Override
    public String getNamespaceURI(String prefix) {
    	final Namespace ns = nsstack.getNamespaceForPrefix(prefix);
    	return ns == null ? null : ns.getURI();
    }

    @Override
    public String getAttributeValue(String namespaceURI, String localName) {
        if(currentEvt != START_ELEMENT){
            throw new IllegalStateException("getAttributeCount not supported for event " + currentEvt);
        }
        
        final Element e = emtstack[depth];
        if (!e.hasAttributes()) {
        	return null;
        }
        
        if(namespaceURI != null){
            return e.getAttributeValue(localName, Namespace.getNamespace(namespaceURI));
        }
        
        //else search by local name only
        for(Attribute a : e.getAttributes()){
            if(a.getName().equalsIgnoreCase(localName)){
                return a.getValue();
            }
        }
        
        return null;
    }

    @Override
    public int getAttributeCount() {
        if(currentEvt != START_ELEMENT){
            throw new IllegalStateException("getAttributeCount not supported for event " + currentEvt);
        }
        return emtstack[depth].getAttributesSize();
    }

    @Override
    public QName getAttributeName(int index) {
        if(currentEvt != START_ELEMENT && currentEvt != ATTRIBUTE){
            throw new IllegalStateException("getAttributeCount not supported for event " + currentEvt);
        }
        
        final Attribute a = emtstack[depth].getAttributes().get(index);
        
        String ns = a.getNamespaceURI();
        if("".equals(ns))
            ns = null;
        String prefix = a.getNamespacePrefix();
        if(prefix == null || "".equals(prefix)){
            prefix = XMLConstants.DEFAULT_NS_PREFIX;
        }
        
        return new QName(ns, a.getName(), prefix);
    }

    @Override
    public String getAttributeNamespace(int index) {
        if(currentEvt != START_ELEMENT && currentEvt != ATTRIBUTE){
            throw new IllegalStateException("getAttributeCount not supported for event " + currentEvt);
        }
        
        final Attribute a = emtstack[depth].getAttributes().get(index);
        return a.getNamespaceURI();
    }

    @Override
    public String getAttributeLocalName(int index) {
        if(currentEvt != START_ELEMENT && currentEvt != ATTRIBUTE){
            throw new IllegalStateException("getAttributeCount not supported for event " + currentEvt);
        }
        
        final Attribute a = emtstack[depth].getAttributes().get(index);
        return a.getName();
    }

    @Override
    public String getAttributePrefix(int index) {
        if(currentEvt != START_ELEMENT && currentEvt != ATTRIBUTE){
            throw new IllegalStateException("getAttributeCount not supported for event " + currentEvt);
        }
        
        final Attribute a = emtstack[depth].getAttributes().get(index);
        return a.getNamespacePrefix();
    }

    @Override
    public String getAttributeType(int index) {
        if(currentEvt != START_ELEMENT && currentEvt != ATTRIBUTE){
            throw new IllegalStateException("getAttributeCount not supported for event " + currentEvt);
        }
        
        final Attribute a = emtstack[depth].getAttributes().get(index);
        return a.getAttributeType().name();
    }

    @Override
    public String getAttributeValue(int index) {
        if(currentEvt != START_ELEMENT && currentEvt != ATTRIBUTE){
            throw new IllegalStateException("getAttributeCount not supported for event " + currentEvt);
        }
        
        final Attribute a = emtstack[depth].getAttributes().get(index);
        return a.getValue();
    }

    @Override
    public boolean isAttributeSpecified(int index) {
        if(currentEvt != START_ELEMENT && currentEvt != ATTRIBUTE){
            throw new IllegalStateException("getAttributeCount not supported for event " + currentEvt);
        }
        
        final Attribute a = emtstack[depth].getAttributes().get(index);
        return a.isSpecified();
    }

    @Override
    public int getNamespaceCount() {
        switch(currentEvt){
            case START_ELEMENT:
            case END_ELEMENT:
            	final Iterator<?> it = nsstack.addedForward().iterator();
            	int cnt = 0;
            	while (it.hasNext()) {
            		cnt++;
            		it.next();
            	}
            	return cnt;
        }
        
        throw new IllegalStateException("getNamespaceCount not supported for event " + currentEvt);
    }
    
    private final Namespace getNamespaceByIndex(int index) {
    	final Iterator<Namespace> it = nsstack.addedForward().iterator();
    	int cnt = 0;
    	while (it.hasNext()) {
    		if (cnt == index) {
    			return it.next();
    		}
    		it.next();
    		cnt++;
    	}
    	throw new NoSuchElementException("No Namespace with index " + index + 
    			" (there are only " + cnt + ").");
    }

    @Override
    public String getNamespacePrefix(int index) {
        switch(currentEvt){
            case START_ELEMENT:
            case END_ELEMENT:
            	return getNamespaceByIndex(index).getPrefix();
        }
        
        throw new IllegalStateException("getNamespacePrefix not supported for event " + currentEvt);
    }

    @Override
    public String getNamespaceURI(int index) {
        
        switch(currentEvt){
            case START_ELEMENT:
            case NAMESPACE:
            case END_ELEMENT:
            	return getNamespaceByIndex(index).getURI();
        }
        
        throw new IllegalStateException("getNamespaceURI not supported for event " + currentEvt);
    }

    @Override
    public NamespaceContext getNamespaceContext() {
        return new JDOMNamespaceContext(nsstack.getScope());
    }

    @Override
    public boolean hasText() {
        switch(currentEvt){
            case CDATA:
            case CHARACTERS:
            case COMMENT:
            case DTD:
            case ENTITY_REFERENCE:
                return true;
            default:
                return false;
        }
    }

    @Override
    public String getText() {
        switch(currentEvt){
            case CDATA:
            case CHARACTERS:
            case COMMENT:
            case DTD:
            case ENTITY_REFERENCE:
                return ctext;
            default:
                throw new IllegalStateException("getText not valid for event type " + currentEvt);
        }
        
    }

    @Override
    public char[] getTextCharacters() {
        return getText().toCharArray();
    }

    @Override
    public int getTextCharacters(int sourceStart, char[] target, int targetStart, int length) throws XMLStreamException {
        char[] chars = getText().toCharArray();
        int i = 0;
        for(; i < length; i++){
            if(sourceStart > chars.length){
                return i;
            }
            if(targetStart > target.length){
                return i;
            }
            
            target[targetStart++] = chars[sourceStart++];
        }
        
        return i;
    }

    @Override
    public int getTextStart() {
        return 0;
    }

    @Override
    public int getTextLength() {
        return getText().length();
    }

    @Override
    public String getEncoding() {
        Object ret = document.getProperty("ENCODING");
        if(ret == null)
            return null;
        
        return ret.toString();
    }

    @Override
    public Location getLocation() {
        return new Location(){
            @Override
            public int getLineNumber() {
                return -1;
            }

            @Override
            public int getColumnNumber() {
                return -1;
            }

            @Override
            public int getCharacterOffset() {
                return -1;
            }

            @Override
            public String getPublicId() {
                return null;
            }

            @Override
            public String getSystemId() {
                return null;
            }
            
        };
    }


    @Override
    public String getVersion() {
        return null;
    }

    @Override
    public boolean isStandalone() {
        Object ret = document.getProperty("STANDALONE");
        return Boolean.TRUE.equals(ret);
    }

    @Override
    public boolean standaloneSet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getCharacterEncodingScheme() {
        Object ret = document.getProperty("ENCODING_SCHEME");
        if(ret == null)
            return null;
        
        return ret.toString();
    }

    @Override
    public Object getProperty(final String name) throws IllegalArgumentException {
    	if (name == null) {
    		throw new IllegalArgumentException("Property name is not allowed to be null");
    	}
    	if (XMLInputFactory.ALLOCATOR.equals(name)) {
    		return null;
    	}
    	if (XMLInputFactory.IS_COALESCING.equals(name)) {
    		return formatstack.getDefaultMode() != TextMode.PRESERVE;
    	}
    	if (XMLInputFactory.IS_NAMESPACE_AWARE.equals(name)) {
    		return Boolean.TRUE;
    	}
    	if (XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES.equals(name)) {
    		return Boolean.FALSE;
    	}
    	if (XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES.equals(name)) {
    		return Boolean.FALSE;
    	}
    	if (XMLInputFactory.IS_VALIDATING.equals(name)) {
    		return Boolean.TRUE;
    	}
    	if (XMLInputFactory.REPORTER.equals(name)) {
    		return null;
    	}
    	if (XMLInputFactory.RESOLVER.equals(name)) {
    		return null;
    	}
        return null;
    }

}
