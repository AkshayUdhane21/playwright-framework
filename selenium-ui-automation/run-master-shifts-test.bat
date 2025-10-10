@echo off
echo ========================================
echo Master Shifts Details Automation Test
echo ========================================
echo.

echo This test will:
echo 1. Navigate to Master Shifts Details page
echo 2. Click Create button
echo 3. Fill Shift Number (1)
echo 4. Fill Shift Name (shift 1)
echo 5. Fill Shift Description (Ak)
echo 6. Set Start Time (08:00)
echo 7. Set End Time (16:00)
echo 8. Click Create/Submit button
echo.

echo Starting test execution...

REM Set Maven home to the local apache-maven installation
set MAVEN_HOME=%~dp0..\apache-maven-3.9.6
set PATH=%MAVEN_HOME%\bin;%PATH%

REM Set JAVA_HOME if not set
if "%JAVA_HOME%"=="" (
    echo Setting JAVA_HOME...
    for /f "tokens=*" %%i in ('where java') do (
        set JAVA_PATH=%%i
        goto :found_java
    )
    :found_java
    if defined JAVA_PATH (
        for %%i in ("%JAVA_PATH%") do set JAVA_HOME=%%~dpi
        set JAVA_HOME=%JAVA_HOME:~0,-1%
        echo JAVA_HOME set to: %JAVA_HOME%
    ) else (
        echo WARNING: Could not automatically set JAVA_HOME
        echo Please set JAVA_HOME manually to your Java installation directory
        pause
        exit /b 1
    )
)

echo.
echo Running Master Shifts Details Automation Test...
echo.

REM Run the specific test method
"%MAVEN_HOME%\bin\mvn" test -Dtest=ComprehensiveMasterNavigationTest#testMasterShiftsDetailsAutomation -DsuiteXmlFile=testng.xml

if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo ✅ TEST COMPLETED SUCCESSFULLY!
    echo ========================================
    echo.
    echo Check the test results in:
    echo - test-output/ directory
    echo - target/surefire-reports/ directory
    echo.
) else (
    echo.
    echo ========================================
    echo ❌ TEST FAILED!
    echo ========================================
    echo.
    echo Check the error messages above for details.
    echo.
)

echo Press any key to exit...
pause > nul

