#!/bin/sh

if [ "$JAVA_HOME" = "" ] ; then
  echo "ERROR: JAVA_HOME not found in your environment."
  echo
  echo "Please, set the JAVA_HOME variable in your environment to match the"
  echo "location of the Java Virtual Machine you want to use."
  exit 1
fi

ADDITIONALCLASSPATH=$JAVA_HOME/lib/classes.zip:$JAVA_HOME/lib/rt.jar:../jdom/lib/collections.jar
export ADDITIONALCLASSPATH

./build.sh $*

ADDITIONALCLASSPATH=
export ADDITIONALCLASSPATH

