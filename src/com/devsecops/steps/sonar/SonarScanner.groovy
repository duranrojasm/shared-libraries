package com.devsecops.steps

class SonarScanner extends StepAbstract {
    String source = './'
    String command

    void run() {
        root.dir(this.source) {
            root.withSonarQubeEnv ('sonarqube') {
                root.sh "${this.command}"
            }
            root.timeout (time: 10, unit: 'MINUTES' ) {
                root.waitForQualityGate abortPipeline: true
            }
        }
    }
}
