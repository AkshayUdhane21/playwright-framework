# PowerShell script to build and run all tests
$ErrorActionPreference = "Stop"

Write-Host "=== Building All Tests ===" -ForegroundColor Cyan

# Build all test targets
Write-Host "`nBuilding config_test..." -ForegroundColor Yellow
cmake --build build --config Debug --target config_test
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Failed to build config_test!" -ForegroundColor Red
    exit 1
}

Write-Host "Building security_test..." -ForegroundColor Yellow
cmake --build build --config Debug --target security_test
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Failed to build security_test!" -ForegroundColor Red
    exit 1
}

Write-Host "`n=== Running All Tests ===" -ForegroundColor Cyan

# Run config tests
Write-Host "`n[1/2] Running Config Tests..." -ForegroundColor Green
Write-Host "----------------------------------------" -ForegroundColor Gray
if (Test-Path "build\Debug\bin\config_test.exe") {
    & "build\Debug\bin\config_test.exe"
    $configExitCode = $LASTEXITCODE
} else {
    Write-Host "ERROR: config_test.exe not found!" -ForegroundColor Red
    $configExitCode = 1
}

Write-Host "`n----------------------------------------" -ForegroundColor Gray

# Run security tests
Write-Host "`n[2/2] Running Security Tests..." -ForegroundColor Green
Write-Host "----------------------------------------" -ForegroundColor Gray
if (Test-Path "build\Debug\bin\security_test.exe") {
    & "build\Debug\bin\security_test.exe"
    $securityExitCode = $LASTEXITCODE
} else {
    Write-Host "ERROR: security_test.exe not found!" -ForegroundColor Red
    $securityExitCode = 1
}

Write-Host "`n----------------------------------------" -ForegroundColor Gray

# Summary
Write-Host "`n=== Test Summary ===" -ForegroundColor Cyan
if ($configExitCode -eq 0) {
    Write-Host "Config Tests:    PASSED" -ForegroundColor Green
} else {
    Write-Host "Config Tests:    FAILED" -ForegroundColor Red
}

if ($securityExitCode -eq 0) {
    Write-Host "Security Tests: PASSED" -ForegroundColor Green
} else {
    Write-Host "Security Tests: FAILED" -ForegroundColor Red
}

$totalExitCode = $configExitCode + $securityExitCode
if ($totalExitCode -eq 0) {
    Write-Host "`nAll tests completed successfully!" -ForegroundColor Green
    exit 0
} else {
    Write-Host "`nSome tests failed!" -ForegroundColor Red
    exit 1
}



































