@echo off

echo Building...

if "%JAVA_HOME%" == "" goto error

set LOCALCLASSPATH=%JAVA_HOME%\lib\tools.jar;..\jdom\lib\xerces.jar;..\jdom\lib\xml-apis.jar;..\jdom\lib\ant.jar;..\jdom\build\classes;.\lib\jakarta-regexp-1.1.jar;..\jdom\lib\jaxen-jdom.jar;..\jdom\lib\jaxen-core.jar;..\jdom\lib\saxpath.jar;.\build\classes
set ANT_HOME=../jdom/lib

echo Building with classpath %LOCALCLASSPATH%;%ADDITIONALCLASSPATH%

echo Starting Ant...

"%JAVA_HOME%\bin\java.exe" -Dant.home="%ANT_HOME%" -classpath "%LOCALCLASSPATH%;%ADDITIONALCLASSPATH%" org.apache.tools.ant.Main %1 %2 %3 %4 %5

goto end

:error

echo ERROR: JAVA_HOME not found in your environment.
echo Please, set the JAVA_HOME variable in your environment to match the
echo location of the Java Virtual Machine you want to use.

:end

set LOCALCLASSPATH=
