package com.devsecops.steps.sonar

import com.devsecops.steps.StepAbstract

class SonarScanner extends StepAbstract {
    String source = './'
    String command

    void run() {
        root.dir(this.source) {
            def scannerHome = tool 'sonarQubeScanner46'
            root.withSonarQubeEnv ('sonarqube') {
                root.sh "${this.command}"
            }
            root.timeout (time: 10, unit: 'MINUTES' ) {
                root.waitForQualityGate abortPipeline: true
            }
        }
    }
}
