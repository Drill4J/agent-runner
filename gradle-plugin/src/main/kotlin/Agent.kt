@file:Suppress("unused")

package com.epam.drill.autotest.gradle

import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories
import org.gradle.process.JavaForkOptions
import org.jetbrains.kotlin.konan.target.Family
import java.io.File
import kotlin.reflect.KClass

private const val EXTENSION_NAME = "drill"

abstract class Agent : Plugin<Project> {

    abstract val extensionClass: KClass<out Configuration>
    abstract val taskType: KClass<out JavaForkOptions>
    abstract val artifactPath: String

    private fun TaskContainer.configure() {

        filterIsInstance(taskType.java).forEach {
            with(it) {
                (it as Task).doFirst {
                    with(project) {
                        prepare()
                        jvmArgs(config.toJvmArgs())
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
                agent("$artifactPath-${presetName}:${config.version}@zip")
            }
            with(tasks) {
                configure()
            }
        }
        Unit
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
            val dynamicLibExtensions = Family.values().map { it.dynamicSuffix }.distinct()

            config.runtimePath = extractedDir
            config.agentPath = extractedDir.listFiles()?.first { file ->
                dynamicLibExtensions.any { it == file.extension }
            } ?: throw GradleException("can't find agent")
        }
    }
}

private val Project.config: Configuration
    get() = extensions.findByName(EXTENSION_NAME) as Configuration

abstract class Configuration {
    lateinit var adminHost: String
    lateinit var agentId: String
    var adminPort: Int = 8080
    var version: String = "+"
    var agentPath: File? = null
    var runtimePath: File? = null
    abstract fun toJvmArgs(): String
}


val presetName: String =
    when {
        Os.isFamily(Os.FAMILY_MAC) -> "macosX64"
        Os.isFamily(Os.FAMILY_UNIX) -> "linuxX64"
        Os.isFamily(Os.FAMILY_WINDOWS) -> "mingwX64"
        else -> throw RuntimeException("Target ${System.getProperty("os.name")} is not supported")
    }

