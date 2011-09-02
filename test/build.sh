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

LOCALCLASSPATH=${JAVA_HOME}/lib/tools.jar${PS}${JAVA_HOME}/lib/dev.jar${PS}../core/lib/ant.jar${PS}../core/lib/xml-apis.jar${PS}../core/lib/xerces.jar${PS}./lib/optional.jar${PS}./lib/junit.jar
ANT_HOME=../core/lib

echo Building with classpath $LOCALCLASSPATH${PS}$ADDITIONALCLASSPATH${PS}$CLASSPATH
echo

echo Starting Ant...
echo

$JAVA_HOME/bin/java -Dant.home=$ANT_HOME -classpath $LOCALCLASSPATH${PS}$ADDITIONALCLASSPATH${PS}$CLASSPATH org.apache.tools.ant.Main $*

