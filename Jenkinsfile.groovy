// Jenkins Pipeline for Yokogawa Heartbeat Project
// This pipeline builds and tests the project using CMake, vcpkg, and CTest

pipeline {
    agent any
    
    environment {
        // vcpkg root - adjust if your vcpkg is installed elsewhere
        VCPKG_ROOT = "${env.VCPKG_ROOT ?: 'C:\\vcpkg'}"
        BUILD_TYPE = "${env.BUILD_TYPE ?: 'Debug'}"
        BUILD_DIR = "build"
        // Configure vcpkg to use a writable binary cache location for Jenkins
        // This prevents issues when Jenkins runs as SYSTEM service which can't access
        // C:\WINDOWS\system32\config\systemprofile\AppData\Local\vcpkg\archives
        // Note: VCPKG_BINARY_SOURCES will be set dynamically in the Environment Setup stage
        // since WORKSPACE is not available in the environment block
    }
    
    options {
        timeout(time: 30, unit: 'MINUTES')
        timestamps()
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
                    echo "VCPKG_BINARY_SOURCES: ${env.VCPKG_BINARY_SOURCES}"
                    
                    // Verify vcpkg exists
                    def vcpkgToolchain = "${env.VCPKG_ROOT}\\scripts\\buildsystems\\vcpkg.cmake"
                    def vcpkgExists = fileExists(vcpkgToolchain)
                    
                    if (!vcpkgExists) {
                        error("vcpkg toolchain file not found at: ${vcpkgToolchain}\n" +
                              "Please ensure vcpkg is installed and VCPKG_ROOT is set correctly.")
                    }
                    
                    echo "✓ vcpkg toolchain found: ${vcpkgToolchain}"
                    
                    // Create vcpkg archives directory in workspace for binary caching
                    // This avoids issues with SYSTEM account AppData paths
                    def vcpkgArchivesDir = "${env.WORKSPACE}\\vcpkg_archives"
                    bat """
                        @echo off
                        if not exist "${vcpkgArchivesDir}" mkdir "${vcpkgArchivesDir}"
                    """
                    echo "✓ vcpkg archives directory configured: ${vcpkgArchivesDir}"
                    
                    // Set VCPKG_BINARY_SOURCES to use workspace directory for binary cache
                    // This prevents vcpkg from trying to access inaccessible SYSTEM AppData paths
                    env.VCPKG_BINARY_SOURCES = "clear;files,${vcpkgArchivesDir},readwrite"
                    echo "VCPKG_BINARY_SOURCES configured: ${env.VCPKG_BINARY_SOURCES}"
                    
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
                            --target all ^
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
                    
                    // Use CTest to run all registered tests
                    // -C specifies the build configuration (Debug/Release)
                    // -V enables verbose output to show gtest details
                    // --output-on-failure shows output even for failed tests
                    // --progress shows test progress  
                    // --test-output-size-passed ensures we see full gtest output (even for passing tests)
                    // -T Test runs tests and shows detailed output
                    def ctestResult = bat(
                        script: """
                            @echo off
                            cd /d ${env.BUILD_DIR}
                            ctest -C ${env.BUILD_TYPE} -V --output-on-failure --progress --test-output-size-passed 10000000
                            if errorlevel 1 exit /b 1
                        """,
                        returnStatus: true
                    )
                    
                    if (ctestResult != 0) {
                        error("Tests failed! Check the output above for details.")
                    }
                    
                    // Show test summary
                    echo "=========================================="
                    echo "Test Summary:"
                    echo "=========================================="
                    bat """
                        @echo off
                        cd /d ${env.BUILD_DIR}
                        ctest -C ${env.BUILD_TYPE} --print-summary
                    """
                    echo "=========================================="
                    echo "✓ All tests passed!"
                    echo "=========================================="
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





