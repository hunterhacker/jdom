import java.io.IOException;

import org.xml.sax.InputSource;

import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

import org.jdom2.contrib.input.scanner.ElementScanner;
import org.jdom2.contrib.input.scanner.ElementListener;


@SuppressWarnings("javadoc")
public class ElementScannerTest
{
   public static XMLOutputter out = new XMLOutputter();

   public static void main(String[] args) throws Exception
   {
      org.jdom2.contrib.input.scanner.XPathMatcher.setDebug(true);

      ElementScanner scanner = new ElementScanner();

      scanner.addElementListener(new Spy("Listener #1 - \"x//y\""),  "x//y");
      scanner.addElementListener(new Spy("Listener #2 - \"y/*/y\""), "y/*/y");
      scanner.addElementListener(new Spy("Listener #3 - \"/*\""),    "/*");
      scanner.addElementListener(new Spy("Listener #4 - \"z\""),     "z");

      scanner.addElementListener(new Spy("Listener #5 - \"*[contains(@name,'.1')]\""),
                                                        "*[contains(@name,'.1')]");
      scanner.addElementListener(new Spy("Listener #6 - \"y[.//y]\""),
                                                        "y[.//y]");

      String input = "test.xml";
      if (args.length > 0)
      {
         input = args[0];
      }

      scanner.parse(new InputSource(input));
   }

   private static class Spy implements ElementListener
   {
      private String name;

      public Spy(String name)
      {
         this.name = name;
      }

      @Override
	public void elementMatched(String path, Element e)
      {
         try
         {
            System.out.print(this.name + "\n>>> " + path + "\n");
            out.output(e, System.out);
            System.out.println("\n<<<\n");
         }
         catch (IOException ex1) { ex1.printStackTrace(); }
      }
   }
}

