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

    JDOMTransformer transformer = new JDOMTransformer(sheetname);
    Document doc2 = transformer.transform(doc);

    XMLOutputter outp = new XMLOutputter();
    outp.setTextNormalize(true);
    outp.setIndent("  ");
    outp.setNewlines(true);
    outp.output(doc2, System.out);
  }
}
