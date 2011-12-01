package org.jdom2.test.cases.filter;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;

import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.DocType;
import org.jdom2.Element;
import org.jdom2.EntityRef;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;
import org.jdom2.filter.ContentFilter;
import org.jdom2.filter.ElementFilter;
import org.jdom2.test.util.UnitTestUtil;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class TestContentFilter extends AbstractTestFilter {
	
	private final int[] allContent = new int[] {
			ContentFilter.CDATA, ContentFilter.COMMENT,
			ContentFilter.DOCTYPE, ContentFilter.DOCUMENT, 
			ContentFilter.ELEMENT, ContentFilter.ENTITYREF,
			ContentFilter.PI, ContentFilter.TEXT
	};
	
	@Test
	public void testSetters() {
		int mask = 0;
		boolean flag = false;
		ContentFilter cfa = new ContentFilter(false);
		do {
			flag = !flag;
			for (int m : allContent) {
				switch (m) {
				case ContentFilter.CDATA :
					cfa.setCDATAVisible(flag);
					break;
				case ContentFilter.COMMENT :
					cfa.setCommentVisible(flag);
					break;
				case ContentFilter.DOCTYPE :
					cfa.setDocTypeVisible(flag);
					break;
				case ContentFilter.DOCUMENT :
					if (flag) {
						cfa.setFilterMask(cfa.getFilterMask() | ContentFilter.DOCUMENT);
					} else {
						cfa.setFilterMask(cfa.getFilterMask() & (~ ContentFilter.DOCUMENT) );
					}
					break;
				case ContentFilter.ELEMENT :
					cfa.setElementVisible(flag);
					break;
				case ContentFilter.ENTITYREF :
					cfa.setEntityRefVisible(flag);
					break;
				case ContentFilter.PI :
					cfa.setPIVisible(flag);
					break;
				case ContentFilter.TEXT :
					cfa.setTextVisible(flag);
					break;
				}
				if (flag) {
					mask |= m;
				} else {
					mask &= (~m);
				}
				if (cfa.getFilterMask() != mask) {
					fail(String.format("ContentFilter Mask is out of sync after " +
							"setting flag %d with value %s", m, flag));
				}
			}
		} while (flag);
	}
	
	@Test
	public void testDefaultDocumentContent() { 
		ContentFilter cf = new ContentFilter();
		cf.setDocumentContent();
		CallBack cb = new CallBack() {
			@Override
			public boolean isValid(Object c) {
				if (c instanceof Element) {
					return true;
				}
				if (c instanceof ProcessingInstruction) {
					return true;
				}
				if (c instanceof DocType) {
					return true;
				}
				if (c instanceof Comment) {
					return true;
				}
				return false;
			}
		};
		
		exerciseContent(cb, ContentFilter.PI, ContentFilter.ELEMENT,
				ContentFilter.COMMENT, ContentFilter.DOCTYPE);

	}
	
	@Test
	public void testAllElementContent() { 
		ContentFilter cf = new ContentFilter();
		cf.setElementContent();
		CallBack cb = new CallBack() {
			@Override
			public boolean isValid(Object c) {
				if (c instanceof Element) {
					return true;
				}
				if (c instanceof ProcessingInstruction) {
					return true;
				}
				if (c instanceof EntityRef) {
					return true;
				}
				if (c instanceof CDATA) {
					return true;
				}
				if (c instanceof Text) {
					return true;
				}
				if (c instanceof Comment) {
					return true;
				}
				return false;
			}
		};
		
		exerciseContent(cb, ContentFilter.ELEMENT, ContentFilter.ENTITYREF,
				ContentFilter.COMMENT, ContentFilter.CDATA,
				ContentFilter.TEXT, ContentFilter.PI);

	}
	
	
	@Test
	public void testAllContentFilter() {
		CallBack cb = new CallBack() {
			@Override
			public boolean isValid(Object c) {
				return c != null;
			}
		};
		exerciseContent(cb, ContentFilter.CDATA, ContentFilter.COMMENT,
				ContentFilter.DOCTYPE, ContentFilter.DOCUMENT, 
				ContentFilter.ELEMENT, ContentFilter.ENTITYREF,
				ContentFilter.PI, ContentFilter.TEXT);
	}
	
	@Test
	public void testElementContentFilter() {
		CallBack cb = new CallBack() {
			@Override
			public boolean isValid(Object c) {
				return c instanceof Element;
			}
		};
		exerciseContent(cb, ContentFilter.ELEMENT);
	}
	
//	@Test
//	public void testDocumentContentFilter() {
//		ContentFilter cf = new ContentFilter(ContentFilter.DOCUMENT);
//		assertTrue(cf.matches(getDocument()));
//		assertFalse(cf.matches(getRoot()));
//		ContentFilter cfe = new ContentFilter(ContentFilter.ELEMENT);
//		assertFalse(cfe.matches(getDocument()));
//	}
	
	@Test
	public void testDocTypeContentFilter() {
		CallBack cb = new CallBack() {
			@Override
			public boolean isValid(Object c) {
				return c instanceof DocType;
			}
		};
		exerciseContent(cb, ContentFilter.DOCTYPE);
	}
	
	@Test
	public void testPIContentFilter() {
		CallBack cb = new CallBack() {
			@Override
			public boolean isValid(Object c) {
				return c instanceof ProcessingInstruction;
			}
		};
		exerciseContent(cb, ContentFilter.PI);
	}
	
	@Test
	public void testCDATAContentFilter() {
		CallBack cb = new CallBack() {
			@Override
			public boolean isValid(Object c) {
				return c instanceof CDATA;
			}
		};
		exerciseContent(cb, ContentFilter.CDATA);
	}
	
	@Test
	public void testCommentContentFilter() {
		CallBack cb = new CallBack() {
			@Override
			public boolean isValid(Object c) {
				return c instanceof Comment;
			}
		};
		exerciseContent(cb, ContentFilter.COMMENT);
	}
	
	private void exerciseContent(CallBack cb, int...types) {
		int mask = 0;
		ContentFilter cfa = new ContentFilter(0);
		for (int m : types) {
			mask |= m;
			switch (m) {
			case ContentFilter.CDATA :
				cfa.setCDATAVisible(true);
				break;
			case ContentFilter.COMMENT :
				cfa.setCommentVisible(true);
				break;
			case ContentFilter.DOCTYPE :
				cfa.setDocTypeVisible(true);
				break;
			case ContentFilter.DOCUMENT :
				cfa.setFilterMask(cfa.getFilterMask() | ContentFilter.DOCUMENT);
				break;
			case ContentFilter.ELEMENT :
				cfa.setElementVisible(true);
				break;
			case ContentFilter.ENTITYREF :
				cfa.setEntityRefVisible(true);
				break;
			case ContentFilter.PI :
				cfa.setPIVisible(true);
				break;
			case ContentFilter.TEXT :
				cfa.setTextVisible(true);
				break;
			}
		}
		exercise(cfa, getRoot(), cb);
		exercise(cfa, getDocument(), cb);
		
		ContentFilter cf = new ContentFilter(mask);
		exercise(cf, getRoot(), cb);
		exercise(cf, getDocument(), cb);

		assertFilterEquals(cf, cfa);
		
		assertTrue(cf.filter(new Object()) == null);
		assertTrue(cfa.filter(new Object()) == null);
	}

	@Test
	public void testEqualsObject() {
		assertFilterEquals(new ContentFilter(), new ContentFilter(true));
		
		ContentFilter cfa = new ContentFilter(ContentFilter.CDATA);
		ContentFilter cfb = UnitTestUtil.deSerialize(cfa);
		assertFilterEquals(cfa, cfb);
		cfa.setCommentVisible(true);
		assertFilterNotEquals(cfa, cfb);
		
		assertFilterNotEquals(cfa, new ContentFilter(true));
		assertFilterNotEquals(cfa, new ContentFilter(false));
		assertFilterNotEquals(cfa, new ElementFilter());
		
	}

}
