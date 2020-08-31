@file:Suppress("unused")

package com.epam.drill.autotest.gradle

import com.epam.drill.agent.runner.AgentConfiguration
import com.epam.drill.agent.runner.Configuration
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.testing.Test
import org.gradle.process.JavaForkOptions
import kotlin.reflect.KClass

open class AutoTestAgent : Agent(){
    override val extensionClass: KClass<out Configuration> = AgentConfiguration::class
    override val taskType: Set<KClass<out JavaForkOptions>> = setOf(Test::class, JavaExec::class)

}
