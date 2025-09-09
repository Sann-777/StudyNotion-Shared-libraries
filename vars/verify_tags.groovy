def call(){
  echo "FRONTEND_IMAGE_TAG: ${params.IMAGE_TAG}"
  echo "API_GATEWAY_IMAGE_TAG: ${params.IMAGE_TAG}"
  echo "AUTH_TAG: ${params.IMAGE_TAG}"
  echo "COURSE_TAG: ${params.IMAGE_TAG}"
  echo "PROFILE_TAG: ${params.IMAGE_TAG}"
  echo "RATING_TAG: ${params.IMAGE_TAG}"
  echo "MEDIA_TAG: ${params.IMAGE_TAG}"
  echo "NOTIFICATION_TAG: ${params.IMAGE_TAG}"
}
