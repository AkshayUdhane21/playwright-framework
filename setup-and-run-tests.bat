@echo off
echo ========================================
echo Playwright Framework - Complete Setup
echo ========================================
echo.

echo Checking system requirements...

REM Check for Java
java -version >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ❌ Java is not installed or not in PATH
    echo.
    echo Please install Java 11 or higher and add it to your PATH
    echo Download from: https://adoptium.net/
    echo.
    echo After installing Java, run this script again.
    pause
    exit /b 1
) else (
    echo ✅ Java is available
)

REM Check for Maven
mvn -version >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ❌ Maven is not installed or not in PATH
    echo.
    echo Using local Maven installation...
    set MAVEN_HOME=%~dp0apache-maven-3.9.6
    set PATH=%MAVEN_HOME%\bin;%PATH%
    
    REM Verify Maven is now available
    mvn -version >nul 2>&1
    if %ERRORLEVEL% neq 0 (
        echo ❌ Local Maven installation failed
        echo Please install Maven and add it to your PATH
        pause
        exit /b 1
    ) else (
        echo ✅ Maven is now available (local installation)
    )
) else (
    echo ✅ Maven is available
)

echo.
echo ========================================
echo System Requirements Check Complete
echo ========================================
echo.

echo Starting mock services and running tests...
echo.

REM Set test mode to mock
echo Setting test mode to mock...
set TEST_MODE=mock

REM Clean previous builds
echo Cleaning previous builds...
call mvn clean -q

REM Compile the project
echo Compiling project...
call mvn compile test-compile -q
if %ERRORLEVEL% neq 0 (
    echo ❌ Compilation failed!
    echo.
    echo Common issues:
    echo 1. Java version compatibility (need Java 11+)
    echo 2. Maven configuration issues
    echo 3. Missing dependencies
    echo.
    pause
    exit /b 1
)

echo ✅ Compilation successful!

REM Run tests with mock services
echo.
echo Running tests with mock services...
echo ========================================

call mvn test -Dtest.mode=mock -Dmock.services.enabled=true

REM Check test results
if %ERRORLEVEL% equ 0 (
    echo.
    echo ========================================
    echo ✅ ALL TESTS PASSED!
    echo ========================================
    echo.
    echo Test reports generated:
    echo - ExtentReports: target/surefire-reports/
    echo - Allure Reports: allure-results/
    echo.
    echo Mock services successfully simulated all microservices:
    echo - Service Registry (Port 8761)
    echo - OPC UA Service (Port 8081)
    echo - Read Data Service (Port 8082)
    echo - Kafka Service (Port 8083)
    echo - Write Data Service (Port 8084)
    echo.
) else (
    echo.
    echo ========================================
    echo ❌ SOME TESTS FAILED!
    echo ========================================
    echo.
    echo Check the test reports for details:
    echo - ExtentReports: target/surefire-reports/
    echo - Allure Reports: allure-results/
    echo.
    echo Troubleshooting:
    echo 1. Check if all mock services started correctly
    echo 2. Verify test configuration in test-config.properties
    echo 3. Review test logs for specific error messages
    echo.
)

echo.
echo Test execution completed!
echo.
echo For Jenkins pipeline, use:
echo mvn test -Dtest.mode=mock -Dmock.services.enabled=true
echo.
pause

