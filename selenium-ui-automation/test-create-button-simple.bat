@echo off
echo ========================================
echo Simple Create Button Test Runner
echo ========================================
echo.

REM Set Maven environment
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

REM Check if Java is available
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java and try again
    pause
    exit /b 1
)

REM Check if Maven is available
"%MAVEN_HOME%\bin\mvn" -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Maven is not working properly
    echo Maven Home: %MAVEN_HOME%
    echo Please check the Maven installation
    pause
    exit /b 1
)

echo Maven Home: %MAVEN_HOME%
echo Java Home: %JAVA_HOME%
echo.

echo Starting Simple Create Button Test...
echo.

REM Run the specific test class
"%MAVEN_HOME%\bin\mvn" test -Dtest=SimpleCreateButtonTest -Dmaven.test.failure.ignore=true

echo.
echo ========================================
echo Simple Create Button Test Completed
echo ========================================
echo.

pause






