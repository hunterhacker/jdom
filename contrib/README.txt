Introduction
============

jdom-contrib is a place for projects that increase the value of JDOM but
aren't (at least yet) in the core distribution.  Contributed code is
placed under the org.jdom.contrib package hierarchy.  

Currently we have org.jdom.contrib packages for beans, helpers, ids, input,
output, and schema.  "beans" holds JDOMBean and can hold related bean
work.  "helpers" holds certain helper functions.  "ids" demonstrates how to
use the attribute type support provided by JDOM to create JDOM documents that
allow looking up elements using the value of their ID attribute.  "input" and
"output" hold builders and outputters.  "schema" has code for in-memory
schema validation.

Some code from contrib (or the equivalent functionality) has been migrated in
to the core code, The code has been left in contrib and been marked as
'deprecated', as example code of what can be done.

If you have an interesting contribution, or just ideas that someone else might
pick up on, post to jdom-interest.
