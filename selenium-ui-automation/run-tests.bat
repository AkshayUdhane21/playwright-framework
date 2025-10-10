@echo off
echo Starting Selenium UI Automation Tests...

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

REM Set environment
set ENV=local

REM Clean previous test results
if exist test-output rmdir /s /q test-output
if exist screenshots rmdir /s /q screenshots
if exist allure-results rmdir /s /q allure-results

REM Create directories
mkdir test-output
mkdir screenshots
mkdir allure-results

REM Run tests
echo Running tests with environment: %ENV%
"%MAVEN_HOME%\bin\mvn" clean test -Denv=%ENV% -DsuiteXmlFile=testng.xml

REM Generate Allure report
echo Generating Allure report...
call "%MAVEN_HOME%\bin\mvn" allure:report

REM Open report
echo Opening test report...
start test-output\UI_Automation_Report_*.html

echo Test execution completed!
pause

