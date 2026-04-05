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

        stage('Install Playwright Browser') {
            steps {
                sh 'gradle installBrowsers'
            }
        }

        stage('Run Visual Tests') {
            steps {
                sh 'gradle clean test allureReport'
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'artifacts/visual/**/*', allowEmptyArchive: true
            archiveArtifacts artifacts: 'build/reports/tests/test/**/*', allowEmptyArchive: true
            archiveArtifacts artifacts: 'build/reports/allure-report/**/*', allowEmptyArchive: true
            junit testResults: 'build/test-results/test/*.xml', allowEmptyResults: true
            allure([
                includeProperties: false,
                jdk: '',
                reportBuildPolicy: 'ALWAYS',
                results: [[path: 'build/allure-results']]
            ])
        }
    }
}
