package org.jdom2.test.cases;

import org.jdom2.DefaultJDOMFactory;
import org.jdom2.JDOMFactory;

public class TestDefaultJDOMFactory extends AbstractTestJDOMFactory {

	@Override
	protected JDOMFactory buildFactory() {
		return new DefaultJDOMFactory();
	}
	
}
