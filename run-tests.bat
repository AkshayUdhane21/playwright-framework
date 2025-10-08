@echo off
echo ========================================
echo Enhanced Playwright Framework
echo Microservices API Testing Suite
echo ========================================
echo.

REM Check if Java is available
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 11 or higher
    pause
    exit /b 1
)

REM Check if Maven is available
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Using local Maven from apache-maven-3.9.6\bin\mvn.cmd
    set MAVEN_CMD=apache-maven-3.9.6\bin\mvn.cmd
) else (
    set MAVEN_CMD=mvn
)

echo Checking microservices status...
echo.

REM Check service status
java -cp target/test-classes utils.ServiceStartupHelper
if %errorlevel% neq 0 (
    echo.
    echo WARNING: Some services are not ready
    echo Please ensure all microservices are running before proceeding
    echo.
    echo Required services:
    echo - Service Registry (Port 8761)
    echo - OPC UA Service (Port 8081)
    echo - Read Data Service (Port 8082)
    echo - Kafka Service (Port 8083)
    echo - Write Data Service (Port 8084)
    echo.
    set /p continue="Do you want to continue anyway? (y/n): "
    if /i not "%continue%"=="y" (
        echo Test execution cancelled
        pause
        exit /b 1
    )
)

echo.
echo Starting test execution...
echo.

REM Clean and compile
echo Cleaning and compiling...
call %MAVEN_CMD% clean compile test-compile
if %errorlevel% neq 0 (
    echo ERROR: Compilation failed
    pause
    exit /b 1
)

echo.
echo Running tests...
echo.

REM Run tests
call %MAVEN_CMD% test
if %errorlevel% neq 0 (
    echo.
    echo WARNING: Some tests failed
    echo Check the reports for details
) else (
    echo.
    echo All tests passed successfully!
)

echo.
echo ========================================
echo Test execution completed
echo ========================================
echo.
echo Reports generated:
echo - ExtentReports: test-output/Enhanced_Test_Report_*.html
echo - Allure Reports: target/site/allure-maven-plugin/
echo - TestNG Reports: test-output/
echo.

REM Open reports directory
set /p open="Open reports directory? (y/n): "
if /i "%open%"=="y" (
    start test-output
)

pause
