package org.jdom2.test.cases.output;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.StAXStreamBuilder;
import org.jdom2.jaxb.JDOMStreamReader;

@SuppressWarnings("javadoc")
public class TestStAXWriterReader extends AbstractTestRoundTrip {

	@Override
	Document roundTrip(final Document doc) {
		final JDOMStreamReader sr = new JDOMStreamReader(doc);
		final StAXStreamBuilder sb = new StAXStreamBuilder();
		try {
			return sb.build(sr);
		} catch (JDOMException e) {
			throw new IllegalStateException(e);
		}
	}

}
