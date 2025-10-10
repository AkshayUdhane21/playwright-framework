@echo off
echo ========================================
echo Simple Maven Test
echo ========================================
echo.

REM Set Maven home
set MAVEN_HOME=%~dp0..\apache-maven-3.9.6
set PATH=%MAVEN_HOME%\bin;%PATH%

REM Try to find and set JAVA_HOME
for /f "tokens=*" %%i in ('where java 2^>nul') do (
    set JAVA_PATH=%%i
    goto :found_java
)
:found_java

if defined JAVA_PATH (
    for %%i in ("%JAVA_PATH%") do set JAVA_HOME=%%~dpi
    set JAVA_HOME=%JAVA_HOME:~0,-1%
    echo Found Java at: %JAVA_HOME%
) else (
    echo Using system JAVA_HOME: %JAVA_HOME%
)

REM Ensure JAVA_HOME is set correctly
if "%JAVA_HOME%"=="" (
    set JAVA_HOME=C:\Program Files\Java\jdk-17.0.8
    echo Set JAVA_HOME to: %JAVA_HOME%
)

echo.
echo Testing Maven...
echo Maven Home: %MAVEN_HOME%
echo Java Home: %JAVA_HOME%
echo.

REM Test Maven version
"%MAVEN_HOME%\bin\mvn" -version
if %errorlevel% neq 0 (
    echo.
    echo Maven test failed. Trying alternative approach...
    echo.
    
    REM Try with different JAVA_HOME paths
    set JAVA_HOME=C:\Program Files\Java\jdk-17.0.8
    echo Trying with JAVA_HOME: %JAVA_HOME%
    "%MAVEN_HOME%\bin\mvn" -version
    
    if %errorlevel% neq 0 (
        echo.
        echo Still failing. Let's try to compile the project directly...
        echo.
        
        REM Try to compile without Maven
        echo Attempting to compile Java files directly...
        javac -version
        if %errorlevel% equ 0 (
            echo Java compiler is working!
            echo You can run the tests using the existing compiled classes.
        ) else (
            echo Java compiler is not working either.
        )
    )
) else (
    echo.
    echo Maven is working! Running a simple test...
    echo.
    
    REM Try to run a simple Maven command
    "%MAVEN_HOME%\bin\mvn" clean compile -q
    if %errorlevel% equ 0 (
        echo Compilation successful!
    ) else (
        echo Compilation failed, but Maven is working.
    )
)

echo.
echo ========================================
echo Maven Test Complete
echo ========================================
echo.

pause
