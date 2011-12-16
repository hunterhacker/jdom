package org.jdom2.test.cases.adapters;

import org.junit.Test;
import org.w3c.dom.Document;

import org.jdom2.JDOMException;
import org.jdom2.adapters.JAXPDOMAdapter;

@SuppressWarnings("javadoc")
public class TestJAXPDOMAdapter {

	@Test
	public void testCreateDocument() throws JDOMException {
		for (int i = 10; i > 0; i--) {
			timeDoc();
		}
	}
	
	private void timeDoc() throws JDOMException {
		long time = System.nanoTime();
		long hash = 0L;
		final int cnt = 1000;
		for (int i = 0; i < cnt; i++) {
			Document doc = new JAXPDOMAdapter().createDocument();
			hash += doc.hashCode();
		}
		time = System.nanoTime() - time;
		System.out.printf("JAXPDOMAdapter Speed %.3f %d\n", (time / 1000000.0) / cnt, hash & 0x01);
	}

}
