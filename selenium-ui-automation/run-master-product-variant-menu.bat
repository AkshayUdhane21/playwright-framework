@echo off
echo ========================================
echo Master Product Variant Test Menu
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
echo Choose your test option:
echo.
echo 1. Complete Automation (Navigate + All Actions)
echo 2. Create Form Test Only
echo 3. Run Both Tests
echo 4. Exit
echo.

set /p choice="Enter your choice (1-4): "

if "%choice%"=="1" goto complete
if "%choice%"=="2" goto createform
if "%choice%"=="3" goto both
if "%choice%"=="4" goto exit
goto invalid

:complete
echo.
echo ========================================
echo Running Complete Automation Test
echo ========================================
echo.
echo This will:
echo - Navigate to Master Product Variant Details page
echo - Perform ALL automation actions (Search, Edit, Status, Create)
echo - Keep browser open
echo.
%MAVEN_CMD% test -Dtest=MasterProductVariantFocusedTest#testMasterProductVariantCompleteAutomation
goto end

:createform
echo.
echo ========================================
echo Running Create Form Test
echo ========================================
echo.
echo This will:
echo - Navigate to Master Product Variant Details page
echo - Test CREATE FORM functionality only
echo - Keep browser open
echo.
%MAVEN_CMD% test -Dtest=MasterProductVariantFocusedTest#testMasterProductVariantCreateFormOnly
goto end

:both
echo.
echo ========================================
echo Running Both Tests
echo ========================================
echo.
echo Running Complete Automation Test first...
%MAVEN_CMD% test -Dtest=MasterProductVariantFocusedTest#testMasterProductVariantCompleteAutomation
echo.
echo Running Create Form Test...
%MAVEN_CMD% test -Dtest=MasterProductVariantFocusedTest#testMasterProductVariantCreateFormOnly
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
