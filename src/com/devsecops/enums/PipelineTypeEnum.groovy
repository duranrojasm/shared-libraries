package com.devsecops.enums

enum PipelineTypeEnum {
    //Type enum
    SYMPHONY('symphony', 'symphonyPipeline'),

    PipelineTypeEnum(String id, String func) {
        this.id = id
        this.func = func
    }
    private final String id
    private final String func

    String getId() {
        id
    }
    String getFunc() {
        func
    }

    public static boolean contains(String s)
    {
        for(PipelineTypeEnum choice:values())
            if (choice.name().equals(s))
                return true;
        return false;
    }
}