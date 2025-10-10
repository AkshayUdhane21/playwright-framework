@echo off
echo Starting Selenium Tests with Version Check...

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
echo SELENIUM TESTS WITH VERSION CHECK
echo ========================================
echo.
echo This will:
echo 1. Check browser and driver versions
echo 2. Auto-download latest compatible drivers
echo 3. Run your Master Module tests
echo 4. Take screenshots for verification
echo.

REM Run driver version test first
echo Step 1: Checking driver versions...
mvn clean test -Denv=%ENV% -Dtest=DriverVersionTest

echo.
echo Step 2: Running Master Module tests...
mvn test -Denv=%ENV% -Dtest=SimpleMasterTest

echo.
echo ========================================
echo ALL TESTS COMPLETED
echo ========================================
echo.
echo Check the output above for any version issues.
echo Screenshots are saved in the screenshots/ directory.
echo.

pause







