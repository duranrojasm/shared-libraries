#!/usr/bin/env groovy

def downloadSources(String branch, String url, String credential) {
    public static final String GIT_COMMIT
    root.echo branch+" "+url+" "+credential
    checkout([
        $class: "GitSCM",
        branches: [[name: "*/${branch}"]],
        userRemoteConfigs: [[
            credentialsId: credential,
            url: url
        ]]
    ])
}