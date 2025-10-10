@echo off
echo ========================================
echo Running New Automation Tests
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
echo Running specific new tests:
echo - Navigation Flow Test (NEW)
echo - Edit Button Functionality
echo - Status Button Functionality  
echo - Search Functionality
echo - Complete Workflow with All Actions
echo.

REM Run specific test methods including navigation flow test
%MAVEN_CMD% test -Dtest=MasterProductVariantTest#testNavigationFlowToMasterProductVariantDetails,MasterProductVariantTest#testEditButtonFunctionality,MasterProductVariantTest#testStatusButtonFunctionality,MasterProductVariantTest#testSearchFunctionality,MasterProductVariantTest#testCompleteWorkflowWithAllActions

echo.
echo ========================================
echo Test execution completed
echo ========================================
echo.
echo Check the following for results:
echo - Console output above
echo - target/surefire-reports/ for detailed reports
echo - screenshots/ folder for captured screenshots
echo - test-output/ folder for HTML reports
echo.
pause
