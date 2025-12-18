# Automated Deployment Script for AtsYokogawaConnectionService
# This script handles deployment of the service to a target environment
# Usage: .\deploy.ps1 -DeployPath "C:\Services\Yokogawa" -Environment "dev"

param(
    [Parameter(Mandatory=$true)]
    [string]$DeployPath,
    
    [Parameter(Mandatory=$false)]
    [string]$Environment = "dev",
    
    [Parameter(Mandatory=$false)]
    [string]$ServiceName = "AtsYokogawaConnectionService",
    
    [Parameter(Mandatory=$false)]
    [string]$BackupPath = "",
    
    [Parameter(Mandatory=$false)]
    [string]$SourceBinPath = "",
    
    [Parameter(Mandatory=$false)]
    [string]$SourceConfigPath = "",
    
    [Parameter(Mandatory=$false)]
    [switch]$SkipBackup,
    
    [Parameter(Mandatory=$false)]
    [switch]$SkipServiceRestart
)

# Check for Administrator privileges
Write-Host "Checking for Administrator privileges..." -ForegroundColor Yellow
if (-not ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)) {
    Write-Host "ERROR: Administrator privileges required for deployment!" -ForegroundColor Red
    Write-Host "Please run this script as Administrator." -ForegroundColor Red
    exit 1
}
Write-Host "Administrator privileges confirmed." -ForegroundColor Green

# Set default backup path if not provided
if ([string]::IsNullOrEmpty($BackupPath)) {
    $BackupPath = Join-Path $DeployPath "backups"
}

# Set default source paths if not provided (assumes running from build directory)
if ([string]::IsNullOrEmpty($SourceBinPath)) {
    $SourceBinPath = Join-Path $PSScriptRoot "..\build\Release\bin"
    if (-not (Test-Path $SourceBinPath)) {
        $SourceBinPath = Join-Path $PSScriptRoot "..\build\Debug\bin"
    }
}

if ([string]::IsNullOrEmpty($SourceConfigPath)) {
    $SourceConfigPath = Join-Path $PSScriptRoot "..\config.json"
}

# Validate source paths
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "Deployment Configuration" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "Deploy Path: $DeployPath" -ForegroundColor White
Write-Host "Environment: $Environment" -ForegroundColor White
Write-Host "Service Name: $ServiceName" -ForegroundColor White
Write-Host "Source Bin Path: $SourceBinPath" -ForegroundColor White
Write-Host "Source Config Path: $SourceConfigPath" -ForegroundColor White
Write-Host "Backup Path: $BackupPath" -ForegroundColor White
Write-Host "==========================================" -ForegroundColor Cyan

# Validate source executable exists
$serviceExe = Join-Path $SourceBinPath "$ServiceName.exe"
if (-not (Test-Path $serviceExe)) {
    Write-Host "ERROR: Service executable not found at: $serviceExe" -ForegroundColor Red
    exit 1
}
Write-Host "✓ Service executable found: $serviceExe" -ForegroundColor Green

# Validate config file exists
if (-not (Test-Path $SourceConfigPath)) {
    Write-Host "WARNING: Config file not found at: $SourceConfigPath" -ForegroundColor Yellow
    Write-Host "Deployment will continue, but service may not work without config." -ForegroundColor Yellow
}

# Create deployment directory structure
Write-Host "`nCreating deployment directory structure..." -ForegroundColor Yellow
$deployBinPath = Join-Path $DeployPath "bin"
$deployLogsPath = Join-Path $DeployPath "logs"
$deployConfigPath = Join-Path $DeployPath "config"

@($DeployPath, $deployBinPath, $deployLogsPath, $deployConfigPath) | ForEach-Object {
    if (-not (Test-Path $_)) {
        New-Item -ItemType Directory -Path $_ -Force | Out-Null
        Write-Host "  Created: $_" -ForegroundColor Green
    } else {
        Write-Host "  Exists: $_" -ForegroundColor Cyan
    }
}

# Backup existing deployment (if service exists)
if (-not $SkipBackup) {
    Write-Host "`nCreating backup of current deployment..." -ForegroundColor Yellow
    
    if (-not (Test-Path $BackupPath)) {
        New-Item -ItemType Directory -Path $BackupPath -Force | Out-Null
    }
    
    $backupTimestamp = Get-Date -Format "yyyyMMdd_HHmmss"
    $backupDir = Join-Path $BackupPath "backup_$backupTimestamp"
    
    if (Test-Path $deployBinPath) {
        if ((Get-ChildItem $deployBinPath -File).Count -gt 0) {
            New-Item -ItemType Directory -Path $backupDir -Force | Out-Null
            Copy-Item -Path $deployBinPath -Destination (Join-Path $backupDir "bin") -Recurse -Force
            if (Test-Path $deployConfigPath) {
                Copy-Item -Path $deployConfigPath -Destination (Join-Path $backupDir "config") -Recurse -Force
            }
            Write-Host "  Backup created: $backupDir" -ForegroundColor Green
        } else {
            Write-Host "  No existing deployment to backup." -ForegroundColor Cyan
        }
    } else {
        Write-Host "  No existing deployment to backup." -ForegroundColor Cyan
    }
}

# Stop service if running
if (-not $SkipServiceRestart) {
    Write-Host "`nStopping service if running..." -ForegroundColor Yellow
    try {
        $service = Get-Service -Name $ServiceName -ErrorAction SilentlyContinue
        if ($service -and $service.Status -eq 'Running') {
            Stop-Service -Name $ServiceName -Force -ErrorAction Stop
            Start-Sleep -Seconds 2
            Write-Host "  Service stopped successfully." -ForegroundColor Green
        } else {
            Write-Host "  Service not running or does not exist." -ForegroundColor Cyan
        }
    } catch {
        Write-Host "  Service not found or already stopped." -ForegroundColor Cyan
    }
}

# Copy new files
Write-Host "`nCopying new deployment files..." -ForegroundColor Yellow

# Copy service executable and dependencies
Write-Host "  Copying service executable and dependencies..." -ForegroundColor Cyan
Get-ChildItem -Path $SourceBinPath -File | ForEach-Object {
    $destFile = Join-Path $deployBinPath $_.Name
    Copy-Item -Path $_.FullName -Destination $destFile -Force
    Write-Host "    Copied: $($_.Name)" -ForegroundColor Green
}

# Copy config file
if (Test-Path $SourceConfigPath) {
    Write-Host "  Copying configuration file..." -ForegroundColor Cyan
    $configDest = Join-Path $deployConfigPath "config.json"
    Copy-Item -Path $SourceConfigPath -Destination $configDest -Force
    Write-Host "    Copied: config.json" -ForegroundColor Green
} else {
    Write-Host "  WARNING: Config file not copied (source not found)" -ForegroundColor Yellow
}

# Update or create service
Write-Host "`nUpdating service configuration..." -ForegroundColor Yellow

$serviceExePath = Join-Path $deployBinPath "$ServiceName.exe"
$serviceKey = "HKLM:\SYSTEM\CurrentControlSet\Services\$ServiceName"

# Check if service exists
$serviceExists = Get-Service -Name $ServiceName -ErrorAction SilentlyContinue

if ($serviceExists) {
    # Update existing service
    Write-Host "  Updating existing service..." -ForegroundColor Cyan
    sc.exe config $ServiceName binPath= "`"$serviceExePath`"" | Out-Null
    Write-Host "  Service path updated." -ForegroundColor Green
} else {
    # Create new service
    Write-Host "  Creating new service..." -ForegroundColor Cyan
    sc.exe create $ServiceName binPath= "`"$serviceExePath`"" start= auto | Out-Null
    sc.exe description $ServiceName "Toggles Yokogawa Connection Healthy tag based off API response" | Out-Null
    Write-Host "  Service created successfully." -ForegroundColor Green
}

# Set environment variables
Write-Host "  Setting environment variables..." -ForegroundColor Cyan
$envVars = @(
    "YOKOGAWA_LOG_PATH=$deployLogsPath\yokogawa_connection_logs.log",
    "YOKOGAWA_CONNECTION_CONFIG_PATH=$deployConfigPath\config.json"
)

Set-ItemProperty -Path $serviceKey -Name 'Environment' -Type MultiString -Value $envVars -Force
Write-Host "  Environment variables set." -ForegroundColor Green

# Start service
if (-not $SkipServiceRestart) {
    Write-Host "`nStarting service..." -ForegroundColor Yellow
    try {
        Start-Service -Name $ServiceName -ErrorAction Stop
        Start-Sleep -Seconds 3
        
        $service = Get-Service -Name $ServiceName
        if ($service.Status -eq 'Running') {
            Write-Host "  ✓ Service started successfully!" -ForegroundColor Green
            Write-Host "  Service Status: $($service.Status)" -ForegroundColor Green
        } else {
            Write-Host "  WARNING: Service started but status is: $($service.Status)" -ForegroundColor Yellow
        }
    } catch {
        Write-Host "  ERROR: Failed to start service: $($_.Exception.Message)" -ForegroundColor Red
        Write-Host "  Please check service logs and configuration." -ForegroundColor Yellow
        exit 1
    }
}

# Verify deployment
Write-Host "`n==========================================" -ForegroundColor Cyan
Write-Host "Deployment Verification" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan

$service = Get-Service -Name $ServiceName -ErrorAction SilentlyContinue
if ($service) {
    Write-Host "Service Status: $($service.Status)" -ForegroundColor $(if ($service.Status -eq 'Running') { 'Green' } else { 'Yellow' })
    Write-Host "Service Path: $serviceExePath" -ForegroundColor White
    Write-Host "Config Path: $deployConfigPath\config.json" -ForegroundColor White
    Write-Host "Log Path: $deployLogsPath\yokogawa_connection_logs.log" -ForegroundColor White
} else {
    Write-Host "WARNING: Service not found after deployment!" -ForegroundColor Yellow
}

if (Test-Path $serviceExePath) {
    $fileInfo = Get-Item $serviceExePath
    Write-Host "`nDeployed Executable:" -ForegroundColor Cyan
    Write-Host "  File: $($fileInfo.Name)" -ForegroundColor White
    Write-Host "  Size: $([math]::Round($fileInfo.Length / 1MB, 2)) MB" -ForegroundColor White
    Write-Host "  Modified: $($fileInfo.LastWriteTime)" -ForegroundColor White
    Write-Host "  ✓ Deployment completed successfully!" -ForegroundColor Green
} else {
    Write-Host "  ✗ ERROR: Deployed executable not found!" -ForegroundColor Red
    exit 1
}

Write-Host "`n==========================================" -ForegroundColor Cyan
Write-Host "Deployment Summary" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "Environment: $Environment" -ForegroundColor White
Write-Host "Deploy Path: $DeployPath" -ForegroundColor White
Write-Host "Service: $ServiceName" -ForegroundColor White
Write-Host "Status: $($service.Status)" -ForegroundColor $(if ($service.Status -eq 'Running') { 'Green' } else { 'Yellow' })
Write-Host "==========================================" -ForegroundColor Cyan












