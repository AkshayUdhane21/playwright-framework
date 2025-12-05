# Testing Guide

## Security Test Class

A comprehensive test suite has been created for the `security` module using Google Test (gtest) with vcpkg integration.

### Test Coverage

The `SecurityTest` class includes 17 comprehensive test cases covering:

- ✅ Basic HMAC computation
- ✅ Known HMAC computation verification
- ✅ Empty inputs (key/data)
- ✅ Long inputs handling
- ✅ Special characters and Unicode
- ✅ Deterministic output verification
- ✅ Uniqueness verification (different inputs = different outputs)
- ✅ Binary data handling
- ✅ Case sensitivity
- ✅ API key format simulation
- ✅ Multiple sequential calls
- ✅ Format validation (64-character hex output)

### Building and Running Tests

#### Option 1: Run ALL Tests (Easiest)
```powershell
.\run_all_tests.ps1
```
This will build and run both `config_test` and `security_test` automatically!

#### Option 2: Using CMake directly

```powershell
# Configure CMake (if not already done)
$vcpkgToolchain = "$env:VCPKG_ROOT\scripts\buildsystems\vcpkg.cmake"
cmake -B build -DCMAKE_BUILD_TYPE=Debug -DCMAKE_TOOLCHAIN_FILE="$vcpkgToolchain" -S .

# Build the security test
cmake --build build --config Debug --target security_test

# Run the tests
.\build\Debug\bin\security_test.exe
```

#### Option 2: Using the helper script

```powershell
.\configure_and_build_tests.ps1
```

Then run:
```powershell
.\build\Debug\bin\security_test.exe
```

#### Option 3: Build all tests

```powershell
cmake --build build --config Debug --target config_test
cmake --build build --config Debug --target security_test

# Run both
.\build\Debug\bin\config_test.exe
.\build\Debug\bin\security_test.exe
```

### Test Results

All 17 tests pass successfully! ✅

```
[==========] Running 17 tests from 1 test suite.
[  PASSED  ] 17 tests.
```

### Project Structure

```
test/
├── CMakeLists.txt              # Main test configuration (modern vcpkg style)
├── config/
│   └── config_test.cpp         # Existing config tests
└── security_test/
    ├── CMakeLists.txt          # Security test CMake configuration
    └── security_test.cpp       # Security test class with 17 test cases
```

### Key Features

- ✅ Modern vcpkg integration using `GTest::gtest` targets
- ✅ C++20 standard
- ✅ Proper CMake configuration with test registration
- ✅ Comprehensive test coverage
- ✅ Well-documented test cases
- ✅ Follows Google Test best practices

### Requirements

- CMake 3.15 or higher
- vcpkg with gtest package installed
- Visual Studio 2019+ (or compatible C++ compiler)
- OpenSSL (already a project dependency)

### Notes

- The tests use the modern vcpkg style with `GTest::gtest` and `GTest::gtest_main` targets
- Tests are automatically registered with CTest via `add_test()`
- All dependencies are managed through vcpkg manifest mode


