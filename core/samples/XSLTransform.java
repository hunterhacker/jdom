package samples;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import org.jdom.transform.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;

public class XSLTransform {

  public static void main(String[] args) throws Exception {
    if (args.length != 2) {
      System.err.println("Usage: samples.XSLTransform [some.xml] [some.xsl]");
      return;
    }

    String docname = args[0];
    String sheetname = args[1];
    SAXBuilder builder = new SAXBuilder();
    Document doc = builder.build(docname);

    Transformer transformer = TransformerFactory.newInstance()
      .newTransformer(new StreamSource(sheetname));
  
    JDOMSource source = new JDOMSource(doc);
    JDOMResult result = new JDOMResult();
    transformer.transform(source, result);
    Document doc2 = result.getDocument();

    XMLOutputter outp = new XMLOutputter();
    outp.setTextNormalize(true);
    outp.setIndent("  ");
    outp.setNewlines(true);
    outp.output(doc2, System.out);
  }
}
