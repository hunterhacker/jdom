package org.jdom2.test.cases.serialize;

import org.jdom2.filter.AbstractFilter;

@SuppressWarnings("javadoc")
public class SFilter extends AbstractFilter<Integer> {

	private static final long serialVersionUID = 1L;

	@Override
	public Integer filter(Object content) {
		if (content instanceof Integer) {
			return (Integer)content;
		}
		return null;
	}

}
