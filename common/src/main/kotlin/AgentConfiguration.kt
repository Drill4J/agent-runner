package com.epam.drill.agent.runner

open class AgentConfiguration : Configuration() {
    var pluginId: String = "test2code"
    var plugins: Set<String> = mutableSetOf()
    override val artifactGroup: String = "com.epam.drill.autotest"
    override val artifactId: String = "autoTestAgent"

    override fun jvmArgs(): Map<String, String> {
        return mapOf(
            "pluginId" to pluginId,
            "plugins" to plugins.joinToString(separator = ";")
        )
    }

}
