@echo off
echo ========================================
echo Selenium Tests with Maven
echo ========================================
echo.

REM Set Maven home
set MAVEN_HOME=%~dp0..\apache-maven-3.9.6
set PATH=%MAVEN_HOME%\bin;%PATH%

REM Set JAVA_HOME explicitly
set JAVA_HOME=C:\Program Files\Java\jdk-17.0.8

echo Maven Home: %MAVEN_HOME%
echo Java Home: %JAVA_HOME%
echo.

REM Test Maven
echo Testing Maven installation...
"%MAVEN_HOME%\bin\mvn" -version
if %errorlevel% neq 0 (
    echo Maven test failed. Trying alternative approach...
    echo.
    
    REM Try running the tests using the existing compiled classes
    echo Attempting to run tests using existing compiled classes...
    echo.
    
    REM Check if target directory exists
    if exist target\test-classes (
        echo Found compiled test classes in target\test-classes
        echo.
        echo You can run the tests using TestNG directly:
        echo java -cp "target\test-classes;target\classes;lib\*" org.testng.TestNG testng.xml
        echo.
    ) else (
        echo No compiled classes found. Please compile first.
    )
    
    pause
    exit /b 1
)

echo.
echo Maven is working! Running tests...
echo.

REM Clean and compile
echo Cleaning and compiling...
"%MAVEN_HOME%\bin\mvn" clean compile -q

REM Run tests
echo Running tests...
"%MAVEN_HOME%\bin\mvn" test -Dtest=FormOpeningTest -Dmaven.test.failure.ignore=true

echo.
echo ========================================
echo Test Execution Complete
echo ========================================
echo.

pause
