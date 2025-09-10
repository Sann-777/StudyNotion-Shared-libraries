def call(Map params) {
    def services = params.SERVICE ?: "all"
    def imageTag = params.IMAGE_TAG ?: "latest"

    echo "🐳 Parallel Building + Tagging images (Docker Compose V2 safe)"
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

    // Determine which services to build
    def selectedServices = (services == "all") ? allServices : services.split(",").collect { it.trim() }

    // Map to hold parallel tasks
    def builds = [:]

    selectedServices.each { svc ->
        builds[svc] = {
            echo "⚡ Building ${svc} using Docker Compose"
            sh "docker-compose build ${svc}"

            // Get the built image name dynamically
            def imageName = sh(
                script: "docker images --format '{{.Repository}}:{{.Tag}}' | grep '${svc}' | head -n1",
                returnStdout: true
            ).trim()

            if (!imageName) {
                error "❌ Could not find built image for ${svc}"
            }

            def remoteImage = "asxhazard/studynotion-${svc}:${imageTag}"
            echo "🏷️ Tagging image ${imageName} → ${remoteImage}"
            sh "docker tag ${imageName} ${remoteImage}"

            echo "✅ Build + Tag complete for ${svc}"
        }
    }

    // Run all builds in parallel
    parallel builds
}
