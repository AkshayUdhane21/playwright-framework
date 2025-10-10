pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    environment {
        JAVA_HOME = tool 'JDK11'
        MAVEN_OPTS = '-Xmx1024m -XX:MaxPermSize=256m'
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
        }

        stage('Test with Real Services') {
            when {
                expression { params.RUN_REAL_TESTS == true }
            }
            steps {
                echo "Running tests with Real Services..."
                bat 'mvn test -Dtest.mode=real -Dreal.services.enabled=true -Dtest.parallel.enabled=true -Dtest.parallel.threads=2'
            }
        }
    }

    post {
        always {
            echo "Build completed"
            cleanWs()
        }
        success {
            echo "All tests passed successfully!"
        }
        failure {
            echo "Some tests failed. Check the console output for details."
        }
        unstable {
            echo "Build is unstable. Some tests failed but build continued."
        }
    }
}