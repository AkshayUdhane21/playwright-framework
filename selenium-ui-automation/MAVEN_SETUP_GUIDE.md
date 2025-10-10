# Maven Setup Guide for Selenium Project

## Overview
This guide explains how to set up and use Maven for the Selenium UI Automation project.

## Prerequisites
- Java 8 or higher (Java 17 recommended)
- Windows operating system

## Maven Installation Status

### ✅ Maven is Already Available
The project includes a local Maven installation in `../apache-maven-3.9.6/`

### ✅ Java is Available
Java 17.0.8 is installed and working correctly.

## Available Scripts

### 1. Maven Setup Script
```batch
setup-maven.bat
```
- Sets up Maven environment variables
- Configures JAVA_HOME automatically
- Tests Maven installation

### 2. Maven Wrapper
```batch
mvn.bat
```
- Wrapper script that sets up environment and runs Maven commands
- Usage: `mvn.bat clean test`

### 3. Test Runners
- `run-tests.bat` - Full test suite with Maven
- `run-simple-test.bat` - Simple test with Maven
- `test-form-opening.bat` - Form opening tests with Maven
- `run-tests-java.bat` - Direct Java execution (no Maven required)

## How to Use

### Option 1: Use Maven (Recommended)
1. Run `setup-maven.bat` to configure environment
2. Run any of the test scripts:
   - `run-tests.bat` - Full test suite
   - `test-form-opening.bat` - Form opening tests
   - `run-simple-test.bat` - Simple test

### Option 2: Use Maven Wrapper
1. Use `mvn.bat` instead of `mvn`:
   ```batch
   mvn.bat clean test
   mvn.bat test -Dtest=FormOpeningTest
   ```

### Option 3: Direct Java Execution
1. Ensure project is compiled (use IDE or Maven)
2. Run `run-tests-java.bat`

## Troubleshooting

### Issue: "JAVA_HOME environment variable is not defined correctly"
**Solution**: The Maven setup scripts automatically detect and set JAVA_HOME. If this fails:
1. Manually set JAVA_HOME to your Java installation directory
2. Example: `set JAVA_HOME=C:\Program Files\Java\jdk-17.0.8`

### Issue: "Maven is not recognized"
**Solution**: Use the provided wrapper scripts:
- `mvn.bat` instead of `mvn`
- Or run `setup-maven.bat` first

### Issue: "No compiled classes found"
**Solution**: Compile the project first:
1. Run `mvn.bat clean compile`
2. Or use your IDE to compile

## Project Structure
```
selenium-ui-automation/
├── src/
│   ├── main/java/          # Main source code
│   └── test/java/          # Test source code
├── target/                 # Compiled classes
├── lib/                    # Dependencies (if any)
├── pom.xml                 # Maven configuration
├── testng.xml             # TestNG configuration
└── *.bat                   # Windows batch scripts
```

## Maven Commands
```batch
# Clean and compile
mvn.bat clean compile

# Run all tests
mvn.bat test

# Run specific test class
mvn.bat test -Dtest=FormOpeningTest

# Run with specific environment
mvn.bat test -Denv=local

# Generate test report
mvn.bat allure:report
```

## Environment Variables
The scripts automatically set:
- `MAVEN_HOME` - Points to local Maven installation
- `JAVA_HOME` - Points to Java installation
- `PATH` - Includes Maven bin directory

## Success Indicators
- Maven version command works: `mvn.bat -version`
- Tests compile successfully: `mvn.bat clean compile`
- Tests run successfully: `mvn.bat test`

## Next Steps
1. Start your application at `http://localhost:5173/`
2. Run `test-form-opening.bat` to test the form opening functionality
3. Check the `test-output/` directory for test results
4. Check the `screenshots/` directory for test screenshots
