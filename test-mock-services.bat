@echo off
echo ========================================
echo Testing Mock Services Setup
echo ========================================
echo.

echo Step 1: Compiling project...
call mvn clean compile test-compile -q
if %ERRORLEVEL% neq 0 (
    echo ❌ Compilation failed
    pause
    exit /b 1
)

echo ✅ Compilation successful!

echo.
echo Step 2: Running a single test to verify mock services...
echo.

call mvn test -Dtest.mode=mock -Dmock.services.enabled=true -Dtest=WriteDataServiceTest#testWriteNodeWithString -q

if %ERRORLEVEL% equ 0 (
    echo.
    echo ✅ Mock services test passed! Your setup is working correctly.
    echo.
    echo Step 3: Running all tests with mock services...
    echo.
    
    call mvn test -Dtest.mode=mock -Dmock.services.enabled=true
    
    if %ERRORLEVEL% equ 0 (
        echo.
        echo ========================================
        echo ✅ ALL TESTS PASSED!
        echo ========================================
        echo.
        echo Your Playwright framework is now working correctly with mock services.
        echo You can run the Jenkins pipeline and it should pass all tests.
    ) else (
        echo.
        echo ========================================
        echo ❌ SOME TESTS FAILED!
        echo ========================================
        echo.
        echo Check the test reports for details.
    )
) else (
    echo.
    echo ❌ Mock services test failed. Check the logs above for details.
)

echo.
pause

