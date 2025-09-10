def call(Map params) {
    def services = params.SERVICE ?: "all"
    def imageTag = params.IMAGE_TAG ?: "latest"

    echo "🐳 Building + Tagging images (Docker Compose V2 safe)"
    echo "Selected SERVICE(s): ${services}"
    echo "New IMAGE_TAG: ${imageTag}"

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

    def buildAndTag = { service ->
        echo "⚡ Building ${service} using Docker Compose"
        sh "docker-compose build ${service}"

        // Get the built image name dynamically (works with Compose V2)
        def imageName = sh(
            script: "docker images --format '{{.Repository}}:{{.Tag}}' | grep '${service}' | head -n1",
            returnStdout: true
        ).trim()

        if (!imageName) {
            error "❌ Could not find built image for ${service}"
        }

        def remoteImage = "asxhazard/studynotion-${service}:${imageTag}"
        echo "🏷️ Tagging image ${imageName} → ${remoteImage}"
        sh "docker tag ${imageName} ${remoteImage}"

        echo "✅ Build + Tag complete for ${service}"
    }

    if (services == "all") {
        allServices.each { svc -> buildAndTag(svc) }
    } else {
        services.split(",").each { svc -> buildAndTag(svc.trim()) }
    }
}
