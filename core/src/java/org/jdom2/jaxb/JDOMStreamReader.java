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

package org.jdom2.jaxb;

import java.util.Iterator;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
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
import org.jdom2.Text;

/**
 * An {@link XMLStreamReader} implementation that reads the XML document
 * out of a JDOM {@link Document}.
 * 
 * The reader reads XML Events by walking the DOM tree, reporting all XML stream
 * events as it encounters them in the DOM.
 * @author gordon burgett https://github.com/gburgett
 */
public class JDOMStreamReader implements XMLStreamReader {

    private Document document;
    private Element root;
    private DomWalkingContentIterator rootIterator;
    private ReaderState state = ReaderState.START_DOCUMENT;
    
    private int currentEvt = START_DOCUMENT;
    
    private Content getCurrentContent(){
        Content ret = rootIterator.getCurrentContent();
        if(ret == null){
            return root;
        }
        return ret;
    }
    
    public JDOMStreamReader(Document document){
        this.document = document;
        this.root = document.getRootElement();
    }
    
    @Override
    public Object getProperty(String name) throws IllegalArgumentException {
        return null;
    }

    @Override
    public int next() throws XMLStreamException {
        switch(state){
            case START_DOCUMENT:
                state = ReaderState.START_ROOT_ELEMENT;
                return currentEvt = START_DOCUMENT;
                
            case START_ROOT_ELEMENT:
                state = ReaderState.WALKING_TREE;
                this.rootIterator = new DomWalkingContentIterator(root);
                return currentEvt = START_ELEMENT;
                
            case WALKING_TREE:
                if(this.rootIterator.hasNext()){
                    return currentEvt = this.rootIterator.next();
                }
                else{
                    state = ReaderState.END_ROOT_ELEMENT;
                    return currentEvt = END_ELEMENT;
                }
                
            case END_ROOT_ELEMENT:
                state = ReaderState.END_DOCUMENT;
                return currentEvt = END_DOCUMENT;
                
            default:
                throw new IllegalStateException("Reader does not have next");
        }
    }

    @Override
    public void require(int type, String namespaceURI, String localName) throws XMLStreamException {
        if(type != currentEvt){
            throw new XMLStreamException("required event " + type + " but got event " + currentEvt);
        }
        
        if(localName != null){
            if(!localName.equals(getLocalName())){
                throw new XMLStreamException("required name " + localName + " but got name " + getLocalName());
            }
        }
        
        if(namespaceURI != null){
            if(!namespaceURI.equals(getNamespaceURI())){
                throw new XMLStreamException("required namespace " + namespaceURI + " but got namespace " + getNamespaceURI());
            }
        }
    }

    @Override
    public String getElementText() throws XMLStreamException {
        if(getEventType() != XMLStreamConstants.START_ELEMENT) {
            throw new XMLStreamException("parser must be on START_ELEMENT to read next text", getLocation());
        }
        
        int eventType = next();
        StringBuffer buf = new StringBuffer();
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
    public boolean hasNext() throws XMLStreamException {
        return !(state == ReaderState.END_DOCUMENT ||
                state == ReaderState.CLOSED);
    }

    @Override
    public void close() throws XMLStreamException {
        this.state = ReaderState.CLOSED;
        this.document = null;
        this.root = null;
        this.rootIterator = null;
    }

    @Override
    public String getNamespaceURI(String prefix) {
        if("xml".equalsIgnoreCase(prefix)){
            return Namespace.XML_NAMESPACE.getURI();
        }
        if("xmlns".equalsIgnoreCase(prefix)){
            return "http://www.w3.org/2000/xmlns/";
        }
        
        Content c = getCurrentContent();
        for(Namespace ns : c.getNamespacesInScope()){
            if(ns.getPrefix().equals(prefix)){
                return ns.getURI();
            }
        }
        
        return null;
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
        return currentEvt == SPACE;
    }

    @Override
    public String getAttributeValue(String namespaceURI, String localName) {
        if(currentEvt != START_ELEMENT && currentEvt != ATTRIBUTE){
            throw new IllegalStateException("getAttributeCount not supported for event " + currentEvt);
        }
        
        Element e = (Element)getCurrentContent();
        
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
        if(currentEvt != START_ELEMENT && currentEvt != ATTRIBUTE){
            throw new IllegalStateException("getAttributeCount not supported for event " + currentEvt);
        }
        
        Element e = (Element)getCurrentContent();
        return e.getAttributes().size();
    }

    @Override
    public QName getAttributeName(int index) {
        if(currentEvt != START_ELEMENT && currentEvt != ATTRIBUTE){
            throw new IllegalStateException("getAttributeCount not supported for event " + currentEvt);
        }
        
        Element e = (Element)getCurrentContent();
        Attribute a = e.getAttributes().get(index);
        
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
        
        Element e = (Element)getCurrentContent();
        Attribute a = e.getAttributes().get(index);
        String ret = a.getNamespaceURI();
        if("".equals(ret)){
            return null;
        }
        return ret;
    }

    @Override
    public String getAttributeLocalName(int index) {
        if(currentEvt != START_ELEMENT && currentEvt != ATTRIBUTE){
            throw new IllegalStateException("getAttributeCount not supported for event " + currentEvt);
        }
        
        Element e = (Element)getCurrentContent();
        Attribute a = e.getAttributes().get(index);
        return a.getName();
    }

    @Override
    public String getAttributePrefix(int index) {
        if(currentEvt != START_ELEMENT && currentEvt != ATTRIBUTE){
            throw new IllegalStateException("getAttributeCount not supported for event " + currentEvt);
        }
        
        Element e = (Element)getCurrentContent();
        Attribute a = e.getAttributes().get(index);
        String ret = a.getNamespacePrefix();
        if(ret == null || "".equals(ret)){
            return XMLConstants.DEFAULT_NS_PREFIX;
        }
        return ret;
    }

    @Override
    public String getAttributeType(int index) {
        if(currentEvt != START_ELEMENT && currentEvt != ATTRIBUTE){
            throw new IllegalStateException("getAttributeCount not supported for event " + currentEvt);
        }
        
        Element e = (Element)getCurrentContent();
        Attribute a = e.getAttributes().get(index);
        
        return a.getAttributeType().name();
    }

    @Override
    public String getAttributeValue(int index) {
        if(currentEvt != START_ELEMENT && currentEvt != ATTRIBUTE){
            throw new IllegalStateException("getAttributeCount not supported for event " + currentEvt);
        }
        
        Element e = (Element)getCurrentContent();
        Attribute a = e.getAttributes().get(index);
        return a.getValue();
    }

    @Override
    public boolean isAttributeSpecified(int index) {
        if(currentEvt != START_ELEMENT && currentEvt != ATTRIBUTE){
            throw new IllegalStateException("getAttributeCount not supported for event " + currentEvt);
        }
        
        Element e = (Element)getCurrentContent();
        Attribute a = e.getAttributes().get(index);
        
        return a.isSpecified();
    }

    @Override
    public int getNamespaceCount() {
        switch(currentEvt){
            case START_ELEMENT:
            case NAMESPACE:
            case END_ELEMENT:
                Element e = (Element)getCurrentContent();
                return e.getNamespacesIntroduced().size();
        }
        
        throw new IllegalStateException("getNamespaceCount not supported for event " + currentEvt);
    }

    @Override
    public String getNamespacePrefix(int index) {
        switch(currentEvt){
            case START_ELEMENT:
            case NAMESPACE:
            case END_ELEMENT:
                Element e = (Element)getCurrentContent();
                Namespace ns = e.getNamespacesIntroduced().get(index);
                String ret = ns.getPrefix();
                if(ret == null || "".equals(ret)){
                    ret = XMLConstants.DEFAULT_NS_PREFIX;
                }
                return ret;
        }
        
        throw new IllegalStateException("getNamespacePrefix not supported for event " + currentEvt);
    }

    @Override
    public String getNamespaceURI(int index) {
        
        switch(currentEvt){
            case START_ELEMENT:
            case NAMESPACE:
            case END_ELEMENT:
                Element e = (Element)getCurrentContent();
                Namespace ns = e.getNamespacesIntroduced().get(index);
                String ret = ns.getURI();
                if("".equals(ret)){
                    ret = null;
                }
                return ret;
        }
        
        throw new IllegalStateException("getNamespaceURI not supported for event " + currentEvt);
    }

    @Override
    public NamespaceContext getNamespaceContext() {
        if(state == ReaderState.START_DOCUMENT || state == ReaderState.END_DOCUMENT || state == ReaderState.CLOSED){
            throw new IllegalStateException("getNamespaceCount not supported for event " + currentEvt);
        }
        
        Content c = getCurrentContent();
        return new org.jdom2.jaxb.JDOMNamespaceContext(c.getNamespacesInScope());
    }

    @Override
    public int getEventType() {
        return currentEvt;
    }

    @Override
    public String getText() {
        Content c = getCurrentContent();
        switch(c.getCType()){
            case CDATA:
            case Text:
            case Comment:
                return c.getValue();
                
            case DocType:
                return ((DocType)c).getInternalSubset();
        }
        
        throw new IllegalStateException("getText not valid for event type " + currentEvt);
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
    public boolean hasText() {
        return currentEvt == CHARACTERS ||
                currentEvt == DTD ||
                currentEvt == ENTITY_REFERENCE ||
                currentEvt == COMMENT ||
                currentEvt == SPACE;
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
    public QName getName() {
        if(currentEvt != START_ELEMENT && currentEvt != END_ELEMENT){
            throw new IllegalStateException("getName not supported for event " + currentEvt);
        }
        
        Element e = (Element)getCurrentContent();
        String ns = e.getNamespaceURI();
        if("".equals(ns)) 
            ns = null;
        
        String prefix = e.getNamespacePrefix();
        if(prefix == null || "".equals(prefix)) 
            prefix = XMLConstants.DEFAULT_NS_PREFIX;
        
        return new QName(ns, e.getName(), prefix);
    }

    @Override
    public String getLocalName() {
        switch(currentEvt){
            case START_ELEMENT:
            case END_ELEMENT:
                Element e = (Element)getCurrentContent();
                return e.getName();
                
            case ENTITY_REFERENCE:
                EntityRef er = (EntityRef)getCurrentContent();
                return er.getName();
        }
        
        throw new IllegalStateException("getLocalName not supported for event " + currentEvt);
    }

    @Override
    public boolean hasName() {
        return currentEvt == START_ELEMENT || currentEvt == END_ELEMENT;
    }

    @Override
    public String getNamespaceURI() {
        if(currentEvt != START_ELEMENT && currentEvt != END_ELEMENT){
            throw new IllegalStateException("getNamespaceURI not supported for event " + currentEvt);
        }
        
        Element e = (Element)getCurrentContent();
        String ret = e.getNamespaceURI();
        if("".equals(ret)){
            ret = null;
        }
        return ret;
    }

    @Override
    public String getPrefix() {
        if(currentEvt != START_ELEMENT && currentEvt != END_ELEMENT){
            throw new IllegalStateException("getName not supported for event " + currentEvt);
        }
        
        Element e = (Element)getCurrentContent();
        String ret = e.getNamespacePrefix();
        if(ret == null || "".equals(ret))
            ret = XMLConstants.DEFAULT_NS_PREFIX;
        
        return ret;
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
    public String getPITarget() {
        if(currentEvt != PROCESSING_INSTRUCTION){
            throw new IllegalStateException("getPITarget not supported for event " + currentEvt);
        }
        
        ProcessingInstruction pi = (ProcessingInstruction)getCurrentContent();
        return pi.getTarget();
    }

    @Override
    public String getPIData() {
        if(currentEvt != PROCESSING_INSTRUCTION){
            throw new IllegalStateException("getPIData not supported for event " + currentEvt);
        }
        
        ProcessingInstruction pi = (ProcessingInstruction)getCurrentContent();
        return pi.getData();
    }
    
    /**
     * An Iterator that recursively walks DOM elements and returns 
     * {@link XMLStreamConstants XML stream events}.
     */
    private class DomWalkingContentIterator implements Iterator<Integer> {
        
        private int contentIndex = 0;
        
        private DomWalkingContentIterator subIterator;
        
        private Content currentContent;
        public Content getCurrentContent(){
            Content ret = null;
            if(subIterator != null){
                ret = subIterator.getCurrentContent();
            }
            
            if(ret == null){
                return currentContent;
            }
            
            return ret;
        }
        
        private Element toWalk;
        
        public DomWalkingContentIterator(Element toWalk){
            this.toWalk = toWalk;
        }
        
        @Override
        public boolean hasNext() {
            //if we're walking a recursive sub-iterator
            if(subIterator != null){
                //we either have sub-iterator content or we have END_ELEMENT
                return true;
            }
            
            //if we have content remaining
            if(toWalk.getContentSize() > contentIndex){
                return true;
            }
            
            return false;
        }

        @Override
        public Integer next() {
            int next;
            if(subIterator != null){
                //walk the sub-element
                if(subIterator.hasNext()){
                    next = subIterator.next();
                    return next;
                }
                else{
                    subIterator = null;
                    return END_ELEMENT;
                }
            }
            
            if(this.toWalk.getContentSize() > contentIndex){
                Content c = this.toWalk.getContent(contentIndex++);
                this.currentContent = c;
                if(c.getCType() == Content.CType.Element){
                    subIterator = new DomWalkingContentIterator((Element)c);
                    return START_ELEMENT;
                }

                return getEventType(c);
            }
            
            throw new IllegalStateException("Iterator does not have next");
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported.");
        }

        
    }
    
    
    private int getEventType(Content c){
        switch(c.getCType()){
            case CDATA:
                return CDATA; 
                
            case Comment:
                return COMMENT;
                
            case DocType:
                return DTD;
                
            case EntityRef:
                //TODO: properly expand entity refs
                throw new UnsupportedOperationException("Entity references not yet supported");
                
            case ProcessingInstruction:
                return PROCESSING_INSTRUCTION;
                
            case Text:
                if(((Text)c).getText().trim().length() == 0){
                    return SPACE;
                }
                return CHARACTERS;
                
            default:
                throw new UnsupportedOperationException("No event type available for content type " + c.getCType());
        }
    }
    
    /**
     * The current state of the reader
     */
    private enum ReaderState{
        START_DOCUMENT,
        START_ROOT_ELEMENT,
        WALKING_TREE,
        END_ROOT_ELEMENT,
        END_DOCUMENT,
        CLOSED
    }
}
