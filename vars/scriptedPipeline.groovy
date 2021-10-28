#!/usr/bin/env groovy
import com.devsecops.bean.GeneralBean
import com.devsecops.common.NodeLabel
import com.devsecops.enums.StageEnum
import com.devsecops.process.FlowAbstract
import com.devsecops.process.PipelineFlow

def call(GeneralBean generalBean, NodeLabel nodeLabel) {
    String defaultBranch = generalBean.getBranch()
    FlowAbstract pipelineFlow

    node(nodeLabel.getValue()){

        /** PARAMETERS **/
        properties([
            parameters([
                choice(name: 'ENV', choices: ['dev', 'cer', 'prd'], description: 'Environment')
            ])
        ])

        /** FIRST STAGE **/
        stage("Checkout") {
            generalBean.setWorkspace(env.WORKSPACE)
            generalBean.setBranch(params.BRANCH)
            generalBean.setEnvironment(params.ENV)
            pipelineFlow = new PipelineFlow(root: this, general: generalBean)
            pipelineFlow.downloadSources().init()
        }

        /** STAGES **/
        def mystages = pipelineFlow.getStageFlow()

        mystages.each { stageKey ->
            StageEnum stageEnum = StageEnum.getStageEnum(stageKey)

            if (pipelineFlow.hasStage(stageEnum)) {

                if (nodeLabel.getValue(stageEnum)) {
                    node(nodeLabel.getValue(stageEnum)) {
                        stage(stageEnum.getName()) {
                            pipelineFlow.runStage(stageEnum)
                        }
                    }
                } else {
                    stage(stageEnum.getName()) {
                        pipelineFlow.runStage(stageEnum)
                    }
                }

            }
        }
    }
}