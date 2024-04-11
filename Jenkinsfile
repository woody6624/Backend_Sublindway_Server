pipeline {
    agent any
    tools {
        jdk "java"
        gradle "gradle"
    }
  environment {
        AWS_S3_BUCKET = "${env.CLOUD_AWS_S3_BUCKET}"
        AWS_STACK_AUTO = "${env.CLOUD_AWS_STACK_AUTO}"
        AWS_REGION = "${env.CLOUD_AWS_REGION}"
        AWS_ACCESS_KEY = "${env.CLOUD_AWS_ACCESS_KEY}"
        AWS_SECRET_KEY = "${env.CLOUD_AWS_SECRET_KEY}" // AWS_SECRET_KEY에 대한 값을 설정해야 합니다.
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        // 자바 스프링 관련 단계 추가
        // 윈도우에서는 'sh'를 'bat'으로 변경
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
                    sh "java -jar -DAWS_SECRET_KEY=${AWS_ACCESS_KEY} -DAWS_SECRET_KEY=${AWS_SECRET_KEY} -DAWS_REGION=${AWS_REGION} -DAWS_STACK_AUTO=${AWS_STACK_AUTO} -DAWS.S3_BUCKET=${AWS_S3_BUCKET} ./build/libs/MainServer-0.0.1-SNAPSHOT.jar"
                }
            }
        }
    }
}
