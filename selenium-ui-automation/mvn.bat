@echo off
REM Maven wrapper script for selenium project

REM Set Maven home to the local apache-maven installation
set MAVEN_HOME=%~dp0..\apache-maven-3.9.6
set PATH=%MAVEN_HOME%\bin;%PATH%

REM Set JAVA_HOME if not set
if "%JAVA_HOME%"=="" (
    for /f "tokens=*" %%i in ('where java 2^>nul') do (
        set JAVA_PATH=%%i
        goto :found_java
    )
    :found_java
    if defined JAVA_PATH (
        for %%i in ("%JAVA_PATH%") do set JAVA_HOME=%%~dpi
        set JAVA_HOME=%JAVA_HOME:~0,-1%
    )
)

REM Run Maven with all passed arguments
"%MAVEN_HOME%\bin\mvn" %*
