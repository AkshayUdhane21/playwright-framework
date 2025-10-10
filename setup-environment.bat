@echo off
echo ========================================
echo Microservices API Test Suite Setup
echo ========================================
echo.
echo This script will help you set up the environment for running tests.
echo.

REM Check if Java is installed
echo Checking Java installation...
java -version >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ❌ Java is not installed or not in PATH
    echo.
    echo Please install Java 11 or higher:
    echo 1. Download from: https://adoptium.net/
    echo 2. Install Java 11 or higher
    echo 3. Add JAVA_HOME environment variable
    echo 4. Add %JAVA_HOME%\bin to PATH
    echo.
    echo After installation, run this script again.
    pause
    exit /b 1
) else (
    echo ✅ Java is installed
    java -version
)

echo.
echo Checking Maven installation...
mvn -version >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ❌ Maven is not installed or not in PATH
    echo.
    echo Using local Maven installation...
    set MAVEN_HOME=%~dp0apache-maven-3.9.6
    set PATH=%MAVEN_HOME%\bin;%PATH%
    
    echo Maven version:
    mvn -version
) else (
    echo ✅ Maven is installed
    mvn -version
)

echo.
echo ========================================
echo Environment Setup Complete
echo ========================================
echo.
echo You can now run tests using:
echo 1. run-all-tests.bat - Run all tests with mock services
echo 2. mvn test - Run tests with Maven
echo 3. Jenkins pipeline - For CI/CD
echo.
echo Test configuration:
echo - Mock services are enabled by default
echo - All tests will run against mock endpoints
echo - Test reports will be generated in test-output/
echo.
pause

