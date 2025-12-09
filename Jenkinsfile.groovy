// Jenkins Pipeline for Yokogawa Heartbeat Project
// This pipeline builds and tests the project using CMake, vcpkg, and CTest

pipeline {
    agent any
    
    environment {
        BUILD_TYPE = "${env.BUILD_TYPE ?: 'Release'}"
        BUILD_DIR = "build"
        // Deployment disabled by default on Linux agent
        DEPLOY_ENVIRONMENT = "${env.DEPLOY_ENVIRONMENT ?: 'none'}"
        DEPLOY_PATH = "${env.DEPLOY_PATH ?: '/opt/yokogawa'}"
        SERVICE_NAME = "AtsYokogawaConnectionService"
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
                    
                    // Verify CMake is available (Linux agent)
                    def cmakeCheck = sh(
                        script: 'cmake --version | head -n 1',
                        returnStdout: true
                    ).trim()
                    
                    if (cmakeCheck.isEmpty()) {
                        error("CMake not found. Please ensure CMake is installed and in PATH.")
                    }
                    
                    echo "✓ CMake found: ${cmakeCheck}"
                }
            }
        }
        
        stage('Configure CMake') {
            steps {
                script {
                    echo "=========================================="
                    echo "Configuring CMake (no vcpkg toolchain)..."
                    echo "=========================================="
                    sh """
                        set -e
                        rm -rf ${env.BUILD_DIR}
                        cmake -B ${env.BUILD_DIR} \\
                              -DCMAKE_BUILD_TYPE=${env.BUILD_TYPE} \\
                              -DBUILD_TESTING=ON \\
                              -S .
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
                    // Build service and tests
                    sh """
                        set -e
                        cmake --build ${env.BUILD_DIR} \\
                              --config ${env.BUILD_TYPE} \\
                              --target ${env.SERVICE_NAME} config_test security_test \\
                              --parallel
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
                    
                    def ctestResult = sh(
                        script: """
                            set -e
                            cd ${env.BUILD_DIR}
                            ctest -C ${env.BUILD_TYPE} -V --output-on-failure --progress --test-output-size-passed 10000000
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
                    sh """
                        cd ${env.BUILD_DIR}
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
                expression { 
                    return env.DEPLOY_ENVIRONMENT != null && env.DEPLOY_ENVIRONMENT != 'none'
                }
            }
            steps {
                script {
                    echo "=========================================="
                    echo "Deploying service to ${env.DEPLOY_ENVIRONMENT}..."
                    echo "=========================================="
                    
                    echo "Deployment is disabled on this Linux pipeline (set DEPLOY_ENVIRONMENT to enable and add Linux deploy logic)."
                }
            }
        }
    }
    
    post {
        always {
            echo "Pipeline completed"
            // Archive test results and service executable
            archiveArtifacts artifacts: "${env.BUILD_DIR}/${env.BUILD_TYPE}/bin/*", allowEmptyArchive: true
        }
        success {
            echo "✓ All stages completed successfully!"
        }
        failure {
            echo "✗ Pipeline failed!"
        }
    }
}





