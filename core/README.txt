Introduction
============

See the web site http://jdom.org


Installing the build tools
==========================

The JDOM build system is based on Jakarta Ant, which is a Java building tool
originally developed for the Jakarta Tomcat project but now used in many other
Apache projects and extended by many developers.

Ant is a little but very handy tool that uses a build file written in XML
(build.xml) as building instructions. For more information refer to
"http://jakarta.apache.org/ant/".

The only thing that you have to make sure of is that the "JAVA_HOME"
environment property is set to match the top level directory containing the
JVM you want to use. For example:

C:\> set JAVA_HOME=C:\jdk1.2.2
  (or jdk1.3.1, etc.)

or on Unix:

% setenv JAVA_HOME /usr/local/java
  (csh)
> JAVA_HOME=/usr/java; export JAVA_HOME
  (ksh, bash)

That's it!


Building instructions
=====================

Ok, let's build the code. First, make sure your current working directory is
where the build.xml file is located. Then type

  ./build.sh (unix)
  .\build.bat (win32)

if everything is right and all the required packages are visible, this action
will generate a file called "jdom.jar" in the "./build" directory. Note, that
if you do further development, compilation time is reduced since Ant is able
to detect which files have changed an to recompile them at need.

If you're compiling for JDK 1.1, then use the build11 scripts:

  ./build11.sh (unix)
  .\build11.bat (win32)

If something went wrong, go to the FAQ section below.

Also, you'll note that reusing a single JVM instance for each task, increases
tremendously the performance of the whole build system, compared to other
tools (i.e. make or shell scripts) where a new JVM is started for each task.


Build targets
=============

The build system is not only responsible for compiling JDOM into a jar file,
but is also responsible for creating the HTML documentation in the form of
javadocs.

These are the meaningful targets for this build file:

 - package [default] -> creates ./build/jdom.jar
 - compile -> compiles the source code
 - samples -> compiles example code
 - javadoc -> generates the API documentation in ./build/javadocs
 - clean -> restores the distribution to its original and clean state

For example, to build the samples, type

build samples
(Windows)

build.sh samples
(Unix)

To learn the details of what each target does, read the build.xml file.  It is
quite understandable.


Bug Reports
===========

Bug reports go to the jdom-interest list at jdom.org.  But *BEFORE YOU POST*
make sure you've tested against the LATEST code available from CVS (or the
daily snapshot).  Odds are good your bug has already been fixed.  If it hasn't
been fixed in the latest version, then when posting *BE SURE TO SAY* which
code version you tested against.  For example, "CVS from October 3rd".  Also
be sure to include enough information to reproduce the bug and full exception
stack traces.  You might also want to read the FAQ at http://jdom.org to find
out if your problem is not really a bug and just a common misunderstanding
about how XML or JDOM works.

