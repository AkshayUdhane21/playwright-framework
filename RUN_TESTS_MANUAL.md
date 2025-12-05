# How to Run Tests Manually

## 🎯 Quick Start - Run ALL Tests

**Easiest way:**
```powershell
cd "C:\Users\akshayu\Downloads\youkugawaheartbeat-master\youkugawaheartbeat-master"
.\run_all_tests.ps1
```

This script will:
- Build both `config_test` and `security_test`
- Run all tests sequentially
- Show a summary of results

---

## Step-by-Step Instructions

### Step 1: Open PowerShell
Open PowerShell and navigate to the project directory:
```powershell
cd "C:\Users\akshayu\Downloads\youkugawaheartbeat-master\youkugawaheartbeat-master"
```

### Step 2: Configure CMake (Only needed once, or after cleaning build directory)
```powershell
$vcpkgToolchain = "$env:VCPKG_ROOT\scripts\buildsystems\vcpkg.cmake"
cmake -B build -DCMAKE_BUILD_TYPE=Debug -DCMAKE_TOOLCHAIN_FILE="$vcpkgToolchain" -S .
```

**What this does:**
- Creates/updates the `build` directory
- Configures CMake with vcpkg toolchain
- Downloads and installs dependencies via vcpkg (if needed)

### Step 3: Build the Security Test
```powershell
cmake --build build --config Debug --target security_test
```

**What this does:**
- Compiles the `security_test` executable
- Links all required libraries (gtest, OpenSSL, etc.)
- Outputs: `build\Debug\bin\security_test.exe`

### Step 4: Run the Tests
```powershell
.\build\Debug\bin\security_test.exe
```

**Expected output:**
```
Running main() from C:\vcpkg\...\gtest_main.cc
[==========] Running 17 tests from 1 test suite.
[----------] Global test environment set-up.
[----------] 17 tests from SecurityTest
[ RUN      ] SecurityTest.BasicHMACComputation
[       OK ] SecurityTest.BasicHMACComputation (1 ms)
[ RUN      ] SecurityTest.KnownHMACComputation
[       OK ] SecurityTest.KnownHMACComputation (0 ms)
...
[----------] 17 tests from SecurityTest (3 ms total)
[----------] Global test environment tear-down.
[==========] 17 tests from 1 test suite ran. (3 ms total)
[  PASSED  ] 17 tests.
```

---

## Quick Commands (Copy & Paste)

### Full workflow (from scratch):
```powershell
# Navigate to project
cd "C:\Users\akshayu\Downloads\youkugawaheartbeat-master\youkugawaheartbeat-master"

# Configure (only needed once)
$vcpkgToolchain = "$env:VCPKG_ROOT\scripts\buildsystems\vcpkg.cmake"
cmake -B build -DCMAKE_BUILD_TYPE=Debug -DCMAKE_TOOLCHAIN_FILE="$vcpkgToolchain" -S .

# Build
cmake --build build --config Debug --target security_test

# Run
.\build\Debug\bin\security_test.exe
```

### If already configured, just build and run:
```powershell
# Build
cmake --build build --config Debug --target security_test

# Run
.\build\Debug\bin\security_test.exe
```

---

## Running All Tests

### Method 1: Use the Script (Easiest)
```powershell
.\run_all_tests.ps1
```

### Method 2: Manual Commands

#### Build All Tests:
```powershell
# Build both tests at once
cmake --build build --config Debug --target config_test security_test
```

#### Run All Tests:
```powershell
# Run config tests
.\build\Debug\bin\config_test.exe

# Run security tests  
.\build\Debug\bin\security_test.exe
```

### Method 3: One-Liner to Run All
```powershell
# Build and run all tests
cmake --build build --config Debug --target config_test security_test; .\build\Debug\bin\config_test.exe; .\build\Debug\bin\security_test.exe
```

## Running Individual Tests

### Run Config Tests Only:
```powershell
# Build config test
cmake --build build --config Debug --target config_test

# Run config test
.\build\Debug\bin\config_test.exe
```

### Run Security Tests Only:
```powershell
# Build security test
cmake --build build --config Debug --target security_test

# Run security test
.\build\Debug\bin\security_test.exe
```

---

## Troubleshooting

### Error: "build is not a directory"
**Solution:** Run Step 2 (Configure CMake) first.

### Error: "VCPKG_ROOT not set"
**Solution:** Make sure vcpkg is installed and VCPKG_ROOT environment variable is set.
```powershell
# Check if set
$env:VCPKG_ROOT

# If not set, you can set it temporarily:
$env:VCPKG_ROOT = "C:\vcpkg"  # Adjust path to your vcpkg installation
```

### Error: "security_test.exe not found"
**Solution:** Make sure the build succeeded. Check for compilation errors in the build output.

### Want to see detailed output?
Add `--verbose` flag:
```powershell
cmake --build build --config Debug --target security_test --verbose
```

---

## Alternative: Using Test Script

You can also use the helper script:
```powershell
.\configure_and_build_tests.ps1
.\build\Debug\bin\security_test.exe
```


