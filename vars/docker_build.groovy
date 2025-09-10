def call(Map params) {
    def services = params.SERVICE ?: "all"
    def imageTag = params.IMAGE_TAG ?: "latest"

    echo "🐳 StudyNotion Docker Image Tagging Script"
    echo "Selected SERVICE(s): ${services}"
    echo "Image TAG: ${imageTag}"

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

    def tagImage = { service ->
        def localImage = "${service}:build"
        def remoteImage = "asxhazard/studynotion-${service}:${imageTag}"

        sh """
          echo "Tagging ${localImage} → ${remoteImage}"
          docker tag ${localImage} ${remoteImage}
        """
        echo "✅ Tagged ${service} → ${remoteImage}"
    }

    if (services == "all") {
        allServices.each { svc -> tagImage(svc) }
    } else {
        def selected = services.split(",")
        selected.each { svc -> tagImage(svc.trim()) }
    }
}
