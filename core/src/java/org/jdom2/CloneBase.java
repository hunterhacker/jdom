package org.jdom2;

/**
 * This simple class just tidies up any cloneable classes.
 * This method deals with any CloneNotSupported exceptions.
 * THis class is package private only.
 * @author Rolf Lear
 *
 */
class CloneBase implements Cloneable {
	
	/**
	 * This convenience method simply deals with the irritating
	 * CloneNotSupportedException, and wraps it in IllegalStateException.
	 * This should never, ever, ever happen. But, it is a pain to deal with
	 * ugly clone code in some classes.
	 * Subclasses of this should still call super.clone() in their clone method.
	 */
	@Override
	protected Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException(String.format(
					"Unable to clone class %s which should always support it.",
					this.getClass().getName()),
					e);
		}
	}

}
