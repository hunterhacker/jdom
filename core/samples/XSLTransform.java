package samples;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import org.jdom.transform.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;

public class XSLTransform {

  public static void main(String[] args) throws Exception {
    SAXBuilder builder = new SAXBuilder();
    Document doc = builder.build(args[0]);
    Document doc2 = transform(doc, args[1]);
    XMLOutputter out = new XMLOutputter("  ", true);
    out.setTextNormalize(true);
    out.output(doc2, System.out);
  }

  public static Document transform(Document in, String stylesheet) 
                              throws JDOMException {
    try {
      Transformer transformer = TransformerFactory.newInstance()
        .newTransformer(new StreamSource(stylesheet));
  
      JDOMResult out = new JDOMResult();
      transformer.transform(new JDOMSource(in), out);
      return out.getDocument();
    }
    catch (TransformerException e) {
      throw new JDOMException("XSLT Transformation failed", e);
    }
  }
}

