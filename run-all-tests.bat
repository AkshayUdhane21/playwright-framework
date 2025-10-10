@echo off
echo ========================================
echo Microservices API Test Suite Runner
echo ========================================

REM Set environment variables
set JAVA_HOME=C:\Program Files\Java\jdk-11
set MAVEN_OPTS=-Xmx1024m -XX:MaxPermSize=256m
set TEST_MODE=mock

echo Java Version:
java -version

echo Maven Version:
mvn -v

echo ========================================
echo Cleaning previous test results...
echo ========================================

if exist target rmdir /s /q target
if exist test-output rmdir /s /q test-output
if exist allure-results rmdir /s /q allure-results

echo ========================================
echo Compiling project...
echo ========================================

mvn clean compile test-compile -DskipTests
if %ERRORLEVEL% neq 0 (
    echo ❌ Compilation failed!
    exit /b 1
)

echo ========================================
echo Running tests with Mock Services...
echo ========================================

mvn test -Dtest.mode=mock -Dmock.services.enabled=true -Dtest.parallel.enabled=true -Dtest.parallel.threads=3
set TEST_RESULT=%ERRORLEVEL%

echo ========================================
echo Test Results Summary
echo ========================================

if %TEST_RESULT% equ 0 (
    echo ✅ All tests passed successfully!
    echo.
    echo Test reports generated in:
    echo - test-output/Enhanced_Test_Report_*.html
    echo - allure-results/ (if Allure is enabled)
    echo.
    echo You can open the HTML report in your browser to view detailed results.
) else (
    echo ❌ Some tests failed!
    echo.
    echo Check the console output above for details.
    echo Test reports are still generated in test-output/ directory.
)

echo ========================================
echo Test execution completed!
echo ========================================

pause

