package com.devsecops.enums

enum StageEnum {
    // STAGE(id, func)
    CHECKOUT('checkout', 'Checkout')

    final String id
    final String name
    static final Map map

    static {
        map = [:] as TreeMap
        values().each{ stage ->
            map.put(stage.id, stage)
        }
    }

    private StageEnum(String id, String name) {
        this.id = id;
        this.name = name;
    }

    static getStageEnum( id ) {
        map[id]
    }
}
