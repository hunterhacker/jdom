package org.jdom2.test.util;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("javadoc")
public final class ListTest extends AbstractTestList<Integer> {

	public ListTest() {
		super(Integer.class, true);
	}

	@Override
	public List<Integer> buildEmptyList() {
		return new ArrayList<Integer>();
	}

	@Override
	public Integer[] buildSampleContent() {
		return new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
	}
	
	@Override
	public Object[] buildIllegalClassContent() {
		return new Object[]{};
	}
	
	@Override
	public Integer[] buildIllegalArgumentContent() {
		return new Integer[0];
	}
	
	@Override
	public Integer[] buildAdditionalContent() {
		return new Integer[0];
	}
	
}