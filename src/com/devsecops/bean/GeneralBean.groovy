package com.devsecops.bean

class GeneralBean {
    
    String environment
    String type
    String gitUrl
    String branch
    String credentials
    String component
    String workspace

    String getCustomWorkspace(String workspace = null) {
        def customWorkspace = workspace ?: this.workspace
        def suffixPath      = this.getWorkspaceSuffixPath().toString()
        customWorkspace     += (suffixPath)? "/${suffixPath}" : ""

        return customWorkspace
    }

    String getWorkspaceSuffixPath() {
        def suffixPath
        if (!branch) {
            throw new Exception("GeneralBean - falta asignar la sgtes. variables: [branch]")
        }
        suffixPath = (component)? "${branch}/${component}" : branch
        return suffixPath
    }
}
