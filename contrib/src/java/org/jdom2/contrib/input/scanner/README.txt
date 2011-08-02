
ElementScanner is a SAX filter that uses XPath-like expressions to select 
element nodes to build and notifies listeners when these elements becomes
available during the SAX parse.

ElementScanner does not aim at providing a faster parsing of XML documents.
Its primary focus is to allow the application to control the parse and to
consume the XML data while they are being parsed.  ElementScanner can be
viewed as a high-level SAX parser that fires events conveying JDOM elements
rather that XML tags and character data.
ElementScanner only notifies of the parsing of element nodes and does not
support reporting the parsing of DOCTYPE data, processing instructions or
comments except for those present within the selected elements.
Applications needing such data shall register a specific SAX ContentHandler
on ElementScanner to receive them in the form of raw SAX events.

To use this package, in addition to JDOM, the following products must be
present in the application class path:
 - Jakarta Regexp 1.1 or higher
   (see "http://jakarta.apache.org/regexp/index.html")
 - Jaxen 1.0 beta7 or higher
   (see "http://www.jaxen.org/")

For detailed information, please refer to the package Javadoc documentation
or the file "package.html" in this directory.


The "doc-files directory contains simple test cases that demonstrate how to
use ElementScanner within an application:

 - ElementScannerTest.java is a simple program that uses ElementScanner
   to parse the XML file passed as argument and registers a set of
   ElementListeners that display the parsed elements.
   Usage: java ElementScannerTest [XML file]

 - test.xml is an example of XML file that can be used with the above
   sample.


-- Laurent Bihanic
