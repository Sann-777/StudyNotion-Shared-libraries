def call(Map params) {
    def services = params.SERVICE ?: "all"
    def imageTag = params.IMAGE_TAG ?: "latest"

    echo "🐳 Building + Tagging images"
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
        def localImage = "studynotion-${service}:latest"
        def remoteImage = "asxhazard/studynotion-${service}:${imageTag}"

        echo "⚡ Building ${service}"
        sh "docker-compose build ${service}"

        echo "🏷️ Tagging ${localImage} → ${remoteImage}"
        sh "docker tag ${localImage} ${remoteImage}"

        echo "✅ Build + Tag complete for ${service}"
    }

    if (services == "all") {
        allServices.each { svc -> buildAndTag(svc) }
    } else {
        services.split(",").each { svc -> buildAndTag(svc.trim()) }
    }
}
