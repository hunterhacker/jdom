package org.jdom2.input;

import org.jdom2.Namespace;

/**
 * This DefaultStAXFilter includes all content and prunes nothing.
 * <p>
 * Override this class to make adjustments to get the results you need.
 * 
 * @see StAXFilter
 * 
 * @author Rolf Lear
 */
public class DefaultStAXFilter implements StAXFilter {

	@Override
	public boolean includeDocType() {
		return true;
	}

	@Override
	public boolean includeElement(final int depth, final String name, final Namespace ns) {
		return true;
	}

	@Override
	public String includeComment(final int depth, final String comment) {
		return comment;
	}

	@Override
	public boolean includeEntityRef(final int depth, final String name) {
		return true;
	}

	@Override
	public String includeCDATA(final int depth, final String text) {
		return text;
	}

	@Override
	public String includeText(final int depth, final String text) {
		return text;
	}

	@Override
	public boolean includeProcessingInstruction(final int depth, final String target) {
		return true;
	}

	@Override
	public boolean pruneElement(final int depth, final String name, final Namespace ns) {
		return false;
	}

	@Override
	public String pruneComment(final int depth, final String comment) {
		return comment;
	}

	@Override
	public boolean pruneEntityRef(final int depth, final String name) {
		return false;
	}

	@Override
	public String pruneCDATA(final int depth, final String text) {
		return text;
	}

	@Override
	public String pruneText(final int depth, final String text) {
		return text;
	}

	@Override
	public boolean pruneProcessingInstruction(final int depth, final String target) {
		return false;
	}

}
