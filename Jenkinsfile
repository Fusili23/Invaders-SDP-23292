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
                    title: "Jenkins 빌드 성공!",
                    description: """
브랜치 : ${env.BRANCH_NAME}
빌드번호 : #${env.BUILD_NUMBER}
상태 : ${currentBuild.currentResult}

${env.BUILD_URL}
""",
                    color: 0x00ff00
                )
            }
        }

        failure {
            withCredentials([string(credentialsId: 'Discord-Webhook', variable: 'DISCORD')]) {
                discordSend(
                    webhookURL: DISCORD,
                    title: "Jenkins 빌드 실패 ",
                    description: """
브랜치 : ${env.BRANCH_NAME}
빌드번호 : #${env.BUILD_NUMBER}
상태 : ${currentBuild.currentResult}

${env.BUILD_URL}
""",
                    color: 0xff0000
                )
            }
        }
    }
}
