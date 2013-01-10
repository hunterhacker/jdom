package org.jdom2.test.cases.output;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.StAXStreamBuilder;
import org.jdom2.output.StAXAsStreamReader;

@SuppressWarnings("javadoc")
public class TestStAXWriterReader extends AbstractTestRoundTrip {

	@Override
	Document roundTrip(final Document doc) {
		final StAXStreamBuilder sb = new StAXStreamBuilder();
		final StAXAsStreamReader sasr = new StAXAsStreamReader();
		try {
			return sb.build(sasr.output(doc));
		} catch (JDOMException e) {
			throw new IllegalStateException(e);
		}
	}

}
