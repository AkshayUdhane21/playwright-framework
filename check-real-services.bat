@echo off
echo ========================================
echo Real Services Health Check
echo ========================================
echo.

echo Checking microservices status...
java -cp target/test-classes utils.ServiceStartupHelper

if %ERRORLEVEL% neq 0 (
    echo.
    echo ERROR: Some services are not ready!
    echo Please ensure all microservices are running:
    echo.
    echo Required services:
    echo - Service Registry: http://localhost:8761
    echo - OPC UA Service: http://localhost:8081
    echo - Read Data Service: http://localhost:8082
    echo - Kafka Service: http://localhost:8083
    echo - Write Data Service: http://localhost:8084
    echo.
    pause
    exit /b 1
) else (
    echo.
    echo All services are ready for real API testing!
    echo.
)

pause

