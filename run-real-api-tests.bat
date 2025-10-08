@echo off
echo ========================================
echo Real API Test Execution
echo ========================================
echo.

echo Checking Java installation...
java -version
if %ERRORLEVEL% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 11 or higher
    pause
    exit /b 1
)

echo.
echo Checking Maven installation...
mvn -version
if %ERRORLEVEL% neq 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven 3.6 or higher
    pause
    exit /b 1
)

echo.
echo Checking real services status...
java -cp target/test-classes utils.ServiceStartupHelper
if %ERRORLEVEL% neq 0 (
    echo.
    echo WARNING: Some services are not ready
    echo Please ensure all microservices are running before proceeding
    echo.
    echo Required services:
    echo - Service Registry: http://localhost:8761
    echo - OPC UA Service: http://localhost:8081
    echo - Read Data Service: http://localhost:8082
    echo - Kafka Service: http://localhost:8083
    echo - Write Data Service: http://localhost:8084
    echo.
    set /p continue="Do you want to continue anyway? (y/n): "
    if /i not "%continue%"=="y" (
        echo Test execution cancelled
        pause
        exit /b 1
    )
)

echo.
echo Cleaning and compiling...
mvn clean compile test-compile
if %ERRORLEVEL% neq 0 (
    echo ERROR: Compilation failed
    pause
    exit /b 1
)

echo.
echo Running real API tests...
echo Test mode: REAL SERVICES
echo.
mvn test
if %ERRORLEVEL% neq 0 (
    echo.
    echo WARNING: Some tests failed
    echo Check the reports for details
) else (
    echo.
    echo All real API tests passed successfully!
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

pause