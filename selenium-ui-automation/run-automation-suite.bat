@echo off
echo ========================================
echo Master Product Variant Automation Suite
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
echo Choose your automation option:
echo.
echo 1. Comprehensive Workflow (Navigate all pages first, then automate)
echo 2. Quick Automation (Direct Master Product Variant automation)
echo 3. Run Both Tests
echo 4. Exit
echo.

set /p choice="Enter your choice (1-4): "

if "%choice%"=="1" goto comprehensive
if "%choice%"=="2" goto quick
if "%choice%"=="3" goto both
if "%choice%"=="4" goto exit
goto invalid

:comprehensive
echo.
echo ========================================
echo Running Comprehensive Workflow Test
echo ========================================
echo.
echo This will:
echo - Navigate through ALL Master pages first
echo - Return to Master Product Variant Details
echo - Perform complete automation
echo - Keep browser open throughout
echo.
%MAVEN_CMD% test -Dtest=ComprehensiveWorkflowTest#testCompleteWorkflowWithoutBrowserRestart
goto end

:quick
echo.
echo ========================================
echo Running Quick Automation Test
echo ========================================
echo.
echo This will:
echo - Navigate directly to Master Product Variant Details
echo - Perform all automation actions quickly
echo - Keep browser open for inspection
echo.
%MAVEN_CMD% test -Dtest=ComprehensiveWorkflowTest#testQuickMasterProductVariantAutomation
goto end

:both
echo.
echo ========================================
echo Running Both Tests
echo ========================================
echo.
echo Running Comprehensive Workflow Test first...
%MAVEN_CMD% test -Dtest=ComprehensiveWorkflowTest#testCompleteWorkflowWithoutBrowserRestart
echo.
echo Running Quick Automation Test...
%MAVEN_CMD% test -Dtest=ComprehensiveWorkflowTest#testQuickMasterProductVariantAutomation
goto end

:invalid
echo.
echo Invalid choice. Please run the script again.
goto exit

:end
echo.
echo ========================================
echo Test(s) Completed
echo ========================================
echo.
echo The browser should still be open for inspection.
echo Check the following for results:
echo - Console output above
echo - target/surefire-reports/ for detailed reports
echo - screenshots/ folder for captured screenshots
echo - test-output/ folder for HTML reports
echo.

:exit
echo Press any key to exit...
pause
