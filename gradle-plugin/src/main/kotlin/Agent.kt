/**
 * Copyright 2020 EPAM Systems
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:Suppress("unused")

package com.epam.drill.autotest.gradle

import com.epam.drill.agent.runner.Configuration
import com.epam.drill.agent.runner.dynamicLibExtensions
import com.epam.drill.agent.runner.presetName
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories
import org.gradle.process.JavaForkOptions
import kotlin.reflect.KClass

private const val EXTENSION_NAME = "drill"

abstract class Agent : Plugin<Project> {

    abstract val extensionClass: KClass<out Configuration>
    abstract val taskType: Set<KClass<out JavaForkOptions>>

    private fun TaskContainer.configure() {
        filter { task -> taskType.any { it.java.isInstance(task) } }.map { it as JavaForkOptions }.forEach {
            println("Task ${(it as Task).name} is modified by Drill")
            with(it) {
                (it as Task).doFirst {
                    with(project) {
                        prepare()
                        val toJvmArgs: List<String> = config.toJvmArgs()
                        println("Drill agent line: $toJvmArgs")
                        it.setJvmArgs(toJvmArgs.asIterable())
                    }
                }
            }
        }
    }

    override fun apply(target: Project) = target.run {
        extensions.create(EXTENSION_NAME, extensionClass.java)
        val agent = configurations.create("agent")
        repositories {
            maven(url = "https://oss.jfrog.org/artifactory/list/oss-release-local")
        }

        afterEvaluate {
            dependencies {
                agent("${config.artifactGroup}:${config.artifactId}-${presetName}:${config.version}@zip")
            }
            with(tasks) {
                configure()
            }
        }
    }

    private fun Project.prepare() {
        val agent = configurations.getAt("agent")
        val resolvedVersion = agent.resolvedConfiguration.firstLevelModuleDependencies.filter {
            it.moduleName == agent.allDependencies.first().name
        }[0].moduleVersion

        if (config.agentPath == null && config.runtimePath == null) {
            val drillDist = rootProject.buildDir.resolve("drill")
            val extractedDir = drillDist.resolve(presetName + "-${resolvedVersion}")
            if (!extractedDir.exists() || extractedDir.listFiles()!!.isEmpty())
                copy {
                    from(zipTree(agent.resolve().first()))
                    into(drillDist)
                }



            config.runtimePath = extractedDir
            config.agentPath = extractedDir.listFiles()?.first { file ->
                dynamicLibExtensions.any { it == file.extension }
            } ?: throw GradleException("can't find agent")
        }
    }
}

private val Project.config: Configuration
    get() = extensions.findByName(EXTENSION_NAME) as Configuration

