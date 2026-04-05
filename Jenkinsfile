pipeline {
    agent any

    options {
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
                sh './gradlew clean test --no-daemon'
            }
        }
    }

   post {
       always {
           allure([
               includeProperties: false,
               results: [[path: 'build/allure-results']]
           ])
       }
   }
}