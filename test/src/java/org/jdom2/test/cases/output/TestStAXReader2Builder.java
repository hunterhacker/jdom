package org.jdom2.test.cases.output;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.StAXStreamBuilder;
import org.jdom2.output.StAXStreamReader;

@SuppressWarnings("javadoc")
public class TestStAXReader2Builder extends AbstractTestRoundTrip {

	@Override
	Document prepare(Document doc) {
		return doc;
	}

	@Override
	Document roundTrip(final Document doc) {
		final StAXStreamBuilder sb = new StAXStreamBuilder();
		final StAXStreamReader sasr = new StAXStreamReader();
		try {
			return sb.build(sasr.output(doc));
		} catch (JDOMException e) {
			throw new IllegalStateException(e);
		}
	}

}
