Introduction
============

jdom-contrib is a place for projects that increase the value of JDOM but
aren't (at least yet) in the core distribution.  Contributors with write
access to jdom-contrib can be found in COMMITTERS.txt.  Contributed code is
placed under the org.jdom.contrib package hierarchy.  

Currently we have org.jdom.contrib packages for beans, helpers, ids, input,
output, and schema.  "beans" holds JDOMBean and can hold related bean
work.  "helpers" holds certain helper functions.  "ids" demonstrates how to
use the attribute type support provided by JDOM to create JDOM documents that
allow looking up elements using the value of their ID attribute.  "input" and
"output" hold builders and outputters.  "schema" has code for in-memory
schema validation.

If you have an interesting contribution, or just ideas that someone else might
pick up on, post to jdom-interest.

Building instructions
=====================

Building jdom-contrib is the same as building jdom.  See the README.txt in the
jdom module.

Note that build.xml assumes ../jdom/build exists and has a jdom.jar within.
The build script also uses ant.jar and other libraries from ../jdom/lib.  What
this means is you must the jdom and jdom-contrib directories at the same
level.  For example:
  cvs.jdom.org/jdom
  cvs.jdom.org/jdom-contrib

