package com.epam.drill.agent.runner

open class AppAgentConfiguration : Configuration() {
    var buildVersion: String? = null
    var instanceId: String? = null
    var groupId: String? = null
    var type: String? = null
    override val artifactGroup: String = "com.epam.drill"
    override val artifactId: String = "drill-agent"

    override fun toJvmArgs(): String {
        return "-agentpath:${agentPath}=" + (
                mapOf(
                    "drillInstallationDir" to runtimePath,
                    "adminAddress" to "$adminHost:$adminPort",
                    "agentId" to agentId
                ) + if (buildVersion != null) mutableMapOf(
                    "buildVersion" to buildVersion
                ) else mutableMapOf<String, Any>() + if (instanceId != null) mutableMapOf(
                    "instanceId" to instanceId
                ) else mutableMapOf<String, Any>() + if (groupId != null) mutableMapOf(
                    "groupId" to groupId
                ) else mutableMapOf<String, Any>()
                ).map { (k, v) ->
            "$k=$v"
        }.joinToString(",")
    }

}

