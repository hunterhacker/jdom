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
	 * <p>
	 * Subclasses of this should still call super.clone() in their clone method.
	 * <p>
	 * Additionally, when you use the concept of 'co-variant return values' you
	 * create 'bridge' methods. By way of example, because we change the return
	 * type of clone() from Object to CloneBase, Java is forced to put in a
	 * 'bridge' method that has an Object return type, even though we never
	 * actually call it.
	 * <p>
	 * This has an impact on the code coverage tool Cobertura, which reports
	 * that there is missed code (and there is, the bridge method). It reports
	 * it as being '0' calls to the 'class' line (the class line is marked red).
	 * <p>
	 * By making this CloneBase code do the first level of co-variant return, it
	 * is this class which is victim of the Cobertura reporting, not the
	 * multiple subclasses (like Attribute, Document, Content, etc.).
	 */
	@Override
	protected CloneBase clone() {
		try {
			return (CloneBase)super.clone();
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException(String.format(
					"Unable to clone class %s which should always support it.",
					this.getClass().getName()),
					e);
		}
	}

}
