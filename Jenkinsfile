pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Test') {
            steps {
                bat './mvnw.cmd clean test'
            }
        }

        stage('Report') {
            steps {
                junit 'target/surefire-reports/*.xml'
            }
        }
    }

    post {
        always {
            echo "Build finished: ${currentBuild.currentResult}"
        }
    }
}