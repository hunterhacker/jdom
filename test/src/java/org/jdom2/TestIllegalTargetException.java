package org.jdom2;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestIllegalTargetException {

	@Test
	public void testIllegalTargetExceptionStringString() {
		assertTrue (null != 
				new IllegalTargetException("target", "reason").getMessage());

	}

	@Test
	public void testIllegalTargetExceptionString() {
		assertTrue (null != 
				new IllegalTargetException("reason").getMessage());
	}

}
