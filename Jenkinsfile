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
                echo "Running Playwright tests..."
                bat 'mvn test'
            }
        }
    }

    post {
        always {
            echo "Cleaning workspace..."
            cleanWs()
        }
    }
}