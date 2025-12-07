pipeline {
    agent any

    tools {
        jdk 'jdk17'
        maven 'maven3'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Test') {
            steps {
                bat 'mvn -B clean test'
            }
        }
    }

    post {

        always {
            echo "Build finished: ${currentBuild.currentResult}"
        }

        success {
            withCredentials([string(credentialsId: 'Discord-Webhook', variable: 'DISCORD')]) {
                discordSend(
                    webhookURL: DISCORD,
                    title: "Jenkins Build Success",
                    description: "Branch: ${env.BRANCH_NAME}\nBuild: #${env.BUILD_NUMBER}\nStatus: SUCCESS",
                    link: env.BUILD_URL,
                    result: currentBuild.currentResult
                )
            }
        }

        failure {
            withCredentials([string(credentialsId: 'Discord-Webhook', variable: 'DISCORD')]) {
                discordSend(
                    webhookURL: DISCORD,
                    title: "Jenkins Build Failed",
                    description: "Branch: ${env.BRANCH_NAME}\nBuild: #${env.BUILD_NUMBER}\nStatus: FAILURE",
                    link: env.BUILD_URL,
                    result: currentBuild.currentResult
                )
            }
        }
    }
}
