#!/bin/sh

echo
echo "Building..."
echo

if [ "$JAVA_HOME" = "" ] ; then
  echo "ERROR: JAVA_HOME not found in your environment."
  echo
  echo "Please, set the JAVA_HOME variable in your environment to match the"
  echo "location of the Java Virtual Machine you want to use."
  exit 1
fi

LOCALCLASSPATH=$JAVA_HOME/lib/tools.jar:$JAVA_HOME/lib/dev.jar:../jdom/lib/xerces.jar:../jdom/lib/xml-apis.jar:../jdom/lib/ant.jar:../jdom/build/classes:./lib/jakarta-regexp-1.1.jar:../jdom/lib/jaxen-jdom.jar:../jdom/lib/jaxen-core.jar:../jdom/lib/saxpath.jar:./build/classes
ANT_HOME=../jdom/lib

echo Building with classpath $LOCALCLASSPATH:$ADDITIONALCLASSPATH
echo

echo Starting Ant...
echo

$JAVA_HOME/bin/java -Dant.home=$ANT_HOME -classpath $LOCALCLASSPATH:$ADDITIONALCLASSPATH org.apache.tools.ant.Main $*
