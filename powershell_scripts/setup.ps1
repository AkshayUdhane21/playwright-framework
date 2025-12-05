# Check for Administrator privileges
Write-Host "Checking for Administrator privileges..." -ForegroundColor Yellow
if (-not ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)) {
    Write-Host "Administrator privileges required. Restarting script with elevated permissions..." -ForegroundColor Red
    Start-Process powershell.exe -Verb RunAs -ArgumentList "-NoProfile -ExecutionPolicy Bypass -File `"$PSCommandPath`""
    exit
}
Write-Host "Administrator privileges confirmed." -ForegroundColor Green

$serviceName = "AtsYokogawaConnectionService"

# Create the service
Write-Host "Creating Windows Service: $serviceName..." -ForegroundColor Yellow
sc.exe create $serviceName binPath= "`"$($PWD.Path)\bin\$serviceName.exe`"" start= auto
Write-Host "Service created successfully." -ForegroundColor Green

# Add service description
Write-Host "Adding service description..." -ForegroundColor Yellow
sc.exe description $serviceName "Toggles Yokogawa Connection Healthy tag based off API response"
Write-Host "Service description added." -ForegroundColor Green

# Registry key for the service
$serviceKey = "HKLM:\SYSTEM\CurrentControlSet\Services\$serviceName"

# Environment variables using $PWD for dynamic paths
Write-Host "Preparing environment variables..." -ForegroundColor Yellow
$envVars = @(
    "YOKOGAWA_LOG_PATH=$($PWD.Path)\logs\yokogawa_connection_logs.log"
    "YOKOGAWA_CONNECTION_CONFIG_PATH=$($PWD.Path)\config.json"
)
Write-Host "Environment variables prepared:" -ForegroundColor Cyan
$envVars | ForEach-Object { Write-Host "  $_" -ForegroundColor White }

# Apply environment variables to the service
Write-Host "Applying environment variables to the service registry..." -ForegroundColor Yellow
Set-ItemProperty -Path $serviceKey -Name 'Environment' -Type MultiString -Value $envVars -Force
Write-Host "Environment variables successfully added." -ForegroundColor Green

# Restart the service to apply changes
Write-Host "Validating service executable..." -ForegroundColor Yellow
if (-not (Test-Path "$PWD\bin\$serviceName.exe")) {
    Write-Host "ERROR: Service executable not found at $PWD\bin\$serviceName.exe" -ForegroundColor Red
    exit 1
}
Write-Host "Executable found. Proceeding..." -ForegroundColor Green

Write-Host "Attempting to restart the service..." -ForegroundColor Yellow
try {
    Restart-Service $serviceName -ErrorAction Stop
    Write-Host "Service restarted successfully." -ForegroundColor Green
} catch {
    Write-Host "WARNING: Service could not be started. This usually means the executable is not a valid Windows Service." -ForegroundColor Red
    Write-Host "TIP: Use NSSM or Task Scheduler if you need to run a normal EXE as a service." -ForegroundColor Cyan
}

$logCleanupScheduledTask = ".\log_cleanup.ps1"
& $logCleanupScheduledTask