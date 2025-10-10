@echo off
echo ========================================
echo Master Product Variant Focused Automation
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
echo 1. Navigate to Master Product Variant Details page ONLY
echo 2. Perform all automation actions on that single page
echo 3. Keep browser open for inspection
echo.
echo Starting focused Master Product Variant automation...
echo.

REM Run focused Master Product Variant test
%MAVEN_CMD% test -Dtest=MasterProductVariantFocusedTest#testMasterProductVariantCompleteAutomation

echo.
echo ========================================
echo Master Product Variant Automation Completed
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
