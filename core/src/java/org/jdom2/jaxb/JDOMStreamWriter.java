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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.jdom2.Attribute;
import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.EntityRef;
import org.jdom2.Namespace;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;

/**
 * An {@link XMLStreamWriter} implementation that writes XML data to a new JDOM
 * {@link Document}.  The document can be retrieved using {@link #getDocument() }.
 * @author gordon burgett https://github.com/gburgett
 */
public class JDOMStreamWriter implements XMLStreamWriter {

    private Document document;
    private Stack<Element> elementStack = new Stack<Element>();
    
    private Element root;
    
    private StreamWriterState state = StreamWriterState.BEFORE_DOCUMENT_START;
    
    /**
     * Gets the {@link Document} that was created by this writer.
     * Only available after {@link #writeEndDocument() } has been called.
     * @return The created {@link Document}
     */
    public Document getDocument(){
        if(state == StreamWriterState.CLOSED){
            throw new IllegalStateException("writer is closed");
        }
        
        if(state != StreamWriterState.DOCUMENT_ENDED){
            throw new IllegalStateException("Cannot get document until writer has ended the document");
        }
        
        return document;
    }
    
    
    @Override
    public void writeStartElement(String localName) throws XMLStreamException {
        this.writeStartElement(null, localName, null);
    }

    @Override
    public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
        this.writeStartElement(null, localName, namespaceURI);
    }

    @Override
    public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
        Namespace ns = getNamespace(namespaceURI, prefix);
        
        Element e = new Element(localName, ns);
        
        switch(state){
            case DOCUMENT_START:
                this.root = e;
                document.setRootElement(root);
                state = StreamWriterState.IN_ROOT_ELEMENT;
                break;
                
            case IN_ROOT_ELEMENT:
                this.root.addContent(e);
                this.elementStack.push(e);
                state = StreamWriterState.IN_ELEMENT;
                break;
                
            case IN_ELEMENT:
                Element parent = this.elementStack.peek();
                parent.addContent(e);
                this.elementStack.push(e);
                //still in element
                break;
                
            default:
                throw new IllegalStateException("Cannot write new element when in state " + state);
        }
    }

    @Override
    public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException {
        this.writeEmptyElement(null, localName, null);
    }

    @Override
    public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
        Namespace ns = getNamespace(namespaceURI, prefix);
        
        Element e = new Element(localName, ns);
        
        switch(state){
            case DOCUMENT_START:
                //an empty root element
                this.root = e;
                document.setRootElement(root);
                state = StreamWriterState.END_ROOT_ELEMENT;
                break;
                
            case IN_ROOT_ELEMENT:
                this.root.addContent(e);
                //still in root element
                break;
                
            case IN_ELEMENT:
                Element pop = this.elementStack.peek();
                if(pop == null){
                    pop = this.root;
                }
                pop.addContent(e);
                //still in element
                break;
                
            default:
                throw new IllegalStateException("Cannot write new element when in state " + state);
        }
    }

    @Override
    public void writeEmptyElement(String localName) throws XMLStreamException {
        this.writeEmptyElement(null, localName, null);
    }

    @Override
    public void writeEndElement() throws XMLStreamException {
        switch(state){
            case IN_ROOT_ELEMENT:
                //done with root element
                state = StreamWriterState.END_ROOT_ELEMENT;
                break;
                
            case IN_ELEMENT:
                this.elementStack.pop();
                if(this.elementStack.isEmpty()){
                    //back to root element
                    state = StreamWriterState.IN_ROOT_ELEMENT;
                }
                //else still in element
                break;
                
            default:
                throw new IllegalStateException("Cannot write end element when in state " + state);
        }
    }

    @Override
    public void writeEndDocument() throws XMLStreamException {
        if(state != StreamWriterState.END_ROOT_ELEMENT){
            throw new IllegalStateException("Cannot write end document before writing end of root element");
        }
        
        state = StreamWriterState.DOCUMENT_ENDED;
    }

    @Override
    public void close() throws XMLStreamException {
        this.document = null;
        this.root = null;
        this.elementStack = null;
        this.state = StreamWriterState.CLOSED;
    }

    @Override
    public void flush() throws XMLStreamException {
    }

    @Override
    public void writeAttribute(String localName, String value) throws XMLStreamException {
        this.writeAttribute(null, null, localName, value);
    }

    @Override
    public void writeAttribute(String prefix, String namespaceURI, String localName, String value) throws XMLStreamException {
        Attribute a = new Attribute(localName, value, getNamespace(namespaceURI, prefix));
        
        switch(state){
            case IN_ROOT_ELEMENT:
                this.root.setAttribute(a);
                break;
                
            case IN_ELEMENT:
                this.elementStack.peek().setAttribute(a);
                break;
                
            default:
                throw new IllegalStateException("Cannot write attribute when in state " + state);
        }
    }

    @Override
    public void writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException {
        this.writeAttribute(null, namespaceURI, localName, value);
    }

    @Override
    public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException {
        Namespace ns = this.getNamespace(namespaceURI, prefix);
        switch(state){
            case IN_ROOT_ELEMENT:
                this.root.addNamespaceDeclaration(ns);
                break;
                
            case IN_ELEMENT:
                this.elementStack.peek().addNamespaceDeclaration(ns);
                break;
                
            default:
                throw new IllegalStateException("Cannot write namespace when in state " + state);
        }
    }

    @Override
    public void writeDefaultNamespace(String namespaceURI) throws XMLStreamException {
        this.writeNamespace(null, namespaceURI);
    }

    @Override
    public void writeComment(String data) throws XMLStreamException {
        Comment c = new Comment(data);
        this.addContent(c);
    }

    @Override
    public void writeProcessingInstruction(String target) throws XMLStreamException {
        this.writeProcessingInstruction(target, null);
    }

    @Override
    public void writeProcessingInstruction(String target, String data) throws XMLStreamException {
        ProcessingInstruction pi = new ProcessingInstruction(target, data);
        this.addContent(pi);
    }

    @Override
    public void writeCData(String data) throws XMLStreamException {
        this.addContent(new CDATA(data));
    }

    @Override
    public void writeDTD(String dtd) throws XMLStreamException {
        throw new UnsupportedOperationException("not supported yet");
    }

    @Override
    public void writeEntityRef(String name) throws XMLStreamException {
        EntityRef e = new EntityRef(name);
        this.addContent(e);
    }

    @Override
    public void writeStartDocument() throws XMLStreamException {
        this.writeStartDocument(null);
    }

    @Override
    public void writeStartDocument(String version) throws XMLStreamException {
        this.writeStartDocument(null, version);
    }

    @Override
    public void writeStartDocument(String encoding, String version) throws XMLStreamException {
        if(this.state != StreamWriterState.BEFORE_DOCUMENT_START){
            throw new IllegalStateException("Cannot write start document while in state " + this.state);
        }
        
        this.document = new Document();
        if(encoding != null && !"".equals(encoding))
            this.document.setProperty("ENCODING", encoding);
        //TODO: how to set version?
        
        this.state = StreamWriterState.DOCUMENT_START;
    }

    @Override
    public void writeCharacters(String text) throws XMLStreamException {
        this.addContent(new Text(text));
    }

    @Override
    public void writeCharacters(char[] text, int start, int len) throws XMLStreamException {
        String s = new String(text, start, len);
        this.addContent(new Text(s));
    }

    @Override
    public String getPrefix(String uri) throws XMLStreamException {
        for(Namespace n : getCurrentNamespaces()){
            if(uri == null){
                if(n.getURI() == null){
                    return n.getPrefix();
                }
            }
            else{
                if(uri.equals(n.getURI())){
                    return n.getPrefix();
                }
            }
        }
        
        return null;
    }

    @Override
    public void setPrefix(String prefix, String uri) throws XMLStreamException {
        switch(state){
            case IN_ROOT_ELEMENT:
                this.root.addNamespaceDeclaration(Namespace.getNamespace(prefix, uri));
            
            case IN_ELEMENT:
                this.elementStack.peek().addNamespaceDeclaration(Namespace.getNamespace(prefix, uri));
                
            default:
                throw new IllegalStateException("Attempt to set prefix outside the context of an element");
        }
    }

    @Override
    public void setDefaultNamespace(String uri) throws XMLStreamException {
        switch(state){
            case IN_ROOT_ELEMENT:
                this.root.addNamespaceDeclaration(Namespace.getNamespace(uri));
            
            case IN_ELEMENT:
                this.elementStack.peek().addNamespaceDeclaration(Namespace.getNamespace(uri));
                
            default:
                throw new IllegalStateException("Attempt to set default namespace outside the context of an element");
        }
    }

    @Override
    public void setNamespaceContext(NamespaceContext context) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public NamespaceContext getNamespaceContext() {
        final List<Namespace> namespaces = getCurrentNamespaces();
        
        return new org.jdom2.jaxb.JDOMNamespaceContext(namespaces);
    }

    private List<Namespace> getCurrentNamespaces() {
        switch(state){
            case IN_ROOT_ELEMENT:
                return root.getNamespacesInScope();
                
            case IN_ELEMENT:
                return elementStack.peek().getNamespacesInScope();
            
            case DOCUMENT_START:
            case END_ROOT_ELEMENT:
                return document.getNamespacesInScope();
                
            default:
                throw new IllegalStateException("Attempt to get namespaces in unsupported state " + this.state);
        }
    }
    
    @Override
    public Object getProperty(String name) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private Namespace getNamespace(String namespaceURI, String prefix) {
        Namespace ns = null;
        if(namespaceURI != null){
            if(prefix == null || "".equals(prefix) || "xmlns".equalsIgnoreCase(prefix)){
                ns = Namespace.getNamespace(namespaceURI);
            }
            else{
                ns = Namespace.getNamespace(prefix, namespaceURI);
            }
        }
        return ns;
    }
    
    private void addContent(Content c){
        switch(state){
            case IN_ROOT_ELEMENT:
                this.root.addContent(c);
                break;
                
            case IN_ELEMENT:
                this.elementStack.peek().addContent(c);
                break;
                
            default:
                throw new IllegalStateException("Cannot write end element when in state " + state);
        }
    }
    
    private enum StreamWriterState{
        BEFORE_DOCUMENT_START,
        DOCUMENT_START,
        IN_ROOT_ELEMENT,
        IN_ELEMENT,
        END_ROOT_ELEMENT,
        DOCUMENT_ENDED,
        CLOSED
    }
}
