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

        stage('Prepare Test Environment') {
            steps {
                sh '''
                    java -version
                    chmod +x gradlew
                    ./gradlew installBrowsers --no-daemon
                '''
            }
        }

        stage('Run Visual Tests') {
            steps {
                sh './gradlew clean test allureReport --no-daemon'
            }
        }
    }

    post {
        always {
            junit testResults: 'build/test-results/test/*.xml', allowEmptyResults: true

            archiveArtifacts artifacts: 'artifacts/visual/**/*', allowEmptyArchive: true
            archiveArtifacts artifacts: 'build/reports/tests/test/**/*', allowEmptyArchive: true
            archiveArtifacts artifacts: 'build/reports/allure-report/**/*', allowEmptyArchive: true
            archiveArtifacts artifacts: 'build/allure-results/**/*', allowEmptyArchive: true
        }
    }
}