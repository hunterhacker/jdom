package org.jdom2.filter;

import org.jdom2.Text;
import org.jdom2.Content.CType;

/**
 * A filter that matches Text, but not CDATA content.
 * 
 * @author Rolf Lear
 *
 */
final class TextOnlyFilter extends AbstractFilter<Text> {

	/**
	 * JDOM2 Serialization: Default mechanism
	 */
	private static final long serialVersionUID = 200L;

	@Override
	public Text filter(Object content) {
		if (content instanceof Text) {
			final Text txt = (Text)content;
			if (txt.getCType() == CType.Text) {
				return txt;
			}
		}
		return null;
	}
	
	@Override
	public int hashCode() {
		return this.getClass().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof TextOnlyFilter;
	}
	
}
