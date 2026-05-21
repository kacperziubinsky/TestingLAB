pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'chmod +x mvnw'
                sh './mvnw -B clean compile'
            }
        }
        stage('Test') {
            steps {
                sh './mvnw -B test'
            }
        }
    }
}
