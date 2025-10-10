@echo off
echo Starting Debug Test...

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
echo RUNNING DEBUG TEST
echo ========================================
echo.
echo This will help us understand what elements are available on the page.
echo Make sure your application is running at http://localhost:5173/
echo.

REM Run debug test
mvn clean test -Denv=%ENV% -Dtest=DebugTest

echo.
echo ========================================
echo DEBUG TEST COMPLETED
echo ========================================
echo.
echo Check the output above and screenshots in the screenshots/ directory.
echo.

pause







