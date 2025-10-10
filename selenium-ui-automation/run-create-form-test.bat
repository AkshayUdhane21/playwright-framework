@echo off
echo ========================================
echo Master Product Variant Create Form Test
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
echo 1. Navigate to Master Product Variant Details page
echo 2. Test CREATE FORM functionality only
echo 3. Keep browser open for inspection
echo.
echo Starting create form test...
echo.

REM Run create form only test
%MAVEN_CMD% test -Dtest=MasterProductVariantFocusedTest#testMasterProductVariantCreateFormOnly

echo.
echo ========================================
echo Create Form Test Completed
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
