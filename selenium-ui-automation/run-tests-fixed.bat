@echo off
echo ========================================
echo Selenium Tests - Fixed Maven Setup
echo ========================================
echo.

REM Set Maven home
set MAVEN_HOME=%~dp0..\apache-maven-3.9.6
set PATH=%MAVEN_HOME%\bin;%PATH%

REM Set JAVA_HOME with proper escaping
set "JAVA_HOME=C:\Program Files\Java\jdk-17.0.8"

echo Maven Home: %MAVEN_HOME%
echo Java Home: %JAVA_HOME%
echo.

REM Test Maven with proper path handling
echo Testing Maven...
"%MAVEN_HOME%\bin\mvn.cmd" -version
if %errorlevel% neq 0 (
    echo Maven test failed. Trying alternative approach...
    echo.
    
    REM Try with mvn instead of mvn.cmd
    "%MAVEN_HOME%\bin\mvn" -version
    if %errorlevel% neq 0 (
        echo.
        echo Maven is not working. Let's try to run the tests using existing compiled classes...
        echo.
        
        REM Check if we can run the tests directly
        if exist target\test-classes\tests\FormOpeningTest.class (
            echo Found FormOpeningTest class!
            echo.
            echo Since Maven is not working, you can:
            echo 1. Use your IDE to run the tests
            echo 2. Or fix the Maven JAVA_HOME issue
            echo 3. Or use the existing working test scripts
            echo.
            echo The form opening functionality has been improved with:
            echo - Multiple XPath strategies
            echo - JavaScript click fallback
            echo - Enhanced debugging
            echo - Better error handling
            echo.
        ) else (
            echo No compiled test classes found.
        )
        
        pause
        exit /b 1
    )
)

echo.
echo Maven is working! Running form opening tests...
echo.

REM Run the form opening test
"%MAVEN_HOME%\bin\mvn" test -Dtest=FormOpeningTest -Dmaven.test.failure.ignore=true

echo.
echo ========================================
echo Test Execution Complete
echo ========================================
echo.

pause
