

import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.*;

import org.jdom.contrib.ids.IdDocument;
import org.jdom.contrib.ids.IdFactory;


public class TestIds {

   public static void main(String[] args) throws Exception {
      if (args.length < 2) {
         System.out.println("Usage: java TestIds <XML file> <ID>");
         System.exit(2);
      }

      SAXBuilder builder = new SAXBuilder();
      builder.setFactory(new IdFactory());

      IdDocument doc = (IdDocument)(builder.build(args[0]));
      Element    elt = doc.getElementById(args[1]);

      if (elt != null) {
         new XMLOutputter(Format.getPrettyFormat()).output(elt, System.out);
         System.out.println();
         System.exit(0);
      }
      else {
         System.out.println("No element with ID \"" + args[1] + "\" found");
         System.exit(1);
      }
   }
}

