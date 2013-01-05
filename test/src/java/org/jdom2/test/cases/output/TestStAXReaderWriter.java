package org.jdom2.test.cases.output;

import javax.xml.stream.XMLStreamException;

import org.jdom2.Document;
import org.jdom2.jaxb.JDOMStreamWriter;
import org.jdom2.output.StAXStreamOutputter;

@SuppressWarnings("javadoc")
public class TestStAXReaderWriter extends AbstractTestRoundTrip {

	@Override
	Document roundTrip(final Document doc) {
		final JDOMStreamWriter sw = new JDOMStreamWriter();
		final StAXStreamOutputter so = new StAXStreamOutputter();
		try {
			so.output(doc, sw);
		} catch (XMLStreamException e) {
			throw new IllegalStateException(e);
		}
		return sw.getDocument();
	}

}
