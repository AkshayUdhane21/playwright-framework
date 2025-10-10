@echo off
echo ========================================
echo Comprehensive Workflow Test Runner
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
echo 1. Navigate through ALL Master pages first
echo 2. Return to Master Product Variant Details
echo 3. Perform complete automation
echo 4. Keep browser open throughout entire process
echo.
echo Starting comprehensive workflow test...
echo.

REM Run comprehensive workflow test
%MAVEN_CMD% test -Dtest=ComprehensiveWorkflowTest#testCompleteWorkflowWithoutBrowserRestart

echo.
echo ========================================
echo Comprehensive Workflow Test Completed
echo ========================================
echo.
echo The browser should still be open for inspection.
echo Check the following for results:
echo - Console output above
echo - target/surefire-reports/ for detailed reports
echo - screenshots/ folder for captured screenshots
echo - test-output/ folder for HTML reports
echo.
echo Press any key to continue...
pause
