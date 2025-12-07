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
                // GitHub Actions랑 최대한 비슷하게
                bat 'mvn -B clean test'
            }
        }

        stage('Report') {
            steps {
                // Maven Surefire 결과 경로
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
