package org.jdom2.test.cases;

import org.jdom2.JDOMFactory;
import org.jdom2.UncheckedJDOMFactory;

@SuppressWarnings("javadoc")
public class TestUncheckedJDOMFactory extends AbstractTestJDOMFactory {

	/**
	 * @param located
	 */
	public TestUncheckedJDOMFactory() {
		super(false);
	}

	@Override
	protected JDOMFactory buildFactory() {
		return new UncheckedJDOMFactory();
	}
	
}
