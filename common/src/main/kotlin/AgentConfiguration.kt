package com.epam.drill.agent.runner

open class AgentConfiguration : Configuration() {
    var pluginId: String = "test2code"
    var groupId: String? = null
    var trace: Boolean = false
    var debug: Boolean = false
    var info: Boolean = false
    var warn: Boolean = false
    var plugins: Set<String> = mutableSetOf()
    override val artifactGroup: String = "com.epam.drill.autotest"
    override val artifactId: String = "autoTestAgent"

    override fun toJvmArgs(): String {
        val serviceGroupId = if (groupId != null) "serviceGroup=$groupId," else ""
        val agentId = if (agentId != null) "agentId=$agentId," else ""
        return "-agentpath:${agentPath}=" +
                "runtimePath=${runtimePath}," +
                "adminHost=$adminHost," +
                "adminPort=$adminPort," +
                agentId +
                "pluginId=$pluginId," +
                serviceGroupId +
                "trace=$trace," +
                "debug=$debug," +
                "info=$info," +
                "warn=$warn," +
                //plugins: junit, jmeter, testng. usage: [ plugins=junit;jmeter ]
                //by default all 3 plugins are active
                "plugins=${plugins.joinToString(separator = ";")}"
    }

}
