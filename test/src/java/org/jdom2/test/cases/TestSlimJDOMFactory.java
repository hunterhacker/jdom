package org.jdom2.test.cases;

import org.jdom2.JDOMFactory;
import org.jdom2.SlimJDOMFactory;

@SuppressWarnings("javadoc")
public class TestSlimJDOMFactory extends AbstractTestJDOMFactory {

	@Override
	protected JDOMFactory buildFactory() {
		return new SlimJDOMFactory();
	}
	
}
