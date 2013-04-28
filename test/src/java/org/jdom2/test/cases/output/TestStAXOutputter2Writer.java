package org.jdom2.test.cases.output;

import javax.xml.stream.XMLStreamException;

import org.jdom2.Document;
import org.jdom2.input.StAXStreamWriter;
import org.jdom2.output.StAXStreamOutputter;

@SuppressWarnings("javadoc")
public class TestStAXOutputter2Writer extends AbstractTestRoundTrip {

	@Override
	Document prepare(Document doc) {
		return doc;
	}

	@Override
	Document roundTrip(final Document doc) {
		final StAXStreamWriter sw = new StAXStreamWriter();
		final StAXStreamOutputter so = new StAXStreamOutputter();
		try {
			so.output(doc, sw);
		} catch (XMLStreamException e) {
			throw new IllegalStateException(e);
		}
		return sw.getDocument();
	}

}
