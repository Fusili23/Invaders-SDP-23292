pipeline {
    agent any

    tools {
        jdk 'jdk17'      // Jenkins > Global Tool Configuration 에서 이름이 jdk17 인 JDK
        maven 'maven3'   // Jenkins > Global Tool Configuration 에서 이름이 maven3 인 Maven
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
        // 빌드 결과는 항상 콘솔에 찍기
        always {
            echo "Build finished: ${currentBuild.currentResult}"
        }

        // ✅ 성공 시 Discord 알림
        success {
            withCredentials([string(credentialsId: 'Discord-Webhook', variable: 'DISCORD')]) {
                discordSend(
                    description: """
Space Invaders CI 빌드 성공 ✅

브랜치 : ${env.BRANCH_NAME}
빌드   : #${env.BUILD_NUMBER}
상태   : ${currentBuild.currentResult}
""",
                    footer: "Space Invaders CI",
                    link: env.BUILD_URL,
                    result: currentBuild.currentResult,
                    title: "Jenkins 빌드 성공 - ${env.JOB_NAME}",
                    webhookURL: DISCORD
                )
            }
        }

        // ❌ 실패 시 Discord 알림
        failure {
            withCredentials([string(credentialsId: 'Discord-Webhook', variable: 'DISCORD')]) {
                discordSend(
                    description: """
Space Invaders CI 빌드 실패 ❌

브랜치 : ${env.BRANCH_NAME}
빌드   : #${env.BUILD_NUMBER}
상태   : ${currentBuild.currentResult}
""",
                    footer: "Space Invaders CI",
                    link: env.BUILD_URL,
                    result: currentBuild.currentResult,
                    title: "Jenkins 빌드 실패 - ${env.JOB_NAME}",
                    webhookURL: DISCORD
                )
            }
        }
    }
}
