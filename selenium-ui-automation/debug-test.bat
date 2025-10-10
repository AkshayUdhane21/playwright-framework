@echo off
echo Starting Debug Test for Selenium UI Automation...

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
echo DEBUGGING SELENIUM UI AUTOMATION
echo ========================================
echo.
echo 1. Checking if application is running at http://localhost:5173/
echo 2. Running a single test to debug element location
echo.

REM Run a single test with debug output
echo Running debug test...
mvn clean test -Denv=%ENV% -Dtest=MasterProductVariantTest#testNavigateToMasterProductVariantDetails -X

echo.
echo ========================================
echo DEBUG COMPLETED
echo ========================================
echo.
echo Check the output above for any errors.
echo Screenshots are saved in the screenshots/ directory.
echo.

pause







