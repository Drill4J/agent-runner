package com.epam.drill.agent.runner

enum class ApplicationType {
    WAR, EAR
}

open class AppAgentConfiguration : Configuration() {
    var buildVersion: String? = null
    var instanceId: String? = null
    var type: ApplicationType? = null
    override val artifactGroup: String = "com.epam.drill"
    override val artifactId: String = "drill-agent"

    override fun toJvmArgs(): String {
        val map = mutableMapOf<String, Any?>()
        map.putAll(
            mapOf(
                "drillInstallationDir" to runtimePath,
                "adminAddress" to "$adminHost:$adminPort",
                "agentId" to agentId
            )
        )
        if (buildVersion != null) map.putAll(mutableMapOf("buildVersion" to buildVersion))
        if (instanceId != null) map.putAll(mutableMapOf("instanceId" to instanceId))
        if (groupId != null) map.putAll(mutableMapOf("groupId" to groupId))
        if (type != null) map.putAll(mutableMapOf("type" to type!!.name))

        return "-agentpath:${agentPath}=" + map.map { (k, v) -> "$k=$v" }.joinToString(",")
    }

}

