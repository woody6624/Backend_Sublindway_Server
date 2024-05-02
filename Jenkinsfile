pipeline {
    agent any
    tools {
        jdk "java"
        gradle "gradle"
    }
    environment {
        AWS_S3_BUCKET = "${env.AWS_S3_BUCKET}"
        AWS_STACK_AUTO = "${env.AWS_STACK_AUTO}"
        AWS_REGION = "${env.AWS_REGION}"
        AWS_ACCESS_KEY = "${env.AWS_ACCESS_KEY}"
        AWS_SECRET_KEY = "${env.AWS_SECRET_KEY}"
        OCR_SECERET_KEY = "${env.OCR_SECERET_KEY}"
        OCR_API = "${env.OCR_API}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Install Dependencies') {
            steps {
                script {
                    sh 'chmod +x ./gradlew'
                    sh './gradlew build'
                }
            }
        }
        stage('Build And Deploy') {
            steps {
                script {
                    sh """
                    java -jar \
                    -DAWS_ACCESS_KEY=${AWS_ACCESS_KEY} \
                    -DAWS_SECRET_KEY=${AWS_SECRET_KEY} \
                    -DAWS_REGION=${AWS_REGION} \
                    -DAWS_STACK_AUTO=${AWS_STACK_AUTO} \
                    -DAWS_S3_BUCKET=${AWS_S3_BUCKET} \
                    -DOCR_SECRET_KEY=${OCR_SECERET_KEY} \
                    -DOCR_API=${OCR_API} \
                    ./build/libs/MainServer-0.0.1-SNAPSHOT.jar
                    """
                }
            }
        }
    }
}
