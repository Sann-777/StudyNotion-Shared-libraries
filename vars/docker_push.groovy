def call(String ProjectName, String ImageTag, String dockerhubuser){
  echo "Pushing the Image to DockerHub"
  
  withCredentials([usernamePassword(credentialsId:'dockerHubCred', passwordVariable:'dockerHubPass', usernameVariable:'dockerHubUser')]){
    sh "docker login -u ${dockerHubUser} -p ${dockerHubPass}"
  }
  
  sh "docker push ${dockerhubuser}/${ProjectName}:${ImageTag}"
  
  echo "Image pushed to DockerHub"
}
