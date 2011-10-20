package org.jdom2.test.cases.transform;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMFactory;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;
import org.jdom2.UncheckedJDOMFactory;
import org.jdom2.transform.JDOMResult;
import org.junit.Test;
import org.xml.sax.ContentHandler;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.ext.LexicalHandler;

@SuppressWarnings("javadoc")
public class TestJDOMResult {
	
	/* *************************************************
	 * These tests cover only a small part of JDOMResult.
	 * The more complex code relates to accepting the
	 * events fired during transformation. Those calls
	 * are tested in TestJDOMTransform which puts 'real'
	 * transformations through JDOMResult.
	 * *************************************************/
	
	@Test
	public void testJDOMResult() {
		JDOMResult result = new JDOMResult();
		assertNotNull(result.getHandler());
		assertTrue(result.getHandler() == result.getLexicalHandler());
	}

	@Test
	public void testSetHandlerContentHandler() {
		JDOMResult result = new JDOMResult();
		ContentHandler handler = result.getHandler();
		assertNotNull(handler);
		ContentHandler toset = new DefaultHandler2();

		// we do not allow others to change the handler on the JDOMResult
		// so this should do nothing.
		result.setHandler(toset);
		
		assertTrue(handler == result.getHandler());
	}

	@Test
	public void testSetLexicalHandlerLexicalHandler() {
		JDOMResult result = new JDOMResult();
		LexicalHandler handler = result.getLexicalHandler();
		assertNotNull(handler);
		LexicalHandler toset = new DefaultHandler2();

		// we do not allow others to change the handler on the JDOMResult
		// so this should do nothing.
		result.setLexicalHandler(toset);
		
		assertTrue(handler == result.getLexicalHandler());
	}

	@Test
	public void testGetSetFactory() {
		JDOMResult result = new JDOMResult();
		JDOMFactory faca = result.getFactory();
		assertTrue(faca == null);
		JDOMFactory facb = new UncheckedJDOMFactory();
		result.setFactory(facb);
		assertTrue(facb == result.getFactory());
		result.setFactory(null);
		assertTrue(null == result.getFactory());
	}

	@Test
	public void testDocumentResult() {
		// in this context, the 'source' provides us with a document.
		// the expectation is that getDocument() will work to retrieve the result
		// but, getNodes will only work if called first... subsequent calls
		// return an empty list.
		JDOMResult result = new JDOMResult();
		assertNull(result.getDocument());
		assertTrue(result.getResult().isEmpty());
		
		// OK, we expect things now.
		Element root = new Element("root");
		Document doc = new Document(root);
		result.setDocument(doc);
		// test a few times. Should not change.
		assertTrue(doc == result.getDocument());
		assertTrue(doc == result.getDocument());
		assertTrue(doc == result.getDocument());
		// after a call to getDocument, getResult should be empty.
		assertTrue(result.getResult().isEmpty());
		// messing with the getDocument side of things should not mess with the
		// document tree.
		assertTrue(root.getParent() == doc);
		
		// OK, reset the result.
		result.setDocument(doc);
		// should be something there this time
		assertTrue(result.getResult().size() == 1);
		// but, now it is cached too. we can get the result...
		assertTrue(root == result.getResult().get(0));
		// using getResult() returns detached content, so....
		assertFalse(doc.hasRootElement());
		assertTrue(null == root.getParent());
		// further, a successful getResult() call invalidates the getDocument()
		// so that will now be null.
		assertNull(result.getDocument());
	}

	@Test
	public void testNodesResult() {
		// In this context, the 'source' provides us with a list of Nodes.
		// In theory, these nodes should all be legal Element content.
		JDOMResult result = new JDOMResult();
		assertNull(result.getDocument());
		assertTrue(result.getResult().isEmpty());
		
		// OK, we expect things now.
		Element child = new Element("child");
		Text text = new Text("text");
		ArrayList<Content> nodes = new ArrayList<Content>(2);
		nodes.add(child);
		nodes.add(text);
		
		result.setResult(nodes);
		
		// test a few times. Should not change.
		assertTrue(nodes == result.getResult());
		assertTrue(!nodes.isEmpty());
		assertTrue(nodes == result.getResult());
		assertTrue(nodes == result.getResult());

		// A call to getDocument should be null after a call to getResult().
		assertTrue(null == result.getDocument());
		
		// after a null call to getDocument, getResult should be unchanged.
		assertTrue(nodes == result.getResult());
		
		// OK, reset the result.
		result.setResult(nodes);
		// should still be nothing there... because Text is not valid Document content
		assertNull(result.getDocument());
		// but, the results are there still.
		assertTrue(result.getResult().size() == 2);
		
		// So, make the content valid for a Document.
		nodes.remove(1); // get rid of text.
		// insert a PI in front of the element.
		nodes.add(0, new ProcessingInstruction("jdomtest", ""));
		
		assertTrue(child.getParent() == null);
		
		// This time we expect a document result.
		result.setResult(nodes);
		Document doc = result.getDocument();
		assertNotNull(doc);
		assertTrue (doc == child.getParent());
		
	}

	
	
}
