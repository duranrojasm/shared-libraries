package com.devsecops.steps.sonar

import com.devsecops.steps.StepAbstract

class SonarScanner extends StepAbstract {
    String source = './'
    String command

    void run() {
        root.dir(this.source) {
            def scannerHome = root.tool 'sonarQubeScanner46'
            root.withSonarQubeEnv ('sonarqube') {
                root.sh "${scannerHome}/bin/sonar-scanner ${this.command}"
                //root.sh "${this.command}"
            }
            root.timeout (time: 10, unit: 'MINUTES' ) {
                root.waitForQualityGate abortPipeline: true
            }
        }
    }
}
