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

LOCALCLASSPATH=$JAVA_HOME/lib/tools.jar:./lib/junit.jar:../jdom/lib/xerces.jar:../jdom/lib/ant.jar:../jdom/build/classes:$JAVA_HOME/lib/dev.jar:$ADDITIONALCLASSPATH
ANT_HOME=./lib

echo Building with classpath $CLASSPATH:$LOCALCLASSPATH:$ADDITIONALCLASSPATH
echo

echo Starting Ant...
echo

$JAVA_HOME/bin/java -Dant.home=$ANT_HOME -classpath $LOCALCLASSPATH:$CLASSPATH org.apache.tools.ant.Main $*
