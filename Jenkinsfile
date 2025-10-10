pipeline {
    agent any

    tools {
        maven 'Maven3'   // ✅ Matches the name in your Jenkins config
    }

    stages {
        stage('Preparation') {
            steps {
                echo "Maven setup verification"
                bat 'mvn -v'
            }
        }

        stage('Build') {
            steps {
                echo "Building Playwright project using Maven..."
                bat 'mvn clean install -DskipTests'
            }
        }

        stage('Test') {
            steps {
                echo "Running Playwright tests with Mock Services..."
                bat 'mvn test -Dtest.mode=mock -Dmock.services.enabled=true'
            }
        }
    }

    post {
        always {
            echo "Cleaning workspace..."
            cleanWs()
        }
        success {
            echo "✅ All tests passed successfully!"
        }
        failure {
            echo "❌ Some tests failed. Check the test reports for details."
        }
    }
}