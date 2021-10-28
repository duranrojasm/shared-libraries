package com.devsecops.common

import com.devsecops.enums.NodeLabelEnum
import com.devsecops.enums.PipelineTypeEnum
import com.devsecops.enums.StageEnum

class PipelineNodeLabel {
    static Map<PipelineTypeEnum, NodeLabel> items = [:]

    static void init() {
        if (items) {
            return
        }
        items.put(PipelineTypeEnum.DEFAULT,
            new NodeLabel( label: NodeLabelEnum.MASTER.value)
        )
        items.put(PipelineTypeEnum.SYMPHONY,
            new NodeLabel( label: NodeLabelEnum.MASTER.value)
        )
    }

    static NodeLabel getItem(PipelineTypeEnum type) {
        init()
        return items.get(type, null)
    }
}
