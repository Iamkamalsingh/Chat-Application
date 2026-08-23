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
                script {
                    try {
                        withCredentials([file(credentialsId: 'google-services-json',
                                              variable: 'GOOGLE_SERVICES')]) {
                            bat 'copy /Y "%GOOGLE_SERVICES%" "Loop\\app\\google-services.json"'
                            echo 'google-services.json injected successfully.'
                        }
                    } catch (Exception e) {
                        error("""
====================================================================
  MISSING CREDENTIAL: google-services-json
====================================================================
  The Firebase config file was not found in Jenkins credentials.

  To fix this:
  1. Go to: Manage Jenkins -> Credentials -> Global -> Add Credentials
  2. Kind      : Secret file
  3. ID        : google-services-json        (must match exactly)
  4. File      : Upload your google-services.json from Firebase Console
  5. Click Save, then re-run this build.
====================================================================
                        """)
                    }
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
            echo 'BUILD SUCCESSFUL - APK is archived as a build artifact.'
        }
        failure {
            echo 'BUILD FAILED - Check console output above for details.'
        }
        always {
            cleanWs()
        }
    }
}
