@echo off

echo JDOM Build System
echo -------------------

if "%JAVA_HOME%" == "" goto error

set LOCALCLASSPATH=%JAVA_HOME%\lib\tools.jar;.\lib\xerces.jar;.\lib\ant.jar;.\lib\jaxp.jar;%ADDITIONALCLASSPATH%
set ANT_HOME=./lib

echo Building with classpath %LOCALCLASSPATH%

echo Starting Ant...

%JAVA_HOME%\bin\java.exe -Dant.home="%ANT_HOME%" -classpath "%LOCALCLASSPATH%" org.apache.tools.ant.Main %1 %2 %3 %4 %5

goto end

:error

echo ERROR: JAVA_HOME not found in your environment.
echo Please, set the JAVA_HOME variable in your environment to match the
echo location of the Java Virtual Machine you want to use.

:end

set LOCALCLASSPATH=

