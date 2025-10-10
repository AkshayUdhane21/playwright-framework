@echo off
echo Starting Simple Master Module Test...

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

REM Create directories
mkdir test-output
mkdir screenshots

echo.
echo ========================================
echo SIMPLE MASTER MODULE TEST
echo ========================================
echo.
echo This test will:
echo 1. Navigate to http://localhost:5173/
echo 2. Click Master button (with 2s delay)
echo 3. Click Master Product Variant Details (with 2s delay)
echo 4. Click Create button (with 2s delay)
echo 5. Fill form with your specified data
echo 6. Submit form (with 2s delay)
echo.
echo Make sure your application is running!
echo.

REM Run simple test
"%MAVEN_HOME%\bin\mvn" clean test -Denv=%ENV% -Dtest=SimpleMasterTest

echo.
echo ========================================
echo SIMPLE TEST COMPLETED
echo ========================================
echo.
echo Check the output above and screenshots in the screenshots/ directory.
echo.

pause

