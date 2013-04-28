package org.jdom2.test.cases.output;

import java.util.Iterator;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stax.StAXResult;
import javax.xml.transform.stax.StAXSource;

import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.StAXStreamWriter;
import org.jdom2.output.StAXStreamReader;

@SuppressWarnings("javadoc")
public class TestStAXReader2Writer extends AbstractTestRoundTrip {

	@Override
	Document prepare(Document doc) {
		Document ret = doc.clone();
		for (Iterator<Content> it = ret.getContent().iterator(); it.hasNext(); ) {
			Content c = it.next();
			if (!(c instanceof Element)) {
				it.remove();
			}
		}
		return ret;
	}

	@Override
	Document roundTrip(final Document doc) {
		try {
			final StAXStreamWriter sw = new StAXStreamWriter();
			final StAXStreamReader sr = new StAXStreamReader();
			final TransformerFactory tf = TransformerFactory.newInstance(
					"com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl",
					this.getClass().getClassLoader());
			final Transformer t = tf.newTransformer();
			StAXSource source = new StAXSource(sr.output(doc));
			StAXResult result = new StAXResult(sw);
			t.transform(source, result);
			return sw.getDocument();
		} catch (Exception e) {
			throw new IllegalStateException("Failed to identity-trasform...", e);
		}
	}

}
