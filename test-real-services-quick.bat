@echo off
echo ========================================
echo Quick Real Services Test
echo ========================================
echo.

echo Step 1: Checking services...
call check-all-services.bat

echo.
echo Step 2: Running quick test...
echo.

mvn clean compile test-compile -q
if %ERRORLEVEL% neq 0 (
    echo ❌ Compilation failed
    pause
    exit /b 1
)

echo Running a single test to verify setup...
mvn test -Dtest=KafkaServiceTest#testKafkaServiceHealth -q

if %ERRORLEVEL% equ 0 (
    echo.
    echo ✅ Quick test passed! Your real server automation is working.
    echo You can now run the full test suite.
) else (
    echo.
    echo ❌ Quick test failed. Check the logs above for details.
)

echo.
pause





