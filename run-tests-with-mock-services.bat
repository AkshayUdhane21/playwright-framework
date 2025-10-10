@echo off
echo ========================================
echo Playwright Framework - Mock Services Test Runner
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
)

echo.
echo Test execution completed!
pause

