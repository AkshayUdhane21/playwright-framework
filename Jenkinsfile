pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    environment {
        JAVA_HOME = tool 'JDK11'
        MAVEN_OPTS = '-Xmx1024m -XX:MaxPermSize=256m'
        TEST_MODE = 'mock'
    }

    stages {
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
            script {
                echo "Generating test reports..."
                try {
                    publishHTML([
                        allowMissing: true,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'test-output',
                        reportFiles: 'Enhanced_Test_Report_*.html',
                        reportName: 'Test Report'
                    ])
                } catch (Exception e) {
                    echo "HTML report generation failed: ${e.getMessage()}"
                }
                
                try {
                    archiveArtifacts artifacts: 'test-output/**/*', allowEmptyArchive: true
                    archiveArtifacts artifacts: 'allure-results/**/*', allowEmptyArchive: true
                } catch (Exception e) {
                    echo "Artifact archiving failed: ${e.getMessage()}"
                }
                
                try {
                    publishTestResults testResultsPattern: 'test-output/testng-results.xml'
                } catch (Exception e) {
                    echo "TestNG results publishing failed: ${e.getMessage()}"
                }
                
                echo "Workspace cleanup..."
                cleanWs()
            }
        }
        success {
            echo "All tests passed successfully!"
        }
        failure {
            echo "Some tests failed. Check the test reports for details."
        }
        unstable {
            echo "Build is unstable. Some tests failed but build continued."
        }
    }
}