pipeline {
    agent any
    tools {
        jdk "java"
        gradle "gradle"
    }
    environment {
        AWS_STACK_AUTO = "${env.CLOUD_AWS_STACK_AUTO}"
        AWS_REGION = "${env.CLOUD_AWS_REGION}"
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
                echo "AWS_S3_BUCKET: ${AWS_S3_BUCKET}"
                    sh "java -jar -DAWS_REGION=${AWS_REGION} -DAWS_STACK_AUTO=${AWS_STACK_AUTO} ./build/libs/muckkitlist_spring-0.0.1-SNAPSHOT.jar"
                }
            }
        }
    }
}
