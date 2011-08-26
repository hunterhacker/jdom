package org.jdom2.test.cases;

import org.jdom2.JDOMFactory;
import org.jdom2.UncheckedJDOMFactory;

public class TestUncheckedJDOMFactory extends AbstractTestJDOMFactory {

	@Override
	protected JDOMFactory buildFactory() {
		return new UncheckedJDOMFactory();
	}
	
}
