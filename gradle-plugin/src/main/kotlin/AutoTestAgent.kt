@file:Suppress("unused")

package com.epam.drill.autotest.gradle

import org.gradle.api.tasks.testing.Test
import org.gradle.process.JavaForkOptions
import kotlin.reflect.KClass

open class AutoTestAgent : Agent(){
    override val extensionClass: KClass<out Configuration> = AgentConfiguration::class
    override val taskType: KClass<out JavaForkOptions> = Test::class
    override val artifactPath: String="com.epam.drill.autotest:autoTestAgent"
}


open class AgentConfiguration : Configuration() {
    var pluginId: String = "test2code"
    var groupId: String? = null
    var trace: Boolean = false
    var debug: Boolean = false
    var info: Boolean = false
    var warn: Boolean = false
    var plugins: Set<String> = mutableSetOf()

    override fun toJvmArgs(): String {
        return "-agentpath:${agentPath}=" +
                "runtimePath=${runtimePath}," +
                "adminHost=$adminHost," +
                "adminPort=$adminPort," +
                "agentId=$agentId," +
                "pluginId=$pluginId," +
                if (groupId != null) "serviceGroupId=$groupId," else "" +
                "trace=$trace`," +
                "debug=$debug," +
                "info=$info," +
                "warn=$warn," +
                //plugins: junit, jmeter, testng. usage: [ plugins=junit;jmeter ]
                //by default all 3 plugins are active
                "plugins=${plugins.joinToString(separator = ";")}"
    }

}
