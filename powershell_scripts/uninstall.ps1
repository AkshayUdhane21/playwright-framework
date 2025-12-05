Write-Host "Checking for Administrator privileges..." -ForegroundColor Yellow
if (-not ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)) {
    Write-Host "Administrator privileges required. Restarting script with elevated permissions..." -ForegroundColor Red
    Start-Process powershell.exe -Verb RunAs -ArgumentList "-NoProfile -ExecutionPolicy Bypass -File `"$PSCommandPath`""
    exit
}
Write-Host "Administrator privileges confirmed." -ForegroundColor Green

# Service name
$serviceName = "AtsYokogawaConnectionService"

Write-Host "Stopping service if running..." -ForegroundColor Yellow
try {
    if (Get-Service -Name $serviceName -ErrorAction SilentlyContinue) {
        Stop-Service -Name $serviceName -Force -ErrorAction SilentlyContinue
        Write-Host "Service stopped successfully." -ForegroundColor Green
    } else {
        Write-Host "Service not found or already stopped." -ForegroundColor Cyan
    }
} catch {
    Write-Host "WARNING: Could not stop the service. It may not exist or is already stopped." -ForegroundColor Red
}

Write-Host "Deleting service..." -ForegroundColor Yellow
try {
    sc.exe delete $serviceName
    Write-Host "Service deleted successfully." -ForegroundColor Green
} catch {
    Write-Host "ERROR: Failed to delete the service." -ForegroundColor Red
}

Write-Host "Removing registry keys..." -ForegroundColor Yellow
$serviceKey = "HKLM:\SYSTEM\CurrentControlSet\Services\$serviceName"
if (Test-Path $serviceKey) {
    Remove-Item -Path $serviceKey -Recurse -Force
    Write-Host "Registry keys removed successfully." -ForegroundColor Green
} else {
    Write-Host "Registry key not found." -ForegroundColor Cyan
}

$taskName = "AtsYokogawaServiceLogCleanup"

schtasks.exe /Delete /TN $taskName /F > $null 2>&1

if ($LASTEXITCODE -eq 0) {
    Write-Host "Scheduled task '$taskName' deleted successfully." -ForegroundColor Green
} else {
    Write-Warning "Scheduled task '$taskName' not found or could not be deleted. It may not exist."
}

Write-Host "Uninstall process completed." -ForegroundColor Green