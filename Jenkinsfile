pipeline {
    agent any

    tools {
        jdk 'JDK21'
    }

    environment {
        ANDROID_HOME     = 'C:\\Users\\ASUS\\AppData\\Local\\Android\\Sdk'
        ANDROID_SDK_ROOT = 'C:\\Users\\ASUS\\AppData\\Local\\Android\\Sdk'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Inject google-services.json') {
            steps {
                // Copy the Firebase config file from Jenkins credentials into the app module.
                // In Jenkins: Manage Jenkins → Credentials → Add "Secret File"
                //             with ID = "google-services-json"
                withCredentials([file(credentialsId: 'google-services-json',
                                      variable: 'GOOGLE_SERVICES')]) {
                    bat 'copy /Y "%GOOGLE_SERVICES%" "Loop\\app\\google-services.json"'
                }
            }
        }

        stage('Check Environment') {
            steps {
                dir('Loop') {
                    bat 'java -version'
                    bat 'gradlew.bat --version'
                }
            }
        }

        stage('Build Debug APK') {
            steps {
                dir('Loop') {
                    bat 'gradlew.bat clean assembleDebug'
                }
            }
        }

        stage('Archive APK') {
            steps {
                archiveArtifacts artifacts: 'Loop/app/build/outputs/apk/debug/*.apk',
                    fingerprint: true
            }
        }
    }

    post {
        success {
            echo '✅ Build successful! APK is archived.'
        }
        failure {
            echo '❌ Build failed. Check the console output above.'
        }
        always {
            // Clean workspace to avoid stale files between builds
            cleanWs()
        }
    }
}
