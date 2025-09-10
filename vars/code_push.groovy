def call(String url, String branch){
    withCredentials([usernamePassword(credentialsId: 'Github', usernameVariable: 'GIT_USER',passwordVariable: 'GIT_PASS')]) {
        sh'''
          git config user.name "$GIT_USER"
          git config user.email "jenkins@example.com"
          git remote set-url origin ${url}
          git add .
          git commit -m "Automated commit from Jenkins"
          git push origin ${branch}
        '''
    }
}
