if (-not ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)) {
    Write-Host "Administrator privileges required. Restarting script with elevated permissions..." -ForegroundColor Red
    Start-Process powershell.exe -Verb RunAs -ArgumentList "-NoProfile -ExecutionPolicy Bypass -File `"$PSCommandPath`""
    exit
}
Write-Host "Administrator privileges confirmed." -ForegroundColor Green

function Test-FileLocked {
    param([string]$Path)
    try {
        $stream = [System.IO.File]::Open($Path, 'Open', 'ReadWrite', 'None')
        if ($stream) { $stream.Close() }
        return $false
    } catch {
        return $true
    }
}

$current_dir   = Split-Path -Parent $MyInvocation.MyCommand.Path
$logs_dir      = Join-Path $current_dir "logs"
$archive_dir   = Join-Path $current_dir "archive"

if (-not (Test-Path $logs_dir)) {
    Write-Output "Logs directory not found: $logs_dir"
    Write-Output "Nothing to clean. Exiting."
    exit 0
}

if (-not (Test-Path $archive_dir)) {
    New-Item -ItemType Directory -Path $archive_dir -Force | Out-Null
}

$today         = Get-Date
$todayFolder   = $today.ToString("yyyy-MM-dd")
$datedFolder   = Join-Path $archive_dir $todayFolder
$zipPath       = Join-Path $archive_dir "$todayFolder.zip"

if (Test-Path $zipPath) {
    Write-Output "Archive already exists for today ($todayFolder.zip). Skipping."
    exit 0
}

New-Item -ItemType Directory -Path $datedFolder -Force | Out-Null

$oldLogs = Get-ChildItem -Path $logs_dir -Filter *.log -File |
    Where-Object { $_.LastWriteTime.Date -lt $today.Date }

foreach ($file in $oldLogs) {
    if (Test-FileLocked $file.FullName) {
        Write-Output "Skipping locked file: $($file.Name)"
        continue
    }
    $dest = Join-Path $datedFolder $file.Name
    Move-Item -Path $file.FullName -Destination $dest -Force
    Write-Output "Moved: $($file.Name) -> $todayFolder\"
}

if ((Get-ChildItem -Path $datedFolder -File).Count -gt 0) {
    Write-Output "Compressing folder $todayFolder ..."

    Compress-Archive -Path "$datedFolder\*" -DestinationPath $zipPath -Force
    Remove-Item -Path $datedFolder -Recurse -Force
    Write-Output "Created: $todayFolder.zip"
} else {
    Remove-Item -Path $datedFolder -Force
    Write-Output "No old logs found today. Nothing to archive."
}

Write-Output "Log cleanup and archiving completed."

Write-Output "Configuring scheduled tasks..."

$taskName   = "AtsYokogawaServiceLogCleanup"
$scriptPath = $MyInvocation.MyCommand.Path
$argument   = "-NoProfile -ExecutionPolicy Bypass -File `"$scriptPath`""

schtasks.exe /Create /TN $taskName `
    /TR "powershell.exe $argument" `
    /SC MONTHLY /D 1 /ST 03:00 `
    /RU "NT AUTHORITY\SYSTEM" `
    /RL HIGHEST `
    /F > $null 2>&1

if ($LASTEXITCODE -eq 0) {
    Write-Host "Main monthly task created successfully (1st of every month at 03:00)" -ForegroundColor Green
}

#Test case:

# $testTime   = (Get-Date).AddMinutes(2)
# $startDate  = $testTime.ToString("dd/MM/yyyy")
# $startTime  = $testTime.ToString("HH:mm")

# schtasks.exe /Create /TN "$taskName-TestNow" `
#     /TR "powershell.exe $argument" `
#     /SC ONCE `
#     /SD $startDate `
#     /ST $startTime `
#     /RU "NT AUTHORITY\SYSTEM" `
#     /RL HIGHEST `
#     /F > $null 2>&1

# if ($LASTEXITCODE -eq 0) {
#     Write-Host "Test task created, will run once in 2 minutes at $startTime today" -ForegroundColor Cyan
# } else {
#     Write-Warning "Could not create test task (maybe it already exists?)"
# }

Write-Host "All done! Check Task Scheduler -> Task Scheduler Library -> AtsYokogawaServiceLogCleanup" -ForegroundColor Green
