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
                bat """
curl -H "Content-Type: application/json" ^
     -X POST ^
     -d "{ \\
        \\"username\\": \\"Jenkins\\", \\
        \\"embeds\\": [{ \\
            \\"title\\": \\"🎉 Jenkins 빌드 성공\\", \\
            \\"description\\": \\"브랜치: ${env.BRANCH_NAME}\\n빌드번호: #${env.BUILD_NUMBER}\\n상태: ${currentBuild.currentResult}\\", \\
            \\"color\\": 3066993 \\
        }] \\
     }" ^
     %DISCORD%
"""
            }
        }

        failure {
            withCredentials([string(credentialsId: 'Discord-Webhook', variable: 'DISCORD')]) {
                bat """
curl -H "Content-Type: application/json" ^
     -X POST ^
     -d "{ \\
        \\"username\\": \\"Jenkins\\", \\
        \\"embeds\\": [{ \\
            \\"title\\": \\"❌ Jenkins 빌드 실패\\", \\
            \\"description\\": \\"브랜치: ${env.BRANCH_NAME}\\n빌드번호: #${env.BUILD_NUMBER}\\n상태: ${currentBuild.currentResult}\\", \\
            \\"color\\": 15158332 \\
        }] \\
     }" ^
     %DISCORD%
"""
            }
        }
    }
}
