@echo off
echo ========================================
echo Complete Microservices Health Check
echo ========================================
echo.

echo Checking all required microservices...
echo.

echo [1/5] Service Registry (Port 8761)...
curl -s http://localhost:8761/actuator/health >nul 2>&1
if %ERRORLEVEL% equ 0 (
    echo ✅ Service Registry is UP
) else (
    echo ❌ Service Registry is DOWN
)

echo.
echo [2/5] OPC UA Service (Port 8081)...
curl -s http://localhost:8081/actuator/health >nul 2>&1
if %ERRORLEVEL% equ 0 (
    echo ✅ OPC UA Service is UP
) else (
    echo ❌ OPC UA Service is DOWN - THIS IS THE PROBLEM!
)

echo.
echo [3/5] Read Data Service (Port 8082)...
curl -s http://localhost:8082/actuator/health >nul 2>&1
if %ERRORLEVEL% equ 0 (
    echo ✅ Read Data Service is UP
) else (
    echo ❌ Read Data Service is DOWN
)

echo.
echo [4/5] Kafka Service (Port 8083)...
curl -s http://localhost:8083/actuator/health >nul 2>&1
if %ERRORLEVEL% equ 0 (
    echo ✅ Kafka Service is UP
) else (
    echo ❌ Kafka Service is DOWN
)

echo.
echo [5/5] Write Data Service (Port 8084)...
curl -s http://localhost:8084/actuator/health >nul 2>&1
if %ERRORLEVEL% equ 0 (
    echo ✅ Write Data Service is UP
) else (
    echo ❌ Write Data Service is DOWN
)

echo.
echo ========================================
echo Service Status Summary
echo ========================================
echo.

echo Required for Real API Testing:
echo - Service Registry: http://localhost:8761
echo - OPC UA Service: http://localhost:8081  [MISSING - START THIS SERVICE]
echo - Read Data Service: http://localhost:8082
echo - Kafka Service: http://localhost:8083
echo - Write Data Service: http://localhost:8084
echo.

echo Next Steps:
echo 1. Start your OPC UA microservice on port 8081
echo 2. Run this script again to verify all services are up
echo 3. Then run your real API tests
echo.

pause

