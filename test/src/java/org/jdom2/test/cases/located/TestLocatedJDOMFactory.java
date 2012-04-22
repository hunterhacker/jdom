package org.jdom2.test.cases.located;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import org.jdom2.Comment;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.JDOMFactory;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.located.Located;
import org.jdom2.located.LocatedJDOMFactory;
import org.jdom2.test.cases.AbstractTestJDOMFactory;
import org.jdom2.test.util.FidoFetch;
import org.jdom2.xpath.XPathFactory;

@SuppressWarnings("javadoc")
public class TestLocatedJDOMFactory extends AbstractTestJDOMFactory {
	
	/**
	 * @param located
	 */
	public TestLocatedJDOMFactory() {
		super(true);
	}

	@Override
	protected JDOMFactory buildFactory() {
		return new LocatedJDOMFactory();
	}
	
	private final void checkLocation(Content c, int line, int col) {
		assertTrue(c instanceof Located);
		Located l = (Located)c;
		if(line != l.getLine()) {
			fail("Expected content " + l + " to be on line " + line + " but its on " + l.getLine());
		}
		if(col != l.getColumn()) {
			fail("Expected content " + l + " to be on col " + col + " but its on " + l.getColumn());
		}
	}
	
	@Test
	public void testLocation() throws JDOMException, IOException {
		SAXBuilder sb = new SAXBuilder();
		sb.setJDOMFactory(new LocatedJDOMFactory());
		sb.setExpandEntities(false);
		Document doc = sb.build(FidoFetch.getFido().getURL("/complex.xml"));
		// it appears the location of the DocType is the start of the internal subset.
		checkLocation(doc.getDocType(), 2, 16);
		final Element root = doc.getRootElement();
		checkLocation(root, 3, 32);
		
		// content0 is first text....
		// tab counts as 1, not 4... thus char 2, not 5....
		checkLocation(root.getContent(0), 5, 2);
		
		// get the comment...
		Comment comment = root.getContent(Filters.comment()).get(0);
		checkLocation(comment, 12, 19);
		
		Element leaf = XPathFactory.instance().compile("//leaf", Filters.element()).evaluateFirst(doc);
		checkLocation(leaf, 21, 24);
		
	}
	
}
