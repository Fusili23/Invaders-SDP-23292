pipeline {
    agent any

    tools {
        jdk 'jdk17'
    }

    environment {
        JAVA_HOME = tool 'jdk17'
        PATH = "${JAVA_HOME}\\bin;${env.PATH}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Test') {
            steps {
                echo "Running tests via mvnw.cmd"
                bat "./mvnw.cmd -B clean test"
            }
        }

        stage('Report') {
            steps {
                echo "Tests completed"
            }
        }
    }

    post {
        always {
            echo "Build finished: ${currentBuild.currentResult}"
        }
    }
}
