// Jenkins Pipeline for Yokogawa Heartbeat Project
// This pipeline builds and tests the project using CMake, vcpkg, and CTest

pipeline {
    agent any
    
    environment {
        // vcpkg root - adjust if your vcpkg is installed elsewhere
        VCPKG_ROOT = "${env.VCPKG_ROOT ?: 'C:\\vcpkg'}"
        BUILD_TYPE = "${env.BUILD_TYPE ?: 'Debug'}"
        BUILD_DIR = "build"
    }
    
    options {
        timeout(time: 30, unit: 'MINUTES')
        timestamps()
        ansiColor('xterm')
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo "Checking out source code..."
                checkout scm
            }
        }
        
        stage('Environment Setup') {
            steps {
                script {
                    echo "=========================================="
                    echo "Setting up environment..."
                    echo "=========================================="
                    echo "VCPKG_ROOT: ${env.VCPKG_ROOT}"
                    echo "BUILD_TYPE: ${env.BUILD_TYPE}"
                    echo "BUILD_DIR: ${env.BUILD_DIR}"
                    
                    // Verify vcpkg exists
                    def vcpkgToolchain = "${env.VCPKG_ROOT}\\scripts\\buildsystems\\vcpkg.cmake"
                    def vcpkgExists = fileExists(vcpkgToolchain)
                    
                    if (!vcpkgExists) {
                        error("vcpkg toolchain file not found at: ${vcpkgToolchain}\n" +
                              "Please ensure vcpkg is installed and VCPKG_ROOT is set correctly.")
                    }
                    
                    echo "✓ vcpkg toolchain found: ${vcpkgToolchain}"
                    
                    // Verify CMake is available
                    def cmakeCheck = bat(
                        script: '@echo off && cmake --version',
                        returnStdout: true
                    ).trim()
                    
                    if (cmakeCheck.isEmpty()) {
                        error("CMake not found. Please ensure CMake is installed and in PATH.")
                    }
                    
                    echo "✓ CMake found:"
                    echo cmakeCheck.split('\n')[0]
                }
            }
        }
        
        stage('Configure CMake') {
            steps {
                script {
                    echo "=========================================="
                    echo "Configuring CMake with vcpkg..."
                    echo "=========================================="
                    def vcpkgToolchain = "${env.VCPKG_ROOT}\\scripts\\buildsystems\\vcpkg.cmake"
                    
                    // Clean previous build if it exists (optional)
                    if (fileExists("${env.BUILD_DIR}")) {
                        echo "Cleaning previous build directory..."
                        bat "if exist ${env.BUILD_DIR} rmdir /s /q ${env.BUILD_DIR}"
                    }
                    
                    bat """
                        @echo off
                        cmake -B ${env.BUILD_DIR} ^
                            -DCMAKE_BUILD_TYPE=${env.BUILD_TYPE} ^
                            -DCMAKE_TOOLCHAIN_FILE="${vcpkgToolchain}" ^
                            -S .
                        if errorlevel 1 exit /b 1
                    """
                    
                    echo "✓ CMake configuration completed successfully"
                }
            }
        }
        
        stage('Build') {
            steps {
                script {
                    echo "=========================================="
                    echo "Building project and tests..."
                    echo "=========================================="
                    bat """
                        @echo off
                        cmake --build ${env.BUILD_DIR} ^
                            --config ${env.BUILD_TYPE} ^
                            --target config_test security_test ^
                            --parallel
                        if errorlevel 1 exit /b 1
                    """
                    echo "✓ Build completed successfully"
                }
            }
        }
        
        stage('Test - CTest') {
            steps {
                script {
                    echo "=========================================="
                    echo "Running tests with CTest..."
                    echo "=========================================="
                    
                    // Run tests using CTest with specific test names
                    def testNames = [
                        'ConfigTest',
                        'SecurityTest'
                    ]
                    
                    def testResults = []
                    def testFailures = []
                    
                    for (def testName : testNames) {
                        echo "Running test: ${testName}"
                        try {
                            def result = bat(
                                script: """
                                    cd ${env.BUILD_DIR}
                                    ctest -C ${env.BUILD_TYPE} -R "^${testName}\$" --output-on-failure
                                """,
                                returnStatus: true
                            )
                            
                            if (result == 0) {
                                echo "✓ ${testName} PASSED"
                                testResults.add("${testName}: PASSED")
                            } else {
                                echo "✗ ${testName} FAILED"
                                testFailures.add("${testName}: FAILED")
                                testResults.add("${testName}: FAILED")
                            }
                        } catch (Exception e) {
                            echo "Error running ${testName}: ${e.getMessage()}"
                            testFailures.add("${testName}: ERROR - ${e.getMessage()}")
                            testResults.add("${testName}: ERROR")
                        }
                    }
                    
                    // Print summary
                    echo "=========================================="
                    echo "Test Summary:"
                    echo "=========================================="
                    for (def result : testResults) {
                        echo result
                    }
                    echo "=========================================="
                    
                    // Fail build if any tests failed
                    if (!testFailures.isEmpty()) {
                        error("Tests failed: ${testFailures.join(', ')}")
                    }
                }
            }
        }
    }
    
    post {
        always {
            echo "Pipeline completed"
            // Archive test results if needed
            archiveArtifacts artifacts: "${env.BUILD_DIR}\\${env.BUILD_TYPE}\\bin\\*test*.exe", allowEmptyArchive: true
        }
        success {
            echo "✓ All stages completed successfully!"
        }
        failure {
            echo "✗ Pipeline failed!"
        }
    }
}





