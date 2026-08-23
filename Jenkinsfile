pipeline {
    agent any

    tools {
        jdk 'JDK21'
    }

    environment {
        ANDROID_HOME = 'C:\\Users\\ASUS\\AppData\\Local\\Android\\Sdk'
        ANDROID_SDK_ROOT = 'C:\\Users\\ASUS\\AppData\\Local\\Android\\Sdk'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Check Environment') {
            steps {
                bat 'java -version'
                bat 'gradlew.bat --version'
            }
        }

        stage('Build Debug APK') {
            steps {
                bat 'gradlew.bat clean assembleDebug'
            }
        }

        stage('Archive APK') {
            steps {
                archiveArtifacts artifacts: 'app/build/outputs/apk/debug/*.apk',
                    fingerprint: true
            }
        }
    }
}
