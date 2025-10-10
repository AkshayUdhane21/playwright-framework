@echo off
echo ========================================
echo Single Browser Navigation Test
echo ========================================
echo.

REM Check if Maven is available
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo Maven not found in PATH. Using local Maven...
    set MAVEN_CMD=.\mvn.bat
) else (
    echo Using system Maven...
    set MAVEN_CMD=mvn
)

echo.
echo This test will:
echo 1. Open ONE browser window
echo 2. Navigate through ALL Master pages first
echo 3. Then perform Master Product Variant automation
echo 4. Keep the SAME browser open throughout
echo.
echo NO NEW BROWSER WILL OPEN - Single session only!
echo.
echo Starting single browser navigation test...
echo.

REM Run single browser navigation test
%MAVEN_CMD% test -Dtest=SingleBrowserNavigationTest#testNavigateAllPagesThenAutomate

echo.
echo ========================================
echo Single Browser Navigation Test Completed
echo ========================================
echo.
echo The SAME browser window should still be open for inspection.
echo Check the following for results:
echo - Console output above
echo - target/surefire-reports/ for detailed reports
echo - screenshots/ folder for captured screenshots
echo - test-output/ folder for HTML reports
echo.
echo Press any key to continue...
pause
