#!/usr/bin/env groovy
import com.devsecops.bean.GeneralBean
import com.devsecops.common.NodeLabel
import com.devsecops.enums.StageEnum
import com.devsecops.process.FlowAbstract
import com.devsecops.process.PipelineFlow

def call(GeneralBean generalBean, NodeLabel nodeLabel) {

    FlowAbstract pipelineFlow

    pipeline {
        agent {
            node {
                label nodeLabel.getValue()
            }
        }
        parameters {
            choice(name: 'ENV', choices: ['dev', 'cert', 'prod'], description: 'Environment')
        }
        environment {
            ENV = "${params.ENV}"
        }
        stages {
            stage('Checkout') {
                steps {
                    script {
                        generalBean.setWorkspace(env.WORKSPACE)
                        generalBean.setEnvironment(ENV)
                        pipelineFlow = new PipelineFlow(root: this, general: generalBean)
                        pipelineFlow.downloadSources().init()
                    }
                }
            }
            stage('Scan') {
                when { expression { return pipelineFlow.hasStage(StageEnum.SCAN) } }
                steps { script { pipelineFlow.runStage(StageEnum.SCAN) } }
            }
        }
    }
}