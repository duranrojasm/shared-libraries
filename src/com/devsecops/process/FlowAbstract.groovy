package com.devsecops.process

import com.devsecops.bean.GeneralBean
import com.devsecops.common.Constants
import com.devsecops.enums.StageEnum
import com.devsecops.enums.StepTypeEnum
import com.devsecops.steps.StepAbstract

abstract class FlowAbstract {
    protected root
    protected Map<Class, StepAbstract> preloadStep = [:]
    protected LinkedHashMap props
    protected LinkedHashMap stages
    public GeneralBean general
    String[] stageFlow

    protected abstract void prepare()

    void init() {
        if (!this.props) {
            this.loadProperties()
            this.prepare()
        }
    }

    FlowAbstract downloadSources() {
        root.dir(general.getCustomWorkspace()) {
            root.utils.downloadSources(
                this.general.getBranch(),
                this.general.getGitUrl(),
                this.general.getCredentials()
            )
        }
        return this
    }

    Boolean hasStage(StageEnum stage) {
        this.init()
        return this.stageFlow.contains(stage.id);
    }

    protected addPreloadStep(StepAbstract stepInstance) {
        this.preloadStep.put(stepInstance.class, stepInstance)
    }

    void runStage(StageEnum stage, String workspace = null) {

        workspace = workspace ?: root.env.WORKSPACE
        println("El Workspace: $workspace")
        List<StepAbstract> steps = this.loadSteps(stage)
        // Exec steps, verify is parallel steps.
        def builders = [:]
        def propsEnv = this.props[this.general.getEnvironment()]
        String[] parallels = (propsEnv.parallel)? propsEnv.parallel.toString().split(","):[]

        root.dir(general.getCustomWorkspace(workspace)) {
            steps.eachWithIndex { item, i ->
                if (parallels.contains(stage.id)) {
                    builders["step${(i + 1)}"] = {
                        item.run()
                    }
                } else {
                    item.run()
                }
            }
            if (builders) {
                root.parallel builders
            }
        }
    }

    protected List<StepAbstract> loadSteps(StageEnum stageEnum) {
        ArrayList<LinkedHashMap> items = []
        def stage = this.stages[stageEnum.id]
        def stageItems = stage
        def stageDefault = [:]

        // Si tiene varios steps con seteo de default
        if (stage instanceof LinkedHashMap && stage.containsKey("default")) {
            stageDefault    = stage.get("default")
            stageItems      = stage.get("steps")
        }

        // Si tiene un solo step
        if (stageItems instanceof LinkedHashMap) {
            items.add(stageItems as LinkedHashMap)
        } // Si tiene varios steps
        else {
            items = stageItems as ArrayList
        }

        Closure asObject = { Map map ->
            def objectInstance
            def stepAttr = stageDefault + map
            def stepTypeEnumKey = this.getStepTypeEnum(stageEnum, stepAttr.type as String)

            if (this.preloadStep.containsKey(stepTypeEnumKey.value)) {
                objectInstance = this.preloadStep.get(stepTypeEnumKey.value)
            } else {
                objectInstance = stepTypeEnumKey.value.newInstance()
            }

            // First attributes
            objectInstance.root = this.root
            stepAttr.each { key, value ->
                if (objectInstance.hasProperty(key as String)) {
                    objectInstance[key as String] = value
                }
            }
            return objectInstance
        }
        return items.collect(asObject) as List<StepAbstract>
    }

    protected StepTypeEnum getStepTypeEnum(StageEnum stage, String type = '') {
        StepTypeEnum result     = StepTypeEnum.COMMAND
        String stepTypeEnumKey  = (stage.toString() + "_" + type.toString()).toUpperCase()

        if (type && !StepTypeEnum.contains(stepTypeEnumKey)) {
            throw new Exception("Error: Dont exist type [${type.toString()}] on stage [${stage.id}], see [devops.properties]")
        }

        if (StepTypeEnum.contains(stepTypeEnumKey)) {
            result = StepTypeEnum[stepTypeEnumKey] as StepTypeEnum
        }
        return result
    }

    protected void loadProperties() {
        this.props = root.mapper.propsToObject(
            general.getCustomWorkspace() + "/" + Constants.PROJECT_CONFIG_PATH
        )
        def propsEnv = this.props[this.general.getEnvironment()]
        this.stages = propsEnv['stages'] as LinkedHashMap
        this.stageFlow = propsEnv['flow'].toString().split(",")
    }
}
