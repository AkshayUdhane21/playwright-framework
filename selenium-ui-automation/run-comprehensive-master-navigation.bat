@echo off
echo ========================================
echo Comprehensive Master Navigation Test
echo ========================================
echo.
echo This test will:
echo 1. Navigate through all Master pages
echo 2. Focus on Master Product Variant Details automation
echo 3. Test create, edit, search, and status functionality
echo.
echo Starting test execution...
echo.

cd /d "%~dp0"

REM Set Maven path
set MAVEN_HOME=%~dp0..\apache-maven-3.9.6
set PATH=%MAVEN_HOME%\bin;%PATH%

REM Run the comprehensive test
mvn test -Dtest=ComprehensiveMasterNavigationTest -Dsuite=comprehensive-master-navigation

echo.
echo ========================================
echo Test execution completed
echo ========================================
pause
