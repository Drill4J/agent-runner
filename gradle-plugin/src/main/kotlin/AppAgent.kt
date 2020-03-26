@file:Suppress("unused")

package com.epam.drill.autotest.gradle

import org.gradle.api.tasks.JavaExec
import org.gradle.process.JavaForkOptions
import kotlin.reflect.KClass

class AppAgent : Agent() {
    override val extensionClass: KClass<out Configuration> = AppAgentConfiguration::class
    override val taskType: KClass<out JavaForkOptions> = JavaExec::class
    override val artifactPath: String="com.epam.drill:drill-agent"
}


open class AppAgentConfiguration : Configuration() {
    var buildVersion: String? = null
    var instanceId: String? = null
    var groupId: String? = null
    var type: String? = null

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

