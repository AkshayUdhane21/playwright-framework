#!/usr/bin/env pwsh
# Stop on errors
$ErrorActionPreference = "Stop"

function Map-Type {
    param([string]$InputType)

    switch -Regex ($InputType.ToLower()) {
        "debug"   { return "Debug" }
        "release" { return "Release" }
        "test"    { return "Test" }
        default {
            Write-Host "Unknown build type: $InputType"
            Write-Host "Usage: ./build.ps1 [debug] [release] [test]"
            exit 1
        }
    }
}

if ($args.Count -eq 0) {
    Write-Host "No build types provided."
    Write-Host "Usage: ./build.ps1 [debug] [release] [test]"
    exit 1
}

foreach ($arg in $args) {
    $Type = Map-Type $arg
    Write-Host "=== Building $Type configuration ==="

    # Configure and build using CMake
    cmake -B build -DCMAKE_BUILD_TYPE=$Type
    cmake --build build --config $Type
}

Write-Host "Done!"
