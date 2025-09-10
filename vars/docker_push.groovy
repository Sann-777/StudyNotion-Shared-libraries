def call(Map params) {
    def services = params.SERVICE ?: "all"
    def imageTag = params.IMAGE_TAG ?: "latest"

    echo "📤 Pushing images to Docker Hub"
    echo "Selected SERVICE(s): ${services}"
    echo "Using IMAGE_TAG: ${imageTag}"

    def allServices = [
        "api-gateway",
        "auth-service",
        "course-service",
        "profile-service",
        "rating-service",
        "media-service",
        "notification-service",
        "frontend"
    ]

    withCredentials([usernamePassword(credentialsId: 'Docker',
                                      usernameVariable: 'DOCKER_USERNAME',
                                      passwordVariable: 'DOCKER_PASSWORD')]) {
        sh """
          echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
        """

        def pushService = { service ->
            def remoteImage = "asxhazard/studynotion-${service}:${imageTag}"
            sh "docker push ${remoteImage}"
            echo "✅ Pushed ${remoteImage}"
        }

        if (services == "all") {
            allServices.each { svc -> pushService(svc) }
        } else {
            services.split(",").each { svc -> pushService(svc.trim()) }
        }
    }
}
