pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    environment {
        JAVA_HOME = tool 'JDK11'
        MAVEN_OPTS = '-Xmx1024m -XX:MaxPermSize=256m'
        TEST_MODE = 'mock'
        GIT_SSL_NO_VERIFY = 'true'
        GIT_HTTP_LOW_SPEED_LIMIT = '1000'
        GIT_HTTP_LOW_SPEED_TIME = '300'
    }

    stages {
        stage('Git Connectivity Check') {
            steps {
                echo "Checking Git connectivity..."
                script {
                    try {
                        bat 'git config --global http.sslVerify false'
                        bat 'git config --global http.postBuffer 524288000'
                        bat 'git config --global http.lowSpeedLimit 1000'
                        bat 'git config --global http.lowSpeedTime 300'
                        echo "Git configuration updated for better connectivity"
                    } catch (Exception e) {
                        echo "Git configuration step failed: ${e.getMessage()}"
                    }
                }
            }
        }
        
        stage('Preparation') {
            steps {
                echo "Maven setup verification"
                bat 'mvn -v'
                echo "Java version check"
                bat 'java -version'
            }
        }

        stage('Clean Workspace') {
            steps {
                echo "Cleaning workspace and target directories..."
                bat 'if exist target rmdir /s /q target'
                bat 'if exist test-output rmdir /s /q test-output'
                bat 'if exist allure-results rmdir /s /q allure-results'
            }
        }

        stage('Compile') {
            steps {
                echo "Compiling project..."
                bat 'mvn clean compile test-compile -DskipTests'
            }
        }

        stage('Test with Mock Services') {
            steps {
                echo "Running tests with Mock Services..."
                bat 'mvn test -Dtest.mode=mock -Dmock.services.enabled=true -Dtest.parallel.enabled=true -Dtest.parallel.threads=3'
            }
            post {
                always {
                    echo "Test execution completed"
                }
            }
        }

        stage('Test with Real Services') {
            when {
                expression { params.RUN_REAL_TESTS == true }
            }
            steps {
                echo "Running tests with Real Services..."
                bat 'mvn test -Dtest.mode=real -Dreal.services.enabled=true -Dtest.parallel.enabled=true -Dtest.parallel.threads=2'
            }
            post {
                always {
                    echo "Real service tests completed"
                }
            }
        }
    }

    post {
        always {
            echo "Generating test reports..."
            publishHTML([
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'test-output',
                reportFiles: 'Enhanced_Test_Report_*.html',
                reportName: 'Test Report'
            ])
            
            // Archive test results
            archiveArtifacts artifacts: 'test-output/**/*', allowEmptyArchive: true
            archiveArtifacts artifacts: 'allure-results/**/*', allowEmptyArchive: true
            
            // Publish TestNG results
            publishTestResults testResultsPattern: 'test-output/testng-results.xml'
            
            echo "Workspace cleanup..."
            cleanWs()
        }
        success {
            echo "All tests passed successfully!"
            emailext (
                subject: "Build Success: ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
                body: "Build ${env.BUILD_NUMBER} of ${env.JOB_NAME} completed successfully.\n\nTest Results: ${env.BUILD_URL}testReport/",
                to: "${env.CHANGE_AUTHOR_EMAIL}"
            )
        }
        failure {
            echo "Some tests failed. Check the test reports for details."
            emailext (
                subject: "Build Failed: ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
                body: "Build ${env.BUILD_NUMBER} of ${env.JOB_NAME} failed.\n\nCheck the console output: ${env.BUILD_URL}console\nTest Results: ${env.BUILD_URL}testReport/",
                to: "${env.CHANGE_AUTHOR_EMAIL}"
            )
        }
        unstable {
            echo "Build is unstable. Some tests failed but build continued."
        }
    }
}