def call(Map params) {
    def services = params.SERVICE ?: "all"
    def imageTag = params.IMAGE_TAG ?: "latest"

    echo "🐳 Docker Compose Build & Push"
    echo "Selected SERVICE(s): ${services}"
    echo "Using IMAGE_TAG: ${imageTag}"

    // All available services (must match docker-compose.yml)
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

    def buildAndPush = { service ->
        def image = "asxhazard/studynotion-${service}:${imageTag}"

        echo "⚡ Building & Pushing ${service} → ${image}"

        // Build using docker-compose
        sh """
          docker-compose build ${service}
          docker tag studynotion-${service}:latest ${image}
          echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
          docker push ${image}
        """

        echo "✅ Done ${service} → ${image}"
    }

    if (services == "all") {
        allServices.each { svc -> buildAndPush(svc) }
    } else {
        services.split(",").each { svc -> buildAndPush(svc.trim()) }
    }
}
