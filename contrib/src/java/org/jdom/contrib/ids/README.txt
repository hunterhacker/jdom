
This package demonstrates how to use the attribute type support provided
by JDOM to create JDOM documents that allow looking up elements using
the value of their ID attribute.
Note that for an attribute to be recognized as an ID, the XML document
must be associated to a DTD which defines the type of the attributes.

For detailed information, please refer to the package Javadoc documentation
or the file "package.html" in this directory.


The "doc-files" directory contains simple test cases that demonstrate how
to use the IdFactory to build an IdDocument and how to retrieve an element
by its ID from an IdDocument:

 - TestIds.java is a simple program that builds an IdDocument from the
   filename passed as first argument and looks up the element whose ID
   value matches the second argument.
   Usage: java TestIds <XML file> <ID>

 - testIds.xml is an example of XML file that can be used with the above
   sample.  It is associated to the DTD "testIds.dtd" which defines which
   attributes are IDs.


-- Laurent Bihanic
