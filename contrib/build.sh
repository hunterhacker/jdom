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

if [ `uname | grep -n CYGWIN` ]; then
  PS=";"
elif [ `uname | grep -n Windows` ]; then
  PS=";"
else
  PS=":"
fi

LOCALCLASSPATH=$JAVA_HOME/lib/tools.jar${PS}$JAVA_HOME/lib/dev.jar${PS}../jdom/lib/ant.jar${PS}../jdom/lib/xml-apis.jar${PS}../jdom/lib/xerces.jar
ANT_HOME=../jdom/lib

echo Building with classpath $LOCALCLASSPATH${PS}$ADDITIONALCLASSPATH
echo

echo Starting Ant...
echo

$JAVA_HOME/bin/java -Dant.home=$ANT_HOME -classpath $LOCALCLASSPATH${PS}$ADDITIONALCLASSPATH org.apache.tools.ant.Main $*
