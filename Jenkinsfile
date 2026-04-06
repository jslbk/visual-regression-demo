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
                sh """
                  java -version
                  chmod +x gradlew
                  ./gradlew installBrowsers --no-daemon -Dvisual.browser=${params.BROWSER}
                """
            }
        }

        stage('Run Visual Tests') {
            steps {
              sh """
                ./gradlew clean test \
                  -Dvisual.profile=${params.PROFILE} \
                  -Dvisual.browser=${params.BROWSER} \
                  -Dvisual.headless=true
              """
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