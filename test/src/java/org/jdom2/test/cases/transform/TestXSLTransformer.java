package org.jdom2.test.cases.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jdom2.Content;
import org.jdom2.DefaultJDOMFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.JDOMFactory;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.transform.XSLTransformException;
import org.jdom2.transform.XSLTransformer;
import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class TestXSLTransformer {
	
	private static final String xslpassthrough = 
			"<?xml version=\"1.0\"?>\n" +
			"<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n" +
			"   <xsl:output encoding=\"UTF-8\" />\n" +
			"   <xsl:template match=\"*\">\n" +
			"      <xsl:copy-of select=\".\" />\n" +
			"   </xsl:template>\n" +
			"</xsl:stylesheet>\n";

	private interface SetupTransform {
		XSLTransformer buildTransformer() throws XSLTransformException;
	}
	
	private void checkDocs(Document docexpect, Document docactual) {
		XMLOutputter out = new XMLOutputter();
		String expect = out.outputString(docexpect);
		String actual = out.outputString(docactual);
		assertEquals(expect, actual);
	}
	
	private void checkDocs(Document docexpect, boolean addroot, List<Content> content) {
		List<Content> toadd = content;
		if (addroot) {
			Element root = new Element(docexpect.getRootElement().getName());
			root.addContent(content);
			toadd = Collections.singletonList((Content)root);
		}
		Document actdoc = new Document(toadd);
		checkDocs(docexpect, actdoc);
	}
	
	private void checkTransform(Document doc, Document content, SetupTransform setup) {
		try {
			XSLTransformer trans = setup == null
					? new XSLTransformer(new StringReader(xslpassthrough))
					: setup.buildTransformer();
			Document out = trans.transform(content);
			checkDocs(doc, out);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected excepion: " + e.getMessage());
		}
	}
	
	private void checkTransform(Document doc, List<Content> content, boolean atroot, SetupTransform setup) {
		try {
			XSLTransformer trans = setup == null
					? new XSLTransformer(new StringReader(xslpassthrough))
					: setup.buildTransformer();
			List<Content> out = trans.transform(content);
			checkDocs(doc, atroot, out);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected excepion: " + e.getMessage());
		}
	}
	
	@Test
	public void testXSLTransformerInputStream() {
		Document doc = new Document(new Element("root"));
		checkTransform(doc, doc, new SetupTransform() {
			@Override
			public XSLTransformer buildTransformer() throws XSLTransformException {
				return new XSLTransformer(new ByteArrayInputStream(xslpassthrough.getBytes()));
			}
		});
	}

	@Test
	public void testXSLTransformerReader() {
		Document doc = new Document(new Element("root"));
		checkTransform(doc, doc, new SetupTransform() {
			@Override
			public XSLTransformer buildTransformer() throws XSLTransformException {
				return new XSLTransformer(new StringReader(xslpassthrough));
			}
		});
	}

	@Test
	public void testXSLTransformerString() throws IOException {
		File tmpf = File.createTempFile("jdomxsltest", ".xml");
		try {
			tmpf.deleteOnExit();
			FileWriter fw = new FileWriter(tmpf);
			fw.write(xslpassthrough);
			fw.flush();
			fw.close();
			final String url = tmpf.toURI().toURL().toExternalForm();
			Document doc = new Document(new Element("root"));
			checkTransform(doc, doc, new SetupTransform() {
				@Override
				public XSLTransformer buildTransformer() throws XSLTransformException {
					return new XSLTransformer(url);
				}
			});
		} finally {
			tmpf.delete();
		}
	}

	@Test
	public void testXSLTransformerFile() throws IOException {
		final File tmpf = File.createTempFile("jdomxsltest", ".xml");
		try {
			tmpf.deleteOnExit();
			FileWriter fw = new FileWriter(tmpf);
			fw.write(xslpassthrough);
			fw.flush();
			fw.close();
			Document doc = new Document(new Element("root"));
			checkTransform(doc, doc, new SetupTransform() {
				@Override
				public XSLTransformer buildTransformer() throws XSLTransformException {
					return new XSLTransformer(tmpf);
				}
			});
		} finally {
			tmpf.delete();
		}
	}

	@Test
	public void testXSLTransformerDocument() throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();
		final Document xsldoc = builder.build(new StringReader(xslpassthrough));
		Document doc = new Document(new Element("root"));
		checkTransform(doc, doc, new SetupTransform() {
			@Override
			public XSLTransformer buildTransformer() throws XSLTransformException {
				return new XSLTransformer(xsldoc);
			}
		});
	}

	@Test
	public void testTransformList() throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();
		final Document xsldoc = builder.build(new StringReader(xslpassthrough));
		Document doc = new Document(new Element("root"));
		List<Content> content = new ArrayList<Content>();
		content.add(new Element("root"));
		checkTransform(doc, content, false, new SetupTransform() {
			@Override
			public XSLTransformer buildTransformer() throws XSLTransformException {
				return new XSLTransformer(xsldoc);
			}
		});
	}

	@Test
	@Ignore
	public void testTransformDocumentEntityResolver() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSetFactory() throws JDOMException, IOException {
		final JDOMFactory fac = new DefaultJDOMFactory() {
			@Override
			public Element element(final int line, final int col, String name, String prefix, String uri) {
				return super.element(line, col, "xx" + name, prefix, uri);
			}
			@Override
			public Element element(final int line, final int col, String name, String uri) {
				return super.element(line, col, "xx" + name, uri);
			}
			@Override
			public Element element(final int line, final int col, String name) {
				return super.element(line, col, "xx" + name);
			}
			@Override
			public Element element(final int line, final int col, String name, Namespace namespace) {
				return super.element(line, col, "xx" + name, namespace);
			}
		};
		
		SAXBuilder builder = new SAXBuilder();
		final Document xsldoc = builder.build(new StringReader(xslpassthrough));
		final XSLTransformer trans = new XSLTransformer(xsldoc);
		assertNull(trans.getFactory());
		trans.setFactory(fac);
		assertTrue(trans.getFactory() == fac);

		Document doc = new Document(new Element("root"));
		Document expect = new Document(new Element("xxroot"));
		checkTransform(expect, doc, new SetupTransform() {
			@Override
			public XSLTransformer buildTransformer() throws XSLTransformException {
				return trans;
			}
		});
		
		
	}

}
