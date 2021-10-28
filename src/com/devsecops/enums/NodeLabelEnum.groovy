package com.devsecops.enums

enum NodeLabelEnum {
    MASTER('master')
    //Slaves...


    NodeLabelEnum(String value) {
        this.value = value
    }
    private final String value
    String getValue() {
        value
    }
}