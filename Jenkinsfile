pipeline {
    agent any

    tools {
        git 'Git'
        jdk 'JDK17'
        maven 'Maven3.9'
    }

    environment {
        JAVA_HOME = tool name: 'JDK17', type: 'hudson.model.JDK'
        PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/ichwansh03/spring-kafka-mongodb.git'
            }
        }

        stage('Validate') {
            steps {
                script {
                 sh 'ls -la'
                }
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Archive Artifact') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }

    post {
        success {
            echo '✅ Build Success! Spring Boot JAR generated in target folder.'
        }
        failure {
            echo '❌ Build Failed. Please check logs.'
        }
    }
}
