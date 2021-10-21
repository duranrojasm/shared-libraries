package com.devsecops.common

import com.devsecops.enums.StageEnum

class NodeLabel {
    String label
    Map<StageEnum, String> stages = [:]

    NodeLabel addStageLabel(StageEnum stage, String label) {
        stages.put(stage, label)
        return this
    }

    String getValue() {
        return label
    }

    String getValue(StageEnum stage) {
        return stages.get(stage, null)
    }
}
