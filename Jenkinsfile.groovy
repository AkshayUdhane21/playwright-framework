// Jenkins Pipeline for Yokogawa Heartbeat Project
// This pipeline builds and tests the project using CMake, vcpkg, and CTest

pipeline {
    agent any
    
    environment {
        BUILD_TYPE = "${env.BUILD_TYPE ?: 'Release'}"
        BUILD_DIR = "build"
        // Deployment configuration
        DEPLOY_ENVIRONMENT = "${env.DEPLOY_ENVIRONMENT ?: 'dev'}"
        DEPLOY_PATH = "${env.DEPLOY_PATH ?: 'C:\\Services\\Yokogawa'}"
        SERVICE_NAME = "AtsYokogawaConnectionService"
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
                    echo "BUILD_TYPE: ${env.BUILD_TYPE}"
                    echo "BUILD_DIR: ${env.BUILD_DIR}"
                    
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
                    echo "Configuring CMake..."
                    echo "=========================================="
                    // Clean previous build if it exists (optional)
                    if (fileExists("${env.BUILD_DIR}")) {
                        echo "Cleaning previous build directory..."
                        bat "if exist ${env.BUILD_DIR} rmdir /s /q ${env.BUILD_DIR}"
                    }
                    
                    bat """
                        @echo off
                        cmake -B ${env.BUILD_DIR} ^
                            -DCMAKE_BUILD_TYPE=${env.BUILD_TYPE} ^
                            -DBUILD_TESTING=ON ^
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
                    echo "Building project, service, and tests..."
                    echo "=========================================="
                    // Build service executable, test executables and their dependencies
                    bat """
                        @echo off
                        cmake --build ${env.BUILD_DIR} ^
                            --config ${env.BUILD_TYPE} ^
                            --target ${env.SERVICE_NAME} config_test security_test ^
                            --parallel
                        if errorlevel 1 exit /b 1
                    """
                    echo "✓ Build completed successfully"
                    
                    // Verify service executable was built
                    def serviceExe = "${env.BUILD_DIR}\\${env.BUILD_TYPE}\\bin\\${env.SERVICE_NAME}.exe"
                    if (!fileExists(serviceExe)) {
                        error("Service executable not found at: ${serviceExe}\nBuild may have failed.")
                    }
                    echo "✓ Service executable verified: ${serviceExe}"
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
                        ctest -C ${env.BUILD_TYPE} -N
                    """
                    echo "=========================================="
                    echo "✓ All tests passed!"
                    echo "=========================================="
                }
            }
        }
        
        stage('Deploy') {
            when {
                // Only deploy if tests passed and deployment is enabled
                expression { 
                    return env.DEPLOY_ENVIRONMENT != null && env.DEPLOY_ENVIRONMENT != 'none'
                }
            }
            steps {
                script {
                    echo "=========================================="
                    echo "Deploying service to ${env.DEPLOY_ENVIRONMENT}..."
                    echo "=========================================="
                    
                    // Determine source paths based on build type
                    def sourceBinPath = "${env.BUILD_DIR}\\${env.BUILD_TYPE}\\bin"
                    def sourceConfigPath = "${env.WORKSPACE}\\config.json"
                    
                    // Verify source files exist
                    def serviceExe = "${sourceBinPath}\\${env.SERVICE_NAME}.exe"
                    if (!fileExists(serviceExe)) {
                        error("Service executable not found at: ${serviceExe}")
                    }
                    
                    echo "Source Bin Path: ${sourceBinPath}"
                    echo "Source Config Path: ${sourceConfigPath}"
                    echo "Deploy Path: ${env.DEPLOY_PATH}"
                    echo "Environment: ${env.DEPLOY_ENVIRONMENT}"
                    
                    // Run deployment script
                    def deployScript = "${env.WORKSPACE}\\powershell_scripts\\deploy.ps1"
                    if (!fileExists(deployScript)) {
                        error("Deployment script not found at: ${deployScript}")
                    }
                    
                    bat """
                        @echo off
                        powershell.exe -ExecutionPolicy Bypass -File "${deployScript}" ^
                            -DeployPath "${env.DEPLOY_PATH}" ^
                            -Environment "${env.DEPLOY_ENVIRONMENT}" ^
                            -ServiceName "${env.SERVICE_NAME}" ^
                            -SourceBinPath "${sourceBinPath}" ^
                            -SourceConfigPath "${sourceConfigPath}"
                        if errorlevel 1 exit /b 1
                    """
                    
                    echo "=========================================="
                    echo "✓ Deployment completed successfully!"
                    echo "=========================================="
                    echo "Service deployed to: ${env.DEPLOY_PATH}"
                    echo "Service name: ${env.SERVICE_NAME}"
                    echo "Environment: ${env.DEPLOY_ENVIRONMENT}"
                }
            }
        }
    }
    
    post {
        always {
            echo "Pipeline completed"
            // Archive test results and service executable
            archiveArtifacts artifacts: "${env.BUILD_DIR}\\${env.BUILD_TYPE}\\bin\\*.exe", allowEmptyArchive: true
        }
        success {
            echo "✓ All stages completed successfully!"
        }
        failure {
            echo "✗ Pipeline failed!"
        }
    }
}





