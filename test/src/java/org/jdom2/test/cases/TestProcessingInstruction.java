package org.jdom2.test.cases;

import static org.jdom2.test.util.UnitTestUtil.checkEquals;
import static org.jdom2.test.util.UnitTestUtil.cloneString;
import static org.jdom2.test.util.UnitTestUtil.deSerialize;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;
import org.jdom2.IllegalDataException;
import org.jdom2.IllegalNameException;
import org.jdom2.IllegalTargetException;
import org.jdom2.ProcessingInstruction;
import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class TestProcessingInstruction {

	@Test
	public void testProcessingInstruction() {
		ProcessingInstruction pi = new ProcessingInstruction() {
			// nothing
    		private static final long serialVersionUID = 200L;
		};
		assertTrue(null == pi.getTarget());
		assertTrue(null == pi.getValue());
	}

	@Test
	public void testProcessingInstructionString() {
		ProcessingInstruction pi = new ProcessingInstruction("test");
		checkEquals(pi.getTarget(), "test");
		checkEquals(pi.getValue(), "");
	}

	@Test
	public void testProcessingInstructionStringString() {
		ProcessingInstruction pi = new ProcessingInstruction("test", "key='value'");
		checkEquals(pi.getTarget(), "test");
		checkEquals(pi.getValue(), "key='value'");
	}

	@Test
	public void testProcessingInstructionStringMap() {
		Map<String, String>kvs = buildMap(
				"key1", "val1",
				"key2", "val2"
				);
		ProcessingInstruction pi = new ProcessingInstruction("test", kvs);
		checkEquals(pi.getTarget(), "test");
		assertTrue(pi.getValue() != null);
		checkMapValues(pi, kvs);
	}
	
	@Test (expected=IllegalTargetException.class)
	public void testIllegalTargetContentA() {
		new ProcessingInstruction("1tgt", "data");
	}
	
	@Test (expected=IllegalTargetException.class)
	public void testIllegalTargetContentB() {
		new ProcessingInstruction(" tgt", "data");
	}
	
	@Test (expected=IllegalTargetException.class)
	public void testIllegalTargetContentC() {
		new ProcessingInstruction("tg?>t", "data");
	}
	
	@Test (expected=IllegalTargetException.class)
	public void testIllegalTargetContentD() {
		new ProcessingInstruction("tgt ", "data");
	}
	
	@Test (expected=IllegalTargetException.class)
	public void testIllegalTargetContentE() {
		new ProcessingInstruction("t gt", "data");
	}
	
	@Test (expected=IllegalTargetException.class)
	public void testIllegalTargetContentF() {
		ProcessingInstruction pi = new ProcessingInstruction("tgt", "data");
		pi.setTarget("tg t");
	}
	
	@Test (expected=IllegalTargetException.class)
	public void testIllegalTargetContentG() {
		ProcessingInstruction pi = new ProcessingInstruction("tgt", "data");
		pi.setTarget("tgt ");
	}
	
	@Test (expected=IllegalTargetException.class)
	public void testIllegalTargetContentH() {
		ProcessingInstruction pi = new ProcessingInstruction("tgt", "data");
		pi.setTarget("tg?t");
	}
	
	@Test (expected=IllegalDataException.class)
	public void testIllegalAttnameA() {
		ProcessingInstruction pi = new ProcessingInstruction("tgt", "data");
		pi.setPseudoAttribute("tg?>t", "val");
	}
	
	@Test (expected=IllegalDataException.class)
	public void testIllegalAttnameB() {
		ProcessingInstruction pi = new ProcessingInstruction("tgt", buildMap("ok", "val"));
		pi.setPseudoAttribute("b?>a", "val");
	}
	
	@Test (expected=IllegalNameException.class)
	@Ignore // FIXME
	public void testIllegalAttnameC() {
		new ProcessingInstruction("tgt", buildMap("ok", "val", "b a", "val"));
	}
	
	@Test (expected=IllegalDataException.class)
	@Ignore //FIXME
	public void testIllegalAttValueA() {
		new ProcessingInstruction("tgt", buildMap("ok", "val", " bad", "va'''\"\"\"l"));
	}
	
	@Test (expected=IllegalDataException.class)
	public void testIllegalAttValueB() {
		new ProcessingInstruction("tgt", buildMap("ok", "val", "b a", "v?>al"));
	}
	
	@Test (expected=IllegalDataException.class)
	public void testIllegalAttValueC() {
		ProcessingInstruction pi = new ProcessingInstruction("tgt", buildMap("ok", "val"));
		pi.setPseudoAttribute("b a", "v?>al");
	}
	
	@Test (expected=IllegalDataException.class)
	public void testIllegalData() {
		ProcessingInstruction pi = new ProcessingInstruction("tgt", buildMap("ok", "val"));
		pi.setData("b?>a=val");
	}
	
	private void checkMapValues(ProcessingInstruction pi, Map<String, String> vals) {
		for (Map.Entry<String,String> me : vals.entrySet()) {
			checkEquals(pi.getPseudoAttributeValue(me.getKey()), me.getValue());
		}
		for (Object s : pi.getPseudoAttributeNames() ) {
			if (!vals.containsKey(s)) {
				fail("ProcessingInstruction has key " + s + " which is not expected.");
			}
		}
	}
	
	private Map<String,String> buildMap(String...kvpairs) {
		assertTrue(kvpairs != null);
		assertTrue(kvpairs.length % 2 == 0);
		LinkedHashMap<String, String> lhm = new LinkedHashMap<String, String>();
		for (int i = 0; i < kvpairs.length; i += 2) {
			lhm.put(kvpairs[i], kvpairs[i+1]);
		}
		return lhm;
	}

	@Test
	public void testGetValue() {
		ProcessingInstruction pi = new ProcessingInstruction("test", "value");
		checkEquals(cloneString("value"), pi.getValue());
	}

	@Test
	public void testCloneA() {
		ProcessingInstruction pi = new ProcessingInstruction("test", "value");
		ProcessingInstruction copy = pi.clone();
		assertTrue(!pi.equals(copy));
		checkEquals(pi.getTarget(), copy.getTarget());
		checkEquals(pi.getValue(), copy.getValue());
	}

	@Test
	public void testCloneB() {
		ProcessingInstruction pi = new ProcessingInstruction("test", "");
		ProcessingInstruction copy = pi.clone();
		assertTrue(!pi.equals(copy));
		checkEquals(pi.getTarget(), copy.getTarget());
		checkEquals(pi.getValue(), copy.getValue());
	}

	@Test
	public void testCloneC() {
		ProcessingInstruction pi = new ProcessingInstruction("test", "");
		pi.setPseudoAttribute("hi", "val");
		pi.removePseudoAttribute("hi");
		ProcessingInstruction copy = pi.clone();
		assertTrue(!pi.equals(copy));
		checkEquals(pi.getTarget(), copy.getTarget());
		checkEquals(pi.getValue(), copy.getValue());
	}

	@Test
	public void testCloneD() {
		Map<String,String> empty = Collections.emptyMap();
		ProcessingInstruction pi = new ProcessingInstruction("test", empty);
		ProcessingInstruction copy = pi.clone();
		assertTrue(!pi.equals(copy));
		checkEquals(pi.getTarget(), copy.getTarget());
		checkEquals(pi.getValue(), copy.getValue());
	}

	@Test
	public void testSerialize() {
		ProcessingInstruction pi = new ProcessingInstruction("test", "value");
		ProcessingInstruction copy = deSerialize(pi);
		checkEquals(pi.getTarget(), copy.getTarget());
		checkEquals(pi.getValue(), copy.getValue());
	}

	@Test
	public void testSetTarget() {
		ProcessingInstruction pi = new ProcessingInstruction("test", "value");
		checkEquals(pi.getTarget(), "test");
		checkEquals(pi.getValue(), "value");
		assertTrue(pi == pi.setTarget("test2"));
		checkEquals(pi.getTarget(), "test2");
		assertTrue(pi == pi.setTarget("test"));
		checkEquals(pi.getTarget(), "test");
	}

	@Test
	public void testGetTarget() {
		ProcessingInstruction pi = new ProcessingInstruction("test", "value");
		checkEquals(pi.getTarget(), "test");
		checkEquals(pi.getValue(), "value");
		pi.setTarget("test2");
		checkEquals(pi.getTarget(), "test2");
		pi.setTarget("test");
		checkEquals(pi.getTarget(), "test");
	}

	@Test
	public void testGetData() {
		ProcessingInstruction pi = new ProcessingInstruction("test", "value");
		checkEquals(pi.getTarget(), "test");
		checkEquals(pi.getValue(), "value");
		checkEquals(pi.getData(), "value");
	}

	@Test
	public void testToString() {
		ProcessingInstruction pi = new ProcessingInstruction("test", "value");
		assertTrue(pi.toString() != null);
	}
	
	@Test
	public void testExercise() {
		Map<String,String> data = new LinkedHashMap<String, String>();
		for (int i = 0; i < 10; i++) {
			data.put(String.format("key%02d", i), String.format(" val %2d ", i));
			Map<String,String> td = new LinkedHashMap<String,String>();
			td.putAll(data);
			exercise(td);
		}
		
	}
	

	private void exercise(Map<String,String> data) {
		exerciseQuotes("'", "'", data);
		exerciseQuotes("\"", "\"", data);
		exerciseQuotes("	\"", "\"	", data);
		exerciseQuotes(" '", "' ", data);
		exerciseQuotes("	'", "'	", data);
		data.put("quote", "Foo'd up!");
		exerciseQuotes("\"", "\"", data);
		data.put("quote", "This is a \"quoted\" value.");
		exerciseQuotes(" '", "' ", data);
		data.remove("quote");
		ProcessingInstruction pi = new ProcessingInstruction("tgt", data);
		for (Map.Entry<String, String> me : data.entrySet()) {
			assertEquals(me.getValue(), pi.getPseudoAttributeValue(me.getKey()));
			assertTrue(pi.removePseudoAttribute(me.getKey()));
			assertFalse(pi.removePseudoAttribute(me.getKey()));
		}
		for (Map.Entry<String, String> me : data.entrySet()) {
			assertTrue(null == pi.getPseudoAttributeValue(me.getKey()));
			assertFalse(pi.removePseudoAttribute(me.getKey()));
			assertTrue(pi == pi.setPseudoAttribute(me.getKey(), me.getValue()));
			assertEquals(me.getValue(), pi.getPseudoAttributeValue(me.getKey()));
		}
		checkEquals(mapValue(data), pi.getData());
		
	}

	private void exerciseQuotes(String open, String close,
			Map<String, String> data) {
		StringBuilder sb = new StringBuilder();
		int cnt = 0;
		for(Map.Entry<String, String> me : data.entrySet()) {
			if (cnt++ > 0) {
				sb.append(" ");
			}
			sb.append(me.getKey()).append("=").append(open).append(me.getValue()).append(close);
		}
		ProcessingInstruction pi = new ProcessingInstruction("test", sb.toString());
		checkEquals(pi.getData(), sb.toString());
		List<?> keys = pi.getPseudoAttributeNames();
		assertTrue(keys != null);
		assertTrue(keys.size() == data.size());
		assertTrue(keys.containsAll(data.keySet()));
		assertTrue(data.keySet().containsAll(keys));
		for (Map.Entry<String,String> me : data.entrySet()) {
			String val = pi.getPseudoAttributeValue(me.getKey());
			assertTrue(val != null);
			String x = me.getValue();
			if (!x.equals(val)) {
				fail("We expected value '" + x + "' but got '" + val + "'.");
			}
		}
		assertEquals(pi.getData(), pi.getValue());
		// ProcessingInstruction does not change the quoting unless you mess with
		// attribute values.
		assertEquals(pi.getData(), pi.getValue());
		pi.setPseudoAttribute("foo", "bar");
		pi.removePseudoAttribute("foo");
		checkEquals(pi.getData(), mapValue(data));
	}
	
	private String mapValue(Map<String,String> data) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String,String> me : data.entrySet()) {
			if (sb.length() > 0) {
				sb.append(" ");
			}
			sb.append(me.getKey()).append("=\"").append(me.getValue()).append("\"");
		}
		return sb.toString();
	}

    @Test
	public void testCloneDetatchParentProcessingInstruction() {
		Element parent = new Element("root");
		ProcessingInstruction content = new ProcessingInstruction("val", "");
		parent.addContent(content);
		ProcessingInstruction clone = content.detach().clone();
		assertEquals(content.getValue(), clone.getValue());
		assertNull(content.getParent());
		assertNull(clone.getParent());
	}

}
