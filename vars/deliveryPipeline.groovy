#!/usr/bin/env groovy
import com.devsecops.bean.GeneralBean
import com.devsecops.common.NodeLabel
import com.devsecops.common.PipelineNodeLabel
import com.devsecops.enums.PipelineTypeEnum

def call(body) {
    
    // evaluate the body block, and collect configuration into the object
    def pipelineParams = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = pipelineParams
    body()

    def generalBean = new GeneralBean(
        type: pipelineParams.type,
        gitUrl: pipelineParams.gitUrl,
        branch: pipelineParams.branch,
        credentials: pipelineParams.credentials
    )

    PipelineTypeEnum pipelineType = generalBean.getType() as PipelineTypeEnum
    NodeLabel nodeLabel = PipelineNodeLabel.getItem(pipelineType)
    "${pipelineType.func}"(generalBean, nodeLabel)
}
