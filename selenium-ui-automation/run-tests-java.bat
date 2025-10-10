@echo off
echo ========================================
echo Selenium Tests - Java Direct Execution
echo ========================================
echo.

REM Check if Java is available
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java and try again
    pause
    exit /b 1
)

echo Java is available!
echo.

REM Check if compiled classes exist
if not exist target\test-classes (
    echo ERROR: No compiled test classes found
    echo Please compile the project first using Maven or IDE
    echo.
    echo You can try:
    echo 1. Run setup-maven.bat first
    echo 2. Or compile using your IDE
    echo 3. Or use the existing run-simple-test.bat
    pause
    exit /b 1
)

echo Found compiled test classes!
echo.

REM Set classpath
set CLASSPATH=target\test-classes;target\classes;lib\*

REM Add Selenium and TestNG jars to classpath
for %%i in (lib\*.jar) do set CLASSPATH=%CLASSPATH%;%%i

echo Classpath: %CLASSPATH%
echo.

REM Run the form opening test
echo Running Form Opening Test...
echo.

java -cp "%CLASSPATH%" org.testng.TestNG -testclass tests.FormOpeningTest

echo.
echo ========================================
echo Test Execution Complete
echo ========================================
echo.

pause
