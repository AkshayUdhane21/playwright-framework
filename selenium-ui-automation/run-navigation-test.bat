@echo off
echo ========================================
echo Running Navigation Flow Test
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
echo Running Navigation Flow Test:
echo - Test proper navigation using dropdown menu
echo - Verify Master button and dropdown items
echo - Navigate to Master Product Variant Details
echo.

REM Run navigation flow test
%MAVEN_CMD% test -Dtest=MasterProductVariantTest#testNavigationFlowToMasterProductVariantDetails

echo.
echo ========================================
echo Navigation Flow Test execution completed
echo ========================================
echo.
echo Check the following for results:
echo - Console output above
echo - target/surefire-reports/ for detailed reports
echo - screenshots/ folder for captured screenshots
echo - test-output/ folder for HTML reports
echo.
pause
