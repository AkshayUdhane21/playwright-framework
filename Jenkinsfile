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
                    echo "Setting up environment..."
                    echo "VCPKG_ROOT: ${env.VCPKG_ROOT}"
                    echo "BUILD_TYPE: ${env.BUILD_TYPE}"
                    
                    // Verify vcpkg exists
                    def vcpkgToolchain = "${env.VCPKG_ROOT}\\scripts\\buildsystems\\vcpkg.cmake"
                    def vcpkgExists = fileExists(vcpkgToolchain)
                    
                    if (!vcpkgExists) {
                        error("vcpkg toolchain file not found at: ${vcpkgToolchain}")
                    }
                    
                    echo "vcpkg toolchain found: ${vcpkgToolchain}"
                }
            }
        }
        
        stage('Configure CMake') {
            steps {
                script {
                    echo "Configuring CMake with vcpkg..."
                    def vcpkgToolchain = "${env.VCPKG_ROOT}\\scripts\\buildsystems\\vcpkg.cmake"
                    
                    bat """
                        cmake -B ${env.BUILD_DIR} ^
                            -DCMAKE_BUILD_TYPE=${env.BUILD_TYPE} ^
                            -DBUILD_TESTING=ON ^
                            -DCMAKE_TOOLCHAIN_FILE="${vcpkgToolchain}" ^
                            -S .
                    """
                }
            }
        }
        
        stage('Build') {
            steps {
                script {
                    echo "Building project and tests..."
                    bat """
                        cmake --build ${env.BUILD_DIR} ^
                            --config ${env.BUILD_TYPE} ^
                            --target config_test security_test ^
                            --parallel
                    """
                }
            }
        }
        
        stage('Test') {
            steps {
                script {
                    echo "Running tests with CTest..."
                    
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
                                    ctest -C ${env.BUILD_TYPE} -R "^${testName}$" --output-on-failure
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
        
        stage('Test - Alternative Direct Execution') {
            steps {
                script {
                    echo "Running tests directly (alternative method)..."
                    
                    // Alternative: Run tests directly if CTest doesn't work
                    def testExecutables = [
                        [name: 'ConfigTest', exe: "${env.BUILD_DIR}\\${env.BUILD_TYPE}\\bin\\config_test.exe"],
                        [name: 'SecurityTest', exe: "${env.BUILD_DIR}\\${env.BUILD_TYPE}\\bin\\security_test.exe"]
                    ]
                    
                    def directTestFailures = []
                    
                    for (def test : testExecutables) {
                        if (fileExists(test.exe)) {
                            echo "Running ${test.name} directly..."
                            def result = bat(
                                script: """
                                    "${test.exe}"
                                """,
                                returnStatus: true
                            )
                            
                            if (result != 0) {
                                echo "✗ ${test.name} FAILED (exit code: ${result})"
                                directTestFailures.add(test.name)
                            } else {
                                echo "✓ ${test.name} PASSED"
                            }
                        } else {
                            echo "⚠ ${test.name} executable not found at: ${test.exe}"
                        }
                    }
                    
                    if (!directTestFailures.isEmpty()) {
                        echo "Direct test execution failures: ${directTestFailures.join(', ')}"
                        // Don't fail here if CTest already passed
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
        cleanup {
            // Cleanup if needed
            echo "Cleaning up..."
        }
    }
}










