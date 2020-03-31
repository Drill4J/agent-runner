@file:Suppress("unused")

package com.epam.drill.autotest.gradle

import com.epam.drill.agent.runner.AppAgentConfiguration
import com.epam.drill.agent.runner.Configuration
import org.gradle.api.tasks.JavaExec
import org.gradle.process.JavaForkOptions
import kotlin.reflect.KClass

class AppAgent : Agent() {
    override val extensionClass: KClass<out Configuration> = AppAgentConfiguration::class
    override val taskType: KClass<out JavaForkOptions> = JavaExec::class
}

