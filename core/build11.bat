@echo off

if "%JAVA_HOME%" == "" goto error

set ADDITIONALCLASSPATH=.\lib\collections.jar;%JAVA_HOME%\lib\classes.zip

call build %1 %2 %3 %4 %5
goto end

:error

echo ERROR: JAVA_HOME not found in your environment.
echo Please, set the JAVA_HOME variable in your environment to match the
echo location of the Java Virtual Machine you want to use.

:end

set ADDITIONALCLASSPATH=

