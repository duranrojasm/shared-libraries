package com.devsecops.steps

class Command extends StepAbstract {
    String source = './'
    String command
    String commandParams

    void run() {
        this.command += (this.commandParams)? " " + this.commandParams : ""
        root.dir(this.source) {
            root.sh "${this.command}"
        }
    }
}