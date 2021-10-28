package com.devsecops.enums

import com.devsecops.steps.*
import com.devsecops.steps.sonar.SonarScanner


/**
 * [STAGE]_[TYPE]
 */
enum StepTypeEnum {
    COMMAND(Command.class), // Por defecto cuando no encuentra [TYPE]
    //SONAR_CODE
    /** SCAN **/
    SCAN_SONAR(SonarScanner.class)

    StepTypeEnum(Class value) {
        this.value = value
    }
    private final Class value
    Class getValue() {
        value
    }

    public static boolean contains(String s)
    {
        for(StepTypeEnum choice:values())
            if (choice.name().equals(s))
                return true;
        return false;
    }
}
