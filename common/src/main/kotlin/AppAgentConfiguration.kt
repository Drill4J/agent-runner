package com.epam.drill.agent.runner

open class AppAgentConfiguration : Configuration() {
    override val artifactGroup: String = "com.epam.drill"
    override val artifactId: String = "drill-agent"

    var buildVersion: String? = null
    var instanceId: String? = null
    var webAppNames: List<String>? = null

    override fun toJvmArgs(): String {
        val map = mutableMapOf<String, Any?>()
        map.putAll(
            mapOf(
                "drillInstallationDir" to runtimePath,
                "adminAddress" to "$adminHost:$adminPort",
                "agentId" to agentId,
                "logLevel" to logLevel.name
            )
        )
        if (buildVersion != null) map.putAll(mutableMapOf("buildVersion" to buildVersion))
        if (instanceId != null) map.putAll(mutableMapOf("instanceId" to instanceId))
        if (groupId != null) map.putAll(mutableMapOf("groupId" to groupId))
        if (webAppNames != null) map.putAll(mutableMapOf("webAppNames" to webAppNames!!.joinToString(separator = ":")))

        return "-agentpath:${agentPath}=" + map.map { (k, v) -> "$k=$v" }.joinToString(",")
    }

}

