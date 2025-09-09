def call(Map params) {
    def services = params.SERVICE ?: "all"
    def imageTag = params.IMAGE_TAG ?: "latest"

    echo "🚀 StudyNotion Image Tag Update Script"
    echo "Selected SERVICE(s): ${services}"
    echo "New IMAGE_TAG: ${imageTag}"

    // All available services
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

    // Helper closure to update yaml file
    def updateImage = { service ->
        def yamlFile = "kubernetes/${service}.yaml"
        def image = "asxhazard/studynotion-${service}:${imageTag}"

        if (fileExists(yamlFile)) {
            sh """
              sed -i -e "s|asxhazard/studynotion-${service}:.*|${image}|g" ${yamlFile}
            """
            echo "✅ Updated ${service} → ${image}"
        } else {
            echo "⚠️  Skipped ${service} (no ${yamlFile} found)"
        }
    }

    if (services == "all") {
        allServices.each { svc -> updateImage(svc) }
    } else {
        def selected = services.split(",")
        selected.each { svc -> updateImage(svc.trim()) }
    }
}
