pipeline {
    agent any
    
    environment {
        // Maven Configuration
        MAVEN_HOME = 'apache-maven-3.9.6'
        PATH = "${MAVEN_HOME}/bin:${env.PATH}"
        
        // Test Configuration
        TEST_ENV = "${params.TEST_ENV ?: 'local'}"
        BROWSER = "${params.BROWSER ?: 'chromium'}"
        HEADLESS = "${params.HEADLESS ?: 'true'}"
        PARALLEL_THREADS = "${params.PARALLEL_THREADS ?: '3'}"
        
        // Reporting
        ALLURE_RESULTS = 'allure-results'
        TEST_OUTPUT = 'test-output'
        REPORTS_DIR = 'reports'
    }
    
    parameters {
        choice(
            name: 'TEST_ENV',
            choices: ['local', 'staging', 'production'],
            description: 'Test environment to run against'
        )
        choice(
            name: 'BROWSER',
            choices: ['chromium', 'firefox', 'webkit'],
            description: 'Browser to use for testing'
        )
        booleanParam(
            name: 'HEADLESS',
            defaultValue: true,
            description: 'Run tests in headless mode'
        )
        string(
            name: 'PARALLEL_THREADS',
            defaultValue: '3',
            description: 'Number of parallel test threads'
        )
        booleanParam(
            name: 'SKIP_TESTS',
            defaultValue: false,
            description: 'Skip test execution (build only)'
        )
        booleanParam(
            name: 'CLEAN_WORKSPACE',
            defaultValue: true,
            description: 'Clean workspace before build'
        )
    }
    
    stages {
        stage('Preparation') {
            steps {
                script {
                    if (params.CLEAN_WORKSPACE) {
                        cleanWs()
                    }
                }
                
                echo "Starting Playwright Test Pipeline"
                echo "Environment: ${TEST_ENV}"
                echo "Browser: ${BROWSER}"
                echo "Headless: ${HEADLESS}"
                echo "Parallel Threads: ${PARALLEL_THREADS}"
                
                // Create necessary directories
                sh 'mkdir -p allure-results test-output reports'
            }
        }
        
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Environment Setup') {
            steps {
                script {
                    // Update configuration based on environment
                    sh """
                        if [ "${TEST_ENV}" = "staging" ]; then
                            sed -i 's|api.base.url=http://localhost:8081|api.base.url=http://staging-api:8081|g' config.properties
                        elif [ "${TEST_ENV}" = "production" ]; then
                            sed -i 's|api.base.url=http://localhost:8081|api.base.url=http://prod-api:8081|g' config.properties
                        fi
                        
                        # Update browser and headless settings
                        echo "browser=${BROWSER}" >> config.properties
                        echo "headless=${HEADLESS}" >> config.properties
                        echo "parallel.threads=${PARALLEL_THREADS}" >> config.properties
                    """
                }
            }
        }
        
        stage('Dependencies') {
            steps {
                script {
                    // Install Playwright browsers
                    sh 'mvn dependency:resolve'
                    sh 'mvn exec:java -Dexec.mainClass="com.microsoft.playwright.CLI" -Dexec.args="install"'
                }
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }
        
        stage('Code Quality') {
            parallel {
                stage('Lint Check') {
                    steps {
                        sh 'mvn checkstyle:check || true'
                    }
                }
                stage('Security Scan') {
                    steps {
                        sh 'mvn org.owasp:dependency-check-maven:check || true'
                    }
                }
            }
        }
        
        stage('Test Execution') {
            when {
                not { params.SKIP_TESTS }
            }
            steps {
                script {
                    try {
                        // Start services if needed
                        if (fileExists('docker-compose.yml')) {
                            sh 'docker-compose up -d --build'
                            sleep(time: 30, unit: 'SECONDS')
                        }
                        
                        // Run tests
                        sh """
                            mvn test -Dtest.parallel.execution=true \
                                     -Dtest.thread.count=${PARALLEL_THREADS} \
                                     -Dbrowser=${BROWSER} \
                                     -Dheadless=${HEADLESS} \
                                     -Dtest.env=${TEST_ENV}
                        """
                    } catch (Exception e) {
                        echo "Test execution failed: ${e.getMessage()}"
                        currentBuild.result = 'UNSTABLE'
                    } finally {
                        // Stop services
                        if (fileExists('docker-compose.yml')) {
                            sh 'docker-compose down || true'
                        }
                    }
                }
            }
        }
        
        stage('Test Reports') {
            when {
                not { params.SKIP_TESTS }
            }
            steps {
                script {
                    // Generate Allure Report
                    if (fileExists('allure-results')) {
                        sh 'mvn allure:report || true'
                    }
                    
                    // Archive test results
                    archiveArtifacts artifacts: 'test-output/**/*', allowEmptyArchive: true
                    archiveArtifacts artifacts: 'allure-results/**/*', allowEmptyArchive: true
                    archiveArtifacts artifacts: 'target/surefire-reports/**/*', allowEmptyArchive: true
                }
            }
        }
        
        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
                archiveArtifacts artifacts: 'target/*.jar', allowEmptyArchive: true
            }
        }
        
        stage('Deploy') {
            when {
                anyOf {
                    branch 'main'
                    branch 'master'
                    branch 'develop'
                }
            }
            steps {
                script {
                    if (params.TEST_ENV == 'production') {
                        // Add production deployment steps here
                        echo "Deploying to production environment"
                    } else {
                        echo "Deploying to ${TEST_ENV} environment"
                    }
                }
            }
        }
    }
    
    post {
        always {
            script {
                // Clean up
                sh 'docker-compose down || true'
                sh 'docker system prune -f || true'
            }
            
            // Publish test results
            publishTestResults testResultsPattern: 'target/surefire-reports/*.xml'
            
            // Publish Allure Report
            allure([
                includeProperties: false,
                jdk: '',
                properties: [],
                reportBuildPolicy: 'ALWAYS',
                results: [[path: 'allure-results']]
            ])
        }
        
        success {
            echo 'Pipeline completed successfully!'
            // Send success notification
            emailext (
                subject: "✅ Playwright Tests Passed - ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
                    <h2>Test Execution Successful</h2>
                    <p><strong>Build:</strong> ${env.JOB_NAME} #${env.BUILD_NUMBER}</p>
                    <p><strong>Environment:</strong> ${TEST_ENV}</p>
                    <p><strong>Browser:</strong> ${BROWSER}</p>
                    <p><strong>Build URL:</strong> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                    <p><strong>Test Report:</strong> <a href="${env.BUILD_URL}allure/">Allure Report</a></p>
                """,
                to: "${env.CHANGE_AUTHOR_EMAIL ?: 'team@company.com'}"
            )
        }
        
        failure {
            echo 'Pipeline failed!'
            // Send failure notification
            emailext (
                subject: "❌ Playwright Tests Failed - ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
                    <h2>Test Execution Failed</h2>
                    <p><strong>Build:</strong> ${env.JOB_NAME} #${env.BUILD_NUMBER}</p>
                    <p><strong>Environment:</strong> ${TEST_ENV}</p>
                    <p><strong>Browser:</strong> ${BROWSER}</p>
                    <p><strong>Build URL:</strong> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                    <p><strong>Console Output:</strong> <a href="${env.BUILD_URL}console">Console Log</a></p>
                """,
                to: "${env.CHANGE_AUTHOR_EMAIL ?: 'team@company.com'}"
            )
        }
        
        unstable {
            echo 'Pipeline completed with warnings!'
        }
        
        cleanup {
            // Clean workspace
            cleanWs()
        }
    }
}


