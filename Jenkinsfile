pipeline {
    agent any

    options {
        timestamps()
        ansiColor('xterm')
        skipDefaultCheckout(true)
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Verify Java') {
            steps {
                sh 'java -version'
            }
        }

        stage('Prepare Gradle Wrapper') {
            steps {
                sh 'chmod +x gradlew'
            }
        }

        stage('Install Playwright Browser') {
            steps {
                sh './gradlew installBrowsers --no-daemon'
            }
        }

        stage('Run Visual Tests') {
            steps {
                sh './gradlew clean test --no-daemon'
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'artifacts/visual/**/*', allowEmptyArchive: true
            archiveArtifacts artifacts: 'build/reports/tests/test/**/*', allowEmptyArchive: true
            archiveArtifacts artifacts: 'build/reports/allure-report/**/*', allowEmptyArchive: true

            junit testResults: 'build/test-results/test/*.xml', allowEmptyResults: true

            allure(
                results: [[path: 'build/allure-results']]
            )
        }
    }
}