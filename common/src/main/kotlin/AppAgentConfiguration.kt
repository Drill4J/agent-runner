package com.epam.drill.agent.runner

open class AppAgentConfiguration : Configuration() {
    override val artifactGroup: String = "com.epam.drill"
    override val artifactId: String = "drill-agent"

    var buildVersion: String? = null
    var instanceId: String? = null
    var webAppNames: List<String>? = null

    override fun jvmArgs(): Map<String, String> {
        return mutableMapOf<String, String>().apply {
            if (buildVersion != null) this["buildVersion"] = buildVersion!!
            if (instanceId != null) this["instanceId"] = instanceId!!
            if (webAppNames != null) this["webAppNames"] = webAppNames!!.joinToString(separator = ":")
        }
    }

}

