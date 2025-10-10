@echo off
echo ========================================
echo Maven Setup for Selenium Project
echo ========================================
echo.

REM Set Maven home to the local apache-maven installation
set MAVEN_HOME=%~dp0..\apache-maven-3.9.6
set PATH=%MAVEN_HOME%\bin;%PATH%

echo Maven Home: %MAVEN_HOME%
echo.

REM Check if Maven is working
echo Testing Maven installation...
"%MAVEN_HOME%\bin\mvn" -version
if %errorlevel% neq 0 (
    echo ERROR: Maven is not working properly
    echo Please check the Maven installation
    pause
    exit /b 1
)

echo.
echo Maven is working correctly!
echo.

REM Set JAVA_HOME if not set
if "%JAVA_HOME%"=="" (
    echo Setting JAVA_HOME...
    for /f "tokens=*" %%i in ('where java') do (
        set JAVA_PATH=%%i
        goto :found_java
    )
    :found_java
    if defined JAVA_PATH (
        for %%i in ("%JAVA_PATH%") do set JAVA_HOME=%%~dpi
        set JAVA_HOME=%JAVA_HOME:~0,-1%
        echo JAVA_HOME set to: %JAVA_HOME%
    ) else (
        echo WARNING: Could not automatically set JAVA_HOME
        echo Please set JAVA_HOME manually to your Java installation directory
    )
)

echo.
echo ========================================
echo Maven Setup Complete
echo ========================================
echo.
echo You can now run Maven commands using:
echo   mvn test
echo   mvn clean test
echo   mvn compile
echo.
echo Or use the provided batch files:
echo   run-tests.bat
echo   test-form-opening.bat
echo.

pause
