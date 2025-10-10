@echo off
echo ========================================
echo Quick Master Product Variant Automation
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
echo 1. Navigate directly to Master Product Variant Details
echo 2. Perform all automation actions quickly
echo 3. Keep browser open for inspection
echo.
echo Starting quick automation test...
echo.

REM Run quick automation test
%MAVEN_CMD% test -Dtest=ComprehensiveWorkflowTest#testQuickMasterProductVariantAutomation

echo.
echo ========================================
echo Quick Automation Test Completed
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
